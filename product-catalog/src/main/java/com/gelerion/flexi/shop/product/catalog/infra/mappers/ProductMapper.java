package com.gelerion.flexi.shop.product.catalog.infra.mappers;

import com.gelerion.flexi.shop.product.catalog.domain.entities.ProductCompositeEntity;
import com.gelerion.flexi.shop.product.catalog.domain.entities.enums.ProductStatus;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.ProductEntity;
import com.gelerion.flexi.shop.product.catalog.infra.mappers.utils.JsonNullableMapper;
import com.gelerion.flexi.shop.product.catalog.models.ProductResource;
import com.gelerion.flexi.shop.product.catalog.models.ProductUpdateRequest;
import org.mapstruct.*;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
        componentModel = ComponentModel.SPRING,
        uses = {
                BrandMapper.class,
                CategoriesMapper.class,
                SpecificationMapper.class,
                TagMapper.class,
                UriMapper.class,
                JsonNullableMapper.class
        }
)
public interface ProductMapper {

    @Mapping(source = "product", target = ".")
    ProductResource toResource(ProductCompositeEntity entity);

    //TODO: map brandId to BrandResource
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "specifications", ignore = true)
    ProductResource toResource(ProductEntity entity);

    default ProductResource.StatusEnum mapStatus(ProductStatus status) {
        if (status == null) return null;
        return ProductResource.StatusEnum.fromValue(status.toString());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductEntity merge(@MappingTarget ProductEntity entity, ProductUpdateRequest patch);

    default ProductStatus mapStatus(ProductUpdateRequest.StatusEnum apiStatus) {
        if (apiStatus == null) return null;
        return ProductStatus.valueOf(apiStatus.name().toUpperCase());
    }

}