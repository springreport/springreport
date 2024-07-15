 /** 
 * 模块：报表系统-OnlineTplSheet
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.onlinetplsheet;

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
* @Description: online_tpl_sheet - 
* @author 
* @date 2023-02-14 11:28:34
* @version V1.0  
 */
@Data
@TableName("online_tpl_sheet")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnlineTplSheet extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** sheet_name - sheet名称 */
    @TableField("sheet_name")
    private String sheetName;

    /** sheet_order - sheet顺序 */
    @TableField("sheet_order")
    private Integer sheetOrder;

    /** rowlen - 单元格高度信息 */
    @TableField("rowlen")
    private String rowlen;

    /** columnlen - 单元格宽度信息 */
    @TableField("columnlen")
    private String columnlen;

    /** merge - 单元格合并信息 */
    @TableField("merge")
    private String merge;

    /** border_info - 边框信息 */
    @TableField("border_info")
    private String borderInfo;

    /** frozen - 冻结属性 */
    @TableField("frozen")
    private String frozen;

    /** images - 图片 */
    @TableField("images")
    private String images;

    /** sheet_index - sheet页唯一索引 */
    @TableField("sheet_index")
    private String sheetIndex;

    /** calc_chain - 计算链，有公式的单元格信息 */
    @TableField("calc_chain")
    private String calcChain;

    /** hyper_link - 超衔接 */
    @TableField("hyper_link")
    private String hyperLink;

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