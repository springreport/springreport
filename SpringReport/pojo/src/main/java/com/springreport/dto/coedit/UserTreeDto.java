package com.springreport.dto.coedit;

import com.springreport.base.BaseEntity;

import lombok.Data;

@Data
public class UserTreeDto extends BaseEntity{

	/**  
	 * @Fields id : id
	 * @author caiyang
	 * @date 2024-11-13 05:27:12 
	 */  
	private String id;
	
	/**  
	 * @Fields name : 名称
	 * @author caiyang
	 * @date 2024-11-13 05:27:05 
	 */  
	private String name;
	
	/**  
	 * @Fields parentId : 上级id
	 * @author caiyang
	 * @date 2024-11-13 05:26:58 
	 */  
	private String parentId;
	
	/**  
	 * @Fields authType : 授权类型
	 * @author caiyang
	 * @date 2024-06-07 03:57:19 
	 */  
	private Integer authType = 1; 
}
