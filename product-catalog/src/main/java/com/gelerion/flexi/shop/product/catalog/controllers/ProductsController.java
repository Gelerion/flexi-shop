package com.gelerion.flexi.shop.product.catalog.controllers;

import com.gelerion.flexi.shop.product.catalog.models.Product;
import com.gelerion.flexi.shop.product.catalog.rest.controllers.ProductsApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductsController implements ProductsApi {

    @Override
    public ResponseEntity<Product> getProduct(String productId) {
        return ResponseEntity.ok(new Product());
    }

    @Override
    public ResponseEntity<List<Product>> searchProducts(String query, String filters, String sort) {
        return null;
    }
}
