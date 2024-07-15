/** 
 * 模块：报表系统-SysRole
 */
package com.springreport.controller.sysrole;

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
import com.springreport.constants.Constants;
import com.springreport.dto.sysrole.MesAuthDto;
import com.springreport.entity.sysrole.SysRole;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.sysrole.ISysRoleService;

 /**  
* @Description: SysRolecontroller类
* @author 
* @date 2021-06-15 07:11:49
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/sysRole")
public class SysRoleController extends BaseController {

	/**
	 * iSysRoleService服务注入
	 */
	@Autowired
	private ISysRoleService iSysRoleService;

	/** 
	* @Description: 分页查询表格
	* @param SysRole
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="SysRole",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"sysRole_search"})
	public Response getTableList(@RequestBody SysRole model)
	{
		BaseEntity result = new BaseEntity();
		result = iSysRoleService.tablePagingQuery(model);
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
	@MethodLog(module="SysRole",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"sysRole_getDetail","sysRole_update"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iSysRoleService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param SysRole
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="SysRole",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"roleCode:required#角色代码;length#角色代码#20","roleName:required#角色名称;length#角色名称#40","roleDesc:required#角色描述;length#角色描述#100",})
	@RequiresPermissions(value = {"sysRole_insert"})
	public Response insert(@RequestBody SysRole model) throws Exception
	{
		BaseEntity result = iSysRoleService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param SysRole
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="SysRole",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","roleCode:required#角色代码;length#角色代码#20","roleName:required#角色名称;length#角色名称#40","roleDesc:required#角色描述;length#角色描述#100",})
	@RequiresPermissions(value = {"sysRole_update"})
	public Response update(@RequestBody SysRole model) throws Exception
	{
		BaseEntity result = iSysRoleService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="SysRole",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"sysRole_delete"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iSysRoleService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param SysRole 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="SysRole",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"sysRole_batchDelete"})
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iSysRoleService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @Title: getRoles
	 * @Description: 获取角色
	 * @return
	 * @author caiyang
	 * @date 2021-06-15 11:31:19 
	 */ 
	@RequestMapping(value = "/getRoles",method = RequestMethod.POST)
	@MethodLog(module="SysRole",remark="获取角色",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"sysUser_getDetail","sysUser_update","sysUser_insert","reportTpl_search"},logical = Logical.OR)
	public Response getRoles(@RequestBody SysRole sysRole)
	{
		List<SysRole> result = this.iSysRoleService.getRoles(sysRole);
		return Response.success(result);
	}
	
	/**  
	 * @Title: authed
	 * @Description: 角色菜单功能授权
	 * @param mesAuthDto
	 * @return
	 * @author caiyang
	 * @date 2021-06-16 07:00:39 
	 */ 
	@RequestMapping(value = "/authed",method = RequestMethod.POST)
	@MethodLog(module="SysRole",remark="角色菜单功能授权",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"roleId:required#角色"})
	@RequiresPermissions(value = {"sysRole_authed"})
	public Response authed(@RequestBody MesAuthDto mesAuthDto)
	{
		BaseEntity result = this.iSysRoleService.authed(mesAuthDto);
		return Response.success(result.getStatusMsg());
	}
		
}
