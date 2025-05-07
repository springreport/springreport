package com.springreport.impl.reporttpldataset;

import com.springreport.entity.doctpl.DocTpl;
import com.springreport.entity.reportdatasource.ReportDatasource;
import com.springreport.entity.reporttpl.ReportTpl;
import com.springreport.entity.reporttpldataset.ReportTplDataset;
import com.springreport.entity.reporttpldatasetgroup.ReportTplDatasetGroup;
import com.springreport.mapper.reporttpldataset.ReportTplDatasetMapper;
import com.springreport.api.doctpl.IDocTplService;
import com.springreport.api.reportdatasource.IReportDatasourceService;
import com.springreport.api.reporttpl.IReportTplService;
import com.springreport.api.reporttpldataset.IReportTplDatasetService;
import com.springreport.api.reporttpldatasetgroup.IReportTplDatasetGroupService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.springreport.util.CheckUtil;
import com.springreport.util.DateUtil;
import com.springreport.util.HttpClientUtil;
import com.springreport.util.InfluxDBConnection;
import com.springreport.util.JdbcUtils;
import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.RedisUtil;
import com.springreport.util.ReportDataUtil;
import com.springreport.util.StringUtil;
import com.github.pagehelper.PageHelper;

import net.sf.jsqlparser.JSQLParserException;

import com.springreport.base.BaseEntity;
import com.springreport.base.DataSourceConfig;
import com.springreport.base.EsDataSourceConfig;
import com.springreport.base.InfluxDbDataSourceConfig;
import com.springreport.base.PageEntity;
import com.springreport.base.TDengineConfig;
import com.springreport.base.TDengineConnection;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.dto.reportdatasource.ApiTestResultDto;
import com.springreport.dto.reporttpldataset.DatasetsParamDto;
import com.springreport.dto.reporttpldataset.MesGetRelyOnSelectData;
import com.springreport.dto.reporttpldataset.MesScreenGetSqlDataDto;
import com.springreport.dto.reporttpldataset.ReportDatasetDto;
import com.springreport.dto.reporttpldataset.ReportParamDto;
import com.springreport.dto.reporttpldataset.ReportTplDatasetDto;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.transaction.annotation.Transactional;

import com.springreport.enums.DatasetTypeEnum;
import com.springreport.enums.DefaultDateTypeEnum;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.ParamTypeEnum;
import com.springreport.enums.ProcedureParamTypeEnum;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.enums.SelectTypeEnum;
import com.springreport.enums.SqlTypeEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: ReportTplDataset服务实现
* @author 
* @date 2021-02-13 11:16:39
* @version V1.0  
 */
@Service
public class ReportTplDatasetServiceImpl extends ServiceImpl<ReportTplDatasetMapper, ReportTplDataset> implements IReportTplDatasetService {
  
	@Autowired
	@Lazy
	private IReportTplService iReportTplService;
	
	@Autowired
	private IReportDatasourceService iReportDatasourceService;
	
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	@Lazy
	private IDocTplService iDocTplService;
	
	@Autowired
	@Lazy
	private IReportTplDatasetGroupService iReportTplDatasetGroupService;
	
	@Value("${merchantmode}")
    private Integer merchantmode;
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(ReportTplDataset model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<ReportTplDataset> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(ReportTplDataset model) {
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
	public BaseEntity update(ReportTplDataset model) {
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
		ReportTplDataset dataset = this.getById(id);
		if(dataset == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"数据集信息"}));
		}
		ReportTplDataset reportTplDataset = new ReportTplDataset();
		reportTplDataset.setId(id);
		reportTplDataset.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(reportTplDataset);
		BaseEntity result = new BaseEntity();
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		if(DatasetTypeEnum.SQL.getCode().intValue() == dataset.getDatasetType().intValue())
		{
			redisUtil.del(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(id));
		}else {
			redisUtil.del(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(dataset.getDatasourceId()));
		}
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
		List<ReportTplDataset> list = new ArrayList<ReportTplDataset>();
		for (int i = 0; i < ids.size(); i++) {
			ReportTplDataset reportTplDataset = new ReportTplDataset();
			reportTplDataset.setId(ids.get(i));
			reportTplDataset.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(reportTplDataset);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**
	*<p>Title: getTplDatasets</p>
	*<p>Description: 获取报表模板关联的数据集</p>
	* @author caiyang
	* @return
	 * @throws Exception 
	*/
	@Override
	public List<ReportDatasetDto> getTplDatasets(ReportTplDataset dataset,UserInfoDto userInfoDto) throws Exception {
		List<ReportDatasetDto> result = new ArrayList<ReportDatasetDto>();
		QueryWrapper<ReportTplDataset> queryWrapper = new QueryWrapper<ReportTplDataset>();
		queryWrapper.eq("tpl_id", dataset.getTplId());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplDataset> reportTplDatasets = this.list(queryWrapper);
		if(reportTplDatasets == null) {
			reportTplDatasets = new ArrayList<>();
		}
		queryWrapper = new QueryWrapper<ReportTplDataset>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", dataset.getMerchantNo());
		}
		queryWrapper.eq("is_common", YesNoEnum.YES.getCode());
		queryWrapper.eq("common_type", dataset.getCommonType());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplDataset> commonDatasets = this.list(queryWrapper);
		if(commonDatasets != null) {
			reportTplDatasets.addAll(commonDatasets);
		}
		if(reportTplDatasets != null && reportTplDatasets.size() > 0)
		{
			ReportDatasetDto reportDatasetDto = null;
			for (int i = 0; i < reportTplDatasets.size(); i++) {
				reportDatasetDto = new ReportDatasetDto();
				BeanUtils.copyProperties(reportTplDatasets.get(i), reportDatasetDto);
				result.add(reportDatasetDto);
			}
			this.iReportDatasourceService.cacheDatasetsColumns(reportTplDatasets,userInfoDto);
		}
		return result;
	}
	
