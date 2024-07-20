package com.gelerion.flexi.shop.product.catalog;

import org.springframework.boot.SpringApplication;

public class TestProductCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProductCatalogApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
