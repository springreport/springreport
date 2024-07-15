package com.springreport.dto.reporttpldatasource;

import com.springreport.entity.reporttpldatasource.ReportTplDatasource;

import lombok.Data;

/**
* <p>Title: ReportTplDatasourceDto</p>
* <p>Description: 报表模板数据源扩展用实体类</p>
* @author caiyang
* @date 2021年3月21日
*/
@Data
public class ReportTplDatasourceDto extends ReportTplDatasource{

	/**
	* @Feilds:dataSourceName 数据源名称
	* @author caiyang
	*/  
	private String dataSourceName;
	
	/**
	* @Feilds:dataSourceCode 数据源代码
	* @author caiyang
	*/  
	private String dataSourceCode;
	
	/**  
	 * @Fields type : 数据源类型
	 * @author caiyang
	 * @date 2021-11-16 05:32:30 
	 */ 
	private Integer type;
			
    /**  
     * @Fields apiColumns : api 数据列
     * @author caiyang
     * @date 2021-11-17 02:12:50 
     */ 
    private String apiColumns;
}
