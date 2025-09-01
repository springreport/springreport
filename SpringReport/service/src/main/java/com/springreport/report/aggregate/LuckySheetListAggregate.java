package com.springreport.report.aggregate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.springreport.base.LuckySheetBindData;
import com.springreport.entity.luckysheetreportcell.LuckysheetReportCell;
import com.springreport.enums.AggregateTypeEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.report.CellUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.LuckysheetUtil;
import com.springreport.util.StringUtil;

/**  
 * @ClassName: ListAggregate
 * @Description: 列表聚合处理
 * @author caiyang
 * @date 2021-05-27 04:55:07 
*/  
public class LuckySheetListAggregate extends Aggregate<LuckysheetReportCell,LuckySheetBindData,Map<String, LuckySheetBindData>,Map<String, String>,Map<String, Integer>>{

	@Override
	public LuckySheetBindData aggregate(LuckysheetReportCell reportCell,LuckySheetBindData bindData,Map<String, LuckySheetBindData> cellBinddata,Map<String, String> reliedGroupMergeCells,Map<String, Integer> indexChains) {
		String property = reportCell.getCellValue();
		if(StringUtil.isNotEmpty(property))
		{
			String[] datasetNames = LuckysheetUtil.getDatasetNames(reportCell.getDatasetName());
			if(datasetNames.length > 1)
			{
				property = property.replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
			}else {
				String cellText =  property.replaceAll(datasetNames[0]+".", "");
				bindData.setCellText(cellText);
				property = property.replaceAll(datasetNames[0]+".", "").replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
			}
		}
		bindData.setProperty(property);
		bindData.setDatasetName(reportCell.getDatasetName());
		List<List<Map<String, Object>>> datas = new ArrayList<List<Map<String,Object>>>();
		List<List<Map<String, Object>>> filterDatas = new ArrayList<List<Map<String,Object>>>();
		JSONArray filters = JSONArray.parseArray(bindData.getCellConditions());
		CellUtil.processCellFilters(filters, bindData, cellBinddata, bindData.getSheetIndex());
		List<Map<String, Object>> cellData = null;
		List<Map<String, Object>> filterCellData = null;
		List<List<Map<String, Object>>> bindDatas = null;
		if(bindData.getLastIsConditions().intValue() == YesNoEnum.YES.getCode().intValue()) {
			bindDatas = bindData.getFilterDatas();
		}else {
			bindDatas = bindData.getDatas();
		}
		if(!ListUtil.isEmpty(bindDatas)) {
			Map<Integer, Integer> firstIndexaChain = new HashMap<>();
			firstIndexaChain = new HashMap<Integer, Integer>();
			for (int i = 0; i < bindDatas.size(); i++) {
				for (int j = 0; j < bindDatas.get(i).size(); j++) {
					cellData = new ArrayList<Map<String,Object>>();
					filterCellData =  new ArrayList<Map<String,Object>>();
					cellData.add(bindDatas.get(i).get(j));
					datas.add(cellData);
					if(AggregateTypeEnum.GROUP.getCode().equals(bindData.getLastAggregateType()) || AggregateTypeEnum.GROUPSUMMARY.getCode().equals(bindData.getLastAggregateType()))
					{
						firstIndexaChain.put(datas.size()-1, i);
					}
					if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
					{
						boolean filterResult = ListUtil.filterDatas(filters, bindDatas.get(i).get(j),bindData.getCellConditionType());
						if(filterResult)
						{
							filterCellData.add(bindDatas.get(i).get(j));
							filterDatas.add(filterCellData);
						}
					}else {
						filterCellData.add(bindDatas.get(i).get(j));
						filterDatas.add(filterCellData);
					}
				}
			}
			if(bindData.getLastIsConditions().intValue() == YesNoEnum.YES.getCode().intValue()) {
				bindData.setIsConditions(YesNoEnum.YES.getCode());
			}
			bindData.setDatas(datas);
			if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
			{
				bindData.setFilterDatas(filterDatas);
			}
			for (Integer key : firstIndexaChain.keySet()) {
				String chainKey = bindData.getCoordsx()+"_"+bindData.getCoordsy()+"_"+key;
				indexChains.put(chainKey, firstIndexaChain.get(key));
			}
			String relyKey = bindData.getCoordsx()+"_"+bindData.getCoordsy();
			processRelyGroupMerge(relyKey, reliedGroupMergeCells, cellBinddata, datas, indexChains, bindData);
			bindData.setLastAggregateType(AggregateTypeEnum.LIST.getCode());
		}else {
			bindData.setDatas(datas);
			bindData.setFilterDatas(datas);
			bindData.setLastAggregateType(AggregateTypeEnum.LIST.getCode());
		}
		return bindData;
	}

	private void processRelyGroupMerge(String relyKey,Map<String, String> reliedGroupMergeCells,Map<String, LuckySheetBindData> cellBinddata,
			List<List<Map<String, Object>>> datas,Map<String, Integer> indexChains,LuckySheetBindData bindData) {
		if(!StringUtil.isEmptyMap(reliedGroupMergeCells) && reliedGroupMergeCells.containsKey(relyKey)) {
			String relied = reliedGroupMergeCells.get(relyKey);//被依赖的单元格
			LuckySheetBindData reliedBindData = cellBinddata.get(relied);
			if(reliedBindData != null) {
				List<Integer> indexArray = new ArrayList<>();
				for (int j = 0; j < datas.size(); j++) {
					String indexKey = relyKey + "_" + j;
					if(indexChains.containsKey(indexKey)) {
						int indexSize = 0;
						if(!indexArray.contains(indexChains.get(indexKey))) {
							reliedBindData.getGroupMergeSize().put(indexChains.get(indexKey), indexSize);
							indexArray.add(indexChains.get(indexKey));
						}else {
							indexSize = reliedBindData.getGroupMergeSize().get(indexChains.get(indexKey));
						}
						reliedBindData.getGroupMergeSize().put(indexChains.get(indexKey), bindData.getGroupMergeSize()!=null&&bindData.getGroupMergeSize().containsKey(j)?indexSize+bindData.getGroupMergeSize().get(j):indexSize+datas.get(j).size());
					}
				}
			}
			if(AggregateTypeEnum.GROUP.getCode().equals(bindData.getLastAggregateType()) || AggregateTypeEnum.GROUPSUMMARY.getCode().equals(bindData.getLastAggregateType()))
			{
				relyKey = reliedBindData.getCoordsx()+"_"+reliedBindData.getCoordsy();
				this.processRelyGroupMerge(relyKey, reliedGroupMergeCells, cellBinddata, reliedBindData.getDatas(), indexChains, reliedBindData);
			}
		}
	}
}
