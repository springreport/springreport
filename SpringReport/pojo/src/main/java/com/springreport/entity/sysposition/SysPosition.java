 /** 
 * 模块：报表系统-SysPosition
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.sysposition;

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
* @Description: sys_position - 
* @author 
* @date 2022-06-24 10:54:59
* @version V1.0  
 */
@Data
@TableName("sys_position")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysPosition extends PageEntity {

    /** id -  */
    
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Integer id;

    /** area_name - 地区名称 */
    @TableField("area_name")
    private String areaName;

    /** area_code - 地区编码 */
    @TableField("area_code")
    private Integer areaCode;

    /** city_code - 城市编码 */
    @TableField("city_code")
    private String cityCode;

    /** level - 地区等级 */
    @TableField("level")
    private Boolean level;

    /** area_index - 地区索引 */
    @TableField("area_index")
    private String areaIndex;
}