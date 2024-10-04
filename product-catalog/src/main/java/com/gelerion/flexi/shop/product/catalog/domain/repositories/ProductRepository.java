package com.gelerion.flexi.shop.product.catalog.domain.repositories;

import com.gelerion.flexi.shop.product.catalog.api.includes.IncludeOption;
import com.gelerion.flexi.shop.product.catalog.domain.entities.ProductCompositeEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.ProductEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.ProductRecord;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository {

    ProductEntity save(ProductEntity product);

    Optional<ProductRecord> findById(UUID productId);

    CompositeProductRepository composite();

    interface CompositeProductRepository {
        Optional<ProductCompositeEntity> findById(UUID productId, Set<IncludeOption> includes);

        Optional<ProductCompositeEntity> findById(UUID productId);
    }

}
