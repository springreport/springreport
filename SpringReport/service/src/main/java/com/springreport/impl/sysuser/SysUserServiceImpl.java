package com.springreport.impl.sysuser;

import com.springreport.entity.sysdept.SysDept;
import com.springreport.entity.sysuser.SysUser;
import com.springreport.entity.sysuserdept.SysUserDept;
import com.springreport.entity.sysuserpost.SysUserPost;
import com.springreport.entity.sysuserrole.SysUserRole;
import com.springreport.mapper.sysuser.SysUserMapper;
import com.springreport.api.sysdept.ISysDeptService;
import com.springreport.api.sysuser.ISysUserService;
import com.springreport.api.sysuserdept.ISysUserDeptService;
import com.springreport.api.sysuserpost.ISysUserPostService;
import com.springreport.api.sysuserrole.ISysUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.DateUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.Md5Util;
import com.springreport.util.MessageUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.dto.coedit.UserTreeDto;
import com.springreport.dto.sysuser.SysUserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: SysUser服务实现
* @author 
* @date 2021-06-15 07:12:03
* @version V1.0  
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
  
	@Autowired
	private ISysUserRoleService iSysUserRoleService;
	
	@Autowired
	private ISysDeptService iSysDeptService;
	
	@Autowired
	private ISysUserDeptService iSysUserDeptService;
	
	@Autowired
	private ISysUserPostService iSysUserPostService;
	
	@Value("${merchantmode}")
    private Integer merchantmode;
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(SysUserDto model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		if(model.getDeptId() != null)
		{
			List<Long> deptIds = new ArrayList<>();
			deptIds.add(model.getDeptId());
			List<SysDept> subDepts = new ArrayList<>();
			iSysDeptService.getAllSubDeptIds(model.getDeptId(), subDepts);
			if(!ListUtil.isEmpty(subDepts))
			{
				for (int i = 0; i < subDepts.size(); i++) {
					deptIds.add(subDepts.get(i).getId());
				}
			}
			model.setDeptIds(deptIds);
		}
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<SysUserDto> list = this.baseMapper.getTableList(model);
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
		SysUserDto result = new SysUserDto();
		SysUser sysUser = this.getById(id);
		BeanUtils.copyProperties(sysUser, result);
		QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>();
		queryWrapper.eq("user_id", id);
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", sysUser.getMerchantNo());
		}
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysUserRole sysUserRole = this.iSysUserRoleService.getOne(queryWrapper,false);
		if(sysUserRole != null)
		{
			result.setRoleId(sysUserRole.getRoleId());
		}
		
		QueryWrapper<SysUserDept> deptQueryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			deptQueryWrapper.eq("merchant_no", sysUser.getMerchantNo());
		}
		deptQueryWrapper.eq("user_id", sysUser.getId());
		deptQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysUserDept sysUserDept = this.iSysUserDeptService.getOne(deptQueryWrapper,false);
		if(sysUserDept != null)
		{
			result.setDeptId(sysUserDept.getDeptId());
		}
		QueryWrapper<SysUserPost> postQueryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			postQueryWrapper.eq("merchant_no", sysUser.getMerchantNo());
		}
		postQueryWrapper.eq("user_id", sysUser.getId());
		postQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysUserPost sysUserPost = this.iSysUserPostService.getOne(postQueryWrapper, false);
		if(sysUserPost != null)
		{
			result.setPostId(sysUserPost.getPostId());
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
	public BaseEntity insert(SysUserDto sysUserDto) {
		BaseEntity result = new BaseEntity();
		//校验登录名是否已经存在
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", sysUserDto.getMerchantNo());
		}
		queryWrapper.eq("user_name", sysUserDto.getUserName());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysUser exist = this.getOne(queryWrapper,false);
		if(exist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"用户名"}));
		}
		SysUser model = new SysUser();
		BeanUtils.copyProperties(sysUserDto, model);
		model.setPassword(Md5Util.generateMd5(Md5Util.generateMd5(sysUserDto.getPassword())));
		model.setLastLoginTime(DateUtil.getTimestampLong());
		this.save(model);
		if(YesNoEnum.NO.getCode().intValue() == sysUserDto.getIsAdmin().intValue())
		{
			SysUserRole sysUserRole = new SysUserRole();
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				sysUserRole.setMerchantNo(sysUserDto.getMerchantNo());
			}
			sysUserRole.setUserId(model.getId());
			sysUserRole.setRoleId(sysUserDto.getRoleId());
			this.iSysUserRoleService.save(sysUserRole);
		}
		SysUserDept sysUserDept = new SysUserDept();
		sysUserDept.setUserId(model.getId());
		sysUserDept.setDeptId(sysUserDto.getDeptId());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			sysUserDept.setMerchantNo(sysUserDto.getMerchantNo());
		}
		this.iSysUserDeptService.save(sysUserDept);
		
		SysUserPost sysUserPost = new SysUserPost();
		sysUserPost.setUserId(model.getId());
		sysUserPost.setPostId(sysUserDto.getPostId());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			sysUserPost.setMerchantNo(sysUserDto.getMerchantNo());
		}
		this.iSysUserPostService.save(sysUserPost);
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
	public BaseEntity update(SysUserDto sysUserDto) {
		BaseEntity result = new BaseEntity();
		sysUserDto.setPassword(null);
		//校验登录名是否已经存在
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
		queryWrapper.ne("id", sysUserDto.getId());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", sysUserDto.getMerchantNo());
		}
		queryWrapper.eq("user_name", sysUserDto.getUserName());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysUser exist = this.getOne(queryWrapper,false);
		if(exist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"用户名"}));
		}
		SysUser model = new SysUser();
		BeanUtils.copyProperties(sysUserDto, model);
		this.updateById(model);
		//获取用户角色
		if(YesNoEnum.NO.getCode().intValue() == sysUserDto.getIsAdmin().intValue())
		{
			QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				userRoleQueryWrapper.eq("merchant_no", model.getMerchantNo());
			}
			userRoleQueryWrapper.eq("user_id", sysUserDto.getId());
			userRoleQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			SysUserRole sysUserRole = this.iSysUserRoleService.getOne(userRoleQueryWrapper, false);
			if(sysUserRole == null)
			{
				sysUserRole = new SysUserRole();
				if(this.merchantmode == YesNoEnum.YES.getCode()) {
					sysUserRole.setMerchantNo(sysUserDto.getMerchantNo());
				}
				sysUserRole.setUserId(model.getId());
				sysUserRole.setRoleId(sysUserDto.getRoleId());
				this.iSysUserRoleService.save(sysUserRole);
			}else {
				UpdateWrapper<SysUserRole> updateWrapper = new UpdateWrapper<SysUserRole>();
				updateWrapper.eq("user_id", sysUserDto.getId());
				if(this.merchantmode == YesNoEnum.YES.getCode()) {
					updateWrapper.eq("merchant_no", sysUserDto.getMerchantNo());
				}
				updateWrapper.eq("del_flag",DelFlagEnum.UNDEL.getCode());
				SysUserRole update = new SysUserRole();
				update.setRoleId(sysUserDto.getRoleId());
				this.iSysUserRoleService.update(update, updateWrapper);
			}
		}else {
			//管理员删除用户角色
			UpdateWrapper<SysUserRole> updateWrapper = new UpdateWrapper<SysUserRole>();
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				updateWrapper.eq("merchant_no", sysUserDto.getMerchantNo());
			}
			updateWrapper.eq("user_id", sysUserDto.getId());
			updateWrapper.eq("del_flag",DelFlagEnum.UNDEL.getCode());
			SysUserRole update = new SysUserRole();
			update.setDelFlag(DelFlagEnum.DEL.getCode());
			this.iSysUserRoleService.update(update, updateWrapper);
		}
		//用户部门
		QueryWrapper<SysUserDept> deptQueryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			deptQueryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		deptQueryWrapper.eq("user_id", sysUserDto.getId());
		deptQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysUserDept sysUserDept = this.iSysUserDeptService.getOne(deptQueryWrapper,false);
		if(sysUserDept == null)
		{
			sysUserDept = new SysUserDept();
			sysUserDept.setUserId(sysUserDto.getId());
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				sysUserDept.setMerchantNo(sysUserDto.getMerchantNo());
			}
			sysUserDept.setDeptId(sysUserDto.getDeptId());
			this.iSysUserDeptService.save(sysUserDept);
		}else {
			UpdateWrapper<SysUserDept> deptUpdateWrapper = new UpdateWrapper<>();
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				deptUpdateWrapper.eq("merchant_no", sysUserDto.getMerchantNo());
			}
			deptUpdateWrapper.eq("user_id", sysUserDto.getId());
			deptUpdateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			SysUserDept deptUpdate = new SysUserDept();
			deptUpdate.setDeptId(sysUserDto.getDeptId());
			this.iSysUserDeptService.update(deptUpdate, deptUpdateWrapper);
		}
		//用户岗位
		QueryWrapper<SysUserPost> postQueryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			postQueryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		postQueryWrapper.eq("user_id", sysUserDto.getId());
		postQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysUserPost sysUserPost = this.iSysUserPostService.getOne(postQueryWrapper, false);
		if(sysUserPost == null)
		{
			sysUserPost = new SysUserPost();
			sysUserPost.setUserId(sysUserDto.getId());
			sysUserPost.setPostId(sysUserDto.getPostId());
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				sysUserPost.setMerchantNo(sysUserDto.getMerchantNo());
			}
			this.iSysUserPostService.save(sysUserPost);
		}else {
			UpdateWrapper<SysUserPost> postUpdateWrapper = new UpdateWrapper<>();
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				postUpdateWrapper.eq("merchant_no", sysUserDto.getMerchantNo());
			}
			postUpdateWrapper.eq("user_id", sysUserDto.getId());
			postUpdateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			SysUserPost postUpdate = new SysUserPost();
			postUpdate.setPostId(sysUserDto.getPostId());
			this.iSysUserPostService.update(postUpdate, postUpdateWrapper);
		}
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
		SysUser sysUser = new SysUser();
		sysUser.setId(id);
		sysUser.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(sysUser);
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
		List<SysUser> list = new ArrayList<SysUser>();
		for (int i = 0; i < ids.size(); i++) {
			SysUser sysUser = new SysUser();
			sysUser.setId(ids.get(i));
			sysUser.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(sysUser);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @MethodName: resetPwd
	 * @Description: 重置密码
	 * @author caiyang
	 * @param sysUser
	 * @return
	 * @see com.springreport.api.sysuser.ISysUserService#resetPwd(com.springreport.entity.sysuser.SysUser)
	 * @date 2023-12-29 01:13:49 
	 */
	@Override
	public BaseEntity resetPwd(SysUser sysUser) {
		BaseEntity result = new BaseEntity();
		SysUser update = new SysUser();
		update.setId(sysUser.getId());
		update.setPassword(Md5Util.generateMd5(Md5Util.generateMd5(Constants.DEFALUT_PASSWORD)));
		this.updateById(update);
		result.setStatusMsg(MessageUtil.getValue("info.operate.success"));
		return result;
	}


	/**  
	 * @MethodName: changePwd
	 * @Description: 用户修改密码
	 * @author caiyang
	 * @param sysUserDto
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.sysuser.ISysUserService#changePwd(com.springreport.dto.sysuser.SysUserDto, com.springreport.base.UserInfoDto)
	 * @date 2023-12-29 02:56:29 
	 */
	@Override
	public BaseEntity changePwd(SysUserDto sysUserDto, UserInfoDto userInfoDto) {
		BaseEntity result = new BaseEntity();
		SysUser sysUser = this.getById(userInfoDto.getUserId());
		if(sysUser == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"用户"}));
		}
		if(!sysUser.getPassword().equals(Md5Util.generateMd5(Md5Util.generateMd5(sysUserDto.getOldPwd()))))
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.oldpwd"));
		}
		SysUser update = new SysUser();
		update.setId(sysUser.getId());
		update.setPassword(Md5Util.generateMd5(Md5Util.generateMd5(sysUserDto.getNewPwd())));
		this.updateById(update);
		result.setStatusMsg(MessageUtil.getValue("info.operate.success"));
		return result;
	}


	/**  
	 * @MethodName: getUsers
	 * @Description: 获取所有用户
	 * @author caiyang
	 * @param sysUserDto
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.sysuser.ISysUserService#getUsers(com.springreport.dto.sysuser.SysUserDto, com.springreport.base.UserInfoDto)
	 * @date 2024-03-02 11:31:41 
	 */
	@Override
	public List<SysUser> getUsers(SysUserDto sysUserDto, UserInfoDto userInfoDto) {
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", sysUserDto.getMerchantNo());
		}
		queryWrapper.ne("id", userInfoDto.getUserId());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<SysUser> result = this.list(queryWrapper);
		return result;
	}


	@Override
	public List<UserTreeDto> getDeptUserTree(SysUser model) {
		List<UserTreeDto> resultList = new ArrayList<UserTreeDto>();
		List<UserTreeDto> list = new ArrayList<UserTreeDto>();
		QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<SysDept> depts = this.iSysDeptService.list(queryWrapper);
		if(ListUtil.isNotEmpty(depts)) {
			UserTreeDto userTreeDto = null;
			for (int i = 0; i < depts.size(); i++) {
				userTreeDto = new UserTreeDto();
				userTreeDto.setId(depts.get(i).getId()+"_dept");
				userTreeDto.setName(depts.get(i).getDeptName());
				userTreeDto.setParentId(depts.get(i).getParentId()+"_dept");
				list.add(userTreeDto);
			}
		}
		List<UserTreeDto> users = this.baseMapper.getdeptusers(model);
		if(ListUtil.isNotEmpty(users)) {
			for (int i = 0; i < users.size(); i++) {
				users.get(i).setParentId(users.get(i).getParentId()+"_dept");
				list.add(users.get(i));
			}
		}
		
		if(!ListUtil.isEmpty(list))
		{
			Map<String, UserTreeDto> entityMap = new HashMap<>();
			Map<String, List<UserTreeDto>> childrenMap = new HashMap<>();
			for (UserTreeDto entity : list){
				entity.setChildren(new ArrayList<>());
				entityMap.put(entity.getId(),entity);
				childrenMap.put(entity.getId(), (List<UserTreeDto>) entity.getChildren());
			}
			// 组装成数结构列表
			for (UserTreeDto entity : list){
				UserTreeDto parentEntity = entityMap.get(entity.getParentId());
				// 如果查询出的列表里面该节点的没有父节点说明是顶级节点
				if(parentEntity == null)
				{
					// 将顶级节点加入结果集中
			         resultList.add(entity);
			         continue;
				}
				// 把自己加到父节点对象里面去
				childrenMap.get(parentEntity.getId()).add(entity);
			}
		}
		return resultList;
	}
}