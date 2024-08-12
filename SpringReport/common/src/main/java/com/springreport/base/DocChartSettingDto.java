package com.springreport.base;


import lombok.Data;

@Data
public class DocChartSettingDto {


    /** id - 主键id */
    
    private Long id;

    /** tpl_id - 模板id */
    private Long tplId;

    /** chart_url - 图表对应的图片链接 */
    private String chartUrl;

    /** chart_name - 图表名称 */
    private String chartName;

    /** show_chart_name - 图表是否显示名称 1是 2否 */
    private Integer showChartName;

    /** dataset_id - 数据集id */
    private Long datasetId;

    /** dataset_name - 数据集名称 */
    private String datasetName;

    /** category_field - 分类字段 */
    private String categoryField;

    /** value_field - 数值字段 */
    private String valueField;
    
    /** series_field - 系列字段 */
    private String seriesField;

    /** chart_type - 图表类型 */
    private String chartType;

}
