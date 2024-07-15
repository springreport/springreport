package com.springreport.dto.coedit;

import lombok.Data;

@Data
public class MqMsgDto {

	/**  
	 * @Fields type : 类型
	 * @author caiyang
	 * @date 2023-09-13 03:52:15 
	 */  
	private String type;
	
	/**  
	 * @Fields params : 参数
	 * @author caiyang
	 * @date 2023-09-13 03:52:36 
	 */  
	private String params;
	
	
	/**  
	 * @Fields method : 方法名称
	 * @author caiyang
	 * @date 2023-09-13 05:33:09 
	 */  
	private String method;
	
	/**  
	 * @Fields sqls : 执行的sql
	 * @author caiyang
	 * @date 2023-09-13 05:57:36 
	 */  
	private String sqls;
}
