/** 
 * 模块：报表系统-SysMerchant
 */
package com.springreport.controller.sysmerchant;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.springreport.base.BaseController;
import com.springreport.base.BaseEntity;
import com.springreport.base.Response;
import com.springreport.constants.Constants;
import com.springreport.dto.sysmerchant.SysMerchantDto;
import com.springreport.entity.sysmerchant.SysMerchant;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.sysmerchant.ISysMerchantService;

 /**  
* @Description: SysMerchantcontroller类
* @author 
* @date 2023-12-22 05:18:53
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/sysMerchant")
public class SysMerchantController extends BaseController {

	/**
	 * iSysMerchantService服务注入
	 */
	@Autowired
	private ISysMerchantService iSysMerchantService;

	/** 
	* @Description: 分页查询表格
	* @param SysMerchant
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="SysMerchant",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody SysMerchant model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iSysMerchantService.tablePagingQuery(model);
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
	@MethodLog(module="SysMerchant",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iSysMerchantService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param SysMerchant
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="SysMerchant",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"merchantName:required#租户名称;length#租户名称#50","phone:required#电话;length#电话#20","email:required#邮箱;length#邮箱#50;email",})
	public Response insert(@RequestBody SysMerchantDto model) throws Exception
	{
		BaseEntity result = iSysMerchantService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param SysMerchant
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="SysMerchant",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","merchantName:required#租户名称;length#租户名称#50","phone:required#电话;length#电话#20","email:required#邮箱;length#邮箱#50;email",})
	public Response update(@RequestBody SysMerchantDto model) throws Exception
	{
		BaseEntity result = iSysMerchantService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="SysMerchant",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iSysMerchantService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param SysMerchant 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="SysMerchant",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iSysMerchantService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: getMerchantList
	 * @Description: 获取所有的商户列表
	 * @author caiyang
	 * @param model
	 * @return Response
	 * @date 2023-12-23 11:31:37 
	 */ 
	@RequestMapping(value = "/getMerchantList",method = RequestMethod.POST)
	@MethodLog(module="SysMerchant",remark="获取所有的商户列表",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getMerchantList()
	{
		List<SysMerchant> result = iSysMerchantService.getMerchantList();
		return Response.success(result);
	}
}
