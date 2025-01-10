package com.springreport.base;

import com.springreport.annotation.LoginUser;
import com.springreport.constants.StatusCode;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;
import com.springreport.util.JWTUtil;
import com.springreport.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/**
 功能描述: 登录用户的MethodArgumentResolver
 */
@Slf4j
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver{

	@Value("${authentic.enabale}")
    private boolean authenticEnabale;
	
	@Value("${thirdParty.type}")
	private String thirdPartyType;
	/**
	 *若不想自定义注解，可以直接在实现HandlerMethodArgumentResolver的supportsParameter直接返回true 这样每一个请求过来的都会分解该参数
	 * 功能描述: 用于判定是否需要处理该参数分解，返回true为需要，并会去调用下面的方法resolveArgument。
	 * supportsParameter方法，
	 * 判断什么时候要执行下面的resolveArgument方法。这里我们判断当一个方法的参数含有@LoginUser
	 * 并且方法的参数是我们的用户类时返回true。
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if(parameter.hasParameterAnnotation(LoginUser.class)){
            return true;
        }
        return false;

	}



	/**
	 *
	 * 功能描述: 真正用于处理参数分解的方法，返回的Object就是controller方法上的形参对象。（获得登录用户信息）
	 * resolveArgument方法，在这里我们直接把放在session中的用户信息放回去即可。
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		if(authenticEnabale)
		{
			/**
			 * 如果是shiro的话直接通过shiro获取用户信息即可
			 */
			String token = webRequest.getHeader("Authorization");
			String thirdPartyType = webRequest.getHeader("thirdPartyType");
			UserInfoDto userInfoDto = null;
			if(StringUtil.isNotEmpty(thirdPartyType) && thirdPartyType.equals(this.thirdPartyType)) {
				log.info("第三方调用登录，调用方："+thirdPartyType);
				userInfoDto = new UserInfoDto();
				userInfoDto.setIsAdmin(YesNoEnum.YES.getCode());
				userInfoDto.setUserName(thirdPartyType);
			}else {
				log.info("token信息："+token);
				userInfoDto = JWTUtil.getUserInfo(token);
				if (userInfoDto == null) {
					throw new BizException(StatusCode.TOKEN_FAILURE, "登陆信息失效，请重新登录");
				}
			}
			return userInfoDto;
		}else {
			UserInfoDto userInfoDto = new UserInfoDto();
			userInfoDto.setIsAdmin(YesNoEnum.YES.getCode());
			return userInfoDto;
		}
		
	}

}
