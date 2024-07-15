/** 
 * 模块：报表系统-SysDept
 */
package com.springreport.controller.sysdept;

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
import com.springreport.entity.sysdept.SysDept;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.sysdept.ISysDeptService;

 /**  
* @Description: SysDeptcontroller类
* @author 
* @date 2023-12-22 05:18:48
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/sysDept")
public class SysDeptController extends BaseController {

	/**
	 * iSysDeptService服务注入
	 */
	@Autowired
	private ISysDeptService iSysDeptService;

	/** 
	* @Description: 分页查询表格
	* @param SysDept
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="SysDept",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody SysDept model)
	{
		List<SysDept> result = iSysDeptService.tablePagingQuery(model);
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
	@MethodLog(module="SysDept",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iSysDeptService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param SysDept
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="SysDept",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"deptName:required#部门名称;length#部门名称#50","leader:required#负责人;length#负责人#50","phone:required#联系电话;length#联系电话#20","email:length#邮箱#50;email",})
	public Response insert(@RequestBody SysDept model) throws Exception
	{
		BaseEntity result = iSysDeptService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param SysDept
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="SysDept",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","deptName:required#部门名称;length#部门名称#50","leader:required#负责人;length#负责人#50","phone:required#联系电话;length#联系电话#20","email:length#邮箱#50;email",})
	public Response update(@RequestBody SysDept model) throws Exception
	{
		BaseEntity result = iSysDeptService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="SysDept",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iSysDeptService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param SysDept 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="SysDept",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iSysDeptService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
}
