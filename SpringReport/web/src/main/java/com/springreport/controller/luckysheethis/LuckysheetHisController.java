/** 
 * 模块：报表系统-LuckysheetHis
 */
package com.springreport.controller.luckysheethis;

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
import com.springreport.entity.luckysheethis.LuckysheetHis;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.luckysheethis.ILuckysheetHisService;

 /**  
* @Description: LuckysheetHiscontroller类
* @author 
* @date 2023-08-23 09:46:05
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/luckysheetHis")
public class LuckysheetHisController extends BaseController {

	/**
	 * iLuckysheetHisService服务注入
	 */
	@Autowired
	private ILuckysheetHisService iLuckysheetHisService;

	/** 
	* @Description: 分页查询表格
	* @param LuckysheetHis
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="LuckysheetHis",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody LuckysheetHis model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iLuckysheetHisService.tablePagingQuery(model);
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
	@MethodLog(module="LuckysheetHis",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
	@Check({"id:required#主键ID"})
	public Response getDetail(@RequestParam Long id) throws Exception
	{
		BaseEntity result = iLuckysheetHisService.getDetail(id);
		return Response.success(result);
	}

}
