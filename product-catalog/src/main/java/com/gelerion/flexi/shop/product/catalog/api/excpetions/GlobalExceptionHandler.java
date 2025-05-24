package com.gelerion.flexi.shop.product.catalog.api.excpetions;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.IntegrityConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * Turns every exception thrown by the API layer into an RFC 7807 JSON object.
 * <p>
 * Spring Boot 3.1+ will automatically serialize ProblemDetail instances; no additional
 * configuration is required as long as `spring.mvc.problem-details.enabled=true`
 * (the default since 3.2).
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /* ───────────────────────────────────────────────────────────────
       Validation: @Valid on controller method arguments
       ─────────────────────────────────────────────────────────────── */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse onInvalidArgument(MethodArgumentNotValidException ex,
                                           HttpServletRequest req) {
        logError(ex, HttpStatus.BAD_REQUEST);
        // Spring has already built a ProblemDetail with an "errors" property, keep it:
        var problemDetails = ex.getBody();
        problemDetails.setProperty("timestamp", OffsetDateTime.now());
        problemDetails.setProperty("correlationId", correlationId(req));
        return ex;
    }

    /* ───────────────────────────────────────────────────────────────
       Validation: javax.validation used manually in the service tier
       ─────────────────────────────────────────────────────────────── */
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse onConstraintViolation(ConstraintViolationException ex,
                                               HttpServletRequest req) {

        return errorBuilder(ex, HttpStatus.BAD_REQUEST, req)
                .property("errors", violationMap(ex))
                .build();
    }

    /* ───────────────────────────────────────────────────────────────
       SQL integrity errors (duplicate key, FK violation …)
       ─────────────────────────────────────────────────────────────── */
    @ExceptionHandler(IntegrityConstraintViolationException.class)
    public ErrorResponse onIntegrityViolation(IntegrityConstraintViolationException ex,
                                              HttpServletRequest req) {
        return errorBuilder(ex, HttpStatus.CONFLICT, req).build();
    }

    /* ───────────────────────────────────────────────────────────────
       Any other jOOQ / JDBC problems
       ─────────────────────────────────────────────────────────────── */
    @ExceptionHandler(DataAccessException.class)
    public ErrorResponse onDataAccess(DataAccessException ex, HttpServletRequest req) {
        return errorBuilder(ex, HttpStatus.BAD_REQUEST, req).build();
    }

    /* ───────────────────────────────────────────────────────────────
       Catch-all — unexpected errors
       ─────────────────────────────────────────────────────────────── */
    @ExceptionHandler(Throwable.class)
    public ErrorResponse onAny(Throwable ex, HttpServletRequest req) {
        return errorBuilder(ex, HttpStatus.INTERNAL_SERVER_ERROR, req).build();
    }

    /* ───────────────────────────────────────────────────────────────
       Jackson parsing errors
       ─────────────────────────────────────────────────────────────── */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse onHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                  HttpServletRequest req) {
        // If the root cause is an InvalidFormatException, we can pull out field/path and bad value
        if (ex.getCause() instanceof InvalidFormatException ife) {
            String fieldPath = ife.getPath()
                    .stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .collect(Collectors.joining("."));

            String detail = String.format(
                    "Property '%s' expects a %s but the value '%s' is not valid",
                    fieldPath,
                    ife.getTargetType().getSimpleName(),
                    ife.getValue()
            );

            return errorBuilder(ex, HttpStatus.BAD_REQUEST, req)
                    .detail("Invalid JSON request")
                    //.property("trace", ex.getMostSpecificCause().getMessage())
                    .property("errors", Map.of(fieldPath, List.of(detail)))
                    .build();
        }

        // Fallback for other parse problems (malformed JSON, missing brackets, etc.)
        return errorBuilder(ex, HttpStatus.BAD_REQUEST, req)
                .detail("Malformed JSON request")
                .property("error", ex.getMostSpecificCause().getMessage())
                .build();
    }

    /* ─────────────────────────── helpers ────────────────────────── */

    private ErrorResponse.Builder errorBuilder(Throwable ex,
                                               HttpStatus status,
                                               HttpServletRequest req) {
        logError(ex, status);

        String detail = status.is5xxServerError() && isProd()
                ? "Unexpected server error — provide the correlationId when contacting support."
                : ex.getMessage();

        return ErrorResponse
                .builder(ex, status, detail)
                .title(status.getReasonPhrase())
                .type(URI.create("https://api.flexishop.com/problems/" + status.value()))
                .instance(URI.create(req.getRequestURI()))
                .property("timestamp", OffsetDateTime.now())
                .property("correlationId", correlationId(req));
    }

    private String correlationId(HttpServletRequest req) {
        String fromHeader = req.getHeader("X-Correlation-Id");
        return StringUtils.hasText(fromHeader) ? fromHeader : UUID.randomUUID().toString();
    }

    private Map<String, List<String>> violationMap(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                .stream()
                .collect(groupingBy(
                        this::pathOf,
                        LinkedHashMap::new,
                        mapping(ConstraintViolation::getMessage, toList())));
    }

    /* --------------------------------------------------------
       Helpers
       -------------------------------------------------------- */
    private void logError(Throwable ex, HttpStatus status) {
        if (status.is5xxServerError()) {
            log.atError().log("Unhandled exception ⟨{}⟩", ex.getClass().getSimpleName(), ex);
        } else {
            log.atDebug().log("Client error: {}", ex.getMessage());
        }
    }

    private boolean isProd() {
        return !"dev".equalsIgnoreCase(System.getenv()
                .getOrDefault("SPRING_PROFILES_ACTIVE", "prod"));
    }

    private String pathOf(ConstraintViolation<?> cv) {
        // propertyPath = "createProduct.arg0.name"
        String[] parts = cv.getPropertyPath().toString().split("\\.");
        return parts.length == 0 ? cv.getPropertyPath().toString() : parts[parts.length - 1];
    }
}