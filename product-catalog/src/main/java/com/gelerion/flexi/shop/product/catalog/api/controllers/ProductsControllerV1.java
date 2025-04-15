package com.gelerion.flexi.shop.product.catalog.api.controllers;

import com.gelerion.flexi.shop.product.catalog.services.ProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
public class ProductsControllerV1 {

    private final ProductsService productsService;

    public ProductsControllerV1(ProductsService productsService) {
        this.productsService = productsService;
    }

    //    @Override
    public ResponseEntity<Void> addTag(UUID productId, String tagId) {
        return null;
    }
/*
//    @Override
    public ResponseEntity<ProductResource> createProduct(CreateProductResource createProductResource) {
        return null;
    }

//    @Override
    public ResponseEntity<Void> deleteProduct(UUID productId) {
        return null;
    }

//    @Override
    public ResponseEntity<CompositeProductResource> getProductById(UUID productId, List<String> include) {
        var product = productsService.getProduct(productId, parseIncludes(include));
        return ResponseEntity.ok(product);
    }

    //TODO: resolve signature clutter
//    @Override
    public ResponseEntity<ProductResourcePaginated> listProducts(List<String> include,
                                                                 List<String> brand,
                                                                 String price,
                                                                 String rating,
                                                                 List<String> productTag,
                                                                 List<String> productCategory,
                                                                 Integer offset,
                                                                 Integer limit,
                                                                 List<String> sortBy,
                                                                 FilterCriteria filterCriteria) {
        return ResponseEntity.ok(new ProductResourcePaginated());
    }

    *//*
        @Override
    public ResponseEntity<ProductResourcePaginated> listProducts(
        List<String> include, List<String> brand, String price, String rating,
        List<String> productTag, List<String> productCategory, Integer offset,
        Integer limit, List<String> sortBy) {

        // 1. Construct your aggregated objects
        FilterCriteria filterCriteria = FilterCriteria.builder()
            .brands(brand)
            .priceFilter(price) // Assume internal parsing logic
            .ratingFilter(rating) // Assume internal parsing logic
            .tags(productTag)
            .categories(productCategory)
            // ... potentially more complex parsing/mapping logic here
            .build();

        PaginationCriteria paginationCriteria = new PaginationCriteria(offset, limit, sortBy);
        IncludeOptions includeOptions = new IncludeOptions(include);

        // 2. Call your clean internal service method
        ProductResourcePaginated result = productService.findProducts(
            filterCriteria,
            paginationCriteria,
            includeOptions
        );

        return ResponseEntity.ok(result);
    }
     *//*

//    @Override
    public ResponseEntity<ProductResourcePaginated> listProducts(List<String> include,
                                                                 List<String> brand,
                                                                 String price,
                                                                 String rating,
                                                                 List<String> productTag,
                                                                 List<String> productCategory,
                                                                 Integer offset,
                                                                 Integer limit,
                                                                 List<String> sortBy) {
        *//*
        Comparison operators such as lte:1000 (less than or equal to 1000) or gte:50 (greater than or equal to 50).
        Example: gte:10 filters for prices greater than or equal to 10.
         *//*
//        Set<IncludeOption> includeCriteria = parseIncludes(include);
//        ProductCriteria productCriteria = new ProductCriteria(product, brand, price, rating);
//        PaginationCriteria paginationCriteria = new PaginationCriteria(offset, limit, sortBy);
//        productsService.listProducts(productCriteria, includeCriteria, paginationCriteria);


        return ResponseEntity.ok(new ProductResourcePaginated());
    }

    *//*
    @GetMapping("/products")
    public ResponseEntity<Page<ProductDTO>> findProducts(
        FilterCriteria filterCriteria, // Resolved by FilterCriteriaResolver
        Includes includes,             // Resolved by a hypothetical IncludesResolver
        Pageable pageable              // Resolved by PageableHandlerMethodArgumentResolver (contains Sort)
    ) {
        Page<ProductDTO> productPage = productService.findProducts(filterCriteria, includes, pageable);
        //... return response...
    }
     *//*


//    @Override
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
    }*/
}
