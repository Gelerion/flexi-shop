package com.gelerion.flexi.shop.product.catalog.infra.mappers;

import com.gelerion.flexi.shop.product.catalog.domain.entities.enums.ProductStatus;
import com.gelerion.flexi.shop.product.catalog.domain.entities.ProductCompositeEntity;
import com.gelerion.flexi.shop.product.catalog.models.CompositeProductResource;
import com.gelerion.flexi.shop.product.catalog.models.ProductResource;
import org.mapstruct.*;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
        componentModel = ComponentModel.SPRING,
        uses = {UriMapper.class,
                TagMapper.class,
                SpecificationMapper.class,
                CategoriesMapper.class}
)
public interface ProductMapper {

    @Mapping(source = "product", target = ".")
    @Mapping(source = "brand.name", target = "brand")
    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "categories", target = "categories")
    CompositeProductResource toResource(ProductCompositeEntity entity);

    default CompositeProductResource.StatusEnum mapStatus(ProductStatus status) {
        return CompositeProductResource.StatusEnum.fromValue(status.toString());
    }

}