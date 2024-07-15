//package com.springreport.subscribe;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.toolkit.IdWorker;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.springreport.api.luckysheetonlinecell.ILuckysheetOnlineCellService;
//import com.springreport.api.onlinetplsheet.IOnlineTplSheetService;
//import com.springreport.constants.Constants;
//import com.springreport.entity.luckysheetonlinecell.LuckysheetOnlineCell;
//import com.springreport.entity.onlinetplsheet.OnlineTplSheet;
//import com.springreport.enums.DelFlagEnum;
//import com.springreport.enums.RedisPrefixEnum;
//import com.springreport.enums.UpdateOnlineTypeEnum;
//import com.springreport.util.RedisUtil;
//import com.springreport.util.StringUtil;
//import com.springreport.websocket.ScreenWebSocket;
//
///**  
// * @ClassName: OnlineReceiver
// * @Description: 在线文档消息订阅接收
// * @author caiyang
// * @date 2023-02-08 09:48:23 
//*/  
//@Component
//public class OnlineReceiver {
//
//	@Autowired
//	private RedisUtil redisUtil;
//	
//	@Autowired
//	private IOnlineTplSheetService iOnlineTplSheetService;
//	
//	@Autowired
//	private ILuckysheetOnlineCellService iLuckysheetOnlineCellService;
//	
//	@Autowired
//	private ScreenWebSocket websocket;
//	
//	private static ObjectMapper objectMapper = new ObjectMapper();
//	
//	public void receiveMessage(String message) throws JsonProcessingException, InterruptedException{
//		JSONObject jsonObject = JSON.parseObject((message.substring(1, message.length() - 1)).replaceAll("\\\\", ""));
//		String operateType = jsonObject.getString("type");
//		Long tplId = jsonObject.getLong("tplId");
//		Long sheetId = jsonObject.getLong("sheetId");
//		int sheetOrder = jsonObject.getIntValue("sheetOrder");
//		String taskId = jsonObject.getString("taskId");
//		Long version = jsonObject.getLong("version");
//		String formatType = jsonObject.getString("formatType");
//		jsonObject.put("sheetOrder", sheetOrder);
//		boolean isProcess = redisUtil.setIfAbsent(RedisPrefixEnum.UPDATEREPORT.getCode()+tplId+"_"+sheetId+"_"+taskId, "1", 10);//防止集群部署并发处理
//		if(!isProcess)
//		{//如果有正在处理的，则直接进行通知即可
//			try {
//				this.websocket.sendTplMessage(String.valueOf(tplId), objectMapper.writeValueAsString(jsonObject));
//			} catch (Exception e) {
//				
//			}
//		}else {
//			if(UpdateOnlineTypeEnum.UPDATECELL.getCode().equals(operateType) || UpdateOnlineTypeEnum.UPDATECELLFORMAT.getCode().equals(operateType)
//					|| UpdateOnlineTypeEnum.UPDATECELLFORMATOTHER.getCode().equals(operateType))
//			{//修改单元格数据
//				JSONArray cellDatas = jsonObject.getJSONArray("cellDatas");
//				try {
//					this.websocket.sendTplMessage(String.valueOf(tplId), objectMapper.writeValueAsString(jsonObject));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				for (int i = 0; i < cellDatas.size(); i++) {
//					int r = cellDatas.getJSONObject(i).getIntValue("r");
//					int c = cellDatas.getJSONObject(i).getIntValue("c");
//					String key = RedisPrefixEnum.ONLINEUPDATEVALUE.getCode()+sheetId+"_"+ r+"_"+c;
//					if(UpdateOnlineTypeEnum.UPDATECELL.getCode().equals(operateType))
//					{
//						key = RedisPrefixEnum.ONLINEUPDATEVALUE.getCode()+sheetId+"_"+ r+"_"+c;
//					}else if(UpdateOnlineTypeEnum.UPDATECELLFORMAT.getCode().equals(operateType)
//							|| UpdateOnlineTypeEnum.UPDATECELLFORMATOTHER.getCode().equals(operateType))
//					{
//						key = RedisPrefixEnum.ONLINEUPDATEFORMAT.getCode()+formatType+"_"+sheetId+"_"+ r+"_"+c;
//					}
//					List<String> keys = new ArrayList<String>();
//					keys.add(key);
//					Long luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//					if(luaResult.intValue() == 1)
//					{
//						QueryWrapper<LuckysheetOnlineCell> cellQueryWrapper = new QueryWrapper<>();
//						cellQueryWrapper.eq("tpl_id", tplId);
//						cellQueryWrapper.eq("sheet_id", sheetId);
//						cellQueryWrapper.eq("coordsx", r);
//						cellQueryWrapper.eq("coordsy", c);
//						cellQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//						LuckysheetOnlineCell luckysheetOnlineCell = this.iLuckysheetOnlineCellService.getOne(cellQueryWrapper,false);
//						if(luckysheetOnlineCell != null)
//						{//单元格存在进行更新
//							LuckysheetOnlineCell update = new LuckysheetOnlineCell();
//							update.setId(luckysheetOnlineCell.getId());
//							if(UpdateOnlineTypeEnum.UPDATECELL.getCode().equals(operateType))
//							{
//								update.setCellData(cellDatas.getJSONObject(i).getString("data"));
//							}else if(UpdateOnlineTypeEnum.UPDATECELLFORMAT.getCode().equals(operateType)
//									|| UpdateOnlineTypeEnum.UPDATECELLFORMATOTHER.getCode().equals(operateType))
//							{
//								switch (formatType) {
//								case "ct":
//									update.setCt(cellDatas.getJSONObject(i).getString("data"));
//									break;
//								case "ff":
//									update.setFf(cellDatas.getJSONObject(i).getString("data"));
//									break;
//								case "bg":
//									update.setBg(cellDatas.getJSONObject(i).getString("data"));
//									break;
//								case "fc":
//									update.setFc(cellDatas.getJSONObject(i).getString("data"));
//									break;
//								case "bl":
//									update.setBl(cellDatas.getJSONObject(i).getIntValue("data"));
//									break;
//								case "it":
//									update.setIt(cellDatas.getJSONObject(i).getIntValue("data"));
//								case "fs":
//									update.setFs(cellDatas.getJSONObject(i).getIntValue("data"));
//									break;
//								case "cl":
//									update.setCl(cellDatas.getJSONObject(i).getIntValue("data"));
//									break;
//								case "vt":
//									update.setVt(cellDatas.getJSONObject(i).getIntValue("data"));
//									break;
//								case "ht":
//									update.setHt(cellDatas.getJSONObject(i).getIntValue("data"));
//									break;
//								default:
//									break;
//								}
//							}
//							this.iLuckysheetOnlineCellService.updateAsync(update, key, version);
//						}else {
//							//单元格不存在，新增一个单元格
//							LuckysheetOnlineCell insert = new LuckysheetOnlineCell();
//							insert.setTplId(tplId);
//							insert.setSheetId(sheetId);
//							insert.setCoordsx(r);
//							insert.setCoordsy(c);
//							if(UpdateOnlineTypeEnum.UPDATECELL.getCode().equals(operateType))
//							{
//								if(StringUtil.isNotEmpty(cellDatas.getJSONObject(i).getString("data")))
//								{
//									insert.setCellData(cellDatas.getJSONObject(i).getString("data"));
//								}else {
//									insert.setCellData("");
//								}
//								insert.setCt("{\"t\":\"s\",\"fa\":\"@\"}");
//								this.iLuckysheetOnlineCellService.insertAsync(insert, key, version);
//							}
//						}
//					}
//				}
//				redisUtil.del(RedisPrefixEnum.UPDATEREPORT.getCode()+tplId+"_"+sheetId+"_"+taskId);
//			}else if(UpdateOnlineTypeEnum.UPDATEMERGE.getCode().equals(operateType))
//			{
//				String key = RedisPrefixEnum.ONLINEUPDATEMERGE.getCode()+sheetId;
//				List<String> keys = new ArrayList<String>();
//				keys.add(key);
//				Long luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//				try {
//					this.websocket.sendTplMessage(String.valueOf(tplId), objectMapper.writeValueAsString(jsonObject));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				if(luaResult.intValue() == 1)
//				{
//					String merge = jsonObject.getString("merge");
//					OnlineTplSheet update = new OnlineTplSheet();
//					update.setId(sheetId);
//					update.setMerge(merge.equals("null")?null:merge);
//					JSONArray cells = jsonObject.getJSONArray("cells");
//					this.iOnlineTplSheetService.updateAsync(update, key, version,cells,sheetId);
//				}
//			}else if(UpdateOnlineTypeEnum.UPDATEBORDER.getCode().equals(operateType)) {
//				String key = RedisPrefixEnum.ONLINEUPDATEBORDERINFO.getCode()+sheetId;
//				List<String> keys = new ArrayList<String>();
//				keys.add(key);
//				Long luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//				try {
//					this.websocket.sendTplMessage(String.valueOf(tplId), objectMapper.writeValueAsString(jsonObject));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				if(luaResult.intValue() == 1)
//				{
//					String borderInfo = jsonObject.getString("borderInfo");
//					OnlineTplSheet update = new OnlineTplSheet();
//					update.setId(sheetId);
//					update.setBorderInfo(borderInfo.equals("null")?null:borderInfo);
//					this.iOnlineTplSheetService.updateAsync(update, key, version,null,sheetId);
//				}
//			}else if(UpdateOnlineTypeEnum.UPDATEROWLEN.getCode().equals(operateType)) {
//				String key = RedisPrefixEnum.ONLINEUPDATEROWLEN.getCode()+sheetId;
//				List<String> keys = new ArrayList<String>();
//				keys.add(key);
//				Long luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//				try {
//					this.websocket.sendTplMessage(String.valueOf(tplId), objectMapper.writeValueAsString(jsonObject));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				if(luaResult.intValue() == 1)
//				{
//					String rowlen = jsonObject.getString("rowlen");
//					OnlineTplSheet update = new OnlineTplSheet();
//					update.setId(sheetId);
//					update.setRowlen(rowlen);
//					this.iOnlineTplSheetService.updateAsync(update, key, version,null,sheetId);
//				}
//			}else if(UpdateOnlineTypeEnum.UPDATECOLUMNLEN.getCode().equals(operateType)) {
//				String key = RedisPrefixEnum.ONLINEUPDATECOLUMNLEN.getCode()+sheetId;
//				List<String> keys = new ArrayList<String>();
//				keys.add(key);
//				Long luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//				try {
//					this.websocket.sendTplMessage(String.valueOf(tplId), objectMapper.writeValueAsString(jsonObject));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				if(luaResult.intValue() == 1)
//				{
//					String columnlen = jsonObject.getString("columnlen");
//					OnlineTplSheet update = new OnlineTplSheet();
//					update.setId(sheetId);
//					update.setColumnlen(columnlen);
//					this.iOnlineTplSheetService.updateAsync(update, key, version,null,sheetId);
//				}
//			}else if(UpdateOnlineTypeEnum.CREATESHEET.getCode().equals(operateType)) {
//				Long newSheetId = IdWorker.getId();
//				jsonObject.put("newSheetId", String.valueOf(newSheetId));
//				jsonObject.put("status", 0);
//				try {
//					this.websocket.sendTplMessage(String.valueOf(tplId), objectMapper.writeValueAsString(jsonObject));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				JSONObject sheetConfig = jsonObject.getJSONObject("sheetConfig");
//				OnlineTplSheet insert = new OnlineTplSheet();
//				insert.setId(newSheetId);
//				insert.setTplId(tplId);
//				insert.setSheetName(sheetConfig.getString("name"));
//				insert.setSheetIndex(sheetConfig.getString("index"));
//				insert.setSheetOrder(sheetConfig.getIntValue("order"));
//				this.iOnlineTplSheetService.insertAsync(insert);
//			}else if(UpdateOnlineTypeEnum.DELETESHEET.getCode().equals(operateType)) {
//				try {
//					this.websocket.sendTplMessage(String.valueOf(tplId), objectMapper.writeValueAsString(jsonObject));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				OnlineTplSheet onlineTplSheet = new OnlineTplSheet();
//				onlineTplSheet.setId(sheetId);
//				this.iOnlineTplSheetService.deleteAsync(onlineTplSheet);
//			}else if(UpdateOnlineTypeEnum.CHANGESHEETNAME.getCode().equals(operateType)) {
//				String key = RedisPrefixEnum.ONLINEUPDATESHEETNAME.getCode()+sheetId;
//				List<String> keys = new ArrayList<String>();
//				keys.add(key);
//				Long luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//				try {
//					this.websocket.sendTplMessage(String.valueOf(tplId), objectMapper.writeValueAsString(jsonObject));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				if(luaResult.intValue() == 1)
//				{
//					String sheetName = jsonObject.getString("sheetName");
//					OnlineTplSheet update = new OnlineTplSheet();
//					update.setId(sheetId);
//					update.setSheetName(sheetName);
//					this.iOnlineTplSheetService.updateAsync(update, key, version,null,sheetId);
//				}
//			}else if(UpdateOnlineTypeEnum.ROWCOLUMNINSERT.getCode().equals(operateType)) {
//				JSONObject params = jsonObject.getJSONObject("insertParams");
//				this.iLuckysheetOnlineCellService.updateAddCoordinateAsync(params, sheetId);
//				try {
//					this.websocket.sendTplMessage(String.valueOf(tplId), objectMapper.writeValueAsString(jsonObject));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				String key = RedisPrefixEnum.ONLINEUPDATEMERGE.getCode()+sheetId;
//				List<String> keys = new ArrayList<String>();
//				keys.add(key);
//				Long luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//				if(luaResult.intValue() == 1)
//				{
//					String merge = params.getString("merge");
//					OnlineTplSheet update = new OnlineTplSheet();
//					update.setId(sheetId);
//					update.setMerge(merge.equals("null")?null:merge);
//					this.iOnlineTplSheetService.updateAsync(update, key, version,null,sheetId);
//				}
//				key = RedisPrefixEnum.ONLINEUPDATEBORDERINFO.getCode()+sheetId;
//				keys = new ArrayList<String>();
//				keys.add(key);
//				luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//				if(luaResult.intValue() == 1)
//				{
//					String borderInfo = params.getString("borderInfo");
//					OnlineTplSheet update = new OnlineTplSheet();
//					update.setId(sheetId);
//					update.setBorderInfo(borderInfo.equals("null")?null:borderInfo);
//					this.iOnlineTplSheetService.updateAsync(update, key, version,null,sheetId);
//				}
//				key = RedisPrefixEnum.ONLINEUPDATEROWLEN.getCode()+sheetId;
//				keys = new ArrayList<String>();
//				keys.add(key);
//				luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//				if(luaResult.intValue() == 1)
//				{
//					String rowlen = params.getString("rowlen");
//					OnlineTplSheet update = new OnlineTplSheet();
//					update.setId(sheetId);
//					update.setRowlen(rowlen);
//					this.iOnlineTplSheetService.updateAsync(update, key, version,null,sheetId);
//				}
//				key = RedisPrefixEnum.ONLINEUPDATECOLUMNLEN.getCode()+sheetId;
//				keys = new ArrayList<String>();
//				keys.add(key);
//				luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//				if(luaResult.intValue() == 1)
//				{
//					String columnlen = params.getString("columnlen");
//					OnlineTplSheet update = new OnlineTplSheet();
//					update.setId(sheetId);
//					update.setColumnlen(columnlen);
//					this.iOnlineTplSheetService.updateAsync(update, key, version,null,sheetId);
//				}
//			}else if(UpdateOnlineTypeEnum.ROWCOLUMNDEL.getCode().equals(operateType)) {
//				JSONObject params = jsonObject.getJSONObject("delParams");
//				this.iLuckysheetOnlineCellService.updateMinusCoordinateAsync(params,sheetId);
//				try {
//					this.websocket.sendTplMessage(String.valueOf(tplId), objectMapper.writeValueAsString(jsonObject));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				String key = RedisPrefixEnum.ONLINEUPDATEMERGE.getCode()+sheetId;
//				List<String> keys = new ArrayList<String>();
//				keys.add(key);
//				Long luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//				if(luaResult.intValue() == 1)
//				{
//					String merge = params.getString("merge");
//					OnlineTplSheet update = new OnlineTplSheet();
//					update.setId(sheetId);
//					update.setMerge(merge.equals("null")?null:merge);
//					this.iOnlineTplSheetService.updateAsync(update, key, version,null,sheetId);
//				}
//				key = RedisPrefixEnum.ONLINEUPDATEBORDERINFO.getCode()+sheetId;
//				keys = new ArrayList<String>();
//				keys.add(key);
//				luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//				if(luaResult.intValue() == 1)
//				{
//					String borderInfo = params.getString("borderInfo");
//					OnlineTplSheet update = new OnlineTplSheet();
//					update.setId(sheetId);
//					update.setBorderInfo(borderInfo.equals("null")?null:borderInfo);
//					this.iOnlineTplSheetService.updateAsync(update, key, version,null,sheetId);
//				}
//				key = RedisPrefixEnum.ONLINEUPDATEROWLEN.getCode()+sheetId;
//				keys = new ArrayList<String>();
//				keys.add(key);
//				luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//				if(luaResult.intValue() == 1)
//				{
//					String rowlen = params.getString("rowlen");
//					OnlineTplSheet update = new OnlineTplSheet();
//					update.setId(sheetId);
//					update.setRowlen(rowlen);
//					this.iOnlineTplSheetService.updateAsync(update, key, version,null,sheetId);
//				}
//				key = RedisPrefixEnum.ONLINEUPDATECOLUMNLEN.getCode()+sheetId;
//				keys = new ArrayList<String>();
//				keys.add(key);
//				luaResult = redisUtil.executeLua(Constants.COMPARELUA, keys, version);
//				if(luaResult.intValue() == 1)
//				{
//					String columnlen = params.getString("columnlen");
//					OnlineTplSheet update = new OnlineTplSheet();
//					update.setId(sheetId);
//					update.setColumnlen(columnlen);
//					this.iOnlineTplSheetService.updateAsync(update, key, version,null,sheetId);
//				}
//			}
//		}
//    }
//}
