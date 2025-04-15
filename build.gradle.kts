
val runAllFlywayMigrateProvider = tasks.register("runAllFlywayMigrate") {
    group = "database"
    description = "Runs Flyway migrations in all applicable subprojects"
    dependsOn(subprojects.mapNotNull { it.tasks.findByName("flywayMigrate")?.path })
}

tasks.register("runAllJooqCodegen") {
    group = "codegen"
    description = "Runs jooqCodegen in all applicable subprojects"
    dependsOn(subprojects.mapNotNull { it.tasks.findByName("jooqCodegen")?.path })
    // Define the ordering HERE:
    mustRunAfter(runAllFlywayMigrateProvider)
}

tasks.register("runAllOpenApiGenerate") {
    group = "codegen"
    description = "Runs openApiGenerate in all applicable subprojects"
    dependsOn(subprojects.mapNotNull { it.tasks.findByName("openApiGenerate")?.path })
}

tasks.register("runAllCodegen") {
    group = "codegen"
    description = "Runs all DB migrations and code generation tasks"
    dependsOn("runAllFlywayMigrate", "runAllJooqCodegen", "runAllOpenApiGenerate")
}

// Optional: A task just for migrations
tasks.register("migrateAllDatabases") {
    group = "database"
    description = "Runs Flyway migrations across all applicable subprojects"
    dependsOn("runAllFlywayMigrate")
}