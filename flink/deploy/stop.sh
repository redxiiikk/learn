#!/bin/bash

# clear resources
nerdctl container stop taskmanager-1
nerdctl container stop taskmanager-2
nerdctl container stop jobmanager
nerdctl container stop redis

nerdctl container rm taskmanager-1
nerdctl container rm taskmanager-2
nerdctl container rm jobmanager
nerdctl container rm redis

nerdctl network rm flink-network
