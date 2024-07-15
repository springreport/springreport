 /** 
 * 模块：报表系统-SysMerchant
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.sysmerchant;

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
* @Description: sys_merchant - 
* @author 
* @date 2023-12-23 08:44:17
* @version V1.0  
 */
@Data
@TableName("sys_merchant")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysMerchant extends PageEntity {

    /** id - 主键id */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** merchant_no - 商户号 */
    @TableField("merchant_no")
    private String merchantNo;

    /** merchant_name - 租户名称 */
    @TableField("merchant_name")
    private String merchantName;

    /** phone - 电话 */
    @TableField("phone")
    private String phone;

    /** email - 邮箱 */
    @TableField("email")
    private String email;

    /** status - 状态 1启用 2禁用 */
    @TableField("status")
    private Integer status;

    /** auth_template - 权限模板 */
    @TableField("auth_template")
    private Long authTemplate;

    /** is_system_merchant - 是否系统默认租户 1是 2否 */
    @TableField("is_system_merchant")
    private Integer isSystemMerchant;

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