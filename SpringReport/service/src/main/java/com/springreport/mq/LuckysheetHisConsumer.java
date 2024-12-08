package com.springreport.mq;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.springreport.api.coedit.ICoeditService;
import com.springreport.api.luckysheetcell.ILuckysheetCellService;
import com.springreport.api.luckysheethis.ILuckysheetHisService;
import com.springreport.api.mqfailedmsg.IMqFailedMsgService;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.dto.coedit.MqConfigDto;
import com.springreport.dto.coedit.MqOperateHisDto;
import com.springreport.dto.coedit.MqRCOprepationDto;
import com.springreport.entity.luckysheet.Luckysheet;
import com.springreport.entity.luckysheetcell.LuckysheetCell;
import com.springreport.entity.luckysheethis.LuckysheetHis;
import com.springreport.entity.mqfailedmsg.MqFailedMsg;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.LuckysheetFormatEnum;
import com.springreport.enums.LuckysheetHtEnums;
import com.springreport.enums.LuckysheetTbEnums;
import com.springreport.enums.LuckysheetTrEnums;
import com.springreport.enums.LuckysheetVtEnums;
import com.springreport.enums.MqTypeEnums;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.mapper.luckysheet.LuckysheetMapper;
import com.springreport.mapper.luckysheetcell.LuckysheetCellMapper;
import com.springreport.mapper.luckysheethis.LuckysheetHisMapper;
import com.springreport.util.JWTUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.Md5Util;
import com.springreport.util.MessageUtil;
import com.springreport.util.Pako_GzipUtils;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RocketMQMessageListener(topic = Constants.MQ_TPOIC_LUCKYSHEET_HIS, consumerGroup = "luckysheetHis",consumeMode = ConsumeMode.ORDERLY,maxReconsumeTimes=3)
public class LuckysheetHisConsumer implements RocketMQListener<String>{
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private LuckysheetHisMapper luckysheetHisMapper;
	
	@Autowired
	private ILuckysheetHisService iLuckysheetHisService;
	
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
	
