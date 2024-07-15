package com.springreport.util;

public class EscapeUtil {

	//mysql的模糊查询时特殊字符转义
    public static String escapeChar(String before){
    	if(before != null)
    	{
    		before = before.trim();
    	}
        if(StringUtil.isNotEmpty(before)){
//        	before = before.replaceAll("/", "//") ;
   		 	before = before.replaceAll("_", "/_") ;
   		 	before = before.replaceAll("%", "/%") ;
        }
        return before ;
    }
}
