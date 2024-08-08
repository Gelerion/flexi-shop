package com.gelerion.flexi.shop.product.catalog.services;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.TagRecord;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {
    private final TagRepository tagRepository;

    public ProductsService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagRecord> getProduct(String productId) {
        return tagRepository.getTags();
    }
}
