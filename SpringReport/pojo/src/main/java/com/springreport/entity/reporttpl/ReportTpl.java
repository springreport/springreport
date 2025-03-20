 /** 
 * 模块：报表系统-ReportTpl
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.reporttpl;

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
* @Description: report_tpl - 
* @author 
* @date 2023-03-28 05:28:13
* @version V1.0  
 */
@Data
@TableName("report_tpl")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportTpl extends PageEntity {

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

    /** tpl_type - 报表类型 1展示报表 2填报报表 */
    @TableField("tpl_type")
    private Integer tplType;

    /** report_type - 报表类型 */
    @TableField("report_type")
    private Long reportType;

    /** is_index - 是否显示行号1是 2否 */
    @TableField("is_index")
    private Integer isIndex;

    /** is_param_merge - 数据集参数是否合并 1是 2否 */
    @TableField("is_param_merge")
    private Integer isParamMerge;

    /** config - luckysheet行高，列宽等额外配置 */
    @TableField("config")
    private String config;

    /** frozen - 冻结属性 */
    @TableField("frozen")
    private String frozen;

    /** images - 图片 */
    @TableField("images")
    private String images;

    /** view_auth - 查看权限 1所有人可见 2指定角色 */
    @TableField("view_auth")
    private Integer viewAuth;

    /** design_pwd - 设计密码 */
    @TableField("design_pwd")
    private String designPwd;

    /** export_encrypt - 导出是否加密 1是 2否 */
    @TableField("export_encrypt")
    private Integer exportEncrypt;

    /** sheet_index - sheet页唯一索引 */
    @TableField("sheet_index")
    private String sheetIndex;

    /** calc_chain - 计算链，有公式的单元格信息 */
    @TableField("calc_chain")
    private String calcChain;

    /** concurrency_flag - 并发控制 1是 2否 */
    @TableField("concurrency_flag")
    private Integer concurrencyFlag;

    /** show_toolbar - 预览是否展示工具栏 1是 2否 */
    @TableField("show_toolbar")
    private Integer showToolbar;

    /** show_row_header - 预览是否显示行标题1是 2否 */
    @TableField("show_row_header")
    private Integer showRowHeader;

    /** show_col_header - 预览是否显示列标题 1是 2否 */
    @TableField("show_col_header")
    private Integer showColHeader;

    /** show_gridlines - 预览是否显示网格线 1是 2否 */
    @TableField("show_gridlines")
    private Integer showGridlines;
    
    /** refresh_page - 上报数据后是否刷新页面 1是 2否 */
    @TableField("refresh_page")
    private Integer refreshPage;

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
    
    /** coedit_flag - 开启协同 1是 2否 */
    @TableField("coedit_flag")
    private Integer coeditFlag;
    
    /** is_template - 是否是模板 1是 2否 */
    @TableField("is_template")
    private Integer isTemplate;
    
    /** template_field - 模板所属行业 */
    @TableField("template_field")
    private Long templateField;
}