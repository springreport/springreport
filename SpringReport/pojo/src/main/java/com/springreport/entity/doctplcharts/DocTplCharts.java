 /** 
 * 模块：报表系统-DocTplCharts
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.doctplcharts;

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
* @Description: doc_tpl_charts - 
* @author 
* @date 2024-08-11 09:12:34
* @version V1.0  
 */
@Data
@TableName("doc_tpl_charts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocTplCharts extends PageEntity {

    /** id - 主键id */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** chart_url - 图表对应的图片链接 */
    @TableField("chart_url")
    private String chartUrl;

    /** chart_name - 图表名称 */
    @TableField("chart_name")
    private String chartName;

    /** show_chart_name - 图表是否显示名称 1是 2否 */
    @TableField("show_chart_name")
    private Integer showChartName;

    /** dataset_id - 数据集id */
    @TableField("dataset_id")
    private Long datasetId;

    /** dataset_name - 数据集名称 */
    @TableField("dataset_name")
    private String datasetName;

    /** category_field - 分组字段 */
    @TableField("category_field")
    private String categoryField;

    /** value_field - 数值字段 */
    @TableField("value_field")
    private String valueField;

    /** series_field - 系列字段 */
    @TableField("series_field")
    private String seriesField;

    /** chart_type - 图表类型 */
    @TableField("chart_type")
    private String chartType;

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