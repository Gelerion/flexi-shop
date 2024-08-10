package com.gelerion.flexi.shop.product.catalog.controllers;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.TagRecord;
import com.gelerion.flexi.shop.product.catalog.models.Product;
import com.gelerion.flexi.shop.product.catalog.models.ProductCriteria;
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
    public ResponseEntity<Product> createProduct(Product product) {
        return null;
    }

    @Override
    public ResponseEntity<Product> getProduct(String productId) {
        var product = productsService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @Override
    public ResponseEntity<List<Product>> searchProducts(ProductCriteria filter, String q, String sort, String order) {
        return null;
    }

    @Override
    public ResponseEntity<Product> updateProduct(String productId, Product product) {
        return null;
    }
}
