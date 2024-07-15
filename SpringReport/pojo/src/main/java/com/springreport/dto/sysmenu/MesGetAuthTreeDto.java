package com.springreport.dto.sysmenu;

import lombok.Data;

/**  
 * @ClassName: MesGetAuthTreeDto
 * @Description: 获取权限树用实体类
 * @author caiyang
 * @date 2021-06-15 06:48:30 
*/  
@Data
public class MesGetAuthTreeDto {

	/**  
	 * @Fields isAdmin : 是否超级用户
	 * @author caiyang
	 * @date 2021年6月15日18:48:57
	 */ 
	private Integer isAdmin;
	
	/**  
	 * @Fields roleId : 角色id
	 * @author caiyang
	 * @date 2021年6月15日18:49:01
	 */ 
	private Long roleId;
	
	/**  
	 * @Fields parentMenuId : 上级菜单id 一级菜单用0表示
	 * @author caiyang
	 * @date 2021-06-15 06:49:45 
	 */ 
	private Long parentMenuId;
	
	/**  
	 * @Fields menuId : 菜单id
	 * @author caiyang
	 * @date 2021-06-16 07:08:20 
	 */ 
	private Long menuId;
	
	/**  
	 * @Fields authTemplateId : 权限模板id
	 * @author caiyang
	 * @date 2023-12-26 11:37:11 
	 */  
	private Long authTemplateId;
	
}
