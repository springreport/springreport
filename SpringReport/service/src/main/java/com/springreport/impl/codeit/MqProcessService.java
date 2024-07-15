package com.springreport.impl.codeit;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.springreport.constants.Constants;
import com.springreport.dto.coedit.MqCacheMsgDto;
import com.springreport.dto.coedit.MqConfigDto;
import com.springreport.dto.coedit.MqOperateHisDto;
import com.springreport.dto.coedit.MqRCOprepationDto;
import com.springreport.entity.luckysheetcell.LuckysheetCell;
import com.springreport.mq.MqProducer;
import com.springreport.util.DateUtil;
import com.springreport.util.Pako_GzipUtils;
import com.springreport.util.StringUtil;
import com.springreport.dto.coedit.WSUserModel;

import lombok.extern.slf4j.Slf4j;

/**  
 * @ClassName: MqProcessService
 * @Description: 处理消息队列服务
 * @author caiyang
 * @date 2023-10-09 08:37:17 
*/ 
@Service
@Slf4j
public class MqProcessService {
	
	@Autowired
	private MqProducer mqProducer;
	
	/**  
	 * @MethodName: updateRedisConfigCache
	 * @Description: 更新缓存中的配置信息
	 * @author caiyang
	 * @param keyName
	 * @param v
	 * @param index
	 * @param listId
	 * @param blockId
	 * @param rowCol void
	 * @date 2023-10-14 04:09:23 
	 */ 
	public void updateRedisConfigCache(String keyName,Object v, String index, String listId,String blockId,String token,String operate) {
		if(StringUtil.isNotEmpty(index))
		{
			MqCacheMsgDto mqCacheMsgDto = new MqCacheMsgDto();
			mqCacheMsgDto.setKeyName(keyName);
			mqCacheMsgDto.setV(v);
			mqCacheMsgDto.setIndex(index);
			mqCacheMsgDto.setListId(listId);
			mqCacheMsgDto.setBlockId(blockId);
			mqCacheMsgDto.setToken(token);
			mqCacheMsgDto.setOperate(operate);
			mqCacheMsgDto.setOperateTime(DateUtil.getTimeStamp());
			String msg = JSON.toJSONString(mqCacheMsgDto);
			String comperssParams = Pako_GzipUtils.compress(msg);
			mqProducer.sendOrderlyMsg(Constants.MQ_TPOIC_LUCKYSHEET_CACHE, comperssParams,index);
		}else {
			this.updateRedisConfigCache(keyName, v, listId, blockId,token,operate);
		}
	}
	
	private void updateRedisConfigCache(String keyName,Object v, String listId,String blockId ,String token,String operate) {
		MqCacheMsgDto mqCacheMsgDto = new MqCacheMsgDto();
		mqCacheMsgDto.setKeyName(keyName);
		mqCacheMsgDto.setV(v);
		mqCacheMsgDto.setListId(listId);
		mqCacheMsgDto.setBlockId(blockId);
		mqCacheMsgDto.setToken(token);
		mqCacheMsgDto.setOperate(operate);
		mqCacheMsgDto.setOperateTime(DateUtil.getTimeStamp());
		String msg = JSON.toJSONString(mqCacheMsgDto);
		String comperssParams = Pako_GzipUtils.compress(msg);
		mqProducer.sendOrderlyMsg(Constants.MQ_TPOIC_LUCKYSHEET_CACHE, comperssParams,listId);
	}
	
