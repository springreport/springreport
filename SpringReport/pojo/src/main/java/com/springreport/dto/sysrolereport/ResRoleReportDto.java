package com.springreport.dto.sysrolereport;

import lombok.Data;

/**  
 * @ClassName: ResRoleReportDto
 * @Description: 查看报表返回结果用实体类
 * @author caiyang
 * @date 2021-06-22 06:16:00 
*/  
@Data
public class ResRoleReportDto {

	
	/**  
	 * @Fields reportId : 报表id
	 * @author caiyang
	 * @date 2021-06-22 06:16:19 
	 */ 
	private Long reportId;
	
	/**  
	 * @Fields reportName : 报表名称
	 * @author caiyang
	 * @date 2021-06-22 06:16:31 
	 */ 
	private String reportName;
	
	/**  
	 * @Fields reportType : 报表类型
	 * @author caiyang
	 * @date 2021-06-22 06:44:08 
	 */ 
	private String reportType;
}
