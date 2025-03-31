#!/bin/bash

# --- Configuration ---
# Set the name of your Docker Compose file
DOCKER_COMPOSE_FILE="docker-compose.yml"
# Set the name of your aggregate Gradle code generation task
GRADLE_CODEGEN_TASK="runAllCodegen"
# Set the name of your main Gradle build task
GRADLE_BUILD_TASK="build"

# Exit immediately if any command fails
set -e

echo "=== Starting Project Setup & Build Script ==="

# --- Step 1: Check and Start Docker Compose ---
echo "[INFO] Checking Docker Compose services from '$DOCKER_COMPOSE_FILE'..."

# Use 'docker compose ps --services --filter "status=running"' to list running services.
# Pipe to 'grep .' which returns success (exit code 0) only if there is some output (i.e., at least one service running).
# Use '-q' for quiet mode on grep (we only care about the exit code).
if docker compose -f "$DOCKER_COMPOSE_FILE" ps --services --filter "status=running" | grep -q .; then
  echo "[INFO] Docker services are already running."
else
  echo "[INFO] Docker services not running. Starting required services..."
  # Attempt to start services in detached mode
  docker compose -f "$DOCKER_COMPOSE_FILE" up -d
  echo "[INFO] Docker services started."
  # Optional: Add a small delay if services need a moment to initialize before Gradle connects
  # echo "[INFO] Waiting a few seconds for services to initialize..."
  # sleep 5
fi

# --- Step 2: Run Gradle Code Generation ---
echo "[INFO] Running Gradle code generation task: '$GRADLE_CODEGEN_TASK'..."
./gradlew "$GRADLE_CODEGEN_TASK"
echo "[INFO] Gradle code generation finished."

# --- Step 3: Run Gradle Build ---
echo "[INFO] Running Gradle build task: '$GRADLE_BUILD_TASK'..."
./gradlew -x test "$GRADLE_BUILD_TASK"
echo "[INFO] Gradle build finished."

echo "=== Script Finished Successfully ==="

exit 0