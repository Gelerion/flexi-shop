package com.gelerion.flexi.shop.product.catalog.domain.repositories;

import com.gelerion.flexi.shop.product.catalog.domain.entities.ProductCompositeEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.ProductEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.ProductRecord;
import com.gelerion.flexi.shop.product.catalog.models.ProductFilterCriteria;
import com.gelerion.flexi.shop.product.catalog.models.ProductIncludeOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    ProductEntity save(ProductEntity product);

    Optional<ProductRecord> findById(UUID productId);

    Page<ProductEntity> findAll(ProductFilterCriteria criteria, Pageable pageable);

    Page<ProductEntity> findAll(Pageable pageable);

    CompositeProductRepository composite();

    interface CompositeProductRepository {
        Optional<ProductCompositeEntity> findById(UUID productId, List<ProductIncludeOption> includes);

        Optional<ProductCompositeEntity> findById(UUID productId);

        Page<ProductCompositeEntity> findAll(ProductFilterCriteria criteria,
                                             List<ProductIncludeOption> includes,
                                             Pageable pageable);
    }

}
