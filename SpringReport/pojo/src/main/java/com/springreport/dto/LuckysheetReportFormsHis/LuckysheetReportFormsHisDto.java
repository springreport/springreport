package com.springreport.dto.LuckysheetReportFormsHis;

import com.springreport.entity.luckysheetreportformshis.LuckysheetReportFormsHis;

import lombok.Data;

@Data
public class LuckysheetReportFormsHisDto extends LuckysheetReportFormsHis{

	/**  
	 * @Fields tplName : 报表名称
	 * @author caiyang
	 * @date 2023-01-31 08:10:41 
	 */  
	private String tplName;
	
	/**  
	 * @Fields sheetName : sheet名称
	 * @author caiyang
	 * @date 2023-01-31 08:10:56 
	 */  
	private String sheetName;
	
	/**  
	 * @Fields datasourceName : 数据源名称
	 * @author caiyang
	 * @date 2023-01-31 08:18:11 
	 */  
	private String datasourceName;
	
	/**  
	 * @Fields creatorName : 操作人
	 * @author caiyang
	 * @date 2023-01-31 08:18:36 
	 */  
	private String creatorName;
}
