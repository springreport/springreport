package com.springreport.dto.reporttpl;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**  
 * @ClassName: GroupSummaryData
 * @Description: 分组汇总计算用实体类
 * @author caiyang
 * @date 2022-02-19 10:18:25 
*/  
@Data
public class GroupSummaryData {

	/**  
	 * @Fields datas : 数据
	 * @author caiyang
	 * @date 2022-02-19 10:19:00 
	 */ 
	private List<Map<String, Object>> datas;
	
	/**  
	 * @Fields digit : 小数位数
	 * @author caiyang
	 * @date 2022-02-19 10:19:15 
	 */ 
	private Integer digit;
	
	/**  
	 * @Fields property : 属性
	 * @author caiyang
	 * @date 2022-02-19 10:21:02 
	 */ 
	private String property;
}
