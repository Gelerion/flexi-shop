package com.gelerion.flexi.shop.product.catalog.api.query.params;

import java.util.List;

public record PaginationCriteria(Integer offset,
                                 Integer limit,
                                 List<String> sortBy) {
}
