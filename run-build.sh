#!/bin/bash
set -a
source .env
set +a

echo "Поднимаем зависимые контейнеры..."
# Запускаем только те контейнеры, которые нужны для сборки (например, базу, Elasticsearch, RabbitMQ, Redis)
docker-compose up -d postgresqldb elasticsearch rabbitmq redis

# Необходимо дождаться, пока сервисы станут доступны
# Простой вариант – фиксированная задержка (можно заменить на цикл ожидания)
echo "Ожидаем готовности сервисов..."
sleep 5

echo "Собираем jar файл..."
# Если у вас используется mvnw, замените на ./mvnw clean install -DskipTests
mvn clean install -Dspring.profiles.active=test

echo "Останавливаем зависимые контейнеры..."
docker-compose down



