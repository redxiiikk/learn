FROM docker.io/arm64v8/openjdk:17.0

ENV APPLICATION_SERVER_PORT=8080 \
    NACOS_SERVER_ADDR=127.0.0.1:8848 \
    NACOS_NAMESPACE=public \
    NACOS_GROUP=DEFAULT_GROUP \
    LABEL_COLOR=baseline

WORKDIR /opt/hello-service
ADD ./build/libs/hello-service-1.0-SNAPSHOT.jar hello-service.jar

ENTRYPOINT "java" "-jar" "hello-service.jar"
