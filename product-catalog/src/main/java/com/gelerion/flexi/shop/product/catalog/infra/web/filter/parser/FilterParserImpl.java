package com.gelerion.flexi.shop.product.catalog.infra.web.filter.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Component
public class FilterParserImpl implements FilterParser {
    private static final String COLON = ":";
    private static final String COMMA = ",";

    private static Optional<String> validateExpression(String expression) {
        if (expression == null || expression.isBlank()) {
            log.atWarn()
                    .log("Received invalid filter expression: {}", expression == null ? "null" : "blank");
            throw new IllegalArgumentException("Filter expression must be a non-empty string");
        }

        if (expression.contains(COLON)) {
            var parts = expression.split(COLON, 2);
            if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
                log.atWarn()
                        .log("Received invalid range filter expression: {} operator or value can not be blank", expression);
                throw new IllegalArgumentException("Invalid range filter format: " + expression);
            }
        }

        log.debug("Validating filter expression: '{}'", expression);
        return Optional.of(expression);
    }

    @Override
    public Stream<FieldFilter> parse(String expression) {
        return validateExpression(expression)
                .map(this::doParse)
                .orElseThrow(() -> new RuntimeException("Failed to parse filter expression: " + expression));
    }

    private Stream<FieldFilter> doParse(String expression) {
        return switch (expression) {
            case String exp when exp.contains(COMMA) -> {
                log.atDebug().log("Processing list filter expression: '{}'", exp);
                var values = exp.split(COMMA);
                if (Arrays.stream(values).anyMatch(String::isBlank)) {
                    throw new IllegalArgumentException("List filter cannot contain empty values: " + exp);
                }
                yield Arrays.stream(values).flatMap(this::doParse);
            }

            case String exp when exp.contains(COLON) -> { //range filter
                // Implementation to parse "operator:value" syntax
                // Example: Handle "rating=gte:2" -> new RangeFilter("gte", 2)
                log.atDebug().log("Processing range filter expression: '{}'", exp);
                String[] parts = expression.split(COLON, 2);
                yield Stream.of(createRangeFilter(parts[0], parts[1]));
            }

            case String exp -> {
                // Implementation to handle single or multiple values for exact match filters.
                // Example: Handle "status=active" -> new LiteralFilter(active);
                log.debug("Processing literal filter expression: '{}'", exp);
                yield Stream.of(new LiteralFieldFilter(exp));
            }
        };
    }

    private FieldFilter createRangeFilter(String operator, String value) {
        return RangeOperator.fromString(operator)
                .map(op -> new RangeFieldFilter(op, value))
                .orElseThrow(() -> {
                    log.atWarn()
                            .log("Unsupported range operator: '{}'", operator);
                    return new IllegalArgumentException("Operator not supported: " + operator);
                });
    }
}
