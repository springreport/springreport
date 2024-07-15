package com.springreport.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springreport.enums.RedisPrefixEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: RedisUtil
 * @Description: redis工具类
 * @author caiyang
 * @date 2020-06-01 08:26:33
 */
@Component
@Slf4j
public class RedisUtil {

	/**
	 * @Fields redisTemplate : redisTemplate注入
	 * @author caiyang
	 * @date 2020-06-01 08:28:22
	 */
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**  
	 * @Title: hasKey
	 * @Description: 判断key是否存在
	 * @param key
	 * @return
	 * @author caiyang
	 * @date 2020-06-01 08:31:47 
	 */ 
	public boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	
	/**  
	 * @Title: del
	 * @Description: 删除缓存，可以传一个值 或多个
	 * @param key
	 * @author caiyang
	 * @date 2020-06-01 08:33:00 
	 */ 
	public void del(String... key) {
		try {
			if (key != null && key.length > 0) {
				if (key.length == 1) {
					redisTemplate.delete(key[0]);
				} else {
					redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void del(List<String> keys)
	{
		if(!ListUtil.isEmpty(keys))
		{
			redisTemplate.delete(keys);
		}
	}
	
	/**  
	 * @Title: get
	 * @Description: 普通缓存获取
	 * @param key
	 * @return
	 * @author caiyang
	 * @date 2020-06-01 08:33:38 
	 */ 
	public Object get(String key) {
		try {
			return key == null ? null : redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**  
	 * @Title: multiGet
	 * @Description: 获取多个值
	 * @param keys
	 * @return
	 * @author caiyang
	 * @date 2020-08-19 05:57:16 
	 */ 
	public List<Object> multiGet(List<String> keys)
	{
		try {
			return keys == null || keys.size() == 0?null:redisTemplate.opsForValue().multiGet(keys);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**  
	 * @MethodName: multiSet
	 * @Description: 添加多个值
	 * @author caiyang
	 * @param map void
	 * @date 2023-09-25 08:18:05 
	 */ 
	public void multiSet(Map<String, JSONObject> map)
	{
		redisTemplate.opsForValue().multiSet(map);
	}
	
	/**  
	 * @Title: set
	 * @Description: 普通缓存放入
	 * @param key
	 * @param value
	 * @return true成功 false失败
	 * @author caiyang
	 * @date 2020-06-01 08:34:21 
	 */ 
	public boolean set(String key, Object value) {
		try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void getset(String key, Object value) {
		redisTemplate.opsForValue().getAndSet(key, value);
	}
	
	/**  
	 * @Title: set
	 * @Description: 普通缓存放入并设置时间
	 * @param key
	 * @param value
	 * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return
	 * @author caiyang
	 * @date 2020-06-01 08:35:02 
	 */ 
	public boolean set(String key, Object value, long time) {
		try {
			if (time > 0) {
				redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			} else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean set(String key, Object value, long time,TimeUnit timeUnit) {
		try {
			if (time > 0) {
				redisTemplate.opsForValue().set(key, value, time, timeUnit);
			} else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**  
	 * @Title: getExpire
	 * @Description: 获取key过期时间
	 * @param key
	 * @return
	 * @author caiyang
	 * @date 2020-06-16 09:04:34 
	 */ 
	public Long getExpire(String key) {
		try {
			if (StringUtil.isNullOrEmpty(key)) {
				return null;
			}else {
				return redisTemplate.getExpire(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**  
	 * @Title: geoAdd
	 * @Description: 添加geo信息
	 * @param key
	 * @param point
	 * @param member
	 * @author caiyang
	 * @date 2020-07-21 02:37:24 
	 */ 
	public void geoAdd(String key,Point point,Object member) {
		redisTemplate.opsForGeo().add(key, point, member);
	}
	
	/**  
	 * @MethodName: increment
	 * @Description: +1
	 * @author caiyang
	 * @param key
	 * @return 
	 * @return long
	 * @date 2023-02-01 02:14:51 
	 */  
	public long increment(String key) {
		Long result = redisTemplate.opsForValue().increment(key);
		return result;
	}
	
	/**  
	 * @MethodName: decrement
	 * @Description: -1
	 * @author caiyang
	 * @param key
	 * @return 
	 * @return long
	 * @date 2023-02-01 02:15:08 
	 */  
	public long decrement(String key) {
		Long result = redisTemplate.opsForValue().decrement(key);
		return result;
	}
	
	/**  
	 * @MethodName: convertAndSend
	 * @Description: 消息发布
	 * @author caiyang
	 * @param channel
	 * @param message 
	 * @return void
	 * @date 2023-02-07 03:59:16 
	 */  
	public void convertAndSend(String channel,Object message)
	{
		redisTemplate.convertAndSend(channel, message);
	}
	
	/**  
	 * @MethodName: setIfAbsent
	 * @Description: 缓存放入并设置过期时间，如果不存在就添加，返回true，如果存在，不会做任何操作，返回false；
	 * @author caiyang
	 * @param key
	 * @param value
	 * @param time
	 * @return 
	 * @return boolean
	 * @date 2023-02-07 05:02:22 
	 */  
	public boolean setIfAbsent(String key,Object value,long time)
	{
		boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
		return result;
	}
	
	public boolean setIfAbsent(String key,Object value)
	{
		boolean result = redisTemplate.opsForValue().setIfAbsent(key, value);
		return result;
	}
	
	public Long executeLua(String lua,List<String> keys,Long version) {
		DefaultRedisScript<Long> script = new DefaultRedisScript<Long>();
		script.setResultType(Long.class);
		script.setScriptText(lua);
		List<String> redisKeys = new ArrayList();
		for (int i = 0; i < keys.size(); i++) {
			redisKeys.add(keys.get(i));
		}
	    return redisTemplate.execute(script,keys,version);
	}
	
	public String executeLua1(String lua,List<String> keys,Long messageTime) {
		DefaultRedisScript<String> script = new DefaultRedisScript<String>();
		script.setResultType(String.class);
		script.setScriptText(lua);
		List<String> redisKeys = new ArrayList();
		for (int i = 0; i < keys.size(); i++) {
			redisKeys.add(keys.get(i));
		}
	    return redisTemplate.execute(script,keys,messageTime);
	}
	
	/**
     * 从队列的左边获取数据,并删除已取出的数据
     * @param key
     * @param T
     * @param count
     * @param <T>
     * @return
     */
    public <T> List<T> popList(String key,Class<T> T,int count){
        List<T> _list=new ArrayList<T>();
        try{
            for(int x=0;x<count;x++){
                Object obj=pop(key);
                if(obj==null){
                    break;
                }
                _list.add((T)obj);
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return _list;
    }
    
    /**
     * 从队列头部移出并获得值
     *
     * @param key
     * @return
     */
    public Object pop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }
    
    /**
     * 按数量获取
     * @param key
     * @param size
     * @return
     */
    public List getListBySize(String key, long size){
        List liststr=redisTemplate.opsForList().range(key, 0, size);
        return liststr;
    }
    
    /**
     * 从头部删除指定数量的key
     * @param key
     * @param size
     */
    public void delList(String key,int size){
        for(int x=0;x<size;x++){
            try{
                redisTemplate.opsForList().leftPop(key);
            }catch(Exception ex){

            }
        }
    }
    
    /**
     * 添加队列(位置尾部)
     * @param key
     * @param val
     * @return  返回val所在队列的序号
     */
    public long addList(String key,Object val){
        return redisTemplate.opsForList().rightPush(key, val);
    }
	
//    @Async
//    public void cellDataBatchSetRedis(List<Object> jsonArray,String sheetIndex,String listId,long expire)
//    {
//    	if(!ListUtil.isEmpty(jsonArray))
//    	{
//    		//批量set数据
//    		redisTemplate.executePipelined((RedisCallback<String>) connection -> {
//    			JSONObject jsonObject = null;
//    	        for (Object cellData : jsonArray) {
//    	        	jsonObject = (JSONObject) cellData;
//    	        	int r = jsonObject.getIntValue("r");
//    	        	int c = jsonObject.getIntValue("c");
//					connection.setEx((RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +sheetIndex + "_" +r+ "_" +c).getBytes(), expire, JSON.toJSONString(cellData).getBytes());
//    	        }
//    	        return null;
//    	    });
//    	}
//    }
    
  public void cellDataBatchSetRedis(List<Object> jsonArray,String sheetIndex,String listId)
  {
  	if(!ListUtil.isEmpty(jsonArray))
  	{
  		//批量set数据
  		redisTemplate.executePipelined((RedisCallback<String>) connection -> {
  			int t = 0;
  	        for (Object cellData : jsonArray) {
				connection.set((RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +sheetIndex + "_" +t).getBytes(), JSON.toJSONString(cellData).getBytes());
				t++;
  	        }
  	        return null;
  	    });
  	}
  }
    
    public List<Object> batchGetCellData(String sheetIndex,String listId){
    	List<Object> list = redisTemplate.executePipelined((RedisCallback<String>) connection -> {
    		List<String> keys = this.getKeys(RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +sheetIndex);
    		for (String key : keys) {
    			connection.get((key).getBytes());
    		}
            return null;
        });
    	return list;
    }
    
    public List<String> getKeys(String pattern)
    {
    	List<String> result = new ArrayList<String>();
    	Set<String> keys = redisTemplate.keys(pattern+"*" );
    	for (String key : keys) {
    		result.add(key);
		}
    	return result;
    	
    }
    
//    public boolean set(String key, Object value,int offset) {
//		try {
//			redisTemplate.opsForValue().set(key, value,offset);
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
}
