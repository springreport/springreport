 /** 
 * 模块：报表系统-LuckysheetCell
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.luckysheetcell;

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
* @Description: luckysheet_cell - 
* @author 
* @date 2023-10-08 08:35:15
* @version V1.0  
 */
@Data
@TableName("luckysheet_cell")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LuckysheetCell extends PageEntity {

    /** id - 主键 */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /** block_id -  */
    @TableField("block_id")
    private String blockId;

    /** row_no - 横坐标 */
    @TableField("row_no")
    private Integer rowNo;

    /** column_no - 纵坐标 */
    @TableField("column_no")
    private Integer columnNo;

    /** sheet_index -  */
    @TableField("sheet_index")
    private String sheetIndex;

    /** list_id -  */
    @TableField("list_id")
    private String listId;

    /** cell_data - 单元格数据信息 */
    @TableField("cell_data")
    private String cellData;

    /** del_flag -  */
    @TableField("del_flag")
    private Integer delFlag;
}