 /** 
 * 模块：报表系统-QrtzReportDetail
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.qrtzreportdetail;

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
* @Description: qrtz_report_detail - 
* @author 
* @date 2023-07-30 12:27:20
* @version V1.0  
 */
@Data
@TableName("qrtz_report_detail")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QrtzReportDetail extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** job_name - 任务名称 */
    @TableField("job_name")
    private String jobName;

    /** job_time_type - 任务执行时间类型 1指定时间 2cron表达式 */
    @TableField("job_time_type")
    private Integer jobTimeType;

    /** job_time - 任务执行时间 */
    @TableField("job_time")
    private String jobTime;

    /** job_cron - 任务执行时间(cron表达式) */
    @TableField("job_cron")
    private String jobCron;

    /** job_data - 任务参数 */
    @TableField("job_data")
    private String jobData;

    /** email - 导出数据后发送邮箱，多个邮箱用;分割 */
    @TableField("email")
    private String email;

    /** export_type - 导出类型 1excel 2pdf 3excel+pdf */
    @TableField("export_type")
    private Integer exportType;

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