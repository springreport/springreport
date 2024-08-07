package com.springreport.dto.reportdatasource;

import java.util.List;

import lombok.Data;

@Data
public class MesReportDatasourceDto {

	/**
	 * 数据源类型
	 */
	List<Integer> datasourceType;
	
	/**  
	 * @Fields merchantNo : 商户号
	 * @author caiyang
	 * @date 2023-12-27 02:53:51 
	 */  
	private String merchantNo;
}
