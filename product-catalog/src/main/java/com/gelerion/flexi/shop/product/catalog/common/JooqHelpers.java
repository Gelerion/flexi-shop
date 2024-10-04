package com.gelerion.flexi.shop.product.catalog.common;

import org.jooq.Field;
import org.jooq.Record;

import java.util.List;

import static org.jooq.impl.DSL.*;

public class JooqHelpers {

    public static class multisets {
        public static <T> Field<List<T>> empty(Class<? extends T> type) {
            return multiset(select(val(null, Record.class))
                    .where(val(false)))
                    .convertFrom(r -> r.into(type));
        }
    }

}
