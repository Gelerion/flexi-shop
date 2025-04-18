package com.gelerion.flexi.shop.product.catalog.infra.configs;

import com.gelerion.flexi.shop.product.catalog.infra.web.FilterCriteriaMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ProductCatalogWebMvcConfig implements WebMvcConfigurer {

    private final FilterCriteriaMethodArgumentResolver filterCriteriaResolver;

    public ProductCatalogWebMvcConfig(FilterCriteriaMethodArgumentResolver filterCriteriaResolver) {
        this.filterCriteriaResolver = filterCriteriaResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(filterCriteriaResolver);
    }
}
