package com.gelerion.flexi.shop.product.catalog.services;

import com.gelerion.flexi.shop.product.catalog.domain.repositories.ProductRepository;
import com.gelerion.flexi.shop.product.catalog.infra.mappers.ProductMapper;
import com.gelerion.flexi.shop.product.catalog.models.ProductResource;
import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.NoDataFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ProductResource getProduct(UUID productId) {
        return productRepository
                .composite()
                .findById(productId)
                .map(productMapper::toResource)
                .orElseThrow(() -> new NoDataFoundException("Product with id " + productId + " not found"));
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
