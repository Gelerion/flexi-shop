package com.gelerion.flexi.shop.product.catalog.infra.web;

import com.gelerion.flexi.shop.product.catalog.infra.web.domain.FilterCriteria;
import com.gelerion.flexi.shop.product.catalog.infra.web.filter.parser.FilterParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
@Slf4j
/*
Spring MVC's HandlerMethodArgumentResolver interface provides a powerful extension point for customizing how
controller method arguments are resolved from incoming web requests. Instead of relying solely on standard
annotations like @RequestParam or @RequestBody, developers can create custom resolvers to handle complex argument
types or non-standard request data binding scenario

E.g. built-in for pagination: PageableHandlerMethodArgumentResolver

Objective:
Details the design and implementation strategy for a robust, dynamic REST API filtering module.
The module is designed to support Projection, Selection, and Range filtering criteria, enabling flexible data retrieval
for API consumers.

Importance:
Implementing dynamic filtering offers substantial benefits for REST APIs. Projection filtering allows clients to
request only the data fields they need, reducing payload size, minimizing network latency, and conserving bandwidth.
Selection and range filtering provide powerful mechanisms for clients to query and explore data based on specific
criteria, reducing the need for numerous specialized API endpoints or complex client-side data manipulation.
This enhances the overall usability, efficiency, and flexibility of the API for diverse consumer needs.

Defining Filtering Mechanisms and API Contract
- Projection Filtering
- Selection Filtering
- Range Filtering

 */
public class FilterCriteriaMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String FIELDS_PARAM = "fields";
    private static final String INCLUDE_PARAM = "include";
    private static final Set<String> SKIP_FILTER_PARAMS = Set.of("page", "size", "sort", FIELDS_PARAM, INCLUDE_PARAM);
    private static final String COMMA = ",";


    private final FilterParser filterParser;

    public FilterCriteriaMethodArgumentResolver(FilterParser filterParser) {
        this.filterParser = filterParser;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return FilterCriteria.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        FilterCriteria criteria = FilterCriteria.empty();
        getParameterStream(webRequest)
                .forEach(param -> processParameter(param, criteria));

        return criteria;
    }

    private void processParameter(ParamEntry param, FilterCriteria criteria) {
        switch (param.name()) {
            case FIELDS_PARAM -> processValues(param, criteria::addProjectionField);

            case INCLUDE_PARAM -> processValues(param, criteria::addIncludesField);

            case String name when !SKIP_FILTER_PARAMS.contains(name) -> {
                log.atDebug().log("Processing filter parameter '{}' with values: {}", name,
                        Arrays.toString(param.values()));
                Arrays.stream(param.values())
                        .flatMap(filterParser::parse)
                        .forEach(filterExpression -> criteria.filters().add(name, filterExpression));
            }

            default -> log.trace("Skipping non-filter parameter: {}", param.name()); // Skip other parameters
        }
    }

    private Stream<ParamEntry> getParameterStream(NativeWebRequest webRequest) {
        Iterator<String> paramNames = webRequest.getParameterNames();
        Spliterator<String> spliterator = Spliterators.spliteratorUnknownSize(
                paramNames, Spliterator.ORDERED);

        return StreamSupport.stream(spliterator, false)
                .map(name -> new ParamEntry(name, webRequest.getParameterValues(name)))
                .filter(entry -> entry.values() != null);
    }

    record ParamEntry(String name, String[] values) {
        @Override
        public String toString() {
            return "%s=%s".formatted(name, Arrays.toString(values));
        }
    }

    private void processValues(ParamEntry param, Consumer<String> action) {
        log.atDebug().log("Processing parameter '{}' with values: {}", param.name(),
                Arrays.toString(param.values()));
        Arrays.stream(param.values())
                .flatMap(this::split)
                .forEach(action);
    }

    private Stream<String> split(String value) {
        return Arrays.stream(value.split(COMMA));
    }

}
