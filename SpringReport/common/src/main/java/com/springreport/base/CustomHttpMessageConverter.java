package com.springreport.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.springreport.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomHttpMessageConverter extends FastJsonHttpMessageConverter{

	private ObjectMapper mapper = new ObjectMapper();
	@Override
	 public void write(Object obj, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		if (type.toString().contains("springfox"))
		{
			HttpHeaders headers = outputMessage.getHeaders();
			ByteArrayOutputStream outnew = new ByteArrayOutputStream();
			mapper.writeValue(outnew, obj);
			outnew.flush();
			headers.setContentLength(outnew.size());
			OutputStream out = outputMessage.getBody();
			outnew.writeTo(out);
			outnew.close();
		}else if (obj.toString().contains("org.springframework.boot.actuate")) {
			super.writeInternal(obj, outputMessage);
		}
//		else if(obj instanceof NoConvertResponse)
//		{
//			HttpHeaders headers = outputMessage.getHeaders();
//			ByteArrayOutputStream outnew = new ByteArrayOutputStream();
//			mapper.writeValue(outnew, obj);
//			outnew.flush();
//			headers.setContentLength(outnew.size());
//			OutputStream out = outputMessage.getBody();
//			outnew.writeTo(out);
//			outnew.close();
//		}
		else {
			if(obj instanceof Response)
			{
				Response response = (Response) obj;
				ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
				if(null != requestAttributes) {
		            HttpServletResponse httpServletResponse = requestAttributes.getResponse();
		            String token = httpServletResponse.getHeader("Authorization");
		            if (StringUtil.isNotEmpty(token)) {
		            	response.setNewToken(token);
					}
		        }
				super.writeInternal(response, outputMessage);
			}else {
				super.writeInternal(obj, outputMessage);
			}
		}
		
	}
}
