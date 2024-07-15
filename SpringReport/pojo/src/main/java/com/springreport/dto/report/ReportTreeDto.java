package com.springreport.dto.report;

import java.util.List;

import lombok.Data;

/**  
 * @ClassName: ReportTreeDto
 * @Description: 报表数实体类
 * @author caiyang
 * @date 2021-06-21 06:42:47 
*/  
@Data
public class ReportTreeDto {

	/**  
	 * @Fields reportId : 报表id
	 * @author caiyang
	 * @date 2021-06-21 06:43:41 
	 */ 
	private String reportId;
	
	/**  
	 * @Fields reportName : 报表名称
	 * @author caiyang
	 * @date 2021-06-21 06:43:52 
	 */ 
	private String reportName;
	
	/**  
	 * @Fields children : 子节点
	 * @author caiyang
	 * @date 2021-06-21 06:46:13 
	 */ 
	private List<ReportTreeDto> children;
	
}
