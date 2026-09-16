package com.springreport.impl.screentpl;

import com.springreport.entity.reporttpldatasource.ReportTplDatasource;
import com.springreport.entity.screencontent.ScreenContent;
import com.springreport.entity.screentpl.ScreenTpl;
import com.springreport.entity.sysuser.SysUser;
import com.springreport.mapper.screencontent.ScreenContentMapper;
import com.springreport.mapper.screentpl.ScreenTplMapper;
import com.springreport.api.reporttpldataset.IReportTplDatasetService;
import com.springreport.api.reporttpldatasource.IReportTplDatasourceService;
import com.springreport.api.reporttype.IReportTypeService;
import com.springreport.api.screencontent.IScreenContentService;
import com.springreport.api.screentpl.IScreenTplService;
import com.springreport.api.sysuser.ISysUserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.CheckUtil;
import com.springreport.util.DateUtil;
import com.springreport.util.JWTUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.StringUtil;
import com.springreport.util.UUIDUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.dto.reporttpl.ShareDto;
import com.springreport.dto.reporttpldataset.MesGetRelyOnSelectData;
import com.springreport.dto.reporttpldataset.ReportParamDto;
import com.springreport.dto.screentpl.MesScreenTplDto;
import com.springreport.dto.screentpl.SaveScreenTplDto;
import com.springreport.dto.screentpl.ScreenTplDto;
import com.springreport.dto.screentpl.ScreenTplTreeDto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.springreport.enums.DefaultDateTypeEnum;
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
	private IReportTplDatasetService iReportTplDatasetService;
	
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
	 * @throws ParseException 
	 * @date 2021-08-02 11:40:00 
	 */
	@Override
	public ScreenTplDto getScreenDesign(ScreenTpl screenTpl,UserInfoDto userInfoDto) throws Exception {
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
		ObjectMapper objectMapper = new ObjectMapper();
		if(ListUtil.isNotEmpty(components)) {
			JSONObject content = null;
			ReportParamDto reportParamDto = null;
			for (int i = 0; i < components.size(); i++) {
				content = objectMapper.readValue(components.get(i).getContent(), JSONObject.class);
				String type = content.getString("type");
				if("formsDate".equals(type)) {
					//日期类型
					reportParamDto = new ReportParamDto();
					reportParamDto.setParamCode(content.getString("paramCode"));
					reportParamDto.setDateFormat(content.getString("dateFormat"));
					reportParamDto.setParamDefault(content.getString("paramDefault"));
					this.processDateParam(reportParamDto);
					content.put("paramValue", reportParamDto.getParamDefault());
					
				}else if("formsDateRange".equals(type)) {
					//日期范围类型
					JSONArray paramValue = new JSONArray();
					String paramDefaultStart = content.getString("paramDefaultStart");
					String paramDefaultEnd = content.getString("paramDefaultEnd");
					if(StringUtil.isNotEmpty(paramDefaultStart)) {
						reportParamDto = new ReportParamDto();
						reportParamDto.setParamCode(content.getString("paramCode"));
						reportParamDto.setDateFormat(content.getString("dateFormat"));
						reportParamDto.setParamDefault(content.getString("paramDefaultStart"));
						this.processDateParam(reportParamDto);
						paramValue.add(reportParamDto.getParamDefault());
					}else {
						paramValue.add("");
					}
					if(StringUtil.isNotEmpty(paramDefaultEnd)) {
						reportParamDto = new ReportParamDto();
						reportParamDto.setParamCode(content.getString("paramCode"));
						reportParamDto.setDateFormat(content.getString("dateFormat"));
						reportParamDto.setParamDefault(content.getString("paramDefaultEnd"));
						this.processDateParam(reportParamDto);
						paramValue.add(reportParamDto.getParamDefault());
					}else {
						paramValue.add("");
					}
					content.put("paramValue", paramValue);
				}else if("formsSelect".equals(type) || "formsMultiselect".equals(type)) {
					//下拉单选或者下拉多选
					String dataType = content.getString("dataType");
					String dataContent = content.getString("dataContent");
					if("2".equals(dataType)) {
						//sql语句
						Long datasourceId = content.getLong("datasourceId");
						if(datasourceId != null && StringUtil.isNotEmpty(dataContent)) {
							MesGetRelyOnSelectData mesGetRelyOnSelectData = new MesGetRelyOnSelectData(); 
							mesGetRelyOnSelectData.setDatasourceId(datasourceId);
							mesGetRelyOnSelectData.setSelectContent(dataContent);
							List<Map<String, Object>> datas = this.iReportTplDatasetService.getSelectData(mesGetRelyOnSelectData,userInfoDto);
							content.put("selectContent", datas);
						}else {
							content.put("dataContent", "");
							content.put("selectContent", new ArrayList());
						}
					}else {
						if(StringUtil.isNotEmpty(dataContent)) {
							content.put("selectContent", JSON.parseArray(dataContent));
						}
					}
					String paramDefault = content.getString("paramDefault");
					if(StringUtil.isNotEmpty(paramDefault)) {
						if("formsSelect".equals(type)) {
							content.put("paramValue", paramDefault);
						}else if("formsMultiselect".equals(type)) {
							String[] paramDefaults = paramDefault.split(",");
	 						content.put("paramValue", paramDefaults);
						}
					}else {
						if("formsSelect".equals(type)) {
							content.put("paramValue", "");
						}else if("formsMultiselect".equals(type)) {
	 						content.put("paramValue", new ArrayList());
						}
					}
				}else if("formsTreeselect".equals(type) || "formsMultitree".equals(type)) {
					//下拉单选或者下拉多选
					String dataType = content.getString("dataType");
					String dataContent = content.getString("dataContent");
					if("2".equals(dataType)) {
						//sql语句
						Long datasourceId = content.getLong("datasourceId");
						if(datasourceId != null && StringUtil.isNotEmpty(dataContent)) {
							MesGetRelyOnSelectData mesGetRelyOnSelectData = new MesGetRelyOnSelectData(); 
							mesGetRelyOnSelectData.setDatasourceId(datasourceId);
							mesGetRelyOnSelectData.setSelectContent(dataContent);
							List<Map<String, Object>> datas = this.iReportTplDatasetService.getTreeSelectData(mesGetRelyOnSelectData,userInfoDto);
							content.put("selectContent", datas);
						}else {
							content.put("dataContent", "");
							content.put("selectContent", new ArrayList());
						}
					}else {
						List<Map> resultList = new ArrayList<Map>();
						if(StringUtil.isNotEmpty(dataContent)) {
							List<Map> list = JSON.parseArray(dataContent,Map.class);
							if(!ListUtil.isEmpty(list)) {
								Map<String, Map<String, Object>> entityMap = new HashMap<>();
								Map<String, List<Map<String, Object>>> childrenMap = new HashMap<>();
								for (Map<String, Object> entity : list){
									entity.put("value", String.valueOf(entity.get("id")));
									entity.put("children", new ArrayList<>());
									entityMap.put(String.valueOf(entity.get("id")),entity);
									childrenMap.put(String.valueOf(entity.get("id")), (List<Map<String, Object>>) entity.get("children"));
								}
								// 组装成数结构列表
								for (Map<String, Object> entity : list){
									Map<String, Object> parentEntity = entityMap.get(String.valueOf(entity.get("pid")));
									if(parentEntity == null)
									{
										// 将顶级节点加入结果集中
								         resultList.add(entity);
								         continue;
									}
									// 把自己加到父节点对象里面去
									childrenMap.get(String.valueOf(parentEntity.get("id"))).add(entity);
								}
							}
						}
						content.put("selectContent", resultList);
					}
					String paramDefault = content.getString("paramDefault");
					if(StringUtil.isNotEmpty(paramDefault)) {
						if("formsTreeselect".equals(type)) {
							content.put("paramValue", paramDefault);
						}else if("formsMultitree".equals(type)) {
							String[] paramDefaults = paramDefault.split(",");
	 						content.put("paramValue", paramDefaults);
						}
					}else {
						if("formsTreeselect".equals(type)) {
							content.put("paramValue", "");
						}else if("formsMultitree".equals(type)) {
	 						content.put("paramValue", new ArrayList());
						}
					}
				}else if("formsCascader".equals(type)) {
					//级联选择
					String paramDefault = content.getString("paramDefault");
					if(StringUtil.isNotEmpty(paramDefault)) {
						String[] paramDefaults = paramDefault.split(",");
 						content.put("paramValue", paramDefaults);
					}else {
						content.put("paramValue", new ArrayList());
					}
				}
				components.get(i).setContent(JSON.toJSONString(content));
			}
		}
		result.setComponents(components);
		return result;
	}
	
	private void processDateParam(ReportParamDto param) throws ParseException {
		if(StringUtil.isNotEmpty(param.getParamDefault()))
		{
			String dateDefaultValue = param.getParamDefault();
			param.setDateDefaultValue(dateDefaultValue);
			String dateFormat = param.getDateFormat();
			if("YYYY-MM-DD".equals(dateFormat))
			{
				dateFormat = DateUtil.FORMAT_LONOGRAM;
			}else if("YYYY-MM".equals(dateFormat))
			{
				dateFormat = DateUtil.FORMAT_YEARMONTH;
			}else if("YYYY-MM-DD HH:mm".equals(dateFormat))
			{
				dateFormat = DateUtil.FORMAT_WITHOUTSECONDS;
			}else if("YYYY-MM-DD HH:mm:ss".equals(dateFormat))
			{
				dateFormat = DateUtil.FORMAT_FULL;
			}else if("YYYY".equals(dateFormat))
			{
				dateFormat = DateUtil.FORMAT_YEAR;
			}else {
				if(StringUtil.isNullOrEmpty(dateFormat)) {
					dateFormat = DateUtil.FORMAT_LONOGRAM;
				}
			}
			if(Constants.CURRENT_DATE.equals(StringUtil.trim(param.getParamDefault()).toLowerCase()))
			{//当前日期
				String currentDate = DateUtil.getNow(StringUtil.isNotEmpty(dateFormat)?dateFormat:DateUtil.FORMAT_LONOGRAM);
				param.setParamDefault(currentDate);
				param.setDateFormat(StringUtil.isNotEmpty(param.getDateFormat())?param.getDateFormat():DateUtil.FORMAT_LONOGRAM);
			}else if(DefaultDateTypeEnum.WF.getCode().equals(StringUtil.trim(param.getParamDefault()).toLowerCase()))
			{//本周第一天
				String currentDate = DateUtil.getWeekStart();
				currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), dateFormat);
				param.setParamDefault(currentDate);
				param.setDateFormat(StringUtil.isNotEmpty(param.getDateFormat())?param.getDateFormat():DateUtil.FORMAT_LONOGRAM);
			}else if(DefaultDateTypeEnum.WL.getCode().equals(StringUtil.trim(param.getParamDefault()).toLowerCase()))
			{//本周最后一天
				String currentDate = DateUtil.getWeekEnd();
				currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), dateFormat);
				param.setParamDefault(currentDate);
				param.setDateFormat(StringUtil.isNotEmpty(param.getDateFormat())?param.getDateFormat():DateUtil.FORMAT_LONOGRAM);
			}else if(DefaultDateTypeEnum.MF.getCode().equals(StringUtil.trim(param.getParamDefault()).toLowerCase()))
			{//本月第一天
				String currentDate = DateUtil.getMonthStart();
				currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), dateFormat);
				param.setParamDefault(currentDate);
				param.setDateFormat(StringUtil.isNotEmpty(param.getDateFormat())?param.getDateFormat():DateUtil.FORMAT_LONOGRAM);
			}else if(DefaultDateTypeEnum.ML.getCode().equals(StringUtil.trim(param.getParamDefault()).toLowerCase()))
			{//本月最后一天
				String currentDate = DateUtil.getMonthEnd();
				currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), dateFormat);
				param.setParamDefault(currentDate);
				param.setDateFormat(StringUtil.isNotEmpty(param.getDateFormat())?param.getDateFormat():DateUtil.FORMAT_LONOGRAM);
			}else if(DefaultDateTypeEnum.SF.getCode().equals(StringUtil.trim(param.getParamDefault()).toLowerCase()))
			{//本季度第一天
				String currentDate = DateUtil.getQuarterStart(new Date());
				currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), dateFormat);
				param.setParamDefault(currentDate);
				param.setDateFormat(StringUtil.isNotEmpty(param.getDateFormat())?param.getDateFormat():DateUtil.FORMAT_LONOGRAM);
			}else if(DefaultDateTypeEnum.SL.getCode().equals(StringUtil.trim(param.getParamDefault()).toLowerCase()))
			{//本季度最后一天
				String currentDate = DateUtil.getQuarterEnd(new Date());
				currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), dateFormat);
				param.setParamDefault(currentDate);
				param.setDateFormat(StringUtil.isNotEmpty(param.getDateFormat())?param.getDateFormat():DateUtil.FORMAT_LONOGRAM);
			}else if(DefaultDateTypeEnum.YF.getCode().equals(StringUtil.trim(param.getParamDefault()).toLowerCase()))
			{//本年度第一天
				String currentDate = DateUtil.getYearStart();
				currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), dateFormat);
				param.setParamDefault(currentDate);
				param.setDateFormat(StringUtil.isNotEmpty(param.getDateFormat())?param.getDateFormat():DateUtil.FORMAT_LONOGRAM);
			}else if(DefaultDateTypeEnum.YL.getCode().equals(StringUtil.trim(param.getParamDefault()).toLowerCase()))
			{//本年度最后一天
				String currentDate = DateUtil.getYearEnd();
				currentDate = DateUtil.date2String(DateUtil.string2Date(currentDate), dateFormat);
				param.setParamDefault(currentDate);
				param.setDateFormat(StringUtil.isNotEmpty(param.getDateFormat())?param.getDateFormat():DateUtil.FORMAT_LONOGRAM);
			}else {
				if(CheckUtil.isNumber(param.getParamDefault()))
				{
					int days = Double.valueOf(param.getParamDefault()).intValue();
					if(DateUtil.FORMAT_YEARMONTH.equals(dateFormat))
					{
						String date = DateUtil.addMonth(days, DateUtil.getNow(DateUtil.FORMAT_LONOGRAM),DateUtil.FORMAT_YEARMONTH);
						param.setParamDefault(date);
					}else if(DateUtil.FORMAT_YEAR.equals(dateFormat))
					{
						String date = DateUtil.addYear(days, DateUtil.getNow(DateUtil.FORMAT_LONOGRAM),DateUtil.FORMAT_YEAR);
						param.setParamDefault(date);
					}else {
						String date = DateUtil.addDays(days, DateUtil.getNow(),StringUtil.isNotEmpty(param.getDateFormat())?dateFormat:DateUtil.FORMAT_LONOGRAM);
						if(StringUtil.isNullOrEmpty(param.getDateFormat()))
						{
							param.setDateFormat(DateUtil.FORMAT_LONOGRAM);
						}
						param.setParamDefault(date);
					}
				}else {
					if(StringUtil.isNullOrEmpty(dateFormat)) {
						dateFormat = DateUtil.FORMAT_LONOGRAM;
					}
					if(!DateUtil.isValidDate(param.getParamDefault(),dateFormat))
					{
						param.setParamDefault("");
					}
				}
			}
		}
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
	public BaseEntity saveScreenDesign(SaveScreenTplDto saveScreenTplDto,UserInfoDto userInfoDto) throws JsonProcessingException {
		BaseEntity result = new BaseEntity();
		ScreenTpl exist = this.getById(saveScreenTplDto.getId());
		if(exist == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist",new String[] {"大屏模板"}));
		}
		if(exist.getIsExample().intValue() == YesNoEnum.YES.getCode().intValue() && userInfoDto.getIsAdmin().intValue() != YesNoEnum.YES.getCode().intValue()) {
			//示例模板只允许超级管理员去修改保存
			throw new BizException(StatusCode.FAILURE, "该模板是示例模板，不允许修改，请见谅！");
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
		screenTpl.setIsExample(YesNoEnum.NO.getCode());
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
				content.put("id", UUIDUtil.getUUID());
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
		String shareUrl = MessageUtil.getValue("screen.share.url")+ "&tplId="+shareDto.getTplId()+ "&token="+token;
		String shareMsg = MessageUtil.getValue("info.share.screen", new String[] {shareUrl,userInfoDto.getUserName(),DateUtil.getNow(),YesNoEnum.YES.getCode().intValue() == shareDto.getIsShareForever().intValue()?"永久有效":String.valueOf(shareDto.getShareTime())+"分钟"});
		result.setShareMsg(shareMsg);
		return result;
	}
}