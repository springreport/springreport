/** 
 * 模块：报表系统-SpringreportField
 */
package com.springreport.controller.springreportfield;

import java.util.ArrayList;
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
import com.springreport.base.PageEntity;
import com.springreport.base.Response;
import com.springreport.base.TreeDto;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.dto.template.MesGetTemplateDto;
import com.springreport.entity.reporttpl.ReportTpl;
import com.springreport.entity.springreportfield.SpringreportField;
import com.springreport.util.ListUtil;
import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.springreportfield.ISpringreportFieldService;

 /**  
* @Description: SpringreportFieldcontroller类
* @author 
* @date 2025-03-18 10:36:28
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/springreportField")
public class SpringreportFieldController extends BaseController {

	/**
	 * iSpringreportFieldService服务注入
	 */
	@Autowired
	private ISpringreportFieldService iSpringreportFieldService;

	/** 
	* @Description: 分页查询表格
	* @param SpringreportField
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="SpringreportField",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody SpringreportField model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iSpringreportFieldService.tablePagingQuery(model);
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
	@MethodLog(module="SpringreportField",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iSpringreportFieldService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param SpringreportField
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="SpringreportField",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"fieldName:required#分类名称;length#分类名称#50",})
	@RequiresPermissions(value = {"excelTemplate_folder","wordTemplate_folder"},logical = Logical.OR)
	public Response insert(@RequestBody SpringreportField model) throws Exception
	{
		BaseEntity result = iSpringreportFieldService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param SpringreportField
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="SpringreportField",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","fieldName:required#分类名称;length#分类名称#50",})
	public Response update(@RequestBody SpringreportField model) throws Exception
	{
		BaseEntity result = iSpringreportFieldService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="SpringreportField",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iSpringreportFieldService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param SpringreportField 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="SpringreportField",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iSpringreportFieldService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	@RequestMapping(value = "/getTemplateFieldTree",method = RequestMethod.POST)
	@MethodLog(module="ReportType",remark="获取报表模板分类树",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"template_market","excelTemplate_search","wordTemplate_search"},logical = Logical.OR)
	public Response getReportTypeTree(@RequestBody SpringreportField model) {
		List<TreeDto> result = new ArrayList<>();
		TreeDto parentTreeDto = new TreeDto();
		parentTreeDto.setId(1L);
		parentTreeDto.setLabel("模板分类");
		List<TreeDto> reportTypeTrees = new ArrayList<>();
		List<SpringreportField> fields = this.iSpringreportFieldService.getFields(model);
		if(!ListUtil.isEmpty(fields))
		{
			for (int i = 0; i < fields.size(); i++) {
				TreeDto treeDto = new TreeDto();
				treeDto.setId(fields.get(i).getId());
				treeDto.setLabel(fields.get(i).getFieldName());
				reportTypeTrees.add(treeDto);
			}
		}
		parentTreeDto.setChildren(reportTypeTrees);
		result.add(parentTreeDto);
		return Response.success(result);
	}
	
	@RequestMapping(value = "/getTemplateFields",method = RequestMethod.POST)
	@MethodLog(module="ReportType",remark="获取报表模板分类",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"template_market","excelTemplate_search","wordTemplate_search"},logical = Logical.OR)
	public Response getTemplateFields(@RequestBody SpringreportField model) {
		List<SpringreportField> fields = this.iSpringreportFieldService.getFields(model);
		return Response.success(fields);
	}
	
	@RequestMapping(value = "/getTemplates",method = RequestMethod.POST)
	@MethodLog(module="ReportType",remark="获取报表模板",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"template_market"},logical = Logical.OR)
	public Response getTemplates(@RequestBody MesGetTemplateDto model,@LoginUser UserInfoDto userInfoDto) {
		PageEntity result = this.iSpringreportFieldService.getTemplates(model,userInfoDto);
		return Response.success(result);
	}
}
