package com.gelerion.flexi.shop.product.catalog.domain.repositories;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SelectConnectByStep;
import org.jooq.SortField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.ProductTable.PRODUCT;

@Slf4j
@Component
public class Paginations {
    private final DSLContext dsl;

    public Paginations(DSLContext dsl) {
        this.dsl = dsl;
    }

    @SuppressWarnings("all")
    private static List<SortField<?>> toOrderBy(Sort sort) {
        return sort.stream()
                .map(o -> o.isAscending()
                        ? PRODUCT.field(o.getProperty()).asc()
                        : PRODUCT.field(o.getProperty()).desc())
                .toList();
    }

    public <R extends org.jooq.Record, T> Page<T> paginate(SelectConnectByStep<R> base,
                                                           Pageable pg,
                                                           Class<T> into) {

        long total = dsl.fetchCount(base);

        log.info("Total records found: {}", total);
        if (total == 0) return Page.empty(pg);

        List<T> data = base
                .orderBy(toOrderBy(pg.getSort()))
                .limit(pg.getPageSize())
                .offset(pg.getOffset())
                .fetchInto(into);

        log.info("Returning {} products for page {} of size {}",
                data.size(), pg.getPageNumber(), pg.getPageSize());
        return new PageImpl<>(data, pg, total);
    }
}
