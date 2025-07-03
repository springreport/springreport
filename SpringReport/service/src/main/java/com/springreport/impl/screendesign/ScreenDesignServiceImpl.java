package com.springreport.impl.screendesign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.api.reportdatasource.IReportDatasourceService;
import com.springreport.api.reporttpldataset.IReportTplDatasetService;
import com.springreport.api.screendesign.IScreenDesignService;
import com.springreport.base.TDengineConnection;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.screendesign.MesGetDynamicData;
import com.springreport.entity.reportdatasource.ReportDatasource;
import com.springreport.entity.reporttpldataset.ReportTplDataset;
import com.springreport.enums.DatasetTypeEnum;
import com.springreport.enums.SqlTypeEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.util.HttpClientUtil;
import com.springreport.util.InfluxDBConnection;
import com.springreport.util.JdbcUtils;
import com.springreport.util.ListUtil;
import com.springreport.util.Md5Util;
import com.springreport.util.MongoClientUtil;
import com.springreport.util.ParamUtil;
import com.springreport.util.RedisUtil;
import com.springreport.util.ReportDataUtil;
import com.springreport.util.StringUtil;

/**  
 * @ClassName: ScreenDesignServiceImpl
 * @Description: 大屏设计用接口实现
 * @author caiyang
 * @date 2024-07-10 07:05:31 
*/ 
@Service
public class ScreenDesignServiceImpl implements IScreenDesignService{
	
	@Autowired
	private IReportTplDatasetService iReportTplDatasetService;
	
	@Autowired
	private IReportDatasourceService iReportDatasourceService;
	
	@Autowired
	private RedisUtil redisUtil;

	/**  
	 * @MethodName: getDynamicDatas
	 * @Description: 获取动态数据集数据
	 * @author caiyang
	 * @param mesGetDynamicData
	 * @return
	 * @see com.springreport.api.screendesign.IScreenDesignService#getDynamicDatas(com.springreport.dto.screendesign.MesGetDynamicData)
	 * @date 2024-07-10 07:31:08 
	 */
	@Override
	public List<Map<String, Object>> getDynamicDatas(MesGetDynamicData mesGetDynamicData,UserInfoDto userInfoDto) throws Exception{
		List<Map<String, Object>> result = new ArrayList<>();
		if(mesGetDynamicData.getDataSetId() != null && ListUtil.isNotEmpty(mesGetDynamicData.getDataColumns())) {
			result = this.getDatasetDatas(mesGetDynamicData,userInfoDto);
		}
		return result;
	}
	
	private List<Map<String, Object>> getDatasetDatas(MesGetDynamicData mesGetDynamicData,UserInfoDto userInfoDto) throws Exception{
		List<Map<String, Object>> datas = null;
//		if(YesNoEnum.YES.getCode().intValue() == mesGetDynamicData.getInitPage().intValue()) {
//			String paramString = JSON.toJSONString(mesGetDynamicData.getParams());
//			String key = mesGetDynamicData.getRequestKey() + "_" + mesGetDynamicData.getDataSetId()+"_"+Md5Util.generateMd5(paramString);
//			Object redisData = redisUtil.get(key);
//			if(redisData == null) {
//				datas = this.getDatas(mesGetDynamicData,userInfoDto);
//				redisUtil.set(key, JSON.toJSONString(redisData), 600);
//			}else {
//				datas = (List<Map<String, Object>>) JSON.parse(redisData.toString());
//			}
//		}else {
//			datas = this.getDatas(mesGetDynamicData,userInfoDto);
//		}
		datas = this.getDatas(mesGetDynamicData,userInfoDto);
		return datas;
	}
	
