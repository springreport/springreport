package com.springreport.util;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.xpack.sql.jdbc.EsDataSource;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.base.DataSourceConfig;
import com.springreport.base.EsDataSourceConfig;
import com.springreport.base.InfluxDbDataSourceConfig;
import com.springreport.base.ReportDataDetailDto;
import com.springreport.base.SqlWhereDto;
import com.springreport.base.TDengineConfig;
import com.springreport.base.TDengineConnection;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.enums.DriverClassEnum;
import com.springreport.enums.InParamTypeEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;

/**
* <p>Title: JdbcUtils</p>
* <p>Description: jdbc工具类</p>
* @author caiyang
* @date 2020年3月5日
*/
public class JdbcUtils {

	private static final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>(100);
	
	private static final Map<String, InfluxDBConnection> influxdbDataSourceMap = new ConcurrentHashMap<>(100);
	
	private static final Map<String, EsDataSource> esDataSourceMap = new ConcurrentHashMap<>(100);
	
	public static DataSource getDataSource(final DataSourceConfig dataSourceConfig) {
        //用数据源用户名,密码,jdbcUrl做为key
		 final String key = String.format("%s|%s|%s", dataSourceConfig.getUser(), dataSourceConfig.getPassword(), dataSourceConfig.getJdbcUrl())
		            .toLowerCase();
        DataSource druidDataSource = dataSourceMap.get(key);
        if (druidDataSource == null) {
        	try {
                DruidDataSource dataSource = new DruidDataSource();
                dataSource.setDriverClassName(dataSourceConfig.getDriverClass());
                dataSource.setUrl(dataSourceConfig.getJdbcUrl());
                dataSource.setUsername(dataSourceConfig.getUser());
                dataSource.setPassword(dataSourceConfig.getPassword());
                dataSource.setInitialSize(MapUtils.getInteger(dataSourceConfig.getOptions(), "initialSize", 3));
                dataSource.setMaxActive(MapUtils.getInteger(dataSourceConfig.getOptions(), "maxActive", 20));
                dataSource.setMinIdle(MapUtils.getInteger(dataSourceConfig.getOptions(), "minIdle", 1));
                dataSource.setMaxWait(MapUtils.getInteger(dataSourceConfig.getOptions(), "maxWait", 60000));
                dataSource.setTimeBetweenEvictionRunsMillis(
                    MapUtils.getInteger(dataSourceConfig.getOptions(), "timeBetweenEvictionRunsMillis", 60000));
                dataSource.setMinEvictableIdleTimeMillis(
                    MapUtils.getInteger(dataSourceConfig.getOptions(), "minEvictableIdleTimeMillis", 300000));
                dataSource.setTestWhileIdle(MapUtils.getBoolean(dataSourceConfig.getOptions(), "testWhileIdle", true));
                dataSource.setTestOnBorrow(MapUtils.getBoolean(dataSourceConfig.getOptions(), "testOnBorrow", false));
                dataSource.setTestOnReturn(MapUtils.getBoolean(dataSourceConfig.getOptions(), "testOnReturn", false));
                dataSource.setMaxOpenPreparedStatements(
                    MapUtils.getInteger(dataSourceConfig.getOptions(), "maxOpenPreparedStatements", 20));
                dataSource.setRemoveAbandoned(MapUtils.getBoolean(dataSourceConfig.getOptions(), "removeAbandoned", true));
                dataSource.setRemoveAbandonedTimeout(
                    MapUtils.getInteger(dataSourceConfig.getOptions(), "removeAbandonedTimeout", 1800));
                dataSource.setLogAbandoned(MapUtils.getBoolean(dataSourceConfig.getOptions(), "logAbandoned", true));
                dataSource.setConnectionErrorRetryAttempts(0);
                dataSource.setBreakAfterAcquireFailure(true);
                if(DriverClassEnum.SQLSERVER.getName().equals(dataSourceConfig.getDriverClass()))
                {
                	dataSource.setValidationQuery("SELECT 1");
                }
                dataSourceMap.put(key, dataSource);
                return dataSource;
            } catch (final Exception ex) {
                throw new BizException(StatusCode.FAILURE, "数据库连接池获取异常，异常信息："+ex.getMessage());
            }
        }
        return druidDataSource;
    }
	
	/**  
	 * @MethodName: getInfluxdbDatasource
	 * @Description: 获取influxdb的数据库连接
	 * @author caiyang
	 * @param dataSourceConfig
	 * @return 
	 * @return InfluxDBConnection
	 * @date 2022-12-06 10:39:39 
	 */  
	public static InfluxDBConnection getInfluxdbDatasource(final InfluxDbDataSourceConfig dataSourceConfig) {
		final String key = String.format("%s|%s|%s|%s", dataSourceConfig.getUsername(), dataSourceConfig.getPassword(), dataSourceConfig.getOpenurl(),dataSourceConfig.getDatabase())
	            .toLowerCase();
//		InfluxDBConnection influxDBConnection = influxdbDataSourceMap.get(key);
//		if(influxDBConnection == null) {
			InfluxDBConnection influxDBConnection =  new InfluxDBConnection(dataSourceConfig.getUsername(), dataSourceConfig.getPassword(), dataSourceConfig.getOpenurl(), dataSourceConfig.getDatabase(), null);
//			influxdbDataSourceMap.put(key, influxDBConnection);
//		}
		return influxDBConnection;
	}
	
	/**  
	 * @MethodName: getEsConnection
	 * @Description: 获取es的数据库连接
	 * @author caiyang
	 * @param esDataSourceConfig
	 * @return 
	 * @return Connection
	 * @throws SQLException 
	 * @date 2023-05-12 04:31:08 
	 */  
	public static EsDataSource getEsDataSource(final EsDataSourceConfig dataSourceConfig) throws SQLException
	{
		final String key = String.format("%s|%s|%s", dataSourceConfig.getUsername(), dataSourceConfig.getPassword(), dataSourceConfig.getUrl())
	            .toLowerCase();
		EsDataSource esDataSource = esDataSourceMap.get(key);
		if(esDataSource == null) {
			esDataSource = new EsDataSource();
			esDataSource.setUrl(dataSourceConfig.getUrl());
			esDataSourceMap.put(key, esDataSource);
		}
		return esDataSource;
	}
	
