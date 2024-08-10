package com.gelerion.flexi.shop.product.catalog.domain.repositories.entities;

import java.util.List;
import java.util.Map;

public record ProductCompositeRecord(
        String productId,
        String name,
        String description,
        Float price,
        String category,
        String subCategory,
        String brand,
        List<Map.Entry<String, String>> specifications,
        List<String> images,
        Integer stock,
        List<String> tags,
        List<String> variants
) {}