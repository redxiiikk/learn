package com.github.redxiiikk.learn.flink.wordcount;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public final class WordCountApplication {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<String> text = executionEnvironment.fromSource(
                generatorSource(), WatermarkStrategy.noWatermarks(), "word-topic"
        );

        text.flatMap(new Tokenizer())
                .keyBy(event -> event.f0)
                .reduce((value1, value2) -> new Tuple2<>(value1.f0, value1.f1 + value2.f1))
                .sinkTo(generatorKafkaSink()).name("word-count-topic");

        executionEnvironment.execute("WordCountApplication");
    }

    private static KafkaSource<String> generatorSource() {
        return KafkaSource.<String>builder()
                .setBootstrapServers("kafka:19092")
                .setGroupId("word-count-consumer")
                .setTopics("word")
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
    }

    private static KafkaSink<Tuple2<String, Integer>> generatorKafkaSink() {
        KafkaRecordSerializationSchema<Tuple2<String, Integer>> recordSerializer
                = KafkaRecordSerializationSchema.builder()
                .setTopic("word_count")
                .setValueSerializationSchema(new JsonSerializationSchema<Tuple2<String, Integer>>())
                .build();

        return KafkaSink.<Tuple2<String, Integer>>builder()
                .setBootstrapServers("kafka:19092")
                .setRecordSerializer(recordSerializer)
                .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
    }

    public static final class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            // normalize and split the line
            String[] tokens = value.toLowerCase().split("\\W+");

            // emit the pairs
            for (String token : tokens) {
                if (token.length() > 0) {
                    out.collect(new Tuple2<>(token, 1));
                }
            }
        }
    }

    public static final class JsonSerializationSchema<T> implements SerializationSchema<T> {
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        @SneakyThrows
        @Override
        public byte[] serialize(T element) {
            return OBJECT_MAPPER.writeValueAsBytes(element);
        }
    }
}
