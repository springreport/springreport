/** 
 * 模块：报表系统-ReportDatasourceDictCode
 */
package com.springreport.controller.reportdatasourcedictcode;

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
import com.springreport.entity.reportdatasourcedictcode.ReportDatasourceDictCode;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reportdatasourcedictcode.IReportDatasourceDictCodeService;

 /**  
* @Description: ReportDatasourceDictCodecontroller类
* @author 
* @date 2021-08-23 07:52:03
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/reportDatasourceDictCode")
public class ReportDatasourceDictCodeController extends BaseController {

	/**
	 * iReportDatasourceDictCodeService服务注入
	 */
	@Autowired
	private IReportDatasourceDictCodeService iReportDatasourceDictCodeService;

	/** 
	* @Description: 分页查询表格
	* @param ReportDatasourceDictCode
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictCode",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody ReportDatasourceDictCode model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iReportDatasourceDictCodeService.tablePagingQuery(model);
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
	@MethodLog(module="ReportDatasourceDictCode",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iReportDatasourceDictCodeService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param ReportDatasourceDictCode
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictCode",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"name:required#字典名称;length#字典名称#50","code:required#字典编码;length#字典编码#50","desc:required#字典描述;length#字典描述#100",})
	public Response insert(@RequestBody ReportDatasourceDictCode model) throws Exception
	{
		BaseEntity result = iReportDatasourceDictCodeService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param ReportDatasourceDictCode
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictCode",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","name:required#字典名称;length#字典名称#50","code:required#字典编码;length#字典编码#50","desc:required#字典描述;length#字典描述#100",})
	public Response update(@RequestBody ReportDatasourceDictCode model) throws Exception
	{
		BaseEntity result = iReportDatasourceDictCodeService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ReportDatasourceDictCode",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iReportDatasourceDictCodeService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param ReportDatasourceDictCode 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictCode",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iReportDatasourceDictCodeService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
}
