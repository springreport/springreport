package com.springreport.api.sysuser;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.sysuser.SysUser;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.coedit.UserTreeDto;
import com.springreport.dto.sysuser.SysUserDto;

 /**  
* @Description: SysUser服务接口
* @author 
* @date 2021-06-15 07:12:03
* @version V1.0  
 */
public interface ISysUserService extends IService<SysUser> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(SysUserDto model);

	/**
	*<p>Title: getDetail</p>
	*<p>Description: 获取详情</p>
	* @author caiyang
	* @param id
	* @return
	*/
	BaseEntity getDetail(Long id);
	
	/**
	*<p>Title: insert</p>
	*<p>Description: 新增数据</p>
	* @author caiyang
	* @param model
	* @return
	*/
	BaseEntity insert(SysUserDto sysUserDto);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(SysUserDto model);
	
	/**
	*<p>Title: delete</p>
	*<p>Description: 单条删除数据</p>
	* @author caiyang
	* @param model
	* @return
	*/
	BaseEntity delete(Long id);
	
	/**
	*<p>Title: deleteBatch</p>
	*<p>Description: 批量删除数据</p>
	* @author caiyang
	* @param list
	* @return
	*/
	BaseEntity deleteBatch(List<Long> ids);
	
	/**  
	 * @MethodName: resetPwd
	 * @Description: 重置密码
	 * @author caiyang
	 * @param sysUser
	 * @return BaseEntity
	 * @date 2023-12-29 01:13:18 
	 */ 
	BaseEntity resetPwd(SysUser sysUser);
	
	/**  
	 * @MethodName: changePwd
	 * @Description: 用户修改密码
	 * @author caiyang
	 * @param sysUserDto
	 * @param userInfoDto
	 * @return BaseEntity
	 * @date 2023-12-29 02:55:45 
	 */ 
	BaseEntity changePwd(SysUserDto sysUserDto,UserInfoDto userInfoDto);
	
	/**  
	 * @MethodName: getUsers
	 * @Description: 获取所有用户
	 * @author caiyang
	 * @param sysUserDto
	 * @param userInfoDto
	 * @return List<SysUser>
	 * @date 2024-03-02 11:31:16 
	 */ 
	List<SysUser> getUsers(SysUserDto sysUserDto,UserInfoDto userInfoDto);
	
	/**  
	 * @MethodName: getDeptUserTree
	 * @Description: 获取部门用户树
	 * @author caiyang
	 * @param model
	 * @return List<UserTreeDto>
	 * @date 2024-11-13 05:42:59 
	 */ 
	List<UserTreeDto> getDeptUserTree(SysUser model);
}
