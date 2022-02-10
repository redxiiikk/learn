#!/bin/bash

# define env
REDIS_IMAGE_TAG="6.2.6"
FLINK_IMAGE_TAG="1.14.3-scala_2.12-java11"
FLINK_PROPERTIES="jobmanager.rpc.address: jobmanager"

# create resources
nerdctl network create flink-network

nohup nerdctl run \
    --rm \
    --name=jobmanager \
    --network flink-network \
    --publish 8081:8081 \
    --env FLINK_PROPERTIES="${FLINK_PROPERTIES}" \
    flink:$FLINK_IMAGE_TAG jobmanager > ./logs/jobmanager.log 2>&1 &

nohup nerdctl run \
    --rm \
    --name=taskmanager-1 \
    --network flink-network \
    --env FLINK_PROPERTIES="${FLINK_PROPERTIES}" \
    flink:$FLINK_IMAGE_TAG taskmanager > ./logs/taskmanager-1.log 2>&1 &

nohup nerdctl run \
    --rm \
    --name=taskmanager-2 \
    --network flink-network \
    --env FLINK_PROPERTIES="${FLINK_PROPERTIES}" \
    flink:$FLINK_IMAGE_TAG taskmanager > ./logs/taskmanager-2.log 2>&1 &

nohup nerdctl run \
    --rm \
    --name=redis \
    --network flink-network \
    --publish 6379:6379 \
    redis:$REDIS_IMAGE_TAG > ./logs/redis.log 2>&1 &

tail -f ./logs/jobmanager.log
