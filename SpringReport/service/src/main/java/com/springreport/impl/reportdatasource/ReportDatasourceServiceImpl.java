package com.springreport.impl.reportdatasource;

import com.springreport.entity.reportdatasource.ReportDatasource;
import com.springreport.entity.reporttpldataset.ReportTplDataset;
import com.springreport.mapper.reportdatasource.ReportDatasourceMapper;
import com.springreport.api.reportdatasource.IReportDatasourceService;
import com.springreport.api.reporttpl.IReportTplService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.springreport.util.HttpClientUtil;
import com.springreport.util.InfluxDBConnection;
import com.springreport.util.JdbcUtils;
import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.MongoClientUtil;
import com.springreport.util.RedisUtil;
import com.springreport.util.ReportDataUtil;
import com.springreport.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.mongodb.client.MongoClient;

import lombok.extern.slf4j.Slf4j;

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
import com.springreport.dto.reportdatasource.MesGetSelectDataDto;
import com.springreport.dto.reportdatasource.MesReportDatasourceDto;
import com.springreport.dto.reporttpldatasource.MesExecSqlDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.transaction.annotation.Transactional;

import com.springreport.enums.DatasetTypeEnum;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.DriverClassEnum;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.enums.SqlTypeEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: ReportDatasource服务实现
* @author 
* @date 2021-02-09 01:18:28
* @version V1.0  
 */
@Slf4j
@Service
public class ReportDatasourceServiceImpl extends ServiceImpl<ReportDatasourceMapper, ReportDatasource> implements IReportDatasourceService {
  
	@Autowired
	@Lazy
	private IReportTplService iReportTplService;
	
