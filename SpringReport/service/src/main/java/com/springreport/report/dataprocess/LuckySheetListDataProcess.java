package com.springreport.report.dataprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.constants.StatusCode;
import com.springreport.dto.reporttpl.LuckySheetBindData;
import com.springreport.dto.reporttpl.LuckySheetFormsBindData;
import com.springreport.entity.luckysheetreportcell.LuckysheetReportCell;
import com.springreport.entity.luckysheetreportformscell.LuckysheetReportFormsCell;
import com.springreport.enums.AggregateTypeEnum;
import com.springreport.enums.CellExtendEnum;
import com.springreport.enums.CellValueTypeEnum;
import com.springreport.enums.DataFromEnum;
import com.springreport.enums.LuckySheetPropsEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;
import com.springreport.report.aggregate.Aggregate;
import com.springreport.report.aggregate.LuckySheetCrossAggregate;
import com.springreport.report.aggregate.LuckySheetFormsGroupAggregate;
import com.springreport.report.aggregate.LuckySheetFormsGroupSummaryAggregate;
import com.springreport.report.aggregate.LuckySheetFormsListAggregate;
import com.springreport.report.aggregate.LuckySheetGroupAggregate;
import com.springreport.report.aggregate.LuckySheetGroupSummaryAggregate;
import com.springreport.report.aggregate.LuckySheetListAggregate;
import com.springreport.util.ListUtil;
import com.springreport.util.SheetUtil;
import com.springreport.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**  
 * @ClassName: ListDataProcess
 * @Description: 列表式报表的动态数据处理
 * @author caiyang
 * @date 2021-05-27 04:33:34 
*/  
public class LuckySheetListDataProcess extends LuckySheetBasicDynamicDataProcess{

	private static Map<String,Aggregate<LuckysheetReportCell,LuckySheetBindData,Map<String, LuckySheetBindData>>> aggregates=new HashMap<String,Aggregate<LuckysheetReportCell,LuckySheetBindData,Map<String, LuckySheetBindData>>>();
	
	private static Map<String,Aggregate<LuckysheetReportFormsCell,LuckySheetFormsBindData,Map<String, LuckySheetFormsBindData>>> formsAggregates=new HashMap<String,Aggregate<LuckysheetReportFormsCell,LuckySheetFormsBindData,Map<String, LuckySheetFormsBindData>>>();
	
