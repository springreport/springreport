 /** 
 * 模块：报表系统-LuckysheetReportFormsCell
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.luckysheetreportformscell;

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
* @Description: luckysheet_report_forms_cell - 填报报表单元格
* @author 
* @date 2022-11-29 07:36:04
* @version V1.0  
 */
@Data
@TableName("luckysheet_report_forms_cell")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LuckysheetReportFormsCell extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** sheet_id - 模板 sheet id */
    @TableField("sheet_id")
    private Long sheetId;

    /** dataset_name - 数据集名称 */
    @TableField("dataset_name")
    private String datasetName;

    /** coordsx - 横坐标 */
    @TableField("coordsx")
    private Integer coordsx;

    /** coordsy - 纵坐标 */
    @TableField("coordsy")
    private Integer coordsy;

    /** cell_extend - 单元格扩展方向 1不扩展 2纵向扩展 3横向扩展 4交叉扩展 */
    @TableField("cell_extend")
    private Integer cellExtend;

    /** cell_value - 单元格值 */
    @TableField("cell_value")
    private String cellValue;

    /** cell_data - 单元格配置 */
    @TableField("cell_data")
    private String cellData;

    /** cell_value_type - 单元格内容类型 1固定值 2变量 */
    @TableField("cell_value_type")
    private Integer cellValueType;

    /** is_merge - 是否是合并单元格 1是 2否 */
    @TableField("is_merge")
    private Integer isMerge;

    /** row_span - 合并行数 */
    @TableField("row_span")
    private Integer rowSpan;

    /** col_span - 合并列数 */
    @TableField("col_span")
    private Integer colSpan;

    /** cell_attrs - 单元格属性 */
    @TableField("cell_attrs")
    private String cellAttrs;

    /** warning - 是否预警 */
    @TableField("warning")
    private Boolean warning;

    /** warning_color - 预警颜色 */
    @TableField("warning_color")
    private String warningColor;

    /** threshold - 预警阈值 */
    @TableField("threshold")
    private String threshold;

    /** warning_content - 预警内容 */
    @TableField("warning_content")
    private String warningContent;

    /** is_function - 是否是函数 1是 2否 */
    @TableField("is_function")
    private Integer isFunction;

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