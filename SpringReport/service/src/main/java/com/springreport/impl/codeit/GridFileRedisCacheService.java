package com.springreport.impl.codeit;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.springreport.util.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GridFileRedisCacheService {
	
	@Autowired
	private RedisUtil redisUtil;
	
	/**
     * 批量更新数据
     */
    private String dbdata_content="lk:dbdata:";

    /**
     * 收集指令信息
     */
    private String hand_flag_content="lk:upflag:";

    /**
     * 收集指令信息内容
     */
    private String qk_handle_content="lk:handler:";
    
    
    /**
     * 存入启用存储指令信息
     * @param key
     * @param val
     */
    public void raddFlagContent(String key, Object val) {
        String redisKey = hand_flag_content + key;
        log.info("raddFlagContent---redisKey="+redisKey+"val="+val);
        redisUtil.set(redisKey, val,240,TimeUnit.MINUTES);
    }
    
    /**
     * 根据key 获得email验证码信息
     * @param key
     */
    public Boolean rgetFlagContent(String key) {
        Boolean flag=false;
        try{
            String redisKey = hand_flag_content + key;
            log.info("rgetFlagContent---redisKey="+redisKey);
            flag=(Boolean) redisUtil.get(redisKey);
        }catch (Exception e) {
            // TODO: handle exception
        }
        if(flag == null) {
        	flag = false;
        }
        return flag;
    }
    
    /**
     * 获取数据
     * @param key
     * @return
     */
    public List<String> rgetHandlerContent(String key){
        try{
            String redisKey=qk_handle_content+key;
            //多节点使用
            List<String> lists=redisUtil.popList(redisKey,String.class,500);
            return lists;
        }catch(Exception ex){
            return null;
        }
    }
    
    public List<JSONObject> rgetDbDataContent(String key){
        try{
            String redisKey=dbdata_content+key;
            List<JSONObject> lists=redisUtil.getListBySize(redisKey, -1);
            if(lists!=null && lists.size()>0){
                //从redis中删除
            	redisUtil.delList(redisKey, lists.size());
                return lists;
            }
        }catch(Exception ex){
            return null;
        }
        return null;
    }
    
    /**
    *
    * @param key
    * @param db
    */
   public void raddDbContent(String key,JSONObject db){
       String redisKey=dbdata_content+key;
       redisUtil.addList(redisKey, db);
   }
}
