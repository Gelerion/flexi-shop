package com.gelerion.flexi.shop.product.catalog.infra.web.domain;

import com.gelerion.flexi.shop.product.catalog.infra.web.filter.parser.FilterParser.FilterExpression;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Set;

public record FilterCriteria(
        Set<String> projection,
        MultiValueMap<String, FilterExpression> filters
) {
    // Convenience constructor for empty criteria
    public static FilterCriteria empty() {
        return new FilterCriteria(Set.of(), new LinkedMultiValueMap<>());
    }
}
