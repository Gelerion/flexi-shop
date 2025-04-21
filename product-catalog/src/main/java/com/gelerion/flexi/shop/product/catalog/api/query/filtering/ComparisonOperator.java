package com.gelerion.flexi.shop.product.catalog.api.query.filtering;

import java.util.Optional;

public enum ComparisonOperator {
    EQ, GT, LT, GTE, LTE;

    public static Optional<ComparisonOperator> fromString(String text) {
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(ComparisonOperator.valueOf(text.strip().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
