package com.gelerion.flexi.shop.product.catalog.services;

import com.gelerion.flexi.shop.product.catalog.api.query.params.IncludeOption;
import com.gelerion.flexi.shop.product.catalog.api.query.params.PaginationCriteria;
import com.gelerion.flexi.shop.product.catalog.api.query.params.ProductCriteria;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.ProductEntity;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.ProductRepository;
import com.gelerion.flexi.shop.product.catalog.infra.mappers.ProductMapper;
import com.gelerion.flexi.shop.product.catalog.models.CompositeProductResource;
import com.gelerion.flexi.shop.product.catalog.models.ProductResource;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.exception.NoDataFoundException;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.BrandTable.BRAND;

@Slf4j
@Service
public class ProductsService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductsService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public CompositeProductResource getProduct(UUID productId, Set<IncludeOption> includes) {
        return productRepository
                .composite()
                .findById(productId, includes)
                .map(productMapper::toResource)
                .orElseThrow(() -> new NoDataFoundException("Product with id " + productId + " not found"));
    }

    public Page<ProductResource> listProducts(ProductCriteria criteria,
                                              Set<IncludeOption> projection,
                                              PaginationCriteria pagination) {


        PageRequest pageable = PageRequest.of(pagination.offset(), pagination.limit());

        Condition condition = DSL.trueCondition();
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
}
