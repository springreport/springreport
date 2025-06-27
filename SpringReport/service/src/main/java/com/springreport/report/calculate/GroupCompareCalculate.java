package com.springreport.report.calculate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Options;
import com.springreport.dto.reporttpl.GroupSummaryData;
import com.springreport.util.CheckUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.StringUtil;

/**  
 * @ClassName: GroupAddCalculate
 * @Description: 分组加法计算
 * @author caiyang
 * @date 2022-02-19 10:19:36 
*/  
public class GroupCompareCalculate extends Calculate<GroupSummaryData>{

	@Override
	public String calculate(GroupSummaryData bindData) {
		BigDecimal result = new BigDecimal(0);
		BigDecimal result1 = new BigDecimal(0);
		BigDecimal result2 = new BigDecimal(0);
		for (int i = 0; i < bindData.getDatas().size(); i++) {
			String compareAttr1 = bindData.getCompareAttr1();
			String compareAttr2 = bindData.getCompareAttr2();
			Object object1 = this.getData(compareAttr1, bindData.getDatas().get(i));
			Object object2 = this.getData(compareAttr2, bindData.getDatas().get(i));
			result1 = result1.add(new BigDecimal(String.valueOf(object1)));
			result2 = result2.add(new BigDecimal(String.valueOf(object2)));
		}
		result = result1.subtract(result2);
		result = result.setScale(bindData.getDigit()==null?2:bindData.getDigit(), RoundingMode.HALF_UP);
		return String.valueOf(result);
	}
	
	private Object getData(String attr,Map<String, Object> data) {
		Object object = 0;
		if(StringUtil.isNullOrEmpty(attr)) {
			return object;
		}
		object = data.get(attr);
		if(object == null) {
			object = 0;
		}else if(!CheckUtil.isNumber(String.valueOf(object))) {
			object = 0;
		}
		return object;
	}

}
