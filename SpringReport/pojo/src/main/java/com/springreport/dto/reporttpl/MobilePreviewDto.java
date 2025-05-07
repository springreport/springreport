package com.springreport.dto.reporttpl;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class MobilePreviewDto {

	/**  
	 * @Fields reportName : 报表名称
	 * @author caiyang
	 * @date 2023-06-29 09:41:19 
	 */  
	private String reportName;
	
	/**  
	 * @Fields reports : 报表内容
	 * @author caiyang
	 * @date 2023-06-29 09:42:22 
	 */  
	private List<ResMobileReport> reports;
	
	/**  
	 * @Fields pagination : 分页信息
	 * @author caiyang
	 * @date 2023-01-16 09:59:01 
	 */  
	private Map<String, Object> pagination;
	
	/**  
	 * @Fields activeSheet : 当前选中的sheet
	 * @author caiyang
	 * @date 2025-05-07 01:48:03 
	 */  
	private String activeSheet;
}
