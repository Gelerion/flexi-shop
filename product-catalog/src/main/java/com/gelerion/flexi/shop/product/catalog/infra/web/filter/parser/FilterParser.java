package com.gelerion.flexi.shop.product.catalog.infra.web.filter.parser;

import java.util.List;

public interface FilterParser {

    FilterExpression parse(String expression);

    sealed interface FilterExpression permits ListFilter, LiteralFilter, RangeFilter {
    }

    record LiteralFilter(Object value) implements FilterExpression {
    }

    record ListFilter(List<LiteralFilter> values) implements FilterExpression {
    }

    record RangeFilter(RangeOperator op, Object value) implements FilterExpression {
    }
}
