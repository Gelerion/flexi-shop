package com.gelerion.flexi.shop.product.catalog.services;

import com.gelerion.flexi.shop.product.catalog.domain.entities.ProductCompositeEntity;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.ProductRepository;
import com.gelerion.flexi.shop.product.catalog.infra.mappers.ProductMapper;
import com.gelerion.flexi.shop.product.catalog.models.ProductResource;
import org.jooq.exception.NoDataFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductsService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductsService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResource getProduct(String productId) {
        return productRepository
                .composite()
                .findById(Integer.parseInt(productId))
                .map(productMapper::toResource)
                .orElseThrow(() -> new NoDataFoundException("Product with id " + productId + " not found"));
    }

    @Transactional
    public ProductResource createProduct(ProductResource product) {
        ProductCompositeEntity productEntity = productMapper.toProductEntity(product);
        System.out.println("productEntity = " + productEntity);
//        Integer id = productRepository.save(productEntity.product()).id();
//        product.setProductId(id.longValue());
        return product;
    }
}
