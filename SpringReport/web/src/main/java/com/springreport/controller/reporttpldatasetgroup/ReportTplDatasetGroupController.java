/** 
 * 模块：报表系统-ReportTplDatasetGroup
 */
package com.springreport.controller.reporttpldatasetgroup;

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
import com.springreport.entity.reporttpldatasetgroup.ReportTplDatasetGroup;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reporttpldatasetgroup.IReportTplDatasetGroupService;

 /**  
* @Description: ReportTplDatasetGroupcontroller类
* @author 
* @date 2024-12-13 08:27:12
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/reportTplDatasetGroup")
public class ReportTplDatasetGroupController extends BaseController {

	/**
	 * iReportTplDatasetGroupService服务注入
	 */
	@Autowired
	private IReportTplDatasetGroupService iReportTplDatasetGroupService;

	/** 
	* @Description: 分页查询表格
	* @param ReportTplDatasetGroup
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDatasetGroup",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody ReportTplDatasetGroup model)
	{
		List<ReportTplDatasetGroup> result = iReportTplDatasetGroupService.tablePagingQuery(model);
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
	@MethodLog(module="ReportTplDatasetGroup",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iReportTplDatasetGroupService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param ReportTplDatasetGroup
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDatasetGroup",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"groupName:required#分组名称;length#分组名称#50","tplId:required#模板id"})
	public Response insert(@RequestBody ReportTplDatasetGroup model) throws Exception
	{
		BaseEntity result = iReportTplDatasetGroupService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param ReportTplDatasetGroup
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDatasetGroup",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","tplId:required#模板id","groupName:required#分组名称;length#分组名称#50",})
	public Response update(@RequestBody ReportTplDatasetGroup model) throws Exception
	{
		BaseEntity result = iReportTplDatasetGroupService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ReportTplDatasetGroup",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iReportTplDatasetGroupService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param ReportTplDatasetGroup 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDatasetGroup",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iReportTplDatasetGroupService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
}
