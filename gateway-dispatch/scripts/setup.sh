#!/usr/bin/env bash
# 1. create virtual machine and start run container runtime
podman machine init --disk-size 50 -m 4069 --cpus 4 -v "$HOME":"$HOME" --image-path "$HOME/Workspace/Library/QEMU/fedora-coreos-37.20230303.2.0-qemu.aarch64.qcow2.xz"
podman machine start podman-machine-default
