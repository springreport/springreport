/** 
 * 模块：报表系统-LuckysheetReportFormsHis
 */
package com.springreport.controller.luckysheetreportformshis;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.springreport.base.BaseController;
import com.springreport.base.BaseEntity;
import com.springreport.base.Response;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.entity.luckysheetreportformshis.LuckysheetReportFormsHis;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.exception.BizException;
import com.springreport.util.MessageUtil;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;
import com.springreport.annotation.MethodLog;
import com.springreport.api.luckysheetreportformshis.ILuckysheetReportFormsHisService;

 /**  
* @Description: LuckysheetReportFormsHiscontroller类
* @author 
* @date 2023-01-31 08:08:17
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/luckysheetReportFormsHis")
public class LuckysheetReportFormsHisController extends BaseController {

	/**
	 * iLuckysheetReportFormsHisService服务注入
	 */
	@Autowired
	private ILuckysheetReportFormsHisService iLuckysheetReportFormsHisService;
	
	@Autowired
	private RedisUtil redisUtil;

	/** 
	* @Description: 分页查询表格
	* @param LuckysheetReportFormsHis
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getTableList",method = RequestMethod.POST)
	@MethodLog(module="LuckysheetReportFormsHis",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getTableList(@RequestBody LuckysheetReportFormsHis model)
	{
		
		BaseEntity result = new BaseEntity();
		result = iLuckysheetReportFormsHisService.tablePagingQuery(model);
		return Response.success(result);
	}
	
	/** 
	* @Description: 分页查询表格
	* @param LuckysheetReportFormsHis
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getShareTableList",method = RequestMethod.POST)
	@MethodLog(module="LuckysheetReportFormsHis",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getShareTableList(@RequestBody LuckysheetReportFormsHis model)
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
		BaseEntity result = new BaseEntity();
		result = iLuckysheetReportFormsHisService.tablePagingQuery(model);
		return Response.success(result);
	}

}
