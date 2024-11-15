/** 
 * 模块：报表系统-SysUser
 */
package com.springreport.controller.sysuser;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import com.springreport.base.BaseController;
import com.springreport.base.BaseEntity;
import com.springreport.base.Response;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.dto.coedit.UserTreeDto;
import com.springreport.dto.sysuser.SysUserDto;
import com.springreport.entity.sysuser.SysUser;

import cn.hutool.system.UserInfo;

import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.sysuser.ISysUserService;

 /**  
* @Description: SysUsercontroller类
* @author 
* @date 2021-06-15 07:12:03
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/sysUser")
public class SysUserController extends BaseController {

	/**
	 * iSysUserService服务注入
	 */
	@Autowired
	private ISysUserService iSysUserService;

	/** 
	* @Description: 分页查询表格
	* @param SysUser
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="SysUser",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"sysUser_search"})
	public Response getTableList(@RequestBody SysUserDto model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iSysUserService.tablePagingQuery(model);
		return Response.success(result);
	}

	/** 
	* @Description: 获取详细信息
	* @param id
	* @param @return  BaseEntity
	* @return  
	* @throws 
	*/ 
	@RequestMapping(value = "/getDetail",method = RequestMethod.GET)
	@MethodLog(module="SysUser",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"sysUser_getDetail","sysUser_update"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iSysUserService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param SysUser
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="SysUser",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"userName:required#用户登录名;length#用户登录名#40","userRealName:length#用户真实姓名#40","userEmail:length#用户邮箱#50;email","userMobile:required#手机;length#手机#20;mobile"
		,"password:required#密码","deptId:required#所属部门","postId:required#岗位"})
	@RequiresPermissions(value = {"sysUser_insert"})
	public Response insert(@RequestBody SysUserDto model) throws Exception
	{
		BaseEntity result = iSysUserService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param SysUser
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="SysUser",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","userName:required#用户登录名;length#用户登录名#40","userRealName:length#用户真实姓名#40","userEmail:length#用户邮箱#50;email","userMobile:required#手机;length#手机#20;mobile"
		,"password:required#密码","deptId:required#所属部门","postId:required#岗位"})
	@RequiresPermissions(value = {"sysUser_update"})
	public Response update(@RequestBody SysUserDto model) throws Exception
	{
		BaseEntity result = iSysUserService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="SysUser",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"sysUser_delete"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iSysUserService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param SysUser 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="SysUser",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"sysUser_batchDelete"})
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iSysUserService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: resetPwd
	 * @Description: 重置密码
	 * @author caiyang
	 * @param sysUser
	 * @return Response
	 * @date 2023-12-29 01:12:32 
	 */ 
	@RequestMapping(value = "/resetPwd",method = RequestMethod.POST)
	@MethodLog(module="SysUser",remark="重置密码",operateType=Constants.OPERATE_TYPE_UPDATE)
	@RequiresPermissions(value = {"sysUser_resetPwd"})
	@Check({"id:required#主键ID"})
	public Response resetPwd(@RequestBody SysUser sysUser)
	{
		BaseEntity result = iSysUserService.resetPwd(sysUser);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: changePwd
	 * @Description: 用户修改密码
	 * @author caiyang
	 * @param sysUser
	 * @return Response
	 * @date 2023-12-29 02:46:26 
	 */ 
	@RequestMapping(value = "/changePwd",method = RequestMethod.POST)
	@MethodLog(module="SysUser",remark="修改密码",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"oldPwd:required#旧密码","newPwd:required#旧密码"})
	public Response changePwd(@RequestBody SysUserDto sysUser,@LoginUser UserInfoDto userInfoDto)
	{
		BaseEntity result = iSysUserService.changePwd(sysUser,userInfoDto);
		return Response.success(result.getStatusMsg());
	}
	
	@RequestMapping(value = "/getUsers",method = RequestMethod.POST)
	@MethodLog(module="SysUser",remark="获取用户",operateType=Constants.OPERATE_TYPE_UPDATE)
	public Response getUsers(@RequestBody SysUserDto sysUser,@LoginUser UserInfoDto userInfoDto) {
		List<SysUser> result = this.iSysUserService.getUsers(sysUser, userInfoDto);
		return Response.success(result);
	}
	
	@RequestMapping(value = "/getDeptUserTree",method = RequestMethod.POST)
	@MethodLog(module="SysUser",remark="获取部门用户树",operateType=Constants.OPERATE_TYPE_UPDATE)
	public Response getDeptUserTree(@RequestBody SysUser model) {
		List<UserTreeDto> result = this.iSysUserService.getDeptUserTree(model);
		return Response.success(result);
	}
}
