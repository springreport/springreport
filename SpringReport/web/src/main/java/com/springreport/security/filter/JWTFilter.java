package com.springreport.security.filter;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springreport.constants.StatusCode;
import com.springreport.enums.MsgLevelEnum;
import com.springreport.security.config.JWTToken;
import com.springreport.util.MessageUtil;
import com.springreport.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: JWTFilter
 * @Description: 运维后台使用filter
 * @Author 蔡阳
 * @DateTime 2019年10月24日13:46:09
 */
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter{

	/**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        req.getRequestURI();
        String authorization = req.getHeader("Authorization");
        if(StringUtil.isNullOrEmpty(authorization))
        {
        	authorization = req.getParameter("Authorization");
        }
        log.info("判断用户是否想要登录：{}，请求接口：{}",authorization,req.getServletPath());
        if ("null".equals(authorization) || authorization == null) {
			return false;
		}else {
			return true;
		}
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        if(StringUtil.isNullOrEmpty(authorization))
        {
        	authorization = httpServletRequest.getParameter("Authorization");
        }
        log.info("判断用户是否想要登录x：{}，请求接口：{}",authorization,httpServletRequest.getServletPath());
        JWTToken token = new JWTToken(authorization,"1");
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        try {
        	getSubject(request, response).login(token);
		} catch (Exception e) {
			return false;
		}
        
        return true;
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
              return executeLogin(request, response);
        }
        return false;
    }
    
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    	
    	HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(200);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        String statusCode = httpServletResponse.getHeader("statusCode");
        PrintWriter out = httpServletResponse.getWriter();
        JSONObject json = new JSONObject();
        if (StatusCode.TOKEN_LOCK.equals(statusCode)) {
        	json.put("code", StatusCode.TOKEN_LOCK);
            json.put("message", MessageUtil.getValue("error.token.stop"));
            json.put("msgLevel", MsgLevelEnum.ERROR.getCode());
            JSONObject result = new JSONObject();
            json.put("responseData",result);
		}else {
			json.put("code", StatusCode.TOKEN_FAILURE);
	        json.put("message", MessageUtil.getValue("error.token"));
	        json.put("msgLevel", MsgLevelEnum.ERROR.getCode());
	        JSONObject result = new JSONObject();
	        json.put("responseData",result);
		}
        
        out.println(json);
        out.flush();
        out.close();
        return false;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return true;
        }
        return super.preHandle(request, response);
    }

    
}
