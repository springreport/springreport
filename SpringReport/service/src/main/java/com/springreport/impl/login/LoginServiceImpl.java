package com.springreport.impl.login;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springreport.api.login.ILoginService;
import com.springreport.api.loginlog.ILoginLogService;
import com.springreport.api.sysapi.ISysApiService;
import com.springreport.api.sysmerchant.ISysMerchantService;
import com.springreport.api.sysmerchantauthtemplate.ISysMerchantAuthTemplateService;
import com.springreport.api.sysmerchantauthtemplateids.ISysMerchantAuthTemplateIdsService;
import com.springreport.api.sysrole.ISysRoleService;
import com.springreport.api.sysroleapi.ISysRoleApiService;
import com.springreport.api.sysuser.ISysUserService;
import com.springreport.api.sysuserdept.ISysUserDeptService;
import com.springreport.api.sysuserrole.ISysUserRoleService;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.StatusCode;
import com.springreport.entity.loginlog.LoginLog;
import com.springreport.entity.sysapi.SysApi;
import com.springreport.entity.sysmerchant.SysMerchant;
import com.springreport.entity.sysmerchantauthtemplateids.SysMerchantAuthTemplateIds;
import com.springreport.entity.sysrole.SysRole;
import com.springreport.entity.sysuser.SysUser;
import com.springreport.entity.sysuserdept.SysUserDept;
import com.springreport.entity.sysuserrole.SysUserRole;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;
import com.springreport.util.CusAccessObjectUtil;
import com.springreport.util.JWTUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.Md5Util;
import com.springreport.util.MessageUtil;

/**  
 * @ClassName: LoginServiceImpl
 * @Description: 登录服务实现
 * @author caiyang
 * @date 2021-06-16 04:36:52 
*/  
@Service
public class LoginServiceImpl implements ILoginService{
	
	@Autowired
	private ISysUserService iSysUserService;
	
	@Autowired
	private ISysUserRoleService iSysUserRoleService;
	
	@Autowired
	private ISysRoleService iSysRoleService;
	
	@Autowired
	private ISysRoleApiService iSysRoleApiService;
	
	@Autowired
	private ISysApiService iSysApiService;
	
	@Autowired
	private ILoginLogService iLoginLogService;
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private ISysMerchantAuthTemplateService iSysMerchantAuthTemplateService;
	
	@Autowired
	private ISysMerchantAuthTemplateIdsService iSysMerchantAuthTemplateIdsService;
	
	@Autowired
	private ISysMerchantService iSysMerchantService;
	
	@Autowired
	private ISysUserDeptService iSysUserDeptService;
	
	@Value("${merchantmode}")
    private Integer merchantmode;

