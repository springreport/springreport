package com.springreport.report.calculate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Options;
import com.springreport.dto.reporttpl.GroupSummaryData;
import com.springreport.util.CheckUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.StringUtil;

/**  
 * @ClassName: GroupAvgCalculate
 * @Description: 分组平均数计算
 * @author caiyang
 * @date 2022-02-19 10:19:36 
*/  
public class GroupAvgCalculate extends Calculate<GroupSummaryData>{

	@Override
	public String calculate(GroupSummaryData bindData) {
		BigDecimal result = new BigDecimal(0);
		BigDecimal sum = new BigDecimal(0);
		int size = 0;
		List<String> properties = new ArrayList<String>();
		Map<String, Object> datas = null;
		for (int i = 0; i < bindData.getDatas().size(); i++) {
			String property = bindData.getProperty();
			if(StringUtil.isNullOrEmpty(property)) {
				break;
			}
			Object object = null;
			if(ListUtil.isEmpty(properties)) {
				datas = ListUtil.getProperties(bindData.getProperty(), bindData.getDatas().get(i));
				for (String key : datas.keySet()) {
					properties.add(key);
				}
			}else {
				datas = ListUtil.getProperties(properties, bindData.getDatas().get(i));
			}
			Set<String> set = datas.keySet();
			if(ListUtil.isNotEmpty(set))
			{
				for (String o : set) {
					property = property.replace(o, datas.get(o)==null?"0":StringUtil.isNullOrEmpty(String.valueOf(datas.get(o)))?"0":String.valueOf(datas.get(o)));
		        }
				try {
					AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
        			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
					object = AviatorEvaluator.execute(property);
				} catch (Exception e) {
					object = 0;
				}
			}else {
				object = bindData.getDatas().get(i).get(property);
			}
			if(CheckUtil.isNumber(String.valueOf(object)))
			{
				sum = sum.add(new BigDecimal(String.valueOf(object)));
			}
			size = size + 1;
		}
		result = sum.divide(new BigDecimal(size),bindData.getDigit(),BigDecimal.ROUND_HALF_UP);
		return String.valueOf(result);
	}

}
