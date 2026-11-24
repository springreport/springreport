package com.springreport.api.reportdatasourcetables;
import java.sql.SQLException;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.reportdatasourcetables.ReportDatasourceTables;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

 /**  
* @Description: ReportDatasourceTables服务接口
* @author 
* @date 2026-11-24 10:51:13
* @version V1.0  
 */
public interface IReportDatasourceTablesService extends IService<ReportDatasourceTables> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(ReportDatasourceTables model);

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
	BaseEntity insert(ReportDatasourceTables model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(ReportDatasourceTables model);
	
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
	*<p>Title: datasouceWhiteCheck</p>
	*<p>Description: 对数据源的表和列名进行白名单校验，防止注入</p>
	* @author caiyang
	* @param list
	* @return
	 * @throws Exception 
	 * @throws SQLException 
	*/
	void datasouceWhiteCheck(Long datasourceId,String tableName,String columnName) throws  Exception;
}
