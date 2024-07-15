/** 
 * 模块：报表系统-ReportDatasourceDictData
 */
package com.springreport.controller.reportdatasourcedictdata;

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
import com.springreport.constants.StatusCode;
import com.springreport.entity.reportdatasourcedictdata.ReportDatasourceDictData;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.exception.BizException;
import com.springreport.util.MessageUtil;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reportdatasourcedictdata.IReportDatasourceDictDataService;

 /**  
* @Description: ReportDatasourceDictDatacontroller类
* @author 
* @date 2022-11-21 01:46:25
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/reportDatasourceDictData")
public class ReportDatasourceDictDataController extends BaseController {

	/**
	 * iReportDatasourceDictDataService服务注入
	 */
	@Autowired
	private IReportDatasourceDictDataService iReportDatasourceDictDataService;
	
	@Autowired
	private RedisUtil redisUtil;

	/** 
	* @Description: 分页查询表格
	* @param ReportDatasourceDictData
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictData",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"reportDatasourceDictType_dictData","reportDatasourceDictData_search"},logical = Logical.OR)
	public Response getTableList(@RequestBody ReportDatasourceDictData model)
	{
		BaseEntity result = new BaseEntity();
		result = iReportDatasourceDictDataService.tablePagingQuery(model);
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
	@MethodLog(module="ReportDatasourceDictData",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportDatasourceDictData_update","reportDatasourceDictData_getDetail"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iReportDatasourceDictDataService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param ReportDatasourceDictData
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictData",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"dictLabel:required#字典标签;length#字典标签#100","datasourceId:required#数据源ID","dictValue:required#字典键值;length#字典键值#100","dictType:required#字典类型;length#字典类型#100","remark:length#备注#500",})
	@RequiresPermissions(value = {"reportDatasourceDictData_insert"},logical = Logical.OR)
	public Response insert(@RequestBody ReportDatasourceDictData model) throws Exception
	{
		BaseEntity result = iReportDatasourceDictDataService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param ReportDatasourceDictData
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictData",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","datasourceId:required#数据源ID","dictLabel:required#字典标签;length#字典标签#100","dictValue:required#字典键值;length#字典键值#100","dictType:required#字典类型;length#字典类型#100","remark:length#备注#500",})
	@RequiresPermissions(value = {"reportDatasourceDictData_update"},logical = Logical.OR)
	public Response update(@RequestBody ReportDatasourceDictData model) throws Exception
	{
		BaseEntity result = iReportDatasourceDictDataService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="ReportDatasourceDictData",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportDatasourceDictData_delete"},logical = Logical.OR)
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iReportDatasourceDictDataService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param ReportDatasourceDictData 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictData",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"reportDatasourceDictData_delete"},logical = Logical.OR)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iReportDatasourceDictDataService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: getDictDatas
	 * @Description: 获取数据源数据字典值
	 * @author caiyang
	 * @param model
	 * @return
	 * @throws Exception 
	 * @return Response
	 * @date 2022-11-21 04:02:52 
	 */  
	@RequestMapping(value = "/getDictDatas",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictData",remark="获取数据源数据字典值",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"datasourceId:required#数据源ID","dictType:required#字典类型"})
	public Response getDictDatas(@RequestBody ReportDatasourceDictData model) throws Exception
	{
		List<ReportDatasourceDictData> result = this.iReportDatasourceDictDataService.getDictDatas(model);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getDictDatas(分享链接用)
	 * @Description: 获取数据源数据字典值
	 * @author caiyang
	 * @param model
	 * @return
	 * @throws Exception 
	 * @return Response
	 * @date 2022-11-21 04:02:52 
	 */  
	@RequestMapping(value = "/getShareDictDatas",method = RequestMethod.POST)
	@MethodLog(module="ReportDatasourceDictData",remark="分享获取数据源数据字典值",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"datasourceId:required#数据源ID","dictType:required#字典类型"})
	public Response getShareDictDatas(@RequestBody ReportDatasourceDictData model) throws Exception
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
		List<ReportDatasourceDictData> result = this.iReportDatasourceDictDataService.getDictDatas(model);
		return Response.success(result);
	}
}
