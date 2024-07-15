package com.springreport.impl.luckysheethis;

import com.springreport.entity.luckysheethis.LuckysheetHis;
import com.springreport.mapper.luckysheethis.LuckysheetHisMapper;
import com.springreport.api.luckysheethis.ILuckysheetHisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.springreport.util.MessageUtil;
import com.springreport.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.SqlOrderEnum;

 /**  
* @Description: LuckysheetHis服务实现
* @author 
* @date 2023-08-23 09:46:05
* @version V1.0  
 */
@Service
public class LuckysheetHisServiceImpl extends ServiceImpl<LuckysheetHisMapper, LuckysheetHis> implements ILuckysheetHisService {
  
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(LuckysheetHis model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("id", SqlOrderEnum.DESC.getCode());
		map.put("create_time", SqlOrderEnum.DESC.getCode());
		String orderSql = StringUtil.formatOrderSql(map);
		model.setOrderSql(orderSql);
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<LuckysheetHis> list = this.baseMapper.searchDataLike(model);
		result.setData(list);
		result.setTotal(page.getTotal());
		result.setCurrentPage(model.getCurrentPage());
		result.setPageSize(model.getPageSize());
		return result;
	}


	/**
	*<p>Title: getDetail</p>
	*<p>Description: 获取详情</p>
	* @author 
	* @param id
	* @return
	*/
	@Override
	public BaseEntity getDetail(Long id) {
		return this.getById(id);
	}

	/**
	*<p>Title: insert</p>
	*<p>Description: 新增数据</p>
	* @author 
	* @param model
	* @return
	*/
	@Transactional
	@Override
	@Async
	public BaseEntity insert(LuckysheetHis model) {
		BaseEntity result = new BaseEntity();
		this.save(model);
		result.setStatusMsg(MessageUtil.getValue("info.insert"));
		return result;
	}

	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author 
	* @param model
	* @return
	*/
	@Transactional
	@Override
	public BaseEntity update(LuckysheetHis model) {
		BaseEntity result = new BaseEntity();
		this.updateById(model);
		result.setStatusMsg(MessageUtil.getValue("info.update"));
		return result;
	}

	/**
	*<p>Title: delete</p>
	*<p>Description: 单条删除数据</p>
	* @author 
	* @param model
	* @return
	*/
	@Transactional
	@Override
	public BaseEntity delete(Long id) {
		LuckysheetHis luckysheetHis = new LuckysheetHis();
		luckysheetHis.setId(id);
		luckysheetHis.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(luckysheetHis);
		BaseEntity result = new BaseEntity();
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}

