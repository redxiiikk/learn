FROM docker.io/flink:1.14.3-scala_2.12-java11

RUN cp /opt/flink/opt/flink-queryable-state-runtime-1.14.3.jar /opt/flink/lib/flink-queryable-state-runtime-1.14.3.jar

ENTRYPOINT ["/docker-entrypoint.sh"]
EXPOSE 6123 8081
CMD ["help"]
