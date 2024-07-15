package com.springreport.dto.coedit;


import com.springreport.dto.coedit.WSUserModel;

import lombok.Data;

@Data
public class MqConfigDto {

	/**  
	 * @Fields keyName : 关键字
	 * @author caiyang
	 * @date 2023-09-30 01:44:37 
	 */  
	private String keyName;
	
	/**  
	 * @Fields v : 值
	 * @author caiyang
	 * @date 2023-09-30 01:44:46 
	 */  
	private Object v;
	
	/**  
	 * @Fields id : 主键id
	 * @author caiyang
	 * @date 2023-10-10 01:16:01 
	 */  
	private Long id;
	
	private String blockId;
	
	private String index;
	
	private String listId;
	
	private String operate;
	
	private  WSUserModel wsUserModel;
	
	/**  
	 * @Fields operateTime : 操作时间
	 * @author caiyang
	 * @date 2023-12-22 10:24:47 
	 */  
	private String operateTime;
	
}
