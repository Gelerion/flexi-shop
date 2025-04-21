package com.gelerion.flexi.shop.product.catalog.domain.specifications;

import com.gelerion.flexi.shop.product.catalog.api.query.filtering.filters.FieldFilter;
import com.gelerion.flexi.shop.product.catalog.api.query.filtering.parser.QueryFilterParser;
import com.gelerion.flexi.shop.product.catalog.domain.entities.tables.ProductTable;
import com.gelerion.flexi.shop.product.catalog.models.ProductFilterCriteria;
import org.jooq.Field;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.gelerion.flexi.shop.product.catalog.domain.entities.tables.ProductTable.PRODUCT;

@Component
public class ProductSpecs {
    private final QueryFilterParser filterParser;

    public ProductSpecs(QueryFilterParser filterParser) {
        this.filterParser = filterParser;
    }

    public JooqSpecification<ProductTable> byCriteria(ProductFilterCriteria productCriteria) {
        Filters filters = new Filters()
                .addIfExist("brand", productCriteria.getBrand())
                .addIfExist("price", productCriteria.getPrice())
                .addIfExist("rating", productCriteria.getRating())
                .addIfExist("inventoryQuantity", productCriteria.getInventoryQuantity())
                .addIfExist("name", productCriteria.getName())
                .addIfExist("product[tag]", productCriteria.getProductTag())
                .addIfExist("product[category]", productCriteria.getProductCategory());

        Optional.ofNullable(productCriteria.getStatus())
                .ifPresent(status -> filters.addIfExist("status", status.getValue()));

        return filters.stream()
                .map(this::mapToSpec)
                .reduce(JooqSpecification.empty(), JooqSpecification::and);
    }

    private JooqSpecification<ProductTable> mapToSpec(Pair<String, FieldFilter> filedFilter) {
        String filedName = filedFilter.getFirst();
        FieldFilter filter = filedFilter.getSecond();

        return switch (filedName) {
            case "price" -> byField(PRODUCT.PRICE, filter);
            case "name" -> byField(PRODUCT.NAME, filter);
            case "brand" -> byField(PRODUCT.brand().NAME, filter);
            default -> throw new IllegalStateException("Unexpected value: " + filedName);
        };
    }

    public JooqSpecification<ProductTable> byPrice(FieldFilter filter) {
        return byField(PRODUCT.PRICE, filter);
    }

    private <T extends Comparable<T>> JooqSpecification<ProductTable> byField(Field<T> field, FieldFilter filter) {
        T value = field.getDataType().convert(filter.value());
        return switch (filter.operator()) {
            case EQ -> JooqSpecification.where(field.eq(value));
            case GT -> JooqSpecification.where(field.gt(value));
            case LT -> JooqSpecification.where(field.lt(value));
            case GTE -> JooqSpecification.where(field.ge(value));
            case LTE -> JooqSpecification.where(field.le(value));
        };
    }

    private class Filters {
        private final MultiValueMap<String, FieldFilter> filters = new LinkedMultiValueMap<>();

        Filters addIfExist(String field, String value) {
            if (value == null || value.isEmpty()) {
                return this;
            }

            filterParser.parse(value).forEach(filter -> this.filters.add(field, filter));
            return this;
        }

        Filters addIfExist(String field, List<String> values) {
            if (values == null || values.isEmpty()) {
                return this;
            }

            values.stream()
                    .flatMap(filterParser::parse)
                    .forEach(filter -> this.filters.add(field, filter));

            return this;
        }

        Stream<Pair<String, FieldFilter>> stream() {
            return filters.entrySet()
                    .stream()
                    .flatMap(entry ->
                            entry.getValue()
                                    .stream()
                                    .map(value -> Pair.of(entry.getKey(), value)));
        }
    }

}
