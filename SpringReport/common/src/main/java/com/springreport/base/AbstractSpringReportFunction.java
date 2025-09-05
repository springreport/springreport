package com.springreport.base;

import java.util.Map;

public abstract class AbstractSpringReportFunction {
	
	/**  
	 * @MethodName: isSpringReportFunction
	 * @Description: 判断单元格是否有springreport的自定义函数
	 * @author caiyang
	 * @param cellValue
	 * @return boolean
	 * @date 2025-08-31 09:05:15 
	 */ 
	public abstract boolean isSpringReportFunction(String cellValue);
	
	
	/**  
	 * @MethodName: calculate
	 * @Description: 公式计算
	 * @author caiyang
	 * @param luckySheetBindData
	 * @return Object
	 * @date 2025-08-31 10:00:33 
	 */ 
	public abstract Object calculate(LuckySheetBindData luckySheetBindData,Map<String, Object> extraParams);
}
