package com.gelerion.flexi.shop.product.catalog.api.controllers;

import com.gelerion.flexi.shop.product.catalog.infra.web.domain.FilterCriteria;
import com.gelerion.flexi.shop.product.catalog.models.ProductCreateRequest;
import com.gelerion.flexi.shop.product.catalog.models.ProductPage;
import com.gelerion.flexi.shop.product.catalog.models.ProductResource;
import com.gelerion.flexi.shop.product.catalog.models.ProductUpdateRequest;
import com.gelerion.flexi.shop.product.catalog.rest.controllers.ProductsApi;
import com.gelerion.flexi.shop.product.catalog.services.ProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
                                                          List<String> include,
                                                          List<String> fields,
                                                          FilterCriteria filterCriteria) {
        var product = productsService.getProduct(productId, filterCriteria);
        return ResponseEntity.ok(product);
    }

    @Override
    public ResponseEntity<ProductPage> listProducts(Integer page,
                                                    Integer size,
                                                    List<String> sort,
                                                    String q,
                                                    List<String> brand,
                                                    List<String> price,
                                                    List<String> rating,
                                                    List<String> productCategory,
                                                    List<String> productTag,
                                                    List<String> include,
                                                    List<String> fields,
                                                    FilterCriteria filterCriteria,
                                                    Pageable pageable) {
        System.out.println("pageable = " + pageable);
        System.out.println("filterCriteria = " + filterCriteria);
        return null;
    }


    @Override
    public ResponseEntity<ProductResource> updateProduct(UUID productId, ProductUpdateRequest productUpdateRequest) {
        return null;
    }
}
