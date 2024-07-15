package com.springreport.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.springreport.base.UserInfoDto;
import com.springreport.enums.RequestHeaderEnums;
import com.springreport.enums.YesNoEnum;
import com.springreport.util.CusAccessObjectUtil;
import com.springreport.util.JWTUtil;
import com.springreport.util.StringUtil;

/**  
 * @ClassName: LogInterceptor
 * @Description: 日志拦截器，将当前操作人输出到日志中
 * @author caiyang
 * @date 2020-07-08 02:58:50 
*/  
public class LogInterceptor extends HandlerInterceptorAdapter{

	/**
     * 会话ID
     */
    private final static String SESSION_KEY = "sessionId";
    
    @Value("${merchantmode}")
    private Integer merchantmode;

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

        // 删除SessionId
        MDC.remove(SESSION_KEY);
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                           Object arg2, ModelAndView arg3) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

    	//获取当前登录人的token信息
    	String authorization = request.getHeader(RequestHeaderEnums.Authorization.getCode());
    	UserInfoDto userInfoDto = JWTUtil.getUserInfo(authorization);
    	if (userInfoDto != null) {
    		MDC.put(SESSION_KEY, userInfoDto.getUserName());
		}else {
			//获取ip信息
			String ip = CusAccessObjectUtil.getIpAddress(request);
			MDC.put(SESSION_KEY, ip);
		}
        return true;
    }
}
