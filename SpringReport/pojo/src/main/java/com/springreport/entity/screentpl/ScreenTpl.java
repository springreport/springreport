 /** 
 * 模块：报表系统-ScreenTpl
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.screentpl;

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
* @Description: screen_tpl - 
* @author 
* @date 2023-01-03 09:13:21
* @version V1.0  
 */
@Data
@TableName("screen_tpl")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScreenTpl extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** merchant_no - 商户号 */
    @TableField("merchant_no")
    private String merchantNo;

    /** tpl_code - 模板标识 */
    @TableField("tpl_code")
    private String tplCode;

    /** tpl_name - 模板名称 */
    @TableField("tpl_name")
    private String tplName;

    /** screen_type - 大屏类型 */
    @TableField("screen_type")
    private Long screenType;

    /** width - 大屏宽度 */
    @TableField("width")
    private Integer width;

    /** height - 大屏高度 */
    @TableField("height")
    private Integer height;

    /** img_url - 背景图 */
    @TableField("img_url")
    private String imgUrl;
    
    /** background - 背景色 */
    @TableField("background")
    private String background;

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
    
    /** is_template - 是否是模板 1是 2否 */
    @TableField("is_template")
    private Integer isTemplate;

    /** template_field - 模板所属行业 */
    @TableField("template_field")
    private Long templateField;
    
    /** is_example - 是否演示示例 1是 2否 */
    @TableField("is_example")
    private Integer isExample;
}