package com.springreport.impl.reporttpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.aviator.AviatorEvaluator;
import com.springreport.api.luckysheetreportformscell.ILuckysheetReportFormsCellService;
import com.springreport.api.luckysheetreportformshis.ILuckysheetReportFormsHisService;
import com.springreport.api.reportdatasource.IReportDatasourceService;
import com.springreport.api.reportdatasourcedictdata.IReportDatasourceDictDataService;
import com.springreport.api.reportformsdatasource.IReportFormsDatasourceService;
import com.springreport.api.reportformsdatasourceattrs.IReportFormsDatasourceAttrsService;
import com.springreport.api.reportsheetpdfprintsetting.IReportSheetPdfPrintSettingService;
import com.springreport.api.reporttpl.IReportTplFormsService;
import com.springreport.api.reporttpl.IReportTplService;
import com.springreport.api.reporttpldataset.IReportTplDatasetService;
import com.springreport.api.reporttplsheet.IReportTplSheetService;
import com.springreport.api.sysrolesheet.ISysRoleSheetService;
import com.springreport.base.BaseEntity;
import com.springreport.base.DataSourceConfig;
import com.springreport.base.ReportDataColumnDto;
import com.springreport.base.ReportDataDetailDto;
import com.springreport.base.TDengineConnection;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.dto.reporttpl.GroupSummaryData;
import com.springreport.dto.reporttpl.LuckySheetBindData;
import com.springreport.dto.reporttpl.LuckySheetFormsBindData;
import com.springreport.dto.reporttpl.MesGenerateReportDto;
import com.springreport.dto.reporttpl.ReportCellDictsDto;
import com.springreport.dto.reporttpl.ReportDataDto;
import com.springreport.dto.reporttpl.ResLuckySheetDataDto;
import com.springreport.dto.reporttpl.ResPreviewData;
import com.springreport.dto.reporttpl.ResReportDataDto;
import com.springreport.entity.luckysheetreportformscell.LuckysheetReportFormsCell;
import com.springreport.entity.reportdatasource.ReportDatasource;
import com.springreport.entity.reportdatasourcedictdata.ReportDatasourceDictData;
import com.springreport.entity.reportformsdatasource.ReportFormsDatasource;
import com.springreport.entity.reportformsdatasourceattrs.ReportFormsDatasourceAttrs;
import com.springreport.entity.reportsheetpdfprintsetting.ReportSheetPdfPrintSetting;
import com.springreport.entity.reporttpl.ReportTpl;
import com.springreport.entity.reporttpldataset.ReportTplDataset;
import com.springreport.entity.reporttplsheet.ReportTplSheet;
import com.springreport.entity.sysrolesheet.SysRoleSheet;
import com.springreport.enums.AggregateTypeEnum;
import com.springreport.enums.BorderTypeEnum;
import com.springreport.enums.CellExtendEnum;
import com.springreport.enums.CellValueTypeEnum;
import com.springreport.enums.DatasetTypeEnum;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.FunctionExpressEnum;
import com.springreport.enums.FunctionTypeEnum;
import com.springreport.enums.LuckySheetPropsEnum;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.enums.SqlTypeEnum;
import com.springreport.enums.TplTypeEnum;
import com.springreport.enums.ValueTypeEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;
import com.springreport.mapper.luckysheetreportformscell.LuckysheetReportFormsCellMapper;
import com.springreport.report.CellUtil;
import com.springreport.report.calculate.Calculate;
import com.springreport.report.calculate.GroupAddCalculate;
import com.springreport.report.calculate.GroupAvgCalculate;
import com.springreport.report.calculate.GroupCountCalculate;
import com.springreport.report.calculate.GroupMaxCalculate;
import com.springreport.report.calculate.GroupMinCalculate;
import com.springreport.report.calculate.LuckySheetFormsAddCalculate;
import com.springreport.report.calculate.LuckySheetFormsAvgCalculate;
import com.springreport.report.calculate.LuckySheetFormsCountCalculate;
import com.springreport.report.calculate.LuckySheetFormsMaxCalculate;
import com.springreport.report.calculate.LuckySheetFormsMinCalculate;
import com.springreport.report.dataprocess.LuckySheetBasicDynamicDataProcess;
import com.springreport.report.dataprocess.LuckySheetListDataProcess;
import com.springreport.util.CheckUtil;
import com.springreport.util.CusAccessObjectUtil;
import com.springreport.util.DateUtil;
import com.springreport.util.HttpClientUtil;
import com.springreport.util.InfluxDBConnection;
import com.springreport.util.JdbcUtils;
import com.springreport.util.ListUtil;
import com.springreport.util.LuckysheetUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.ParamUtil;
import com.springreport.util.RedisUtil;
import com.springreport.util.ReportDataUtil;
import com.springreport.util.StringUtil;



@Service
public class ReportTplFormsServiceImpl implements IReportTplFormsService{
	
	@Autowired
	private IReportTplService iReportTplService;
	
	@Autowired
	private IReportTplSheetService iReportTplSheetService;
	
	@Autowired
	private ISysRoleSheetService iSysRoleSheetService;
	
	@Autowired
	private ILuckysheetReportFormsCellService iLuckysheetReportFormsCellService;
	
	@Autowired
	private IReportTplDatasetService iReportTplDatasetService;
	
	@Autowired
	private IReportDatasourceService iReportDatasourceService;
	
	@Autowired
	private IReportFormsDatasourceService iReportFormsDatasourceService;
	
	@Autowired
	private IReportFormsDatasourceAttrsService iReportFormsDatasourceAttrsService;
	
	@Autowired
	private IReportDatasourceDictDataService iReportDatasourceDictDataService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private ILuckysheetReportFormsHisService iLuckysheetReportFormsHisService;
	
	@Autowired
	protected HttpServletRequest httpServletRequest;
	
	@Autowired
	private LuckysheetReportFormsCellMapper luckysheetReportFormsCellMapper;
	
	@Autowired
	private IReportSheetPdfPrintSettingService iReportSheetPdfPrintSettingService;
	
	private static Map<Integer,LuckySheetBasicDynamicDataProcess> luckySheetDataProcess=new HashMap<Integer,LuckySheetBasicDynamicDataProcess>();
	
	private static Map<Integer, Calculate> luckySheetCalculates = new HashMap<Integer, Calculate>();
	
	private static Map<Integer, Calculate> luckySheetGroupCalculates = new HashMap<Integer, Calculate>();
	
	static{
		luckySheetDataProcess.put(TplTypeEnum.LIST.getCode(),new LuckySheetListDataProcess());
		luckySheetDataProcess.put(TplTypeEnum.FORMS.getCode(),new LuckySheetListDataProcess());
	}
	
	static{
		luckySheetCalculates.put(FunctionTypeEnum.SUM.getCode(), new LuckySheetFormsAddCalculate());
		luckySheetCalculates.put(FunctionTypeEnum.AVG.getCode(), new LuckySheetFormsAvgCalculate());
		luckySheetCalculates.put(FunctionTypeEnum.MAX.getCode(), new LuckySheetFormsMaxCalculate());
		luckySheetCalculates.put(FunctionTypeEnum.MIN.getCode(), new LuckySheetFormsMinCalculate());
		luckySheetCalculates.put(FunctionTypeEnum.COUNT.getCode(), new LuckySheetFormsCountCalculate());
	}
	
	static{
		luckySheetGroupCalculates.put(FunctionTypeEnum.SUM.getCode(), new GroupAddCalculate());
		luckySheetGroupCalculates.put(FunctionTypeEnum.AVG.getCode(), new GroupAvgCalculate());
		luckySheetGroupCalculates.put(FunctionTypeEnum.MAX.getCode(), new GroupMaxCalculate());
		luckySheetGroupCalculates.put(FunctionTypeEnum.MIN.getCode(), new GroupMinCalculate());
		luckySheetGroupCalculates.put(FunctionTypeEnum.COUNT.getCode(), new GroupCountCalculate());
	}
	

	/**  
	 * @MethodName: previewLuckysheetReportFormsData
	 * @Description: 填报报表预览数据
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param userInfoDto
	 * @return
	 * @throws Exception 
	 * @see com.springreport.api.reporttpl.IReportTplFormsService#previewLuckysheetReportFormsData(com.springreport.dto.reporttpl.MesGenerateReportDto, com.springreport.base.UserInfoDto)
	 * @date 2022-11-17 04:11:57 
	 */  
	@Override
	public ResPreviewData previewLuckysheetReportFormsData(MesGenerateReportDto mesGenerateReportDto,
			UserInfoDto userInfoDto,ReportTpl reportTpl,boolean isPagination) throws Exception {
		ResPreviewData result = this.generateLuckySheetReportFormsData(mesGenerateReportDto, isPagination, userInfoDto,reportTpl);
		return result;
	}
	
