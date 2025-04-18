package com.gelerion.flexi.shop.product.catalog.services;

import com.gelerion.flexi.shop.product.catalog.api.query.params.PaginationCriteria;
import com.gelerion.flexi.shop.product.catalog.api.query.params.ProductCriteria;
import com.gelerion.flexi.shop.product.catalog.api.query.params.ProductIncludeOption;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.ProductTable;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.ProductEntity;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.ProductRepository;
import com.gelerion.flexi.shop.product.catalog.domain.specifications.JooqSpecification;
import com.gelerion.flexi.shop.product.catalog.infra.mappers.ProductMapper;
import com.gelerion.flexi.shop.product.catalog.infra.web.domain.FilterCriteria;
import com.gelerion.flexi.shop.product.catalog.infra.web.filter.parser.FilterParser;
import com.gelerion.flexi.shop.product.catalog.models.ProductResource;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.exception.NoDataFoundException;
import org.jooq.impl.DSL;
import org.jooq.tools.Convert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.BrandTable.BRAND;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
public class ProductsService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductsService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @SuppressWarnings("unchecked")
    private static <N extends Number> N cast(String v, Field<N> col) {
        return (N) Convert.convert(v, col.getType());
    }

    public ProductResource getProduct(UUID productId, FilterCriteria filterCriteria) {
        JooqSpecification<ProductTable> spec = JooqSpecification.empty();
        for (Map.Entry<String, List<FilterParser.FieldFilter>> entry : filterCriteria.filters().entrySet()) {
            String fieldName = entry.getKey();
            List<FilterParser.FieldFilter> filters = entry.getValue();
            for (FilterParser.FieldFilter filter : filters) {
                spec.and(productTable -> switch (filter) {
                    case FilterParser.LiteralFieldFilter lit -> {
                        Field<?> col = productTable.field(fieldName);
                        Object v = lit.value();
                        String it = Convert.convert(v, col.getType());
                        col.eq(lit.value(), col);
                    }
                    case FilterParser.RangeFieldFilter range -> productTable.get(fieldName()).between(filter.value());
                })
            }

        }


        return productRepository
                .composite()
                .findById(productId, parseIncludes(filterCriteria.includes()))
                .map(productMapper::toResource)
                .orElseThrow(() -> new NoDataFoundException("Product with id " + productId + " not found"));
    }

    public Page<ProductResource> listProducts(ProductCriteria criteria,
                                              Set<ProductIncludeOption> projection,
                                              PaginationCriteria pagination) {


        PageRequest pageable = PageRequest.of(pagination.offset(), pagination.limit());


        Condition condition = DSL.noCondition();
        if (criteria.brand() != null) {
            condition = condition.and(BRAND.NAME.eq(criteria.brand()));
        }

//        if (criteria.price()!= null) {
//            PriceCriteria priceCriteria = criteria.price();
//            if (priceCriteria.getEq() != null) {
//                condition = condition.and(PRODUCT.PRICE.eq(BigDecimal.valueOf(priceCriteria.getEq())));
//            }
//
//            if (priceCriteria.getGte() != null) {
//                condition = condition.and(PRODUCT.PRICE.ge(BigDecimal.valueOf(priceCriteria.getGte())));
//            }
//
//            if (priceCriteria.getLte() != null) {
//                condition = condition.and(PRODUCT.PRICE.le(BigDecimal.valueOf(priceCriteria.getLte())));
//            }

//            condition = condition.and(PRODUCT.PRICE.eq(criteria.price()));
//        }


        Page<ProductEntity> result = productRepository.findAll(condition, pageable);
        System.out.println(result);
        System.out.println(result.getContent());

        return null;
    }

    @Transactional
    public ProductResource createProduct(ProductResource product) {
//        ProductCompositeEntity productEntity = productMapper.toProductEntity(product);
//        System.out.println("productEntity = " + productEntity);
//        Integer id = productRepository.save(productEntity.product()).id();
//        product.setProductId(id.longValue());
        return product;
    }

    private Set<ProductIncludeOption> parseIncludes(Set<String> includes) {
        if (includes == null || includes.isEmpty()) {
            return EnumSet.noneOf(ProductIncludeOption.class);
        }
        return includes
                .stream()
                .map(String::trim)
                .map(String::toUpperCase)
                .map(ProductIncludeOption::valueOf)
                .collect(toSet());
    }
}
