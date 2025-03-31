#!/bin/bash
set -a
source .env
set +a

echo "Поднимаем зависимые контейнеры..."
docker-compose up -d postgresqldb elasticsearch rabbitmq redis

echo "Ожидаем готовности сервисов..."
sleep 5

echo "Собираем jar файл..."
mvn clean install -Dspring.profiles.active=test

echo "Останавливаем зависимые контейнеры..."
docker-compose down



