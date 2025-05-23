package com.gelerion.flexi.shop.product.catalog.infra.configs;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public JsonNullableModule jsonNullModule() {
        return new JsonNullableModule();
    }
}
