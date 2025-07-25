/** 
 * 模块：报表系统-ReportTpl
 */
package com.springreport.controller.reporttpl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.EncryptedDocumentException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import com.springreport.base.BaseController;
import com.springreport.base.BaseEntity;
//import com.springreport.base.NoConvertResponse;
import com.springreport.base.PageEntity;
import com.springreport.base.Response;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.dto.reporttpl.MesChangePwd;
import com.springreport.dto.reporttpl.MesGenerateReportDto;
import com.springreport.dto.reporttpl.MesLuckysheetsTplDto;
import com.springreport.dto.reporttpl.MobilePreviewDto;
import com.springreport.dto.reporttpl.ReportDataDto;
import com.springreport.dto.reporttpl.ReportTplDto;
import com.springreport.dto.reporttpl.ReportTplTreeDto;
import com.springreport.dto.reporttpl.ResPreviewData;
import com.springreport.dto.reporttpl.ResSheetsSettingsDto;
import com.springreport.dto.reporttpl.ShareDto;
import com.springreport.dto.sysrolereport.MesRoleReportDto;
import com.springreport.entity.reporttpl.ReportTpl;
import com.springreport.entity.reporttype.ReportType;
import com.springreport.entity.sysuser.SysUser;
import com.springreport.entity.sysuserrole.SysUserRole;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;
import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.Pako_GzipUtils;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;

