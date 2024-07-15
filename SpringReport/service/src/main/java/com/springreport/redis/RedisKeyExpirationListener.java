package com.springreport.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.springreport.enums.RedisPrefixEnum;
import com.springreport.util.DateUtil;
import com.springreport.util.IpAndPortUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.RedisUtil;
import com.springreport.websocket.RedisMessageModel;
import com.springreport.websocket.RedisMessagePublish;

@Service
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener{
 
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private RedisMessagePublish redisMessagePublish;
	
	public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String expiredKey = message.toString();
		if(expiredKey.contains(RedisPrefixEnum.DOCUMENTCACHEFLAG.getCode()))
		{
			String listId = expiredKey.split(":")[1];
			List<String> sheetKeys = redisUtil.getKeys(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId);
			if(!ListUtil.isEmpty(sheetKeys))
			{
				redisUtil.del(sheetKeys);
			}
			List<String> dataKeys = redisUtil.getKeys(RedisPrefixEnum.DOCOMENTCELLDATA.getCode()+listId);
			if(!ListUtil.isEmpty(dataKeys))
			{
				redisUtil.del(dataKeys);
			}
		}else if(expiredKey.contains(RedisPrefixEnum.COEDITSHAREMODE.getCode())) {
			String listId = expiredKey.split(":")[1];
			Map<String, Object> map = new HashMap<>();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("t", "shareModeOperation");
			data.put("k", "exitShareMode");
			map.put("data", data);
			map.put("createTime", DateUtil.getTimestampLong());
			map.put("returnMessage", "success");
			map.put("type", 2);
			map.put("status", 0);
			redisMessagePublish.publishMessage(new RedisMessageModel(IpAndPortUtil.getIpAddressAndPort(), listId, map,true));
		}
	}
}
