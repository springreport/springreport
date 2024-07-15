 /** 
 * 模块：报表系统-ReportDatasourceDictValue
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.reportdatasourcedictvalue;

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
* @Description: report_datasource_dict_value - 
* @author 
* @date 2021-08-23 07:52:08
* @version V1.0  
 */
@Data
@TableName("report_datasource_dict_value")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDatasourceDictValue extends PageEntity {

    /** id -  */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** code - 字典编码 */
    @TableField("code")
    private String code;

    /** value - 字典值 */
    @TableField("value")
    private String value;

    /** value_name - 字典值名称 */
    @TableField("value_name")
    private String valueName;

    /** status - 状态 1启用 2禁用 */
    @TableField("status")
    private Integer status;

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
}