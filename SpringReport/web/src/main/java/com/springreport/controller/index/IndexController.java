package com.springreport.controller.index;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.index.IIndexService;
import com.springreport.base.BaseController;
import com.springreport.base.Response;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.dto.index.IndexDto;
import com.springreport.entity.sysmerchant.SysMerchant;
import com.springreport.entity.sysuser.SysUser;

@RestController
@RequestMapping("/springReport/api/index")
public class IndexController extends BaseController{

	@Autowired
	private IIndexService iIndexService;
	
	/**  
	 * @MethodName: getIndexData
	 * @Description: 获取首页数据
	 * @author caiyang
	 * @param userInfoDto
	 * @return
	 * @throws UnsupportedEncodingException Response
	 * @date 2024-12-11 10:06:38 
	 */ 
	@RequestMapping(value = "/getIndexData",method = RequestMethod.POST)
	@MethodLog(module="index",remark="获取首页数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getIndexData(@RequestBody SysMerchant sysMerchant,@LoginUser UserInfoDto userInfoDto) throws UnsupportedEncodingException {
		IndexDto result = this.iIndexService.getIndexData(sysMerchant,userInfoDto);
		return Response.success(result);
	}
}
