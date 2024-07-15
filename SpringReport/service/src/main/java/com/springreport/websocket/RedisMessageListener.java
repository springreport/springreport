package com.springreport.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.springreport.util.JsonUtil;
import com.springreport.util.StringUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

/**
 * 监听
 * @author Administrator
 */
@Component
public class RedisMessageListener implements MessageListener {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 订阅者收到消息
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        //JSON.parse(serializerValue.deserialize(message.getBody()).toString())
        RedisSerializer<?> serializerKey = redisTemplate.getKeySerializer();
        RedisSerializer<?> serializerValue = redisTemplate.getValueSerializer();
        Object channel = serializerKey.deserialize(message.getChannel());
        Object body = serializerValue.deserialize(message.getBody());
        String messageStr = body.toString();
        //System.out.println("主题: " + channel);
        //System.out.println("消息内容: " + String.valueOf(body));
        messageStr = StringEscapeUtils.unescapeJava(messageStr).replaceAll("\"", "'");
        RedisMessageModel bson1=JSON.parseObject(messageStr.substring(1, messageStr.length() - 1).replaceAll("\"", "'"), RedisMessageModel.class);
        System.out.println("得到Redis推送消息："+StringUtil.getStringShow(bson1.toString()));
        MyWebSocketHandler.sendMessageToUserByRedis(bson1);
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
