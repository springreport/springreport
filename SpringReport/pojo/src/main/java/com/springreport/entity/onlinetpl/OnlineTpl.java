 /** 
 * 模块：报表系统-OnlineTpl
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.onlinetpl;

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
* @Description: online_tpl - 
* @author 
* @date 2023-11-25 08:26:07
* @version V1.0  
 */
@Data
@TableName("online_tpl")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnlineTpl extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** merchant_no - 商户号 */
    @TableField("merchant_no")
    private String merchantNo;

    /** report_id - 来源报表id */
    @TableField("report_id")
    private Long reportId;

    /** tpl_name - 文档名称 */
    @TableField("tpl_name")
    private String tplName;

    /** list_id - 文档唯一标识 */
    @TableField("list_id")
    private String listId;

    /** view_auth - 查看权限 1所有人可见 2指定角色 */
    @TableField("view_auth")
    private Integer viewAuth;

    /** design_pwd - 设计密码 */
    @TableField("design_pwd")
    private String designPwd;

    /** export_encrypt - 导出是否加密 1是 2否 */
    @TableField("export_encrypt")
    private Integer exportEncrypt;

    /** source - 来源方式 1自己创建 2动态报表另存为 */
    @TableField("source")
    private Integer source;

    /** data_load_type - 数据加载方式 1全部加载2单sheet加载 */
    @TableField("data_load_type")
    private Integer dataLoadType;

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
    
    /** report_type - 报表类型 */
    @TableField("report_type")
    private Long reportType;
}