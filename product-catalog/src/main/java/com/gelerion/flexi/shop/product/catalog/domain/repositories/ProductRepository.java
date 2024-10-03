package com.gelerion.flexi.shop.product.catalog.domain.repositories;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.ProductEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.ProductRecord;
import com.gelerion.flexi.shop.product.catalog.domain.entities.ProductCompositeEntity;

import java.util.Optional;

public interface ProductRepository {

    ProductEntity save(ProductEntity product);

    Optional<ProductRecord> findById(int productId);

    CompositeProductRepository composite();

    interface CompositeProductRepository {
        Optional<ProductCompositeEntity> findById(int productId);
    }

}
