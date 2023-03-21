#!/usr/bin/env bash

podman container stop hello-service-baseline hello-service-dev
podman container rm hello-service-baseline hello-service-dev
podman image rm docker.io/redxiiikk/learn-hello-service:1.0.0

./gradlew hello-service:clean  hello-service:bootJar

cd hello-service && podman build -t docker.io/redxiiikk/learn-hello-service:1.0.0 . && cd ..
cd ./deploy && podman-compose up -d hello-service-baseline hello-service-dev && cd ..

podman image list --all | grep none | awk '{print $3}' | xargs podman image rm
