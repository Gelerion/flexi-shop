package com.gelerion.flexi.shop.product.catalog.api.controllers;

import com.gelerion.flexi.shop.product.catalog.api.query.params.IncludeOption;
import com.gelerion.flexi.shop.product.catalog.models.*;
import com.gelerion.flexi.shop.product.catalog.rest.controllers.ProductsApi;
import com.gelerion.flexi.shop.product.catalog.services.ProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

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
        var product = productsService.getProduct(productId, parseIncludes(include));
        return ResponseEntity.ok(product);
    }

    @Override
    public ResponseEntity<ProductResourcePaginated> listProducts(List<String> include,
                                                                 List<String> brand,
                                                                 String price,
                                                                 String rating,
                                                                 List<String> productTag,
                                                                 List<String> productCategory,
                                                                 Integer offset,
                                                                 Integer limit,
                                                                 List<String> sortBy) {
//        Set<IncludeOption> includeCriteria = parseIncludes(include);
//        ProductCriteria productCriteria = new ProductCriteria(product, brand, price, rating);
//        PaginationCriteria paginationCriteria = new PaginationCriteria(offset, limit, sortBy);
//        productsService.listProducts(productCriteria, includeCriteria, paginationCriteria);
        return ResponseEntity.ok(new ProductResourcePaginated());
    }


    @Override
    public ResponseEntity<ProductResource> updateProduct(UUID productId,
                                                         ProductResourceUpdateRequest productResourceUpdateRequest) {
        return null;
    }

    private Set<IncludeOption> parseIncludes(List<String> includes) {
        if (includes == null || includes.isEmpty()) {
            return EnumSet.noneOf(IncludeOption.class);
        }
        return includes
                .stream()
                .map(String::trim)
                .map(String::toUpperCase)
                .map(IncludeOption::valueOf)
                .collect(toSet());
    }
}
