/** 
 * 模块：报表系统-ReportDatasourceTables
 */
package com.springreport.controller.reportdatasourcetables;

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
import com.springreport.entity.reportdatasourcetables.ReportDatasourceTables;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reportdatasourcetables.IReportDatasourceTablesService;

 /**  
* @Description: ReportDatasourceTablescontroller类
* @author 
* @date 2026-11-24 10:51:13
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/reportDatasourceTables")
public class ReportDatasourceTablesController extends BaseController {

	/**
	 * iReportDatasourceTablesService服务注入
	 */
	@Autowired
	private IReportDatasourceTablesService iReportDatasourceTablesService;

	/** 
	* @Description: 分页查询表格
	* @param ReportDatasourceTables
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceTables",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"reportDatasourceTables_search"},logical = Logical.OR)
	public Response getTableList(@RequestBody ReportDatasourceTables model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iReportDatasourceTablesService.tablePagingQuery(model);
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
	@MethodLog(module="ReportDatasourceTables",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportDatasourceTables_getDetail"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iReportDatasourceTablesService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param ReportDatasourceTables
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceTables",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"tableName:required#表名;length#表名#200","datasourceId:required",})
	@RequiresPermissions(value = {"reportDatasourceTables_insert"},logical = Logical.OR)
	public Response insert(@RequestBody ReportDatasourceTables model) throws Exception
	{
		BaseEntity result = iReportDatasourceTablesService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param ReportDatasourceTables
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceTables",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","tableName:required#表名;length#表名#200",})
	@RequiresPermissions(value = {"reportDatasourceTables_update"},logical = Logical.OR)
	public Response update(@RequestBody ReportDatasourceTables model) throws Exception
	{
		BaseEntity result = iReportDatasourceTablesService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ReportDatasourceTables",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportDatasourceTables_delete"},logical = Logical.OR)
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iReportDatasourceTablesService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param ReportDatasourceTables 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceTables",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"reportDatasourceTables_delete"},logical = Logical.OR)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iReportDatasourceTablesService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
}
