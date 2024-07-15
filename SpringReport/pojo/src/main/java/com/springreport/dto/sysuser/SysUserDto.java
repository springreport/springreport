package com.springreport.dto.sysuser;

import java.util.List;

import com.springreport.entity.sysuser.SysUser;

import lombok.Data;

/**  
 * @ClassName: SysUserDto
 * @Description: 用户表扩展用实体类
 * @author caiyang
 * @date 2021-06-15 08:04:10 
*/  
@Data
public class SysUserDto extends SysUser{

	/**  
	 * @Fields roleId : 角色id
	 * @author caiyang
	 * @date 2021-06-15 08:04:04 
	 */ 
	private Long roleId;
	
	/**  
	 * @Fields roleName : 角色名称
	 * @author caiyang
	 * @date 2021-06-15 02:48:42 
	 */ 
	private String roleName;
	
	/**  
	 * @Fields deptId : 部门id
	 * @author caiyang
	 * @date 2023-12-26 02:06:50 
	 */  
	private Long deptId;
	
	/**  
	 * @Fields deptName : 部门名称
	 * @author caiyang
	 * @date 2023-12-26 02:09:48 
	 */  
	private String deptName;
	
	/**  
	 * @Fields postId : 岗位id
	 * @author caiyang
	 * @date 2023-12-26 02:10:03 
	 */  
	private Long postId;
	
	/**  
	 * @Fields postName : 岗位名称
	 * @author caiyang
	 * @date 2023-12-26 02:10:21 
	 */  
	private String postName;
	
	/**  
	 * @Fields deptIds : 部门以及所有的子部门集合
	 * @author caiyang
	 * @date 2023-12-26 02:47:06 
	 */  
	private List<Long> deptIds;
	
	/**  
	 * @Fields newPwd : 新密码
	 * @author caiyang
	 * @date 2023-12-29 02:45:46 
	 */  
	private String newPwd;
	
	/**  
	 * @Fields oldPwd : 旧密码
	 * @author caiyang
	 * @date 2023-12-29 02:45:58 
	 */  
	private String oldPwd;
}
