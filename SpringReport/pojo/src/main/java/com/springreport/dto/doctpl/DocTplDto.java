package com.springreport.dto.doctpl;

import java.util.List;

import com.springreport.entity.doctpl.DocTpl;

import lombok.Data;

@Data
public class DocTplDto extends DocTpl{
	
	/**
	 * 	报表数据源
	 */
	private List<Long> dataSource;
	
	/**
	 * 	数据源编码
	 */
	private String dataSourceCode;
	
	/**
	 * 	数据源名称
	 */
	private String dataSourceName;
	
	/**  
	 * @Fields datasourceId : 数据源id
	 * @author caiyang
	 * @date 2023-03-29 02:48:41 
	 */  
	private String datasourceId;
	
	/**  
	 * @Fields margins : 页边距
	 * @author caiyang
	 * @date 2024-11-22 12:44:18 
	 */  
	private String margins = "[]";
}
