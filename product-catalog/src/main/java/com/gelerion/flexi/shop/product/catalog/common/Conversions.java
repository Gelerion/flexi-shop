package com.gelerion.flexi.shop.product.catalog.common;

public class Conversions {

    public static Integer nullSafeLongToInt(Long number) {
        if (number == null) return null;
        return number.intValue();
    }
}
