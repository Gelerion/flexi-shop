package com.gelerion.flexi.shop.product.catalog.api.converter;

import com.gelerion.flexi.shop.product.catalog.models.ProductIncludeOption;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/*
Query parameters (@ModelAttribute) go through Spring’s DataBinder/ConversionService, which by default
only knows how to do Enum.valueOf(...) (i.e. uppercase constant names) and standard JDK conversions.
 */
@Component
public class ProductIncludeOptionConverter implements Converter<String, ProductIncludeOption> {

    @Override
    public ProductIncludeOption convert(String source) {
        if (source.isBlank()) {
            return null;
        }

        return ProductIncludeOption.fromValue(source);
    }
}
