FROM docker.io/arm64v8/openjdk:17.0

ENV APPLICATION_SERVER_PORT=8080 \
    NACOS_SERVER_ADDR=127.0.0.1:8848 \
    NACOS_NAMESPACE=public \
    NACOS_GROUP=DEFAULT_GROUP

WORKDIR /opt/gateway-service
ADD ./build/libs/gateway-service-1.0-SNAPSHOT.jar gateway-service.jar

ENTRYPOINT "java" "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" "-jar" "gateway-service.jar"
