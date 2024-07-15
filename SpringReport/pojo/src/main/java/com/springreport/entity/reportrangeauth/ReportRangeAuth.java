 /** 
 * 模块：报表系统-ReportRangeAuth
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.reportrangeauth;

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
* @Description: report_range_auth - 
* @author 
* @date 2024-03-03 05:52:11
* @version V1.0  
 */
@Data
@TableName("report_range_auth")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportRangeAuth extends PageEntity {

    /** id - 主键id */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** sheet_id - sheet页id */
    @TableField("sheet_id")
    private Long sheetId;
    
    /** sheet_index - sheet页唯一索引 */
    @TableField("sheet_index")
    private String sheetIndex;

    /** range_txt - 范围 如A1:C1 */
    @TableField("range_txt")
    private String rangeTxt;

    /** rows - 起始横坐标 */
    @TableField("rows_no")
    private String rowsNo;

    /** cols - 起始纵坐标 */
    @TableField("cols_no")
    private String colsNo;
    
    /** type - 类型 1报表 2协同文档 */
    @TableField("type")
    private Integer type;

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