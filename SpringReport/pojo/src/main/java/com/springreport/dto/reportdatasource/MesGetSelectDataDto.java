package com.springreport.dto.reportdatasource;

import lombok.Data;

@Data
public class MesGetSelectDataDto {

	/**
	 * 数据源id
	 */
	private Long dataSourceId;
	
	/**
	 * sql语句
	 */
	private String selectContent;
}
