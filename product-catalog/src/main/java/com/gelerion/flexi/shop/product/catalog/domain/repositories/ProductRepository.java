package com.gelerion.flexi.shop.product.catalog.domain.repositories;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.ProductEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.ProductRecord;
import com.gelerion.flexi.shop.product.catalog.domain.entities.ProductCompositeEntity;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    ProductEntity save(ProductEntity product);

    Optional<ProductRecord> findById(UUID productId);

    CompositeProductRepository composite();

    interface CompositeProductRepository {
        Optional<ProductCompositeEntity> findById(UUID productId);
    }

}
