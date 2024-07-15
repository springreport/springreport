package com.springreport.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.springreport.util.StringUtil;



@ControllerAdvice
public class BaseResponseBodyAdvice implements ResponseBodyAdvice<Object>{

	@Autowired
	protected HttpServletRequest httpServletRequest;
	
	@Autowired
	protected HttpServletResponse httpServletResponse;
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
//		System.out.println("响应拦截成功");
		if(body instanceof Response)
		{
			Response responseBody = (Response) body;
			String token = httpServletResponse.getHeader("Authorization");
			if(StringUtil.isNotEmpty(token))
			{
				responseBody.setNewToken(token);
			}
			return responseBody;
		}else {
			return body;
		}
	}

}
