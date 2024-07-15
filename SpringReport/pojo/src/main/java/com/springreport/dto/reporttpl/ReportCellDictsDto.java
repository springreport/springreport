package com.springreport.dto.reporttpl;

import java.util.Map;

import lombok.Data;

/**  
 * @ClassName: ReportDictsDto
 * @Description: 报表单元格对应的数据字典
 * @author caiyang
 * @date 2022-11-28 02:54:32 
*/  
@Data
public class ReportCellDictsDto {

	/**  
	 * @Fields cellDictsValueLabel : 单元格坐标对应的字典 外层map的key是sheetindex_r_c，内层map的key是value，根据value获取label，后台显示数据用
	 * @author caiyang
	 * @date 2022-11-28 02:55:50 
	 */  
	private Map<String, Map<String, String>> cellDictsValueLabel;
	
	/**  
	 * @Fields cellDictsLabelValue : 单元格坐标对应的字典 外层map的key是sheetindex_r_c，内层map的key是label，根据label获取value，返回给前端上报数据时用
	 * @author caiyang
	 * @date 2022-11-28 02:57:00 
	 */  
	private Map<String, Map<String, String>> cellDictsLabelValue;
}
