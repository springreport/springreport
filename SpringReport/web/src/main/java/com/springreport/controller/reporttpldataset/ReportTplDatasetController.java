/** 
 * 模块：报表系统-ReportTplDataset
 */
package com.springreport.controller.reporttpldataset;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.dto.reportdatasource.ApiTestResultDto;
import com.springreport.dto.reporttpldataset.MesGetRelyOnSelectData;
import com.springreport.dto.reporttpldataset.MesScreenGetSqlDataDto;
import com.springreport.dto.reporttpldataset.ReportDatasetDto;
import com.springreport.dto.reporttpldataset.ReportTplDatasetDto;
import com.springreport.entity.reporttpldataset.ReportTplDataset;
import com.springreport.entity.reporttpldatasetgroup.ReportTplDatasetGroup;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.exception.BizException;
import com.springreport.util.MessageUtil;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;

import net.sf.jsqlparser.JSQLParserException;

import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reporttpldataset.IReportTplDatasetService;

 /**  
* @Description: ReportTplDatasetcontroller类
* @author 
* @date 2021-02-13 11:16:39
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/reportTplDataset")
public class ReportTplDatasetController extends BaseController {

	/**
	 * iReportTplDatasetService服务注入
	 */
	@Autowired
	private IReportTplDatasetService iReportTplDatasetService;
	
	@Autowired
	private RedisUtil redisUtil;


	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ReportTplDataset",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportDesign_deleteDataSet","screenTpl_screenDesign","reportForms_deleteDataSet"},logical = Logical.OR)
	public Response delete(@RequestParam Long id,@LoginUser UserInfoDto userInfoDto)
	{
		BaseEntity result = iReportTplDatasetService.delete(id);
		Map<String, String> data = new HashMap<>();
		data.put("id", String.valueOf(id));
		data.put("userName", userInfoDto.getUserName());
		return Response.success(data,result.getStatusMsg());
	}

	/** 
	* @Description: 获取模板关联的数据集
	* @param ReportTplDataset 
	* @return Response 
	 * @throws Exception 
	 * @throws 
	*/ 
	@RequestMapping(value = "/getTplDatasets",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDataset",remark="获取模板关联的数据集",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板ID"})
	@RequiresPermissions(value = {"reportTpl_reportDesign","screenTpl_screenDesign","slidTpl_design","docTpl_design","excelTemplate_design","template_market","wordTemplate_design","screenTemplate_design"},logical = Logical.OR)
	public Response getTplDatasets(@RequestBody ReportTplDataset dataset,@LoginUser UserInfoDto userInfoDto) throws Exception {
		List<ReportDatasetDto> result = this.iReportTplDatasetService.getTplDatasets(dataset,userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getDatasetColumns
	 * @Description: 获取数据集字段
	 * @author caiyang
	 * @param dataset
	 * @return 
	 * @return Response
	 * @throws Exception 
	 * @date 2022-10-12 06:38:23 
	 */  
	@RequestMapping(value = "/getDatasetColumns",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDataset",remark="获取数据集字段",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键"})
	@RequiresPermissions(value = {"reportTpl_reportDesign","screenTpl_screenDesign","screenTemplate_design"},logical = Logical.OR)
	public Response getDatasetColumns(@RequestBody ReportTplDataset dataset,@LoginUser UserInfoDto userInfoDto) throws Exception
	{
		List<Map<String, Object>> result = this.iReportTplDatasetService.getDatasetColumns(dataset,userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getApiDefaultRequestResult
	 * @Description: 获取api数据集默认参数请求结果
	 * @author caiyang
	 * @param dataset
	 * @return 
	 * @return Response
	 * @date 2023-01-11 10:38:55 
	 */  
	@RequestMapping(value = "/getApiDefaultRequestResult",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDataset",remark="获取api数据集默认参数请求结果",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键"})
	@RequiresPermissions(value = {"reportTpl_reportDesign","screenTpl_screenDesign"},logical = Logical.OR)
	public Response getApiDefaultRequestResult(@RequestBody ReportTplDataset dataset) {
		ApiTestResultDto result = this.iReportTplDatasetService.getApiDefaultRequestResult(dataset);
		return Response.success(result);
	}
	
	/**
	*<p>Title: addTplDataSets</p>
	*<p>Description: 报表模板添加数据集</p>
	* @author caiyang
	* @param reportTplDataset
	* @return
	 * @throws Exception 
	*/
	@RequestMapping(value = "/addTplDataSets",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDataset",remark="添加模板数据集",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"datasetName:required#数据集名称","tplId:required#模板id","datasourceId:required#数据源id"})
	@RequiresPermissions(value = {"reportDesign_addDataSet","reportDesign_editDataSet","reportForms_addDataSet","reportForms_editDataSet","slidTpl_design","docTpl_design"},logical = Logical.OR)
	public Response addTplDataSets(@RequestBody ReportTplDataset reportTplDataset,@LoginUser UserInfoDto userInfoDto) throws Exception {
		ReportDatasetDto result = this.iReportTplDatasetService.addTplDataSets(reportTplDataset,userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @Title: getReportDatasetsParam
	 * @Description: 预览报表获取报表数据集参数
	 * @param reportTplDataset
	 * @return
	 * @author caiyang
	 * @throws ParseException 
	 * @date 2021-06-03 02:16:17 
	 */ 
	@RequestMapping(value = "/getReportDatasetsParam",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDataset",remark="预览获取报表数据集参数",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板id"})
	@RequiresPermissions(value = {"reportDesign_previewReport","reportTpl_reportView","viewReport_view","docTpl_view","slide_view","template_market","excelTemplate_viewReport","wordTemplate_view"},logical = Logical.OR)
	public Response getReportDatasetsParam(@RequestBody ReportTplDatasetDto reportTplDataset) throws ParseException
	{
		Map<String, Object> result = this.iReportTplDatasetService.getReportDatasetsParam(reportTplDataset);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getShareReportDatasetsParam
	 * @Description: 预览报表获取报表数据集参数(分享链接用)
	 * @author caiyang
	 * @param reportTplDataset
	 * @return
	 * @throws ParseException Response
	 * @date 2023-06-26 09:56:30 
	 */ 
	@RequestMapping(value = "/getShareReportDatasetsParam",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDataset",remark="分享预览获取报表数据集参数",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板id"})
	public Response getShareReportDatasetsParam(@RequestBody ReportTplDatasetDto reportTplDataset) throws ParseException
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
		Map<String, Object> result = this.iReportTplDatasetService.getReportDatasetsParam(reportTplDataset);
		return Response.success(result);
	}
	
	
	/**  
	 * @Title: getTplDatasetColumns
	 * @Description: 获取数据集的列
	 * @param reportTplDataset
	 * @return
	 * @author caiyang
	 * @throws Exception 
	 * @date 2021-07-14 05:05:18 
	 */ 
	@RequestMapping(value = "/getTplDatasetColumns",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDataset",remark="获取数据集的列",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#数据集id"})
	@RequiresPermissions(value = {"screenTpl_screenDesign"})
	public Response getTplDatasetColumns(@RequestBody ReportTplDataset reportTplDataset,@LoginUser UserInfoDto userInfoDto) throws Exception
	{
		List<Map<String, Object>> result = this.iReportTplDatasetService.getTplDatasetColumns(reportTplDataset,userInfoDto);
		return Response.success(result);
	}
	
	
	/**  
	 * @MethodName: getSelectData
	 * @Description: 获取下拉数据
	 * @author caiyang
	 * @param mesGetRelyOnSelectData
	 * @return 
	 * @return Response
	 * @throws JSQLParserException 
	 * @date 2022-08-08 08:18:39 
	 */  
	@RequestMapping(value = "/getSelectData",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDataset",remark="获取下拉数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"selectContent:required#查询sql","datasourceId:required#数据源id","params:required#参数"})
//	@RequiresPermissions(value = {"reportDesign_previewReport","reportTpl_reportView","viewReport_view"},logical = Logical.OR)
	public Response getSelectData(@RequestBody MesGetRelyOnSelectData mesGetRelyOnSelectData) throws JSQLParserException {
		List<Map<String, Object>> result = this.iReportTplDatasetService.getSelectData(mesGetRelyOnSelectData);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getRelyOnSelectData
	 * @Description: 获取依赖项的下拉数据
	 * @author caiyang
	 * @param mesGetRelyOnSelectData
	 * @return 
	 * @return Response
	 * @throws JSQLParserException 
	 * @date 2022-08-08 08:18:39 
	 */  
	@RequestMapping(value = "/getRelyOnData",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDataset",remark="获取依赖项的下拉数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"selectContent:required#查询sql","datasourceId:required#数据源id","params:required#参数"})
//	@RequiresPermissions(value = {"reportDesign_previewReport","reportTpl_reportView","viewReport_view"},logical = Logical.OR)
	public Response getRelyOnSelectData(@RequestBody MesGetRelyOnSelectData mesGetRelyOnSelectData) throws JSQLParserException {
		List<Map<String, Object>> result = this.iReportTplDatasetService.getRelyOnSelectData(mesGetRelyOnSelectData);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getTreeSelectData
	 * @Description: 获取下拉树数据
	 * @author caiyang
	 * @param mesGetRelyOnSelectData
	 * @return 
	 * @return Response
	 * @throws JSQLParserException 
	 * @date 2022-08-08 08:18:39 
	 */  
	@RequestMapping(value = "/getTreeSelectData",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDataset",remark="获取下拉数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"selectContent:required#查询sql","datasourceId:required#数据源id","params:required#参数"})
//	@RequiresPermissions(value = {"reportDesign_previewReport","reportTpl_reportView","viewReport_view"},logical = Logical.OR)
	public Response getTreeSelectData(@RequestBody MesGetRelyOnSelectData mesGetRelyOnSelectData) throws JSQLParserException {
		List<Map<String, Object>> result = this.iReportTplDatasetService.getTreeSelectData(mesGetRelyOnSelectData);
		return Response.success(result);
	}
	
	/** 
	* @Description: 获取模板关联的数据集（分组）
	* @param ReportTplDataset 
	* @return Response 
	 * @throws Exception 
	 * @throws 
	*/ 
	@RequestMapping(value = "/getTplGroupDatasets",method = RequestMethod.POST)
	@MethodLog(module="ReportTplDataset",remark="获取模板关联的数据集(分组)",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplId:required#模板ID"})
	@RequiresPermissions(value = {"reportTpl_reportDesign","screenTpl_screenDesign","slidTpl_design","docTpl_design","excelTemplate_design","template_market","wordTemplate_design"},logical = Logical.OR)
	public Response getTplGroupDatasets(@RequestBody ReportTplDataset dataset,@LoginUser UserInfoDto userInfoDto) throws Exception {
		List<ReportTplDatasetGroup> result = this.iReportTplDatasetService.getTplGroupDatasets(dataset,userInfoDto);
		return Response.success(result);
	}
	
}
