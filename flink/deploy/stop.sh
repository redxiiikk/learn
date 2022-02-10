#!/bin/bash

# clear resources
nerdctl compose -p flink -f ./compose.yml down
