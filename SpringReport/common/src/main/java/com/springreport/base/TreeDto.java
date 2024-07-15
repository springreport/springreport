package com.springreport.base;

import java.util.List;

import lombok.Data;

@Data
public class TreeDto extends BaseEntity{

	/**  
	 * @Fields id : id
	 * @author caiyang
	 * @date 2023-01-28 08:46:42 
	 */  
	private Long id;
	
	/**  
	 * @Fields label : label
	 * @author caiyang
	 * @date 2023-01-28 08:46:59 
	 */  
	private String label;
	
	/**  
	 * @Fields children : 子节点
	 * @author caiyang
	 * @date 2023-01-28 08:47:32 
	 */  
//	private List<TreeDto> children;
}
