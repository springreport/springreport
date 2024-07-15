 /** 
 * 模块：报表系统-SysPost
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.syspost;

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
* @Description: sys_post - 
* @author 
* @date 2023-12-22 05:19:10
* @version V1.0  
 */
@Data
@TableName("sys_post")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysPost extends PageEntity {

    /** id - 主键id */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** merchant_no - 商户号 */
    @TableField("merchant_no")
    private String merchantNo;

    /** post_code - 岗位编码 */
    @TableField("post_code")
    private String postCode;

    /** post_name - 岗位名称 */
    @TableField("post_name")
    private String postName;

    /** post_sort - 排序 */
    @TableField("post_sort")
    private Integer postSort;

    /** status - 状态（1正常 2停用） */
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