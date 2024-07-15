/** 
 * 模块：报表系统-SysMenu
 */
package com.springreport.controller.sysmenu;

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
import com.springreport.dto.sysmenu.AuthTreeDto;
import com.springreport.dto.sysmenu.IndexMenuTreeDto;
import com.springreport.entity.sysmenu.SysMenu;
import com.springreport.entity.sysrole.SysRole;
import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.sysmenu.ISysMenuService;

 /**  
* @Description: SysMenucontroller类
* @author 
* @date 2021-06-15 07:11:44
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/sysMenu")
public class SysMenuController extends BaseController {

	/**
	 * iSysMenuService服务注入
	 */
	@Autowired
	private ISysMenuService iSysMenuService;

	/** 
	* @Description: 分页查询表格
	* @param SysMenu
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="SysMenu",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"sysMenu_search"})
	public Response getTableList(@RequestBody SysMenu model)
	{
		
		List<SysMenu> result = iSysMenuService.tablePagingQuery(model);
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
	@MethodLog(module="SysMenu",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"sysMenu_getDetail","sysMenu_update"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iSysMenuService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param SysMenu
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="SysMenu",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"menuName:required#菜单名称;length#菜单名称#20","menuUrl:required#菜单url;length#菜单url#200","menuIcon:required#菜单图标;length#菜单图标#50",})
	@RequiresPermissions(value = {"sysMenu_insert"})
	public Response insert(@RequestBody SysMenu model) throws Exception
	{
		BaseEntity result = iSysMenuService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param SysMenu
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="SysMenu",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","menuName:required#菜单名称;length#菜单名称#20","menuUrl:required#菜单url;length#菜单url#200","menuIcon:required#菜单图标;length#菜单图标#50",})
	@RequiresPermissions(value = {"sysMenu_update"})
	public Response update(@RequestBody SysMenu model) throws Exception
	{
		BaseEntity result = iSysMenuService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="SysMenu",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"sysMenu_delete"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iSysMenuService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param SysMenu 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="SysMenu",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"sysMenu_batchDelete"})
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iSysMenuService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @Title: getParentMenus
	 * @Description: 获取上级菜单
	 * @param sysMenu
	 * @return
	 * @author caiyang
	 * @date 2021-06-15 04:37:15 
	 */ 
	@RequestMapping(value = "/getParentMenus",method = RequestMethod.POST)
	@MethodLog(module="SysMenu",remark="获取上级菜单",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"sysMenu_getDetail","sysMenu_update","sysMenu_insert"},logical = Logical.OR)
	public Response getParentMenus(@RequestBody SysMenu sysMenu)
	{
		List<SysMenu> result = this.iSysMenuService.getParentMenus(sysMenu);
		return Response.success(result);
	}
	
	/**  
	 * @Title: getAuthTree
	 * @Description: 获取角色授权树
	 * @param sysRole
	 * @param userInfoDto
	 * @return
	 * @author caiyang
	 * @date 2021-06-16 08:10:07 
	 */ 
	@RequestMapping(value = "/getAuthTree",method = RequestMethod.POST)
	@MethodLog(module="SysMenu",remark="获取角色授权树",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"sysRole_authed"})
	public Response getAuthTree(@RequestBody SysRole sysRole,@LoginUser UserInfoDto userInfoDto)
	{
		AuthTreeDto result = iSysMenuService.getAuthTree(sysRole, userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @Title: getIndexMenu
	 * @Description: 获取首页菜单
	 * @param userInfoDto
	 * @return
	 * @author caiyang
	 * @date 2021-06-18 01:32:16 
	 */ 
	@RequestMapping(value = "/getIndexMenu",method = RequestMethod.POST)
	@MethodLog(module="SysMenu",remark="获取首页菜单树",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getIndexMenu(@LoginUser UserInfoDto userInfoDto)
	{
		List<IndexMenuTreeDto> result = this.iSysMenuService.getIndexMenu(userInfoDto);
		return Response.success(result);
	}
}
