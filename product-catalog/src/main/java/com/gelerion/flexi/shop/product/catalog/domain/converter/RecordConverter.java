package com.gelerion.flexi.shop.product.catalog.domain.converter;

import java.util.function.Function;

public interface RecordConverter<RECORD, ENTITY> extends Function<RECORD, ENTITY> {
}
