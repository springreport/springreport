/** 
 * 模块：报表系统-DocTpl
 */
package com.springreport.controller.doctpl;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import com.springreport.base.BaseController;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.Response;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.dto.doctpl.DocDto;
import com.springreport.dto.doctpl.DocTplDto;
import com.springreport.dto.doctpl.DocTplSettingsDto;
import com.springreport.dto.doctpl.DocTplTreeDto;
import com.springreport.dto.reporttpl.MesGenerateReportDto;
import com.springreport.entity.doctpl.DocTpl;
import com.springreport.entity.doctplsettings.DocTplSettings;
import com.springreport.entity.reporttype.ReportType;
import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.doctpl.IDocTplService;

 /**  
* @Description: DocTplcontroller类
* @author 
* @date 2024-05-02 08:55:33
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/docTpl")
public class DocTplController extends BaseController {

	/**
	 * iDocTplService服务注入
	 */
	@Autowired
	private IDocTplService iDocTplService;

	/** 
	* @Description: 分页查询表格
	* @param DocTpl
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="DocTpl",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"docTpl_search","wordTemplate_search"},logical = Logical.OR)
	public Response getTableList(@RequestBody DocTpl model)
	{
		PageEntity result = iDocTplService.tablePagingQuery(model);
		return Response.success(result);
	}
	
	@RequestMapping(value = "/getChildren",method = RequestMethod.POST)
	@MethodLog(module="DocTpl",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"docTpl_search","wordTemplate_search"},logical = Logical.OR)
	public Response getChildren(@RequestBody DocTpl model)
	{
		List<DocTplTreeDto> result = iDocTplService.getChildren(model);
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
	@MethodLog(module="DocTpl",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"docTpl_getDetail","docTpl_edit","wordTemplate_getDetail","wordTemplate_update"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iDocTplService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param DocTpl
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="DocTpl",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"tplCode:required#模板标识;length#模板标识#40","tplName:required#模板名称;length#模板名称#40",})
	@RequiresPermissions(value = {"docTpl_insert","wordTemplate_insert"},logical = Logical.OR)
	public Response insert(@RequestBody DocTplDto model) throws Exception
	{
		BaseEntity result = iDocTplService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param DocTpl
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="DocTpl",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","tplCode:required#模板标识;length#模板标识#40","tplName:required#模板名称;length#模板名称#40",})
	@RequiresPermissions(value = {"docTpl_edit","wordTemplate_update"},logical = Logical.OR)
	public Response update(@RequestBody DocTplDto model) throws Exception
	{
		BaseEntity result = iDocTplService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="DocTpl",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"docTpl_delete","wordTemplate_delete"},logical = Logical.OR)
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iDocTplService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param DocTpl 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="DocTpl",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"docTpl_delete"})
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iDocTplService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: getDocTplSettings
	 * @Description: 获取doc文本模板数据
	 * @author caiyang
	 * @param model
	 * @return Response
	 * @date 2024-05-03 09:52:39 
	 */ 
	@RequestMapping(value = "/getDocTplSettings",method = RequestMethod.POST)
	@RequiresPermissions(value = {"docTpl_design","template_market","wordTemplate_design"},logical = Logical.OR)
	@Check({"tplId:required#模板ID"})
	public Response getDocTplSettings(@RequestBody DocTplSettings model) {
		BaseEntity result = this.iDocTplService.getDocTplSettings(model);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: saveDocTplSettings
	 * @Description: 保存doc模板数据
	 * @author caiyang
	 * @param model
	 * @return Response
	 * @date 2024-05-03 04:04:05 
	 */ 
	@RequestMapping(value = "/saveDocTplSettings",method = RequestMethod.POST)
	@RequiresPermissions(value = {"docTpl_save"})
	@Check({"tplId:required#模板ID"})
	public Response saveDocTplSettings(@RequestBody DocTplSettingsDto model) {
		BaseEntity result = this.iDocTplService.saveDocTplSettings(model);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: downLoadDocTpl
	 * @Description: 导出word模板
	 * @author caiyang
	 * @param model
	 * @return Response
	 * @throws Exception 
	 * @date 2024-05-09 05:05:31 
	 */ 
	@RequestMapping(value = "/downLoadDocTpl",method = RequestMethod.POST)
	@Check({"tplId:required#模板ID"})
	@RequiresPermissions(value = {"docTpl_export"})
	public void downLoadDocTpl(@RequestBody DocTplSettings model) throws Exception {
		this.iDocTplService.downLoadDocTpl(model, httpServletResponse);
	}
	
	/**  
	 * @MethodName: previewDoc
	 * @Description: doc预览
	 * @author caiyang
	 * @param model
	 * @return
	 * @throws Exception Response
	 * @date 2024-05-07 09:23:34 
	 */ 
	@RequestMapping(value = "/previewDoc",method = RequestMethod.POST)
	@Check({"tplId:required#模板ID"})
	@RequiresPermissions(value = {"docTpl_preview","docTpl_view","template_market","wordTemplate_view"},logical = Logical.OR)
	public Response previewDoc(@RequestBody MesGenerateReportDto model,@LoginUser UserInfoDto userInfoDto) throws Exception{
		Map<String, Object> result = this.iDocTplService.previewDoc(model,userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: uploadDocx
	 * @Description: 上传docx文件
	 * @author caiyang
	 * @param file
	 * @return Response
	 * @date 2024-09-28 07:15:00 
	 */ 
	@RequestMapping("/uploadDocx")
	public Response uploadDocx(@RequestParam("file") MultipartFile file) throws Exception{
		DocDto result = this.iDocTplService.uploadDocx(file);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: copyReport
	 * @Description: 复制模板
	 * @author caiyang
	 * @param file
	 * @return
	 * @throws Exception Response
	 * @date 2025-04-01 04:31:05 
	 */ 
	@RequestMapping("/copyReport")
	public Response copyReport(@RequestBody DocTpl docTpl) throws Exception{
		BaseEntity result = this.iDocTplService.copyReport(docTpl);
		return Response.success(result.getStatusMsg());
	}
}
