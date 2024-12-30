package com.springreport.api.reportdatasource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.reportdatasource.ReportDatasource;
import com.springreport.entity.reporttpldataset.ReportTplDataset;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.reportdatasource.MesGetSelectDataDto;
import com.springreport.dto.reportdatasource.MesReportDatasourceDto;
import com.springreport.dto.reporttpldatasource.MesExecSqlDto;

 /**  
* @Description: ReportDatasource服务接口
* @author 
* @date 2021-02-09 01:18:28
* @version V1.0  
 */
public interface IReportDatasourceService extends IService<ReportDatasource> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(ReportDatasource model);

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
	BaseEntity insert(ReportDatasource model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(ReportDatasource model);
	
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
	*<p>Title: getReportDatasource</p>
	*<p>Description: 获取数据源</p>
	* @author caiyang
	* @return
	*/
	List<ReportDatasource> getReportDatasource(MesReportDatasourceDto mesReportDatasourceDto);
	
	/**
	*<p>Title: execSql</p>
	*<p>Description: </p>
	* @author caiyang
	* @param mesExecSqlDto
	* @return
	 * @throws SQLException 
	 * @throws Exception 
	*/
	List<Map<String, Object>> execSql(MesExecSqlDto mesExecSqlDto,UserInfoDto userInfoDto) throws SQLException, Exception;
	
	/**  
	 * @Title: connectionTest
	 * @Description: 数据源连接测试
	 * @param reportDatasource
	 * @return
	 * @author caiyang
	 * @date 2022-04-24 01:22:07 
	 */ 
	BaseEntity connectionTest(ReportDatasource reportDatasource);
	
	/**  
	 * @Title: getDatasourceSelectData
	 * @Description: 获取下拉框数据
	 * @param mesGetSelectDataDto
	 * @return
	 * @author caiyang
	 * @date 2022年5月22日13:38:57
	 */
	List<Map<String, Object>> getDatasourceSelectData(MesGetSelectDataDto mesGetSelectDataDto);
	
	/**  
	 * @MethodName: cacheDatasetsColumns
	 * @Description: 缓存数据集列
	 * @author caiyang
	 * @param datasets 
	 * @return void
	 * @throws SQLException 
	 * @throws Exception 
	 * @date 2022-10-13 04:54:28 
	 */  
	void cacheDatasetsColumns(List<ReportTplDataset> datasets,UserInfoDto userInfoDto) throws SQLException, Exception;
	
	/**  
	 * @MethodName: getDatabseTables
	 * @Description: 获取数据库的所有表信息
	 * @author caiyang
	 * @param datasource 
	 * @return void
	 * @date 2022-11-15 07:59:14 
	 */  
	List<Map<String, String>> getDatabseTables(ReportDatasource datasource);
	
	/**  
	 * @MethodName: parseApiResultAttr
	 * @Description: 解析api数据集结果属性
	 * @author caiyang
	 * @param reportDatasource
	 * @return JSONArray
	 * @date 2024-12-24 03:01:59 
	 */ 
	JSONArray parseApiResultAttr(ReportDatasource reportDatasource);
}
