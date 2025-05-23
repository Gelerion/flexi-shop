package com.gelerion.flexi.shop.product.catalog.infra.mappers;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.TagEntity;
import com.gelerion.flexi.shop.product.catalog.models.TagResource;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {

    TagResource toResource(TagEntity tagEntity);

    default String map(TagEntity tag) {
        if (tag == null) return null;
        return tag.getName();
    }
}
