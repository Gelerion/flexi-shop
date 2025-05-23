package com.gelerion.flexi.shop.product.catalog.infra.mappers.utils;

import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface JsonNullableMapper {
    //https://kdrozd.pl/how-to-perform-a-partial-update-patch-with-explicit-null/

    default <T> JsonNullable<T> wrap(T entity) {
        return JsonNullable.of(entity);
    }

    /**
     * Unwraps a JsonNullable<T> to its value T.
     */
    default <T> T unwrap(JsonNullable<T> jsonNullable) {
        return jsonNullable.get(); // .get() is safe here due to isDefined() check
    }

    /**
     * Condition method for MapStruct to check if a JsonNullable field is present.
     * Can be used with @Condition on a mapping.
     * Not strictly part of to/from conversion but useful for mappers using this helper.
     */
    @Condition
    default <T> boolean isDefined(JsonNullable<T> jsonNullable) {
        return jsonNullable != null && jsonNullable.isPresent();
    }
}
