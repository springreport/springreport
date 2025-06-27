package com.springreport.report.calculate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import com.springreport.dto.reporttpl.LuckySheetBindData;
import com.springreport.util.CheckUtil;
import com.springreport.util.StringUtil;

/**
* <p>Title: LuckySheetCountCalculate</p>
* <p>Description: 计数</p>
* @author caiyang
* @date 2020年5月19日
*/
public class LuckySheetCompareCalculate extends Calculate<LuckySheetBindData>{

	@Override
	public String calculate(LuckySheetBindData bindData) {
		BigDecimal result = new BigDecimal(0);
		BigDecimal result1 = new BigDecimal(0);
		BigDecimal result2 = new BigDecimal(0);
		for (int i = 0; i < bindData.getDatas().size(); i++) {
			for (int j = 0; j < bindData.getDatas().get(i).size(); j++) {
				String compareAttr1 = bindData.getCompareAttr1();
				String compareAttr2 = bindData.getCompareAttr2();
				Object object1 = this.getData(compareAttr1, bindData.getDatas().get(i).get(j));
				Object object2 = this.getData(compareAttr2, bindData.getDatas().get(i).get(j));
				result1 = result1.add(new BigDecimal(String.valueOf(object1)));
				result2 = result2.add(new BigDecimal(String.valueOf(object2)));
			}
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
