package com.springreport.dto.coedit;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class MqCacheMsgDto {

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
	
	private String index;
	
	private String listId;
	
	private String blockId;
	
	private String rowCol;
	
	private Long id;
	
	/**  
	 * @Fields operate : 操作名称
	 * @author caiyang
	 * @date 2023-11-22 03:55:17 
	 */  
	private String operate;
	
	private  String token;
	
	/**  
	 * @Fields changeBefore : 修改前数据
	 * @author caiyang
	 * @date 2023-11-22 06:45:47 
	 */  
	private Object changeBefore;
	
	/**  
	 * @Fields operateTime : 操作时间
	 * @author caiyang
	 * @date 2023-12-22 10:24:47 
	 */  
	private String operateTime;
}
