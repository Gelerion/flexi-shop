package com.gelerion.flexi.shop.product.catalog.services;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.ProductEntity;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.ProductRepository;
import com.gelerion.flexi.shop.product.catalog.infra.mappers.ProductMapper;
import com.gelerion.flexi.shop.product.catalog.models.ProductFilterCriteria;
import com.gelerion.flexi.shop.product.catalog.models.ProductIncludeOption;
import com.gelerion.flexi.shop.product.catalog.models.ProductResource;
import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.NoDataFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProductsService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductsService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResource getProduct(UUID productId, List<ProductIncludeOption> include) {
        return productRepository
                .composite()
                .findById(productId, include)
                .map(productMapper::toResource)
                .orElseThrow(() -> new NoDataFoundException("Product with id " + productId + " not found"));
    }


    public Page<ProductResource> listProducts(ProductFilterCriteria criteria, Pageable pageable) {
        Page<ProductEntity> result = productRepository.findAll(criteria, pageable);
        return result.map(productMapper::toResource);
    }

    public Page<ProductResource> listProducts(ProductFilterCriteria filter,
                                              List<ProductIncludeOption> include,
                                              Pageable pageable) {
        return productRepository
                .composite()
                .findAll(filter, include, pageable)
                .map(productMapper::toResource);
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
