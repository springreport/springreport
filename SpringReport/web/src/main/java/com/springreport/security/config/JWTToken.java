package com.springreport.security.config;

import org.apache.shiro.authc.AuthenticationToken;

import lombok.Data;

/**
 * @ClassName: JWTToken
 * @Description: 
 * @Author 蔡阳
 * @DateTime 2020年5月31日12:00:52
 */
@Data
public class JWTToken implements AuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	/**
	* @Feilds:token
	* @author caiyang
	*/  
	private String token;
	
	/**
	* @Feilds:type 类型 
	* @author caiyang
	*/  
	private String type;
	
	public JWTToken(String token,String type) {
		this.token = token;
		this.type = type;
	}
	@Override
	public Object getPrincipal() {
		return token;
	}

	
	@Override
	public Object getCredentials() {
		return token;
	}

}
