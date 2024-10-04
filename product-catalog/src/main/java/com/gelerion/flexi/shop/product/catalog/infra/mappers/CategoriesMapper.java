package com.gelerion.flexi.shop.product.catalog.infra.mappers;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoriesMapper {

    default String map(CategoryEntity category) {
        return category.name();
    }
}
