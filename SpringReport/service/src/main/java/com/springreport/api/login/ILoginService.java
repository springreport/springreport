package com.springreport.api.login;

import java.io.UnsupportedEncodingException;

import com.springreport.base.UserInfoDto;
import com.springreport.entity.sysuser.SysUser;

/**  
 * @ClassName: ILoginService
 * @Description: 登录用service
 * @author caiyang
 * @date 2021-06-16 04:29:32 
*/  
public interface ILoginService {

	/**  
	 * @Title: doLogin
	 * @Description: 登录
	 * @param sysUser
	 * @return
	 * @author caiyang
	 * @throws UnsupportedEncodingException 
	 * @date 2021-06-16 04:48:32 
	 */ 
	UserInfoDto doLogin(SysUser sysUser) throws UnsupportedEncodingException;
	
	/**  
	 * @MethodName: getMerchantMode
	 * @Description: 获取当前是否是多租户模式
	 * @author caiyang
	 * @return boolean
	 * @date 2023-12-22 06:19:36 
	 */ 
	Integer getMerchantMode();
	
	/**  
	 * @MethodName: getUserInfoByToken
	 * @Description: 根据token获取用户信息
	 * @author caiyang
	 * @param userInfoDto
	 * @return UserInfoDto
	 * @date 2024-09-24 10:10:56 
	 */ 
	UserInfoDto getUserInfoByToken(UserInfoDto userInfoDto);
}
