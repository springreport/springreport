package com.springreport.impl.screentpl;

import com.springreport.entity.reporttpldatasource.ReportTplDatasource;
import com.springreport.entity.screencontent.ScreenContent;
import com.springreport.entity.screentpl.ScreenTpl;
import com.springreport.entity.sysuser.SysUser;
import com.springreport.mapper.screencontent.ScreenContentMapper;
import com.springreport.mapper.screentpl.ScreenTplMapper;
import com.springreport.api.reporttpldatasource.IReportTplDatasourceService;
import com.springreport.api.reporttype.IReportTypeService;
import com.springreport.api.screencontent.IScreenContentService;
import com.springreport.api.screentpl.IScreenTplService;
import com.springreport.api.sysuser.ISysUserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.DateUtil;
import com.springreport.util.JWTUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.StatusCode;
import com.springreport.dto.reporttpl.ShareDto;
import com.springreport.dto.screentpl.MesScreenTplDto;
import com.springreport.dto.screentpl.SaveScreenTplDto;
import com.springreport.dto.screentpl.ScreenTplDto;
import com.springreport.dto.screentpl.ScreenTplTreeDto;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: ScreenTpl服务实现
* @author 
* @date 2021-08-02 07:01:17
* @version V1.0  
 */
@Service
public class ScreenTplServiceImpl extends ServiceImpl<ScreenTplMapper, ScreenTpl> implements IScreenTplService {
  
	@Autowired
	private IScreenContentService iScreenContentService;
	
	@Autowired
	private IReportTplDatasourceService iReportTplDatasourceService;

	@Autowired
	private ScreenContentMapper screenContentMapper;
	
	@Value("${merchantmode}")
    private Integer merchantmode;
	
	@Autowired
	private IReportTypeService iReportTypeService;
	