	private List<Map<String, Object>> getDatas(MesGetDynamicData mesGetDynamicData,UserInfoDto userInfoDto) throws Exception{
		List<Map<String, Object>> datas = null;
		//获取数据源
		Map<String, Object> dataSetAndDatasource = this.iReportTplDatasetService.getTplDatasetAndDatasource(mesGetDynamicData.getDataSetId());
		int type = (int) dataSetAndDatasource.get("type");
		DataSource dataSource = null;
		InfluxDBConnection dbConnection = null;
		TDengineConnection tDengineConnection = null;
		Object data = dataSetAndDatasource.get("dataSource");
		String userName = dataSetAndDatasource.get("useName") != null?String.valueOf(dataSetAndDatasource.get("useName")):"";
		String password = dataSetAndDatasource.get("password") != null?String.valueOf(dataSetAndDatasource.get("password")):"";
		if(data instanceof DataSource)
		{
			dataSource = (DataSource) data;
		}else if(data instanceof InfluxDBConnection)
		{
			dbConnection = (InfluxDBConnection) data;
		}else if(data instanceof TDengineConnection)
		{
			tDengineConnection = (TDengineConnection) data;
		}
		Map<String, Object> params = mesGetDynamicData.getParams();
		Map<String, Object> systemParams =ParamUtil.getSystemParam(userInfoDto);
		params.putAll(systemParams);
		ReportTplDataset reportTplDataset = (ReportTplDataset) dataSetAndDatasource.get("tplDataSet");
		if(DatasetTypeEnum.SQL.getCode().intValue() == reportTplDataset.getDatasetType().intValue() || DatasetTypeEnum.MONGO.getCode().intValue() == reportTplDataset.getDatasetType().intValue()) {
			String sql = reportTplDataset.getTplSql();
			if(SqlTypeEnum.SQL.getCode().intValue() == reportTplDataset.getSqlType().intValue())
			{
				if(type == 6)
				{//influxdb
					sql = JdbcUtils.processSqlParams(sql, params);
					datas = ReportDataUtil.getInfluxdbData(dbConnection, sql);
				}else if(type == 10)
				{//tdengine
					sql = JdbcUtils.processSqlParams(sql, params);
					datas = ReportDataUtil.getDatasourceDataBySql(tDengineConnection.getConnection(), sql);
				}else if(type == 14){//mongodb
					sql = JdbcUtils.processSqlParams(sql, params);
					if(reportTplDataset.getMongoSearchType().intValue() == 1) {
						datas = MongoClientUtil.findGetData(String.valueOf(dataSetAndDatasource.get("jdbcUrl")), reportTplDataset.getMongoTable(), sql, reportTplDataset.getMongoOrder());
					}else if(reportTplDataset.getMongoSearchType().intValue() == 2) {
						datas = MongoClientUtil.aggregateGetData(String.valueOf(dataSetAndDatasource.get("jdbcUrl")), reportTplDataset.getMongoTable(), sql);
					}
				}else {
					sql = JdbcUtils.processSqlParams(sql,params);
					//根据sql获取数据
					if(type == 9)
					{
						datas = ReportDataUtil.getDatasourceDataBySql(dataSource, sql,userName,password);
					}else {
						datas = ReportDataUtil.getDatasourceDataBySql(dataSource, sql);
					}
				}
			}else {
//				datas = ReportDataUtil.getDatasourceDataByProcedure(dataSource, sql, params, JSONArray.parseArray(reportTplDataset.getInParam()), JSONArray.parseArray(reportTplDataset.getOutParam()));
			}
		}else {//api
			ReportDatasource reportDatasource = this.iReportDatasourceService.getById(reportTplDataset.getDatasourceId());
			Map<String, String> headers = null;
			if(StringUtil.isNotEmpty(reportDatasource.getApiRequestHeader()))
			{
				JSONArray headersArray = JSONArray.parseArray(reportDatasource.getApiRequestHeader());
				if(!ListUtil.isEmpty(headersArray))
				{
					headers = new HashMap<String, String>();
					for (int j = 0; j < headersArray.size(); j++) {
						String headerName = headersArray.getJSONObject(j).getString("headerName");
						if(params != null && params.containsKey(headerName)) {
							headers.put(headerName, String.valueOf(params.get(headerName)));	
						}else {
							headers.put(headerName, String.valueOf(headersArray.getJSONObject(j).getString("headerValue")));	
						}	
					}
				}
			}
			String result = null;
			if("post".equals(reportDatasource.getApiRequestType()))
			{
				result = HttpClientUtil.doPostJson(reportDatasource.getJdbcUrl(), JSONObject.toJSONString(params), headers);
			}else {
				result = HttpClientUtil.doGet(reportDatasource.getJdbcUrl(),headers,params);
			}
			Map<String, Object> apiResult = ReportDataUtil.getApiResult(result, reportDatasource.getApiResultType(), reportDatasource.getApiColumnsPrefix(),null);
			datas = (List<Map<String, Object>>) apiResult.get("datas");
		}
		return datas;
	}

}
