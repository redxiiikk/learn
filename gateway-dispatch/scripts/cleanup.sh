#!/usr/bin/env bash
podman container stop hello-service-baseline hello-service-dev gateway-service nacos-mysql nacos-register-service
podman machine stop podman-machine-default