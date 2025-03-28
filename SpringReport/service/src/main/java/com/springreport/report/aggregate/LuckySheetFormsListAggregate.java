package com.springreport.report.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.springreport.dto.reporttpl.LuckySheetFormsBindData;
import com.springreport.entity.luckysheetreportformscell.LuckysheetReportFormsCell;
import com.springreport.enums.AggregateTypeEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.report.CellUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.LuckysheetUtil;

/**  
 * @ClassName: ListAggregate
 * @Description: 列表聚合处理
 * @author caiyang
 * @date 2021-05-27 04:55:07 
*/  
public class LuckySheetFormsListAggregate extends Aggregate<LuckysheetReportFormsCell,LuckySheetFormsBindData,Map<String, LuckySheetFormsBindData>,Map<String, String>,Map<String, Integer>>{

	@Override
	public LuckySheetFormsBindData aggregate(LuckysheetReportFormsCell reportCell,LuckySheetFormsBindData bindData,Map<String, LuckySheetFormsBindData> cellBinddata,Map<String, String> reliedGroupMergeCells,Map<String, Integer> indexChains) {
		String property = reportCell.getCellValue();
		String[] datasetNames = LuckysheetUtil.getDatasetNames(reportCell.getDatasetName());
		if(datasetNames.length > 1)
		{
			property = property.replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
		}else {
			property = property.replaceAll(datasetNames[0]+".", "").replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
		}
		bindData.setProperty(property);
		bindData.setDatasetName(reportCell.getDatasetName());
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
			List<Map<String, Object>> cellData = null;
			List<Map<String, Object>> filterCellData = null;
			for (int i = 0; i < bindDatas.size(); i++) {
				for (int j = 0; j < bindDatas.get(i).size(); j++) {
					cellData = new ArrayList<Map<String,Object>>();
					filterCellData =  new ArrayList<Map<String,Object>>(); 
					cellData.add(bindDatas.get(i).get(j));
					datas.add(cellData);
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
			bindData.setLastAggregateType(AggregateTypeEnum.LIST.getCode());
		}else {
			bindData.setDatas(datas);
			bindData.setFilterDatas(datas);
			bindData.setLastAggregateType(AggregateTypeEnum.LIST.getCode());
		}
		return bindData;
	}

}
