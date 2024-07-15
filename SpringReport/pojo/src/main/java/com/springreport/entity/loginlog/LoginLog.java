 /** 
 * 模块：报表系统-LoginLog
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.loginlog;

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
* @Description: login_log - 
* @author 
* @date 2021-08-12 04:00:54
* @version V1.0  
 */
@Data
@TableName("login_log")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginLog extends PageEntity {

    /** id -  */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** operate_time - 操作时间 */
    @TableField("operate_time")
    private Date operateTime;

    /** status - 是否登录成功 1是 2否 默认 1 */
    @TableField("status")
    private Integer status;

    /** error_info - 错误信息 */
    @TableField("error_info")
    private String errorInfo;

    /** operate_ip - 请求机器ip */
    @TableField("operate_ip")
    private String operateIp;

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