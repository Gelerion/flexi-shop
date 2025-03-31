package com.gelerion.flexi.shop.product.catalog.domain.specifications;

import org.jooq.Condition;

public interface Specification {

    Condition toCondition();
}