	private ResPreviewData generateLuckySheetReportFormsData (MesGenerateReportDto mesGenerateReportDto,boolean isPagination,UserInfoDto userInfoDto,ReportTpl reportTpl) throws Exception
	{
		ResPreviewData resPreviewData = new ResPreviewData();
		List<ResLuckySheetDataDto> sheetDatas = new ArrayList<>();
		if(reportTpl.getConcurrencyFlag().intValue() == YesNoEnum.YES.getCode())
		{//该报表提交数据时需要控制防止多人编辑，先后提交数据覆盖的问题
			Long version = null;
			if(redisUtil.hasKey(RedisPrefixEnum.REPORTDATAVERSION.getCode()+reportTpl.getId()))
			{
				version =  new Long(String.valueOf(redisUtil.get(RedisPrefixEnum.REPORTDATAVERSION.getCode()+reportTpl.getId())));
			}else {
				version = redisUtil.increment(RedisPrefixEnum.REPORTDATAVERSION.getCode()+reportTpl.getId());
			}
			resPreviewData.setVersion(version);;
		}
		//获取所有有权限的sheet
		List<ReportTplSheet> sheets = null;
		if(userInfoDto.getIsAdmin().intValue() == YesNoEnum.YES.getCode() || reportTpl.getViewAuth().intValue() == 1)
		{//超级管理员
			QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
			sheetQueryWrapper.eq("tpl_id", reportTpl.getId());
			sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			if(StringUtil.isNotEmpty(mesGenerateReportDto.getSheetIndex()))
			{
				sheetQueryWrapper.eq("sheet_index", mesGenerateReportDto.getSheetIndex());
			}
			sheets = this.iReportTplSheetService.list(sheetQueryWrapper);
		}else {
			QueryWrapper<SysRoleSheet> roleSheetQueryWrapper = new QueryWrapper<>();
			roleSheetQueryWrapper.eq("report_id", reportTpl.getId());
			roleSheetQueryWrapper.eq("role_id", userInfoDto.getRoleId());
			roleSheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<SysRoleSheet> roleSheets = this.iSysRoleSheetService.list(roleSheetQueryWrapper);
			if(ListUtil.isEmpty(roleSheets))
			{
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.noauth.view.report"));
			}
			List<Long> sheetIds = new ArrayList<>();
			for (int i = 0; i < roleSheets.size(); i++) {
				sheetIds.add(roleSheets.get(i).getSheetId());
			}
			QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
			sheetQueryWrapper.in("id", sheetIds);
			sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			if(StringUtil.isNotEmpty(mesGenerateReportDto.getSheetIndex()))
			{
				sheetQueryWrapper.eq("sheet_index", mesGenerateReportDto.getSheetIndex());
			}
			sheets = this.iReportTplSheetService.list(sheetQueryWrapper);
		}
		Map<String, JSONObject> imgCells = new HashMap<>();
		Map<String, Object> mergePagination = new HashMap<String, Object>();
		int currentPage = -1;
		int pageSize = -1;
		List<Map<String, String>> reportSqls = new ArrayList<>();
		if(!ListUtil.isEmpty(sheets))
		{
			//获取数据字典
			ReportCellDictsDto cellDictsDto = this.getReportDict(sheets);
			for (int t = 0; t < sheets.size(); t++) {
				ResLuckySheetDataDto resLuckySheetDataDto = new ResLuckySheetDataDto();
				Map<String, JSONArray> cellConditionFormat = new HashMap<>();//动态单元格对应的条件格式
				Map<String, String> fixedCellsMap = new HashMap<>();//所有的静态单元格
				Map<String, String> variableCellsMap = new HashMap<>();//所有的动态单元格
				//获取所有的变量单元格
				QueryWrapper<LuckysheetReportFormsCell> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("tpl_id", reportTpl.getId());
				queryWrapper.eq("sheet_id", sheets.get(t).getId());
				queryWrapper.eq("cell_value_type", CellValueTypeEnum.VARIABLE.getCode());
				queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<LuckysheetReportFormsCell> list = this.iLuckysheetReportFormsCellService.list(queryWrapper);
				List<String> usedDataSet = new ArrayList<String>();//存放所有数据集
				HashSet<String> hs = new LinkedHashSet<>();
				if (!ListUtil.isEmpty(list)) {
					for (int i = 0; i < list.size(); i++) {
						String[] datesetNames = list.get(i).getDatasetName().split(",");
						for (String datasetName : datesetNames) {
							hs.add(datasetName);
						}
					}
					usedDataSet = new ArrayList<>(hs);
				}
				Map<String, String> datasetNameIdMap = new HashMap<String, String>();
				List<List<String>> columnNames = this.iReportTplService.getTplDatasetsColumnNames(reportTpl.getId(),datasetNameIdMap,userInfoDto);
//				Map<String, Map<String, Integer>> paginationMap = new HashMap<String, Map<String,Integer>>();
//				Map<String, Integer> mergePagination = new HashMap<String, Integer>();
				Map<String, List<Map<String, Object>>> datasetDatas = new HashMap<String, List<Map<String,Object>>>();//数据集对应的原始数据
				Map<String, Map<String, List<List<Map<String, Object>>>>> processedCells = new HashMap<>();
				//查询每个数据集对应的数据
				if(!ListUtil.isEmpty(usedDataSet))
				{
					List<LuckySheetFormsBindData> dataSetsBindDatas = new ArrayList<>();//所有数据集绑定的数据
					for (int i = 0; i < usedDataSet.size(); i++) {
						List<Map<String, Object>> datas = null;
						Map<String, Object> result = null;
						if(!datasetDatas.containsKey(usedDataSet.get(i)))
						{
							result = this.getDatasetDatas(reportTpl, mesGenerateReportDto, usedDataSet.get(i), isPagination, mergePagination,reportSqls,userInfoDto);
 							datas = (List<Map<String, Object>>) result.get("datas");
							datasetDatas.put(usedDataSet.get(i), datas);
						}else {
							datas = datasetDatas.get(usedDataSet.get(i));
						}
						//获取数据集对应的变量
						LuckysheetReportFormsCell cell = new LuckysheetReportFormsCell();
						cell.setTplId(reportTpl.getId());
						cell.setSheetId(sheets.get(t).getId());
						cell.setDatasetName(usedDataSet.get(i));
						List<LuckysheetReportFormsCell> variableCells = this.luckysheetReportFormsCellMapper.getVariableCells(cell);
						if(!ListUtil.isEmpty(variableCells)) {
							for (int j = 0; j < variableCells.size(); j++) {
								if(CellValueTypeEnum.VARIABLE.getCode().intValue() == variableCells.get(j).getCellValueType())
								{
									Map<String, String> map = ListUtil.getNewCellValue(variableCells.get(j).getCellValue(), columnNames, datasetNameIdMap);
									variableCells.get(j).setCellValue(map.get("cellValue"));
									variableCells.get(j).setDatasetName(map.get("datasetNames"));
									variableCellsMap.put(variableCells.get(j).getCoordsx() + "_" + variableCells.get(j).getCoordsy(), variableCells.get(j).getCoordsx() + "_" + variableCells.get(j).getCoordsy());
								}else {
									if(StringUtil.isNotEmpty(variableCells.get(j).getDatasetName()))
									{
										String datasetName = "";
										String[] datasetNames = LuckysheetUtil.getDatasetNames(variableCells.get(j).getDatasetName());
										for (int k = 0; k < datasetNames.length; k++) {
											if(datasetNameIdMap.containsKey(datasetNames[k]))
											{
												if(StringUtil.isNullOrEmpty(datasetName))
												{
													datasetName = datasetNameIdMap.get(datasetNames[k])+"_"+datasetNames[k];
												}else {
													datasetName = datasetName + "," + datasetNameIdMap.get(datasetNames[k])+"_"+datasetNames[k];
												}
											}else {
												if(StringUtil.isNullOrEmpty(datasetName))
												{
													datasetName = datasetNames[k];
												}else {
													datasetName = datasetName + "," + datasetNames[k];
												}
											}
										}
										variableCells.get(j).setDatasetName(datasetName);
									}
								}
							}
							String datasetName = usedDataSet.get(i);
							if(datasetNameIdMap.containsKey(usedDataSet.get(i)))
							{
								datasetName = datasetNameIdMap.get(usedDataSet.get(i))+"-"+usedDataSet.get(i);
							}
							List<LuckySheetFormsBindData> dataSetBindDatas = luckySheetDataProcess.get(reportTpl.getTplType().intValue()).processForms(variableCells, datas,datasetName,processedCells,sheets.get(t).getSheetIndex());
							if(!ListUtil.isEmpty(dataSetBindDatas))
							{
								dataSetsBindDatas.addAll(dataSetBindDatas);
							}
						}
					}
					ObjectMapper objectMapper = null;
					//获取所有固定的单元格,并封装成bindata与动态数据组成一个list
					QueryWrapper<LuckysheetReportFormsCell> fixed = new QueryWrapper<>();
					fixed.eq("tpl_id", reportTpl.getId());
					fixed.eq("sheet_id", sheets.get(t).getId());
					fixed.eq("cell_value_type", CellValueTypeEnum.FIXED.getCode());
					fixed.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					fixed.orderByAsc("coordsx","coordsy");
					List<LuckysheetReportFormsCell> fixedCells = this.iLuckysheetReportFormsCellService.list(fixed);
					if(!ListUtil.isEmpty(fixedCells))
					{
						if(objectMapper == null)
						{
							objectMapper = new ObjectMapper();
						}
						LuckySheetFormsBindData bindData = null;
						for (int i = 0; i < fixedCells.size(); i++) {
							bindData = new LuckySheetFormsBindData();
							bindData.setReportCellId(fixedCells.get(i).getId());
							bindData.setCoordsx(fixedCells.get(i).getCoordsx());
							bindData.setCoordsy(fixedCells.get(i).getCoordsy());
							bindData.setCellValueType(CellValueTypeEnum.FIXED.getCode());
							bindData.setCellValue(fixedCells.get(i).getCellValue());
							JSONObject cellAttrs = JSONObject.parseObject(fixedCells.get(i).getCellAttrs());
							//2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//							boolean warning = cellAttrs.getBooleanValue("warning");
//							bindData.setWarning(warning);
//							String warningRules = cellAttrs.getString("warningRules");
//							if(StringUtil.isNullOrEmpty(warningRules))
//							{
//								warningRules = ">=";
//							}
//							bindData.setWarningRules(warningRules);
//							String threshold = cellAttrs.getString("threshold");
//							if(StringUtil.isNullOrEmpty(threshold))
//							{
//								threshold = "80";
//							}
//							bindData.setThreshold(threshold);
//							String warningColor = cellAttrs.getString("warningColor");
//							if(StringUtil.isNullOrEmpty(warningColor))
//							{
//								warningColor = "#FF0000";
//							}
//							bindData.setWarningColor(warningColor);
//							String warningContent = cellAttrs.getString("warningContent");
//							bindData.setWarningContent(warningContent);
							JSONArray cellconditions = cellAttrs.getJSONArray("cellconditions");
							if(!ListUtil.isEmpty(cellconditions))
							{
								bindData.setIsConditions(YesNoEnum.YES.getCode());
								bindData.setCellConditions(JSON.toJSONString(cellconditions));
							}else {
								bindData.setIsConditions(YesNoEnum.NO.getCode());
							}
							bindData.setGroupProperty(cellAttrs.getString("groupProperty"));
							boolean unitTransfer = cellAttrs.getBooleanValue("unitTransfer");
							bindData.setUnitTransfer(unitTransfer);
							Integer transferType = cellAttrs.getInteger("transferType");
							bindData.setTransferType(transferType==null?1:transferType);
							String multiple = cellAttrs.getString("multiple");
							bindData.setMultiple(StringUtil.isNullOrEmpty(multiple)?"100":multiple);
							
							bindData.setIsMerge(fixedCells.get(i).getIsMerge());
							bindData.setRowSpan(fixedCells.get(i).getRowSpan());
							bindData.setColSpan(fixedCells.get(i).getColSpan());
							bindData.setIsFunction(fixedCells.get(i).getIsFunction());
							try {
								bindData.setCellData(objectMapper.readValue(fixedCells.get(i).getCellData(), Map.class));
							} catch (Exception e) {
								e.printStackTrace();
								throw new BizException(StatusCode.FAILURE, "单元格数据解析失败，请检查单元格数据格式是否正确！");
							}
							dataSetsBindDatas.add(bindData);
							fixedCellsMap.put(fixedCells.get(i).getCoordsx() + "_" + fixedCells.get(i).getCoordsy(), fixedCells.get(i).getCoordsx() + "_" + fixedCells.get(i).getCoordsy());
						}
					}
					JSONArray conditionForamt = JSONArray.parseArray(sheets.get(t).getConditionformatSave());
					if(ListUtil.isNotEmpty(conditionForamt)) {
						for (int i = 0; i < conditionForamt.size(); i++) {
							JSONObject formatObj = JSON.parseObject(JSON.toJSONString(conditionForamt.getJSONObject(i)));
							JSONArray cellRange = conditionForamt.getJSONObject(i).getJSONArray("cellrange");
							for (int j = 0; j < cellRange.size(); j++) {
								JSONArray column = cellRange.getJSONObject(j).getJSONArray("column");
								JSONArray row = cellRange.getJSONObject(j).getJSONArray("row");
								for (int k = row.getIntValue(0); k <= row.getIntValue(1); k++) {
									for (int k2 = column.getIntValue(0); k2 <= column.getIntValue(1); k2++) {
										String key = k+"_"+k2;
										if(variableCellsMap.containsKey(key)) {
											if(cellConditionFormat.containsKey(key)) {
												cellConditionFormat.get(key).add(formatObj);
											}else {
												JSONArray conditions = new JSONArray();
												conditions.add(formatObj);
												cellConditionFormat.put(k+"_"+k2, conditions);
											}
										}
									}
								}
							}
						}
					}
					QueryWrapper<LuckysheetReportFormsCell> allCellsQueryWrapper = new QueryWrapper<>();
					allCellsQueryWrapper.eq("tpl_id", reportTpl.getId());
					allCellsQueryWrapper.eq("sheet_id", sheets.get(t).getId());
					allCellsQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					allCellsQueryWrapper.orderByAsc("coordsx","coordsy");
					List<LuckysheetReportFormsCell> allCells = this.iLuckysheetReportFormsCellService.list(allCellsQueryWrapper);
					if(objectMapper == null)
					{
						objectMapper = new ObjectMapper();
					}
					Map<String, Object> config = new HashMap<String, Object>();
					if(StringUtil.isNotEmpty(sheets.get(t).getConfig()))
					{
						config = objectMapper.readValue(sheets.get(t).getConfig(), Map.class);
					}
					resLuckySheetDataDto = this.buildLuckysheetDatas(allCells, dataSetsBindDatas,config,cellDictsDto.getCellDictsValueLabel(),sheets.get(t).getSheetIndex(),reportTpl.getId(),currentPage > 0,currentPage,pageSize,cellConditionFormat);
				}else {
					//没有数据集查询所有的静态单元格
					//获取所有固定的单元格,并封装成bindata与动态数据组成一个list
					List<LuckySheetFormsBindData> dataSetsBindDatas = new ArrayList<>();//所有数据集绑定的数据
					QueryWrapper<LuckysheetReportFormsCell> fixed = new QueryWrapper<>();
					fixed.eq("tpl_id", reportTpl.getId());
					fixed.eq("sheet_id", sheets.get(t).getId());
					fixed.eq("cell_value_type", CellValueTypeEnum.FIXED.getCode());
					fixed.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					fixed.orderByAsc("coordsx","coordsy");
					List<LuckysheetReportFormsCell> fixedCells = this.iLuckysheetReportFormsCellService.list(fixed);
					if(!ListUtil.isEmpty(fixedCells))
					{
						ObjectMapper objectMapper = new ObjectMapper();
						LuckySheetFormsBindData bindData = null;
						for (int i = 0; i < fixedCells.size(); i++) {
							bindData = new LuckySheetFormsBindData();
							bindData.setReportCellId(fixedCells.get(i).getId());
							bindData.setCoordsx(fixedCells.get(i).getCoordsx());
							bindData.setCoordsy(fixedCells.get(i).getCoordsy());
							bindData.setCellValueType(CellValueTypeEnum.FIXED.getCode());
							bindData.setCellValue(fixedCells.get(i).getCellValue());
							bindData.setIsMerge(fixedCells.get(i).getIsMerge());
							bindData.setRowSpan(fixedCells.get(i).getRowSpan());
							bindData.setColSpan(fixedCells.get(i).getColSpan());
							try {
								bindData.setCellData(objectMapper.readValue(fixedCells.get(i).getCellData(), Map.class));
							} catch (Exception e) {
								e.printStackTrace();
								throw new BizException(StatusCode.FAILURE, "单元格数据解析失败，请检查单元格数据格式是否正确！");
							}
							bindData.setIsFunction(fixedCells.get(i).getIsFunction());
							bindData.setWarning(fixedCells.get(i).getWarning());
							bindData.setWarningContent(fixedCells.get(i).getWarningContent());
							bindData.setWarningColor(fixedCells.get(i).getWarningColor());
							bindData.setThreshold(fixedCells.get(i).getThreshold());
							JSONObject cellAttrs = JSONObject.parseObject(fixedCells.get(i).getCellAttrs());
							//2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//							boolean warning = cellAttrs.getBooleanValue("warning");
//							bindData.setWarning(warning);
//							String warningRules = cellAttrs.getString("warningRules");
//							if(StringUtil.isNullOrEmpty(warningRules))
//							{
//								warningRules = ">=";
//							}
//							bindData.setWarningRules(warningRules);
//							String threshold = cellAttrs.getString("threshold");
//							if(StringUtil.isNullOrEmpty(threshold))
//							{
//								threshold = "80";
//							}
//							bindData.setThreshold(threshold);
//							String warningColor = cellAttrs.getString("warningColor");
//							if(StringUtil.isNullOrEmpty(warningColor))
//							{
//								warningColor = "#FF0000";
//							}
//							bindData.setWarningColor(warningColor);
//							String warningContent = cellAttrs.getString("warningContent");
//							bindData.setWarningContent(warningContent);
							JSONArray cellconditions = cellAttrs.getJSONArray("cellconditions");
							if(!ListUtil.isEmpty(cellconditions))
							{
								bindData.setIsConditions(YesNoEnum.YES.getCode());
								bindData.setCellConditions(JSON.toJSONString(cellconditions));
							}else {
								bindData.setIsConditions(YesNoEnum.NO.getCode());
							}
							bindData.setGroupProperty(cellAttrs.getString("groupProperty"));
							boolean unitTransfer = cellAttrs.getBooleanValue("unitTransfer");
							bindData.setUnitTransfer(unitTransfer);
							Integer transferType = cellAttrs.getInteger("transferType");
							bindData.setTransferType(transferType==null?1:transferType);
							String multiple = cellAttrs.getString("multiple");
							bindData.setMultiple(StringUtil.isNullOrEmpty(multiple)?"100":multiple);
							dataSetsBindDatas.add(bindData);
						}
						QueryWrapper<LuckysheetReportFormsCell> allCellsQueryWrapper = new QueryWrapper<>();
						allCellsQueryWrapper.eq("tpl_id", reportTpl.getId());
						allCellsQueryWrapper.eq("sheet_id", sheets.get(t).getId());
						allCellsQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
						allCellsQueryWrapper.orderByAsc("coordsx","coordsy");
						List<LuckysheetReportFormsCell> allCells = this.iLuckysheetReportFormsCellService.list(allCellsQueryWrapper);
						Map<String, Object> config = new HashMap<String, Object>();
						if(StringUtil.isNotEmpty(sheets.get(t).getConfig()))
						{
							config = objectMapper.readValue(sheets.get(t).getConfig(), Map.class);
						}
						resLuckySheetDataDto = this.buildLuckysheetDatas(allCells, dataSetsBindDatas,config,null,null,reportTpl.getId(),currentPage > 0,currentPage,pageSize,null);
					}
				}
				if(resLuckySheetDataDto.getCalcChain() != null && resLuckySheetDataDto.getCalcChain().size() > 0) {
					for (int j = 0; j < resLuckySheetDataDto.getCalcChain().size(); j++) {
						resLuckySheetDataDto.getCalcChain().get(j).put("index", sheets.get(t).getSheetIndex());
					}
				}
				ObjectMapper objectMapper = new ObjectMapper();
				if(StringUtil.isNotEmpty(sheets.get(t).getImages()))
				{
					JSONObject images = objectMapper.readValue(sheets.get(t).getImages(), JSONObject.class);
					if(!ListUtil.isEmpty(resLuckySheetDataDto.getImageDatas()))
					{
						for (int j = 0; j < resLuckySheetDataDto.getImageDatas().size(); j++) {
							images.put(IdWorker.getIdStr(), resLuckySheetDataDto.getImageDatas().get(j).getJSONObject("imgInfo"));
						}
					}
					resLuckySheetDataDto.setImages(images);
				}else {
					if(!ListUtil.isEmpty(resLuckySheetDataDto.getImageDatas()))
					{
						JSONObject images = new JSONObject();
						for (int j = 0; j < resLuckySheetDataDto.getImageDatas().size(); j++) {
							images.put(IdWorker.getIdStr(), resLuckySheetDataDto.getImageDatas().get(j).getJSONObject("imgInfo"));
						}
						resLuckySheetDataDto.setImages(images);
					}else {
						resLuckySheetDataDto.setImages(null);
					}
				}
				resLuckySheetDataDto.setSheetIndex(sheets.get(t).getSheetIndex());
				resLuckySheetDataDto.setSheetName(sheets.get(t).getSheetName());
				resLuckySheetDataDto.setSheetOrder(sheets.get(t).getSheetOrder());
				mergePagination.put("currentPage", mesGenerateReportDto.getPagination().get("currentPage"));
				QueryWrapper<ReportFormsDatasource> datasourceQueryWrapper = new QueryWrapper<>();
				datasourceQueryWrapper.eq("tpl_id", reportTpl.getId());
				datasourceQueryWrapper.eq("sheet_id", sheets.get(t).getId());
				datasourceQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<ReportFormsDatasource> reportFormsDatasources = this.iReportFormsDatasourceService.list(datasourceQueryWrapper);
				if(!ListUtil.isEmpty(reportFormsDatasources))
				{
					Map<String, JSONObject> cellDatasourceConfig = new HashMap<>();
					Map<String, JSONObject> tableKeys = new HashMap<>();
					for (int i = 0; i < reportFormsDatasources.size(); i++) {
						QueryWrapper<ReportFormsDatasourceAttrs> attrsQueryWrapper = new QueryWrapper<>();
						attrsQueryWrapper.eq("report_forms_datasource_id", reportFormsDatasources.get(i).getId());
						attrsQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
						List<ReportFormsDatasourceAttrs> formsDatasourceAttrs = this.iReportFormsDatasourceAttrsService.list(attrsQueryWrapper);
						if(!ListUtil.isEmpty(formsDatasourceAttrs))
						{
							for (int j = 0; j < formsDatasourceAttrs.size();j++) {
								JSONObject jsonObject = new JSONObject();
								if(formsDatasourceAttrs.get(j).getType().intValue() == 1) {
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("r", formsDatasourceAttrs.get(j).getCoordsx());
									jsonObject.put("c", formsDatasourceAttrs.get(j).getCoordsy());
									jsonObject.put("datasourceId", String.valueOf(reportFormsDatasources.get(i).getDatasourceId()));
									jsonObject.put("table", reportFormsDatasources.get(i).getTableName());
									jsonObject.put("name", reportFormsDatasources.get(i).getName());
									String key = formsDatasourceAttrs.get(j).getCoordsx() + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+formsDatasourceAttrs.get(j).getCoordsy();
									cellDatasourceConfig.put(key, jsonObject);
								}else {
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("idType", formsDatasourceAttrs.get(j).getIdType());
									jsonObject.put("datasourceId", String.valueOf(reportFormsDatasources.get(i).getDatasourceId()));
									jsonObject.put("table", reportFormsDatasources.get(i).getTableName());
									jsonObject.put("name", reportFormsDatasources.get(i).getName());
									String key = String.valueOf(reportFormsDatasources.get(i).getDatasourceId()) + "|"
										+  reportFormsDatasources.get(i).getTableName() + "|"
										+ formsDatasourceAttrs.get(j).getColumnName()+"|"+reportFormsDatasources.get(i).getName();
									tableKeys.put(key, jsonObject);
								}
							}
						}
					}
//					if(!ListUtil.isEmpty(resLuckySheetDataDto.getImageDatas()))
//					{
//						Map<String, Object> rowLen = (Map<String, Object>) resLuckySheetDataDto.getConfig().get(LuckySheetPropsEnum.ROWLEN.getCode());
//						Map<String, Object> colLen = (Map<String, Object>) resLuckySheetDataDto.getConfig().get(LuckySheetPropsEnum.COLUMNLEN.getCode());
////						this.processImageDatas(resLuckySheetDataDto.getImageDatas(),rowLen,colLen,imgCells,sheets.get(t).getSheetIndex());
//					}
					if(resLuckySheetDataDto.getCalcChain() != null && resLuckySheetDataDto.getCalcChain().size() > 0) {
						for (int j = 0; j < resLuckySheetDataDto.getCalcChain().size(); j++) {
							resLuckySheetDataDto.getCalcChain().get(j).put("index", sheets.get(t).getSheetIndex());
						}
					}
					if(StringUtil.isNotEmpty(sheets.get(t).getFrozen()))
					{
						resLuckySheetDataDto.setFrozen(objectMapper.readValue(sheets.get(t).getFrozen(), Map.class));
					}else {
						resLuckySheetDataDto.setFrozen(new HashMap<String, Object>());
					}
//					resLuckySheetDataDto.setCellDatasourceConfig(cellDatasourceConfig);
					resLuckySheetDataDto.setTableKeys(tableKeys);
				}
				//获取打印设置
				QueryWrapper<ReportSheetPdfPrintSetting> pdfPrintQueryWrapper = new QueryWrapper<ReportSheetPdfPrintSetting>();
				pdfPrintQueryWrapper.eq("tpl_id", reportTpl.getId());
				pdfPrintQueryWrapper.eq("tpl_sheet_id", sheets.get(t).getId());
				pdfPrintQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				ReportSheetPdfPrintSetting printSettings = this.iReportSheetPdfPrintSettingService.getOne(pdfPrintQueryWrapper);
				resLuckySheetDataDto.setPrintSettings(printSettings);
				resLuckySheetDataDto.setBase64Imgs(JSON.parseObject(sheets.get(t).getImages()));
				if(printSettings != null && printSettings.getHorizontalPage().intValue() == 1)
				{
					resLuckySheetDataDto.setPageDivider(StringUtil.isNotEmpty(sheets.get(t).getPageDivider())?JSON.parseArray(sheets.get(t).getPageDivider()):new JSONArray());
				}
				if(!StringUtil.isEmptyMap(resLuckySheetDataDto.getBase64Imgs()))
				{
					JSONObject image = new JSONObject();
					Set<String> set = resLuckySheetDataDto.getBase64Imgs().keySet();
					for (String key : set) {
						JSONObject imgInfo = resLuckySheetDataDto.getBase64Imgs().getJSONObject(key);
						Map<String, Object> configColumnLen = (Map<String, Object>) resLuckySheetDataDto.getConfig().get(LuckySheetPropsEnum.COLUMNLEN.getCode());
						Map<String, Object> configRowLen = (Map<String, Object>) resLuckySheetDataDto.getConfig().get(LuckySheetPropsEnum.ROWLEN.getCode());
						JSONObject coords = LuckysheetUtil.calculateImgCoors(imgInfo,configRowLen,resLuckySheetDataDto.getRowhidden(),configColumnLen,resLuckySheetDataDto.getColhidden());
						int str = coords.getIntValue("str");
						int stc = coords.getIntValue("stc");
						int edr = coords.getIntValue("edr");
						int edc = coords.getIntValue("edc");
						if(StringUtil.isEmptyMap(resLuckySheetDataDto.getStartXAndY()))
						{
							Map<String, Integer> startXAndY = new HashMap<>();
							startXAndY.put("x", str);
							startXAndY.put("y", stc);
							resLuckySheetDataDto.setStartXAndY(startXAndY);
						}else {
							if(str < resLuckySheetDataDto.getStartXAndY().get("x"))
							{
								resLuckySheetDataDto.getStartXAndY().put("x", str);
							}
							if(stc < resLuckySheetDataDto.getStartXAndY().get("y"))
							{
								resLuckySheetDataDto.getStartXAndY().put("y", stc);
							}
						}
						if(edr > resLuckySheetDataDto.getMaxXAndY().get("maxX"))
						{
							resLuckySheetDataDto.getMaxXAndY().put("maxX", edr);
						}
						if(edc > resLuckySheetDataDto.getMaxXAndY().get("maxY"))
						{
							resLuckySheetDataDto.getMaxXAndY().put("maxY", edc);
						}
						JSONObject data = new JSONObject();
						data.put("rs", edr-str+1);
						data.put("cs", edc-stc+1);
						image.put(str+"_"+stc, data);
					}
					resLuckySheetDataDto.setImgInfo(image);
				}
				resLuckySheetDataDto.setPageDivider(StringUtil.isNotEmpty(sheets.get(t).getPageDivider())?JSON.parseArray(sheets.get(t).getPageDivider()):new JSONArray());
				if(!StringUtil.isEmptyMap(cellConditionFormat)) {
					JSONArray luckysheetConditionformatSave = new JSONArray();
					cellConditionFormat.forEach((key, value) -> {
						luckysheetConditionformatSave.addAll(value);
					});
					resLuckySheetDataDto.setLuckysheetConditionformatSave(luckysheetConditionformatSave);
				}
				sheetDatas.add(resLuckySheetDataDto);
				resPreviewData.setCellDictsLabelValue(cellDictsDto.getCellDictsLabelValue());
			}
		}
		resPreviewData.setSheetDatas(sheetDatas);
		resPreviewData.setImgCells(imgCells);
		resPreviewData.setIsParamMerge(reportTpl.getIsParamMerge());
		resPreviewData.setPagination(mergePagination);
		resPreviewData.setTplName(reportTpl.getTplName());
		resPreviewData.setShowToolbar(reportTpl.getShowToolbar());
		resPreviewData.setShowRowHeader(reportTpl.getShowRowHeader());
		resPreviewData.setShowColHeader(reportTpl.getShowColHeader());
		resPreviewData.setShowGridlines(reportTpl.getShowGridlines());
		resPreviewData.setReportSqls(reportSqls);
		return resPreviewData;
	}
	
	/**  
	 * @MethodName: processImageDatas
	 * @Description: 处理数据中的图片信息
	 * @author caiyang
	 * @param images
	 * @param rowlen
	 * @param columnlen 
	 * @return void
	 * @date 2023-01-06 02:39:18 
	 */  
	private void processImageDatas(List<JSONObject> images,Map<String, Object> rowlen,Map<String, Object> columnlen,Map<String, JSONObject> imgCells,String sheetIndex)
	{
		if(!ListUtil.isEmpty(images))
		{
			for (int i = 0; i < images.size(); i++) {
				JSONObject img = images.get(i);
				int r = img.getIntValue("r");//横坐标
				int c = img.getIntValue("c");//纵坐标
				int isMerge = img.getIntValue("isMerge");
				//计算图片的距离左侧和上方的距离
//				double left = 0;
//				double top = 0;
				double imgHeight = img.getDoubleValue("height");//图片单元格高度
				double imgWidth = img.getDoubleValue("width");//图片单元格高度
				int extend = img.getIntValue("extend");
				if(YesNoEnum.YES.getCode().intValue() == isMerge)
				{
					int rowSpan = img.getIntValue("rowSpan");
					int colSpan = img.getIntValue("colSpan");
					for (int j = 0; j < rowSpan; j++) {
						if(j != 0)
						{
							Object len = rowlen.get(String.valueOf(r+j));
							if(len == null)
							{
								imgHeight = imgHeight + Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT;
							}else {
								imgHeight = imgHeight + Double.parseDouble(String.valueOf(len));
							}
						}
					}
					for (int j = 0; j < colSpan; j++) {
						if(j != 0)
						{
							Object len = columnlen.get(String.valueOf(c+j));
							if(len == null)
							{
								imgWidth = imgWidth + Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH;
							}else {
								imgWidth = imgWidth + Double.parseDouble(String.valueOf(len));
							}
						}
					}
				}
				
//				for (int j = 0; j < r; j++) {
//					Object len =  rowlen.get(String.valueOf(j));
//					if(len == null)
//					{
//						top = top + Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT;
//					}else {
//						top = top + Double.parseDouble(String.valueOf(len));
//					}
//				}
//				if(1 == extend)
//				{
//					top = top + r;	
//				}
//				for (int j = 0; j < c; j++) {
//					Object len = columnlen.get(String.valueOf(j));
//					if(len == null)
//					{
//						left = left + Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH;
//					}else {
//						left = left + Double.parseDouble(String.valueOf(len));
//					}
//				}
//				if(2 == extend)
//				{
//					left = left + c;
//				}
				JSONObject imgInfo = img.getJSONObject("imgInfo");
//				JSONObject defaultInfo = imgInfo.getJSONObject("default");
//				defaultInfo.put("left", left);
//				defaultInfo.put("top", top);
//				defaultInfo.put("width", imgWidth);
//				defaultInfo.put("height", imgHeight);
				JSONObject pic = new JSONObject();
//				pic.put("left", left);
//				pic.put("top", top);
				pic.put("width", imgWidth);
				pic.put("height", imgHeight);
				String url = imgInfo.getString("src");
				String imgBase64 = StringUtil.imgToBase64(url);
				pic.put("imgBase64", imgBase64);
				imgCells.put(sheetIndex + "_" + r + "_" + c, pic);
			}
		}
	}
	
