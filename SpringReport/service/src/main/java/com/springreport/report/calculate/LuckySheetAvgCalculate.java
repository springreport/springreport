package com.springreport.report.calculate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Options;
import com.springreport.base.LuckySheetBindData;
//import com.springreport.base.CustomSpringReportFunction;
import com.springreport.base.CustomSpringReportFunction;
import com.springreport.util.CheckUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.StringUtil;

/**
* <p>Title: AvgCalculate</p>
* <p>Description: 平均值计算</p>
* @author caiyang
* @date 2020年5月20日
*/
public class LuckySheetAvgCalculate extends Calculate<LuckySheetBindData>{

	
	private static CustomSpringReportFunction customSpringReportFunction;
	
	static {
		customSpringReportFunction = new CustomSpringReportFunction();
	}
	
	@Override
	public String calculate(LuckySheetBindData bindData) {
		BigDecimal result = new BigDecimal(0);
		BigDecimal sum = new BigDecimal(0);
		int size = 0;
//		String[] datasetNames = bindData.getDatasetName().split(",");
		if(bindData.getMultiDatas().size() > 1)
		{
			List<String> properties = new ArrayList<String>();
			String[] datasets = bindData.getDatasetName().split(",");
			Map<String, Object> datas = null;
			Map<String, String> calculateFormula = new LinkedHashMap<String, String>();
			for (int i = 0; i < datasets.length; i++) {
				List<List<Map<String, Object>>> datasetDatas = bindData.getMultiDatas().get(datasets[i]);
				properties = new ArrayList<String>();
				for (int t = 0; t < datasetDatas.size(); t++) {
					for (int j = 0; j < datasetDatas.get(t).size(); j++) {
						String formulaKey = t+"_"+j;
						String property = bindData.getProperty();
						if(StringUtil.isNullOrEmpty(property)) {
							break;
						}
						datas = ListUtil.getProperties(property, datasetDatas.get(t).get(j),datasets[i]);
						for (String key : datas.keySet()) {
							properties.add(key);
						}
						Set<String> set = datas.keySet();
						if(ListUtil.isNotEmpty(set)) {
							for (String o : set) {
								if(calculateFormula.containsKey(formulaKey)) {
									property = calculateFormula.get(formulaKey);
								}
					        	property = property.replace(o, datas.get(o)==null?"0":StringUtil.isNullOrEmpty(String.valueOf(datas.get(o)))?"0":String.valueOf(datas.get(o)));
					        	calculateFormula.put(formulaKey, property);
							}
						}
						if(i == 0) {
							size = size +1;
						}
					}
				}
			}
			Object object = null;
			if(!StringUtil.isEmptyMap(calculateFormula)) {
				LuckySheetBindData luckySheetBindData = new LuckySheetBindData();
				BeanUtils.copyProperties(bindData, luckySheetBindData);
				for (String key : calculateFormula.keySet()) {
					String formula = calculateFormula.get(key);
					if(customSpringReportFunction.isSpringReportFunction(formula)) {
						Map<String, Object> extraParams = new HashMap<>();
			        	extraParams.put("userInfo", null);
			        	extraParams.put("viewParams", null);
			        	luckySheetBindData.setProperty(formula);
			        	object = customSpringReportFunction.calculate(luckySheetBindData, extraParams);
					}else {
						try {
							AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
		        			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
							object = AviatorEvaluator.execute(formula);
						} catch (Exception e) {
							object = 0;
						}
					}
					
					if(CheckUtil.isNumber(String.valueOf(object)))
					{
						sum = sum.add(new BigDecimal(String.valueOf(object)));
					}
				}
			}
		}else {
			List<String> properties = new ArrayList<String>();
			Map<String, Object> datas = null;
			String property = bindData.getProperty();
			for (int i = 0; i < bindData.getDatas().size(); i++) {
				for (int j = 0; j < bindData.getDatas().get(i).size(); j++) {
					if(ListUtil.isEmpty(properties)) {
						datas = ListUtil.getProperties(bindData.getProperty(), bindData.getDatas().get(i).get(j));
						for (String key : datas.keySet()) {
							properties.add(key);
						}
					}else {
						datas = ListUtil.getProperties(properties, bindData.getDatas().get(i).get(j));
					}
					Set<String> set = datas.keySet();
					String tempProperty = bindData.getProperty();
					for (String o : set) {
						tempProperty = tempProperty.replace(o, datas.get(o)==null?"0":StringUtil.isNullOrEmpty(String.valueOf(datas.get(o)))?"0":String.valueOf(datas.get(o)));
					}
					Object object = null;
					try {
						AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
						object = AviatorEvaluator.execute(tempProperty);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(CheckUtil.isNumber(String.valueOf(object)))
					{
						sum = sum.add(new BigDecimal(String.valueOf(object)));
					}
					size = size + 1;
					if(bindData.getIsDump() && StringUtil.isNotEmpty(bindData.getDumpAttr())) {
						break;
					}
				}
			}
		}
		result = sum.divide(new BigDecimal(size),bindData.getDigit(),BigDecimal.ROUND_HALF_UP);
		return String.valueOf(result);
	}

}
