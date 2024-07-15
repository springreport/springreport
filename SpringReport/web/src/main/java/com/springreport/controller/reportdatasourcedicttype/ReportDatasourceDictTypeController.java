/** 
 * 模块：报表系统-ReportDatasourceDictType
 */
package com.springreport.controller.reportdatasourcedicttype;

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
import com.springreport.entity.reportdatasourcedicttype.ReportDatasourceDictType;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reportdatasourcedicttype.IReportDatasourceDictTypeService;

 /**  
* @Description: ReportDatasourceDictTypecontroller类
* @author 
* @date 2022-11-21 01:46:20
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/reportDatasourceDictType")
public class ReportDatasourceDictTypeController extends BaseController {

	/**
	 * iReportDatasourceDictTypeService服务注入
	 */
	@Autowired
	private IReportDatasourceDictTypeService iReportDatasourceDictTypeService;

	/** 
	* @Description: 分页查询表格
	* @param ReportDatasourceDictType
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictType",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"reportDatasource_dict","reportDatasourceDictType_search"},logical = Logical.OR)
	public Response getTableList(@RequestBody ReportDatasourceDictType model)
	{
		BaseEntity result = new BaseEntity();
		result = iReportDatasourceDictTypeService.tablePagingQuery(model);
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
	@MethodLog(module="ReportDatasourceDictType",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportDatasourceDictType_update","reportDatasourceDictType_getDetail"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iReportDatasourceDictTypeService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param ReportDatasourceDictType
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictType",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"dictName:required#字典类型名称;length#字典类型名称#100","dictType:required#字典类型;length#字典类型#100","remark:length#备注#200",})
	@RequiresPermissions(value = {"reportDatasourceDictType_insert"},logical = Logical.OR)
	public Response insert(@RequestBody ReportDatasourceDictType model) throws Exception
	{
		BaseEntity result = iReportDatasourceDictTypeService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param ReportDatasourceDictType
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictType",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","dictName:required#字典类型名称;length#字典类型名称#100","dictType:required#字典类型;length#字典类型#100","remark:length#备注#200",})
	@RequiresPermissions(value = {"reportDatasourceDictType_update"},logical = Logical.OR)
	public Response update(@RequestBody ReportDatasourceDictType model) throws Exception
	{
		BaseEntity result = iReportDatasourceDictTypeService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ReportDatasourceDictType",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportDatasourceDictType_delete"},logical = Logical.OR)
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iReportDatasourceDictTypeService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param ReportDatasourceDictType 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictType",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"reportDatasourceDictType_delete"},logical = Logical.OR)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iReportDatasourceDictTypeService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: getDatasourceDictTypes
	 * @Description: 获取数据源字典类型
	 * @author caiyang
	 * @param model
	 * @return 
	 * @return Response
	 * @date 2022-11-21 03:54:35 
	 */  
	@RequestMapping(value = "/getDatasourceDictTypes",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictType",remark="获取数据源字典类型",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"datasourceId:required#数据源ID"})
	public Response getDatasourceDictTypes(@RequestBody ReportDatasourceDictType model) {
		List<ReportDatasourceDictType> result = this.iReportDatasourceDictTypeService.getDatasourceDictTypes(model);
		return Response.success(result);
	}
}
