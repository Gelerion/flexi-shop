package com.gelerion.flexi.shop.product.catalog.domain.repositories;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.TagRecord;

import java.util.List;

public interface TagRepository {

    List<TagRecord> getTags();

}
