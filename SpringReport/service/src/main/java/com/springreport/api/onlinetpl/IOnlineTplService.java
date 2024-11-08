package com.springreport.api.onlinetpl;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.springreport.entity.onlinetpl.OnlineTpl;
import com.springreport.entity.reporttype.ReportType;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.onlinetpl.MesRangeAuthDto;
import com.springreport.dto.onlinetpl.OnlineTplTreeDto;
import com.springreport.dto.onlinetpl.ResOnlineTplInfo;
import com.springreport.dto.onlinetpl.ResSaveOnlineDocDto;
import com.springreport.dto.reporttpl.ResSheetsSettingsDto;

 /**  
* @Description: OnlineTpl服务接口
* @author 
* @date 2023-02-06 08:03:24
* @version V1.0  
 */
public interface IOnlineTplService extends IService<OnlineTpl> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	List<OnlineTplTreeDto> tablePagingQuery(ReportType model);
	
	/**  
	 * @MethodName: getChildren
	 * @Description: 获取文件夹数据
	 * @author caiyang
	 * @param model
	 * @return List<OnlineTplTreeDto>
	 * @date 2024-11-05 08:55:31 
	 */ 
	List<OnlineTplTreeDto> getChildren(OnlineTpl model);

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
	BaseEntity insert(OnlineTpl model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(OnlineTpl model);
	
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
	 * @MethodName: getOnlineTplSettings
	 * @Description: 获取在线文档内容
	 * @author caiyang
	 * @param onlineTpl
	 * @param userInfoDto
	 * @return 
	 * @return ResSheetsSettingsDto
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2023-02-07 09:31:30 
	 */  
	ResSheetsSettingsDto getOnlineTplSettings(OnlineTpl onlineTpl,UserInfoDto userInfoDto) throws JsonMappingException, JsonProcessingException;
	
	/**  
	 * @MethodName: getTplInfo
	 * @Description: 获取文档信息
	 * @author caiyang
	 * @param onlineTpl
	 * @param userInfoDto
	 * @return ResOnlineTplInfo
	 * @date 2024-03-09 10:19:26 
	 */ 
	ResOnlineTplInfo getOnlineTplInfo(OnlineTpl onlineTpl,UserInfoDto userInfoDto);
	
	/**  
	 * @MethodName: rangeAuth
	 * @Description: 协同文档选区授权
	 * @author caiyang
	 * @param model
	 * @param userInfoDto void
	 * @date 2024-03-09 01:15:05 
	 */ 
	void rangeAuth(MesRangeAuthDto model,UserInfoDto userInfoDto);
	
	/**  
	 * @MethodName: deletRangeAuth
	 * @Description: 删除选区授权
	 * @author caiyang
	 * @param model
	 * @param userInfoDto void
	 * @date 2024-03-09 05:22:42 
	 */ 
	void deletRangeAuth(MesRangeAuthDto model,UserInfoDto userInfoDto);
	
	/**  
	 * @MethodName: getCoeditAuth
	 * @Description: 获取协同文档授权范围
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return JSONObject
	 * @date 2024-03-11 11:42:13 
	 */ 
	JSONObject getCoeditAuth(OnlineTpl model,UserInfoDto userInfoDto);
}
