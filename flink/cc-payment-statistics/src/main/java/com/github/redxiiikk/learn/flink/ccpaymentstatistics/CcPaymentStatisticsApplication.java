package com.github.redxiiikk.learn.flink.ccpaymentstatistics;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.redxiiikk.learn.flink.ccpaymentstatistics.event.CcPaymentEvent;
import com.github.redxiiikk.learn.flink.ccpaymentstatistics.event.CcPaymentStatisticsEvent;
import lombok.SneakyThrows;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CcPaymentStatisticsApplication {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.enableCheckpointing(100, CheckpointingMode.AT_LEAST_ONCE);

        DataStreamSource<CcPaymentEvent> source = environment.fromSource(
                generatorSource(), WatermarkStrategy.noWatermarks(), "cc-payments-topic"
        );

        ReduceFunction<CcPaymentStatisticsEvent> reduceFunction = (event1, event2) -> new CcPaymentStatisticsEvent(
                event1.getMerchantId(), event1.getAmount() + event2.getAmount()
        );

        source.map(CcPaymentEvent::to)
                .keyBy(CcPaymentStatisticsEvent::getMerchantId)
                .reduce(reduceFunction)
                .sinkTo(generatorKafkaSink()).name("cc-payments-merchant-statistics-topic");

        environment.execute("CcPaymentStatisticsApplication");
    }

    private static KafkaSource<CcPaymentEvent> generatorSource() {
        return KafkaSource.<CcPaymentEvent>builder()
                .setBootstrapServers("kafka:19092")
                .setGroupId("cc-payment-statistics-consumer")
                .setTopics("cc_payments")
                .setValueOnlyDeserializer(new JsonDeserializationSchema<>(CcPaymentEvent.class))
                .build();
    }

    private static KafkaSink<CcPaymentStatisticsEvent> generatorKafkaSink() {
        KafkaRecordSerializationSchema<CcPaymentStatisticsEvent> recordSerializer
                = KafkaRecordSerializationSchema.builder()
                .setTopic("cc_payments_merchant_statistics")
                .setKeySerializationSchema(
                        (CcPaymentStatisticsEvent event) -> event.getMerchantId().getBytes(StandardCharsets.UTF_8)
                )
                .setValueSerializationSchema(new JsonSerializationSchema<>())
                .build();

        return KafkaSink.<CcPaymentStatisticsEvent>builder()
                .setBootstrapServers("kafka:19092")
                .setRecordSerializer(recordSerializer)
                .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
    }

    public static final class JsonDeserializationSchema<T> implements DeserializationSchema<T> {
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        private final Class<T> tClass;

        public JsonDeserializationSchema(Class<T> tClass) {
            this.tClass = tClass;
        }

        @Override
        public T deserialize(byte[] message) throws IOException {
            return OBJECT_MAPPER.readValue(message, tClass);
        }

        @Override
        public boolean isEndOfStream(T nextElement) {
            return false;
        }

        @Override
        public TypeInformation<T> getProducedType() {
            return TypeInformation.of(tClass);
        }
    }

    public static final class JsonSerializationSchema<T> implements SerializationSchema<T> {
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        static {
            OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
        }

        @SneakyThrows
        @Override
        public byte[] serialize(T element) {
            return OBJECT_MAPPER.writeValueAsBytes(element);
        }
    }
}

