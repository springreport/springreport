package com.springreport.impl.onlinetpl;

import com.springreport.entity.luckysheet.Luckysheet;
import com.springreport.entity.luckysheetonlinecell.LuckysheetOnlineCell;
import com.springreport.entity.onlinetpl.OnlineTpl;
import com.springreport.entity.onlinetplsheet.OnlineTplSheet;
import com.springreport.entity.reportrangeauth.ReportRangeAuth;
import com.springreport.entity.reportrangeauthuser.ReportRangeAuthUser;
import com.springreport.entity.reporttype.ReportType;
import com.springreport.entity.sysuser.SysUser;
import com.springreport.mapper.luckysheet.LuckysheetMapper;
import com.springreport.mapper.onlinetpl.OnlineTplMapper;
import com.springreport.api.coedit.ICoeditService;
import com.springreport.api.luckysheetonlinecell.ILuckysheetOnlineCellService;
import com.springreport.api.onlinetpl.IOnlineTplService;
import com.springreport.api.onlinetplsheet.IOnlineTplSheetService;
import com.springreport.api.reportrangeauth.IReportRangeAuthService;
import com.springreport.api.reportrangeauthuser.IReportRangeAuthUserService;
import com.springreport.api.reporttype.IReportTypeService;
import com.springreport.api.sysuser.ISysUserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;
import com.springreport.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.dto.onlinetpl.MesRangeAuthDto;
import com.springreport.dto.onlinetpl.OnlineTplTreeDto;
import com.springreport.dto.onlinetpl.ResOnlineTplInfo;
import com.springreport.dto.reporttpl.ResLuckySheetTplSettingsDto;
import com.springreport.dto.reporttpl.ResSheetsSettingsDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.MqTypeEnums;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;
import com.springreport.impl.codeit.MqProcessService;

 /**  
* @Description: OnlineTpl服务实现
* @author 
* @date 2023-02-06 08:03:24
* @version V1.0  
 */
@Service
public class OnlineTplServiceImpl extends ServiceImpl<OnlineTplMapper, OnlineTpl> implements IOnlineTplService {
  
	@Autowired
	private IOnlineTplSheetService iOnlineTplSheetService;
	
	@Autowired
	private ILuckysheetOnlineCellService iLuckysheetOnlineCellService;
	
	@Autowired
	private ICoeditService iCoeditService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private MqProcessService mqProcessService;
	
	@Autowired
	private ISysUserService iSysUserService;
	
	@Autowired
	private IReportRangeAuthService iReportRangeAuthService;
	
	@Autowired
	private IReportRangeAuthUserService iReportRangeAuthUserService;
	
	@Value("${merchantmode}")
    private Integer merchantmode;
	
	@Autowired
	private IReportTypeService iReportTypeService;
	
