 /** 
 * 模块：报表系统-ReportDatasourceDictData
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.reportdatasourcedictdata;

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
* @Description: report_datasource_dict_data - 
* @author 
* @date 2022-11-21 01:46:25
* @version V1.0  
 */
@Data
@TableName("report_datasource_dict_data")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDatasourceDictData extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** datasource_id - 数据源id */
    @TableField("datasource_id")
    private Long datasourceId;

    /** dict_label - 字典标签 */
    @TableField("dict_label")
    private String dictLabel;

    /** dict_value - 字典键值 */
    @TableField("dict_value")
    private String dictValue;

    /** dict_type - 字典类型 */
    @TableField("dict_type")
    private String dictType;

    /** remark - 备注 */
    @TableField("remark")
    private String remark;

    /** sort - 排序 */
    @TableField("sort")
    private Integer sort;

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