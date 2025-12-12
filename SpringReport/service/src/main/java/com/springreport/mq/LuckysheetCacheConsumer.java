package com.springreport.mq;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.springreport.api.mqfailedmsg.IMqFailedMsgService;
import com.springreport.constants.Constants;
import com.springreport.dto.coedit.MqCacheMsgDto;
import com.springreport.dto.coedit.MqOperateHisDto;
import com.springreport.dto.coedit.MqRCOprepationDto;
import com.springreport.entity.luckysheet.Luckysheet;
import com.springreport.entity.mqfailedmsg.MqFailedMsg;
import com.springreport.enums.MqTypeEnums;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.impl.codeit.MqProcessService;
import com.springreport.util.ListUtil;
import com.springreport.util.Md5Util;
import com.springreport.util.Pako_GzipUtils;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RocketMQMessageListener(topic = Constants.MQ_TPOIC_LUCKYSHEET_CACHE, consumerGroup = "luckysheetCache",consumeMode = ConsumeMode.ORDERLY,maxReconsumeTimes=3)
public class LuckysheetCacheConsumer implements RocketMQListener<String>{
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private MqProcessService mqProcessService;
	
	@Autowired
	private IMqFailedMsgService iMqFailedMsgService;
	
	@Override
    public void onMessage(String message) {
		String md5Str = Md5Util.generateMd5(message);
		String redisKey = RedisPrefixEnum.MQRETRYTIMES.getCode()+md5Str;
		Integer times = (Integer) redisUtil.get(redisKey);
		String uncompressData = Pako_GzipUtils.uncompress(message);
		if(times != null)
		{
			if(times >= 3)
			{
				try {
					redisUtil.del(redisKey);
					this.doProcess(uncompressData, redisKey);
				} catch (Exception e) {
					System.err.println("mq消费异常，异常信息："+e.getMessage());
					StackTraceElement[] elements = e.getStackTrace();
					StackTraceElement element = null;
					if(elements != null && elements.length>0)
					{
						element = elements[0];
					}
					String errMsg = e.getMessage();
					if(element != null)
					{
						errMsg = errMsg+"，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
					}
					MqFailedMsg failedMsg = new MqFailedMsg();
					failedMsg.setType(1);
					failedMsg.setMessageContent(uncompressData);
					failedMsg.setFailureException(errMsg);
					failedMsg.setCreateTime(new Date());
					failedMsg.setUpdateTime(new Date());
					try {
						this.iMqFailedMsgService.save(failedMsg);
					} catch (Exception e2) {
						System.err.println("mq消费信息插入异常，异常信息："+e2.getMessage());
						e.printStackTrace();
					}
				}
			}else {
				redisUtil.increment(redisKey);
				this.doProcess(uncompressData, redisKey);
			}
		}else {
			redisUtil.increment(redisKey);
			this.doProcess(uncompressData, redisKey);
		}
	}
	
