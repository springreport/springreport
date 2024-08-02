package com.springreport.impl.screentpl;

import com.springreport.entity.reporttpldatasource.ReportTplDatasource;
import com.springreport.entity.screencontent.ScreenContent;
import com.springreport.entity.screentpl.ScreenTpl;
import com.springreport.mapper.screencontent.ScreenContentMapper;
import com.springreport.mapper.screentpl.ScreenTplMapper;
import com.springreport.api.reporttpldataset.IReportTplDatasetService;
import com.springreport.api.reporttpldatasource.IReportTplDatasourceService;
import com.springreport.api.screencontent.IScreenContentService;
import com.springreport.api.screentpl.IScreenTplService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.constants.StatusCode;
import com.springreport.dto.screentpl.MesScreenTplDto;
import com.springreport.dto.screentpl.SaveScreenTplDto;
import com.springreport.dto.screentpl.ScreenTplDto;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import com.springreport.enums.ComponentTypeEnum;
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
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<ScreenTpl> list = this.baseMapper.searchDataLike(model);
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
}