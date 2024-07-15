 /** 
 * 模块：报表系统-SysMenu
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.sysmenu;

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
* @Description: sys_menu - 
* @author 
* @date 2022-06-29 06:36:41
* @version V1.0  
 */
@Data
@TableName("sys_menu")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysMenu extends PageEntity {

    /** id -  */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** menu_name - 菜单名称 */
    @TableField("menu_name")
    private String menuName;

    /** menu_url - 菜单url */
    @TableField("menu_url")
    private String menuUrl;

    /** parent_menu_id - 上级菜单id，一级菜单用0表示 */
    @TableField("parent_menu_id")
    private Long parentMenuId;

    /** menu_icon - 菜单图标 */
    @TableField("menu_icon")
    private String menuIcon;

    /** is_hidden - 是否隐藏 1是 2否 */
    @TableField("is_hidden")
    private Integer isHidden;

    /** sort - 排序 */
    @TableField("sort")
    private Integer sort;

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