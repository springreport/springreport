/** 
 * 模块：报表系统-SysRoleSheet
 */
package com.springreport.controller.sysrolesheet;

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
import com.springreport.dto.report.TreeDto;
import com.springreport.dto.sysrole.MesAuthDto;
import com.springreport.entity.sysrolesheet.SysRoleSheet;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.sysrolesheet.ISysRoleSheetService;

 /**  
* @Description: SysRoleSheetcontroller类
* @author 
* @date 2022-11-08 07:32:54
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/sysRoleSheet")
public class SysRoleSheetController extends BaseController {

	/**
	 * iSysRoleSheetService服务注入
	 */
	@Autowired
	private ISysRoleSheetService iSysRoleSheetService;

	/**  
	 * @Title: getReportTree
	 * @Description: 获取报表树
	 * @param model
	 * @return
	 * @author caiyang
	 * @date 2022年7月6日07:58:12
	 */ 
	@RequestMapping(value = "/getReportTree",method = RequestMethod.POST)
	@MethodLog(module="SysRoleReport",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getReportTree(@RequestBody SysRoleSheet model)
	{
		TreeDto result = this.iSysRoleSheetService.getReportTree(model);
		return Response.success(result);
	}
	
	/**  
	 * @Title: authed
	 * @Description: 报表角色授权
	 * @param mesAuthDto
	 * @return
	 * @author caiyang
	 * @date 2022年7月6日07:58:12
	 */ 
	@RequestMapping(value = "/authed",method = RequestMethod.POST)
	@MethodLog(module="SysRoleReport",remark="报表角色授权",operateType=Constants.OPERATE_TYPE_UPDATE)
	@Check({"roleId:required#角色"})
	public Response authed(@RequestBody MesAuthDto mesAuthDto) 
	{
		BaseEntity result = this.iSysRoleSheetService.authed(mesAuthDto);
		return Response.success(result.getStatusMsg());
	}
}
