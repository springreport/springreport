//package com.springreport.subscribe;
//
//
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.listener.PatternTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//
//import com.springreport.enums.RedisChannelEnum;
// 
// 
//@Configuration
//@AutoConfigureAfter({OnlineReceiver.class})
//public class SubscriberConfig {
// 
//    /**
//     * 注入消息监听适配器
//     */
//    @Bean
//    public MessageListenerAdapter getMessageListenerAdapter(OnlineReceiver receiver){
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
// 
//    /**
//     * 注入消息监听容器
//     */
//    @Bean
//    public RedisMessageListenerContainer getRedisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter messageListenerAdapter){
//        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
// 
//        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
//        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, new PatternTopic(RedisChannelEnum.UPDATEONLINEREPORT.getCode()));
//        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, new PatternTopic(RedisChannelEnum.SCREENREFSHCHANNEL.getCode()));
//        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, new PatternTopic(RedisChannelEnum.SCREENCOMPONENTREFSHCHANNEL.getCode()));
//        return redisMessageListenerContainer;
//    }
//}