	@Autowired
	private ISysUserService iSysUserService;
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(ScreenTpl model) {
		PageEntity result = new PageEntity();
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		QueryWrapper<ScreenTpl> screenTplWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode() && model.getMerchantNo() != null) {
			screenTplWrapper.eq("merchant_no", model.getMerchantNo());
		}
		if(model.getReportType() != null && model.getReportType() != 0) {
			screenTplWrapper.eq("report_type", model.getReportType());
		}
		if(model.getIsTemplate() != null && model.getIsTemplate().intValue() == YesNoEnum.YES.getCode().intValue()) {
			screenTplWrapper.eq("is_template", model.getIsTemplate());
		}else {
			screenTplWrapper.eq("is_template", YesNoEnum.NO.getCode());
		}
		if(model.getTemplateField() != null && model.getTemplateField() != 0) {
			screenTplWrapper.eq("template_field", model.getTemplateField());
		}
		if(StringUtil.isNotEmpty(model.getTplCode())) {
			screenTplWrapper.like("tpl_code", model.getTplCode());
		}
		if(StringUtil.isNotEmpty(model.getTplName())) {
			screenTplWrapper.like("tpl_name", model.getTplName());
		}
		screenTplWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ScreenTpl> list = this.list(screenTplWrapper);
		result.setData(list);
		result.setTotal(page.getTotal());
		result.setCurrentPage(model.getCurrentPage());
		result.setPageSize(model.getPageSize());
		return result;
	}

	@Override
	public List<ScreenTplTreeDto> getChildren(ScreenTpl model) {
		List<ScreenTplTreeDto> result = new ArrayList<>();
		QueryWrapper<ScreenTpl> screenTplWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			screenTplWrapper.eq("merchant_no", model.getMerchantNo());
		}
		if(this.merchantmode == YesNoEnum.YES.getCode() && model.getMerchantNo() != null) {
			screenTplWrapper.eq("merchant_no", model.getMerchantNo());
		}
		if(model.getReportType() != null && model.getReportType() != 0) {
			screenTplWrapper.eq("report_type", model.getReportType());
		}
		if(model.getIsTemplate() != null && model.getIsTemplate().intValue() == YesNoEnum.YES.getCode().intValue()) {
			screenTplWrapper.eq("is_template", model.getIsTemplate());
		}else {
			screenTplWrapper.eq("is_template", YesNoEnum.NO.getCode());
		}
		if(model.getTemplateField() != null && model.getTemplateField() != 0) {
			screenTplWrapper.eq("template_field", model.getTemplateField());
		}
		if(StringUtil.isNotEmpty(model.getTplCode())) {
			screenTplWrapper.like("tpl_code", model.getTplCode());
		}
		if(StringUtil.isNotEmpty(model.getTplName())) {
			screenTplWrapper.like("tpl_name", model.getTplName());
		}
		screenTplWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ScreenTpl> tpls = this.list(screenTplWrapper);
		if(ListUtil.isNotEmpty(tpls)) {
			ScreenTplTreeDto screenTplTreeDto = null;
			for (int i = 0; i < tpls.size(); i++) {
				screenTplTreeDto = new ScreenTplTreeDto();
				BeanUtils.copyProperties(tpls.get(i), screenTplTreeDto);
				screenTplTreeDto.setIcon("iconfont icon-xingzhuang");
				screenTplTreeDto.setType("2");
				screenTplTreeDto.setHasChildren(false);
				result.add(screenTplTreeDto);
			}
		}
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
		MesScreenTplDto result = new MesScreenTplDto();
		ScreenTpl screenTpl = this.getById(id);
		BeanUtils.copyProperties(screenTpl, result);
		QueryWrapper<ReportTplDatasource> queryWrapper = new QueryWrapper<ReportTplDatasource>();
		queryWrapper.eq("tpl_id", screenTpl.getId());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplDatasource> list = this.iReportTplDatasourceService.list(queryWrapper);
		List<Long> dataSource = new ArrayList<Long>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				dataSource.add(list.get(i).getDatasourceId());
			}
		}
		result.setDataSource(dataSource);
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
	public BaseEntity insert(MesScreenTplDto mesScreenTplDto) {
		QueryWrapper<ScreenTpl> queryWrapper = new QueryWrapper<ScreenTpl>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", mesScreenTplDto.getMerchantNo());
		}
		queryWrapper.eq("tpl_code", mesScreenTplDto.getTplCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ScreenTpl isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该大屏标识"}));
		}
		BaseEntity result = new BaseEntity();
		ScreenTpl model = new ScreenTpl();
		BeanUtils.copyProperties(mesScreenTplDto, model);
		model.setBackground("#EDEDEF");//设置默认画板颜色
		this.save(model);
		//保存数据源
		if(!ListUtil.isEmpty(mesScreenTplDto.getDataSource()))
		{
			List<ReportTplDatasource> datasources = new ArrayList<ReportTplDatasource>();
			ReportTplDatasource datasource = null;
			for (int i = 0; i < mesScreenTplDto.getDataSource().size(); i++) {
				datasource = new ReportTplDatasource();
				datasource.setTplId(model.getId());
				datasource.setDatasourceId(mesScreenTplDto.getDataSource().get(i));
				datasources.add(datasource);
			}
			this.iReportTplDatasourceService.saveBatch(datasources);
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
	public BaseEntity update(MesScreenTplDto mesScreenTplDto) {
		QueryWrapper<ScreenTpl> queryWrapper = new QueryWrapper<ScreenTpl>();
		queryWrapper.ne("id", mesScreenTplDto.getId());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", mesScreenTplDto.getMerchantNo());
		}
		queryWrapper.eq("tpl_code", mesScreenTplDto.getTplCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ScreenTpl isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该大屏标识"}));
		}
		BaseEntity result = new BaseEntity();
		ScreenTpl model = new ScreenTpl();
		BeanUtils.copyProperties(mesScreenTplDto, model);
		this.updateById(model);
		UpdateWrapper<ReportTplDatasource> updateWrapper = new UpdateWrapper<ReportTplDatasource>();
		updateWrapper.eq("tpl_id", model.getId());
		updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportTplDatasource entity = new ReportTplDatasource();
		entity.setDelFlag(DelFlagEnum.DEL.getCode());
		//先删除之前的数据源
		this.iReportTplDatasourceService.update(entity, updateWrapper);
		//再新增数据源
		if(!ListUtil.isEmpty(mesScreenTplDto.getDataSource()))
		{
			List<ReportTplDatasource> datasources = new ArrayList<ReportTplDatasource>();
			ReportTplDatasource datasource = null;
			for (int i = 0; i < mesScreenTplDto.getDataSource().size(); i++) {
				datasource = new ReportTplDatasource();
				datasource.setTplId(model.getId());
				datasource.setDatasourceId(mesScreenTplDto.getDataSource().get(i));
				datasources.add(datasource);
			}
			this.iReportTplDatasourceService.saveBatch(datasources);
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
		ScreenTpl screenTpl = new ScreenTpl();
		screenTpl.setId(id);
		screenTpl.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(screenTpl);
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
		List<ScreenTpl> list = new ArrayList<ScreenTpl>();
		for (int i = 0; i < ids.size(); i++) {
			ScreenTpl screenTpl = new ScreenTpl();
			screenTpl.setId(ids.get(i));
			screenTpl.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(screenTpl);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @Title: getScreenDesign
	 * @Description: 获取大屏设计内容
	 * @param screenTpl
	 * @return 
	 * @see com.caiyang.api.screentpl.IScreenTplService#getScreenDesign(com.caiyang.entity.screentpl.ScreenTpl) 
	 * @author caiyang
	 * @date 2021-08-02 11:40:00 
	 */
	@Override
	public ScreenTplDto getScreenDesign(ScreenTpl screenTpl) {
		ScreenTplDto result = new ScreenTplDto();
		//获取大屏模板
		screenTpl = this.getById(screenTpl.getId());
		if(screenTpl == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist",new String[] {"大屏模板"}));
		}
		BeanUtils.copyProperties(screenTpl, result);
		//获取大屏模板内容
		QueryWrapper<ScreenContent> queryWrapper = new QueryWrapper<ScreenContent>();
		queryWrapper.eq("tpl_id", screenTpl.getId());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ScreenContent> components = this.iScreenContentService.list(queryWrapper);
		result.setComponents(components);
		return result;
	}


	/**  
	 * @Title: saveScreenDesign
	 * @Description: 保存大屏设计
	 * @param screenTplDto 
	 * @see com.caiyang.api.screentpl.IScreenTplService#saveScreenDesign(com.caiyang.dto.screentpl.ScreenTplDto) 
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @date 2021-08-02 04:38:59 
	 */
	@Override
	public BaseEntity saveScreenDesign(SaveScreenTplDto saveScreenTplDto) throws JsonProcessingException {
		BaseEntity result = new BaseEntity();
		ScreenTpl exist = this.getById(saveScreenTplDto.getId());
		if(exist == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist",new String[] {"大屏模板"}));
		}
		ScreenTpl screenTpl = new ScreenTpl();
		BeanUtils.copyProperties(saveScreenTplDto, screenTpl);
		this.updateById(screenTpl);
		ScreenContent model = new ScreenContent();
		model.setTplId(saveScreenTplDto.getId());
		if(!ListUtil.isEmpty(saveScreenTplDto.getComponents()))
		{
			ObjectMapper mapper = new ObjectMapper();
			List<ScreenContent> addComponents = new ArrayList<ScreenContent>();//新增的组件
			List<ScreenContent> updateComponents = new ArrayList<ScreenContent>();//更新的组件
			List<ScreenContent> deleteComponents = new ArrayList<ScreenContent>();//删除的组件
			List<Long> contentIds = this.screenContentMapper.getTplContentIds(model);
			if(ListUtil.isNotEmpty(contentIds)) {
				ScreenContent screenContent = null;
				for (int i = 0; i < saveScreenTplDto.getComponents().size(); i++) {
					screenContent = new ScreenContent();
					String primaryKey = "";
					if(saveScreenTplDto.getComponents().get(i).get("primaryKey") != null)
					{
						primaryKey = String.valueOf(saveScreenTplDto.getComponents().get(i).get("primaryKey"));
					}
					if(StringUtil.isNotEmpty(primaryKey)) {
						//更新的组件
						screenContent.setId(Long.valueOf(primaryKey));
						screenContent.setContent(mapper.writeValueAsString(saveScreenTplDto.getComponents().get(i)));
						updateComponents.add(screenContent);
						contentIds.remove(Long.valueOf(primaryKey));//更新的组件从查询出来的id集合中删除，集合中最后剩余的就是删除的组件
					}else {
						//新增的组件
						Long id = IdWorker.getId(screenContent);
						screenContent.setId(id);
						screenContent.setTplId(saveScreenTplDto.getId());
						saveScreenTplDto.getComponents().get(i).put("primaryKey", String.valueOf(id));
						screenContent.setContent(mapper.writeValueAsString(saveScreenTplDto.getComponents().get(i)));
						addComponents.add(screenContent);
					}
				}
			}else {
				//全部是新增的组件
				ScreenContent screenContent = null;
				for (int i = 0; i < saveScreenTplDto.getComponents().size(); i++) {
					screenContent = new ScreenContent();
					Long id = IdWorker.getId(screenContent);
					screenContent.setId(id);
					screenContent.setTplId(saveScreenTplDto.getId());
					saveScreenTplDto.getComponents().get(i).put("primaryKey", String.valueOf(id));
					screenContent.setContent(mapper.writeValueAsString(saveScreenTplDto.getComponents().get(i)));
					addComponents.add(screenContent);
				}
			}
			if(!ListUtil.isEmpty(addComponents))
			{
				this.iScreenContentService.saveBatch(addComponents);
			}
			if(!ListUtil.isEmpty(updateComponents))
			{
				this.iScreenContentService.updateBatchById(updateComponents);
			}
			if(!ListUtil.isEmpty(contentIds))
			{
				ScreenContent screenContent = null;
				for (int i = 0; i < contentIds.size(); i++) {
					screenContent = new ScreenContent();
					screenContent.setId(contentIds.get(i));
					screenContent.setDelFlag(DelFlagEnum.DEL.getCode());
					deleteComponents.add(screenContent);
				}
				this.iScreenContentService.updateBatchById(deleteComponents);
			}
		}else {
			UpdateWrapper<ScreenContent> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("tpl_id", saveScreenTplDto.getId());
			updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			ScreenContent screenContent = new ScreenContent();
			screenContent.setDelFlag(DelFlagEnum.DEL.getCode());
			this.iScreenContentService.update(screenContent, updateWrapper);
		}
		result.setStatusMsg(MessageUtil.getValue("info.save"));
		return result;
	}


	/**  
	 * @Title: getScreens
	 * @Description: 获取所有的大屏
	 * @return 
	 * @see com.caiyang.api.screentpl.IScreenTplService#getScreens() 
	 * @author caiyang
	 * @date 2021-08-30 07:39:52 
	 */
	@Override
	public List<ScreenTpl> getScreens(ScreenTpl screenTpl) {
		QueryWrapper<ScreenTpl> queryWrapper = new QueryWrapper<ScreenTpl>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", screenTpl.getMerchantNo());
		}
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		queryWrapper.orderByAsc("id");
		List<ScreenTpl> result = this.list(queryWrapper);
		return result;
	}

	@Override
	public BaseEntity copyScreen(ScreenTpl model) {
		BaseEntity result = new BaseEntity();
		Long tplId = model.getId();
		ScreenTpl screenTpl = this.getById(model.getId());
		String end = DateUtil.getLastSixDigits();
		String newCode = screenTpl.getTplCode()+"_copy_"+end;
		String newName = screenTpl.getTplName()+"_copy_"+end;
		screenTpl.setTplCode(newCode);
		screenTpl.setTplName(newName);
		screenTpl.setIsTemplate(YesNoEnum.NO.getCode());
		screenTpl.setTemplateField(null);
		screenTpl.setId(null);
		this.save(screenTpl);
		//获取所有的组件
		QueryWrapper<ScreenContent> screenContentQueryWrapper = new QueryWrapper<>();
		screenContentQueryWrapper.eq("tpl_id", tplId);
		screenContentQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ScreenContent> screenContents = this.iScreenContentService.list(screenContentQueryWrapper);
		if(ListUtil.isNotEmpty(screenContents)) {
			for (int i = 0; i < screenContents.size(); i++) {
				Long id = IdWorker.getId();
				screenContents.get(i).setId(id);
				JSONObject content = JSONObject.parseObject(screenContents.get(i).getContent());
				content.put("primaryKey", id);
				screenContents.get(i).setContent(JSON.toJSONString(content));
				screenContents.get(i).setTplId(screenTpl.getId());
			}
			this.iScreenContentService.saveBatch(screenContents);
		}
		//保存报表关联的数据源
		QueryWrapper<ReportTplDatasource> tplDatasourceQueryWrapper = new QueryWrapper<>();
		tplDatasourceQueryWrapper.eq("tpl_id", tplId);
		tplDatasourceQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplDatasource> datasources = this.iReportTplDatasourceService.list(tplDatasourceQueryWrapper);
		if(ListUtil.isNotEmpty(datasources)) {
			for (int i = 0; i < datasources.size(); i++) {
				datasources.get(i).setId(null);
				datasources.get(i).setTplId(screenTpl.getId());
			}
			this.iReportTplDatasourceService.saveBatch(datasources);
		}
		result.setStatusMsg(MessageUtil.getValue("info.copy",new String[] {newName}));
		return result;
	}

	/**  
	 * @MethodName: getShareUrl
	 * @Description: 获取大屏分享链接
	 * @author caiyang
	 * @param shareDto
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.screentpl.IScreenTplService#getShareUrl(com.springreport.dto.reporttpl.ShareDto, com.springreport.base.UserInfoDto)
	 * @date 2025-07-19 07:27:39 
	 */
	@Override
	public ShareDto getShareUrl(ShareDto shareDto, UserInfoDto userInfoDto) {
		ShareDto result = new ShareDto();
		SysUser sysUser = iSysUserService.getById(userInfoDto.getUserId());
		if(sysUser == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist",new String[] {"用户信息"}));
		}
		String token = "";
		if(YesNoEnum.YES.getCode().intValue() == shareDto.getIsShareForever().intValue()) {
			token = JWTUtil.sign(userInfoDto, sysUser.getPassword(),3153600000L);
		}else {
			token = JWTUtil.sign(userInfoDto, sysUser.getPassword(),shareDto.getShareTime()*60L);
		}
		String shareUrl = MessageUtil.getValue("screen.share.url") + "&token="+token;
		String shareMsg = MessageUtil.getValue("info.share.screen", new String[] {shareUrl,userInfoDto.getUserName(),DateUtil.getNow(),YesNoEnum.YES.getCode().intValue() == shareDto.getIsShareForever().intValue()?"永久有效":String.valueOf(shareDto.getShareTime())+"分钟"});
		result.setShareMsg(shareMsg);
		return result;
	}
}