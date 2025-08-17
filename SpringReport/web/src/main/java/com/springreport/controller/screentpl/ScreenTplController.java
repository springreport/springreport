/** 
 * 模块：报表系统-ScreenTpl
 */
package com.springreport.controller.screentpl;

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
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.dto.reporttpl.ShareDto;
import com.springreport.dto.screentpl.MesScreenTplDto;
import com.springreport.dto.screentpl.SaveScreenTplDto;
import com.springreport.dto.screentpl.ScreenTplDto;
import com.springreport.dto.screentpl.ScreenTplTreeDto;
import com.springreport.entity.reporttype.ReportType;
import com.springreport.entity.screentpl.ScreenTpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.screentpl.IScreenTplService;

 /**  
* @Description: ScreenTplcontroller类
* @author 
* @date 2021-08-02 07:01:17
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/screenTpl")
public class ScreenTplController extends BaseController {

	/**
	 * iScreenTplService服务注入
	 */
	@Autowired
	private IScreenTplService iScreenTplService;
	
	/** 
	* @Description: 分页查询表格
	* @param ScreenTpl
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="ScreenTpl",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"screenTpl_search","screenTemplate_search"},logical = Logical.OR)
	public Response getTableList(@RequestBody ScreenTpl model)
	{
		PageEntity result = iScreenTplService.tablePagingQuery(model);
		return Response.success(result);
	}
	
	@RequestMapping(value = "/getChildren",method = RequestMethod.POST)
	@MethodLog(module="ScreenTpl",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"screenTpl_search","screenTemplate_search"},logical = Logical.OR)
	public Response getChildren(@RequestBody ScreenTpl model)
	{
		List<ScreenTplTreeDto> result = iScreenTplService.getChildren(model);
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
	@MethodLog(module="ScreenTpl",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"screenTpl_getDetail","screenTpl_update","screenTpl_copy","screenTemplate_getDetail","screenTemplate_edit"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iScreenTplService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param ScreenTpl
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="ScreenTpl",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"tplCode:required#模板标识;length#模板标识#40","tplName:required#模板名称;length#模板名称#40"})
	@RequiresPermissions(value = {"screenTpl_insert","screenTemplate_insert"},logical = Logical.OR)
	public Response insert(@RequestBody MesScreenTplDto mesScreenTplDto) throws Exception
	{
		BaseEntity result = iScreenTplService.insert(mesScreenTplDto);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param ScreenTpl
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="ScreenTpl",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","tplCode:required#模板标识;length#模板标识#40","tplName:required#模板名称;length#模板名称#40"})
	@RequiresPermissions(value = {"screenTpl_update","screenTemplate_edit"},logical = Logical.OR)
	public Response update(@RequestBody MesScreenTplDto mesScreenTplDto) throws Exception
	{
		BaseEntity result = iScreenTplService.update(mesScreenTplDto);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ScreenTpl",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"screenTpl_delete","screenTemplate_delete"},logical = Logical.OR)
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iScreenTplService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param ScreenTpl 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="ScreenTpl",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"screenTpl_deleteBatch"})
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iScreenTplService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @Title: getScreenDesign
	 * @Description: 获取大屏设计详情
	 * @param screenTpl
	 * @return
	 * @author caiyang
	 * @date 2021-08-02 11:38:22 
	 */ 
	@RequestMapping(value = "/getScreenDesign",method = RequestMethod.POST)
	@MethodLog(module="ScreenTpl",remark="获取大屏设计详情",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"screenTpl_screenDesign","screenTpl_previewDesign","screenTpl_viewScreen","multiScreen_preview","multiScreen_view","screenTemplate_design"},logical = Logical.OR)
	public Response getScreenDesign(@RequestBody ScreenTpl screenTpl,@LoginUser UserInfoDto userInfoDto)
	{
		ScreenTplDto result = this.iScreenTplService.getScreenDesign(screenTpl);
		return Response.success(result);
	}
	
	/**  
	 * @Title: saveScreenDesign
	 * @Description: 保存大屏设计
	 * @param screenTplDto
	 * @return
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @date 2021-08-02 04:35:27 
	 */ 
	@RequestMapping(value = "/saveScreenDesign",method = RequestMethod.POST)
	@MethodLog(module="ScreenTpl",remark="保存大屏设计",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"id:required#主键ID","width:required#大屏宽度","height:required#大屏高度"})
	@RequiresPermissions(value = {"screenTpl_saveDesign","screenTemplate_design"},logical = Logical.OR)
	public Response saveScreenDesign(@RequestBody SaveScreenTplDto saveScreenTplDto,@LoginUser UserInfoDto userInfoDto) throws JsonProcessingException
	{
		BaseEntity result = this.iScreenTplService.saveScreenDesign(saveScreenTplDto,userInfoDto);
		return Response.success(result.getStatusMsg());
	}
	
	/** 
	* @Description: 获取所有的报表
	* @param ReportTpl
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getScreens",method = RequestMethod.POST)
	@MethodLog(module="ScreenTpl",remark="获取所有的大屏",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getReports(@RequestBody ScreenTpl screenTpl)
	{
		List<ScreenTpl> result = iScreenTplService.getScreens(screenTpl);
		return Response.success(result);
	}
	
	@RequestMapping(value = "/copyScreen",method = RequestMethod.POST)
	@MethodLog(module="ScreenTpl",remark="复制大屏",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response copyScreen(@RequestBody ScreenTpl screenTpl)
	{
		BaseEntity result = iScreenTplService.copyScreen(screenTpl);
		return Response.success(result.getStatusMsg());
	}
	
	@RequestMapping(value = "/getShareUrl",method = RequestMethod.POST)
	@MethodLog(module="ScreenTpl",remark="获取分享链接",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getShareUrl(@RequestBody ShareDto shareDto,@LoginUser UserInfoDto userInfoDto)
	{
		ShareDto result = this.iScreenTplService.getShareUrl(shareDto, userInfoDto);
		return Response.success(result);
	}
}
