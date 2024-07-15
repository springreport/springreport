package com.springreport.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
* @ClassName: LoginUser
* @Description: 获取登陆信息
* @author caiyang
* @date 2020年5月29日13:19:24
*
*/
@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface LoginUser {

}
