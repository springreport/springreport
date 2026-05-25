package com.springreport.dto.onlinetpl;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

/**  
 * @ClassName: MesRangeAuthDto
 * @Description: 协同文档授权用参数实体类
 * @author caiyang
 * @date 2024-03-09 01:05:07 
*/ 
@Data
public class MesRangeAuthDto {

	private String listId;
	
	private JSONObject rangeAuth;
	
	/**  
	 * @Fields authType : 保护类型 1范围保护 2工作表保护
	 * @author caiyang
	 * @date 2026-05-23 01:46:27 
	 */  
	 
	private Integer authType = 1;
	
	private String sheetIndex;
}
