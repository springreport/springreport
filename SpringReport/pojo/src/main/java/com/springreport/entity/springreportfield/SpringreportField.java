 /** 
 * 模块：报表系统-SpringreportField
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.entity.springreportfield;

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
* @Description: springreport_field - 
* @author 
* @date 2025-03-18 10:36:28
* @version V1.0  
 */
@Data
@TableName("springreport_field")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpringreportField extends PageEntity {

    /** id - 主键id */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;
    
    /** merchant_no - 商户号 */
    @TableField("merchant_no")
    private String merchantNo;
    
    /** field_name - 行业名称 */
    @TableField("field_name")
    private String fieldName;

    /** type - 分类 1excel报表 2word报表 3大屏 */
    @TableField("type")
    private Integer type;

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