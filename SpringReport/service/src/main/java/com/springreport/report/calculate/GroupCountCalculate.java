package com.springreport.report.calculate;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.springreport.dto.reporttpl.GroupSummaryData;

/**  
 * @ClassName: GroupAddCalculate
 * @Description: 分组加法计算
 * @author caiyang
 * @date 2022-02-19 10:19:36 
*/  
public class GroupCountCalculate extends Calculate<GroupSummaryData>{

	@Override
	public String calculate(GroupSummaryData bindData) {
		BigDecimal result = new BigDecimal(bindData.getDatas().size());
		result = result.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
		return String.valueOf(result);
	}

}
