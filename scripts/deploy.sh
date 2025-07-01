#!/bin/bash

ENV=${1:-dev}

case "$ENV" in
  dev)  BRANCH="develop" ;;
  prod) BRANCH="production" ;;
  *)    echo "🚨 Environment not supported: '$ENV'"; exit 1 ;;
esac

COMPOSE_FILE="./docker/$ENV/docker-compose.yaml"
PROJECT=${2:-coffee-shop-management}

echo "🚀 Pulling code from '$BRANCH'"
git fetch origin
git checkout "$BRANCH"
git pull origin "$BRANCH"

if [ ! -f "$COMPOSE_FILE" ]; then
  echo "🚨 File not found: $COMPOSE_FILE"
  exit 1
fi

echo "🚀 Deploying to $ENV environment with branch '$BRANCH'"
docker compose -f "$COMPOSE_FILE" -p "$PROJECT" up --build -d

INTERVAL=3
echo "⏳ Waiting $INTERVAL seconds..."
sleep $INTERVAL

# OPTIONAL: Apply migration if using Flyway or Liquibase
# Assumes your Spring Boot app runs migrations automatically
# Or you can use the following if Flyway CLI is used in a container:
# echo "🚀 Applying database migration..."
# docker exec $PROJECT-app flyway migrate

echo "✅ Deployment successful for '$PROJECT' on '$ENV' environment"
