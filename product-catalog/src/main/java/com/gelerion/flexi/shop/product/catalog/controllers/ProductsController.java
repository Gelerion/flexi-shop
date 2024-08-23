package com.gelerion.flexi.shop.product.catalog.controllers;

import com.gelerion.flexi.shop.product.catalog.models.ProductCriteria;
import com.gelerion.flexi.shop.product.catalog.models.ProductResource;
import com.gelerion.flexi.shop.product.catalog.rest.controllers.ProductsApi;
import com.gelerion.flexi.shop.product.catalog.services.ProductsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductsController implements ProductsApi {

    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    public ResponseEntity<ProductResource> createProduct(ProductResource product) {
        return null;
    }

    @Override
    public ResponseEntity<ProductResource> getProduct(String productId) {
        var product = productsService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @Override
    public ResponseEntity<List<ProductResource>> searchProducts(ProductCriteria filter, String q, String sort, String order) {
        return null;
    }

    @Override
    public ResponseEntity<ProductResource> updateProduct(String productId, ProductResource product) {
        return null;
    }
}
