package com.springreport.report.calculate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

import com.googlecode.aviator.AviatorEvaluator;
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
public class GroupAddCalculate extends Calculate<GroupSummaryData>{

	@Override
	public String calculate(GroupSummaryData bindData) {
		BigDecimal result = new BigDecimal(0);
		for (int i = 0; i < bindData.getDatas().size(); i++) {
			String property = bindData.getProperty();
			if(StringUtil.isNullOrEmpty(property)) {
				break;
			}
			Object object = null;
			Map<String, Object> datas = ListUtil.getProperties(bindData.getProperty(), bindData.getDatas().get(i));
			Set<String> set = datas.keySet();
			if(ListUtil.isNotEmpty(set))
			{
				for (String o : set) {
		        	property = property.replace(o, datas.get(o)==null?"0":StringUtil.isNullOrEmpty(String.valueOf(datas.get(o)))?"0":String.valueOf(datas.get(o)));
		        }
				try {
					object = AviatorEvaluator.execute(property);
				} catch (Exception e) {
					object = 0;
				}
				
			}else {
				object = bindData.getDatas().get(i).get(property);
			}
			if(CheckUtil.isNumber(String.valueOf(object)))
			{
				result = result.add(new BigDecimal(String.valueOf(object)));
			}
		}
		result = result.setScale(bindData.getDigit()==null?2:bindData.getDigit(), RoundingMode.HALF_UP);
		return String.valueOf(result);
	}

}
