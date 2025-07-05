package com.springreport;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.springreport.base.CurrentUserMethodArgumentResolver;
import com.springreport.interceptor.LogInterceptor;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**  
 * @ClassName: Application
 * @Description: 启动类
 * @author caiyang
 * @date 2020-06-05 04:20:46 
*/  
@SpringBootApplication
@MapperScan("com.springreport.mapper")
@EnableAsync
public class Application extends WebMvcConfigurationSupport{

	/**
     * 本地保存路径
     */
	@Value("${file.path}")
    private String dirPath;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.err.println("success to start up SpringReport service");
	}
	
	/** 
	* <p>Title: configureMessageConverters</p>
	* <p>Description: 自定义消息转换器，封装返回的数据
	* @param converters
	* @see org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#configureMessageConverters(java.util.List)
	*/
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	  //1. 需要定义一个converter转换消息的对象
//      CustomHttpMessageConverter fasHttpMessageConverter = new CustomHttpMessageConverter();
	  FastJsonHttpMessageConverter  fasHttpMessageConverter = new FastJsonHttpMessageConverter ();
      //升级最新版本需加=============================================================
      List<MediaType> supportedMediaTypes = new ArrayList<>();
      supportedMediaTypes.add(MediaType.APPLICATION_JSON);
      supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
      supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
      supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
      supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
      supportedMediaTypes.add(MediaType.APPLICATION_PDF);
      supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
      supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
      supportedMediaTypes.add(MediaType.APPLICATION_XML);
      supportedMediaTypes.add(MediaType.IMAGE_GIF);
      supportedMediaTypes.add(MediaType.IMAGE_JPEG);
      supportedMediaTypes.add(MediaType.IMAGE_PNG);
      supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
      supportedMediaTypes.add(MediaType.TEXT_HTML);
      supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
//      supportedMediaTypes.add(MediaType.TEXT_PLAIN);
      supportedMediaTypes.add(MediaType.TEXT_XML);
      supportedMediaTypes.add(new MediaType("application", "vnd.spring-boot.actuator.v2+json"));
      supportedMediaTypes.add(new MediaType("application", "vnd.spring-boot.actuator.v1+json"));
      supportedMediaTypes.add(new MediaType("application", "vnd.spring-boot.actuator.v3+json"));
      fasHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
      //2. 添加fastjson的配置信息，比如:是否需要格式化返回的json的数据
      FastJsonConfig fastJsonConfig = new FastJsonConfig();
      fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullBooleanAsFalse,SerializerFeature.WriteNullNumberAsZero);
      fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
      fastJsonConfig.setCharset(Charset.forName("UTF-8"));
      //解决Long转json精度丢失的问题
      SerializeConfig serializeConfig = SerializeConfig.globalInstance;
      serializeConfig.put(BigDecimal.class, ToStringSerializer.instance);
//      serializeConfig.put(Integer.class, ToStringSerializer.instance);
      serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
      serializeConfig.put(Long.class, ToStringSerializer.instance);
      serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
      fastJsonConfig.setSerializeConfig(serializeConfig);
      fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
      fastJsonConfig.setCharset(Charset.forName("UTF-8"));
      //3. 在converter中添加配置信息
      fasHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
      StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
      converters.add(fasHttpMessageConverter);
      converters.add(stringHttpMessageConverter);
    }
	
	/**  
	 * @Title: addArgumentResolvers
	 * @Description: 使获取用户信息注解配置生效
	 * @param argumentResolvers 
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#addArgumentResolvers(java.util.List) 
	 * @author caiyang
	 * @date 2020年5月14日 
	 */
	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
//		List<HttpMessageConverter<?>> converters = new ArrayList<>();
//		converters.add(fastJsonHttpMessageConverter);
        argumentResolvers.add(currentUserMethodArgumentResolver());
//        super.addArgumentResolvers(argumentResolvers);
    }
	
	/**
	*<p>Title: currentUserMethodArgumentResolver</p>
	*<p>Description: 获取用户信息注解配置</p>
	* @author caiyang
	* @return
	*/
	@Bean
	public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
	    return new CurrentUserMethodArgumentResolver();
	}
	
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 解决静态资源无法访问
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
		// 解决swagger无法访问
		registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		// 解决swagger的js文件无法访问
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		//静态文件访问
		registry.addResourceHandler("/images/**").addResourceLocations("file:"+dirPath);
	}
	
	/**
	*<p>Title: addCorsMappings</p>
	*<p>Description: 跨域设置</p>
	* @author caiyang
	* @param registry
	*/
	@Override
	protected void addCorsMappings(CorsRegistry registry) {
		super.addCorsMappings(registry);
		registry.addMapping("/**").allowedMethods("*").allowedOrigins("*").allowedHeaders("*");
	}
	
	/**
     * 日志拦截器
     *
     * @return
     */
    @Bean
    public HandlerInterceptor logInterceptor() {
        return new LogInterceptor();
    }
    
    /**
     * 注册拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
