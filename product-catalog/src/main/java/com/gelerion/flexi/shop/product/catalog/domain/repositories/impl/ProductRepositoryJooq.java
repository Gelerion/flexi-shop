package com.gelerion.flexi.shop.product.catalog.domain.repositories.impl;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.ProductRecord;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.ProductRepository;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.entities.ProductCompositeRecord;
import org.jooq.*;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.Brand.BRAND;
import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.Category.*;
import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.Product.PRODUCT;
import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.Specification.SPECIFICATION;
import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.SubCategory.SUB_CATEGORY;
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
    public ProductRecord save(ProductRecord product) {
        return dsl.insertInto(PRODUCT)
                .set(product)
                .returning()
                .fetchOne();
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
        public Optional<ProductCompositeRecord> findById(int productId) {
            return dsl.select(
                    PRODUCT.ID,
                    PRODUCT.NAME,
                    PRODUCT.PRICE,
                    PRODUCT.STOCK,
                    CATEGORY.NAME.as("category_name"),
                    SUB_CATEGORY.NAME.as("sub_category_name"),
                    BRAND.NAME.as("brand_name"),
                    multiset(
                            select(SPECIFICATION.KEY, SPECIFICATION.VALUE)
                                    .from(SPECIFICATION)
                                    .where(SPECIFICATION.PRODUCT_ID.eq(PRODUCT.ID))
                            //.convertFrom(r -> r.into(Actor.class)))
                    ).as("specifications")
            )
                    .from(PRODUCT)
                    .join(CATEGORY).on(PRODUCT.CATEGORY_ID.eq(CATEGORY.ID))
                    .join(SUB_CATEGORY).on(PRODUCT.SUB_CATEGORY_ID.eq(SUB_CATEGORY.ID))
                    .join(BRAND).on(PRODUCT.BRAND_ID.eq(BRAND.ID))
                    .where(PRODUCT.ID.eq(productId))
                    // .fetch(mapping(Film::new))
                    .fetchOptional(record -> new ProductCompositeRecord(
                            record.get(PRODUCT.ID).toString(),
                            record.get(PRODUCT.NAME),
                            "",
//                            record.get(PRODUCT.DESCRIPTION),
                            record.get(PRODUCT.PRICE).floatValue(),
                            record.get(CATEGORY.NAME),
                            record.get(SUB_CATEGORY.NAME),
                            record.get(BRAND.NAME),
                            record.component8().map(it ->
                                    Map.entry(it.component1(), it.component2())
                            ),
                            Collections.emptyList(),
                            0,
                            Collections.emptyList(),
                            Collections.emptyList()

                    ));
        }
    }
}