	private void doProcess(String uncompressData,String redisKey) {
		MqOperateHisDto mqOperateHisDto = JSON.parseObject(uncompressData, MqOperateHisDto.class);
		String keyName = mqOperateHisDto.getKey();
		LuckysheetHis luckysheetHis = new LuckysheetHis();
		UserInfoDto userInfoDto = JWTUtil.getUserInfo(mqOperateHisDto.getToken());
		if(userInfoDto != null)
		{
			luckysheetHis.setOperator(userInfoDto.getUserName());
			luckysheetHis.setCreator(userInfoDto.getUserId());
			luckysheetHis.setUpdater(userInfoDto.getUserId());
			luckysheetHis.setCreateTime(new Date());
			luckysheetHis.setUpdateTime(new Date());
		}
		if(MqTypeEnums.UPDATEBORDERINFO.getCode().equals(keyName))
		{//更新边框信息
			String changeAfter = mqOperateHisDto.getChangeAfter()==null?String.valueOf(new JSONArray()):String.valueOf(mqOperateHisDto.getChangeAfter());
			String changeBefore = (mqOperateHisDto.getChangeBefore()==null||"null".equals(mqOperateHisDto.getChangeBefore()))?String.valueOf(new JSONArray()):String.valueOf(mqOperateHisDto.getChangeBefore());
			if(!changeAfter.equals(changeBefore))
			{
				luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
				luckysheetHis.setListId(mqOperateHisDto.getListId());
				luckysheetHis.setBson(changeAfter);
				luckysheetHis.setBeforeJson(changeBefore);
				luckysheetHis.setChangeDesc("更新边框信息，操作："+mqOperateHisDto.getOperate());
				luckysheetHis.setRemark(mqOperateHisDto.getOperate());
				luckysheetHis.setType(1);
				luckysheetHis.setOperateKey("borderInfo");
				this.luckysheetHisMapper.insert(luckysheetHis);
			}
		}else if(MqTypeEnums.UPDATEHYPERLINK.getCode().equals(keyName)) {
			//更新超链接信息
			String changeAfter = mqOperateHisDto.getChangeAfter()==null?String.valueOf(new JSONObject()):String.valueOf(mqOperateHisDto.getChangeAfter());
			String changeBefore = (mqOperateHisDto.getChangeBefore()==null||"null".equals(mqOperateHisDto.getChangeBefore()))?String.valueOf(new JSONObject()):String.valueOf(mqOperateHisDto.getChangeBefore());
			if(!changeAfter.equals(changeBefore))
			{
				luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
				luckysheetHis.setListId(mqOperateHisDto.getListId());
				luckysheetHis.setBson(changeAfter);
				luckysheetHis.setBeforeJson(changeBefore);
				luckysheetHis.setChangeDesc("更新超链接信息，操作："+mqOperateHisDto.getOperate());
				luckysheetHis.setRemark(mqOperateHisDto.getOperate());
				luckysheetHis.setType(1);
				luckysheetHis.setOperateKey("hyperlink");
				this.luckysheetHisMapper.insert(luckysheetHis);
			}
		}else if(MqTypeEnums.UPDATEIMAGES.getCode().equals(keyName)) {
			//更新图片信息
			String changeAfter = mqOperateHisDto.getChangeAfter()==null?String.valueOf(new JSONObject()):String.valueOf(mqOperateHisDto.getChangeAfter());
			String changeBefore = (mqOperateHisDto.getChangeBefore()==null||"null".equals(mqOperateHisDto.getChangeBefore()))?String.valueOf(new JSONObject()):String.valueOf(mqOperateHisDto.getChangeBefore());
			if(!changeAfter.equals(changeBefore))
			{
				luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
				luckysheetHis.setListId(mqOperateHisDto.getListId());
				luckysheetHis.setBson(changeAfter);
				luckysheetHis.setBeforeJson(changeBefore);
				luckysheetHis.setChangeDesc("更新图片信息，操作："+mqOperateHisDto.getOperate());
				luckysheetHis.setRemark(mqOperateHisDto.getOperate());
				luckysheetHis.setType(1);
				luckysheetHis.setOperateKey("image");
				this.luckysheetHisMapper.insert(luckysheetHis);
			}
		}else if(MqTypeEnums.UPDATEFROZEN.getCode().equals(keyName)) {
			//更新冻结信息
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(String.valueOf(mqOperateHisDto.getChangeBefore()));
			luckysheetHis.setChangeDesc("更新冻结信息，操作："+mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("frozen");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.UPDATEFILTERSELECT.getCode().equals(keyName)) {
			//更新筛选条件
			String changeAfter = mqOperateHisDto.getChangeAfter()==null?String.valueOf(new JSONObject()):String.valueOf(mqOperateHisDto.getChangeAfter());
			String changeBefore = (mqOperateHisDto.getChangeBefore()==null||"null".equals(mqOperateHisDto.getChangeBefore()))?String.valueOf(new JSONObject()):String.valueOf(mqOperateHisDto.getChangeBefore());
			if(!changeAfter.equals(changeBefore))
			{
				luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
				luckysheetHis.setListId(mqOperateHisDto.getListId());
				luckysheetHis.setBson(changeAfter);
				luckysheetHis.setBeforeJson(changeBefore);
				luckysheetHis.setChangeDesc("更新筛选信息，操作："+mqOperateHisDto.getOperate());
				luckysheetHis.setRemark(mqOperateHisDto.getOperate());
				luckysheetHis.setType(1);
				luckysheetHis.setOperateKey("filter");
				this.luckysheetHisMapper.insert(luckysheetHis);
			}
		}else if(MqTypeEnums.UPDATEFILTER.getCode().equals(keyName)) {
			//更新筛选信息
			String changeAfter = mqOperateHisDto.getChangeAfter()==null?String.valueOf(new JSONObject()):String.valueOf(mqOperateHisDto.getChangeAfter());
			String changeBefore = (mqOperateHisDto.getChangeBefore()==null||"null".equals(mqOperateHisDto.getChangeBefore()))?String.valueOf(new JSONObject()):String.valueOf(mqOperateHisDto.getChangeBefore());
			if(!changeAfter.equals(changeBefore))
			{
				luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
				luckysheetHis.setListId(mqOperateHisDto.getListId());
				luckysheetHis.setBson(changeAfter);
				luckysheetHis.setBeforeJson(changeBefore);
				luckysheetHis.setChangeDesc("更新筛选信息，操作："+mqOperateHisDto.getOperate());
				luckysheetHis.setRemark(mqOperateHisDto.getOperate());
				luckysheetHis.setType(1);
				luckysheetHis.setOperateKey("filter_select");
				this.luckysheetHisMapper.insert(luckysheetHis);
			}
		}else if(MqTypeEnums.CLEARFILTER.getCode().equals(keyName)) {
			//清除筛选
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(String.valueOf(mqOperateHisDto.getChangeBefore()));
			luckysheetHis.setChangeDesc("清除筛选，操作："+mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("fsc");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.UPDATEDATAVERIFICATION.getCode().equals(keyName)){
			//数据验证
			String changeAfter = mqOperateHisDto.getChangeAfter()==null?String.valueOf(new JSONObject()):String.valueOf(mqOperateHisDto.getChangeAfter());
			String changeBefore = (mqOperateHisDto.getChangeBefore()==null||"null".equals(mqOperateHisDto.getChangeBefore()))?String.valueOf(new JSONObject()):String.valueOf(mqOperateHisDto.getChangeBefore());
			if(!changeAfter.equals(changeBefore))
			{
				luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
				luckysheetHis.setListId(mqOperateHisDto.getListId());
				luckysheetHis.setBson(changeAfter);
				luckysheetHis.setBeforeJson(changeBefore);
				luckysheetHis.setChangeDesc("更新数据验证信息，操作："+mqOperateHisDto.getOperate());
				luckysheetHis.setRemark(mqOperateHisDto.getOperate());
				luckysheetHis.setType(1);
				luckysheetHis.setOperateKey("dataverification");
				this.luckysheetHisMapper.insert(luckysheetHis);
			}
		}else if(MqTypeEnums.UPDATELUCKYSHEETALTERNATEFORMATSAVE.getCode().equals(keyName)) {
			//交替颜色
			String changeAfter = mqOperateHisDto.getChangeAfter()==null?String.valueOf(new JSONArray()):String.valueOf(mqOperateHisDto.getChangeAfter());
			String changeBefore = (mqOperateHisDto.getChangeBefore()==null||"null".equals(mqOperateHisDto.getChangeBefore()))?String.valueOf(new JSONArray()):String.valueOf(mqOperateHisDto.getChangeBefore());
			if(!changeAfter.equals(changeBefore))
			{
				luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
				luckysheetHis.setListId(mqOperateHisDto.getListId());
				luckysheetHis.setBson(changeAfter);
				luckysheetHis.setBeforeJson(changeBefore);
				luckysheetHis.setChangeDesc(mqOperateHisDto.getOperate());
				luckysheetHis.setRemark(mqOperateHisDto.getOperate());
				luckysheetHis.setType(1);
				luckysheetHis.setOperateKey("alternateformatSave");
				this.luckysheetHisMapper.insert(luckysheetHis);
			}
		}else if(MqTypeEnums.UPDATEROWHIDDEN.getCode().equals(keyName)) {
			//隐藏行
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(String.valueOf(mqOperateHisDto.getChangeBefore()));
			luckysheetHis.setChangeDesc("更新隐藏行信息，操作："+mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("rowhidden");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.UPDATECOLHIDDEN.getCode().equals(keyName)) {
			//隐藏列
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(String.valueOf(mqOperateHisDto.getChangeBefore()));
			luckysheetHis.setChangeDesc("更新隐藏列信息，操作："+mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("colhidden");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.UPDATEROWLEN.getCode().equals(keyName)) {
			//更新行高
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(String.valueOf(mqOperateHisDto.getChangeBefore()));
			luckysheetHis.setChangeDesc("更新行高，操作："+mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("rowlen");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.UPDATECOLLEN.getCode().equals(keyName)) {
			//更新列宽
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(String.valueOf(mqOperateHisDto.getChangeBefore()));
			luckysheetHis.setChangeDesc("更新列宽，操作："+mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("collen");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.UPDATEROW.getCode().equals(keyName)) {
			//更新行数
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(String.valueOf(mqOperateHisDto.getChangeBefore()));
			luckysheetHis.setChangeDesc("更新总行数，总行数由"+mqOperateHisDto.getChangeBefore()+"变为"+mqOperateHisDto.getChangeAfter()+"，操作："+mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("row");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.UPDATECOLUMN.getCode().equals(keyName))
		{//更新列数
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(String.valueOf(mqOperateHisDto.getChangeBefore()));
			luckysheetHis.setChangeDesc("更新总列数，总列数由"+mqOperateHisDto.getChangeBefore()+"变为"+mqOperateHisDto.getChangeAfter()+"，操作："+mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("column");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if("merge".equals(keyName)) {
			//更新合并信息
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(String.valueOf(mqOperateHisDto.getChangeBefore()));
			luckysheetHis.setChangeDesc("合并单元格信息更新，操作："+mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("merge");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.DELETEROWCOLS.getCode().equals(keyName)) {
			MqRCOprepationDto mqRCOprepationDto = JSONObject.parseObject(JSONObject.toJSONString(mqOperateHisDto.getChangeAfter()),MqRCOprepationDto.class);
			int start = mqRCOprepationDto.getIndex();
			int len = mqRCOprepationDto.getLen();
			String rc = mqRCOprepationDto.getRc();
			String index = mqOperateHisDto.getIndex();
			String listId = mqOperateHisDto.getListId();
			JSONObject params = new JSONObject();
			params.put("index", index);
			params.put("listId", listId);
			params.put("len", len);
			params.put("start", start);
			//删除对应的单元格数据
			if("r".equals(rc))
			{
				luckysheetHis.setOperateKey("dr");
				this.luckysheetHisMapper.delRowDelCells(params);
			}else {
				luckysheetHis.setOperateKey("dc");
				this.luckysheetHisMapper.delColDelCells(params);
			}
			//更新坐标
			if("r".equals(rc))
			{
				this.luckysheetHisMapper.delRowUpdate(params);
			}else {
				this.luckysheetHisMapper.delColUpdate(params);
			}
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(String.valueOf(mqOperateHisDto.getChangeBefore()));
			luckysheetHis.setChangeDesc("删除行列信息，操作："+mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.ADDROWCOLS.getCode().equals(keyName)) {
			//新增行列信息
			MqRCOprepationDto mqRCOprepationDto = JSONObject.parseObject(JSONObject.toJSONString(mqOperateHisDto.getChangeAfter()),MqRCOprepationDto.class);
			int start = mqRCOprepationDto.getIndex();
			int len = mqRCOprepationDto.getLen();
			String rc = mqRCOprepationDto.getRc();
			String direction = mqRCOprepationDto.getDirection();
			String index = mqOperateHisDto.getIndex();
			String listId = mqOperateHisDto.getListId();
			JSONObject params = new JSONObject();
			params.put("index", index);
			params.put("listId", listId);
			params.put("len", len);
			if("lefttop".equals(direction))
			{
				params.put("start", start);
			}else {
				params.put("start", start+1);
			}
			//更新坐标
			if("r".equals(rc)) {
				luckysheetHis.setOperateKey("ar");
				this.luckysheetHisMapper.addRowUpdate(params);
			}else {
				luckysheetHis.setOperateKey("ac");
				this.luckysheetHisMapper.addColUpdate(params);
			}
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(null);
			luckysheetHis.setBeforeJson(null);
			luckysheetHis.setChangeDesc("增加行列信息，操作："+mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.UPDATENAME.getCode().equals(keyName)) {
			//更新sheet名称
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(String.valueOf(mqOperateHisDto.getChangeBefore()));
			luckysheetHis.setChangeDesc(mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("name");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.UPDATECOLOR.getCode().equals(keyName)) {
			//更新sheet颜色
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(String.valueOf(mqOperateHisDto.getChangeBefore()));
			luckysheetHis.setChangeDesc(mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("color");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.UPDATEHIDESTATUS.getCode().equals(keyName)) {
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setChangeDesc(mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("sha");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.ADDSHEET.getCode().equals(keyName)) {
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(String.valueOf(mqOperateHisDto.getChangeBefore()));
			luckysheetHis.setChangeDesc(mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("sh");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.UPDATEORDER.getCode().equals(keyName)) {
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setChangeDesc(mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("shr");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}else if(MqTypeEnums.COPYSHEET.getCode().equals(keyName)) {
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setChangeDesc(mqOperateHisDto.getOperate());
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(1);
			luckysheetHis.setOperateKey("shc");
			this.luckysheetHisMapper.insert(luckysheetHis);
		}
		else if(MqTypeEnums.UPDATECELLDATA.getCode().equals(keyName)) {
			//更新单元格数据
			luckysheetHis.setRowNo(Integer.parseInt(mqOperateHisDto.getRowCol().split("_")[0]));
			luckysheetHis.setColNo(Integer.parseInt(mqOperateHisDto.getRowCol().split("_")[1]));
			luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
			luckysheetHis.setListId(mqOperateHisDto.getListId());
			luckysheetHis.setBson(String.valueOf(mqOperateHisDto.getChangeAfter()));
			luckysheetHis.setBeforeJson(mqOperateHisDto.getChangeBefore() !=null?String.valueOf(mqOperateHisDto.getChangeBefore()):"");
			luckysheetHis.setRemark(mqOperateHisDto.getOperate());
			luckysheetHis.setType(2);
			luckysheetHis.setOperateKey("v");
			String changeDesc = "";
			if(mqOperateHisDto.getChangeBefore() == null)
			{
				changeDesc = getCellDataDiffMsg((JSONObject)mqOperateHisDto.getChangeAfter(),new JSONObject(),mqOperateHisDto.getOperate());
			}else if(mqOperateHisDto.getChangeAfter() == null) {
				changeDesc = MessageUtil.getValue("luckysheet.cell.clear");
			}else {
				changeDesc = getCellDataDiffMsg((JSONObject)mqOperateHisDto.getChangeAfter(),(JSONObject)mqOperateHisDto.getChangeBefore(),mqOperateHisDto.getOperate());
			}
			luckysheetHis.setChangeDesc(changeDesc+"操作："+mqOperateHisDto.getOperate());
			if(StringUtil.isNotEmpty(changeDesc))
			{
				this.luckysheetHisMapper.insert(luckysheetHis);
			}
		}else if(MqTypeEnums.BATCHINSERTCELLDATA.getCode().equals(keyName)) {
			Map<String, JSONObject> cacheDatas =  (Map<String, JSONObject>) mqOperateHisDto.getChangeAfter();
			Map<String, JSONObject> changeBefore = mqOperateHisDto.getChangeBefore()!=null?(Map<String, JSONObject>) mqOperateHisDto.getChangeBefore():new HashMap<String, JSONObject>();
			List<LuckysheetHis> hises = new ArrayList<>();
			LuckysheetHis his = null;
			for (String key : cacheDatas.keySet()) {
				his = new LuckysheetHis();
				String r = key.split("_")[key.split("_").length-2];
				String c = key.split("_")[key.split("_").length-1];
				his.setRowNo(Integer.parseInt(r));
				his.setColNo(Integer.parseInt(c));
				his.setSheetIndex(mqOperateHisDto.getIndex());
				his.setListId(mqOperateHisDto.getListId());
				his.setBson(JSON.toJSONString(cacheDatas.get(key)));
				JSONObject before = changeBefore.get(r+"_"+c)==null?new JSONObject():changeBefore.get(r+"_"+c);
				his.setBeforeJson(JSON.toJSONString(before));
				String changeDesc = this.getCellDataDiffMsg(cacheDatas.get(key), before, mqOperateHisDto.getOperate());
				if(StringUtil.isNotEmpty(changeDesc))
				{
					his.setChangeDesc(changeDesc+"操作："+mqOperateHisDto.getOperate());
					his.setRemark(mqOperateHisDto.getOperate());
					his.setType(2);
					his.setOperateKey("bv");
					if(userInfoDto != null)
					{
						his.setOperator(userInfoDto.getUserName());
						his.setCreator(userInfoDto.getUserId());
						his.setUpdater(userInfoDto.getUserId());
						his.setCreateTime(new Date());
						his.setUpdateTime(new Date());
					}
					hises.add(his);
				}
			}
			if(!ListUtil.isEmpty(hises))
			{
				this.iLuckysheetHisService.saveBatch(hises);
			}
		}else if(MqTypeEnums.BATCHDELETECELLDATA.getCode().equals(keyName)) {
			//批量删除数据
			List<LuckysheetHis> hises = new ArrayList<>();
			LuckysheetHis his = null;
			List<String> ids = (List<String>) mqOperateHisDto.getChangeAfter();
			Map<String, JSONObject> changeBefore = mqOperateHisDto.getChangeBefore()!=null?(Map<String, JSONObject>) mqOperateHisDto.getChangeBefore():new HashMap<String, JSONObject>();
			for (int i = 0; i < ids.size(); i++) {
				String r = ids.get(i).split("_")[ids.get(i).split("_").length-2];
				String c = ids.get(i).split("_")[ids.get(i).split("_").length-1];
				if(changeBefore.get(r+"_"+c)==null)
				{
					break;
				}
				his = new LuckysheetHis();
				his.setRowNo(Integer.parseInt(r));
				his.setColNo(Integer.parseInt(c));
				his.setSheetIndex(mqOperateHisDto.getIndex());
				his.setListId(mqOperateHisDto.getListId());
				his.setBson(null);
				JSONObject before = changeBefore.get(r+"_"+c)==null?new JSONObject():changeBefore.get(r+"_"+c);
				his.setBeforeJson(JSON.toJSONString(before));
				String changeDesc = this.getCellDataDiffMsg(new JSONObject(), before, mqOperateHisDto.getOperate());
				if(StringUtil.isNotEmpty(changeDesc))
				{
					his.setChangeDesc(changeDesc+"操作："+mqOperateHisDto.getOperate());
					his.setRemark(mqOperateHisDto.getOperate());
					his.setType(2);
					his.setOperateKey("bv");
					if(userInfoDto != null)
					{
						his.setOperator(userInfoDto.getUserName());
						his.setCreator(userInfoDto.getUserId());
						his.setUpdater(userInfoDto.getUserId());
						his.setCreateTime(new Date());
						his.setUpdateTime(new Date());
					}
					hises.add(his);
				}
			}
			if(!ListUtil.isEmpty(hises))
			{
				this.iLuckysheetHisService.saveBatch(hises);
			}
		}else if(MqTypeEnums.INSERTCHART.getCode().equals(keyName) || MqTypeEnums.MOVECHART.getCode().equals(keyName) || MqTypeEnums.CHANGECHARTRANGE.getCode().equals(keyName)
				|| MqTypeEnums.UPDATECHART.getCode().equals(keyName) || MqTypeEnums.DELETECHART.getCode().equals(keyName))
		{
			String changeAfter = mqOperateHisDto.getChangeAfter()==null?String.valueOf(new JSONArray()):String.valueOf(mqOperateHisDto.getChangeAfter());
			String changeBefore = (mqOperateHisDto.getChangeBefore()==null||"null".equals(mqOperateHisDto.getChangeBefore()))?String.valueOf(new JSONArray()):String.valueOf(mqOperateHisDto.getChangeBefore());
			if(!changeAfter.equals(changeBefore))
			{
				luckysheetHis.setSheetIndex(mqOperateHisDto.getIndex());
				luckysheetHis.setListId(mqOperateHisDto.getListId());
				luckysheetHis.setBson(changeAfter);
				luckysheetHis.setBeforeJson(changeBefore);
				luckysheetHis.setChangeDesc("更新图表信息，操作："+mqOperateHisDto.getOperate());
				luckysheetHis.setRemark(mqOperateHisDto.getOperate());
				luckysheetHis.setType(1);
				luckysheetHis.setOperateKey(keyName);
				this.luckysheetHisMapper.insert(luckysheetHis);
			}
		}
		redisUtil.del(redisKey);
	}
	
	/**  
	 * @MethodName: getCellDataDiffMsg
	 * @Description: 获取单元格数据的比较信息
	 * @author caiyang
	 * @param newData
	 * @param beforeData
	 * @return String
	 * @date 2023-11-22 07:51:45 
	 */ 
	private String getCellDataDiffMsg(JSONObject newData, JSONObject beforeData,String operate) {
		String result = "";
		boolean isReservse = false;//是否反过来比较
		Map<String, JSONObject> diffResult = null;
		diffResult = StringUtil.compare2MapDiff2(newData.getJSONObject("v") == null?new JSONObject():newData.getJSONObject("v"),
				beforeData.getJSONObject("v") == null ? new JSONObject() : beforeData.getJSONObject("v"));
		if(diffResult == null || StringUtil.isEmptyMap(diffResult.get("changeAfter")))
		{
			isReservse = true;
			diffResult = StringUtil.compare2MapDiff2(beforeData.getJSONObject("v") == null ? new JSONObject() : beforeData.getJSONObject("v"), newData.getJSONObject("v")==null?new JSONObject():newData.getJSONObject("v"));
		}
		if(diffResult != null)
		{
			JSONObject changeBefore = diffResult.get("changeBefore");
			JSONObject changeAfter = diffResult.get("changeAfter");
			if(StringUtil.isEmptyMap(changeAfter))
			{
				
			}else {
				if (changeAfter.containsKey("f")) {
					String beforeValue = changeBefore.getString("f");
					if (StringUtil.isNullOrEmpty(beforeValue)) {
						beforeValue = "无公式";
					}
					String afterValue = changeAfter.getString("f");
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "公式", afterValue, beforeValue });
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "公式", beforeValue, afterValue });
					}
				} else if (changeAfter.containsKey("v")) {// 值变化
					String beforeValue = changeBefore.getString("v");
					if (StringUtil.isNullOrEmpty(beforeValue)) {
						beforeValue = "空值";
					}
					String afterValue = changeAfter.getString("v");
					if (StringUtil.isNullOrEmpty(afterValue)) {
						afterValue = "空值";
					}
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "值", afterValue, beforeValue });
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "值", beforeValue, afterValue });
					}
				} else if (changeAfter.containsKey("ff")) {
					// 字体发生变化
					String beforeValue = changeBefore.getString("ff");
					String afterValue = changeAfter.getString("ff");
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "字体", afterValue, beforeValue });
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "字体", beforeValue, afterValue });
					}
				} else if (changeAfter.containsKey("fs")) {
					// 字号大小变化
					String beforeValue = changeBefore.getString("fs");
					if (StringUtil.isNullOrEmpty(beforeValue)) {
						beforeValue = "10";
					}
					String afterValue = changeAfter.getString("fs");
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "字号大小", afterValue, beforeValue });
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "字号大小", beforeValue, afterValue });
					}
					
				} else if (changeAfter.containsKey("bl")) {
					// 字体加粗
					String beforeValue = changeBefore.getString("bl");
					if (StringUtil.isNullOrEmpty(beforeValue)) {
						beforeValue = "0";
					}
					String afterValue = changeAfter.getString("bl");
					if (StringUtil.isNullOrEmpty(afterValue)) {
						afterValue = "0";
					}
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "是否粗体",
								afterValue.equals("0") ? "否" : "是",beforeValue.equals("0") ? "否" : "是"});
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "是否粗体",
								beforeValue.equals("0") ? "否" : "是", afterValue.equals("0") ? "否" : "是" });
					}
					
				} else if (changeAfter.containsKey("it")) {
					// 字体加粗
					String beforeValue = changeBefore.getString("it");
					if (StringUtil.isNullOrEmpty(beforeValue)) {
						beforeValue = "0";
					}
					String afterValue = changeAfter.getString("it");
					if (StringUtil.isNullOrEmpty(afterValue)) {
						afterValue = "0";
					}
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "是否斜体",
								afterValue.equals("0") ? "否" : "是",beforeValue.equals("0") ? "否" : "是" });
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "是否斜体",
								beforeValue.equals("0") ? "否" : "是", afterValue.equals("0") ? "否" : "是" });
					}
					
				} else if (changeAfter.containsKey("cl")) {
					// 删除线
					String beforeValue = changeBefore.getString("cl");
					if (StringUtil.isNullOrEmpty(beforeValue)) {
						beforeValue = "0";
					}
					String afterValue = changeAfter.getString("cl");
					if (StringUtil.isNullOrEmpty(afterValue)) {
						afterValue = "0";
					}
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "删除线",
								afterValue.equals("0") ? "无" : "有",beforeValue.equals("0") ? "无" : "有" });
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "删除线",
								beforeValue.equals("0") ? "无" : "有", afterValue.equals("0") ? "无" : "有" });
					}
					result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "删除线",
							beforeValue.equals("0") ? "无" : "有", afterValue.equals("0") ? "无" : "有" });
				} else if (changeAfter.containsKey("un")) {
					// 下划线
					String beforeValue = changeBefore.getString("un");
					if (StringUtil.isNullOrEmpty(beforeValue)) {
						beforeValue = "0";
					}
					String afterValue = changeAfter.getString("un");
					if (StringUtil.isNullOrEmpty(afterValue)) {
						afterValue = "0";
					}
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "下划线",
								afterValue.equals("0") ? "无" : "有",beforeValue.equals("0") ? "无" : "有" });
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "下划线",
								beforeValue.equals("0") ? "无" : "有", afterValue.equals("0") ? "无" : "有" });
					}
				} else if (changeAfter.containsKey("fc")) {
					// 字体颜色
					String beforeValue = changeBefore.getString("fc");
					if (StringUtil.isNullOrEmpty(beforeValue)) {
						beforeValue = "#000000";
					}
					String afterValue = changeAfter.getString("fc");
					if (StringUtil.isNullOrEmpty(afterValue)) {
						afterValue = "#000000";
					}
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "字体颜色", afterValue, beforeValue });
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "字体颜色", beforeValue, afterValue });
					}
				} else if (changeAfter.containsKey("bg")) {
					// 背景颜色
					String beforeValue = changeBefore.getString("bg");
					if (StringUtil.isNullOrEmpty(beforeValue)) {
						beforeValue = "#ffffff";
					}
					String afterValue = changeAfter.getString("bg");
					if (StringUtil.isNullOrEmpty(afterValue)) {
						afterValue = "#ffffff";
					}
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "背景颜色", afterValue, beforeValue });
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "背景颜色", beforeValue, afterValue });
					}
				} else if (changeAfter.containsKey("ht")) {
					// 水平对齐
					String beforeValue = changeBefore.getString("ht");
					if (StringUtil.isNullOrEmpty(beforeValue)) {
						beforeValue = "1";
					}
					String afterValue = changeAfter.getString("ht");
					if (StringUtil.isNullOrEmpty(afterValue)) {
						afterValue = "1";
					}
					beforeValue = LuckysheetHtEnums.getValue(beforeValue);
					afterValue = LuckysheetHtEnums.getValue(afterValue);
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "水平对齐方式", afterValue, beforeValue });
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "水平对齐方式", beforeValue, afterValue });
					}
				} else if (changeAfter.containsKey("vt")) {
					// 垂直对齐
					String beforeValue = changeBefore.getString("vt");
					if (StringUtil.isNullOrEmpty(beforeValue)) {
						beforeValue = "2";
					}
					String afterValue = changeAfter.getString("vt");
					if (StringUtil.isNullOrEmpty(afterValue)) {
						afterValue = "2";
					}
					beforeValue = LuckysheetVtEnums.getValue(beforeValue);
					afterValue = LuckysheetVtEnums.getValue(afterValue);
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "垂直对齐方式", afterValue, beforeValue });
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "垂直对齐方式", beforeValue, afterValue });
					}
				} else if (changeAfter.containsKey("tb")) {
					// 文本换行
					String beforeValue = changeBefore.getString("tb");
					if (StringUtil.isNullOrEmpty(beforeValue)) {
						beforeValue = "0";
					}
					String afterValue = changeAfter.getString("tb");
					if (StringUtil.isNullOrEmpty(afterValue)) {
						afterValue = "0";
					}
					beforeValue = LuckysheetTbEnums.getValue(beforeValue);
					afterValue = LuckysheetTbEnums.getValue(afterValue);
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "文本换行方式", afterValue, beforeValue });
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "文本换行方式", beforeValue, afterValue });
					}
					
				} else if (changeAfter.containsKey("tr")) {
					// 文本旋转
					String beforeValue = changeBefore.getString("tr");
					if (StringUtil.isNullOrEmpty(beforeValue)) {
						beforeValue = "0";
					}
					String afterValue = changeAfter.getString("tr");
					if (StringUtil.isNullOrEmpty(afterValue)) {
						afterValue = "0";
					}
					beforeValue = LuckysheetTrEnums.getValue(beforeValue);
					afterValue = LuckysheetTrEnums.getValue(afterValue);
					if(isReservse)
					{
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "文本旋转", afterValue, beforeValue });
					}else {
						result = MessageUtil.getValue("luckysheet.cell.change",
								new String[] { "文本旋转", beforeValue, afterValue });
					}
				} else if (changeAfter.containsKey("ps")) {
					result = operate;
				} else if (changeAfter.containsKey("ct")) {
					String beforeValue = "General";
					String afterValue = "General";
					changeAfter.entrySet().removeIf(entry -> entry.getValue() == null);
					changeBefore.entrySet().removeIf(entry -> entry.getValue() == null);
					if("inlineStr".equals(changeAfter.getJSONObject("ct").getString("t")))
					{
						JSONArray s = changeAfter.getJSONObject("ct").getJSONArray("s");
						afterValue = JSONArray.toJSONString(s);
						if(StringUtil.isEmptyMap(changeBefore)) {
							beforeValue = "空值";
						}else if("inlineStr".equals(changeBefore.getJSONObject("ct").getString("t"))) {
							s = changeBefore.getJSONObject("ct").getJSONArray("s");
							beforeValue = JSONArray.toJSONString(s);
						}else {
							beforeValue = beforeData.getString("v");
						}
						if (!afterValue.equals(beforeValue)) {
							String afterMsg = "";
							String beforeMsg = "";
							afterMsg = afterValue;
							beforeMsg = beforeValue;
							if(isReservse)
							{
								result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "值",afterMsg, beforeMsg });
							}else {
								result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "值", beforeMsg, afterMsg });
							}
						}
					}else if("inlineStr".equals(changeBefore.getJSONObject("ct").getString("t"))) {
						JSONArray s = changeBefore.getJSONObject("ct").getJSONArray("s");
						beforeValue = JSONArray.toJSONString(s);
						if(StringUtil.isEmptyMap(changeAfter)) {
							afterValue = "空值";
						}else if("inlineStr".equals(changeAfter.getJSONObject("ct").getString("t"))) {
							s = changeAfter.getJSONObject("ct").getJSONArray("s");
							afterValue = JSONArray.toJSONString(s);
						}
						else {
							afterValue = newData.getString("v");
						}
						if (!afterValue.equals(beforeValue)) {
							String afterMsg = "";
							String beforeMsg = "";
							afterMsg = afterValue;
							beforeMsg = beforeValue;
							if(isReservse)
							{
								result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "值",afterMsg, beforeMsg });
							}else {
								result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "值", beforeMsg, afterMsg });
							}
						}
					}else {
						if (changeBefore.getJSONObject("ct") != null) {
							beforeValue = changeBefore.getJSONObject("ct").getString("fa");
						}
						
						if (changeAfter.getJSONObject("ct") != null) {
							afterValue = changeAfter.getJSONObject("ct").getString("fa");
						}
						if (!afterValue.equals(beforeValue)) {
							String afterMsg = "";
							String beforeMsg = "";
							if (LuckysheetFormatEnum.getValue(afterValue) != null) {
								afterMsg = LuckysheetFormatEnum.getValue(afterValue) + "-" + afterValue;
							} else {
								afterMsg = "更多格式" + "-" + afterValue;
							}
							if (LuckysheetFormatEnum.getValue(beforeValue) != null) {
								beforeMsg = LuckysheetFormatEnum.getValue(beforeValue) + "-" + beforeValue;
							} else {
								beforeMsg = "更多格式" + "-" + beforeValue;
							}
							if(isReservse)
							{
								result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "格式",afterMsg, beforeMsg });
							}else {
								result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "格式", beforeMsg, afterMsg });
							}
						}
					}
				}else if (changeAfter.containsKey("mc")) {
					JSONObject newDataObj = newData.getJSONObject("v");
					String newV = "";
					if(newDataObj != null)
					{
						newV = StringUtil.isNotEmpty(newDataObj.getString("v"))?newDataObj.getString("v"):"空值";
					}
					JSONObject beforeDataObj = beforeData.getJSONObject("v");
					String beforeV = "";
					if(beforeDataObj != null)
					{
						beforeV = StringUtil.isNotEmpty(beforeDataObj.getString("v"))?beforeDataObj.getString("v"):"空值";
					}
					if(!newV.equals(beforeV))
					{
						result = "单元格合并，由【"+beforeV+"】变更为【"+newV+"】";
					}
				}
			}
		}
		
		return result;
	}
}
