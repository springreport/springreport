package com.springreport.report.calculate;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.springreport.dto.reporttpl.LuckySheetBindData;
import com.springreport.util.StringUtil;

/**
* <p>Title: LuckySheetCountCalculate</p>
* <p>Description: 计数</p>
* @author caiyang
* @date 2020年5月19日
*/
public class LuckySheetCountCalculate extends Calculate<LuckySheetBindData>{

	@Override
	public String calculate(LuckySheetBindData bindData) {
		int size = 0;
		for (int i = 0; i < bindData.getDatas().size(); i++) {
			if(bindData.getIsDump() && StringUtil.isNotEmpty(bindData.getDumpAttr())) {
				size = size + 1;
			}else {
				size = size + bindData.getDatas().get(i).size(); 
			}
		}
		BigDecimal result = new BigDecimal(size);
		result = result.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
		return String.valueOf(result);
	}

}
