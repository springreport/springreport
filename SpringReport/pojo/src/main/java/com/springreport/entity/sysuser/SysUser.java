 /** 
 * 模块：报表系统-SysUser
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.sysuser;

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
* @Description: sys_user - 
* @author 
* @date 2021-06-15 02:34:30
* @version V1.0  
 */
@Data
@TableName("sys_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUser extends PageEntity {

    /** id -  */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** merchant_no - 商户号 */
    @TableField("merchant_no")
    private String merchantNo;

    /** user_name - 用户登录名 */
    @TableField("user_name")
    private String userName;

    /** user_real_name - 用户真实姓名 */
    @TableField("user_real_name")
    private String userRealName;

    /** password - 用户密码 */
    @TableField("password")
    private String password;

    /** user_email - 用户邮箱，唯一 */
    @TableField("user_email")
    private String userEmail;

    /** user_phone - 座机 */
    @TableField("user_phone")
    private String userPhone;

    /** user_mobile - 手机，唯一 */
    @TableField("user_mobile")
    private String userMobile;

    /** user_head_name - 头像文件名称 */
    @TableField("user_head_name")
    private String userHeadName;

    /** user_head - 用户头像路径 */
    @TableField("user_head")
    private String userHead;

    /** user_locked - 用户是否锁定 1是 2否 */
    @TableField("user_locked")
    private Integer userLocked;

    /** last_login_time - 上次登录时间 */
    @TableField("last_login_time")
    private Long lastLoginTime;

    /** attempt - 账户尝试登录次数 */
    @TableField("attempt")
    private Integer attempt;

    /** is_admin - 是否超级管理员 1是 2否 */
    @TableField("is_admin")
    private Integer isAdmin;
    
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