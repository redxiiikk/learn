#!/usr/bin/env bash

podman container stop gateway-service
podman container rm gateway-service
podman image rm docker.io/redxiiikk/learn-gateway-service:1.0.0

./gradlew gateway-service:clean  gateway-service:bootJar

cd gateway-service && podman build -t docker.io/redxiiikk/learn-gateway-service:1.0.0 . && cd ..
cd ./deploy && podman-compose up -d gateway-service && cd ..

podman image list --all | grep none | awk '{print $3}' | xargs podman image rm
