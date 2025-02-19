package com.springreport.api.luckysheetreportformshis;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.luckysheetreportformshis.LuckysheetReportFormsHis;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;

 /**  
* @Description: LuckysheetReportFormsHis服务接口
* @author 
* @date 2023-01-29 04:05:08
* @version V1.0  
 */
public interface ILuckysheetReportFormsHisService extends IService<LuckysheetReportFormsHis> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(LuckysheetReportFormsHis model);

	/**
	*<p>Title: getDetail</p>
	*<p>Description: 获取详情</p>
	* @author caiyang
	* @param id
	* @return
	*/
	BaseEntity getDetail(Long id);
	
	/**
	*<p>Title: insert</p>
	*<p>Description: 新增数据</p>
	* @author caiyang
	* @param model
	* @return
	*/
	BaseEntity insert(LuckysheetReportFormsHis model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(LuckysheetReportFormsHis model);
	
	/**
	*<p>Title: delete</p>
	*<p>Description: 单条删除数据</p>
	* @author caiyang
	* @param model
	* @return
	*/
	BaseEntity delete(Long id);
	
	/**
	*<p>Title: deleteBatch</p>
	*<p>Description: 批量删除数据</p>
	* @author caiyang
	* @param list
	* @return
	*/
	BaseEntity deleteBatch(List<Long> ids);
	
	/**  
	 * @MethodName: saveReportHis
	 * @Description: 保存上报历史记录
	 * @author caiyang
	 * @param reportDatas
	 * @param basicData 
	 * @return void
	 * @date 2023-01-29 04:10:44 
	 */  
	void saveReportHis(Map<String, Map<String, Object>> reportDatas,Map<String, Map<String, Object>> basicDatas,Long tplId,String ip,UserInfoDto userInfoDto);

	/**  
	 * @MethodName: saveDeleteHis
	 * @Description: 保存删除历史
	 * @author caiyang
	 * @param model
	 * @param tplId
	 * @param ip
	 * @param userInfoDto void
	 * @date 2025-02-18 09:36:48 
	 */ 
	void saveDeleteHis(JSONObject model,String ip,UserInfoDto userInfoDto);
}
