 /** 
 * 模块：报表系统-Luckysheet
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.luckysheet;

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
* @Description: luckysheet - 
* @author 
* @date 2024-12-07 12:49:33
* @version V1.0  
 */
@Data
@TableName("luckysheet")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Luckysheet extends PageEntity {

    /** id -  */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** block_id -  */
    @TableField("block_id")
    private String blockId;

    /** row_size - 行数 */
    @TableField("row_size")
    private Integer rowSize;

    /** column_size - 列数 */
    @TableField("column_size")
    private Integer columnSize;

    /** sheet_index -  */
    @TableField("sheet_index")
    private String sheetIndex;

    /** list_id -  */
    @TableField("list_id")
    private String listId;

    /** status -  */
    @TableField("status")
    private Integer status;

    /** sheet_order -  */
    @TableField("sheet_order")
    private Integer sheetOrder;

    /** sheet_name - sheet名称 */
    @TableField("sheet_name")
    private String sheetName;

    /** color - sheet颜色 */
    @TableField("color")
    private String color;

    /** hide - 是否隐藏 0不隐藏 1隐藏 */
    @TableField("hide")
    private Integer hide;

    /** merge_info - 单元格合并信息 */
    @TableField("merge_info")
    private String mergeInfo;

    /** rowlen - 行高集合 */
    @TableField("rowlen")
    private String rowlen;

    /** columnlen - 列宽集合 */
    @TableField("columnlen")
    private String columnlen;

    /** rowhidden - 隐藏行集合 */
    @TableField("rowhidden")
    private String rowhidden;

    /** colhidden - 隐藏列集合 */
    @TableField("colhidden")
    private String colhidden;

    /** border_info - 边框信息 */
    @TableField("border_info")
    private String borderInfo;

    /** calcChain - 公式链 */
    @TableField("calcChain")
    private String calcchain;

    /** filter_select - 筛选范围 */
    @TableField("filter_select")
    private String filterSelect;

    /** filter - 筛选的具体设置 */
    @TableField("filter")
    private String filter;

    /** luckysheet_alternateformat_save - 交替颜色配置 */
    @TableField("luckysheet_alternateformat_save")
    private String luckysheetAlternateformatSave;

    /** luckysheet_conditionformat_save - 条件格式 */
    @TableField("luckysheet_conditionformat_save")
    private String luckysheetConditionformatSave;

    /** image - 图片信息 */
    @TableField("image")
    private String image;

    /** dataVerification - 数据验证信息 */
    @TableField("dataVerification")
    private String dataverification;

    /** frozen - 冻结信息 */
    @TableField("frozen")
    private String frozen;

    /** hyperlink - 超链接信息 */
    @TableField("hyperlink")
    private String hyperlink;

    /** del_flag -  */
    @TableField("del_flag")
    private Integer delFlag;

    /** filterrowhidden - 过滤隐藏行 */
    @TableField("filterrowhidden")
    private String filterrowhidden;

    /** chart - 图表 */
    @TableField("chart")
    private String chart;
}