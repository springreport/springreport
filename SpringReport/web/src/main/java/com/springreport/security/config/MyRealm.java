package com.springreport.security.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springreport.api.sysapi.ISysApiService;
import com.springreport.api.sysmerchant.ISysMerchantService;
import com.springreport.api.sysroleapi.ISysRoleApiService;
import com.springreport.api.sysuser.ISysUserService;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.StatusCode;
import com.springreport.entity.sysapi.SysApi;
import com.springreport.entity.sysmerchant.SysMerchant;
import com.springreport.entity.sysuser.SysUser;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.TokenExpiredException;
import com.springreport.util.DateUtil;
import com.springreport.util.JWTUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;



/**
 * @ClassName: MyRealm
 * @Description:
 * @Author 蔡阳
 * @DateTime 2020年5月31日12:01:59
 */

public class MyRealm extends AuthorizingRealm {

	@Autowired
	@Lazy
	private ISysRoleApiService iSysRoleApiService;
	
	@Autowired
	@Lazy
	private ISysApiService iSysApiService;
	
	@Autowired
	@Lazy
	private ISysUserService iSysUserService;
	
	
	@Autowired
	@Lazy
	private RedisUtil redisUtil;
	
	@Value("${authentic.enabale}")
    private boolean authenticEnabale;
	
	@Value("${merchantmode}")
    private Integer merchantmode;
	
	@Autowired
	private ISysMerchantService iSysMerchantService;
	
	@Value("${thirdParty.type}")
	private String thirdPartyType;
	
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JWTToken;
	}

	/**
	 *	权限验证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if(authenticEnabale)
		{
			UserInfoDto userInfoDto = null;
			if(principals.toString().equals(thirdPartyType)) {
				userInfoDto = new UserInfoDto();
				userInfoDto.setIsAdmin(YesNoEnum.YES.getCode());
				userInfoDto.setIsSystemMerchant(YesNoEnum.NO.getCode());
			}else {
				 userInfoDto = JWTUtil.getUserInfo(principals.toString());
			}
			SysMerchant merchant = null;
			List<Long> merchantAuthIds = null;//商户拥有的全部权限id
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				QueryWrapper<SysMerchant> merchantQueryWrapper = new QueryWrapper<>();
				merchantQueryWrapper.eq("merchant_no", userInfoDto.getMerchantNo());
				merchantQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				merchant = this.iSysMerchantService.getOne(merchantQueryWrapper, false);
				merchantAuthIds = this.iSysMerchantService.getMerchantAuthApi(merchant);
			}
			if (YesNoEnum.YES.getCode()!=userInfoDto.getIsAdmin()) {
				//非管理员
				JSONObject params = new JSONObject();
				params.put("roleId", userInfoDto.getRoleId());
				if(this.merchantmode == YesNoEnum.YES.getCode()) {
					params.put("merchantNo", userInfoDto.getMerchantNo());
				}
				List<SysApi> apis = this.iSysRoleApiService.getApisByRole(params);
				//获取用户相应的permission  
		        List<String> permissions = new ArrayList<String>();	
		        if (!ListUtil.isEmpty(apis)) {
		        	for (int i = 0; i < apis.size(); i++) {
		        		if(this.merchantmode == YesNoEnum.YES.getCode()) {
		        			if(!ListUtil.isEmpty(merchantAuthIds) && merchantAuthIds.contains(apis.get(i).getId()))
			        		{
		        				permissions.add(apis.get(i).getApiCode());
			        		}
		        		}else {
		        			permissions.add(apis.get(i).getApiCode());
		        		}
			        }
				}
		        info.setStringPermissions(new HashSet<>(permissions));
			}else {
				//管理员
				List<String> permissions = new ArrayList<String>();	
				QueryWrapper<SysApi> queryWrapper = new QueryWrapper<SysApi>();
				queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<SysApi> apis = this.iSysApiService.list(queryWrapper);
				if (!ListUtil.isEmpty(apis)) {
					for (int i = 0; i < apis.size(); i++) {
						if(this.merchantmode == YesNoEnum.YES.getCode()) {
		        			if(!ListUtil.isEmpty(merchantAuthIds) && merchantAuthIds.contains(apis.get(i).getId()))
			        		{
		        				permissions.add(apis.get(i).getApiCode());
			        		}
		        		}else {
		        			permissions.add(apis.get(i).getApiCode());
		        		}
					}
				}
				info.setStringPermissions(new HashSet<>(permissions));
			}
		}
		return info;
	}

	/**
	 * 默认使用此方法进行用户正确与否验证，错误抛出异常即可
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		JWTToken jwtToken = (JWTToken) authenticationToken;
		String token = jwtToken.getToken();
		ServletResponse response = ((WebSubject)SecurityUtils.getSubject()).getServletResponse(); 
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		if(StringUtil.isNotEmpty(jwtToken.getType()) && jwtToken.getType().equals(thirdPartyType)) {
			return new SimpleAuthenticationInfo(jwtToken.getType(), token, "my_realm");
		}
		// 解密获得username，用于和数据库进行对比
		UserInfoDto userInfoDto = JWTUtil.getUserInfo(token);
		if (StringUtil.isNullOrEmpty(userInfoDto.getUserName())) {
			throw new AuthenticationException("token 无效！");
		}
		SysUser sysUser = this.iSysUserService.getById(userInfoDto.getUserId());
		if (sysUser == null) {
			throw new AuthenticationException("用户" + userInfoDto.getUserName() + "不存在");
		}
		if (YesNoEnum.YES.getCode().intValue() == sysUser.getUserLocked().intValue()) {
			httpServletResponse.setHeader("statusCode", StatusCode.TOKEN_LOCK);
			throw new TokenExpiredException("账号已被锁定!");
		}
		String statusCode = JWTUtil.verify(token, userInfoDto, sysUser.getPassword());// token校验返回码
		if (StatusCode.TOKEN_FAILURE.equals(statusCode)) {
			// token过期失效，并且token在正常失效范围内，则可以正常访问，否则不允许访问
			// 判断过期时间是否超过两个小时，超过两个小时，则token失效，两个小时之内允许访问并刷新token
			long now = DateUtil.getTimestampLong();// 获取当前时间
			long expireDate = userInfoDto.getExpireDate().getTime();// 获取过期时间
			if ((now - expireDate) < JWTUtil.REFRESH_TIME && YesNoEnum.NO.getCode().intValue() == userInfoDto.getIsShareToken().intValue()) {
				String newToken = JWTUtil.sign(userInfoDto, sysUser.getPassword());
				httpServletResponse.setHeader("Authorization", newToken);// 刷新token
				return new SimpleAuthenticationInfo(token, token, "my_realm");
			} else {
				httpServletResponse.setHeader("statusCode", StatusCode.TOKEN_FAILURE);
				throw new TokenExpiredException("token过期，请重新登录!");
			}

		} else if (StatusCode.TOKEN_ERROR.equals(statusCode)) {
			// token错误
			throw new AuthenticationException("token无效!");
		}
		return new SimpleAuthenticationInfo(token, token, "my_realm");
	}

}
