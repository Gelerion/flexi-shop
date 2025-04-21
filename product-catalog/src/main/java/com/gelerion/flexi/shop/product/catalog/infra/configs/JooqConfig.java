package com.gelerion.flexi.shop.product.catalog.infra.configs;

import org.jooq.conf.RenderImplicitJoinType;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    /**
     * WARNING: Enabling implicit to-many joins can have significant performance implications
     * and might lead to unexpected Cartesian products if not fully understood.
     * Prefer explicit JOINs where possible.
     */
    @Bean
    public DefaultConfigurationCustomizer jooqConfigurationCustomizer() {
        return configuration -> {
            configuration.settings()
                    .withRenderImplicitJoinToManyType(RenderImplicitJoinType.LEFT_JOIN)
                    .withExecuteLogging(true);
        };
    }

}
