package com.github.redxiiikk.learn.flink.vehicle.statistics.mileage;

import com.github.redxiiikk.learn.flink.commons.JsonDeserializationSchema;
import com.github.redxiiikk.learn.flink.commons.JsonSerializationSchema;
import com.github.redxiiikk.learn.flink.vehicle.statistics.mileage.event.VehicleLocationEvent;
import com.github.redxiiikk.learn.flink.vehicle.statistics.mileage.event.VehicleMileageStatisticsEvent;
import com.github.redxiiikk.learn.flink.vehicle.statistics.mileage.event.VehicleOrderEvent;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.nio.charset.StandardCharsets;

public class VehicleMileageStatisticsApplication {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        MapStateDescriptor<String, VehicleOrderEvent> orderStateDesc = new MapStateDescriptor<>(
                "vehicle-order-state", Types.STRING, TypeInformation.of(new TypeHint<>() {})
        );

        BroadcastStream<VehicleOrderEvent> vehicleOrderState = env.fromSource(
                source("vehicle_order", VehicleOrderEvent.class),
                WatermarkStrategy.noWatermarks(),
                "vehicle-order-topic"
        ).broadcast(orderStateDesc);

        env.fromSource(
                        source("vehicle_location", VehicleLocationEvent.class),
                        WatermarkStrategy.noWatermarks(),
                        "vehicle-location-topic"
                )
                .keyBy(VehicleLocationEvent::getId)
                .connect(vehicleOrderState)
                .process(new VehicleMileageStatisticsProcessFunction())
                .sinkTo(sink());

        env.execute("TotalVehicleMileageApplication");
    }

    public static <T> KafkaSource<T> source(String topicName, Class<T> tClass) {
        return KafkaSource.<T>builder()
                .setBootstrapServers("kafka:19092")
                .setGroupId(topicName + "-consumer")
                .setTopics(topicName)
                .setValueOnlyDeserializer(new JsonDeserializationSchema<>(tClass))
                .build();
    }

    private static KafkaSink<VehicleMileageStatisticsEvent> sink() {
        KafkaRecordSerializationSchema<VehicleMileageStatisticsEvent> recordSerializer
                = KafkaRecordSerializationSchema.builder()
                .setTopic("vehicle_mileage_statistics")
                .setKeySerializationSchema(
                        (VehicleMileageStatisticsEvent event) -> event.getOrderId().getBytes(StandardCharsets.UTF_8)
                )
                .setValueSerializationSchema(new JsonSerializationSchema<>())
                .build();

        return KafkaSink.<VehicleMileageStatisticsEvent>builder()
                .setBootstrapServers("kafka:19092")
                .setRecordSerializer(recordSerializer)
                .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
    }

    public static class VehicleMileageStatisticsProcessFunction extends
            KeyedBroadcastProcessFunction<String, VehicleLocationEvent, VehicleOrderEvent,
                    VehicleMileageStatisticsEvent> {

        private final MapStateDescriptor<String, VehicleOrderEvent> orderStateDesc;
        private final MapStateDescriptor<String, VehicleMileageStatisticsEvent> vehicleMileageStateDesc;

        public VehicleMileageStatisticsProcessFunction() {
            orderStateDesc = new MapStateDescriptor<>(
                    "vehicle-order-state", Types.STRING, TypeInformation.of(new TypeHint<>() {})
            );

            vehicleMileageStateDesc = new MapStateDescriptor<>(
                    "vehicle-mileage-state", Types.STRING, TypeInformation.of(new TypeHint<>() {})
            );
        }

        @Override
        public void processElement(
                VehicleLocationEvent locationEvent,
                KeyedBroadcastProcessFunction<String, VehicleLocationEvent, VehicleOrderEvent,
                        VehicleMileageStatisticsEvent>.ReadOnlyContext ctx,
                Collector<VehicleMileageStatisticsEvent> out) throws Exception {

            ReadOnlyBroadcastState<String, VehicleOrderEvent> broadcastState = ctx.getBroadcastState(orderStateDesc);
            if (!broadcastState.contains(locationEvent.getId())) {
                return;
            }

            VehicleOrderEvent orderEvent = broadcastState.get(locationEvent.getId());
            if ("CLOSED".equals(orderEvent.getStatue())) {
                return;
            }


            MapState<String, VehicleMileageStatisticsEvent> mileageState = getRuntimeContext().getMapState(
                    vehicleMileageStateDesc);

            VehicleMileageStatisticsEvent mileageStatisticsEvent = !mileageState.contains(locationEvent.getId())
                    ? new VehicleMileageStatisticsEvent(orderEvent.getId(), locationEvent.getId(), locationEvent)
                    : mileageState.get(locationEvent.getId()).updateLocation(locationEvent);

            mileageState.put(locationEvent.getId(), mileageStatisticsEvent);

            out.collect(mileageStatisticsEvent);
        }

        @Override
        public void processBroadcastElement(
                VehicleOrderEvent value,
                KeyedBroadcastProcessFunction<String, VehicleLocationEvent, VehicleOrderEvent,
                        VehicleMileageStatisticsEvent>.Context ctx,
                Collector<VehicleMileageStatisticsEvent> out) throws Exception {

            ctx.getBroadcastState(orderStateDesc).put(value.getVehicleNumber(), value);
        }
    }
}
