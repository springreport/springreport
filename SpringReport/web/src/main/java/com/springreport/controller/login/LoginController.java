package com.springreport.controller.login;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.login.ILoginService;
import com.springreport.base.BaseController;
import com.springreport.base.Response;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.entity.sysuser.SysUser;

/**  
 * @ClassName: LoginController
 * @Description: 登录用controller
 * @author caiyang
 * @date 2021-06-16 04:28:30 
*/  
@RestController
@RequestMapping("/springReport/api/login")
public class LoginController extends BaseController{

	@Autowired
	private ILoginService iLoginService;
	
	/**  
	 * @Title: doLogin
	 * @Description: 用户登录
	 * @param userAccount
	 * @return
	 * @author caiyang
	 * @throws UnsupportedEncodingException 
	 * @date 2020-06-12 01:20:05 
	 */ 
	@RequestMapping(value = "/doLogin",method = RequestMethod.POST)
	@Check({"userName:required#登录名","password:required#密码"})
	@MethodLog(module="Login",remark="用户登录",operateType=Constants.OPERATE_TYPE_UPDATE)
	public Response doLogin(@RequestBody SysUser sysUser) throws UnsupportedEncodingException {
		UserInfoDto result = iLoginService.doLogin(sysUser);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getMerchantMode
	 * @Description: 获取当前是否是多租户模式
	 * @author caiyang
	 * @return Response
	 * @date 2023-12-22 06:18:54 
	 */ 
	@RequestMapping(value = "/getMerchantMode",method = RequestMethod.POST)
	public Response getMerchantMode() {
		Integer result = this.iLoginService.getMerchantMode();
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: getUserInfoByToken
	 * @Description: 根据token获取用户信息
	 * @author caiyang
	 * @param userInfoDto
	 * @return Response
	 * @date 2024-09-24 10:10:03 
	 */ 
	@RequestMapping(value = "/getUserInfoByToken",method = RequestMethod.POST)
	public Response getUserInfoByToken(@LoginUser UserInfoDto userInfoDto) {
		UserInfoDto result = iLoginService.getUserInfoByToken(userInfoDto);
		return Response.success(result);
	}
}
