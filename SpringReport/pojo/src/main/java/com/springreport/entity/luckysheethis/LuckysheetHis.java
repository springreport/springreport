 /** 
 * 模块：报表系统-LuckysheetHis
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.luckysheethis;

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
* @Description: luckysheet_his - 
* @author 
* @date 2023-11-27 06:13:36
* @version V1.0  
 */
@Data
@TableName("luckysheet_his")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LuckysheetHis extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** row_no - 行号 */
    @TableField("row_no")
    private Integer rowNo;

    /** col_no - 列号 */
    @TableField("col_no")
    private Integer colNo;

    /** sheet_index - sheet唯一标识 */
    @TableField("sheet_index")
    private String sheetIndex;

    /** list_id - 文档唯一标识 */
    @TableField("list_id")
    private String listId;

    /** bson - 本次修改参数 */
    @TableField("bson")
    private String bson;

    /** before_json - 上次修改参数 */
    @TableField("before_json")
    private String beforeJson;

    /** change_desc - 修改描述 */
    @TableField("change_desc")
    private String changeDesc;

    /** remark - 备注 */
    @TableField("remark")
    private String remark;

    /** type - 修改类型 1整体配置修改 2单元格内容修改 */
    @TableField("type")
    private Integer type;

    /** operate_key - 操作key */
    @TableField("operate_key")
    private String operateKey;

    /** operator - 操作人 */
    @TableField("operator")
    private String operator;

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