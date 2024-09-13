package com.springreport.websocket;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springreport.api.coedit.ICoeditService;
import com.springreport.api.reporttplsheet.IReportTplSheetService;
import com.springreport.constants.Constants;
import com.springreport.dto.coedit.ShareModeInfo;
import com.springreport.dto.coedit.WSUserModel;
import com.springreport.dto.reporttpl.ResSheetsSettingsDto;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.impl.codeit.RedisLock;
import com.springreport.util.DateUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.Pako_GzipUtils;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;
import com.springreport.util.UrlUtils;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MyWebSocketHandler  extends TextWebSocketHandler{
	
	@Autowired
	private ICoeditService iCoeditService;
	
	@Autowired
	private RedisMessagePublish redisMessagePublish;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private RedisUtil redisUtil;
	
	/**
     * 先注册一个websocket服务器，将连接上的所有用户放进去
     * 外层key gridKey（文档id），内层key session ID（用户id）
     */
    private static final Hashtable<String, Hashtable<String, WSUserModel>> USER_SOCKET_SESSION_MAP;

    public static String ipAndPort;
    
    static {
        USER_SOCKET_SESSION_MAP = new Hashtable<String, Hashtable<String, WSUserModel>>(12);
    }
    
    /**
     * 前台连接并且注册了账户
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.setTextMessageSizeLimit(2560000);
        session.setBinaryMessageSizeLimit(2560000);
        openConn(session);
    }
    
    /**
     * 接受消息
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message.getPayloadLength() == 0) {
            //无消息直接返回
            return;
        }
        //返回消息类型type :0：连接成功，1.发送给发送信息用户，2.发送信息给其他用户，3.发送选区位置信息 999、用户连接断开
        Map map = new HashMap<>();
        boolean _b = true;
        boolean s = true;
        WSUserModel wsUserModel = new WSUserModel(session);
        String content = message.getPayload().toString();
        boolean sendSelf = false;
        if ("rub".equals(content)) {
            log.info("保持连接状态信息");
        } else {
            log.info("消息解压前：" + StringUtil.getStringShow(content));
            String contentReal = Pako_GzipUtils.unCompressToURI(content);
            log.info("消息解压后：" + StringUtil.getStringShow(contentReal));
            //content=contentReal;
            JSONObject bson = null;
            try {
                bson = JSONObject.parseObject(contentReal);
            } catch (Exception ex) {
                log.info("json字符串转换错误str:" + JSONObject.toJSONString(contentReal));
                return;
            }
            boolean isReport = bson.getBooleanValue("isReport");//是否是报表模式
//            int reportType = bson.getIntValue("reportType");
            String _id = wsUserModel.getGridKey();
            String gridKey = UrlUtils.urlDecode(_id);
            if(!isReport)
            {
            	redisUtil.set(RedisPrefixEnum.DOCUMENTCACHEFLAG.getCode()+gridKey, 1,Constants.LUCKYCACHETIME);
            }
            if (bson != null && !StringUtil.isNullOrEmpty(_id)) {
                if (bson.get("t").equals("mv")) {
                    //记录光标位置
                    s = false;
                    map.put("type", 3);
                    map.put("username", wsUserModel.getUserName());
                    map.put("id", "" + wsUserModel.getWs().getId());
                    this.sengMsg(wsUserModel, sendSelf, contentReal, s,bson,session);
                } else if (bson.get("t").equals("rv_end")) {
                	this.sengMsg(wsUserModel, sendSelf, contentReal, s,bson,session);
                    //当前sheet的index值
                    String i = bson.get("i").toString();
                    String key = gridKey + wsUserModel.getWs().getId();
                    key = key + i;
                } else if (bson.get("t").equals("shareModeOperation")) {
                	//共享模式操作
                	bson.put("userId", wsUserModel.getUserId());
                	bson.put("userName", wsUserModel.getUserName());
                	this.sengMsg(wsUserModel, sendSelf, JSONObject.toJSONString(bson), s,bson,session);
                	String k = bson.get("k").toString();
                	if("exitShareMode".equals(k))
                	{
                		redisUtil.del(RedisPrefixEnum.COEDITSHAREMODE.getCode()+gridKey);
                	}else if("scroll".equals(k))
                	{//记录滚动条位置
                		Object redisShareModeInfo =  redisUtil.get(RedisPrefixEnum.COEDITSHAREMODE.getCode()+gridKey);
                		if(redisShareModeInfo != null)
                		{
                			ShareModeInfo shareModeInfo = JSON.parseObject(redisShareModeInfo.toString(), ShareModeInfo.class);
                			JSONObject v = bson.getJSONObject("v");
                			int scrollLeft = v.getIntValue("scrollLeft");
                			int scrollTop = v.getIntValue("scrollTop");
                			shareModeInfo.setScrollLeft(scrollLeft);
                			shareModeInfo.setScrollTop(scrollTop);
                			redisUtil.set(RedisPrefixEnum.COEDITSHAREMODE.getCode()+gridKey, JSON.toJSONString(shareModeInfo),0);
                		}
                	}else if("changeSheet".equals(k))
                	{//记录当前sheet
                		Object redisShareModeInfo =  redisUtil.get(RedisPrefixEnum.COEDITSHAREMODE.getCode()+gridKey);
                		if(redisShareModeInfo != null)
                		{
                			ShareModeInfo shareModeInfo = JSON.parseObject(redisShareModeInfo.toString(), ShareModeInfo.class);
                			String index = bson.getString("i");
                			shareModeInfo.setSheetIndex(index);
                			shareModeInfo.setRange(null);
                			redisUtil.set(RedisPrefixEnum.COEDITSHAREMODE.getCode()+gridKey, JSON.toJSONString(shareModeInfo),0);
                		}
                	}else if("mv".equals(k)) {
                		//记录选中位置
                		Object redisShareModeInfo =  redisUtil.get(RedisPrefixEnum.COEDITSHAREMODE.getCode()+gridKey);
                		if(redisShareModeInfo != null)
                		{
                			ShareModeInfo shareModeInfo = JSON.parseObject(redisShareModeInfo.toString(), ShareModeInfo.class);
                			JSONArray v = bson.getJSONArray("v");
                			if(!ListUtil.isEmpty(v))
                			{
                				JSONArray range = new JSONArray();
                				for (int i = 0; i < v.size(); i++) {
									JSONObject jsonObject = new JSONObject();
									jsonObject.put("row", v.getJSONObject(i).getJSONArray("row"));
									jsonObject.put("column", v.getJSONObject(i).getJSONArray("column"));
									range.add(jsonObject);
								}
                				shareModeInfo.setRange(range);
                				redisUtil.set(RedisPrefixEnum.COEDITSHAREMODE.getCode()+gridKey, JSON.toJSONString(shareModeInfo),0);
                			}
                		}
                	}
                }else if(bson.get("t").equals("reportTplTempCache")) {
                	String sheetIndex = bson.getString("i");
                	if(!redisUtil.hasKey(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+sheetIndex)) {
                		bson.put("v",new JSONObject());
                		bson.put("k", "sheetNotExist");
                		bson.put("t", "reportDesign");
                		Map infoMap = new HashMap<>();
                		infoMap.put("data", bson);
                		infoMap.put("createTime", DateUtil.getTimestampLong());
                		infoMap.put("returnMessage", "success");
                		infoMap.put("type", 2);
                		infoMap.put("status", 0);
                		sendMessageToUser(session, JSON.toJSONString(infoMap));
                	}else {
                		redisUtil.set(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+sheetIndex, JSON.toJSONString(bson.get("v")),Constants.REPORT_TPL_CACHE_TIME);
                	}
                }else if(bson.get("t").equals("frozenCancleTempCache")) {
                	String sheetIndex = bson.getString("i");
                	if(redisUtil.hasKey(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+sheetIndex)) {
                		redisUtil.set(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+sheetIndex, JSON.toJSONString(bson.get("v")),Constants.REPORT_TPL_CACHE_TIME);
                	}
                }else if(bson.get("t").equals("reportTplNewTempCache")) {
                	String sheetIndex = bson.getString("i");
                	redisUtil.set(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+sheetIndex, JSON.toJSONString(bson.get("v")),Constants.REPORT_TPL_CACHE_TIME);
                }else if(bson.get("t").equals("deleteSheet")) {
                	String sheetIndex = bson.getString("i");
                	redisUtil.del(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+sheetIndex);
                }else if(bson.get("t").equals("reportTplAttrTempCache")) {
                	String i = bson.getString("i");
                	String key = RedisPrefixEnum.REPORTTPLTEMPCACHE.getCode()+i;
                	Object redisCache = redisUtil.get(key);
                	if(redisCache != null)
                	{
                		ResSheetsSettingsDto settings = JSON.parseObject(redisCache.toString(), ResSheetsSettingsDto.class);
                		boolean paramMerge = bson.getBooleanValue("v");
                		settings.setIsParamMerge(paramMerge?1:2);
                		redisUtil.set(key, JSON.toJSONString(settings),Constants.REPORT_TPL_CACHE_TIME);
                	}
                }else if(bson.get("t").equals("reportDesign")) {
                	bson.put("userName", wsUserModel.getUserName());
                	this.sengMsg(wsUserModel, sendSelf, JSON.toJSONString(bson), s,bson,session);
                }else if(bson.get("t").equals("reportCellChanged")) {//修改单元格数据
                	String key = "";
                	JSONObject v = bson.getJSONObject("v");
                	String i = bson.getString("i");
                	boolean isPagination = v.getBooleanValue("isPagination");
                	int r = v.getIntValue("r");
                	int c = v.getIntValue("c");
                	if(isPagination)
                	{
                		int pageIndex = v.getIntValue("pageIndex");
                		int pageSize = v.getIntValue("pageSize");
                		key = RedisPrefixEnum.REPORTCELLDATATEMPCACHE.getCode()+i+"-"+pageIndex+"-"+pageSize+"-"+r+"-"+c;
                	}else {
                		key = RedisPrefixEnum.REPORTCELLDATATEMPCACHE.getCode()+i+"-"+r+"-"+c;
                	}
                	String value = JSON.toJSONString(v.getJSONObject("value"));
                	redisUtil.set(key, value,Constants.REPORT_TPL_CACHE_TIME);
                }else if (bson.get("t").equals("rv")) {
//                	if(reportType != 1)
//                	{
                	JSONObject customKey = new JSONObject();
                	customKey.put("ts", DateUtil.getTimeStamp());
            		bson.put("customKey", customKey);
                	this.sengMsg(wsUserModel, sendSelf, contentReal, s,bson,session);
//                	}
                	if(!isReport)
                	{
                		String key = bson.get("i").toString();
                    	RedisLock redisLock = new RedisLock(redisTemplate, key);
                    	try {
                    		if(redisLock.lock())
                    		{
                    			iCoeditService.getIndexRvForThread(gridKey, bson,key,wsUserModel);
                    		}
    					} catch (Exception e) {
    						e.printStackTrace();
    					}finally {
                            redisLock.unlock();
                        }
                	}
                } else if (bson.get("t").equals("isha")){
                	sendSelf = false;
                	//导入excel
                	this.sengMsg(wsUserModel, sendSelf, contentReal, s,bson,session);
                }else {
                    //其它操作
                	String key = "";
                	if(bson.get("i") != null)
                	{
                		key = bson.get("i").toString();
                	}else {
                		key = gridKey;
                	}
                    RedisLock redisLock = new RedisLock(redisTemplate, key);
                    try {
                        if (redisLock.lock()) {
                        	String _str = "";
                        	if(!isReport)
                        	{
                                 _str = this.iCoeditService.handleUpdate(gridKey, bson,wsUserModel);
                        	}
                        	String t = bson.getString("t");
                        	if("v".equals(t)) {
                        		JSONObject customKey = new JSONObject();
                        		customKey.put("ts", DateUtil.getTimeStamp());
                        		if(bson.getJSONObject("v") == null) {
                        			bson.put("v", new JSONObject());
                        		}
                        		bson.getJSONObject("v").put("customKey", customKey);
                        	}
                            this.sengMsg(wsUserModel, sendSelf, contentReal, s,bson,session);

                            if (_str.length() == 0) {

                            } else {
                                log.error("handleUpdate--error:{}" ,_str);
                                _b = false;
                            }
                        } else {
                            log.error("handleUpdate--:redisLock---lock");
                            _b = false;
                        }
                    } catch (Exception e) {
                        log.error("handleUpdate--:redisLock--error:{}",e);
                        _b = false;
                    } finally {
                        redisLock.unlock();
                    }
                }

            } else {
                _b = false;
            }

        }
    }
    
    public void sengMsg(WSUserModel wsUserModel,boolean sendSelf,String contentReal,boolean s,JSONObject bson,WebSocketSession session) {
    	try {
    		ObjectMapper obj = new ObjectMapper();
        	Map maps = new HashMap<>();
        	Map map = new HashMap<>();
        	if(!s)
        	{
        		 map.put("type", 3);
                 map.put("username", wsUserModel.getUserName());
                 map.put("id", "" + wsUserModel.getWs().getId());
        	}
        	 //表示发送其他共享编辑者收到更新信息（0:更新）
            String returnMessage = "success";
            map.put("status", "0");
            maps.put("status", "0");
            map.put("returnMessage", returnMessage);
            map.put("createTime", System.currentTimeMillis());

            //发送消息给本机其他用户
            if (s) {
                map.put("type", 2);
            }
            if(bson.getBooleanValue("isReport"))
            {
            	bson.remove("operate");
            }
            map.put("data", bson);
//            String param = JSON.toJSONString(map);
            //消息发送到redis
            sendMessageToUserByCurrent(wsUserModel, map,sendSelf);
            redisMessagePublish.publishMessage(new RedisMessageModel(ipAndPort, wsUserModel.getGridKey(), map,sendSelf));
            //只给发送此信息的用户发送信息
//            maps.put("returnMessage", returnMessage);
//            maps.put("createTime", System.currentTimeMillis());
//            maps.put("type", 1);
//            maps.put("data", bson);
//            String params = obj.writeValueAsString(maps);
//            sendMessageToUser(session, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }

    /**
     * 消息传输错误处理，如果出现错误直接断开连接
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("handleTransportError:{};exception:{}" ,session,exception);
        if (session.isOpen()) {
            session.close();
        }
        closeConn(session, true,null);
    }

    /**
     * 关闭连接后
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        closeConn(session, false,closeStatus.getReason());
    }
    
    private void openConn(WebSocketSession session) {
        //创建一个连接窗口，并加入的队列中
        WSUserModel ws = new WSUserModel(session);
        WSUserModel.webSocketMapAdd(USER_SOCKET_SESSION_MAP, ws);
        try {
            Map map = new HashMap<>();
            map.put("message", "连接成功");
            map.put("status", "0");
            map.put("type", "0");
            ObjectMapper obj = new ObjectMapper();
            String params = obj.writeValueAsString(map);
            sendMessageToUser(session, params);
			Object onlineUsers = redisUtil.get(RedisPrefixEnum.COEDITUSERS.getCode()+ws.getGridKey());
            LinkedHashMap<String, JSONObject> userMap = null; 
            if(onlineUsers == null)
            {
            	userMap = new LinkedHashMap<>(); 
            }else {
            	userMap = JSON.parseObject(onlineUsers.toString(), LinkedHashMap.class);
            }
            if(!userMap.containsKey(ws.getUserId().toString()))
            {
            	JSONObject userInfo = new JSONObject();
            	userInfo.put("userId", ws.getUserId());
            	userInfo.put("userName", ws.getUserName());
            	userInfo.put("color", Constants.COLORS[RandomUtil.randomInt(39)]);
            	userMap.put(ws.getUserId().toString(), userInfo);
            	redisUtil.set(RedisPrefixEnum.COEDITUSERS.getCode()+ws.getGridKey(),JSON.toJSONString(userMap));
            	//更新文档用户信息
            	JSONArray userArray = convertMapToJsonArray(userMap);
            	Map<String, Object> userInfoMap = new HashMap<>();
    			Map<String, Object> data = new HashMap<String, Object>();
    			data.put("t", "notifications");
    			data.put("k", "userChanged");
    			data.put("v", userArray);
    			userInfoMap.put("data", data);
    			userInfoMap.put("createTime", DateUtil.getTimestampLong());
    			userInfoMap.put("returnMessage", "success");
    			userInfoMap.put("type", 2);
    			userInfoMap.put("status", 0);
            	redisMessagePublish.publishMessage(new RedisMessageModel(ipAndPort, ws.getGridKey(), userInfoMap,true));
            }
		
        } catch (Exception e) {
            log.error("openConn--Exception:" + e);
        }
    }
    
    private void closeConn(WebSocketSession session, boolean isError,String reason) {
        WSUserModel wsUserModel = new WSUserModel(session);
        WSUserModel.webSocketMapRemove(USER_SOCKET_SESSION_MAP, wsUserModel);
        if (isError) {
            log.error("窗口关闭(Error):{},当前在线人数为{}" ,wsUserModel.getId());
            if(StringUtil.isNullOrEmpty(reason) || (!reason.toLowerCase().contains(Constants.WEBSOCKET_SESSIONOUT_REASON) && !reason.toLowerCase().contains(Constants.WEBSOCKET_SESSIONOUT_REASON2)))
            {
            	String key = RedisPrefixEnum.COEDITUSERS.getCode()+wsUserModel.getGridKey();
    			Object onlineUsers = redisUtil.get(RedisPrefixEnum.COEDITUSERS.getCode()+wsUserModel.getGridKey());
                LinkedHashMap<String, JSONObject> userMap = null; 
                if(onlineUsers != null && !"{}".equals(onlineUsers.toString()))
                {
                	userMap = JSON.parseObject(onlineUsers.toString(), LinkedHashMap.class);
                	if(userMap.containsKey(wsUserModel.getUserId().toString()))
                    {
                		userMap.remove(wsUserModel.getUserId().toString());
                		if(StringUtil.isEmptyMap(userMap))
                		{
                			redisUtil.del(RedisPrefixEnum.COEDITUSERS.getCode()+wsUserModel.getGridKey());
                			List<String> keys = redisUtil.getKeys(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+wsUserModel.getGridKey());
                        	redisUtil.del(keys);
                        	keys = redisUtil.getKeys(RedisPrefixEnum.REPORTTPLTEMPCACHE.getCode()+wsUserModel.getGridKey());
                        	redisUtil.del(keys);
                        	if(wsUserModel.getGridKey().contains("reportFormsMode-")) {
                        		String keyPattern = RedisPrefixEnum.REPORTCELLDATATEMPCACHE.getCode()+wsUserModel.getGridKey().split("-")[1];
                        		keys = redisUtil.getKeys(keyPattern);
                        		if(!ListUtil.isEmpty(keys))
                        		{
                        			redisUtil.del(keys);
                        		}
                        	}
                		}else {
                			redisUtil.set(RedisPrefixEnum.COEDITUSERS.getCode()+wsUserModel.getGridKey(),JSON.toJSONString(userMap));
                		}
            			//更新文档用户信息
            			JSONArray userArray = convertMapToJsonArray(userMap);
                    	Map<String, Object> userInfoMap = new HashMap<>();
            			Map<String, Object> data = new HashMap<String, Object>();
            			data.put("t", "notifications");
            			data.put("k", "userChanged");
            			data.put("v", userArray);
            			userInfoMap.put("data", data);
            			userInfoMap.put("createTime", DateUtil.getTimestampLong());
            			userInfoMap.put("returnMessage", "success");
            			userInfoMap.put("type", 2);
            			userInfoMap.put("status", 0);
                    	redisMessagePublish.publishMessage(new RedisMessageModel(ipAndPort, wsUserModel.getGridKey(), userInfoMap,true));
                    }
                }else {
                	redisUtil.del(key);
                	List<String> keys = redisUtil.getKeys(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+wsUserModel.getGridKey());
                	redisUtil.del(keys);
                	if(wsUserModel.getGridKey().contains("reportFormsMode-")) {
                		String keyPattern = RedisPrefixEnum.REPORTCELLDATATEMPCACHE.getCode()+wsUserModel.getGridKey().split("-")[1];
                		keys = redisUtil.getKeys(keyPattern);
                		if(!ListUtil.isEmpty(keys))
                		{
                			redisUtil.del(keys);
                		}
                	}
                }
            }
        } else {
            log.error("窗口关闭:{},当前在线人数为:{}",wsUserModel.getId());
            try{
                Map map = new HashMap<>(2);
                map.put("message", "用户退出");
                map.put("type", 999);
                map.put("username", wsUserModel.getUserName());
                map.put("id", "" + wsUserModel.getWs().getId());
                //消息发送到redis
                redisMessagePublish.publishMessage(new RedisMessageModel(ipAndPort, wsUserModel.getGridKey(), map,false));
                sendMessageToUserByCurrent(wsUserModel, map,false);
                if(StringUtil.isNullOrEmpty(reason) || (!reason.toLowerCase().contains(Constants.WEBSOCKET_SESSIONOUT_REASON) && !reason.toLowerCase().contains(Constants.WEBSOCKET_SESSIONOUT_REASON2)))
                {
                	String key = RedisPrefixEnum.COEDITUSERS.getCode()+wsUserModel.getGridKey();
        			Object onlineUsers = redisUtil.get(RedisPrefixEnum.COEDITUSERS.getCode()+wsUserModel.getGridKey());
                    LinkedHashMap<String, JSONObject> userMap = null; 
                    if(onlineUsers != null && !"{}".equals(onlineUsers.toString()))
                    {
                    	userMap = JSON.parseObject(onlineUsers.toString(), LinkedHashMap.class);
                    	if(userMap.containsKey(wsUserModel.getUserId().toString()))
                        {
                    		userMap.remove(wsUserModel.getUserId().toString());
                    		if(StringUtil.isEmptyMap(userMap))
                    		{
                    			redisUtil.del(RedisPrefixEnum.COEDITUSERS.getCode()+wsUserModel.getGridKey());
                    			List<String> keys = redisUtil.getKeys(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+wsUserModel.getGridKey());
                            	redisUtil.del(keys);
                            	keys = redisUtil.getKeys(RedisPrefixEnum.REPORTTPLTEMPCACHE.getCode()+wsUserModel.getGridKey());
                            	redisUtil.del(keys);
                            	if(wsUserModel.getGridKey().contains("reportFormsMode-")) {
                            		String keyPattern = RedisPrefixEnum.REPORTCELLDATATEMPCACHE.getCode()+wsUserModel.getGridKey().split("-")[1];
                            		keys = redisUtil.getKeys(keyPattern);
                            		if(!ListUtil.isEmpty(keys))
                            		{
                            			redisUtil.del(keys);
                            		}
                            	}
                    		}else {
                    			redisUtil.set(RedisPrefixEnum.COEDITUSERS.getCode()+wsUserModel.getGridKey(),JSON.toJSONString(userMap));
                    		}
                			//更新文档用户信息
                			JSONArray userArray = convertMapToJsonArray(userMap);
                        	Map<String, Object> userInfoMap = new HashMap<>();
                			Map<String, Object> data = new HashMap<String, Object>();
                			data.put("t", "notifications");
                			data.put("k", "userChanged");
                			data.put("v", userArray);
                			userInfoMap.put("data", data);
                			userInfoMap.put("createTime", DateUtil.getTimestampLong());
                			userInfoMap.put("returnMessage", "success");
                			userInfoMap.put("type", 2);
                			userInfoMap.put("status", 0);
                        	redisMessagePublish.publishMessage(new RedisMessageModel(ipAndPort, wsUserModel.getGridKey(), userInfoMap,true));
                        }
                    }else {
                    	redisUtil.del(key);
                    	List<String> keys = redisUtil.getKeys(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+wsUserModel.getGridKey());
                    	redisUtil.del(keys);
                    	if(wsUserModel.getGridKey().contains("reportFormsMode-")) {
                    		String keyPattern = RedisPrefixEnum.REPORTCELLDATATEMPCACHE.getCode()+wsUserModel.getGridKey().split("-")[1];
                    		keys = redisUtil.getKeys(keyPattern);
                    		if(!ListUtil.isEmpty(keys))
                    		{
                    			redisUtil.del(keys);
                    		}
                    	}
                    }
                }
            }catch (Exception ex){
                log.error("用户下线群发失败:{}",ex);
            }
        }

    }
    
    /**
     * 群发消息（本节点，其他窗口）
     *
     * @param _wsUserModel 消息提供者
     * @param _content     内容
     * @param sendSelf     是否发送给自己
     */
    public void sendMessageToUserByCurrent(WSUserModel _wsUserModel, Object _content,boolean sendSelf) {
        if (USER_SOCKET_SESSION_MAP != null && _wsUserModel != null && _content != null) {
            if (USER_SOCKET_SESSION_MAP.containsKey(_wsUserModel.getGridKey())) {
                //必须是同一个文档
                Hashtable<String, WSUserModel> _userMap = USER_SOCKET_SESSION_MAP.get(_wsUserModel.getGridKey());
                sendMessageToUser(_userMap, _content, _wsUserModel.getWs().getId(),sendSelf);
            }
        }
    }
    
    /**
     * 给同一个文档的全部用户发消息（除了提供消息的用户）
     *
     * @param _userMap 用户组
     * @param _content 内容
     * @param _uid     数据提供的用户sessionid（redis订阅设定为null）
     */
    private static void sendMessageToUser(Hashtable<String, WSUserModel> _userMap, Object _content, String _uid,boolean sendSelf) {
        if (_userMap != null && _content != null) {
            TextMessage message = new TextMessage(JSON.toJSONString(_content));
            //BinaryMessage message= new BinaryMessage(_content.getBytes());
            _userMap.forEach((k, v) -> {
                if (null == _uid || !k.equals(_uid) || sendSelf) {
                    //给（非消息提供者）打开改文档的用户发消息
                    sendMessageToUser(v.getWs(), message);
                }
            });
        }
    }
    
    /**
     * 给单个用户发消息
     *
     * @param session
     * @param message
     */
    private static void sendMessageToUser(WebSocketSession session, WebSocketMessage message) {
        try {
            log.error("sendMessageToUser--WebSocketSession");
            synchronized (session) {
                session.sendMessage(message);
            }
        } catch (Exception ex) {
            log.error(ex.toString() + ";WebSocketSession:" + session + "message" + message);
        }
    }

    private static void sendMessageToUser(WebSocketSession session, String message) {

        log.error("sendMessageToUser--onlyForUser");
        sendMessageToUser(session, new TextMessage(message));
    }
    
    /**
     * 群发消息（redis消息订阅）
     **/
    public static void sendMessageToUserByRedis(RedisMessageModel model) {
        sendMessageToUserByRedis(model.getIpandport(), model.getGridkey(), model.getContent(),model.isSendSelf());
    }

    /**
     * 群发消息（redis消息订阅）
     *
     * @param _ipAndPort 提供数据的节点
     * @param _gridkey   文档id
     * @param _content   内容
     */
    private static void sendMessageToUserByRedis(String _ipAndPort, String _gridkey, Object _content,boolean sendSelf) {
        if (USER_SOCKET_SESSION_MAP != null && _ipAndPort != null && _gridkey != null && _content != null) {
            if (!_ipAndPort.equals(ipAndPort) || sendSelf) {
                //不是本机的
                if (USER_SOCKET_SESSION_MAP.containsKey(_gridkey)) {
                    //必须是同一个文档
                    Hashtable<String, WSUserModel> _userMap = USER_SOCKET_SESSION_MAP.get(_gridkey);
                    sendMessageToUser(_userMap, _content, null,sendSelf);
                }
            }
        }
    }
    
    private static JSONArray convertMapToJsonArray(Map<String, JSONObject> map) {
    	JSONArray result = new JSONArray();
    	if(!StringUtil.isEmptyMap(map))
    	{
    		for(Map.Entry entry : map.entrySet()){
        		result.add(entry.getValue());
        	}
    	}
    	return result;
    }
}
