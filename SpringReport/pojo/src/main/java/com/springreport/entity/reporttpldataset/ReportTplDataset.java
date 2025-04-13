 /** 
 * 模块：报表系统-ReportTplDataset
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.reporttpldataset;

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
* @Description: report_tpl_dataset - 
* @author 
* @date 2021-10-26 02:16:12
* @version V1.0  
 */
@Data
@TableName("report_tpl_dataset")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportTplDataset extends PageEntity {

    /** id - 主键id */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** merchant_no - 商户号 */
    @TableField("merchant_no")
    private String merchantNo;

    /** dataset_type - 数据集类型 1sql 2api */
    @TableField("dataset_type")
    private Integer datasetType;

    /** dataset_name - 数据集名称 */
    @TableField("dataset_name")
    private String datasetName;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** datasource_id - 数据源id */
    @TableField("datasource_id")
    private Long datasourceId;

    /** tpl_sql - sql语句 */
    @TableField("tpl_sql")
    private String tplSql;

    /** tpl_param - 参数 */
    @TableField("tpl_param")
    private String tplParam;

    /** sql_type - sql类型 1标准sql 2存储过程 */
    @TableField("sql_type")
    private Integer sqlType;

    /** in_param - 存储过程入参 */
    @TableField("in_param")
    private String inParam;

    /** out_param - 存储过程出参 */
    @TableField("out_param")
    private String outParam;

    /** is_pagination - 是否分页 1是 2否 */
    @TableField("is_pagination")
    private Integer isPagination;

    /** page_count - 每页显示条数 */
    @TableField("page_count")
    private Integer pageCount;

    /** type - 类型 1报表 2大屏 */
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
    
    /** current_page_attr - 当前页码属性 */
    @TableField("current_page_attr")
    private String currentPageAttr;

    /** page_count_attr - 每页显示条数属性 */
    @TableField("page_count_attr")
    private String pageCountAttr;

    /** total_attr - 数据总条数属性 */
    @TableField("total_attr")
    private String totalAttr;
    
    /**  
     * @Fields groupId : 分组id
     * @author caiyang
     * @date 2024-12-13 05:55:06 
     */  
    @TableField("group_id")
    private Long groupId;
    
    /** sub_param_attrs - 主表传递给子表的参数属性 */
    @TableField("sub_param_attrs")
    private String subParamAttrs;
    
    /** is_common - 是否公共数据集 1是 2否 */
    @TableField("is_common")
    private Integer isCommon;
    
    /** common_type - 类型 1excel报表 2word报表 3ppt报表 */
    @TableField("common_type")
    private Integer commonType;
    
    /** is_convert - 是否行列转置 1是 2否 */
    @TableField("is_convert")
    private Integer isConvert;
    
    /** header_name - 行转列字段 */
    @TableField("header_name")
    private String headerName;
    
    /** value_field - 行转列值 */
    @TableField("value_field")
    private String valueField;
    
    /** fixed_column - 固定列 */
    @TableField("fixed_column")
    private String fixedColumn;
}