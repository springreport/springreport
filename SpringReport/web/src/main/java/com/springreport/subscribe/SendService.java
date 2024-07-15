//package com.springreport.subscribe;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.springreport.enums.RedisChannelEnum;
//import com.springreport.util.RedisUtil;
//
//@Service
//public class SendService {
//	
//	@Autowired
//	private RedisUtil redisUtil;
//	
//	public void sendMessage(String channel,Object message){
//        try {
//        	redisUtil.convertAndSend(channel, message);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//
//}
