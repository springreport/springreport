package com.springreport.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;

public class MybatisTemplateSqlExcutor {

    /**  
     * @Fields SCRIPT_TEMPLATE : 脚本模板
     * @author caiyang
     * @date 2023-02-20 10:32:14 
     */  
    private static final String SCRIPT_TEMPLATE = "<script>\n%s\n</script>";
    
    /**  
     * @Fields EMPTY_XML : 空MP配置模板，用于构建MP环境配置，（放这里是由于博客的编辑器识别问题，会导致高亮错误）
     * @author caiyang
     * @date 2023-02-20 10:33:34 
     */  
    private static final String EMPTY_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" //
            + "<!DOCTYPE configuration\r\n" //
            + " PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\"\r\n" //
            + " \"http://mybatis.org/dtd/mybatis-3-config.dtd\">\r\n"//
            + "<configuration>\r\n" //
            + "</configuration>";
    
    /**
     * MP环境配置
     */
    private static Configuration configuration;
    
    static {
    	  InputStream inputStream = new ByteArrayInputStream(EMPTY_XML.getBytes(StandardCharsets.UTF_8));
          XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(inputStream, null, null);
          configuration = xmlConfigBuilder.parse();
    }
    
    /**
     * 从MP复制过来的脚本解析方法
     */
    private static SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType) {
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }
    
    /**  
     * @MethodName: parseSql
     * @Description: 解析sql
     * @author caiyang
     * @param sqlTemplate
     * @param params
     * @return
     * @throws SQLException 
     * @return String
     * @date 2023-02-20 11:11:27 
     */  
    public static String parseSql(String sqlTemplate, Map<String, Object> params) throws SQLException {
    	 String script = String.format(SCRIPT_TEMPLATE, sqlTemplate);
    	 XPathParser parser = new XPathParser(script, false, new Properties(), new XMLMapperEntityResolver());
    	 SqlSource source = createSqlSource(configuration, parser.evalNode("/script"), Map.class);
    	 BoundSql boundSql = source.getBoundSql(params);
    	 List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
         Object parameterObject = boundSql.getParameterObject();
         String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
         if(!ListUtil.isEmpty(parameterMappings))
         {
        	 MetaObject metaObject = configuration.newMetaObject(parameterObject);
        	 for (ParameterMapping parameterMapping : parameterMappings)
        	 {
        		 String propertyName = parameterMapping.getProperty();
        		 if (metaObject.hasGetter(propertyName))
        		 {
        			 Object obj = metaObject.getValue(propertyName);
        		     sql = sql.replaceFirst("\\?",Matcher.quoteReplacement(getParameterValue(obj)));
        		 }else if (boundSql.hasAdditionalParameter(propertyName))
        	     {
        		      // 该分支是动态sql
        		      Object obj = boundSql.getAdditionalParameter(propertyName);
        		      sql = sql.replaceFirst("\\?",Matcher.quoteReplacement(getParameterValue(obj)));
        		 }else
        		 {// 打印出缺失，提醒该参数缺失并防止错位
        		      sql = sql.replaceFirst("\\?", "缺失");
        		 }
        	 }
         }
         return sql;
    }
    
	/**  
	 * @MethodName: getParameterValue
	 * @Description: 获取参数值
	 * @author caiyang
	 * @param obj
	 * @return 
	 * @return String
	 * @date 2023-02-20 11:11:06 
	 */  
	private static String getParameterValue(Object obj) {
		String value = null;
		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format(new Date()) + "'";
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "";
			}
		}
		return value;
	}

	public static void main(String[] args) throws SQLException {
		String queryJson = "<if test=\"age!=null and age!=''\">\r\n"
				+ "  {\"age\": {$gt: #{age}}\r\n"
				+ "}</if>";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("age", null);
		String sql = parseSql(queryJson, param);
		System.err.println(sql);
	}
}
