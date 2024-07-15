package com.springreport.annotation;


import org.springframework.web.bind.annotation.Mapping;
 
import java.lang.annotation.*;
 
 
/**
 * @author caiyang
 * @desc 请求数据解密
 * @date 2020年11月5日16:20:14
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface SecurityParameter {
 
    /**
     * 入参是否解密，默认解密
     */
    boolean inDecode() default true;
 
    /**
     * 出参是否加密，默认加密
     */
    boolean outEncode() default true;

}