package com.gelerion.flexi.shop.product.catalog.infra.mappers;

import com.gelerion.flexi.shop.product.catalog.domain.repositories.entities.ProductCompositeRecord;
import com.gelerion.flexi.shop.product.catalog.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

//    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // Mapping from ProductCompositeRecord to Product
//    Product toProduct(ProductCompositeRecord record);

}