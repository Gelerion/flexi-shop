import org.jooq.meta.jaxb.MatcherTransformType
import java.util.*

buildscript {
	dependencies {
		classpath("org.flywaydb:flyway-database-postgresql:11.8.2")
	}
}

plugins {
	java
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
//	id("org.springframework.cloud.contract") version "4.1.3"
	id("org.openapi.generator") version "7.13.0"
	id("idea")
	id("org.jooq.jooq-codegen-gradle") version "3.20.4"
	id("org.flywaydb.flyway") version "11.8.2"
//	kotlin("jvm")
}

group = "com.gelerion.flexi.shop"
version = "0.0.1-SNAPSHOT"

val config = Properties().apply {
	load(file("../.env").inputStream())
}

object Versions {
	const val logbook = "3.9.0"
	const val jacksonNullable = "0.2.6"
	const val jooq = "3.20.4"
}

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

extra["springCloudVersion"] = "2024.0.1"
ext["jooq.version"] = Versions.jooq

dependencies {
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	compileOnly("org.projectlombok:lombok")

    // Web
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Spring
	implementation("org.springframework.boot:spring-boot-starter-aop")

	// Monitoring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")

	// Logging
	implementation("org.zalando:logbook-spring-boot-starter:${Versions.logbook}")

	// Persistence
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.jooq:jooq-meta")
	implementation("org.jooq:jooq-codegen")
	jooqCodegen("org.postgresql:postgresql")

	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.postgresql:postgresql")

	//for pagination
	implementation("org.springframework.data:spring-data-commons")

	// Mapping
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	// Reliability
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")

	// Dev
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	// https://github.com/maciejwalkowiak/spring-boot-startup-report
	developmentOnly("com.maciejwalkowiak.spring:spring-boot-startup-report:0.2.0")

	// Helpers
	implementation("com.google.guava:guava:33.4.8-jre")
	implementation("org.openapitools:jackson-databind-nullable:${Versions.jacksonNullable}")

    // Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
//	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
	testImplementation("org.testcontainers:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation(kotlin("stdlib-jdk8"))
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
	options.compilerArgs.addAll(listOf(
		"-Xlint:unchecked",  // Enables warnings for unchecked operations
		"-Xlint:deprecation", // Enables warnings for deprecated API usage
		"-Amapstruct.suppressGeneratorTimestamp=true", // Suppress timestamp in generated code
		"-Amapstruct.suppressGeneratorVersionInfoComment=true", // Suppress version info in comments
		"-Amapstruct.verbose=true" // Enable verbose output for MapStruct
	))
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
		"openApiNullable" to "true", // Enables handling of explicit nulls vs. absent fields using JsonNullable
		"skipDefaultInterface" to "true", // Do not generate stub implementations
		"additionalModelTypeAnnotations" to "@lombok.Data\n\t@lombok.AllArgsConstructor\n\t@lombok.NoArgsConstructor", // Add Lombok annotations
		"useTags" to "true", // Generate one interface per tag
		"documentationProvider" to "none", // No documentation provider
		"useSwaggerUI" to "false", // Do not use Swagger UI
		"useSpringBoot3" to "true",
		"sourceFolder" to "", // Do not add src/main/java package
		"useJakartaEe" to "true",
		"imports" to "true",
	))
	additionalProperties.put("useSpringPageable", "true")
	importMappings.set(
		mapOf(
			"Pageable" to "org.springframework.data.domain.Pageable",
			"SortOrderInfo" to "org.springframework.data.domain.Sort.Order",
//			"JsonNullable" to "org.openapitools.jackson.nullable.JsonNullable",
		)
	)
	typeMappings.set(
		mapOf(
			"Pageable" to "org.springframework.data.domain.Pageable",
			"SortOrderInfo" to "org.springframework.data.domain.Sort.Order",
		)
	)
	generateApiDocumentation.set(true)
	generateModelDocumentation.set(true)
}

sourceSets {
	main {
		java {
			srcDirs(
				"${layout.buildDirectory.get()}/generated/api",
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
			user = config["DB_USER"] as String
			password = config["DB_PASSWORD"] as String
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
				isFluentSetters = true
				isJavaTimeTypes = true
				isJooqVersionReference = true

				isPojos = true    // Generates POJOs
				isImmutablePojos = false
				isFluentSetters = true
//				isPojosAsJavaRecordClasses = true
//				isRecords = true  // Generates Record classes

				// Naming conventions: add Entity suffix for repository layer objects
				strategy {
					matchers {
						tables {
							table {
								tableClass {
									transform = MatcherTransformType.PASCAL
									expression = "\$0_Table"
								}
								pojoClass {
									transform = MatcherTransformType.PASCAL
									expression = "\$0_Entity"
								}
							}
						}
					}
				}
			}
		}
	}
}

flyway {
	driver = "org.postgresql.Driver"
	url = "jdbc:postgresql://localhost:5432/product_catalog"
	user = config["DB_USER"] as String
	password = config["DB_PASSWORD"] as String
	locations = arrayOf("filesystem:src/main/resources/db/migration")
	cleanDisabled = false
}

//tasks.contractTest {
//	useJUnitPlatform()
//}