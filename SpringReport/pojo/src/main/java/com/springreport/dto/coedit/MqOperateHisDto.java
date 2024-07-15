package com.springreport.dto.coedit;

import lombok.Data;

/**  
 * @ClassName: MqOperateHisDto
 * @Description: 保持历史记录用实体类
 * @author caiyang
 * @date 2023-11-22 05:18:49 
*/ 
@Data
public class MqOperateHisDto {

	/**  
	 * @Fields key : 操作关键字
	 * @author caiyang
	 * @date 2023-11-22 05:19:15 
	 */  
	private String key;
	
	/**  
	 * @Fields operate : 操作
	 * @author caiyang
	 * @date 2023-11-22 05:58:09 
	 */  
	private String operate;
	
	/**  
	 * @Fields changeBefore : 修改前数据
	 * @author caiyang
	 * @date 2023-11-22 05:58:43 
	 */  
	private Object changeBefore;
	
	/**  
	 * @Fields changeAfter : 修改后数据
	 * @author caiyang
	 * @date 2023-11-22 06:04:22 
	 */  
	private Object changeAfter;
	
	/**  
	 * @Fields token : token信息
	 * @author caiyang
	 * @date 2023-11-22 06:13:12 
	 */  
	private String token;
	
	/**  
	 * @Fields listId : 文档唯一标识
	 * @author caiyang
	 * @date 2023-11-22 06:25:27 
	 */  
	private String listId;
	
	/**  
	 * @Fields index : sheet唯一标识
	 * @author caiyang
	 * @date 2023-11-22 06:25:44 
	 */  
	private String index;
	
	private String rowCol;
	
	/**  
	 * @Fields operateTime : 操作时间
	 * @author caiyang
	 * @date 2023-12-22 10:24:47 
	 */  
	private String operateTime;
	
}
