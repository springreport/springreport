package com.springreport.mq;


import java.util.ArrayList;
import java.util.Date;
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
import com.springreport.api.luckysheetcell.ILuckysheetCellService;
import com.springreport.api.mqfailedmsg.IMqFailedMsgService;
import com.springreport.constants.Constants;
import com.springreport.dto.coedit.MqConfigDto;
import com.springreport.dto.coedit.MqOperateHisDto;
import com.springreport.dto.coedit.MqRCOprepationDto;
import com.springreport.entity.luckysheet.Luckysheet;
import com.springreport.entity.luckysheetcell.LuckysheetCell;
import com.springreport.entity.mqfailedmsg.MqFailedMsg;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.MqTypeEnums;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.mapper.luckysheet.LuckysheetMapper;
import com.springreport.mapper.luckysheetcell.LuckysheetCellMapper;
import com.springreport.util.ListUtil;
import com.springreport.util.Md5Util;
import com.springreport.util.Pako_GzipUtils;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RocketMQMessageListener(topic = Constants.MQ_TPOIC_LUCKYSHEET, consumerGroup = "luckysheet",consumeMode = ConsumeMode.ORDERLY,maxReconsumeTimes=3)
public class LuckysheetConsumer implements RocketMQListener<String>{
	
	@Autowired
	private LuckysheetMapper luckysheetMapper;
	
