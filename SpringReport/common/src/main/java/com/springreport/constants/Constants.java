/*   
 * Copyright (c) 2016-2020 canaanQD. All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * ISCAS. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with canaanQD.   
 *   
 */

package com.springreport.constants;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: 系统常量类
 * @author 蔡阳
 * @date 2018年4月24日17:47:40
 * @version V1.0
 */
public class Constants {
	
	/**
	 * 操作类型，用于操作日志时存储
	 */
	public static final String OPERATE_TYPE_ADD = "Add";
	public static final String OPERATE_TYPE_UPDATE = "Update";
	public static final String OPERATE_TYPE_DELETE = "Delete";
	public static final String OPERATE_TYPE_BATCHDELETE = "BatchDelete";
	public static final String OPERATE_TYPE_SEARCH = "Search";
	public static final String OPERATE_TYPE_PAGEINIT = "PageInit";
	public static final String OPERATE_TYPE_LOGIN = "Login";
	public static final String OPERATE_TYPE_LOGOUT = "Logout";
	public static final String OPERATE_TYPE_REMOTE = "Remote";
	public static final String OPERATE_TYPE_SOCKET = "Socket";
	
	/**
	 * 	默认密码
	 */
	public static final String DEFALUT_PASSWORD = "123456";
	
	/**
	 * 	管理员角色名称
	 */
	public static final String ROLE_ADMIN_NAME = "系统管理员";
	
	/**
     * @Fields PREMISSION_STRING : 登录并授权访问权限
     * @author caiyang
     * @date 2021年6月18日07:08:12
     */
    public static final String  PREMISSION_STRING = "jwt,perms[{0}]";

    /**
     * @Fields PUBLIC_STRING : 公开访问权限
     * @author caiyang
     * @date 2021年6月18日07:08:17
     */
    public static final String  PUBLIC_STRING = "anon";

    /**
     * @Fields JWT_STRING : 登陆访问权限
     * @author caiyang
     * @date 2021年6月18日07:08:21
     */
    public static final String  JWT_STRING = "jwt";
    
    /**  
     * @Fields ORACLE_START : oracle查询一条数据前半部分
     * @author caiyang
     * @date 2021-11-30 09:27:05 
     */ 
    public static final String ORACLE_START = "SELECT * FROM (";
    
    /**  
     * @Fields ORACLE_END : oracle查询一条数据后半部分
     * @author caiyang
     * @date 2021-11-30 09:27:21 
     */ 
    public static final String ORACLE_END = ") WHERE ROWNUM = 1";
    
    /**  
     * @Fields HEART_BEAT : socket心跳检测标识
     * @author caiyang
     * @date 2021-11-30 09:28:28 
     */ 
    public static final String SOCKET_HEART_BEAT = "socketHeartBeat";
    
    /**  
     * @Fields DEFAULT_FONT_FAMILY : luckysheet单元格默认字体格式
     * @author caiyang
     * @date 2022-02-14 04:19:50 
     */ 
    public static final String DEFAULT_FONT_FAMILY = "Times New Roman";
    
    /**  
     * @Fields DEFAULT_FONT_COLOR : luckysheet单元格默认字体颜色
     * @author caiyang
     * @date 2022-02-14 04:48:14 
     */ 
    public static final String DEFAULT_FONT_COLOR = "#000000";
    
    /**  
     * @Fields DEFAULT_FONT_SIZE : 默认字体大小
     * @author caiyang
     * @date 2022-02-15 07:13:17 
     */ 
    public static final String DEFAULT_FONT_SIZE = "10";
    
    /**  
     * @Fields CURRENT_DATE : 当前日期用字符串
     * @author caiyang
     * @date 2022-04-01 09:36:02 
     */ 
    public static final String CURRENT_DATE = "current";
    
