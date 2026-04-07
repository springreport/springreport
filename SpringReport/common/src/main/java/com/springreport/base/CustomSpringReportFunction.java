package com.springreport.base;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
public class CustomSpringReportFunction extends AbstractSpringReportFunction{


	@Override
	public boolean isSpringReportFunction(String cellValue) {
		return false;
	}

	@Override
	public Object calculate(LuckySheetBindData luckySheetBindData,Map<String, Object> extraParams) {
		return null;
	}

	@Override
	public JSONArray parseFormula(String property) {
		return null;
	}

}
