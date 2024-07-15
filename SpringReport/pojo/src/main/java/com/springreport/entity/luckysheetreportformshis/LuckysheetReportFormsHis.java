 /** 
 * 模块：报表系统-LuckysheetReportFormsHis
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.luckysheetreportformshis;

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
* @Description: luckysheet_report_forms_his - 填报报表历史记录
* @author 
* @date 2023-01-30 07:42:31
* @version V1.0  
 */
@Data
@TableName("luckysheet_report_forms_his")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LuckysheetReportFormsHis extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** tpl_id - 报表模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** sheet_index - 报表模板sheet 标识 */
    @TableField("sheet_index")
    private String sheetIndex;

    /** datasource_id - 数据源id */
    @TableField("datasource_id")
    private Long datasourceId;

    /** table_name - 表名称 */
    @TableField("table_name")
    private String tableName;

    /** basic_data - 原始数据 */
    @TableField("basic_data")
    private String basicData;

    /** report_data - 上报的数据 */
    @TableField("report_data")
    private String reportData;

    /** change_data_before - 发生变化的部分修改前的数据 */
    @TableField("change_data_before")
    private String changeDataBefore;

    /** change_data_after - 发生变化的部分修改后的数据 */
    @TableField("change_data_after")
    private String changeDataAfter;

    /** operate_ip - 请求机器ip */
    @TableField("operate_ip")
    private String operateIp;

    /** creator - 创建人 */
    @TableField(value = "creator")
    private Long creator;

    /** create_time - 创建时间 */
    @TableField(value = "create_time")
    private Date createTime;

    /** updater - 更新人 */
   @TableField(value = "updater",fill = FieldFill.INSERT_UPDATE)
    private Long updater;

    /** update_time - 更新时间 */
    @TableField(value = "update_time")
    private Date updateTime;

    /** del_flag - 删除标记 1未删除 2已删除 */
    @TableField("del_flag")
    private Integer delFlag;
}