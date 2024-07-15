package com.springreport.dto.coedit;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class RangeValueDto {

	/**  
	 * @Fields rangeAxis : 范围标识，如A1:B1
	 * @author caiyang
	 * @date 2024-06-12 04:29:17 
	 */  
	private String rangeAxis;
	
	/**  
	 * @Fields sheetIndex : sheet标识
	 * @author caiyang
	 * @date 2024-06-12 04:29:06 
	 */  
	private String sheetIndex;
	
	/**  
	 * @Fields listId : 文档标识
	 * @author caiyang
	 * @date 2024-06-12 04:28:57 
	 */  
	private String listId;
	
	/**  
	 * @Fields range : 范围
	 * @author caiyang
	 * @date 2024-06-13 10:50:50 
	 */  
	private JSONObject range;
}
