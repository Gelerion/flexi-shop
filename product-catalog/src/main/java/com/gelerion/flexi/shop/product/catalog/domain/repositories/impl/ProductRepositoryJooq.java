package com.gelerion.flexi.shop.product.catalog.domain.repositories.impl;

import com.gelerion.flexi.shop.product.catalog.common.JooqHelpers;
import com.gelerion.flexi.shop.product.catalog.domain.entities.ProductCompositeEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.*;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.Paginations;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.ProductRepository;
import com.gelerion.flexi.shop.product.catalog.domain.specifications.ProductSpecs;
import com.gelerion.flexi.shop.product.catalog.models.ProductFilterCriteria;
import com.gelerion.flexi.shop.product.catalog.models.ProductIncludeOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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
@RequiredArgsConstructor
public class ProductRepositoryJooq implements ProductRepository {
    private final DSLContext dsl;
    private final CompositeProductRepository compositeProductRepository;
    private final ProductSpecs productSpecs;
    private final Paginations paginations;

    @Override
    public ProductEntity save(ProductEntity product) {
        return dsl.insertInto(PRODUCT)
                .set(dsl.newRecord(PRODUCT, product))
                .returning()
                .fetchOneInto(ProductEntity.class);
    }

    @Override
    public Optional<ProductEntity> findById(UUID productId) {
        return dsl.selectFrom(PRODUCT)
                .where(PRODUCT.ID.eq(productId))
                .fetchOptional()
                .map(toProductEntity);
    }

    @Override
    public Page<ProductEntity> findAll(Pageable pageable) {
        //Use seek instead of limit/offset -- https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/seek-clause/
        var query = dsl.select(PRODUCT.asterisk())
                .from(PRODUCT)
                .join(BRAND).on(PRODUCT.BRAND_ID.eq(BRAND.ID));

        return paginations.paginate(query, pageable, ProductEntity.class);
    }

    @Override
    public Page<ProductEntity> findAll(ProductFilterCriteria criteria, Pageable pageable) {
        Condition where = productSpecs.byCriteria(criteria).toCondition(PRODUCT);

        var query = dsl.select(PRODUCT.asterisk())
                .from(PRODUCT)
                .join(BRAND).on(PRODUCT.BRAND_ID.eq(BRAND.ID))
                .where(where);

        return paginations.paginate(query, pageable, ProductEntity.class);
    }

    //When to use DSL vs. direct record updates:
    //A good rule of thumb is:
    // "If I can express it in a single record.store() or record.update(), I do. If I need any extra SQL magic, I switch to the DSL."

    //Concurrency considerations
    // To prevent lost updates, include an optimistic or pessimistic locking check
    // With Postgres we can leverage xmin hidden field which we can use for optimistic locking,
    // e.g. .where(PRODUCT.ID.eq(...).and(PRODUCT.XMIN.eq(expectedXmin)))
    @Override
    public ProductEntity update(ProductEntity product) {
        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("ProductEntity or its ID cannot be null for update");
        }

        return dsl.update(PRODUCT)
                .set(dsl.newRecord(PRODUCT, product)) // Sets all non-null fields from productRecord
                .where(PRODUCT.ID.eq(product.getId()))
                .returning()
                .fetchOneInto(ProductEntity.class);
    }

    @Override
    public CompositeProductRepository composite() {
        return compositeProductRepository;
    }

    @Repository
    public static class ProductCompositeRepositoryJooq implements ProductRepository.CompositeProductRepository {
        private final DSLContext dsl;
        private final ProductSpecs productSpecs;
        private final Paginations paginations;

        public ProductCompositeRepositoryJooq(DSLContext dsl,
                                              ProductSpecs productSpecs,
                                              Paginations paginations) {
            this.dsl = dsl;
            this.productSpecs = productSpecs;
            this.paginations = paginations;
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
            return select(includes)
                    .where(PRODUCT.ID.eq(productId))
                    .fetchOptional(mapping(ProductCompositeEntity::new));
        }

        @Override
        public Optional<ProductCompositeEntity> findById(UUID productId) {
            return select(List.of(ProductIncludeOption.values()))
                    .where(PRODUCT.ID.eq(productId))
                    .fetchOptional(mapping(ProductCompositeEntity::new));
        }

        @Override
        public Page<ProductCompositeEntity> findAll(ProductFilterCriteria criteria,
                                                    List<ProductIncludeOption> includes,
                                                    Pageable pageable) {
            Condition where = productSpecs.byCriteria(criteria).toCondition(PRODUCT);

            var query = select(includes)
                    .where(where);

            return paginations.paginate(query, pageable, ProductCompositeEntity.class);
        }

        private SelectJoinStep<Record6<ProductEntity,
                BrandEntity,
                List<CategoryEntity>,
                List<SpecificationEntity>,
                List<ImageEntity>,
                List<TagEntity>>>
        select(List<ProductIncludeOption> includes) {
            return dsl.select(
                            PRODUCT.convertFrom(toProductEntity),
                            PRODUCT.brand().convertFrom(toBrandEntity),
                            includes.contains(ProductIncludeOption.CATEGORIES) ?
                                    multisets.CATEGORIES : JooqHelpers.multisets.empty(CategoryEntity.class),
                            includes.contains(ProductIncludeOption.SPECIFICATIONS) ?
                                    multisets.SPECIFICATIONS : JooqHelpers.multisets.empty(SpecificationEntity.class),
                            includes.contains(ProductIncludeOption.IMAGES) ?
                                    multisets.IMAGES : JooqHelpers.multisets.empty(ImageEntity.class),
                            includes.contains(ProductIncludeOption.TAGS) ?
                                    multisets.TAGS : JooqHelpers.multisets.empty(TagEntity.class)
                    )
                    .from(PRODUCT);
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