	@Autowired
	private LuckysheetCellMapper luckysheetCellMapper;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private ILuckysheetCellService iLuckysheetCellService;
	
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
					failedMsg.setType(2);
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
		MqConfigDto mqConfigDto = JSON.parseObject(uncompressData, MqConfigDto.class);
		String keyName = mqConfigDto.getKeyName();
		Object v = mqConfigDto.getV();
		Long id = mqConfigDto.getId();
		Luckysheet luckysheet = new Luckysheet();
		luckysheet.setId(id);
		if(MqTypeEnums.UPDATEBORDERINFO.getCode().equals(keyName))
		{//更新边框信息
			if(v == null)
			{
				v = new JSONArray();
			}
			luckysheet.setBorderInfo(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATECONFIG.getCode().equals(keyName)) {
			if(v == null || "{}".equals(JSON.toJSONString(v)))
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
					if("merge".equals(key))
					{
						luckysheet.setMergeInfo(JSON.toJSONString(entry.getValue()));
					}else if("rowlen".equals(key))
					{
						luckysheet.setRowlen(JSON.toJSONString(entry.getValue()));
					}else if("columnlen".equals(key)) {
						luckysheet.setColumnlen(JSON.toJSONString(entry.getValue()));
					}else if("rowhidden".equals(key)) {
						luckysheet.setRowhidden(JSON.toJSONString(entry.getValue()));
					}else if("colhidden".equals(key)) {
						luckysheet.setColhidden(JSON.toJSONString(entry.getValue()));
					}else if("borderInfo".equals(key)) {
						luckysheet.setBorderInfo(JSON.toJSONString(entry.getValue()));
					}
				}
			}
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATEHYPERLINK.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONObject();
			}
			luckysheet.setHyperlink(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATEIMAGES.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONObject();
			}
			luckysheet.setImage(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATEFROZEN.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONObject();
			}
			luckysheet.setFrozen(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATEFILTERSELECT.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONObject();
			}
			luckysheet.setFilterSelect(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATEFILTER.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONObject();
			}
			luckysheet.setFilter(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.CLEARFILTER.getCode().equals(keyName)) {
			luckysheet.setFilterSelect(JSON.toJSONString(new JSONObject()));
			luckysheet.setFilter(JSON.toJSONString(new JSONObject()));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATEDATAVERIFICATION.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONObject();
			}
			luckysheet.setDataverification(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATECALCCHAIN.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONArray();
			}
			luckysheet.setCalcchain(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATELUCKYSHEETALTERNATEFORMATSAVE.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONArray();
			}
			luckysheet.setLuckysheetAlternateformatSave(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATELUCKYSHEETCONDITIONFORMATSAVE.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONObject();
			}
			luckysheet.setLuckysheetConditionformatSave(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.ADDFC.getCode().equals(keyName)) {
			JSONObject _v = (JSONObject) v;
			String listId = _v.getString("listId");
			String index = _v.getString("index");
			String blockId = _v.getString("blockId");
			_v.remove("listId");
			_v.remove("blockId");
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
			JSONArray calcChain = JSON.parseArray(luckysheet.getCalcchain());
			if(!ListUtil.isEmpty(calcChain))
			{
				boolean isExist = false;
				int r = _v.getIntValue("r");
				int c = _v.getIntValue("c");
				for (int i = calcChain.size()-1; i >=0; i--) {
					JSONObject calc = calcChain.getJSONObject(i);
					if(r == calc.getIntValue("r") && c == calc.getIntValue("c"))
					{
						isExist = true;
						break;
					}
				}
				if(!isExist)
				{
					calcChain.add(_v);
				}
			}else {
				calcChain = new JSONArray();
				calcChain.add(_v);
			}
			luckysheet.setCalcchain(JSON.toJSONString(calcChain));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.DELFC.getCode().equals(keyName)) {
			JSONObject _v = (JSONObject) v;
			String listId = _v.getString("listId");
			String index = _v.getString("index");
			String blockId = _v.getString("blockId");
			_v.remove("listId");
			_v.remove("blockId");
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
			JSONArray calcChain = JSON.parseArray(luckysheet.getCalcchain());
			int pos = -1;
			if(!ListUtil.isEmpty(calcChain))
			{
				int r = _v.getIntValue("r");
				int c = _v.getIntValue("c");
				for (int i = calcChain.size()-1; i >=0; i--) {
					JSONObject calc = calcChain.getJSONObject(i);
					if(r == calc.getIntValue("r") && c == calc.getIntValue("c"))
					{
						pos = i;
						break;
					}
				}
				if(pos >= 0)
				{
					calcChain.remove(pos);
				}
			}
			luckysheet.setCalcchain(JSON.toJSONString(calcChain));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATEROWHIDDEN.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONObject();
			}
			luckysheet.setRowhidden(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATEFILTERROWHIDDEN.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONObject();
			}
			luckysheet.setFilterrowhidden(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATECOLHIDDEN.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONObject();
			}
			luckysheet.setColhidden(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATEROWLEN.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONObject();
			}
			luckysheet.setRowlen(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATECOLLEN.getCode().equals(keyName)) {
			if(v == null)
			{
				v = new JSONObject();
			}
			luckysheet.setColumnlen(JSON.toJSONString(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATEROW.getCode().equals(keyName)) {
			luckysheet.setRowSize(Integer.parseInt(String.valueOf(v)));
			this.luckysheetMapper.updateById(luckysheet);
		}
		else if(MqTypeEnums.UPDATECOLUMN.getCode().equals(keyName)) {
			luckysheet.setColumnSize(Integer.parseInt(String.valueOf(v)));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATENAME.getCode().equals(keyName)) {
			JSONObject _v = (JSONObject) v;
			String txt = _v.getString("txt");
			String oldtxt =  _v.getString("oldtxt");
			luckysheet.setSheetName(txt);
			this.luckysheetMapper.updateById(luckysheet);
			JSONObject updateCells = _v.getJSONObject("updateCells");
			if(!StringUtil.isEmptyMap(updateCells))
			{
				JSONObject params = new JSONObject();
				params.put("blockId", mqConfigDto.getBlockId());
				params.put("listId", mqConfigDto.getListId());
				params.put("oldtxt", oldtxt+"!");
				params.put("txt", txt+"!");
				for(String key:updateCells.keySet()){
					JSONArray cells = updateCells.getJSONArray(key);
					if(!ListUtil.isEmpty(cells))
					{
						params.put("index", key);
						for (int i = 0; i < cells.size(); i++) {
							int r = cells.getJSONObject(i).getIntValue("r");
							int c = cells.getJSONObject(i).getIntValue("c");
							params.put("r", r);
							params.put("c", c);
							this.luckysheetCellMapper.updateCellFormulaSheetName(params);
						}
					}
				}
			}
		}else if(MqTypeEnums.UPDATECOLOR.getCode().equals(keyName)) {
			luckysheet.setColor(v == null?"":String.valueOf(v));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATEHIDESTATUS.getCode().equals(keyName)) {
			luckysheet.setHide(Integer.parseInt(String.valueOf(v)));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.UPDATEORDER.getCode().equals(keyName)) {
			String blockId = mqConfigDto.getBlockId();
			String listId = mqConfigDto.getIndex();
			JSONObject _v = (JSONObject) v;
			UpdateWrapper<Luckysheet> updateWrapper = null;
			Luckysheet updateData = null;
			for (Map.Entry entry : _v.entrySet()) {
				 String key = (String) entry.getKey();
				 Object value = entry.getValue();
				 updateWrapper = new UpdateWrapper<>();
				 updateWrapper.eq("block_id", blockId);
				 updateWrapper.eq("list_id", listId);
				 updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				 updateWrapper.eq("sheet_index", key);
				 updateData = new Luckysheet();
				 updateData.setSheetOrder(Integer.parseInt(value.toString()));
				 this.luckysheetMapper.update(updateData, updateWrapper);
			}
		}else if(MqTypeEnums.DELSHEET.getCode().equals(keyName)) {
			String blockId = mqConfigDto.getBlockId();
			String listId = mqConfigDto.getListId();
			String index = mqConfigDto.getIndex();
			Luckysheet update = new Luckysheet();
			update.setDelFlag(DelFlagEnum.DEL.getCode());
			UpdateWrapper<Luckysheet> updateWrapper = new UpdateWrapper<Luckysheet>();
			updateWrapper.eq("block_id", blockId);
			updateWrapper.eq("sheet_index", index);
			updateWrapper.eq("list_id", listId);
			this.luckysheetMapper.update(update, updateWrapper);
		}else if(MqTypeEnums.ADDSHEET.getCode().equals(keyName)) {
			String blockId = mqConfigDto.getBlockId();
			String listId = mqConfigDto.getListId();
			String index = mqConfigDto.getIndex();
			JSONObject _v = (JSONObject) v;
			luckysheet.setBlockId(blockId);
			luckysheet.setRowSize(Constants.DEFAULT_SHEET_ROW);
			luckysheet.setColumnSize(Constants.DEFAULT_SHEET_COLUMN);
			luckysheet.setSheetIndex(index);
			luckysheet.setListId(listId);
			luckysheet.setStatus(0);
			luckysheet.setSheetOrder(_v.getIntValue("order"));
			luckysheet.setSheetName(_v.getString("name"));
			this.luckysheetMapper.insert(luckysheet);
		}else if(MqTypeEnums.COPYSHEET.getCode().equals(keyName)) {
			luckysheet = JSONObject.parseObject(v.toString(), Luckysheet.class) ;
			this.luckysheetMapper.insert(luckysheet);
		}else if(MqTypeEnums.INSERTCELLDATA.getCode().equals(keyName))
        {
			LuckysheetCell luckysheetCell = JSONObject.parseObject(v.toString(), LuckysheetCell.class) ;
        	this.luckysheetCellMapper.insert(luckysheetCell);
        }else if(MqTypeEnums.UPDATECELLDATA.getCode().equals(keyName))
        {
        	LuckysheetCell luckysheetCell = JSONObject.parseObject(v.toString(), LuckysheetCell.class);
        	this.luckysheetCellMapper.updateById(luckysheetCell);
        }else if(MqTypeEnums.BATCHDELETECELLDATA.getCode().equals(keyName)) {
			List<Long> delIds = JSON.parseArray(v.toString(), Long.class);
			this.luckysheetCellMapper.deleteBatchIds(delIds);
		}else if(MqTypeEnums.BATCHINSERTCELLDATA.getCode().equals(keyName)) {
			List<LuckysheetCell> datas = JSON.parseArray(v.toString(), LuckysheetCell.class);
			this.iLuckysheetCellService.saveBatch(datas);
		}else if(MqTypeEnums.BATCHUPDATECELLDATA.getCode().equals(keyName)) {
			List<LuckysheetCell> datas = JSON.parseArray(v.toString(), LuckysheetCell.class);
			this.iLuckysheetCellService.updateBatchById(datas);
		}else if(MqTypeEnums.DELETEROWCOLS.getCode().equals(keyName)) {
			MqRCOprepationDto mqRCOprepationDto = JSONObject.parseObject(JSONObject.toJSONString(v),MqRCOprepationDto.class);
			int start = mqRCOprepationDto.getIndex();
			int len = mqRCOprepationDto.getLen();
			String rc = mqRCOprepationDto.getRc();
			String index = mqConfigDto.getIndex();
			String blockId = mqConfigDto.getBlockId();
			String listId = mqConfigDto.getListId();
			JSONObject params = new JSONObject();
			params.put("index", index);
			params.put("blockId", blockId);
			params.put("listId", listId);
			params.put("len", len);
			params.put("start", start);
			//删除对应的单元格数据
			if("r".equals(rc))
			{
				this.iLuckysheetCellService.delRowDelCells(params);
			}else {
				this.iLuckysheetCellService.delColDelCells(params);
			}
			//更新坐标
			if("r".equals(rc))
			{
				this.iLuckysheetCellService.delRowUpdate(params);
			}else {
				this.iLuckysheetCellService.delColUpdate(params);
			}
			//如果有合并单元格，需要更新合并单元格的信息
			JSONObject mergeCells = mqRCOprepationDto.getMergeCells();
			if(!StringUtil.isEmptyMap(mergeCells))
			{
				 List<LuckysheetCell> list = new ArrayList<LuckysheetCell>();
				 QueryWrapper<LuckysheetCell> queryWrapper = null;
				 for(Map.Entry entry : mergeCells.entrySet()){
					 String key = (String) entry.getKey();
					 Object value = entry.getValue();
					 int r = Integer.parseInt(key.split("_")[0]);
					 int c = Integer.parseInt(key.split("_")[1]);
					 queryWrapper = new QueryWrapper<>();
					 queryWrapper.eq("block_id", blockId);
					 queryWrapper.eq("sheet_index", index);
					 queryWrapper.eq("list_id", listId);
					 queryWrapper.eq("row_no", r);
					 queryWrapper.eq("column_no", c);
					 LuckysheetCell luckysheetCell = this.iLuckysheetCellService.getOne(queryWrapper, false);
					 if(luckysheetCell != null)
					 {
						 JSONObject cellData = JSON.parseObject(luckysheetCell.getCellData());
						 cellData.put("v", value);
						 luckysheetCell.setCellData(JSON.toJSONString(cellData));
						 list.add(luckysheetCell);
					 }
				 }
				 if(!ListUtil.isEmpty(list))
				 {
					 this.iLuckysheetCellService.updateBatchById(list); 
				 }
			}
			//如果有更新函数单元格信息，则更新对应的函数单元格数据
			JSONObject updateFuncCell = mqRCOprepationDto.getUpdateFuncCell();
			if(!StringUtil.isEmptyMap(updateFuncCell))
			{
				List<LuckysheetCell> list = new ArrayList<LuckysheetCell>();
				QueryWrapper<LuckysheetCell> queryWrapper = null;
				for(Map.Entry entry : updateFuncCell.entrySet()){
					 String key = (String) entry.getKey();
					 Object value = entry.getValue();
					 int r = Integer.parseInt(key.split("_")[0]);
					 int c = Integer.parseInt(key.split("_")[1]);
					 queryWrapper = new QueryWrapper<>();
					 queryWrapper.eq("block_id", blockId);
					 queryWrapper.eq("sheet_index", index);
					 queryWrapper.eq("list_id", listId);
					 queryWrapper.eq("row_no", r);
					 queryWrapper.eq("column_no", c);
					 LuckysheetCell luckysheetCell = this.iLuckysheetCellService.getOne(queryWrapper, false);
					 if(luckysheetCell != null)
					 {
						 JSONObject cellData = JSON.parseObject(luckysheetCell.getCellData());
						 cellData.put("v", value);
						 luckysheetCell.setCellData(JSON.toJSONString(cellData));
						 list.add(luckysheetCell);
					 }
				 }
				 if(!ListUtil.isEmpty(list))
				 {
					 this.iLuckysheetCellService.updateBatchById(list); 
				 }
			}
		}else if(MqTypeEnums.ADDROWCOLS.getCode().equals(keyName)) {
			MqRCOprepationDto mqRCOprepationDto = JSONObject.parseObject(JSONObject.toJSONString(v),MqRCOprepationDto.class);
			int start = mqRCOprepationDto.getIndex();
			int len = mqRCOprepationDto.getLen();
			String rc = mqRCOprepationDto.getRc();
			String direction = mqRCOprepationDto.getDirection();
			String index = mqConfigDto.getIndex();
			String blockId = mqConfigDto.getBlockId();
			String listId = mqConfigDto.getListId();
			JSONObject params = new JSONObject();
			params.put("index", index);
			params.put("blockId", blockId);
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
				this.iLuckysheetCellService.addRowUpdate(params);
			}else {
				this.iLuckysheetCellService.addColUpdate(params);
			}
			//如果有合并单元格，需要更新合并单元格的信息
			JSONObject mergeCells = mqRCOprepationDto.getMergeCells();
			if(!StringUtil.isEmptyMap(mergeCells))
			{
				 List<LuckysheetCell> list = new ArrayList<LuckysheetCell>();
				 QueryWrapper<LuckysheetCell> queryWrapper = null;
				 for(Map.Entry entry : mergeCells.entrySet()){
					 String key = (String) entry.getKey();
					 Object value = entry.getValue();
					 int r = Integer.parseInt(key.split("_")[0]);
					 int c = Integer.parseInt(key.split("_")[1]);
					 queryWrapper = new QueryWrapper<>();
					 queryWrapper.eq("block_id", blockId);
					 queryWrapper.eq("sheet_index", index);
					 queryWrapper.eq("list_id", listId);
					 queryWrapper.eq("row_no", r);
					 queryWrapper.eq("column_no", c);
					 LuckysheetCell luckysheetCell = this.iLuckysheetCellService.getOne(queryWrapper, false);
					 if(luckysheetCell != null)
					 {
						 JSONObject cellData = JSON.parseObject(luckysheetCell.getCellData());
						 cellData.put("v", value);
						 luckysheetCell.setCellData(JSON.toJSONString(cellData));
						 list.add(luckysheetCell); 
					 }
				 }
				 if(!ListUtil.isEmpty(list))
				 {
					 this.iLuckysheetCellService.updateBatchById(list); 
				 }
			}
			//如果有更新函数单元格信息，则更新对应的函数单元格数据
			JSONObject updateFuncCell = mqRCOprepationDto.getUpdateFuncCell();
			if(!StringUtil.isEmptyMap(updateFuncCell))
			{
				List<LuckysheetCell> list = new ArrayList<LuckysheetCell>();
				QueryWrapper<LuckysheetCell> queryWrapper = null;
				for(Map.Entry entry : updateFuncCell.entrySet()){
					 String key = (String) entry.getKey();
					 Object value = entry.getValue();
					 int r = Integer.parseInt(key.split("_")[0]);
					 int c = Integer.parseInt(key.split("_")[1]);
					 queryWrapper = new QueryWrapper<>();
					 queryWrapper.eq("block_id", blockId);
					 queryWrapper.eq("sheet_index", index);
					 queryWrapper.eq("list_id", listId);
					 queryWrapper.eq("row_no", r);
					 queryWrapper.eq("column_no", c);
					 LuckysheetCell luckysheetCell = this.iLuckysheetCellService.getOne(queryWrapper, false);
					 JSONObject cellData = JSON.parseObject(luckysheetCell.getCellData());
					 cellData.put("v", value);
					 luckysheetCell.setCellData(JSON.toJSONString(cellData));
					 list.add(luckysheetCell);
				 }
				 if(!ListUtil.isEmpty(list))
				 {
					 this.iLuckysheetCellService.updateBatchById(list); 
				 }
			}
		}else if(MqTypeEnums.DELSHEETCELL.getCode().equals(keyName)) {
			String blockId = mqConfigDto.getBlockId();
			String listId = mqConfigDto.getListId();
			String index = mqConfigDto.getIndex();
			QueryWrapper<LuckysheetCell> queryWrapper = new QueryWrapper<LuckysheetCell>();
			queryWrapper.eq("block_id", blockId);
			queryWrapper.eq("sheet_index", index);
			queryWrapper.eq("list_id", listId);
			this.luckysheetCellMapper.delete(queryWrapper);
		}else if(MqTypeEnums.DELDOC.getCode().equals(keyName)) {
			String blockId = mqConfigDto.getBlockId();
			String listId = mqConfigDto.getListId();
			Luckysheet update = new Luckysheet();
			update.setDelFlag(DelFlagEnum.DEL.getCode());
			UpdateWrapper<Luckysheet> updateWrapper = new UpdateWrapper<Luckysheet>();
			updateWrapper.eq("block_id", blockId);
			updateWrapper.eq("list_id", listId);
			this.luckysheetMapper.update(update, updateWrapper);
			QueryWrapper<LuckysheetCell> queryWrapper = new QueryWrapper<LuckysheetCell>();
			queryWrapper.eq("block_id", blockId);
			queryWrapper.eq("list_id", listId);
			this.luckysheetCellMapper.delete(queryWrapper);
		}else if(MqTypeEnums.INSERTCHART.getCode().equals(keyName))
		{//新增图表
			JSONObject _v = (JSONObject) v;
			String listId = mqConfigDto.getListId();
			String index = mqConfigDto.getIndex();
			String blockId = mqConfigDto.getBlockId();
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
			String chartStr = luckysheet.getChart();
			JSONArray charts = null;
			if(StringUtil.isNotEmpty(chartStr)) {
				boolean isExist = false;
				charts = JSON.parseArray(chartStr);
				String vchartId = _v.getString("chart_id");
				for (int i = 0; i < charts.size(); i++) {
					String chartId = charts.getJSONObject(i).getString("chart_id");
					if(chartId.equals(vchartId)) {
						isExist = true;
						break;
					}
				}
				if(!isExist) {
					charts.add(_v);
				}
			}else {
				charts = new JSONArray();
				charts.add(_v);
			}
			luckysheet.setChart(JSON.toJSONString(charts));
			this.luckysheetMapper.updateById(luckysheet);
		}else if(MqTypeEnums.MOVECHART.getCode().equals(keyName) || MqTypeEnums.CHANGECHARTRANGE.getCode().equals(keyName) || MqTypeEnums.UPDATECHART.getCode().equals(keyName))
		{//移动图表
			JSONObject _v = (JSONObject) v;
			String listId = mqConfigDto.getListId();
			String index = mqConfigDto.getIndex();
			String blockId = mqConfigDto.getBlockId();
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
			String chartStr = luckysheet.getChart();
			JSONArray charts = null;
			if(StringUtil.isNotEmpty(chartStr)) {
				charts = JSON.parseArray(chartStr);
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
						this.luckysheetMapper.updateById(luckysheet);
						break;
					}
				}
			}
		}else if(MqTypeEnums.DELETECHART.getCode().equals(keyName))
		{
			JSONObject _v = JSON.parseObject(JSON.toJSONString(v));
			String listId = mqConfigDto.getListId();
			String index = mqConfigDto.getIndex();
			String blockId = mqConfigDto.getBlockId();
			Object data = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
			luckysheet = JSON.parseObject(data.toString(),Luckysheet.class);
			if(data != null) {
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
							this.luckysheetMapper.updateById(luckysheet);
							break;
						}
					}
				}
			}
		}
		redisUtil.del(redisKey);
	
	}
}