	/**
	*<p>Title: releaseJdbcResource</p>
	*<p>Description: 释放数据库资源</p>
	* @author caiyang
	* @param conn
	* @param stmt
	* @param rs
	*/
	public static void releaseJdbcResource(final Connection conn, final Statement stmt, final ResultSet rs) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (final SQLException ex) {
            throw new BizException(StatusCode.FAILURE,"数据库资源释放异常");
        }
    }
	
	public static void releaseJdbcResource(final Connection conn, final Statement stmt,PreparedStatement ps, final ResultSet rs) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if(ps != null) {
            	ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (final SQLException ex) {
            throw new BizException(StatusCode.FAILURE,"数据库资源释放异常");
        }
    }
	
	/**
	*<p>Title: preprocessSqlText</p>
	*<p>Description: 预处理sql，防止sql数据过多,并且处理sql中的参数</p>
	* @author caiyang
	* @param sqlText
	* @return
	 * @throws SQLException 
	*/
	public static String preprocessSqlText(String sqlText,int dataSourceType,Map<String, Object> params) throws SQLException {
        sqlText = StringUtils.stripEnd(sqlText.trim(), ";");
        Pattern paramPattern=Pattern.compile("\\$\\s*\\{(.*?)}");
		Matcher parammatcher=paramPattern.matcher(sqlText);
		while(parammatcher.find()){
			sqlText = parammatcher.replaceAll("''");
		}
		sqlText = MybatisTemplateSqlExcutor.parseSql(sqlText,params);
		System.err.println("解析后的sql:"+sqlText);
		if(DriverClassEnum.MYSQL.getCode().intValue() == dataSourceType || DriverClassEnum.POSTGRESQL.getCode().intValue() == dataSourceType
				|| DriverClassEnum.INFLUXDB.getCode().intValue() == dataSourceType)
		{
			final Pattern pattern = Pattern.compile(".*limit\\s+(\\S+?)($|;|\\s+.+)", Pattern.CASE_INSENSITIVE);
	        final Matcher matcher = pattern.matcher(sqlText);
	        if (matcher.find()) {
	            
	        }else {
	        	sqlText = sqlText + " limit 1";
	        }
	        
		}else if(DriverClassEnum.SQLSERVER.getCode().intValue() == dataSourceType)
		{
			final Pattern topPattern = Pattern.compile("top", Pattern.CASE_INSENSITIVE);
			final Matcher topMatcher = topPattern.matcher(sqlText);
			if(!topMatcher.find())
			{
				Pattern pattern = Pattern.compile("distinct", Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(sqlText);
				if(matcher.find()) {
					
				}else {
					pattern = Pattern.compile("select", Pattern.CASE_INSENSITIVE);
					matcher = pattern.matcher(sqlText);
					if (matcher.find()) {
						sqlText = matcher.replaceFirst("select top 1");
					}
				}
				
			}
		}else if(DriverClassEnum.ORACLE.getCode().intValue() == dataSourceType)
		{
			sqlText = Constants.ORACLE_START + sqlText + Constants.ORACLE_END;
		}
		System.err.println("解析后的sql:"+sqlText);
        return sqlText;
    }
	
	/**
	*<p>Title: processSqlPage</p>
	*<p>Description: sql分页处理</p>
	* @author caiyang
	* @param sqlText
	* @param pageSize
	* @param currentPage
	* @return
	*/
	public static String processSqlPage(String sqlText, int pageSize,int currentPage,int dataSourceType)
	{
		sqlText = StringUtils.stripEnd(sqlText.trim(), ";");
        final Pattern pattern = Pattern.compile("limit.*?$", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(sqlText);
        if (matcher.find()) {
            sqlText = matcher.replaceFirst("");
        }
        return sqlText + " limit " + String.valueOf((currentPage-1)*pageSize) + ","+ String.valueOf(pageSize);
	}
	
	
	/**
	*<p>Title: parseMetaDataColumns</p>
	*<p>Description: 解析sql并获取sql中的列</p>
	* @author caiyang
	* @param dataSource
	* @param sqlText
	* @return
	*/
	public static List<Map<String, Object>> parseMetaDataColumns(DataSource dataSource,String sqlText,int dataSourceType,String sqlParams,UserInfoDto userInfoDto) {
		List<Map<String, Object>> result = null;
		Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	    	conn = dataSource.getConnection();
	    	DatabaseMetaData metaData = conn.getMetaData();
	        stmt = conn.createStatement();
	        Map<String, Object> params = new HashMap<>();
	        if(StringUtil.isNotEmpty(sqlParams))
			{
				JSONArray jsonArray = JSONArray.parseArray(sqlParams);
				if(!ListUtil.isEmpty(jsonArray))
				{
					sqlText = JdbcUtils.processSqlDynamicParam(sqlText);
					for (int i = 0; i < jsonArray.size(); i++) {
						String paramType = jsonArray.getJSONObject(i).getString("paramType");
						String paramDefault = jsonArray.getJSONObject(i).getString("paramDefault");
						String paramCode = jsonArray.getJSONObject(i).getString("paramCode");
						String dateFormat = jsonArray.getJSONObject(i).getString("dateFormat");
						if(StringUtil.isNotEmpty(paramType))
						{
							if("date".equals(paramType.toLowerCase()))
							{
								params.put(paramCode, StringUtil.isNotEmpty(dateFormat)?DateUtil.getNow(dateFormat):DateUtil.getNow(DateUtil.FORMAT_LONOGRAM));
							}else if("mutiselect".equals(paramType.toLowerCase()) || "multitreeselect".equals(paramType.toLowerCase()))
							{
								params.put(paramCode, new JSONArray());
							}
							else {
								params.put(paramCode, paramDefault);
							}
						}else {
							params.put(paramCode, paramDefault);
						}
						
					}
				}
			}
	       //系统变量
			if(userInfoDto != null) {
				JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(userInfoDto));
				for (int i = 0; i < Constants.SYSTEM_PARAM.length; i++) {
					Object value = jsonObject.get(Constants.SYSTEM_PARAM[i]);
					params.put(Constants.SYSTEM_PARAM[i], value);
				}
			}
			sqlText = VelocityUtil.parseInfluxdb(sqlText, params,"influxdb");
	        rs = stmt.executeQuery(JdbcUtils.preprocessSqlText(sqlText,dataSourceType,params));
            final ResultSetMetaData rsMataData = rs.getMetaData();
            final int count = rsMataData.getColumnCount();
            result = new ArrayList<>(count);
            Map<String, Map<String, String>> tableColumnRemarks = new HashMap<String, Map<String,String>>();
            for (int i = 1; i <= count; i++) {
            	Map<String, Object> column = new HashMap<String, Object>();
            	String tableName = rsMataData.getTableName(i);
            	Map<String, String> columnComments = null;
            	if(!tableColumnRemarks.containsKey(tableName)) {
            		getColumnComments(metaData,tableName,tableColumnRemarks);
            	}
            	columnComments = tableColumnRemarks.get(tableName);
            	String columnName = rsMataData.getColumnName(i);
            	column.put("columnName", columnName);
            	column.put("name", rsMataData.getColumnLabel(i));
            	column.put("dataType", rsMataData.getColumnTypeName(i));
            	column.put("width", rsMataData.getColumnDisplaySize(i));
            	column.put("className", rsMataData.getColumnClassName(i));
            	column.put("remark", columnComments.get(columnName));
            	result.add(column);
            }
        } catch (final SQLException ex) {
        	Collection<DataSource> datasources = dataSourceMap.values();
        	while(datasources.contains(dataSource))
        	{
        		datasources.remove(dataSource);
        	}
            throw new BizException(StatusCode.FAILURE,MessageUtil.getValue("error.sql", new String[] {ex.getMessage()}));
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, rs);
        }
	    return result;
	}
	
	/**获取字段注解*/
    private static Map<String, String> getColumnComments(DatabaseMetaData metaData, String tableName,Map<String, Map<String, String>> tableColumnRemarks) throws SQLException {
        Map<String, String> columnComments = new HashMap<>();
        try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnComment = columns.getString("REMARKS");
                columnComments.put(columnName, columnComment);
            }
        }
        tableColumnRemarks.put(tableName, columnComments);
        return columnComments;
    }
	
	public static List<Map<String, Object>> parseMetaDataColumns(DataSource dataSource,String sqlText,int dataSourceType,String sqlParams,String username,String password,UserInfoDto userInfoDto) {
		List<Map<String, Object>> result = null;
		Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	    	conn = dataSource.getConnection(StringUtil.isNullOrEmpty(username)?"":username,StringUtil.isNullOrEmpty(password)?"":password);
	    	DatabaseMetaData metaData = conn.getMetaData();
	    	stmt = conn.createStatement();
	        Map<String, Object> params = new HashMap<>();
	        if(StringUtil.isNotEmpty(sqlParams))
			{
				JSONArray jsonArray = JSONArray.parseArray(sqlParams);
				if(!ListUtil.isEmpty(jsonArray))
				{
					sqlText = JdbcUtils.processSqlDynamicParam(sqlText);
					for (int i = 0; i < jsonArray.size(); i++) {
						String paramType = jsonArray.getJSONObject(i).getString("paramType");
						String paramDefault = jsonArray.getJSONObject(i).getString("paramDefault");
						String paramCode = jsonArray.getJSONObject(i).getString("paramCode");
						String dateFormat = jsonArray.getJSONObject(i).getString("dateFormat");
						if(StringUtil.isNotEmpty(paramType))
						{
							if("date".equals(paramType.toLowerCase()))
							{
								params.put(paramCode, StringUtil.isNotEmpty(dateFormat)?DateUtil.getNow(dateFormat):DateUtil.getNow(DateUtil.FORMAT_LONOGRAM));
							}else if("mutiselect".equals(paramType.toLowerCase()))
							{
								params.put(paramCode, new JSONArray());
							}
							else {
								params.put(paramCode, paramDefault);
							}
						}else {
							params.put(paramCode, paramDefault);
						}
					}
				}
			}
	      //系统变量
			if(userInfoDto != null) {
				JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(userInfoDto));
				for (int i = 0; i < Constants.SYSTEM_PARAM.length; i++) {
					Object value = jsonObject.get(Constants.SYSTEM_PARAM[i]);
					params.put(Constants.SYSTEM_PARAM[i], value);
				}
			}
	        sqlText = VelocityUtil.parseInfluxdb(sqlText, params,"influxdb");
	        rs = stmt.executeQuery(JdbcUtils.preprocessSqlText(sqlText,dataSourceType,params));
            final ResultSetMetaData rsMataData = rs.getMetaData();
            final int count = rsMataData.getColumnCount();
            result = new ArrayList<>(count);
            Map<String, Map<String, String>> tableColumnRemarks = new HashMap<String, Map<String,String>>();
            for (int i = 1; i <= count; i++) {
            	Map<String, Object> column = new HashMap<String, Object>();
            	String tableName = rsMataData.getTableName(i);
            	Map<String, String> columnComments = null;
            	if(!tableColumnRemarks.containsKey(tableName)) {
            		getColumnComments(metaData,tableName,tableColumnRemarks);
            	}
            	columnComments = tableColumnRemarks.get(tableName);
            	String columnName = rsMataData.getColumnName(i);
            	column.put("columnName", rsMataData.getColumnName(i));
            	column.put("name", rsMataData.getColumnLabel(i));
            	column.put("dataType", rsMataData.getColumnTypeName(i));
            	column.put("width", rsMataData.getColumnDisplaySize(i));
            	column.put("className", rsMataData.getColumnClassName(i));
            	column.put("remark", columnComments.get(columnName));
            	result.add(column);
            }
        } catch (final SQLException ex) {
        	Collection<DataSource> datasources = dataSourceMap.values();
        	while(datasources.contains(dataSource))
        	{
        		datasources.remove(dataSource);
        	}
            throw new BizException(StatusCode.FAILURE,MessageUtil.getValue("error.sql", new String[] {ex.getMessage()}));
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, rs);
        }
	    return result;
	}
	
	public static List<Map<String, Object>> parseMetaDataColumns(Connection conn,String sqlText,int dataSourceType,String sqlParams,UserInfoDto userInfoDto) {
		List<Map<String, Object>> result = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	        stmt = conn.createStatement();
	        DatabaseMetaData metaData = conn.getMetaData();
	        Map<String, Object> params = new HashMap<>();
	        if(StringUtil.isNotEmpty(sqlParams))
			{
				JSONArray jsonArray = JSONArray.parseArray(sqlParams);
				if(!ListUtil.isEmpty(jsonArray))
				{
					sqlText = JdbcUtils.processSqlDynamicParam(sqlText);
					for (int i = 0; i < jsonArray.size(); i++) {
						String paramType = jsonArray.getJSONObject(i).getString("paramType");
						String paramDefault = jsonArray.getJSONObject(i).getString("paramDefault");
						String paramCode = jsonArray.getJSONObject(i).getString("paramCode");
						String dateFormat = jsonArray.getJSONObject(i).getString("dateFormat");
						if("date".equals(paramType.toLowerCase()))
						{
							params.put(paramCode, StringUtil.isNotEmpty(dateFormat)?DateUtil.getNow(dateFormat):DateUtil.getNow(DateUtil.FORMAT_LONOGRAM));
						}else if("mutiselect".equals(paramType.toLowerCase()))
						{
							params.put(paramCode, new JSONArray());
						}
						else {
							params.put(paramCode, paramDefault);
						}
						
					}
				}
			}
	       //系统变量
			if(userInfoDto != null) {
				JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(userInfoDto));
				for (int i = 0; i < Constants.SYSTEM_PARAM.length; i++) {
					Object value = jsonObject.get(Constants.SYSTEM_PARAM[i]);
					params.put(Constants.SYSTEM_PARAM[i], value);
				}
			}
	        sqlText = VelocityUtil.parseInfluxdb(sqlText, params,"influxdb");
	        rs = stmt.executeQuery(JdbcUtils.preprocessSqlText(sqlText,dataSourceType,params));
            final ResultSetMetaData rsMataData = rs.getMetaData();
            final int count = rsMataData.getColumnCount();
            result = new ArrayList<>(count);
            Map<String, Map<String, String>> tableColumnRemarks = new HashMap<String, Map<String,String>>();
            for (int i = 1; i <= count; i++) {
            	Map<String, Object> column = new HashMap<String, Object>();
            	String tableName = rsMataData.getTableName(i);
            	Map<String, String> columnComments = null;
            	if(!tableColumnRemarks.containsKey(tableName)) {
            		getColumnComments(metaData,tableName,tableColumnRemarks);
            	}
            	columnComments = tableColumnRemarks.get(tableName);
            	String columnName = rsMataData.getColumnName(i);
            	column.put("columnName", columnName);
            	column.put("name", rsMataData.getColumnLabel(i));
            	column.put("dataType", rsMataData.getColumnTypeName(i));
            	column.put("width", rsMataData.getColumnDisplaySize(i));
            	column.put("className", rsMataData.getColumnClassName(i));
            	column.put("remark", columnComments.get(columnName));
            	result.add(column);
            }
        } catch (final SQLException ex) {
            throw new BizException(StatusCode.FAILURE,MessageUtil.getValue("error.sql", new String[] {ex.getMessage()}));
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, rs);
        }
	    return result;
	}
	
	public static List<Map<String, Object>> parseInfluxdbColumns(InfluxDBConnection dbConnection,String sqlText,int dataSourceType,String sqlParams,UserInfoDto userInfoDto) throws SQLException
	{
		List<Map<String, Object>> result = new ArrayList<>();
		Map<String, Object> params = new HashMap<>();
		if(StringUtil.isNotEmpty(sqlParams))
		{
			JSONArray jsonArray = JSONArray.parseArray(sqlParams);
			if(!ListUtil.isEmpty(jsonArray))
			{
				sqlText = JdbcUtils.processSqlDynamicParam(sqlText);
				for (int i = 0; i < jsonArray.size(); i++) {
					String paramType = jsonArray.getJSONObject(i).getString("paramType");
					String paramDefault = jsonArray.getJSONObject(i).getString("paramDefault");
					String paramCode = jsonArray.getJSONObject(i).getString("paramCode");
					String dateFormat = jsonArray.getJSONObject(i).getString("dateFormat");
					if(StringUtil.isNullOrEmpty(paramDefault))
					{
						throw new BizException(StatusCode.FAILURE, "当数据库为influxdb时，参数中的默认值必须填写。");
					}
					if("date".equals(paramType.toLowerCase()))
					{
						params.put(paramCode, StringUtil.isNotEmpty(dateFormat)?DateUtil.getNow(dateFormat):DateUtil.getNow(DateUtil.FORMAT_LONOGRAM));
					}else if("mutiselect".equals(paramType.toLowerCase()))
					{
						params.put(paramCode, new JSONArray());
					}
					else {
						params.put(paramCode, paramDefault);
					}
				}
			}
		}
		//系统变量
		if(userInfoDto != null) {
			JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(userInfoDto));
			for (int i = 0; i < Constants.SYSTEM_PARAM.length; i++) {
				Object value = jsonObject.get(Constants.SYSTEM_PARAM[i]);
				params.put(Constants.SYSTEM_PARAM[i], value);
			}
		}
		sqlText = VelocityUtil.parseInfluxdb(sqlText, params,"influxdb");
		String sql = JdbcUtils.preprocessSqlText(sqlText,dataSourceType,params);
		QueryResult queryResult = dbConnection.query(sql);
		if(queryResult.getResults().get(0).getSeries() != null)
		{
			List<String> columns = queryResult.getResults().get(0).getSeries().get(0).getColumns();
			if(!ListUtil.isEmpty(columns))
			{
				for (int i = 0; i < columns.size(); i++) {
					Map<String, Object> column = new HashMap<String, Object>();
	            	column.put("columnName", columns.get(i));
	            	column.put("name", columns.get(i));
	            	result.add(column);
				}
			}
		}
		return result;
	}
	
	public static List<Map<String, Object>> parseProcedureColumns(DataSource dataSource,String sqlText,int dataSourceType,JSONArray inParams,JSONArray outParams,UserInfoDto userInfoDto){
		List<Map<String, Object>> result = null;
		Connection conn = null;
		CallableStatement cstm = null;
	    ResultSet rs = null;
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
	    	//系统变量
	    	Map<String, Object> systemParams = new HashMap<>();
			if(userInfoDto != null) {
				JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(userInfoDto));
				for (int i = 0; i < Constants.SYSTEM_PARAM.length; i++) {
					Object value = jsonObject.get(Constants.SYSTEM_PARAM[i]);
					systemParams.put("paramDefault", value);
				}
			}
	    	if(!ListUtil.isEmpty(inParams))
    		{
	    		JSONObject jsonObject = null;
    			for (int i = 0; i < inParams.size(); i++) {
    				jsonObject = (JSONObject) inParams.get(i);
    				String paramCode = jsonObject.getString("paramCode");
    				if(systemParams.containsKey(paramCode) && StringUtil.isNullOrEmpty(jsonObject.getString("paramDefault"))) {
    					//系统参数如果没有设置默认值，则设置默认值为当前登录用户的信息
    					jsonObject.put("paramDefault", systemParams.get(paramCode));
    				}
    				//数值类的参数，没有默认值，不输入的时候传0，因为不支持传null
    				if(InParamTypeEnum.INT.getCode().equals(jsonObject.getString("paramType")))
    				{
    					if(StringUtil.isNotEmpty(jsonObject.getString("paramDefault"))) {
    						cstm.setInt(i+1, Integer.valueOf(String.valueOf(jsonObject.get("paramDefault"))));
    					}else {
    						cstm.setInt(i+1, 0);
    					}
    				}else if(InParamTypeEnum.STRING.getCode().equals(jsonObject.getString("paramType")))
    				{
    					if(StringUtil.isNotEmpty(jsonObject.getString("paramDefault"))) {
    						cstm.setString(i+1, String.valueOf(jsonObject.get("paramDefault")));
    					}else {
    						cstm.setString(i+1, null);
    					}
    				}else if(InParamTypeEnum.LONG.getCode().equals(jsonObject.getString("paramType")))
    				{
    					if(StringUtil.isNotEmpty(jsonObject.getString("paramDefault"))) {
    						cstm.setLong(i+1, Long.valueOf(String.valueOf(jsonObject.get("paramDefault"))));
    					}else {
    						cstm.setLong(i+1, 0);
    					}
    				}else if(InParamTypeEnum.DOUBLE.getCode().equals(jsonObject.getString("paramType")))
    				{
    					if(StringUtil.isNotEmpty(jsonObject.getString("paramDefault"))) {
    						cstm.setDouble(i+1, Double.valueOf(String.valueOf(jsonObject.get("paramDefault"))));
    					}else {
    						cstm.setDouble(i+1, 0);
    					}
    				}else if(InParamTypeEnum.FLOAT.getCode().equals(jsonObject.getString("paramType")))
    				{
    					if(StringUtil.isNotEmpty(jsonObject.getString("paramDefault"))) {
    						cstm.setFloat(i+1, Float.valueOf(String.valueOf(jsonObject.get("paramDefault"))));
    					}else {
    						cstm.setFloat(i+1, 0);
    					}
    				}else if(InParamTypeEnum.BIGDECIMAL.getCode().equals(jsonObject.getString("paramType")))
    				{
    					if(StringUtil.isNotEmpty(jsonObject.getString("paramDefault"))) {
    						cstm.setBigDecimal(i+1, new BigDecimal(String.valueOf(jsonObject.get("paramDefault"))));
    					}else {
    						cstm.setBigDecimal(i+1, new BigDecimal(0));
    					}
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
    					String defaultDate = "";
    					try {
    						defaultDate = DateUtil.getDefaultDate(String.valueOf(jsonObject.get("paramDefault")),dateFormat);
						} catch (Exception e) {
							defaultDate = DateUtil.getNow(DateUtil.FORMAT_LONOGRAM);
						}
    					cstm.setDate(i+1, DateUtil.string2SqlDate(defaultDate,dateFormat));
    				}else if(InParamTypeEnum.DATETIME.getCode().equals(jsonObject.getString("paramType")))
    				{
    					String defaultDate = "";
    					try {
    						defaultDate = DateUtil.getDefaultDate(String.valueOf(jsonObject.get("paramDefault")),DateUtil.FORMAT_FULL);
						} catch (Exception e) {
							defaultDate = DateUtil.getNow(DateUtil.FORMAT_FULL);
						}
    					cstm.setTimestamp(i+1, DateUtil.string2SqlTimestamp(defaultDate,DateUtil.FORMAT_FULL));
    				}
				}
    		}
	    	if(ListUtil.isEmpty(outParams))
	    	{	//如果返回值为空，则说明存储过程会返回字段，需要解析
	    		rs = cstm.executeQuery();
    			final ResultSetMetaData rsMataData = rs.getMetaData();
                final int count = rsMataData.getColumnCount();
                result = new ArrayList<>();
                for (int i = 1; i <= count; i++) {
                	Map<String, Object> column = new HashMap<String, Object>();
                	column.put("columnName", rsMataData.getColumnName(i));
                	column.put("name", rsMataData.getColumnLabel(i));
                	column.put("dataType", rsMataData.getColumnTypeName(i));
                	column.put("width", rsMataData.getColumnDisplaySize(i));
                	column.put("className", rsMataData.getColumnClassName(i));
                	result.add(column);
                }                   
	    	}else {
	    		//如果返回值不为空，则直接使用返回值
	    		 result = new ArrayList<>();
	    		for (int i = 0; i < outParams.size(); i++) {
	    			JSONObject jsonObject = (JSONObject) outParams.get(i);
	    			Map<String, Object> column = new HashMap<String, Object>();
                	column.put("columnName", jsonObject.get("paramCode"));
                	column.put("name", jsonObject.get("paramCode"));
                	column.put("dataType", jsonObject.get("paramType"));
                	result.add(column);
	    		}
	    	}
            
        } catch (final SQLException ex) {
        	Collection<DataSource> datasources = dataSourceMap.values();
        	while(datasources.contains(dataSource))
        	{
        		datasources.remove(dataSource);
        	}
            throw new BizException(StatusCode.FAILURE,MessageUtil.getValue("error.sql", new String[] {ex.getMessage()}));
        } finally {
            JdbcUtils.releaseJdbcResource(conn, cstm, rs);
        }
	    return result;
	}
	
	/**  
	 * @Title: processSqlDynamicParam
	 * @Description: 处理sql中的动态参数，去掉空格
	 * @param sql
	 * @return
	 * @author caiyang
	 * @date 2021-06-25 07:11:37 
	 */ 
	public static String processSqlDynamicParam(String sqlText)
	{
		List<String> matchGroup = new ArrayList<String>();
		Pattern paramPattern=Pattern.compile("\\$\\s*\\{(.*?)}");
		Matcher parammatcher=paramPattern.matcher(sqlText);
		while(parammatcher.find()){
			matchGroup.add(parammatcher.group());
		}
		if(!ListUtil.isEmpty(matchGroup))
		{
			for (int i = 0; i < matchGroup.size(); i++) {
				String original = matchGroup.get(i);
				String newParam = matchGroup.get(i).replaceAll(" ", "");
				sqlText = sqlText.replace(original, newParam);
			}
		}
		return sqlText;
	}
	
	/**  
	 * @Title: processSqlParams
	 * @Description: 处理sql中的参数
	 * @param sql
	 * @param params
	 * @param updateTime 用来处理旧数据，当updateTime等于null时说明是旧数据
	 * @return
	 * @author caiyang
	 * @throws JSQLParserException 
	 * @date 2021-11-01 02:59:05 
	 */ 
	public static String processSqlParams(String sql,Map<String, Object> params) throws JSQLParserException {
		if(params != null)
		{
			try {
				sql = MybatisTemplateSqlExcutor.parseSql(sql, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.err.println("解析后的sql："+sql);
	    return sql;
	} 
	
	/**  
	 * @MethodName: processInfluxdbSqlParams
	 * @Description: 处理Influxdb sql中的参数
	 * @author caiyang
	 * @param sql
	 * @param params
	 * @return
	 * @throws JSQLParserException 
	 * @return String
	 * @date 2022-12-15 01:47:17 
	 */  
	public static String processInfluxdbSqlParams(String sql,Map<String, Object> params) throws JSQLParserException {
		if(params != null)
		{
			sql = JdbcUtils.processSqlDynamicParam(sql);
			sql = VelocityUtil.parseInfluxdb(sql, params,"influxdb");
//			sql = JsqlparserUtil.formatSql(sql);
//			Expression where = JsqlparserUtil.getwhere(sql);
//		    JsqlparserUtil jsqlparserUtil = new JsqlparserUtil();
//		    List<SqlWhereDto> sqlWhereDtos = jsqlparserUtil.getWhereList(where);
//		    if(!ListUtil.isEmpty(sqlWhereDtos))
//		    {
//		    	String newWhere = where.toString();
//		    	for (int j = 0; j < sqlWhereDtos.size(); j++) {
//					if(YesNoEnum.YES.getCode().intValue() == sqlWhereDtos.get(j).getIsReplace().intValue())
//					{
//						newWhere = newWhere.replace(sqlWhereDtos.get(j).getExpression(), sqlWhereDtos.get(j).getReplaceExpression());
//					}
//				}
//		    	newWhere = newWhere.replaceAll("'' = ''", "1 = 1");
//		    	sql = sql.replace(where.toString(), newWhere);
//		    }
		}
	    return sql;
	} 
	
	/**  
	 * @Title: getPaginationSql
	 * @Description: sql添加分页条件
	 * @param sql
	 * @param dataSourceType
	 * @param pageCount
	 * @param currentPage
	 * @return
	 * @author caiyang
	 * @throws JSQLParserException 
	 * @date 2021-11-08 04:13:35 
	 */ 
	public static String getPaginationSql(String sql,int dataSourceType,int pageCount,int currentPage) throws JSQLParserException {
		sql = sql.replaceAll(";", "");
		if(DriverClassEnum.MYSQL.getCode().intValue() == dataSourceType || DriverClassEnum.DAMENG.getCode().intValue() == dataSourceType || DriverClassEnum.DORIS.getCode().intValue() == dataSourceType)
		{
			sql = sql + " limit " + (currentPage-1)*pageCount + "," + pageCount;
		}else if(DriverClassEnum.ORACLE.getCode().intValue() == dataSourceType)
		{
			sql = "select tmp_page.*, rownum rowno from (" + sql + ") tmp_page";
			sql = "select * from (" + sql + ") where rowno <= " + currentPage*pageCount + " and rowno > " + (currentPage-1)*pageCount;
		}else if(DriverClassEnum.SQLSERVER.getCode().intValue() == dataSourceType) 
		{
			sql = "select row_number () over (order by rand()) page_row_number,* from (" + sql + ") as page_table_alias";
			sql = "select top "+pageCount + " * from (" + sql + ") as page_table_alias where page_row_number > " + (currentPage-1)*pageCount + "order by page_row_number";
		}else if(DriverClassEnum.POSTGRESQL.getCode().intValue() == dataSourceType || DriverClassEnum.KINGBASE.getCode().intValue() == dataSourceType || DriverClassEnum.HIGODB.getCode().intValue() == dataSourceType) {
			sql = sql + " limit " + pageCount + " offset " + (currentPage-1)*pageCount;
		}else if(DriverClassEnum.INFLUXDB.getCode().intValue() == dataSourceType || DriverClassEnum.TDENGINE.getCode().intValue() == dataSourceType) {
			sql = sql + " limit " + pageCount + " offset " + (currentPage-1)*pageCount;
		}
		
		return sql;
	}
	
	public static String getPaginationSql(String sql,int dataSourceType,int pageCount,int startPage,int endPage) throws JSQLParserException {
		sql = sql.replaceAll(";", "");
		if(DriverClassEnum.MYSQL.getCode().intValue() == dataSourceType || DriverClassEnum.DAMENG.getCode().intValue() == dataSourceType)
		{
			sql = sql + " limit " + (endPage-startPage+1)*pageCount + " offset " + (startPage-1)*pageCount;
		}else if(DriverClassEnum.ORACLE.getCode().intValue() == dataSourceType)
		{
			sql = "select tmp_page.*, rownum rowno from (" + sql + ") tmp_page";
			sql = "select * from (" + sql + ") where rowno <= " + (endPage-startPage+1)*pageCount + " and rowno > " + (startPage-1)*pageCount;
		}else if(DriverClassEnum.SQLSERVER.getCode().intValue() == dataSourceType) 
		{
			sql = "select row_number () over (order by rand()) page_row_number,* from (" + sql + ") as page_table_alias";
			sql = "select top "+(endPage-startPage+1)*pageCount + " * from (" + sql + ") as page_table_alias where page_row_number > " + (startPage-1)*pageCount + "order by page_row_number";
		}else if(DriverClassEnum.POSTGRESQL.getCode().intValue() == dataSourceType || DriverClassEnum.KINGBASE.getCode().intValue() == dataSourceType) {
			sql = sql + " limit " + (endPage-startPage+1)*pageCount + " offset " + (startPage-1)*pageCount;
		}else if(DriverClassEnum.INFLUXDB.getCode().intValue() == dataSourceType || DriverClassEnum.TDENGINE.getCode().intValue() == dataSourceType) {
			sql = sql + " limit " + (endPage-startPage+1)*pageCount + " offset " + (startPage-1)*pageCount;
		}
		
		return sql;
	}
	
	/**  
	 * @Title: getCountSql
	 * @Description: 查询结果的总数sql
	 * @param sql
	 * @param pageCount
	 * @param currentPage
	 * @return
	 * @author caiyang
	 * @date 2021-11-08 04:37:05 
	 */ 
	public static String getCountSql(String sql)
	{
		sql = sql.replaceAll(";", "");
		sql = "select count(*) from (" + sql + ") alis_t";
		return sql;
	}
	
	/**  
	 * @MethodName: getInfluxdbCountSql
	 * @Description: influxdb查询结果的总数sql
	 * @author caiyang
	 * @param sql
	 * @return 
	 * @return String
	 * @date 2022-12-15 02:07:57 
	 */  
	public static String getInfluxdbCountSql(String sql)
	{
		sql = sql.replaceAll(";", "");
		sql = "select count(*) from (" + sql + ")";
		return sql;
	}
	
	/**  
	 * @Title: processPageParams
	 * @Description: 处理页面传入的参数
	 * @param params
	 * @param tplParams
	 * @author caiyang
	 * @date 2021-11-26 08:08:27 
	 */ 
	public static Map<String, Object> processPageParams(Map<String, Object> pageParams,String tplParams)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		if(StringUtil.isNotEmpty(tplParams))
		{
			JSONArray jsonArray = JSONObject.parseArray(tplParams);
			if(!ListUtil.isEmpty(jsonArray))
			{
				for (int i = 0; i < jsonArray.size(); i++) {
					if(jsonArray.getJSONObject(i).get("paramCode") != null && StringUtil.isNotEmpty(String.valueOf(jsonArray.getJSONObject(i).get("paramCode"))))
					{
						if(pageParams != null)
						{
							if(pageParams.containsKey(jsonArray.getJSONObject(i).get("paramCode")))
							{
								result.put(String.valueOf(jsonArray.getJSONObject(i).get("paramCode")), pageParams.get(jsonArray.getJSONObject(i).get("paramCode")));
							}else {
								if(jsonArray.getJSONObject(i).get("paramDefault") != null && StringUtil.isNotEmpty(String.valueOf(jsonArray.getJSONObject(i).get("paramDefault"))))
								{
									result.put(String.valueOf(jsonArray.getJSONObject(i).get("paramCode")), String.valueOf(jsonArray.getJSONObject(i).get("paramDefault")));
								}else {
									result.put(String.valueOf(jsonArray.getJSONObject(i).get("paramCode")), "");
								}
							}
						}else if(jsonArray.getJSONObject(i).get("paramDefault") != null && StringUtil.isNotEmpty(String.valueOf(jsonArray.getJSONObject(i).get("paramDefault"))))
						{
							result.put(String.valueOf(jsonArray.getJSONObject(i).get("paramCode")), String.valueOf(jsonArray.getJSONObject(i).get("paramDefault")));
						}
					}
				}
			}
		}
		return result;
	}
	
	/**  
	 * @Title: connectionTest
	 * @Description: 连接测试
	 * @param dataSourceConfig
	 * @return
	 * @author caiyang
	 * @date 2022-04-24 02:10:41 
	 */ 
	public static boolean connectionTest(DataSourceConfig dataSourceConfig) {
		boolean result = false;
		try {
			Class.forName(dataSourceConfig.getDriverClass());//加载驱动类
			Connection conn=DriverManager.getConnection(dataSourceConfig.getJdbcUrl(),dataSourceConfig.getUser(),dataSourceConfig.getPassword());//用参数得到连接对象
			result = true;
			conn.close();
		} catch (Exception e) {
			throw new BizException(StatusCode.FAILURE, "数据库连接测试失败，失败原因："+e.getMessage());
		}
		return result;
	}
	
	/**  
	 * @MethodName: parseDatabaseTables
	 * @Description: 解析数据库中的表
	 * @author caiyang
	 * @param dataSourceConfig
	 * @return 
	 * @return List<Map<String,String>>
	 * @date 2022-11-15 08:03:41 
	 */  
	public static List<Map<String, String>> parseDatabaseTables(DataSourceConfig dataSourceConfig){
		List<Map<String, String>> result = new ArrayList<>();
		try {
			if (dataSourceConfig.getDriverClass().equals(DriverClassEnum.INFLUXDB.getName())) {
				String url = dataSourceConfig.getJdbcUrl().substring(0, dataSourceConfig.getJdbcUrl().lastIndexOf("/"));
				String databaseName = dataSourceConfig.getJdbcUrl().substring(dataSourceConfig.getJdbcUrl().lastIndexOf("/") + 1, dataSourceConfig.getJdbcUrl().length());
				// 创建InfluxDB连接
				InfluxDB influxDB = InfluxDBFactory.connect(url, dataSourceConfig.getUser(), dataSourceConfig.getPassword());
				influxDB.setDatabase(databaseName);
				// 执行查询以获取所有表（measurements）
				String queryStr = "SHOW MEASUREMENTS";
				Query query = new Query(queryStr, databaseName);
				QueryResult queryResult = influxDB.query(query);
				// 处理查询结果
				if (queryResult.hasError()) {
					System.out.println("Error: " + queryResult.getError());
				} else {
					List<QueryResult.Result> results = queryResult.getResults();
					for (QueryResult.Result re : results) {
						List<QueryResult.Series> seriesList = re.getSeries();
						for (QueryResult.Series series : seriesList) {
							List<List<Object>> values = series.getValues();
							for (List<Object> value : values) {
								Map<String, String> map = new HashMap<>();
								map.put("name", (String) value.get(0));
								map.put("value", (String) value.get(0));
								result.add(map);
							}
						}
					}
				}
				// 关闭连接
				influxDB.close();
			}else{
				Class.forName(dataSourceConfig.getDriverClass());//加载驱动类
				Connection conn=DriverManager.getConnection(dataSourceConfig.getJdbcUrl(),dataSourceConfig.getUser(),dataSourceConfig.getPassword());//用参数得到连接对象
				DatabaseMetaData metaData = conn.getMetaData();
				String schema = null;
				if(!DriverClassEnum.SQLSERVER.getName().equals(dataSourceConfig.getDriverClass())) {
					schema = conn.getSchema();
				}
				ResultSet tables = metaData.getTables(conn.getCatalog(), schema, null, new String[]{"TABLE"});
				while (tables.next()) {
					Map<String, String> map = new HashMap<>();
					map.put("name", tables.getString("TABLE_NAME"));
					map.put("value", tables.getString("TABLE_NAME"));
					result.add(map);
				}
			}
		} catch (Exception e) {
			throw new BizException(StatusCode.FAILURE, "数据库连接测试失败，失败原因："+e.getMessage());
		}
		return result;
	}
	
	/**  
	 * @MethodName: influxdbTest
	 * @Description: influxdb链接测试
	 * @author caiyang
	 * @param username
	 * @param password
	 * @param openurl
	 * @param database
	 * @param retentionPolicy
	 * @return 
	 * @return boolean
	 * @date 2022-12-06 09:14:50 
	 */  
	public static boolean influxdbTest(String username, String password, String openurl, String database,
			String retentionPolicy) {
		InfluxDBConnection influxDBConnection = new InfluxDBConnection(username, password, openurl, database, retentionPolicy);
		boolean result = influxDBConnection.ping();
		return result;
	}
	
	/**  
	 * @MethodName: esTest
	 * @Description: elasticsearch测试连接
	 * @author caiyang
	 * @param url
	 * @param userName
	 * @param password
	 * @return 
	 * @return boolean
	 * @date 2023-05-12 03:00:44 
	 */  
	public static boolean esTest(String url,String userName,String password)
	{
		boolean result = true;
		EsDataSource esDataSource = new EsDataSource();
		esDataSource.setUrl(url);
		try {
			esDataSource.getConnection(StringUtil.isNullOrEmpty(userName)?"":userName,StringUtil.isNullOrEmpty(password)?"":password);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	/**  
	 * @MethodName: getTDengineConnection
	 * @Description: 获取TDengineConnection
	 * @author caiyang
	 * @param config
	 * @return
	 * @throws Exception 
	 * @return Connection
	 * @date 2023-05-16 07:40:22 
	 */  
	public static TDengineConnection getTDengineConnection(final TDengineConfig  config) throws Exception{
		TDengineConnection tDengineConnection = new TDengineConnection(config.getJdbcUrl(), config.getUsername(), config.getPassword());
		return tDengineConnection;
	}
	
	public static Map<String, String> parseMetaDataColumnsJavaType(DataSource dataSource,String sqlText,int dataSourceType) {
		Map<String, String> result = new HashMap<>();
		Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	    	conn = dataSource.getConnection();
	        stmt = conn.createStatement();
			sqlText = VelocityUtil.parseInfluxdb(sqlText, null,"influxdb");
	        rs = stmt.executeQuery(JdbcUtils.preprocessSqlText(sqlText,dataSourceType,null));
            final ResultSetMetaData rsMataData = rs.getMetaData();
            final int count = rsMataData.getColumnCount();
            for (int i = 1; i <= count; i++) {
            	result.put(rsMataData.getColumnName(i), rsMataData.getColumnClassName(i));
            }
        } catch (final SQLException ex) {
        	Collection<DataSource> datasources = dataSourceMap.values();
        	while(datasources.contains(dataSource))
        	{
        		datasources.remove(dataSource);
        	}
            throw new BizException(StatusCode.FAILURE,MessageUtil.getValue("error.sql", new String[] {ex.getMessage()}));
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, rs);
        }
	    return result;
	}
	
	public static void main(String[] args) {
//		String sql = "select  D.CUSTOMER_CODE AS 客户编码 ,D.CUSTOMER_NAME AS  客户名称,G.ADMIN_UNIT_NAME AS 部门, F.EMPLOYEE_NAME as 业务员, E.ITEM_CODE AS 品号,B.ITEM_DESCRIPTION AS 品名,B.ITEM_SPECIFICATION AS 规格型号,\n" + 
//				"cast(sum(B.PRICE_QTY) as float) AS 数量,  cast(sum(B.AMOUNT_UNINCLUDE_TAX_OC+B.TAX_OC) as float) as 原币金额 ,  \n" + 
//				"cast(sum(B.AMOUNT_UNINCLUDE_TAX_BC+B.TAX_BC) as float) as 本币金额,\n" + 
//				"A.DOC_NO as 销货单号,A.UDF022 AS 出口目的国,max(A.TRANSACTION_DATE) as 交易日期 from SALES_DELIVERY A \n" + 
//				"left join SALES_DELIVERY_D B on A.SALES_DELIVERY_ID = B.SALES_DELIVERY_ID \n" + 
//				"left join CUSTOMER D on A.CUSTOMER_ID = D.CUSTOMER_BUSINESS_ID\n" + 
//				"LEFT JOIN  ITEM E on B.ITEM_ID = E.ITEM_BUSINESS_ID \n" + 
//				"LEFT JOIN   EMPLOYEE F  ON  A.Owner_Emp = F.EMPLOYEE_ID \n" + 
//				"LEFT JOIN ADMIN_UNIT G ON A.Owner_Dept = G.ADMIN_UNIT_ID \n" + 
//				" where  B.ITEM_ID<>'A6F1E65B-D51F-4290-E9CD-13C9C63D584E'  and A.TRANSACTION_DATE>=$ {startdate} and A.TRANSACTION_DATE <=${enddate}  \n" + 
//				" group by D.CUSTOMER_CODE,D.CUSTOMER_NAME,G.ADMIN_UNIT_NAME,F.EMPLOYEE_NAME,E.ITEM_CODE,B.ITEM_DESCRIPTION,B.ITEM_SPECIFICATION,A.DOC_NO,A.UDF022\n" + 
//				"order by DOC_NO DESC";
////		JdbcUtils.processSqlDynamicParam(sql);
//		JdbcUtils.preprocessSqlText(sql,3);
		//数据源配置
//		DataSourceConfig dataSourceConfig = new DataSourceConfig(1L, "com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/report?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true", "root", "root", null);
//		//获取数据源
//		DataSource dataSource = JdbcUtils.getDataSource(dataSourceConfig);
//		JdbcUtils.parseProcedureColumns(dataSource, "call next4()",1,null,null);
		int pageCount = 10;
		int currentPage = 2;
		String sql = "SELECT * FROM test";
		sql = "select row_number () over (order by rand()) page_row_number,* from (" + sql + ") as page_table_alias";
		sql = "select top "+pageCount + " * from (" + sql + ") as page_table_alias where page_row_number > " + (currentPage-1)*pageCount + " order by page_row_number";
		System.err.println(101/pageCount+1);
	 }
	
	
}
