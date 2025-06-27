 /** 
 * 模块：报表系统-LuckysheetReportBlockCell
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.luckysheetreportblockcell;

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
* @Description: luckysheet_report_block_cell - 
* @author 
* @date 2025-06-23 12:14:21
* @version V1.0  
 */
@Data
@TableName("luckysheet_report_block_cell")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LuckysheetReportBlockCell extends PageEntity {

    /** id - 主键id */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** report_cell_id - 单元格id */
    @TableField("report_cell_id")
    private Long reportCellId;

    /** dataset_name - 数据集名称 */
    @TableField("dataset_name")
    private String datasetName;

    /** coordsx - 横坐标 */
    @TableField("coordsx")
    private Integer coordsx;

    /** coordsy - 纵坐标 */
    @TableField("coordsy")
    private Integer coordsy;

    /** cell_value - 单元格值 */
    @TableField("cell_value")
    private String cellValue;

    /** cell_data - 单元格配置 */
    @TableField("cell_data")
    private String cellData;

    /** cell_value_type - 单元格内容类型 1固定值 2变量 */
    @TableField("cell_value_type")
    private Integer cellValueType;

    /** is_link - 是否超链接 1是 2否 默认2 */
    @TableField("is_link")
    private Integer isLink;

    /** link_config - 超链接配置 */
    @TableField("link_config")
    private String linkConfig;

    /** is_merge - 是否是合并单元格 1是 2否 */
    @TableField("is_merge")
    private Integer isMerge;

    /** row_span - 合并行数 */
    @TableField("row_span")
    private Integer rowSpan;

    /** col_span - 合并列数 */
    @TableField("col_span")
    private Integer colSpan;

    /** is_function - 是否是函数 1是 2否 */
    @TableField("is_function")
    private Integer isFunction;

    /** is_data_verification - 是否是数据校验项 1是 2否 */
    @TableField("is_data_verification")
    private Integer isDataVerification;

    /** data_verification - 数据校验项目 */
    @TableField("data_verification")
    private String dataVerification;

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

    /** is_sub_cell - 是否子循环块单元格1是 2否 */
    @TableField("is_sub_cell")
    private Integer isSubCell;
}