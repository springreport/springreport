//package com.springreport.impl.codeit;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.springreport.api.luckysheethis.ILuckysheetHisService;
//import com.springreport.constants.Constants;
//import com.springreport.dto.coedit.MqMsgDto;
//import com.springreport.entity.luckysheet.Luckysheet;
//import com.springreport.enums.DelFlagEnum;
//import com.springreport.enums.MqTypeEnums;
//import com.springreport.enums.RedisPrefixEnum;
//import com.springreport.enums.SystemDatasourceTypeEnum;
//import com.springreport.mapper.luckysheet.LuckysheetMapper;
//import com.springreport.mq.MqProducer;
//import com.springreport.util.DatasourceTypeUtil;
//import com.springreport.util.JfGridFileUtil;
//import com.springreport.util.ListUtil;
//import com.springreport.util.Pako_GzipUtils;
//import com.springreport.util.RedisUtil;
//import com.springreport.util.StringUtil;
//import com.springreport.websocket.WSUserModel;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class RecordDataUpdataUpdateService{
//
//	@Autowired
//	private LuckysheetMapper luckysheetMapper;
//	
//	@Autowired
//	private ILuckysheetHisService iLuckysheetHisService;
//	
//	@Autowired
//	private MqProcessService asyncProcessService;
//	
//	@Autowired
//	private MqProducer mqProducer;
//	
//	@Autowired
//	private RedisUtil redisUtil;
//	
//	/**  
//	 * @MethodName: updateCellDataListValue
//	 * @Description: 更新jsonb中某条文本数据
//	 * @author caiyang
//	 * @param query
//	 * @param keyName
//	 * @param position
//	 * @param v
//	 * @return boolean
//	 * @date 2023-08-13 08:26:42 
//	 */ 
//	public void updateCellDataListValue(JSONObject conditions, String keyName, String position, Object v,
//			JSONObject _dbObject, WSUserModel wsUserModel, String index, String listId,String blockId,String operate) {
//		this.asyncProcessService.updateRedisJsonData(keyName, v, index, listId, blockId);
//		try {
//			String hisKeyName = StringUtil.positionHandle2(keyName);
//			keyName=StringUtil.positionHandle(keyName);
//			if(position!=null){
//	            keyName=keyName+"["+position+"]";
//	        }
//			conditions.put("keyName", keyName);
//			if(v instanceof JSON){
//				
//				conditions.put("isJson", "yes");
//				conditions.put("v", ((JSON) v).toJSONString());
//				
//			}else {
//				conditions.put("isJson", "no");
//				conditions.put("v", v);
//			}
//			String type = DatasourceTypeUtil.getDatasourceType();
//			MqMsgDto mqMsgDto = new MqMsgDto();
//			List<String> sqls = new ArrayList<>();
//			if(SystemDatasourceTypeEnum.ORACLE.getCode().equals(type))
//			{
//				String sql = "";
//				String sqlNull = "";
//	        	if(!keyName.contains("."))
//	        	{
//	        		if("config".equals(keyName))
//	        		{
//	        			sql = "json_data = JSON_MERGEPATCH(json_data,'{"+"\""+keyName+"\""+":' ||JSON_QUERY('"+JSON.toJSONString(v)+"','$') || '}')";
//		        		sql = sql.replace("{}", "null");
//	        		}else {
//	        			if("{}".equals(JSON.toJSONString(v)))
//	        			{
//	        				sql = "json_data = JSON_MERGEPATCH(json_data,'{"+"\""+keyName+"\""+":null}')";
//	        			}else {
//	        				if(v instanceof JSON)
//	        				{
//	        					sql = "json_data = JSON_MERGEPATCH(json_data,'{"+"\""+keyName+"\""+":' ||JSON_QUERY('"+JSON.toJSONString(v)+"','$') || '}')";
//				        		sql = sql.replaceAll("\"\\{\\}\"", "null");	
//	        				}else {
//	        					sql = "json_data = JSON_MERGEPATCH(json_data,'{"+"\""+keyName+"\""+":"+ v + "}')";
//	        				}
//			        		sqlNull = "json_data = JSON_MERGEPATCH(json_data,'{"+"\""+keyName+"\""+":null}')";
//	        			}
//	        		}
//	        	}else {
//	        		String[] split = keyName.split("\\.");
//		        	StringBuilder sb=new StringBuilder(20);
//		        	sb.append("json_data = JSON_MERGEPATCH(json_data,'{");
//		        	for (int i = 0; i < 2; i++) {
//		        		if(i == 0)
//		        		{
//		        			sb.append("\"").append(split[i]).append("\"").append(":{");
//		        		}else {
//		        			sb.append("\"").append(split[i]).append("\"").append(":' || JSON_QUERY('").append(JSON.toJSONString(v)).append("','$') || '}}')");
//		        		}
//					}
//		        	sql = sb.toString();
//	        	}
//	        	if(StringUtil.isNotEmpty(sqlNull))
//	        	{
//	        		conditions.put("sql", sqlNull);
//	        		sqls.add(sqlNull);
////		        	this.luckysheetMapper.dynamicUpdateJsonbData(conditions);
//	        	}
//	        	conditions.put("sql", sql);
//	        	sqls.add(sql);
////	        	this.luckysheetMapper.dynamicUpdateJsonbData(conditions);
//	        	String params = JSON.toJSONString(conditions);
//	        	String sqlStrings = JSON.toJSONString(sqls);
//	    		String comperssParams = Pako_GzipUtils.compress(params);
//	    		String compressSqls = Pako_GzipUtils.compress(sqlStrings);
//	    		mqMsgDto.setType(MqTypeEnums.UPDATECONFIG.getCode());
//	    		mqMsgDto.setParams(comperssParams);
//	    		mqMsgDto.setSqls(compressSqls);
//	    		mqMsgDto.setMethod("dynamicUpdateJsonbData");
//			}else {
////				this.luckysheetMapper.updateJsonData(conditions);
//				String params = JSON.toJSONString(conditions);
//	    		String comperssParams = Pako_GzipUtils.compress(params);
//	    		mqMsgDto.setType(MqTypeEnums.UPDATECONFIG.getCode());
//	    		mqMsgDto.setParams(comperssParams);
//	    		mqMsgDto.setMethod("updateJsonData");
//			}
//			String msg = JSON.toJSONString(mqMsgDto);
//    		this.mqProducer.sendAsyncMsg(Constants.MQ_TPOIC_LUCKYSHEET, msg);
//			if(v instanceof JSON && StringUtil.isNotEmpty(operate)){
//				this.iLuckysheetHisService.saveSheetSettings(_dbObject,v,hisKeyName,wsUserModel,index,listId,operate);
//			}
//		} catch (Exception e) {
//			log.error(e.getMessage());
//		}
//	}
//	
//	/**  
//	 * @MethodName: updateJsonbForElementInsert
//	 * @Description: jsonb数据中元素添加元素
//	 * @author caiyang
//	 * @param query
//	 * @param word
//	 * @param db
//	 * @param position
//	 * @return boolean
//	 * @date 2023-08-15 08:30:23 
//	 */ 
//	public boolean updateJsonbForElementInsert(JSONObject query, String word, JSONObject db, Integer position,
//			JSONObject _dbObject, WSUserModel wsUserModel, String index, String listId,String operate,String blockId) {
//		try {
//			String type = DatasourceTypeUtil.getDatasourceType();
//			if(position!=null){
//				if(SystemDatasourceTypeEnum.MYSQL.getCode().equals(type))
//				{
//					word=word+"["+position+"]";
//				}else {
//					word=word+","+position;
//				}
//	        }
//			word = StringUtil.positionHandle(word);
////			db.put("position", position);
//			this.asyncProcessService.updateRedisJsonData("calcChain", db, index, listId, blockId);
//			query.put("keyWord", "calcChain");
//			query.put("v", db.toString(SerializerFeature.WriteMapNullValue));
//			MqMsgDto mqMsgDto = new MqMsgDto();
//			mqMsgDto.setType(MqTypeEnums.UPDATECONFIG.getCode());
//			String params = JSON.toJSONString(query);
//			String comperssParams = Pako_GzipUtils.compress(params);
//			mqMsgDto.setParams(comperssParams);
//			mqMsgDto.setMethod("updateArrayInsert");
//			String msg = JSON.toJSONString(mqMsgDto);
//    		this.mqProducer.sendAsyncMsg(Constants.MQ_TPOIC_LUCKYSHEET, msg);
////			this.luckysheetMapper.updateJsonbForElementInsert(query);
//			return true;
//		} catch (Exception e) {
//			log.error(e.getMessage());
//            return false;
//		}
//		
//	}
//	
//	/**  
//	 * @MethodName: rmJsonbDataForEmpty
//	 * @Description: 更新 ,将key设置NULL
//	 * @author caiyang
//	 * @param query
//	 * @param word
//	 * @return boolean
//	 * @date 2023-08-15 03:30:27 
//	 */ 
//	public boolean rmJsonbDataForEmpty(JSONObject query, String word) {
//		try {
//			this.asyncProcessService.updateRedisJsonData("fsc", null, query.getString("index"), query.getString("list_id"), query.getString("block_id"));
//			JSONObject wordJson=JSONObject.parseObject("{"+word+"}");
//			Map<String, Object> queryDB = wordJson.getInnerMap();
//			StringBuilder sb=new StringBuilder(20);;
//			String type = DatasourceTypeUtil.getDatasourceType();
//			MqMsgDto mqMsgDto = new MqMsgDto();
//			List<String> sqls = new ArrayList<>();
//			mqMsgDto.setMethod("dynamicUpdateJsonbData");
//			mqMsgDto.setType(MqTypeEnums.UPDATECONFIG.getCode());
//			if(SystemDatasourceTypeEnum.DM.getCode().equals(type))
//			{
//				for (String key : queryDB.keySet()) {
//					sb=new StringBuilder(20);
//					sb.append("json_data=jsonb_set(json_data,'{").append(key).append("}',").append("'null'::jsonb,true)");
////					query.put("sql", sb.toString());
//					sqls.add(sb.toString());
////				    this.luckysheetMapper.dynamicUpdateJsonbData(query);
//				}
//			}else if(SystemDatasourceTypeEnum.ORACLE.getCode().equals(type))
//			{
//				for (String key : queryDB.keySet()) {
//					sb=new StringBuilder(20);
//					sb.append("json_data = JSON_MERGEPATCH(json_data,'{").append("\"").append(key).append("\":null}')");
////					query.put("sql", sb.toString());
//					sqls.add(sb.toString());
////				    this.luckysheetMapper.dynamicUpdateJsonbData(query);
//				}
//			}else if(SystemDatasourceTypeEnum.SQLSERVER.getCode().equals(type))
//			{
//				for (String key : queryDB.keySet()) {
//					sb=new StringBuilder(20);
//					sb.append("json_data=JSON_MODIFY(json_data,'$.").append(key).append("',").append("null)");
////					query.put("sql", sb.toString());
//					sqls.add(sb.toString());
////				    this.luckysheetMapper.dynamicUpdateJsonbData(query);
//				}
//			}else {
//				for (String key : queryDB.keySet()) {
//		            if(SystemDatasourceTypeEnum.MYSQL.getCode().equals(type))
//		            {
//		            	if(sb.toString().length()>0){
//			                sb.append(",");
//			            }
//		            	sb.append("json_data=json_set(json_data,\"$."+key+"\",null)");	
//		            }else if(SystemDatasourceTypeEnum.POSTGRESQL.getCode().equals(type) || SystemDatasourceTypeEnum.KINGBASE.getCode().equals(type)
//		            		|| SystemDatasourceTypeEnum.DM.getCode().equals(type)) {
//		            	if(sb.toString().length()>0)
//		            	{
//		            		sb.append(" || ").append("jsonb_set(json_data,'{").append(key).append("}',").append("'null'::jsonb,true)");
//		            	}else {
//		            		sb.append("json_data=jsonb_set(json_data,'{").append(key).append("}',").append("'null'::jsonb,true)");	
//		            	}
//		            	
//		            }
//		            
//		        }
////		        query.put("sql", sb.toString());
////		        this.luckysheetMapper.dynamicUpdateJsonbData(query);
//		        sqls.add(sb.toString());
//			}
//			String params = JSON.toJSONString(query);
//        	String sqlStrings = JSON.toJSONString(sqls);
//        	String comperssParams = Pako_GzipUtils.compress(params);
//    		String compressSqls = Pako_GzipUtils.compress(sqlStrings);
//    		mqMsgDto.setParams(comperssParams);
//    		mqMsgDto.setSqls(compressSqls);
//    		String msg = JSON.toJSONString(mqMsgDto);
// 	        this.mqProducer.sendAsyncMsg(Constants.MQ_TPOIC_LUCKYSHEET, msg);
//	        return true;
//		} catch (Exception e) {
//			log.error(e.getMessage());
//            return false;
//		}
//		
//	}
//	
//	 /**  
//	 * @MethodName: updateJsonbForSetRootNull
//	 * @Description: jsonb数据中元素添加元素(根节点)
//	 * @author caiyang
//	 * @param query
//	 * @param word
//	 * @param db
//	 * @param position
//	 * @param words
//	 * @return boolean
//	 * @date 2023-08-15 10:13:45 
//	 */ 
//	public boolean updateJsonbForSetRootNull(JSONObject query, String word, Object db, Integer position, String words,
//			JSONObject _dbObject, WSUserModel wsUserModel, String index, String listId,String operate,String blockId) {
//		return updateJsonbForInsertNull(query, word, db, position, words,_dbObject,wsUserModel,index,listId,operate,blockId); 
//	}
//	
//	/**  
//	 * @MethodName: updateJsonbForInsertNull
//	 * @Description: jsonb数据中元素添加元素（集合插入）
//	 * @author caiyang
//	 * @param query
//	 * @param word
//	 * @param db
//	 * @param position
//	 * @param words
//	 * @return boolean
//	 * @date 2023-08-15 10:14:09 
//	 */ 
//	public boolean updateJsonbForInsertNull(JSONObject query, String word, Object db, Integer position, String words,
//			JSONObject _dbObject, WSUserModel wsUserModel, String index, String listId,String operate,String blockId) {
//		try {
//			String hisWord = StringUtil.positionHandle2(word);
//			if(word!=null){
//				word=StringUtil.positionHandle(word);
//	        }
//			JSONObject newObj=JSONObject.parseObject("{"+words+"}");
//	        String key=JfGridFileUtil.getKeyName(newObj);
//	        query.put("keyName", key);
//	        key = StringUtil.positionHandle(key);
//	        query.put("key", key);
//	        query.put("v", newObj.getString(query.getString("keyName")));
//	        MqMsgDto mqMsgDto = new MqMsgDto();
//	        List<String> sqls = new ArrayList<>();
//			mqMsgDto.setType(MqTypeEnums.UPDATECONFIG.getCode());
//	        if(position!=null){
//	            word=word+"."+position;
//	        }
//	        query.put("word", word);
//	        query.put("db", JSON.toJSONString(db));
//	        this.asyncProcessService.updateRedisJsonData(word, db, index, listId, blockId);
//	        String type = DatasourceTypeUtil.getDatasourceType();
//	        if(SystemDatasourceTypeEnum.ORACLE.getCode().equals(type))
//	        {
//	        	String sql = "";
//	        	if(!word.contains("."))
//	        	{
//	        		sql = "json_data = JSON_MERGEPATCH(json_data,'{"+"\""+word+"\""+":' ||JSON_QUERY('"+JSON.toJSONString(db)+"','$') || '}'";
//	        	}else {
//	        		String[] split = word.split("\\.");
//		        	StringBuilder sb=new StringBuilder(20);
//		        	sb.append("json_data = JSON_MERGEPATCH(json_data,'{");
//		        	for (int i = 0; i < 2; i++) {
//		        		if(i == 0)
//		        		{
//		        			sb.append("\"").append(split[i]).append("\"").append(":{");
//		        		}else {
//		        			sb.append("\"").append(split[i]).append("\"").append(":' || JSON_QUERY('").append(JSON.toJSONString(db)).append("','$') || '}}')");
//		        		}
//					}
//		        	sql = sb.toString();
//	        	}
//	        	query.put("sql", sql);
////	        	this.luckysheetMapper.dynamicUpdateJsonbData(query);
//	        	sqls.add(sql);
//	        	String params = JSON.toJSONString(query);
//	        	String sqlStrings = JSON.toJSONString(sqls);
//	        	String comperssParams = Pako_GzipUtils.compress(params);
//	    		String compressSqls = Pako_GzipUtils.compress(sqlStrings);
//	    		mqMsgDto.setType(MqTypeEnums.UPDATECONFIG.getCode());
//	    		mqMsgDto.setParams(comperssParams);
//	    		mqMsgDto.setSqls(compressSqls);
//	    		mqMsgDto.setMethod("dynamicUpdateJsonbData");
//	        }else {
//	        	String params = JSON.toJSONString(query);
//	        	String comperssParams = Pako_GzipUtils.compress(params);
//	        	mqMsgDto.setType(MqTypeEnums.UPDATECONFIG.getCode());
//	        	mqMsgDto.setMethod("updateConfigAttr");
//	        	mqMsgDto.setParams(comperssParams);
////	        	this.luckysheetMapper.updateConfigAttr(query);
//	        }
//	        String msg = JSON.toJSONString(mqMsgDto);
//	        this.mqProducer.sendAsyncMsg(Constants.MQ_TPOIC_LUCKYSHEET, msg);
//	        if(db instanceof JSON && StringUtil.isNotEmpty(operate)){
//				this.iLuckysheetHisService.saveSheetSettings(_dbObject,db,hisWord,wsUserModel,index,listId,operate);
//			}
//	        return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}
//	
//	/**  
//	 * @MethodName: updateCellDataListTxtValue
//	 * @Description: 更新jsonb中某条文本数据
//	 * @author caiyang
//	 * @param query
//	 * @param keyName
//	 * @param position
//	 * @param v
//	 * @return boolean
//	 * @date 2023-08-15 02:20:41 
//	 */ 
//	public boolean updateCellDataListTxtValue(JSONObject query, String keyName, Integer position, Object v
//			,JSONObject _dbObject, WSUserModel wsUserModel,String index, String listId,String blockId, String operate) {
//		this.asyncProcessService.updateRedisJsonData(keyName, v, index, listId, blockId);
//		this.asyncProcessService.updateCellDataListTxtValue(query, keyName, position, v, _dbObject, wsUserModel, index, listId, operate);
//		return true;
//	}
//	
//	/**  
//	 * @MethodName: updateJsonbForSetNull
//	 * @Description: jsonb数据中元素添加元素（集合插入）,不存在创建一个空集合
//	 * @author caiyang
//	 * @param query
//	 * @param word
//	 * @param db
//	 * @param position
//	 * @return boolean
//	 * @date 2023-08-15 03:05:41 
//	 */ 
//	public boolean updateJsonbForSetNull(JSONObject query, String word, JSONObject db, Integer position, String index, String listId,String blockId) {
//		try {
//			this.asyncProcessService.updateRedisJsonData("calcChain", db, index, listId, blockId);
//			query.put("key", word);
//			query.put("v", "[]");
////			this.luckysheetMapper.createConfigAttr(query);
//			if(position!=null){
//	            word=word+","+position;
//	        }
//			word = StringUtil.positionHandle(word);
//			query.put("word", word);
//			query.put("db", db.toString(SerializerFeature.WriteMapNullValue));
////			this.luckysheetMapper.updateConfigAttr(query);
//			MqMsgDto mqMsgDto = new MqMsgDto();
//			mqMsgDto.setType(MqTypeEnums.UPDATECONFIG.getCode());
//			String params = JSON.toJSONString(query);
//			String comperssParams = Pako_GzipUtils.compress(params);
//			mqMsgDto.setParams(comperssParams);
//    		mqMsgDto.setMethod("updateJsonbForSetNull");
//    		String msg = JSON.toJSONString(mqMsgDto);
//    		this.mqProducer.sendAsyncMsg(Constants.MQ_TPOIC_LUCKYSHEET, msg);
//			return true;
//		} catch (Exception e) {
//			log.error(e.getMessage());
//			return false;
//		}
//	}
//	
//	/**  
//	 * @MethodName: updateJsonbDataForKeys
//	 * @Description: 更新 ,按key设置值
//	 * @author caiyang
//	 * @param query
//	 * @param word
//	 * @return boolean
//	 * @date 2023-08-15 05:46:08 
//	 */ 
//	public boolean updateJsonbDataForKeys(JSONObject query, JSONObject word) {
//		try {
//			String type = DatasourceTypeUtil.getDatasourceType();
//			StringBuilder sb=new StringBuilder(20);
//	        Map<String, Object> queryDB = word.getInnerMap();
//	        if(SystemDatasourceTypeEnum.MYSQL.getCode().equals(type))
//	        {
//	        	for (String key : queryDB.keySet()) {
//		            if(sb.toString().length()>0){
//		                sb.append(",");
//		            }
//		            sb.append("json_data=json_set(json_data,\"$."+key+"\",CAST(\'"+queryDB.get(key)+"\' AS JSON))");
//		        }
//	        	query.put("sql", sb.toString());
//	 	        this.luckysheetMapper.dynamicUpdateJsonbData(query);
//	        } else if(SystemDatasourceTypeEnum.DM.getCode().equals(type)) {
//	        	for (String key : queryDB.keySet()) {
//					sb=new StringBuilder(20);
//					sb.append("json_data=jsonb_set(json_data,'{").append(key).append("}',").append("'").append(queryDB.get(key)).append("'::jsonb,true)");
//					query.put("sql", sb.toString());
//				    this.luckysheetMapper.dynamicUpdateJsonbData(query);
//				}
//	        }else if(SystemDatasourceTypeEnum.ORACLE.getCode().equals(type)) {
//	        	for (String key : queryDB.keySet()) {
//					sb=new StringBuilder(20);
//					sb.append("json_data=JSON_MERGEPATCH(json_data,'{").append("\"").append(key).append("\":").append(queryDB.get(key)).append("}')");
//					query.put("sql", sb.toString());
//				    this.luckysheetMapper.dynamicUpdateJsonbData(query);
//				}
//	        }else if(SystemDatasourceTypeEnum.SQLSERVER.getCode().equals(type)) {
//	        	for (String key : queryDB.keySet()) {
//					sb=new StringBuilder(20);
//					sb.append("json_data=JSON_MODIFY(json_data,'$.").append(key).append("',").append("JSON_QUERY(").append(queryDB.get(key)).append("))");
//					query.put("sql", sb.toString());
//				    this.luckysheetMapper.dynamicUpdateJsonbData(query);
//				}
//	        }else if(SystemDatasourceTypeEnum.POSTGRESQL.getCode().equals(type) || SystemDatasourceTypeEnum.KINGBASE.getCode().equals(type)) {
//	        	for (String key : queryDB.keySet()) {
//	        		if(sb.toString().length()>0)
//	            	{
//	            		sb.append(" || ").append("jsonb_set(json_data,'{").append(key).append("}',").append("'").append(queryDB.get(key)).append("'::jsonb,true)");	
//	            	}else {
//	            		sb.append("json_data=jsonb_set(json_data,'{").append(key).append("}',").append("'").append(queryDB.get(key)).append("'::jsonb,true)");	
//	            	}
//		        }
//	        	query.put("sql", sb.toString());
//			    this.luckysheetMapper.dynamicUpdateJsonbData(query);
//	        }
//	        return true;
//		} catch (Exception e) {
//			log.error(e.getMessage());
//            return false;
//		}
//		 
//	}
//	
//	/**  
//	 * @MethodName: updateDataMsgHide
//	 * @Description: 更新sheet隐藏状态
//	 * @author caiyang
//	 * @param model
//	 * @param hide
//	 * @param index1
//	 * @param index2
//	 * @return boolean
//	 * @date 2023-08-16 09:06:49 
//	 */ 
//	public boolean updateDataMsgHide(GridRecordDataModel model, Integer hide, String index1, String index2,String blockId) {
//		try {
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("list_id", model.getList_id());
//			jsonObject.put("index", index1);
//			jsonObject.put("block_id", blockId);
//			String type = DatasourceTypeUtil.getDatasourceType();
//			String sql = "";
//			if(SystemDatasourceTypeEnum.MYSQL.getCode().equals(type))
//			{
//				sql = " status=0 ,json_data=json_set(json_data,\"$.hide\","+hide+")";
//			}else if(SystemDatasourceTypeEnum.SQLSERVER.getCode().equals(type))
//			{
//				sql = " status=0 ,json_data=JSON_MODIFY(json_data,'$.hide',"+hide+")";
//			}else if(SystemDatasourceTypeEnum.POSTGRESQL.getCode().equals(type) || SystemDatasourceTypeEnum.KINGBASE.getCode().equals(type)
//					|| SystemDatasourceTypeEnum.DM.getCode().equals(type))
//			{
//				sql = " status=0 ,json_data=jsonb_set(json_data,'{hide}',"+"'"+hide+"'::jsonb"+",true)";
//			}else if(SystemDatasourceTypeEnum.ORACLE.getCode().equals(type))
//			{
//				sql = " status=0 ,json_data=JSON_MERGEPATCH(json_data,'{\"hide\":1}')";
//			}
//			jsonObject.put("sql", sql);
//			this.luckysheetMapper.dynamicUpdateJsonbData(jsonObject);
//			
//			Luckysheet luckysheet = new Luckysheet();
//			luckysheet.setStatus(1);
//			UpdateWrapper<Luckysheet> updateWrapper = new UpdateWrapper<>();
//			updateWrapper.eq("list_id", model.getList_id());
//			updateWrapper.eq("sheet_index", index2);
//			updateWrapper.eq("block_id", blockId);
//			this.luckysheetMapper.update(luckysheet, updateWrapper);
//			return true;
//		} catch (Exception e) {
//			log.error(e.getMessage());
//			return false;
//		}
//	}
//	
//	/**  
//	 * @MethodName: updateDataMsgNoHide
//	 * @Description: 更新sheet隐藏状态
//	 * @author caiyang
//	 * @param model
//	 * @param hide
//	 * @param index
//	 * @return boolean
//	 * @date 2023-08-16 09:46:51 
//	 */ 
//	public boolean updateDataMsgNoHide(GridRecordDataModel model, Integer hide, String index,String blockId) {
//		try {
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("list_id", model.getList_id());
//			jsonObject.put("block_id", blockId);
//			String sql = " status=0 ";
//			jsonObject.put("sql", sql);
//			this.luckysheetMapper.dynamicUpdateJsonbData(jsonObject);
//			
//			jsonObject = new JSONObject();
//			jsonObject.put("list_id", model.getList_id());
//			jsonObject.put("index", index);
//			jsonObject.put("block_id", blockId);
//			String type = DatasourceTypeUtil.getDatasourceType();
//			if(SystemDatasourceTypeEnum.MYSQL.getCode().equals(type))
//			{
//				sql = " status=1 ,json_data=json_set(json_data,\"$.hide\","+hide+")";
//			}else if(SystemDatasourceTypeEnum.SQLSERVER.getCode().equals(type))
//			{
//				sql = " status=1 ,json_data=JSON_MODIFY(json_data,'$.hide',"+hide+")";
//			}else if(SystemDatasourceTypeEnum.POSTGRESQL.getCode().equals(type) || SystemDatasourceTypeEnum.KINGBASE.getCode().equals(type)
//					|| SystemDatasourceTypeEnum.DM.getCode().equals(type))
//			{
//				sql = " status=1 ,json_data=jsonb_set(json_data,'{hide}',"+"'"+hide+"'::jsonb"+",true)";
//			}else if(SystemDatasourceTypeEnum.ORACLE.getCode().equals(type))
//			{
//				sql = " status=1 ,json_data=JSON_MERGEPATCH(json_data,'{\"hide\":0}')";
//			}
//			jsonObject.put("sql", sql);
//			this.luckysheetMapper.dynamicUpdateJsonbData(jsonObject);
//			return true;
//		} catch (Exception e) {
//			log.error(e.getMessage());
//			return false;
//		}
//	}
//	
//	/**  
//	 * @MethodName: processCalc
//	 * @Description: 公式链，更新对应的单元格公式和数据
//	 * @author caiyang
//	 * @param v
//	 * @param index
//	 * @param listId void
//	 * @date 2023-09-08 10:52:53 
//	 */ 
//	@Async
//	public void processCalc(Object v, WSUserModel wsUserModel, String index, String listId,String operate) {
//		if(v != null)
//		{
//			JSONArray calcChain = (JSONArray) v;
//			if(!ListUtil.isEmpty(calcChain))
//			{
//				JSONObject jsonObject = null;
//				QueryWrapper<Luckysheet> queryWrapper = null;
//				for (int i = 0; i < calcChain.size(); i++) {
//					jsonObject = calcChain.getJSONObject(i);
//					if(jsonObject.containsKey("func"))
//					{
//						JSONArray func = jsonObject.getJSONArray("func");
//						String formula = func.getString(2);
//						Object value = func.get(1);
//						int r = jsonObject.getIntValue("r");
//						int c = jsonObject.getIntValue("c");
//						String key =RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" +r+"_"+c;
//			    		Object redisData = redisUtil.get(key);
//			    		Luckysheet luckysheet = null;
//			    		JSONObject jsonData_v = null;
//			    		if(redisData == null)
//			    		{
//			    			queryWrapper = new QueryWrapper<>();
//							queryWrapper.eq("block_id", r+"_"+c);
//							queryWrapper.eq("sheet_index", index);
//							queryWrapper.eq("list_id", listId);
//							queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//							luckysheet = this.luckysheetMapper.selectOne(queryWrapper);
//							JSONObject jsonData = (JSONObject) luckysheet.getJsonData();
//							jsonData_v = jsonData.getJSONArray("celldata").getJSONObject(0).getJSONObject("v");
//			    		}else {
//			    			jsonData_v = JSON.parseObject(JSON.toJSONString(redisData));
//			    		}
//						if(jsonData_v != null)
//						{
//							JSONObject sourceData = JSON.parseObject(JSON.toJSONString(jsonData_v));
//							jsonData_v.put("v", value);
//							jsonData_v.put("f", formula);
//							jsonData_v.put("m", String.valueOf(value));
//							JSONObject conditions = new JSONObject();
//							conditions.put("list_id",listId);
//							conditions.put("index",index);
//							conditions.put("block_id",r+"_"+c);
//							this.updateCellDataListValue(conditions, "celldata,0,v", null, jsonData_v, sourceData, wsUserModel, index, listId,r+"_"+c, operate);
//						}
//					}
//				}
//			}
//		}
//	}
//	
//	@Async
//	public void Operation_fc(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
//        try {
//            //当前sheet的index值
//            String i = bson.get("i").toString();
//            //对象值，这里对象的内部字段不需要单独更新，所以存为文本即可  2018-11-28 前段需求必须取出时为对象
//            JSONObject v = bson.getJSONObject("v");
////            if (bson.get("v") instanceof String) {
////                v = bson.get("v").toString();
////            } else {
////                v = bson.get("v");
////            }
//
//            //操作类型,add为新增，update为更新，del为删除
//            String op = bson.get("op").toString();
//            //更新或者删除的函数位置
//            String pos = bson.get("pos").toString();
//            //1、先获取原数据
//            JSONObject _dbObject = this.getConfigByGridKey(gridKey, i,blockId);
//            JSONObject sourceData = JSON.parseObject(JSON.toJSONString(_dbObject));
//            //Query query = new Query();
//            //query.addCriteria(Criteria.where("list_id").is(gridKey).and("index").is(i).and("block_id").is(JfGridConfigModel.FirstBlockID));
//            JSONObject query=getQuery(gridKey,i,blockId);
//            boolean _result = false;
//            Object calcChain = JfGridFileUtil.getObjectByIndex(_dbObject, "calcchain");
//            if (calcChain == null) {
//                //不存在 (只处理添加)
//                if (op.equals("add")) {
//
//                    //update.set("calcChain",_dlist);//添加
//                    _result = this.updateJsonbForSetNull(query, "calcChain", v, 0,i,gridKey,blockId);
//                }
//            } else {
//                //存在
//                if (op.equals("add")) {
//                    _result = this.updateJsonbForElementInsert(query, "calcChain", v, 0,sourceData,wsUserModel,i,gridKey,bson.getString("operate"),blockId);
//                } else if (op.equals("update")) {
//                    //update.set("calcChain."+pos,v);//修改
//                	v.put("position", pos);
//                    this.updateCellDataListValue(query, "calcChain", pos, v,sourceData,wsUserModel,i,gridKey,blockId,bson.getString("operate"));
//                    _result = true;
//                } else if (op.equals("del")) {
//                	this.delCalChain(query, v, wsUserModel, i, gridKey, blockId,bson.getString("operate"));
////                    if (calcChain instanceof List) {
////                        List<JSONObject> _list = (List<JSONObject>) calcChain;
////                        Integer size = Integer.valueOf(pos);
////                        if (size <= _list.size()) {
////                            int listindex = size;
////                            _list.remove(listindex);
////                            //update.set("calcChain",calcChain);//重新赋值
////                            this.updateCellDataListValue(query, "calcChain", null, calcChain,sourceData,wsUserModel,i,gridKey,blockId,bson.getString("operate"));
////                            _result = true;
////                        }
////                    }
//                }
//            }
//            if (!_result) {
//                return;
//            }
//
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//	}
//	
//	private void delCalChain(JSONObject query,JSONObject v,WSUserModel wsUserModel,String index,String gridKey,String blockId,String operate) {
//		query.put("keyName", "calcChain");
//		query.put("r", v.get("r"));
//		query.put("c", v.get("c"));
//		this.asyncProcessService.updateRedisJsonData("delCalc", query, index, gridKey, blockId);
//	}
//	
//	 /**  
//     * @MethodName: getConfigByGridKey
//     * @Description: 获取指定xls、sheet中的config中数据 （存放在第一块中）
//     * @author caiyang
//     * @return JSONObject
//     * @date 2023-08-15 09:43:37 
//     */ 
//    private JSONObject getConfigByGridKey(String listId, String index,String blockId) {
//    	try {
//    		JSONObject db=new JSONObject();
//    		Map<String, Object> map = this.getMapData(listId, index, blockId);
//        	for (String key : map.keySet()) {
//        		 if("json_data".equals(key))
//        		 {
//        			 Object jsonData = map.get("json_data");
//        			 JSONObject jsonObject = StringUtil.getJonsData(jsonData);
//        			 if(jsonObject.containsKey("config"))
//        			 {
//        				 if(jsonObject.get("config") != null)
//        				 {
//        					 db.put("config", jsonObject.getJSONObject("config"));
//        				 }else {
//        					 db.put("config", new JSONObject());
//        				 }
//        			 }
//        			 if(jsonObject.containsKey("calcChain"))
//        			 {
//        				 if(jsonObject.get("calcChain") != null)
//        				 {
//        					 db.put("calcchain", jsonObject.getJSONArray("calcChain"));
//        				 }else {
//        					 db.put("calcchain", new JSONArray());
//        				 }
//        			 }
//        			 if(jsonObject.containsKey("filter"))
//        			 {
//        				 if(jsonObject.get("filter") != null)
//        				 {
//        					 db.put("filter", jsonObject.getJSONObject("filter"));
//        				 }else {
//        					 db.put("filter", new JSONObject());
//        				 }
//        			 }
//        			 if(jsonObject.containsKey("hyperlink"))
//        			 {
//        				 if(jsonObject.get("hyperlink") != null)
//        				 {
//        					 db.put("hyperlink", jsonObject.getJSONObject("hyperlink"));
//        				 }else {
//        					 db.put("hyperlink", new JSONObject());
//        				 }
//        			 }if(jsonObject.containsKey("dataVerification"))
//        			 {
//        				 if(jsonObject.get("dataVerification") != null)
//        				 {
//        					 db.put("dataVerification", jsonObject.getJSONObject("dataVerification"));
//        				 }else {
//        					 db.put("dataVerification", new JSONObject());
//        				 }
//        			 }if(jsonObject.containsKey("images"))
//        			 {
//        				 if(jsonObject.get("images") != null)
//        				 {
//        					 db.put("images", jsonObject.getJSONObject("images"));
//        				 }else {
//        					 db.put("images", new JSONObject());
//        				 }
//        			 }
//        		 }else {
//        			 db.put(key.toLowerCase(), map.get(key)); 
//        		 }
//        	 }
//        	 return db;
//		} catch (Exception e) {
//			log.error(e.getMessage());
//            return null;
//		}
//    }
//    
//    private Map<String, Object> getMapData(String listId, String index,String blockId){
//    	Object object = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
//    	Luckysheet luckysheet = null;
//    	Map<String, Object> mapData = new HashMap<>();;
//		if(object == null)
//		{
//			QueryWrapper<Luckysheet> queryWrapper = new QueryWrapper<Luckysheet>();
//        	queryWrapper.eq("list_id", listId);
//        	queryWrapper.eq("sheet_index", index);
//        	queryWrapper.eq("block_id", blockId);
//        	queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//        	luckysheet = this.luckysheetMapper.selectOne(queryWrapper);
//        	redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
//		}else {
//			luckysheet = JSON.parseObject(object.toString(), Luckysheet.class);
//		}
//		mapData.put("id", luckysheet.getId());
//		mapData.put("block_id", luckysheet.getBlockId());
//		mapData.put("row_col", luckysheet.getRowCol());
//		mapData.put("sheet_index", luckysheet.getSheetIndex());
//		mapData.put("list_id", luckysheet.getListId());
//		mapData.put("status", luckysheet.getStatus());
//		mapData.put("json_data", luckysheet.getJsonData());
//		mapData.put("sheet_order", luckysheet.getSheetOrder());
//		mapData.put("del_flag", DelFlagEnum.UNDEL.getCode());
//		return mapData;
//    }
//    
//    private JSONObject getQuery(String gridKey,String i,String blockId){
//        JSONObject query=new JSONObject();
//        query.put("list_id",gridKey);
//        query.put("index",i);
//        query.put("block_id",blockId);
//        return query;
//    }
//}
