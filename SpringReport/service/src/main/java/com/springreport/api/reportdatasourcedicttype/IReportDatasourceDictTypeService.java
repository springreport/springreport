package com.springreport.api.reportdatasourcedicttype;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.reportdatasourcedicttype.ReportDatasourceDictType;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

 /**  
* @Description: ReportDatasourceDictType服务接口
* @author 
* @date 2022-11-21 01:46:20
* @version V1.0  
 */
public interface IReportDatasourceDictTypeService extends IService<ReportDatasourceDictType> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(ReportDatasourceDictType model);

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
	BaseEntity insert(ReportDatasourceDictType model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(ReportDatasourceDictType model);
	
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
	 * @MethodName: getDatasourceDictTypes
	 * @Description: 获取数据源字典类型
	 * @author caiyang
	 * @param model
	 * @return 
	 * @return List<ReportDatasourceDictType>
	 * @date 2022-11-21 03:55:28 
	 */  
	List<ReportDatasourceDictType> getDatasourceDictTypes(ReportDatasourceDictType model);
}
