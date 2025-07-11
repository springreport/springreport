 /** 
 * 模块：报表系统-LuckysheetReportCell
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.luckysheetreportcell;

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
* @Description: luckysheet_report_cell - 
* @author 
* @date 2025-01-24 08:15:17
* @version V1.0  
 */
@Data
@TableName("luckysheet_report_cell")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LuckysheetReportCell extends PageEntity {

    /** id -  */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** sheet_id - 模板sheetid */
    @TableField("sheet_id")
    private Long sheetId;

    /** dataset_name -  */
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

    /** cell_value_type - 单元格内容类型 1固定值 2变量 3循环块 */
    @TableField("cell_value_type")
    private Integer cellValueType;

    /** aggregate_type - 聚合类型 */
    @TableField("aggregate_type")
    private String aggregateType;

    /** group_property - 分组属性 */
    @TableField("group_property")
    private String groupProperty;

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

    /** cell_function - 函数类型 1求和 2求平均值 3最大值 4最小值 */
    @TableField("cell_function")
    private Integer cellFunction;

    /** digit - 聚合函数小数位数 */
    @TableField("digit")
    private Integer digit;

    /** group_summary_dependency_r - 分组依赖行号 */
    @TableField("group_summary_dependency_r")
    private String groupSummaryDependencyR;

    /** group_summary_dependency_c - 分组依赖列号 */
    @TableField("group_summary_dependency_c")
    private String groupSummaryDependencyC;

    /** data_from - 数据来源 1默认 2原始数据 3单元格 */
    @TableField("data_from")
    private Integer dataFrom;

    /** is_group_merge - 合并单元格是否合一 */
    @TableField("is_group_merge")
    private Boolean isGroupMerge;

    /** is_function - 是否是函数 1是 2否 */
    @TableField("is_function")
    private Integer isFunction;

    /** warning - 是否预警 */
    @TableField("warning")
    private Boolean warning;

    /** warning_rules - 预警规则 */
    @TableField("warning_rules")
    private String warningRules;

    /** warning_color - 预警颜色 */
    @TableField("warning_color")
    private String warningColor;

    /** threshold - 预警阈值 */
    @TableField("threshold")
    private String threshold;

    /** warning_content - 预警内容 */
    @TableField("warning_content")
    private String warningContent;

    /** is_relied - 数据是否被其他单元格依赖 1是 2否 */
    @TableField("is_relied")
    private Integer isRelied;

    /** rely_cells - 依赖单元格坐标，多个单元格之间用逗号分割 */
    @TableField("rely_cells")
    private String relyCells;

    /** is_dict - 是否是数据字典 */
    @TableField("is_dict")
    private Boolean isDict;

    /** datasource_id - 数据字典数据源id */
    @TableField("datasource_id")
    private Long datasourceId;

    /** dict_type - 数据字典类型 */
    @TableField("dict_type")
    private String dictType;

    /** alternate_format - 是否交替颜色1是 2否 */
    @TableField("alternate_format")
    private Integer alternateFormat;

    /** alternate_format_bc_odd - 奇数行背景颜色 */
    @TableField("alternate_format_bc_odd")
    private String alternateFormatBcOdd;

    /** alternate_format_bc_even - 偶数行背景颜色 */
    @TableField("alternate_format_bc_even")
    private String alternateFormatBcEven;

    /** alternate_format_fc_odd - 奇数行字体颜色 */
    @TableField("alternate_format_fc_odd")
    private String alternateFormatFcOdd;

    /** alternate_format_fc_even - 偶数行字体颜色 */
    @TableField("alternate_format_fc_even")
    private String alternateFormatFcEven;

    /** is_conditions - 是否有单元格过滤条件 1是 2否 */
    @TableField("is_conditions")
    private Integer isConditions;

    /** cell_conditions - 单元格过滤条件 */
    @TableField("cell_conditions")
    private String cellConditions;

    /** filter_type - 过滤类型 and or */
    @TableField("filter_type")
    private String filterType;

    /** is_hidden_conditions - 是否有隐藏条件 1是 2否 */
    @TableField("is_hidden_conditions")
    private Integer isHiddenConditions;

    /** hidden_type - 隐藏条件类型 and or */
    @TableField("hidden_type")
    private String hiddenType;

    /** hidden_conditions - 隐藏条件 */
    @TableField("hidden_conditions")
    private String hiddenConditions;

    /** is_chart_attr - 是否是图表中的单元格属性 1是 2否 */
    @TableField("is_chart_attr")
    private Integer isChartAttr;

    /** chart_ids - 图表唯一标识，多个图表用,分隔 */
    @TableField("chart_ids")
    private String chartIds;

    /** series_name - 图表系列名称 */
    @TableField("series_name")
    private String seriesName;

    /** is_chart_cell - 是否是图表的第一个单元格 1是 2否 */
    @TableField("is_chart_cell")
    private Integer isChartCell;

    /** is_data_verification - 是否是数据校验项 1是 2否 */
    @TableField("is_data_verification")
    private Integer isDataVerification;

    /** data_verification - 数据校验项目 */
    @TableField("data_verification")
    private String dataVerification;

    /** is_drill - 是否下钻 */
    @TableField("is_drill")
    private Boolean isDrill;

    /** drill_id - 下钻报表id */
    @TableField("drill_id")
    private Long drillId;

    /** drill_attrs - 下钻属性 */
    @TableField("drill_attrs")
    private String drillAttrs;

    /** unit_transfer - 是否数值单位转换 */
    @TableField("unit_transfer")
    private Boolean unitTransfer;

    /** transfer_type - 转换方式 1乘法 2除法 */
    @TableField("transfer_type")
    private Integer transferType;

    /** multiple - 倍数 */
    @TableField("multiple")
    private String multiple;

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

    /** is_subtotal - 是否需要小计 */
    @TableField("is_subtotal")
    private Boolean isSubtotal;

    /** subtotal_cells - 小计单元格 */
    @TableField("subtotal_cells")
    private String subtotalCells;

    /** is_subtotal_calc - 是否有小计公式链 */
    @TableField("is_subtotal_calc")
    private Boolean isSubtotalCalc;

    /** subtotal_calc - 小计公式链 */
    @TableField("subtotal_calc")
    private String subtotalCalc;

    /** subtotal_attrs - 小计属性 */
    @TableField("subtotal_attrs")
    private String subtotalAttrs;

    /** cell_fill_type - 数据填充方式 1插入 2覆盖 */
    @TableField("cell_fill_type")
    private Integer cellFillType;

    /** forms_attrs - 填报属性配置 */
    @TableField("forms_attrs")
    private String formsAttrs;
    
    /** hloop_count - 横向循环次数 */
    @TableField("hloop_count")
    private Integer hloopCount;

    /** hloop_empty_count - 横向循环间隔空行数 */
    @TableField("hloop_empty_count")
    private Integer hloopEmptyCount;

    /** vloop_empty_count - 纵向循环间隔空行数 */
    @TableField("vloop_empty_count")
    private Integer vloopEmptyCount;
    
    /** is_object - 是否是复杂对象 */
    @TableField("is_object")
    private Boolean isObject;
    
    /** data_type - 数据类型 1数组 2对象数组 3对象 */
    @TableField("data_type")
    private Integer dataType;
    
    /** data_attr - 属性 */
    @TableField("data_attr")
    private String dataAttr;
    
    /** sub_extend - 子数据扩展方向 1不扩展 2纵向扩展 3横向扩展 */
    @TableField("sub_extend")
    private Integer subExtend;
    
    /** priorty_move_direction - 优先移动方向 1向下 2向右 */
    @TableField("priorty_move_direction")
    private Integer priortyMoveDirection;
    
    /** source_type - 数据字典数据来源 1数据字典 2sql语句 3自定义 */
    @TableField("source_type")
    private Integer sourceType;

    /** dict_content - sql语句或者自定义数据字典内容 */
    @TableField("dict_content")
    private String dictContent;
    
    /** sub_block_range -  子循环块范围*/
    @TableField("sub_block_range")
    private String subBlockRange;
    
    /** compare_attr1 - 同比/环比本期属性 */
    @TableField("compare_attr1")
    private String compareAttr1;

    /** compare_attr2 - 同比/环比同期属性 */
    @TableField("compare_attr2")
    private String compareAttr2;
    
    /** is_dump - 去重属性 */
    @TableField("is_dump")
    private Boolean isDump;
    
    /** dump_attr - 去重属性 */
    @TableField("dump_attr")
    private String dumpAttr;
    
    /** keep_empty_cell - 没有数据时是否保留空单元格*/
    @TableField("keep_empty_cell")
    private Boolean keepEmptyCell;
}