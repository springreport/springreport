package com.springreport.shiro;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.PermissionAnnotationMethodInterceptor;
import org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor;

public class ShiroMethodInterceptor extends AopAllianceAnnotationsAuthorizingMethodInterceptor{

	 public ShiroMethodInterceptor() {
	     super();
	 }
	 
	 public ShiroMethodInterceptor skipPremissionHandler() {
	     
	        List<AuthorizingAnnotationMethodInterceptor> interceptors = this.getMethodInterceptors().stream()
	                .filter(authorizingAnnotationMethodInterceptor ->
	                        !(authorizingAnnotationMethodInterceptor instanceof PermissionAnnotationMethodInterceptor))
	                .collect(Collectors.toList());
	        PermissionAnnotationMethodInterceptor interceptor = new PermissionAnnotationMethodInterceptor();
	        //设置成自己的注解处理器!
	        interceptor.setHandler(new ShiroPermissionHandler());
	        interceptors.add(interceptor);
	        setMethodInterceptors(interceptors);
	        return this;
	    }
}
