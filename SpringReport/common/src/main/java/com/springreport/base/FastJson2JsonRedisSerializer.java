package com.springreport.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * Redis的序列化工具类
 *
 * @author jiangkuan
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
    private final Class<T> clazz;
    private final Charset charset;

    @SuppressWarnings("unchecked")
    public FastJson2JsonRedisSerializer() {
        this((Class<T>) Object.class, Charset.forName("UTF-8"));
    }

    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        this(clazz, Charset.forName("UTF-8"));
    }

    public FastJson2JsonRedisSerializer(Class<T> clazz, Charset charset) {
        super();
        this.clazz = clazz;
        this.charset = charset;
    }

    /**
     * {@inheritDoc}
     */
    public byte[] serialize(T t) throws SerializationException {
        if (null == t) {
            return new byte[0];
        }
        //SerializerFeature.WriteClassName - 全序列化
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(charset);
    }

    /**
     * {@inheritDoc}
     */
    public T deserialize(byte[] bytes) throws SerializationException {
        if (null == bytes || 1 > bytes.length) {
            return null;
        }
        return JSON.parseObject(new String(bytes, charset), clazz);
    }
}