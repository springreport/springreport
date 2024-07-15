package com.springreport.exception;

import org.apache.shiro.authc.AuthenticationException;

/**  
 * @ClassName: TokenExpiredException
 * @Description: token过期异常
 * @author caiyang
 * @date 2020-06-08 04:47:26 
*/  
public class TokenExpiredException extends AuthenticationException{

	public TokenExpiredException(String message)
	{
		super(message);
	}
}