	/**  
	 * @MethodName: getReportDict
	 * @Description: 获取报表的数据字典
	 * @author caiyang
	 * @param sheets 
	 * @return void
	 * @date 2022-11-28 09:43:56 
	 */  
	private ReportCellDictsDto getReportDict(List<ReportTplSheet> sheets) {
		ReportCellDictsDto result = new ReportCellDictsDto();
		List<Long> sheetIds = new ArrayList<>();
		Map<Long, String> sheetIndexMap = new HashMap<>();
		for (int i = 0; i < sheets.size(); i++) {
			sheetIds.add(sheets.get(i).getId());
			sheetIndexMap.put(sheets.get(i).getId(), sheets.get(i).getSheetIndex());
		}
		QueryWrapper<LuckysheetReportFormsCell> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("sheet_id", sheetIds);
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<LuckysheetReportFormsCell> list = this.iLuckysheetReportFormsCellService.list(queryWrapper);
		if(!ListUtil.isEmpty(list))
		{
			Map<String, Map<String, String>> cellDictsValueLabel = new HashMap<String, Map<String,String>>();
			Map<String, Map<String, String>> cellDictsLabelValue = new HashMap<String, Map<String,String>>();
			for (int i = 0; i < list.size(); i++) {
				JSONObject cellAttrs = JSONObject.parseObject(list.get(i).getCellAttrs());
				int valueType = cellAttrs.getIntValue("valueType");
				if(valueType == 4)
				{
					Long datasourceId = cellAttrs.getLong("datasourceId");
					String dictType = cellAttrs.getString("dictType");
					List<ReportDatasourceDictData> dictDatas = null;
					if(datasourceId != null && StringUtil.isNotEmpty(dictType))
					{
						dictDatas = (List<ReportDatasourceDictData>) redisUtil.get(datasourceId+"_"+dictType);
						if(ListUtil.isEmpty(dictDatas))
						{
							QueryWrapper<ReportDatasourceDictData> dictDataQueryWrapper = new QueryWrapper<>();
							dictDataQueryWrapper.eq("datasource_id", datasourceId);
							dictDataQueryWrapper.eq("dict_type", dictType);
							dictDataQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
							dictDatas = this.iReportDatasourceDictDataService.list(dictDataQueryWrapper);
							if(!ListUtil.isEmpty(dictDatas))
							{
								redisUtil.set(datasourceId+"_"+dictType, dictDatas);
								Map<String, String> valueLabelMap = new HashMap<>();
								Map<String, String> labelValueMap = new HashMap<>();
								for (int j = 0; j < dictDatas.size(); j++) {
									valueLabelMap.put(dictDatas.get(j).getDictValue(), dictDatas.get(j).getDictLabel());
									labelValueMap.put(dictDatas.get(j).getDictLabel(), dictDatas.get(j).getDictValue());
									cellDictsValueLabel.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), valueLabelMap);
									cellDictsLabelValue.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), labelValueMap);
								}
							}
						}else {
							Map<String, String> valueLabelMap = new HashMap<>();
							Map<String, String> labelValueMap = new HashMap<>();
							for (int j = 0; j < dictDatas.size(); j++) {
								valueLabelMap.put(dictDatas.get(j).getDictValue(), dictDatas.get(j).getDictLabel());
								labelValueMap.put(dictDatas.get(j).getDictLabel(), dictDatas.get(j).getDictValue());
								cellDictsValueLabel.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), valueLabelMap);
								cellDictsLabelValue.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), labelValueMap);
							}
						}
					}
				}
			}
			result.setCellDictsLabelValue(cellDictsLabelValue);
			result.setCellDictsValueLabel(cellDictsValueLabel);
		}
		return result;
	}
	/**  
	 * @Title: getDatasetParamInfo
	 * @Description: 获取数据集的参数信息
	 * @param usedDatasetName
	 * @param mesGenerateReportDto
	 * @return
	 * @author caiyang
	 * @date 2021-06-18 04:24:20 
	 */ 
	private Map<String, Object> getDatasetParamInfo(String usedDatasetName,MesGenerateReportDto mesGenerateReportDto){
		Map<String, Object> result = null;
		if(ListUtil.isEmpty(mesGenerateReportDto.getSearchData()))
		{
			return result;
		}else {
			for (int i = 0; i < mesGenerateReportDto.getSearchData().size(); i++) {
				if(usedDatasetName.equals(mesGenerateReportDto.getSearchData().get(i).get("datasetName")))
				{
					result = mesGenerateReportDto.getSearchData().get(i);
					break;
				}
			}
		}
		return result;
	}
	
	/**
	*<p>Title: getViewParams</p>
	*<p>Description: 获取页面的参数</p>
	* @author caiyang
	* @param jsonArray
	* @return
	*/
	private Map<String, Object> getViewParams(JSONArray jsonArray)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject param = jsonArray.getJSONObject(i);
			String key = param.getString("paramCode");
			Object value = param.get(key);
			result.put(key, value);
		}
		return result;
	}
	
	private ResLuckySheetDataDto buildLuckysheetDatas(List<LuckysheetReportFormsCell> allCells,List<LuckySheetFormsBindData> dataSetsBindDatas,
			Map<String, Object> config,Map<String, Map<String, String>> cellDictsValueLabel,String sheetIndex,Long tplId,boolean isPagination,
			int currentPage,int pageSize,Map<String, JSONArray> cellConditionFormat) throws IOException{
		ResLuckySheetDataDto result = new ResLuckySheetDataDto();
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, LuckySheetFormsBindData> binddataMap = CellUtil.luckySheetFormsBindDataCoordinateMap(dataSetsBindDatas);
		//将dataSetsBindDatas按照allCells的顺序重新排列
		List<LuckySheetFormsBindData> sortedBindData = new ArrayList<>();
		Map<String, JSONObject> extraCustomCellConfigs = new HashMap<>();//额外单元格配置
		Map<String, Boolean> allowEditConfigs = new HashMap<>();//是否允许单元格进行编辑配置信息
		Map<String, JSONObject> extendCellOrigin = new HashMap<>();//扩展单元格的坐标与原始单元格坐标的对应关系
		Map<String, JSONObject> columnStartCoords = new HashMap<>();//列对应的起始坐标
		if(!ListUtil.isEmpty(allCells))
		{
			for (int i = 0; i < allCells.size(); i++) {
				LuckySheetFormsBindData bindData = binddataMap.get(allCells.get(i).getCoordsx() + "-" + allCells.get(i).getCoordsy());
				if(bindData != null)
				{
					sortedBindData.add(bindData);
				}else {
					processExtendCellOrigin(extendCellOrigin,allCells.get(i));
					String originCell = allCells.get(i).getCoordsx() + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() + allCells.get(i).getCoordsy();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("r", allCells.get(i).getCoordsx());
					jsonObject.put("c", allCells.get(i).getCoordsy());
					columnStartCoords.put(originCell, jsonObject);
				}
				if(allCells.get(i).getCellAttrs() != null)
				{
					JSONObject extraCustomCellConfig = objectMapper.readValue(allCells.get(i).getCellAttrs(), JSONObject.class);
					Boolean allowEdit = extraCustomCellConfig.getBoolean("allowEdit");
					if(allowEdit == null || allowEdit)
					{
						allowEditConfigs.put(allCells.get(i).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+allCells.get(i).getCoordsy(), true);
					}else {
						allowEditConfigs.put(allCells.get(i).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+allCells.get(i).getCoordsy(), false);
					}
					extraCustomCellConfigs.put(allCells.get(i).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+allCells.get(i).getCoordsy(), extraCustomCellConfig);
				}
			}
		}
		result.setExtraCustomCellConfigs(extraCustomCellConfigs);
		result.setAllowEditConfigs(allowEditConfigs);
		Map<String, Map<String, Object>> mergeMap = new HashMap<String, Map<String,Object>>();//合并单元格
		Map<String, Object> borderInfo = new HashMap<String, Object>();//边框
		List<Object> borderInfos = new ArrayList<Object>();
		Object colhidden = config.get(LuckySheetPropsEnum.COLHIDDEN.getCode());
		Object rowhidden = config.get(LuckySheetPropsEnum.ROWHIDDEN.getCode());
		Map<String, Object> configRowLen = config.get(LuckySheetPropsEnum.ROWLEN.getCode()) != null?(HashMap<String, Object>) config.get(LuckySheetPropsEnum.ROWLEN.getCode()):new HashMap<String, Object>() ;
		Map<String, Object> configColumnLen = config.get(LuckySheetPropsEnum.COLUMNLEN.getCode()) != null?(HashMap<String, Object>) config.get(LuckySheetPropsEnum.COLUMNLEN.getCode()):new HashMap<String, Object>();
		List<Map<String, Object>> borderConfig = config.get(LuckySheetPropsEnum.BORDERINFO.getCode()) != null?(List<Map<String, Object>>)config.get(LuckySheetPropsEnum.BORDERINFO.getCode()):new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> cellDatas = new ArrayList<Map<String,Object>>();//单元格数据
		Map<String, Object> rowlen = new HashMap<String, Object>();//行高
		Map<String, Object> columnlen = new HashMap<String, Object>();//列宽
		Map<String, Integer> maxCoordinate = new HashMap<String, Integer>();//坐标对应的最大值，横坐标 x-+坐标值:纵坐标的值，纵坐标y-+坐标值：横坐标的值，
		Map<String, Integer> maxXAndY = new HashMap<String, Integer>();
		List<JSONObject> calcChain = new ArrayList<>();//计算链
		List<JSONObject> images = new ArrayList<>();
		JSONObject drillCells = new JSONObject();//下钻报表单元格
		Map<String, Integer> startXAndY = new HashMap<>();//起始横坐标和纵坐标
		Map<String, Object> replacedData = new HashMap<>();//被缓存替换的数据 
		Map<String, JSONObject> cacheDatas = new HashMap<>();//缓存数据
		String keyPattern = "";
		if(currentPage > 0)
		{
			keyPattern = RedisPrefixEnum.REPORTCELLDATATEMPCACHE.getCode()+tplId+"-"+sheetIndex+"-"+currentPage+"-"+pageSize;
		}else {
			keyPattern = RedisPrefixEnum.REPORTCELLDATATEMPCACHE.getCode()+tplId+"-"+sheetIndex;
		}
		List<String> keys = redisUtil.getKeys(keyPattern);
		if(!ListUtil.isEmpty(keys))
		{
			List<Object> redisDatas = redisUtil.multiGet(keys);
			if(!ListUtil.isEmpty(redisDatas))
			{
				for (int j = 0; j < redisDatas.size(); j++) {
					JSONObject cellData = JSON.parseObject(redisDatas.get(j).toString());
					int r = cellData.getIntValue("r");
					int c = cellData.getIntValue("c");
					cacheDatas.put(keyPattern+"-"+r+"-"+c, cellData);
				}
			}
		}
		maxXAndY.put("maxX", 0);//最大横坐标
		maxXAndY.put("maxY", 0);//最大纵坐标
		for (int i = 0; i < sortedBindData.size(); i++) {
			if(i == 0)
			{
				startXAndY.put("x", sortedBindData.get(i).getCoordsx());
				startXAndY.put("y", sortedBindData.get(i).getCoordsy());
			}
			Object dataRowLen = configRowLen.get(String.valueOf(sortedBindData.get(i).getCoordsx()));
			Object dataColLen = configColumnLen.get(String.valueOf(sortedBindData.get(i).getCoordsy()));
			if(CellValueTypeEnum.FIXED.getCode().intValue() == sortedBindData.get(i).getCellValueType())
			{//固定值
				this.processFixedValue(maxCoordinate, sortedBindData.get(i), mergeMap,configRowLen, configColumnLen, 
						rowlen, columnlen, cellDatas, null,dataRowLen,dataColLen,maxXAndY,borderInfo,borderConfig,borderInfos,calcChain,extendCellOrigin,columnStartCoords
						,images,rowhidden,colhidden,cacheDatas,keyPattern,replacedData);
			}else {//动态值
				if(AggregateTypeEnum.SUMMARY.getCode().equals(sortedBindData.get(i).getAggregateType()))
				{
					this.processSummaryValue(maxCoordinate, sortedBindData.get(i), cellDatas, rowlen, columnlen,dataRowLen,dataColLen,
							maxXAndY,borderInfo,borderConfig,borderInfos,extendCellOrigin,columnStartCoords,mergeMap,configRowLen,configColumnLen,cellConditionFormat);
				}else if(AggregateTypeEnum.GROUPSUMMARY.getCode().equals(sortedBindData.get(i).getAggregateType())) {
					this.processGroupSummaryValue(maxCoordinate, sortedBindData.get(i), mergeMap, cellDatas, configRowLen, configColumnLen, rowlen, columnlen, 
							objectMapper, dataRowLen, dataColLen,maxXAndY,borderInfo,borderConfig,borderInfos,extendCellOrigin,columnStartCoords,drillCells,cellConditionFormat);
				}else {
					this.processListGroupValue(maxCoordinate, sortedBindData.get(i), cellDatas, null, dataRowLen, dataColLen, rowlen, columnlen, mergeMap, objectMapper,
							maxXAndY,borderInfo,borderConfig,borderInfos,calcChain,extendCellOrigin,columnStartCoords,cellDictsValueLabel,sheetIndex,configRowLen,configColumnLen
							,images,rowhidden,colhidden,drillCells,cacheDatas,keyPattern,replacedData,cellConditionFormat);
				}
			}
		}
		if(!StringUtil.isEmptyMap(cacheDatas))
		{
			List<Object> newCellDatas = new ArrayList<>();
			for(Map.Entry entry : cacheDatas.entrySet()){
				newCellDatas.add(entry.getValue());
			}
			result.setNewCellDatas(newCellDatas);
		}
		if(!StringUtil.isEmptyMap(borderInfo))
		{
			borderInfo.forEach((key, value) -> {
				if(value != null) {
					borderInfos.addAll((Collection<? extends Object>) value);
				}
			});
		}
		result.setCellDatas(cellDatas);
		result.setReplacedData(replacedData);
		Map<String, Object> newConfig = new HashMap<String, Object>();
		newConfig.put(LuckySheetPropsEnum.ROWHIDDEN.getCode(), rowhidden);
		newConfig.put(LuckySheetPropsEnum.COLHIDDEN.getCode(), colhidden);
		newConfig.put(LuckySheetPropsEnum.ROWLEN.getCode(), rowlen);
		newConfig.put(LuckySheetPropsEnum.COLUMNLEN.getCode(), columnlen);
		newConfig.put(LuckySheetPropsEnum.MERGECONFIG.getCode(), mergeMap);
		if(!ListUtil.isEmpty(borderInfos))
		{
			newConfig.put(LuckySheetPropsEnum.BORDERINFO.getCode(), borderInfos);
		}
		result.setConfig(newConfig);
		result.setMaxXAndY(maxXAndY);
		result.setExtendCellOrigin(extendCellOrigin);
		result.setColumnStartCoords(columnStartCoords);
		result.setCalcChain(calcChain);
		result.setImageDatas(images);
		result.setDrillCells(drillCells);
		result.setStartXAndY(startXAndY);
		result.setMaxXAndY(maxXAndY);
		result.setColhidden(JSONObject.parseObject(JSON.toJSONString(colhidden)));
		result.setRowhidden(JSONObject.parseObject(JSON.toJSONString(rowhidden)));
		return result;
	}
	
	/**  
	 * @Title: processFixedValue
	 * @Description: 单元格固定值处理
	 * @param maxCoordinate 行坐标对应的列最大值，列坐标对应的行最大值
	 * @param luckySheetBindData 绑定的数据
	 * @param mergeMap 合并单元格数据
	 * @param maxX 最大横坐标
	 * @param maxY 最大纵坐标
	 * @param configRowLen 行宽配置
	 * @param configColumnLen 列高配置
	 * @param rowlen 行宽
	 * @param columnlen 列高
	 * @param cellDatas 单元格对应的数据集合
	 * @param hyperlinks 超链接集合
	 * @param extendCellOrigin 扩展单元格与原始单元格对应关系
	 * @author caiyang
	 * @date 2022-03-20 10:54:30 
	 */ 
	private void processFixedValue(Map<String, Integer> maxCoordinate,LuckySheetFormsBindData luckySheetBindData,
			Map<String, Map<String, Object>> mergeMap,Map<String, Object> configRowLen,
			Map<String, Object> configColumnLen,Map<String, Object> rowlen,Map<String, Object> columnlen,
			List<Map<String, Object>> cellDatas,Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen,Map<String, Integer> maxXAndY,
			Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos,List<JSONObject> calcChain,Map<String, JSONObject> extendCellOrigin
			,Map<String, JSONObject> columnStartCoords, List<JSONObject> images,Object rowhidden,Object colhidden,Map<String, JSONObject> cacheDatas,String keyPattern,Map<String, Object> replacedData) {
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		//固定值
		Map<String, Integer> rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
		luckySheetBindData.getCellData().put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
		luckySheetBindData.getCellData().put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
		this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
		this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		boolean isImg = false;
		Map<String, Object> cellConfig = (Map<String, Object>) luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode());
		Object originalValue = cellConfig.get(LuckySheetPropsEnum.CELLVALUE.getCode());
		if(FunctionExpressEnum.NOW.getCode().equals(luckySheetBindData.getCellValue()))
		{
			Map<String, Object> ct = (Map<String, Object>) cellConfig.get("ct");
			cellConfig.put(LuckySheetPropsEnum.CELLVALUE.getCode(), DateUtil.getNow(LuckysheetUtil.getDateFormat(ct)));
		}else {
			if(originalValue != null)
			{
				Object value = null;
				if(StringUtil.isImgUrl(String.valueOf(originalValue)))
				{
					value = "";
					isImg = true;
				}else {
					try {
						value = AviatorEvaluator.execute(String.valueOf(originalValue));
					} catch (Exception e) {
						
					}
				}
				if(value == null)
				{
					value = originalValue;
				}
				//2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//				if(luckySheetBindData.getWarning())
//				{
//					if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//					{
//						JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//						if(jsonObject != null)
//						{
//							cellConfig.put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//							if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//							{
//								cellConfig.put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//							}else {
//								cellConfig.put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//							}
//						}
//					}
//				}
				if(luckySheetBindData.getUnitTransfer())
				{
					value = this.processUnitTransfer(value, luckySheetBindData);
				}
				cellConfig.put(LuckySheetPropsEnum.CELLVALUE.getCode(), String.valueOf(value));
			}
		}
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue())
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("r", rowAndCol.get("maxX"));
			jsonObject.put("c", rowAndCol.get("maxY"));
			calcChain.add(jsonObject);
		}
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge())
		{
			((Map<String, Object>)(((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			((Map<String, Object>)(((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			Map<String, Object> merge = new HashMap<String, Object>();
			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
			if((rowAndCol.get("maxX") + luckySheetBindData.getRowSpan()-1)> maxX)
			{
				maxX = rowAndCol.get("maxX") + luckySheetBindData.getRowSpan()-1;
			}
			if((rowAndCol.get("maxY") + luckySheetBindData.getColSpan()-1)> maxY)
			{
				maxY = rowAndCol.get("maxY") + luckySheetBindData.getColSpan()-1;
			}
		}else {
			if(rowAndCol.get("maxX")>maxX)
			{
				maxX = rowAndCol.get("maxX");
			}
			if(rowAndCol.get("maxY")>maxY)
			{
				maxY = rowAndCol.get("maxY");
			}
		}
		if(dataRowLen != null)
		{
			if(rowlen.get(String.valueOf(rowAndCol.get("maxX"))) == null)
			{
				rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
			}
		}
		if(dataColLen != null)
		{
			if(columnlen.get(String.valueOf(rowAndCol.get("maxY"))) == null)
			{
				columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
			}
		}
		String cacheKey = keyPattern+"-"+rowAndCol.get("maxX")+"-"+rowAndCol.get("maxY");
		if(cacheDatas.containsKey(cacheKey))
		{
			replacedData.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"),JSON.parse(JSON.toJSONString(luckySheetBindData.getCellData())));
			JSONObject data = cacheDatas.get(cacheKey);
			((Map<String, Object>)(((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())))).put(LuckySheetPropsEnum.CELLVALUE.getCode(), data.get(LuckySheetPropsEnum.CELLVALUE.getCode()));
			((Map<String, Object>)(((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())))).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), data.get(LuckySheetPropsEnum.CELLVALUEM.getCode()));
			cacheDatas.remove(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"));
		}
		cellDatas.add(luckySheetBindData.getCellData());
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge()) {
			for (int t = 0; t < luckySheetBindData.getRowSpan(); t++) {
				for (int j = 0; j < luckySheetBindData.getColSpan(); j++) {
					if(t != 0 || j != 0)
					{
						Map<String, Object> mc = new HashMap<String, Object>();
						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
						Map<String, Object> cellValue = new HashMap<String, Object>();
						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
						Map<String, Object> mergeCellData = new HashMap<String, Object>();
						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+t);
						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+j);
						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
						cellDatas.add(mergeCellData);
						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+j));
						if(mcDataColLen != null)
						{
							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+j))== null)
							{
								columnlen.put(String.valueOf(rowAndCol.get("maxY")+j), mcDataColLen);
							}
						}
					}
				}
				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+t));
				if(mcDataRowLen != null)
				{
					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+t))==null)
					{
						rowlen.put(String.valueOf(rowAndCol.get("maxX")+t), mcDataRowLen);
					}
				}
			}
		}
		if(isImg)
		{
			double top = LuckysheetUtil.calculateTop(rowlen, rowAndCol.get("maxX"),rowhidden);
			double left = LuckysheetUtil.calculateLeft(columnlen, rowAndCol.get("maxY"),colhidden);
			Object width = LuckysheetUtil.calculateWidth(columnlen, rowAndCol.get("maxY"), luckySheetBindData.getColSpan());
			Object height = LuckysheetUtil.calculateHeight(rowlen, rowAndCol.get("maxX"), luckySheetBindData.getRowSpan());
			JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
			imgInfo.getJSONObject("default").put("top", top);
			imgInfo.getJSONObject("default").put("left", left);
			imgInfo.getJSONObject("default").put("width", width);
			imgInfo.getJSONObject("default").put("height", height);
			imgInfo.getJSONObject("crop").put("width", width);
			imgInfo.getJSONObject("crop").put("height", height);
			imgInfo.put("src", originalValue);
			JSONObject img = new JSONObject();
			img.put("imgInfo", imgInfo);
			img.put("r", rowAndCol.get("maxX"));
			img.put("c", rowAndCol.get("maxY"));
			if(dataRowLen != null)
			{
				img.put("height", dataRowLen);
			}else {
				img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
			}
			if(dataColLen != null)
			{
				img.put("width", dataColLen);
			}else {
				img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
			}
			if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge()) {
				img.put("isMerge", YesNoEnum.YES.getCode().intValue());
				img.put("rowSpan", luckySheetBindData.getRowSpan());
				img.put("colSpan", luckySheetBindData.getColSpan());
			}else {
				img.put("isMerge", YesNoEnum.NO.getCode().intValue());
			}
			img.put("extend", 0);
			images.add(img);
		}
		//边框处理
		if(border != null)
		{
			String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
			this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//			Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
//			if(cellBorder != null)
//			{
//				borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//				if(cellBorder instanceof List)
//				{
//					borderInfos.addAll((Collection<? extends Object>) cellBorder);
//				}else {
//					borderInfos.add(cellBorder);
//				}
//			}
		}
		maxCoordinate.put("x-"+luckySheetBindData.getCoordsx(),rowAndCol.get("maxY")+luckySheetBindData.getColSpan());
		maxCoordinate.put("y-"+luckySheetBindData.getCoordsy(),rowAndCol.get("maxX")+luckySheetBindData.getRowSpan());
		maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
	}
	
	/**  
	 * @Title: getBorderType
	 * @Description: 获取边框类型
	 * @param borderConfig 边框配置信息
	 * @param coordsx 横坐标
	 * @param coordsy 纵坐标
	 * @author caiyang
	 * @date 2022-03-24 08:07:14 
	 */ 
	private List<Map<String, Object>> getBorderType(List<Map<String, Object>> borderConfig,int coordsx,int coordsy) {
		List<Map<String, Object>> borders = new ArrayList<>();
		if(!ListUtil.isEmpty(borderConfig))
		{
			Map<String, Object> result = null;
			Map<String, Integer> borderTypes = new HashMap<>();
			for (int i = 0; i < borderConfig.size(); i++) {
				String rangeType = (String) borderConfig.get(i).get(LuckySheetPropsEnum.RANGETYPE.getCode());
				if(!"range".equals(rangeType))
				{
					Map<String, Object> map = (Map<String, Object>) borderConfig.get(i).get("value");
					int row_index = (int) map.get("row_index");
					int col_index = (int) map.get("col_index");
					if(coordsx == row_index && coordsy == col_index)
					{
						result = borderConfig.get(i);
					}
				}else {
					List<Map<String, Object>> range = (List<Map<String, Object>>) borderConfig.get(i).get(LuckySheetPropsEnum.BORDERRANGE.getCode());
					for (int j = 0; j < range.size(); j++) {
						List<Integer> column = (List<Integer>) range.get(j).get(LuckySheetPropsEnum.BORDERCOLUMNRANGE.getCode()); 
						List<Integer> row = (List<Integer>) range.get(j).get(LuckySheetPropsEnum.BORDERROWRANGE.getCode()); 
						if(coordsx >= row.get(0).intValue() && coordsx <= row.get(1).intValue() && coordsy >= column.get(0).intValue() && coordsy <= column.get(1).intValue())
						{
							result = borderConfig.get(i);
							break;
						}
					}
				}
				
				if(result != null)
				{
					if(BorderTypeEnum.BORDERNONE.getCode().equals(result.get(LuckySheetPropsEnum.BORDERTYPE.getCode())))
					{
						result = null;
						borders = null;
						borders = new ArrayList<>();
						borderTypes = null;
						borderTypes = new HashMap<>();
					}else {
						if(!borderTypes.containsKey(String.valueOf(result.get(LuckySheetPropsEnum.BORDERTYPE.getCode()))))
						{
							borders.add(result);
							borderTypes.put(String.valueOf(result.get(LuckySheetPropsEnum.BORDERTYPE.getCode())),borders.size()-1);
						}else {
							int index = borderTypes.get(String.valueOf(result.get(LuckySheetPropsEnum.BORDERTYPE.getCode())));
							borders.set(index, result);
						}
					}
				}
			}
		}
		return borders;
	}
	
	/**  
	 * @Title: borderProcess
	 * @Description: 边框处理
	 * @param border
	 * @author caiyang
	 * @date 2022-03-24 10:58:05 
	 */ 
	private void borderProcess(List<Map<String, Object>> borders,int startCoordsx,int endCoordsx,int startCoordsy,int endCoordsy,
			Map<String, Object> borderInfo,LuckySheetFormsBindData luckySheetBindData,String key) {
		List<Map<String, Object>> list = null;
		if(borderInfo.containsKey(key))
		{
			list = (List<Map<String, Object>>) borderInfo.get(key);
			if(ListUtil.isNotEmpty(list)){
				for (int i = 0; i < list.size(); i++) {
					List<Map<String, List<Integer>>> ranges = (List<Map<String, List<Integer>>>) list.get(i).get("range");
					Map<String, List<Integer>> range = ranges.get(0);
					if(endCoordsy > range.get("column").get(1))
					{
						range.get("column").set(1, endCoordsy);
					}
					if(endCoordsx > range.get("row").get(1))
					{
						range.get("row").set(1, endCoordsx);
					}
				}	
			}
		}else {
			if(!ListUtil.isEmpty(borders))
			{
				for (int t = 0; t < borders.size(); t++) {
					Map<String, Object> border = borders.get(t);
					if(border != null)
					{
						String rangType = (String) border.get(LuckySheetPropsEnum.RANGETYPE.getCode());
						if(LuckySheetPropsEnum.BORDERRANGE.getCode().equals(rangType))
						{
							Map<String, Object> cellBorder = null;
							String borderType = (String) border.get(LuckySheetPropsEnum.BORDERTYPE.getCode());
							if(BorderTypeEnum.BORDERALL.getCode().equals(borderType) || BorderTypeEnum.BORDERLEFT.getCode().equals(borderType)
							|| BorderTypeEnum.BORDERRIGHT.getCode().equals(borderType)|| BorderTypeEnum.BORDERTOP.getCode().equals(borderType)
							|| BorderTypeEnum.BORDERBOTTOM.getCode().equals(borderType))
							{
								cellBorder = new HashMap<String, Object>();
								cellBorder.put(LuckySheetPropsEnum.BORDERTYPE.getCode(), border.get(LuckySheetPropsEnum.BORDERTYPE.getCode()));
								cellBorder.put(LuckySheetPropsEnum.RANGETYPE.getCode(), border.get(LuckySheetPropsEnum.RANGETYPE.getCode()));
								cellBorder.put(LuckySheetPropsEnum.RANGECOLOR.getCode(), border.get(LuckySheetPropsEnum.RANGECOLOR.getCode()));
//								Map<String, Object> range = new HashMap<String, Object>();
								List<Map<String, Object>> rangeList = new ArrayList<Map<String,Object>>();
								Map<String, Object> rangeMap = new HashMap<String, Object>();
								List<Integer> column = new ArrayList<Integer>();
								column.add(startCoordsy);
								column.add(endCoordsy);
								List<Integer> row = new ArrayList<Integer>();
								row.add(startCoordsx);
								row.add(endCoordsx);
								rangeMap.put(LuckySheetPropsEnum.BORDERCOLUMNRANGE.getCode(), column);
								rangeMap.put(LuckySheetPropsEnum.BORDERROWRANGE.getCode(), row);
								rangeList.add(rangeMap);
								cellBorder.put(LuckySheetPropsEnum.BORDERRANGE.getCode(), rangeList);
								cellBorder.put(LuckySheetPropsEnum.RANGESTYLE.getCode(), border.get(LuckySheetPropsEnum.RANGESTYLE.getCode()));
							}
							if(list == null)
							{
								list = new ArrayList<Map<String,Object>>();
							}
							list.add(cellBorder);
						}
					}
				}
				borderInfo.put(key, list);
			}
		}
	}

	private Map<String, Integer> getMaxRowAndCol(Map<String, Integer> maxCoordinate,int coordsx,int coordsy,int rowSpan,int colSpan)
	{
		Map<String, Integer> rowAndCol = new HashMap<String, Integer>();
		//默认自己最大
		int maxX = coordsx;
		int maxY = coordsy;
		for (int i = 0; i < rowSpan; i++) {
			for (int j = 0; j < colSpan; j++) {
				Integer x = maxCoordinate.get("y-"+(coordsy+j)) == null?0:maxCoordinate.get("y-"+(coordsy+j));
				Integer y = maxCoordinate.get("x-"+(coordsx+i)) == null?0:maxCoordinate.get("x-"+(coordsx+i));
				if(maxX >= x)
				{
					maxCoordinate.put("y-"+(coordsy+j), maxX);
				}else {
					maxX = x;
				}
				if(maxY >= y)
				{
					maxCoordinate.put("x-"+(coordsx+i), maxY);
				}else {
					maxY = y;
				}
			}
		}
		rowAndCol.put("maxX", maxX);
		rowAndCol.put("maxY", maxY);
		return rowAndCol;
	}
	
	/**  
	 * @Title: processListGroupValue
	 * @Description: 处理列表数据
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param cellDatas
	 * @param hyperlinks
	 * @param dataRowLen
	 * @param dataColLen
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @param mergeMap
	 * @param objectMapper
	 * @author caiyang
	 * @date 2022-03-20 07:58:10 
	 */ 
	private void processListGroupValue(Map<String, Integer> maxCoordinate,LuckySheetFormsBindData luckySheetBindData,
			List<Map<String, Object>> cellDatas,Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen
			,Map<String, Object> rowlen,Map<String, Object> columnlen,Map<String, Map<String, Object>> mergeMap,
			ObjectMapper objectMapper,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos
			,List<JSONObject> calcChain,Map<String, JSONObject> extendCellOrigin,Map<String, JSONObject> columnStartCoords
			,Map<String, Map<String, String>> cellDictsValueLabel,String sheetIndex,
			Map<String, Object> configRowLen,Map<String, Object> configColumnLen, List<JSONObject> images,Object rowhidden,Object colhidden
			,JSONObject drillCells,Map<String, JSONObject> cacheDatas,String keyPattern,Map<String, Object> replacedData,Map<String, JSONArray> cellConditionFormat) {
		List<List<Map<String, Object>>> datas = null;
		if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode())
		{
			datas = luckySheetBindData.getFilterDatas();
		}else {
			datas = luckySheetBindData.getDatas();
		}
		if(ListUtil.isEmpty(datas))
		{
			return;
		}
        for (int j = 0; j < datas.size(); j++) {
            if(CellExtendEnum.NOEXTEND.getCode().intValue() == luckySheetBindData.getCellExtend().intValue())
            {//非扩展单元格处理
            	this.processNotExtendListGroupValue(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, 
            		rowlen, columnlen, maxXAndY,borderInfo,borderConfig,borderInfos,calcChain,extendCellOrigin,columnStartCoords,cellDictsValueLabel,sheetIndex,
            		mergeMap,configRowLen,configColumnLen,images,rowhidden,colhidden,drillCells,cacheDatas,keyPattern,replacedData,cellConditionFormat);
                break;
            }else if(CellExtendEnum.VERTICAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()){
                //向下扩展单元格处理
            	this.processVerticalListGroupValue(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, 
            		rowlen, columnlen,mergeMap, objectMapper,maxXAndY,borderInfo,borderConfig,borderInfos,calcChain,extendCellOrigin,
            		columnStartCoords,cellDictsValueLabel,sheetIndex,configRowLen,configColumnLen,images,rowhidden,colhidden,drillCells,cacheDatas,keyPattern,replacedData,cellConditionFormat);
            }else if(CellExtendEnum.HORIZONTAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()){
            	//向右扩展单元格处理
            	this.processHorizontalListGroupValue(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, 
            		columnlen, mergeMap, objectMapper,maxXAndY,borderInfo,borderConfig,borderInfos,calcChain,extendCellOrigin,columnStartCoords,
            		cellDictsValueLabel,sheetIndex,configRowLen,configColumnLen,images,rowhidden,colhidden,drillCells,cacheDatas,keyPattern,replacedData,cellConditionFormat);
            }
        }
    
	}
	
	/**  
	 * @Title: processNotExtendListGroupValue
	 * @Description: 处理列表分组不扩展数据
	 * @param j
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param cellDatas
	 * @param hyperlinks
	 * @param dataRowLen
	 * @param dataColLen
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @author caiyang
	 * @date 2022-03-21 07:45:50 
	 */ 
	private void processNotExtendListGroupValue(int j,Map<String, Integer> maxCoordinate,LuckySheetFormsBindData luckySheetBindData,List<Map<String, Object>> cellDatas,
			Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen,Map<String, Object> rowlen,Map<String, Object> columnlen
			,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos,List<JSONObject> calcChain,
			Map<String, JSONObject> extendCellOrigin,Map<String, JSONObject> columnStartCoords,Map<String, Map<String, String>> cellDictsValueLabel,String sheetIndex,
			Map<String, Map<String, Object>> mergeMap,Map<String, Object> configRowLen,Map<String, Object> configColumnLen, List<JSONObject> images,Object rowhidden,Object colhidden
			,JSONObject drillCells,Map<String, JSONObject> cacheDatas,String keyPattern,Map<String, Object> replacedData,Map<String, JSONArray> cellConditionFormat) {
		List<List<Map<String, Object>>> bindDatas = null;
//		Map<String, Object> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode())
		{
			bindDatas = luckySheetBindData.getFilterDatas();
		}else {
			bindDatas = luckySheetBindData.getDatas();
		}
		//不扩展
		Map<String, Integer> rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,luckySheetBindData.getColSpan());
		this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
		if(j == 0)
		{
			this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		}
		String cellKey = sheetIndex + "_" + luckySheetBindData.getCoordsx() + "_" + luckySheetBindData.getCoordsy();
		luckySheetBindData.getCellData().put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        luckySheetBindData.getCellData().put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        String[] datasetNames = LuckysheetUtil.getDatasetNames(luckySheetBindData.getDatasetName());
        String property = luckySheetBindData.getProperty();
        int setSize = 1;
        boolean isJustProperty = false;
        if(cellDictsValueLabel.containsKey(cellKey))
        {
        	property = cellDictsValueLabel.get(cellKey).get(bindDatas.get(j).get(0).get(luckySheetBindData.getProperty()));
        	if(property == null)
        	{
        		property = String.valueOf(bindDatas.get(j).get(0).get(luckySheetBindData.getProperty()));
        	}
        	isJustProperty = true;
        }else {
        	Map<String, Object> datas = new HashMap<String, Object>();
            if(datasetNames.length > 1)
            {
            	for (int i = 0; i < datasetNames.length; i++) {
            		Map<String, Object> datasetDatas = ListUtil.getProperties(luckySheetBindData.getProperty(), 
            				luckySheetBindData.getMultiDatas().get(datasetNames[i]).get(j).get(0), datasetNames[i]);
            		datas.putAll(datasetDatas);
    			}
            }else {
            	datas = ListUtil.getProperties(luckySheetBindData.getProperty(), bindDatas.get(j).get(0));	
            }
            Set<String> set = datas.keySet();
            for (String o : set) {
            	property = property.replace(o, datas.get(o)==null?"":String.valueOf(datas.get(o)));
            	if(set.size() == 1)
            	{
            		if(property.equals("") || property.length() == String.valueOf(datas.get(o)).length())
            		{
            			isJustProperty = true;
            		}
            	}
            }
            setSize = set.size();
        }
        Object value = null;
        boolean isImg = false;
        try {
        	if(setSize > 1)
        	{
        		value = AviatorEvaluator.execute(property);
        	}else {
        		if(StringUtil.isImgUrl(property))
        		{
        			value = property;
					isImg = true;
        		}else if(!isJustProperty)
        		{
        			value = AviatorEvaluator.execute(property);
        		}else {
        			value = property;
        		}
        	}
		} catch (Exception e) {
			
		}
        if(value == null)
        {
        	value = property;
        }
        //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer())
		{
			value = this.processUnitTransfer(value, luckySheetBindData);
		}
        String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
    	Object v = LuckysheetUtil.formatValue(format, value);
    	((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v==null?"":String.valueOf(v));
    	((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(),v==null?"":String.valueOf(v));
    	if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue())
    	{
    		((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.FUNCTION.getCode(), value);
    		JSONObject jsonObject = new JSONObject();
			jsonObject.put("r", rowAndCol.get("maxX"));
			jsonObject.put("c", rowAndCol.get("maxY"));
			calcChain.add(jsonObject);
    	}
    	String key = rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY");
        if(luckySheetBindData.getCellAttrs().getBooleanValue("isDrill") && drillCells != null)
    	{
    		JSONObject drillInfo = new JSONObject();
    		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getCellAttrs().get("drillId"));
    		JSONObject drillParams = getDrillParams(bindDatas.get(j).get(0),luckySheetBindData.getCellAttrs().getString("drillAttrs"));
    		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
    		drillCells.put(key, drillInfo);
    	}
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge())
		{
			((Map<String, Object>)(((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			((Map<String, Object>)(((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			Map<String, Object> merge = new HashMap<String, Object>();
			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
			for (int t = 0; t < luckySheetBindData.getRowSpan(); t++) {
				for (int m = 0; m < luckySheetBindData.getColSpan(); m++) {
					if(t != 0 || m != 0)
					{
						Map<String, Object> mc = new HashMap<String, Object>();
						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
						Map<String, Object> cellValue = new HashMap<String, Object>();
						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
						Map<String, Object> mergeCellData = new HashMap<String, Object>();
						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+t);
						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+m);
						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
						cellDatas.add(mergeCellData);
						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+m));
						if(mcDataColLen != null)
						{
							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+m))== null)
							{
								columnlen.put(String.valueOf(rowAndCol.get("maxY")+m), mcDataColLen);
							}
						}
					}
				}
				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+t));
				if(mcDataRowLen != null)
				{
					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+t))==null)
					{
						rowlen.put(String.valueOf(rowAndCol.get("maxX")+t), mcDataRowLen);	
					}
				}
			}
			if((rowAndCol.get("maxX") + luckySheetBindData.getRowSpan()-1)> maxX)
			{
				maxX = rowAndCol.get("maxX") + luckySheetBindData.getRowSpan()-1;
			}
			if((rowAndCol.get("maxY") + luckySheetBindData.getColSpan()-1)> maxY)
			{
				maxY = rowAndCol.get("maxY") + luckySheetBindData.getColSpan()-1;
			}
		}else {
			if(rowAndCol.get("maxX")>maxX)
			{
				maxX = rowAndCol.get("maxX");
			}
			if(rowAndCol.get("maxY")>maxY)
			{
				maxY = rowAndCol.get("maxY");
			}
		}
        if(dataRowLen != null)
		{
        	if(rowlen.get(String.valueOf(rowAndCol.get("maxX")))==null)
        	{
        		rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
        	}
		}
		if(dataColLen != null)
		{
			if(columnlen.get(String.valueOf(rowAndCol.get("maxY")))== null)
			{
				columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
			}
		}
		if(isImg)
		{
			double top = LuckysheetUtil.calculateTop(rowlen, rowAndCol.get("maxX"),rowhidden);
			double left = LuckysheetUtil.calculateLeft(columnlen, rowAndCol.get("maxY"),colhidden);
			Object width = LuckysheetUtil.calculateWidth(columnlen, rowAndCol.get("maxY"), luckySheetBindData.getColSpan());
			Object height = LuckysheetUtil.calculateHeight(rowlen, rowAndCol.get("maxX"), luckySheetBindData.getRowSpan());
			JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
			imgInfo.getJSONObject("default").put("top", top);
			imgInfo.getJSONObject("default").put("left", left);
			imgInfo.getJSONObject("default").put("width", width);
			imgInfo.getJSONObject("default").put("height", height);
			imgInfo.getJSONObject("crop").put("width", width);
			imgInfo.getJSONObject("crop").put("height", height);
			imgInfo.put("src", property);
			JSONObject img = new JSONObject();
			img.put("imgInfo", imgInfo);
			img.put("r", rowAndCol.get("maxX"));
			img.put("c", rowAndCol.get("maxY"));
			if(dataRowLen != null)
			{
				img.put("height", dataRowLen);
			}else {
				img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
			}
			if(dataColLen != null)
			{
				img.put("width", dataColLen);
			}else {
				img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
			}
			if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge()) {
				img.put("isMerge", YesNoEnum.YES.getCode().intValue());
				img.put("rowSpan", luckySheetBindData.getRowSpan());
				img.put("colSpan", luckySheetBindData.getColSpan());
			}else {
				img.put("isMerge", YesNoEnum.NO.getCode().intValue());
			}
			img.put("extend", 0);
			images.add(img);
		}
        if(border != null)
		{
        	String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
        	this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//			Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
//			if(cellBorder != null)
//			{
//				borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//				if(cellBorder instanceof List)
//				{
//					borderInfos.addAll((Collection<? extends Object>) cellBorder);
//				}else {
//					borderInfos.add(cellBorder);
//				}
//			}
		}
        String cacheKey = keyPattern+"-"+rowAndCol.get("maxX")+"-"+rowAndCol.get("maxY");
        if(cacheDatas.containsKey(cacheKey))
        {
        	replacedData.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"),JSON.parseObject(JSON.toJSONString(luckySheetBindData.getCellData())));
        	JSONObject data = cacheDatas.get(cacheKey);
			((Map<String, Object>)(((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())))).put(LuckySheetPropsEnum.CELLVALUE.getCode(), data.get(LuckySheetPropsEnum.CELLVALUE.getCode()));
			((Map<String, Object>)(((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())))).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), data.get(LuckySheetPropsEnum.CELLVALUEM.getCode()));
			cacheDatas.remove(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"));
        }
        cellDatas.add(luckySheetBindData.getCellData());
        maxCoordinate.put("x-"+luckySheetBindData.getCoordsx(), rowAndCol.get("maxY")+luckySheetBindData.getColSpan());
        maxCoordinate.put("y-"+luckySheetBindData.getCoordsy(), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan());
		maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
	}
	
	/**  
	 * @Title: processVerticalListGroupValue
	 * @Description: 处理分组列表向下扩展数据
	 * @param j
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param cellDatas
	 * @param hyperlinks
	 * @param dataRowLen
	 * @param dataColLen
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @param mergeMap
	 * @param objectMapper
	 * @author caiyang
	 * @date 2022-03-21 07:49:26 
	 */ 
	private void processVerticalListGroupValue(int j,Map<String, Integer> maxCoordinate,LuckySheetFormsBindData luckySheetBindData,List<Map<String, Object>> cellDatas,
			Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen,Map<String, Object> rowlen,Map<String, Object> columnlen
			, Map<String, Map<String, Object>> mergeMap,ObjectMapper objectMapper,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos
			,List<JSONObject> calcChain,Map<String, JSONObject> extendCellOrigin,Map<String, JSONObject> columnStartCoords,Map<String, Map<String, String>> cellDictsValueLabel,String sheetIndex
			,Map<String, Object> configRowLen,Map<String, Object> configColumnLen, List<JSONObject> images,Object rowhidden,Object colhidden
			,JSONObject drillCells,Map<String, JSONObject> cacheDatas,String keyPattern,Map<String, Object> replacedData,Map<String, JSONArray> cellConditionFormat)
	{
		List<List<Map<String, Object>>> bindDatas = null;
		if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode())
		{
			bindDatas = luckySheetBindData.getFilterDatas();
		}else {
			bindDatas = luckySheetBindData.getDatas();
		}
