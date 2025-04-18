package com.gelerion.flexi.shop.product.catalog.domain.specifications;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

@FunctionalInterface
public interface JooqSpecification<R extends Table<? extends Record>> {

    static <R extends Table<? extends Record>> JooqSpecification<R> where(Condition cond) {
        return root -> cond;
    }

    static <R extends Table<? extends Record>> JooqSpecification<R> empty() {
        return root -> DSL.noCondition();
    }

    static <R extends Table<? extends Record>> JooqSpecification<R> alwaysTrue() {
        return root -> DSL.trueCondition();
    }

    Condition toCondition(R root);

    default JooqSpecification<R> and(JooqSpecification<R> other) {
        return root -> this.toCondition(root).and(other.toCondition(root));
    }

    default JooqSpecification<R> or(JooqSpecification<R> other) {
        return root -> this.toCondition(root).or(other.toCondition(root));
    }
}
