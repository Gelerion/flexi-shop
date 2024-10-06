package com.gelerion.flexi.shop.product.catalog.domain.repositories.impl;

import com.gelerion.flexi.shop.product.catalog.api.query.params.IncludeOption;
import com.gelerion.flexi.shop.product.catalog.common.JooqHelpers;
import com.gelerion.flexi.shop.product.catalog.domain.entities.ProductCompositeEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.*;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.ProductRecord;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.gelerion.flexi.shop.product.catalog.domain.converter.impl.JooqRecordConverters.toBrandEntity;
import static com.gelerion.flexi.shop.product.catalog.domain.converter.impl.JooqRecordConverters.toProductEntity;
import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.BrandTable.BRAND;
import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.ImageTable.IMAGE;
import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.ProductTable.PRODUCT;
import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.SpecificationTable.SPECIFICATION;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.*;

@Slf4j
@Repository
public class ProductRepositoryJooq implements ProductRepository {
    private static final long ZERO_RECORDS = 0L;

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
    public Optional<ProductRecord> findById(UUID productId) {
        return dsl.selectFrom(PRODUCT)
                .where(PRODUCT.ID.eq(productId))
                .fetchOptional();
    }

    @Override
    public Page<ProductEntity> findAll(Condition condition, Pageable pageable) {
        log.info("Executing query with condition: {}", condition);

        // fetch total count for pagination metadata
        long total = dsl.selectCount()
                .from(PRODUCT)
                .join(BRAND).on(PRODUCT.BRAND_ID.eq(BRAND.ID))
                .where(condition)
                .fetchOptional(0, long.class)
                .orElse(0L);

        log.info("Total records found: {}", total);
        if (total == ZERO_RECORDS) {
            log.info("No records found, returning empty Page.");
            return Page.empty(pageable);
        }

        List<ProductEntity> products = dsl.select(PRODUCT.asterisk())
                .from(PRODUCT)
                .join(BRAND).on(PRODUCT.BRAND_ID.eq(BRAND.ID))
                .where(condition)
                .orderBy(convertSortToOrderBy(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(ProductEntity.class);

        log.info("Returning {} products for page {} of size {}",
                products.size(), pageable.getPageNumber(), pageable.getPageSize());
        return new PageImpl<>(products, pageable, total);
    }

    private List<SortField<?>> convertSortToOrderBy(Sort sort) {
        List<SortField<?>> orderByFields = new ArrayList<>();
        for (Sort.Order order : sort) {
            if (order.getDirection().isAscending()) {
                orderByFields.add(PRODUCT.field(order.getProperty()).asc());
            } else {
                orderByFields.add(PRODUCT.field(order.getProperty()).desc());
            }
            log.debug("Sorting by: {} {}", order.getProperty(), order.getDirection());
        }
        return orderByFields;
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
        public Optional<ProductCompositeEntity> findById(UUID productId, Set<IncludeOption> includes) {
            return dsl.select(
                            PRODUCT.convertFrom(toProductEntity),
                            PRODUCT.brand().as("brand").convertFrom(toBrandEntity),
                            includes.contains(IncludeOption.CATEGORIES) ?
                                    multisets.CATEGORIES : JooqHelpers.multisets.empty(CategoryEntity.class),
                            includes.contains(IncludeOption.SPECIFICATIONS) ?
                                    multisets.SPECIFICATIONS : JooqHelpers.multisets.empty(SpecificationEntity.class),
                            includes.contains(IncludeOption.IMAGES) ?
                                    multisets.IMAGES : JooqHelpers.multisets.empty(ImageEntity.class),
                            includes.contains(IncludeOption.TAGS) ?
                                    multisets.TAGS : JooqHelpers.multisets.empty(TagEntity.class)
                    )
                    .from(PRODUCT)
                    .where(PRODUCT.ID.eq(productId))
                    .fetchOptional(mapping(ProductCompositeEntity::new));
        }

        @Override
        public Optional<ProductCompositeEntity> findById(UUID productId) {
            return dsl.select(
                            PRODUCT.convertFrom(toProductEntity),
                            // implicit join https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/implicit-join/
                            PRODUCT.brand().as("brand").convertFrom(toBrandEntity),
                            multisets.CATEGORIES,
                            multisets.SPECIFICATIONS,
                            multisets.IMAGES,
                            multisets.TAGS
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
                select(PRODUCT.tag().asterisk())
                        .from(PRODUCT.tag()))
                .as("tags")
                .convertFrom(r -> r.into(TagEntity.class));

        private static final Field<List<CategoryEntity>> CATEGORIES = multiset(
                select(PRODUCT.category().asterisk())
                        .from(PRODUCT.category()))
                        .as("categories")
                        .convertFrom(r -> r.into(CategoryEntity.class));
    }
}