	private void doProcess(String uncompressData,String redisK) {
		MqCacheMsgDto mqCacheMsgDto = JSON.parseObject(uncompressData, MqCacheMsgDto.class);
		String keyName = mqCacheMsgDto.getKeyName();
		String index = mqCacheMsgDto.getIndex();
		String listId = mqCacheMsgDto.getListId();
		String blockId = mqCacheMsgDto.getBlockId();
		String rowCol = mqCacheMsgDto.getRowCol();
		Object v = mqCacheMsgDto.getV();
		Luckysheet luckysheet = null;
		if(MqTypeEnums.UPDATEBORDERINFO.getCode().equals(keyName))
		{//更新边框
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getBorderInfo());
				luckysheet.setBorderInfo(JSON.toJSONString(v));
				mqOperateHisDto.setChangeAfter(JSON.toJSONString(v));
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(listId);
				mqOperateHisDto.setIndex(index);
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.UPDATECONFIG.getCode().equals(keyName))
		{//更新config所有的信息
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				if(v == null)
				{
					luckysheet.setMergeInfo(JSON.toJSONString(new JSONObject()));
					luckysheet.setRowlen(JSON.toJSONString(new JSONObject()));
					luckysheet.setColumnlen(JSON.toJSONString(new JSONObject()));
					luckysheet.setRowhidden(JSON.toJSONString(new JSONObject()));
					luckysheet.setColhidden(JSON.toJSONString(new JSONObject()));
					luckysheet.setBorderInfo(JSON.toJSONString(new JSONArray()));
				}else {
					JSONObject _v = JSON.parseObject(JSON.toJSONString(v));
					for(Map.Entry entry : _v.entrySet()){
						String key = (String) entry.getKey();
						MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
						mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
						mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
						mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
						mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
						if("merge".equals(key))
						{
							mqOperateHisDto.setKey("merge");
							mqOperateHisDto.setChangeBefore(luckysheet.getMergeInfo()==null?"{}":luckysheet.getMergeInfo());
							luckysheet.setMergeInfo(JSON.toJSONString(entry.getValue()));
							mqOperateHisDto.setChangeAfter(JSON.toJSONString(entry.getValue()));
							if(mqOperateHisDto.getChangeBefore() == null)
							{
								mqOperateHisDto.setChangeBefore("");
							}
							if(!mqOperateHisDto.getChangeBefore().equals(mqOperateHisDto.getChangeAfter()))
							{
								mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
							}
						}else if("rowlen".equals(key))
						{
							mqOperateHisDto.setKey(MqTypeEnums.UPDATEROWLEN.getCode());
							mqOperateHisDto.setChangeBefore(luckysheet.getRowlen()==null?"{}":luckysheet.getRowlen());
							luckysheet.setRowlen(JSON.toJSONString(entry.getValue()));
							mqOperateHisDto.setChangeAfter(JSON.toJSONString(entry.getValue()));
							if(mqOperateHisDto.getChangeBefore() == null)
							{
								mqOperateHisDto.setChangeBefore("");
							}
							if(!mqOperateHisDto.getChangeBefore().equals(mqOperateHisDto.getChangeAfter()))
							{
								mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
							}
						}else if("columnlen".equals(key)) {
							mqOperateHisDto.setKey(MqTypeEnums.UPDATECOLLEN.getCode());
							mqOperateHisDto.setChangeBefore(luckysheet.getColumnlen()==null?"{}":luckysheet.getColumnlen());
							luckysheet.setColumnlen(JSON.toJSONString(entry.getValue()));
							mqOperateHisDto.setChangeAfter(JSON.toJSONString(entry.getValue()));
							if(mqOperateHisDto.getChangeBefore() == null)
							{
								mqOperateHisDto.setChangeBefore("");
							}
							if(!mqOperateHisDto.getChangeBefore().equals(mqOperateHisDto.getChangeAfter()))
							{
								mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
							}
						}else if("rowhidden".equals(key)) {
							mqOperateHisDto.setKey(MqTypeEnums.UPDATEROWHIDDEN.getCode());
							mqOperateHisDto.setChangeBefore(luckysheet.getRowhidden()==null?"{}":luckysheet.getRowhidden());
							luckysheet.setRowhidden(JSON.toJSONString(entry.getValue()));
							mqOperateHisDto.setChangeAfter(JSON.toJSONString(entry.getValue()));
							if(mqOperateHisDto.getChangeBefore() == null)
							{
								mqOperateHisDto.setChangeBefore("");
							}
							if(!mqOperateHisDto.getChangeBefore().equals(mqOperateHisDto.getChangeAfter()))
							{
								mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
							}
						}else if("colhidden".equals(key)) {
							mqOperateHisDto.setKey(MqTypeEnums.UPDATECOLHIDDEN.getCode());
							mqOperateHisDto.setChangeBefore(luckysheet.getColhidden()==null?"{}":luckysheet.getColhidden());
							luckysheet.setColhidden(JSON.toJSONString(entry.getValue()));
							mqOperateHisDto.setChangeAfter(JSON.toJSONString(entry.getValue()));
							if(mqOperateHisDto.getChangeBefore() == null)
							{
								mqOperateHisDto.setChangeBefore("");
							}
							if(!mqOperateHisDto.getChangeBefore().equals(mqOperateHisDto.getChangeAfter()))
							{
								mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
							}
						}else if("borderInfo".equals(key)) {
							mqOperateHisDto.setKey(MqTypeEnums.UPDATEBORDERINFO.getCode());
							mqOperateHisDto.setChangeBefore(luckysheet.getBorderInfo()==null?"[]":luckysheet.getBorderInfo());
							luckysheet.setBorderInfo(JSON.toJSONString(entry.getValue()));
							mqOperateHisDto.setChangeAfter(JSON.toJSONString(entry.getValue()));
							if(mqOperateHisDto.getChangeBefore() == null)
							{
								mqOperateHisDto.setChangeBefore("");
							}
							if(!mqOperateHisDto.getChangeBefore().equals(mqOperateHisDto.getChangeAfter()))
							{
								mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
							}
						}
					}
				}
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
			}
		}else if(MqTypeEnums.UPDATEHYPERLINK.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getHyperlink());
				luckysheet.setHyperlink(JSON.toJSONString(v));
				mqOperateHisDto.setChangeAfter(v);
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.UPDATEIMAGES.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getImage());
				luckysheet.setImage(JSON.toJSONString(v));
				mqOperateHisDto.setChangeAfter(v);
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.UPDATEFROZEN.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
 			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getFrozen());
				luckysheet.setFrozen(JSON.toJSONString(v));
				mqOperateHisDto.setChangeAfter(v);
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.UPDATEFILTERSELECT.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getFilterSelect());
				luckysheet.setFilterSelect(JSON.toJSONString(v));
				mqOperateHisDto.setChangeAfter(v);
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.UPDATEFILTER.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getFilter());
				luckysheet.setFilter(JSON.toJSONString(v));
				mqOperateHisDto.setChangeAfter(v);
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.CLEARFILTER.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getFilterSelect());
				luckysheet.setFilterSelect(JSON.toJSONString(new JSONObject()));
				luckysheet.setFilter(JSON.toJSONString(new JSONObject()));
				mqOperateHisDto.setChangeAfter(JSON.toJSONString(new JSONObject()));
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.UPDATEDATAVERIFICATION.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getDataverification());
				luckysheet.setDataverification(JSON.toJSONString(v));
				mqOperateHisDto.setChangeAfter(v);
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.UPDATECALCCHAIN.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				luckysheet.setCalcchain(JSON.toJSONString(v));
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
			}
		}else if(MqTypeEnums.UPDATELUCKYSHEETALTERNATEFORMATSAVE.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getLuckysheetAlternateformatSave());
				luckysheet.setLuckysheetAlternateformatSave(JSON.toJSONString(v));
				mqOperateHisDto.setChangeAfter(v);
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.UPDATELUCKYSHEETCONDITIONFORMATSAVE.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				luckysheet.setLuckysheetConditionformatSave(JSON.toJSONString(v));
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
			}
		}else if(MqTypeEnums.ADDFC.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				JSONArray calcChain = JSON.parseArray(luckysheet.getCalcchain());
				if(ListUtil.isEmpty(calcChain))
				{
					calcChain = new JSONArray();
				}
				calcChain.add(v);
				luckysheet.setCalcchain(JSON.toJSONString(calcChain));
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
			}
		}else if(MqTypeEnums.DELFC.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				JSONArray calcChain = JSON.parseArray(luckysheet.getCalcchain());
				if(!ListUtil.isEmpty(calcChain))
				{
					JSONObject _v = (JSONObject) v;
					int pos = -1;
					int r = _v.getIntValue("r");
					int c = _v.getIntValue("c");
					for (int i = 0; i < calcChain.size(); i++) {
						JSONObject calc = calcChain.getJSONObject(i);
						if(r == calc.getIntValue("r") && c == calc.getIntValue("c"))
						{
							pos = i;
							break;
						}
					}
					if(pos >= 0) {
						calcChain.remove(pos);
						luckysheet.setCalcchain(JSON.toJSONString(calcChain));
						redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
					}
				}
			}
		}else if(MqTypeEnums.UPDATEROWHIDDEN.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getRowhidden());
				luckysheet.setRowhidden(JSON.toJSONString(v));
				mqOperateHisDto.setChangeAfter(v);
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));	
				if(mqOperateHisDto.getChangeBefore() == null)
				{
					mqOperateHisDto.setChangeBefore("");
				}
				if(!mqOperateHisDto.getChangeBefore().equals(mqOperateHisDto.getChangeAfter()))
				{
					mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
				}
			}
		}else if(MqTypeEnums.UPDATEFILTERROWHIDDEN.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getFilterrowhidden());
				luckysheet.setFilterrowhidden(JSON.toJSONString(v));
				mqOperateHisDto.setChangeAfter(v);
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));	
				if(mqOperateHisDto.getChangeBefore() == null)
				{
					mqOperateHisDto.setChangeBefore("");
				}
				if(!mqOperateHisDto.getChangeBefore().equals(mqOperateHisDto.getChangeAfter()))
				{
					mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
				}
			}
		}else if(MqTypeEnums.UPDATECOLHIDDEN.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getRowhidden());
				luckysheet.setColhidden(JSON.toJSONString(v));
				mqOperateHisDto.setChangeBefore(luckysheet.getRowhidden());
				mqOperateHisDto.setChangeAfter(v);
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));	
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.UPDATEROWLEN.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getRowlen());
				luckysheet.setRowlen(JSON.toJSONString(v));
				mqOperateHisDto.setChangeAfter(v);
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				if(mqOperateHisDto.getChangeBefore() == null)
				{
					mqOperateHisDto.setChangeBefore("");
				}
				if(!mqOperateHisDto.getChangeBefore().equals(mqOperateHisDto.getChangeAfter()))
				{
					mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
				}
			}
		}else if(MqTypeEnums.UPDATECOLLEN.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getColumnlen());
				luckysheet.setColumnlen(JSON.toJSONString(v));
				mqOperateHisDto.setChangeAfter(v);
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				if(mqOperateHisDto.getChangeBefore() == null)
				{
					mqOperateHisDto.setChangeBefore("");
				}
				if(!mqOperateHisDto.getChangeBefore().equals(mqOperateHisDto.getChangeAfter()))
				{
					mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
				}
			}
		}else if(MqTypeEnums.UPDATEROW.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getRowSize());
				luckysheet.setRowSize(Integer.parseInt(String.valueOf(v)));
				mqOperateHisDto.setChangeAfter(v);
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.UPDATECOLUMN.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getColumnSize());
				luckysheet.setColumnSize(Integer.parseInt(String.valueOf(v)));
				mqOperateHisDto.setChangeAfter(v);
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.UPDATENAME.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			JSONObject _v = (JSONObject) v;
			String txt = _v.getString("txt");
			String oldtxt =  _v.getString("oldtxt");
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getSheetName());
				luckysheet.setSheetName(txt);
				mqOperateHisDto.setChangeAfter(txt);
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
			JSONObject updateCells = _v.getJSONObject("updateCells");
			if(!StringUtil.isEmptyMap(updateCells))
			{
				List<String> cellKeys = null;
				Map<String, JSONObject> cellDatasCache = null;
				for(String key:updateCells.keySet()){
					JSONArray cells = updateCells.getJSONArray(key);
					cellKeys = new ArrayList<>();
					cellDatasCache = new HashMap<>();
					if(!ListUtil.isEmpty(cells))
					{
						for (int i = 0; i < cells.size(); i++) {
							int r = cells.getJSONObject(i).getIntValue("r");
							int c = cells.getJSONObject(i).getIntValue("c");
							String cellKey = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +key + "_" + r + "_" + c;
							cellKeys.add(cellKey);
							cellDatasCache.put(r + "_" + c, cells.getJSONObject(i));
						}
						List<Object> cacheDatas = redisUtil.multiGet(cellKeys);
						if(!ListUtil.isEmpty(cacheDatas))
						{
							Map<String, JSONObject> saveRedisData = new HashMap<>();
							JSONObject cacheData = null;
							for (int i = 0; i < cacheDatas.size(); i++) {
								cacheData = (JSONObject) cacheDatas.get(i);
								int r = cacheData.getIntValue("r");
								int c = cacheData.getIntValue("c");
								if(cacheData.getJSONObject("v") != null && StringUtil.isNotEmpty(cacheData.getJSONObject("v").getString("f")))
								{
									if(cellDatasCache.get(r+"_"+c) != null)
									{
										cacheData.getJSONObject("v").getString("f").replaceAll(oldtxt+"!", txt+"!");
										saveRedisData.put(RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +key + "_" +index + "_" + r + "_" + c, cacheData);
									}
								}
							}
							if(!StringUtil.isEmptyMap(saveRedisData))
							{
								redisUtil.multiSet(saveRedisData);
							}
						}
					}
				}
			}
		}else if(MqTypeEnums.UPDATECOLOR.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getColor());
				luckysheet.setColor(v == null?"":String.valueOf(v));
				mqOperateHisDto.setChangeAfter(v);
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.UPDATEHIDESTATUS.getCode().equals(keyName)) {
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null)
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				mqOperateHisDto.setChangeBefore(luckysheet.getHide());
				luckysheet.setHide(Integer.parseInt(String.valueOf(v)));
				mqOperateHisDto.setChangeAfter(v);
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.UPDATEORDER.getCode().equals(keyName)) {
			List<String> keys = redisUtil.getKeys(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_");
			List<Object> list = redisUtil.multiGet(keys);
			if(!ListUtil.isEmpty(list))
			{
				JSONObject data = (JSONObject) v;
				String sheetIndex = "";
				int beforeChange = 0;
				int change = 0;
				for (int i = 0; i < list.size(); i++) {
					luckysheet = JSON.parseObject(list.get(i).toString(),Luckysheet.class);
					String key = RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+luckysheet.getSheetIndex()+"_"+blockId;
					Integer sheetOrder = data.getInteger(luckysheet.getSheetIndex());
					if(StringUtil.isNotEmpty(mqCacheMsgDto.getOperate()) && mqCacheMsgDto.getOperate().contains(luckysheet.getSheetIndex()))
					{
						beforeChange = luckysheet.getSheetOrder();
						sheetIndex = luckysheet.getSheetIndex();
					}
					if(sheetOrder != null)
					{
						luckysheet.setSheetOrder(sheetOrder);
						change = sheetOrder;
						redisUtil.set(key,JSON.toJSONString(luckysheet));
					}
				}
				if(StringUtil.isNotEmpty(sheetIndex))
				{
					MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
					mqOperateHisDto.setKey(keyName);
					mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate()+"，顺序由【"+beforeChange+"】变化为【"+change+"】");
					mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
					mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
					mqOperateHisDto.setIndex(sheetIndex);
					mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
				}
			}
		}else if(MqTypeEnums.DELSHEET.getCode().equals(keyName)) {
			String sheetKey = RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId;
			redisUtil.del(sheetKey);
		}else if(MqTypeEnums.ADDSHEET.getCode().equals(keyName)) {
			JSONObject _v = (JSONObject) v;
			luckysheet = new Luckysheet();
			luckysheet.setId(_v.getLongValue("id"));
			luckysheet.setBlockId(blockId);
			luckysheet.setRowSize(Constants.DEFAULT_SHEET_ROW);
			luckysheet.setColumnSize(Constants.DEFAULT_SHEET_COLUMN);
			luckysheet.setSheetIndex(index);
			luckysheet.setListId(listId);
			luckysheet.setStatus(0);
			luckysheet.setSheetOrder(_v.getIntValue("order"));
			luckysheet.setSheetName(_v.getString("name"));
			luckysheet.setHide(0);
			String sheetKey = RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId;
			redisUtil.set(sheetKey,JSON.toJSONString(luckysheet));
			MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
			mqOperateHisDto.setKey(keyName);
			mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
			mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
			mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
			mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
			mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
		}else if(MqTypeEnums.COPYSHEET.getCode().equals(keyName)) {
			redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId, JSON.toJSONString(v));
			MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
			mqOperateHisDto.setKey(keyName);
			mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
			mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
			mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
			mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
			mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
		}else if(MqTypeEnums.UPDATECELLDATA.getCode().equals(keyName))
		{//更新或者新增数据
			redisUtil.set(RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + rowCol,v);
			if(StringUtil.isNotEmpty(mqCacheMsgDto.getOperate()))
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setRowCol(rowCol);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				mqOperateHisDto.setChangeBefore(mqCacheMsgDto.getChangeBefore());
				mqOperateHisDto.setChangeAfter(v);
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				this.mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.BATCHDELETECELLDATA.getCode().equals(keyName))
		{
			List<String> ids = (List<String>) v;
			redisUtil.del(ids);
			if(StringUtil.isNotEmpty(mqCacheMsgDto.getOperate()))
			{
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setRowCol(rowCol);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				mqOperateHisDto.setChangeBefore(mqCacheMsgDto.getChangeBefore());
				mqOperateHisDto.setChangeAfter(v);
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				this.mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.BATCHINSERTCELLDATA.getCode().equals(keyName))
		{
			Map<String, JSONObject> cacheDatas =  (Map<String, JSONObject>) v;
			redisUtil.multiSet(cacheDatas);
			if(StringUtil.isNotEmpty(mqCacheMsgDto.getOperate())) {
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				mqOperateHisDto.setKey(keyName);
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				mqOperateHisDto.setChangeAfter(v);
				mqOperateHisDto.setChangeBefore(mqCacheMsgDto.getChangeBefore());
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
				mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
				this.mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.DELETEROWCOLS.getCode().equals(keyName))
		{//删除行列操作
			MqRCOprepationDto mqRCOprepationDto = JSONObject.parseObject(JSONObject.toJSONString(v),MqRCOprepationDto.class);
			int start = mqRCOprepationDto.getIndex();
			int len = mqRCOprepationDto.getLen();
			int column = mqRCOprepationDto.getColumn();
			int row = mqRCOprepationDto.getRow();
			String rc = mqRCOprepationDto.getRc();
			JSONObject mergeCells = mqRCOprepationDto.getMergeCells();
			JSONObject updateFuncCell = mqRCOprepationDto.getUpdateFuncCell();
			List<String> delKeys = new ArrayList<>();
			List<String> keys = new ArrayList<>();
			Map<String, Object> delCacheDatas = new HashMap<>();//删除单元格的缓存数据
			Map<String, Object> cacheDatas = new HashMap<>();//缓存数据
			List<String> redisKeys = redisUtil.getKeys(RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index);
			if("r".equals(rc))
			{
				if(!ListUtil.isEmpty(redisKeys))
				{
					for (int i = 0; i < redisKeys.size(); i++) {
						int r = Integer.parseInt(redisKeys.get(i).split("_")[redisKeys.get(i).split("_").length-2]);
						if(start<=r && r <=(start+len-1))
						{
							delKeys.add(redisKeys.get(i));
						}else if(r >= start+len)
						{
							keys.add(redisKeys.get(i));
						}
					}
				}
				for (int i = (row-len); i < row; i++) {
					for (int j = 0; j < column; j++) {
						String key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + i + "_" +  j;
						delKeys.add(key);
					}
				}
			}else if("c".equals(rc))
			{
				if(!ListUtil.isEmpty(redisKeys))
				{
					for (int i = 0; i < redisKeys.size(); i++) {
						int c = Integer.parseInt(redisKeys.get(i).split("_")[redisKeys.get(i).split("_").length-1]);
						if(start<=c && c <=(start+len-1))
						{
							delKeys.add(redisKeys.get(i));
						}else if(c >= start+len)
						{
							keys.add(redisKeys.get(i));
						}
					}
				}
				for (int i = 0; i < row; i++) {
					for (int j = (column-len); j < column; j++) {
						String key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + i + "_" +  j;
						delKeys.add(key);
					}
				}
			}
			Map<String, JSONObject> newDataMap = new HashMap<>();
			if(!ListUtil.isEmpty(keys))
			{
				List<Object> redisDatas = redisUtil.multiGet(keys);
				JSONObject redisData = null;
				for (int i = 0; i < redisDatas.size(); i++) {
					redisData = (JSONObject) redisDatas.get(i);
					if(redisData != null)
					{
						int r = redisData.getIntValue("r");
						int c = redisData.getIntValue("c");
						String key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + r + "_" +  c;
						delKeys.add(key);
						cacheDatas.put(r+"_"+c, redisData);
						if("r".equals(rc)) {
							r = r - len;
						}else {
							c = c - len;
						}
						redisData.put("r", r);
						redisData.put("c", c);
						if(redisData.getJSONObject("v") !=null && redisData.getJSONObject("v").containsKey("mc"))
						{
							if(mergeCells.containsKey(r+"_"+c))
							{
								redisData.put("v", mergeCells.get(r+"_"+c));
								mergeCells.remove(r+"_"+c);
							}else {
								redisData.getJSONObject("v").remove("mc");
							}
						}
						key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + r + "_" +  c;
						newDataMap.put(key, redisData);
					}
				}
			}
			if(!StringUtil.isEmptyMap(mergeCells))
			{
				List<String> mergeKeys = new ArrayList<>();
				JSONObject cellData = null;
				for (String key : mergeCells.keySet()) {
					key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + key;
					mergeKeys.add(key);
				}
				List<Object> unProcessedMergeCells = redisUtil.multiGet(mergeKeys);
				JSONObject redisData = null;
				for (int i = 0; i < unProcessedMergeCells.size(); i++) {
					redisData = (JSONObject) unProcessedMergeCells.get(i);
					if(redisData != null)
					{
						int r = redisData.getIntValue("r");
						int c = redisData.getIntValue("c");
						long id = redisData.getLongValue("id");
						cellData = new JSONObject();
						cellData.put("r", r);
						cellData.put("c", c);
						cellData.put("v", mergeCells.getJSONObject(r+"_"+c));
						cellData.put("id", id);
						String key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + r + "_" +  c;
						newDataMap.put(key, cellData);
						
					}
				}
			}
			if(!StringUtil.isEmptyMap(updateFuncCell))
			{
				List<String> funcKeys = new ArrayList<>();
				for (String key : updateFuncCell.keySet()) {
					String  redisKey = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + key;
					if(newDataMap.containsKey(redisKey))
					{
						newDataMap.get(redisKey).put("v", updateFuncCell.get(key));
					}else {
						funcKeys.add(redisKey);
					}
				}
				if(!ListUtil.isEmpty(funcKeys))
				{
					List<Object> list = redisUtil.multiGet(funcKeys);
					JSONObject redisData = null;
					for (int i = 0; i < list.size(); i++) {
						redisData = (JSONObject) list.get(i);
						if(redisData != null) {
							int r = redisData.getIntValue("r");
							int c = redisData.getIntValue("c");
							String key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + r + "_" +  c;
							redisData.put("v", updateFuncCell.get(r + "_" +  c));
							newDataMap.put(key, redisData);
						}
					}
				}
			}
			if(!ListUtil.isEmpty(delKeys))
			{
				List<Object> delObjs = redisUtil.multiGet(delKeys);
				if(!ListUtil.isEmpty(delObjs))
				{
					JSONObject data = null;
					for (int i = 0; i < delObjs.size(); i++) {
						if(delObjs.get(i) != null)
						{
							data = (JSONObject) delObjs.get(i);
							int r = data.getIntValue("r");
							int c = data.getIntValue("c");
							delCacheDatas.put(r+"_"+c, data);
						}
					}
				}
				redisUtil.del(delKeys);
			}
			if(!StringUtil.isEmptyMap(newDataMap))
			{
				redisUtil.multiSet(newDataMap);
			}
			MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
			mqOperateHisDto.setKey(MqTypeEnums.DELETEROWCOLS.getCode());
			mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
			mqOperateHisDto.setChangeAfter(mqRCOprepationDto);
			mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
			mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
			mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
			this.mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
		}else if(MqTypeEnums.ADDROWCOLS.getCode().equals(keyName)) {
			//添加行列操作
			MqRCOprepationDto mqRCOprepationDto = JSONObject.parseObject(JSONObject.toJSONString(v),MqRCOprepationDto.class);
			int start = mqRCOprepationDto.getIndex();
			int len = mqRCOprepationDto.getLen();
			int column = mqRCOprepationDto.getColumn();
			int row = mqRCOprepationDto.getRow();
			String rc = mqRCOprepationDto.getRc();
			String direction = mqRCOprepationDto.getDirection();
			JSONObject mergeCells = mqRCOprepationDto.getMergeCells();
			JSONObject updateFuncCell = mqRCOprepationDto.getUpdateFuncCell();
			List<String> keys = new ArrayList<>();
			List<String> redisKeys = redisUtil.getKeys(RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index);
			if("r".equals(rc))
			{//添加行
				int startRow = 0;
				if("lefttop".equals(direction))
				{//向上
					startRow = start;
				}else {
					//向下
					startRow = start+1;
				}
				for (int i = 0; i < redisKeys.size(); i++) {
					int r = Integer.parseInt(redisKeys.get(i).split("_")[redisKeys.get(i).split("_").length-2]);
					if(r >= startRow)
					{
						keys.add(redisKeys.get(i));
					}
				}
			}else {
				//添加列
				int startCol = 0;
				if("lefttop".equals(direction))
				{//向左
					startCol = start;
				}else {
					//向右
					startCol = start+1;
				}
				for (int i = 0; i < redisKeys.size(); i++) {
					int c = Integer.parseInt(redisKeys.get(i).split("_")[redisKeys.get(i).split("_").length-1]);
					if(c >= startCol)
					{
						keys.add(redisKeys.get(i));
					}
				}
			}
			List<String> delKeys = new ArrayList<>();
			Map<String, JSONObject> newDataMap = new HashMap<>();
			if(!ListUtil.isEmpty(keys))
			{
				List<Object> redisDatas = redisUtil.multiGet(keys);
				JSONObject redisData = null;
				for (int i = 0; i < redisDatas.size(); i++) {
					redisData = (JSONObject) redisDatas.get(i);
					if(redisData != null)
					{
						int r = redisData.getIntValue("r");
						int c = redisData.getIntValue("c");
						String key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + r + "_" +  c;
						delKeys.add(key);
						if("r".equals(rc)) {
							r = r+ len;
						}else {
							c = c + len;
						}
						redisData.put("r", r);
						redisData.put("c", c);
						if(redisData.getJSONObject("v") !=null && redisData.getJSONObject("v").containsKey("mc"))
						{
							if(mergeCells.containsKey(r+"_"+c))
							{
								redisData.put("v", mergeCells.get(r+"_"+c));
								mergeCells.remove(r+"_"+c);
							}else {
								redisData.getJSONObject("v").remove("mc");
							}
						}
						key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + r + "_" +  c;
						newDataMap.put(key, redisData);
					}
				}
			}
			if(!StringUtil.isEmptyMap(mergeCells)) {
				List<String> mergeKeys = new ArrayList<>();
				JSONObject cellData = null;
				for (String key : mergeCells.keySet()) {
					key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + key;
					mergeKeys.add(key);
				}
				List<Object> unProcessedMergeCells = redisUtil.multiGet(mergeKeys);
				JSONObject redisData = null;
				for (int i = 0; i < unProcessedMergeCells.size(); i++) {
					redisData = (JSONObject) unProcessedMergeCells.get(i);
					if(redisData != null)
					{
						int r = redisData.getIntValue("r");
						int c = redisData.getIntValue("c");
						long id = redisData.getLongValue("id");
						cellData = new JSONObject();
						cellData.put("r", r);
						cellData.put("c", c);
						cellData.put("v", mergeCells.getJSONObject(r+"_"+c));
						cellData.put("id", id);
						String key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + r + "_" +  c;
						newDataMap.put(key, cellData);
					}
				}
			}
			if(!StringUtil.isEmptyMap(updateFuncCell))
			{
				List<String> funcKeys = new ArrayList<>();
				for (String key : updateFuncCell.keySet()) {
					String  redisKey = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + key;
					if(newDataMap.containsKey(redisKey))
					{
						newDataMap.get(redisKey).put("v", updateFuncCell.get(key));
					}else {
						funcKeys.add(redisKey);
					}
				}
				if(!ListUtil.isEmpty(funcKeys))
				{
					List<Object> list = redisUtil.multiGet(funcKeys);
					JSONObject redisData = null;
					for (int i = 0; i < list.size(); i++) {
						redisData = (JSONObject) list.get(i);
						if(redisData != null) {
							int r = redisData.getIntValue("r");
							int c = redisData.getIntValue("c");
							String key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" + r + "_" +  c;
							redisData.put("v", updateFuncCell.get(r + "_" +  c));
							newDataMap.put(key, redisData);
						}
					}
				}
			}
			if(!ListUtil.isEmpty(delKeys))
			{
				redisUtil.del(delKeys);
			}
			if(!StringUtil.isEmptyMap(newDataMap))
			{
				redisUtil.multiSet(newDataMap);
			}
			MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
			mqOperateHisDto.setKey(MqTypeEnums.ADDROWCOLS.getCode());
			mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
			mqOperateHisDto.setChangeAfter(v);
			mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
			mqOperateHisDto.setListId(mqCacheMsgDto.getListId());
			mqOperateHisDto.setIndex(mqCacheMsgDto.getIndex());
			this.mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
		}else if(MqTypeEnums.DELSHEETCELL.getCode().equals(keyName))
		{
			String keyPattern = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" + index + "_";
			List<String> keys = redisUtil.getKeys(keyPattern);
			redisUtil.del(keys);
		}else if(MqTypeEnums.INITSHEETCONFIG.getCode().equals(keyName))
		{
			redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,v.toString());
		}else if(MqTypeEnums.DELDOC.getCode().equals(keyName))
		{
			String keyPattern = RedisPrefixEnum.DOCOMENTDATA.getCode() +listId + "_";
			List<String> keys = redisUtil.getKeys(keyPattern);
			redisUtil.del(keys);
			keyPattern = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_";
			keys = redisUtil.getKeys(keyPattern);
			redisUtil.del(keys);
		}else if(MqTypeEnums.INSERTCHART.getCode().equals(keyName))
		{
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null) {
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				String chartStr = luckysheet.getChart();
				JSONArray charts = null;
				if(StringUtil.isNotEmpty(chartStr)) {
					charts = JSON.parseArray(chartStr);
				}else {
					charts = new JSONArray();
				}
				charts.add(v);
				luckysheet.setChart(JSON.toJSONString(charts));
				redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
				mqOperateHisDto.setChangeBefore(chartStr);
				mqOperateHisDto.setChangeAfter(luckysheet.getChart());
				mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
				mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
				mqOperateHisDto.setListId(listId);
				mqOperateHisDto.setIndex(index);
				mqOperateHisDto.setKey(keyName);
				mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
			}
		}else if(MqTypeEnums.MOVECHART.getCode().equals(keyName) || MqTypeEnums.CHANGECHARTRANGE.getCode().equals(keyName) || MqTypeEnums.UPDATECHART.getCode().equals(keyName))
		{
			JSONObject _v = JSON.parseObject(JSON.toJSONString(v));
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null) {
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				String chartStr = luckysheet.getChart();
				JSONArray charts = null;
				if(StringUtil.isNotEmpty(chartStr)) {
					charts = JSON.parseArray(chartStr);
				}else {
					charts = new JSONArray();
				}
				if(ListUtil.isNotEmpty(charts)) {
					String vchartId = _v.getString("chart_id");
					for (int i = 0; i < charts.size(); i++) {
						String chartId = charts.getJSONObject(i).getString("chart_id");
						if(chartId.equals(vchartId)) {
							if(MqTypeEnums.MOVECHART.getCode().equals(keyName)) {
								charts.getJSONObject(i).put("left", _v.getDoubleValue("left"));
								charts.getJSONObject(i).put("top", _v.getDoubleValue("top"));
								charts.getJSONObject(i).put("width", _v.getDoubleValue("width"));
								charts.getJSONObject(i).put("height", _v.getDoubleValue("height"));
							}
							charts.getJSONObject(i).put("chartOptions", v);
							luckysheet.setChart(JSON.toJSONString(charts));
							redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
							mqOperateHisDto.setChangeBefore(chartStr);
							mqOperateHisDto.setChangeAfter(luckysheet.getChart());
							mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
							mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
							mqOperateHisDto.setListId(listId);
							mqOperateHisDto.setIndex(index);
							mqOperateHisDto.setKey(keyName);
							mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
							break;
						}
					}
				}
			}
		}else if(MqTypeEnums.DELETECHART.getCode().equals(keyName))
		{
			JSONObject _v = JSON.parseObject(JSON.toJSONString(v));
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			if(data != null) {
				MqOperateHisDto mqOperateHisDto = new MqOperateHisDto(); 
				luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
				String chartStr = luckysheet.getChart();
				JSONArray charts = null;
				if(StringUtil.isNotEmpty(chartStr)) {
					charts = JSON.parseArray(chartStr);
				}else {
					charts = new JSONArray();
				}
				if(ListUtil.isNotEmpty(charts)) {
					String vchartId = _v.getString("chart_id");
					for (int i = 0; i < charts.size(); i++) {
						String chartId = charts.getJSONObject(i).getString("chart_id");
						if(chartId.equals(vchartId)) {
							charts.remove(i);
							luckysheet.setChart(JSON.toJSONString(charts));
							redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
							mqOperateHisDto.setChangeBefore(chartStr);
							mqOperateHisDto.setChangeAfter(luckysheet.getChart());
							mqOperateHisDto.setOperate(mqCacheMsgDto.getOperate());
							mqOperateHisDto.setToken(mqCacheMsgDto.getToken());
							mqOperateHisDto.setListId(listId);
							mqOperateHisDto.setIndex(index);
							mqOperateHisDto.setKey(keyName);
							mqProcessService.saveOperateHis(mqOperateHisDto, listId, index);
							break;
						}
					}
				}
			}
		}
		redisUtil.del(redisK);
	}
}