	/**  
	 * @MethodName: getDatasetColumns
	 * @Description: 获取数据集字段
	 * @author caiyang
	 * @param dataset
	 * @return
	 * @throws Exception 
	 * @see com.springreport.api.reporttpldataset.IReportTplDatasetService#getDatasetColumns(com.springreport.entity.reporttpldataset.ReportTplDataset)
	 * @date 2022-10-12 06:32:12 
	 */  
	@Override
	public List<Map<String, Object>> getDatasetColumns(ReportTplDataset dataset,UserInfoDto userInfoDto) throws Exception {
		dataset = this.getById(dataset.getId());
		if(dataset == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"数据集信息"}));
		}
		ReportDatasource reportDatasource = this.iReportDatasourceService.getById(dataset.getDatasourceId());
		if (reportDatasource == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"数据库信息"}));
		}
		List<Map<String, Object>> cacheColumns = null;
		if(DatasetTypeEnum.SQL.getCode().intValue() == dataset.getDatasetType().intValue())
		{//数据库
			cacheColumns =(List<Map<String, Object>>) redisUtil.get(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(dataset.getId()));
		}else {//api
			cacheColumns =(List<Map<String, Object>>) redisUtil.get(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(reportDatasource.getId()));
		}
		if(cacheColumns != null)
		{
			return cacheColumns;
		}
		List<Map<String, Object>> columns = null;
		if(DatasetTypeEnum.SQL.getCode().intValue() == dataset.getDatasetType().intValue())
		{
			if(reportDatasource.getType().intValue() == 6)
			{
				InfluxDbDataSourceConfig dataSourceConfig = new InfluxDbDataSourceConfig(dataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl().substring(0,reportDatasource.getJdbcUrl().lastIndexOf("/")), reportDatasource.getJdbcUrl().substring(reportDatasource.getJdbcUrl().lastIndexOf("/")+1));
				InfluxDBConnection dbConnection = JdbcUtils.getInfluxdbDatasource(dataSourceConfig);
				if(dbConnection == null) {
					throw new BizException(StatusCode.FAILURE, "influxdb连接失败!");
				}
				columns = JdbcUtils.parseInfluxdbColumns(dbConnection, dataset.getTplSql(), reportDatasource.getType(),dataset.getTplParam(),userInfoDto);
			}else if(reportDatasource.getType().intValue() == 10) {
				TDengineConfig config = new TDengineConfig(dataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
				TDengineConnection tDengineConnection = new TDengineConnection(config.getJdbcUrl(), config.getUsername(), config.getPassword());
				columns = JdbcUtils.parseMetaDataColumns(tDengineConnection.getConnection(), dataset.getTplSql(),reportDatasource.getType(),dataset.getTplParam(),userInfoDto);
			}else {
				DataSource dataSource = null;
				if(reportDatasource.getType().intValue() == 9)
				{
					EsDataSourceConfig esDataSourceConfig = new EsDataSourceConfig(dataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
					dataSource = JdbcUtils.getEsDataSource(esDataSourceConfig);
				}else {
					//数据源配置
					DataSourceConfig dataSourceConfig = new DataSourceConfig(dataset.getDatasourceId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
					//获取数据源
					dataSource = JdbcUtils.getDataSource(dataSourceConfig);
				}
				
				//解析sql
				if(SqlTypeEnum.SQL.getCode().intValue() == dataset.getSqlType().intValue())
				{
					if(reportDatasource.getType().intValue() == 9)
					{
						columns = JdbcUtils.parseMetaDataColumns(dataSource, dataset.getTplSql(),reportDatasource.getType(),dataset.getTplParam(),reportDatasource.getUserName(),reportDatasource.getPassword(),userInfoDto);
					}else {
						columns = JdbcUtils.parseMetaDataColumns(dataSource, dataset.getTplSql(),reportDatasource.getType(),dataset.getTplParam(),userInfoDto);
					}
				}else {
					 columns = JdbcUtils.parseProcedureColumns(dataSource, dataset.getTplSql(), reportDatasource.getType(), JSONArray.parseArray(dataset.getInParam()), JSONArray.parseArray(dataset.getOutParam()),userInfoDto);
				}
			}
			redisUtil.set(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(dataset.getId()), columns);
		}else {
			JSONArray jsonArray = JSONArray.parseArray(reportDatasource.getApiColumns());
			if(!ListUtil.isEmpty(jsonArray))
			{
				columns = new ArrayList<Map<String,Object>>();
				Map<String, Object> map = null;
				for (int j = 0; j < jsonArray.size(); j++) {
					map = new HashMap<String, Object>();
					map.put("columnName", jsonArray.getJSONObject(j).get("propName"));
					map.put("name", jsonArray.getJSONObject(j).get("propCode"));
					columns.add(map);
				}
			}
			redisUtil.set(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(reportDatasource.getId()), columns);
		}
		return columns;
	}

	/**  
	 * @MethodName: getApiDefaultRequestResult
	 * @Description: 获取api数据集默认参数请求结果
	 * @author caiyang
	 * @param dataset
	 * @see com.springreport.api.reporttpldataset.IReportTplDatasetService#getApiDefaultRequestResult(com.springreport.entity.reporttpldataset.ReportTplDataset)
	 * @date 2023-01-11 10:42:22 
	 */  
	@Override
	public ApiTestResultDto getApiDefaultRequestResult(ReportTplDataset dataset) {
		ApiTestResultDto result = new ApiTestResultDto();
		dataset = this.getById(dataset.getId());
		if(dataset == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"数据集信息"}));
		}
		ReportDatasource reportDatasource = this.iReportDatasourceService.getById(dataset.getDatasourceId());
		if (reportDatasource == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"数据库信息"}));
		}
		Map<String, Object> params = new HashMap<>();
		Map<String, String> headers = new HashMap<>();
		if(StringUtil.isNotEmpty(dataset.getTplParam()))
		{
			List<ReportParamDto> tplParams = JSONArray.parseArray(dataset.getTplParam(), ReportParamDto.class);
			if(!ListUtil.isEmpty(tplParams))
			{
				for (int i = 0; i < tplParams.size(); i++) {
					if(StringUtil.isNotEmpty(tplParams.get(i).getParamDefault()))
					{
						params.put(tplParams.get(i).getParamCode(), tplParams.get(i).getParamDefault());
					}
				}
			}
		}
		if(StringUtil.isNotEmpty(reportDatasource.getApiRequestHeader()))
		{
			JSONArray jsonArray = JSONArray.parseArray(reportDatasource.getApiRequestHeader());
			if(!ListUtil.isEmpty(jsonArray))
			{
				for (int i = 0; i < jsonArray.size(); i++) {
					String headerName = jsonArray.getJSONObject(i).getString("headerName");
					String headerValue = jsonArray.getJSONObject(i).getString("headerValue");
					if(StringUtil.isNotEmpty(headerValue))
					{
						headers.put(headerName, headerValue);
					}
				}
			}
		}
		String requestResult = HttpClientUtil.connectionTest(reportDatasource.getJdbcUrl(),reportDatasource.getApiRequestType(),params,headers);
		result.setApiResult(requestResult);
		return result;
	}

	/**
	*<p>Title: addTplDataSets</p>
	*<p>Description: 报表模板添加数据集</p>
	* @author caiyang
	* @param model
	 * @throws Exception 
	*/
	@Override
	@Transactional
	public ReportDatasetDto addTplDataSets(ReportTplDataset reportTplDataset,UserInfoDto userInfoDto) throws Exception {
		ReportDatasetDto result = new ReportDatasetDto();
//		ReportTpl tpl = this.iReportTplService.getById(reportTplDataset.getTplId());
//		if (tpl == null) {
//			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"报表模板"}));
//		}
		//获取数据源并解析sql
		ReportDatasource reportDatasource = this.iReportDatasourceService.getById(reportTplDataset.getDatasourceId());
		if (reportDatasource == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"数据库信息"}));
		}
		
		List<Map<String, Object>> columns = null;
		if(DatasetTypeEnum.API.getCode().intValue() == reportTplDataset.getDatasetType().intValue())
		{
			JSONArray jsonArray = JSONArray.parseArray(reportDatasource.getApiColumns());
			if(!ListUtil.isEmpty(jsonArray))
			{
				columns = new ArrayList<Map<String,Object>>();
				Map<String, Object> map = null;
				for (int i = 0; i < jsonArray.size(); i++) {
					map = new HashMap<String, Object>();
					map.put("columnName", jsonArray.getJSONObject(i).get("propName"));
					map.put("name", jsonArray.getJSONObject(i).get("propCode"));
					columns.add(map);
				}
			}
		}else {
			if(reportDatasource.getType().intValue() == 6)
			{//influxdb
				InfluxDbDataSourceConfig dataSourceConfig = new InfluxDbDataSourceConfig(reportTplDataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl().substring(0,reportDatasource.getJdbcUrl().lastIndexOf("/")), reportDatasource.getJdbcUrl().substring(reportDatasource.getJdbcUrl().lastIndexOf("/")+1));
				InfluxDBConnection dbConnection = JdbcUtils.getInfluxdbDatasource(dataSourceConfig);
				if(dbConnection == null) {
					throw new BizException(StatusCode.FAILURE, "influxdb连接失败!");
				}
				columns = JdbcUtils.parseInfluxdbColumns(dbConnection, reportTplDataset.getTplSql(), reportDatasource.getType(),reportTplDataset.getTplParam(),userInfoDto);
			}else if(reportDatasource.getType().intValue() == 10) {
				TDengineConfig config = new TDengineConfig(reportTplDataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
				TDengineConnection tDengineConnection = new TDengineConnection(config.getJdbcUrl(), config.getUsername(), config.getPassword());
				columns = JdbcUtils.parseMetaDataColumns(tDengineConnection.getConnection(), reportTplDataset.getTplSql(),reportDatasource.getType(),reportTplDataset.getTplParam(),userInfoDto);
			}else {
				DataSource dataSource = null;
				if(reportDatasource.getType().intValue() == 9)
				{
					EsDataSourceConfig esDataSourceConfig = new EsDataSourceConfig(reportTplDataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
					dataSource = JdbcUtils.getEsDataSource(esDataSourceConfig);
				}else {
					//数据源配置
					DataSourceConfig dataSourceConfig = new DataSourceConfig(reportTplDataset.getDatasourceId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
					//获取数据源
					dataSource = JdbcUtils.getDataSource(dataSourceConfig);
				}
				if(SqlTypeEnum.SQL.getCode().intValue() == reportTplDataset.getSqlType().intValue())
				{//标准sql
					if(reportDatasource.getType().intValue() == 9)
					{
						columns = JdbcUtils.parseMetaDataColumns(dataSource, reportTplDataset.getTplSql(),reportDatasource.getType(),reportTplDataset.getTplParam(),reportDatasource.getUserName(),reportDatasource.getPassword(),userInfoDto);
					}else {
						columns = JdbcUtils.parseMetaDataColumns(dataSource, reportTplDataset.getTplSql(),reportDatasource.getType(),reportTplDataset.getTplParam(),userInfoDto);
					}
					
				}else {
				 //存储过程
					columns = JdbcUtils.parseProcedureColumns(dataSource, reportTplDataset.getTplSql(), reportDatasource.getType(), JSONArray.parseArray(reportTplDataset.getInParam()), JSONArray.parseArray(reportTplDataset.getOutParam()),userInfoDto);
				}
			}
			
		}
		QueryWrapper<ReportTplDataset> queryWrapper = new QueryWrapper<ReportTplDataset>();
		if (reportTplDataset.getId() == null) {
			//新增，判断数据集名称是否已经存在
			queryWrapper.eq("tpl_id", reportTplDataset.getTplId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			queryWrapper.eq("dataset_name", reportTplDataset.getDatasetName());
			ReportTplDataset exist = this.getOne(queryWrapper,false);
			if (exist != null) {
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"数据集名称"}));
			}
			queryWrapper = new QueryWrapper<ReportTplDataset>();
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				queryWrapper.eq("merchant_no", reportTplDataset.getMerchantNo());
			}
			queryWrapper.eq("is_common", YesNoEnum.YES.getCode());
			queryWrapper.eq("common_type", reportTplDataset.getCommonType());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			queryWrapper.eq("dataset_name", reportTplDataset.getDatasetName());
			exist = this.getOne(queryWrapper,false);
			if (exist != null) {
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"数据集名称"}));
			}
			//新增数据集
			ReportTplDataset insertData = new ReportTplDataset();
			insertData.setMerchantNo(reportTplDataset.getMerchantNo());
			insertData.setDatasetName(reportTplDataset.getDatasetName());
			insertData.setDatasetType(reportTplDataset.getDatasetType());
			insertData.setTplId(reportTplDataset.getTplId());
			insertData.setDatasourceId(reportTplDataset.getDatasourceId());
			insertData.setTplSql(reportTplDataset.getTplSql());
			insertData.setTplParam(reportTplDataset.getTplParam());
			insertData.setIsPagination(reportTplDataset.getIsPagination());
			insertData.setPageCount(reportTplDataset.getPageCount());
			insertData.setSqlType(reportTplDataset.getSqlType());
			insertData.setInParam(reportTplDataset.getInParam());
			insertData.setOutParam(reportTplDataset.getOutParam());
			insertData.setCurrentPageAttr(reportTplDataset.getCurrentPageAttr());
			insertData.setPageCountAttr(reportTplDataset.getPageCountAttr());
			insertData.setTotalAttr(reportTplDataset.getTotalAttr());
			insertData.setGroupId(reportTplDataset.getGroupId());
			insertData.setSubParamAttrs(reportTplDataset.getSubParamAttrs());
			insertData.setIsCommon(reportTplDataset.getIsCommon());
			insertData.setIsConvert(reportTplDataset.getIsConvert());
			insertData.setHeaderName(reportTplDataset.getHeaderName());
			insertData.setValueField(reportTplDataset.getValueField());
			insertData.setFixedColumn(reportTplDataset.getFixedColumn());
			insertData.setCommonType(reportTplDataset.getCommonType());
			this.save(insertData);
			result.setId(insertData.getId());
		}else {
			//编辑，更新数据集
			this.updateById(reportTplDataset);
		}
		BeanUtils.copyProperties(reportTplDataset, result);
		result.setColumns(columns);
		if(DatasetTypeEnum.API.getCode().intValue() == reportTplDataset.getDatasetType().intValue())
		{
			redisUtil.del(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(reportDatasource.getId()));
		}else {
			redisUtil.del(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(result.getId()));
		}
		if(!ListUtil.isEmpty(columns))
		{
			if(DatasetTypeEnum.API.getCode().intValue() == reportTplDataset.getDatasetType().intValue())
			{
				redisUtil.set(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(reportDatasource.getId()), columns);
			}else {
				redisUtil.set(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(result.getId()), columns);
			}
		}
		
		return result;
	}


	/**  
	 * @Title: getTplDatasetAndDatasource
	 * @Description: 根据模板id和数据集名称获取数据集和数据源
	 * @param tplId
	 * @param datasetName
	 * @return 
	 * @see com.caiyang.api.reporttpldataset.IReportTplDatasetService#getTplDatasource(java.lang.Long, java.lang.String) 
	 * @author caiyang
	 * @throws Exception 
	 * @date 2021-05-26 06:17:47 
	 */
	@Override
	public Map<String, Object> getTplDatasetAndDatasource(Long tplId, String datasetName,String merchantNo) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		QueryWrapper<ReportTplDataset> datasetQueryWrapper = new QueryWrapper<ReportTplDataset>();
		datasetQueryWrapper.eq("tpl_id", tplId);
		datasetQueryWrapper.eq("dataset_name", datasetName);
		datasetQueryWrapper.eq("is_common", YesNoEnum.NO.getCode());
		datasetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportTplDataset reportTplDataset = this.getOne(datasetQueryWrapper,false);
		if(reportTplDataset == null)
		{
			datasetQueryWrapper = new QueryWrapper<ReportTplDataset>();
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				datasetQueryWrapper.eq("merchant_no", merchantNo);
			}
			datasetQueryWrapper.eq("dataset_name", datasetName);
			datasetQueryWrapper.eq("is_common", YesNoEnum.YES.getCode());
			datasetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			reportTplDataset = this.getOne(datasetQueryWrapper,false);
			if(reportTplDataset == null) {
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist",new String[] {"数据集"+datasetName}));
			}
		}
		this.datasourceProcess(result, reportTplDataset);
		return result;
	}
	
	@Override
	public Map<String, Object> getTplDatasetAndDatasource(Long dataSetId) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		ReportTplDataset reportTplDataset = this.getById(dataSetId);
		if(reportTplDataset == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist",new String[] {"数据集"}));
		}
		this.datasourceProcess(result, reportTplDataset);
		return result;
	}
	
	private void datasourceProcess(Map<String, Object> result,ReportTplDataset reportTplDataset) throws Exception{
		ReportDatasource reportDatasource = this.iReportDatasourceService.getById(reportTplDataset.getDatasourceId());
		if (reportDatasource == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"数据库信息"}));
		}
		if(reportDatasource.getType().intValue() == 6)
		{//influxdb
			InfluxDbDataSourceConfig dataSourceConfig = new InfluxDbDataSourceConfig(reportTplDataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl().substring(0,reportDatasource.getJdbcUrl().lastIndexOf("/")), reportDatasource.getJdbcUrl().substring(reportDatasource.getJdbcUrl().lastIndexOf("/")+1));
			InfluxDBConnection dbConnection = JdbcUtils.getInfluxdbDatasource(dataSourceConfig);
			if(dbConnection != null) {
				result.put("dataSource", dbConnection);
				result.put("tplDataSet", reportTplDataset);
				result.put("type", reportDatasource.getType());
			}
		}else if(reportDatasource.getType().intValue() == 9)
		{//elasticsearch
			EsDataSourceConfig esDataSourceConfig = new EsDataSourceConfig(reportTplDataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
			DataSource esDataSource = JdbcUtils.getEsDataSource(esDataSourceConfig);
			if(esDataSource != null)
			{
				result.put("dataSource", esDataSource);
				result.put("tplDataSet", reportTplDataset);
				result.put("useName", reportDatasource.getUserName());
				result.put("password", reportDatasource.getPassword());
				result.put("type", reportDatasource.getType());
			}
		}else if(reportDatasource.getType().intValue() == 10)
		{//tdengine
			TDengineConfig config = new TDengineConfig(reportTplDataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
			TDengineConnection conn = JdbcUtils.getTDengineConnection(config);
			if(conn != null)
			{
				result.put("dataSource", conn);
				result.put("tplDataSet", reportTplDataset);
				result.put("useName", reportDatasource.getUserName());
				result.put("password", reportDatasource.getPassword());
				result.put("type", reportDatasource.getType());
			}
		}else {
			//数据源配置
			DataSourceConfig dataSourceConfig = new DataSourceConfig(reportTplDataset.getDatasourceId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
			//获取数据源
			DataSource dataSource = JdbcUtils.getDataSource(dataSourceConfig);
			result.put("dataSource", dataSource);
			result.put("tplDataSet", reportTplDataset);
			result.put("type", reportDatasource.getType());
		}
	}


	/**  
	 * @Title: getReportDatasetsParam
	 * @Description: 获取报表数据集参数
	 * @param reportTplDataset
	 * @return 
	 * @see com.caiyang.api.reporttpldataset.IReportTplDatasetService#getReportDatasetsParam(com.caiyang.entity.reporttpldataset.ReportTplDataset) 
	 * @author caiyang
	 * @throws ParseException 
	 * @throws JSQLParserException 
	 * @date 2021-06-03 02:25:17 
	 */
	@Override
	public Map<String, Object> getReportDatasetsParam(ReportTplDatasetDto reportTplDataset) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paginationMap = new HashMap<>();//分页参数
		List<String> apiHeaders = new ArrayList<>();//api请求的headers
		boolean isPagination = false;
		List<DatasetsParamDto> datasetsParams = new ArrayList<DatasetsParamDto>();
		int isParamMerge = YesNoEnum.YES.getCode();
		if(reportTplDataset.getReportType().intValue() == 1) {
			ReportTpl reportTpl = this.iReportTplService.getById(reportTplDataset.getTplId());
			if (reportTpl == null) {
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"报表模板"}));
			}
			if(YesNoEnum.YES.getCode().intValue() == reportTplDataset.getIsMobile().intValue())
			{//手机端不管是否设置合并参数都进行参数合并
				isParamMerge = YesNoEnum.YES.getCode();
			}else {
				isParamMerge = reportTpl.getIsParamMerge();
			}
		}else if(reportTplDataset.getReportType().intValue() == 2) {
			DocTpl docTpl = this.iDocTplService.getById(reportTplDataset.getTplId());
			if (docTpl == null) {
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"报表模板"}));
			}
			if(docTpl.getParamMerge().intValue() == YesNoEnum.NO.getCode().intValue()) {
				isParamMerge = YesNoEnum.NO.getCode();
			}
		}
		//获取所有的数据集
		QueryWrapper<ReportTplDataset> datasetQueryWrapper = new QueryWrapper<ReportTplDataset>();
		datasetQueryWrapper.eq("tpl_id", reportTplDataset.getTplId());
		datasetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplDataset> datasets = this.list(datasetQueryWrapper);
		if (!ListUtil.isEmpty(datasets)) {
			for (int i = 0; i < datasets.size(); i++) {
				DatasetsParamDto datasetsParamDto = new DatasetsParamDto();
				List<ReportParamDto> params = null;
				if(DatasetTypeEnum.API.getCode().intValue() == datasets.get(i).getDatasetType().intValue()) {
					//api接口请求，获取所有的headers
					ReportDatasource reportDatasource = this.iReportDatasourceService.getById(datasets.get(i).getDatasourceId());
					if(StringUtil.isNotEmpty(reportDatasource.getApiRequestHeader())) {
						JSONArray apiRequestHeader = JSON.parseArray(reportDatasource.getApiRequestHeader());
						if(ListUtil.isNotEmpty(apiRequestHeader)) {
							for (int j = 0; j < apiRequestHeader.size(); j++) {
								String headerName = apiRequestHeader.getJSONObject(j).getString("headerName");
								if(!apiHeaders.contains(headerName)) {
									apiHeaders.add(apiRequestHeader.getJSONObject(j).getString("headerName"));
								}
							}
						}
					}
				}
				if(SqlTypeEnum.SQL.getCode().intValue() == datasets.get(i).getSqlType().intValue())
				{
					params = JSONArray.parseArray(datasets.get(i).getTplParam(), ReportParamDto.class);
				}else if(SqlTypeEnum.FUNCTION.getCode().intValue() == datasets.get(i).getSqlType().intValue()) {
					params = JSONArray.parseArray(datasets.get(i).getInParam(), ReportParamDto.class);
					for (int j = 0; j < params.size(); j++) {
//						params.get(j).setParamRequired(YesNoEnum.YES.getCode());
						if(ProcedureParamTypeEnum.STRING.getCode().equals(params.get(j).getParamType()))
						{
							params.get(j).setParamType(ParamTypeEnum.VARCHAR.getCode());
						}else if(ProcedureParamTypeEnum.DATE.getCode().equals(params.get(j).getParamType())){
							params.get(j).setParamType(ParamTypeEnum.DATE.getCode());
						}else if(ProcedureParamTypeEnum.DATETIME.getCode().equals(params.get(j).getParamType())){
							params.get(j).setParamType(ParamTypeEnum.DATE.getCode());
							params.get(j).setDateFormat("yyyy-MM-dd HH:mm:ss");
						}else {
							params.get(j).setParamType(ParamTypeEnum.NUMBER.getCode());
						}
					}
				}
				if(!ListUtil.isEmpty(params))
				{
					ReportDatasource reportDatasource = null;
					for (int j = 0; j < params.size(); j++) {
						Map<String, Object> map = new HashMap<String, Object>();
						if(YesNoEnum.YES.getCode().intValue() == params.get(j).getParamRequired())
						{
							map.put("required", true);
						}
						if (ParamTypeEnum.NUMBER.getCode().equals(params.get(j).getParamType())) {
							if(!ParamTypeEnum.SELECT.getCode().equals(params.get(j).getComponentType()) 
								&& !ParamTypeEnum.MUTISELECT.getCode().equals(params.get(j).getComponentType())
								&& !ParamTypeEnum.TREESELECT.getCode().equals(params.get(j).getComponentType())
								&& !ParamTypeEnum.MULTITREESELECT.getCode().equals(params.get(j).getComponentType())) {
								map.put("type", ParamTypeEnum.NUMBER.getCode());
							}
						}
						if (ParamTypeEnum.SELECT.getCode().equals(params.get(j).getParamType()) 
								|| ParamTypeEnum.MUTISELECT.getCode().equals(params.get(j).getParamType()) || 
								ParamTypeEnum.SELECT.getCode().equals(params.get(j).getComponentType()) 
								|| ParamTypeEnum.MUTISELECT.getCode().equals(params.get(j).getComponentType())) {
							if (SelectTypeEnum.SQL.getCode().equals(params.get(j).getSelectType()))
							{
								if(params.get(j).getIsRelyOnParams() == null || params.get(j).getIsRelyOnParams().intValue() == YesNoEnum.NO.getCode().intValue())
								{
									if(StringUtil.isNotEmpty(params.get(j).getParamDefault()) || reportTplDataset.isInitSelectData())
									{
										if(reportDatasource == null)
										{
											reportDatasource = iReportDatasourceService.getById(params.get(j).getDatasourceId()!=null?params.get(j).getDatasourceId():datasets.get(i).getDatasourceId());
										}
										//数据源配置
										DataSourceConfig dataSourceConfig = new DataSourceConfig(reportDatasource.getId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
										//获取数据源
										DataSource dataSource = JdbcUtils.getDataSource(dataSourceConfig);
										List<Map<String, Object>> selectDatas = ReportDataUtil.getSelectData(dataSource, params.get(j).getSelectContent());
										if (selectDatas != null) {
											params.get(j).setSelectData(JSONArray.toJSON(selectDatas));
										}else {
											params.get(j).setSelectData(new ArrayList<Map<String, Object>>());
										}
										if(!ListUtil.isEmpty(selectDatas))
										{
											params.get(j).setInit(true);
										}
									}else {
										if(params.get(j).getDatasourceId() == null) {
											params.get(j).setDatasourceId(datasets.get(i).getDatasourceId());
										}
									}
								}else {
									if(params.get(j).getDatasourceId() == null) {
										params.get(j).setDatasourceId(datasets.get(i).getDatasourceId());
									}
								}
							}else {
								if(StringUtil.isNotEmpty(params.get(j).getParamDefault()) || reportTplDataset.isInitSelectData()) {
									params.get(j).setSelectData(JSONArray.parseArray(params.get(j).getSelectContent()));
								}
							}
							//TODO 支持接口获取
						}
						else if(ParamTypeEnum.DATE.getCode().equals(params.get(j).getParamType()))
						{
							if(StringUtil.isNotEmpty(params.get(j).getParamDefault()))
							{
								String dateDefaultValue = params.get(j).getParamDefault();
								params.get(j).setDateDefaultValue(dateDefaultValue);
								String dateFormat = params.get(j).getDateFormat();
								if("YYYY-MM-DD".equals(dateFormat))
								{
									dateFormat = DateUtil.FORMAT_LONOGRAM;
								}else if("YYYY-MM".equals(dateFormat))
								{
									dateFormat = DateUtil.FORMAT_YEARMONTH;
								}else if("YYYY-MM-DD HH:mm".equals(dateFormat))
								{
									dateFormat = DateUtil.FORMAT_WITHOUTSECONDS;
								}else {
									if(StringUtil.isNullOrEmpty(dateFormat)) {
			    						dateFormat = DateUtil.FORMAT_LONOGRAM;
			    					}
								}
								if(Constants.CURRENT_DATE.equals(StringUtil.trim(params.get(j).getParamDefault()).toLowerCase()))
								{//当前日期
									String currentDate = DateUtil.getNow(StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
									params.get(j).setParamDefault(currentDate);
									params.get(j).setDateFormat(StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
								}else if(DefaultDateTypeEnum.WF.getCode().equals(StringUtil.trim(params.get(j).getParamDefault()).toLowerCase()))
								{//本周第一天
									String currentDate = DateUtil.getWeekStart();
									currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
									params.get(j).setParamDefault(currentDate);
									params.get(j).setDateFormat(StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
								}else if(DefaultDateTypeEnum.WL.getCode().equals(StringUtil.trim(params.get(j).getParamDefault()).toLowerCase()))
								{//本周最后一天
									String currentDate = DateUtil.getWeekEnd();
									currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
									params.get(j).setParamDefault(currentDate);
									params.get(j).setDateFormat(StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
								}else if(DefaultDateTypeEnum.MF.getCode().equals(StringUtil.trim(params.get(j).getParamDefault()).toLowerCase()))
								{//本月第一天
									String currentDate = DateUtil.getMonthStart();
									currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
									params.get(j).setParamDefault(currentDate);
									params.get(j).setDateFormat(StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
								}else if(DefaultDateTypeEnum.ML.getCode().equals(StringUtil.trim(params.get(j).getParamDefault()).toLowerCase()))
								{//本月最后一天
									String currentDate = DateUtil.getMonthEnd();
									currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
									params.get(j).setParamDefault(currentDate);
									params.get(j).setDateFormat(StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
								}else if(DefaultDateTypeEnum.SF.getCode().equals(StringUtil.trim(params.get(j).getParamDefault()).toLowerCase()))
								{//本季度第一天
									String currentDate = DateUtil.getQuarterStart(new Date());
									currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
									params.get(j).setParamDefault(currentDate);
									params.get(j).setDateFormat(StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
								}else if(DefaultDateTypeEnum.SL.getCode().equals(StringUtil.trim(params.get(j).getParamDefault()).toLowerCase()))
								{//本季度最后一天
									String currentDate = DateUtil.getQuarterEnd(new Date());
									currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
									params.get(j).setParamDefault(currentDate);
									params.get(j).setDateFormat(StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
								}else if(DefaultDateTypeEnum.YF.getCode().equals(StringUtil.trim(params.get(j).getParamDefault()).toLowerCase()))
								{//本年度第一天
									String currentDate = DateUtil.getYearStart();
									currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
									params.get(j).setParamDefault(currentDate);
									params.get(j).setDateFormat(StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
								}else if(DefaultDateTypeEnum.YL.getCode().equals(StringUtil.trim(params.get(j).getParamDefault()).toLowerCase()))
								{//本年度最后一天
									String currentDate = DateUtil.getYearEnd();
									currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
									params.get(j).setParamDefault(currentDate);
									params.get(j).setDateFormat(StringUtil.isNotEmpty(params.get(j).getDateFormat())?params.get(j).getDateFormat():DateUtil.FORMAT_LONOGRAM);
								}else {
									if(CheckUtil.isNumber(params.get(j).getParamDefault()))
									{
										int days = Double.valueOf(params.get(j).getParamDefault()).intValue();
										if(DateUtil.FORMAT_YEARMONTH.equals(dateFormat))
										{
											String date = DateUtil.addMonth(days, DateUtil.getNow(DateUtil.FORMAT_LONOGRAM),DateUtil.FORMAT_YEARMONTH);
											params.get(j).setParamDefault(date);
										}else if(DateUtil.FORMAT_YEAR.equals(dateFormat))
										{
											String date = DateUtil.addYear(days, DateUtil.getNow(DateUtil.FORMAT_LONOGRAM),DateUtil.FORMAT_YEAR);
											params.get(j).setParamDefault(date);
										}else {
											String date = DateUtil.addDays(days, DateUtil.getNow(),StringUtil.isNotEmpty(params.get(j).getDateFormat())?dateFormat:DateUtil.FORMAT_LONOGRAM);
											if(StringUtil.isNullOrEmpty(params.get(j).getDateFormat()))
											{
												params.get(j).setDateFormat(DateUtil.FORMAT_LONOGRAM);
											}
											params.get(j).setParamDefault(date);
										}
									}else {
				    					if(StringUtil.isNullOrEmpty(dateFormat)) {
				    						dateFormat = DateUtil.FORMAT_LONOGRAM;
				    					}
										if(!DateUtil.isValidDate(params.get(j).getParamDefault(),dateFormat))
										{
											params.get(j).setParamDefault("");
										}
									}
								}
							}
						}
						else if(ParamTypeEnum.MULTITREESELECT.getCode().equals(params.get(j).getParamType()) || ParamTypeEnum.TREESELECT.getCode().equals(params.get(j).getParamType()) || 
								ParamTypeEnum.MULTITREESELECT.getCode().equals(params.get(j).getComponentType()) || ParamTypeEnum.TREESELECT.getCode().equals(params.get(j).getComponentType())) {
							if(StringUtil.isNotEmpty(params.get(j).getParamDefault()) || reportTplDataset.isInitSelectData())
							{
								if(reportDatasource == null)
								{
									reportDatasource = iReportDatasourceService.getById(params.get(j).getDatasourceId() != null?params.get(j).getDatasourceId():datasets.get(i).getDatasourceId());
								}
								//数据源配置
								DataSourceConfig dataSourceConfig = new DataSourceConfig(reportDatasource.getId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
								//获取数据源
								DataSource dataSource = JdbcUtils.getDataSource(dataSourceConfig);
								String sql = params.get(j).getSelectContent();
								if(params.get(j).getIsRelyOnParams().intValue() == 1) {
									sql = JdbcUtils.processSqlParams(sql, new HashMap<>());
								}
								List<Map<String, Object>> list = ReportDataUtil.getSelectData(dataSource, sql);
								List<Map<String, Object>> resultList = new ArrayList<>();
								if(!ListUtil.isEmpty(list)) {
									Map<String, Map<String, Object>> entityMap = new HashMap<>();
									Map<String, List<Map<String, Object>>> childrenMap = new HashMap<>();
									for (Map<String, Object> entity : list){
										entity.put("value", String.valueOf(entity.get("id")));
										entity.put("children", new ArrayList<>());
										entityMap.put(String.valueOf(entity.get("id")),entity);
										childrenMap.put(String.valueOf(entity.get("id")), (List<Map<String, Object>>) entity.get("children"));
									}
									// 组装成数结构列表
									for (Map<String, Object> entity : list){
										Map<String, Object> parentEntity = entityMap.get(String.valueOf(entity.get("pid")));
										if(parentEntity == null)
										{
											// 将顶级节点加入结果集中
									         resultList.add(entity);
									         continue;
										}
										// 把自己加到父节点对象里面去
										childrenMap.get(String.valueOf(parentEntity.get("id"))).add(entity);
									}
									params.get(j).setSelectData(resultList);
									params.get(j).setInit(true);
								}
							}else {
								if(params.get(j).getDatasourceId() == null) {
									params.get(j).setDatasourceId(datasets.get(i).getDatasourceId());
								}
							}
						}
						params.get(j).setRules(map);
					}
					
				}
				if(YesNoEnum.YES.getCode().intValue() == datasets.get(i).getIsPagination().intValue())
				{
					if(!isPagination)
					{
						isPagination = true;
					}
					Object currentPage = paginationMap.get("paginationMap");
					if(currentPage == null)
					{
						paginationMap.put("currentPage", "1");
					}
					Object pageCount = paginationMap.get("pageCount");
					if(pageCount == null)
					{
						paginationMap.put("pageCount",String.valueOf(datasets.get(i).getPageCount()));
					}else {
						if(datasets.get(i).getPageCount() > Integer.parseInt(String.valueOf(pageCount)))
						{
							paginationMap.put("pageCount",String.valueOf(datasets.get(i).getPageCount()));
						}
					}
				}
				datasetsParamDto.setDatasetId(datasets.get(i).getId());
				datasetsParamDto.setDatasetName(datasets.get(i).getDatasetName());
				datasetsParamDto.setDatasourceId(datasets.get(i).getDatasourceId());
				datasetsParamDto.setParams(params);
				datasetsParams.add(datasetsParamDto);
//				else {
//					params = new ArrayList<ReportParamDto>();
//				}
				
			}
		}
		if(YesNoEnum.YES.getCode().intValue() == isParamMerge)
		{//参数是否合并
			List<DatasetsParamDto> mergedDatasetsParams = new ArrayList<DatasetsParamDto>();//合并后的参数
			DatasetsParamDto datasetsParamDto = new DatasetsParamDto();
			datasetsParamDto.setDatasetName("数据集参数");
			List<ReportParamDto> params = this.processParamMerge(datasetsParams);
			datasetsParamDto.setParams(params);
			mergedDatasetsParams.add(datasetsParamDto);
			resultMap.put("params", mergedDatasetsParams);
		}else {
			resultMap.put("params", datasetsParams);
		}
		resultMap.put("isPagination", isPagination);
		resultMap.put("isParamMerge", isParamMerge);
		resultMap.put("pagination", paginationMap);
		resultMap.put("apiHeaders", apiHeaders);
		return resultMap;
	}
	
	/**  
	 * @Title: processParamMerge
	 * @Description: 处理参数合并
	 * @param params
	 * @return
	 * @author caiyang
	 * @date 2022-03-31 07:49:48 
	 */ 
	private List<ReportParamDto> processParamMerge(List<DatasetsParamDto> datasetsParams)
	{
		List<ReportParamDto> result = new ArrayList<ReportParamDto>();
		//参数编码对应的参数
		Map<String, ReportParamDto> codeParam = new LinkedHashMap<String, ReportParamDto>();
		if(!ListUtil.isEmpty(datasetsParams))
		{
			for (int i = 0; i < datasetsParams.size(); i++) {
				if(datasetsParams.get(i).getParams() == null) {
					continue;
				}
				for (int j = 0; j < datasetsParams.get(i).getParams().size(); j++) {
					String paramCode = datasetsParams.get(i).getParams().get(j).getParamCode();
					ReportParamDto reportParamDto = codeParam.get(paramCode);
					if(reportParamDto != null)
					{
						if(StringUtil.isNullOrEmpty(reportParamDto.getParamDefault()))
						{//默认值处理
							if(StringUtil.isNotEmpty(datasetsParams.get(i).getParams().get(j).getParamDefault()))
							{
								reportParamDto.setParamDefault(datasetsParams.get(i).getParams().get(j).getParamDefault());
							}
						}
						//是否必填处理
						if(YesNoEnum.YES.getCode().intValue() != reportParamDto.getParamRequired().intValue())
						{
							if(YesNoEnum.YES.getCode().intValue() == datasetsParams.get(i).getParams().get(j).getParamRequired().intValue())
							{
								reportParamDto.setParamRequired(YesNoEnum.YES.getCode());
								datasetsParams.get(i).getParams().get(j).getRules().put("required", true);
							}
						}
					}else {
						codeParam.put(paramCode, datasetsParams.get(i).getParams().get(j));
					}
				}
			}
		}
		if(!StringUtil.isEmptyMap(codeParam))
		{
			Map<String, ReportParamDto> pageMap = new LinkedHashMap<String, ReportParamDto>();
			if(codeParam.get("currentPage") != null)
			{
				ReportParamDto currentPage = codeParam.get("currentPage");
				ReportParamDto pageCount = codeParam.get("pageCount");
				ReportParamDto totalCount = codeParam.get("totalCount");
				ReportParamDto totalPage = codeParam.get("totalPage");
				codeParam.remove("currentPage");
				codeParam.remove("pageCount");
				codeParam.remove("totalCount");
				codeParam.remove("totalPage");
				codeParam.put("currentPage", currentPage);
				codeParam.put("pageCount", pageCount);
				codeParam.put("totalCount", totalCount);
				codeParam.put("totalPage", totalPage);
			}
			for (Map.Entry<String, ReportParamDto> entry : codeParam.entrySet()) {
				result.add(entry.getValue());
			}
		}
		return result;
	}


	/**  
	 * @Title: getTplDatasetColumns
	 * @Description: 获取数据集所有的列
	 * @param reportTplDataset 
	 * @see com.caiyang.api.reporttpldataset.IReportTplDatasetService#getTplDatasetColumns(com.caiyang.entity.reporttpldataset.ReportTplDataset) 
	 * @author caiyang
	 * @throws Exception 
	 * @date 2021-07-14 05:01:58 
	 */
	@Override
	public List<Map<String, Object>> getTplDatasetColumns(ReportTplDataset reportTplDataset,UserInfoDto userInfoDto) throws Exception {
		reportTplDataset = this.getById(reportTplDataset.getId());
		if(reportTplDataset == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"数据集"}));
		}
		ReportDatasource reportDatasource = this.iReportDatasourceService.getById(reportTplDataset.getDatasourceId());
		if (reportDatasource == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"数据库信息"}));
		}
		//解析sql
		List<Map<String, Object>> columns = null;
		if(reportDatasource.getType().intValue() == 6)
		{//influxdb
			InfluxDbDataSourceConfig dataSourceConfig = new InfluxDbDataSourceConfig(reportTplDataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl().substring(0,reportDatasource.getJdbcUrl().lastIndexOf("/")), reportDatasource.getJdbcUrl().substring(reportDatasource.getJdbcUrl().lastIndexOf("/")+1));
			InfluxDBConnection dbConnection = JdbcUtils.getInfluxdbDatasource(dataSourceConfig);
			if(dbConnection == null) {
				throw new BizException(StatusCode.FAILURE, "influxdb连接失败!");
			}
			columns = JdbcUtils.parseInfluxdbColumns(dbConnection, reportTplDataset.getTplSql(), reportDatasource.getType(),reportTplDataset.getTplParam(),userInfoDto);
		}else if(reportDatasource.getType().intValue() == 10) {
			TDengineConfig config = new TDengineConfig(reportTplDataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
			TDengineConnection tDengineConnection = new TDengineConnection(config.getJdbcUrl(), config.getUsername(), config.getPassword());
			columns = JdbcUtils.parseMetaDataColumns(tDengineConnection.getConnection(), reportTplDataset.getTplSql(),reportDatasource.getType(),reportTplDataset.getTplParam(),userInfoDto);
		}else {
			DataSource dataSource = null;
			if(reportDatasource.getType().intValue() == 9)
			{
				EsDataSourceConfig esDataSourceConfig = new EsDataSourceConfig(reportTplDataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
				dataSource = JdbcUtils.getEsDataSource(esDataSourceConfig);
			}else {
				//数据源配置
				DataSourceConfig dataSourceConfig = new DataSourceConfig(reportTplDataset.getDatasourceId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
				//获取数据源
				dataSource = JdbcUtils.getDataSource(dataSourceConfig);
			}
			if(SqlTypeEnum.SQL.getCode().intValue() == reportTplDataset.getSqlType().intValue())
			{
				columns = JdbcUtils.parseMetaDataColumns(dataSource, reportTplDataset.getTplSql(),reportDatasource.getType(),reportTplDataset.getTplParam(),userInfoDto);
			}else {
				columns = JdbcUtils.parseProcedureColumns(dataSource, reportTplDataset.getTplSql(), reportDatasource.getType(), JSONArray.parseArray(reportTplDataset.getInParam()), JSONArray.parseArray(reportTplDataset.getOutParam()),userInfoDto);
			}
		}
		return columns;
	}



	/**  
	 * @MethodName: getRelyOnSelectData
	 * @Description: 获取依赖项的下拉数据
	 * @author caiyang
	 * @param mesGetRelyOnSelectData
	 * @return
	 * @throws JSQLParserException 
	 * @see com.springreport.api.reporttpldataset.IReportTplDatasetService#getRelyOnSelectData(com.springreport.dto.reporttpldataset.MesGetRelyOnSelectData)
	 * @date 2022-08-08 08:22:11 
	 */  
	@Override
	public List<Map<String, Object>> getRelyOnSelectData(MesGetRelyOnSelectData mesGetRelyOnSelectData) throws JSQLParserException {
		ReportDatasource reportDatasource  = iReportDatasourceService.getById(mesGetRelyOnSelectData.getDatasourceId());
		//数据源配置
		DataSourceConfig dataSourceConfig = new DataSourceConfig(reportDatasource.getId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
		//获取数据源
		DataSource dataSource = JdbcUtils.getDataSource(dataSourceConfig);
		String sql = JdbcUtils.processSqlParams(mesGetRelyOnSelectData.getSelectContent(), mesGetRelyOnSelectData.getParams());
		List<Map<String, Object>> selectDatas = ReportDataUtil.getSelectData(dataSource, sql);
		return selectDatas;
	}


	
	private List<Map<String, Object>> getChartDatas(ReportDatasource reportDatasource,ReportTplDataset reportTplDataset,MesScreenGetSqlDataDto mesScreenGetSqlDataDto) throws Exception{
		DataSource dataSource = null;
		InfluxDBConnection dbConnection = null;
		TDengineConnection tDengineConnection = null;
		if(reportDatasource.getType().intValue() == 6)
		{//influxdb
			InfluxDbDataSourceConfig dataSourceConfig = new InfluxDbDataSourceConfig(reportTplDataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl().substring(0,reportDatasource.getJdbcUrl().lastIndexOf("/")), reportDatasource.getJdbcUrl().substring(reportDatasource.getJdbcUrl().lastIndexOf("/")+1));
			dbConnection = JdbcUtils.getInfluxdbDatasource(dataSourceConfig);
		}else if(reportDatasource.getType().intValue() == 10) {
			TDengineConfig config = new TDengineConfig(reportTplDataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
			tDengineConnection = new TDengineConnection(config.getJdbcUrl(), config.getUsername(), config.getPassword());
		}else {
			if(reportDatasource.getType().intValue() == 9)
			{
				EsDataSourceConfig esDataSourceConfig = new EsDataSourceConfig(reportTplDataset.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
				dataSource = JdbcUtils.getEsDataSource(esDataSourceConfig);
			}else {
				//数据源配置
				DataSourceConfig dataSourceConfig = new DataSourceConfig(reportTplDataset.getDatasourceId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
				//获取数据源
				dataSource = JdbcUtils.getDataSource(dataSourceConfig);
			}
		}
		//根据sql获取数据
		List<Map<String, Object>> datas = null;
		if(SqlTypeEnum.SQL.getCode().intValue() == reportTplDataset.getSqlType().intValue())
		{
			String sql = reportTplDataset.getTplSql();
			Map<String, Object> map = JdbcUtils.processPageParams(mesScreenGetSqlDataDto.getParams(), reportTplDataset.getTplParam());
			sql = JdbcUtils.processSqlParams(sql, map);
			if(reportDatasource.getType().intValue() == 6)
			{//influxdb
				datas = ReportDataUtil.getInfluxdbData(dbConnection, sql);
			}else if(reportDatasource.getType().intValue() == 10){
				datas = ReportDataUtil.getDatasourceDataBySql(tDengineConnection.getConnection(), sql);
			}else {
				if(reportDatasource.getType().intValue() == 9)
				{
					datas = ReportDataUtil.getDatasourceDataBySql(dataSource, sql, reportDatasource.getUserName(), reportDatasource.getPassword());
				}else {
					datas = ReportDataUtil.getDatasourceDataBySql(dataSource, sql);
				}
			}
		}else {
			Map<String, Object> map = JdbcUtils.processPageParams(mesScreenGetSqlDataDto.getParams(), reportTplDataset.getInParam());
			datas = ReportDataUtil.getDatasourceDataByProcedure(dataSource, reportTplDataset.getTplSql(), map, JSONArray.parseArray(reportTplDataset.getInParam()), JSONArray.parseArray(reportTplDataset.getOutParam()));
		}
		return datas;
	}


	/**  
	 * @MethodName: getSelectData
	 * @Description: 获取下拉数据
	 * @author caiyang
	 * @param mesGetRelyOnSelectData
	 * @return
	 * @throws JSQLParserException
	 * @see com.springreport.api.reporttpldataset.IReportTplDatasetService#getSelectData(com.springreport.dto.reporttpldataset.MesGetRelyOnSelectData)
	 * @date 2024-01-22 08:52:15 
	 */
	@Override
	public List<Map<String, Object>> getSelectData(MesGetRelyOnSelectData mesGetRelyOnSelectData)
			throws JSQLParserException {
		ReportDatasource reportDatasource  = iReportDatasourceService.getById(mesGetRelyOnSelectData.getDatasourceId());
		//数据源配置
		DataSourceConfig dataSourceConfig = new DataSourceConfig(reportDatasource.getId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
		//获取数据源
		DataSource dataSource = JdbcUtils.getDataSource(dataSourceConfig);
		String sql = JdbcUtils.processSqlParams(mesGetRelyOnSelectData.getSelectContent(), mesGetRelyOnSelectData.getParams());
		List<Map<String, Object>> selectDatas = ReportDataUtil.getSelectData(dataSource, sql);
		return selectDatas;
	
	}


	/**  
	 * @MethodName: getTreeSelectData
	 * @Description: 获取下拉树数据
	 * @author caiyang
	 * @param mesGetRelyOnSelectData
	 * @return
	 * @throws JSQLParserException
	 * @see com.springreport.api.reporttpldataset.IReportTplDatasetService#getTreeSelectData(com.springreport.dto.reporttpldataset.MesGetRelyOnSelectData)
	 * @date 2024-01-23 10:12:21 
	 */
	@Override
	public List<Map<String, Object>> getTreeSelectData(MesGetRelyOnSelectData mesGetRelyOnSelectData)
			throws JSQLParserException {
		ReportDatasource reportDatasource  = iReportDatasourceService.getById(mesGetRelyOnSelectData.getDatasourceId());
		//数据源配置
		DataSourceConfig dataSourceConfig = new DataSourceConfig(reportDatasource.getId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
		//获取数据源
		DataSource dataSource = JdbcUtils.getDataSource(dataSourceConfig);
		String sql = JdbcUtils.processSqlParams(mesGetRelyOnSelectData.getSelectContent(), mesGetRelyOnSelectData.getParams());
		List<Map<String, Object>> list = ReportDataUtil.getSelectData(dataSource, sql);
		List<Map<String, Object>> resultList = new ArrayList<>();
		if(!ListUtil.isEmpty(list)) {
			Map<String, Map<String, Object>> entityMap = new HashMap<>();
			Map<String, List<Map<String, Object>>> childrenMap = new HashMap<>();
			for (Map<String, Object> entity : list){
				entity.put("value", String.valueOf(entity.get("id")));
				entity.put("children", new ArrayList<>());
				entityMap.put(String.valueOf(entity.get("id")),entity);
				childrenMap.put(String.valueOf(entity.get("id")), (List<Map<String, Object>>) entity.get("children"));
			}
			// 组装成数结构列表
			for (Map<String, Object> entity : list){
				Map<String, Object> parentEntity = entityMap.get(String.valueOf(entity.get("pid")));
				if(parentEntity == null)
				{
					// 将顶级节点加入结果集中
			         resultList.add(entity);
			         continue;
				}
				// 把自己加到父节点对象里面去
				childrenMap.get(String.valueOf(parentEntity.get("id"))).add(entity);
			}
		}
		return resultList;
	}


	/**  
	 * @MethodName: getDatasetDatasource
	 * @Description: 获取数据集对应的数据源
	 * @author caiyang
	 * @param reportTplDatasetDto
	 * @return
	 * @see com.springreport.api.reporttpldataset.IReportTplDatasetService#getDatasetDatasource(com.springreport.dto.reporttpldataset.ReportTplDatasetDto)
	 * @date 2024-05-07 12:17:28 
	 */
	@Override
	public Object getDatasetDatasource(ReportDatasource reportDatasource) throws Exception{
		if (reportDatasource == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"数据库信息"}));
		}
		if(reportDatasource.getType().intValue() == 6)
		{//influxdb
			InfluxDbDataSourceConfig dataSourceConfig = new InfluxDbDataSourceConfig(reportDatasource.getId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl().substring(0,reportDatasource.getJdbcUrl().lastIndexOf("/")), reportDatasource.getJdbcUrl().substring(reportDatasource.getJdbcUrl().lastIndexOf("/")+1));
			InfluxDBConnection dbConnection = JdbcUtils.getInfluxdbDatasource(dataSourceConfig);
			return dbConnection;
		}else if(reportDatasource.getType().intValue() == 9)
		{//elasticsearch
			EsDataSourceConfig esDataSourceConfig = new EsDataSourceConfig(reportDatasource.getId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
			DataSource esDataSource = JdbcUtils.getEsDataSource(esDataSourceConfig);
			return esDataSource;
		}else if(reportDatasource.getType().intValue() == 10)
		{//tdengine
			TDengineConfig config = new TDengineConfig(reportDatasource.getId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
			TDengineConnection conn = JdbcUtils.getTDengineConnection(config);
			return conn;
		}else {
			//数据源配置
			DataSourceConfig dataSourceConfig = new DataSourceConfig(reportDatasource.getId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
			//获取数据源
			DataSource dataSource = JdbcUtils.getDataSource(dataSourceConfig);
			return dataSource;
		}
	}

	/**
	*<p>Title: getTplGroupDatasets</p>
	*<p>Description: 获取报表模板关联的数据集(分组)</p>
	* @author caiyang
	* @return
	 * @throws SQLException 
	 * @throws Exception 
	*/
	@Override
	public List<ReportTplDatasetGroup> getTplGroupDatasets(ReportTplDataset dataset, UserInfoDto userInfoDto)
			throws SQLException, Exception {
		//版本更新新增数据集分组功能，为了适应新版本，先判断是否存在分组，不存在则先添加一个默认分组
		QueryWrapper<ReportTplDatasetGroup> groupQueryWrapper = new QueryWrapper<>();
		groupQueryWrapper.eq("tpl_id", dataset.getTplId());
		groupQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		long count = this.iReportTplDatasetGroupService.count(groupQueryWrapper);
		ReportTplDatasetGroup reportTplDatasetGroup = null;
		if(count == 0) {
			reportTplDatasetGroup = new ReportTplDatasetGroup();
			reportTplDatasetGroup.setGroupName("默认分组");
			reportTplDatasetGroup.setTplId(dataset.getTplId());
			this.iReportTplDatasetGroupService.save(reportTplDatasetGroup);
			UpdateWrapper<ReportTplDataset> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("tpl_id", dataset.getTplId());
			updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			updateWrapper.eq("is_common", YesNoEnum.NO.getCode());
			ReportTplDataset update = new ReportTplDataset();
			update.setGroupId(reportTplDatasetGroup.getId());
			this.update(update, updateWrapper);
		}
		List<ReportTplDatasetGroup> datasetGroups = this.iReportTplDatasetGroupService.list(groupQueryWrapper);
		
		List<ReportDatasetDto> datasets = new ArrayList<ReportDatasetDto>();
		QueryWrapper<ReportTplDataset> queryWrapper = new QueryWrapper<ReportTplDataset>();
		queryWrapper.eq("tpl_id", dataset.getTplId());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", dataset.getMerchantNo());
		}
		queryWrapper.eq("is_common", YesNoEnum.NO.getCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplDataset> reportTplDatasets = this.list(queryWrapper);
		if(reportTplDatasets != null && reportTplDatasets.size() > 0)
		{
			ReportDatasetDto reportDatasetDto = null;
			for (int i = 0; i < reportTplDatasets.size(); i++) {
				reportDatasetDto = new ReportDatasetDto();
				BeanUtils.copyProperties(reportTplDatasets.get(i), reportDatasetDto);
				datasets.add(reportDatasetDto);
			}
//			this.iReportDatasourceService.cacheDatasetsColumns(reportTplDatasets,userInfoDto);
		}
		Map<Long, List<ReportDatasetDto>> reportDatasetsMap = null;
		if(ListUtil.isNotEmpty(datasets)) {
			reportDatasetsMap = datasets.stream().collect(Collectors.groupingBy(ReportDatasetDto::getGroupId));
		}
		if(ListUtil.isNotEmpty(datasetGroups)) {
			for (int i = 0; i < datasetGroups.size(); i++) {
				if(reportDatasetsMap != null && reportDatasetsMap.containsKey(datasetGroups.get(i).getId())) {
					List<ReportDatasetDto> list = reportDatasetsMap.get(datasetGroups.get(i).getId());
					datasetGroups.get(i).setData(list);
				}
			}
		}
		//获取公共数据集
		queryWrapper = new QueryWrapper<ReportTplDataset>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", dataset.getMerchantNo());
		}
		queryWrapper.eq("is_common", YesNoEnum.YES.getCode());
		queryWrapper.eq("common_type", dataset.getCommonType());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplDataset> commonReportTplDatasets = this.list(queryWrapper);
		ReportTplDatasetGroup commonGroup = new ReportTplDatasetGroup();
		commonGroup.setGroupName("公共数据集分组");
		commonGroup.setId(0L);
		List<ReportDatasetDto> commonDatasets = new ArrayList<ReportDatasetDto>();
		if(ListUtil.isNotEmpty(commonReportTplDatasets)) {
			ReportDatasetDto reportDatasetDto = null;
			for (int i = 0; i < commonReportTplDatasets.size(); i++) {
				reportDatasetDto = new ReportDatasetDto();
				BeanUtils.copyProperties(commonReportTplDatasets.get(i), reportDatasetDto);
				commonDatasets.add(reportDatasetDto);
			}
			commonGroup.setData(commonDatasets);
		}
		datasetGroups.add(commonGroup);
		return datasetGroups;
	}
}