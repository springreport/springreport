package com.springreport.impl.luckysheetreportformshis;

import com.springreport.entity.luckysheetreportformshis.LuckysheetReportFormsHis;
import com.springreport.mapper.luckysheetreportformshis.LuckysheetReportFormsHisMapper;
import com.springreport.api.luckysheetreportformshis.ILuckysheetReportFormsHisService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.springreport.util.CusAccessObjectUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.LuckysheetReportFormsHis.LuckysheetReportFormsHisDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.SqlOrderEnum;

 /**  
* @Description: LuckysheetReportFormsHis服务实现
* @author 
* @date 2023-01-29 04:05:08
* @version V1.0  
 */
@Service
public class LuckysheetReportFormsHisServiceImpl extends ServiceImpl<LuckysheetReportFormsHisMapper, LuckysheetReportFormsHis> implements ILuckysheetReportFormsHisService {
  
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(LuckysheetReportFormsHis model) {
		PageEntity result = new PageEntity();
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("create_time", SqlOrderEnum.DESC.getCode());
		map.put("id", SqlOrderEnum.DESC.getCode());
		String orderSql = StringUtil.formatOrderSql(map);
		model.setOrderSql(orderSql);
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<LuckysheetReportFormsHisDto> list = this.baseMapper.getTableList(model);
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
	public BaseEntity insert(LuckysheetReportFormsHis model) {
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
	public BaseEntity update(LuckysheetReportFormsHis model) {
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
		LuckysheetReportFormsHis luckysheetReportFormsHis = new LuckysheetReportFormsHis();
		luckysheetReportFormsHis.setId(id);
		luckysheetReportFormsHis.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(luckysheetReportFormsHis);
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
		List<LuckysheetReportFormsHis> list = new ArrayList<LuckysheetReportFormsHis>();
		for (int i = 0; i < ids.size(); i++) {
			LuckysheetReportFormsHis luckysheetReportFormsHis = new LuckysheetReportFormsHis();
			luckysheetReportFormsHis.setId(ids.get(i));
			luckysheetReportFormsHis.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(luckysheetReportFormsHis);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @MethodName: saveReportHis
	 * @Description: 保存上报历史记录
	 * @author caiyang
	 * @param reportDatas
	 * @param basicData
	 * @param tplId
	 * @see com.springreport.api.luckysheetreportformshis.ILuckysheetReportFormsHisService#saveReportHis(java.util.Map, java.util.Map)
	 * @date 2023-01-29 04:15:20 
	 */  
	@Override
	@Async
	public void saveReportHis(Map<String, Map<String, Object>> reportDatas,
			Map<String, Map<String, Object>> basicDatas,Long tplId,String ip
			,UserInfoDto userInfoDto) {
		List<LuckysheetReportFormsHis> luckysheetReportFormsHises = new ArrayList<>();
		Date current = new Date();
		if(reportDatas != null)
		{
			reportDatas.forEach((key, value) -> {
				value.remove("isSRrowChanged");
				value.remove("isSRInsertData");
				LuckysheetReportFormsHis formsHis = new LuckysheetReportFormsHis();
				formsHis.setTplId(tplId);
				String sheetIndex = key.split("\\|")[0];
				String datasourceId = key.split("\\|")[1];
				String tableName = key.split("\\|")[2];
				formsHis.setSheetIndex(sheetIndex);
				formsHis.setDatasourceId(Long.parseLong(datasourceId));
				formsHis.setTableName(tableName);
				formsHis.setReportData(JSON.toJSONString(value, SerializerFeature.WriteMapNullValue));
				Map<String, Object> basicData = basicDatas.get(key);
				if(basicData != null)
				{
					formsHis.setBasicData(JSON.toJSONString(basicData, SerializerFeature.WriteMapNullValue));
				}
				Map<String, String> diff = StringUtil.compare2MapDiff(value, basicData);
				if(diff != null)
				{
					String changeAfter = diff.get("changeAfter");
					if(StringUtil.isNotEmpty(changeAfter) && !"{}".equals(changeAfter)) {
						formsHis.setOperateIp(ip);
						formsHis.setChangeDataBefore(diff.get("changeBefore"));
						formsHis.setChangeDataAfter(changeAfter);
						formsHis.setCreator(userInfoDto.getUserId());
						formsHis.setUpdater(userInfoDto.getUserId());
						formsHis.setCreateTime(current);
						formsHis.setUpdateTime(current);
						luckysheetReportFormsHises.add(formsHis);
					}
				}
				
			});
			this.saveBatch(luckysheetReportFormsHises);
		}
		
	}


	/**  
	 * @MethodName: saveDeleteHis
	 * @Description: 保存删除历史
	 * @author caiyang
	 * @param model
	 * @param tplId
	 * @param ip
	 * @param userInfoDto
	 * @see com.springreport.api.luckysheetreportformshis.ILuckysheetReportFormsHisService#saveDeleteHis(com.alibaba.fastjson.JSONObject, java.lang.Long, java.lang.String, com.springreport.base.UserInfoDto)
	 * @date 2025-02-18 09:37:02 
	 */
	@Override
	@Async
	public void saveDeleteHis(JSONObject model, String ip, UserInfoDto userInfoDto) {
		LuckysheetReportFormsHis formsHis = new LuckysheetReportFormsHis();
		Date current = new Date();
		formsHis.setTplId(model.getLong("tplId"));
		String sheetIndex = model.getString("sheetIndex");
		String datasourceId = model.getString("datasourceId");
		String tableName = model.getString("table");
		String column = model.getString("column");
		Object value = model.get("value");
		JSONObject reportData = new JSONObject();
		reportData.put(column, value);
		formsHis.setSheetIndex(sheetIndex);
		formsHis.setDatasourceId(Long.parseLong(datasourceId));
		formsHis.setTableName(tableName);
		formsHis.setReportData(JSON.toJSONString(reportData, SerializerFeature.WriteMapNullValue));
		formsHis.setBasicData(JSON.toJSONString(reportData, SerializerFeature.WriteMapNullValue));
		formsHis.setOperateIp(ip);
		formsHis.setChangeDataBefore(JSON.toJSONString(reportData, SerializerFeature.WriteMapNullValue));
		formsHis.setChangeDataAfter("{}");
		formsHis.setCreator(userInfoDto.getUserId());
		formsHis.setUpdater(userInfoDto.getUserId());
		formsHis.setCreateTime(current);
		formsHis.setUpdateTime(current);
		this.save(formsHis);
	}
}