package com.gelerion.flexi.shop.product.catalog.api.query.params;

import com.gelerion.flexi.shop.product.catalog.models.ProductNestedResourcesCriteria;

public record ProductCriteria(
        ProductNestedResourcesCriteria product,
        String brand)
//        PriceCriteria price,
//        RatingCriteria rating)
{
}
