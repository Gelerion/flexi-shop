package com.gelerion.flexi.shop.product.catalog.services;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.ProductRecord;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.ProductRepository;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.entities.ProductCompositeRecord;
import com.gelerion.flexi.shop.product.catalog.infra.mappers.ProductMapper;
import com.gelerion.flexi.shop.product.catalog.models.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductsService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductsService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public Product getProduct(String productId) {
        Optional<ProductCompositeRecord> compositeProduct = productRepository.composite().findById(Integer.parseInt(productId));
        System.out.println("compositeProduct = " + compositeProduct.get());

        Optional<ProductRecord> mbProduct = productRepository.findById(Integer.parseInt(productId));
        ProductRecord productRecord = mbProduct.get();

        Product product = new Product();
        product.setProductId(String.valueOf(productRecord.getId()));
        product.setName(productRecord.getName());
        product.setBrand(String.valueOf(productRecord.getBrandId()));
        product.setCategory(String.valueOf(productRecord.getSubCategoryId()));

        return product;
    }
}
