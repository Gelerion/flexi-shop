package com.gelerion.flexi.shop.product.catalog.api.converter;

import com.gelerion.flexi.shop.product.catalog.models.ProductStatusParam;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductStatusConverter implements Converter<String, ProductStatusParam> {

    @Override
    public ProductStatusParam convert(String source) {
        if (source.isBlank()) {
            return null;
        }

        return ProductStatusParam.fromValue(source);
    }
}
