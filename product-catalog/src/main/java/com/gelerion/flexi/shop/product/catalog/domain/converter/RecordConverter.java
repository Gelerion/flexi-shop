package com.gelerion.flexi.shop.product.catalog.domain.converter;

import org.jooq.Record;

import java.util.function.Function;

public interface RecordConverter<R extends Record, ENTITY> extends Function<R, ENTITY> {
}