	@Autowired
	private RedisUtil redisUtil;
	
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
	public PageEntity tablePagingQuery(ReportDatasource model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<ReportDatasource> list = this.baseMapper.searchDataLike(model);
		if(ListUtil.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setPassword(null);
			}
		}
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
	public BaseEntity insert(ReportDatasource model) {
		BaseEntity result = new BaseEntity();
		//校验code是否已经存在
		QueryWrapper<ReportDatasource> queryWrapper = new QueryWrapper<ReportDatasource>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("code", model.getCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportDatasource isExist = this.getOne(queryWrapper,false);
		if (isExist != null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该编码"}));
		}
		//数据驱动
		if (DriverClassEnum.MYSQL.getCode().intValue() == model.getType().intValue()) {
			model.setDriverClass(DriverClassEnum.MYSQL.getName());
		}else if(DriverClassEnum.SQLSERVER.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.SQLSERVER.getName());
		}else if(DriverClassEnum.ORACLE.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.ORACLE.getName());
		}else if(DriverClassEnum.POSTGRESQL.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.POSTGRESQL.getName());
		}else if(DriverClassEnum.DAMENG.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.DAMENG.getName());
		}else if(DriverClassEnum.CLICKHOUSE.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.CLICKHOUSE.getName());
		}else if(DriverClassEnum.KINGBASE.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.KINGBASE.getName());
		}else if(DriverClassEnum.HIGODB.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.HIGODB.getName());
		}else if (DriverClassEnum.DORIS.getCode().intValue() == model.getType().intValue()) {
			model.setDriverClass(DriverClassEnum.DORIS.getName());
		}else if (DriverClassEnum.INFLUXDB.getCode().intValue() == model.getType().intValue()) {
			model.setDriverClass(DriverClassEnum.INFLUXDB.getName());
		}else if (DriverClassEnum.MONGODB.getCode().intValue() == model.getType().intValue()) {
			model.setDriverClass(DriverClassEnum.MONGODB.getName());
		}else if (DriverClassEnum.TDENGINE.getCode().intValue() == model.getType().intValue()) {
			model.setDriverClass(DriverClassEnum.TDENGINE.getName());
		}
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
	public BaseEntity update(ReportDatasource model) {
		BaseEntity result = new BaseEntity();
		//校验code是否已经存在
		QueryWrapper<ReportDatasource> queryWrapper = new QueryWrapper<ReportDatasource>();
		queryWrapper.ne("id", model.getId());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("code", model.getCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportDatasource isExist = this.getOne(queryWrapper,false);
		if (isExist != null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该编码"}));
		}
		//数据驱动
		if (DriverClassEnum.MYSQL.getCode().intValue() == model.getType().intValue()) {
			model.setDriverClass(DriverClassEnum.MYSQL.getName());
		}else if(DriverClassEnum.SQLSERVER.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.SQLSERVER.getName());
		}else if(DriverClassEnum.ORACLE.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.ORACLE.getName());
		}else if(DriverClassEnum.POSTGRESQL.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.POSTGRESQL.getName());
		}else if(DriverClassEnum.DAMENG.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.DAMENG.getName());
		}else if(DriverClassEnum.CLICKHOUSE.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.CLICKHOUSE.getName());
		}else if(DriverClassEnum.KINGBASE.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.KINGBASE.getName());
		}else if(DriverClassEnum.HIGODB.getCode().intValue() == model.getType().intValue())
		{
			model.setDriverClass(DriverClassEnum.HIGODB.getName());
		}else if (DriverClassEnum.DORIS.getCode().intValue() == model.getType().intValue()) {
			model.setDriverClass(DriverClassEnum.DORIS.getName());
		}else if (DriverClassEnum.INFLUXDB.getCode().intValue() == model.getType().intValue()) {
			model.setDriverClass(DriverClassEnum.INFLUXDB.getName());
		}else if (DriverClassEnum.MONGODB.getCode().intValue() == model.getType().intValue()) {
			model.setDriverClass(DriverClassEnum.MONGODB.getName());
		}else if (DriverClassEnum.TDENGINE.getCode().intValue() == model.getType().intValue()) {
			model.setDriverClass(DriverClassEnum.TDENGINE.getName());
		}
		this.updateById(model);
		result.setStatusMsg(MessageUtil.getValue("info.update"));
		if(4 == model.getType().intValue())
		{//接口数据源进行更新，清除一下字段缓存
			redisUtil.del(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(model.getId()));
		}
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
		ReportDatasource reportDatasource = new ReportDatasource();
		reportDatasource.setId(id);
		reportDatasource.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(reportDatasource);
		BaseEntity result = new BaseEntity();
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		redisUtil.del(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(id));
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
		List<ReportDatasource> list = new ArrayList<ReportDatasource>();
		for (int i = 0; i < ids.size(); i++) {
			ReportDatasource reportDatasource = new ReportDatasource();
			reportDatasource.setId(ids.get(i));
			reportDatasource.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(reportDatasource);
			redisUtil.del(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(ids.get(i)));
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}

	/**
	*<p>Title: getReportDatasource</p>
	*<p>Description: 获取数据源</p>
	* @author caiyang
	* @return
	*/
	@Override
	public List<ReportDatasource> getReportDatasource(MesReportDatasourceDto mesReportDatasourceDto) {
		QueryWrapper<ReportDatasource> queryWrapper = new QueryWrapper<ReportDatasource>();
		if(!ListUtil.isEmpty(mesReportDatasourceDto.getDatasourceType()))
		{
			queryWrapper.in("type", mesReportDatasourceDto.getDatasourceType());
		}
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", mesReportDatasourceDto.getMerchantNo());
		}
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportDatasource> result = this.list(queryWrapper);
		return result;
	}
	
	/**
	*<p>Title: execSql</p>
	*<p>Description: 执行解析sql语句</p>
	* @author caiyang
	* @param mesExecSqlDto
	* @return
	 * @throws Exception 
	*/
	@Override
	public List<Map<String, Object>> execSql(MesExecSqlDto mesExecSqlDto,UserInfoDto userInfoDto) throws Exception {
		//获取模板信息
//		ReportTpl reportTpl = iReportTplService.getById(mesExecSqlDto.getTplId());
//		if (reportTpl == null) {
//			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"报表模板"}));
//		}
		ReportDatasource reportDatasource = this.getById(mesExecSqlDto.getDatasourceId());
		if (reportDatasource == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"数据库信息"}));
		}
		List<Map<String, Object>> result = null;
		if(reportDatasource.getType().intValue() == 6)
		{//influxdb
			InfluxDbDataSourceConfig dataSourceConfig = new InfluxDbDataSourceConfig(mesExecSqlDto.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl().substring(0,reportDatasource.getJdbcUrl().lastIndexOf("/")), reportDatasource.getJdbcUrl().substring(reportDatasource.getJdbcUrl().lastIndexOf("/")+1));
			InfluxDBConnection dbConnection = JdbcUtils.getInfluxdbDatasource(dataSourceConfig);
			if(dbConnection == null) {
				throw new BizException(StatusCode.FAILURE, "influxdb连接失败!");
			}
			result = JdbcUtils.parseInfluxdbColumns(dbConnection, mesExecSqlDto.getTplSql(), reportDatasource.getType(),mesExecSqlDto.getSqlParams(),userInfoDto);
		}else if(reportDatasource.getType().intValue() == 10) {
			TDengineConfig config = new TDengineConfig(mesExecSqlDto.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
			TDengineConnection tDengineConnection = new TDengineConnection(config.getJdbcUrl(), config.getUsername(), config.getPassword());
			result = JdbcUtils.parseMetaDataColumns(tDengineConnection.getConnection(), mesExecSqlDto.getTplSql(),reportDatasource.getType(),mesExecSqlDto.getSqlParams(),userInfoDto);
		}else if(reportDatasource.getType().intValue() == 14) {
			if(mesExecSqlDto.getMongoSearchType().intValue() == 1) {//find查询
				result = MongoClientUtil.getFields(reportDatasource.getJdbcUrl(), mesExecSqlDto.getMongoTable());
			}else if(mesExecSqlDto.getMongoSearchType().intValue() == 2){//aggregate查询
				String sqlText = JdbcUtils.parseSql(mesExecSqlDto.getTplSql(), mesExecSqlDto.getSqlParams(), userInfoDto);
				result = MongoClientUtil.getaggregateFields(reportDatasource.getJdbcUrl(), mesExecSqlDto.getMongoTable(),sqlText);
			}
		}else {
			//获取数据源
			DataSource dataSource = null;
			if(reportDatasource.getType().intValue() == 9)
			{
				EsDataSourceConfig esDataSourceConfig = new EsDataSourceConfig(mesExecSqlDto.getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
				dataSource = JdbcUtils.getEsDataSource(esDataSourceConfig);
			}else {
				DataSourceConfig dataSourceConfig = new DataSourceConfig(mesExecSqlDto.getDatasourceId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
				dataSource = JdbcUtils.getDataSource(dataSourceConfig);
			}
			//解析sql
			if(SqlTypeEnum.SQL.getCode().intValue() == mesExecSqlDto.getSqlType().intValue())
			{//标准sql
				if(reportDatasource.getType().intValue() == 9)
				{
					result = JdbcUtils.parseMetaDataColumns(dataSource, mesExecSqlDto.getTplSql(),reportDatasource.getType(),mesExecSqlDto.getSqlParams(),reportDatasource.getUserName(),reportDatasource.getPassword(),userInfoDto);
				}else {
					result = JdbcUtils.parseMetaDataColumns(dataSource, mesExecSqlDto.getTplSql(),reportDatasource.getType(),mesExecSqlDto.getSqlParams(),userInfoDto);
				}
			}else {
			 //存储过程
				result = JdbcUtils.parseProcedureColumns(dataSource, mesExecSqlDto.getTplSql(), reportDatasource.getType(), JSONArray.parseArray(mesExecSqlDto.getInParam()), JSONArray.parseArray(mesExecSqlDto.getOutParam()),userInfoDto);
			}
		}
		
		return result;
	}


	/**  
	 * @Title: connectionTest
	 * @Description: 数据源连接测试
	 * @param reportDatasource
	 * @return 
	 * @see com.caiyang.api.reportdatasource.IReportDatasourceService#connectionTest(com.caiyang.entity.reportdatasource.ReportDatasource) 
	 * @author caiyang
	 * @date 2022-04-24 01:22:28 
	 */
	@Override
	public BaseEntity connectionTest(ReportDatasource reportDatasource) {
		ApiTestResultDto result = new ApiTestResultDto();
		if(reportDatasource.getType().intValue() == 4)
		{//接口
			Map<String, String> headers = new HashMap<>();
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
			JSONObject params = JSON.parseObject(reportDatasource.getApiParams());
			String requestResult = HttpClientUtil.connectionTest(reportDatasource.getJdbcUrl(), reportDatasource.getApiRequestType(), params, headers);
			if(requestResult.startsWith("{\"errCode\":\"50001\"")) {
				JSONObject jsonObject = JSON.parseObject(requestResult);
				result.setStatusCode(StatusCode.FAILURE);
				result.setStatusMsg("接口连接测试失败，错误信息："+jsonObject.getString("errMsg"));
				result.setApiResult(requestResult);
			}else {
				result.setStatusMsg("接口连接测试成功！");
				result.setApiResult(requestResult);
			}
			
		}else if(reportDatasource.getType().intValue() == 6) {
			boolean isSuccess = JdbcUtils.influxdbTest(reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl().substring(0,reportDatasource.getJdbcUrl().lastIndexOf("/")), null, null);
			if(isSuccess)
			{
				result.setStatusMsg("influxdb连接测试成功！");
			}else {
				throw new BizException(StatusCode.FAILURE, "influxdb连接测试失败");
			}
		}else if(reportDatasource.getType().intValue() == 9) {
			boolean isSuccess = JdbcUtils.esTest(reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword());
			if(isSuccess)
			{
				result.setStatusMsg("elasticsearch连接测试成功！");
			}else {
				throw new BizException(StatusCode.FAILURE, "elasticsearch连接测试失败");
			}
		}else if(reportDatasource.getType().intValue() == 14) {
			boolean isSuccess = JdbcUtils.mongoTest(reportDatasource.getJdbcUrl());
			if(isSuccess)
			{
				result.setStatusMsg("mongodb连接测试成功！");
			}else {
				throw new BizException(StatusCode.FAILURE, "mongodb连接测试失败");
			}
		}else {
			if(StringUtil.isNullOrEmpty(reportDatasource.getUserName()))
			{
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notnull", new String[] {"登录名"}));
			}
			if(StringUtil.isNullOrEmpty(reportDatasource.getPassword()))
			{
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notnull", new String[] {"密码"}));
			}
			//数据驱动
			if (DriverClassEnum.MYSQL.getCode().intValue() == reportDatasource.getType().intValue()) {
				reportDatasource.setDriverClass(DriverClassEnum.MYSQL.getName());
			}else if(DriverClassEnum.SQLSERVER.getCode().intValue() == reportDatasource.getType().intValue())
			{
				reportDatasource.setDriverClass(DriverClassEnum.SQLSERVER.getName());
			}else if(DriverClassEnum.ORACLE.getCode().intValue() == reportDatasource.getType().intValue())
			{
				reportDatasource.setDriverClass(DriverClassEnum.ORACLE.getName());
			}else if(DriverClassEnum.POSTGRESQL.getCode().intValue() == reportDatasource.getType().intValue())
			{
				reportDatasource.setDriverClass(DriverClassEnum.POSTGRESQL.getName());
			}else if(DriverClassEnum.DAMENG.getCode().intValue() == reportDatasource.getType().intValue())
			{
				reportDatasource.setDriverClass(DriverClassEnum.DAMENG.getName());
			}else if(DriverClassEnum.CLICKHOUSE.getCode().intValue() == reportDatasource.getType().intValue())
			{
				reportDatasource.setDriverClass(DriverClassEnum.CLICKHOUSE.getName());
			}else if(DriverClassEnum.TDENGINE.getCode().intValue() == reportDatasource.getType().intValue())
			{
				reportDatasource.setDriverClass(DriverClassEnum.TDENGINE.getName());
			}else if(DriverClassEnum.KINGBASE.getCode().intValue() == reportDatasource.getType().intValue())
			{
				reportDatasource.setDriverClass(DriverClassEnum.KINGBASE.getName());
			}else if(DriverClassEnum.HIGODB.getCode().intValue() == reportDatasource.getType().intValue())
			{
				reportDatasource.setDriverClass(DriverClassEnum.HIGODB.getName());
			}else if(DriverClassEnum.DORIS.getCode().intValue() == reportDatasource.getType().intValue())
			{
				reportDatasource.setDriverClass(DriverClassEnum.DORIS.getName());
			}else if(DriverClassEnum.MONGODB.getCode().intValue() == reportDatasource.getType().intValue())
			{
				reportDatasource.setDriverClass(DriverClassEnum.MONGODB.getName());
			}
			//数据库
			//数据源配置
			DataSourceConfig dataSourceConfig = new DataSourceConfig(null, reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
			//获取数据源
			JdbcUtils.connectionTest(dataSourceConfig);
			result.setStatusMsg("数据库连接测试成功！");
		}
		return result;
	}
	
	
	/**  
	 * @Title: getDatasourceSelectData
	 * @Description: 获取下拉框数据
	 * @param mesGetSelectDataDto
	 * @return
	 * @author caiyang
	 * @date 2022年5月22日13:38:57
	 */ 
	@Override
	public List<Map<String, Object>> getDatasourceSelectData(MesGetSelectDataDto mesGetSelectDataDto) {
		ReportDatasource reportDatasource = this.getById(mesGetSelectDataDto.getDataSourceId());
		//数据源配置
		DataSourceConfig dataSourceConfig = new DataSourceConfig(reportDatasource.getId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
		//获取数据源
		DataSource dataSource = JdbcUtils.getDataSource(dataSourceConfig);
		List<Map<String, Object>> selectDatas = ReportDataUtil.getSelectData(dataSource, mesGetSelectDataDto.getSelectContent());
		if (selectDatas == null) {
			selectDatas = new ArrayList<Map<String, Object>>();
		}
		return selectDatas;
	}

	/**  
	 * @MethodName: cacheDatasetsColumns
	 * @Description: 缓存数据集列
	 * @author caiyang
	 * @param reportTplDatasets
	 * @throws Exception 
	 * @see com.springreport.api.reportdatasource.IReportDatasourceService#cacheDatasetsColumns(java.util.List)
	 * @date 2022-10-13 05:00:41 
	 */  
	@Override
	@Async
	public void cacheDatasetsColumns(List<ReportTplDataset> reportTplDatasets,UserInfoDto userInfoDto) throws Exception {
		if(reportTplDatasets != null && reportTplDatasets.size() > 0)
		{
			List<Map<String, Object>> columns = null;
			for (int i = 0; i < reportTplDatasets.size(); i++) {
				//获取数据源并解析sql
				ReportDatasource reportDatasource = this.getById(reportTplDatasets.get(i).getDatasourceId());
				if (reportDatasource != null) {
					columns = (List<Map<String, Object>>) redisUtil.get(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(reportTplDatasets.get(i).getId()));
					if(ListUtil.isEmpty(columns))
					{
						if(DatasetTypeEnum.SQL.getCode().intValue() == reportTplDatasets.get(i).getDatasetType().intValue())
						{
							if(reportDatasource.getType().intValue() == 6)
							{
								InfluxDbDataSourceConfig dataSourceConfig = new InfluxDbDataSourceConfig(reportTplDatasets.get(i).getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl().substring(0,reportDatasource.getJdbcUrl().lastIndexOf("/")), reportDatasource.getJdbcUrl().substring(reportDatasource.getJdbcUrl().lastIndexOf("/")+1));
								InfluxDBConnection dbConnection = JdbcUtils.getInfluxdbDatasource(dataSourceConfig);
								if(dbConnection == null) {
									throw new BizException(StatusCode.FAILURE, "influxdb连接失败!");
								}
								columns = JdbcUtils.parseInfluxdbColumns(dbConnection, reportTplDatasets.get(i).getTplSql(), reportDatasource.getType(),reportTplDatasets.get(i).getTplParam(),userInfoDto);
							}else if(reportDatasource.getType().intValue() == 10){
								TDengineConfig config = new TDengineConfig(reportTplDatasets.get(i).getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
								TDengineConnection tDengineConnection = new TDengineConnection(config.getJdbcUrl(), config.getUsername(), config.getPassword());
								columns = JdbcUtils.parseMetaDataColumns(tDengineConnection.getConnection(), reportTplDatasets.get(i).getTplSql(),reportDatasource.getType(),reportTplDatasets.get(i).getTplParam(),userInfoDto);
							}else {
								DataSource dataSource = null;
								if(reportDatasource.getType().intValue() == 9)
								{
									EsDataSourceConfig esDataSourceConfig = new EsDataSourceConfig(reportTplDatasets.get(i).getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
									dataSource = JdbcUtils.getEsDataSource(esDataSourceConfig);
								}else {
									//数据源配置
									DataSourceConfig dataSourceConfig = new DataSourceConfig(reportTplDatasets.get(i).getDatasourceId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
									//获取数据源
									dataSource = JdbcUtils.getDataSource(dataSourceConfig);
								}
								//解析sql
								if(SqlTypeEnum.SQL.getCode().intValue() == reportTplDatasets.get(i).getSqlType().intValue())
								{
									if(reportDatasource.getType().intValue() == 9)
									{
										columns = JdbcUtils.parseMetaDataColumns(dataSource, reportTplDatasets.get(i).getTplSql(),reportDatasource.getType(),reportTplDatasets.get(i).getTplParam(),reportDatasource.getUserName(),reportDatasource.getPassword(),userInfoDto);
									}else {
										columns = JdbcUtils.parseMetaDataColumns(dataSource, reportTplDatasets.get(i).getTplSql(),reportDatasource.getType(),reportTplDatasets.get(i).getTplParam(),userInfoDto);
									}
								}else {
									columns = JdbcUtils.parseProcedureColumns(dataSource, reportTplDatasets.get(i).getTplSql(), reportDatasource.getType(), JSONArray.parseArray(reportTplDatasets.get(i).getInParam()), JSONArray.parseArray(reportTplDatasets.get(i).getOutParam()),userInfoDto);
								}
							}
							redisUtil.set(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(reportTplDatasets.get(i).getId()), columns);
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
						}
					}
				}
			}
		}
		
	}


	/**  
	 * @MethodName: getDatabseTables
	 * @Description: 获取数据库的所有表信息
	 * @author caiyang
	 * @param datasource
	 * @see com.springreport.api.reportdatasource.IReportDatasourceService#getDatabseTables(com.springreport.entity.reportdatasource.ReportDatasource)
	 * @date 2022-11-15 07:59:38 
	 */  
	@Override
	public List<Map<String, String>> getDatabseTables(ReportDatasource datasource) {
		List<Map<String, String>> result = null;
		ReportDatasource reportDatasource = this.getById(datasource.getId());
		if (reportDatasource == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"数据库信息"}));
		}
		//数据源配置
		DataSourceConfig dataSourceConfig = new DataSourceConfig(datasource.getId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
		//获取数据源
//		DataSource dataSource = JdbcUtils.getDataSource(dataSourceConfig);
		result = JdbcUtils.parseDatabaseTables(dataSourceConfig);
		return result;
	}


	/**  
	 * @MethodName: parseApiResultAttr
	 * @Description: 解析api数据集结果属性
	 * @author caiyang
	 * @param reportDatasource
	 * @return
	 * @see com.springreport.api.reportdatasource.IReportDatasourceService#parseApiResultAttr(com.springreport.entity.reportdatasource.ReportDatasource)
	 * @date 2024-12-24 03:02:19 
	 */
	@Override
	public JSONArray parseApiResultAttr(ReportDatasource reportDatasource) {
		JSONArray result = new JSONArray();
		Map<String, String> headers = new HashMap<>();
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
		JSONObject params = JSON.parseObject(reportDatasource.getApiParams());
		String requestResult = HttpClientUtil.connectionTest(reportDatasource.getJdbcUrl(), reportDatasource.getApiRequestType(), params, headers);
		if(StringUtil.isNotEmpty(requestResult)) {
			if("ObjectArray".equals(reportDatasource.getApiResultType())) {
				//对象数组
				try {
					JSONArray resultArray = JSON.parseArray(requestResult);
					if(ListUtil.isNotEmpty(resultArray)) {
						JSONObject jsonObject = resultArray.getJSONObject(0);
						JSONObject attr = null;
						for (String key : jsonObject.keySet()) {
							attr = new JSONObject();
							attr.put("propCode", key);
							attr.put("propName", key);
							result.add(attr);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.jsonarrayparse"));
				}
			}else if("Object".equals(reportDatasource.getApiResultType())) {
				//对象
				try {
					JSONObject resultObject = JSON.parseObject(requestResult);
					if(StringUtil.isNullOrEmpty(reportDatasource.getApiColumnsPrefix())) {
						JSONObject attr = null;
						for (String key : resultObject.keySet()) {
							attr = new JSONObject();
							attr.put("propCode", key);
							attr.put("propName", key);
							result.add(attr);
						}
					}else {
						String[] prefix = reportDatasource.getApiColumnsPrefix().split("\\.");
						Object data = null;
						for (int i = 0; i < prefix.length; i++) {
							data = resultObject.get(prefix[i]);
							if(data instanceof JSONObject) {
								resultObject = (JSONObject) data;
							}else if(data instanceof JSONArray) {
								resultObject = ((JSONArray) data).getJSONObject(0);
							}
						}
						JSONObject dataObj = null;
						if(data instanceof JSONObject) {
							dataObj = (JSONObject) data;
						}else if(data instanceof JSONArray) {
							JSONArray dataArray = (JSONArray) data;
							if(ListUtil.isNotEmpty(dataArray)) {
								dataObj = dataArray.getJSONObject(0);
							}
						}
						if(dataObj != null) {
							JSONObject attr = null;
							for (String key : dataObj.keySet()) {
								attr = new JSONObject();
								attr.put("propCode", key);
								attr.put("propName", key);
								result.add(attr);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.jsonparse"));
				}
			}
		}
		return result;
	}

}