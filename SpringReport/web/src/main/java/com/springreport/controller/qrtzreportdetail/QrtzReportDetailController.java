/** 
 * 模块：报表系统-QrtzReportDetail
 */
package com.springreport.controller.qrtzreportdetail;

import java.text.ParseException;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import com.springreport.base.BaseController;
import com.springreport.base.BaseEntity;
import com.springreport.base.Response;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.dto.qrtzreportdetail.ReqIndexQrtzDto;
import com.springreport.entity.qrtzreportdetail.QrtzReportDetail;
import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.qrtzreportdetail.IQrtzReportDetailService;

 /**  
* @Description: QrtzReportDetailcontroller类
* @author 
* @date 2023-07-28 09:43:20
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/qrtzReportDetail")
public class QrtzReportDetailController extends BaseController {

	/**
	 * iQrtzReportDetailService服务注入
	 */
	@Autowired
	private IQrtzReportDetailService iQrtzReportDetailService;

	/** 
	* @Description: 分页查询表格
	* @param QrtzReportDetail
	* @param @return  
	* @return Response
	 * @throws ParseException 
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="QrtzReportDetail",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"reportTask_search"})
	public Response getTableList(@RequestBody QrtzReportDetail model) throws ParseException
	{
		
		BaseEntity result = new BaseEntity();
		result = iQrtzReportDetailService.tablePagingQuery(model);
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
	@MethodLog(module="QrtzReportDetail",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportTask_detail","reportTask_edit"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iQrtzReportDetailService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param QrtzReportDetail
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="QrtzReportDetail",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"jobName:required#任务名称;length#任务名称#50","jobCron:length#任务执行时间#50","email:required#邮箱",})
	@RequiresPermissions(value = {"reportTask_insert"})
	public Response insert(@RequestBody QrtzReportDetail model) throws Exception
	{
		BaseEntity result = iQrtzReportDetailService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param QrtzReportDetail
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="QrtzReportDetail",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","jobName:required#任务名称;length#任务名称#50","jobCron:length#任务执行时间#50","email:required#邮箱",})
	@RequiresPermissions(value = {"reportTask_edit"})
	public Response update(@RequestBody QrtzReportDetail model) throws Exception
	{
		BaseEntity result = iQrtzReportDetailService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="QrtzReportDetail",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportTask_delete"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iQrtzReportDetailService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param QrtzReportDetail 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="QrtzReportDetail",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"reportTask_batchdelete"})
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iQrtzReportDetailService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: runTask
	 * @Description: 定时任务立即执行一次
	 * @author caiyang
	 * @param id
	 * @return Response
	 * @throws Exception 
	 * @date 2023-07-30 09:54:42 
	 */ 
	@RequestMapping(value = "/runTask",method = RequestMethod.GET)
	@MethodLog(module="QrtzReportDetail",remark="立即执行",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportTask_runTask"})
	public Response runTask(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iQrtzReportDetailService.runTask(id);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: pauseTask
	 * @Description: 暂停任务
	 * @author caiyang
	 * @param id
	 * @return
	 * @throws Exception Response
	 * @date 2023-07-30 11:40:13 
	 */ 
	@RequestMapping(value = "/pauseTask",method = RequestMethod.GET)
	@MethodLog(module="QrtzReportDetail",remark="暂停任务",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportTask_pause"})
	public Response pauseTask(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iQrtzReportDetailService.pauseTask(id);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: resumeTask
	 * @Description: 恢复任务
	 * @author caiyang
	 * @param id
	 * @return
	 * @throws Exception Response
	 * @date 2023-07-30 11:43:51 
	 */ 
	@RequestMapping(value = "/resumeTask",method = RequestMethod.GET)
	@MethodLog(module="QrtzReportDetail",remark="恢复任务",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"reportTask_resume"})
	public Response resumeTask(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iQrtzReportDetailService.resumeTask(id);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: getIndexTaskList
	 * @Description: 获取首页任务列表
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return Response
	 * @date 2024-12-10 06:52:11 
	 */ 
	@RequestMapping(value = "/getIndexTaskList",method = RequestMethod.POST)
	@MethodLog(module="QrtzReportDetail",remark="获取首页任务列表",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getIndexTaskList(@RequestBody ReqIndexQrtzDto model,@LoginUser UserInfoDto userInfoDto)
	{
		BaseEntity result = iQrtzReportDetailService.getIndexTaskList(model,userInfoDto);
		return Response.success(result);
	}
}
