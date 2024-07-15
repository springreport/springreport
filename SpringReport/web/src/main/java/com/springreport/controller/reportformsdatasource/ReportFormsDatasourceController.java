/** 
 * 模块：报表系统-ReportFormsDatasource
 */
package com.springreport.controller.reportformsdatasource;

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
import com.springreport.entity.reportformsdatasource.ReportFormsDatasource;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reportformsdatasource.IReportFormsDatasourceService;

 /**  
* @Description: ReportFormsDatasourcecontroller类
* @author 
* @date 2022-11-16 01:51:19
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/reportFormsDatasource")
public class ReportFormsDatasourceController extends BaseController {

	/**
	 * iReportFormsDatasourceService服务注入
	 */
	@Autowired
	private IReportFormsDatasourceService iReportFormsDatasourceService;

	/** 
	* @Description: 分页查询表格
	* @param ReportFormsDatasource
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="ReportFormsDatasource",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody ReportFormsDatasource model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iReportFormsDatasourceService.tablePagingQuery(model);
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
	@MethodLog(module="ReportFormsDatasource",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iReportFormsDatasourceService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param ReportFormsDatasource
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="ReportFormsDatasource",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"name:required#名称;length#名称#200",})
	public Response insert(@RequestBody ReportFormsDatasource model) throws Exception
	{
		BaseEntity result = iReportFormsDatasourceService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param ReportFormsDatasource
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="ReportFormsDatasource",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","name:required#名称;length#名称#200",})
	public Response update(@RequestBody ReportFormsDatasource model) throws Exception
	{
		BaseEntity result = iReportFormsDatasourceService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ReportFormsDatasource",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iReportFormsDatasourceService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param ReportFormsDatasource 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="ReportFormsDatasource",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iReportFormsDatasourceService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
}
