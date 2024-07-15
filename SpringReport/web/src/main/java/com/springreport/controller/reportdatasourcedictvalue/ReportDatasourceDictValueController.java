/** 
 * 模块：报表系统-ReportDatasourceDictValue
 */
package com.springreport.controller.reportdatasourcedictvalue;

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
import com.springreport.entity.reportdatasourcedictvalue.ReportDatasourceDictValue;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reportdatasourcedictvalue.IReportDatasourceDictValueService;

 /**  
* @Description: ReportDatasourceDictValuecontroller类
* @author 
* @date 2021-08-23 07:52:08
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/reportDatasourceDictValue")
public class ReportDatasourceDictValueController extends BaseController {

	/**
	 * iReportDatasourceDictValueService服务注入
	 */
	@Autowired
	private IReportDatasourceDictValueService iReportDatasourceDictValueService;

	/** 
	* @Description: 分页查询表格
	* @param ReportDatasourceDictValue
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictValue",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody ReportDatasourceDictValue model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iReportDatasourceDictValueService.tablePagingQuery(model);
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
	@MethodLog(module="ReportDatasourceDictValue",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iReportDatasourceDictValueService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param ReportDatasourceDictValue
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictValue",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"code:required#字典编码;length#字典编码#50","value:required#字典值;length#字典值#50","valueName:required#字典值名称;length#字典值名称#50",})
	public Response insert(@RequestBody ReportDatasourceDictValue model) throws Exception
	{
		BaseEntity result = iReportDatasourceDictValueService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param ReportDatasourceDictValue
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictValue",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","code:required#字典编码;length#字典编码#50","value:required#字典值;length#字典值#50","valueName:required#字典值名称;length#字典值名称#50",})
	public Response update(@RequestBody ReportDatasourceDictValue model) throws Exception
	{
		BaseEntity result = iReportDatasourceDictValueService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ReportDatasourceDictValue",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iReportDatasourceDictValueService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param ReportDatasourceDictValue 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictValue",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iReportDatasourceDictValueService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
}
