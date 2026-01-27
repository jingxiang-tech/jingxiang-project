package com.inbyte.commons.util.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * 序列化注解自定义实现
 * JsonSerializer<String>：指定String 类型，serialize()方法用于将修改后的数据载入
 */
public class ReadableByteSerializer extends JsonSerializer<Integer> {

    @Override
    public void serialize(Integer size, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String val;
        if (size <= 1024) {
            val = size + "b";
        } else if (size > 1024) {
            val = (size / 1024) + "kb";
        } else if (size > 1024 * 1024) {
            val = (size > 1024 * 1024) + "mb";
        } else {
            val = (size > 1024 * 1024 * 1024) + "gb";
        }
        gen.writeString(val);
    }
}