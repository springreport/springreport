 /** 
 * 模块：报表系统-ReportRangeAuthUser
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.reportrangeauthuser;

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
* @Description: report_range_auth_user - 
* @author 
* @date 2024-06-07 03:36:31
* @version V1.0  
 */
@Data
@TableName("report_range_auth_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportRangeAuthUser extends PageEntity {

    /** id - 主键id */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** sheet_id - sheet页id */
    @TableField("sheet_id")
    private Long sheetId;

    /** range_auth_id - 授权范围id */
    @TableField("range_auth_id")
    private Long rangeAuthId;

    /** user_id - 用户id */
    @TableField("user_id")
    private Long userId;

    /** type - 类型 1报表 2协同文档 */
    @TableField("type")
    private Integer type;

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

    /** auth_type - 授权类型 1编辑权限 2不允许查看 */
    @TableField("auth_type")
    private Integer authType;
}