 /** 
 * 模块：报表系统-ReportTplSheetChart
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.reporttplsheetchart;

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
* @Description: report_tpl_sheet_chart - 
* @author 
* @date 2023-04-19 01:40:52
* @version V1.0  
 */
@Data
@TableName("report_tpl_sheet_chart")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportTplSheetChart extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** sheet_id - sheetid */
    @TableField("sheet_id")
    private Long sheetId;

    /** chart_id - 图表标识 */
    @TableField("chart_id")
    private String chartId;

    /** title - 图表标题 */
    @TableField("title")
    private String title;

    /** data_type - 数据来源 1自定义 2数据集 */
    @TableField("data_type")
    private Integer dataType;

    /** dataset_id - 数据集id */
    @TableField("dataset_id")
    private Long datasetId;

    /** dataset_name - 数据集名称 */
    @TableField("dataset_name")
    private String datasetName;

    /** attr - 属性 */
    @TableField("attr")
    private String attr;

    /** xaxis_datas - 自定义值 */
    @TableField("xaxis_datas")
    private String xaxisDatas;

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