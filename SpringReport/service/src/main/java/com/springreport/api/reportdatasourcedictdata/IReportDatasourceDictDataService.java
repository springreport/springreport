package com.springreport.api.reportdatasourcedictdata;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.reportdatasourcedictdata.ReportDatasourceDictData;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

 /**  
* @Description: ReportDatasourceDictData服务接口
* @author 
* @date 2022-11-21 01:46:25
* @version V1.0  
 */
public interface IReportDatasourceDictDataService extends IService<ReportDatasourceDictData> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(ReportDatasourceDictData model);

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
	BaseEntity insert(ReportDatasourceDictData model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(ReportDatasourceDictData model);
	
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
	 * @MethodName: getDictDatas
	 * @Description: 获取数据源数据字典值
	 * @author caiyang
	 * @param model
	 * @return 
	 * @return List<ReportDatasourceDictData>
	 * @date 2022-11-21 04:19:49 
	 */  
	List<ReportDatasourceDictData> getDictDatas(ReportDatasourceDictData model);
}
