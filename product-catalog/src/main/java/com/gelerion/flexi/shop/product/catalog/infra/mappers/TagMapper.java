package com.gelerion.flexi.shop.product.catalog.infra.mappers;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.TagEntity;
import org.mapstruct.*;

import java.util.List;

import static com.gelerion.flexi.shop.product.catalog.common.Conversions.nullSafeLongToInt;
import static java.util.stream.Collectors.toList;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {

    default String map(TagEntity tag) {
        return tag.tag();
    }

    @Mapping(target = "tag", source = "tag")
    TagEntity toTagEntity(String tag);

    @Mapping(target = "tag", source = "tag")
    @Mapping(target = "productId", source = "productId")
    TagEntity toTagEntity(String tag, Integer productId);

    default List<TagEntity> toTagEntities(List<String> tags, Long productId) {
        return tags.stream()
                .map(tag -> toTagEntity(tag, nullSafeLongToInt(productId)))
                .collect(toList());
    }
}
