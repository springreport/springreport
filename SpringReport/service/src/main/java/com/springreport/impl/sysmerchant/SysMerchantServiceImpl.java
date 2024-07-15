package com.springreport.impl.sysmerchant;

import com.springreport.entity.sysapi.SysApi;
import com.springreport.entity.sysmerchant.SysMerchant;
import com.springreport.entity.sysmerchantauthtemplate.SysMerchantAuthTemplate;
import com.springreport.entity.sysmerchantauthtemplateids.SysMerchantAuthTemplateIds;
import com.springreport.entity.sysuser.SysUser;
import com.springreport.mapper.sysmerchant.SysMerchantMapper;
import com.springreport.api.sysapi.ISysApiService;
import com.springreport.api.sysmerchant.ISysMerchantService;
import com.springreport.api.sysmerchantauthtemplate.ISysMerchantAuthTemplateService;
import com.springreport.api.sysmerchantauthtemplateids.ISysMerchantAuthTemplateIdsService;
import com.springreport.api.sysuser.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.Md5Util;
import com.springreport.util.MessageUtil;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.dto.sysmerchant.SysMerchantDto;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: SysMerchant服务实现
* @author 
* @date 2023-12-22 05:18:53
* @version V1.0  
 */
@Service
public class SysMerchantServiceImpl extends ServiceImpl<SysMerchantMapper, SysMerchant> implements ISysMerchantService {
  
	@Autowired
	private ISysUserService iSysUserService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private ISysMerchantAuthTemplateService iSysMerchantAuthTemplateService;
	
	@Autowired
	private ISysMerchantAuthTemplateIdsService iSysMerchantAuthTemplateIdsService;
	
	@Autowired
	private ISysApiService iSysApiService;
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(SysMerchant model) {
		PageEntity result = new PageEntity();
		model.setIsSystemMerchant(YesNoEnum.NO.getCode());
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<SysMerchant> list = this.baseMapper.searchDataLike(model);
		result.setData(list);
		result.setTotal(page.getTotal());
		result.setCurrentPage(model.getCurrentPage());
		result.setPageSize(model.getPageSize());
		return result;
	}


	/**
	*<p>Title: getDetail</p>
	*<p>Description: 获取详情</p>
	* @author 
	* @param id
	* @return
	*/
	@Override
	public BaseEntity getDetail(Long id) {
		SysMerchant merchant = this.getById(id);
		SysMerchantDto result = new SysMerchantDto();
		BeanUtils.copyProperties(merchant, result);
		SysMerchantAuthTemplate merchantAuthTemplate = this.iSysMerchantAuthTemplateService.getById(result.getAuthTemplate());
		if(merchantAuthTemplate == null || merchantAuthTemplate.getDelFlag().intValue() == DelFlagEnum.DEL.getCode().intValue())
		{
			result.setAuthTemplate(null);
		}
		return result;
	}

