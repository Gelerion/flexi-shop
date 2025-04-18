# === Configuration ===
TARGET_JAVA_VERSION ?= 21.0.3
SDKMAN_JAVA_IDENTIFIER := $(TARGET_JAVA_VERSION)-zulu
DOCKER_COMPOSE_FILE := docker-compose.yml

# === Shell and Tools ===
SHELL := /bin/bash
.ONESHELL:
SDKMAN_INIT := source "$$HOME/.sdkman/bin/sdkman-init.sh"

# === Gradle Tasks ===
# Define the aggregate codegen task name defined in your root build.gradle
GRADLE_CODEGEN_TASK := runAllCodegen
# Define the clean task
GRADLE_CLEAN_TASK := clean

# === Targets ===
.PHONY: all init check-java switch-java check-docker docker-up docker-down run-codegen clean help

# Default target - alias for the main initialization sequence
all: init

# Main initialization target: Ensures correct Java, starts Docker, runs ALL codegen
init: switch-java check-docker-running $(GRADLE_CODEGEN_TASK)
	@echo "=== Project Initialization Finished ==="

# Target to explicitly check and switch Java version
switch-java:
	@echo "--- Checking Java Version (Target: $(TARGET_JAVA_VERSION)) ---"
	@$(SDKMAN_INIT); \
	current_java_version=$$(java -version 2>&1 | awk -F '"' '/version/ {print $$2}'); \
	echo "Current Java version detected: $$current_java_version"; \
	if [ "$$current_java_version" != "$(TARGET_JAVA_VERSION)" ]; then \
		echo "Switching to Java $(TARGET_JAVA_VERSION) ($(SDKMAN_JAVA_IDENTIFIER))..."; \
		if ! sdk use java $(SDKMAN_JAVA_IDENTIFIER); then \
			echo "ERROR: Failed to switch Java version. Is $(SDKMAN_JAVA_IDENTIFIER) installed via SDKMAN?"; \
			exit 1; \
		fi; \
		echo "Switched. Current version now:"; \
		java -version; \
	else \
		echo "Java version $(TARGET_JAVA_VERSION) is already active."; \
	fi

# Target to check if Docker services are running and start if not
check-docker-running: docker-up

# Target to bring up Docker Compose services if they are not running
docker-up:
	@echo "--- Checking Docker Services ---"
	@if ! docker compose -f $(DOCKER_COMPOSE_FILE) ps --services --filter "status=running" | grep . > /dev/null; then \
		echo "Docker services not running or $(DOCKER_COMPOSE_FILE) not found. Starting them now..."; \
		if ! docker compose -f $(DOCKER_COMPOSE_FILE) up -d; then \
			echo "ERROR: Failed to start Docker services."; \
			exit 1; \
		fi; \
		echo "Docker services started."; \
	else \
		echo "Docker services appear to be running."; \
	fi

# Target to stop and remove Docker Compose services
docker-down:
	@echo "Stopping and removing Docker Compose services..."
	docker compose -f $(DOCKER_COMPOSE_FILE) down

# Target to run all code generation tasks using the aggregate Gradle task
# Ensures Java is switched first by depending on switch-java
$(GRADLE_CODEGEN_TASK): switch-java
	@echo "--- Running All Code Generation Tasks ($(GRADLE_CODEGEN_TASK)) ---"
	@$(SDKMAN_INIT); \
	echo "Using JAVA_HOME=$$JAVA_HOME"; \
	echo "Gradle Version:"; \
	./gradlew -version; \
	echo "Executing $(GRADLE_CODEGEN_TASK)... (This may take a while)"; \
	if ! ./gradlew $(GRADLE_CODEGEN_TASK); then \
		echo "ERROR: Gradle task $(GRADLE_CODEGEN_TASK) failed."; \
		exit 1; \
	fi
	@echo "--- Code Generation Finished ---"

# Target to clean Gradle build artifacts
$(GRADLE_CLEAN_TASK):
	@echo "Cleaning up Gradle build artifacts..."
	$(SDKMAN_INIT); \
	export JAVA_HOME=$$(sdk home java $(SDKMAN_JAVA_IDENTIFIER)); \
	./gradlew $(GRADLE_CLEAN_TASK)

# Help target to display available commands
help:
	@echo "Available commands:"
	@echo "  make all                   Alias for 'make init'"
	@echo "  make init                  Ensures correct Java, starts Docker, runs all code generation tasks (jOOQ, OpenAPI)"
	@echo "  make $(GRADLE_CODEGEN_TASK)    Runs all code generation tasks across submodules (after ensuring correct Java)"
	@echo "  make switch-java           Checks current Java and switches to $(TARGET_JAVA_VERSION) if needed"
	@echo "  make docker-up             Checks Docker services and starts them if needed"
	@echo "  make check-docker-status   Shows the status of Docker services (uses 'docker compose ps')"
	@echo "  make docker-down           Stops and removes Docker services"
	@echo "  make $(GRADLE_CLEAN_TASK)              Runs './gradlew $(GRADLE_CLEAN_TASK)'"
	@echo "  make help                  Shows this help message"
	@echo ""
	@echo "Configuration:"
	@echo "  TARGET_JAVA_VERSION=$(TARGET_JAVA_VERSION) (override with 'make TARGET_JAVA_VERSION=x.y.z ...')"
	@echo "  DOCKER_COMPOSE_FILE=$(DOCKER_COMPOSE_FILE)"
	@echo "  GRADLE_CODEGEN_TASK=$(GRADLE_CODEGEN_TASK) (Gradle task for all codegen)"

# Added a specific target just for checking status without trying to start
.PHONY: check-docker-status
check-docker-status:
	@echo "Checking status of Docker Compose services..."
	docker compose -f $(DOCKER_COMPOSE_FILE) ps