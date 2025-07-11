package com.springreport.util;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.influxdb.dto.QueryResult;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.springreport.base.ReportDataColumnDto;
import com.springreport.base.ReportDataDetailDto;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.StatusCode;
import com.springreport.enums.InParamTypeEnum;
import com.springreport.enums.OutParamTypeEnum;
import com.springreport.enums.ResultTypeEnum;
import com.springreport.exception.BizException;

import dm.jdbc.driver.DmdbNClob;
import lombok.extern.slf4j.Slf4j;

/**  
 * @ClassName: ReportDataUtil
 * @Description: 报表数据工具类
 * @author caiyang
 * @date 2021-11-18 10:38:58 
*/  
@Slf4j
public class ReportDataUtil {

	/**  
	 * @Title: getApiResult
	 * @Description: 处理api返回结果
	 * @param apiResult
	 * @param apiResultType
	 * @param apiPrefix
	 * @return
	 * @author caiyang
	 * @date 2021-11-18 02:05:43 
	 */ 
	public static Map<String, Object> getApiResult(String apiResult,String apiResultType,String apiPrefix,String totalAttr)
	{
		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> list = null;
		Object resultObj = null;
		Long total = 0l;
		if(ResultTypeEnum.OBJECT.getCode().equals(apiResultType))
		{//返回类型是对象
			JSONObject jsonObject = JSONObject.parseObject(apiResult);
			Object apiResultObject = jsonObject;
			if(StringUtil.isNotEmpty(apiPrefix) && jsonObject != null)
			{//前缀是否为空
				String[] prefixes = apiPrefix.split("[.]");
				for (int j = 0; j < prefixes.length; j++) {
					if(apiResultObject instanceof JSONObject) {
						Object object = ((JSONObject)apiResultObject).get(prefixes[j]);
						if(object instanceof JSONObject)
						{
							if(j == prefixes.length - 1)
							{
								resultObj = object;
							}else {
								jsonObject = JSONObject.parseObject(JSONObject.toJSONString(object));
								if(StringUtil.isNotEmpty(totalAttr)) {
									total = jsonObject.getLongValue(totalAttr);
								}
							}
							
						}else if(object instanceof JSONArray)
						{
							if(j == prefixes.length - 1)
							{
								resultObj = object;
								if(StringUtil.isNotEmpty(totalAttr)) {
									total = jsonObject.getLongValue(totalAttr);
								}
							}else {
								JSONArray datas = (JSONArray) object;
								apiResultObject = datas;
							}
						}else {
							throw new BizException(StatusCode.FAILURE,"不支持的返回值格式。");
						}
					}else if(apiResultObject instanceof JSONArray) {
						JSONArray apiResultObjectArray = (JSONArray) apiResultObject;
						JSONArray datas = new JSONArray();
						if(ListUtil.isNotEmpty(apiResultObjectArray)) {
							for (int i = 0; i < apiResultObjectArray.size(); i++) {
								JSONObject data = apiResultObjectArray.getJSONObject(i);
								Object subData = data.get(prefixes[j]);
								if(subData instanceof JSONObject) {
									datas.add(subData);
								}else {
									datas.addAll((JSONArray)subData);
								}
							}
						}
						apiResultObject = datas;
						if(j == prefixes.length - 1) {
							resultObj = apiResultObject;
						}
					}else {
						throw new BizException(StatusCode.FAILURE,"不支持的返回值格式。");
					}
					
				}
			}else {
				resultObj = JSONObject.parseObject(apiResult);
				if(StringUtil.isNotEmpty(totalAttr)) {
					total = jsonObject.getLongValue(totalAttr);
				}
			}
		}else {//返回类型是对象数组
			JSONArray jsonArray = JSONArray.parseArray(apiResult);
			if(jsonArray.get(0) instanceof JSONObject)
			{
				resultObj = jsonArray;
			}else {
				throw new BizException(StatusCode.FAILURE, "不支持的返回值格式。");
			}
		}
		if(resultObj instanceof JSONObject)
		{
			list = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = JSON.parseObject(JSONObject.toJSONString(resultObj,SerializerFeature.WriteMapNullValue), LinkedHashMap.class);
			list.add(map);
		}else if(resultObj instanceof JSONArray){
			JSONArray jsonArray = (JSONArray) resultObj;
			Map<String, Object> map = null;
			list = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < jsonArray.size(); i++) {
				map = JSONObject.parseObject(JSONObject.toJSONString(jsonArray.get(i),SerializerFeature.WriteMapNullValue), LinkedHashMap.class);
				list.add(map);
			}
		}
		result.put("datas", list);
		result.put("total", total);
		return result;
	}
	
	/**  
	 * @Title: getDatasourceDataBySql
	 * @Description: 根据sql获取数据
	 * @param dataSource
	 * @param sqlText
	 * @return
	 * @author caiyang
	 * @date 2021-11-18 02:05:56 
	 */ 
	public static List<Map<String, Object>> getDatasourceDataBySql(DataSource dataSource, String sqlText) {
		Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    List<Map<String, Object>> result = null;
	    try {
	    	log.error("执行sql："+sqlText);
	    	conn = dataSource.getConnection();
	    	stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlText);
            final ResultSetMetaData rsMataData = rs.getMetaData();
            final int count = rsMataData.getColumnCount();
            result = new ArrayList<>();
            while(rs.next()){
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                for(int i = 0; i < count; i++){
                	Object value = rs.getObject(rsMataData.getColumnLabel(i+1));
                	if(value instanceof DmdbNClob) {
                		DmdbNClob dmdbNClob = (dm.jdbc.driver.DmdbNClob) value;
                		map.put(rsMataData.getColumnLabel(i+1), dmdbNClob.data);
                	}else {
                		map.put(rsMataData.getColumnLabel(i+1), value);
                	}
                }
                result.add(map);
            }
        } catch (final SQLException ex) {
            throw new BizException(StatusCode.FAILURE,"sql语句执行错误，请检查sql语句是否拼写正确或者数据源是否选择正确。错误信息："+ex.getMessage());
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, rs);
        }
		return result;
	}
	
	public static List<Map<String, Object>> getDatasourceDataBySql(Connection conn, String sqlText) {
	    Statement stmt = null;
	    ResultSet rs = null;
	    List<Map<String, Object>> result = null;
	    try {
	    	stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlText);
            log.info("执行sql："+sqlText);
            final ResultSetMetaData rsMataData = rs.getMetaData();
            final int count = rsMataData.getColumnCount();
            result = new ArrayList<>();
            while(rs.next()){
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                for(int i = 0; i < count; i++){
                 map.put(rsMataData.getColumnLabel(i+1), rs.getObject(rsMataData.getColumnLabel(i+1)));
                }
                result.add(map);
            }
        } catch (final SQLException ex) {
            throw new BizException(StatusCode.FAILURE,"sql语句执行错误，请检查sql语句是否拼写正确或者数据源是否选择正确。错误信息："+ex.getMessage());
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, rs);
        }
		return result;
	}
	
	public static List<Map<String, Object>> getDatasourceDataBySql(DataSource dataSource, String sqlText,String username,String password) {
		Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    List<Map<String, Object>> result = null;
	    try {
	    	conn = dataSource.getConnection(StringUtil.isNullOrEmpty(username)?"":username,StringUtil.isNullOrEmpty(password)?"":password);
	    	stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlText);
            log.info("执行sql："+sqlText);
            final ResultSetMetaData rsMataData = rs.getMetaData();
            final int count = rsMataData.getColumnCount();
            result = new ArrayList<>();
            while(rs.next()){
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                for(int i = 0; i < count; i++){
                 map.put(rsMataData.getColumnLabel(i+1), rs.getObject(rsMataData.getColumnLabel(i+1)));
                }
                result.add(map);
            }
        } catch (final SQLException ex) {
            throw new BizException(StatusCode.FAILURE,"sql语句执行错误，请检查sql语句是否拼写正确或者数据源是否选择正确。错误信息："+ex.getMessage());
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, rs);
        }
		return result;
	}
	
	/**  
	 * @MethodName: getInfluxdbData
	 * @Description: 获取influxdb数据
	 * @author caiyang
	 * @param connection
	 * @param sqlText
	 * @return 
	 * @return List<Map<String,Object>>
	 * @date 2022-12-06 04:28:27 
	 */  
	public static List<Map<String, Object>> getInfluxdbData(InfluxDBConnection connection,String sqlText)
	{
		List<Map<String, Object>> result = null;
		try {
			QueryResult queryResult = connection.query(sqlText);
			if(queryResult.getResults().get(0).getSeries() != null)
			{
				List<String> columns = queryResult.getResults().get(0).getSeries().get(0).getColumns();
				List<List<Object>> values= queryResult.getResults().get(0).getSeries().get(0).getValues();
				if(!ListUtil.isEmpty(values))
				{
					result = new ArrayList<>();
					for (int i = 0; i < values.size(); i++) {
						List<Object> rowValues = values.get(i);
						Map<String, Object> map = new LinkedHashMap<>();
						for (int j = 0; j < rowValues.size(); j++) {
							map.put(columns.get(j), rowValues.get(j));
						}
						result.add(map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connection.close();
		}
		
		return result;
	}
	
	/**  
	 * @Title: getDatasourceDataByProcedure
	 * @Description: 根据存储过程获取数据
	 * @param dataSource
	 * @param sqlText
	 * @param params
	 * @param inParams
	 * @param outParams
	 * @return
	 * @author caiyang
	 * @date 2021-11-18 02:06:49 
	 */ 
	public static List<Map<String, Object>> getDatasourceDataByProcedure(DataSource dataSource, String sqlText,Map<String, Object> params,JSONArray inParams,JSONArray outParams) {
		if(params == null)
		{
			params = new HashMap<String, Object>();
		}
		List<Map<String, Object>> result = null;
		Connection conn = null;
		CallableStatement cstm = null;
	    ResultSet rs = null;
	    int inParamSize = 0;
	    try {
	    	conn = dataSource.getConnection();
	    	if(!sqlText.startsWith("{"))
	    	{
	    		sqlText = "{" + sqlText;
	    	}
	    	if(!sqlText.endsWith("}"))
	    	{
	    		sqlText = sqlText + "}";
	    	}
	    	cstm = conn.prepareCall(sqlText); //实例化对象cstm
	    	result = new ArrayList<>();
	    	if(!ListUtil.isEmpty(inParams))
	    	{
	    		inParamSize = inParams.size();
	    		JSONObject jsonObject = null;
	    		for (int i = 0; i < inParams.size(); i++) {
	    			jsonObject = (JSONObject) inParams.get(i);
	    			String paramCode = jsonObject.getString("paramCode");
	    			Object param = params.get(paramCode);
	    			if(param == null)
	    			{
	    				param = jsonObject.get("paramDefault");
	    			}
	    			if(InParamTypeEnum.INT.getCode().equals(jsonObject.getString("paramType")))
	    			{
	    				if(StringUtil.isNotEmpty(jsonObject.getString("paramDefault"))) {
	    					cstm.setInt(i+1, Integer.valueOf(String.valueOf(param)));
	    				}else {
	    					cstm.setInt(i+1, 0);
	    				}
	    			}else if(InParamTypeEnum.STRING.getCode().equals(jsonObject.getString("paramType")))
    				{
	    				if(StringUtil.isNotEmpty(jsonObject.getString("paramDefault"))) {
	    					cstm.setString(i+1, String.valueOf(param));
	    				}else {
	    					cstm.setString(i+1, null);
	    				}
    				}else if(InParamTypeEnum.LONG.getCode().equals(jsonObject.getString("paramType")))
    				{
    					cstm.setLong(i+1, Long.valueOf(String.valueOf(param)));
    				}else if(InParamTypeEnum.DOUBLE.getCode().equals(jsonObject.getString("paramType")))
    				{
    					cstm.setDouble(i+1, Double.valueOf(String.valueOf(param)));
    				}else if(InParamTypeEnum.FLOAT.getCode().equals(jsonObject.getString("paramType")))
    				{
    					cstm.setFloat(i+1, Float.valueOf(String.valueOf(param)));
    				}else if(InParamTypeEnum.BIGDECIMAL.getCode().equals(jsonObject.getString("paramType")))
    				{
    					cstm.setBigDecimal(i+1, new BigDecimal(String.valueOf(param)));
    				}else if(InParamTypeEnum.DATE.getCode().equals(jsonObject.getString("paramType")))
	    			{
    					String dateFormat = jsonObject.getString("dateFormat");
    					if(StringUtil.isNullOrEmpty(dateFormat)) {
    						dateFormat = DateUtil.FORMAT_LONOGRAM;
    					}
    					if("YYYY-MM-DD".equals(dateFormat))
						{
							dateFormat = DateUtil.FORMAT_LONOGRAM;
						}else if("YYYY-MM".equals(dateFormat))
						{
							dateFormat = DateUtil.FORMAT_YEARMONTH;
						}else if("YYYY-MM-DD HH:mm".equals(dateFormat))
						{
							dateFormat = DateUtil.FORMAT_WITHOUTSECONDS;
						}else if("YYYY".equals(dateFormat))
						{
							dateFormat = DateUtil.FORMAT_YEAR;
						}
    					cstm.setDate(i+1, DateUtil.string2SqlDate(String.valueOf(param),dateFormat));
	    			}else if(InParamTypeEnum.DATETIME.getCode().equals(jsonObject.getString("paramType")))
    				{
    					cstm.setTimestamp(i+1, DateUtil.string2SqlTimestamp(String.valueOf(jsonObject.get("paramDefault")),DateUtil.FORMAT_FULL));
    				}
	    		}
	    	}
	    	if(ListUtil.isEmpty(outParams))
	    	{
	            rs = cstm.executeQuery();
	            log.info("执行sql："+sqlText);
	            final ResultSetMetaData rsMataData = rs.getMetaData();
	            final int count = rsMataData.getColumnCount();
	            result = new ArrayList<>();
	            while(rs.next()){
	                Map<String, Object> map = new LinkedHashMap<String, Object>();
	                for(int i = 0; i < count; i++){
	                 map.put(rsMataData.getColumnLabel(i+1), rs.getObject(rsMataData.getColumnLabel(i+1)));
	                }
	                result.add(map);
	            }
	    	}else {
	    		JSONObject jsonObject = null;
	    		for (int i = 0; i < outParams.size(); i++) {
	    			jsonObject = (JSONObject) outParams.get(i);
	    			if(OutParamTypeEnum.INTEGER.getCode().equals(jsonObject.getString("paramType")))
	    			{
	    				cstm.registerOutParameter(i+1+inParamSize, Types.INTEGER);
	    			}else if(OutParamTypeEnum.VARCHAR.getCode().equals(jsonObject.getString("paramType")))
		    		{
	    				cstm.registerOutParameter(i+1+inParamSize, Types.VARCHAR);
		    		}else if(OutParamTypeEnum.BIGINT.getCode().equals(jsonObject.getString("paramType"))) 
		    		{
		    			cstm.registerOutParameter(i+1+inParamSize, Types.BIGINT);
		    		}else if(OutParamTypeEnum.FLOAT.getCode().equals(jsonObject.getString("paramType"))) 
		    		{
		    			cstm.registerOutParameter(i+1+inParamSize, Types.FLOAT);
		    		}else if(OutParamTypeEnum.DOUBLE.getCode().equals(jsonObject.getString("paramType"))) 
		    		{
		    			cstm.registerOutParameter(i+1+inParamSize, Types.DOUBLE);
		    		}else if(OutParamTypeEnum.DECIMAL.getCode().equals(jsonObject.getString("paramType"))) 
		    		{
		    			cstm.registerOutParameter(i+1+inParamSize, Types.DECIMAL);
		    		}
	    		}
	    		cstm.executeQuery();
	    		log.info("执行sql："+sqlText);
	    		Map<String, Object> map = new HashMap<String, Object>();
	    		for (int i = 0; i < outParams.size(); i++) {
	    			jsonObject = (JSONObject) outParams.get(i);
	    			String paramCode = jsonObject.getString("paramCode");
	    			int index = i + 1 + inParamSize;
	    			if(OutParamTypeEnum.INTEGER.getCode().equals(jsonObject.getString("paramType")))
	    			{
	    				int outParam = cstm.getInt(index);
	    				map.put(paramCode, outParam);
	    			}else if(OutParamTypeEnum.VARCHAR.getCode().equals(jsonObject.getString("paramType")))
		    		{
	    				String outParam = cstm.getString(index);
	    				map.put(paramCode, outParam);
		    		}else if(OutParamTypeEnum.BIGINT.getCode().equals(jsonObject.getString("paramType"))) 
		    		{
		    			long outParam = cstm.getLong(index);
		    			map.put(paramCode, outParam);
		    		}else if(OutParamTypeEnum.FLOAT.getCode().equals(jsonObject.getString("paramType"))) 
		    		{
		    			float outParam = cstm.getFloat(index);
		    			map.put(paramCode, outParam);
		    		}else if(OutParamTypeEnum.DOUBLE.getCode().equals(jsonObject.getString("paramType"))) 
		    		{
		    			double outParam = cstm.getDouble(index);
		    			map.put(paramCode, outParam);
		    		}else if(OutParamTypeEnum.DECIMAL.getCode().equals(jsonObject.getString("paramType"))) 
		    		{
		    			BigDecimal outParam = cstm.getBigDecimal(index);
		    			map.put(paramCode, outParam);
		    		}
	    		}
	    		result.add(map);
	    	}
		} catch (Exception ex) {
			throw new BizException(StatusCode.FAILURE,"sql语句执行错误，请检查sql语句是否拼写正确或者数据源是否选择正确。错误信息："+ex.getMessage());
		} finally {
            JdbcUtils.releaseJdbcResource(conn, cstm, rs);
        }
		return result;
	}
	
	/**  
	 * @Title: getSelectData
	 * @Description: 获取下拉选择框数据
	 * @param dataSource
	 * @param sqlText
	 * @return
	 * @author caiyang
	 * @date 2021-11-18 02:15:52 
	 */ 
	public static List<Map<String, Object>> getSelectData(DataSource dataSource, String sqlText) {
		Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    List<Map<String, Object>> result = null;
	    try {
	    	conn = dataSource.getConnection();
	    	stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlText);
            final ResultSetMetaData rsMataData = rs.getMetaData();
            final int count = rsMataData.getColumnCount();
            result = new ArrayList<>(count);
            while(rs.next()){
                Map<String, Object> map = new HashMap<String, Object>();
                for(int i = 0; i < count; i++){
                 map.put(rsMataData.getColumnLabel(i+1).toLowerCase(), rs.getObject(rsMataData.getColumnLabel(i+1)));
                }
                result.add(map);
            }
        } catch (final SQLException ex) {
        	throw new BizException(StatusCode.FAILURE,"sql语句执行错误，请检查sql语句是否拼写正确或者数据源是否选择正确。错误信息："+ex.getMessage());
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, rs);
        }
	    return result;
	}
	
	/**  
	 * @Title: getDataCountBySQL
	 * @Description: 获取数据总条数
	 * @param dataSource
	 * @param sqlText
	 * @return
	 * @author caiyang
	 * @date 2021-11-18 02:16:18 
	 */ 
	public static long getDataCountBySQL(DataSource dataSource, String sqlText) {
		long result = 0;
		Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	    	conn = dataSource.getConnection();
	    	stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlText);
            rs.next();
            result = rs.getLong(1);
        } catch (final SQLException ex) {
        	throw new BizException(StatusCode.FAILURE,"sql语句执行错误，请检查sql语句是否拼写正确或者数据源是否选择正确。错误信息："+ex.getMessage());
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, rs);
        }
		return result;
	}
	
	public static int getDataCountBySQL(Connection conn, String sqlText) {
		int result = 0;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	    	stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlText);
            rs.next();
            result = rs.getInt(1);
        } catch (final SQLException ex) {
        	throw new BizException(StatusCode.FAILURE,"sql语句执行错误，请检查sql语句是否拼写正确或者数据源是否选择正确。错误信息："+ex.getMessage());
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, rs);
        }
		return result;
	}
	
	/**  
	 * @MethodName: getInfluxdbDataCountBySQL
	 * @Description: influxdb获取数据总条数
	 * @author caiyang
	 * @param connection
	 * @param sqlText
	 * @return 
	 * @return int
	 * @date 2022-12-15 02:31:04 
	 */  
	public static int getInfluxdbDataCountBySQL(InfluxDBConnection connection, String sqlText) {
		int result = 0;
		QueryResult queryResult = connection.query(sqlText);
		if(queryResult.getResults().get(0).getSeries() != null)
		{
			List<List<Object>> values= queryResult.getResults().get(0).getSeries().get(0).getValues();
			if(!ListUtil.isEmpty(values))
			{
				List<Object> rowValues = values.get(0);
				if(rowValues.size() == 1)
				{
					Double count = (double) rowValues.get(0);
					result = count.intValue();
				}else {
					Double count = (double) rowValues.get(1);
					result = count.intValue();
				}
			}
		}
		return result;
	}
	
	/**  
	 * @MethodName: reportData
	 * @Description: 上报数据
	 * @author caiyang
	 * @param dataSource
	 * @param details 
	 * @return void
	 * @date 2022-11-23 11:13:44 
	 */  
	public static void reportData(DataSource dataSource,Map<String, List<ReportDataDetailDto>> mapDetails,int type,UserInfoDto userInfoDto,Map<String, Map<String, String>> tableColumnJavaType) {
//		if(type == 1)
//		{//mysql类型处理
//			processMysqlReportData(dataSource,mapDetails);
//		}else {
//			procesOtherDatabase(dataSource,mapDetails,type);
//		}
		procesOtherDatabase(dataSource,mapDetails,type,userInfoDto,tableColumnJavaType);
	}
	
	/**  
	 * @MethodName: processMysqlReportData
	 * @Description: mysql上报数据处理
	 * @author caiyang
	 * @param dataSource
	 * @param mapDetails 
	 * @return void
	 * @date 2022-11-24 08:43:03 
	 */  
	private static void processMysqlReportData(DataSource dataSource,Map<String, List<ReportDataDetailDto>> mapDetails) {
		Connection conn = null;
	    PreparedStatement ps = null;
	    try {
	    	conn = dataSource.getConnection();
	    	Set<String> tableNames = mapDetails.keySet();
	    	Map<String, List<List<Object>>> sqlParamsMap = new HashMap<>(); 
	    	for(String tableName : tableNames)
	    	{
	    		List<ReportDataDetailDto> details = mapDetails.get(tableName);
	    		if(!ListUtil.isEmpty(details))
	    		{
	    			for (int i = 0; i < details.size(); i++) {
	    				List<Object> params = new ArrayList<>();
	    				String columnSql = "";
		    			String paramSql = "";
		    			String duplicateKeySql = "";
	    				List<ReportDataColumnDto> columns = details.get(i).getColumns();
	    				for (int j = 0; j < columns.size(); j++) {
	    					params.add(columns.get(j).getData());
	    					if(j == 0)
	    					{
	    						columnSql = columnSql + columns.get(j).getColumnName();
	    						paramSql = paramSql + "?";
	    						duplicateKeySql = duplicateKeySql + columns.get(j).getColumnName() + "= ?"; 
	    					}else {
	    						columnSql = columnSql + "," + columns.get(j).getColumnName();
	    						paramSql = paramSql + "," + "?";
	    						duplicateKeySql = duplicateKeySql + "," + columns.get(j).getColumnName() + "= ?"; 
	    					}
						}
	    				String sql = "INSERT INTO " + tableName + "(" + columnSql + ") VALUES (" + paramSql + ") ON DUPLICATE KEY UPDATE " + duplicateKeySql; 
	    				if(sqlParamsMap.get(sql) == null)
	    				{
	    					List<List<Object>> list = new ArrayList<>();
	    					list.add(params);
	    					sqlParamsMap.put(sql, list);
	    				}else {
	    					sqlParamsMap.get(sql).add(params);
	    				}
	    			}
	    		}
	    	}
	    	if(!sqlParamsMap.isEmpty())
	    	{
	    		conn.setAutoCommit(false);
	    		Set<String> sqls = sqlParamsMap.keySet();
	    		for(String sql : sqls)
	    		{
	    			ps = conn.prepareStatement(sql);
	    			List<List<Object>> params = sqlParamsMap.get(sql);
	    			for (int i = 0; i < params.size(); i++) {
	    				List<Object> rowParams = params.get(i);
	    				for (int j = 0; j < rowParams.size(); j++) {
							ps.setObject(j+1, rowParams.get(j));
							ps.setObject(j+1+rowParams.size(), rowParams.get(j));
						}
	    				ps.addBatch();
					}
	    			ps.executeBatch();
	    			ps.clearBatch();
	    			ps.close();
	    		}
	    		conn.commit();
	    	}
        } catch (final SQLException ex) {
        	throw new BizException(StatusCode.FAILURE,"sql语句执行错误，请检查sql语句是否拼写正确或者数据源是否选择正确。错误信息："+ex.getMessage());
        } finally {
            JdbcUtils.releaseJdbcResource(conn, ps, null);
        }
	}
	
	/**  
	 * @MethodName: procesOtherDatabase
	 * @Description: 处理其他数据库的数据上报
	 * @author caiyang
	 * @param dataSource
	 * @param mapDetails 
	 * @return void
	 * @date 2022-11-24 09:23:17 
	 */  
	private static void procesOtherDatabase(DataSource dataSource,Map<String, List<ReportDataDetailDto>> mapDetails,int type,UserInfoDto userInfoDto,Map<String, Map<String, String>> tableColumnJavaType) {
		Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	    	conn = dataSource.getConnection();
	    	Set<String> tableNames = mapDetails.keySet();
	    	Map<String, List<List<Object>>> sqlParamsMap = new HashMap<>(); 
	    	for(String tableName : tableNames)
	    	{
	    		List<ReportDataDetailDto> details = mapDetails.get(tableName);
	    		Map<String, String> columnJavaType = tableColumnJavaType.get(tableName);
	    		if(!ListUtil.isEmpty(details))
	    		{
	    			for (int i = 0; i < details.size(); i++) {
	    				 List<ReportDataColumnDto> keys = details.get(i).getKeys();
	    				 List<ReportDataColumnDto> columns = details.get(i).getColumns();
	    				 JSONObject autoFillAttrs = details.get(i).getAutoFillAttrs();
	    				 if(details.get(i).isInsert())
	    				 {//新增数据
	    					 processInsertSql(columns,sqlParamsMap,tableName,autoFillAttrs,userInfoDto,details.get(i).getDatasoaurceId(),details.get(i).getFormsName(),columnJavaType);
	    				 }else {
	    					 //主键有数据，则是更新数据，没有数据则是新增数据
	    					 boolean isInsert = false;//是否是插入数据，如果有主键为空，则就认为是插入数据
	    					 for (int j = 0; j < keys.size(); j++) {
	    						 if(keys.get(j).getData() == null) {
	    							 isInsert = true;
	    							 break;
	    						 }
	    					 }
	    					 if(isInsert || ListUtil.isEmpty(keys)) {
	    						 processInsertSql(columns,sqlParamsMap,tableName,autoFillAttrs,userInfoDto,details.get(i).getDatasoaurceId(),details.get(i).getFormsName(),columnJavaType);
	    					 }else {
	    						 //keys如果是多个，需要先查询看一下是否有数据，有则更新，没有则新增
	    						 if(ListUtil.isNotEmpty(keys) && keys.size() >1) {
	    							String whereSql = " ";
	 	    						List<Object> whereParams = new ArrayList<>();
	 	    						for (int j = 0; j < keys.size(); j++) {
	 	    							if(keys.get(j).getData() == null) {
	 	    								continue;
	 	    							}
	 	    							if(keys.get(j).getIdType().intValue() == 3) {
	 	    								whereParams.add(Long.parseLong(String.valueOf(keys.get(j).getData())));
	 	    							}else {
	 	    								whereParams.add(keys.get(j).getData());
	 	    							}
	 	    							if(StringUtil.isNullOrEmpty(whereSql.trim()))
	 	    							{
	 	    								whereSql = whereSql + keys.get(j).getColumnName() + " = ?";
	 	    							}else {
	 	    								whereSql = whereSql + " AND " + keys.get(j).getColumnName() + " = ?";
	 	    							}
	 	    						}
	 	    						if(StringUtil.isNotEmpty(whereSql)) {
	 	    							String selectSql = "select * from " + tableName + " where " + whereSql;
	 		    						selectSql = JdbcUtils.preprocessSqlText(selectSql, type,null);
	 		    						ps = conn.prepareStatement(selectSql);
	 		    						if(!ListUtil.isEmpty(whereParams))
	 		    						{
	 		    							for (int j = 0; j < whereParams.size(); j++) {
	 											ps.setObject(j+1, whereParams.get(j));
	 										}
	 		    						}
	 		    						rs = ps.executeQuery();
	 		    						if(!rs.next())
	 		    						{//没有数据，新增
	 		    							processInsertSql(columns,sqlParamsMap,tableName,autoFillAttrs,userInfoDto,details.get(i).getDatasoaurceId(),details.get(i).getFormsName(),columnJavaType);
	 		    						}else {
	 		    							//有数据，更新
	 		    							processUpdateSql(columns,keys,sqlParamsMap,tableName,autoFillAttrs,userInfoDto,details.get(i).getDatasoaurceId(),details.get(i).getFormsName(),columnJavaType);
	 		    						}
	 		    						ps.close();	
	 	    						}else {
	 	    							processInsertSql(columns,sqlParamsMap,tableName,autoFillAttrs,userInfoDto,details.get(i).getDatasoaurceId(),details.get(i).getFormsName(),columnJavaType);
	 	    						}
	    						 }else {
	    							 processUpdateSql(columns,keys,sqlParamsMap,tableName,autoFillAttrs,userInfoDto,details.get(i).getDatasoaurceId(),details.get(i).getFormsName(),columnJavaType); 
	    						 }
	    					 }
	    						
//	    					if(isAutoKey)
//	    					{
//	    						processInsertSql(columns,sqlParamsMap,tableName);
//	    					}else {
//	    						
//	    					}
	    				 }
	    			}
	    		}
	    	}
	    	if(!sqlParamsMap.isEmpty())
	    	{
	    		conn.setAutoCommit(false);
	    		Set<String> sqls = sqlParamsMap.keySet();
	    		for(String sql : sqls)
	    		{
	    			ps = conn.prepareStatement(sql);
	    			List<List<Object>> params = sqlParamsMap.get(sql);
	    			for (int i = 0; i < params.size(); i++) {
	    				List<Object> rowParams = params.get(i);
	    				for (int j = 0; j < rowParams.size(); j++) {
							ps.setObject(j+1, rowParams.get(j));
						}
	    				ps.addBatch();
	    			}
	    			ps.executeBatch();
	    			ps.clearBatch();
	    			ps.close();
	    		}
	    		conn.commit();
	    	}
        } catch (final SQLException ex) {
        	throw new BizException(StatusCode.FAILURE,"sql语句执行错误，请检查sql语句是否拼写正确或者数据源是否选择正确。错误信息："+ex.getMessage());
        } finally {
            JdbcUtils.releaseJdbcResource(conn,ps, rs);
        }
	}
	
	/**  
	 * @MethodName: processInsertSql
	 * @Description: 新增语句处理
	 * @author caiyang
	 * @param columns
	 * @param sqlParamsMap
	 * @param tableName 
	 * @return void
	 * @date 2022-11-24 10:33:19 
	 */  
	private static void processInsertSql(List<ReportDataColumnDto> columns,Map<String, List<List<Object>>> sqlParamsMap,String tableName,JSONObject autoFillAttrs,UserInfoDto userInfoDto,String datasourceId,String formsName,Map<String, String> columnJavaType)
	{
		List<Object> params = new ArrayList<>();
		String columnSql = "";
		String paramSql = "";
		for (int j = 0; j < columns.size(); j++) {
			String column = columns.get(j).getColumnName();
			Object data = columns.get(j).getData();
			if(!StringUtil.isEmptyMap(columnJavaType) && columnJavaType.containsKey(column)) {
				data = convertValue(columnJavaType.get(column), data);
			}
			params.add(data);
			if(j == 0)
			{
				columnSql = columnSql + column;
				paramSql = paramSql + "?";
			}else {
				columnSql = columnSql + "," + column;
				paramSql = paramSql + "," + "?"; 
			}
		 }
		String attrKey = datasourceId + "|" + tableName + "|" + formsName;
		for (String key : autoFillAttrs.keySet()) {
			  if(!key.contains(attrKey)) {
				  continue;
			  }
		      JSONObject attr = autoFillAttrs.getJSONObject(key);
		      int fillStrategy = attr.getIntValue("fillStrategy");
		      if(fillStrategy == 1 || fillStrategy == 3) {
		    	  String columnName = attr.getString("columnName");
		    	  int fillType = attr.getIntValue("fillType");
		    	  if(fillType == 1) {//系统时间
		    		  params.add(DateUtil.string2SqlTimestamp(DateUtil.getNow(),DateUtil.FORMAT_FULL) );  
		    	  }else if(fillType == 2) {//用户id
		    		  Object data = userInfoDto.getUserId();
			  		  if(!StringUtil.isEmptyMap(columnJavaType) && columnJavaType.containsKey(columnName)) {
			  			data = convertValue(columnJavaType.get(columnName), data);
			  		  }
		    		  params.add(data);  
		    	  }else if(fillType == 3) {//用户名
		    		  params.add(userInfoDto.getUserName());  
		    	  }else if(fillType == 4) {//商户号
		    		  params.add(userInfoDto.getMerchantNo());  
		    	  }else if(fillType == 99 || fillType == 5) {//自定义数据 || 单元格数据
		    		  Object data = attr.get("fillValue");
		    		  if(!StringUtil.isEmptyMap(columnJavaType) && columnJavaType.containsKey(columnName)) {
		  				data = convertValue(columnJavaType.get(columnName), data);
		  			  }
		    		  params.add(data);  
		    	  }
		    	  columnSql = columnSql + "," + columnName;
		    	  paramSql = paramSql + "," + "?"; 
		      }
		}
		 String sql = "INSERT INTO " + tableName + "(" + columnSql + ") VALUES (" + paramSql + ")";
		 if(sqlParamsMap.get(sql) == null)
		 {
			List<List<Object>> list = new ArrayList<>();
			list.add(params);
			sqlParamsMap.put(sql, list);
		 }else {
			 sqlParamsMap.get(sql).add(params);
		 }
	}
	
	/**  
	 * @MethodName: processUpdateSql
	 * @Description: 处理更新sql
	 * @author caiyang
	 * @param columns
	 * @param sqlParamsMap
	 * @param tableName 
	 * @return void
	 * @date 2022-11-24 10:55:16 
	 */  
	private static void processUpdateSql(List<ReportDataColumnDto> columns,List<ReportDataColumnDto> keys,Map<String, List<List<Object>>> sqlParamsMap,String tableName,JSONObject autoFillAttrs,UserInfoDto userInfoDto,String datasourceId,String formsName,Map<String, String> columnJavaType)
	{
		List<Object> params = new ArrayList<>();
		String columnSql = "";
		String whereSql = " ";
		Map<String, String> keysMap = new HashMap<>();
		for (int j = 0; j < keys.size(); j++) {
			keysMap.put(keys.get(j).getColumnName(), keys.get(j).getColumnName());
		}
		for (int j = 0; j < columns.size(); j++) {
			if(keysMap.containsKey(columns.get(j).getColumnName())) {
				continue;
			}
			String column = columns.get(j).getColumnName();
			Object data = columns.get(j).getData();
			if(!StringUtil.isEmptyMap(columnJavaType) && columnJavaType.containsKey(column)) {
				data = convertValue(columnJavaType.get(column), data);
			}
			params.add(data);
			if(StringUtil.isNullOrEmpty(columnSql))
			{
				columnSql = columnSql + column + " = ?";
			}else {
				columnSql = columnSql + "," + column + " = ?";
			}
		}
		String attrKey = datasourceId + "|" + tableName + "|" + formsName;
		for (String key : autoFillAttrs.keySet()) {
			  if(!key.contains(attrKey)) {
				  continue;
			  }
		      JSONObject attr = autoFillAttrs.getJSONObject(key);
		      int fillStrategy = attr.getIntValue("fillStrategy");
		      if(fillStrategy == 2 || fillStrategy == 3) {
		    	  String columnName = attr.getString("columnName");
		    	  int fillType = attr.getIntValue("fillType");
		    	  if(fillType == 1) {//系统时间
		    		  params.add(DateUtil.string2SqlTimestamp(DateUtil.getNow(),DateUtil.FORMAT_FULL) );
		    	  }else if(fillType == 2) {//用户id
		    		  Object data = userInfoDto.getUserId();
			  		  if(!StringUtil.isEmptyMap(columnJavaType) && columnJavaType.containsKey(columnName)) {
			  			data = convertValue(columnJavaType.get(columnName), data);
			  		  }
		    		  params.add(data);
		    	  }else if(fillType == 3) {//用户名
		    		  params.add(userInfoDto.getUserName());  
		    	  }else if(fillType == 4) {//商户号
		    		  params.add(userInfoDto.getMerchantNo());  
		    	  }else if(fillType == 99 || fillType == 5) {//自定义数据
		    		  Object data = attr.get("fillValue");
		    		  if(!StringUtil.isEmptyMap(columnJavaType) && columnJavaType.containsKey(columnName)) {
		  				data = convertValue(columnJavaType.get(columnName), data);
		  			  }
		    		  params.add(data);  
		    	  }
		    	  columnSql = columnSql + "," + columnName + " = ?";
		      }
		}
		for (int j = 0; j < keys.size(); j++) {
			if(keys.get(j).getData() == null) {
				continue;
			}
			String column = keys.get(j).getColumnName();
			Object data = null;
			data = keys.get(j).getData();
//			if(keys.get(j).getIdType().intValue() == 3) {
//				data = keys.get(j).getData()
//				params.add(Long.parseLong(String.valueOf(keys.get(j).getData())));
//			}else {
//				params.add(keys.get(j).getData());
//			}
			if(!StringUtil.isEmptyMap(columnJavaType) && columnJavaType.containsKey(column)) {
				data = convertValue(columnJavaType.get(column), data);
			}
			params.add(data);
			if(StringUtil.isNullOrEmpty(whereSql.trim()))
			{
				whereSql = whereSql + keys.get(j).getColumnName() + " = ?";
			}else {
				whereSql = whereSql + " AND " + keys.get(j).getColumnName() + " = ?";
			}
		}
		String sql = "UPDATE " + tableName + " SET " + columnSql + " where " + whereSql;
		if(sqlParamsMap.get(sql) == null)
		 {
			List<List<Object>> list = new ArrayList<>();
			list.add(params);
			sqlParamsMap.put(sql, list);
		 }else {
			 sqlParamsMap.get(sql).add(params);
		 }
	}
	
	/**  
	 * @MethodName: deleteData
	 * @Description: 删除数据
	 * @author caiyang
	 * @param dataSource
	 * @param mapDetails
	 * @param type
	 * @param userInfoDto void
	 * @date 2025-02-17 12:41:01 
	 */ 
	public static void deleteData(DataSource dataSource,JSONObject params,UserInfoDto userInfoDto,Map<String, String> columnJavaType) {
		String column = params.getString("column");
		String deleteColumn = params.getString("deleteColumn");
		Object value =params.getString("value");
		String table = params.getString("table");
		String deleteType = params.getString("deleteType");
		Object deleteValue = params.get("deleteValue");
		String sql = "";
		if(!StringUtil.isEmptyMap(columnJavaType)) {
			if(columnJavaType.containsKey(column)) {
				String javaType = columnJavaType.get(column);
				value = convertValue(javaType, value);
			}
			if(columnJavaType.containsKey(deleteColumn)) {
				String javaType = columnJavaType.get(deleteColumn);
				deleteValue = convertValue(javaType, deleteValue);
			}
		}
		if("1".equals(deleteType)) {//物理删除
			sql = "delete from " + table + " where " + column +" = ?";
		}else {//逻辑删除
			sql = "update " + table + " set " + deleteColumn + " = ?"  + " where " + column +" = ?";
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(sql);
			if("1".equals(deleteType)) {//物理删除
				stmt.setObject(1, value);
			}else {
				stmt.setObject(1, deleteValue);
				stmt.setObject(2, value);
			}
			stmt.execute();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
            JdbcUtils.releaseJdbcResource(conn,stmt, null);
        }
	}
	
	private static Object convertValue(String type,Object value) {
		if(value != null) {
			if("java.lang.Long".equals(type)) {
				value = Long.parseLong(String.valueOf(value));
			}else if("java.lang.Integer".equals(type)) {
				value = Integer.parseInt(String.valueOf(value));
			}else if("java.lang.Double".equals(type)) {
				value = Double.parseDouble(String.valueOf(value));
			}else if("java.lang.Float".equals(type)) {
				value = Float.parseFloat(String.valueOf(value));
			}else if("java.math.BigDecimal".equals(type)) {
				value = new BigDecimal(String.valueOf(value));
			}else if("java.sql.Timestamp".equals(type)) {
				value = DateUtil.string2SqlTimestamp(String.valueOf(value),DateUtil.FORMAT_FULL);
			}else if("java.sql.Date".equals(type)) {
				value = DateUtil.string2SqlDate(String.valueOf(value),DateUtil.FORMAT_LONOGRAM);
			}else if("java.lang.Boolean".equals(type)) {
				value = Boolean.parseBoolean(String.valueOf(value));
			}else if("java.lang.String".equals(type)) {
				value = String.valueOf(value);
			}
		}
		return value;
	}
}
