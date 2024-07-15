package com.springreport.annotation;

import com.springreport.constants.StatusCode;
import com.springreport.exception.BizException;
import com.springreport.util.CheckUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/** 
* @ClassName: CheckParamAspect 
* @Description: 参数校验 切面
* @author caiyang
* @date 2019年4月5日 上午8:04:46 
*  
*/ 
@Component
@Aspect
public class CheckParamAspect {

    private static final Logger logger = LoggerFactory.getLogger(CheckParamAspect.class);

    @Pointcut("@annotation(com.springreport.annotation.Check)")
	public void methodCachePointcut() {
	}
    
    @Around(value = "methodCachePointcut()") 
    public Object check(ProceedingJoinPoint point) throws Throwable {
        Object obj;
        // 参数校验
        String msg = doCheck(point);
        if (!StringUtils.isEmpty(msg)) {
            // 这里可以返回自己封装的返回类，例如：return ResultBuilder.unsuccess(msg);
            throw new BizException(StatusCode.CHECK_FAILURE, MessageUtil.getValue("error.check.failure", new String[] {msg}));
        }
        // 通过校验，继续执行原有方法
        obj = point.proceed();
        return obj;
    }
    /** 
    * @Title: doCheck 
    * @Description: 参数校验
    * @param @param point
    * @param @return 设定文件 
    * @return String 返回类型 
    * @throws 
    */ 
    private String doCheck(ProceedingJoinPoint point) {
    	RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    	HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
    	String requestType = request.getMethod();//获取请求类型
    	
        // 获取方法参数值
        Object[] methodArguments = point.getArgs();
        Object[] arguments = point.getArgs();
        List<Object> objects = new ArrayList<Object>();
        if(methodArguments.length >0)
        {
//        	for (int i = 0; i < methodArguments.length; i++) {
//				if (!(methodArguments[i] instanceof UserInfoDto)) {
//					objects.add(methodArguments[i]);
//				}
//			}
        	objects.add(methodArguments[0]);
        }
        arguments = objects.toArray();
        // 获取方法
        Method method = getMethod(point);
        // 默认的错误信息
        String methodInfo = StringUtils.isEmpty(method.getName()) ? "" : " while calling " + method.getName();
        String methodName = method.getDeclaringClass() + "." +method.getName();
        StringBuffer sb = new StringBuffer();
        if (isCheck(method, arguments)) {
            Check annotation = method.getAnnotation(Check.class);
            String[] fields = annotation.value();
            if(!requestType.equals("GET"))
            {
            	// post请求，只支持对第一个参数进行校验，参数映射到实体类中
                Object vo = arguments[0];
                if (vo == null) {
                	throw new BizException(StatusCode.CHECK_FAILURE,"校验参数为空，请确认是否需要进行参数校验，不需要校验则将方法"+methodName+"上的check注解去掉，如需要校验则请查看参数是否正确传递。");
                } else {
                    for (String field : fields) {
                    	List<FieldInfo> infos = resolveField(field, methodInfo);
                    	for (int i = 0; i < infos.size(); i++) {
                    		// 获取字段的值
                    		Object value = ReflectionUtil.invokeGetter(vo, infos.get(i).field);
                    		Boolean isValid = infos.get(i).optEnum.fun.apply(value, infos.get(i).msgParam);
                    		sb.append(isValid ? "" : getErrorMsg(infos.get(i)));
//                    		if (!isValid) {
//    							sb.append("</br>");
//    						}
    					}
                    }
                }
            }else {
            	//get请求，从request中获取参数
            	if (request.getParameterMap().size() < fields.length) {
            		throw new BizException(StatusCode.CHECK_FAILURE,"校验参数个数与传入的参数个数不匹配，请检查方法"+methodName+"中传入的参数和校验参数是否一致。");
				}
            	for (String field : fields) {
                	List<FieldInfo> infos = resolveField(field, methodInfo);
                	for (int i = 0; i < infos.size(); i++) {
                		// 获取字段的值
                		Object value = request.getParameter(infos.get(i).field);
                		Boolean isValid = infos.get(i).optEnum.fun.apply(value, infos.get(i).msgParam);
                		sb.append(isValid ? "" : getErrorMsg(infos.get(i)));
                		if (!isValid) {
							sb.append("</br>");
						}
					}
                }
			}
            
        }
        return sb.toString();
    }
    
