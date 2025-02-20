/** 
 * 模块：医院入驻平台-ReportType
 */
package com.springreport.controller.reporttype;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reporttype.IReportTypeService;
import com.springreport.base.BaseController;
import com.springreport.base.BaseEntity;
import com.springreport.base.Response;
import com.springreport.base.TreeDto;
import com.springreport.constants.Constants;
import com.springreport.entity.reporttype.ReportType;
import com.springreport.util.ListUtil;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
 /**  
* @Description: ReportTypecontroller类
* @author 
* @date 2021-02-09 08:59:58
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/reportType")
public class ReportTypeController extends BaseController {

	/**
	 * iReportTypeService服务注入
	 */
	@Autowired
	private IReportTypeService iReportTypeService;

	/** 
	* @Description: 分页查询表格
	* @param ReportType
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="ReportType",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"reportType_search"})
	public Response getTableList(@RequestBody ReportType model)
	{
		BaseEntity result = new BaseEntity();
		result = iReportTypeService.tablePagingQuery(model);
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
	@MethodLog(module="ReportType",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
//	@RequiresPermissions(value = {"reportType_getDetail","reportType_update","reportTpl_update","reportTpl_getDetail","onlineTpl_update","onlineTpl_getDetai","docTpl_getDetail","docTpl_edit","screenTpl_getDetail","screenTpl_update"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iReportTypeService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param ReportType
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="ReportType",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"reportTypeName:required#报表类型名称;length#报表类型名称#50",})
	@RequiresPermissions(value = {"reportType_insert","screenTpl_folder","docTpl_folder","onlineTpl_folder","reportTpl_folder"},logical = Logical.OR)
	public Response insert(@RequestBody ReportType model) throws Exception
	{
		BaseEntity result = iReportTypeService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param ReportType
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="ReportType",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","reportTypeName:required#报表类型名称;length#报表类型名称#50",})
//	@RequiresPermissions(value = {"reportType_update","reportTpl_update","onlineTpl_update","docTpl_edit","screenTpl_update"})
	public Response update(@RequestBody ReportType model) throws Exception
	{
		BaseEntity result = iReportTypeService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ReportType",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
//	@RequiresPermissions(value = {"reportType_delete","reportTpl_delete"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iReportTypeService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param ReportType 
	* @return Response 
	* @throws 
	*/ 
//	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
//	@MethodLog(module="ReportType",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
//	@RequiresPermissions(value = {"reportType_batchDelete"})
//	public Response deletebatch(@RequestBody List<Long> ids)
//	{
//		BaseEntity result = iReportTypeService.deleteBatch(ids);
//		return Response.success(result.getStatusMsg());
//	}
//	
	/** 
	* @Description: 获取报表类型
	* @param ReportType 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/getReportType",method = RequestMethod.POST)
	@MethodLog(module="ReportType",remark="获取报表类型",operateType=Constants.OPERATE_TYPE_SEARCH)
//	@RequiresPermissions(value = {"reportTpl_search","viewReport_Search"},logical = Logical.OR)
	public Response getReportType(@RequestBody ReportType reportType) {
		List<ReportType> result = this.iReportTypeService.getReportType(reportType);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getReportTypeTree
	 * @Description: 获取报表类型树
	 * @author caiyang
	 * @return 
	 * @return Response
	 * @date 2023-01-28 09:10:52 
	 */  
	@RequestMapping(value = "/getReportTypeTree",method = RequestMethod.POST)
	@MethodLog(module="ReportType",remark="获取报表类型树",operateType=Constants.OPERATE_TYPE_SEARCH)
//	@RequiresPermissions(value = {"reportTpl_search","viewReport_Search"},logical = Logical.OR)
	public Response getReportTypeTree(@RequestBody ReportType reportType) {
		List<TreeDto> result = new ArrayList<>();
		TreeDto parentTreeDto = new TreeDto();
		parentTreeDto.setId(1L);
		parentTreeDto.setLabel("报表目录");
		List<TreeDto> reportTypeTrees = new ArrayList<>();
		List<ReportType> reportTypes = this.iReportTypeService.getReportType(reportType);
		if(!ListUtil.isEmpty(reportTypes))
		{
			for (int i = 0; i < reportTypes.size(); i++) {
				TreeDto treeDto = new TreeDto();
				treeDto.setId(reportTypes.get(i).getId());
				treeDto.setLabel(reportTypes.get(i).getReportTypeName());
				reportTypeTrees.add(treeDto);
			}
		}
		parentTreeDto.setChildren(reportTypeTrees);
		result.add(parentTreeDto);
		return Response.success(result);
	}
}
