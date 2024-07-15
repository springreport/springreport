package com.springreport.base;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.springreport.enums.RequestHeaderEnums;
import com.springreport.enums.YesNoEnum;
import com.springreport.util.JWTUtil;
import com.springreport.util.StringUtil;

@ControllerAdvice(annotations = RequestMapping.class)
public class MerchantRequestBodyAdvice implements RequestBodyAdvice{

	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Value("${merchantmode}")
    private Integer merchantmode;
	
	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		String requestType = httpServletRequest.getMethod();//获取请求类型
		HttpInputMessage result = inputMessage;
		if ("POST".equals(requestType)) {
			if(merchantmode.intValue() == YesNoEnum.YES.getCode().intValue())
			{//多租户模式
				String authorization = httpServletRequest.getHeader(RequestHeaderEnums.Authorization.getCode());
				if (StringUtil.isNotEmpty(authorization)) {
					UserInfoDto userInfoDto = JWTUtil.getUserInfo(authorization);
					if (userInfoDto != null && userInfoDto.getIsSystemMerchant() != null) {
						if(YesNoEnum.YES.getCode().intValue() != userInfoDto.getIsSystemMerchant().intValue()) {
							//非系统默认租户，则使用token中的租户号
							JSONObject requestBody = JSON.parseObject(IOUtils.toString(inputMessage.getBody()));
							requestBody.put("merchantNo", userInfoDto.getMerchantNo());
							result = new HttpInputMessage() {
					            @Override
					            public HttpHeaders getHeaders() {
					                return inputMessage.getHeaders();
					            }
					            @Override
					            public InputStream getBody() throws IOException {
					                return new ByteArrayInputStream(requestBody.toJSONString().getBytes(StandardCharsets.UTF_8));
					            }
					        };
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		// TODO Auto-generated method stub
		return body;
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		// TODO Auto-generated method stub
		return body;
	}

}
