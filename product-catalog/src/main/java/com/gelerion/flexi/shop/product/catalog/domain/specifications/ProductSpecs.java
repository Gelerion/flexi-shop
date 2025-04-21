package com.gelerion.flexi.shop.product.catalog.domain.specifications;

import com.gelerion.flexi.shop.product.catalog.api.query.filtering.filters.FieldFilter;
import com.gelerion.flexi.shop.product.catalog.api.query.filtering.parser.QueryFilterParser;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.ProductTable;
import com.gelerion.flexi.shop.product.catalog.models.ProductFilterCriteria;
import com.google.common.collect.ImmutableList;
import org.apache.logging.log4j.util.Strings;
import org.jooq.Field;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.ProductTable.PRODUCT;

@Component
public class ProductSpecs {

    private static final List<FieldBinding> FIELD_BINDINGS = ImmutableList.of(
            bind(ProductFilterCriteria::getName, PRODUCT.NAME),
            bind(ProductFilterCriteria::getPrice, PRODUCT.PRICE),
            bind(ProductFilterCriteria::getStatus, PRODUCT.STATUS),
            bind(ProductFilterCriteria::getInventoryQuantity, PRODUCT.INVENTORY_QUANTITY),
            bind(ProductFilterCriteria::getBrand, PRODUCT.brand().NAME),
            bind(ProductFilterCriteria::getProductTag, PRODUCT.tag().NAME),
            bind(ProductFilterCriteria::getProductCategory, PRODUCT.category().NAME)
    );

    private static <T extends Comparable<? super T>> JooqSpecification<ProductTable> byField(Field<T> field,
                                                                                             FieldFilter filter) {
        T value = field.getDataType().convert(filter.value());
        return switch (filter.operator()) {
            case EQ -> JooqSpecification.where(field.eq(value));
            case GT -> JooqSpecification.where(field.gt(value));
            case LT -> JooqSpecification.where(field.lt(value));
            case GTE -> JooqSpecification.where(field.ge(value));
            case LTE -> JooqSpecification.where(field.le(value));
        };
    }

    private final QueryFilterParser filterParser;

    public ProductSpecs(QueryFilterParser filterParser) {
        this.filterParser = filterParser;
    }

    private static <T extends Comparable<? super T>> FieldBinding bind(
            Function<ProductFilterCriteria, ?> extractor,
            Field<T> field
    ) {
        return new FieldBinding(extractor, filter -> byField(field, filter));
    }

    public JooqSpecification<ProductTable> byPrice(FieldFilter filter) {
        return byField(PRODUCT.PRICE, filter);
    }

    public JooqSpecification<ProductTable> byCriteria(ProductFilterCriteria criteria) {
        return FIELD_BINDINGS
                .stream()
                .flatMap(fieldBinding -> toSpecs(criteria, fieldBinding))
                .reduce(JooqSpecification.empty(), JooqSpecification::and);
    }

    private Stream<JooqSpecification<ProductTable>> toSpecs(ProductFilterCriteria criteria,
                                                            FieldBinding binding) {
        var rawValue = binding.valueExtractor.apply(criteria);
        if (rawValue == null) {
            return Stream.empty();
        }

        var values = (rawValue instanceof Collection<?> c)
                ? c
                : List.of(rawValue);

        return values.stream()
                .map(String::valueOf)
                .filter(Strings::isNotBlank)
                .flatMap(filterParser::parse)
                .map(binding.specBuilder);
    }

    private record FieldBinding(
            Function<ProductFilterCriteria, ?> valueExtractor,
            Function<FieldFilter, JooqSpecification<ProductTable>> specBuilder) {
    }
}
