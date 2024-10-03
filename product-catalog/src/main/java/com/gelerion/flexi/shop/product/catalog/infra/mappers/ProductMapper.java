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
        uses = { UriMapper.class, TagMapper.class, SpecificationMapper.class }
)
public interface ProductMapper {

//    @Mapping(source = "product", target = ".")
//    @Mapping(source = "product.id", target = "productId")
//    @Mapping(source = "category.name", target = "category")
//    @Mapping(source = "subCategory.name", target = "subCategory")
//    @Mapping(source = "brand.name", target = "brand")
//    ProductResource toResource(ProductCompositeEntity entity);
//
//    SpecificationResource toSpecificationResource(SpecificationEntity specification);
//
//    ImageResource toImageResource(ImageEntity image);
//
//    VariantResource toVariantResource(VariantEntity variant);
//
//    @Mapping(source = "productId", target = "product.id")
//    @Mapping(source = "name", target = "product.name")
//    @Mapping(source = "description", target = "product.description")
//    @Mapping(source = "price", target = "product.price")
//    @Mapping(source = "stock", target = "product.stock")
//    @Mapping(source = "category", target = "category.name")
//    @Mapping(source = "subCategory", target = "subCategory.name")
//    @Mapping(source = "brand", target = "brand.name")
//    ProductCompositeEntity toProductEntity(ProductResource product);

}