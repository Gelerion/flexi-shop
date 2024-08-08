package com.gelerion.flexi.shop.product.catalog.domain.repositories.impl;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.Tag;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.records.TagRecord;
import com.gelerion.flexi.shop.product.catalog.domain.repositories.TagRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryJooq implements TagRepository {

    private final DSLContext dsl;

    public TagRepositoryJooq(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public List<TagRecord> getTags() {
        return dsl.selectFrom(Tag.TAG).fetch();
    }
}