	/**  
	 * @MethodName: updateRedisCellValueCache
	 * @Description: 更新redis中的单个单元格数据信息
	 * @author caiyang
	 * @param keyName
	 * @param v
	 * @param index
	 * @param listId
	 * @param blockId
	 * @param rowCol void
	 * @date 2023-10-14 04:16:18 
	 */ 
	public void updateRedisCellValueCache(String keyName,Object v, String index, String listId,String blockId,String rowCol,String token,String operate,JSONObject changeBefore) {
		MqCacheMsgDto mqCacheMsgDto = new MqCacheMsgDto();
		mqCacheMsgDto.setKeyName(keyName);
		mqCacheMsgDto.setV(v);
		mqCacheMsgDto.setIndex(index);
		mqCacheMsgDto.setListId(listId);
		mqCacheMsgDto.setBlockId(blockId);
		mqCacheMsgDto.setRowCol(rowCol);
		mqCacheMsgDto.setToken(token);
		mqCacheMsgDto.setOperate(operate);
		mqCacheMsgDto.setChangeBefore(changeBefore);
		mqCacheMsgDto.setOperateTime(DateUtil.getTimeStamp());
		String msg = JSON.toJSONString(mqCacheMsgDto);
		String comperssParams = Pako_GzipUtils.compress(msg);
		mqProducer.sendOrderlyMsg(Constants.MQ_TPOIC_LUCKYSHEET_CACHE, comperssParams,index);
	}
	
	/**  
	 * @MethodName: updateRedisBatchCellValueCache
	 * @Description: 批量更新单元格数据缓存
	 * @author caiyang
	 * @param keyName
	 * @param v
	 * @param index
	 * @param listId
	 * @param blockId void
	 * @date 2023-10-14 04:49:49 
	 */ 
	public void updateRedisBatchCellValueCache(String keyName,Object v, String index, String listId,String blockId,String token,String operate,Object changeBeforeData) {
		MqCacheMsgDto mqCacheMsgDto = new MqCacheMsgDto();
		mqCacheMsgDto.setKeyName(keyName);
		mqCacheMsgDto.setV(v);
		mqCacheMsgDto.setIndex(index);
		mqCacheMsgDto.setListId(listId);
		mqCacheMsgDto.setBlockId(blockId);
		mqCacheMsgDto.setToken(token);
		mqCacheMsgDto.setOperate(operate);
		mqCacheMsgDto.setChangeBefore(changeBeforeData);
		mqCacheMsgDto.setOperateTime(DateUtil.getTimeStamp());
		String msg = JSON.toJSONString(mqCacheMsgDto);
		String comperssParams = Pako_GzipUtils.compress(msg);
		mqProducer.sendOrderlyMsg(Constants.MQ_TPOIC_LUCKYSHEET_CACHE, comperssParams,index);
	}
	
	/**  
	 * @MethodName: processCellData
	 * @Description: 单元格数据处理
	 * @author caiyang
	 * @param keyName
	 * @param luckysheetCell void
	 * @date 2023-10-09 08:46:59 
	 */ 
	public void processCellData(String keyName,LuckysheetCell luckysheetCell,String index) {
		MqConfigDto mqConfigDto = new MqConfigDto();
		mqConfigDto.setKeyName(keyName);
		mqConfigDto.setV(luckysheetCell);
		mqConfigDto.setOperateTime(DateUtil.getTimeStamp());
		String msg = JSON.toJSONString(mqConfigDto);
		String comperssParams = Pako_GzipUtils.compress(msg);
		mqProducer.sendOrderlyMsg(Constants.MQ_TPOIC_LUCKYSHEET, comperssParams,index);
	}
	
	/**  
	 * @MethodName: batchProcessCellData
	 * @Description: 批量操作单元格数据
	 * @author caiyang
	 * @param keyName
	 * @param luckysheetCell void
	 * @date 2023-10-11 10:10:20 
	 */ 
	public void batchProcessCellData(String keyName,Object datas,String index,String listId,String blockId) {
		MqConfigDto mqConfigDto = new MqConfigDto();
		mqConfigDto.setKeyName(keyName);
		mqConfigDto.setV(datas);
		mqConfigDto.setIndex(index);
		mqConfigDto.setListId(listId);
		mqConfigDto.setBlockId(blockId);
		mqConfigDto.setOperateTime(DateUtil.getTimeStamp());
		String msg = JSON.toJSONString(mqConfigDto);
		String comperssParams = Pako_GzipUtils.compress(msg);
		mqProducer.sendOrderlyMsg(Constants.MQ_TPOIC_LUCKYSHEET, comperssParams,StringUtil.isNullOrEmpty(index)?listId:index);
	}
	