	@Value("${thirdParty.type}")
    private String thirdPartyType;
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public List<OnlineTplTreeDto> tablePagingQuery(ReportType model) {
		List<OnlineTplTreeDto> result = new ArrayList<>();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		QueryWrapper<ReportType> queryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		queryWrapper.eq("type", 3);
		List<ReportType> list = this.iReportTypeService.list(queryWrapper);
		if(ListUtil.isNotEmpty(list)) {
			OnlineTplTreeDto onlineTplTreeDto = null;
			for (int i = 0; i < list.size(); i++) {
				onlineTplTreeDto = new OnlineTplTreeDto();
				onlineTplTreeDto.setId(list.get(i).getId());
				onlineTplTreeDto.setTplName(list.get(i).getReportTypeName());
				onlineTplTreeDto.setIcon("iconfont icon-wenjianjiakai");
				onlineTplTreeDto.setType("1");
				result.add(onlineTplTreeDto);
			}
		}
		QueryWrapper<OnlineTpl> onlineTplWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			onlineTplWrapper.eq("merchant_no", model.getMerchantNo());
		}
		onlineTplWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		onlineTplWrapper.isNull("report_type");
		List<OnlineTpl> tpls = this.list(onlineTplWrapper);
		if(ListUtil.isNotEmpty(tpls)) {
			OnlineTplTreeDto onlineTplTreeDto = null;
			for (int i = 0; i < tpls.size(); i++) {
				onlineTplTreeDto = new OnlineTplTreeDto();
				BeanUtils.copyProperties(tpls.get(i), onlineTplTreeDto);
				onlineTplTreeDto.setIcon("iconfont icon-Excel");
				onlineTplTreeDto.setType("2");
				onlineTplTreeDto.setHasChildren(false);
				result.add(onlineTplTreeDto);
			}
		}
		return result;
	}
	
	@Override
	public List<OnlineTplTreeDto> getChildren(OnlineTpl model) {
		List<OnlineTplTreeDto> result = new ArrayList<>();
		QueryWrapper<OnlineTpl> onlineTplWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			onlineTplWrapper.eq("merchant_no", model.getMerchantNo());
		}
		onlineTplWrapper.eq("report_type", model.getReportType());
		onlineTplWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<OnlineTpl> tpls = this.list(onlineTplWrapper);
		if(ListUtil.isNotEmpty(tpls)) {
			OnlineTplTreeDto onlineTplTreeDto = null;
			for (int i = 0; i < tpls.size(); i++) {
				onlineTplTreeDto = new OnlineTplTreeDto();
				BeanUtils.copyProperties(tpls.get(i), onlineTplTreeDto);
				onlineTplTreeDto.setIcon("iconfont icon-Excel");
				onlineTplTreeDto.setType("2");
				onlineTplTreeDto.setHasChildren(false);
				result.add(onlineTplTreeDto);
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
	public BaseEntity insert(OnlineTpl model) {
		BaseEntity result = new BaseEntity();
		model.setListId(UUIDUtil.getUUID());
		QueryWrapper<OnlineTpl> queryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("tpl_name", model.getTplName().trim());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		OnlineTpl isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"文档名称"}));
		}
		this.save(model);
		Luckysheet luckysheet = new Luckysheet();
		luckysheet.setBlockId(String.valueOf(model.getId()));
		luckysheet.setSheetIndex("Sheet"+UUIDUtil.getUUID());
		luckysheet.setListId(model.getListId());
		luckysheet.setRowSize(Constants.DEFAULT_SHEET_ROW);
		luckysheet.setColumnSize(Constants.DEFAULT_SHEET_COLUMN);
		luckysheet.setSheetName(Constants.DEFAULT_SHEET_NAME);
		luckysheet.setHide(0);
		luckysheet.setStatus(1);
		luckysheet.setSheetOrder(0);
		this.iCoeditService.save(luckysheet);
		redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+luckysheet.getListId()+"_"+luckysheet.getSheetIndex()+"_"+luckysheet.getBlockId(), JSONObject.toJSONString(luckysheet));
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
	public BaseEntity update(OnlineTpl model) {
		BaseEntity result = new BaseEntity();
		QueryWrapper<OnlineTpl> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("id", model.getId());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("tpl_name", model.getTplName().trim());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		OnlineTpl isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"文档名称"}));
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
		OnlineTpl onlineTpl = new OnlineTpl();
		onlineTpl.setId(id);
		onlineTpl.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(onlineTpl);
		BaseEntity result = new BaseEntity();
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		OnlineTpl data = this.getById(id);
		if(data != null)
		{
			redisUtil.del(RedisPrefixEnum.COEDIT.getCode()+data.getListId());
			this.mqProcessService.updateRedisConfigCache(MqTypeEnums.DELDOC.getCode(), null, null, data.getListId(), String.valueOf(data.getId()),null,null);
			this.mqProcessService.batchProcessCellData(MqTypeEnums.DELDOC.getCode(), null, null, data.getListId(), String.valueOf(data.getId()));
		}
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
		List<OnlineTpl> list = new ArrayList<OnlineTpl>();
		for (int i = 0; i < ids.size(); i++) {
			OnlineTpl onlineTpl = new OnlineTpl();
			onlineTpl.setId(ids.get(i));
			onlineTpl.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(onlineTpl);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @MethodName: getOnlineTplSettings
	 * @Description: 获取在线文档内容
	 * @author caiyang
	 * @param onlineTpl
	 * @param userInfoDto
	 * @return
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @see com.springreport.api.onlinetpl.IOnlineTplService#getOnlineTplSettings(com.springreport.entity.onlinetpl.OnlineTpl, com.springreport.base.UserInfoDto)
	 * @date 2023-02-07 09:33:29 
	 */ 
	@Override
	@Transactional
	public ResSheetsSettingsDto getOnlineTplSettings(OnlineTpl onlineTpl, UserInfoDto userInfoDto) throws JsonMappingException, JsonProcessingException {
		ResSheetsSettingsDto settings = new ResSheetsSettingsDto();
		List<ResLuckySheetTplSettingsDto> list = new ArrayList<>();
		onlineTpl = this.getById(onlineTpl.getId());
		if(onlineTpl == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"该文档"}));
		}
		//获取所有sheet
		QueryWrapper<OnlineTplSheet> sheetQueryWrapper = new QueryWrapper<>();
		sheetQueryWrapper.eq("tpl_id", onlineTpl.getId());
		sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<OnlineTplSheet> sheets = this.iOnlineTplSheetService.list(sheetQueryWrapper);
		Map<String, Long> sheetIndexIdMap = new HashMap<>();
		if(!ListUtil.isEmpty(sheets))
		{
			this.processSheetCells(sheets, onlineTpl, list,sheetIndexIdMap);
		}else {
			OnlineTplSheet onlineTplSheet = new OnlineTplSheet();
			onlineTplSheet.setTplId(onlineTpl.getId());
			onlineTplSheet.setSheetName("sheet1");
			onlineTplSheet.setSheetIndex("Sheet"+UUIDUtil.getUUID());
			onlineTplSheet.setSheetOrder(0);
			this.iOnlineTplSheetService.save(onlineTplSheet);
			sheets.add(onlineTplSheet);
			this.processSheetCells(sheets, onlineTpl, list,sheetIndexIdMap);
			sheetIndexIdMap.put(onlineTplSheet.getSheetIndex(), onlineTplSheet.getId());
		}
		settings.setSettings(list);
		settings.setSheetIndexIdMap(sheetIndexIdMap);
		return settings;
	}
	
	private void processSheetCells(List<OnlineTplSheet> sheets,OnlineTpl onlineTpl,List<ResLuckySheetTplSettingsDto> list,Map<String, Long> sheetIndexIdMap) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		for (int t = 0; t < sheets.size(); t++) {
			ResLuckySheetTplSettingsDto result = new ResLuckySheetTplSettingsDto();
			QueryWrapper<LuckysheetOnlineCell> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("tpl_id", onlineTpl.getId());
			queryWrapper.eq("sheet_id", sheets.get(t).getId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<LuckysheetOnlineCell> cells = this.iLuckysheetOnlineCellService.list(queryWrapper);
			if(!ListUtil.isEmpty(cells))
			{
				List<Map<String, Object>> cellDatas = new ArrayList<Map<String, Object>>();
				Map<String, Map<String, Object>> hyperlinks = new HashMap<String, Map<String, Object>>();
				Map<String, Object> merge = null;
				for (LuckysheetOnlineCell luckysheetOnlineCell : cells) {
					Map<String, Object> cellData = new HashMap<>();
					cellData.put("r", luckysheetOnlineCell.getCoordsx());
					cellData.put("c", luckysheetOnlineCell.getCoordsy());
					Map<String, Object> v = new HashMap<>();
					if(StringUtil.isNotEmpty(luckysheetOnlineCell.getCellData()))
					{
						v.put("v", luckysheetOnlineCell.getCellData());
						v.put("m", luckysheetOnlineCell.getCellData());
					}
					if(StringUtil.isNotEmpty(luckysheetOnlineCell.getCt()))
					{
						v.put("ct", objectMapper.readValue(luckysheetOnlineCell.getCt(), JSONObject.class));
					}
					if(StringUtil.isNotEmpty(luckysheetOnlineCell.getBg()))
					{
						v.put("bg", luckysheetOnlineCell.getBg());
					}
					if(StringUtil.isNotEmpty(luckysheetOnlineCell.getFf()))
					{
						v.put("ff", luckysheetOnlineCell.getFf());
					}
					if(StringUtil.isNotEmpty(luckysheetOnlineCell.getFc()))
					{
						v.put("fc", luckysheetOnlineCell.getFc());
					}
					if(luckysheetOnlineCell.getBl() != null)
					{
						v.put("bl", luckysheetOnlineCell.getBl());
					}
					if(luckysheetOnlineCell.getIt() != null)
					{
						v.put("it", luckysheetOnlineCell.getIt());
					}
					if(luckysheetOnlineCell.getFs() != null)
					{
						v.put("fs", luckysheetOnlineCell.getFs());
					}
					if(luckysheetOnlineCell.getCl() != null)
					{
						v.put("cl", luckysheetOnlineCell.getCl());
					}
					if(luckysheetOnlineCell.getVt() != null)
					{
						v.put("vt", luckysheetOnlineCell.getVt());
					}
					if(luckysheetOnlineCell.getHt() != null)
					{
						v.put("ht", luckysheetOnlineCell.getHt());
					}
					cellData.put("v", v);
					cellDatas.add(cellData);
				}
				result.setCellDatas(cellDatas);
			}
			Map<String, Object> config = new HashMap<>();
			if(StringUtil.isNotEmpty(sheets.get(t).getRowlen()))
			{
				config.put("rowlen", objectMapper.readValue(sheets.get(t).getRowlen(), Map.class));
			}
			if(StringUtil.isNotEmpty(sheets.get(t).getColumnlen()))
			{
				config.put("columnlen", objectMapper.readValue(sheets.get(t).getColumnlen(), Map.class));
			}
			if(StringUtil.isNotEmpty(sheets.get(t).getMerge()))
			{
				config.put("merge", objectMapper.readValue(sheets.get(t).getMerge(), Map.class));
			}
			if(StringUtil.isNotEmpty(sheets.get(t).getBorderInfo()))
			{
				config.put("borderInfo", objectMapper.readValue(sheets.get(t).getBorderInfo(), List.class));
			}
			result.setConfig(config);
			if(StringUtil.isNotEmpty(sheets.get(t).getImages()))
			{
				result.setImages(objectMapper.readValue(sheets.get(t).getImages(), Object.class));
			}else {
				result.setImages(null);
			}
			if(StringUtil.isNullOrEmpty(sheets.get(t).getSheetIndex()))
			{
				result.setSheetIndex("Sheet"+UUIDUtil.getUUID());
			}else {
				result.setSheetIndex(sheets.get(t).getSheetIndex());
			}
			if(StringUtil.isNotEmpty(sheets.get(t).getCalcChain()))
			{
				result.setCalcChain(JSONArray.parseArray(sheets.get(t).getCalcChain()));
			}
			result.setSheetName(sheets.get(t).getSheetName());
			result.setSheetOrder(sheets.get(t).getSheetOrder());
			sheetIndexIdMap.put(sheets.get(t).getSheetIndex(), sheets.get(t).getId());
			list.add(result);
		}
	}


	/**  
	 * @MethodName: getOnlineTplInfo
	 * @Description: 获取文档信息
	 * @author caiyang
	 * @param onlineTpl
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.onlinetpl.IOnlineTplService#getOnlineTplInfo(com.springreport.entity.onlinetpl.OnlineTpl, com.springreport.base.UserInfoDto)
	 * @date 2024-03-09 10:20:14 
	 */
	@Override
	public ResOnlineTplInfo getOnlineTplInfo(OnlineTpl onlineTpl, UserInfoDto userInfoDto) {
		ResOnlineTplInfo result = new ResOnlineTplInfo();
		QueryWrapper<OnlineTpl> onlineTplQueryWrapper = new QueryWrapper<>();
		onlineTplQueryWrapper.eq("list_id", onlineTpl.getListId());
		onlineTpl = this.getOne(onlineTplQueryWrapper);
		if(onlineTpl != null)
		{
			boolean isCreator = false;
			if(StringUtil.isNotEmpty(userInfoDto.getUserName()) && userInfoDto.getUserName().equals(this.thirdPartyType)) {
				isCreator = true;
				result.setIsThirdParty(YesNoEnum.YES.getCode());
			}else {
				isCreator = onlineTpl.getCreator().longValue() == userInfoDto.getUserId().longValue();
			}
			result.setCreator(isCreator);
			SysUser sysUser = this.iSysUserService.getById(onlineTpl.getCreator());
			if(sysUser != null)
			{
				result.setCreatorName(sysUser.getUserName());
			}
			//获取权限信息
			JSONObject sheetRangeAuth = new JSONObject();
			if(isCreator) {
				QueryWrapper<ReportRangeAuth> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("tpl_id", onlineTpl.getId());
				queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<ReportRangeAuth> reportRangeAuths = this.iReportRangeAuthService.list(queryWrapper);
				if(ListUtil.isNotEmpty(reportRangeAuths))
				{
					List<Long> authIds = new ArrayList<>();
					for (int i = 0; i < reportRangeAuths.size(); i++) {
						authIds.add(reportRangeAuths.get(i).getId());
					}
					//获取权限对应的用户信息
					QueryWrapper<ReportRangeAuthUser> rangeAuthUserQueryWrapper = new QueryWrapper<>();
					rangeAuthUserQueryWrapper.eq("tpl_id", onlineTpl.getId());
					rangeAuthUserQueryWrapper.in("range_auth_id", authIds);
					rangeAuthUserQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					List<ReportRangeAuthUser> reportRangeAuthUsers = this.iReportRangeAuthUserService.list(rangeAuthUserQueryWrapper);
					Map<Long, List<ReportRangeAuthUser>> groupDatas = null;
					if(ListUtil.isNotEmpty(reportRangeAuthUsers))
					{
						groupDatas = reportRangeAuthUsers.stream().collect(Collectors.groupingBy(ReportRangeAuthUser::getRangeAuthId));
					}
					for (int i = 0; i < reportRangeAuths.size(); i++) {
						JSONObject rangeAxis = new JSONObject();
						rangeAxis.put("rangeAxis", reportRangeAuths.get(i).getRangeTxt());
						JSONObject range = new JSONObject();
						range.put("row", JSON.parseArray(reportRangeAuths.get(i).getRowsNo()));
						range.put("column", JSON.parseArray(reportRangeAuths.get(i).getColsNo()));
						rangeAxis.put("range", range);
						rangeAxis.put("sheetIndex", reportRangeAuths.get(i).getSheetIndex());
						List<ReportRangeAuthUser> datas = groupDatas.get(reportRangeAuths.get(i).getId());
						if(ListUtil.isNotEmpty(datas))
						{
							JSONArray userIds = new JSONArray();
							JSONObject userAuth = new JSONObject();
							for (int j = 0; j < datas.size(); j++) {
								userIds.add(datas.get(j).getUserId());
								userAuth.put(String.valueOf(datas.get(j).getUserId()), datas.get(j).getAuthType());
							}
							rangeAxis.put("userIds", userIds);
							rangeAxis.put("userAuth", userAuth);
						}
						if(!sheetRangeAuth.containsKey(reportRangeAuths.get(i).getSheetIndex())) {
							sheetRangeAuth.put(reportRangeAuths.get(i).getSheetIndex(), new JSONObject());
						}
						sheetRangeAuth.getJSONObject(reportRangeAuths.get(i).getSheetIndex()).put(reportRangeAuths.get(i).getRangeTxt(), rangeAxis);
					}
				}
			}else {
				//获取用户对应的权限
				QueryWrapper<ReportRangeAuthUser> rangeAuthUserQueryWrapper = new QueryWrapper<>();
				rangeAuthUserQueryWrapper.eq("tpl_id", onlineTpl.getId());
				rangeAuthUserQueryWrapper.eq("user_id", userInfoDto.getUserId());
				rangeAuthUserQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<ReportRangeAuthUser> reportRangeAuthUsers = this.iReportRangeAuthUserService.list(rangeAuthUserQueryWrapper);
				if(ListUtil.isNotEmpty(reportRangeAuthUsers))
				{
					JSONObject rangeAuthType = new JSONObject();
					List<Long> authRangeIds = new ArrayList<>();
					for (int i = 0; i < reportRangeAuthUsers.size(); i++) {
						authRangeIds.add(reportRangeAuthUsers.get(i).getRangeAuthId());
						rangeAuthType.put(String.valueOf(reportRangeAuthUsers.get(i).getRangeAuthId()), reportRangeAuthUsers.get(i).getAuthType());
					}
					//获取对应的权限操作范围
					QueryWrapper<ReportRangeAuth> queryWrapper = new QueryWrapper<>();
					queryWrapper.in("id", authRangeIds);
					List<ReportRangeAuth> reportRangeAuths = this.iReportRangeAuthService.list(queryWrapper);
					if(ListUtil.isNotEmpty(reportRangeAuths))
					{
						for (int i = 0; i < reportRangeAuths.size(); i++) {
							JSONObject rangeAxis = new JSONObject();
							rangeAxis.put("rangeAxis", reportRangeAuths.get(i).getRangeTxt());
							JSONObject range = new JSONObject();
							range.put("row", JSON.parseArray(reportRangeAuths.get(i).getRowsNo()));
							range.put("column", JSON.parseArray(reportRangeAuths.get(i).getColsNo()));
							rangeAxis.put("range", range);
							rangeAxis.put("sheetIndex", reportRangeAuths.get(i).getSheetIndex());
							if(rangeAuthType.containsKey(String.valueOf(reportRangeAuths.get(i).getId()))) {
								rangeAxis.put("authType", rangeAuthType.getInteger(String.valueOf(reportRangeAuths.get(i).getId())));
							}else {
								rangeAxis.put("authType", 1);
							}
							if(!sheetRangeAuth.containsKey(reportRangeAuths.get(i).getSheetIndex())) {
								sheetRangeAuth.put(reportRangeAuths.get(i).getSheetIndex(), new JSONObject());
							}
							
							sheetRangeAuth.getJSONObject(reportRangeAuths.get(i).getSheetIndex()).put(reportRangeAuths.get(i).getRangeTxt(), rangeAxis);
						}
					}
				}
			}
			result.setSheetRangeAuth(sheetRangeAuth);
			result.setTplName(onlineTpl.getTplName());
		}
		return result;
	}


	/**  
	 * @MethodName: rangeAuth
	 * @Description: 协同文档选区授权
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @see com.springreport.api.onlinetpl.IOnlineTplService#rangeAuth(com.springreport.dto.onlinetpl.MesRangeAuthDto, com.springreport.base.UserInfoDto)
	 * @date 2024-03-09 01:16:45 
	 */
	@Override
	@Transactional
	public void rangeAuth(MesRangeAuthDto model, UserInfoDto userInfoDto) {
		QueryWrapper<OnlineTpl> onlineTplQueryWrapper = new QueryWrapper<>();
		onlineTplQueryWrapper.eq("list_id", model.getListId());
		OnlineTpl onlineTpl = this.getOne(onlineTplQueryWrapper);
		if(onlineTpl != null && onlineTpl.getCreator().longValue() == userInfoDto.getUserId().longValue())
		{
			if(model.getRangeAuth() != null)
			{
				String rangeAxis = model.getRangeAuth().getString("rangeAxis");
				String sheetIndex = model.getRangeAuth().getString("sheetIndex");
				JSONArray userIds = model.getRangeAuth().getJSONArray("userIds");
				JSONObject userAuthType = model.getRangeAuth().getJSONObject("userAuthType");
				QueryWrapper<ReportRangeAuth> rangeAuthQueryWrapper = new QueryWrapper<>();
				rangeAuthQueryWrapper.eq("tpl_id", onlineTpl.getId());
				rangeAuthQueryWrapper.eq("sheet_index", sheetIndex);
				rangeAuthQueryWrapper.eq("range_txt", rangeAxis);
				ReportRangeAuth rangeAuth = this.iReportRangeAuthService.getOne(rangeAuthQueryWrapper);
				if(rangeAuth == null)
				{
					rangeAuth = new ReportRangeAuth();
					rangeAuth.setId(IdWorker.getId());
				}else {
					//删除用户授权信息
					QueryWrapper<ReportRangeAuthUser> reportRangeAuthUserQueryWrapper = new QueryWrapper<>();
					reportRangeAuthUserQueryWrapper.eq("tpl_id", onlineTpl.getId());
					reportRangeAuthUserQueryWrapper.eq("range_auth_id", rangeAuth.getId());
					this.iReportRangeAuthUserService.remove(reportRangeAuthUserQueryWrapper);
				}
				rangeAuth.setTplId(onlineTpl.getId());
				rangeAuth.setSheetIndex(sheetIndex);
				rangeAuth.setRangeTxt(rangeAxis);
				rangeAuth.setRowsNo(JSON.toJSONString(model.getRangeAuth().getJSONObject("range").getJSONArray("row")));
				rangeAuth.setColsNo(JSON.toJSONString(model.getRangeAuth().getJSONObject("range").getJSONArray("column")));
				rangeAuth.setType(2);
				List<ReportRangeAuthUser> rangeAuthUsers = new ArrayList<>();
				ReportRangeAuthUser rangeAuthUser = null;
				for (int i = 0; i < userIds.size(); i++) {
					rangeAuthUser = new ReportRangeAuthUser();
					rangeAuthUser.setTplId(onlineTpl.getId());
					rangeAuthUser.setRangeAuthId(rangeAuth.getId());
					rangeAuthUser.setUserId(userIds.getLong(i));
					rangeAuthUser.setType(2);
					rangeAuthUser.setAuthType(userAuthType.getInteger(userIds.getString(i)));
					rangeAuthUsers.add(rangeAuthUser);
				}
				this.iReportRangeAuthService.saveOrUpdate(rangeAuth);
				this.iReportRangeAuthUserService.saveBatch(rangeAuthUsers);
			}
			
		}
		
	}


	/**  
	 * @MethodName: deletRangeAuth
	 * @Description: 删除选区授权
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @see com.springreport.api.onlinetpl.IOnlineTplService#deletRangeAuth(com.springreport.dto.onlinetpl.MesRangeAuthDto, com.springreport.base.UserInfoDto)
	 * @date 2024-03-09 05:23:35 
	 */
	@Override
	public void deletRangeAuth(MesRangeAuthDto model, UserInfoDto userInfoDto) {
		QueryWrapper<OnlineTpl> onlineTplQueryWrapper = new QueryWrapper<>();
		onlineTplQueryWrapper.eq("list_id", model.getListId());
		OnlineTpl onlineTpl = this.getOne(onlineTplQueryWrapper);
		if(onlineTpl != null && onlineTpl.getCreator().longValue() == userInfoDto.getUserId().longValue())
		{
			if(model.getRangeAuth() != null) {
				String rangeAxis = model.getRangeAuth().getString("rangeAxis");
				String sheetIndex = model.getRangeAuth().getString("sheetIndex");
				QueryWrapper<ReportRangeAuth> rangeAuthQueryWrapper = new QueryWrapper<>();
				rangeAuthQueryWrapper.eq("tpl_id", onlineTpl.getId());
				rangeAuthQueryWrapper.eq("sheet_index", sheetIndex);
				rangeAuthQueryWrapper.eq("range_txt", rangeAxis);
				ReportRangeAuth rangeAuth = this.iReportRangeAuthService.getOne(rangeAuthQueryWrapper);
				if(rangeAuth != null) {
					this.iReportRangeAuthService.removeById(rangeAuth.getId());
					//删除用户授权信息
					QueryWrapper<ReportRangeAuthUser> reportRangeAuthUserQueryWrapper = new QueryWrapper<>();
					reportRangeAuthUserQueryWrapper.eq("tpl_id", onlineTpl.getId());
					reportRangeAuthUserQueryWrapper.eq("range_auth_id", rangeAuth.getId());
					this.iReportRangeAuthUserService.remove(reportRangeAuthUserQueryWrapper);
				}
			}
		}
		
	}

	/**  
	 * @MethodName: getCoeditAuth
	 * @Description: 获取协同文档授权范围
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.coedit.ICoeditService#getCoeditAuth(com.springreport.entity.onlinetpl.OnlineTpl, com.springreport.base.UserInfoDto)
	 * @date 2024-03-11 12:01:22 
	 */
	@Override
	public JSONObject getCoeditAuth(OnlineTpl model, UserInfoDto userInfoDto) {
		QueryWrapper<OnlineTpl> onlineTplQueryWrapper = new QueryWrapper<>();
		onlineTplQueryWrapper.eq("list_id", model.getListId());
		model = this.getOne(onlineTplQueryWrapper, false);
		JSONObject sheetRangeAuth = new JSONObject();
		if(model != null)
		{
			//获取用户对应的权限
			QueryWrapper<ReportRangeAuthUser> rangeAuthUserQueryWrapper = new QueryWrapper<>();
			rangeAuthUserQueryWrapper.eq("tpl_id", model.getId());
			rangeAuthUserQueryWrapper.eq("user_id", userInfoDto.getUserId());
			rangeAuthUserQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<ReportRangeAuthUser> reportRangeAuthUsers = this.iReportRangeAuthUserService.list(rangeAuthUserQueryWrapper);
			if(ListUtil.isNotEmpty(reportRangeAuthUsers))
			{
				List<Long> authRangeIds = new ArrayList<>();
				JSONObject rangeAuthType = new JSONObject();
				for (int i = 0; i < reportRangeAuthUsers.size(); i++) {
					authRangeIds.add(reportRangeAuthUsers.get(i).getRangeAuthId());
					rangeAuthType.put(String.valueOf(reportRangeAuthUsers.get(i).getRangeAuthId()), reportRangeAuthUsers.get(i).getAuthType());
				}
				//获取对应的权限操作范围
				QueryWrapper<ReportRangeAuth> queryWrapper = new QueryWrapper<>();
				queryWrapper.in("id", authRangeIds);
				List<ReportRangeAuth> reportRangeAuths = this.iReportRangeAuthService.list(queryWrapper);
				if(ListUtil.isNotEmpty(reportRangeAuths))
				{
					for (int i = 0; i < reportRangeAuths.size(); i++) {
						JSONObject rangeAxis = new JSONObject();
						rangeAxis.put("rangeAxis", reportRangeAuths.get(i).getRangeTxt());
						JSONObject range = new JSONObject();
						range.put("row", JSON.parseArray(reportRangeAuths.get(i).getRowsNo()));
						range.put("column", JSON.parseArray(reportRangeAuths.get(i).getColsNo()));
						rangeAxis.put("range", range);
						rangeAxis.put("sheetIndex", reportRangeAuths.get(i).getSheetIndex());
						if(!sheetRangeAuth.containsKey(reportRangeAuths.get(i).getSheetIndex())) {
							sheetRangeAuth.put(reportRangeAuths.get(i).getSheetIndex(), new JSONObject());
						}
						if(rangeAuthType.containsKey(String.valueOf(reportRangeAuths.get(i).getId()))) {
							rangeAxis.put("authType", rangeAuthType.getInteger(String.valueOf(reportRangeAuths.get(i).getId())));
						}else {
							rangeAxis.put("authType", 1);
						}
						sheetRangeAuth.getJSONObject(reportRangeAuths.get(i).getSheetIndex()).put(reportRangeAuths.get(i).getRangeTxt(), rangeAxis);
					}
				}
			}
		}
		return sheetRangeAuth;
	}

}