/**
 * @Title: JWTUtil.java
 * @Package www.codepeople.cn.util
 * @Description: 
 * Copyright: Copyright (c) 2019 www.codepeople.cn Inc. All rights reserved. 
 * Website: www.codepeople.cn
 * 注意：本内容仅限于海南科澜技术信息有限公司内部传阅，禁止外泄以及用于其他的商业目 
 * @Author 刘仁
 * @DateTime 2019年4月1日 下午4:32:56
 * @version V1.0
 */

package com.springreport.util;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.StatusCode;
import com.springreport.enums.YesNoEnum;

/**
 * @ClassName: JWTUtil
 * @Description: 
 * @Author 蔡阳
 * @DateTime 2019年10月24日13:45:42
 */

public class JWTUtil {
	// 过期时间24小时
	private static final long EXPRIE_TIME = 24*60*60*1000;
	
	/**  
	 * @Fields PC_REFRESH_TIME : token过期刷新时间2小时,过期2个小时以内允许继续访问并重新生成token
	 * @author caiyang
	 * @date 2020-06-08 08:08:48 
	 */ 
	public static final long REFRESH_TIME = 2*60*60*1000L;
	
	public static String verify(String token, UserInfoDto userInfoDto, String secret) {
		try {
			Algorithm algorithm = Algorithm.HMAC512(secret);
			JWTVerifier verifier = JWT.require(algorithm)
					.withClaim("userName", userInfoDto.getUserName())
					.withClaim("userRealName", userInfoDto.getUserRealName())
					.withClaim("userId", userInfoDto.getUserId())
					.withClaim("roleId", userInfoDto.getRoleId())
					.withClaim("roleName", userInfoDto.getRoleName())
					.withClaim("merchantNo", userInfoDto.getMerchantNo())
					.withClaim("deptId", userInfoDto.getDeptId())
					.build();
			verifier.verify(token);
			return StatusCode.SUCCESS;
		} catch (Exception e) {
			if (e instanceof TokenExpiredException) {
        		return StatusCode.TOKEN_FAILURE;//token过期失效
        	}else {
        		return StatusCode.TOKEN_ERROR;//token错误
        	}
		}
	}
	/**
	 * @Title: getUsername
	 * @Description: 获取token中的信息无需secret解密也能获得
	 * @Author 刘仁
	 * @DateTime 2019年4月1日 下午4:42:39
	 * @param token
	 * @return
	 */
	public static String getUsername(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("userName").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	
	public static Long getUserId(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("userId").asLong();
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	
	public static String sign(UserInfoDto userInfoDto, String secret) {
		Date date = new Date(System.currentTimeMillis()+EXPRIE_TIME);
		Algorithm algorithm = Algorithm.HMAC512(secret);
		// 附带username信息
		return JWT.create()
				.withClaim("userName", userInfoDto.getUserName())
				.withClaim("userRealName", userInfoDto.getUserRealName())
				.withClaim("userId", userInfoDto.getUserId())
				.withClaim("roleId", userInfoDto.getRoleId())
				.withClaim("roleName", userInfoDto.getRoleName())
				.withClaim("isAdmin", userInfoDto.getIsAdmin())
				.withClaim("merchantNo", userInfoDto.getMerchantNo())
				.withClaim("merchantMode", userInfoDto.getMerchantMode())
				.withClaim("isSystemMerchant", userInfoDto.getIsSystemMerchant())
				.withClaim("deptId", userInfoDto.getDeptId())
				.withClaim("isShareToken", YesNoEnum.NO.getCode())
				.withExpiresAt(date)
				.sign(algorithm);
	}
	
	public static String sign(UserInfoDto userInfoDto, String secret,Long expireTime) {
		Date date = new Date(System.currentTimeMillis()+expireTime*1000);
		Algorithm algorithm = Algorithm.HMAC512(secret);
		// 附带username信息
		return JWT.create()
				.withClaim("userName", userInfoDto.getUserName())
				.withClaim("userRealName", userInfoDto.getUserRealName())
				.withClaim("userId", userInfoDto.getUserId())
				.withClaim("roleId", userInfoDto.getRoleId())
				.withClaim("roleName", userInfoDto.getRoleName())
				.withClaim("isAdmin", userInfoDto.getIsAdmin())
				.withClaim("merchantNo", userInfoDto.getMerchantNo())
				.withClaim("merchantMode", userInfoDto.getMerchantMode())
				.withClaim("isSystemMerchant", userInfoDto.getIsSystemMerchant())
				.withClaim("deptId", userInfoDto.getDeptId())
				.withClaim("isShareToken", YesNoEnum.YES.getCode())
				.withExpiresAt(date)
				.sign(algorithm);
	}
	
	public static UserInfoDto getUserInfo(String token)
	{
		try {
			UserInfoDto userInfoDto = new UserInfoDto();
			DecodedJWT jwt = JWT.decode(token);
			String userName = jwt.getClaim("userName").asString();
			userInfoDto.setUserName(userName);
			String userRealName = jwt.getClaim("userRealName").asString();
			userInfoDto.setUserRealName(userRealName);
			Long userId = jwt.getClaim("userId").asLong();
			userInfoDto.setUserId(userId);
			Long roleId = jwt.getClaim("roleId").asLong();
			userInfoDto.setRoleId(roleId);
			String roleName = jwt.getClaim("roleName").asString();
			userInfoDto.setRoleName(roleName);
			Integer isAdmin = jwt.getClaim("isAdmin").asInt();
			userInfoDto.setIsAdmin(isAdmin);
			Date expireDate = jwt.getExpiresAt();
			userInfoDto.setExpireDate(expireDate);
			Integer merchantMode = jwt.getClaim("merchantMode").asInt();
			userInfoDto.setMerchantMode(merchantMode);
			String merchantNo = jwt.getClaim("merchantNo").asString();
			userInfoDto.setMerchantNo(merchantNo);
			Integer isSystemMerchant = jwt.getClaim("isSystemMerchant").asInt();
			userInfoDto.setIsSystemMerchant(isSystemMerchant);
			Long deptId = jwt.getClaim("deptId").asLong();
			userInfoDto.setDeptId(deptId);
			Integer isShareToken = jwt.getClaim("isShareToken").asInt();
			userInfoDto.setIsShareToken(isShareToken == null?YesNoEnum.NO.getCode():isShareToken);
			return userInfoDto;
		} catch (Exception e) {
			return new UserInfoDto();
		}
	}
}
