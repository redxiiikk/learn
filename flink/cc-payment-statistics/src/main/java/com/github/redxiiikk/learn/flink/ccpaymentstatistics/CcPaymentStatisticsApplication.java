package com.github.redxiiikk.learn.flink.ccpaymentstatistics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.IOException;

public class CcPaymentStatisticsApplication {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<CcPaymentEvent> source = KafkaSource.<CcPaymentEvent>builder()
                .setBootstrapServers("lensesio:9092")
                .setGroupId("cc-payment-statistics-consumer")
                .setTopics("cc_payments")
                .setValueOnlyDeserializer(new JsonDeserializer())
                .build();

        KafkaSink<CcPaymentEvent> sink = KafkaSink.<CcPaymentEvent>builder()
                .setBootstrapServers("lensesio:9092")
                .setRecordSerializer(
                        KafkaRecordSerializationSchema.builder()
                                .setTopic("cc_payment_statistics")
                                .setValueSerializationSchema(new JsonSerialization())
                                .build()
                )
                .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
        environment.fromSource(source, WatermarkStrategy.noWatermarks(), "cc-payment-topic")
                .keyBy(CcPaymentEvent::getMerchantId)
                .reduce((value1, value2) -> {
                    CcPaymentEvent result = new CcPaymentEvent();
                    result.setMerchantId(value1.getMerchantId());
                    result.setAmount(value1.getAmount() + value2.getAmount());
                    return result;
                })
                .sinkTo(sink);

        environment.execute("CcPaymentStatisticsApplication");
    }

    public static final class JsonDeserializer implements DeserializationSchema<CcPaymentEvent> {
        private final static ObjectMapper objectMapper;

        static {
            objectMapper = new ObjectMapper();
        }

        @Override
        public CcPaymentEvent deserialize(byte[] bytes) throws IOException {
            return objectMapper.readValue(StringEscapeUtils.escapeJava(new String(bytes)), CcPaymentEvent.class);
        }

        @Override
        public boolean isEndOfStream(CcPaymentEvent ccPaymentEvent) {
            return false;
        }

        @Override
        public TypeInformation<CcPaymentEvent> getProducedType() {
            return TypeInformation.of(CcPaymentEvent.class);
        }
    }

    public static final class JsonSerialization implements SerializationSchema<CcPaymentEvent> {
        private final static ObjectMapper objectMapper;

        static {
            objectMapper = new ObjectMapper();
        }

        @Override
        public byte[] serialize(CcPaymentEvent ccPaymentEvent) {
            try {
                return objectMapper.writeValueAsBytes(ccPaymentEvent);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return new byte[0];
        }
    }
}
