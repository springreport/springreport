 /** 
 * 模块：报表系统-ReportDatasourceDictType
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.reportdatasourcedicttype;

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
* @Description: report_datasource_dict_type - 
* @author 
* @date 2022-11-21 01:46:20
* @version V1.0  
 */
@Data
@TableName("report_datasource_dict_type")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDatasourceDictType extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** datasource_id - 数据源id */
    @TableField("datasource_id")
    private Long datasourceId;

    /** dict_name - 字典类型名称 */
    @TableField("dict_name")
    private String dictName;

    /** dict_type - 字典类型 */
    @TableField("dict_type")
    private String dictType;

    /** remark - 备注 */
    @TableField("remark")
    private String remark;

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
    
    /** merchant_no - 商户号 */
    @TableField("merchant_no")
    private String merchantNo;
}