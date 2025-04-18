package com.gelerion.flexi.shop.product.catalog.api.query.params;

import com.gelerion.flexi.shop.product.catalog.models.ProductResource;

public record ProductCriteria(
        ProductResource product,
        String brand)
//        PriceCriteria price,
//        RatingCriteria rating)
{
}
