package com.springreport.impl.sysrole;

import com.springreport.entity.sysrole.SysRole;
import com.springreport.entity.sysroleapi.SysRoleApi;
import com.springreport.entity.sysrolemenu.SysRoleMenu;
import com.springreport.mapper.sysrole.SysRoleMapper;
import com.springreport.api.sysrole.ISysRoleService;
import com.springreport.api.sysroleapi.ISysRoleApiService;
import com.springreport.api.sysrolemenu.ISysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.constants.StatusCode;
import com.springreport.dto.sysrole.MesAuthDto;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: SysRole服务实现
* @author 
* @date 2021-06-15 07:11:49
* @version V1.0  
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
  
	@Autowired
	private ISysRoleMenuService iSysRoleMenuService;
	
	@Autowired
	private ISysRoleApiService iSysRoleApiService;
	
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
	public PageEntity tablePagingQuery(SysRole model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<SysRole> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(SysRole model) {
		BaseEntity result = new BaseEntity();
		QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("role_code", model.getRoleCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		SysRole isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该角色编码"}));
		}
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
	public BaseEntity update(SysRole model) {
		BaseEntity result = new BaseEntity();
		QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("id", model.getId());
		queryWrapper.eq("role_code", model.getRoleCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		SysRole isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该角色编码"}));
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
		SysRole sysRole = new SysRole();
		sysRole.setId(id);
		sysRole.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(sysRole);
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
		List<SysRole> list = new ArrayList<SysRole>();
		for (int i = 0; i < ids.size(); i++) {
			SysRole sysRole = new SysRole();
			sysRole.setId(ids.get(i));
			sysRole.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(sysRole);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @Title: getRoles
	 * @Description: 获取角色
	 * @return 
	 * @see com.caiyang.api.sysrole.ISysRoleService#getRoles() 
	 * @author caiyang
	 * @date 2021-06-15 01:17:48 
	 */
	@Override
	public List<SysRole> getRoles(SysRole sysRole) {
		QueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", sysRole.getMerchantNo());
		}
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<SysRole> result = this.list(queryWrapper);
		return result;
	}


	/**  
	 * @Title: authed
	 * @Description: 角色菜单功能授权
	 * @param mesAuthDto
	 * @return 
	 * @see com.caiyang.api.sysrole.ISysRoleService#authed(com.caiyang.dto.sysrole.MesAuthDto) 
	 * @author caiyang
	 * @date 2021-06-16 07:02:35 
	 */
	@Override
	public BaseEntity authed(MesAuthDto mesAuthDto) {
		BaseEntity result = new BaseEntity();
		if(ListUtil.isEmpty(mesAuthDto.getAuthed()))
		{
			UpdateWrapper<SysRoleApi> apiUpdateWrapper = new UpdateWrapper<SysRoleApi>();
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				apiUpdateWrapper.eq("merchant_no", mesAuthDto.getMerchantNo());
			}
			apiUpdateWrapper.eq("role_id", mesAuthDto.getRoleId());
			apiUpdateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			SysRoleApi apiUpdate = new SysRoleApi();
			apiUpdate.setDelFlag(DelFlagEnum.DEL.getCode());
			this.iSysRoleApiService.update(apiUpdate, apiUpdateWrapper);
			UpdateWrapper<SysRoleMenu> menuUpdateWrapper = new UpdateWrapper<SysRoleMenu>();
			menuUpdateWrapper.eq("role_id", mesAuthDto.getRoleId());
			menuUpdateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				menuUpdateWrapper.eq("merchant_no", mesAuthDto.getMerchantNo());
			}
			SysRoleMenu menuUpdate = new SysRoleMenu();
			menuUpdate.setDelFlag(DelFlagEnum.DEL.getCode());
			this.iSysRoleMenuService.update(menuUpdate, menuUpdateWrapper);
		}else {
			//获取当前用户的菜单权限和功能权限
			QueryWrapper<SysRoleMenu> roleMenuQueryWrapper = new QueryWrapper<SysRoleMenu>();
			roleMenuQueryWrapper.eq("role_id", mesAuthDto.getRoleId());
			roleMenuQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				roleMenuQueryWrapper.eq("merchant_no", mesAuthDto.getMerchantNo());
			}
			List<SysRoleMenu> menus = this.iSysRoleMenuService.list(roleMenuQueryWrapper);
			QueryWrapper<SysRoleApi> roleApiQueryWrapper = new QueryWrapper<SysRoleApi>();
			roleApiQueryWrapper.eq("role_id", mesAuthDto.getRoleId());
			roleApiQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				roleApiQueryWrapper.eq("merchant_no", mesAuthDto.getMerchantNo());
			}
			List<SysRoleApi> apis = this.iSysRoleApiService.list(roleApiQueryWrapper);
			List<String> userAuth = new ArrayList<String>();//用户当前拥有的权限
			if (menus != null && menus.size() > 0) {
				for (int i = 0; i < menus.size(); i++) {
					userAuth.add(String.valueOf(menus.get(i).getMenuId()));
				}
			}
			if (apis != null && apis.size() > 0) {
				for (int i = 0; i < apis.size(); i++) {
					userAuth.add("api-"+ apis.get(i).getApiId());
				}
			}
			//用户拥有的权限和新授权的权限对比，得到需要添加和删除的权限
			List<String> addData = ListUtil.getDiffData(mesAuthDto.getAuthed(), userAuth);//需要添加的权限
			List<String> delData = ListUtil.getDiffData(userAuth, mesAuthDto.getAuthed());//需要删除的权限
			List<SysRoleMenu> addMenus = new ArrayList<SysRoleMenu>();//新增加的菜单权限
			List<SysRoleApi> addApis = new ArrayList<SysRoleApi>();//新增加的功能权限
			if (!ListUtil.isEmpty(delData)) {
				for (int i = 0; i < delData.size(); i++) {
					if (delData.get(i).contains("api-")) {//功能
						UpdateWrapper<SysRoleApi> updateWrapper = new UpdateWrapper<SysRoleApi>();
						if(this.merchantmode == YesNoEnum.YES.getCode()) {
							updateWrapper.eq("merchant_no", mesAuthDto.getMerchantNo());
						}
						updateWrapper.eq("role_id", mesAuthDto.getRoleId());
						updateWrapper.eq("api_id", delData.get(i).split("-")[1]);
						updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
						SysRoleApi update = new SysRoleApi();
						update.setDelFlag(DelFlagEnum.DEL.getCode());
						this.iSysRoleApiService.update(update, updateWrapper);
					}else {//菜单
						UpdateWrapper<SysRoleMenu> updateWrapper = new UpdateWrapper<SysRoleMenu>();
						if(this.merchantmode == YesNoEnum.YES.getCode()) {
							updateWrapper.eq("merchant_no", mesAuthDto.getMerchantNo());
						}
						updateWrapper.eq("role_id", mesAuthDto.getRoleId());
						updateWrapper.eq("menu_id", delData.get(i));
						updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
						SysRoleMenu update = new SysRoleMenu();
						update.setDelFlag(DelFlagEnum.DEL.getCode());
						this.iSysRoleMenuService.update(update, updateWrapper);
					}
				}
			}
			if (!ListUtil.isEmpty(addData)) {
				for (int i = 0; i < addData.size(); i++) {
					if (addData.get(i).contains("api-")) {//功能
						SysRoleApi sysRoleApi = new SysRoleApi();
						if(this.merchantmode == YesNoEnum.YES.getCode()) {
							sysRoleApi.setMerchantNo(mesAuthDto.getMerchantNo());
						}
						sysRoleApi.setRoleId(mesAuthDto.getRoleId());
						sysRoleApi.setApiId(Long.valueOf(addData.get(i).split("-")[1]));
						addApis.add(sysRoleApi);
					}else {//菜单
						SysRoleMenu sysRoleMenu = new SysRoleMenu();
						sysRoleMenu.setRoleId(mesAuthDto.getRoleId());
						if(this.merchantmode == YesNoEnum.YES.getCode()) {
							sysRoleMenu.setMerchantNo(mesAuthDto.getMerchantNo());
						}
						sysRoleMenu.setMenuId(Long.valueOf(addData.get(i)));
						addMenus.add(sysRoleMenu);
					}
				}
			}
			if(!ListUtil.isEmpty(addApis))
			{
				this.iSysRoleApiService.saveBatch(addApis);
			}
			if(!ListUtil.isEmpty(addMenus))
			{
				this.iSysRoleMenuService.saveBatch(addMenus);
			}
		}
		
		result.setStatusMsg(MessageUtil.getValue("info.role.auth"));
		return result;
	}
	
	
}