package com.gelerion.flexi.shop.product.catalog.domain.repositories.impl;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.*;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.ProductRecord;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.ProductRepository;
import com.gelerion.flexi.shop.product.catalog.domain.entities.ProductCompositeEntity;
import org.jetbrains.annotations.Nullable;
import org.jooq.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.gelerion.flexi.shop.product.catalog.domain.converter.impl.JooqRecordConverters.*;
import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.ImageTable.IMAGE;
import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.ProductTable.PRODUCT;
import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.SpecificationTable.SPECIFICATION;
import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.TagTable.TAG;
import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.VariantTable.VARIANT;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.*;

@Repository
public class ProductRepositoryJooq implements ProductRepository {
    private final DSLContext dsl;
    private final CompositeProductRepository compositeProductRepository;

    public ProductRepositoryJooq(DSLContext dsl, CompositeProductRepository compositeProductRepository) {
        this.dsl = dsl;
        this.compositeProductRepository = compositeProductRepository;
    }

    @Override
    public ProductEntity save(ProductEntity product) {
        return dsl.insertInto(PRODUCT)
                .set(dsl.newRecord(PRODUCT, product))
                .returning()
                .fetchOneInto(ProductEntity.class);
    }

    @Override
    public Optional<ProductRecord> findById(int productId) {
        return dsl.selectFrom(PRODUCT)
                .where(PRODUCT.ID.eq(productId))
                .fetchOptional();
    }

    @Override
    public CompositeProductRepository composite() {
        return compositeProductRepository;
    }

    @Repository
    public static class ProductCompositeRepositoryJooq implements ProductRepository.CompositeProductRepository {
        private final DSLContext dsl;

        public ProductCompositeRepositoryJooq(DSLContext dsl) {
            this.dsl = dsl;
        }

        @Override
        public Optional<ProductCompositeEntity> findById(int productId) {
            return dsl.select(
                            PRODUCT.convertFrom(toProductEntity),
                            // implicit join https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/implicit-join/
                            PRODUCT.category().as("category").convertFrom(toCategoryEntity),
                            PRODUCT.subCategory().as("sub_category").convertFrom(toSubCategoryEntity),
                            PRODUCT.brand().as("brand").convertFrom(toBrandEntity),
                            multisets.SPECIFICATIONS,
                            multisets.IMAGES,
                            multisets.TAGS,
                            multisets.VARIANTS
                    )
                    .from(PRODUCT)
                    .where(PRODUCT.ID.eq(productId))
                    .fetchOptional(mapping(ProductCompositeEntity::new));
        }
    }

    private static class multisets {
        private static final Field<List<SpecificationEntity>> SPECIFICATIONS = multiset(
                selectFrom(SPECIFICATION)
                        .where(SPECIFICATION.PRODUCT_ID.eq(PRODUCT.ID)))
                .as("specifications")
                .convertFrom(r -> r.into(SpecificationEntity.class));

        private static final Field<List<ImageEntity>> IMAGES = multiset(
                selectFrom(IMAGE)
                        .where(IMAGE.PRODUCT_ID.eq(PRODUCT.ID)))
                .as("images")
                .convertFrom(r -> r.into(ImageEntity.class));

        private static final Field<List<TagEntity>> TAGS = multiset(
                selectFrom(TAG)
                        .where(TAG.PRODUCT_ID.eq(PRODUCT.ID)))
                .as("tags")
                .convertFrom(r -> r.into(TagEntity.class));

        private static final Field<List<VariantEntity>> VARIANTS = multiset(
                selectFrom(VARIANT)
                        .where(VARIANT.PRODUCT_ID.eq(PRODUCT.ID)))
                .as("variants")
                .convertFrom(r -> r.into(VariantEntity.class));
    }
}
