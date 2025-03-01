 /** 
 * 模块：报表系统-ReportFormsDatasourceAttrs
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.reportformsdatasourceattrs;

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
* @Description: report_forms_datasource_attrs - 填报绑定的数据源属性
* @author 
* @date 2025-02-27 02:32:15
* @version V1.0  
 */
@Data
@TableName("report_forms_datasource_attrs")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportFormsDatasourceAttrs extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** report_forms_datasource_id - 填报绑定数据源表主键 */
    @TableField("report_forms_datasource_id")
    private Long reportFormsDatasourceId;

    /** type - 列类型 1关联单元格列 2主键列 3 自动填充列 4删除设置 5主数据集关联列 */
    @TableField("type")
    private Integer type;

    /** coordsx - 单元格横坐标 */
    @TableField("coordsx")
    private Integer coordsx;

    /** coordsy - 单元格纵坐标 */
    @TableField("coordsy")
    private Integer coordsy;

    /** cell_coords - 单元格原始坐标 */
    @TableField("cell_coords")
    private String cellCoords;

    /** column_name - 数据源列名 */
    @TableField("column_name")
    private String columnName;

    /** id_type - 主键生成规则 1 自定义填写 2雪花算法 3自增主键 */
    @TableField("id_type")
    private Integer idType;

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

    /** fill_strategy - 填充策略 1 插入 2更新 3插入/更新数据 */
    @TableField("fill_strategy")
    private Integer fillStrategy;

    /** fill_type - 填充类型 1系统时间 2用户id 3用户名 4商户号 */
    @TableField("fill_type")
    private Integer fillType;

    /** fill_value - 填充值 */
    @TableField("fill_value")
    private String fillValue;

    /** delete_type - 删除方式 1物理删除 2逻辑删除 */
    @TableField("delete_type")
    private Integer deleteType;

    /** delete_value - 删除值 */
    @TableField("delete_value")
    private String deleteValue;

    /** main_column - 主表数据列 */
    @TableField("main_column")
    private String mainColumn;

    /** main_name - 主表数据源绑定名 */
    @TableField("main_name")
    private String mainName;

    /** main_datasource_id - 主表数据源id */
    @TableField("main_datasource_id")
    private Long mainDatasourceId;

    /** main_table - 主表表名称 */
    @TableField("main_table")
    private String mainTable;
}