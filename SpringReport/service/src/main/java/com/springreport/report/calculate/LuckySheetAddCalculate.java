package com.springreport.report.calculate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Options;
import com.springreport.base.LuckySheetBindData;
import com.springreport.util.CheckUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.LuckysheetUtil;
import com.springreport.util.StringUtil;

/**
* <p>Title: AddCalculate</p>
* <p>Description: 加法计算</p>
* @author caiyang
* @date 2020年5月19日
*/
public class LuckySheetAddCalculate extends Calculate<LuckySheetBindData>{

	@Override
	public String calculate(LuckySheetBindData bindData) {
		BigDecimal result = new BigDecimal(0);
		String[] datasetNames = LuckysheetUtil.getDatasetNames(bindData.getDatasetName());
		if(datasetNames.length > 1)
		{
			for (int i = 0; i < datasetNames.length; i++) {
				List<List<Map<String, Object>>> datas = bindData.getMultiDatas().get(datasetNames[i]);
				List<String> properties = ListUtil.getPropertyList(bindData.getProperty(), datas.get(0).get(0), datasetNames[i]);
				for (int j = 0; j < properties.size(); j++) {
					for (int j2 = 0; j2 < bindData.getDatas().size(); j2++) {
						for (int k = 0; k < bindData.getDatas().get(j2).size(); k++) {
							Object object = bindData.getDatas().get(j2).get(k).get(properties.get(j));
							if(CheckUtil.isNumber(String.valueOf(object)))
							{
								result = result.add(new BigDecimal(String.valueOf(object)));
							}
						}
					}
				}
			}
		}else {
			List<String> properties = new ArrayList<String>();
			Map<String, Object> datas = null;
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
						result = result.add(new BigDecimal(String.valueOf(object)));
					}
					if(bindData.getIsDump() && StringUtil.isNotEmpty(bindData.getDumpAttr())) {
						break;
					}
				}
			}
		}
		
		result = result.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
		return String.valueOf(result);
	}

}