	/**
	*<p>Title: insert</p>
	*<p>Description: 新增数据</p>
	* @author 
	* @param model
	* @return
	*/
	@Transactional
	@Override
	public BaseEntity insert(SysMerchantDto model) {
		BaseEntity result = new BaseEntity();
		QueryWrapper<SysMerchant> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("merchant_name", model.getMerchantName().trim());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysMerchant isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"租户名称"}));
		}
		queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("phone", model.getPhone().trim());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"电话"}));
		}
		String merchantNo = StringUtil.getSequenceNo(Constants.MERCHANT_SEQ_PRE, redisUtil.increment(RedisPrefixEnum.SEQUENCE.getCode()+Constants.MERCHANT_SEQ_PRE), 8);
		model.setMerchantNo(merchantNo);
		this.save(model);
		SysUser sysUser = new SysUser();
		sysUser.setMerchantNo(merchantNo);
		sysUser.setUserName(model.getUserName());
		sysUser.setUserRealName(model.getMerchantName());
		sysUser.setUserEmail(model.getEmail());
		sysUser.setUserMobile(model.getPhone());
		sysUser.setIsAdmin(YesNoEnum.YES.getCode());
		sysUser.setPassword(Md5Util.generateMd5(model.getPassword()));
		this.iSysUserService.save(sysUser);
		result.setStatusMsg(MessageUtil.getValue("info.insert"));
		return result;
	}

	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author 
	* @param model
	* @return
	*/
	@Transactional
	@Override
	public BaseEntity update(SysMerchantDto model) {
		BaseEntity result = new BaseEntity();
		QueryWrapper<SysMerchant> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("id", model.getId());
		queryWrapper.eq("merchant_name", model.getMerchantName().trim());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysMerchant isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"租户名称"}));
		}
		queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("id", model.getId());
		queryWrapper.eq("phone", model.getPhone().trim());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"电话"}));
		}
		this.updateById(model);
		result.setStatusMsg(MessageUtil.getValue("info.update"));
		return result;
	}

	/**
	*<p>Title: delete</p>
	*<p>Description: 单条删除数据</p>
	* @author 
	* @param model
	* @return
	*/
	@Transactional
	@Override
	public BaseEntity delete(Long id) {
		SysMerchant sysMerchant = new SysMerchant();
		sysMerchant.setId(id);
		sysMerchant.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(sysMerchant);
		BaseEntity result = new BaseEntity();
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}

	/**
	*<p>Title: deleteBatch</p>
	*<p>Description: 批量删除数据</p>
	* @author 
	* @param list
	* @return
	*/
	@Transactional
	@Override
	public BaseEntity deleteBatch(List<Long> ids) {
		List<SysMerchant> list = new ArrayList<SysMerchant>();
		for (int i = 0; i < ids.size(); i++) {
			SysMerchant sysMerchant = new SysMerchant();
			sysMerchant.setId(ids.get(i));
			sysMerchant.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(sysMerchant);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @MethodName: getMerchantList
	 * @Description: 获取所有的商户列表
	 * @author caiyang
	 * @return
	 * @see com.springreport.api.sysmerchant.ISysMerchantService#getMerchantList()
	 * @date 2023-12-23 11:33:16 
	 */
	@Override
	public List<SysMerchant> getMerchantList() {
		QueryWrapper<SysMerchant> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<SysMerchant> result = this.list(queryWrapper);
		return result;
	}


	/**  
	 * @MethodName: getMerchantAuthApi
	 * @Description: 获取商户有权限的按钮权限
	 * @author caiyang
	 * @param merchant
	 * @return
	 * @see com.springreport.api.sysmerchant.ISysMerchantService#getMerchantAuthApi(com.springreport.entity.sysmerchant.SysMerchant)
	 * @date 2023-12-27 10:22:10 
	 */
	@Override
	public List<Long> getMerchantAuthApi(SysMerchant merchant) {
		List<Long> merchantAuthIds = null;//商户拥有的全部权限id
		if(merchant == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"商户"}));
		}
		if(merchant.getIsSystemMerchant().intValue() == YesNoEnum.YES.getCode().intValue())
		{
			QueryWrapper<SysApi> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<SysApi> apis = this.iSysApiService.list(queryWrapper);
			if(!ListUtil.isEmpty(apis))
			{
				merchantAuthIds = new ArrayList<Long>();
				for (int i = 0; i < apis.size(); i++) {
					merchantAuthIds.add(apis.get(i).getId());
				}
			}
		}else {
			SysMerchantAuthTemplate authTemplate = this.iSysMerchantAuthTemplateService.getById(merchant.getAuthTemplate());
			if(authTemplate != null && authTemplate.getDelFlag().intValue() == DelFlagEnum.UNDEL.getCode().intValue())
			{
				QueryWrapper<SysMerchantAuthTemplateIds> templateIdsQueryWrapper = new QueryWrapper<>();
				templateIdsQueryWrapper.eq("template_id", merchant.getAuthTemplate());
				templateIdsQueryWrapper.eq("auth_type", 2);
				templateIdsQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<SysMerchantAuthTemplateIds> authTemplateIds = this.iSysMerchantAuthTemplateIdsService.list(templateIdsQueryWrapper);
				if(!ListUtil.isEmpty(authTemplateIds)) {
					merchantAuthIds = new ArrayList<Long>();
					for (int i = 0; i < authTemplateIds.size(); i++) {
						merchantAuthIds.add(authTemplateIds.get(i).getAuthId());
					}
				}
			}
		}
		return merchantAuthIds;
	}
}