    /**  
     * @Fields DEFALUT_CELL_ATTRS : 填报单元格默认属性
     * @author caiyang
     * @date 2022-12-08 02:09:42 
     */  
    public static final String DEFALUT_CELL_ATTRS = "{\"cellExtend\":\"1\", \"aggregateType\":\"list\", \"digit\":\"2\", \"cellFunction\":\"\", \"dataFrom\":1, \"dependencyCell\":\"\", \"valueType\":\"1\", \"require\":false,\"allowEdit\":true, \"lengthValid\":false, \"minLength\":\"\", \"maxLength\":\"\", \"textValidRule\":\"0\", \"regex\":\"\", \"numberRangeValid\":false, \"minValue\":\"\", \"maxValue\":\"\", \"digit\":0, \"dateFormat\":\"yyyy-MM-dd\", \"datasourceId\":\"\", \"dictType\":\"\" }";

    /**  
     * @Fields IMG_EXTENTIONS : 常见图片文件扩展名
     * @author caiyang
     * @date 2023-01-06 10:54:42 
     */  
    public static final String IMG_EXTENTIONS = "jpg,jpeg,png,bmp";
    
    /**  
     * @Fields DEFAULT_IMG_INFO : 报表图片默认信息
     * @author caiyang
     * @date 2023-01-06 11:26:11 
     */  
    public static final String DEFAULT_IMG_INFO = "{\"type\":\"1\",\"src\":\"\",\"originWidth\":300,\"originHeight\":200,\"default\":{\"width\":74,\"height\":20,\"left\":74,\"top\":100},\"crop\":{\"width\":74,\"height\":20,\"offsetLeft\":0,\"offsetTop\":0},\"isFixedPos\":false,\"fixedLeft\":120,\"fixedTop\":190,\"border\":{\"width\":0,\"radius\":0,\"style\":\"solid\",\"color\":\"#000\"}}";

    /**  
     * @Fields DEFAULT_LUCKYSHEET_CELL_WIDTH : luckysheet单元格默认宽度
     * @author caiyang
     * @date 2023-01-06 02:30:07 
     */  
    public static final double DEFAULT_LUCKYSHEET_CELL_WIDTH = 74;
    
    /**  
     * @Fields DEFAULT_LUCKYSHEET_CELL_HEIGHT : luckysheet单元格默认高度
     * @author caiyang
     * @date 2023-01-06 02:30:39 
     */  
    public static final double DEFAULT_LUCKYSHEET_CELL_HEIGHT = 20;
    
    /**  
     * @Fields compareLua : 比较大小lua脚本
     * @author caiyang
     * @date 2023-02-13 11:04:19 
     */  
    public static final String COMPARELUA = "local version = tonumber(redis.call('get', KEYS[1]) or '0') local arg=tonumber(ARGV[1]) if version<arg then redis.call('set',KEYS[1],arg) return 1 else return 0 end";
    /**  
     * @Fields EQUALLUA : 比较相等lua脚本
     * @author caiyang
     * @date 2023-02-13 11:05:06 
     */  
    public static final String EQUALLUA = "if redis.call('get', KEYS[1])==ARGV[1] then return 1 else return 0 end";
    
    public static final String EMPTY_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" //
            + "<!DOCTYPE configuration\r\n" //
            + " PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\"\r\n" //
            + " \"http://mybatis.org/dtd/mybatis-3-config.dtd\">\r\n"//
            + "<configuration>\r\n" //
            + "</configuration>";
    
    /**  
     * @Fields DROPDOWN : excel下拉选项默认值
     * @author caiyang
     * @date 2023年4月30日21:21:12
     */ 
    public static final String DROPDOWN = "{\"type\":\"dropdown\",\"type2\":false,\"value1\":\"\",\"value2\":\"\",\"checked\":false,\"remote\":false,\"prohibitInput\":false,\"hintShow\":false,\"hintText\":\"\"}";

    /**  
     * @Fields INTEGER : excel单元格整数类型默认值
     * @author caiyang
     * @date 2023-05-04 01:46:22 
     */  
    public static final String DATAVERIFY = "{\"prohibitInput\":false,\"type2\":\"bw\",\"hintShow\":false,\"hintText\":\"\",\"value2\":\"\",\"value1\":\"\",\"checked\":false,\"type\":\"number_integer\",\"remote\":false}";

