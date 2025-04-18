package com.gelerion.flexi.shop.product.catalog.infra.web.filter.parser;

import java.util.stream.Stream;

public interface FilterParser {

    Stream<FieldFilter> parse(String expression);

    sealed interface FieldFilter permits LiteralFieldFilter, RangeFieldFilter {
    }

    record LiteralFieldFilter(Object value) implements FieldFilter {
    }

    record RangeFieldFilter(RangeOperator op, Object value) implements FieldFilter {
    }
}
