package com.gelerion.flexi.shop.product.catalog.domain.repositories.impl;

import com.gelerion.flexi.shop.product.catalog.common.JooqHelpers;
import com.gelerion.flexi.shop.product.catalog.domain.entities.ProductCompositeEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.*;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.ProductRecord;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.ProductRepository;
import com.gelerion.flexi.shop.product.catalog.models.ProductIncludeOption;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Page<ProductEntity> findAll(Pageable pageable) {
        // fetch total count for pagination metadata
        long total = dsl.selectCount()
                .from(PRODUCT)
                .join(BRAND).on(PRODUCT.BRAND_ID.eq(BRAND.ID))
                .fetchOptional(0, long.class)
                .orElse(0L);

        log.info("Total records found: {}", total);
        if (total == ZERO_RECORDS) {
            log.info("No records found, returning empty Page.");
            return Page.empty(pageable);
        }

        //Use seek instead of limit/offet -- https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/seek-clause/
        List<ProductEntity> products = dsl.select(PRODUCT.asterisk())
                .from(PRODUCT)
                .join(BRAND).on(PRODUCT.BRAND_ID.eq(BRAND.ID))
                .orderBy(convertSortToOrderBy(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(ProductEntity.class);

        log.info("Returning {} products for page {} of size {}",
                products.size(), pageable.getPageNumber(), pageable.getPageSize());
        return new PageImpl<>(products, pageable, total);
    }

    @Override
    public Page<ProductEntity> findAll(Condition condition, Pageable pageable) {
        log.info("Executing query with condition: {}", condition);

        Field<BigDecimal> field = field(PRODUCT.PRICE);

//        Condition condition1 = toCondition(field);
//        LessThan<? extends Number> numberLessThan = new LessThan<>();
//        numberLessThan.toCondition(field, BigDecimal.valueOf(4));

        /*
         //JOIN with a product_tag mapping table and the tag table
           condition = condition.and(DSL.exists(
         //      DSL.selectOne()
         //         .from(Tables.PRODUCT_TAG)
         //         .join(Tables.TAG).on(Tables.PRODUCT_TAG.TAG_ID.eq(Tables.TAG.ID)) // Or TAG.SLUG/NAME
         //         .where(Tables.PRODUCT_TAG.PRODUCT_ID.eq(PRODUCT.ID))
         //         .and(Tables.TAG.ID.in(tagIds)) // Or TAG.SLUG/NAME
         // ));
         */

        // fetch total count for pagination metadata
        //productRepository.countByCriteria(queryParams);
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

        //Use seek instead of limit/offet -- https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/seek-clause/
        List<ProductEntity> products = dsl.select(PRODUCT.asterisk())
                .from(PRODUCT)
                .join(BRAND).on(PRODUCT.BRAND_ID.eq(BRAND.ID))
                .where(condition)
                .orderBy(convertSortToOrderBy(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(ProductEntity.class);

        LessThan priceLessThan = new LessThan();
        Condition condition1 = priceLessThan.toCondition(PRODUCT.PRICE, BigDecimal.valueOf(10));
        //PRODUCT.PRICE.getName()

        log.info("Returning {} products for page {} of size {}",
                products.size(), pageable.getPageNumber(), pageable.getPageSize());
        return new PageImpl<>(products, pageable, total);
    }

    public record LessThan() {
        public <T extends Number> Condition toCondition(Field<T> field, T value) {
            return field.lessThan(value);
        }
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

        //Why multisets for Dynamic Data Inclusion?
        /*
        Strategy 1: Dynamic JOINs based on includes
        A traditional approach is to parse the includes parameter and, for each requested relation (e.g., "tags"),
        dynamically add the necessary JOIN clauses to the query, similar to how joins are added for filtering.
        The SELECT clause must also be modified to include columns from these joined tables.

        However, this strategy has significant drawbacks:
            - Flattened Results: Standard SQL joins produce flat, tabular results. If a product has multiple tags,
              joining PRODUCTS with TAGS (via PRODUCT_TAGS) will result in multiple rows for the same product,
              duplicating the product's data.
            - Mapping Complexity: Reconstructing nested DTOs (e.g., a ProductDTO containing a List<TagDTO>) from
              this flattened result set requires complex and often inefficient mapping logic in the application layer.
              While jOOQ offers mapping capabilities, including mapping flattened results using dot-notation
              aliases (TAGS.NAME.as("tags.name")), this can be verbose and doesn't fundamentally solve the data
              duplication issue at the SQL level.

        Strategy 2: Leveraging the MULTISET Operator
        A more modern and often superior approach, available since jOOQ 3.14 and leveraging standard SQL
        features (often emulated by jOOQ using SQL/JSON or SQL/XML), is the MULTISET operator.
        MULTISET allows fetching nested collections directly within a single SQL query. For each row in the
        main query (e.g., each product), a correlated subquery is executed to fetch the related items (e.g., tags),
        and the results are aggregated into a nested collection within that main row.
         */
        @Override
        public Optional<ProductCompositeEntity> findById(UUID productId, List<ProductIncludeOption> includes) {
            return dsl.select(
                            PRODUCT.convertFrom(toProductEntity),
                            PRODUCT.brand().as("brand").convertFrom(toBrandEntity),
                            includes.contains(ProductIncludeOption.CATEGORIES) ?
                                    multisets.CATEGORIES : JooqHelpers.multisets.empty(CategoryEntity.class),
                            includes.contains(ProductIncludeOption.SPECIFICATIONS) ?
                                    multisets.SPECIFICATIONS : JooqHelpers.multisets.empty(SpecificationEntity.class),
                            includes.contains(ProductIncludeOption.IMAGES) ?
                                    multisets.IMAGES : JooqHelpers.multisets.empty(ImageEntity.class),
                            includes.contains(ProductIncludeOption.TAGS) ?
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
