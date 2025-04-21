package com.gelerion.flexi.shop.product.catalog.api.query.filtering.parser;

import com.gelerion.flexi.shop.product.catalog.api.query.filtering.filters.FieldFilter;

import java.util.stream.Stream;

public interface QueryFilterParser {

    Stream<FieldFilter> parse(String expression);

}