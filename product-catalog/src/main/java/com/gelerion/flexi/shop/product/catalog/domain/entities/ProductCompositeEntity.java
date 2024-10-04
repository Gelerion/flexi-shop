package com.gelerion.flexi.shop.product.catalog.domain.entities;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.*;

import java.util.List;

public record ProductCompositeEntity(
        ProductEntity product,
        BrandEntity brand,
        List<CategoryEntity> categories,
        List<SpecificationEntity> specifications,
        List<ImageEntity> images,
        List<TagEntity> tags
) {
}
