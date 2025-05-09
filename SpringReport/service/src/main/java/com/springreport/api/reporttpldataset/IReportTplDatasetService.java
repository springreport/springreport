package com.springreport.api.reporttpldataset;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.reportdatasource.ReportDatasource;
import com.springreport.entity.reporttpldataset.ReportTplDataset;
import com.springreport.entity.reporttpldatasetgroup.ReportTplDatasetGroup;

import net.sf.jsqlparser.JSQLParserException;

import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.reportdatasource.ApiTestResultDto;
import com.springreport.dto.reporttpldataset.DatasetsParamDto;
import com.springreport.dto.reporttpldataset.MesGetRelyOnSelectData;
import com.springreport.dto.reporttpldataset.MesScreenGetSqlDataDto;
import com.springreport.dto.reporttpldataset.ReportDatasetDto;
import com.springreport.dto.reporttpldataset.ReportTplDatasetDto;

 /**  
* @Description: ReportTplDataset服务接口
* @author 
* @date 2021-02-13 11:16:39
* @version V1.0  
 */
public interface IReportTplDatasetService extends IService<ReportTplDataset> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(ReportTplDataset model);

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
	BaseEntity insert(ReportTplDataset model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(ReportTplDataset model);
	
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
	*<p>Title: getTplDatasets</p>
	*<p>Description: 获取报表模板关联的数据集</p>
	* @author caiyang
	* @return
	 * @throws SQLException 
	 * @throws Exception 
	*/
	List<ReportDatasetDto> getTplDatasets(ReportTplDataset dataset,UserInfoDto userInfoDto) throws SQLException, Exception;
	
	/**  
	 * @MethodName: getDatasetColumns
	 * @Description: 获取数据集字段
	 * @author caiyang
	 * @param dataset
	 * @return 
	 * @return List<Map<String,Object>>
	 * @throws SQLException 
	 * @throws Exception 
	 * @date 2022-10-12 06:31:31 
	 */  
	List<Map<String, Object>> getDatasetColumns(ReportTplDataset dataset,UserInfoDto userInfoDto) throws SQLException, Exception;
	
	/**  
	 * @MethodName: getApiDefaultRequestResult
	 * @Description: 获取api数据集默认参数请求结果
	 * @author caiyang
	 * @param dataset 
	 * @return void
	 * @throws ParseException 
	 * @date 2023-01-11 10:41:37 
	 */  
	ApiTestResultDto getApiDefaultRequestResult(ReportTplDataset dataset) throws ParseException;
	
	/**
	*<p>Title: addTplDataSets</p>
	*<p>Description: 报表模板添加数据集</p>
	* @author caiyang
	* @param reportTplDataset
	 * @throws SQLException 
	 * @throws Exception 
	*/
	ReportDatasetDto addTplDataSets(ReportTplDataset reportTplDataset,UserInfoDto userInfoDto) throws SQLException, Exception;
	
	/**  
	 * @Title: getTplDatasetAndDatasource
	 * @Description: 根据模板id和数据集名称获取数据集和数据源
	 * @param tplId
	 * @param datasetName
	 * @return
	 * @author caiyang
	 * @throws SQLException 
	 * @throws Exception 
	 * @date 2021-05-26 06:17:03 
	 */ 
	Map<String, Object> getTplDatasetAndDatasource(Long tplId,String datasetName,String merchantNo) throws SQLException, Exception;
	
	/**  
	 * @MethodName: getTplDatasetAndDatasource
	 * @Description: 根据数据集id获取数据集和数据源
	 * @author caiyang
	 * @param datasetId
	 * @return
	 * @throws SQLException
	 * @throws Exception Map<String,Object>
	 * @date 2024-07-10 09:50:36 
	 */ 
	Map<String, Object> getTplDatasetAndDatasource(Long datasetId) throws SQLException, Exception;
	
	/**  
	 * @Title: getReportDatasetsParam
	 * @Description: 获取报表数据集参数
	 * @param reportTplDataset
	 * @return
	 * @author caiyang
	 * @throws ParseException 
	 * @date 2021-06-03 02:24:46 
	 */ 
	Map<String, Object> getReportDatasetsParam(ReportTplDatasetDto reportTplDataset) throws Exception;
	
	/**  
	 * @Title: getTplDatasetColumns
	 * @Description: 获取数据集所有的列
	 * @param reportTplDataset
	 * @author caiyang
	 * @throws SQLException 
	 * @throws Exception 
	 * @date 2021-07-14 05:01:40 
	 */ 
	List<Map<String, Object>> getTplDatasetColumns(ReportTplDataset reportTplDataset,UserInfoDto userInfoDto) throws SQLException, Exception;
	
	
	/**  
	 * @MethodName: getRelyOnSelectData
	 * @Description: 获取依赖项的下拉数据
	 * @author caiyang
	 * @param mesGetRelyOnSelectData
	 * @return 
	 * @return List<Map<String,Object>>
	 * @throws JSQLParserException 
	 * @date 2022-08-08 08:21:46 
	 */  
	List<Map<String, Object>> getRelyOnSelectData(MesGetRelyOnSelectData mesGetRelyOnSelectData) throws JSQLParserException;
	
	/**  
	 * @MethodName: getSelectData
	 * @Description: 获取下拉数据
	 * @author caiyang
	 * @param mesGetRelyOnSelectData
	 * @return
	 * @throws JSQLParserException List<Map<String,Object>>
	 * @date 2024-01-22 08:47:38 
	 */ 
	List<Map<String, Object>> getSelectData(MesGetRelyOnSelectData mesGetRelyOnSelectData) throws JSQLParserException;
	
	/**  
	 * @MethodName: getTreeSelectData
	 * @Description: 获取下拉树数据
	 * @author caiyang
	 * @param mesGetRelyOnSelectData
	 * @return
	 * @throws JSQLParserException ReportParamTreeDto
	 * @date 2024-01-23 10:12:01 
	 */ 
	List<Map<String, Object>> getTreeSelectData(MesGetRelyOnSelectData mesGetRelyOnSelectData) throws JSQLParserException;
	
	/**  
	 * @MethodName: getDatasetDatasource
	 * @Description: 获取数据集对应的数据源
	 * @author caiyang
	 * @param reportTplDatasetDto
	 * @return Object
	 * @throws Exception 
	 * @date 2024-05-07 12:17:09 
	 */ 
	Object getDatasetDatasource(ReportDatasource reportDatasource) throws Exception;
	
	/**
	*<p>Title: getTplGroupDatasets</p>
	*<p>Description: 获取报表模板关联的数据集(分组)</p>
	* @author caiyang
	* @return
	 * @throws SQLException 
	 * @throws Exception 
	*/
	List<ReportTplDatasetGroup> getTplGroupDatasets(ReportTplDataset dataset,UserInfoDto userInfoDto) throws SQLException, Exception;
}
