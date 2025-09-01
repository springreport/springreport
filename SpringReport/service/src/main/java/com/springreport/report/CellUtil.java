package com.springreport.report;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.springreport.base.LuckySheetBindData;
import com.springreport.dto.reporttpl.LuckySheetFormsBindData;
import com.springreport.enums.CellExtendEnum;
import com.springreport.enums.CellValueTypeEnum;
import com.springreport.enums.ConditionTypeEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.util.ListUtil;
import com.springreport.util.SheetUtil;
import com.springreport.util.StringUtil;

/**  
 * @ClassName: CellUtil
 * @Description: 单元格工具类
 * @author caiyang
 * @date 2021-05-27 04:42:57 
*/  
public class CellUtil {

	
	public static Map<String,LuckySheetBindData> luckySheetBindDataCoordinateMap(List<LuckySheetBindData> datas)
	{
		Map<String, LuckySheetBindData> result = new HashMap<String, LuckySheetBindData>();
		if(!ListUtil.isEmpty(datas))
		{
			for (int i = 0; i < datas.size(); i++) {
				result.put(String.valueOf(datas.get(i).getCoordsx()) + "-" + String.valueOf(datas.get(i).getCoordsy()), datas.get(i));
			}
		}
		return result;
	}
	
	public static Map<String,LuckySheetFormsBindData> luckySheetFormsBindDataCoordinateMap(List<LuckySheetFormsBindData> datas)
	{
		Map<String, LuckySheetFormsBindData> result = new HashMap<String, LuckySheetFormsBindData>();
		if(!ListUtil.isEmpty(datas))
		{
			for (int i = 0; i < datas.size(); i++) {
				result.put(String.valueOf(datas.get(i).getCoordsx()) + "-" + String.valueOf(datas.get(i).getCoordsy()), datas.get(i));
			}
		}
		return result;
	}
	
	/**  
	 * @MethodName: processCellFilters
	 * @Description: 处理过滤条件是单元格的，处理单元格数据
	 * @author caiyang
	 * @param filters
	 * @param bindData
	 * @param cellBinddata void
	 * @date 2024-02-08 04:34:09 
	 */ 
	public static void processCellFilters(JSONArray filters,LuckySheetBindData bindData,Map<String, LuckySheetBindData> cellBindData,String sheetIndex) {
		if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
		{
			if(ListUtil.isNotEmpty(filters))
			{
				for (int i = 0; i < filters.size(); i++) {
					String type = filters.getJSONObject(i).getString("type");
					String condition = filters.getJSONObject(i).getString("value");
					if(ConditionTypeEnum.CELL.getCode().equals(type)) {
						if(StringUtil.isNotEmpty(condition)) {
							try {
								int c = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(condition)) - 1;
								int r = SheetUtil.getRowNum(condition) - 1;
								LuckySheetBindData luckySheetBindData = cellBindData.get(sheetIndex+"-"+r+"-"+c);
								if(luckySheetBindData != null)
								{
									if(CellValueTypeEnum.FIXED.getCode().intValue() == luckySheetBindData.getCellValueType()) {
										//固定值
										Object value = luckySheetBindData.getCellValue();
										filters.getJSONObject(i).put("value", value);
									}else {
										//动态值
										List<List<Map<String, Object>>> datas = null;
										if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
										{
											datas = luckySheetBindData.getFilterDatas();
										}else {
											datas = luckySheetBindData.getDatas();
										}
										if(CellExtendEnum.NOEXTEND.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()) {
											//不扩展
											if(ListUtil.isNotEmpty(datas))
											{
												Object value = datas.get(0).get(0).get(luckySheetBindData.getProperty());
												if(value == null)
												{
													value = "";
												}
												filters.getJSONObject(i).put("value", value);
											}
										}else if(CellExtendEnum.VERTICAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()
												|| CellExtendEnum.HORIZONTAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue())
										{
											Set values = new HashSet<>();
											if(ListUtil.isNotEmpty(datas))
											{
												for (int j = 0; j < datas.size(); j++) {
													for (int j2 = 0; j2 < datas.get(j).size(); j2++) {
														Object value = datas.get(j).get(j2).get(luckySheetBindData.getProperty());
														if(value != null)
														{
															values.add(String.valueOf(value));
														}
													}
												}
											}
											if(ListUtil.isNotEmpty(values))
											{
												String str = String.join(",", values);
												filters.getJSONObject(i).put("value", str);
											}
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	
	public static void processCellFilters(JSONArray filters,LuckySheetFormsBindData bindData,Map<String, LuckySheetFormsBindData> cellBindData,String sheetIndex) {
		if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
		{
			if(ListUtil.isNotEmpty(filters))
			{
				for (int i = 0; i < filters.size(); i++) {
					String type = filters.getJSONObject(i).getString("type");
					String condition = filters.getJSONObject(i).getString("value");
					if(ConditionTypeEnum.CELL.getCode().equals(type)) {
						if(StringUtil.isNotEmpty(condition)) {
							try {
								int c = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(condition)) - 1;
								int r = SheetUtil.getRowNum(condition) - 1;
								LuckySheetFormsBindData luckySheetBindData = cellBindData.get(sheetIndex+"-"+r+"-"+c);
								if(luckySheetBindData != null)
								{
									if(CellValueTypeEnum.FIXED.getCode().intValue() == luckySheetBindData.getCellValueType()) {
										//固定值
										Object value = luckySheetBindData.getCellValue();
										filters.getJSONObject(i).put("value", value);
									}else {
										//动态值
										List<List<Map<String, Object>>> datas = null;
										if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
										{
											datas = luckySheetBindData.getFilterDatas();
										}else {
											datas = luckySheetBindData.getDatas();
										}
										if(CellExtendEnum.NOEXTEND.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()) {
											//不扩展
											if(ListUtil.isNotEmpty(datas))
											{
												Object value = datas.get(0).get(0).get(luckySheetBindData.getProperty());
												if(value == null)
												{
													value = "";
												}
												filters.getJSONObject(i).put("value", value);
											}
										}else if(CellExtendEnum.VERTICAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()
												|| CellExtendEnum.HORIZONTAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue())
										{
											Set values = new HashSet<>();
											if(ListUtil.isNotEmpty(datas))
											{
												for (int j = 0; j < datas.size(); j++) {
													for (int j2 = 0; j2 < datas.get(j).size(); j2++) {
														Object value = datas.get(j).get(j2).get(luckySheetBindData.getProperty());
														if(value != null)
														{
															values.add(String.valueOf(value));
														}
													}
												}
											}
											if(ListUtil.isNotEmpty(values))
											{
												String str = String.join(",", values);
												filters.getJSONObject(i).put("value", str);
											}
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}
