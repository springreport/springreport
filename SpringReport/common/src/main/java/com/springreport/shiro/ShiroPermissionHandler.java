package com.springreport.shiro;

import java.lang.annotation.Annotation;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.aop.PermissionAnnotationHandler;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.springreport.util.MessageUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShiroPermissionHandler extends PermissionAnnotationHandler{
	
	@Override
	public void assertAuthorized(Annotation a) throws AuthorizationException {
		String authenticEnabale = MessageUtil.getValue("authentic.enabale");
		if("true".equals(authenticEnabale))
		{
			super.assertAuthorized(a);
		}
		
	}
}