    /**  
     * @Fields COMMENT : luckysheet 批注信息默认值
     * @author caiyang
     * @date 2023-05-05 08:14:51 
     */  
    public static final String COMMENT = "{\"left\":null,\"top\":null,\"width\":null,\"height\":null,\"value\":\"\",\"isshow\":false}";

    /**  
     * @Fields AUTHORITY : 工作表保护配置
     * @author caiyang
     * @date 2023-05-05 04:40:01 
     */  
    public static final String AUTHORITY = "{\"password\":\"\",\"algorithmName\":\"None\",\"saltValue\":null,\"hintText\":\"\",\"sheet\":1,\"selectLockedCells\":1,\"selectunLockedCells\":1,\"formatCells\":0,\"formatColumns\":0,\"formatRows\":0,\"insertColumns\":0,\"insertRows\":0,\"insertHyperlinks\":0,\"deleteColumns\":0,\"deleteRows\":0,\"sort\":0,\"filter\":0,\"usePivotTablereports\":0,\"editObjects\":0,\"editScenarios\":0,\"allowRangeList\":[]}";
    
    /**  
     * @Fields UNPROTECTCONFIG : 不受保护配置
     * @author caiyang
     * @date 2023-05-05 04:24:21 
     */  
    public static final String UNPROTECTCONFIG = "{\"name\":\"Default0\",\"password\":\"\",\"hintText\":\"\",\"algorithmName\":\"None\",\"saltValue\":null,\"checkRangePasswordUrl\":null,\"sqref\":\"$E$1:$E$9\"}";

    /**  
     * @Fields DEFAULT_JSON_DATA : 默认jsondata
     * @author caiyang
     * @date 2023-08-19 08:54:23 
     */  
    public static final String DEFAULT_JSON_DATA = "{\"row\": 84, \"name\": \"Sheet1\", \"color\": \"\", \"column\": 60, \"config\": {}, \"celldata\": [], \"isPivotTable\": false}";

    public static final String DEFAULT_SHEET_NAME = "Sheet1";
    
    public static final int DEFAULT_SHEET_ROW = 84;
    
    public static final int DEFAULT_SHEET_COLUMN = 60;
    
    public static final String MQ_TPOIC_LUCKYSHEET = "luckysheetTopic";
    
//    public static final String MQ_TPOIC_LUCKYSHEET_CELL = "luckysheetCellTopic";
    
//    public static final String MQ_TPOIC_LUCKYSHEET_NOORDERLY = "luckysheetTopicNoOrderly";
    
    public static final String MQ_TPOIC_LUCKYSHEET_CACHE = "luckysheetTopicConfigCache";
    
//    public static final String MQ_TPOIC_LUCKYSHEET_CELLVALUE_CACHE = "luckysheetTopicCellValueConfigCache";
    
//    public static final String MQ_TPOIC_LUCKYSHEET_CACHE_NOORDERLY = "luckysheetTopicCacheNoOrderly";
    
    public static final String MQ_TPOIC_LUCKYSHEET_HIS = "luckysheetHisTopic";
    
    public static final int MQ_LIST_LIMIT_SIZE = 100000;
    
    /**  
     * @Fields LUCKYCACHETIME : luckysheet协同编辑缓存时间 48小时
     * @author caiyang
     * @date 2023-09-28 01:20:02 
     */  
    public static final Long LUCKYCACHETIME = 172800L;
    
    /**  
     * @Fields MERCHANT_SEQ_PRE : 商户号前缀
     * @author caiyang
     * @date 2023-12-24 11:04:25 
     */  
    public static final String MERCHANT_SEQ_PRE = "SR";
    
    /**  
     * @Fields SHAREMODE_TIME : 文档共享模式默认时间 24小时
     * @author caiyang
     * @date 2024-01-01 05:20:30 
     */  
    public static final Long SHAREMODE_TIME = 86400L;
    
