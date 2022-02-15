package com.github.redxiiikk.learn.flink.commons;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.flink.api.common.serialization.SerializationSchema;

public class JsonSerializationSchema<T> implements SerializationSchema<T> {
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
