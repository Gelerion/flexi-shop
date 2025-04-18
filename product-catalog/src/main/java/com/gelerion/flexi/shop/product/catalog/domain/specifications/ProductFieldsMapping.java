package com.gelerion.flexi.shop.product.catalog.domain.specifications;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.ProductRecord;
import org.jooq.TableField;

import java.util.Map;


public class ProductFieldsMapping<T> {
    private Map<String, TableField<ProductRecord, T>> a;
}
