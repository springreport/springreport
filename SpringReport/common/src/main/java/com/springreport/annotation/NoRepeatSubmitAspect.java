package com.springreport.annotation;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.springreport.util.CusAccessObjectUtil;
import com.springreport.util.Md5Util;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;

/**  
 * @ClassName: NoRepeatSubmitAspect
 * @Description: 防止重复提交切面
 * @author caiyang
 * @date 2020-06-01 08:47:46 
*/  
@Aspect
@Component
public class NoRepeatSubmitAspect {

	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	/**  
	 * @Fields redisUtil : redis工具类注入
	 * @author caiyang
	 * @date 2020-06-01 08:48:10 
	 */ 
	@Autowired
	private RedisUtil redisUtil;
	
	/**  
	 * @Fields isNoRepeatSubmit : 放重复提交是否开启
	 * @author caiyang
	 * @date 2020-06-01 08:51:20 
	 */ 
	@Value("${norepeatsubmit.is.enable}")
	private boolean isNoRepeatSubmit;
	
	@Pointcut("@annotation(com.springreport.annotation.NoRepeatSubmit)")
	public void noRepeatSubmit() {

	}
	
	@Around("noRepeatSubmit()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
		if (!isNoRepeatSubmit) {
            return pjp.proceed();
        }
		String token = httpServletRequest.getHeader("Authorization");//获取token
		if (StringUtil.isNullOrEmpty(token) || (StringUtil.isNotEmpty(token) && token.equals("null"))) {
			token = CusAccessObjectUtil.getIpAddress(httpServletRequest);
		}
	    String path = httpServletRequest.getServletPath();//获取请求的url
	    MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        NoRepeatSubmit noRepeatSubmit = method.getAnnotation(NoRepeatSubmit.class);
        boolean validate = validate(token, path, pjp, noRepeatSubmit.expire());
        Object proceed = null;
        if (validate) {
        	 proceed = pjp.proceed();
		}
        return proceed;
	}
	
	/**  
	 * @Title: validate
	 * @Description: 校验是否重复提交 
	 * @param token
	 * @param path
	 * @param pjp
	 * @param expire
	 * @author caiyang
	 * @date 2020-06-01 09:09:14 
	 */ 
	private boolean validate(String token,String path,ProceedingJoinPoint pjp,int expire)
	{
		Object[] args = pjp.getArgs();//请求参数
		String paramJsonStr = "";//请求参数字符串
		if (args != null) {
            for (int i = 0; i < args.length; i++) {
                paramJsonStr.concat(JSON.toJSONString(args[i]));
            }
        }
		String key = Md5Util.generateMd5(token+paramJsonStr+path);//token+参数+请求地址进行md5加密后生成一个key
		if (redisUtil.hasKey(key)) {
//			throw new BizException(StatusCode.REPEAT_SUBMIT_ERROR, MessageUtil.getValue("error.repreat.submit"));
			return false;
		}
		redisUtil.set(key, 1, expire);
		return true;
	}
}
