#!/usr/bin/env bash
cd deploy && podman-compose up -d nacos-mysql nacos-register-service && cd ..
