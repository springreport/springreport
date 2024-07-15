 /** 
 * 模块：报表系统-DocTplSettings
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.doctplsettings;

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
* @Description: doc_tpl_settings - 
* @author 
* @date 2024-05-02 08:55:39
* @version V1.0  
 */
@Data
@TableName("doc_tpl_settings")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocTplSettings extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;
    
    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** header - 页眉 */
    @TableField("header")
    private String header;

    /** footer - 页脚 */
    @TableField("footer")
    private String footer;

    /** main - 内容 */
    @TableField("main")
    private String main;

    /** paper_direction - 纸张方向 vertical纵向 horizontal横向 */
    @TableField("paper_direction")
    private String paperDirection;

    /** width - 宽度 */
    @TableField("width")
    private Integer width;

    /** height - 高度 */
    @TableField("height")
    private Integer height;
    
    /** margins - 页边距 */
    @TableField("margins")
    private String margins;
    
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
    
    /** watermark - 水印 */
    @TableField("watermark")
    private String watermark;
}