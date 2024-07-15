package com.springreport.dto.sysmenu;

import java.util.List;

import com.springreport.entity.sysmenu.SysMenu;

import lombok.Data;

/**
* <p>Title: MenuTreeDto</p>
* <p>Description: 菜单树用实体类</p>
* @author caiyang
* @date 2020年6月10日17:02:36
*/
@Data
public class MenuTreeDto extends SysMenu{

	/**
	* @Fields children 子节点
	* @author caiyang
	*/  
//	private	List<MenuTreeDto> children;
	
	/**  
	 * @Fields menuId : 菜单id
	 * @author caiyang
	 * @date 2020-06-11 10:55:10 
	 */ 
	private String menuId;
}
