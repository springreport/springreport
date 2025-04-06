/** 
 * 模块：报表系统-ReportTplDatasource
 */
package com.springreport.controller.reporttpldatasource;

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
import com.springreport.dto.reporttpldatasource.ReportTplDatasourceDto;
import com.springreport.entity.reporttpldatasource.ReportTplDatasource;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reporttpldatasource.IReportTplDatasourceService;

 /**  
* @Description: ReportTplDatasourcecontroller类
* @author 
* @date 2021-02-13 11:16:43
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/reportTplDatasource")
public class ReportTplDatasourceController extends BaseController {

	/**
	 * iReportTplDatasourceService服务注入
	 */
	@Autowired
	private IReportTplDatasourceService iReportTplDatasourceService;

	/** 
	* @Description: 分页查询表格
	* @param ReportTplDatasource
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDatasource",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody ReportTplDatasource model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iReportTplDatasourceService.tablePagingQuery(model);
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
	@MethodLog(module="ReportTplDatasource",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iReportTplDatasourceService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param ReportTplDatasource
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDatasource",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"merchantNo:required#商户号;length#商户号#20",})
	public Response insert(@RequestBody ReportTplDatasource model) throws Exception
	{
		BaseEntity result = iReportTplDatasourceService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param ReportTplDatasource
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDatasource",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","merchantNo:required#商户号;length#商户号#20",})
	public Response update(@RequestBody ReportTplDatasource model) throws Exception
	{
		BaseEntity result = iReportTplDatasourceService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ReportTplDatasource",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iReportTplDatasourceService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param ReportTplDatasource 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDatasource",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iReportTplDatasourceService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/**
	*<p>Title: getReportTplDatasoure</p>
	*<p>Description: 获取报表模板数据源</p>
	* @author caiyang
	* @param reportTplDatasource
	* @return
	*/
	@RequestMapping(value = "/getReportTplDatasoure",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDatasource",remark="获取报表模板数据源",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板ID"})
	@RequiresPermissions(value = {"reportTpl_reportDesign","screenTpl_screenDesign","slidTpl_design","docTpl_design","excelTemplate_design","template_market","wordTemplate_design","screenTemplate_design"},logical = Logical.OR)
	public Response getReportTplDatasoure(@RequestBody ReportTplDatasource reportTplDatasource) {
		List<ReportTplDatasourceDto> result = this.iReportTplDatasourceService.getReportTplDatasoure(reportTplDatasource);
		return Response.success(result);
	}
	
}
