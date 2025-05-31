package com.gelerion.flexi.shop.product.catalog.infra.mappers.utils;

import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

@MapperConfig
public interface CommonIgnoreConfig {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Object ignoreAuditFields(Object source);
}