	static{
		aggregates.put(AggregateTypeEnum.GROUP.getCode(),new LuckySheetGroupAggregate());
		aggregates.put(AggregateTypeEnum.LIST.getCode(),new LuckySheetListAggregate());
		aggregates.put(AggregateTypeEnum.SUMMARY.getCode(),new LuckySheetListAggregate());
		aggregates.put(AggregateTypeEnum.GROUPSUMMARY.getCode(),new LuckySheetGroupSummaryAggregate());
		aggregates.put(AggregateTypeEnum.CROSS.getCode(),new LuckySheetCrossAggregate());
	}
	static {
		formsAggregates.put(AggregateTypeEnum.GROUP.getCode(),new LuckySheetFormsGroupAggregate());
		formsAggregates.put(AggregateTypeEnum.LIST.getCode(),new LuckySheetFormsListAggregate());
		formsAggregates.put(AggregateTypeEnum.SUMMARY.getCode(),new LuckySheetFormsListAggregate());
		formsAggregates.put(AggregateTypeEnum.GROUPSUMMARY.getCode(),new LuckySheetFormsGroupSummaryAggregate());
//		formsAggregates.put(AggregateTypeEnum.CROSS.getCode(),new LuckySheetCrossAggregate());
	}
	@Override
	public List<LuckySheetBindData> process(List<LuckysheetReportCell> variableCells, List<Map<String, Object>> data,String datasetName,
			Map<String, Map<String, List<List<Map<String, Object>>>>> processedCells,Map<String, LuckySheetBindData> blockBindDatas,
			Map<String, Object> subtotalCellDatas,Map<String, Object> subtotalCellMap,String sheetIndex,Map<String, LuckySheetBindData> cellBindData,Map<String, Integer> subTotalDigits,int tplType,List<String> subTotalCellCoords) {
		List<LuckySheetBindData> bindDatas = new ArrayList<LuckySheetBindData>();
		if(!ListUtil.isEmpty(data))
		{
			LuckySheetBindData bindData = null;
			String lastAggregateType = "";//上组数据聚合方式
//			boolean lastIsGroupMerge = false;//上组数据分组是否合一
//			int lastCellExtend = CellExtendEnum.NOEXTEND.getCode().intValue();//上组数据扩展方向
			int lastIsConditions = 2; 
			List<List<Map<String, Object>>> lastData = null;
			List<List<Map<String, Object>>> lastFilterData = null;
			ObjectMapper objectMapper = new ObjectMapper();
			for (int i = 0; i < variableCells.size(); i++) {
				String key = variableCells.get(i).getSheetId() + "-" + variableCells.get(i).getCoordsx()+"_"+variableCells.get(i).getCoordsy();
				bindData = new LuckySheetBindData();
				bindData.setTplType(tplType);
				bindData.setSheetId(variableCells.get(i).getSheetId());
				bindData.setReportCellId(variableCells.get(i).getId());
				bindData.setCoordsx(variableCells.get(i).getCoordsx());
				bindData.setCoordsy(variableCells.get(i).getCoordsy());
				bindData.setCellExtend(variableCells.get(i).getCellExtend());
				bindData.setAggregateType(variableCells.get(i).getAggregateType());
				bindData.setCellValueType(variableCells.get(i).getCellValueType());
				bindData.setIsLink(variableCells.get(i).getIsLink());
				bindData.setRowSpan(variableCells.get(i).getRowSpan());
				bindData.setColSpan(variableCells.get(i).getColSpan());
				bindData.setIsMerge(variableCells.get(i).getIsMerge());
				bindData.setDigit(variableCells.get(i).getDigit());
				bindData.setCellFunction(variableCells.get(i).getCellFunction());
				bindData.setDataFrom(variableCells.get(i).getDataFrom());
				bindData.setIsGroupMerge(variableCells.get(i).getIsGroupMerge());
				bindData.setGroupSummaryDependencyR(variableCells.get(i).getGroupSummaryDependencyR());
				bindData.setGroupSummaryDependencyC(variableCells.get(i).getGroupSummaryDependencyC());
				bindData.setIsFunction(variableCells.get(i).getIsFunction());
				bindData.setWarning(variableCells.get(i).getWarning());
				bindData.setWarningRules(variableCells.get(i).getWarningRules());
				bindData.setWarningContent(variableCells.get(i).getWarningContent());
				bindData.setWarningColor(variableCells.get(i).getWarningColor());
				bindData.setThreshold(variableCells.get(i).getThreshold());
				bindData.setIsRelied(variableCells.get(i).getIsRelied());
				bindData.setRelyCells(variableCells.get(i).getRelyCells());
				bindData.setDatasetName(variableCells.get(i).getDatasetName());
				bindData.setIsDict(variableCells.get(i).getIsDict());
				bindData.setDatasourceId(variableCells.get(i).getDatasourceId());
				bindData.setDictType(variableCells.get(i).getDictType());
				bindData.setHloopCount(variableCells.get(i).getHloopCount()==null?0:variableCells.get(i).getHloopCount());
				bindData.setHloopEmptyCount(variableCells.get(i).getHloopEmptyCount()==null?0:variableCells.get(i).getHloopEmptyCount());
				bindData.setVloopEmptyCount(variableCells.get(i).getVloopEmptyCount()==null?0:variableCells.get(i).getVloopEmptyCount());
				if(StringUtil.isNotEmpty(variableCells.get(i).getFormsAttrs())) {
					//填报设置如果设置了下拉单选数据字典，则以填报设置为准
					JSONObject formsAttrs = JSON.parseObject(variableCells.get(i).getFormsAttrs());
					if(!StringUtil.isEmptyMap(formsAttrs)) {
						String valueType = formsAttrs.getString("valueType");
						if("4".equals(valueType)) {
							String datasourceId = formsAttrs.getString("datasourceId");
							String dictType = formsAttrs.getString("dictType");
							if(StringUtil.isNotEmpty(datasourceId) && StringUtil.isNotEmpty(dictType)) {
								bindData.setIsDict(true);
								bindData.setDatasourceId(Long.parseLong(datasourceId));
								bindData.setDictType(dictType);
							}
						}
					}
				}
				bindData.setAlternateFormat(variableCells.get(i).getAlternateFormat());
				bindData.setAlternateFormatFcOdd(variableCells.get(i).getAlternateFormatFcOdd());
				bindData.setAlternateFormatFcEven(variableCells.get(i).getAlternateFormatFcEven());
				bindData.setAlternateFormatBcOdd(variableCells.get(i).getAlternateFormatBcOdd());
				bindData.setAlternateFormatBcEven(variableCells.get(i).getAlternateFormatBcEven());
				bindData.setGroupProperty(variableCells.get(i).getGroupProperty());
				bindData.setIsConditions(variableCells.get(i).getIsConditions());
				bindData.setCellConditions(variableCells.get(i).getCellConditions());
				bindData.setIsDataVerification(variableCells.get(i).getIsDataVerification());
				bindData.setDataVerification(variableCells.get(i).getDataVerification());
				bindData.setIsDrill(variableCells.get(i).getIsDrill());
				bindData.setDrillId(variableCells.get(i).getDrillId());
				bindData.setDrillAttrs(variableCells.get(i).getDrillAttrs());
				bindData.setUnitTransfer(variableCells.get(i).getUnitTransfer());
				bindData.setTransferType(variableCells.get(i).getTransferType());
				bindData.setMultiple(variableCells.get(i).getMultiple());
				bindData.setCellConditionType(variableCells.get(i).getFilterType());
				bindData.setIsSubtotal(variableCells.get(i).getIsSubtotal());
				bindData.setSubtotalCells(variableCells.get(i).getSubtotalCells());
				bindData.setIsSubtotalCalc(variableCells.get(i).getIsSubtotalCalc());
				bindData.setSubtotalCalc(variableCells.get(i).getSubtotalCalc());
				bindData.setSheetIndex(sheetIndex);
				bindData.setIsChartAttr(variableCells.get(i).getIsChartAttr());
				bindData.setCellFillType(variableCells.get(i).getCellFillType());
				if(variableCells.get(i).getIsSubtotal()) {
					if(StringUtil.isNotEmpty(variableCells.get(i).getSubtotalCells())) {
						JSONArray subtotalCells = JSON.parseArray(variableCells.get(i).getSubtotalCells());
						if(ListUtil.isNotEmpty(subtotalCells)) {
							for (int j = 0; j < subtotalCells.size(); j++) {
								String coords = subtotalCells.getJSONObject(j).getString("coords");
								int[] cellCoor = SheetUtil.convertFromExcelCoordinate(coords);
								int r = cellCoor[0] - 1;
								int c = cellCoor[1] - 1;
								String subTotalKey = bindData.getSheetId() + "_" + r + "_" + c; 
								subTotalDigits.put(subTotalKey, subtotalCells.getJSONObject(j).getInteger("digit"));
							}
						}
					}
				}
				try {
					if(variableCells.get(i).getCellData() != null)
					{
						bindData.setCellData(objectMapper.readValue(variableCells.get(i).getCellData(), Map.class));
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new BizException(StatusCode.FAILURE, "单元格数据解析失败，请检查单元格数据格式是否正确！");
				}
				if(YesNoEnum.YES.getCode().intValue() == variableCells.get(i).getIsLink().intValue())
				{
					try {
						bindData.setLinkConfig(objectMapper.readValue(variableCells.get(i).getLinkConfig(), Map.class));
					} catch (Exception e) {
						e.printStackTrace();
						throw new BizException(StatusCode.FAILURE, "单元格超链接数据解析失败，请检查单元格超链接数据格式是否正确！");
					}
				}
				if(i == 0)
				{
					bindData.setLastAggregateType(variableCells.get(i).getAggregateType());
					List<List<Map<String, Object>>> datas = new ArrayList<List<Map<String,Object>>>();
					datas.add(data);
					bindData.setDatas(datas);
					bindData.setIsFirst(YesNoEnum.YES.getCode());
				}else {
					bindData.setAggregateType(variableCells.get(i).getAggregateType());
					bindData.setLastAggregateType(lastAggregateType);
					if(DataFromEnum.DEFAULT.getCode().intValue() == bindData.getDataFrom().intValue())
					{//默认，使用上一个单元格计算完后的数据
						bindData.setLastIsConditions(lastIsConditions);
						bindData.setDatas(lastData);
						bindData.setFilterDatas(lastFilterData);
					}else if(DataFromEnum.ORIGINAL.getCode().intValue() == bindData.getDataFrom().intValue())
					{//使用原始数据
						List<List<Map<String, Object>>> datas = new ArrayList<List<Map<String,Object>>>();
						datas.add(data);
						bindData.setDatas(datas);
						bindData.setLastAggregateType(bindData.getAggregateType());
					}else if(DataFromEnum.CELL.getCode().intValue() == bindData.getDataFrom().intValue())
					{//数据来源于单元格，则使用对应单元格的数据
						if(StringUtil.isNullOrEmpty(bindData.getGroupSummaryDependencyR()) || StringUtil.isNullOrEmpty(bindData.getGroupSummaryDependencyC()))
						{
							throw new BizException(StatusCode.FAILURE, "单元格"+SheetUtil.excelColIndexToStr(bindData.getCoordsy()+1)+(bindData.getCoordsx()+1)+"数据来源于单元格，依赖的单元格行号和列号必须同时填写！");
						}
						int r = Integer.valueOf(bindData.getGroupSummaryDependencyR()) - 1;
						int c = SheetUtil.excelColStrToNum(bindData.getGroupSummaryDependencyC())-1;
						LuckySheetBindData luckySheetBindData = cellBindData.get(sheetIndex+"-"+r+"-"+c);
						if(luckySheetBindData != null)
						{
							bindData.setLastAggregateType(luckySheetBindData.getAggregateType());
							bindData.setLastIsConditions(lastIsConditions);
							bindData.setLastIsGroupMerge(luckySheetBindData.getIsGroupMerge());
							bindData.setLastCellExtend(luckySheetBindData.getCellExtend());
							bindData.setDatas(luckySheetBindData.getDatas());
							bindData.setFilterDatas(luckySheetBindData.getFilterDatas());
						}else {
							bindData.setDatas(lastData);
							bindData.setFilterDatas(lastData);
						}
					}
					bindData.setIsFirst(YesNoEnum.NO.getCode());
				}
				if(CellExtendEnum.CROSS.getCode().intValue() == variableCells.get(i).getCellExtend().intValue())
				{
					bindData.setOriginalData(data);;
					bindData = aggregates.get(AggregateTypeEnum.CROSS.getCode()).aggregate(variableCells.get(i),bindData,cellBindData);
				}else {
					bindData = aggregates.get(variableCells.get(i).getAggregateType()).aggregate(variableCells.get(i),bindData,cellBindData);
				}
				cellBindData.put(sheetIndex+"-"+bindData.getCoordsx() + "-" + bindData.getCoordsy(), bindData);
				if(variableCells.get(i).getCellValueType().intValue() == 3)
				{//循环块
  					Map<String, List<List<Map<String, Object>>>> blockDatas = null;
  					LuckySheetBindData sheetBindData = blockBindDatas.get(key);
					if(sheetBindData != null)
					{
						blockDatas = sheetBindData.getBlockDatas();
					}else {
						sheetBindData = bindData;
						blockDatas = new HashMap<>();
					}
   					blockDatas.put(datasetName, bindData.getDatas());
     				sheetBindData.setBlockDatas(blockDatas);
     				if(blockBindDatas.containsKey(key))
    				{
    					continue;
    				}else {
    					blockBindDatas.put(key, bindData);
    				}
				}
				
				if(bindData.getIsSubtotalCalc())
				{//计算分组计算连的分组数量
					if(StringUtil.isNotEmpty(bindData.getSubtotalCalc()))
					{
						JSONArray subtotalCalc = JSON.parseArray(bindData.getSubtotalCalc());
						if(ListUtil.isNotEmpty(subtotalCalc))
						{
							Map<Integer, Integer> groupSubtotalCount = new HashMap<Integer, Integer>();
							List<List<Map<String, Object>>> datas = null;
							for (int t = 0; t < bindData.getDatas().size(); t++) {
								int subtotalCount = 0;
								List<Map<String, Object>> groupDatas = bindData.getDatas().get(t);
								for (int j = 0; j < subtotalCalc.size(); j++) {
									datas = new ArrayList<List<Map<String,Object>>>();
									datas.add(groupDatas);
									JSONObject calc = subtotalCalc.getJSONObject(j);
									JSONArray attrs = calc.getJSONArray("attrs");
									if(ListUtil.isNotEmpty(attrs)) {
 										for (int k = 0; k < attrs.size(); k++) {
  											datas = processSubtotalCalc(attrs.getString(k),datas);
										}
									}
									for (int k = 0; k < datas.size(); k++) {
										if(datas.get(k).size() > 1)
										{
											subtotalCount = subtotalCount + 1;
										}
									}
								}
								groupSubtotalCount.put(t, subtotalCount);
							}
							bindData.setGroupSubtotalCount(groupSubtotalCount);
						}
					}
				}
				if(bindData.getIsSubtotal())
				{
					subTotalCellCoords.add(0,bindData.getCoordsx()+"-"+bindData.getCoordsy());
					if(StringUtil.isNotEmpty(bindData.getSubtotalCells()))
					{
						JSONArray subtotalCells = JSON.parseArray(bindData.getSubtotalCells());
						JSONArray subtotalAttrs = JSON.parseArray(variableCells.get(i).getSubtotalAttrs());
						JSONObject subtotalAttr = ListUtil.isEmpty(subtotalAttrs)?null:subtotalAttrs.getJSONObject(0);
						bindData.setSubtotalAttrs(subtotalAttr);
						if(ListUtil.isNotEmpty(subtotalCells) && ListUtil.isNotEmpty(bindData.getDatas()))
						{
							int index = 0;
							for (int t = 0; t < bindData.getDatas().size(); t++) {
								index = index + bindData.getDatas().get(t).size();
								if(bindData.getDatas().get(t).size() <= 1)
								{
									continue;
								}
								for (int j = 0; j < subtotalCells.size(); j++) {
									JSONObject subtotalCell = subtotalCells.getJSONObject(j);
									String coords = subtotalCell.getString("coords").toUpperCase();
									String type = subtotalCell.getString("type");
									int c = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(coords)) - 1;
									int r = SheetUtil.getRowNum(coords) - 1;
									String dataKey = variableCells.get(i).getSheetId()+"-"+(index-1)+"-"+r+"-"+c+"-"+bindData.getCoordsx()+"-"+bindData.getCoordsy();
									String cellTypeKey = "cellType-"+dataKey;
									JSONObject cellType = new JSONObject();
									if(subtotalAttr != null)
									{
										cellType.putAll(subtotalAttr);
									}
									if(subtotalCellDatas.containsKey(dataKey))
									{
										subtotalCellDatas.put("next-"+dataKey, bindData.getDatas().get(t));
										cellType.put("type", type);
										subtotalCellDatas.put("next-"+cellTypeKey, cellType);
									}else {
										subtotalCellDatas.put(dataKey, bindData.getDatas().get(t));
										cellType.put("type", type);
										subtotalCellDatas.put(cellTypeKey, cellType);
									}
									//记录需要小计的原始单元格
									subtotalCellMap.put(variableCells.get(i).getSheetId()+"-" + r+"-"+c, "1");
								}
							}
						}
					}
				}
				bindDatas.add(bindData);
 				lastAggregateType = bindData.getAggregateType();
 				lastIsConditions = bindData.getIsConditions();
				lastData = bindData.getDatas();
				lastFilterData = bindData.getFilterDatas();
				Map<String, List<List<Map<String, Object>>>> processedCellData = processedCells.get(variableCells.get(i).getSheetId() + "-" + bindData.getCoordsx() +"-" + bindData.getCoordsy());
				if(processedCellData != null)
				{
					processedCellData.put(datasetName, bindData.getDatas());
					bindData.setMultiDatas(processedCellData);
				}else {
					Map<String, List<List<Map<String, Object>>>> map = new HashMap<>();
					map.put(datasetName, bindData.getDatas());
					processedCells.put(variableCells.get(i).getSheetId() + "-" + bindData.getCoordsx() +"-" + bindData.getCoordsy(), map);
					bindData.setMultiDatas(map);
				}
			}
		}
		
		return bindDatas;
	}
	@Override
	public List<LuckySheetFormsBindData> processForms(List<LuckysheetReportFormsCell> variableCells,
			List<Map<String, Object>> data,String datasetName,Map<String, Map<String, List<List<Map<String, Object>>>>> processedCells,String sheetIndex) {
		List<LuckySheetFormsBindData> bindDatas = new ArrayList<LuckySheetFormsBindData>();
		if(!ListUtil.isEmpty(data))
		{
			Map<String, LuckySheetFormsBindData> cellBindData = new HashMap<String, LuckySheetFormsBindData>();//每个单元格绑定的数据
			LuckySheetFormsBindData bindData = null;
			String lastAggregateType = "";//上组数据聚合方式
			int lastIsConditions = 2; 
			List<List<Map<String, Object>>> lastData = null;
			List<List<Map<String, Object>>> lastFilterData = null;
			ObjectMapper objectMapper = new ObjectMapper();
			for (int i = 0; i < variableCells.size(); i++) {
				bindData = new LuckySheetFormsBindData();
				if(StringUtil.isNotEmpty(variableCells.get(i).getCellAttrs()))
				{
					try {
						bindData.setCellAttrs(objectMapper.readValue(variableCells.get(i).getCellAttrs(), JSONObject.class));
					} catch (Exception e) {
						e.printStackTrace();
						throw new BizException(StatusCode.FAILURE, "单元格属性解析失败，请检查单元格数据格式是否正确！");
					}
				}
				JSONObject cellAttrs = JSONObject.parseObject(variableCells.get(i).getCellAttrs());
				bindData.setReportCellId(variableCells.get(i).getId());
				bindData.setCoordsx(variableCells.get(i).getCoordsx());
				bindData.setCoordsy(variableCells.get(i).getCoordsy());
				bindData.setCellExtend(variableCells.get(i).getCellExtend());
				bindData.setAggregateType(StringUtil.isNotEmpty(cellAttrs.getString("aggregateType"))?cellAttrs.getString("aggregateType"):"list");
				bindData.setDigit(cellAttrs.getInteger("digit")==null?0:cellAttrs.getInteger("digit"));
				bindData.setCellValueType(CellValueTypeEnum.VARIABLE.getCode());
				bindData.setCellFunction(cellAttrs.getInteger("cellFunction")==null?1:cellAttrs.getInteger("cellFunction"));
				bindData.setDataFrom(cellAttrs.getInteger("dataFrom")==null?1:cellAttrs.getInteger("dataFrom"));
				bindData.setDatasetName(variableCells.get(i).getDatasetName());
				String dependencyCell = cellAttrs.getString("dependencyCell");
				if(StringUtil.isNotEmpty(dependencyCell))
				{
					String r = StringUtil.getNumeric(dependencyCell);
					if(StringUtil.isNotEmpty(r))
					{
						String c = dependencyCell.replace(r, "");
						bindData.setGroupSummaryDependencyR(r);
						bindData.setGroupSummaryDependencyC(c);
					}
				}
				bindData.setIsFunction(variableCells.get(i).getIsFunction());
				bindData.setRowSpan(variableCells.get(i).getRowSpan());
				bindData.setColSpan(variableCells.get(i).getColSpan());
				bindData.setIsMerge(variableCells.get(i).getIsMerge());
				boolean warning = cellAttrs.getBooleanValue("warning");
				bindData.setWarning(warning);
				String warningRules = cellAttrs.getString("warningRules");
				if(StringUtil.isNullOrEmpty(warningRules))
				{
					warningRules = ">=";
				}
				bindData.setWarningRules(warningRules);
				String threshold = cellAttrs.getString("threshold");
				if(StringUtil.isNullOrEmpty(threshold))
				{
					threshold = "80";
				}
				bindData.setThreshold(threshold);
				String warningColor = cellAttrs.getString("warningColor");
				if(StringUtil.isNullOrEmpty(warningColor))
				{
					warningColor = "#FF0000";
				}
				bindData.setWarningColor(warningColor);
				String warningContent = cellAttrs.getString("warningContent");
				bindData.setWarningContent(warningContent);
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
				bindData.setCellConditionType(StringUtil.isNullOrEmpty(cellAttrs.getString("filterType"))?"and":cellAttrs.getString("filterType"));
				bindData.setSheetIndex(sheetIndex);
				try {
					bindData.setCellData(objectMapper.readValue(variableCells.get(i).getCellData(), Map.class));
				} catch (Exception e) {
					e.printStackTrace();
					throw new BizException(StatusCode.FAILURE, "单元格数据解析失败，请检查单元格数据格式是否正确！");
				}
				if(i == 0)
				{
					bindData.setLastAggregateType(bindData.getAggregateType());
					List<List<Map<String, Object>>> datas = new ArrayList<List<Map<String,Object>>>();
					datas.add(data);
					bindData.setDatas(datas);
					bindData.setIsFirst(YesNoEnum.YES.getCode());
				}else {
					bindData.setLastAggregateType(lastAggregateType);
					if(DataFromEnum.DEFAULT.getCode().intValue() == bindData.getDataFrom().intValue())
					{//默认，使用上一个单元格计算完后的数据
						bindData.setLastIsConditions(lastIsConditions);
						bindData.setDatas(lastData);
						bindData.setFilterDatas(lastFilterData);
					}else if(DataFromEnum.ORIGINAL.getCode().intValue() == bindData.getDataFrom().intValue())
					{//使用原始数据
						List<List<Map<String, Object>>> datas = new ArrayList<List<Map<String,Object>>>();
						datas.add(data);
						bindData.setDatas(datas);
						bindData.setLastAggregateType(bindData.getAggregateType());
					}else if(DataFromEnum.CELL.getCode().intValue() == bindData.getDataFrom().intValue())
					{//数据来源于单元格，则使用对应单元格的数据
						if(StringUtil.isNullOrEmpty(bindData.getGroupSummaryDependencyR()) || StringUtil.isNullOrEmpty(bindData.getGroupSummaryDependencyC()))
						{
							throw new BizException(StatusCode.FAILURE, "单元格"+SheetUtil.excelColIndexToStr(bindData.getCoordsy()+1)+(bindData.getCoordsx()+1)+"数据来源于单元格，依赖的单元格行号和列号必须同时填写！");
						}
						int r = Integer.valueOf(bindData.getGroupSummaryDependencyR()) - 1;
						int c = SheetUtil.excelColStrToNum(bindData.getGroupSummaryDependencyC())-1;
						LuckySheetFormsBindData luckySheetBindData = cellBindData.get(r+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+c);
						if(luckySheetBindData != null)
						{
							bindData.setLastAggregateType(luckySheetBindData.getAggregateType());
							bindData.setLastIsConditions(lastIsConditions);
							bindData.setLastCellExtend(luckySheetBindData.getCellExtend());
							bindData.setDatas(luckySheetBindData.getDatas());
							bindData.setFilterDatas(luckySheetBindData.getFilterDatas());
						}else {
							bindData.setDatas(lastData);
							bindData.setFilterDatas(lastData);
						}
					}
					bindData.setIsFirst(YesNoEnum.NO.getCode());
				}
				if(CellExtendEnum.CROSS.getCode().intValue() == variableCells.get(i).getCellExtend().intValue())
				{
					//TODO 待确认交叉扩展是否可以做
//					bindData = formsAggregates.get(AggregateTypeEnum.CROSS.getCode()).aggregate(variableCells.get(i),bindData);
				}else {
					bindData = formsAggregates.get(bindData.getAggregateType()).aggregate(variableCells.get(i),bindData,null);
				}
				cellBindData.put(bindData.getCoordsx() + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() + bindData.getCoordsy(), bindData);
				bindDatas.add(bindData);
				lastAggregateType = bindData.getAggregateType();
				lastIsConditions = bindData.getIsConditions();
				lastData = bindData.getDatas();
				lastFilterData = bindData.getFilterDatas();
				Map<String, List<List<Map<String, Object>>>> processedCellData = processedCells.get(bindData.getCoordsx() +"_" + bindData.getCoordsy());
				if(processedCellData != null)
				{
					processedCellData.put(datasetName, bindData.getDatas());
					bindData.setMultiDatas(processedCellData);
				}else {
					Map<String, List<List<Map<String, Object>>>> map = new HashMap<>();
					map.put(datasetName, bindData.getDatas());
					processedCells.put(bindData.getCoordsx() +"_" + bindData.getCoordsy(), map);
					bindData.setMultiDatas(map);
				}
			}
		}
		
		return bindDatas;
	}
	
