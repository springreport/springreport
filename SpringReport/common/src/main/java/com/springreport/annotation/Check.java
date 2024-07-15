package com.springreport.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
* @ClassName: Check
* @Description: controller参数校验 注解
* @author caiyang
* @date 2020年5月29日13:19:19
*
*/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RUNTIME)
public @interface Check {

    /**
    * @Title: 字段校验规则，格式：字段名+校验规则+冒号+错误信息id，例如：id<10:error.system
    * @Description: 
    * @param @return 参数
    * @return String[]
    * @throws
    */
    String[] value();
}
