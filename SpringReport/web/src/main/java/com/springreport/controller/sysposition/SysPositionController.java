/** 
 * 模块：报表系统-SysPosition
 */
package com.springreport.controller.sysposition;

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
import com.springreport.entity.sysposition.SysPosition;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.sysposition.ISysPositionService;

 /**  
* @Description: SysPositioncontroller类
* @author 
* @date 2022-06-24 10:54:59
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/sysPosition")
public class SysPositionController extends BaseController {

	/**
	 * iSysPositionService服务注入
	 */
	@Autowired
	private ISysPositionService iSysPositionService;

	/** 
	* @Description: 分页查询表格
	* @param SysPosition
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="SysPosition",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public void getTableList(@RequestBody SysPosition model)
	{
		
		iSysPositionService.tablePagingQuery(model);
	}

	
}
