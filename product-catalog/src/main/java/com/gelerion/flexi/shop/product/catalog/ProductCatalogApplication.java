package com.gelerion.flexi.shop.product.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class ProductCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductCatalogApplication.class, args);
	}

}
