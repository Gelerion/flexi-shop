package com.gelerion.flexi.shop.product.catalog.infra.web.domain;

import com.gelerion.flexi.shop.product.catalog.api.query.filtering.filters.FieldFilter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.Set;

public record FilterCriteria(
        Set<String> projection,
        Set<String> includes,
        MultiValueMap<String, FieldFilter> filters
) {

    public static FilterCriteria empty() {
        return new FilterCriteria(new HashSet<>(), new HashSet<>(), new LinkedMultiValueMap<>());
    }

    public void addProjectionField(String fieldName) {
        this.projection.add(fieldName);
    }

    public void addIncludesField(String fieldName) {
        this.includes.add(fieldName);
    }

}
