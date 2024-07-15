package com.springreport.dto.sysmenu;

import lombok.Data;

/**  
 * @ClassName: MesGetIndexMenuDto
 * @Description: 获取首页菜单用参数实体类
 * @author caiyang
 * @date 2021-06-18 02:16:32 
*/  
@Data
public class MesGetIndexMenuDto {

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
	 * @Fields authTemplateId : 权限模板id
	 * @author caiyang
	 * @date 2023-12-26 11:37:11 
	 */  
	private Long authTemplateId;
}
