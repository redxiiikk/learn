# 1. create virtual machine and start run container runtime
podman machine init --disk-size 50 -m 4069 --cpus 4 -v $HOME:$HOME --image-path "$HOME/Workspace/Library/QEMU/fedora-coreos-37.20230303.2.0-qemu.aarch64.qcow2.xz"
podman machine start podman-machine-default

# 2. start deploy all service

# 2.1 deploy register service
cd deploy && podman-compose up -d nacos-mysql nacos-register-service && cd ..

# 2.2 deploy gateway service
podman container stop gateway-service
podman container rm gateway-service
podman image rm docker.io/redxiiikk/learn-gateway-service:1.0.0
./gradlew gateway-service:clean  gateway-service:bootJar
cd gateway-service && podman build -t docker.io/redxiiikk/learn-gateway-service:1.0.0 . && cd ..
cd ./deploy && podman-compose up -d gateway-service && cd ..
podman image list --all | grep none | awk '{print $3}' | xargs podman image rm

# 2.3 deploy hello service
podman container stop hello-service-baseline hello-service-dev
podman container rm hello-service-baseline hello-service-dev
podman image rm docker.io/redxiiikk/learn-hello-service:1.0.0
./gradlew hello-service:clean  hello-service:bootJar
cd hello-service && podman build -t docker.io/redxiiikk/learn-hello-service:1.0.0 . && cd ..
cd ./deploy && podman-compose up -d hello-service-baseline hello-service-dev && cd ..
podman image list --all | grep none | awk '{print $3}' | xargs podman image rm

# 3. stop all service & stop podman machine
podman container stop hello-service-baseline hello-service-dev gateway-service nacos-mysql nacos-register-service
podman machine stop podman-machine-default