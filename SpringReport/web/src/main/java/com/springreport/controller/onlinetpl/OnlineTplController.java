/** 
 * 模块：报表系统-OnlineTpl
 */
package com.springreport.controller.onlinetpl;

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
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.dto.onlinetpl.MesRangeAuthDto;
import com.springreport.dto.onlinetpl.OnlineTplTreeDto;
import com.springreport.dto.onlinetpl.ResOnlineTplInfo;
import com.springreport.dto.onlinetpl.ResSaveOnlineDocDto;
import com.springreport.dto.reporttpl.ResSheetsSettingsDto;
import com.springreport.entity.onlinetpl.OnlineTpl;
import com.springreport.entity.reporttype.ReportType;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.onlinetpl.IOnlineTplService;

 /**  
* @Description: OnlineTplcontroller类
* @author 
* @date 2023-02-06 08:03:24
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/onlineTpl")
public class OnlineTplController extends BaseController {

	/**
	 * iOnlineTplService服务注入
	 */
	@Autowired
	private IOnlineTplService iOnlineTplService;
	
//	@Autowired
//	private SendService sendService;
	
	@Autowired
	private RedisUtil redisUtil;

	/** 
	* @Description: 分页查询表格
	* @param OnlineTpl
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="OnlineTpl",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"onlineTpl_search"})
	public Response getTableList(@RequestBody ReportType model)
	{
		List<OnlineTplTreeDto> result = iOnlineTplService.tablePagingQuery(model);
		return Response.success(result);
	}
	
	@RequestMapping(value = "/getChildren",method = RequestMethod.POST)
	@MethodLog(module="OnlineTpl",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"onlineTpl_search"})
	public Response getChildren(@RequestBody OnlineTpl model)
	{
		List<OnlineTplTreeDto> result = iOnlineTplService.getChildren(model);
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
	@MethodLog(module="OnlineTpl",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"onlineTpl_getDetai","onlineTpl_update"},logical = Logical.OR)
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iOnlineTplService.getDetail(id);
		return Response.success(result);
	}

	/**
	* @throws Exception  
	* @Description: 新增
	* @param OnlineTpl
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/insert",method = RequestMethod.POST)
	@MethodLog(module="OnlineTpl",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
	@Check({"tplName:required#模板名称;length#模板名称#40"})
	@RequiresPermissions(value = {"onlineTpl_insert"})
	public Response insert(@RequestBody OnlineTpl model) throws Exception
	{
		BaseEntity result = iOnlineTplService.insert(model);
		return Response.success(result.getStatusMsg());
	}

	/**
	* @throws Exception  
	* @Description: 更新
	* @param OnlineTpl
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	@MethodLog(module="OnlineTpl",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"id:required#主键ID","tplName:required#模板名称;length#模板名称#40"})
	@RequiresPermissions(value = {"onlineTpl_update"})
	public Response update(@RequestBody OnlineTpl model) throws Exception
	{
		BaseEntity result = iOnlineTplService.update(model);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 单条删除
	* @param id
	* @return  BaseEntity
	* @throws 
	*/ 
	@RequestMapping(value = "/delete",method = RequestMethod.GET)
	@MethodLog(module="OnlineTpl",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"onlineTpl_delete"})
	public Response delete(@RequestParam Long id)
	{
		BaseEntity result = iOnlineTplService.delete(id);
		return Response.success(result.getStatusMsg());
	}

	/** 
	* @Description: 批量删除
	* @param OnlineTpl 
	* @return Response 
	* @throws 
	*/ 
	@RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
	@MethodLog(module="OnlineTpl",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
	@RequiresPermissions(value = {"onlineTpl_delete"})
	public Response deletebatch(@RequestBody List<Long> ids)
	{
		BaseEntity result = iOnlineTplService.deleteBatch(ids);
		return Response.success(result.getStatusMsg());
	}
	
	/**  
	 * @MethodName: getOnlineTplSettings
	 * @Description: 获取在线文档内容
	 * @author caiyang
	 * @param onlineTpl
	 * @param userInfoDto
	 * @return
	 * @throws Exception 
	 * @return Response
	 * @date 2023-02-07 09:26:54 
	 */  
	@RequestMapping(value = "/getOnlineTplSettings",method = RequestMethod.POST)
	@MethodLog(module="OnlineTpl",remark="获取在线文档内容",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	@RequiresPermissions(value = {"onlineTpl_editDoc"})
	public Response getOnlineTplSettings(@RequestBody OnlineTpl onlineTpl,@LoginUser UserInfoDto userInfoDto) throws Exception {
		ResSheetsSettingsDto result = this.iOnlineTplService.getOnlineTplSettings(onlineTpl,userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: saveCellData
	 * @Description: 保存单元格数据
	 * @author caiyang
	 * @param model
	 * @return 
	 * @return Response
	 * @throws JsonProcessingException 
	 * @date 2023-02-07 02:47:59 
	 */  
	@RequestMapping(value = "/saveOnlineDoc",method = RequestMethod.POST)
	@MethodLog(module="OnlineTpl",remark="保存在线文档",operateType=Constants.OPERATE_TYPE_SEARCH)
	@RequiresPermissions(value = {"onlineTpl_editDoc"})
	public Response saveCellData(@RequestBody ResSaveOnlineDocDto model) throws JsonProcessingException {
		long version = redisUtil.increment(RedisPrefixEnum.UPDATEONLINEVERSION.getCode()+model.getTplId());
		model.setVersion(version);
//		this.sendService.sendMessage(RedisChannelEnum.UPDATEONLINEREPORT.getCode(),JSONObject.toJSONString(model));
		return Response.success(null);
	}
	
	/**  
	 * @MethodName: getOnlineTplInfo
	 * @Description: 获取在线文档信息
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return
	 * @throws JsonProcessingException Response
	 * @date 2024-03-09 12:31:28 
	 */ 
	@RequestMapping(value = "/getOnlineTplInfo",method = RequestMethod.POST)
	@MethodLog(module="OnlineTpl",remark="获取在线文档信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"listId:required#文档标识"})
	public Response getOnlineTplInfo(@RequestBody OnlineTpl model,@LoginUser UserInfoDto userInfoDto) throws JsonProcessingException {
		ResOnlineTplInfo result = this.iOnlineTplService.getOnlineTplInfo(model, userInfoDto);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: rangeAuth
	 * @Description: 协同文档选区授权
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return Response
	 * @date 2024-03-09 01:06:54 
	 */ 
	@RequestMapping(value = "/rangeAuth",method = RequestMethod.POST)
	@MethodLog(module="OnlineTpl",remark="选区授权",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"listId:required#文档标识"})
	public Response rangeAuth(@RequestBody MesRangeAuthDto model,@LoginUser UserInfoDto userInfoDto) {
		this.iOnlineTplService.rangeAuth(model, userInfoDto);
		return Response.success(null);
	}
	
	/**  
	 * @MethodName: deletRangeAuth
	 * @Description: 删除选区授权
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return Response
	 * @date 2024-03-09 05:17:09 
	 */ 
	@RequestMapping(value = "/deletRangeAuth",method = RequestMethod.POST)
	@MethodLog(module="OnlineTpl",remark="删除选区授权",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"listId:required#文档标识"})
	public Response deletRangeAuth(@RequestBody MesRangeAuthDto model,@LoginUser UserInfoDto userInfoDto) {
		this.iOnlineTplService.deletRangeAuth(model, userInfoDto);
		return Response.success(null);
	}
	
	/**  
	 * @MethodName: getTplAuth
	 * @Description: 获取协同文档授权范围
	 * @author caiyang
	 * @param userInfoDto
	 * @return Response
	 * @date 2024-03-07 06:28:47 
	 */ 
	@RequestMapping(value = "/getCoeditAuth",method = RequestMethod.POST)
	@MethodLog(module="coedit",remark="获取协同文档授权范围",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getCoeditAuth(@RequestBody OnlineTpl model,@LoginUser UserInfoDto userInfoDto)
	{
		JSONObject result = this.iOnlineTplService.getCoeditAuth(model, userInfoDto);
		return Response.success(result);
	}
}
