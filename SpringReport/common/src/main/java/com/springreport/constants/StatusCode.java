package com.springreport.constants;

/**   
 * @ClassName:  StatusCode   
 * @Description:状态码
 * @author: caiyang 
 * @date:   2019年2月15日 上午9:20:45   
 *      
 */
public class StatusCode {
	
	/**
	 * :执行成功
	 */
	public static String SUCCESS = "200";
	
	/**
	 * 执行失败异常
	 */
	public static String FAILURE = "50001";
	
	/**
	 * 参数校验异常
	 */
	public static String CHECK_FAILURE = "50002";
	
	/**
	 * 访问权限异常
	 */
	public static String AUTH_FAILURE = "50003";
	
	/**
	 * TOKEN失效
	 */
	public static String TOKEN_FAILURE = "50004";
	
	/**
	* @Feilds:TOKEN_ERROR token错误
	* @author caiyang
	*/  
	public static String TOKEN_ERROR = "50005";
	
	/**  
	 * @Fields LOGIN_FAILURE : 登录失败
	 * @author caiyang
	 * @date 2020年5月14日 
	 */ 
	public static String LOGIN_FAILURE = "50006";
	
	
	/**  
	 * @Fields REPEAT_SUBMIT_ERROR : 重复提交错误码
	 * @author caiyang
	 * @date 2020年6月1日09:21:24
	 */ 
	public static String REPEAT_SUBMIT_ERROR = "50007";
	
	/**  
	 * @Fields TOKEN_EMPTY : token为空错误
	 * @author caiyang
	 * @date 2020-06-08 04:24:07 
	 */ 
	public static String TOKEN_EMPTY = "50008";
	
	/**  
	 * @Fields TOKEN_LOCK : 用户锁定，token失效
	 * @author caiyang
	 * @date 2020-06-24 10:52:53 
	 */ 
	public static String TOKEN_LOCK = "50009";
	
	/**  
	 * @Fields TOKEN_CHANGE : 已在其他端登录
	 * @author caiyang
	 * @date 2020-06-30 04:05:36 
	 */ 
	public static String TOKEN_CHANGE = "50010";
	
	/**  
	 * @Fields ACCOUNT_NOTEXIST : 账号不存在
	 * @author caiyang
	 * @date 2020年8月18日15:41:54
	 */ 
	public static String ACCOUNT_NOTEXIST = "50011";
}
