package com.springreport.impl.sysmerchantauthtemplate;

import com.springreport.entity.sysmerchantauthtemplate.SysMerchantAuthTemplate;
import com.springreport.entity.sysmerchantauthtemplateids.SysMerchantAuthTemplateIds;
import com.springreport.entity.sysrolemenu.SysRoleMenu;
import com.springreport.mapper.sysmenu.SysMenuMapper;
import com.springreport.mapper.sysmerchantauthtemplate.SysMerchantAuthTemplateMapper;
import com.springreport.api.sysapi.ISysApiService;
import com.springreport.api.sysmerchantauthtemplate.ISysMerchantAuthTemplateService;
import com.springreport.api.sysmerchantauthtemplateids.ISysMerchantAuthTemplateIdsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.StatusCode;
import com.springreport.dto.sysmenu.AuthTreeDto;
import com.springreport.dto.sysmenu.MenuTreeDto;
import com.springreport.dto.sysmenu.MesGetAuthTreeDto;
import com.springreport.dto.sysmerchantauthtemplate.SysMerchantAuthTemplateDto;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: SysMerchantAuthTemplate服务实现
* @author 
* @date 2023-12-22 05:18:59
* @version V1.0  
 */
@Service
public class SysMerchantAuthTemplateServiceImpl extends ServiceImpl<SysMerchantAuthTemplateMapper, SysMerchantAuthTemplate> implements ISysMerchantAuthTemplateService {
  
	@Autowired
	private ISysMerchantAuthTemplateIdsService iSysMerchantAuthTemplateIdsService;
	
