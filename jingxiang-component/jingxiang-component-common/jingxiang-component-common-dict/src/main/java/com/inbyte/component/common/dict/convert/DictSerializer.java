package com.inbyte.component.common.dict.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.inbyte.commons.util.StringUtil;
import com.inbyte.component.common.dict.DictUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

/**
 * 序列化注解自定义实现
 * JsonSerializer<String>：指定String 类型，serialize()方法用于将修改后的数据载入
 */
public class DictSerializer extends JsonSerializer<Serializable> implements ContextualSerializer {

    private String dictName;
    private String originalFieldName;

    @Override
    public void serialize(Serializable key, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(key);

        // 判断Key是否列表类型
        if (key instanceof Collection) {
            gen.writeStringField(originalFieldName + "Name", DictUtil.getName(dictName, (Collection) key));
        } else {
            gen.writeStringField(originalFieldName + "Name", DictUtil.getName(dictName, key));
        }
    }

    /**
     * 获取属性上的注解属性
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        this.originalFieldName = property.getName();
        DictSerialize annotation = property.getAnnotation(DictSerialize.class);
        String name = annotation.name();
        if (StringUtil.isEmpty(name)) {
            this.dictName = annotation.value().getSimpleName();
        } else {
            this.dictName = name;
        }
        return this;
    }

}