/** 
 * 模块：报表系统-SysApi
 */
package com.springreport.controller.sysapi;

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
import com.springreport.entity.sysapi.SysApi;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.sysapi.ISysApiService;

 /**  
* @Description: SysApicontroller类
* @author 
* @date 2021-06-15 07:11:40
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/sysApi")
public class SysApiController extends BaseController {

	/**
	 * iSysApiService服务注入
	 */
	@Autowired
	private ISysApiService iSysApiService;

	/** 
	* @Description: 分页查询表格
	* @param SysApi
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="SysApi",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"sysApi_search","sysMenu_sysApi"},logical = Logical.OR)
	public Response getTableList(@RequestBody SysApi model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iSysApiService.tablePagingQuery(model);
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
	@MethodLog(module="SysApi",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"sysApi_getDetail","sysApi_update"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iSysApiService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param SysApi
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="SysApi",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"apiCode:required#权限标识;length#权限标识#50","apiName:required#权限名称;length#权限名称#50","apiFunction:required#权限描述;length#权限描述#200","sort:required#排序",})
	@RequiresPermissions(value = {"sysApi_insert"})
	public Response insert(@RequestBody SysApi model) throws Exception
	{
		BaseEntity result = iSysApiService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param SysApi
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="SysApi",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","apiCode:required#权限标识;length#权限标识#50","apiName:required#权限名称;length#权限名称#50","apiFunction:required#权限描述;length#权限描述#200","sort:required#排序",})
	@RequiresPermissions(value = {"sysApi_update"})
	public Response update(@RequestBody SysApi model) throws Exception
	{
		BaseEntity result = iSysApiService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="SysApi",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"sysApi_delete"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iSysApiService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param SysApi 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="SysApi",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"sysApi_batchDelete"})
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iSysApiService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
}
