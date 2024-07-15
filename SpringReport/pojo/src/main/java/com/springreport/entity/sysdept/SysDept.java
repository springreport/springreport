 /** 
 * 模块：报表系统-SysDept
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.sysdept;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

 /**  
* @Description: sys_dept - 
* @author 
* @date 2023-12-22 05:18:48
* @version V1.0  
 */
@Data
@TableName("sys_dept")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysDept extends BaseEntity {

    /** id - 主键id */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** merchant_no - 商户号 */
    @TableField("merchant_no")
    private String merchantNo;

    /** parent_id - 父级id，一级id用0表示 */
    @TableField("parent_id")
    private Long parentId;

    /** parent_id_list - 父级id集合 */
    @TableField("parent_id_list")
    private String parentIdList;

    /** dept_name - 部门名称 */
    @TableField("dept_name")
    private String deptName;

    /** leader - 负责人 */
    @TableField("leader")
    private String leader;

    /** phone - 联系电话 */
    @TableField("phone")
    private String phone;

    /** email - 邮箱 */
    @TableField("email")
    private String email;

    /** dept_sort - 排序 */
    @TableField("dept_sort")
    private Integer deptSort;

    /** status - 部门状态（1正常 2停用） */
    @TableField("status")
    private Integer status;

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