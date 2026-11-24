/** 
 * 模块：报表系统-Whitelist
 */
package com.springreport.controller.whitelist;

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
import com.springreport.entity.whitelist.Whitelist;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.whitelist.IWhitelistService;

 /**  
* @Description: Whitelistcontroller类
* @author 
* @date 2026-11-24 10:50:41
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/whitelist")
public class WhitelistController extends BaseController {

	/**
	 * iWhitelistService服务注入
	 */
	@Autowired
	private IWhitelistService iWhitelistService;

	/** 
	* @Description: 分页查询表格
	* @param Whitelist
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="Whitelist",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"whitelist_search"},logical = Logical.OR)
	public Response getTableList(@RequestBody Whitelist model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iWhitelistService.tablePagingQuery(model);
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
	@MethodLog(module="Whitelist",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"whitelist_getDetail","whitelist_update"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iWhitelistService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param Whitelist
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="Whitelist",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"value:required#IP或域名值;length#IP或域名值#255","valueType:required#类型",})
	@RequiresPermissions(value = {"whitelist_insert"},logical = Logical.OR)
	public Response insert(@RequestBody Whitelist model) throws Exception
	{
		BaseEntity result = iWhitelistService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param Whitelist
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="Whitelist",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","value:required#IP或域名值;length#IP或域名值#255","valueType:required#类型"})
	@RequiresPermissions(value = {"whitelist_update"},logical = Logical.OR)
	public Response update(@RequestBody Whitelist model) throws Exception
	{
		BaseEntity result = iWhitelistService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="Whitelist",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"whitelist_delete"},logical = Logical.OR)
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iWhitelistService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param Whitelist 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="Whitelist",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"whitelist_delete"},logical = Logical.OR)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iWhitelistService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
}
