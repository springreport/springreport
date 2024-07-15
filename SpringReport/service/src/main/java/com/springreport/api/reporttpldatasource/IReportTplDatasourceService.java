package com.springreport.api.reporttpldatasource;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.reporttpldatasource.ReportTplDatasource;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.dto.reporttpldatasource.MesExecSqlDto;
import com.springreport.dto.reporttpldatasource.ReportTplDatasourceDto;

 /**  
* @Description: ReportTplDatasource服务接口
* @author 
* @date 2021-02-13 11:16:43
* @version V1.0  
 */
public interface IReportTplDatasourceService extends IService<ReportTplDatasource> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(ReportTplDatasource model);

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
	BaseEntity insert(ReportTplDatasource model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(ReportTplDatasource model);
	
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
	*<p>Title: getReportTplDatasoure</p>
	*<p>Description: 获取报表模板数据源</p>
	* @author caiyang
	* @param reportTplDatasource
	* @return
	*/
	List<ReportTplDatasourceDto> getReportTplDatasoure(ReportTplDatasource reportTplDatasource);
	
}
