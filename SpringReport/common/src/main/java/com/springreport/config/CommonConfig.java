package com.springreport.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
* <p>Title: CommonConfig</p>
* <p>Description: 共通的配置文件</p>
* @author caiyang
* @date 2019年12月21日
*/
@Configuration
@PropertySource(name="env.properties",value= {"classpath:message.properties"},ignoreResourceNotFound=false,encoding="UTF-8")
@EnableCaching
public class CommonConfig {

}