	/**  
	 * @Title: doLogin
	 * @Description: 登录
	 * @param sysUser
	 * @return 
	 * @see com.caiyang.api.login.ILoginService#doLogin(com.caiyang.entity.sysuser.SysUser) 
	 * @author caiyang
	 * @throws UnsupportedEncodingException 
	 * @date 2021-06-16 04:48:46 
	 */
	@Override
	public UserInfoDto doLogin(SysUser sysUser) throws UnsupportedEncodingException {
		LoginLog loginLog = new LoginLog();
		loginLog.setOperateTime(new Date());
		loginLog.setOperateIp(CusAccessObjectUtil.getIpAddress(httpServletRequest));
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
		queryWrapper.eq("user_name", sysUser.getUserName());
		if(this.merchantmode == YesNoEnum.YES.getCode())
		{//多租户模式，需要使用租户code
			queryWrapper.eq("merchant_no", sysUser.getMerchantNo());
		}
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysUser user = this.iSysUserService.getOne(queryWrapper,false);
		if(user == null)
		{
			loginLog.setStatus(YesNoEnum.NO.getCode());
			loginLog.setErrorInfo(MessageUtil.getValue("error.notexist", new String[] {"用户信息"}));
			this.iLoginLogService.save(loginLog);
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.login"));
		}
		if(user.getUserLocked().intValue() == YesNoEnum.YES.getCode().intValue())
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.status.forbidden", new String[] {"用户"}));
		}
		if(!Md5Util.generateMd5(sysUser.getPassword()).equals(user.getPassword()))
		{
			loginLog.setStatus(YesNoEnum.NO.getCode());
			loginLog.setErrorInfo(MessageUtil.getValue("error.login"));
			this.iLoginLogService.save(loginLog);
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.login"));
		}
		UserInfoDto result = new UserInfoDto();
		result.setUserId(user.getId());
		result.setUserName(user.getUserName());
		result.setUserRealName(user.getUserRealName());
		List<String> apis = new ArrayList<String>();
		if(YesNoEnum.YES.getCode().intValue() == user.getIsAdmin().intValue())
		{
			result.setIsAdmin(YesNoEnum.YES.getCode());
			result.setRoleName("管理员");
			if(this.merchantmode == YesNoEnum.YES.getCode())
			{
				QueryWrapper<SysMerchant> merchantQueryWrapper = new QueryWrapper<>();
				merchantQueryWrapper.eq("merchant_no", sysUser.getMerchantNo());
				merchantQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				SysMerchant sysMerchant = this.iSysMerchantService.getOne(merchantQueryWrapper, false);
				if(sysMerchant == null)
				{
					throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"租户信息"}));
				}
				if(sysMerchant.getStatus().intValue() == YesNoEnum.NO.getCode().intValue())
				{
					throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.status.forbidden", new String[] {"租户"}));
				}
				if(YesNoEnum.YES.getCode().intValue() == sysMerchant.getIsSystemMerchant().intValue())
				{
					QueryWrapper<SysApi> apiQueryWrapper = new QueryWrapper<SysApi>();
					apiQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					List<SysApi> sysApis = this.iSysApiService.list(apiQueryWrapper);
					if(!ListUtil.isEmpty(sysApis))
					{
						for (int i = 0; i < sysApis.size(); i++) {
							apis.add(sysApis.get(i).getApiCode());
						}
					}
					result.setIsSystemMerchant(YesNoEnum.YES.getCode());
				}else {
					QueryWrapper<SysMerchantAuthTemplateIds> idsQueryWrapper = new QueryWrapper<>();
					idsQueryWrapper.eq("template_id", sysMerchant.getAuthTemplate());
					idsQueryWrapper.eq("auth_type", 2);
					idsQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					List<SysMerchantAuthTemplateIds> merchantAuthTemplateIds = this.iSysMerchantAuthTemplateIdsService.list(idsQueryWrapper);
					if(!ListUtil.isEmpty(merchantAuthTemplateIds))
					{
						List<Long> ids = new ArrayList<>();
						for (int i = 0; i < merchantAuthTemplateIds.size(); i++) {
							ids.add(merchantAuthTemplateIds.get(i).getAuthId());
						}
						QueryWrapper<SysApi> apiQueryWrapper = new QueryWrapper<SysApi>();
						apiQueryWrapper.in("id", ids);
						apiQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
						List<SysApi> sysApis = this.iSysApiService.list(apiQueryWrapper);
						if(!ListUtil.isEmpty(sysApis))
						{
							for (int i = 0; i < sysApis.size(); i++) {
								apis.add(sysApis.get(i).getApiCode());
							}
						}
					}
					result.setIsSystemMerchant(YesNoEnum.NO.getCode());
				}
			}else {
				QueryWrapper<SysApi> apiQueryWrapper = new QueryWrapper<SysApi>();
				apiQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<SysApi> sysApis = this.iSysApiService.list(apiQueryWrapper);
				if(!ListUtil.isEmpty(sysApis))
				{
					for (int i = 0; i < sysApis.size(); i++) {
						apis.add(sysApis.get(i).getApiCode());
					}
				}
			}
		}else {
			result.setIsAdmin(YesNoEnum.NO.getCode());
			result.setMerchantNo(user.getMerchantNo());
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				QueryWrapper<SysMerchant> merchantQueryWrapper = new QueryWrapper<>();
				merchantQueryWrapper.eq("merchant_no", sysUser.getMerchantNo());
				merchantQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				SysMerchant sysMerchant = this.iSysMerchantService.getOne(merchantQueryWrapper, false);
				if(sysMerchant == null)
				{
					throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"租户信息"}));
				}
				if(YesNoEnum.YES.getCode().intValue() == sysMerchant.getIsSystemMerchant().intValue()) {
					result.setIsSystemMerchant(YesNoEnum.YES.getCode());
				}else {
					result.setIsSystemMerchant(YesNoEnum.NO.getCode());
				}
			}
			//获取角色
			QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<SysUserRole>();
			userRoleQueryWrapper.eq("user_id", user.getId());
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				userRoleQueryWrapper.eq("merchant_no", user.getMerchantNo());
			}
			userRoleQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			SysUserRole sysUserRole = this.iSysUserRoleService.getOne(userRoleQueryWrapper,false);
			if(sysUserRole != null)
			{
				SysRole sysRole = this.iSysRoleService.getById(sysUserRole.getRoleId());
				result.setRoleId(sysRole.getId());
				result.setRoleName(sysRole.getRoleName());
				//根据角色获取权限
				JSONObject params = new JSONObject();
				params.put("roleId", sysRole.getId());
				if(this.merchantmode == YesNoEnum.YES.getCode()) {
					params.put("merchantNo", user.getMerchantNo());
				}
				List<SysApi> sysApis = this.iSysRoleApiService.getApisByRole(params);
				if(!ListUtil.isEmpty(sysApis))
				{
					for (int i = 0; i < sysApis.size(); i++) {
						apis.add(sysApis.get(i).getApiCode());
					}
				}
			}
		}
		//获取用户所属部门
		QueryWrapper<SysUserDept> userDeptQueryWrapper = new QueryWrapper<>();
		userDeptQueryWrapper.eq("user_id", user.getId());
		userDeptQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysUserDept sysUserDept = this.iSysUserDeptService.getOne(userDeptQueryWrapper, false);
		if(sysUserDept != null) {
			result.setDeptId(sysUserDept.getDeptId());
		}
		loginLog.setStatus(YesNoEnum.YES.getCode());
		this.iLoginLogService.save(loginLog);
		result.setApis(apis);
		result.setMerchantNo(user.getMerchantNo());
		result.setMerchantMode(merchantmode);
		String token = JWTUtil.sign(result, user.getPassword());
		result.setToken(token);
		return result;
	}

	/**  
	 * @MethodName: getMerchantMode
	 * @Description: 获取当前是否是多租户模式
	 * @author caiyang
	 * @return
	 * @see com.springreport.api.login.ILoginService#getMerchantMode()
	 * @date 2023-12-22 06:20:00 
	 */
	@Override
	public Integer getMerchantMode() {
		return this.merchantmode;
	}

	/**  
	 * @MethodName: getUserInfoByToken
	 * @Description: 根据token获取用户信息
	 * @author caiyang
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.login.ILoginService#getUserInfoByToken(com.springreport.base.UserInfoDto)
	 * @date 2024-09-24 10:16:09 
	 */
	@Override
	public UserInfoDto getUserInfoByToken(UserInfoDto userInfoDto) {
		UserInfoDto result = new UserInfoDto();
		result.setUserName(userInfoDto.getUserName());
		result.setUserRealName(userInfoDto.getUserRealName());
		result.setRoleName(userInfoDto.getRoleName());
		result.setMerchantNo(userInfoDto.getMerchantNo());
		result.setUserId(userInfoDto.getUserId());
		result.setMerchantMode(this.merchantmode);
		result.setIsSystemMerchant(userInfoDto.getIsSystemMerchant());
		List<String> apis = new ArrayList<String>();
		if(YesNoEnum.YES.getCode().intValue() == userInfoDto.getIsAdmin().intValue())
		{
			result.setIsAdmin(YesNoEnum.YES.getCode());
			result.setRoleName("管理员");
			if(this.merchantmode == YesNoEnum.YES.getCode())
			{
				QueryWrapper<SysMerchant> merchantQueryWrapper = new QueryWrapper<>();
				merchantQueryWrapper.eq("merchant_no", userInfoDto.getMerchantNo());
				merchantQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				SysMerchant sysMerchant = this.iSysMerchantService.getOne(merchantQueryWrapper, false);
				if(sysMerchant == null)
				{
					throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"租户信息"}));
				}
				if(sysMerchant.getStatus().intValue() == YesNoEnum.NO.getCode().intValue())
				{
					throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.status.forbidden", new String[] {"租户"}));
				}
				if(YesNoEnum.YES.getCode().intValue() == sysMerchant.getIsSystemMerchant().intValue())
				{
					QueryWrapper<SysApi> apiQueryWrapper = new QueryWrapper<SysApi>();
					apiQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					List<SysApi> sysApis = this.iSysApiService.list(apiQueryWrapper);
					if(!ListUtil.isEmpty(sysApis))
					{
						for (int i = 0; i < sysApis.size(); i++) {
							apis.add(sysApis.get(i).getApiCode());
						}
					}
					result.setIsSystemMerchant(YesNoEnum.YES.getCode());
				}else {
					QueryWrapper<SysMerchantAuthTemplateIds> idsQueryWrapper = new QueryWrapper<>();
					idsQueryWrapper.eq("template_id", sysMerchant.getAuthTemplate());
					idsQueryWrapper.eq("auth_type", 2);
					idsQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					List<SysMerchantAuthTemplateIds> merchantAuthTemplateIds = this.iSysMerchantAuthTemplateIdsService.list(idsQueryWrapper);
					if(!ListUtil.isEmpty(merchantAuthTemplateIds))
					{
						List<Long> ids = new ArrayList<>();
						for (int i = 0; i < merchantAuthTemplateIds.size(); i++) {
							ids.add(merchantAuthTemplateIds.get(i).getAuthId());
						}
						QueryWrapper<SysApi> apiQueryWrapper = new QueryWrapper<SysApi>();
						apiQueryWrapper.in("id", ids);
						apiQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
						List<SysApi> sysApis = this.iSysApiService.list(apiQueryWrapper);
						if(!ListUtil.isEmpty(sysApis))
						{
							for (int i = 0; i < sysApis.size(); i++) {
								apis.add(sysApis.get(i).getApiCode());
							}
						}
					}
					result.setIsSystemMerchant(YesNoEnum.NO.getCode());
				}
			}else {
				QueryWrapper<SysApi> apiQueryWrapper = new QueryWrapper<SysApi>();
				apiQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<SysApi> sysApis = this.iSysApiService.list(apiQueryWrapper);
				if(!ListUtil.isEmpty(sysApis))
				{
					for (int i = 0; i < sysApis.size(); i++) {
						apis.add(sysApis.get(i).getApiCode());
					}
				}
			}
		}else {
			SysRole sysRole = this.iSysRoleService.getById(userInfoDto.getRoleId());
			result.setRoleId(sysRole.getId());
			result.setRoleName(sysRole.getRoleName());
			//根据角色获取权限
			JSONObject params = new JSONObject();
			params.put("roleId", sysRole.getId());
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				params.put("merchantNo", userInfoDto.getMerchantNo());
			}
			List<SysApi> sysApis = this.iSysRoleApiService.getApisByRole(params);
			if(!ListUtil.isEmpty(sysApis))
			{
				for (int i = 0; i < sysApis.size(); i++) {
					apis.add(sysApis.get(i).getApiCode());
				}
			}
		}
		result.setApis(apis);
		return result;
	}

	
}