    /**
    * @Title: resolveField
    * @Description: 解析校验内容
    * @param @param fieldStr
    * @param @param methodInfo
    * @param @return 参数
    * @return List<FieldInfo>
    * @throws
    */
    private List<FieldInfo> resolveField(String fieldStr, String methodInfo)
    {
    	List<FieldInfo> fieldInfos = new ArrayList<CheckParamAspect.FieldInfo>();
    	
    	
    	if (fieldStr.contains(KEYSEPARATOR)) {
    		String key = fieldStr.split(KEYSEPARATOR)[0];
    		String value = fieldStr.split(KEYSEPARATOR)[1];
    		if (StringUtil.isNullOrEmpty(value)) {
    			throw new BizException(StatusCode.CHECK_FAILURE,MessageUtil.getValue("error.check.format"));
    		}else {
				//解析校验内容
	    		for (int i = 0; i < value.split(RULESEPARAOR).length; i++) {
	    			FieldInfo fieldInfo = new FieldInfo();
					String validRule = value.split(RULESEPARAOR)[i];//校验规则
					if (validRule.contains(PARAMSEPARATOR)) {
						String [] validParam = new String[validRule.split(PARAMSEPARATOR).length];
						for (int j = 0; j < validParam.length-1; j++) {
							validParam[j]=validRule.split(PARAMSEPARATOR)[j+1];
						}
						validParam[validRule.split(PARAMSEPARATOR).length-1] = key;
						fieldInfo.field = key;//校验的字段
						fieldInfo.msgParam = validParam;//msg需要的参数
						fieldInfo.validRule = validRule.split(PARAMSEPARATOR)[0];//校验规则
						setOptEum(fieldInfo);
						fieldInfos.add(fieldInfo);
					}else {
						fieldInfo.field = key;
						String[] validParam = new String[1];
						validParam[0] = key;
						fieldInfo.msgParam = validParam;
						fieldInfo.validRule = validRule;
						setOptEum(fieldInfo);
						fieldInfos.add(fieldInfo);
					}
				}
			}
    		
    	}else {
			throw new BizException(StatusCode.CHECK_FAILURE,MessageUtil.getValue("error.check.format"));
		}
    	return fieldInfos;
    }


    /**
     * 是否不为空
     *
     * @param value       字段值
     * @param msgParam 错误消息参数，其中包括一些判断条件，例如长度
     * @return 是否不为空
     */
    private static Boolean isNotNull(Object value, String[] msgParam) {
        return CheckUtil.isNotNull(value);
    }
    
    /**
    * @Title: checkLength
    * @Description: 字符串长度校验
    * @param @param value
    * @param @param msgParam
    * @param @return 参数
    * @return Boolean
    * @throws
    */
    private static Boolean checkLength(Object value,String[] msgParam)
    {
    	if (value == null) {
			return Boolean.TRUE;
		}
    	if (msgParam == null || msgParam.length == 0) {
			throw new BizException(StatusCode.CHECK_FAILURE, MessageUtil.getValue("error.msgparam.length"));
		}
    	if (msgParam.length < 3) {
    		throw new BizException(StatusCode.CHECK_FAILURE, MessageUtil.getValue("error.msgparam.length",new String[] {msgParam[msgParam.length-1]}));
		}
    	if (!CheckUtil.isNumeric(msgParam[1])) {
    		throw new BizException(StatusCode.CHECK_FAILURE, MessageUtil.getValue("error.msgparam.length",new String[] {msgParam[msgParam.length-1]}));
		}
    	return CheckUtil.validStrLength(value.toString(), Integer.parseInt(msgParam[1]));
    }
    
    /**  
     * @Title: checkFixedLength
     * @Description: 校验固定长度
     * @param value
     * @param msgParam
     * @return
     * @author caiyang
     * @date 2020-07-14 09:08:09 
     */ 
    private static Boolean checkFixedLength(Object value,String[] msgParam)
    {
    	if (value == null) {
			return Boolean.TRUE;
		}
    	if (msgParam == null || msgParam.length == 0) {
			throw new BizException(StatusCode.CHECK_FAILURE, MessageUtil.getValue("error.msgparam.length"));
		}
    	if (msgParam.length < 3) {
    		throw new BizException(StatusCode.CHECK_FAILURE, MessageUtil.getValue("error.msgparam.length",new String[] {msgParam[msgParam.length-1]}));
		}
    	if (!CheckUtil.isNumeric(msgParam[1])) {
    		throw new BizException(StatusCode.CHECK_FAILURE, MessageUtil.getValue("error.msgparam.length",new String[] {msgParam[msgParam.length-1]}));
		}
    	return CheckUtil.validFixedLength(value.toString(), Integer.parseInt(msgParam[1]));
    }
    
    /**
    * @Title: isMobile
    * @Description: 是否为手机号码校验
    * @param @param value
    * @param @param msgParam
    * @param @return 参数
    * @return Boolean
    * @throws
    */
    private static Boolean isMobile(Object value,String[] msgParam)
    {
    	if (value == null) {
			return true;
		}
    	return CheckUtil.isMobileNO(value.toString());
    }
    
