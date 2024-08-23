package com.gelerion.flexi.shop.product.catalog.infra.mappers;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class UriMapper {

    public URI asUri(String uri) throws URISyntaxException {
        return new URI(uri);
    }

}
