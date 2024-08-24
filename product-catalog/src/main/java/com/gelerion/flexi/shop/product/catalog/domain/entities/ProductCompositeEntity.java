package com.gelerion.flexi.shop.product.catalog.domain.entities;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.*;

import java.util.List;

public record ProductCompositeEntity(
        ProductEntity product,
        CategoryEntity category,
        SubCategoryEntity subCategory,
        BrandEntity brand,
        List<SpecificationEntity> specifications,
        List<ImageEntity> images,
        List<TagEntity> tags,
        List<VariantEntity> variants
) {
}