import cn.hutool.json.JSONUtil;
import net.sf.jsqlparser.JSQLParserException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reporttpl.IReportTplFormsService;
import com.springreport.api.reporttpl.IReportTplService;
import com.springreport.api.sysuser.ISysUserService;
import com.springreport.api.sysuserrole.ISysUserRoleService;

 /**  
* @Description: ReportTplcontroller类
* @author 
* @date 2021-02-13 11:16:33
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/reportTpl")
public class ReportTplController extends BaseController {

	/**
	 * iReportTplService服务注入
	 */
	@Autowired
	private IReportTplService iReportTplService;
	
	@Autowired
	private IReportTplFormsService iReportTplFormsService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private ISysUserService iSysUserService;
	
	@Autowired
	private ISysUserRoleService iSysUserRoleService;

	/** 
	* @Description: 分页查询表格
	* @param ReportTpl
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"reportTpl_search","viewReport_Search","excelTemplate_search"},logical = Logical.OR)
	public Response getTableList(@RequestBody ReportTpl model)
	{
		PageEntity result = iReportTplService.tablePagingQuery(model);
		return Response.success(result);
	}
	
	@RequestMapping(value = "/getChildren",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"reportTpl_search","excelTemplate_search"},logical = Logical.OR)
	public Response getChildren(@RequestBody ReportTpl model)
	{
		List<ReportTplTreeDto> result = iReportTplService.getChildren(model);
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
	@MethodLog(module="ReportTpl",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportTpl_getDetail","reportTpl_update","reportTpl_reportDesign","reportDesign_previewReport","reportTpl_reportView","reportTpl_copy","viewReport_view","template_market","excelTemplate_getDetail","excelTemplate_update","excelTemplate_design"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iReportTplService.getDetail(id);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getShareDetail
	 * @Description: 分享获取详情
	 * @author caiyang
	 * @param id
	 * @return
	 * @throws Exception Response
	 * @date 2023-06-27 08:57:03 
	 */ 
	@RequestMapping(value = "/getShareDetail",method = RequestMethod.GET)
	@MethodLog(module="ReportTpl",remark="分享获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getShareDetail(@RequestParam Long id) throws Exception
	{
		String shareCode = this.httpServletRequest.getHeader("shareCode");
		String shareUser = this.httpServletRequest.getHeader("shareUser");
		if(StringUtil.isNullOrEmpty(shareCode) || StringUtil.isNullOrEmpty(shareUser))
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		Object redisShareCode = redisUtil.get(RedisPrefixEnum.SHAREREPORT.getCode()+shareCode);;
		if(redisShareCode == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.time"));
		}
		BaseEntity result = iReportTplService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param ReportTpl
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"tplCode:required#报表标识;length#报表标识#40","tplName:required#报表名称;length#报表名称#40"})
	@RequiresPermissions(value = {"reportTpl_insert","excelTemplate_insert"},logical = Logical.OR)
	public Response insert(@RequestBody ReportTplDto model) throws Exception
	{
		BaseEntity result = iReportTplService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param ReportTpl
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","tplCode:required#报表标识;length#报表标识#40","tplName:required#报表名称;length#报表名称#40"})
	@RequiresPermissions(value = {"reportTpl_update","excelTemplate_update"},logical = Logical.OR)
	public Response update(@RequestBody ReportTplDto model) throws Exception
	{
		BaseEntity result = iReportTplService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ReportTpl",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportTpl_delete","excelTemplate_deleteOne"},logical = Logical.OR)
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iReportTplService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param ReportTpl 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"reportTpl_batchDelete"})
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iReportTplService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	
	/** 
	* @Description: 获取所有的报表
	* @param ReportTpl
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getReports",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="获取所有的报表",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getReports()
	{
		List<ReportTpl> result = iReportTplService.getReports();
		return Response.success(result);
	}
	
	/**
	*<p>Title: saveLuckySheetTpl</p>
	*<p>Description: 保存luckysheet模板</p>
	* @author caiyang
	* @param reportTplDto
	* @return
	* @throws Exception
	*/
	@RequestMapping(value = "/saveLuckySheetTpl",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="保存模板",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"tplId:required#主键ID"})
	@RequiresPermissions(value = {"reportDesign_saveLuckyTpl","excelTemplate_design"},logical = Logical.OR)
	public Response saveLuckySheetTpl(@RequestBody MesLuckysheetsTplDto mesLuckysheetsTplDto,@LoginUser UserInfoDto userInfoDto) throws Exception
	{
		BaseEntity result = this.iReportTplService.saveLuckySheetTpl(mesLuckysheetsTplDto,userInfoDto);
		return Response.success(result,result.getStatusMsg());
	}
	
	/**  
	 * @Title: getTplSettings
	 * @Description: 获取模板设置
	 * @param reportTpl
	 * @return
	 * @author caiyang
	 * @throws IOException 
	 * @date 2021-05-25 03:51:25 
	 */ 
	@RequestMapping(value = "/getLuckySheetTplSettings",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="获取模板设置",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportTpl_reportDesign","excelTemplate_design","template_market"},logical = Logical.OR)
	public Response getLuckySheetTplSettings(@RequestBody ReportTplDto reportTpl,@LoginUser UserInfoDto userInfoDto) throws Exception {
		ResSheetsSettingsDto result = this.iReportTplService.getLuckySheetTplSettings(reportTpl,userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @Title: previewLuckysheetReportData
	 * @Description: luckysheet预览报表
	 * @param mesGenerateReportDto
	 * @return
	 * @throws JSQLParserException
	 * @author caiyang
	 * @date 2022-02-02 08:28:40 
	 */ 
	@RequestMapping(value = "/previewLuckysheetReportData",method = RequestMethod.POST)
//	@MethodLog(module="ReportTpl",remark="预览luckysheet报表",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板"})
	@RequiresPermissions(value = {"reportDesign_previewReport","reportTpl_reportView","viewReport_view","template_market","excelTemplate_viewReport"},logical = Logical.OR)
	public String previewLuckysheetReportData(@RequestBody MesGenerateReportDto mesGenerateReportDto,@LoginUser UserInfoDto userInfoDto) throws Exception
	{
		ResPreviewData result = this.iReportTplService.previewLuckysheetReportData(mesGenerateReportDto,userInfoDto);
		if(ListUtil.isNotEmpty(result.getSheetDatas())) {
			for (int i = 0; i < result.getSheetDatas().size(); i++) {
				result.getSheetDatas().get(i).setCellBindData(null);
			}
		}
		httpServletResponse.setHeader("Content-Encoding", "gzip");
		httpServletResponse.setContentType("text/html");
		String resultStr="";
		Response response = new Response();
		response.setCode("200");
		response.setResponseData(result);
		resultStr=JSONUtil.toJsonStr(response);
		try {
	         byte dest[]= Pako_GzipUtils.compress2(resultStr);
	         OutputStream out=httpServletResponse.getOutputStream();
	         out.write(dest);
	         out.close();
	         out.flush();
	     } catch (Exception e) {
	        e.printStackTrace();
	     }
		return null;
	}
	
	/**  
	 * @MethodName: previewShareLuckysheetReportData
	 * @Description: luckysheet预览报表(分享链接用)
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @return
	 * @throws Exception Response
	 * @date 2023-06-26 11:05:33 
	 */ 
	@RequestMapping(value = "/previewShareLuckysheetReportData",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="分享预览luckysheet报表",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板"})
	public String previewShareLuckysheetReportData(@RequestBody MesGenerateReportDto mesGenerateReportDto) throws Exception
	{
		String shareCode = this.httpServletRequest.getHeader("shareCode");
		String shareUser = this.httpServletRequest.getHeader("shareUser");
		if(StringUtil.isNullOrEmpty(shareCode) || StringUtil.isNullOrEmpty(shareUser))
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		Object redisShareCode = redisUtil.get(RedisPrefixEnum.SHAREREPORT.getCode()+shareCode);;
		if(redisShareCode == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.time"));
		}
		String thirdPartyType = MessageUtil.getValue("thirdParty.type");
		UserInfoDto userInfoDto = new UserInfoDto();
		SysUser sysUser = null;
		if(thirdPartyType.equals(shareUser)) {
			sysUser = new SysUser();
			sysUser.setIsAdmin(YesNoEnum.YES.getCode());
			userInfoDto.setIsAdmin(YesNoEnum.YES.getCode());
		}else {
			sysUser = iSysUserService.getById(shareUser);
			if(sysUser == null)
			{
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
			}
			userInfoDto.setUserId(sysUser.getId());
			userInfoDto.setIsAdmin(sysUser.getIsAdmin());
			userInfoDto.setMerchantNo(sysUser.getMerchantNo());
		}
		
		if(YesNoEnum.YES.getCode().intValue() != sysUser.getIsAdmin().intValue())
		{
			QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("user_id", sysUser.getId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			SysUserRole sysUserRole = this.iSysUserRoleService.getOne(queryWrapper, false);
			if(sysUserRole != null)
			{
				userInfoDto.setRoleId(sysUserRole.getRoleId());
			}
		}
		ResPreviewData result = this.iReportTplService.previewLuckysheetReportData(mesGenerateReportDto,userInfoDto);
		httpServletResponse.setHeader("Content-Encoding", "gzip");
		httpServletResponse.setContentType("text/html");
		String resultStr="";
		Response response = new Response();
		response.setCode("200");
		response.setResponseData(result);
		resultStr=JSONUtil.toJsonStr(response);
		try {
	         byte dest[]= Pako_GzipUtils.compress2(resultStr);
	         OutputStream out=httpServletResponse.getOutputStream();
	         out.write(dest);
	         out.close();
	         out.flush();
	     } catch (Exception e) {
	        e.printStackTrace();
	     }
		return null;
	}
	
	/**  
	 * @Title: exportExcel
	 * @Description: 导出excel数据
	 * @param mesGenerateReportDto
	 * @throws Exception
	 * @author caiyang
	 * @date 2021-06-10 02:34:54 
	 */ 
	@RequestMapping(value = "/luckySheetExportExcel",method = RequestMethod.POST)
	@RequiresPermissions(value = {"reportDesign_previewReport","reportTpl_reportView","viewReport_view","template_market"},logical = Logical.OR)
	public void luckySheetExportExcel(@RequestBody MesGenerateReportDto mesGenerateReportDto,@LoginUser UserInfoDto userInfoDto) throws Exception {
		this.iReportTplService.luckySheetExportExcel(mesGenerateReportDto,userInfoDto, httpServletResponse);
	}
	
	/**  
	 * @MethodName: shareLuckySheetExportExcel
	 * @Description: 导出excel数据（分享链接用）
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param userInfoDto
	 * @throws Exception void
	 * @date 2023-06-27 08:30:55 
	 */ 
	@RequestMapping(value = "/shareLuckySheetExportExcel",method = RequestMethod.POST)
	public void shareLuckySheetExportExcel(@RequestBody MesGenerateReportDto mesGenerateReportDto) throws Exception {
		String shareCode = this.httpServletRequest.getHeader("shareCode");
		String shareUser = this.httpServletRequest.getHeader("shareUser");
		if(StringUtil.isNullOrEmpty(shareCode) || StringUtil.isNullOrEmpty(shareUser))
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		Object redisShareCode = redisUtil.get(RedisPrefixEnum.SHAREREPORT.getCode()+shareCode);;
		if(redisShareCode == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.time"));
		}
		SysUser sysUser = iSysUserService.getById(shareUser);
		if(sysUser == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		UserInfoDto userInfoDto = new UserInfoDto();
		userInfoDto.setUserId(sysUser.getId());
		userInfoDto.setIsAdmin(sysUser.getIsAdmin());
		if(YesNoEnum.YES.getCode().intValue() != sysUser.getIsAdmin().intValue())
		{
			QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("user_id", sysUser.getId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			SysUserRole sysUserRole = this.iSysUserRoleService.getOne(queryWrapper, false);
			if(sysUserRole != null)
			{
				userInfoDto.setRoleId(sysUserRole.getRoleId());
			}
		}
		this.iReportTplService.luckySheetExportExcel(mesGenerateReportDto,userInfoDto, httpServletResponse);
	}
	
	/**  
	 * @MethodName: validateDesignPwd
	 * @Description: 校验设计密码
	 * @author caiyang
	 * @param reportTpl 
	 * @return void
	 * @date 2022-07-05 08:17:54 
	 */  
	@RequestMapping(value = "/validateDesignPwd",method = RequestMethod.POST)
	@RequiresPermissions(value = {"reportTpl_reportDesign"})
	@Check({"id:required#id","designPwd:required#密码"})
	public Response validateDesignPwd(@RequestBody ReportTpl reportTpl)
	{
		reportTpl = this.iReportTplService.validateDesignPwd(reportTpl);
		return Response.success(reportTpl);
	}
	
	/**  
	 * @MethodName: changeDesignPwd
	 * @Description: 修改设计密码
	 * @author caiyang
	 * @param reportTpl
	 * @return 
	 * @return Response
	 * @date 2022-07-05 05:46:02 
	 */  
	@RequestMapping(value = "/changeDesignPwd",method = RequestMethod.POST)
	@RequiresPermissions(value = {"reportTpl_changePwd"})
	@Check({"id:required#id","oldPwd:required#旧密码"})
	public Response changeDesignPwd(@RequestBody MesChangePwd model)
	{
		BaseEntity result = this.iReportTplService.changeDesignPwd(model);
		return Response.success(result,result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: getRoleReports
	 * @Description: 获取角色对应的报表
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return 
	 * @return Response
	 * @date 2022-07-06 08:27:43 
	 */  
	@RequestMapping(value = "/getRoleReports",method = RequestMethod.POST)
	@RequiresPermissions(value = {"viewReport_Search"})
	public Response getRoleReports(@RequestBody MesRoleReportDto model,@LoginUser UserInfoDto userInfoDto)
	{
		model.setIsAdmin(userInfoDto.getIsAdmin());
		model.setRoleId(userInfoDto.getRoleId());
		List<ReportTplTreeDto> result = this.iReportTplService.getRoleReports(model);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: saveReportFormsTpl
	 * @Description: 保存填报模板
	 * @author caiyang
	 * @param mesLuckysheetsTplDto
	 * @return
	 * @throws Exception 
	 * @return Response
	 * @date 2022-11-16 01:37:52 
	 */  
	@RequestMapping(value = "/saveReportFormsTpl",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="保存填报模板",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"tplId:required#主键ID"})
	@RequiresPermissions(value = {"reportForms_saveTpl"})
	public Response saveReportFormsTpl(@RequestBody MesLuckysheetsTplDto mesLuckysheetsTplDto,@LoginUser UserInfoDto userInfoDto) throws Exception
	{
		BaseEntity result = this.iReportTplService.saveReportFormsTpl(mesLuckysheetsTplDto,userInfoDto);
		return Response.success(result,result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: getReportFormsTpl
	 * @Description: 获取填报报表模板
	 * @author caiyang
	 * @param reportTpl
	 * @param userInfoDto
	 * @return
	 * @throws Exception 
	 * @return Response
	 * @date 2022-11-17 11:25:04 
	 */  
	@RequestMapping(value = "/getReportFormsTpl",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="获取填报报表模板",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportTpl_reportDesign"})
	public Response getReportFormsTpl(@RequestBody ReportTpl reportTpl,@LoginUser UserInfoDto userInfoDto) throws Exception
	{
		ResSheetsSettingsDto result = this.iReportTplService.getReportFormsTpl(reportTpl,userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @Title: previewLuckysheetFormsReportData
	 * @Description: luckysheet预览报表
	 * @param mesGenerateReportDto
	 * @return
	 * @throws JSQLParserException
	 * @author caiyang
	 * @date 2022-02-02 08:28:40 
	 */ 
	@RequestMapping(value = "/previewLuckysheetFormsReportData",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="预览luckysheet报表",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板"})
	@RequiresPermissions(value = {"reportForms_previewReport","reportTpl_reportView","viewReport_view"},logical = Logical.OR)
	public Response previewLuckysheetFormsReportData(@RequestBody MesGenerateReportDto mesGenerateReportDto,@LoginUser UserInfoDto userInfoDto) throws Exception
	{
//		ResPreviewData result = this.iReportTplFormsService.previewLuckysheetReportFormsData(mesGenerateReportDto,userInfoDto);
		return Response.success(null);
	}
	
	/**  
	 * @Title: previewShareLuckysheetFormsReportData
	 * @Description: luckysheet预览报表(分享链接用)
	 * @param mesGenerateReportDto
	 * @return
	 * @throws JSQLParserException
	 * @author caiyang
	 * @date 2022-02-02 08:28:40 
	 */ 
	@RequestMapping(value = "/previewShareLuckysheetFormsReportData",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="预览luckysheet报表",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板"})
	public Response previewShareLuckysheetFormsReportData(@RequestBody MesGenerateReportDto mesGenerateReportDto) throws Exception
	{
		String shareCode = this.httpServletRequest.getHeader("shareCode");
		String shareUser = this.httpServletRequest.getHeader("shareUser");
		if(StringUtil.isNullOrEmpty(shareCode) || StringUtil.isNullOrEmpty(shareUser))
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		Object redisShareCode = redisUtil.get(RedisPrefixEnum.SHAREREPORT.getCode()+shareCode);;
		if(redisShareCode == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.time"));
		}
		SysUser sysUser = iSysUserService.getById(shareUser);
		if(sysUser == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		UserInfoDto userInfoDto = new UserInfoDto();
		userInfoDto.setUserId(sysUser.getId());
		userInfoDto.setIsAdmin(sysUser.getIsAdmin());
		if(YesNoEnum.YES.getCode().intValue() != sysUser.getIsAdmin().intValue())
		{
			QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("user_id", sysUser.getId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			SysUserRole sysUserRole = this.iSysUserRoleService.getOne(queryWrapper, false);
			if(sysUserRole != null)
			{
				userInfoDto.setRoleId(sysUserRole.getRoleId());
			}
		}
		ReportTpl reportTpl = this.iReportTplService.getById(mesGenerateReportDto.getTplId());
		if (reportTpl == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"报表模板"}));
		}
		ResPreviewData result = this.iReportTplFormsService.previewLuckysheetReportFormsData(mesGenerateReportDto,userInfoDto,reportTpl,true);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: reportData
	 * @Description: 上报数据
	 * @author caiyang
	 * @return 
	 * @return Response
	 * @date 2022-11-22 07:01:57 
	 */  
	@RequestMapping(value = "/reportData",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="上报数据",operateType=Constants.OPERATE_TYPE_UPDATE)
	@RequiresPermissions(value = {"reportForms_ReportData"},logical = Logical.OR)
	public Response reportData(@RequestBody ReportDataDto model,@LoginUser UserInfoDto userInfoDto) {
		BaseEntity result = this.iReportTplFormsService.reportData(model,userInfoDto);
		return Response.success(result,result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: shareReportData
	 * @Description: 上报数据(分享链接用)
	 * @author caiyang
	 * @return 
	 * @return Response
	 * @date 2022-11-22 07:01:57 
	 */  
	@RequestMapping(value = "/shareReportData",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="上报数据",operateType=Constants.OPERATE_TYPE_UPDATE)
	public Response shareReportData(@RequestBody ReportDataDto model) {
		String shareCode = this.httpServletRequest.getHeader("shareCode");
		String shareUser = this.httpServletRequest.getHeader("shareUser");
		if(StringUtil.isNullOrEmpty(shareCode) || StringUtil.isNullOrEmpty(shareUser))
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		Object redisShareCode = redisUtil.get(RedisPrefixEnum.SHAREREPORT.getCode()+shareCode);;
		if(redisShareCode == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.time"));
		}
		if(!"1".equals(String.valueOf(redisShareCode)))
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.submit.report"));
		}
		SysUser sysUser = iSysUserService.getById(shareUser);
		if(sysUser == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		UserInfoDto userInfoDto = new UserInfoDto();
		userInfoDto.setUserId(sysUser.getId());
		userInfoDto.setIsAdmin(sysUser.getIsAdmin());
		if(YesNoEnum.YES.getCode().intValue() != sysUser.getIsAdmin().intValue())
		{
			QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("user_id", sysUser.getId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			SysUserRole sysUserRole = this.iSysUserRoleService.getOne(queryWrapper, false);
			if(sysUserRole != null)
			{
				userInfoDto.setRoleId(sysUserRole.getRoleId());
			}
		}
		BaseEntity result = this.iReportTplFormsService.reportData(model,userInfoDto);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: copyReport
	 * @Description: 复制报表
	 * @author caiyang
	 * @param model
	 * @return 
	 * @return Response
	 * @date 2022-12-05 03:13:41 
	 */  
	@RequestMapping(value = "/copyReport",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="复制报表",operateType=Constants.OPERATE_TYPE_ADD)
	@RequiresPermissions(value = {"reportTpl_copy","template_market"},logical = Logical.OR)
	public Response copyReport(@RequestBody ReportTplDto model) {
		BaseEntity result = this.iReportTplService.copyReport(model);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: transf2OnlineReport
	 * @Description: 转成在线报表文档
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @return 
	 * @return Response
	 * @throws Exception 
	 * @date 2023-02-06 08:30:51 
	 */  
	@RequestMapping(value = "/transf2OnlineReport",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="转成在线报表文档",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板"})
	@RequiresPermissions(value = {"reportForms_previewReport","reportTpl_reportView","viewReport_view"},logical = Logical.OR)
	public Response transf2OnlineReport(@RequestBody MesGenerateReportDto mesGenerateReportDto,@LoginUser UserInfoDto userInfoDto) throws Exception {
		BaseEntity result = this.iReportTplService.transf2OnlineReport(mesGenerateReportDto, userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getSheetPdf
	 * @Description: 生成sheet页的pdf并返回访问路径
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param userInfoDto
	 * @return 
	 * @return Response
	 * @throws Exception 
	 * @date 2023-04-03 01:46:58 
	 */  
	@RequestMapping(value = "/getSheetPdf",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="生成sheet页的pdf并返回访问路径",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板"})
	@RequiresPermissions(value = {"reportDesign_previewReport","reportTpl_reportView","viewReport_view","template_market"},logical = Logical.OR)
	public Response getSheetPdf(@RequestBody MesGenerateReportDto mesGenerateReportDto,@LoginUser UserInfoDto userInfoDto) throws Exception {
		Map<String, Object> result = this.iReportTplService.getSheetPdf(mesGenerateReportDto, userInfoDto, httpServletResponse);
		return Response.success(result);
	}
	
	@RequestMapping(value = "/getShareSheetPdf",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="生成sheet页的pdf并返回访问路径",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板"})
	public Response getShareSheetPdf(@RequestBody MesGenerateReportDto mesGenerateReportDto) throws Exception {
		String shareCode = this.httpServletRequest.getHeader("shareCode");
		String shareUser = this.httpServletRequest.getHeader("shareUser");
		if(StringUtil.isNullOrEmpty(shareCode) || StringUtil.isNullOrEmpty(shareUser))
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		Object redisShareCode = redisUtil.get(RedisPrefixEnum.SHAREREPORT.getCode()+shareCode);;
		if(redisShareCode == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.time"));
		}
		SysUser sysUser = iSysUserService.getById(shareUser);
		if(sysUser == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		UserInfoDto userInfoDto = new UserInfoDto();
		userInfoDto.setUserId(sysUser.getId());
		userInfoDto.setIsAdmin(sysUser.getIsAdmin());
		if(YesNoEnum.YES.getCode().intValue() != sysUser.getIsAdmin().intValue())
		{
			QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("user_id", sysUser.getId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			SysUserRole sysUserRole = this.iSysUserRoleService.getOne(queryWrapper, false);
			if(sysUserRole != null)
			{
				userInfoDto.setRoleId(sysUserRole.getRoleId());
			}
		}
		Map<String, Object> result = this.iReportTplService.getSheetPdf(mesGenerateReportDto, userInfoDto, httpServletResponse);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: uploadExcel
	 * @Description: 上传excel文件解析其中的数据
	 * @author caiyang
	 * @param file
	 * @return 
	 * @return Response
	 * @throws IOException 
	 * @throws EncryptedDocumentException 
	 * @throws Exception 
	 * @date 2023年4月29日20:36:09
	 */ 
	@RequestMapping("/uploadExcel")
	public Response uploadExcel(@RequestParam("file") MultipartFile file,@RequestParam("gridKey") String gridKey,@LoginUser UserInfoDto userInfoDto) throws Exception  {
		JSONArray result = this.iReportTplService.uploadExcel(file,gridKey,userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getShareUrl
	 * @Description: 获取分享的url
	 * @author caiyang
	 * @param userInfoDto
	 * @return Response
	 * @date 2023-06-25 01:55:01 
	 */  
	@RequestMapping("/getShareUrl")
	@Check({"tplId:required#模板","shareType:required#分享类型"})
	public Response getShareUrl(@RequestBody ShareDto shareDto, @LoginUser UserInfoDto userInfoDto) {
		ShareDto result = this.iReportTplService.getShareUrl(shareDto, userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getMobileInfo
	 * @Description: 获取手机报表信息
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param userInfoDto
	 * @return
	 * @throws Exception Response
	 * @date 2023-06-29 09:43:14 
	 */ 
	@RequestMapping(value = "/getMobileReport",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="获取手机报表信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板"})
	@RequiresPermissions(value = {"reportDesign_previewReport","reportTpl_reportView","viewReport_view","excelTemplate_viewReport"},logical = Logical.OR)
	public Response getMobileReport(@RequestBody MesGenerateReportDto mesGenerateReportDto,@LoginUser UserInfoDto userInfoDto) throws Exception {
		MobilePreviewDto result = this.iReportTplService.getMobileInfo(mesGenerateReportDto, userInfoDto);
		result.setActiveSheet(mesGenerateReportDto.getActiveSheet());
		return Response.success(result);
	}
	
	@RequestMapping(value = "/getMobileShareReport",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="获取分享手机报表信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板"})
	public Response getMobileShareReport(@RequestBody MesGenerateReportDto mesGenerateReportDto) throws Exception {
		String shareCode = this.httpServletRequest.getHeader("shareCode");
		String shareUser = this.httpServletRequest.getHeader("shareUser");
		if(StringUtil.isNullOrEmpty(shareCode) || StringUtil.isNullOrEmpty(shareUser))
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		Object redisShareCode = redisUtil.get(RedisPrefixEnum.SHAREREPORT.getCode()+shareCode);;
		if(redisShareCode == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.time"));
		}
		String thirdPartyType = MessageUtil.getValue("thirdParty.type");
		UserInfoDto userInfoDto = new UserInfoDto();
		SysUser sysUser = null;
		if(thirdPartyType.equals(shareUser)) {
			sysUser = new SysUser();
			sysUser.setIsAdmin(YesNoEnum.YES.getCode());
			userInfoDto.setIsAdmin(YesNoEnum.YES.getCode());
		}else {
			sysUser = iSysUserService.getById(shareUser);
			if(sysUser == null)
			{
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
			}
			userInfoDto.setUserId(sysUser.getId());
			userInfoDto.setIsAdmin(sysUser.getIsAdmin());
		}
		if(YesNoEnum.YES.getCode().intValue() != sysUser.getIsAdmin().intValue())
		{
			QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("user_id", sysUser.getId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			SysUserRole sysUserRole = this.iSysUserRoleService.getOne(queryWrapper, false);
			if(sysUserRole != null)
			{
				userInfoDto.setRoleId(sysUserRole.getRoleId());
			}
		}
		MobilePreviewDto result = this.iReportTplService.getMobileInfo(mesGenerateReportDto, userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @Title: exportExcel
	 * @Description: 导出excel数据
	 * @param mesGenerateReportDto
	 * @throws Exception
	 * @author caiyang
	 * @date 2021-06-10 02:34:54 
	 */ 
	@RequestMapping(value = "/getSheetPdfStream",method = RequestMethod.POST)
	@RequiresPermissions(value = {"reportDesign_previewReport","reportTpl_reportView","viewReport_view","template_market"},logical = Logical.OR)
	public void getSheetPdfStream(@RequestBody MesGenerateReportDto mesGenerateReportDto,@LoginUser UserInfoDto userInfoDto) throws Exception {
		this.iReportTplService.getSheetPdfStream(mesGenerateReportDto,userInfoDto, httpServletResponse);
	}
	
	/**  
	 * @MethodName: getShareSheetPdfStream
	 * @Description: 分享获取pdf打印文件流
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @return
	 * @throws Exception Response
	 * @date 2023-12-06 11:26:14 
	 */ 
	@RequestMapping(value = "/getShareSheetPdfStream",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="分享获取pdf打印文件流",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板"})
	public void getShareSheetPdfStream(@RequestBody MesGenerateReportDto mesGenerateReportDto) throws Exception {
		String shareCode = this.httpServletRequest.getHeader("shareCode");
		String shareUser = this.httpServletRequest.getHeader("shareUser");
		if(StringUtil.isNullOrEmpty(shareCode) || StringUtil.isNullOrEmpty(shareUser))
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		Object redisShareCode = redisUtil.get(RedisPrefixEnum.SHAREREPORT.getCode()+shareCode);;
		if(redisShareCode == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.time"));
		}
		SysUser sysUser = iSysUserService.getById(shareUser);
		if(sysUser == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		UserInfoDto userInfoDto = new UserInfoDto();
		userInfoDto.setUserId(sysUser.getId());
		userInfoDto.setIsAdmin(sysUser.getIsAdmin());
		if(YesNoEnum.YES.getCode().intValue() != sysUser.getIsAdmin().intValue())
		{
			QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("user_id", sysUser.getId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			SysUserRole sysUserRole = this.iSysUserRoleService.getOne(queryWrapper, false);
			if(sysUserRole != null)
			{
				userInfoDto.setRoleId(sysUserRole.getRoleId());
			}
		}
		this.iReportTplService.getSheetPdfStream(mesGenerateReportDto,userInfoDto, httpServletResponse);
	}
	
	/**  
	 * @MethodName: uploadReportTpl
	 * @Description: 上传报表模板(excel)
	 * @author caiyang
	 * @param file
	 * @param userInfoDto
	 * @return
	 * @throws Exception Response
	 * @date 2023-12-13 10:10:35 
	 */ 
	@RequestMapping("/uploadReportTpl")
	public String uploadReportTpl(@RequestParam("file") MultipartFile file,@RequestParam("tplId") long tplId,@RequestParam("isFormsReport") int isFormsReport,@LoginUser UserInfoDto userInfoDto) throws Exception  {
		httpServletResponse.setHeader("Content-Encoding", "gzip");
		httpServletResponse.setContentType("text/html");
		String resultStr="";
		JSONArray result = this.iReportTplService.uploadReportTpl(file, tplId,isFormsReport, userInfoDto);;
		Response response = new Response();
		response.setCode("200");
		response.setResponseData(result);
		resultStr=JSONUtil.toJsonStr(response);
		try {
	         byte dest[]= Pako_GzipUtils.compress2(resultStr);
	         OutputStream out=httpServletResponse.getOutputStream();
	         out.write(dest);
	         out.close();
	         out.flush();
	     } catch (Exception e) {
	        e.printStackTrace();
	     }
		return null;
	}
	
	/** 
	* @Description: 获取所有的模板
	* @param ReportTpl
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getAllTpls",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="获取所有的模板",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getAllTpls(@RequestBody ReportTpl model)
	{
		List<ReportTpl> tpls = iReportTplService.getAllTpls(model);
		return Response.success(tpls);
	}
	
	/**  
	 * @MethodName: getTplAuth
	 * @Description: 获取模板授权范围
	 * @author caiyang
	 * @param userInfoDto
	 * @return Response
	 * @date 2024-03-07 06:28:47 
	 */ 
	@RequestMapping(value = "/getTplAuth",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="获取模板权限",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTplAuth(@RequestBody ReportTpl model,@LoginUser UserInfoDto userInfoDto)
	{
		JSONObject result = this.iReportTplService.getTplAuth(model, userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: deleteReportData
	 * @Description: 填报报表删除数据
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return Response
	 * @date 2025-02-17 12:09:52 
	 */ 
	@RequestMapping(value = "/deleteReportData",method = RequestMethod.POST)
	@MethodLog(module="ReportTpl",remark="填报报表删除数据",operateType=Constants.OPERATE_TYPE_DELETE)
	public Response deleteReportData(@RequestBody JSONObject model,@LoginUser UserInfoDto userInfoDto) {
		BaseEntity result = this.iReportTplFormsService.deleteReportData(model,userInfoDto);
		return Response.success(result,result.getStatusMsg());
	}
}
