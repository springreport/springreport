 /** 
 * 模块：报表系统-SysMerchantAuthTemplateIds
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.sysmerchantauthtemplateids;

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
* @Description: sys_merchant_auth_template_ids - 
* @author 
* @date 2023-12-22 05:19:05
* @version V1.0  
 */
@Data
@TableName("sys_merchant_auth_template_ids")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysMerchantAuthTemplateIds extends PageEntity {

    /** id - 主键id */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** template_id - 模板id */
    @TableField("template_id")
    private Long templateId;

    /** auth_type - 1菜单 2按钮 */
    @TableField("auth_type")
    private Integer authType;

    /** auth_id - 权限id */
    @TableField("auth_id")
    private Long authId;

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