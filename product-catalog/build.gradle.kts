plugins {
	java
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
//	id("org.springframework.cloud.contract") version "4.1.3"
	id("org.openapi.generator") version "7.7.0"
	id("idea")
	id("org.jooq.jooq-codegen-gradle") version "3.19.10"
}

group = "com.gelerion.flexi.shop"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2023.0.2"

dependencies {
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	compileOnly("org.projectlombok:lombok")

    // Web
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Monitoring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")

	// Persistence
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.jooq:jooq-meta")
	implementation("org.jooq:jooq-codegen")
	jooqCodegen("org.postgresql:postgresql")

	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	runtimeOnly("org.postgresql:postgresql")


	// Reliability
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")

	// Dev
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    // Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
//	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
	testImplementation("org.testcontainers:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

//contracts {
//}

openApiGenerate {
	generatorName.set("spring")
	inputSpec.set("$projectDir/api/openapi/openapi.yaml")
	outputDir.set("${layout.buildDirectory.get()}/generated/api") // Output directory for the generated code
	apiPackage.set("com.gelerion.flexi.shop.product.catalog.rest.controllers") // Package for the generated API classes
	modelPackage.set("com.gelerion.flexi.shop.product.catalog.models") // Package for the generated models
	configOptions.set(mapOf(
		"interfaceOnly" to "true", // Do not generate implementations
		"openApiNullable" to "false", // Do not annotate with custom nullable annotation
		"skipDefaultInterface" to "true", // Do not generate stub implementations
		"additionalModelTypeAnnotations" to "@lombok.Data @lombok.AllArgsConstructor @lombok.NoArgsConstructor", // Add Lombok annotations
		"useTags" to "true", // Generate one interface per tag
		"documentationProvider" to "none", // No documentation provider
		"useSwaggerUI" to "false", // Do not use Swagger UI
		"useSpringBoot3" to "true"
	))
	generateApiDocumentation.set(false)
	generateModelDocumentation.set(false)
}

sourceSets {
	main {
		java {
			srcDirs(
				"${layout.buildDirectory.get()}/generated/api/src/main/java/",
				"${layout.buildDirectory.get()}/generated/jooq"
			)
		}
	}
}

idea {
	module {
		isDownloadJavadoc = true
		isDownloadSources = true
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jooq {
	configuration {
		jdbc {
			driver = "org.postgresql.Driver"
			url = "jdbc:postgresql://localhost:5432/product_catalog"
			user = "user"
			password = "password"
		}

		generator {
			database {
				name = "org.jooq.meta.postgres.PostgresDatabase"
				inputSchema = "public"
			}

			target {
				packageName = "com.gelerion.flexi.shop.product.catalog.domain.entities"
				directory = "${layout.buildDirectory.get()}/generated/jooq"
			}

			generate {
				isDeprecated = false
				isImmutablePojos = false
				isFluentSetters = true
			}
		}
	}
}

//tasks.contractTest {
//	useJUnitPlatform()
//}
