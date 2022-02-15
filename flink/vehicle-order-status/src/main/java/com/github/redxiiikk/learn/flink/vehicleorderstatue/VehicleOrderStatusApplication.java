package com.github.redxiiikk.learn.flink.vehicleorderstatue;

import com.github.redxiiikk.learn.flink.commons.JsonDeserializationSchema;
import com.github.redxiiikk.learn.flink.vehicleorderstatue.event.VehicleOrderEvent;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class VehicleOrderStatusApplication {

    private static final String KAFKA_BOOTSTRAP_SERVERS = "kafka:19092";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        environment.fromSource(
                        source(), WatermarkStrategy.noWatermarks(), "vehicle-order-consumer"
                )
                .keyBy(VehicleOrderEvent::getVehicleNumber)
                .asQueryableState("vehicle-order-queryable-state");

        environment.execute("VehicleOrderStatusApplication");
    }

    private static KafkaSource<VehicleOrderEvent> source() {
        return KafkaSource.<VehicleOrderEvent>builder()
                .setBootstrapServers(KAFKA_BOOTSTRAP_SERVERS)
                .setGroupId("vehicle-order")
                .setTopics("vehicle_order")
                .setValueOnlyDeserializer(new JsonDeserializationSchema<>(VehicleOrderEvent.class))
                .build();
    }
}