	/**  
	 * @MethodName: processSubtotalCalc
	 * @Description: 处理分组小计链
	 * @author caiyang
	 * @param attr
	 * @param datas
	 * @return List<List<Map<String,Object>>>
	 * @date 2024-01-25 04:08:38 
	 */ 
	private List<List<Map<String, Object>>> processSubtotalCalc(String attr,List<List<Map<String, Object>>> datas)
	{
		List<List<Map<String, Object>>> groupDatas = new ArrayList<List<Map<String,Object>>>();
		for (int i = 0; i < datas.size(); i++) {
			Map<String, List<Map<String, Object>>> dataMap = new LinkedHashMap<String, List<Map<String, Object>>>();
			for (int j = 0; j < datas.get(i).size(); j++) {
				Map<String, Object> map = datas.get(i).get(j);
				List<Map<String, Object>> rowList=null;
				String value = String.valueOf(map.get(attr));
				if (dataMap.containsKey(value)) {
					rowList = dataMap.get(value);
				}else {
					rowList = new ArrayList<Map<String,Object>>();
					dataMap.put(value, rowList);
				}
				rowList.add(map);
			}
			Set<String> keySet = dataMap.keySet();
			if(keySet.size() == 1)
			{
				if(keySet.toArray()[0].equals("null"))
				{
					List<Map<String, Object>> cellData = null;
					Iterator<Entry<String, List<Map<String, Object>>>> entries = dataMap.entrySet().iterator();
					while(entries.hasNext()){
						List<Map<String, Object>> list = entries.next().getValue();
						for (int j = 0; j < list.size(); j++) {
							cellData = new ArrayList<Map<String,Object>>();
							cellData.add(list.get(j));
							groupDatas.add(cellData);
						}
					}
				}else {
					Iterator<Entry<String, List<Map<String, Object>>>> entries = dataMap.entrySet().iterator();
					while(entries.hasNext()){
						groupDatas.add(entries.next().getValue());
					}
				}
			}else {
				Iterator<Entry<String, List<Map<String, Object>>>> entries = dataMap.entrySet().iterator();
				while(entries.hasNext()){
					groupDatas.add(entries.next().getValue());
				}
			}
		}
		return groupDatas;
	}

}
