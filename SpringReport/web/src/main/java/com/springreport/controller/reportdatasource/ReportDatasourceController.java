/** 
 * 模块：报表系统-ReportDatasource
 */
package com.springreport.controller.reportdatasource;

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
import com.springreport.dto.reportdatasource.ApiTestResultDto;
import com.springreport.dto.reportdatasource.MesGetSelectDataDto;
import com.springreport.dto.reportdatasource.MesReportDatasourceDto;
import com.springreport.dto.reporttpldatasource.MesExecSqlDto;
import com.springreport.entity.reportdatasource.ReportDatasource;
import com.springreport.entity.reporttpldataset.ReportTplDataset;
import com.alibaba.fastjson.JSONArray;
import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reportdatasource.IReportDatasourceService;
 /**  
* @Description: ReportDatasourcecontroller类
* @author 
* @date 2021-02-09 01:18:28
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/reportDatasource")
public class ReportDatasourceController extends BaseController {

	/**
	 * iReportDatasourceService服务注入
	 */
	@Autowired
	private IReportDatasourceService iReportDatasourceService;
	
	/** 
	* @Description: 分页查询表格
	* @param ReportDatasource
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasource",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"reportDatasource_search"})
	public Response getTableList(@RequestBody ReportDatasource model)
	{
		BaseEntity result = new BaseEntity();
		result = iReportDatasourceService.tablePagingQuery(model);
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
	@MethodLog(module="ReportDatasource",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportDatasource_getDetail","reportDatasource_update"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iReportDatasourceService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param ReportDatasource
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasource",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"code:required#编码;length#编码#50","name:required#数据库名称;length#数据库名称#40","jdbcUrl:required#数据库链接url;length#数据库链接url#500"})
	@RequiresPermissions(value = {"reportDatasource_insert"})
	public Response insert(@RequestBody ReportDatasource model) throws Exception
	{
		BaseEntity result = iReportDatasourceService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param ReportDatasource
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasource",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","code:required#编码;length#编码#50","name:required#数据库名称;length#数据库名称#40","jdbcUrl:required#数据库链接url;length#数据库链接url#500"})
	@RequiresPermissions(value = {"reportDatasource_update"})
	public Response update(@RequestBody ReportDatasource model) throws Exception
	{
		BaseEntity result = iReportDatasourceService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ReportDatasource",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportDatasource_delete"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iReportDatasourceService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param ReportDatasource 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasource",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"reportDatasource_batchDelete"})
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iReportDatasourceService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/** 
	* @Description: 获取数据源
	* @return Response 
	* @throws 
	*/
	@RequestMapping(value = "/getReportDatasource",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasource",remark="获取数据源",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"reportTpl_insert","reportTpl_update","reportTpl_getDetail","screenTpl_screenDesign","reportTpl_search","screenTpl_search"},logical = Logical.OR)
	public Response getReportDatasource(@RequestBody MesReportDatasourceDto mesReportDatasourceDto) {
		List<ReportDatasource> result = this.iReportDatasourceService.getReportDatasource(mesReportDatasourceDto);
		return Response.success(result);
	}
	
	/**
	*<p>Title: execSql</p>
	*<p>Description: 执行解析sql语句</p>
	* @author caiyang
	* @return
	 * @throws Exception 
	*/
	@RequestMapping(value = "/execSql",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasource",remark="执行解析sql语句",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"tplSql:required#sql语句","datasourceId:required#数据库id"})
	@RequiresPermissions(value = {"reportDesign_addDataSet","reportDesign_editDataSet","screenTpl_screenDesign","reportForms_addDataSet","reportForms_editDataSet","screenTemplate_design","wordTemplate_design","excelTemplate_design","template_market","slidTpl_design","docTpl_design"},logical = Logical.OR)
	public Response execSql(@RequestBody MesExecSqlDto mesExecSqlDto,@LoginUser UserInfoDto userInfoDto) throws Exception {
		List<Map<String, Object>> result = this.iReportDatasourceService.execSql(mesExecSqlDto,userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @Title: connectionTest
	 * @Description: 数据源连接测试
	 * @param reportDatasource
	 * @return
	 * @author caiyang
	 * @date 2022-04-24 01:17:46 
	 */ 
	@RequestMapping(value = "/connectionTest",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasource",remark="数据源连接测试",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"type:required#数据源类型","jdbcUrl:required#数据源连接"})
	public Response connectionTest(@RequestBody ReportDatasource reportDatasource) {
		BaseEntity result = this.iReportDatasourceService.connectionTest(reportDatasource);
		return Response.success(result,result.getStatusMsg());
	}
	
	/**  
	 * @Title: getDatasourceSelectData
	 * @Description: 获取下拉框数据
	 * @param mesGetSelectDataDto
	 * @return
	 * @author caiyang
	 * @date 2022年5月22日13:38:57
	 */ 
	@RequestMapping(value = "/getDatasourceSelectData",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasource",remark="获取下拉框数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"dataSourceId:required#数据源id"})
	public Response getDatasourceSelectData(@RequestBody MesGetSelectDataDto mesGetSelectDataDto) {
		List<Map<String, Object>> result = this.iReportDatasourceService.getDatasourceSelectData(mesGetSelectDataDto);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getDatabseTables
	 * @Description: 获取数据库的所有表信息
	 * @author caiyang
	 * @param datasource
	 * @return 
	 * @return Response
	 * @date 2022-11-15 07:58:17 
	 */  
	@RequestMapping(value = "/getDatabseTables",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasource",remark="获取数据库的所有表信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#数据源id"})
	public Response getDatabseTables(@RequestBody ReportDatasource datasource)
	{
		List<Map<String, String>> result = this.iReportDatasourceService.getDatabseTables(datasource);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: parseApiResultAttr
	 * @Description: 解析api数据集结果属性
	 * @author caiyang
	 * @param model
	 * @return Response
	 * @date 2024-12-24 02:56:08 
	 */ 
	@RequestMapping(value = "/parseApiResultAttr",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasource",remark="解析api数据集结果属性",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"apiResultType:required#返回值类型","apiRequestType:required#请求方式","jdbcUrl:required#请求链接"})
	public Response parseApiResultAttr(@RequestBody ReportDatasource model) {
		JSONArray result = this.iReportDatasourceService.parseApiResultAttr(model);
		return Response.success(result);
	}
}