	/**  
	 * @MethodName: updateSheetConfig
	 * @Description: 配置信息更新处理
	 * @author caiyang
	 * @param keyName
	 * @param v
	 * @param index
	 * @param listId
	 * @param blockId
	 * @param id void
	 * @date 2023-10-10 01:15:33 
	 */ 
	public void updateSheetConfig(String keyName,Object v,Long id,String index,String blockId,String listId)
	{
		MqConfigDto mqConfigDto = new MqConfigDto();
		mqConfigDto.setKeyName(keyName);
		mqConfigDto.setV(v);
		mqConfigDto.setId(id);
		mqConfigDto.setIndex(index);
		mqConfigDto.setBlockId(blockId);
		mqConfigDto.setListId(listId);
		mqConfigDto.setOperateTime(DateUtil.getTimeStamp());
		String msg = JSON.toJSONString(mqConfigDto);
		String comperssParams = Pako_GzipUtils.compress(msg);
		mqProducer.sendOrderlyMsg(Constants.MQ_TPOIC_LUCKYSHEET, comperssParams,index);
	}
	
	
	/**  
	 * @MethodName: datasetDataProcess
	 * @Description: 数据库数据不需要按顺序处理的mq消息
	 * @author caiyang
	 * @param index
	 * @param blockId
	 * @param listId void
	 * @date 2023-11-01 02:50:17 
	 */ 
//	public void datasetDataProcess(String key,String index,String blockId,String listId)
//	{
//		MqConfigDto mqConfigDto = new MqConfigDto();
//		mqConfigDto.setKeyName(key);
//		mqConfigDto.setIndex(index);
//		mqConfigDto.setBlockId(blockId);
//		mqConfigDto.setListId(listId);
//		String msg = JSON.toJSONString(mqConfigDto);
//		String comperssParams = Pako_GzipUtils.compress(msg);
//		mqProducer.sendSyncMsg(Constants.MQ_TPOIC_LUCKYSHEET_NOORDERLY, comperssParams);
//	}
	
	/**  
	 * @MethodName: rowColOperate
	 * @Description: 删除增加行列操作
	 * @author caiyang
	 * @param keyName
	 * @param mqRCOprepationDto
	 * @param index void
	 * @date 2023-10-24 03:01:25 
	 */ 
	public void rowColOperate(String keyName,MqRCOprepationDto mqRCOprepationDto,String index,String listId,String blockId)
	{
		MqConfigDto mqConfigDto = new MqConfigDto();
		mqConfigDto.setKeyName(keyName);
		mqConfigDto.setIndex(index);
		mqConfigDto.setListId(listId);
		mqConfigDto.setBlockId(blockId);
		mqConfigDto.setV(mqRCOprepationDto);
		mqConfigDto.setOperateTime(DateUtil.getTimeStamp());
		String msg = JSON.toJSONString(mqConfigDto);
		String comperssParams = Pako_GzipUtils.compress(msg);
		mqProducer.sendOrderlyMsg(Constants.MQ_TPOIC_LUCKYSHEET, comperssParams,index);
	}
	
	public void saveOperateHis(MqOperateHisDto mqOperateHisDto,String listId,String index) {
		mqOperateHisDto.setOperateTime(DateUtil.getTimeStamp());
		String msg = JSON.toJSONString(mqOperateHisDto);
		String comperssParams = Pako_GzipUtils.compress(msg);
		mqProducer.sendOrderlyMsg(Constants.MQ_TPOIC_LUCKYSHEET_HIS, comperssParams,StringUtil.isNotEmpty(index)?index:listId);
	}
}
