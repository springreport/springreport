package com.springreport.impl.reportrangeauthuser;

import com.springreport.entity.onlinetpl.OnlineTpl;
import com.springreport.entity.reportrangeauth.ReportRangeAuth;
import com.springreport.entity.reportrangeauthuser.ReportRangeAuthUser;
import com.springreport.entity.sysuser.SysUser;
import com.springreport.mapper.reportrangeauthuser.ReportRangeAuthUserMapper;
import com.springreport.api.onlinetpl.IOnlineTplService;
import com.springreport.api.reportrangeauth.IReportRangeAuthService;
import com.springreport.api.reportrangeauthuser.IReportRangeAuthUserService;
import com.springreport.api.sysuser.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.reportRangeAuthUser.RangeAuthUserDto;
import com.springreport.dto.sysuser.SysUserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;

 /**  
* @Description: ReportRangeAuthUser服务实现
* @author 
* @date 2024-03-03 05:52:17
* @version V1.0  
 */
@Service
public class ReportRangeAuthUserServiceImpl extends ServiceImpl<ReportRangeAuthUserMapper, ReportRangeAuthUser> implements IReportRangeAuthUserService {
  
	@Autowired
	private ISysUserService iSysUserService;
	
	@Autowired
	@Lazy
	private IOnlineTplService iOnlineTplService;
	
	@Autowired
	private IReportRangeAuthService iReportRangeAuthService;
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(ReportRangeAuthUser model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<ReportRangeAuthUser> list = this.baseMapper.searchDataLike(model);
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
		return this.getById(id);
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
	public BaseEntity insert(ReportRangeAuthUser model) {
		BaseEntity result = new BaseEntity();
		this.save(model);
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
	public BaseEntity update(ReportRangeAuthUser model) {
		BaseEntity result = new BaseEntity();
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
		ReportRangeAuthUser reportRangeAuthUser = new ReportRangeAuthUser();
		reportRangeAuthUser.setId(id);
		reportRangeAuthUser.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(reportRangeAuthUser);
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
		List<ReportRangeAuthUser> list = new ArrayList<ReportRangeAuthUser>();
		for (int i = 0; i < ids.size(); i++) {
			ReportRangeAuthUser reportRangeAuthUser = new ReportRangeAuthUser();
			reportRangeAuthUser.setId(ids.get(i));
			reportRangeAuthUser.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(reportRangeAuthUser);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @MethodName: getRangeUsers
	 * @Description: 获取用户和对应的授权
	 * @author caiyang
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.reportrangeauthuser.IReportRangeAuthUserService#getRangeUsers(com.springreport.base.UserInfoDto)
	 * @date 2024-06-05 11:31:00 
	 */
	@Override
	public List<RangeAuthUserDto> getRangeUsers(UserInfoDto userInfoDto) {
		List<RangeAuthUserDto> result = new ArrayList<>();
		SysUserDto sysUserDto = new SysUserDto();
		sysUserDto.setMerchantNo(userInfoDto.getMerchantNo());
		List<SysUser> sysUsers = this.iSysUserService.getUsers(sysUserDto, userInfoDto);
		if(ListUtil.isNotEmpty(sysUsers)) {
			RangeAuthUserDto rangeAuthUserDto = null;
			for (int i = 0; i < sysUsers.size(); i++) {
				rangeAuthUserDto = new RangeAuthUserDto();
				rangeAuthUserDto.setId(sysUsers.get(i).getId());
				rangeAuthUserDto.setUserName(sysUsers.get(i).getUserName());
				result.add(rangeAuthUserDto);
			}
		}
		return result;
	}


	/**  
	 * @MethodName: getUserNoAuthRange
	 * @Description: 获取用户没有查看权限的授权
	 * @author caiyang
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.reportrangeauthuser.IReportRangeAuthUserService#getUserNoAuthRange(com.springreport.base.UserInfoDto)
	 * @date 2024-06-10 08:04:18 
	 */
	@Override
	public Map<String, List<ReportRangeAuth>> getUserNoAuthRange(String listId,UserInfoDto userInfoDto) {
		Map<String, List<ReportRangeAuth>> result = null;
		QueryWrapper<OnlineTpl> onlineTplQueryWrapper = new QueryWrapper<>();
		onlineTplQueryWrapper.eq("list_id", listId);
		onlineTplQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		OnlineTpl onlineTpl = iOnlineTplService.getOne(onlineTplQueryWrapper, false);
		if(onlineTpl != null) {
			QueryWrapper<ReportRangeAuthUser> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("tpl_id", onlineTpl.getId());
			queryWrapper.eq("user_id", userInfoDto.getUserId());
			queryWrapper.eq("auth_type", 2);
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<ReportRangeAuthUser> rangeAuthUsers = this.list(queryWrapper);
			if(ListUtil.isNotEmpty(rangeAuthUsers)) {
				List<Long> rangeAuthIds = new ArrayList<>();
				for (int i = 0; i < rangeAuthUsers.size(); i++) {
					rangeAuthIds.add(rangeAuthUsers.get(i).getRangeAuthId());
				}
				List<ReportRangeAuth> auths = this.iReportRangeAuthService.listByIds(rangeAuthIds);
				if(ListUtil.isNotEmpty(auths)) {
					result = auths.stream().collect(Collectors.groupingBy(ReportRangeAuth::getSheetIndex));
				}
			}
		}
		return result;
	}
}