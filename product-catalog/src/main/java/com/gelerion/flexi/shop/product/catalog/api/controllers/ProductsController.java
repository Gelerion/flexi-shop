package com.gelerion.flexi.shop.product.catalog.api.controllers;

import com.gelerion.flexi.shop.product.catalog.models.*;
import com.gelerion.flexi.shop.product.catalog.rest.controllers.ProductsApi;
import com.gelerion.flexi.shop.product.catalog.services.ProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class ProductsController implements ProductsApi {

    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    public ResponseEntity<Void> addTag(UUID productId, String tagId) {
        return null;
    }

    @Override
    public ResponseEntity<ProductResource> createProduct(CreateProductResource createProductResource) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteProduct(UUID productId) {
        return null;
    }

    @Override
    public ResponseEntity<CompositeProductResource> getProductById(UUID productId, List<String> include) {
        var product = productsService.getProduct(productId);
        return ResponseEntity.ok(product);
    }


    @Override
    public ResponseEntity<ProductResourcePaginated> listProducts(List<String> include,
                                                                 String q,
                                                                 ProducNestedResourcesCriteria product,
                                                                 String brand,
                                                                 PriceCriteria price,
                                                                 RatingCriteria rating,
                                                                 Integer offset,
                                                                 Integer limit,
                                                                 List<String> sortBy) {
        return null;
    }

    @Override
    public ResponseEntity<ProductResource> updateProduct(UUID productId, ProductResourceUpdateRequest productResourceUpdateRequest) {
        return null;
    }

//    @Override
//    public ResponseEntity<ProductResource> createProduct(ProductResource productResource) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<Void> deleteProduct(UUID productId) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ProductResource> getProductById(UUID productId) {
//        var product = productsService.getProduct(productId);
//        return ResponseEntity.ok(product);
//    }
//
//    @Override
//    public ResponseEntity<ProductResourceList> listProducts(Integer limit, Integer offset, String q, PriceCriteria price, RatingCriteria rating, List<String> sortBy) {
//        return null;
//    }
//
//
//    @Override
//    public ResponseEntity<ProductResource> updateProduct(UUID productId, ProductResource productResource) {
//        return null;
//    }
}
