package com.gelerion.flexi.shop.product.catalog.api.query.filtering.filters;

import com.gelerion.flexi.shop.product.catalog.api.query.filtering.ComparisonOperator;

public record FieldFilter(ComparisonOperator operator, Object value) {
    public <T> T value(Class<T> clazz) {
        return clazz.cast(this.value());
    }
}