	@Autowired
	private SysMenuMapper sysMenuMapper;
	
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
	public PageEntity tablePagingQuery(SysMerchantAuthTemplate model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<SysMerchantAuthTemplate> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(SysMerchantAuthTemplateDto model) {
		BaseEntity result = new BaseEntity();
		//校验名称是否已经存在
		QueryWrapper<SysMerchantAuthTemplate> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("template_name", model.getTemplateName().trim());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysMerchantAuthTemplate isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"模板名称"}));
		}
		this.save(model);
		if(!ListUtil.isEmpty(model.getAuthed()))
		{
			List<SysMerchantAuthTemplateIds> authTemplateIds = new ArrayList<SysMerchantAuthTemplateIds>();
			SysMerchantAuthTemplateIds merchantAuthTemplateIds = null;
			for (int i = 0; i < model.getAuthed().size(); i++) {
				merchantAuthTemplateIds = new SysMerchantAuthTemplateIds();
				merchantAuthTemplateIds.setTemplateId(model.getId());
				if(model.getAuthed().get(i).contains("api-"))
				{//按钮
					merchantAuthTemplateIds.setAuthType(2);
					merchantAuthTemplateIds.setAuthId(Long.parseLong(model.getAuthed().get(i).split("-")[1]));
				}else {//菜单
					merchantAuthTemplateIds.setAuthType(1);
					merchantAuthTemplateIds.setAuthId(Long.parseLong(model.getAuthed().get(i)));
				}
				authTemplateIds.add(merchantAuthTemplateIds);
			}
			this.iSysMerchantAuthTemplateIdsService.saveBatch(authTemplateIds);
		}
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
	public BaseEntity update(SysMerchantAuthTemplateDto model) {
		BaseEntity result = new BaseEntity();
		//校验名称是否已经存在
		QueryWrapper<SysMerchantAuthTemplate> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("id", model.getId());
		queryWrapper.eq("template_name", model.getTemplateName().trim());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysMerchantAuthTemplate isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"模板名称"}));
		}
		this.updateById(model);
		//获取当前所有的权限
		QueryWrapper<SysMerchantAuthTemplateIds> templateIdsQueryWrapper = new QueryWrapper<>();
		templateIdsQueryWrapper.eq("template_id", model.getId());
		templateIdsQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<SysMerchantAuthTemplateIds> sysMerchantAuthTemplateIds = this.iSysMerchantAuthTemplateIdsService.list(templateIdsQueryWrapper);
		if(ListUtil.isEmpty(sysMerchantAuthTemplateIds))
		{
			List<SysMerchantAuthTemplateIds> authTemplateIds = new ArrayList<SysMerchantAuthTemplateIds>();
			SysMerchantAuthTemplateIds merchantAuthTemplateIds = null;
			for (int i = 0; i < model.getAuthed().size(); i++) {
				merchantAuthTemplateIds = new SysMerchantAuthTemplateIds();
				merchantAuthTemplateIds.setTemplateId(model.getId());
				if(model.getAuthed().get(i).contains("api-"))
				{//按钮
					merchantAuthTemplateIds.setAuthType(2);
					merchantAuthTemplateIds.setAuthId(Long.parseLong(model.getAuthed().get(i).split("-")[1]));
				}else {//菜单
					merchantAuthTemplateIds.setAuthType(1);
					merchantAuthTemplateIds.setAuthId(Long.parseLong(model.getAuthed().get(i)));
				}
				authTemplateIds.add(merchantAuthTemplateIds);
			}
			this.iSysMerchantAuthTemplateIdsService.saveBatch(authTemplateIds);
		}else {
			List<String> userAuth = new ArrayList<String>();//用户当前拥有的权限
			for (int i = 0; i < sysMerchantAuthTemplateIds.size(); i++) {
				if(sysMerchantAuthTemplateIds.get(i).getAuthType().intValue() == 1)
				{
					userAuth.add(String.valueOf(sysMerchantAuthTemplateIds.get(i).getAuthId()));
 				}else {
					userAuth.add("api-"+String.valueOf(sysMerchantAuthTemplateIds.get(i).getAuthId()));
				}
			}
			List<String> addData = ListUtil.getDiffData(model.getAuthed(), userAuth);//需要添加的权限
			List<String> delData = ListUtil.getDiffData(userAuth, model.getAuthed());//需要删除的权限
			if(!ListUtil.isEmpty(addData))
			{
				List<SysMerchantAuthTemplateIds> addDatas = new ArrayList<>();
				SysMerchantAuthTemplateIds merchantAuthTemplateIds = null;
				for (int i = 0; i < addData.size(); i++) {
					merchantAuthTemplateIds = new SysMerchantAuthTemplateIds();
					merchantAuthTemplateIds.setTemplateId(model.getId());
					if(addData.get(i).contains("api-"))
					{//按钮
  						merchantAuthTemplateIds.setAuthType(2);
 						merchantAuthTemplateIds.setAuthId(Long.parseLong(addData.get(i).split("-")[1]));
					}else {//菜单
						merchantAuthTemplateIds.setAuthType(1);
						merchantAuthTemplateIds.setAuthId(Long.parseLong(addData.get(i)));
					}
					addDatas.add(merchantAuthTemplateIds);
				}
				this.iSysMerchantAuthTemplateIdsService.saveBatch(addDatas);
			}
			if(!ListUtil.isEmpty(delData))
			{
				List<Long> authIds = new ArrayList<>();
				for (int i = 0; i < delData.size(); i++) {
					if(delData.get(i).contains("api-")) {
						authIds.add(Long.parseLong(delData.get(i).split("-")[1]));
					}else {
						authIds.add(Long.parseLong(delData.get(i)));
					}
				}
				UpdateWrapper<SysMerchantAuthTemplateIds> updateWrapper = new UpdateWrapper<>();
				updateWrapper.eq("template_id", model.getId());
				updateWrapper.in("auth_id", authIds);
				SysMerchantAuthTemplateIds updateData = new SysMerchantAuthTemplateIds();
				updateData.setDelFlag(DelFlagEnum.DEL.getCode());
				this.iSysMerchantAuthTemplateIdsService.update(updateData, updateWrapper);
			}
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
		//删除模板
		SysMerchantAuthTemplate sysMerchantAuthTemplate = new SysMerchantAuthTemplate();
		sysMerchantAuthTemplate.setId(id);
		sysMerchantAuthTemplate.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(sysMerchantAuthTemplate);
		//删除模板中的权限
		UpdateWrapper<SysMerchantAuthTemplateIds> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("template_id", id);
		updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysMerchantAuthTemplateIds update = new SysMerchantAuthTemplateIds();
		update.setDelFlag(DelFlagEnum.DEL.getCode());
		this.iSysMerchantAuthTemplateIdsService.update(update, updateWrapper);
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
		List<SysMerchantAuthTemplate> list = new ArrayList<SysMerchantAuthTemplate>();
		for (int i = 0; i < ids.size(); i++) {
			SysMerchantAuthTemplate sysMerchantAuthTemplate = new SysMerchantAuthTemplate();
			sysMerchantAuthTemplate.setId(ids.get(i));
			sysMerchantAuthTemplate.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(sysMerchantAuthTemplate);
		}
		//删除模板
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		//删除模板中的权限
		UpdateWrapper<SysMerchantAuthTemplateIds> updateWrapper = new UpdateWrapper<>();
		updateWrapper.in("template_id", ids);
		updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysMerchantAuthTemplateIds update = new SysMerchantAuthTemplateIds();
		update.setDelFlag(DelFlagEnum.DEL.getCode());
		this.iSysMerchantAuthTemplateIdsService.update(update, updateWrapper);
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @MethodName: getMerchantTemplateAuthTree
	 * @Description: 获取租户权限模板权限树
	 * @author caiyang
	 * @param merchantAuthTemplate
	 * @return
	 * @see com.springreport.api.sysmerchantauthtemplate.ISysMerchantAuthTemplateService#getMerchantTemplateAuthTree(com.springreport.entity.sysmerchantauthtemplate.SysMerchantAuthTemplate)
	 * @date 2023-12-23 05:20:37 
	 */
	@Override
	public AuthTreeDto getMerchantTemplateAuthTree(SysMerchantAuthTemplate merchantAuthTemplate) {
		AuthTreeDto authTreeDto = new AuthTreeDto();
		List<String> authed = new ArrayList<String>();//已经授权的功能
		if(merchantAuthTemplate.getId() != null)
		{
			QueryWrapper<SysMerchantAuthTemplateIds> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("template_id", merchantAuthTemplate.getId());
			queryWrapper.eq("auth_type", 2);
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<SysMerchantAuthTemplateIds> merchantAuthTemplateIds = this.iSysMerchantAuthTemplateIdsService.list(queryWrapper);
			if(!ListUtil.isEmpty(merchantAuthTemplateIds))
			{
				for (int i = 0; i < merchantAuthTemplateIds.size(); i++) {
					authed.add("api-"+merchantAuthTemplateIds.get(i).getAuthId());
				}
			}
			authTreeDto.setAuthed(authed);
		}
		MesGetAuthTreeDto mesGetAuthTreeDto = new MesGetAuthTreeDto();
		mesGetAuthTreeDto.setIsAdmin(YesNoEnum.YES.getCode());
		mesGetAuthTreeDto.setParentMenuId(0L);
		//获取有权限的顶级菜单
		List<MenuTreeDto> list = this.sysMenuMapper.getMenuTree(mesGetAuthTreeDto);
		this.getSubAuthTree(list, authed);
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
	private void getSubAuthTree(List<MenuTreeDto> list,List<String> authed) {
		if(!ListUtil.isEmpty(list))
		{
			for (int i = 0; i < list.size(); i++) {
				MesGetAuthTreeDto mesGetAuthTreeDto = new MesGetAuthTreeDto();
				mesGetAuthTreeDto.setIsAdmin(YesNoEnum.YES.getCode());
				mesGetAuthTreeDto.setParentMenuId(list.get(i).getId());
				List<MenuTreeDto> subMenus = this.sysMenuMapper.getMenuTree(mesGetAuthTreeDto);
				if(!ListUtil.isEmpty(subMenus))
				{
					this.getSubAuthTree(subMenus,authed);
				}else {
					mesGetAuthTreeDto.setMenuId(list.get(i).getId());
					//获取菜单下需要授权的接口
					List<MenuTreeDto> apis = this.iSysApiService.getMenuApis(mesGetAuthTreeDto);
					if(ListUtil.isEmpty(apis)) {
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
	 * @MethodName: getAuthTemplates
	 * @Description: 获取所有的权限模板
	 * @author caiyang
	 * @return
	 * @see com.springreport.api.sysmerchantauthtemplate.ISysMerchantAuthTemplateService#getAuthTemplates()
	 * @date 2023-12-24 10:07:04 
	 */
	@Override
	public List<SysMerchantAuthTemplate> getAuthTemplates() {
		QueryWrapper<SysMerchantAuthTemplate> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<SysMerchantAuthTemplate> result = this.list(queryWrapper);
		return result;
	}

}