package com.springreport.dto.report;

import java.util.List;

import lombok.Data;

/**  
 * @ClassName: TreeDto
 * @Description: 树结构实体类
 * @author caiyang
 * @date 2021-06-21 06:47:30 
*/  
@Data
public class TreeDto {
	
	/**  
	 * @Fields treeData : 报表数
	 * @author caiyang
	 * @date 2021-06-21 06:47:08 
	 */ 
	private List<?> treeData;

	/**
	* @Feilds:authed 已经有的权限
	* @author caiyang
	*/  
	private List<String> authed;
}
