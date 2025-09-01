package com.springreport.report.calculate;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.springreport.base.LuckySheetBindData;
import com.springreport.dto.reporttpl.LuckySheetFormsBindData;
import com.springreport.util.CheckUtil;

/**
* <p>Title: AddCalculate</p>
* <p>Description: 加法计算</p>
* @author caiyang
* @date 2020年5月19日
*/
public class LuckySheetFormsAddCalculate extends Calculate<LuckySheetFormsBindData>{

	@Override
	public String calculate(LuckySheetFormsBindData bindData) {
		BigDecimal result = new BigDecimal(0);
		String property = bindData.getProperty();
		for (int i = 0; i < bindData.getDatas().size(); i++) {
			for (int j = 0; j < bindData.getDatas().get(i).size(); j++) {
				Object object = bindData.getDatas().get(i).get(j).get(property);
				if(CheckUtil.isNumber(String.valueOf(object)))
				{
					result = result.add(new BigDecimal(String.valueOf(object)));
				}
			}
		}
		result = result.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
		return String.valueOf(result);
	}

}