//		Map<String, Object> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
        Map<String, Integer> rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
        this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
        if(j == 0)
		{
			this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		}
        //纵向扩展(向下扩展)
        Map<String, Object> cellData = null;
        try {
        	cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        String cellKey = sheetIndex + "_" + luckySheetBindData.getCoordsx() + "_" + luckySheetBindData.getCoordsy();
        String property = luckySheetBindData.getProperty();
        int setSize = 1;
        boolean isJustProperty = false;
        this.processConditionFormat(luckySheetBindData, cellConditionFormat, (int)cellData.get(LuckySheetPropsEnum.R.getCode()), (int)cellData.get(LuckySheetPropsEnum.R.getCode()), j == 0);
        if(cellDictsValueLabel.containsKey(cellKey))
        {
        	property = cellDictsValueLabel.get(cellKey).get(bindDatas.get(j).get(0).get(luckySheetBindData.getProperty()));
        	if(property == null)
        	{
        		property = String.valueOf(bindDatas.get(j).get(0).get(luckySheetBindData.getProperty()));
        	}
        	isJustProperty = true;
        }else {
        	Map<String, Object> datas = ListUtil.getProperties(luckySheetBindData.getProperty(), bindDatas.get(j).get(0));
            Set<String> set = datas.keySet();
            setSize = set.size();
            for (String o : set) {
            	property = property.replace(o, datas.get(o)==null?"":String.valueOf(datas.get(o)));
            	if(set.size() == 1)
            	{
             		if(property.equals("") || property.length() == String.valueOf(datas.get(o)).length())
            		{
            			isJustProperty = true;
            		}
            	}
            }
        }
        Object value = null;
        boolean isImg = false;
        try {
        	if(setSize > 1)
        	{
        		value = AviatorEvaluator.execute(property);
        	}else {
        		if(StringUtil.isImgUrl(property))
        		{
        			value = property;
					isImg = true;
        		}else if(!isJustProperty)
        		{
        			value = AviatorEvaluator.execute(property);
        		}else {
        			value = property;
        		}
        	}
		} catch (Exception e) {
			
		}
        if(value == null)
        {
        	value = property;
        }
       //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer())
		{
			value = this.processUnitTransfer(value, luckySheetBindData);
		}
        this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), j == 0);
        String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
    	Object v = LuckysheetUtil.formatValue(format, value);
    	((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v==null?"":String.valueOf(v));
    	((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v==null?"":String.valueOf(v));
    	String cacheKey = keyPattern+"-"+rowAndCol.get("maxX")+"-"+rowAndCol.get("maxY");
    	if(cacheDatas.containsKey(cacheKey)) {
    		replacedData.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"),JSON.parse(JSON.toJSONString(cellData)));
    		JSONObject data = cacheDatas.get(cacheKey);
			((Map<String, Object>)((cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())))).put(LuckySheetPropsEnum.CELLVALUE.getCode(), data.get(LuckySheetPropsEnum.CELLVALUE.getCode()));
			((Map<String, Object>)((cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())))).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), data.get(LuckySheetPropsEnum.CELLVALUEM.getCode()));
			cacheDatas.remove(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"));
    	}
    	if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue())
    	{
    		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.FUNCTION.getCode(), value);
    		JSONObject jsonObject = new JSONObject();
			jsonObject.put("r", rowAndCol.get("maxX"));
			jsonObject.put("c", rowAndCol.get("maxY"));
			calcChain.add(jsonObject);
    	}
    	if(dataRowLen != null)
        {
    		if(rowlen.get(String.valueOf(rowAndCol.get("maxX")))==null)
    		{
    			rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
    		}
        }
        if(dataColLen != null)
        {
        	if(columnlen.get(String.valueOf(rowAndCol.get("maxY")))== null)
        	{
        		columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
        	}
        }
        String key = rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY");
        if(luckySheetBindData.getCellAttrs().getBooleanValue("isDrill") && drillCells != null)
    	{
    		JSONObject drillInfo = new JSONObject();
    		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getCellAttrs().get("drillId"));
    		JSONObject drillParams = getDrillParams(bindDatas.get(j).get(0),luckySheetBindData.getCellAttrs().getString("drillAttrs"));
    		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
    		drillCells.put(key, drillInfo);
    	}
    	if(bindDatas.get(j).size()>1)
    	{
    		Map<String, Object> mergeConfig = new HashMap<String, Object>();
    		mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    		mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    		int rowSpan = bindDatas.get(j).size();
    		int colSpan = luckySheetBindData.getColSpan();
    		if(luckySheetBindData.getIsMerge().intValue() == 1)
    		{
    			rowSpan = bindDatas.get(j).size() * luckySheetBindData.getRowSpan();
    		}
    		mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
    		mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
    		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
    		cellDatas.add(cellData);
    		Map<String, Object> merge = new HashMap<String, Object>();
    		merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    		merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    		merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
    		merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
    		mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
    		for (int k = 1; k < rowSpan; k++) {
    			int dataIndex = k/luckySheetBindData.getRowSpan();
    			for (int i = 1; i <= colSpan; i++) {
    				if(k == 1 && i == 1)
					{
						//do nothing
					}else{
    					Map<String, Object> mc = new HashMap<String, Object>();
        				mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        				mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        				Map<String, Object> cellValue = new HashMap<String, Object>();
        				Map<String, Object> mergeCellData = new HashMap<String, Object>();
        				mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
        				mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
        				mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
        				cellDatas.add(mergeCellData);
        				Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i-1));
        				if(mcDataColLen != null)
						{
        					if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i-1))== null)
        					{
        						columnlen.put(String.valueOf(rowAndCol.get("maxY")+i-1), mcDataColLen);
        					}
						}
    				}
				}
    			Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k-dataIndex*luckySheetBindData.getRowSpan()));
    			if(mcDataRowLen != null)
				{
    				if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
    				{
    					rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
    				}
				}
    		}
    		if((rowAndCol.get("maxX")+rowSpan-1)>maxX)
    		{
    			 maxX = rowAndCol.get("maxX")+rowSpan-1;
    		}
    		if((rowAndCol.get("maxY")+colSpan-1)>maxY)
    		{
    			maxY = rowAndCol.get("maxY")+colSpan-1;
    		}
    		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
    		this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1,borderInfo,luckySheetBindData,borderKey);
