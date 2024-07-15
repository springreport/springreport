package com.springreport.websocket;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.springreport.enums.RedisChannelEnum;
import com.springreport.util.MessageUtil;
import com.springreport.util.RedisUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 发布
 * @author Administrator
 */
@Slf4j
@Data
@Service
public class RedisMessagePublish {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 发布者
     * @param redisMessageModel  消息内容
     * @return
     */
    public boolean publishMessage(RedisMessageModel redisMessageModel){
        try{
        	redisUtil.convertAndSend(RedisChannelEnum.COEDITDOCUMENT.getCode(),JSONObject.toJSONString(redisMessageModel));
            return true;
        }catch (Exception ex){
            log.error("publishMessage Error:{}",ex);
            return false;
        }
    }

}
