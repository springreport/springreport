/** 
 * 模块：报表系统-SysPost
 */
package com.springreport.controller.syspost;

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
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.entity.syspost.SysPost;
import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.syspost.ISysPostService;

 /**  
* @Description: SysPostcontroller类
* @author 
* @date 2023-12-22 05:19:10
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/sysPost")
public class SysPostController extends BaseController {

	/**
	 * iSysPostService服务注入
	 */
	@Autowired
	private ISysPostService iSysPostService;

	/** 
	* @Description: 分页查询表格
	* @param SysPost
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="SysPost",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody SysPost model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iSysPostService.tablePagingQuery(model);
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
	@MethodLog(module="SysPost",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iSysPostService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param SysPost
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="SysPost",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"postCode:required#岗位编码;length#岗位编码#50","postName:required#岗位名称;length#岗位名称#50","postSort:required#岗位排序"})
	public Response insert(@RequestBody SysPost model) throws Exception
	{
		BaseEntity result = iSysPostService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param SysPost
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="SysPost",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","postCode:required#岗位编码;length#岗位编码#50","postName:required#岗位名称;length#岗位名称#50","postSort:required#岗位排序"})
	public Response update(@RequestBody SysPost model,@LoginUser UserInfoDto userInfoDto) throws Exception
	{
		BaseEntity result = iSysPostService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="SysPost",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iSysPostService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param SysPost 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="SysPost",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iSysPostService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: getSysPosts
	 * @Description: 获取所有的岗位
	 * @author caiyang
	 * @param model
	 * @return Response
	 * @date 2023-12-26 03:46:48 
	 */ 
	@RequestMapping(value = "/getSysPosts",method = RequestMethod.POST)
	@MethodLog(module="SysPost",remark="获取所有的岗位",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getSysPosts(@RequestBody SysPost model)
	{
		List<SysPost> result = iSysPostService.getSysPosts(model);
		return Response.success(result);
	}
}
