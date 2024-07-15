package com.springreport.dto.reporttpl;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**  
 * @ClassName: ResReportSettingsDto
 * @Description: 报表模板设置返回结果用参数实体类
 * @author caiyang
 * @date 2021-05-25 03:52:57 
*/  
@Data
public class ResReportTplSettingsDto {

	/**  
	 * @Fields isIndex : 是否显示行号
	 * @author caiyang
	 * @date 2021-08-19 06:29:23 
	 */ 
	private Integer isIndex;
	
	/**
	* @Feilds:cells 单元格属性
	* @author caiyang
	*/  
	private List<Map<String, Object>> cells;
}
