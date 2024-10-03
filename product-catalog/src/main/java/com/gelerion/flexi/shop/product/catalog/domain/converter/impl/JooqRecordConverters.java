package com.gelerion.flexi.shop.product.catalog.domain.converter.impl;

import com.gelerion.flexi.shop.product.catalog.domain.converter.RecordConverter;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.BrandEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.CategoryEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.ProductEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.SubCategoryEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.BrandRecord;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.CategoryRecord;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.ProductRecord;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.SubCategoryRecord;

public class JooqRecordConverters {

    public static final RecordConverter<ProductRecord, ProductEntity> toProductEntity =
            r -> r.into(ProductEntity.class);

    public static final RecordConverter<CategoryRecord, CategoryEntity> toCategoryEntity =
            r -> r.into(CategoryEntity.class);

    public static final RecordConverter<SubCategoryRecord, SubCategoryEntity> toSubCategoryEntity =
            r -> r.into(SubCategoryEntity.class);

    public static final RecordConverter<BrandRecord, BrandEntity> toBrandEntity =
            r -> r.into(BrandEntity.class);

}
