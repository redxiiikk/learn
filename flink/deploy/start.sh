#!/bin/bash

nerdctl build -f ./flink/Dockerfile --tag 'redxiiikk/flink:latest' ./flink
nerdctl compose -p flink -f ./compose.yml up -d

# nerdctl container start kowl
