package com.gelerion.flexi.shop.product.catalog.infra.web.filter.parser;

import java.util.Optional;

public enum RangeOperator {
    EQ, GT, LT, GTE, LTE;

    public static Optional<RangeOperator> fromString(String text) {
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(RangeOperator.valueOf(text.strip().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
