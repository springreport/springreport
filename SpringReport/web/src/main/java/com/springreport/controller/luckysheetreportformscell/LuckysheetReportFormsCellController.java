/** 
 * 模块：报表系统-LuckysheetReportFormsCell
 */
package com.springreport.controller.luckysheetreportformscell;

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
import com.springreport.entity.luckysheetreportformscell.LuckysheetReportFormsCell;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.luckysheetreportformscell.ILuckysheetReportFormsCellService;

 /**  
* @Description: LuckysheetReportFormsCellcontroller类
* @author 
* @date 2022-11-16 01:47:45
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/luckysheetReportFormsCell")
public class LuckysheetReportFormsCellController extends BaseController {

	/**
	 * iLuckysheetReportFormsCellService服务注入
	 */
	@Autowired
	private ILuckysheetReportFormsCellService iLuckysheetReportFormsCellService;

	/** 
	* @Description: 分页查询表格
	* @param LuckysheetReportFormsCell
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="LuckysheetReportFormsCell",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody LuckysheetReportFormsCell model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iLuckysheetReportFormsCellService.tablePagingQuery(model);
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
	@MethodLog(module="LuckysheetReportFormsCell",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iLuckysheetReportFormsCellService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param LuckysheetReportFormsCell
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="LuckysheetReportFormsCell",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"datasetName:required#数据集名称;length#数据集名称#40","cellValue:required#单元格值;length#单元格值#65535","cellData:required#单元格配置;length#单元格配置#65535","cellAttrs:required#单元格属性;length#单元格属性#65535","warningColor:required#预警颜色;length#预警颜色#20","threshold:required#预警阈值;length#预警阈值#10","warningContent:required#预警内容;length#预警内容#500",})
	public Response insert(@RequestBody LuckysheetReportFormsCell model) throws Exception
	{
		BaseEntity result = iLuckysheetReportFormsCellService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param LuckysheetReportFormsCell
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="LuckysheetReportFormsCell",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","datasetName:required#数据集名称;length#数据集名称#40","cellValue:required#单元格值;length#单元格值#65535","cellData:required#单元格配置;length#单元格配置#65535","cellAttrs:required#单元格属性;length#单元格属性#65535","warningColor:required#预警颜色;length#预警颜色#20","threshold:required#预警阈值;length#预警阈值#10","warningContent:required#预警内容;length#预警内容#500",})
	public Response update(@RequestBody LuckysheetReportFormsCell model) throws Exception
	{
		BaseEntity result = iLuckysheetReportFormsCellService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="LuckysheetReportFormsCell",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iLuckysheetReportFormsCellService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param LuckysheetReportFormsCell 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="LuckysheetReportFormsCell",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iLuckysheetReportFormsCellService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
}
