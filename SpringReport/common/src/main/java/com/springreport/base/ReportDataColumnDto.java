package com.springreport.base;

import lombok.Data;

@Data
public class ReportDataColumnDto {

	/**  
	 * @Fields columnName : 列名
	 * @author caiyang
	 * @date 2022-11-23 07:53:40 
	 */  
	private String columnName;
	
	/**  
	 * @Fields data : 列值
	 * @author caiyang
	 * @date 2022-11-23 07:54:02 
	 */  
	private Object data;
	
	/**  
	 * @Fields idType : 主键生成规则
	 * @author caiyang
	 * @date 2022-11-23 07:54:42 
	 */  
	private Integer idType;
 }
