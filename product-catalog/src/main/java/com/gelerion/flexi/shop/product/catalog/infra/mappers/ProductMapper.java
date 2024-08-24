package com.gelerion.flexi.shop.product.catalog.infra.mappers;

import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.pojos.*;
import com.gelerion.flexi.shop.product.catalog.domain.entities.ProductCompositeEntity;
import com.gelerion.flexi.shop.product.catalog.models.ImageResource;
import com.gelerion.flexi.shop.product.catalog.models.ProductResource;
import com.gelerion.flexi.shop.product.catalog.models.SpecificationResource;
import com.gelerion.flexi.shop.product.catalog.models.VariantResource;
import org.mapstruct.*;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
        componentModel = ComponentModel.SPRING,
        uses = UriMapper.class
)
public interface ProductMapper {

    @Mapping(source = "product", target = ".")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "category.name", target = "category")
    @Mapping(source = "subCategory.name", target = "subCategory")
    @Mapping(source = "brand.name", target = "brand")
    ProductResource toResource(ProductCompositeEntity entity);

    SpecificationResource toSpecificationResource(SpecificationEntity specification);

    ImageResource toImageResource(ImageEntity image);

    VariantResource toVariantResource(VariantEntity variant);

    default String map(TagEntity tag) {
        return tag.tag();
    }

}