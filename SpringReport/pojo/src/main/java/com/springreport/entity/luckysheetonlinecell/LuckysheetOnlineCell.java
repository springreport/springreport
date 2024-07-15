 /** 
 * 模块：报表系统-LuckysheetOnlineCell
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.luckysheetonlinecell;

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
* @Description: luckysheet_online_cell - 
* @author 
* @date 2023-02-13 05:27:44
* @version V1.0  
 */
@Data
@TableName("luckysheet_online_cell")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LuckysheetOnlineCell extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** tpl_id - 模板id */
    @TableField("tpl_id")
    private Long tplId;

    /** sheet_id - 模板sheetid */
    @TableField("sheet_id")
    private Long sheetId;

    /** coordsx - 横坐标 */
    @TableField("coordsx")
    private Integer coordsx;

    /** coordsy - 纵坐标 */
    @TableField("coordsy")
    private Integer coordsy;

    /** cell_data - 单元格值 */
    @TableField("cell_data")
    private String cellData;

    /** cell_value_type - 单元格内容类型 1固定值 2变量 3循环块 */
    @TableField("cell_value_type")
    private Integer cellValueType;

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

    /** ct - 单元格值格式 */
    @TableField("ct")
    private String ct;

    /** ff - 字体 */
    @TableField("ff")
    private String ff;

    /** bg - 背景颜色 */
    @TableField("bg")
    private String bg;

    /** fc - 字体颜色 */
    @TableField("fc")
    private String fc;

    /** bl - 粗体 0 常规  1加粗 */
    @TableField("bl")
    private Integer bl;

    /** it - 斜体 0 常规  1 斜体 */
    @TableField("it")
    private Integer it;

    /** fs - 字体大小 */
    @TableField("fs")
    private Integer fs;

    /** cl - 删除线0 常规 、 1 删除线 */
    @TableField("cl")
    private Integer cl;

    /** vt - 垂直对齐 0 中间、1 上、2下 */
    @TableField("vt")
    private Integer vt;

    /** ht - 水平对齐 0 居中、1 左、2右 */
    @TableField("ht")
    private Integer ht;
}