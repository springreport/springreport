package com.springreport.dto.reportRangeAuthUser;

import lombok.Data;

@Data
public class RangeAuthUserDto {

	/**  
	 * @Fields id : 用户id
	 * @author caiyang
	 * @date 2024-06-05 11:27:34 
	 */  
	private Long id;
	
	/**  
	 * @Fields userName : 用户名称
	 * @author caiyang
	 * @date 2024-06-05 11:28:05 
	 */  
	private String userName;
	
	/**  
	 * @Fields authType : 授权类型
	 * @author caiyang
	 * @date 2024-06-07 03:57:19 
	 */  
	private Integer authType = 1;
}