	/**
	*<p>Title: deleteBatch</p>
	*<p>Description: 批量删除数据</p>
	* @author 
	* @param list
	* @return
	*/
	@Transactional
	@Override
	public BaseEntity deleteBatch(List<Long> ids) {
		List<LuckysheetHis> list = new ArrayList<LuckysheetHis>();
		for (int i = 0; i < ids.size(); i++) {
			LuckysheetHis luckysheetHis = new LuckysheetHis();
			luckysheetHis.setId(ids.get(i));
			luckysheetHis.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(luckysheetHis);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @MethodName: saveCellHisOne
	 * @Description: 保存单元格修改历史记录(单条)
	 * @author caiyang
	 * @param beforeData
	 * @param newData
	 * @see com.springreport.api.luckysheethis.ILuckysheetHisService#saveHisOne(com.alibaba.fastjson.JSONObject, com.springreport.entity.luckysheet.Luckysheet, int)
	 * @date 2023-08-23 09:56:13 
	 */
//	@Override
//	@Async
//	public void saveCellHisOne(JSONObject beforeData, JSONObject newData,WSUserModel wsUserModel,String index,String listId,String operate) {
//		try {
//			LuckysheetHis luckysheetHis = new LuckysheetHis();
//			UserInfoDto userInfoDto = JWTUtil.getUserInfo(wsUserModel.getToken());
//			if(newData == null)
//			{//删除单元格数据
//				String r = beforeData.getString("r");
//				String c = beforeData.getString("c");
//				luckysheetHis.setRowCol(r+"_"+c);
//				luckysheetHis.setBson(null);
//				luckysheetHis.setBeforeJson(beforeData);
//				luckysheetHis.setChangeDesc(MessageUtil.getValue("luckysheet.cell.clear"));
//			}else if(beforeData == null) {
//				//新增数据
//				String r = newData.getString("r");
//				String c = newData.getString("c");
//				luckysheetHis.setRowCol(r+"_"+c);
//				luckysheetHis.setBson(newData);
//				luckysheetHis.setBeforeJson(null);
//				luckysheetHis.setChangeDesc(this.getDiffMsg(newData.getJSONObject("v"),new JSONObject(),operate));
//			}else {
//				String r = beforeData.getString("r");
//				String c = beforeData.getString("c");
//				luckysheetHis.setRowCol(r+"_"+c);
//				luckysheetHis.setBson(newData);
//				luckysheetHis.setBeforeJson(beforeData);
//				luckysheetHis.setChangeDesc(this.getDiffMsg(newData, beforeData,operate));
//			}
//			luckysheetHis.setOperateKey("v");
//			luckysheetHis.setSheetIndex(index);
//			luckysheetHis.setListId(listId);
//			luckysheetHis.setRemark(operate);
//			luckysheetHis.setType(2);
//			luckysheetHis.setOperator(userInfoDto.getUserName());
//			luckysheetHis.setCreator(userInfoDto.getUserId());
//			luckysheetHis.setCreateTime(new Date());
//			luckysheetHis.setUpdater(userInfoDto.getUserId());
//			luckysheetHis.setUpdateTime(new Date());
//			if(StringUtil.isNotEmpty(luckysheetHis.getChangeDesc()))
//			{
//				this.save(luckysheetHis);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
//	
//	/**  
//	 * @MethodName: getDiffMsg
//	 * @Description: 获取单元格改变前后的对比信息 
//	 * @author caiyang
//	 * @param newData
//	 * @param beforeData
//	 * @return String
//	 * @date 2023-08-23 04:00:06 
//	 */ 
//	private String getDiffMsg(JSONObject newData, JSONObject beforeData, String operate) {
//		String result = "";
//		boolean isReservse = false;//是否反过来比较
//		Map<String, JSONObject> diffResult = null;
//		diffResult = StringUtil.compare2MapDiff2(newData,
//				beforeData.getJSONObject("v") == null ? new JSONObject() : beforeData.getJSONObject("v"));
//		if(diffResult == null || StringUtil.isEmptyMap(diffResult.get("changeAfter")))
//		{
//			isReservse = true;
//			diffResult = StringUtil.compare2MapDiff2(beforeData.getJSONObject("v") == null ? new JSONObject() : beforeData.getJSONObject("v"), newData);
//		}
//		if(diffResult != null)
//		{
//			JSONObject changeBefore = diffResult.get("changeBefore");
//			JSONObject changeAfter = diffResult.get("changeAfter");
//			if(StringUtil.isEmptyMap(changeAfter))
//			{
//				result = operate;
//			}else {
//				if (changeAfter.containsKey("f")) {
//					String beforeValue = changeBefore.getString("f");
//					if (StringUtil.isNullOrEmpty(beforeValue)) {
//						beforeValue = "无公式";
//					}
//					String afterValue = changeAfter.getString("f");
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "公式", afterValue, beforeValue });
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "公式", beforeValue, afterValue });
//					}
//				} else if (changeAfter.containsKey("v")) {// 值变化
//					String beforeValue = changeBefore.getString("v");
//					if (StringUtil.isNullOrEmpty(beforeValue)) {
//						beforeValue = "空值";
//					}
//					String afterValue = changeAfter.getString("v");
//					if (StringUtil.isNullOrEmpty(afterValue)) {
//						afterValue = "空值";
//					}
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "值", afterValue, beforeValue });
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "值", beforeValue, afterValue });
//					}
//				} else if (changeAfter.containsKey("ff")) {
//					// 字体发生变化
//					String beforeValue = changeBefore.getString("ff");
//					String afterValue = changeAfter.getString("ff");
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "字体", afterValue, beforeValue });
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "字体", beforeValue, afterValue });
//					}
//				} else if (changeAfter.containsKey("fs")) {
//					// 字号大小变化
//					String beforeValue = changeBefore.getString("fs");
//					if (StringUtil.isNullOrEmpty(beforeValue)) {
//						beforeValue = "10";
//					}
//					String afterValue = changeAfter.getString("fs");
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "字号大小", afterValue, beforeValue });
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "字号大小", beforeValue, afterValue });
//					}
//					
//				} else if (changeAfter.containsKey("bl")) {
//					// 字体加粗
//					String beforeValue = changeBefore.getString("bl");
//					if (StringUtil.isNullOrEmpty(beforeValue)) {
//						beforeValue = "0";
//					}
//					String afterValue = changeAfter.getString("bl");
//					if (StringUtil.isNullOrEmpty(afterValue)) {
//						afterValue = "0";
//					}
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "是否粗体",
//								afterValue.equals("0") ? "否" : "是",beforeValue.equals("0") ? "否" : "是"});
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "是否粗体",
//								beforeValue.equals("0") ? "否" : "是", afterValue.equals("0") ? "否" : "是" });
//					}
//					
//				} else if (changeAfter.containsKey("it")) {
//					// 字体加粗
//					String beforeValue = changeBefore.getString("it");
//					if (StringUtil.isNullOrEmpty(beforeValue)) {
//						beforeValue = "0";
//					}
//					String afterValue = changeAfter.getString("it");
//					if (StringUtil.isNullOrEmpty(afterValue)) {
//						afterValue = "0";
//					}
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "是否斜体",
//								afterValue.equals("0") ? "否" : "是",beforeValue.equals("0") ? "否" : "是" });
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "是否斜体",
//								beforeValue.equals("0") ? "否" : "是", afterValue.equals("0") ? "否" : "是" });
//					}
//					
//				} else if (changeAfter.containsKey("cl")) {
//					// 删除线
//					String beforeValue = changeBefore.getString("cl");
//					if (StringUtil.isNullOrEmpty(beforeValue)) {
//						beforeValue = "0";
//					}
//					String afterValue = changeAfter.getString("cl");
//					if (StringUtil.isNullOrEmpty(afterValue)) {
//						afterValue = "0";
//					}
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "删除线",
//								afterValue.equals("0") ? "无" : "有",beforeValue.equals("0") ? "无" : "有" });
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "删除线",
//								beforeValue.equals("0") ? "无" : "有", afterValue.equals("0") ? "无" : "有" });
//					}
//					result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "删除线",
//							beforeValue.equals("0") ? "无" : "有", afterValue.equals("0") ? "无" : "有" });
//				} else if (changeAfter.containsKey("un")) {
//					// 下划线
//					String beforeValue = changeBefore.getString("un");
//					if (StringUtil.isNullOrEmpty(beforeValue)) {
//						beforeValue = "0";
//					}
//					String afterValue = changeAfter.getString("un");
//					if (StringUtil.isNullOrEmpty(afterValue)) {
//						afterValue = "0";
//					}
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "下划线",
//								afterValue.equals("0") ? "无" : "有",beforeValue.equals("0") ? "无" : "有" });
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "下划线",
//								beforeValue.equals("0") ? "无" : "有", afterValue.equals("0") ? "无" : "有" });
//					}
//				} else if (changeAfter.containsKey("fc")) {
//					// 字体颜色
//					String beforeValue = changeBefore.getString("fc");
//					if (StringUtil.isNullOrEmpty(beforeValue)) {
//						beforeValue = "#000000";
//					}
//					String afterValue = changeAfter.getString("fc");
//					if (StringUtil.isNullOrEmpty(afterValue)) {
//						afterValue = "#000000";
//					}
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "字体颜色", afterValue, beforeValue });
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "字体颜色", beforeValue, afterValue });
//					}
//				} else if (changeAfter.containsKey("bg")) {
//					// 背景颜色
//					String beforeValue = changeBefore.getString("bg");
//					if (StringUtil.isNullOrEmpty(beforeValue)) {
//						beforeValue = "#ffffff";
//					}
//					String afterValue = changeAfter.getString("bg");
//					if (StringUtil.isNullOrEmpty(afterValue)) {
//						afterValue = "#ffffff";
//					}
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "背景颜色", afterValue, beforeValue });
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "背景颜色", beforeValue, afterValue });
//					}
//				} else if (changeAfter.containsKey("ht")) {
//					// 水平对齐
//					String beforeValue = changeBefore.getString("ht");
//					if (StringUtil.isNullOrEmpty(beforeValue)) {
//						beforeValue = "1";
//					}
//					String afterValue = changeAfter.getString("ht");
//					if (StringUtil.isNullOrEmpty(afterValue)) {
//						afterValue = "1";
//					}
//					beforeValue = LuckysheetHtEnums.getValue(beforeValue);
//					afterValue = LuckysheetHtEnums.getValue(afterValue);
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "水平对齐方式", afterValue, beforeValue });
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "水平对齐方式", beforeValue, afterValue });
//					}
//				} else if (changeAfter.containsKey("vt")) {
//					// 垂直对齐
//					String beforeValue = changeBefore.getString("vt");
//					if (StringUtil.isNullOrEmpty(beforeValue)) {
//						beforeValue = "2";
//					}
//					String afterValue = changeAfter.getString("vt");
//					if (StringUtil.isNullOrEmpty(afterValue)) {
//						afterValue = "2";
//					}
//					beforeValue = LuckysheetVtEnums.getValue(beforeValue);
//					afterValue = LuckysheetVtEnums.getValue(afterValue);
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "垂直对齐方式", afterValue, beforeValue });
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "垂直对齐方式", beforeValue, afterValue });
//					}
//				} else if (changeAfter.containsKey("tb")) {
//					// 文本换行
//					String beforeValue = changeBefore.getString("tb");
//					if (StringUtil.isNullOrEmpty(beforeValue)) {
//						beforeValue = "0";
//					}
//					String afterValue = changeAfter.getString("tb");
//					if (StringUtil.isNullOrEmpty(afterValue)) {
//						afterValue = "0";
//					}
//					beforeValue = LuckysheetTbEnums.getValue(beforeValue);
//					afterValue = LuckysheetTbEnums.getValue(afterValue);
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "文本换行方式", afterValue, beforeValue });
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "文本换行方式", beforeValue, afterValue });
//					}
//					
//				} else if (changeAfter.containsKey("tr")) {
//					// 文本旋转
//					String beforeValue = changeBefore.getString("tr");
//					if (StringUtil.isNullOrEmpty(beforeValue)) {
//						beforeValue = "0";
//					}
//					String afterValue = changeAfter.getString("tr");
//					if (StringUtil.isNullOrEmpty(afterValue)) {
//						afterValue = "0";
//					}
//					beforeValue = LuckysheetTrEnums.getValue(beforeValue);
//					afterValue = LuckysheetTrEnums.getValue(afterValue);
//					if(isReservse)
//					{
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "文本旋转", afterValue, beforeValue });
//					}else {
//						result = MessageUtil.getValue("luckysheet.cell.change",
//								new String[] { "文本旋转", beforeValue, afterValue });
//					}
//				} else if (changeAfter.containsKey("ps")) {
//					result = operate;
//				} else if (changeAfter.containsKey("ct")) {
//					String beforeValue = "General";
//					if (changeBefore.getJSONObject("ct") != null) {
//						beforeValue = changeBefore.getJSONObject("ct").getString("fa");
//					}
//					String afterValue = "General";
//					if (changeAfter.getJSONObject("ct") != null) {
//						afterValue = changeAfter.getJSONObject("ct").getString("fa");
//					}
//					if (!afterValue.equals(beforeValue)) {
//						String afterMsg = "";
//						String beforeMsg = "";
//						if (LuckysheetFormatEnum.getValue(afterValue) != null) {
//							afterMsg = LuckysheetFormatEnum.getValue(afterValue) + "-" + afterValue;
//						} else {
//							afterMsg = "更多格式" + "-" + afterValue;
//						}
//						if (LuckysheetFormatEnum.getValue(beforeValue) != null) {
//							beforeMsg = LuckysheetFormatEnum.getValue(beforeValue) + "-" + beforeValue;
//						} else {
//							beforeMsg = "更多格式" + "-" + beforeValue;
//						}
//						if(isReservse)
//						{
//							result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "格式",afterMsg, beforeMsg });
//						}else {
//							result = MessageUtil.getValue("luckysheet.cell.change", new String[] { "格式", beforeMsg, afterMsg });
//						}
//					}
//				}
//			}
//		}else {
//			result = operate;
//		}
//		if(StringUtil.isNullOrEmpty(result))
//		{
//			if(operate != null && operate.contains("清除内容"))
//			{
//				result = operate;
//			}
//		}
//		return result;
//	}
//
//
//	/**  
//	 * @MethodName: saveBatchUpdate
//	 * @Description: 批量更新单元格记录历史记录
//	 * @author caiyang
//	 * @param originalExistsBlock
//	 * @param _existsBlock
//	 * @param wsUserModel
//	 * @param index
//	 * @param listId
//	 * @see com.springreport.api.luckysheethis.ILuckysheetHisService#saveBatchUpdate(java.util.HashMap, java.util.HashMap, com.springreport.websocket.WSUserModel, java.lang.String, java.lang.String)
//	 * @date 2023-08-24 10:02:34 
//	 */
//	@Override
//	@Async
//	public void saveBatchUpdate(HashMap<String, JSONObject> originalExistsBlock,
//			HashMap<String, JSONObject> _existsBlock, WSUserModel wsUserModel, String index, String listId,String remark) {
//		if(!StringUtil.isEmptyMap(_existsBlock))
//		{
//			for(Map.Entry entry : _existsBlock.entrySet()){
//				 String mapKey = (String) entry.getKey();
//				 JSONObject mapValue = (JSONObject) entry.getValue();
//				 JSONObject newdata = null;
//				 if(!ListUtil.isEmpty(mapValue.getJSONArray("celldata")))
//				 {
//					 newdata = mapValue.getJSONArray("celldata").getJSONObject(0).getJSONObject("v");
//				 }
//				 JSONObject beforeData = originalExistsBlock.get(mapKey).getJSONArray("celldata").getJSONObject(0);
//				 this.saveCellHisOne(beforeData, newdata, wsUserModel, index, listId,remark);
//			}
//		}
//		
//	}
//
//
//	/**  
//	 * @MethodName: saveBatchInsert
//	 * @Description: 批量插入单元格更新历史记录
//	 * @author caiyang
//	 * @param _notexistsBlock
//	 * @param wsUserModel
//	 * @param index
//	 * @param listId
//	 * @see com.springreport.api.luckysheethis.ILuckysheetHisService#saveBatchInsert(java.util.HashMap, com.springreport.websocket.WSUserModel, java.lang.String, java.lang.String)
//	 * @date 2023-08-24 10:43:05 
//	 */
//	@Override
//	@Async
//	public void saveBatchInsert(HashMap<String, JSONObject> _notexistsBlock, WSUserModel wsUserModel, String index,
//			String listId,String remark) {
//		for(Map.Entry entry : _notexistsBlock.entrySet()){
//			 String mapKey = (String) entry.getKey();
//			 JSONObject mapValue = (JSONObject) entry.getValue();
//			 JSONObject newdata = mapValue.getJSONArray("celldata").getJSONObject(0);
//			 this.saveCellHisOne(null, newdata, wsUserModel, index, listId,remark);
//		}
//		
//	}
//
//
//	/**  
//	 * @MethodName: batchDelCells
//	 * @Description: 增加行列引起的批量删除单元格
//	 * @author caiyang
//	 * @param lists
//	 * @see com.springreport.api.luckysheethis.ILuckysheetHisService#batchDelCells(java.util.List)
//	 * @date 2023-08-24 01:32:25 
//	 */
//	@Override
//	@Async
//	public void batchDelCells(List<Luckysheet> lists, WSUserModel wsUserModel, String index,String listId,String remark) {
//		if(!ListUtil.isEmpty(lists))
//		{
//			for (int i = 0; i < lists.size(); i++) {
//				JSONObject jsonObject = null;
//				if(lists.get(i).getJsonData() instanceof KBobject)
//				{
//					KBobject kbObject = (KBobject) lists.get(i).getJsonData();
//					jsonObject = JSON.parseObject(kbObject.getValue());
//				}else {
//					jsonObject = (JSONObject) lists.get(i).getJsonData();
//				}
//				this.saveCellHisOne(jsonObject.getJSONArray("celldata").getJSONObject(0), null, wsUserModel, index, listId,remark);
//			}
//		}
//		
//	}
//
//
//	/**  
//	 * @MethodName: saveBatchInsert
//	 * @Description: 增加行列引起的批量新增单元格
//	 * @author caiyang
//	 * @param lists
//	 * @param wsUserModel
//	 * @param index
//	 * @param listId
//	 * @param remark
//	 * @see com.springreport.api.luckysheethis.ILuckysheetHisService#saveBatchInsert(java.util.List, com.springreport.websocket.WSUserModel, java.lang.String, java.lang.String, java.lang.String)
//	 * @date 2023-08-24 05:04:22 
//	 */
//	@Override
//	@Async
//	public void saveBatchInsert(List<Luckysheet> lists, WSUserModel wsUserModel, String index, String listId,String remark) {
//		if(!ListUtil.isEmpty(lists))
//		{
//			String type = DatasourceTypeUtil.getDatasourceType();
//			for (int i = 0; i < lists.size(); i++) {
//				JSONObject jsonObject = null;
//				if(lists.get(i).getJsonData() instanceof KBobject)
//				{
//					KBobject kbObject = (KBobject) lists.get(i).getJsonData();
//					jsonObject = JSON.parseObject(kbObject.getValue());
//				}else {
//					if(SystemDatasourceTypeEnum.ORACLE.getCode().equals(type))
//					{
//						jsonObject = new JSONObject();
//						JSONArray jsonArray = new JSONArray();
//						jsonArray.add((JSONObject) lists.get(i).getJsonData());
//						jsonObject.put("celldata", jsonArray);
//					}else {
//						jsonObject = (JSONObject) lists.get(i).getJsonData();
//					}
//				}
//				this.saveCellHisOne(null, jsonObject.getJSONArray("celldata").getJSONObject(0), wsUserModel, index, listId,remark);
//			}
//		}
//	}
//
//
//	/**  
//	 * @MethodName: saveSheetSettings
//	 * @Description: 保存sheet配置信息修改历史
//	 * @author caiyang
//	 * @param beforeData
//	 * @param newData
//	 * @param key
//	 * @param wsUserModel
//	 * @param index
//	 * @param listId
//	 * @param operate
//	 * @see com.springreport.api.luckysheethis.ILuckysheetHisService#saveSheetSettings(com.alibaba.fastjson.JSONObject, com.alibaba.fastjson.JSONObject, java.lang.String, com.springreport.websocket.WSUserModel, java.lang.String, java.lang.String, java.lang.String)
//	 * @date 2023-08-27 04:29:06 
//	 */
//	@Override
//	@Async
//	public void saveSheetSettings(JSONObject beforeData, Object newDataObject, String key, WSUserModel wsUserModel,
//			String index, String listId, String operate) {
//		LuckysheetHis luckysheetHis = new LuckysheetHis();
//		UserInfoDto userInfoDto = JWTUtil.getUserInfo(wsUserModel.getToken());
//		if("config".equals(key))
//		{
//			JSONObject newData = (JSONObject) newDataObject;
//			JSONObject beforeConfig = beforeData.getJSONObject("config");
//			String diffMsg = this.getConfigDiffMsg(beforeConfig, newData, operate);
//			luckysheetHis.setChangeDesc(diffMsg);
//			luckysheetHis.setBeforeJson(beforeConfig);
//			luckysheetHis.setBson(newData);
//		}else if(key.contains("config.")) {
//			JSONObject beforeConfig = beforeData.getJSONObject("config");
//			if(key.contains("borderInfo"))
//			{//边框信息
//				JSONArray beforeBorderInfo = beforeConfig.getJSONArray("borderInfo") == null?new JSONArray(): beforeConfig.getJSONArray("borderInfo");
//				luckysheetHis.setChangeDesc("修改边框信息");
//				luckysheetHis.setBeforeJson(beforeBorderInfo);
//				luckysheetHis.setBson(newDataObject);
//			}else if(key.contains("rowlen"))
//			{//行高
//				String diffMsg = this.getDiffMsg("rowlen", beforeConfig, newDataObject, operate);
//				luckysheetHis.setChangeDesc(diffMsg);
//				luckysheetHis.setBeforeJson(beforeConfig);
//				luckysheetHis.setBson(newDataObject);
//			}else if(key.contains("columnlen"))
//			{//列宽
//				String diffMsg = this.getDiffMsg("columnlen", beforeConfig, newDataObject, operate);
//				luckysheetHis.setChangeDesc(diffMsg);
//				luckysheetHis.setBeforeJson(beforeConfig);
//				luckysheetHis.setBson(newDataObject);
//			}else if(key.contains("rowhidden")) {
//				//隐藏行
//				String diffMsg = this.getDiffMsg("rowhidden", beforeConfig, newDataObject, operate);
//				luckysheetHis.setChangeDesc(diffMsg);
//				luckysheetHis.setBeforeJson(beforeConfig);
//				luckysheetHis.setBson(newDataObject);
//			}else if(key.contains("colhidden")) {
//				//隐藏列
//				String diffMsg = this.getDiffMsg("colhidden", beforeConfig, newDataObject, operate);
//				luckysheetHis.setChangeDesc(diffMsg);
//				luckysheetHis.setBeforeJson(beforeConfig);
//				luckysheetHis.setBson(newDataObject);
//			}
//		}else if("name".equals(key)) {
//			luckysheetHis.setChangeDesc(operate);
//			luckysheetHis.setBeforeJson(beforeData);
//			luckysheetHis.setBson(newDataObject);
//		}else if("color".equals(key)) {
//			if("撤销".equals(operate) || "重做".equals(operate))
//			{
//				luckysheetHis.setChangeDesc(operate);
//				luckysheetHis.setBeforeJson(beforeData);
//				luckysheetHis.setBson(newDataObject);
//			}else {
//				luckysheetHis.setChangeDesc(operate.split("，")[0]+operate.split("，")[1]+"，修改后颜色："+(newDataObject==null?"无":newDataObject));
//				luckysheetHis.setBeforeJson(beforeData);
//				luckysheetHis.setBson(newDataObject);
//			}
//		}else if("filter_select".equals(key))
//		{
//			if(newDataObject == null)
//			{
//				if(!StringUtil.isEmptyMap(beforeData.getJSONObject("filter_select")))
//				{
//					luckysheetHis.setChangeDesc("清空筛选");
//				}
//			}else {
//				luckysheetHis.setChangeDesc("添加筛选");
//			}
//			luckysheetHis.setBeforeJson(beforeData);
//			luckysheetHis.setBson(newDataObject);
//		}
//		else
//		{
//			String msg =  this.getDiffMsg(key, beforeData, newDataObject,operate);
//			luckysheetHis.setChangeDesc(msg);
//			Object beforeObj = beforeData.get(key);
//			if(beforeObj == null)
//			{
//				beforeObj = beforeData.get(key.toLowerCase());
//				if(beforeObj == null)
//				{
//					beforeObj = new JSONObject();
//				}
//			}
//			luckysheetHis.setBeforeJson(beforeObj);
//			luckysheetHis.setBson(newDataObject);
//		}
//		luckysheetHis.setOperateKey(key);
//		luckysheetHis.setSheetIndex(index);
//		luckysheetHis.setListId(listId);
//		luckysheetHis.setRemark(operate);
//		luckysheetHis.setType(1);
//		luckysheetHis.setOperator(userInfoDto.getUserName());
//		luckysheetHis.setCreator(userInfoDto.getUserId());
//		luckysheetHis.setCreateTime(new Date());
//		luckysheetHis.setUpdater(userInfoDto.getUserId());
//		luckysheetHis.setUpdateTime(new Date());
//		if(StringUtil.isNotEmpty(luckysheetHis.getChangeDesc()))
//		{
//			this.save(luckysheetHis);
//		}
//	}
//	
//	private String getConfigDiffMsg(JSONObject beforeConfig,JSONObject newConfig,String operate) {
//		String result = "";
//		Map<String, JSONObject> diffResult = null;
//		if("撤销".equals(operate))
//		{
//			diffResult = StringUtil.compare2MapDiff2(beforeConfig,newConfig);
//		}else {
//			diffResult = StringUtil.compare2MapDiff2(newConfig,beforeConfig);
//		}
//		if(diffResult != null && !diffResult.isEmpty())
//		{
//			JSONObject changeBefore = diffResult.get("changeBefore");
//			JSONObject changeAfter = diffResult.get("changeAfter");
//			if(!StringUtil.isEmptyMap(changeAfter))
//			{
//				if(changeAfter.containsKey("merge"))
//				{
//					if("撤销".equals(operate))
//					{
//						result = "取消合并单元格，取消信息-";
//					}else {
//						result = "新增合并单元格，合并信息-";
//					}
//					JSONObject beforeMerge = changeBefore.getJSONObject("merge");
//					JSONObject newMerge = changeAfter.getJSONObject("merge");
//					Map<String, JSONObject> mergeDiff = StringUtil.compare2MapDiff2(newMerge,beforeMerge);
//					if(mergeDiff == null || StringUtil.isEmptyMap(mergeDiff.get("changeAfter")))
//					{
//						if("撤销".equals(operate))
//						{
//							result = "新增合并单元格，合并信息-";
//						}else {
//							result = "取消合并单元格，取消信息-";
//						}
//						mergeDiff = StringUtil.compare2MapDiff2(beforeMerge,newMerge);
//					}
//					if(mergeDiff != null &&!StringUtil.isEmptyMap(mergeDiff.get("changeAfter")))
//					{
//						JSONObject diff = mergeDiff.get("changeAfter");
//						Set<String> set = diff.keySet();
//						String info = "";
//						int i = 0;
//						for (String k : set) {
//						    JSONObject merge = diff.getJSONObject(k);
//						    int r = merge.getIntValue("r");
//						    int c = merge.getIntValue("c");
//						    int rs = merge.getIntValue("rs");
//						    int cs = merge.getIntValue("cs");
//						    info = "起始行号："+(r+1)+"，起始列号："+SheetUtil.excelColIndexToStr(c+1)
//						    +"，合并行数："+rs +"，合并列数："+cs;
//						    if(i > 0)
//						    {
//						    	info = info + "；";
//						    }
//						    i++;
//						}
//						result = result + info;
//					}else {
//						result = "";
//					}
//				}else if(changeAfter.containsKey("rowlen"))
//				{//行高
//					JSONObject beforeRowlen = changeBefore.getJSONObject("rowlen");
//					JSONObject newRowlen = changeAfter.getJSONObject("rowlen");
//					Map<String, JSONObject> rowlenDiff = StringUtil.compare2MapDiff2(newRowlen,beforeRowlen);
//				}
//			}
//		}
//		return result;
//	}
//
//
//	/**  
//	 * @MethodName: saveSheetDelConfig
//	 * @Description: 删除config中的属性
//	 * @author caiyang
//	 * @param beforeData
//	 * @param newData
//	 * @param key
//	 * @param wsUserModel
//	 * @param index
//	 * @param listId
//	 * @param operate
//	 * @see com.springreport.api.luckysheethis.ILuckysheetHisService#saveSheetDelConfig(com.alibaba.fastjson.JSONObject, com.alibaba.fastjson.JSONObject, java.lang.String, com.springreport.websocket.WSUserModel, java.lang.String, java.lang.String, java.lang.String)
//	 * @date 2023-08-27 08:00:18 
//	 */
//	@Override
//	public void saveSheetDelConfig(JSONObject beforeData, JSONObject newData, String key, WSUserModel wsUserModel,
//			String index, String listId, String operate) {
//		LuckysheetHis luckysheetHis = new LuckysheetHis();
//		UserInfoDto userInfoDto = JWTUtil.getUserInfo(wsUserModel.getToken());
//		String[] keys = key.split("\\.");
//		Object beforeObj = beforeData.getJSONObject(keys[1]);
//		if(beforeObj != null && !"{}".equals(String.valueOf(beforeObj)))
//		{
//			if("config.rowlen".equals(key))
//			{//清空行高信息
//				luckysheetHis.setChangeDesc("清空行高信息");
//			}else if("config.columnlen".equals(key))
//			{
//				luckysheetHis.setChangeDesc("清空列宽信息");
//			}else if("config.rowhidden".equals(key))
//			{
//				luckysheetHis.setChangeDesc("清空隐藏行信息");
//			}else if("config.colhidden".equals(key))
//			{
//				luckysheetHis.setChangeDesc("清空隐藏列信息");
//			}else if("config.borderInfo".equals(key))
//			{
//				luckysheetHis.setChangeDesc("清空边框信息");
//			}
//			luckysheetHis.setOperateKey(key);
//			luckysheetHis.setBeforeJson(beforeData);
//			luckysheetHis.setBson(newData);
//			luckysheetHis.setSheetIndex(index);
//			luckysheetHis.setListId(listId);
//			luckysheetHis.setRemark(operate);
//			luckysheetHis.setType(1);
//			luckysheetHis.setOperator(userInfoDto.getUserName());
//			luckysheetHis.setCreator(userInfoDto.getUserId());
//			luckysheetHis.setCreateTime(new Date());
//			luckysheetHis.setUpdater(userInfoDto.getUserId());
//			luckysheetHis.setUpdateTime(new Date());
//			this.save(luckysheetHis);
//		}
//	}
//	
//	/**  
//	 * @MethodName: getDiffMsg
//	 * @Description: 获取对比信息
//	 * @author caiyang
//	 * @param key
//	 * @param beforeData
//	 * @param newData
//	 * @return String
//	 * @date 2023-08-28 10:54:49 
//	 */ 
//	private String getDiffMsg(String key,JSONObject beforeData,Object newData,String operate)
//	{
//		String result = "";
//		if(beforeData == null)
//		{
//			return result;
//		}
//		if("calcChain".equals(key))
//		{
//			JSONArray beforeChain = beforeData.getJSONArray(key);
//			if(beforeChain == null)
//			{
//				beforeChain = beforeData.getJSONArray(key.toLowerCase());
//			}
//			if(beforeChain == null)
//			{
//				beforeChain = new JSONArray();
//			}
//			JSONArray newChain = (JSONArray) newData;
//			String beforeMd5 = Md5Util.generateMd5(JSON.toJSONString(beforeChain));
//			String newMd5 = Md5Util.generateMd5(JSON.toJSONString(newChain));
//			if(!newMd5.equals(beforeMd5))
//			{
//				if(beforeChain.size() == newChain.size())
//				{
//					result = "公式进行修改";
//				}else if(beforeChain.size()>newChain.size())
//				{
//					result = "有公式删除";
//				}else {
//					result = "有公式新增";
//				}
//			}
//		}else if("luckysheet_conditionformat_save".equals(key) || "luckysheet_alternateformat_save".equals(key))
//		{
//			System.out.println(key);
//		}else {
//			JSONObject beforeObj = beforeData.getJSONObject(key);
//			if(beforeObj == null)
//			{
//				beforeObj = beforeData.getJSONObject(key.toLowerCase());
//				if(beforeObj == null)
//				{
//					beforeObj = new JSONObject();
//				}
//			}
//			JSONObject newObj = new JSONObject();
//			if(newData instanceof JSON)
//			{
//				newObj = JSON.parseObject(JSON.toJSONString(newData));
//			}
//			if("images".equals(key))
//			{
//				newObj = this.sortImageDatas(newObj);
//				beforeObj = this.sortImageDatas(beforeObj);
//			}
//			Map<String, JSONObject> diffResult = null;
//			boolean isReservse = false;//是否反过来比较
//			diffResult = StringUtil.compare2MapDiff2(newObj,
//					beforeObj);
//			if(diffResult == null || StringUtil.isEmptyMap(diffResult.get("changeAfter")))
//			{
//				isReservse = true;
//				diffResult = StringUtil.compare2MapDiff2(beforeObj,newObj);
//			}
//			if(diffResult != null && !StringUtil.isEmptyMap(diffResult.get("changeAfter")))
//			{
//				JSONObject changeBefore = diffResult.get("changeBefore");
//				JSONObject changeAfter = diffResult.get("changeAfter");
//				if("hyperlink".equals(key))
//				{
//					Set<String> set = changeAfter.keySet();
//					for (String k : set) {
//						int r = Integer.parseInt(k.split("_")[0]);
//						int c = Integer.parseInt(k.split("_")[1]);
//						String linkAddress = changeAfter.getJSONObject(k).getString("linkAddress");
//						String columnName = SheetUtil.excelColIndexToStr((c+1));
//						result = "单元格"+columnName+(r+1)+(isReservse?"删除":"添加")+"超链接:"+linkAddress;
//						break;
//					}
//				}else if("frozen".equals(key))
//				{
//					result = operate;
//				}else if("filter_select".equals(key))
//				{
//					result = operate;
//				}else if("dataVerification".equals(key))
//				{
//					Set<String> set = changeAfter.keySet();
//					String cell = "";
//					int i = 0;
//					for (String k : set) {
//						int r = Integer.parseInt(k.split("_")[0]);
//						int c = Integer.parseInt(k.split("_")[1]);
//						String columnName = SheetUtil.excelColIndexToStr((c+1));
//						if(i > 0)
//						{
//							cell = cell + "，" + columnName + (r+1) ;
//						}else {
//							cell = cell + columnName + (r+1);
//						}
//						i ++ ;
//					}
//					if(isReservse)
//					{
//						result = "为以下单元格删除数据验证信息："+cell;
//					}else {
//						result = "为以下单元格添加数据验证信息："+cell;
//					}
//				}else if("rowlen".equals(key))
//				{//行高
//					result = "修改行高，";
//					Set<String> set = changeAfter.keySet();
//					int i = 0;
//					for (String k : set) {
//						String r = String.valueOf((Integer.parseInt(k) + 1));
//						String newlen = changeAfter.getString(k);
//						String before = StringUtil.isNullOrEmpty(changeBefore.getString(k))?"19":changeBefore.getString(k);
//						String msg = MessageUtil.getValue("luckysheet.rowlen.change", new String[] {r,before,newlen});
//						if(i > 0)
//						{
//							result = result + "，" + msg;
//						}else {
//							result = result + msg;
//						}
//						i++;
//					}
//				}else if("columnlen".equals(key))
//				{//列宽
//					result = "修改列宽，";
//					Set<String> set = changeAfter.keySet();
//					int i = 0;
//					for (String k : set) {
//						String column = SheetUtil.excelColIndexToStr(Integer.parseInt(k) + 1);
//						String newlen = changeAfter.getString(k);
//						String before = StringUtil.isNullOrEmpty(changeBefore.getString(k))?"73":changeBefore.getString(k);
//						String msg = MessageUtil.getValue("luckysheet.columnlen.change", new String[] {column,before,newlen});
//						if(i > 0)
//						{
//							result = result + "，" + msg;
//						}else {
//							result = result + msg;
//						}
//						i++;
//					}
//				}else if("images".equals(key)) {
//					result = operate;
//				}else if("rowhidden".equals(key)) {
//					result = operate;
//				}else if("colhidden".equals(key)) {
//					result = operate;
//				}
//			}
//			
//		}
//		return result;
//	}
//	
//	private JSONObject sortImageDatas(JSONObject datas) {
//		JSONObject result = new JSONObject();
//		if(!StringUtil.isEmptyMap(datas))
//		{
//			Set<String> set = datas.keySet();
//			for (String k : set) {
//				JSONObject image = datas.getJSONObject(k);
//				JSONObject newData = new JSONObject();
//				newData.put("border", image.get("border"));
//				newData.put("originHeight", image.get("originHeight"));
//				newData.put("fixedLeft", image.get("fixedLeft"));
//				newData.put("default", image.get("default"));
//				newData.put("src", image.get("src"));
//				newData.put("originWidth", image.get("originWidth"));
//				newData.put("fixedTop", image.get("fixedTop"));
//				newData.put("isFixedPos", image.get("isFixedPos"));
//				newData.put("type", image.get("type"));
//				newData.put("crop", image.get("crop"));
//				result.put(k, newData);
//			}
//		}
//		return result;
//	}
}