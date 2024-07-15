 /** 
 * 模块：报表系统-MqFailedMsg
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.mqfailedmsg;

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
* @Description: mq_failed_msg - 
* @author 
* @date 2023-12-22 10:38:34
* @version V1.0  
 */
@Data
@TableName("mq_failed_msg")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MqFailedMsg extends PageEntity {

    /** id - 主键id */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** type - 消息类型 1缓存 2数据库 3历史记录 */
    @TableField("type")
    private Integer type;

    /** message_content - 消息内容 */
    @TableField("message_content")
    private String messageContent;

    /** failure_exception - 失败异常信息 */
    @TableField("failure_exception")
    private String failureException;

    /** create_time - 创建时间 */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /** update_time - 更新时间 */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}