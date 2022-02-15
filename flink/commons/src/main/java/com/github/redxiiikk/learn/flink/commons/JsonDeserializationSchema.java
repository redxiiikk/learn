package com.github.redxiiikk.learn.flink.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

public class JsonDeserializationSchema<T> implements DeserializationSchema<T> {
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
