# 1. create virtual machine and start run container runtime
podman machine init -m 4069 --cpus 4 -v $HOME:$HOME --now


# 2. start deploy all service
# 2.1 deploy register service
cd deploy && podman-compose up -d nacos-mysql nacos-register-service && cd ..
# 2.2 deploy gateway service
./gradlew gateway-service:clean  gateway-service:bootJar
podman image rm docker.io/redxiiikk/learn-gateway-service:1.0.0
cd gateway-service && podman build -t docker.io/redxiiikk/learn-gateway-service:1.0.0 . && cd ..
cd ./deploy && podman-compose up -d gateway-service && cd ..

# 2.3 deploy hello service
./gradlew hello-service:clean  hello-service:bootJar
podman image rm docker.io/redxiiikk/learn-hello-service:1.0.0
cd hello-service && podman build -t docker.io/redxiiikk/learn-hello-service:1.0.0 . && cd ..
cd ./deploy && podman-compose up -d hello-service-baseline hello-service-dev && cd ..

