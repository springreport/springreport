package com.springreport.base;

import lombok.Data;

/**  
 * @ClassName: ScreenContentDto
 * @Description: 大屏内容用实体类
 * @author caiyang
 * @date 2021-11-30 03:00:25 
*/  
@Data
public class ScreenContentDto {
	
	/**  
	 * @Fields id : 主键id
	 * @author caiyang
	 * @date 2021-11-30 05:02:00 
	 */ 
	private Long id;

	/**  
	 * @Fields componentsType : 组件类型
	 * @author caiyang
	 * @date 2021-11-30 03:12:36 
	 */ 
	private String componentsType;
	
	/**  
	 * @Fields x : 组件横坐标
	 * @author caiyang
	 * @date 2021-11-30 03:13:54 
	 */ 
	private String x;
	
	/**  
	 * @Fields y : 组件纵坐标
	 * @author caiyang
	 * @date 2021-11-30 03:14:18 
	 */ 
	private String y;
	
	/**  
	 * @Fields isRefresh : 是否定时刷新
	 * @author caiyang
	 * @date 2021-11-30 03:28:49 
	 */ 
	private Boolean isRefresh;
	
	/**  
	 * @Fields refreshTime : 定时刷新时长
	 * @author caiyang
	 * @date 2021-11-30 03:29:10 
	 */ 
	private String refreshTime;
	
	/**  
	 * @Fields content : 组件配置
	 * @author caiyang
	 * @date 2021-11-30 03:39:51 
	 */ 
	private String content;
	
}