//    		Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1);
//     		if(cellBorder != null)
//     		{
//     			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//     			if(cellBorder instanceof List)
// 				{
// 					borderInfos.addAll((Collection<? extends Object>) cellBorder);
// 				}else {
// 					borderInfos.add(cellBorder);
// 				}
//     		}
    	}else {
    		if(luckySheetBindData.getIsMerge().intValue() == 1)
    		{
    			Map<String, Object> mergeConfig = new HashMap<String, Object>();
    			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),luckySheetBindData.getRowSpan());
    			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
    			cellDatas.add(cellData);
    			Map<String, Object> merge = new HashMap<String, Object>();
    			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
    			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
    			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
    			for (int k = 1; k <= luckySheetBindData.getRowSpan(); k++) {
    				for (int i = 1; i <= luckySheetBindData.getColSpan(); i++) {
    					if(k == 1 && i == 1)
    					{
    						//do nothing
    					}else {
    						Map<String, Object> mc = new HashMap<String, Object>();
    						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    						Map<String, Object> cellValue = new HashMap<String, Object>();
    						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
    						Map<String, Object> mergeCellData = new HashMap<String, Object>();
    						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
    						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
    						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
    						cellDatas.add(mergeCellData);
    						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
            				if(mcDataColLen != null)
    						{
            					if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
            					{
            						columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
            					}
    						}
    					}
    				}
    				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
        			if(mcDataRowLen != null)
					{
        				if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
        				{
        					rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
        				}
					}
    			}
    			if((rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1)>maxX)
    			{
    				maxX = rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1;
    			}
    			if((rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1)>maxY)
    			{
    				maxY = rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1;
    			}
    			String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
    			this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//    			Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
//        		if(cellBorder != null)
//        		{
//        			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//        			if(cellBorder instanceof List)
//    				{
//    					borderInfos.addAll((Collection<? extends Object>) cellBorder);
//    				}else {
//    					borderInfos.add(cellBorder);
//    				}
//        		}
    		}else {
              cellDatas.add(cellData);
              if(rowAndCol.get("maxX")>maxX)
              {
                  maxX = rowAndCol.get("maxX");
              }
              if(rowAndCol.get("maxY")>maxY)
              {
            	  maxY = rowAndCol.get("maxY");
              }
              String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
              this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"),borderInfo,luckySheetBindData,borderKey);
//              Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"));
//      		  if(cellBorder != null)
//      		  {
//      			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//      			if(cellBorder instanceof List)
//  				{
//  					borderInfos.addAll((Collection<? extends Object>) cellBorder);
//  				}else {
//  					borderInfos.add(cellBorder);
//  				}
//      		  }
    		}
    	}
    	if(isImg)
		{
    		double top = LuckysheetUtil.calculateTop(rowlen, rowAndCol.get("maxX"),rowhidden);
			double left = LuckysheetUtil.calculateLeft(columnlen, rowAndCol.get("maxY"),colhidden);
			Object width = LuckysheetUtil.calculateWidth(columnlen, rowAndCol.get("maxY"), luckySheetBindData.getColSpan());
			Object height = LuckysheetUtil.calculateHeight(rowlen, rowAndCol.get("maxX"), bindDatas.get(j).size())*luckySheetBindData.getRowSpan();
			JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
			imgInfo.getJSONObject("default").put("top", top);
			imgInfo.getJSONObject("default").put("left", left);
			imgInfo.getJSONObject("default").put("width", width);
			imgInfo.getJSONObject("default").put("height", height);
			imgInfo.getJSONObject("crop").put("width", width);
			imgInfo.getJSONObject("crop").put("height", height);
			imgInfo.put("src", property);
			JSONObject img = new JSONObject();
			img.put("imgInfo", imgInfo);
			img.put("r", rowAndCol.get("maxX"));
			img.put("c", rowAndCol.get("maxY"));
			if(dataRowLen != null)
			{
				img.put("height", dataRowLen);
			}else {
				img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
			}
			if(dataColLen != null)
			{
				img.put("width", dataColLen);
			}else {
				img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
			}
			int colSpan = luckySheetBindData.getColSpan();
			int rowSpan = bindDatas.get(j).size() * luckySheetBindData.getRowSpan();
			if(colSpan > 1 || rowSpan > 1)
			{
				img.put("isMerge", YesNoEnum.YES.getCode().intValue());
				img.put("rowSpan", rowSpan);
				img.put("colSpan", colSpan);
			}else {
				img.put("isMerge", YesNoEnum.NO.getCode().intValue());
			}
		
			img.put("extend", 1);
			images.add(img);
		}
    	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
    		maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()*bindDatas.get(j).size());
    	}
        if(j == bindDatas.size()-1)
        {
        	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
        		maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+luckySheetBindData.getColSpan());
        	}
        }
        maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
	}
	
	/**  
	 * @Title: processHorizontalListGroupValue
	 * @Description: 处理分组列表向右扩展数据
	 * @param j
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param cellDatas
	 * @param hyperlinks
	 * @param dataRowLen
	 * @param dataColLen
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @param mergeMap
	 * @param objectMapper
	 * @author caiyang
	 * @date 2022-03-21 07:50:28 
	 */ 
	private void processHorizontalListGroupValue(int j,Map<String, Integer> maxCoordinate,LuckySheetFormsBindData luckySheetBindData,List<Map<String, Object>> cellDatas,
			Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen,Map<String, Object> rowlen,Map<String, Object> columnlen
			, Map<String, Map<String, Object>> mergeMap,ObjectMapper objectMapper,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos
			,List<JSONObject> calcChain,Map<String, JSONObject> extendCellOrigin,Map<String, JSONObject> columnStartCoords,Map<String, Map<String, String>> cellDictsValueLabel,String sheetIndex
			,Map<String, Object> configRowLen,Map<String, Object> configColumnLen, List<JSONObject> images,Object rowhidden,Object colhidden
			,JSONObject drillCells,Map<String, JSONObject> cacheDatas,String keyPattern,Map<String, Object> replacedData,Map<String, JSONArray> cellConditionFormat)
	{
		List<List<Map<String, Object>>> bindDatas = null;
		if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode())
		{
			bindDatas = luckySheetBindData.getFilterDatas();
		}else {
			bindDatas = luckySheetBindData.getDatas();
		}
//		Map<String, Object> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
        Map<String, Integer> rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
        this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
        if(j == 0)
		{
			this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		}
        //横向扩展(向右扩展)
        Map<String, Object> cellData = null;
        try {
        	cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        String cellKey = sheetIndex + "_" + luckySheetBindData.getCoordsx() + "_" + luckySheetBindData.getCoordsy();
        String property = luckySheetBindData.getProperty();
        int setSize = 1;
        boolean isJustProperty = false;
        if(cellDictsValueLabel.containsKey(cellKey))
        {
        	property = cellDictsValueLabel.get(cellKey).get(bindDatas.get(j).get(0).get(luckySheetBindData.getProperty()));
        	if(property == null)
        	{
        		property = String.valueOf(bindDatas.get(j).get(0).get(luckySheetBindData.getProperty()));
        	}
        	isJustProperty = true;
        }else {
        	Map<String, Object> datas = ListUtil.getProperties(luckySheetBindData.getProperty(), bindDatas.get(j).get(0));
            Set<String> set = datas.keySet();
            setSize = set.size();
            for (String o : set) {
            	property = property.replace(o, datas.get(o)==null?"":String.valueOf(datas.get(o)));
            	if(set.size() == 1)
            	{
            		if(property.equals("") || property.length() == String.valueOf(datas.get(o)).length())
            		{
            			isJustProperty = true;
            		}
            	}
            }
        }
        Object value = null;
        boolean isImg = false;
        try {
        	if(setSize > 1)
        	{
        		value = AviatorEvaluator.execute(property);
        	}else {
        		if(StringUtil.isImgUrl(property))
        		{
        			value = property;
					isImg = true;
        		}else if(!isJustProperty)
        		{
        			value = AviatorEvaluator.execute(property);
        		}else {
        			value = property;
        		}
        	}
		} catch (Exception e) {
			 
		}
        if(value == null)
        {
        	value = property;
        }
      //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer())
		{
			value = this.processUnitTransfer(value, luckySheetBindData);
		}
        String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
    	Object v = LuckysheetUtil.formatValue(format, value);
    	((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v==null?"":String.valueOf(v));
    	((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v==null?"":String.valueOf(v));
    	String cacheKey = keyPattern+"-"+rowAndCol.get("maxX")+"-"+rowAndCol.get("maxY");
    	if(cacheDatas.containsKey(cacheKey)) {
    		replacedData.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"),JSON.parse(JSON.toJSONString(cellData)));
    		JSONObject data = cacheDatas.get(cacheKey);
			((Map<String, Object>)((cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())))).put(LuckySheetPropsEnum.CELLVALUE.getCode(), data.get(LuckySheetPropsEnum.CELLVALUE.getCode()));
			((Map<String, Object>)((cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())))).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), data.get(LuckySheetPropsEnum.CELLVALUEM.getCode()));
			cacheDatas.remove(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"));
    	}
    	if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue())
    	{
    		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.FUNCTION.getCode(), value);
    		JSONObject jsonObject = new JSONObject();
			jsonObject.put("r", rowAndCol.get("maxX"));
			jsonObject.put("c", rowAndCol.get("maxY"));
			calcChain.add(jsonObject);
    	}
    	if(dataRowLen != null)
        {
    		if(rowlen.get(String.valueOf(rowAndCol.get("maxX")))==null)
    		{
    			rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
    		}
        }
        if(dataColLen != null)
        {
        	if(columnlen.get(String.valueOf(rowAndCol.get("maxY")))== null)
        	{
        		columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
        	}
        }
        this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX"), rowAndCol.get("maxY"), j == 0);
        String key = rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY");
        if(luckySheetBindData.getCellAttrs().getBooleanValue("isDrill") && drillCells != null)
    	{
    		JSONObject drillInfo = new JSONObject();
    		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getCellAttrs().get("drillId"));
    		JSONObject drillParams = getDrillParams(bindDatas.get(j).get(0),luckySheetBindData.getCellAttrs().getString("drillAttrs"));
    		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
    		drillCells.put(key, drillInfo);
    	}
    	if(bindDatas.get(j).size()>1)
        {
        	Map<String, Object> mergeConfig = new HashMap<String, Object>();
    		mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    		mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    		int rowSpan = luckySheetBindData.getRowSpan();
    		int colSpan = bindDatas.get(j).size();
    		if(luckySheetBindData.getIsMerge().intValue() == 1)
    		{
    			colSpan = bindDatas.get(j).size() * luckySheetBindData.getColSpan();
    		}
    		mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
    		mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
    		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
    		cellDatas.add(cellData);
    		Map<String, Object> merge = new HashMap<String, Object>();
    		merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    		merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    		merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
    		merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
    		mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
    		for (int k = 1; k < rowSpan; k++) {
    			for (int i = 1; i <= colSpan; i++) {
    				int dataIndex = i/luckySheetBindData.getColSpan();
    				if(k == 1 && i == 1)
					{
						//do nothing
					}else {
						Map<String, Object> mc = new HashMap<String, Object>();
	    				mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
	    				mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
	    				Map<String, Object> cellValue = new HashMap<String, Object>();
	    				Map<String, Object> mergeCellData = new HashMap<String, Object>();
	    				mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
	    				mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
	    				mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
	    				cellDatas.add(mergeCellData);
	    				Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i-dataIndex*luckySheetBindData.getColSpan()));
        				if(mcDataColLen != null)
						{
        					if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
        					{
        						columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
        					}
						}
					}
    				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
        			if(mcDataRowLen != null)
					{
        				if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
        				{
        					rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
        				}
					}
    			}
    		}
    		if((rowAndCol.get("maxY")+colSpan-1)>maxY)
    		{
    			maxY = rowAndCol.get("maxY")+colSpan-1;
    		}
    		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
    		this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1,borderInfo,luckySheetBindData,borderKey);
//    		Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1);
//     		if(cellBorder != null)
//     		{
//     			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//     			if(cellBorder instanceof List)
// 				{
// 					borderInfos.addAll((Collection<? extends Object>) cellBorder);
// 				}else {
// 					borderInfos.add(cellBorder);
// 				}
//     		}
        }else {
        	if(luckySheetBindData.getIsMerge().intValue() == 1) {
        		Map<String, Object> mergeConfig = new HashMap<String, Object>();
    			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),luckySheetBindData.getRowSpan());
    			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
    			cellDatas.add(cellData);
    			Map<String, Object> merge = new HashMap<String, Object>();
    			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
    			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
    			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
    			for (int k = 1; k <= luckySheetBindData.getRowSpan(); k++) {
    				for (int i = 1; i <= luckySheetBindData.getColSpan(); i++) {
    					if(k == 1 && i == 1)
    					{
    						//do nothing
    					}else {
    						Map<String, Object> mc = new HashMap<String, Object>();
    						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    						Map<String, Object> cellValue = new HashMap<String, Object>();
    						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
    						Map<String, Object> mergeCellData = new HashMap<String, Object>();
    						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
    						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
    						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
    						cellDatas.add(mergeCellData);
    						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
            				if(mcDataColLen != null)
    						{
            					if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
            					{
            						columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
            					}
    						}
    					}
    				}
    				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
        			if(mcDataRowLen != null)
					{
        				if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
        				{
        					rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
        				}
					}
    			}
    			if((rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1)>maxX)
    			{
    				maxX = rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1;
    			}
    			if((rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1)>maxY)
    			{
    				maxY = rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1;
    			}
    			String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
    			this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//    			Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
