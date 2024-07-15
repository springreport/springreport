package com.springreport.impl.sysmenu;

import com.springreport.entity.sysmenu.SysMenu;
import com.springreport.entity.sysmerchant.SysMerchant;
import com.springreport.entity.sysrole.SysRole;
import com.springreport.entity.sysroleapi.SysRoleApi;
import com.springreport.entity.sysrolemenu.SysRoleMenu;
import com.springreport.mapper.sysmenu.SysMenuMapper;
import com.springreport.api.sysapi.ISysApiService;
import com.springreport.api.sysmenu.ISysMenuService;
import com.springreport.api.sysmerchant.ISysMerchantService;
import com.springreport.api.sysroleapi.ISysRoleApiService;
import com.springreport.api.sysrolemenu.ISysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.StringUtil;
import com.springreport.base.BaseEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.StatusCode;
import com.springreport.dto.sysmenu.AuthTreeDto;
import com.springreport.dto.sysmenu.IndexMenuTreeDto;
import com.springreport.dto.sysmenu.MenuTreeDto;
import com.springreport.dto.sysmenu.MesGetAuthTreeDto;
import com.springreport.dto.sysmenu.MesGetIndexMenuDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.SqlOrderEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: SysMenu服务实现
* @author 
* @date 2021-06-15 07:11:44
* @version V1.0  
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
  
	@Autowired
	private ISysRoleApiService iSysRoleApiService;
	
	@Autowired
	private ISysApiService iSysApiService;
	
	@Autowired
	private ISysRoleMenuService iSysRoleMenuService;
	
	@Autowired
	private ISysMerchantService iSysMerchantService;
	
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
	public List<SysMenu> tablePagingQuery(SysMenu model) {
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("sort", SqlOrderEnum.ASC.getCode());
		map.put("id", SqlOrderEnum.ASC.getCode());
		String orderSql = StringUtil.formatOrderSql(map);
		model.setOrderSql(orderSql);
		List<SysMenu> list = this.baseMapper.searchDataLike(model);
		List<SysMenu> resultList = new ArrayList<>();
		if(!ListUtil.isEmpty(list))
		{
			Map<Long, SysMenu> entityMap = new HashMap<>();
			Map<Long, List<SysMenu>> childrenMap = new HashMap<>();
			for (SysMenu entity : list){
				entity.setChildren(new ArrayList<>());
				entityMap.put(entity.getId(),entity);
				childrenMap.put(entity.getId(), (List<SysMenu>) entity.getChildren());
			}
			// 组装成数结构列表
			for (SysMenu entity : list){
				SysMenu parentEntity = entityMap.get(entity.getParentMenuId());
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
	public BaseEntity insert(SysMenu model) {
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
	public BaseEntity update(SysMenu model) {
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
		SysMenu sysMenu = new SysMenu();
		sysMenu.setId(id);
		sysMenu.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(sysMenu);
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
		List<SysMenu> list = new ArrayList<SysMenu>();
		for (int i = 0; i < ids.size(); i++) {
			SysMenu sysMenu = new SysMenu();
			sysMenu.setId(ids.get(i));
			sysMenu.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(sysMenu);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @Title: getParentMenus
	 * @Description: 获取上级菜单
	 * @param sysMenu
	 * @return 
	 * @see com.caiyang.api.sysmenu.ISysMenuService#getParentMenus(com.caiyang.entity.sysmenu.SysMenu) 
	 * @author caiyang
	 * @date 2021-06-15 04:34:55 
	 */
	@Override
	public List<SysMenu> getParentMenus(SysMenu sysMenu) {
		QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<SysMenu>();
		if(sysMenu.getId() != null)
		{
			queryWrapper.ne("id", sysMenu.getId());
		}
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<SysMenu> result = this.list(queryWrapper);
		return result;
	}


	/**  
	 * @Title: getAuthTree
	 * @Description: 获取权限树
	 * @param sysRole
	 * @param userInfoDto
	 * @return 
	 * @see com.caiyang.api.sysmenu.ISysMenuService#getAuthTree(com.caiyang.entity.sysrole.SysRole, com.caiyang.base.UserInfoDto) 
	 * @author caiyang
	 * @date 2021-06-15 06:29:01 
	 */
	@Override
	public AuthTreeDto getAuthTree(SysRole sysRole, UserInfoDto userInfoDto) {
		AuthTreeDto authTreeDto = new AuthTreeDto();
		List<String> authed = new ArrayList<String>();//已经授权的功能
		//获取角色已经授权的功能
		QueryWrapper<SysRoleApi> roleApiQueryWrapper = new QueryWrapper<SysRoleApi>();
		roleApiQueryWrapper.eq("role_id", sysRole.getId());
		roleApiQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			roleApiQueryWrapper.eq("merchant_no", sysRole.getMerchantNo());
		}
		List<SysRoleApi> roleApis = this.iSysRoleApiService.list(roleApiQueryWrapper);
		List<Long> merchantAuthIds = null;//商户拥有的全部权限id
		SysMerchant merchant = null;
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			QueryWrapper<SysMerchant> merchantQueryWrapper = new QueryWrapper<>();
			merchantQueryWrapper.eq("merchant_no", sysRole.getMerchantNo());
			merchantQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			merchant = this.iSysMerchantService.getOne(merchantQueryWrapper, false);
			merchantAuthIds = this.iSysMerchantService.getMerchantAuthApi(merchant);
		}
		if(!ListUtil.isEmpty(roleApis))
		{
			for (int i = 0; i < roleApis.size(); i++) {
				if(this.merchantmode == YesNoEnum.YES.getCode()) {
					if(!ListUtil.isEmpty(merchantAuthIds) && merchantAuthIds.contains(roleApis.get(i).getApiId()))
					{
						authed.add("api-"+roleApis.get(i).getApiId());
					}
				}else {
					authed.add("api-"+roleApis.get(i).getApiId());
				}
			}
		}
		authTreeDto.setAuthed(authed);
		MesGetAuthTreeDto mesGetAuthTreeDto = new MesGetAuthTreeDto();
		if(YesNoEnum.YES.getCode().intValue() == userInfoDto.getIsAdmin().intValue())
		{
			mesGetAuthTreeDto.setIsAdmin(YesNoEnum.YES.getCode());
		}else {
			mesGetAuthTreeDto.setRoleId(userInfoDto.getRoleId());
		}
		mesGetAuthTreeDto.setParentMenuId(0L);
		if(this.merchantmode.intValue() == YesNoEnum.YES.getCode().intValue())
		{
			if(merchant.getIsSystemMerchant().intValue() == YesNoEnum.NO.getCode().intValue())
			{
				mesGetAuthTreeDto.setAuthTemplateId(merchant.getAuthTemplate());
			}
		}
		
		//获取有权限的顶级菜单
		List<MenuTreeDto> list = this.baseMapper.getMenuTree(mesGetAuthTreeDto);
		this.getSubAuthTree(list, authed, userInfoDto, sysRole.getId(),merchant);
		authTreeDto.setTreeData(list);
		authTreeDto.setAuthed(authed);
		return authTreeDto;
	}
	
	/**  
	 * @Title: getSubAuthTree
	 * @Description: 递归获取所有的子菜单和功能
	 * @param list
	 * @param authed
	 * @param userInfoDto
	 * @param roleId
	 * @author caiyang
	 * @date 2021-06-16 06:47:26 
	 */ 
	private void getSubAuthTree(List<MenuTreeDto> list,List<String> authed,UserInfoDto userInfoDto,Long roleId,SysMerchant merchant) {
		if(!ListUtil.isEmpty(list))
		{
			for (int i = 0; i < list.size(); i++) {
				MesGetAuthTreeDto mesGetAuthTreeDto = new MesGetAuthTreeDto();
				if(YesNoEnum.YES.getCode().intValue() == userInfoDto.getIsAdmin().intValue())
				{
					mesGetAuthTreeDto.setIsAdmin(YesNoEnum.YES.getCode());
				}else {
					mesGetAuthTreeDto.setRoleId(userInfoDto.getRoleId());
				}
				mesGetAuthTreeDto.setParentMenuId(list.get(i).getId());
				if(this.merchantmode.intValue() == YesNoEnum.YES.getCode().intValue())
				{
					if(merchant.getIsSystemMerchant().intValue() == YesNoEnum.NO.getCode().intValue())
					{
						mesGetAuthTreeDto.setAuthTemplateId(merchant.getAuthTemplate());
					}
				}
				List<MenuTreeDto> subMenus = this.baseMapper.getMenuTree(mesGetAuthTreeDto);
				if(!ListUtil.isEmpty(subMenus))
				{
					this.getSubAuthTree(subMenus,authed,userInfoDto,roleId,merchant);
				}else {
					mesGetAuthTreeDto.setMenuId(list.get(i).getId());
					//获取菜单下需要授权的接口
					List<MenuTreeDto> apis = this.iSysApiService.getMenuApis(mesGetAuthTreeDto);
					if(ListUtil.isEmpty(apis)) {
						//如果菜单下没有接口，则判断该菜单是否有权限(顶级菜单除外)
						if(0L != list.get(i).getParentMenuId() && YesNoEnum.NO.getCode().intValue() == userInfoDto.getIsAdmin().intValue())
						{
							QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<SysRoleMenu>();
							queryWrapper.eq("role_id", roleId);
							queryWrapper.eq("menu_id", list.get(i).getId());
							queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
							SysRoleMenu sysRoleMenu = this.iSysRoleMenuService.getOne(queryWrapper,false);
							if(sysRoleMenu != null)
							{
								authed.add(String.valueOf(list.get(i).getId()));
							}
						}
					}else {
						subMenus = new ArrayList<MenuTreeDto>();
						subMenus.addAll(apis);
						
					}
				}
				list.get(i).setChildren(subMenus);
			}
		}
	}


	/**  
	 * @Title: getIndexMenu
	 * @Description: 获取首页菜单
	 * @param userInfoDto
	 * @return 
	 * @see com.caiyang.api.sysmenu.ISysMenuService#getIndexMenu(com.caiyang.base.UserInfoDto) 
	 * @author caiyang
	 * @date 2021-06-18 02:12:32 
	 */
	@Override
	public List<IndexMenuTreeDto> getIndexMenu(UserInfoDto userInfoDto) {
		MesGetIndexMenuDto mesGetIndexMenuDto = new MesGetIndexMenuDto();
		SysMerchant merchant = null;
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			QueryWrapper<SysMerchant> merchantQueryWrapper = new QueryWrapper<>();
			merchantQueryWrapper.eq("merchant_no", userInfoDto.getMerchantNo());
			merchantQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			merchant = this.iSysMerchantService.getOne(merchantQueryWrapper, false);
			if(merchant == null)
			{
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"商户"}));
			}
		}
		if(YesNoEnum.YES.getCode().intValue() == userInfoDto.getIsAdmin())
		{
			mesGetIndexMenuDto.setIsAdmin(YesNoEnum.YES.getCode());
		}else {
			mesGetIndexMenuDto.setIsAdmin(YesNoEnum.NO.getCode());
			mesGetIndexMenuDto.setRoleId(userInfoDto.getRoleId());
		}
		if(this.merchantmode.intValue() == YesNoEnum.YES.getCode().intValue())
		{
			if(merchant.getIsSystemMerchant().intValue() == YesNoEnum.NO.getCode().intValue())
			{
				mesGetIndexMenuDto.setAuthTemplateId(merchant.getAuthTemplate());
			}
		}
		mesGetIndexMenuDto.setParentMenuId(0L);
		List<IndexMenuTreeDto> list = this.baseMapper.getIndexMenu(mesGetIndexMenuDto);
		this.getSubIndexMenu(list, userInfoDto,merchant);
		return list;
	}
	
	
	/**  
	 * @Title: getSubIndexMenu
	 * @Description: 递归获取子菜单
	 * @param list
	 * @param userInfoDto
	 * @author caiyang
	 * @date 2021-06-18 02:28:57 
	 */ 
	private void getSubIndexMenu(List<IndexMenuTreeDto> list,UserInfoDto userInfoDto,SysMerchant merchant) {
		if(!ListUtil.isEmpty(list))
		{
			for (int i = 0; i < list.size(); i++) {
				MesGetIndexMenuDto mesGetIndexMenuDto = new MesGetIndexMenuDto();
				if (YesNoEnum.YES.getCode() == userInfoDto.getIsAdmin()) {
					mesGetIndexMenuDto.setIsAdmin(YesNoEnum.YES.getCode());
				}else {
					mesGetIndexMenuDto.setIsAdmin(YesNoEnum.NO.getCode());
					mesGetIndexMenuDto.setRoleId(userInfoDto.getRoleId());
				}
				mesGetIndexMenuDto.setParentMenuId(list.get(i).getId());
				if(this.merchantmode.intValue() == YesNoEnum.YES.getCode().intValue())
				{
					if(merchant.getIsSystemMerchant().intValue() == YesNoEnum.NO.getCode().intValue())
					{
						mesGetIndexMenuDto.setAuthTemplateId(merchant.getAuthTemplate());
					}
				}
				List<IndexMenuTreeDto> subMenus =  this.baseMapper.getIndexMenu(mesGetIndexMenuDto);
				list.get(i).setSubs(subMenus);
				this.getSubIndexMenu(subMenus, userInfoDto,merchant);
			}
		}
	}
}