package com.springreport.report.calculate;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.springreport.base.LuckySheetBindData;
import com.springreport.dto.reporttpl.LuckySheetFormsBindData;

/**
* <p>Title: LuckySheetCountCalculate</p>
* <p>Description: 计数</p>
* @author caiyang
* @date 2020年5月19日
*/
public class LuckySheetFormsCountCalculate extends Calculate<LuckySheetFormsBindData>{

	@Override
	public String calculate(LuckySheetFormsBindData bindData) {
		int size = 0;
		for (int i = 0; i < bindData.getDatas().size(); i++) {
			for (int j = 0; j < bindData.getDatas().get(i).size(); j++) {
				size = size + bindData.getDatas().get(i).size();
			}
		}
		BigDecimal result = new BigDecimal(size);
		result = result.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
		return String.valueOf(result);
	}

}
