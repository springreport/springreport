package com.springreport.base;

import java.util.Map;

public class CustomSpringReportFunction extends AbstractSpringReportFunction{

	@Override
	public boolean isSpringReportFunction(String cellValue) {
		return false;
	}

	@Override
	public Object calculate(LuckySheetBindData luckySheetBindData,Map<String, Object> extraParams) {
		return null;
	}

}
