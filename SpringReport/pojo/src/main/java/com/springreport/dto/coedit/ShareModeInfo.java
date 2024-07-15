package com.springreport.dto.coedit;

import com.alibaba.fastjson.JSONArray;

import lombok.Data;

/**  
 * @ClassName: ShareModeInfo
 * @Description: 共享模式信息
 * @author caiyang
 * @date 2024-01-01 05:12:04 
*/ 
@Data
public class ShareModeInfo {

	/**  
	 * @Fields gridKey : 文档唯一标识
	 * @author caiyang
	 * @date 2024-01-01 05:12:21 
	 */  
	private String gridKey;
	
	/**  
	 * @Fields userId : 用户id
	 * @author caiyang
	 * @date 2024-01-01 05:12:37 
	 */  
	private Long userId;
	
	/**  
	 * @Fields userName : 用户登录名
	 * @author caiyang
	 * @date 2024-01-01 05:12:51 
	 */  
	private String userName;
	
	/**  
	 * @Fields userRealName : 用户姓名
	 * @author caiyang
	 * @date 2024-01-01 05:13:05 
	 */  
	private String userRealName;
	
	/**  
	 * @Fields scrollLeft : 横向滚动条位置
	 * @author caiyang
	 * @date 2024-01-03 08:54:20 
	 */  
	private Integer scrollLeft = 0;
	
	/**  
	 * @Fields scrollTop : 纵向滚动条位置
	 * @author caiyang
	 * @date 2024-01-03 08:54:43 
	 */  
	private Integer scrollTop = 0;
	
	/**  
	 * @Fields sheetIndex : 当前index
	 * @author caiyang
	 * @date 2024-01-03 08:55:02 
	 */  
	private String sheetIndex;
	
	/**  
	 * @Fields range : 当前选中位置
	 * @author caiyang
	 * @date 2024-01-03 09:57:33 
	 */  
	private JSONArray range;
}