//        		if(cellBorder != null)
//        		{
//        			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//        			if(cellBorder instanceof List)
//    				{
//    					borderInfos.addAll((Collection<? extends Object>) cellBorder);
//    				}else {
//    					borderInfos.add(cellBorder);
//    				}
//        		}
        	}else {
        		cellDatas.add(cellData);
                if(rowAndCol.get("maxY")>maxY)
                {
                	maxY = rowAndCol.get("maxY");
                }
                if(rowAndCol.get("maxX")>maxX)
                {
                	maxX = rowAndCol.get("maxX");
                }
                String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
                this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"),borderInfo,luckySheetBindData,borderKey);
//                Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"));
//        		if(cellBorder != null)
//        		{
//        			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//        			if(cellBorder instanceof List)
//    				{
//    					borderInfos.addAll((Collection<? extends Object>) cellBorder);
//    				}else {
//    					borderInfos.add(cellBorder);
//    				}
//        		}
        	}
        }
    	if(isImg)
		{
    		double top = LuckysheetUtil.calculateTop(rowlen, rowAndCol.get("maxX"),rowhidden);
			double left = LuckysheetUtil.calculateLeft(columnlen, rowAndCol.get("maxY"),colhidden);
			Object width = LuckysheetUtil.calculateWidth(columnlen, rowAndCol.get("maxY"), (bindDatas.get(j).size())*luckySheetBindData.getColSpan());
			Object height = LuckysheetUtil.calculateHeight(rowlen, rowAndCol.get("maxX"), luckySheetBindData.getRowSpan());
			JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
			imgInfo.getJSONObject("default").put("top", top);
			imgInfo.getJSONObject("default").put("left", left);
			imgInfo.getJSONObject("default").put("width", width);
			imgInfo.getJSONObject("default").put("height", height);
			imgInfo.getJSONObject("crop").put("width", width);
			imgInfo.getJSONObject("crop").put("height", height);
			imgInfo.put("src", property);
			JSONObject img = new JSONObject();
			img.put("imgInfo", imgInfo);
			img.put("r", rowAndCol.get("maxX"));
			img.put("c", rowAndCol.get("maxY"));
			if(dataRowLen != null)
			{
				img.put("height", dataRowLen);
			}else {
				img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
			}
			if(dataColLen != null)
			{
				img.put("width", dataColLen);
			}else {
				img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
			}
			if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge()) {
				img.put("isMerge", YesNoEnum.YES.getCode().intValue());
				img.put("rowSpan", luckySheetBindData.getRowSpan());
				img.put("colSpan", luckySheetBindData.getColSpan());
			}else {
				img.put("isMerge", YesNoEnum.NO.getCode().intValue());
			}
			int colSpan = bindDatas.get(j).size() * luckySheetBindData.getColSpan();
			int rowSpan = luckySheetBindData.getRowSpan();
			if(colSpan > 1 || rowSpan > 1)
			{
				img.put("isMerge", YesNoEnum.YES.getCode().intValue());
				img.put("rowSpan", rowSpan);
				img.put("colSpan", colSpan);
			}else {
				img.put("isMerge", YesNoEnum.NO.getCode().intValue());
			}
			img.put("extend", 2);
			images.add(img);
		}
    	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
    		maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()*bindDatas.get(j).size());
    	}
        if(j==bindDatas.size()-1)
        {
        	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
        		maxCoordinate.put("y-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan());
        	}
        }
        maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
	}
	
	/**  
	 * @Title: processSummaryValue
	 * @Description: 处理汇总聚合
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param cellDatas
	 * @param configRowLen
	 * @param configColumnLen
	 * @param maxX
	 * @param maxY
	 * @param rowlen
	 * @param columnlen
	 * @author caiyang
	 * @date 2022-03-20 05:29:45 
	 */ 
	private void processSummaryValue(Map<String, Integer> maxCoordinate,LuckySheetFormsBindData luckySheetBindData,
			List<Map<String, Object>> cellDatas,Map<String, Object> rowlen,
			Map<String, Object> columnlen,Object dataRowLen,Object dataColLen,Map<String, Integer> maxXAndY,
			Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos,Map<String, JSONObject> extendCellOrigin
			,Map<String, JSONObject> columnStartCoords,Map<String, Map<String, Object>> mergeMap,Map<String, Object> configRowLen,Map<String, Object> configColumnLen
			,Map<String, JSONArray> cellConditionFormat) {
//		Map<String, Object> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		}
		//计算结果
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsConditions().intValue())
		{
			luckySheetBindData.setDatas(luckySheetBindData.getFilterDatas());
		}
        String calculateResult = luckySheetCalculates.get(luckySheetBindData.getCellFunction().intValue()).calculate(luckySheetBindData);
      //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(calculateResult, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer())
 		{
 			Object resultData  = this.processUnitTransfer(calculateResult, luckySheetBindData);
 			if(resultData != null)
 			{
 				calculateResult = String.valueOf(resultData);
 			}
 		}
        Map<String, Integer> rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
        Map<String, Object> cellData = luckySheetBindData.getCellData();
        this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
		this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
		cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), calculateResult);
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), calculateResult);
        if(luckySheetBindData.getIsMerge().intValue() == 1) {
        	Map<String, Object> mergeConfig = new HashMap<String, Object>();
			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),luckySheetBindData.getRowSpan());
			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
			Map<String, Object> merge = new HashMap<String, Object>();
			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
			for (int k = 1; k <= luckySheetBindData.getRowSpan(); k++) {
				for (int i = 1; i <= luckySheetBindData.getColSpan(); i++) {
					if(k == 1 && i == 1)
					{
						//do nothing
					}else {
						Map<String, Object> mc = new HashMap<String, Object>();
						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
						Map<String, Object> cellValue = new HashMap<String, Object>();
						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
						Map<String, Object> mergeCellData = new HashMap<String, Object>();
						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
						cellDatas.add(mergeCellData);
						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i-1));
						if(mcDataColLen != null)
						{
							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i-1))== null)
							{
								columnlen.put(String.valueOf(rowAndCol.get("maxY")+i-1), mcDataColLen);
							}
						}
					}
				}
				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k-1));
				if(mcDataRowLen != null)
				{
					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k-1))==null)
					{
						rowlen.put(String.valueOf(rowAndCol.get("maxX")+k-1), mcDataRowLen);
					}
				}
			}
        }
        cellDatas.add(cellData);
        this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX"), rowAndCol.get("maxY"), true);
        if(dataRowLen != null)
        {
        	if(rowlen.get(String.valueOf(rowAndCol.get("maxX")))==null)
        	{
        		rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
        	}
        }
        if(dataColLen != null)
        {
        	if(columnlen.get(String.valueOf(rowAndCol.get("maxY")))== null)
        	{
        		columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
        	}
        }
        if(rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1>maxX)
        {
            maxX = rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1;
        }
        if(rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1>maxY)
        {
            maxY = rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1;
        }
        String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
        this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//        Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
//        if(cellBorder != null)
//		{
//			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//			if(cellBorder instanceof List)
//			{
//				borderInfos.addAll((Collection<? extends Object>) cellBorder);
//			}else {
//				borderInfos.add(cellBorder);
//			}
//		}
        maxCoordinate.put("x-"+luckySheetBindData.getCoordsx(), rowAndCol.get("maxY")+luckySheetBindData.getColSpan());
        maxCoordinate.put("y-"+luckySheetBindData.getCoordsy(), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan());
        maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
	}
	
	/**  
	 * @Title: processGroupSummaryValue
	 * @Description: 处理分组汇总数据
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param mergeMap
	 * @param cellDatas
	 * @param configRowLen
	 * @param configColumnLen
	 * @param maxX
	 * @param maxY
	 * @param rowlen
	 * @param columnlen
	 * @param objectMapper
	 * @param dataRowLen
	 * @param dataColLen
	 * @author caiyang
	 * @date 2022-03-20 07:52:19 
	 */ 
	private void processGroupSummaryValue(Map<String, Integer> maxCoordinate,LuckySheetFormsBindData luckySheetBindData,
			 Map<String, Map<String, Object>> mergeMap,List<Map<String, Object>> cellDatas,Map<String, Object> configRowLen,Map<String, Object> configColumnLen,
			Map<String, Object> rowlen,Map<String, Object> columnlen,ObjectMapper objectMapper,
			Object dataRowLen,Object dataColLen,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,
			List<Map<String, Object>> borderConfig,List<Object> borderInfos,Map<String, JSONObject> extendCellOrigin,Map<String, JSONObject> columnStartCoords
			,JSONObject drillCells,Map<String, JSONArray> cellConditionFormat) {
		//分组聚合
		List<List<Map<String, Object>>> datas = null;
        if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode())
		{
			datas = luckySheetBindData.getFilterDatas();
		}else {
			datas = luckySheetBindData.getDatas();
		}
        if(!ListUtil.isEmpty(datas))
        {
        	GroupSummaryData groupSummaryData = new GroupSummaryData();
            groupSummaryData.setDigit(luckySheetBindData.getDigit());
            groupSummaryData.setProperty(luckySheetBindData.getProperty());
            for (int j = 0; j < datas.size(); j++) {
                Map<String, Integer> rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
                if(CellExtendEnum.NOEXTEND.getCode().intValue() == luckySheetBindData.getCellExtend().intValue())
                {//不扩展
                    this.processNotExtendGroupSummaryValue(maxCoordinate, luckySheetBindData, rowAndCol, groupSummaryData, datas.get(j), 
                    		cellDatas, dataRowLen, dataColLen, rowlen, columnlen,objectMapper,maxXAndY,borderInfo,borderConfig,borderInfos,
                    		extendCellOrigin,columnStartCoords,mergeMap,configRowLen,configColumnLen,drillCells,cellConditionFormat);
                    break;
                }else if(CellExtendEnum.VERTICAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()){
                    //向下扩展
                    this.processVerticalGroupSummaryValue(j, maxCoordinate, luckySheetBindData, rowAndCol, groupSummaryData, datas.get(j), 
                    		cellDatas, dataRowLen, dataColLen, mergeMap, rowlen, columnlen,objectMapper, datas.size(),maxXAndY,borderInfo,
                    		borderConfig,borderInfos,extendCellOrigin,columnStartCoords,configRowLen,configColumnLen,drillCells,cellConditionFormat);
                }else if(CellExtendEnum.HORIZONTAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()){
                	this.processHorizontalGroupSummaryValue(j, maxCoordinate, luckySheetBindData, rowAndCol, groupSummaryData, datas.get(j), 
                			cellDatas, dataRowLen, dataColLen, mergeMap, rowlen, columnlen, objectMapper, datas.size(),
                			maxXAndY,borderInfo,borderConfig,borderInfos,extendCellOrigin,columnStartCoords,configRowLen,configColumnLen,drillCells,cellConditionFormat);
                }
            }
        }
	}
	
	/**  
	 * @Title: processNotExtendGroupSummaryValue
	 * @Description: 处理分组合计不扩展单元格数据
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param rowAndCol
	 * @param groupSummaryData
	 * @param data
	 * @param cellDatas
	 * @param dataRowLen
	 * @param dataColLen
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @param objectMapper
	 * @author caiyang
	 * @date 2022-03-21 07:17:36 
	 */ 
	private void processNotExtendGroupSummaryValue(Map<String, Integer> maxCoordinate,LuckySheetFormsBindData luckySheetBindData,Map<String, Integer> rowAndCol,GroupSummaryData groupSummaryData,
			List<Map<String, Object>> data,List<Map<String, Object>> cellDatas,Object dataRowLen,Object dataColLen,
			Map<String, Object> rowlen,Map<String, Object> columnlen,ObjectMapper objectMapper,Map<String, Integer> maxXAndY,
			Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos,Map<String, JSONObject> extendCellOrigin,
			Map<String, JSONObject> columnStartCoords,Map<String, Map<String, Object>> mergeMap,Map<String, Object> configRowLen,Map<String, Object> configColumnLen
			,JSONObject drillCells,Map<String, JSONArray> cellConditionFormat) {
//		Map<String, Object> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
		this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		//不扩展
        luckySheetBindData.setCoordsx(rowAndCol.get("maxX"));
        luckySheetBindData.setCoordsy(rowAndCol.get("maxY"));
        Map<String, Object> cellData = null;
        try {
            cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        String key = cellData.get(LuckySheetPropsEnum.R.getCode())+"_"+cellData.get(LuckySheetPropsEnum.C.getCode());
        if(luckySheetBindData.getCellAttrs().getBooleanValue("isDrill") && drillCells != null)
    	{
    		JSONObject drillInfo = new JSONObject();
    		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getCellAttrs().get("drillId"));
    		JSONObject drillParams = getDrillParams(data.get(0),luckySheetBindData.getCellAttrs().getString("drillAttrs"));
    		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
    		drillCells.put(key, drillInfo);
    	}
        this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX"), rowAndCol.get("maxY"), true);
        //计算
        groupSummaryData.setDatas(data);
        String value = luckySheetGroupCalculates.get(luckySheetBindData.getCellFunction().intValue()).calculate(groupSummaryData);
       //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer())
		{
			Object resultData  = this.processUnitTransfer(value, luckySheetBindData);
			if(resultData != null)
			{
				value = String.valueOf(resultData);
			}
		}
        String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
    	Object v = LuckysheetUtil.formatValue(format, value);
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v==null?"":String.valueOf(v));
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v==null?"":String.valueOf(v));
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge())
        {
        	((Map<String, Object>)(((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			((Map<String, Object>)(((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			Map<String, Object> merge = new HashMap<String, Object>();
			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
			for (int t = 0; t < luckySheetBindData.getRowSpan(); t++) {
				for (int m = 0; m < luckySheetBindData.getColSpan(); m++) {
					if(t != 0 || m != 0)
					{
						Map<String, Object> mc = new HashMap<String, Object>();
						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
						Map<String, Object> cellValue = new HashMap<String, Object>();
						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
						Map<String, Object> mergeCellData = new HashMap<String, Object>();
						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+t);
						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+m);
						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
						cellDatas.add(mergeCellData);
						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+m));
						if(mcDataColLen != null)
						{
							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+m))== null)
							{
								columnlen.put(String.valueOf(rowAndCol.get("maxY")+m), mcDataColLen);
							}
						}
					}
					Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()));
					if(mcDataRowLen != null)
					{
						if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+t))==null)
						{
							rowlen.put(String.valueOf(rowAndCol.get("maxX")+t), mcDataRowLen);
						}
					}
				}
			}
			if((rowAndCol.get("maxX") + luckySheetBindData.getRowSpan()-1)> maxX)
			{
				maxX = rowAndCol.get("maxX") + luckySheetBindData.getRowSpan()-1;
			}
			if((rowAndCol.get("maxY") + luckySheetBindData.getColSpan()-1)> maxY)
			{
				maxY = rowAndCol.get("maxY") + luckySheetBindData.getColSpan()-1;
			}
        }else {
        	if(rowAndCol.get("maxX")>maxX)
			{
				maxX = rowAndCol.get("maxX");
			}
			if(rowAndCol.get("maxY")>maxY)
			{
				maxY = rowAndCol.get("maxY");
			}
			if(dataRowLen != null)
			{
				if(rowlen.get(String.valueOf(rowAndCol.get("maxX")))==null)
				{
					rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
				}
			}
			if(dataColLen != null)
			{
				if(columnlen.get(String.valueOf(rowAndCol.get("maxY")))== null)
				{
					columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);	
				}
			}
        }
        cellDatas.add(cellData);
        maxCoordinate.put("x-"+luckySheetBindData.getCoordsx(), rowAndCol.get("maxY")+luckySheetBindData.getColSpan());
        maxCoordinate.put("y-"+luckySheetBindData.getCoordsy(), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan());
        maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
		this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//		Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
//		if(cellBorder != null)
//		{
//			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//			if(cellBorder instanceof List)
//			{
//				borderInfos.addAll((Collection<? extends Object>) cellBorder);
//			}else {
//				borderInfos.add(cellBorder);
//			}
//		}
	}
	
	/**  
	 * @Title: processVerticalGroupSummaryValue
	 * @Description: 处理分组合计向下扩展数据
	 * @param j
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param rowAndCol
	 * @param groupSummaryData
	 * @param data
	 * @param cellDatas
	 * @param dataRowLen
	 * @param dataColLen
	 * @param mergeMap
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @param objectMapper
	 * @param dataSize
	 * @author caiyang
	 * @date 2022-03-21 07:39:12 
	 */ 
	private void processVerticalGroupSummaryValue(int j,Map<String, Integer> maxCoordinate,LuckySheetFormsBindData luckySheetBindData,Map<String, Integer> rowAndCol,GroupSummaryData groupSummaryData,
			List<Map<String, Object>> data,List<Map<String, Object>> cellDatas,Object dataRowLen,Object dataColLen,Map<String, Map<String, Object>> mergeMap,
			Map<String, Object> rowlen,Map<String, Object> columnlen,ObjectMapper objectMapper, int dataSize,Map<String, Integer> maxXAndY,
			Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos,Map<String, JSONObject> extendCellOrigin,Map<String, JSONObject> columnStartCoords
			,Map<String, Object> configRowLen,Map<String, Object> configColumnLen,JSONObject drillCells,Map<String, JSONArray> cellConditionFormat) {
//		Map<String, Object> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
        if(j == 0)
		{
			this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		}
        Map<String, Object> cellData = null;
        try {
            cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        String key = cellData.get(LuckySheetPropsEnum.R.getCode())+"_"+cellData.get(LuckySheetPropsEnum.C.getCode());
        if(luckySheetBindData.getCellAttrs().getBooleanValue("isDrill") && drillCells != null)
    	{
    		JSONObject drillInfo = new JSONObject();
    		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getCellAttrs().get("drillId"));
    		JSONObject drillParams = getDrillParams(data.get(0),luckySheetBindData.getCellAttrs().getString("drillAttrs"));
    		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
    		drillCells.put(key, drillInfo);
    	}
        this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX"), rowAndCol.get("maxY"), j == 0);
        groupSummaryData.setDatas(data);
        String value = luckySheetGroupCalculates.get(luckySheetBindData.getCellFunction().intValue()).calculate(groupSummaryData);
      //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer())
		{
			Object resultData  = this.processUnitTransfer(value, luckySheetBindData);
			if(resultData != null)
			{
				value = String.valueOf(resultData);
			}
		}
        String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
    	Object v = LuckysheetUtil.formatValue(format, value);
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v==null?"":String.valueOf(v));
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v==null?"":String.valueOf(v));
        if(dataRowLen != null)
        {
        	if(rowlen.get(String.valueOf(rowAndCol.get("maxX")))==null)
        	{
        		rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
        	}
        }
        if(dataColLen != null)
        {
        	if(columnlen.get(String.valueOf(rowAndCol.get("maxY")))== null)
        	{
        		columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
        	}
        }
        
        if(data.size()>1)
    	{
    		Map<String, Object> mergeConfig = new HashMap<String, Object>();
    		mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    		mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    		int rowSpan = data.size();
    		int colSpan = luckySheetBindData.getColSpan();
    		if(luckySheetBindData.getIsMerge().intValue() == 1)
    		{
    			rowSpan = data.size() * luckySheetBindData.getRowSpan();
    		}
    		mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
    		mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
    		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
    		cellDatas.add(cellData);
    		Map<String, Object> merge = new HashMap<String, Object>();
    		merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    		merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    		merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
    		merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
    		mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
    		for (int k = 1; k < rowSpan; k++) {
    			for (int i = 1; i <= colSpan; i++) {
    				if(k == 1 && i == 1)
					{
						//do nothing
					}else{
    					Map<String, Object> mc = new HashMap<String, Object>();
        				mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        				mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        				Map<String, Object> cellValue = new HashMap<String, Object>();
        				Map<String, Object> mergeCellData = new HashMap<String, Object>();
        				mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
        				mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
        				mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
        				cellDatas.add(mergeCellData);
        				Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
						if(mcDataColLen != null)
						{
							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
							{
								columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
							}
						}
    				}
				}
    			Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
				if(mcDataRowLen != null)
				{
					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
					{
						rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
					}
				}
    		}
    		if((rowAndCol.get("maxX")+rowSpan-1)>maxX)
    		{
    			 maxX = rowAndCol.get("maxX")+rowSpan-1;
    		}
    		if((rowAndCol.get("maxY")+colSpan-1)>maxY)
			{
				maxY = rowAndCol.get("maxY")+colSpan-1;
			}
    		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
    		this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1,borderInfo,luckySheetBindData,borderKey);
//    		Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1);
//     		if(cellBorder != null)
//     		{
//     			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//     			if(cellBorder instanceof List)
// 				{
// 					borderInfos.addAll((Collection<? extends Object>) cellBorder);
// 				}else {
// 					borderInfos.add(cellBorder);
// 				}
//     		}
    	}else {
    		if(luckySheetBindData.getIsMerge().intValue() == 1)
    		{
    			Map<String, Object> mergeConfig = new HashMap<String, Object>();
    			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),luckySheetBindData.getRowSpan());
    			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
    			cellDatas.add(cellData);
    			Map<String, Object> merge = new HashMap<String, Object>();
    			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
    			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
    			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
    			for (int k = 1; k <= luckySheetBindData.getRowSpan(); k++) {
    				for (int i = 1; i <= luckySheetBindData.getColSpan(); i++) {
    					if(k == 1 && i == 1)
    					{
    						//do nothing
    					}else {
    						Map<String, Object> mc = new HashMap<String, Object>();
    						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    						Map<String, Object> cellValue = new HashMap<String, Object>();
    						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
    						Map<String, Object> mergeCellData = new HashMap<String, Object>();
    						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
    						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
    						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
    						cellDatas.add(mergeCellData);
    						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
    						if(mcDataColLen != null)
    						{
    							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
    							{
    								columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
    							}
    						}
    					}
    				}
    				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
    				if(mcDataRowLen != null)
					{
    					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
    					{
    						rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
    					}
					}
    			}
    			if((rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1)>maxX)
    			{
    				maxX = rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1;
    			}
    			if((rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1)>maxY)
    			{
    				maxY = rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1;
    			}
    			String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
    			this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//    			Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
