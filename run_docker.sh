#!/usr/bin/env bash

echo Matando procesos viejos
docker-compose rm -fs

echo Buildeando aplicación
./gradlew build

echo Creando containers nuevos
docker-compose build --no-cache
docker-compose up -d