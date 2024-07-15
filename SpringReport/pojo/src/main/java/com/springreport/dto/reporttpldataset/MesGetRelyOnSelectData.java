package com.springreport.dto.reporttpldataset;

import java.util.Map;

import lombok.Data;

@Data
public class MesGetRelyOnSelectData {

	/**  
	 * @Fields selectContent : 查询sql
	 * @author caiyang
	 * @date 2022-08-08 08:15:22 
	 */  
	private String selectContent;
	
	/**  
	 * @Fields datasourceId : 数据源id
	 * @author caiyang
	 * @date 2022-08-08 08:15:53 
	 */  
	private Long datasourceId;
	
	/**  
	 * @Fields params : 参数
	 * @author caiyang
	 * @date 2022-08-08 08:17:41 
	 */  
	private Map<String, Object> params;
}
