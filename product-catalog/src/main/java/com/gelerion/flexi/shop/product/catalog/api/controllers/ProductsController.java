package com.gelerion.flexi.shop.product.catalog.api.controllers;

import com.gelerion.flexi.shop.product.catalog.infra.mappers.PageableMapper;
import com.gelerion.flexi.shop.product.catalog.models.*;
import com.gelerion.flexi.shop.product.catalog.rest.controllers.ProductsApi;
import com.gelerion.flexi.shop.product.catalog.services.ProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class ProductsController implements ProductsApi {
    private final ProductsService productsService;
    private final PageableMapper mapper;

    public ProductsController(ProductsService productsService,
                              PageableMapper mapper) {
        this.productsService = productsService;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<Void> addProductTag(UUID productId, Integer tagId) {
        return null;
    }

    @Override
    public ResponseEntity<ProductResource> createProduct(ProductCreateRequest productCreateRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteProduct(UUID productId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteProductTag(UUID productId, Integer tagId) {
        return null;
    }


    @Override
    public ResponseEntity<ProductResource> getProductById(UUID productId,
                                                          List<ProductIncludeOption> include) {
        ProductResource product = productsService.getProduct(productId, include);
        return ResponseEntity.ok(product);
    }

    @Override
    public ResponseEntity<ProductPage> listProducts(ProductFilterCriteria filter,
                                                    List<ProductIncludeOption> include,
                                                    Pageable pageable) {
        if (include == null || include.isEmpty()) {
            Page<ProductResource> products = productsService.listProducts(filter, pageable);
            return ResponseEntity.ok(mapper.toProductPage(products));
        }

        Page<ProductResource> products = productsService.listProducts(filter, include, pageable);
        return ResponseEntity.ok(mapper.toProductPage(products));
    }

    @Override
    public ResponseEntity<ProductResource> updateProduct(UUID productId, ProductUpdateRequest productUpdateRequest) {
        return null;
    }
}
