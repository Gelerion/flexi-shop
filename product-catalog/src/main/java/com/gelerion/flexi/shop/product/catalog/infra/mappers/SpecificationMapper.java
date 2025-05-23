package com.gelerion.flexi.shop.product.catalog.infra.mappers;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.SpecificationEntity;
import com.gelerion.flexi.shop.product.catalog.models.SpecificationResource;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SpecificationMapper {

    SpecificationResource toResource(SpecificationEntity specificationEntity);

}
