package com.springreport.report.calculate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Options;
import com.springreport.dto.reporttpl.LuckySheetBindData;
import com.springreport.util.CheckUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.StringUtil;

/**
* <p>Title: MaxCalculate</p>
* <p>Description: 求最大值</p>
* @author caiyang
* @date 2020年5月20日
*/
public class LuckySheetMaxCalculate extends Calculate<LuckySheetBindData>{

	@Override
	public String calculate(LuckySheetBindData bindData) {
		BigDecimal result = new BigDecimal(0);
		String property = bindData.getProperty();
		for (int i = 0; i < bindData.getDatas().size(); i++) {
			for (int j = 0; j < bindData.getDatas().get(i).size(); j++) {
				Map<String, Object> datas = ListUtil.getProperties(bindData.getProperty(), bindData.getDatas().get(i).get(j));
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
					BigDecimal number = new BigDecimal(String.valueOf(object));
					if (number.compareTo(result)>0) {
						result = number;
					}
				}
			}
		}
		result = result.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
		return String.valueOf(result);
	}

}
