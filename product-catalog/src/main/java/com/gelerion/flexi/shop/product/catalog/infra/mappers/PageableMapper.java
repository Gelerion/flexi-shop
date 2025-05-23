package com.gelerion.flexi.shop.product.catalog.infra.mappers;

import com.gelerion.flexi.shop.product.catalog.models.ProductPage;
import com.gelerion.flexi.shop.product.catalog.models.ProductResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {Sort.class, Sort.Order.class})
public interface PageableMapper {

    @Mapping(target = "sort", expression = "java(toOrderList(page.getSort()))")
    ProductPage toProductPage(Page<ProductResource> page);

    default List<Sort.Order> toOrderList(Sort sort) {
        return (sort == null || sort.isUnsorted())
                ? Collections.emptyList()
                : StreamSupport.stream(sort.spliterator(), false)
                .collect(Collectors.toList());
    }
}