    /**  
     * @Fields colors : 自定义颜色
     * @author caiyang
     * @date 2024-01-04 05:08:01 
     */  
    public static final String[] COLORS = new String[] {"#FFB6C1","#DC143C","#C71585","#6495ED","#87CEFA","#5F9EA0","#20B2AA","#3CB371","#FFD700","#DAA520","#FFA500","#D2691E","#FF7F50","#FF6347","#00CED1","#c1232b","#27727b","#e87c25","#b5c334","#9bca63","#60c0dd","#d7504b","#26c0c0","#c12e34","#e6b600","#0098d9","#2b821d","#005eaa","#339ca8","#32a487","#3fb1e3","#626c91","#a0a7e6","#8B4513","#FF4500","#B22222","#40E0D0","#32CD32","#1E90FF","#BDB76B"};

    public static final String WEBSOCKET_SESSIONOUT_REASON = "this connection was established under an authenticated http session that has ended";
    
    public static final String WEBSOCKET_SESSIONOUT_REASON2 = "idle timeout expired";
    
    /**  
     * @Fields REPORT_TPL_CACHE_TIME : 报表模板缓存时间
     * @author caiyang
     * @date 2024-01-01 05:20:30 
     */  
    public static final Long REPORT_TPL_CACHE_TIME  = 86400L;
    
    /**  
     * @Fields LUCKYSHEET_FONTS : luckysheet支持的字体
     * @author caiyang
     * @date 2024-04-21 08:39:45 
     */  
    public static final List<String> LUCKYSHEET_FONTS = Arrays.asList(new String[] {"Times New Roman","Arial","Tahoma","Verdana","微软雅黑","宋体","黑体","楷体","仿宋","新宋体","华文新魏","华文行楷","华文隶书"});
    
    /**  
     * @Fields DEFAULT_LUCKYSHEET_FONT : luckysheet默认字体
     * @author caiyang
     * @date 2024-04-21 08:39:53 
     */  
    public static final String DEFAULT_LUCKYSHEET_FONT = "Times New Roman";
    
    /**  
     * @Fields SYSTEM_PARAM : 系统参数 用户id，用户账号，角色id，商户号，部门id
     * @author caiyang
     * @date 2024-08-14 06:40:11 
     */  
    public static final String[] SYSTEM_PARAM = new String[] {"userId","userName","roleId","merchantNo","deptId"};

    /**  
     * @Fields CELL_RULE : 单元格规则
     * @author caiyang
     * @date 2024-08-21 10:08:35 
     */  
    public static final String CELL_RULE = "{\"conditionRange\":[],\"cellrange\":[{\"column\":[1,1],\"row\":[2,2]}],\"format\":{\"textColor\":\"#9c0006\",\"cellColor\":\"#ffc7ce\"},\"conditionValue\":[\"5\"],\"conditionName\":\"greaterThan\",\"type\":\"default\"}";
    
    /**  
     * @Fields DATA_BAR_RULE : 数据条规则
     * @author caiyang
     * @date 2024-08-21 01:17:32 
     */  
    public static final String DATA_BAR_RULE = "{\"cellrange\":[{\"column\":[1,1],\"row\":[2,2]}],\"format\":[\"#638ec6\",\"#ffffff\"],\"type\":\"dataBar\"}";

    /**  
     * @Fields COLORGRADATION_RULE : 色阶数据规则
     * @author caiyang
     * @date 2024-08-21 01:48:56 
     */  
    public static final String COLORGRADATION_RULE = "{\"cellrange\":[{\"column\":[1,1],\"row\":[2,2]}],\"format\":[\"rgb(99, 190, 123)\",\"rgb(255, 235, 132)\",\"rgb(248, 105, 107)\"],\"type\":\"colorGradation\"}";

    /**  
     * @Fields ICON_SET_RULE : 图标集规则
     * @author caiyang
     * @date 2024-08-21 03:09:18 
     */  
    public static final String ICON_SET_RULE = "{\"cellrange\":[{\"column\":[1,1],\"row\":[2,2]}],\"format\":{\"len\":\"3\",\"top\":\"0\",\"leftMin\":\"0\"},\"type\":\"icons\"}";

}
