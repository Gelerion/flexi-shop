package com.gelerion.flexi.shop.product.catalog.api.controllers;

import com.gelerion.flexi.shop.product.catalog.models.*;
import com.gelerion.flexi.shop.product.catalog.rest.controllers.ProductsApi;
import com.gelerion.flexi.shop.product.catalog.services.ProductsService;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ProductsController implements ProductsApi {

    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }


    @Override
    public ResponseEntity<ProductResource> createProduct(ProductResource productResource) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long productId) {
        return null;
    }

    @Override
    public ResponseEntity<ProductResource> getProductById(Long productId) {
        return null;
    }

    @Override
    public ResponseEntity<ProductResourceList> listProducts(Integer limit,
                                                            Integer offset,
                                                            String q,
                                                            PriceCriteria price,
                                                            RatingCriteria rating,
                                                            List<@Pattern(regexp = "^[a-zA-Z0-9_]+:(asc|desc)$")
                                                                    String> sortBy) {
        return null;
    }

    @Override
    public ResponseEntity<ProductResource> updateProduct(Long productId, ProductResource productResource) {
        return null;
    }
}
