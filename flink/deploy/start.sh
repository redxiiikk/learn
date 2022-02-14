#!/bin/bash

nerdctl compose -p flink -f ./compose.yml up -d

# nerdctl container start kowl kafka-exporter
