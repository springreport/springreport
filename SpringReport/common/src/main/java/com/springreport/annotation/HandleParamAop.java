package com.springreport.annotation;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.springreport.base.UserInfoDto;

/**  
 * @ClassName: HandleParamAop
 * @Description: 参数处理用aop
 * @author caiyang
 * @date 2020-08-26 05:17:08 
*/  
@Aspect
@Component
public class HandleParamAop {

	
		 @Autowired
		 private HttpServletRequest httpServletRequest;
		
		 @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
		    public void handleParamAop() {
		 }
	 	@Before("handleParamAop()")
	    public void paramHandler(JoinPoint joinPoint) throws Throwable {
	 		try {
		        Object[] args = joinPoint.getArgs();
		        Class clazz;
		        Method[] methods;
		        String requestType = httpServletRequest.getMethod();//获取请求类型
		        if ("POST".equals(requestType)) {
			        for (Object object : args) {
			            if (null == object) {
			                continue;
			            }
			            else if(object instanceof UserInfoDto)
			            {
			            	continue;
			            }else if(object instanceof MultipartFile)
			            {
			            	continue;
			            }
			            else if(object.getClass().getName().contains("ApplicationHttpRequest"))
			            {
			            	continue;
			            }
			            else
			            {
			            	JSONObject jsonObject = (JSONObject) JSONObject.toJSON(object);
				            JSONObject param = new JSONObject();
				            Set<String> set = jsonObject.keySet(); 
				            Iterator<String> it = set.iterator();
				            while (it.hasNext()) {
		            			String key = it.next();
		            			 Object value = jsonObject.get(key);
		            			 if(value != null)
		            			 {
		            				 if(value instanceof String)
		                			 {
		            					 param.put(key, value.toString().trim());
		                			 }else {
		                				 param.put(key, value);
		                			 }
		            			 }
		            			 
		            		}
				            Object result = object.getClass().newInstance();
				            result = JSONObject.toJavaObject(param, object.getClass());
				            BeanUtils.copyProperties(result, object);
			            }
			            
			        }
			        
		        }
	 		} catch (Exception e) {
//				e.printStackTrace();
			}
	 	}
}
