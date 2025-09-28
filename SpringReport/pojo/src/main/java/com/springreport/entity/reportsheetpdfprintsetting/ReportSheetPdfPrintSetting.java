 /** 
 * 模块：报表系统-ReportSheetPdfPrintSetting
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.reportsheetpdfprintsetting;

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
* @Description: report_sheet_pdf_print_setting - 
* @author 
* @date 2025-02-10 02:03:13
* @version V1.0  
 */
@Data
@TableName("report_sheet_pdf_print_setting")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportSheetPdfPrintSetting extends PageEntity {

    /** id - 主键id */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** tpl_sheet_id - 模板sheeti d */
    @TableField("tpl_sheet_id")
    private Long tplSheetId;

    /** page_type - 纸张类型 1 A3 2 A4 3 A5 4 A6 5 B2 6B3 7B4 8 B5 9LETTER 10 LEGAL */
    @TableField("page_type")
    private Integer pageType;

    /** page_layout - 纸张布局 1纵向 2横向 */
    @TableField("page_layout")
    private Integer pageLayout;

    /** page_header_show - 页眉是否显示 1是 2否 */
    @TableField("page_header_show")
    private Integer pageHeaderShow;

    /** page_header_content - 页眉显示内容 */
    @TableField("page_header_content")
    private String pageHeaderContent;

    /** page_header_position - 页眉显示位置 1左 2中 3右 */
    @TableField("page_header_position")
    private Integer pageHeaderPosition;

    /** water_mark_show - 水印是否显示 1是 2否 */
    @TableField("water_mark_show")
    private Integer waterMarkShow;

    /** water_mark_type - 水印类型 1文本 2图片 */
    @TableField("water_mark_type")
    private Integer waterMarkType;

    /** water_mark_content - 文本水印内容 */
    @TableField("water_mark_content")
    private String waterMarkContent;

    /** water_mark_img - 图片水印url */
    @TableField("water_mark_img")
    private String waterMarkImg;

    /** water_mark_opacity - 水印透明度 大于0小于1的值 */
    @TableField("water_mark_opacity")
    private Float waterMarkOpacity;

    /** page_show - 页码是否显示 1是 2否 */
    @TableField("page_show")
    private Integer pageShow;

    /** page_position - 页码显示位置 1左 2中 3右 */
    @TableField("page_position")
    private Integer pagePosition;

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

    /** horizontal_page - 是否水平分页 1是 2否 */
    @TableField("horizontal_page")
    private Integer horizontalPage;

    /** horizontal_page_column - 分页列，多个列用,隔开 */
    @TableField("horizontal_page_column")
    private String horizontalPageColumn;

    /** fixed_header - 固定表头 1是 2否 */
    @TableField("fixed_header")
    private Integer fixedHeader;

    /** fixed_header_start - 固定表头起始行 */
    @TableField("fixed_header_start")
    private Integer fixedHeaderStart;

    /** fixed_header_end - 固定表头结束行 */
    @TableField("fixed_header_end")
    private Integer fixedHeaderEnd;

    /** custom_margin - 自定义页边距 1是 2否 */
    @TableField("custom_margin")
    private Integer customMargin;

    /** left_margin - 左边距 */
    @TableField("left_margin")
    private Integer leftMargin;

    /** right_margin - 右边距 */
    @TableField("right_margin")
    private Integer rightMargin;

    /** top_margin - 上边距 */
    @TableField("top_margin")
    private Integer topMargin;

    /** bottom_margin - 下边距 */
    @TableField("bottom_margin")
    private Integer bottomMargin;
    
    /** font_multi - 字体缩放倍数 */
    @TableField("font_multi")
    private Float fontMulti;

    /** rowheight_multi - 行高缩放倍数 */
    @TableField("rowheight_multi")
    private Float rowheightMulti;
}