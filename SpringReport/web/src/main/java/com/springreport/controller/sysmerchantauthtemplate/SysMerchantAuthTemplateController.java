/** 
 * 模块：报表系统-SysMerchantAuthTemplate
 */
package com.springreport.controller.sysmerchantauthtemplate;

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
import com.springreport.dto.sysmenu.AuthTreeDto;
import com.springreport.dto.sysmerchantauthtemplate.SysMerchantAuthTemplateDto;
import com.springreport.entity.sysmerchantauthtemplate.SysMerchantAuthTemplate;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.sysmerchantauthtemplate.ISysMerchantAuthTemplateService;

 /**  
* @Description: SysMerchantAuthTemplatecontroller类
* @author 
* @date 2023-12-22 05:18:59
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/sysMerchantAuthTemplate")
public class SysMerchantAuthTemplateController extends BaseController {

	/**
	 * iSysMerchantAuthTemplateService服务注入
	 */
	@Autowired
	private ISysMerchantAuthTemplateService iSysMerchantAuthTemplateService;

	/** 
	* @Description: 分页查询表格
	* @param SysMerchantAuthTemplate
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="SysMerchantAuthTemplate",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody SysMerchantAuthTemplate model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iSysMerchantAuthTemplateService.tablePagingQuery(model);
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
	@MethodLog(module="SysMerchantAuthTemplate",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iSysMerchantAuthTemplateService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param SysMerchantAuthTemplate
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="SysMerchantAuthTemplate",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"templateName:required#模板名称;length#模板名称#50"})
	public Response insert(@RequestBody SysMerchantAuthTemplateDto model) throws Exception
	{
		BaseEntity result = iSysMerchantAuthTemplateService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param SysMerchantAuthTemplate
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="SysMerchantAuthTemplate",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","templateName:required#模板名称;length#模板名称#50",})
	public Response update(@RequestBody SysMerchantAuthTemplateDto model) throws Exception
	{
		BaseEntity result = iSysMerchantAuthTemplateService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="SysMerchantAuthTemplate",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iSysMerchantAuthTemplateService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param SysMerchantAuthTemplate 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="SysMerchantAuthTemplate",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iSysMerchantAuthTemplateService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: getAuthTree
	 * @Description: 获取租户模板权限树
	 * @author caiyang
	 * @param model
	 * @return
	 * @throws Exception Response
	 * @date 2023-12-23 05:13:49 
	 */ 
	@RequestMapping(value = "/getMerchantTemplateAuthTree",method = RequestMethod.POST)
	@MethodLog(module="SysMerchantAuthTemplate",remark="获取权限树",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getMerchantTemplateAuthTree(@RequestBody SysMerchantAuthTemplate model) throws Exception
	{
		AuthTreeDto authTreeDto = this.iSysMerchantAuthTemplateService.getMerchantTemplateAuthTree(model);
		return Response.success(authTreeDto);
	}
	
	/**  
	 * @MethodName: getAuthTemplates
	 * @Description: 获取所有的权限模板
	 * @author caiyang
	 * @param model
	 * @return Response
	 * @date 2023-12-24 10:01:53 
	 */ 
	@RequestMapping(value = "/getAuthTemplates",method = RequestMethod.POST)
	@MethodLog(module="SysMerchantAuthTemplate",remark="获取所有的权限模板",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getAuthTemplates()
	{
		List<SysMerchantAuthTemplate> result = iSysMerchantAuthTemplateService.getAuthTemplates();
		return Response.success(result);
	}
}
