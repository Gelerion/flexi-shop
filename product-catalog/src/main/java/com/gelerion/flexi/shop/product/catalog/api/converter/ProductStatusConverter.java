package com.gelerion.flexi.shop.product.catalog.api.converter;

import com.gelerion.flexi.shop.product.catalog.models.ProductResource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductStatusConverter implements Converter<String, ProductResource.StatusEnum> {

    @Override
    public ProductResource.StatusEnum convert(String source) {
        if (source.isBlank()) {
            return null;
        }

        return ProductResource.StatusEnum.fromValue(source);
    }
}
