package com.springreport.report.aggregate;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.springreport.base.LuckySheetBindData;
import com.springreport.entity.luckysheetreportcell.LuckysheetReportCell;
import com.springreport.enums.AggregateTypeEnum;
import com.springreport.enums.DataFromEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.report.CellUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.LuckysheetUtil;
import com.springreport.util.StringUtil;

/**  
 * @ClassName: GroupAggregate
 * @Description: 分组聚合处理
 * @author caiyang
 * @date 2021-05-27 04:55:07 
*/  
public class LuckySheetGroupSummaryAggregate extends Aggregate<LuckysheetReportCell,LuckySheetBindData,Map<String, LuckySheetBindData>,Map<String, String>,Map<String, Integer>>{

	@Override
	public LuckySheetBindData aggregate(LuckysheetReportCell reportCell,LuckySheetBindData bindData,Map<String, LuckySheetBindData> cellBinddata,Map<String, String> reliedGroupMergeCells,Map<String, Integer> indexChains) {
		String property = reportCell.getCellValue();
		String[] datasetNames = LuckysheetUtil.getDatasetNames(reportCell.getDatasetName());
		if(datasetNames.length > 1)
		{
			property = property.replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
		}else {
			String cellText =  property.replaceAll(datasetNames[0]+".", "");
			bindData.setCellText(cellText);
			property = property.replaceAll(datasetNames[0]+".", "").replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
		}
		bindData.setProperty(property);
		String groupProperty = property;
		if(StringUtil.isNotEmpty(bindData.getGroupProperty()))
		{
			groupProperty = bindData.getGroupProperty();
		}
		List<List<Map<String, Object>>> datas = new ArrayList<List<Map<String,Object>>>();
		List<List<Map<String, Object>>> filterDatas = new ArrayList<List<Map<String,Object>>>();
		JSONArray filters = JSONArray.parseArray(bindData.getCellConditions());
		CellUtil.processCellFilters(filters, bindData, cellBinddata, bindData.getSheetIndex());
		List<List<Map<String, Object>>> bindDatas = null;
		if(bindData.getLastIsConditions().intValue() == YesNoEnum.YES.getCode().intValue()) {
			bindDatas = bindData.getFilterDatas();
		}else {
			bindDatas = bindData.getDatas();
		} 
		if(!ListUtil.isEmpty(bindDatas))
		{
			if(AggregateTypeEnum.GROUP.getCode().equals(bindData.getLastAggregateType()) || AggregateTypeEnum.GROUPSUMMARY.getCode().equals(bindData.getLastAggregateType()))
			{
				//上次数据处理是分组聚合
				//本次数据处理是分组聚合，则数据进行分组处理
//				if(bindData.getLastIsGroupMerge()) {
//					bindData.setContinueGroupMergeCount(bindData.getContinueGroupMergeCount() + 1);
//				}else {
//					bindData.setContinueGroupMergeCount(0);
//				}
				String[] properties = groupProperty.split(",");
				Map<Integer, Integer> firstIndexaChain = new HashMap<>();
				for (int t = 0; t < properties.length; t++) {
					datas = new ArrayList<List<Map<String,Object>>>();
					Map<Integer, Integer> tempFirstIndexaChain = JSON.parseObject(JSON.toJSONString(firstIndexaChain),Map.class);
					Map<String, Integer> valueIndex = new HashMap<String, Integer>();
					firstIndexaChain = new HashMap<Integer, Integer>();
					for (int i = 0; i < bindDatas.size(); i++) {
						Map<String, List<Map<String, Object>>> dataMap = new LinkedHashMap<String, List<Map<String, Object>>>();
						Map<String, List<Map<String, Object>>> filterDataMap = new LinkedHashMap<String, List<Map<String, Object>>>();
						for (int j = 0; j < bindDatas.get(i).size(); j++) {
							Map<String, Object> map = bindDatas.get(i).get(j);
							String value = String.valueOf(map.get(properties[t]));
							List<Map<String, Object>> rowList=null;
							if (dataMap.containsKey(value)) {
								rowList = dataMap.get(value);
							}else {
								rowList = new ArrayList<Map<String,Object>>();
								dataMap.put(value, rowList);
								valueIndex.put(value, i);
							}
							rowList.add(map);
							if(t == properties.length - 1)
							{
								List<Map<String, Object>> filterRowList=null;
								if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
								{
									boolean filterResult = ListUtil.filterDatas(filters, map,bindData.getCellConditionType());
									if(filterResult)
									{
										if(filterDataMap.containsKey(value))
										{
											filterRowList = filterDataMap.get(value);
										}else {
											filterRowList = new ArrayList<Map<String,Object>>();
											filterDataMap.put(value, filterRowList);
										}
										filterRowList.add(map);
									}
								}
							}
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
										datas.add(cellData);
									}
								}
							}else {
								Iterator<Entry<String, List<Map<String, Object>>>> entries = dataMap.entrySet().iterator();
								while(entries.hasNext()){
									datas.add(entries.next().getValue());
								}
							}
							
						}else {
//							Iterator<Entry<String, List<Map<String, Object>>>> entries = dataMap.entrySet().iterator();
//							while(entries.hasNext()){
//								datas.add(entries.next().getValue());
//								if(valueIndex.containsKey(entries.next().getKey())) {
//									if(t == 0) {
//										firstIndexaChain.put(datas.size()-1, valueIndex.get(entries.next().getKey()));
//									}else {
//										if(tempFirstIndexaChain.containsKey(valueIndex.get(entries.next().getKey()))) {
//											firstIndexaChain.put(datas.size()-1, tempFirstIndexaChain.get(i));
//										}
//									}
//								}
//							}
							for (String key : dataMap.keySet()) {
								datas.add(dataMap.get(key));
								if(valueIndex.containsKey(key)) {
									if(t == 0) {
										firstIndexaChain.put(datas.size()-1, valueIndex.get(key));
									}else {
										if(tempFirstIndexaChain.containsKey(valueIndex.get(key))) {
											firstIndexaChain.put(datas.size()-1, tempFirstIndexaChain.get(i));
										}
									}
								}
							}
						}
						if(t == properties.length - 1)
						{
							Set<String> keySet2 = dataMap.keySet();
							if(keySet2.size() == 1)
							{
								if(keySet2.toArray()[0].equals("null"))
								{
									List<Map<String, Object>> cellData = null;
									Iterator<Entry<String, List<Map<String, Object>>>> entries = filterDataMap.entrySet().iterator();
									while(entries.hasNext()){
										List<Map<String, Object>> list = entries.next().getValue();
										for (int j = 0; j < list.size(); j++) {
											cellData = new ArrayList<Map<String,Object>>();
											cellData.add(list.get(j));
											filterDatas.add(cellData);
										}
									}
								}else {
									Iterator<Entry<String, List<Map<String, Object>>>> entries = filterDataMap.entrySet().iterator();
									while(entries.hasNext()){
										filterDatas.add(entries.next().getValue());
									}
								}
							}else {
								Iterator<Entry<String, List<Map<String, Object>>>> entries = filterDataMap.entrySet().iterator();
								while(entries.hasNext()){
									filterDatas.add(entries.next().getValue());
								}
							}
						}
					}
					bindDatas = datas;
					bindData.setDatas(datas);
					if(bindData.getIsConditions().intValue() == YesNoEnum.NO.getCode().intValue()) {
						filterDatas = datas;
						bindData.setFilterDatas(datas);
					}
					if(bindData.getLastIsConditions().intValue() == YesNoEnum.YES.getCode().intValue()) {
						bindData.setIsConditions(YesNoEnum.YES.getCode());
					}
					if(t == properties.length - 1)
					{
						if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
						{
							bindData.setFilterDatas(filterDatas);
						}
						
						for (Integer key : firstIndexaChain.keySet()) {
							String chainKey = bindData.getCoordsx()+"_"+bindData.getCoordsy()+"_"+key;
							indexChains.put(chainKey, firstIndexaChain.get(key));
						}
						if(bindData.getIsGroupMerge()) {
							if(ListUtil.isNotEmpty(bindData.getDatas())) {
								Map<Integer, Integer> groupMergeSize = new HashMap<Integer, Integer>();
								for (int j = 0; j < bindData.getDatas().size(); j++) {
									groupMergeSize.put(j, 1);
								}
								bindData.setGroupMergeSize(groupMergeSize);;
							}
						}
//						if(!StringUtil.isEmptyMap(reliedGroupMergeCells) && reliedGroupMergeCells.containsKey(relyKey)) {
//							String relied = reliedGroupMergeCells.get(relyKey);//被依赖的单元格
//							LuckySheetBindData reliedBindData = cellBinddata.get(relied);
//							if(reliedBindData != null) {
//								List<Integer> indexArray = new ArrayList<>();
//								for (int j = 0; j < datas.size(); j++) {
//									String indexKey = relyKey + "_" + j;
//									if(indexChains.containsKey(indexKey)) {
//										int indexSize = 0;
//										if(!indexArray.contains(indexChains.get(indexKey))) {
//											reliedBindData.getGroupMergeSize().put(indexChains.get(indexKey), indexSize);
//											indexArray.add(indexChains.get(indexKey));
//										}else {
//											indexSize = reliedBindData.getGroupMergeSize().get(indexChains.get(indexKey));
//										}
//										if(bindData.getIsGroupMerge()) {
//											reliedBindData.getGroupMergeSize().put(indexChains.get(indexKey), bindData.getGroupMergeSize().containsKey(j)?indexSize+bindData.getGroupMergeSize().get(j):indexSize+1);
//										}else {
//											reliedBindData.getGroupMergeSize().put(indexChains.get(indexKey), bindData.getGroupMergeSize().containsKey(j)?indexSize+bindData.getGroupMergeSize().get(j):indexSize+datas.get(j).size());
//										}
//									}
//								}
//							}
//						}
					}
				}
				bindData.setLastAggregateType(AggregateTypeEnum.GROUP.getCode());
			}else if(AggregateTypeEnum.LIST.getCode().equals(bindData.getLastAggregateType())){
				//上次数据处理是列表聚合，则不进行分组，直接返回全部数据
				datas.addAll(bindDatas);
				if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
				{
					List<Map<String, Object>> filterCellData = null;
					for (int i = 0; i < bindData.getDatas().size(); i++) {
						filterCellData = new ArrayList<Map<String,Object>>();
						boolean filterResult = ListUtil.filterDatas(filters, bindData.getDatas().get(i).get(0),bindData.getCellConditionType());
						if(filterResult)
						{
							filterCellData.add(bindData.getDatas().get(i).get(0));
							filterDatas.add(filterCellData);
						}
					}
					bindData.setFilterDatas(filterDatas);
				}else {
					bindData.setFilterDatas(datas);
				}
				bindData.setDatas(datas);
				if(bindData.getLastIsConditions().intValue() == YesNoEnum.YES.getCode().intValue()) {
					bindData.setIsConditions(YesNoEnum.YES.getCode());
				}
				bindData.setLastAggregateType(AggregateTypeEnum.LIST.getCode());
			}
		}
		return bindData;
	}

}