//        		if(cellBorder != null)
//        		{
//        			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//        			if(cellBorder instanceof List)
//    				{
//    					borderInfos.addAll((Collection<? extends Object>) cellBorder);
//    				}else {
//    					borderInfos.add(cellBorder);
//    				}
//        		}
    		}else {
    			cellDatas.add(cellData);
                if(rowAndCol.get("maxX")>maxX)
                {
                    maxX = rowAndCol.get("maxX");
                }
                String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
                this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"),borderInfo,luckySheetBindData,borderKey);
//                Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"));
//        		if(cellBorder != null)
//        		{
//        			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//        			if(cellBorder instanceof List)
//    				{
//    					borderInfos.addAll((Collection<? extends Object>) cellBorder);
//    				}else {
//    					borderInfos.add(cellBorder);
//    				}
//        		}
    		}
    	}
        if(j == dataSize-1)
        {
        	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
        		maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+luckySheetBindData.getColSpan());
        	}
        }
        for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
        	maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()*data.size());
        }
        maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
	}
	
	/**  
	 * @Title: processHorizontalGroupSummaryValue
	 * @Description: 处理分组数据向右扩展数据
	 * @param j
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param rowAndCol
	 * @param groupSummaryData
	 * @param data
	 * @param cellDatas
	 * @param dataRowLen
	 * @param dataColLen
	 * @param mergeMap
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @param objectMapper
	 * @param dataSize
	 * @author caiyang
	 * @date 2022-03-21 07:39:29 
	 */ 
	private void processHorizontalGroupSummaryValue(int j,Map<String, Integer> maxCoordinate,LuckySheetFormsBindData luckySheetBindData,Map<String, Integer> rowAndCol,GroupSummaryData groupSummaryData,
			List<Map<String, Object>> data,List<Map<String, Object>> cellDatas,Object dataRowLen,Object dataColLen,Map<String, Map<String, Object>> mergeMap,
			Map<String, Object> rowlen,Map<String, Object> columnlen,ObjectMapper objectMapper, int dataSize,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos
			,Map<String, JSONObject> extendCellOrigin,Map<String, JSONObject> columnStartCoords,Map<String, Object> configRowLen,Map<String, Object> configColumnLen
			,JSONObject drillCells,Map<String, JSONArray> cellConditionFormat)
	{
//		Map<String, Object> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
        if(j == 0)
		{
			this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		}
        Map<String, Object> cellData = null;
        try {
            cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        String key = cellData.get(LuckySheetPropsEnum.R.getCode())+"_"+cellData.get(LuckySheetPropsEnum.C.getCode());
        if(luckySheetBindData.getCellAttrs().getBooleanValue("isDrill") && drillCells != null)
    	{
    		JSONObject drillInfo = new JSONObject();
    		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getCellAttrs().get("drillId"));
    		JSONObject drillParams = getDrillParams(data.get(0),luckySheetBindData.getCellAttrs().getString("drillAttrs"));
    		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
    		drillCells.put(key, drillInfo);
    	}
        this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX"), rowAndCol.get("maxY"), j == 0);
        groupSummaryData.setDatas(data);
        String value = luckySheetGroupCalculates.get(luckySheetBindData.getCellFunction().intValue()).calculate(groupSummaryData);
      //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer())
		{
			Object resultData  = this.processUnitTransfer(value, luckySheetBindData);
			if(resultData != null)
			{
				value = String.valueOf(resultData);
			}
		}
        String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
    	Object v = LuckysheetUtil.formatValue(format, value);
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v==null?"":String.valueOf(v));
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v==null?"":String.valueOf(v));
        if(dataRowLen != null)
        {
        	if(rowlen.get(String.valueOf(rowAndCol.get("maxX"))) != null)
        	{
        		rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
        	}
        }
        if(dataColLen != null)
        {
        	if(columnlen.get(String.valueOf(rowAndCol.get("maxY"))) != null)
        	{
        		columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
        	}
        }
        if(rowAndCol.get("maxX")>maxX)
        {
            maxX = rowAndCol.get("maxX");
        }
        if(data.size()>1)
    	{
        	Map<String, Object> mergeConfig = new HashMap<String, Object>();
    		mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    		mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    		int rowSpan = luckySheetBindData.getRowSpan();
    		int colSpan = data.size();
    		if(luckySheetBindData.getIsMerge().intValue() == 1)
    		{
    			colSpan = data.size() * luckySheetBindData.getColSpan();
    		}
    		mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
    		mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
    		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
    		cellDatas.add(cellData);
    		Map<String, Object> merge = new HashMap<String, Object>();
    		merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    		merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    		merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
    		merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
    		mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
    		for (int k = 1; k < rowSpan; k++) {
    			for (int i = 1; i <= colSpan; i++) {
    				if(k == 1 && i == 1)
					{
						//do nothing
					}else {
						Map<String, Object> mc = new HashMap<String, Object>();
	    				mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
	    				mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
	    				Map<String, Object> cellValue = new HashMap<String, Object>();
	    				Map<String, Object> mergeCellData = new HashMap<String, Object>();
	    				mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
	    				mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
	    				mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
	    				cellDatas.add(mergeCellData);
	    				Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i-1));
						if(mcDataColLen != null)
						{
							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
							{
								columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
							}
						}
					}
    			}
    			Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
				if(mcDataRowLen != null)
				{
					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
					{
						rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
					}
				}
    		}
    		if((rowAndCol.get("maxY")+colSpan-1)>maxY)
    		{
    			maxY = rowAndCol.get("maxY")+colSpan-1;
    		}
    		if((rowAndCol.get("maxX")+rowSpan-1)>maxX)
    		{
    			maxX = rowAndCol.get("maxX")+rowSpan-1;
    		}
    		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
    		this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1,borderInfo,luckySheetBindData,borderKey);
//    		Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1);
//     		if(cellBorder != null)
//     		{
//     			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//     			if(cellBorder instanceof List)
// 				{
// 					borderInfos.addAll((Collection<? extends Object>) cellBorder);
// 				}else {
// 					borderInfos.add(cellBorder);
// 				}
//     		}
    	}else {
        	if(luckySheetBindData.getIsMerge().intValue() == 1) {
        		Map<String, Object> mergeConfig = new HashMap<String, Object>();
    			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),luckySheetBindData.getRowSpan());
    			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
    			cellDatas.add(cellData);
    			Map<String, Object> merge = new HashMap<String, Object>();
    			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
    			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
    			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
    			for (int k = 1; k <= luckySheetBindData.getRowSpan(); k++) {
    				for (int i = 1; i <= luckySheetBindData.getColSpan(); i++) {
    					if(k == 1 && i == 1)
    					{
    						//do nothing
    					}else {
    						Map<String, Object> mc = new HashMap<String, Object>();
    						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    						Map<String, Object> cellValue = new HashMap<String, Object>();
    						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
    						Map<String, Object> mergeCellData = new HashMap<String, Object>();
    						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
    						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
    						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
    						cellDatas.add(mergeCellData);
    						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
    						if(mcDataColLen != null)
    						{
    							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
    							{
    								columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
    							}
    						}
    					}
    				}
    				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
    				if(mcDataRowLen != null)
					{
    					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
    					{
    						rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);	
    					}
					}
    			}
    			if((rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1)>maxX)
    			{
    				maxX = rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1;
    			}
    			if((rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1)>maxY)
    			{
    				maxY = rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1;
    			}
    			String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
    			this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//    			Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
