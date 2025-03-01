 /** 
 * 模块：报表系统-ReportFormsDatasource
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.reportformsdatasource;

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
* @Description: report_forms_datasource - 填报绑定的数据源
* @author 
* @date 2025-02-27 02:32:08
* @version V1.0  
 */
@Data
@TableName("report_forms_datasource")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportFormsDatasource extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** sheet_id - 模板sheetid */
    @TableField("sheet_id")
    private Long sheetId;

    /** name - 名称 */
    @TableField("name")
    private String name;

    /** datasource_id - 数据源id */
    @TableField("datasource_id")
    private Long datasourceId;

    /** table_name - 表名 */
    @TableField("table_name")
    private String tableName;

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

    /** is_main - 是否主数据集 1是 2否 */
    @TableField("is_main")
    private Integer isMain;
}