/** 
 * 模块：报表系统-ReportFormsDatasourceAttrs
 */
package com.springreport.controller.reportformsdatasourceattrs;

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
import com.springreport.entity.reportformsdatasourceattrs.ReportFormsDatasourceAttrs;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reportformsdatasourceattrs.IReportFormsDatasourceAttrsService;

 /**  
* @Description: ReportFormsDatasourceAttrscontroller类
* @author 
* @date 2022-11-16 01:47:58
* @version V1.0  
 */
@RestController
@RequestMapping("/api/reportFormsDatasourceAttrs")
public class ReportFormsDatasourceAttrsController extends BaseController {

	/**
	 * iReportFormsDatasourceAttrsService服务注入
	 */
	@Autowired
	private IReportFormsDatasourceAttrsService iReportFormsDatasourceAttrsService;

	/** 
	* @Description: 分页查询表格
	* @param ReportFormsDatasourceAttrs
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="ReportFormsDatasourceAttrs",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody ReportFormsDatasourceAttrs model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iReportFormsDatasourceAttrsService.tablePagingQuery(model);
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
	@MethodLog(module="ReportFormsDatasourceAttrs",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iReportFormsDatasourceAttrsService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param ReportFormsDatasourceAttrs
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="ReportFormsDatasourceAttrs",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"columnName:required#数据源列名;length#数据源列名#100",})
	public Response insert(@RequestBody ReportFormsDatasourceAttrs model) throws Exception
	{
		BaseEntity result = iReportFormsDatasourceAttrsService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param ReportFormsDatasourceAttrs
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="ReportFormsDatasourceAttrs",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","columnName:required#数据源列名;length#数据源列名#100",})
	public Response update(@RequestBody ReportFormsDatasourceAttrs model) throws Exception
	{
		BaseEntity result = iReportFormsDatasourceAttrsService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ReportFormsDatasourceAttrs",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iReportFormsDatasourceAttrsService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param ReportFormsDatasourceAttrs 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="ReportFormsDatasourceAttrs",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iReportFormsDatasourceAttrsService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
}
