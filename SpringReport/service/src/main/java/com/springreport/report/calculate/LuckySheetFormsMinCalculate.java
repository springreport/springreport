package com.springreport.report.calculate;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.springreport.base.LuckySheetBindData;
import com.springreport.dto.reporttpl.LuckySheetFormsBindData;
import com.springreport.util.CheckUtil;

/**
* <p>Title: MaxCalculate</p>
* <p>Description: 求最小值</p>
* @author caiyang
* @date 2020年6月7日09:43:17
*/
public class LuckySheetFormsMinCalculate extends Calculate<LuckySheetFormsBindData>{

	@Override
	public String calculate(LuckySheetFormsBindData bindData) {
		BigDecimal result = new BigDecimal(0);
		String property = bindData.getProperty();
		for (int i = 0; i < bindData.getDatas().size(); i++) {
			for (int j = 0; j < bindData.getDatas().get(i).size(); j++) {
				Object object = bindData.getDatas().get(i).get(j).get(property);
				if(CheckUtil.isNumber(String.valueOf(object)))
				{
					BigDecimal number = new BigDecimal(String.valueOf(object));
					if (number.compareTo(result)<0) {
						result = number;
					}
				}
			}
		}
		result = result.setScale(bindData.getDigit(), RoundingMode.HALF_UP); 
		return String.valueOf(result);
	}

}
