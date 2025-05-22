 /** 
 * 模块：报表系统-ReportDatasource
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.reportdatasource;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.springreport.base.PageEntity;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

 /**  
* @Description: report_datasource - 
* @author 
* @date 2023-02-02 10:42:21
* @version V1.0  
 */
@Data
@TableName("report_datasource")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDatasource extends PageEntity {

    /** id - 主键id */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** merchant_no - 商户号 */
    @TableField("merchant_no")
    private String merchantNo;

    /** code - 编码 */
    @TableField("code")
    private String code;

    /** name - 数据库名称 */
    @TableField("name")
    private String name;

    /** type - 数据库类型 1mysql 2oracle 3 sqlserver 4api */
    @TableField("type")
    private Integer type;

    /** driver_class - 驱动 */
    @TableField("driver_class")
    private String driverClass;

    /** jdbc_url - 数据库链接url */
    @TableField("jdbc_url")
    private String jdbcUrl;

    /** user_name - 登录名 */
    @TableField("user_name")
    private String userName;

    /** password - 密码 */
    @TableField("password")
    private String password;

    /** api_columns_prefix - 属性前缀 */
    @TableField("api_columns_prefix")
    private String apiColumnsPrefix;

    /** api_columns - api返回列 */
    @TableField("api_columns")
    private String apiColumns;

    /** api_result_type - api返回类型 String Array ObjectArray Object */
    @TableField("api_result_type")
    private String apiResultType;

    /** api_request_type - 请求方式 post get */
    @TableField("api_request_type")
    private String apiRequestType;

    /** api_request_header - 接口请求头 */
    @TableField("api_request_header")
    private String apiRequestHeader;

    /** creator - 创建人 */
    @TableField(value = "creator",fill = FieldFill.INSERT)
    private Long creator;

    /** create_time - 创建时间 */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /** updater - 更新人 */
   @TableField(value = "updater",fill = FieldFill.INSERT_UPDATE)
    private Long updater;

    /** update_time - 更新时间 */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /** del_flag - 删除标记 1未删除 2已删除 */
    @TableField("del_flag")
    private Integer delFlag;

     /** api_params - api请求json */
     @TableField("api_params")
     private String apiParams;
}