    /**
     * @Title: isMobile
     * @Description: 是否为手机号码或者座机号码校验
     * @param @param value
     * @param @param msgParam
     * @param @return 参数
     * @return Boolean
     * @throws
     */
     private static Boolean isMobileOrPhone(Object value,String[] msgParam)
     {
     	if (value == null || StringUtil.isNullOrEmpty(value.toString())) {
 			return true;
 		}
     	return CheckUtil.isMobileOrPhone(value.toString());
     }
    
    /**
    * @Title: isEmail
    * @Description: 是否为邮箱格式校验
    * @param @param value
    * @param @param msgParam
    * @param @return 参数
    * @return Boolean
    * @throws
    */
    private static Boolean isEmail(Object value,String[] msgParam)
    {
    	if (value == null) {
			return true;
		}
    	return CheckUtil.isEmail(value.toString());
    }
    
    /**
    *<p>Title: lessThan</p>
    *<p>Description: 校验最大值</p>
    * @author caiyang
    * @param value
    * @param msgParam
    * @return
    */
    private static Boolean lessThan(Object value,String[] msgParam) {
    	if (value == null) {
			return Boolean.TRUE;
		}
    	if (msgParam == null || msgParam.length == 0) {
			throw new BizException(StatusCode.CHECK_FAILURE, MessageUtil.getValue("error.msgparam.max"));
		}
    	if (msgParam.length < 3) {
    		throw new BizException(StatusCode.CHECK_FAILURE, MessageUtil.getValue("error.msgparam.max",new String[] {msgParam[msgParam.length-1]}));
		}
    	if (!CheckUtil.isNumeric(msgParam[1])) {
    		throw new BizException(StatusCode.CHECK_FAILURE, MessageUtil.getValue("error.msgparam.max",new String[] {msgParam[msgParam.length-1]}));
		}
    	if (!CheckUtil.isNumeric(value.toString())) {
    		throw new BizException(StatusCode.CHECK_FAILURE, MessageUtil.getValue("error.msgparam.max",new String[] {msgParam[msgParam.length-1]}));
		}
    	return CheckUtil.validMax(new BigDecimal(value.toString()), new BigDecimal(msgParam[1]));
    }
    
    /**  
     * @Title: checkDate
     * @Description: 日期格式校验
     * @param value
     * @author caiyang
     * @date 2020-07-23 03:37:49 
     */ 
    private static Boolean checkDate(Object value,String[] msgParam) {
    	if (value == null) {
			return Boolean.TRUE;
		}
    	return CheckUtil.isDate(value.toString(), "yyyy-MM-dd");
    }


    /**
     * 判断是否符合参数规则
     *
     * @param method    方法
     * @param arguments 方法参数
     * @return 是否符合
     */
    private Boolean isCheck(Method method, Object[] arguments) {
        Boolean isCheck = Boolean.TRUE;
        // 只允许有一个参数
        if (!method.isAnnotationPresent(Check.class)
                || arguments == null
                || arguments.length != 1) {
            isCheck = Boolean.FALSE;
        }
        return isCheck;
    }

