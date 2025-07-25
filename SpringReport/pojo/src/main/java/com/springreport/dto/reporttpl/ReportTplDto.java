package com.springreport.dto.reporttpl;

import java.util.List;

import com.springreport.entity.reporttpl.ReportTpl;

import lombok.Data;

/**  
* @Description: report_tpl扩展用实体类
* @author 
* @date 2021年2月13日15:53:48
* @version V1.0  
 */
@Data
public class ReportTplDto extends ReportTpl{

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
	 * 	报表类型名称
	 */
	private String reportTypeName;
	
	/**  
	 * @Fields roles : 角色
	 * @author caiyang
	 * @date 2022-07-12 04:07:48 
	 */  
	private List<Long> roles;
	
	/**  
	 * @Fields datasourceId : 数据源id
	 * @author caiyang
	 * @date 2023-03-29 02:48:41 
	 */  
	private String datasourceId;
	
	/**  
	 * @Fields allowUpdate : 是否允许协同编辑，不允许则不将模板数据放到redis中，每次都从数据库获取
	 * @author caiyang
	 * @date 2025-07-19 08:56:56 
	 */  
	private boolean allowUpdate;
	
}
