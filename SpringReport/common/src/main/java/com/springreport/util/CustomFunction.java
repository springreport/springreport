package com.springreport.util;

import java.util.List;

/**  
 * @ClassName: CustomFunction
 * @Description: 自定义函数
 * @author caiyang
 * @date 2025-03-20 07:04:01 
*/ 
public class CustomFunction {

	private static int dataLength(int size,int ext) {
		return size+ext;
	}
	
	public static Object execute(String methodName,List<String> params) {
		Object result = null;
		switch (methodName) {
		case "dataLength":
			result = dataLength(Integer.parseInt(params.get(0)), Integer.parseInt(params.get(1)));
			break;
		default:
			break;
		}
		return result;
	} 
}