    /**
     * 获取方法
     *
     * @param joinPoint ProceedingJoinPoint
     * @return 方法
     */
    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint
                        .getTarget()
                        .getClass()
                        .getDeclaredMethod(joinPoint.getSignature().getName(),
                                method.getParameterTypes());
            } catch (SecurityException | NoSuchMethodException e) {
            	logger.error("" + e);
            }
        }
        return method;
    }

    /**
     * 字段信息
     */
    class FieldInfo {
        /**
         * 字段
         */
        String field;
        
        /**
        * 消息提示需要的参数
        */
        String[] msgParam;
        
        /**
        * 校验内容
        */
        String validRule;
        /**
         * 操作符
         */
        String operator;
        /**
         * 操作枚举
         */
        Operator optEnum;
    }

    /**
     * 操作枚举，封装操作符和对应的校验规则
     */
    enum Operator {
    	
    	/**
    	* 不为空
    	*/
    	REQUIRED("required", CheckParamAspect::isNotNull),
    	
    	/**
    	* @Fields LENGTH : 最大长度
    	*/
    	LENGTH("length", CheckParamAspect::checkLength),
    	
    	/**
    	* @Fields FIXEDLENGTH : 固定长度
    	*/
    	FIXEDLENGTH("fixedlength", CheckParamAspect::checkFixedLength),
    	
    	/**
    	* @Fields MOBILE : 手机号码
    	*/
    	MOBILE("mobile", CheckParamAspect::isMobile),
    	
    	/**  
    	 * @Fields MOBILEPHONE : 手机号码或者座机号码
    	 * @author caiyang
    	 * @date 2020-06-18 03:23:38 
    	 */ 
    	MOBILEPHONE("mobilePhone", CheckParamAspect::isMobileOrPhone),
    	
    	/**
    	* @Fields EMAIL : 邮箱
    	*/
    	EMAIL("email", CheckParamAspect::isEmail),
    	
    	/**
    	* @Fields max : 最大值
    	*/
    	MAX("max", CheckParamAspect::lessThan),
    	
    	/**
    	* @Fields date : 日期类型 yyyy-MM-dd类型
    	*/
    	DATE("date", CheckParamAspect::checkDate);

        private String value;

        /**
         * BiFunction：接收字段值(Object)和参数数(String)，返回是否符合规则(Boolean)
         */
        private BiFunction<Object, String[], Boolean> fun;

        Operator(String value, BiFunction<Object, String[], Boolean> fun) {
            this.value = value;
            this.fun = fun;
        }
    }
    
    /**
    * @Title: setOptEum
    * @Description: 校验规则设置
    * @param @param fieldInfo
    * @param @return 参数
    * @return FieldInfo
    * @throws
    */
    private FieldInfo setOptEum(FieldInfo fieldInfo)
    {
    	if (Operator.REQUIRED.value.equals(fieldInfo.validRule)) {
			fieldInfo.optEnum = Operator.REQUIRED;
		}
    	else if (Operator.LENGTH.value.equals(fieldInfo.validRule)) {
			fieldInfo.optEnum = Operator.LENGTH;
		}
		else if (Operator.MOBILE.value.equals(fieldInfo.validRule)) {
			fieldInfo.optEnum = Operator.MOBILE;
		}
		else if (Operator.MOBILEPHONE.value.equals(fieldInfo.validRule)) {
			fieldInfo.optEnum = Operator.MOBILEPHONE;
		}
		else if (Operator.EMAIL.value.equals(fieldInfo.validRule)) {
			fieldInfo.optEnum = Operator.EMAIL;
		}else if (Operator.MAX.value.equals(fieldInfo.validRule)) {
			fieldInfo.optEnum = Operator.MAX;
		}else if (Operator.FIXEDLENGTH.value.equals(fieldInfo.validRule)) {
			fieldInfo.optEnum = Operator.FIXEDLENGTH;
		}else if (Operator.DATE.value.equals(fieldInfo.validRule)) {
			fieldInfo.optEnum = Operator.DATE;
		}
    	
    	return fieldInfo;
    }

    /**
    * @Title: getErrorMsg
    * @Description: 获取错误信息
    * @param @param fieldInfo
    * @param @return 参数
    * @return String
    * @throws
    */
    private String getErrorMsg(FieldInfo fieldInfo)
    {
    	if (Operator.REQUIRED.value.equals(fieldInfo.validRule)) {
			return MessageUtil.getValue("error.notnull", fieldInfo.msgParam);
		}
    	else if (Operator.LENGTH.value.equals(fieldInfo.validRule)) {
			return MessageUtil.getValue("error.length", fieldInfo.msgParam);
		}
    	else if (Operator.MOBILE.value.equals(fieldInfo.validRule)) {
			return MessageUtil.getValue("error.format.input",new String[] {"手机号码"});
		}
    	else if (Operator.MOBILEPHONE.value.equals(fieldInfo.validRule)) {
			return MessageUtil.getValue("error.phonemobile");
		}
    	else if (Operator.EMAIL.value.equals(fieldInfo.validRule)) {
			return MessageUtil.getValue("error.format.input",new String[] {"邮箱"});
		}
    	else if (Operator.MAX.value.equals(fieldInfo.validRule)) {
			return MessageUtil.getValue("error.max",fieldInfo.msgParam);
		}else if (Operator.FIXEDLENGTH.value.equals(fieldInfo.validRule)) {
			return MessageUtil.getValue("error.fixedlength",fieldInfo.msgParam);
		}else if (Operator.DATE.value.equals(fieldInfo.validRule)) {
			return MessageUtil.getValue("error.format.input",new String[] {"日期"});
		}
    	return "";
    }

    // -====================== 常量 =========================

    private static final String KEYSEPARATOR = ":";//key和value之间的分隔标志
    private static final String RULESEPARAOR = ";";//校验规则之间的分隔标志
    private static final String PARAMSEPARATOR = "#";//一个校验规则中参数的分隔标志
    private static final String DEFAULTVALIDRULE = "required";//默认的校验规则



}