//        		if(cellBorder != null)
//        		{
//        			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//        			if(cellBorder instanceof List)
//    				{
//    					borderInfos.addAll((Collection<? extends Object>) cellBorder);
//    				}else {
//    					borderInfos.add(cellBorder);
//    				}
//        		}
        	}else {
        		cellDatas.add(cellData);
                if(rowAndCol.get("maxY")>maxY)
                {
                	maxY = rowAndCol.get("maxY");
                }
                String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
                this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"),borderInfo,luckySheetBindData,borderKey);
//                Object cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"));
//        		if(cellBorder != null)
//        		{
//        			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//        			if(cellBorder instanceof List)
//    				{
//    					borderInfos.addAll((Collection<? extends Object>) cellBorder);
//    				}else {
//    					borderInfos.add(cellBorder);
//    				}
//        		}
        	}
    	}
        for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
        	maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()*data.size());	
        }
        if(j==dataSize-1)
        {
        	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
        		maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+luckySheetBindData.getColSpan());
        	}
        }
        maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
	}
	
	/**  
	 * @MethodName: processExtendCellOrigin
	 * @Description: 扩展单元格和原始单元格的关系
	 * @author caiyang
	 * @param extendCellOrigin
	 * @param luckySheetBindData
	 * @param rowAndCol 
	 * @return void
	 * @date 2022-12-09 08:31:14 
	 */  
	private void processExtendCellOrigin(Map<String, JSONObject> extendCellOrigin,LuckySheetFormsBindData luckySheetBindData,Map<String, Integer> rowAndCol)
	{
		String cellFlag = rowAndCol.get("maxX") + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() + rowAndCol.get("maxY");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("r", luckySheetBindData.getCoordsx());
		jsonObject.put("c", luckySheetBindData.getCoordsy());
		Map<String, Object> cellData = luckySheetBindData.getCellData();
		if(cellData.get("v") != null)
		{
			Map<String, Object> v = (Map<String, Object>) cellData.get("v");
			Object bg = v.get("bg");
			if(bg != null)
			{
				jsonObject.put("bg", bg);
			}
			Object ps = v.get("ps");
			if(ps != null)
			{
				jsonObject.put("ps", ps);
			}
		}
		
		extendCellOrigin.put(cellFlag, jsonObject);
	}
	
	private void processExtendCellOrigin(Map<String, JSONObject> extendCellOrigin,LuckysheetReportFormsCell luckysheetReportFormsCell)
	{
		String cellFlag = luckysheetReportFormsCell.getCoordsx() + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() + luckysheetReportFormsCell.getCoordsy();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("r", luckysheetReportFormsCell.getCoordsx());
		jsonObject.put("c", luckysheetReportFormsCell.getCoordsy());
		Map<String, Object> cellData = JSON.parseObject(luckysheetReportFormsCell.getCellData(), Map.class);
		if(cellData.get("v") != null)
		{
			Map<String, Object> v = (Map<String, Object>) cellData.get("v");
			Object bg = v.get("bg");
			if(bg != null)
			{
				jsonObject.put("bg", bg);
			}
			Object ps = v.get("ps");
			if(ps != null)
			{
				jsonObject.put("ps", ps);
			}
		}
		
		extendCellOrigin.put(cellFlag, jsonObject);
	}
	
	/**  
	 * @MethodName: processColumnStartCoords
	 * @Description: 原始动态单元格对应的起始单元格
	 * @author caiyang
	 * @param columnStartCoords
	 * @param luckySheetBindData
	 * @param rowAndCol 
	 * @return void
	 * @date 2022-12-09 08:31:28 
	 */  
	private void processColumnStartCoords(Map<String, JSONObject> columnStartCoords,LuckySheetFormsBindData luckySheetBindData,Map<String, Integer> rowAndCol) {
		String originCell = luckySheetBindData.getCoordsx() + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() + luckySheetBindData.getCoordsy();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("r", rowAndCol.get("maxX"));
		jsonObject.put("c", rowAndCol.get("maxY"));
		columnStartCoords.put(originCell, jsonObject);
	}

	/**  
	 * @MethodName: reportData
	 * @Description: 上报数据
	 * @author caiyang
	 * @param model
	 * @return
	 * @see com.springreport.api.reporttpl.IReportTplFormsService#reportData(com.springreport.dto.reporttpl.ReportDataDto)
	 * @date 2022-11-23 07:23:23 
	 */  
	@Override
	@Transactional
	public BaseEntity reportData(ReportDataDto model,UserInfoDto userInfoDto) {
		ResReportDataDto result = new ResReportDataDto();
		Map<String, Map<String, List<ReportDataDetailDto>>> reportDataDetails = new HashMap<>();
		Map<String, List<String>> datasourceTalbes = new HashMap<>();
		ReportTpl reportTpl = this.iReportTplService.getById(model.getTplId());
		if (reportTpl == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"报表模板"}));
		}
		if(reportTpl.getConcurrencyFlag().intValue() == YesNoEnum.YES.getCode())
		{
			if(model.getVersion() == null)
			{
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.reportversion.null", null));
			}else {
				Long version = redisUtil.increment(RedisPrefixEnum.REPORTDATAVERSION.getCode()+reportTpl.getId());
				if(model.getVersion().longValue() != (version.longValue()-1))
				{
					throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.reportversion.change", null));
				}
			}
		}
		if(model.getMainRowDatas() == null) {
			model.setMainRowDatas(new JSONObject());
		}
		LinkedHashMap<String, Map<String, Object>> mainReportDatas = new LinkedHashMap<>();//主数据集
		LinkedHashMap<String, Map<String, Object>> reportDatas = new LinkedHashMap<>();//主数据集
		if(model.getReportDatas() != null && !model.getReportDatas().isEmpty()&&!StringUtil.isEmptyMap(model.getMainDatasources())) {
			Set<String> set = model.getReportDatas().keySet();
			for (String o : set) {
				String sheetIndex = o.split("\\|")[0];
				String datasourceId = o.split("\\|")[1];
				String table = o.split("\\|")[2];
				List<String> tables =  datasourceTalbes.get(datasourceId);
				if(tables == null) {
					tables = new ArrayList<>();
					datasourceTalbes.put(datasourceId, tables);
				}
				if(!tables.contains(table)) {
					tables.add(table);
				}
				String name = o.split("\\|")[3];
				if(model.getMainDatasources().containsKey(sheetIndex)) {
					String key = datasourceId + "|" + name + "|" + table;
					if(model.getMainDatasources().getJSONObject(sheetIndex).containsKey(key)) {
						mainReportDatas.put(o, model.getReportDatas().get(o));
					}else {
						reportDatas.put(o, model.getReportDatas().get(o));
					}
				}else {
					reportDatas.put(o, model.getReportDatas().get(o));
				}
			}
			mainReportDatas.putAll(reportDatas);
			model.setReportDatas(mainReportDatas);
		}
		if(model.getReportDatas() != null && !model.getReportDatas().isEmpty())
		{
			Set<String> set = model.getReportDatas().keySet();
			for (String o : set) {
//				Map<String, String> diff = StringUtil.compare2MapDiff(value, basicData);
				if(model.getBasicDatas().containsKey(o))
				{
					Map<String, Object> basicData = model.getBasicDatas().get(o);
					Map<String, String> diff = StringUtil.compare2MapDiff(model.getReportDatas().get(o), basicData);
					if(diff != null)
					{
						String changeAfter = diff.get("changeAfter");
						if(StringUtil.isNullOrEmpty(changeAfter) || "{}".equals(changeAfter)) {
							continue;
						}
					}
				}
				String sheetIndex = o.split("\\|")[0];
				String datasourceId = o.split("\\|")[1];
				Map<String, List<ReportDataDetailDto>> datasourceMap = reportDataDetails.get(datasourceId);
				if(datasourceMap == null)
				{
					datasourceMap = new HashMap<>();
					reportDataDetails.put(datasourceId, datasourceMap);
				}
				String table = o.split("\\|")[2];
				String name = o.split("\\|")[3];
				List<ReportDataDetailDto> tableList = datasourceMap.get(table);
				if(tableList == null)
				{
					tableList = new ArrayList<>();
					datasourceMap.put(table, tableList);
				}
				Map<String, Object> datas = model.getReportDatas().get(o);
				if(datas != null && !datas.isEmpty())
				{
					ReportDataDetailDto reportDataDetailDto = new ReportDataDetailDto();
					List<ReportDataColumnDto> keys = new ArrayList<>();
					List<ReportDataColumnDto> columns  = new ArrayList<>();
					JSONObject autoFillAttrs = new JSONObject();
					Set<String> mapKeys = datas.keySet();
					reportDataDetailDto.setDatasoaurceId(datasourceId);
					reportDataDetailDto.setFormsName(name);
					if(datas.containsKey("isSRInsertData")) {
						reportDataDetailDto.setInsert((boolean) datas.get("isSRInsertData"));
					}
					for (String key : mapKeys) {
						if("isSRrowChanged".equals(key) || "isSRInsertData".equals(key)) {
							continue;
						}
						String reculateKey = o + "|" + key;
						String mainkey = datasourceId + "|" + name + "|" + table + "|" + key;
						ReportDataColumnDto dataColumnDto = new ReportDataColumnDto();
						if(model.getDatasKey() != null && !model.getDatasKey().isEmpty())
						{
							String tableKeyFlag = sheetIndex+"|"+datasourceId+"|"+table+"|"+key+"|"+name;
							if(model.getDatasKey().containsKey(tableKeyFlag))
							{
								dataColumnDto.setColumnName(key);
								dataColumnDto.setIdType(Integer.parseInt(String.valueOf(model.getDatasKey().get(tableKeyFlag).get("idType"))));
								if(datas.get(key) != null)
								{
									dataColumnDto.setData(datas.get(key));
									columns.add(dataColumnDto);
								}else {
									if(String.valueOf(model.getDatasKey().get(tableKeyFlag).get("idType")).equals("2"))
									{	
										dataColumnDto.setData(IdWorker.getIdStr());
										reportDataDetailDto.setInsert(true);
										columns.add(dataColumnDto);
										if(!StringUtil.isEmptyMap(model.getMainDatasources())) {
											if(model.getMainDatasources().containsKey(sheetIndex) 
													&& model.getMainDatasources().getJSONObject(sheetIndex).containsKey(mainkey)) {
												model.getMainRowDatas().put(sheetIndex + "|"+mainkey, dataColumnDto.getData());
											}
										}
									}
								}
								keys.add(dataColumnDto);
							}else {
								dataColumnDto.setColumnName(key);
								if(model.getReCalculate() != null)
								{
									if(model.getReCalculate().containsKey(reculateKey))
									{
										JSONObject jsonObject = model.getReCalculate().getJSONObject(reculateKey);
										Object value = datas.get(key);
 										int transferType = jsonObject.getIntValue("transferType");
										String multiple = jsonObject.getString("multiple");
 										int digit = jsonObject.getIntValue("digit");
										if(value == null)
										{
											value = 0;
										}
 										BigDecimal data = new BigDecimal(String.valueOf(value));
 										if(transferType == 1)
										{
											data = data.divide(new BigDecimal(multiple));
										}else {
											data = data.multiply(new BigDecimal(multiple));
										}
										data = data.setScale(digit, RoundingMode.HALF_UP);
										dataColumnDto.setData(data);
									}else {
										dataColumnDto.setData(datas.get(key));
									}
								}else {
									dataColumnDto.setData(datas.get(key));
								}
								columns.add(dataColumnDto);
							}
						}else {
							dataColumnDto.setColumnName(key);
							if(model.getReCalculate() != null)
							{
								if(model.getReCalculate().containsKey(reculateKey))
								{
									JSONObject jsonObject = model.getReCalculate().getJSONObject(reculateKey);
									Object value = datas.get(key);
									int transferType = jsonObject.getIntValue("transferType");
									String multiple = jsonObject.getString("multiple");
									int digit = jsonObject.getIntValue("digit");
									if(value == null)
									{
										value = 0;
									}
									BigDecimal data = new BigDecimal(String.valueOf(value));
									if(transferType == 1)
									{
										data = data.divide(new BigDecimal(multiple));
									}else {
										data = data.multiply(new BigDecimal(multiple));
									}
									data = data.setScale(digit, RoundingMode.HALF_UP);
									dataColumnDto.setData(data);
								}else {
									dataColumnDto.setData(datas.get(key));
								}
							}else {
								dataColumnDto.setData(datas.get(key));
							}
							columns.add(dataColumnDto);
						}
					}
					if(!StringUtil.isEmptyMap(model.getMainAttrs()) && model.getMainAttrs().containsKey(sheetIndex)) {
						String mainAttrKey = datasourceId + "|" + name + "|" + table;
						JSONObject mainAttr = model.getMainAttrs().getJSONObject(sheetIndex);
						if(mainAttr.containsKey(mainAttrKey)) {
							JSONArray attrs = mainAttr.getJSONArray(mainAttrKey);
							if(ListUtil.isNotEmpty(attrs)) {
								ReportDataColumnDto dataColumnDto = null;
								for (int i = 0; i < attrs.size(); i++) {
									JSONObject jsonObject = attrs.getJSONObject(i);
									String columnName = jsonObject.getString("columnName");
									String mainColumn = jsonObject.getString("mainColumn");
									String mainTable = jsonObject.getString("mainTable");
									String mainName = jsonObject.getString("mainName");
									String mainDatasourceId = jsonObject.getString("mainDatasourceId");
									String mainRowDataKey = sheetIndex + "|" + mainDatasourceId + "|" + mainName + "|" + mainTable + "|" + mainColumn;
									if(model.getMainRowDatas().containsKey(mainRowDataKey)) {
										dataColumnDto = new ReportDataColumnDto();
										dataColumnDto.setColumnName(columnName);
										dataColumnDto.setData(model.getMainRowDatas().get(mainRowDataKey));
										columns.add(dataColumnDto);
									}
								}
							}
						}
					}
					if(model.getAutoFillAttrs() != null && model.getAutoFillAttrs().getJSONObject(sheetIndex) != null) {
						JSONObject autoFills = model.getAutoFillAttrs().getJSONObject(sheetIndex);
						autoFills.forEach((key, value) -> {
						    if(key.startsWith(datasourceId+"|")) {
						    	autoFillAttrs.put(key, value);
						    }
						});
					}
					reportDataDetailDto.setColumns(columns);
					reportDataDetailDto.setKeys(keys);
					reportDataDetailDto.setAutoFillAttrs(autoFillAttrs);
					tableList.add(reportDataDetailDto);
				}
			}
		}
		if(!reportDataDetails.isEmpty())
		{
			Set<String> datasourceIds = reportDataDetails.keySet();
			for(String datasourceId:datasourceIds)
			{
				Map<String, Map<String, String>> tableColumnJavaType = new HashMap<String, Map<String,String>>();
				ReportDatasource reportDatasource = this.iReportDatasourceService.getById(Long.valueOf(datasourceId));
				DataSourceConfig dataSourceConfig = new DataSourceConfig(Long.valueOf(datasourceId), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
				DataSource dataSource = JdbcUtils.getDataSource(dataSourceConfig);
				if(reportDatasource.getType().intValue() == 5 || reportDatasource.getType().intValue() == 11 || reportDatasource.getType().intValue() == 12) {
					List<String> tables = datasourceTalbes.get(datasourceId);
					if(ListUtil.isNotEmpty(tables)) {
						for (int i = 0; i < tables.size(); i++) {
							String sqlText = "select *" + " from " + tables.get(i);
							Map<String, String> columnJavaType = JdbcUtils.parseMetaDataColumnsJavaType(dataSource, sqlText, reportDatasource.getType());
							tableColumnJavaType.put(tables.get(i), columnJavaType);
						}
					}
				}
				ReportDataUtil.reportData(dataSource,reportDataDetails.get(datasourceId),reportDatasource.getType(),userInfoDto,tableColumnJavaType);
			}
		}
		result.setStatusMsg(MessageUtil.getValue("info.reportdata"));
		String ip = CusAccessObjectUtil.getIpAddress(httpServletRequest);
		this.iLuckysheetReportFormsHisService.saveReportHis(model.getReportDatas(), model.getBasicDatas(), model.getTplId(),ip,userInfoDto);
		if(reportTpl.getConcurrencyFlag().intValue() == YesNoEnum.YES.getCode())
		{//该报表提交数据时需要控制防止多人编辑，先后提交数据覆盖的问题
			Long version = null;
			if(redisUtil.hasKey(RedisPrefixEnum.REPORTDATAVERSION.getCode()+reportTpl.getId()))
			{
				version =  new Long(String.valueOf(redisUtil.get(RedisPrefixEnum.REPORTDATAVERSION.getCode()+reportTpl.getId())));
			}else {
				version = redisUtil.increment(RedisPrefixEnum.REPORTDATAVERSION.getCode()+reportTpl.getId());
			}
			result.setVersion(version);;
		}
		return result;
	}
	
	/**  
	 * @MethodName: processCellWarning
	 * @Description: 单元格预警处理
	 * @author caiyang
	 * @param value
	 * @param bindData 
	 * @return void
	 * @date 2022-11-04 08:02:39 
	 */  
	private JSONObject processCellWarning(Object value,LuckySheetFormsBindData bindData) {
		if(value != null && CheckUtil.isNumber(String.valueOf(value)))
		{
			String rules = bindData.getWarningRules();
			BigDecimal data = new BigDecimal(String.valueOf(value));
			BigDecimal threshold = new BigDecimal(bindData.getThreshold());
			switch (rules) {
			case ">":
				if(data.compareTo(threshold)>0)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("left", null);
					jsonObject.put("top", null);
					jsonObject.put("width", null);
					jsonObject.put("height", null);
					jsonObject.put("value", bindData.getWarningContent());
					jsonObject.put("isshow", false);
					return jsonObject;
				}
				break;
			case ">=":
				if(data.compareTo(threshold)>=0)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("left", null);
					jsonObject.put("top", null);
					jsonObject.put("width", null);
					jsonObject.put("height", null);
					jsonObject.put("value", bindData.getWarningContent());
					jsonObject.put("isshow", false);
					return jsonObject;
				}
				break;
			case "=":
				if(data.compareTo(threshold)==0)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("left", null);
					jsonObject.put("top", null);
					jsonObject.put("width", null);
					jsonObject.put("height", null);
					jsonObject.put("value", bindData.getWarningContent());
					jsonObject.put("isshow", false);
					return jsonObject;
				}
				break;
			case "<":
				if(data.compareTo(threshold)<0)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("left", null);
					jsonObject.put("top", null);
					jsonObject.put("width", null);
					jsonObject.put("height", null);
					jsonObject.put("value", bindData.getWarningContent());
					jsonObject.put("isshow", false);
					return jsonObject;
				}
				break;
			case "<=":
				if(data.compareTo(threshold)<=0)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("left", null);
					jsonObject.put("top", null);
					jsonObject.put("width", null);
					jsonObject.put("height", null);
					jsonObject.put("value", bindData.getWarningContent());
					jsonObject.put("isshow", false);
					return jsonObject;
				}
				break;
			default:
				break;
			}
			
		}
		return null;
	}
	
	private Object processUnitTransfer(Object value,LuckySheetFormsBindData bindData) {
		Object result = null;
		try {
			if(value != null && CheckUtil.isNumber(String.valueOf(value)))
			{
				BigDecimal data = new BigDecimal(String.valueOf(value));
				int transferType = bindData.getTransferType().intValue();
				switch (transferType) {
				case 1://乘法
					data = data.multiply(new BigDecimal(bindData.getMultiple()));
					result = data.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
					break;
				case 2://除法
					data = data.divide(new BigDecimal(bindData.getMultiple()));
					result = data.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result == null)
		{
			result = value;
		}
		return result;
	}
	/**  
	 * @MethodName: getDrillParams
	 * @Description: 获取下钻参数
	 * @author caiyang
	 * @param cellData
	 * @param drillAttrs
	 * @return 
	 * @return JSONObject
	 * @date 2023-05-09 08:38:28 
	 */  
	private JSONObject getDrillParams(Map<String, Object> cellData,String drillAttrs)
	{
		JSONObject result = null;
		if(StringUtil.isNotEmpty(drillAttrs) && cellData != null && !cellData.isEmpty())
		{
			result = new JSONObject();
			String[] attrs = drillAttrs.split(",");
			for (int i = 0; i < attrs.length; i++) {
				Object value = cellData.get(attrs[i]);
				if(value != null)
				{
					result.put(attrs[i], String.valueOf(value));
				}
			}
		}
		return result;
	}
	
	/**  
	 * @MethodName: processConditionFormat
	 * @Description: 条件格式处理
	 * @author caiyang
	 * @param luckySheetBindData
	 * @param cellConditionFormat 单元格对应的条件格式
	 * @param x 计算后的横坐标
	 * @param y 计算后的纵坐标
	 * @param extend 扩展方向 1不扩展 2向下扩展 2向右扩展 
	 * @param isInit 是否是第一个计算的单元格
	 * void
	 * @date 2024-08-18 08:56:52 
	 */ 
	private void processConditionFormat(LuckySheetFormsBindData luckySheetBindData,Map<String, JSONArray> cellConditionFormat,int x,int y,boolean isInit) {
		String conditonFormatKey = luckySheetBindData.getCoordsx() + "_" + luckySheetBindData.getCoordsy();
		if(!StringUtil.isEmptyMap(cellConditionFormat) && cellConditionFormat.containsKey(conditonFormatKey)) {
			for (int i = 0; i < cellConditionFormat.get(conditonFormatKey).size(); i++) {
				if(isInit) {
					JSONArray cellrange = new JSONArray();
					JSONObject range = new JSONObject();
					JSONArray row = new JSONArray();
					row.add(x);
					row.add(x);
					JSONArray column = new JSONArray();
					column.add(y);
					column.add(y);
					range.put("row", row);
					range.put("column", column);
					cellrange.add(range);
					cellConditionFormat.get(conditonFormatKey).getJSONObject(i).put("cellrange", cellrange);
				}else {
					JSONObject range = cellConditionFormat.get(conditonFormatKey).getJSONObject(i).getJSONArray("cellrange").getJSONObject(0);
					range.getJSONArray("row").set(1, x);
					range.getJSONArray("column").set(1, y);
				}
			}
		}
	}
	
	private Map<String, Object> getDatasetDatas(ReportTpl reportTpl,MesGenerateReportDto mesGenerateReportDto,String datasetName,
			boolean isPagination,Map<String, Object> mergePagination,List<Map<String, String>> reportSqls,UserInfoDto userInfoDto) throws Exception {
		Map<String, String> sqlMap = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> datas = null;
		Map<String, Object> searchInfo = null;
		if(YesNoEnum.YES.getCode().intValue() == reportTpl.getIsParamMerge().intValue() || YesNoEnum.YES.getCode().intValue() == mesGenerateReportDto.getIsMobile().intValue())
		{
			searchInfo = mesGenerateReportDto.getSearchData().get(0);
		}else {
			searchInfo = this.getDatasetParamInfo(datasetName, mesGenerateReportDto);
		}
		//获取数据源
		Map<String, Object> dataSetAndDatasource = this.iReportTplDatasetService.getTplDatasetAndDatasource(reportTpl.getId(), datasetName,userInfoDto.getMerchantNo());
		int type = (int) dataSetAndDatasource.get("type");
		DataSource dataSource = null;
		InfluxDBConnection dbConnection = null;
		TDengineConnection tDengineConnection = null;
		Object data = dataSetAndDatasource.get("dataSource");
		String userName = dataSetAndDatasource.get("useName") != null?String.valueOf(dataSetAndDatasource.get("useName")):"";
		String password = dataSetAndDatasource.get("password") != null?String.valueOf(dataSetAndDatasource.get("password")):"";
		if(data instanceof DataSource)
		{
			dataSource = (DataSource) data;
		}else if(data instanceof InfluxDBConnection)
		{
			dbConnection = (InfluxDBConnection) data;
		}else if(data instanceof TDengineConnection)
		{
			tDengineConnection = (TDengineConnection) data;
		}
		ReportTplDataset reportTplDataset = (ReportTplDataset) dataSetAndDatasource.get("tplDataSet");
		Map<String, Object> params = null;
		if(searchInfo != null)
		{
			params = ParamUtil.getViewParams((JSONArray) searchInfo.get("params"),userInfoDto);
		}
		if(DatasetTypeEnum.SQL.getCode().intValue() == reportTplDataset.getDatasetType().intValue())
		{
			String sql = reportTplDataset.getTplSql();
			if(SqlTypeEnum.SQL.getCode().intValue() == reportTplDataset.getSqlType().intValue())
			{
				String countSql = "";
				if(type == 6)
				{//influxdb
					sql = JdbcUtils.processSqlParams(sql, params);
					if(isPagination && YesNoEnum.YES.getCode().intValue() == reportTplDataset.getIsPagination().intValue())
					{
						countSql = JdbcUtils.getInfluxdbCountSql(sql);
						ReportDatasource reportDatasource = this.iReportDatasourceService.getById(reportTplDataset.getDatasourceId());
						if(YesNoEnum.YES.getCode().intValue() == mesGenerateReportDto.getIsCustomerPage().intValue())
						{
							sql = JdbcUtils.getPaginationSql(sql, reportDatasource.getType(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))), mesGenerateReportDto.getStartPage(),mesGenerateReportDto.getEndPage());
						}else {
							sql = JdbcUtils.getPaginationSql(sql, reportDatasource.getType(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("currentPage"))));
						}
					}
					datas = ReportDataUtil.getInfluxdbData(dbConnection, sql);
					if(StringUtil.isNotEmpty(countSql))
					{
						int count = ReportDataUtil.getInfluxdbDataCountBySQL(dbConnection, countSql);
						Long totalCount = (Long) mergePagination.get("totalCount");
						Integer pageCount = (Integer) mergePagination.get("pageCount");
						Integer paramsPageCount = Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount")));
						if(totalCount == null)
						{
							mergePagination.put("totalCount", count);
//							mergePagination.put("totalPage", count/Integer.valueOf(String.valueOf(params.get("pageCount")))+1);
						}else {
							if(count > totalCount)
							{
								mergePagination.put("totalCount", count);
//								mergePagination.put("totalPage", count/Integer.valueOf(String.valueOf(params.get("pageCount")))+1);
							}
						}
						if(pageCount == null)
						{
							mergePagination.put("pageCount", pageCount);
						}else {
							if(paramsPageCount < pageCount)
							{
								mergePagination.put("pageCount", paramsPageCount);
							}
						}
					}
				}else if(type == 10)
				{//tdengine
					sql = JdbcUtils.processSqlParams(sql, params);
					if(isPagination && YesNoEnum.YES.getCode().intValue() == reportTplDataset.getIsPagination().intValue())
					{
						countSql = JdbcUtils.getCountSql(sql);
						ReportDatasource reportDatasource = this.iReportDatasourceService.getById(reportTplDataset.getDatasourceId());
						if(YesNoEnum.YES.getCode().intValue() == mesGenerateReportDto.getIsCustomerPage().intValue())
						{
							sql = JdbcUtils.getPaginationSql(sql, reportDatasource.getType(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))), mesGenerateReportDto.getStartPage(),mesGenerateReportDto.getEndPage());
						}else {
							sql = JdbcUtils.getPaginationSql(sql, reportDatasource.getType(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("currentPage"))));
						}
					}
					datas = ReportDataUtil.getDatasourceDataBySql(tDengineConnection.getConnection(), sql);
					if(StringUtil.isNotEmpty(countSql))
					{
						tDengineConnection.buildConnection();
						int count = ReportDataUtil.getDataCountBySQL(tDengineConnection.getConnection(), countSql);
						Long totalCount = (Long) mergePagination.get("totalCount");
						Integer pageCount = (Integer) mergePagination.get("pageCount");
						Integer paramsPageCount = Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount")));
						if(totalCount == null)
						{
							mergePagination.put("totalCount", count);
						}else {
							if(count > totalCount)
							{
								mergePagination.put("totalCount", count);
							}
						}
						if(pageCount == null)
						{
							mergePagination.put("pageCount", paramsPageCount);
						}else {
							if(paramsPageCount < pageCount)
							{
								mergePagination.put("pageCount", paramsPageCount);
							}
						}
					}
				}else {
					sql = JdbcUtils.processSqlParams(sql,params);
					if(isPagination && YesNoEnum.YES.getCode().intValue() == reportTplDataset.getIsPagination().intValue())
					{
						countSql = JdbcUtils.getCountSql(sql);
						ReportDatasource reportDatasource = this.iReportDatasourceService.getById(reportTplDataset.getDatasourceId());
						if(YesNoEnum.YES.getCode().intValue() == mesGenerateReportDto.getIsCustomerPage().intValue())
						{
							sql = JdbcUtils.getPaginationSql(sql, reportDatasource.getType(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))), mesGenerateReportDto.getStartPage(),mesGenerateReportDto.getEndPage());
						}else {
							sql = JdbcUtils.getPaginationSql(sql, reportDatasource.getType(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("currentPage"))));
						}
					}
					//根据sql获取数据
					if(type == 9)
					{
						datas = ReportDataUtil.getDatasourceDataBySql(dataSource, sql,userName,password);
						if(isPagination && YesNoEnum.YES.getCode().intValue() == reportTplDataset.getIsPagination().intValue())
						{
							datas = ListUtil.getSubList(datas,Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("currentPage"))),Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))));
						}
					}else {
						datas = ReportDataUtil.getDatasourceDataBySql(dataSource, sql);
					}
					if(StringUtil.isNotEmpty(countSql))
					{
						long count = ReportDataUtil.getDataCountBySQL(dataSource, countSql);
						Long totalCount = (Long) mergePagination.get("totalCount");
						Integer pageCount = (Integer) mergePagination.get("pageCount");
						Integer paramsPageCount = Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount")));
						if(totalCount == null)
						{
							mergePagination.put("totalCount", count);
						}else {
							if(count > totalCount)
							{
								mergePagination.put("totalCount", count);
							}
						}
						if(pageCount == null)
						{
							mergePagination.put("pageCount", paramsPageCount);
						}else {
							if(paramsPageCount < pageCount)
							{
								mergePagination.put("pageCount", paramsPageCount);
							}
						}
					}
				}
			}else {
				datas = ReportDataUtil.getDatasourceDataByProcedure(dataSource, sql, params, JSONArray.parseArray(reportTplDataset.getInParam()), JSONArray.parseArray(reportTplDataset.getOutParam()));
			}
			sqlMap.put("name", reportTplDataset.getDatasetName());
			sqlMap.put("sql", sql);
			reportSqls.add(sqlMap);
		}else {//api
			ReportDatasource reportDatasource = this.iReportDatasourceService.getById(reportTplDataset.getDatasourceId());
			Map<String, String> headers = null;
			if(StringUtil.isNotEmpty(reportDatasource.getApiRequestHeader()))
			{
				JSONArray headersArray = JSONArray.parseArray(reportDatasource.getApiRequestHeader());
				if(!ListUtil.isEmpty(headersArray))
				{
					headers = new HashMap<String, String>();
					for (int j = 0; j < headersArray.size(); j++) {
						String headerName = headersArray.getJSONObject(j).getString("headerName");
						if(mesGenerateReportDto.getApiHeaders() != null && mesGenerateReportDto.getApiHeaders().containsKey(headerName)) {
							headers.put(headerName, String.valueOf(mesGenerateReportDto.getApiHeaders().get(headerName)));	
						}else {
							headers.put(headerName, String.valueOf(headersArray.getJSONObject(j).getString("headerValue")));	
						}	
					}
				}
			}
			String result = null;
			if(isPagination && reportTplDataset.getIsPagination().intValue() == YesNoEnum.YES.getCode().intValue()) {
				//分页查询
				if(YesNoEnum.YES.getCode().intValue() == mesGenerateReportDto.getIsCustomerPage().intValue()) {
					params.put(reportTplDataset.getCurrentPageAttr(), mesGenerateReportDto.getStartPage());
					params.put(reportTplDataset.getPageCountAttr(), (mesGenerateReportDto.getEndPage()-mesGenerateReportDto.getStartPage()+1)*Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))));
				}else {
					params.put(reportTplDataset.getCurrentPageAttr(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("currentPage"))));
					params.put(reportTplDataset.getPageCountAttr(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))));
				}
			}
			if("post".equals(reportDatasource.getApiRequestType()))
			{
				result = HttpClientUtil.doPostJson(reportDatasource.getJdbcUrl(), JSONObject.toJSONString(params), headers);
			}else {
				result = HttpClientUtil.doGet(reportDatasource.getJdbcUrl(),headers,params);
			}
			Map<String, Object> apiResult = ReportDataUtil.getApiResult(result, reportDatasource.getApiResultType(), reportDatasource.getApiColumnsPrefix(),reportTplDataset.getIsPagination().intValue() == YesNoEnum.YES.getCode().intValue()?reportTplDataset.getTotalAttr():null);
			datas = (List<Map<String, Object>>) apiResult.get("datas");
			if(reportTplDataset.getIsPagination().intValue() == YesNoEnum.YES.getCode().intValue()) {
				Long totalCount = (Long) mergePagination.get("totalCount");
				Integer pageCount = (Integer) mergePagination.get("pageCount");
				Integer paramsPageCount = Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount")));
				long count = (long) apiResult.get("total");
				if(totalCount == null)
				{
					mergePagination.put("totalCount", count);
				}else {
					if(count > totalCount)
					{
						mergePagination.put("totalCount", count);
					}
				}
				if(pageCount == null)
				{
					mergePagination.put("pageCount", paramsPageCount);
				}else {
					if(paramsPageCount < pageCount)
					{
						mergePagination.put("pageCount", paramsPageCount);
					}
				}
			}
		}
		resultMap.put("datas", datas);
		resultMap.put("params", params);
		return resultMap;
	}

	/**  
	 * @MethodName: deleteReportData
	 * @Description: 填报报表删除数据
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.reporttpl.IReportTplFormsService#deleteReportData(com.alibaba.fastjson.JSONObject, com.springreport.base.UserInfoDto)
	 * @date 2025-02-17 12:26:37 
	 */
	@Override
	public BaseEntity deleteReportData(JSONObject model, UserInfoDto userInfoDto) {
		BaseEntity result = new BaseEntity();
		Long datasourceId = model.getLongValue("datasourceId");
		ReportDatasource reportDatasource = this.iReportDatasourceService.getById(Long.valueOf(datasourceId));
		DataSourceConfig dataSourceConfig = new DataSourceConfig(Long.valueOf(datasourceId), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
		DataSource dataSource = JdbcUtils.getDataSource(dataSourceConfig);
		Map<String, String> columnJavaType = new HashMap<>();
		if(reportDatasource.getType().intValue() == 5 || reportDatasource.getType().intValue() == 11 || reportDatasource.getType().intValue() == 12) {
			String table = model.getString("table");
			String column = model.getString("column");
			String deleteType = model.getString("deleteType");
			String deleteColumn = model.getString("deleteColumn");
			String sqlText = "";
			if("1".equals(deleteType)) {//物理删除
				sqlText = "select " + column + " from " + table;
			}else {
				sqlText = "select " + column + " , " + deleteColumn + " from " + table;
			}
			columnJavaType = JdbcUtils.parseMetaDataColumnsJavaType(dataSource, sqlText, reportDatasource.getType());
		}
		ReportDataUtil.deleteData(dataSource, model, userInfoDto,columnJavaType);
		String ip = CusAccessObjectUtil.getIpAddress(httpServletRequest);
		this.iLuckysheetReportFormsHisService.saveDeleteHis(model,ip,userInfoDto);
		return result;
	}
	
}
