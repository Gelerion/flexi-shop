package com.gelerion.flexi.shop.product.catalog.infra.mappers;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.CategoryEntity;
import com.gelerion.flexi.shop.product.catalog.models.CategoryResource;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoriesMapper {

    CategoryResource toResource(CategoryEntity categoryEntity);

    default String map(CategoryEntity category) {
        return category.getName();
    }
}
