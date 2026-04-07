 /** 
 * 模块：报表系统-ReportTplSheet
 * 本文件由代码生成器自动完成,不允许进行修改
 */
package com.springreport.dto.reporttplsheet;

import com.springreport.entity.reporttplsheet.ReportTplSheet;

import lombok.Data;

 /**  
* @Description: report_tpl_sheet - 
* @author 
* @date 2024-02-25 05:10:18
* @version V1.0  
 */
@Data
public class ReportTplSheetDto extends ReportTplSheet {

    /**  
     * @Fields property : 循环过滤属性
     * @author caiyang
     * @date 2026-04-02 12:48:07 
     */  
    private String property;
    
    /**  
     * @Fields filterValue : 循环过滤值
     * @author caiyang
     * @date 2026-04-02 01:12:14 
     */  
     
    private String filterValue;
    
    /**  
     * @Fields source : 来源于哪个sheet
     * @author caiyang
     * @date 2026-04-02 12:48:46 
     */  
    private ReportTplSheetDto source;
}