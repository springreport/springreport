package com.springreport.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.constants.Constants;
import com.springreport.enums.CellFormatEnum;
import com.springreport.enums.LuckySheetPropsEnum;

/**  
 * @ClassName: LuckysheetUtil
 * @Description: luckysheet工具类
 * @author caiyang
 * @date 2022-10-12 03:51:11 
*/  
public class LuckysheetUtil {

	/**  
	 * @MethodName: formatValue
	 * @Description: 格式化值
	 * @author caiyang
	 * @return 
	 * @return String
	 * @date 2022-10-12 03:53:10 
	 */  
	public static Object formatValue(String fa,Object value)
	{
		if(value == null || StringUtil.isNullOrEmpty(String.valueOf(value)))
		{
			return "";
		}
		if(fa.equals(CellFormatEnum.GENERAL.getCode()))
		{//自动直接返回
			return value;
		}else if(fa.equals(CellFormatEnum.TEXT.getCode())) {
			//纯文本
			return String.valueOf(value);
		}else if(fa.equals(CellFormatEnum.INTEGER.getCode())) {
			//整数
			if(CheckUtil.isNumber(String.valueOf(value)))
			{
				return numberTranform(value,0);
			}else {
				return value;
			}
		}else if(fa.equals(CellFormatEnum.INTEGER_2.getCode())) {
			//逗号分割整数
			if(CheckUtil.isNumber(String.valueOf(value)))
			{
				String numStr = numberTranform(value,0);
				return strAddComma(numStr);
			}else {
				return value;
			}
		}else if(fa.equals(CellFormatEnum.FLOAT1.getCode())) {
			//一位小数
			if(CheckUtil.isNumber(String.valueOf(value)))
			{
				return numberTranform(value,1);
			}else {
				return value;
			}
		}else if(fa.equals(CellFormatEnum.FLOAT2_1.getCode()) || fa.equals(CellFormatEnum.FLOAT2_2.getCode())) {
			//两位小数
			if(CheckUtil.isNumber(String.valueOf(value)))
			{
				return numberTranform(value,2);
			}else {
				return value;
			}
		}else if(fa.equals(CellFormatEnum.FLOAT2_3.getCode()) || fa.equals(CellFormatEnum.FLOAT2_4.getCode())) {
			//两位小数逗号分割
			if(CheckUtil.isNumber(String.valueOf(value)))
			{
				String numStr = numberTranform(value,2);
				return strAddComma(numStr);
			}else {
				return value;
			}
		}else if(fa.equals(CellFormatEnum.PERCENT1.getCode())) {
			//整数百分比
			if(CheckUtil.isNumber(String.valueOf(value)))
			{
//				return parsePercent(value,fa);
				return value;
			}else {
				return value;
			}
		}else if(fa.equals(CellFormatEnum.PERCENT2_1.getCode()) || fa.equals(CellFormatEnum.PERCENT2_2.getCode())) {
			//两位小数百分比
			if(CheckUtil.isNumber(String.valueOf(value)))
			{
//				return parsePercent(value,fa.replace("#", ""));
				return value;
			}else {
				return value;
			}
		}else if(fa.equals(CellFormatEnum.SCIENTIC_1.getCode()) || fa.equals(CellFormatEnum.SCIENTIC_2.getCode())) {
			//科学计数法
			if(CheckUtil.isNumber(String.valueOf(value)))
			{
				return double2Scientific((long) value);
			}else {
				return value;
			}
		}else if(fa.equals(CellFormatEnum.ACCOUNT.getCode())) {
			//会计
			if(CheckUtil.isNumber(String.valueOf(value)))
			{
				return accountFormat(value);
			}else {
				return value;
			}
		}else if(fa.equals(CellFormatEnum.WANYUAN.getCode())) {
			//万元
			if(CheckUtil.isNumber(String.valueOf(value)))
			{
				return wanyuanFormat(value);
			}else {
				return value;
			}
		}else if(fa.equals(CellFormatEnum.WANYUAN_2.getCode())) {
			//万元2位小数
			if(CheckUtil.isNumber(String.valueOf(value)))
			{
				return wanyuanFormat(value);
			}else {
				return value;
			}
		}else if(fa.equals(CellFormatEnum.CURRENCY_1.getCode()) || fa.equals(CellFormatEnum.CURRENCY_2.getCode())) {
			//货币(人民币)
			if(CheckUtil.isNumber(String.valueOf(value)))
			{
				BigDecimal bigDecimal = new BigDecimal(String.valueOf(value));
				DecimalFormat decimalFormat = new DecimalFormat(".00");
				if(bigDecimal.compareTo(new BigDecimal(0)) < 0)
				{
					return "-¥" + decimalFormat.format(Math.abs(bigDecimal.longValue()));
				}else {
					return "¥" + decimalFormat.format(bigDecimal.longValue());
				}
			}else {
				return value;
			}
		}else if(fa.equals(CellFormatEnum.DATE_1.getCode())) {
			//日期格式yyyy-MM-dd
			if(CheckUtil.isDate(String.valueOf(value), DateUtil.FORMAT_LONOGRAM))
			{
				return DateUtil.date2String(DateUtil.string2Date(String.valueOf(value), DateUtil.FORMAT_LONOGRAM), DateUtil.FORMAT_LONOGRAM);
			}else {
				if(CheckUtil.isNumeric(String.valueOf(value))) {
					 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(String.valueOf(value)));
					 return DateUtil.date2String(date, DateUtil.FORMAT_LONOGRAM);
				}else {
					return value;
				}
			}
		}else if(fa.equals(CellFormatEnum.DATE_2.getCode())) {
			//日期格式yyyy-MM-dd hh:mm AM/PM
			if(CheckUtil.isDate(String.valueOf(value), DateUtil.FORMAT_FULL))
			{
				return DateUtil.date2String(DateUtil.string2Date(String.valueOf(value), DateUtil.FORMAT_FULL), DateUtil.FORMAT_FULL_12).replace("上午", "AM").replace("下午", "PM");
			}else {
				if(CheckUtil.isNumeric(String.valueOf(value))) {
					 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(String.valueOf(value)));
					 return DateUtil.date2String(date, DateUtil.FORMAT_FULL);
				}else {
					return value;
				}
			}
		}else if(fa.equals(CellFormatEnum.DATE_3.getCode())) {
			//日期格式yyyy-MM-dd hh:mm
			if(CheckUtil.isDate(String.valueOf(value), DateUtil.FORMAT_FULL))
			{
				return DateUtil.date2String(DateUtil.string2Date(String.valueOf(value), DateUtil.FORMAT_WITHOUTSECONDS), DateUtil.FORMAT_WITHOUTSECONDS);
			}else {
				if(CheckUtil.isNumeric(String.valueOf(value))) {
					 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(String.valueOf(value)));
					 return DateUtil.date2String(date, DateUtil.FORMAT_WITHOUTSECONDS);
				}else {
					return value;
				}
			}
		}else if(fa.equals(CellFormatEnum.DATE_4.getCode())) {
			if(CheckUtil.isDate(String.valueOf(value), DateUtil.FORMAT_FULL))
			{
				return DateUtil.date2String(DateUtil.string2Date(String.valueOf(value), DateUtil.FORMAT_HOURSMINUTES), DateUtil.FORMAT_HOURSMINUTES);
			}else {
				if(CheckUtil.isNumeric(String.valueOf(value))) {
					 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(String.valueOf(value)));
					 return DateUtil.date2String(date, DateUtil.FORMAT_HOURSMINUTES).replace("上午", "AM").replace("下午", "PM");
				}else {
					return value;
				}
			}
		}else if(fa.equals(CellFormatEnum.DATE_5.getCode())) {
			if(CheckUtil.isDate(String.valueOf(value), DateUtil.FORMAT_FULL))
			{
				return DateUtil.date2String(DateUtil.string2Date(String.valueOf(value), DateUtil.FORMAT_LONOGRAM_CN_2), DateUtil.FORMAT_LONOGRAM_CN_2);
			}else {
				if(CheckUtil.isNumeric(String.valueOf(value))) {
					 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(String.valueOf(value)));
					 return DateUtil.date2String(date, DateUtil.FORMAT_LONOGRAM_CN_2);
				}else {
					return value;
				}
			}
		}else if(fa.equals(CellFormatEnum.DATE_6.getCode())) {
			if(CheckUtil.isDate(String.valueOf(value), DateUtil.FORMAT_LONOGRAM_2))
			{
				return DateUtil.date2String(DateUtil.string2Date(String.valueOf(value), DateUtil.FORMAT_LONOGRAM_2), DateUtil.FORMAT_LONOGRAM_2);
			}else {
				if(CheckUtil.isNumeric(String.valueOf(value))) {
					 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(String.valueOf(value)));
					 return DateUtil.date2String(date, DateUtil.FORMAT_LONOGRAM_2);
				}else {
					return value;
				}
			}
		}else if(fa.equals(CellFormatEnum.DATE_7.getCode())) {
			if(CheckUtil.isDate(String.valueOf(value), DateUtil.FORMAT_FULL))
			{
				return DateUtil.date2String(DateUtil.string2Date(String.valueOf(value), DateUtil.FORMAT_FULL), DateUtil.FORMAT_LONOGRAM_CN_2);
			}else {
				if(CheckUtil.isNumeric(String.valueOf(value))) {
					 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(String.valueOf(value)));
					 return DateUtil.date2String(date, DateUtil.FORMAT_LONOGRAM_CN_2);
				}else {
					return value;
				}
			}
		}else if(fa.equals(CellFormatEnum.DATE_8.getCode())) {
			if(CheckUtil.isDate(String.valueOf(value), DateUtil.FORMAT_FULL))
			{
				return DateUtil.date2String(DateUtil.string2Date(String.valueOf(value), DateUtil.FORMAT_FULL), DateUtil.FORMAT_DATE);
			}else {
				if(CheckUtil.isNumeric(String.valueOf(value))) {
					 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(String.valueOf(value)));
					 return DateUtil.date2String(date, DateUtil.FORMAT_DATE);
				}else {
					return value;
				}
			}
		}else if(fa.equals(CellFormatEnum.DATE_9.getCode())) {
			if(CheckUtil.isDate(String.valueOf(value), DateUtil.FORMAT_FULL))
			{
				return DateUtil.date2String(DateUtil.string2Date(String.valueOf(value), DateUtil.FORMAT_FULL), DateUtil.FORMAT_DATE_2);
			}else {
				if(CheckUtil.isNumeric(String.valueOf(value))) {
					 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(String.valueOf(value)));
					 return DateUtil.date2String(date, DateUtil.FORMAT_DATE_2);
				}else {
					return value;
				}
			}
		}else if(fa.equals(CellFormatEnum.DATE_10.getCode())) {
			if(CheckUtil.isDate(String.valueOf(value), DateUtil.FORMAT_FULL))
			{
				return DateUtil.date2String(DateUtil.string2Date(String.valueOf(value), DateUtil.FORMAT_FULL), DateUtil.FORMAT_DATE_CN);
			}else {
				if(CheckUtil.isNumeric(String.valueOf(value))) {
					 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(String.valueOf(value)));
					 return DateUtil.date2String(date, DateUtil.FORMAT_DATE_CN);
				}else {
					return value;
				}
			}
		}else if(fa.equals(CellFormatEnum.DATE_11.getCode())) {
			if(CheckUtil.isDate(String.valueOf(value), DateUtil.FORMAT_FULL))
			{
				return DateUtil.date2String(DateUtil.string2Date(String.valueOf(value), DateUtil.FORMAT_FULL), DateUtil.FORMAT_HOURSMINUTESSECONDS_2);
			}else {
				if(CheckUtil.isNumeric(String.valueOf(value))) {
					 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(String.valueOf(value)));
					 return DateUtil.date2String(date, DateUtil.FORMAT_HOURSMINUTESSECONDS_2);
				}else {
					return value;
				}
			}
		}else if(fa.equals(CellFormatEnum.DATE_12.getCode())) {
			if(CheckUtil.isDate(String.valueOf(value), DateUtil.FORMAT_FULL))
			{
				return DateUtil.date2String(DateUtil.string2Date(String.valueOf(value), DateUtil.FORMAT_FULL), DateUtil.FORMAT_HOURSMINUTES_3);
			}else {
				if(CheckUtil.isNumeric(String.valueOf(value))) {
					 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(String.valueOf(value)));
					 return DateUtil.date2String(date, DateUtil.FORMAT_HOURSMINUTES_3);
				}else {
					return value;
				}
			}
		}
		
		return value;
	}
	
	/**  
	 * @MethodName: getCellFormat
	 * @Description: 获取单元格格式
	 * @author caiyang
	 * @param cellData
	 * @return 
	 * @return String
	 * @date 2022-10-13 06:48:37 
	 */  
	public static String getCellFormat(Map<String, Object> cellData)
	{
		String result = "";
		try {
			JSONObject v = JSONObject.parseObject(JSON.toJSONString(cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())));
			JSONObject ct = v.getJSONObject(LuckySheetPropsEnum.CELLTYPE.getCode());
			result = ct.getString(LuckySheetPropsEnum.CELLFORMAT.getCode());
		} catch (Exception e) {
		}
		return result;
	}
	
	public static JSONObject getCellFormatObject(Map<String, Object> cellData)
	{
		JSONObject v = JSONObject.parseObject(JSON.toJSONString(cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())));
		JSONObject ct = v.getJSONObject(LuckySheetPropsEnum.CELLTYPE.getCode());
		return ct;
	}
	
	/**  
	 * @MethodName: numberTranform
	 * @Description: 数值转换成对应的格式
	 * @author caiyang
	 * @param value 数值字符串
	 * @param digit 小数位数
	 * @return 
	 * @return String
	 * @date 2022-10-12 04:28:56 
	 */  
	private static String numberTranform(Object value,int digit)
	{
		if(value == null) {
			value = 0;
		}
		BigDecimal bigDecimal = new BigDecimal(String.valueOf(value));
		bigDecimal = bigDecimal.setScale(digit, RoundingMode.HALF_UP);
		return String.valueOf(bigDecimal);
	}
	
	/**  
	 * @MethodName: parsePercent
	 * @Description: 数字转成百分比
	 * @author caiyang
	 * @param value
	 * @param digit
	 * @return 
	 * @return String
	 * @date 2022-10-12 04:41:29 
	 */  
	private static String parsePercent(Object value,String fa)
	{
		DecimalFormat decimalFormat = new DecimalFormat(fa);
		 
        // 格式化数字并输出结果
        String formattedNumber = decimalFormat.format(Double.parseDouble(value.toString()));
        return formattedNumber;
	}
	
	/**  
	 * @MethodName: double2Scientific
	 * @Description: 数字转科学计数法
	 * @author caiyang
	 * @param num
	 * @return 
	 * @return String
	 * @date 2022-10-13 08:15:52 
	 */  
	private static String double2Scientific(double num)
	{
		String str = String.format("%E", num);
		String temp = str.substring(0,str.indexOf(".")+4);
		String f = String.format("%.2f", Double.parseDouble(temp));
		str = f + str.substring(str.indexOf("E"));
		return str;
	}
	
	/**  
	 * @MethodName: accountFormat
	 * @Description: 会计格式
	 * @author caiyang
	 * @param value
	 * @return 
	 * @return String
	 * @date 2022-10-13 08:44:44 
	 */  
	private static String accountFormat(Object value) {
		String result = "";
		long l = Long.parseLong(String.valueOf(value));
		if(l >=0)
		{
			result = "¥(" + l + ")";
		}else {
			result = "-¥(" + Math.abs(l) + ")";
		}
		return result;
	}
	
	private static String wanyuanFormat(Object value)
	{
		String result = "";
		BigDecimal bigDecimal = new BigDecimal(String.valueOf(value));
		BigDecimal[] decimalResults = bigDecimal.divideAndRemainder(new BigDecimal(10000));
		if(decimalResults[0].compareTo(new BigDecimal(0)) == 0)
		{
			if(isIntegerValue(decimalResults[1]))
			{
				result = String.valueOf(decimalResults[1].intValue());
			}else {
				DecimalFormat decimalFormat = new DecimalFormat(".00");
				result = decimalFormat.format(decimalResults[1]);
			}
		}else {
			if(decimalResults[1].compareTo(new BigDecimal(0)) == 0)
			{
				result = decimalResults[0].intValue() + "万";
			}else {
				if(decimalResults[1].compareTo(new BigDecimal(1)) < 0)
				{
					DecimalFormat decimalFormat = new DecimalFormat(".00");
					result = decimalResults[0].intValue() + "万" + decimalFormat.format(Math.abs(decimalResults[1].doubleValue()));
				}else {
					if(isIntegerValue(decimalResults[1]))
					{
						DecimalFormat decimalFormat = new DecimalFormat("0000");
						result = decimalResults[0].intValue() + "万" + decimalFormat.format(Math.abs(decimalResults[1].doubleValue()));
					}else {
						DecimalFormat decimalFormat = new DecimalFormat("0000.00");
						result = decimalResults[0].intValue() + "万" + decimalFormat.format(Math.abs(decimalResults[1].doubleValue()));
					}
				}
			}
		}
		return result;
	}
	
	private static String wanyuanFormat2(Object value)
	{
		String result = "";
		BigDecimal bigDecimal = new BigDecimal(String.valueOf(value));
		BigDecimal[] decimalResults = bigDecimal.divideAndRemainder(new BigDecimal(10000));
		if(decimalResults[0].compareTo(new BigDecimal(0)) == 0)
		{
			DecimalFormat decimalFormat = new DecimalFormat(".00");
			result = decimalFormat.format(decimalResults[1]);
		}else {
			if(decimalResults[1].compareTo(new BigDecimal(0)) == 0)
			{
				result = decimalResults[0].intValue() + "万.00";
			}else {
				if(decimalResults[1].compareTo(new BigDecimal(1)) < 0)
				{
					DecimalFormat decimalFormat = new DecimalFormat(".00");
					result = decimalResults[0].intValue() + "万" + decimalFormat.format(Math.abs(decimalResults[1].doubleValue()));
				}else {
					DecimalFormat decimalFormat = new DecimalFormat("0000.00");
					result = decimalResults[0].intValue() + "万" + decimalFormat.format(Math.abs(decimalResults[1].doubleValue()));
				}
				
				
			}
		}
		return result;
	}
	
	/**  
	 * @MethodName: isIntegerValue
	 * @Description: 校验bigdecimal是否是整数
	 * @author caiyang
	 * @param bd
	 * @return 
	 * @return boolean
	 * @date 2022-10-13 03:54:49 
	 */  
	private static boolean isIntegerValue(BigDecimal bd) {
	    boolean ret;
	    try {
	        bd.toBigIntegerExact();
	        ret = true;
	    } catch (ArithmeticException ex) {
	        ret = false;
	    }

	    return ret;
	}
	
	/**  
	 * @MethodName: strAddComma
	 * @Description: 数字逗号分割
	 * @author caiyang
	 * @param str
	 * @return 
	 * @return String
	 * @date 2022-10-14 03:37:17 
	 */  
	private static String strAddComma(String str) {
		if (str == null) {
			str = "";
		}
		String addCommaStr = ""; // 需要添加逗号的字符串（整数）
		String tmpCommaStr = ""; // 小数，等逗号添加完后，最后在末尾补上
		if (str.contains(".")) {
			addCommaStr = str.substring(0,str.indexOf("."));
			tmpCommaStr = str.substring(str.indexOf("."),str.length());
		}else{
			addCommaStr = str;
		}
		// 将传进数字反转
		String reverseStr = new StringBuilder(addCommaStr).reverse().toString();
		String strTemp = "";
		for (int i = 0; i < reverseStr.length(); i++) {
			if (i * 3 + 3 > reverseStr.length()) {
				strTemp += reverseStr.substring(i * 3, reverseStr.length());
				break;
			}
			strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
		}
		// 将 "5,000,000," 中最后一个","去除
		if (strTemp.endsWith(",")) {
			strTemp = strTemp.substring(0, strTemp.length() - 1);
		}
		// 将数字重新反转,并将小数拼接到末尾
		String resultStr = new StringBuilder(strTemp).reverse().toString() + tmpCommaStr;
		return resultStr;
	}
	
	public static String getPercent(double data, int digit) {
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(digit);
        return numberFormat.format(data);
    }
	
	public static String getDateFormat(Map<String, Object> ct) {
		String format = "";
		String fa = String.valueOf(ct.get("fa"));
		if(StringUtil.isNullOrEmpty(fa))
		{
			format = DateUtil.FORMAT_WITHOUTSECONDS;
		}else if("yyyy-MM-dd hh:mm".equals(fa))
		{
			format = DateUtil.FORMAT_WITHOUTSECONDS;
		}else if("yyyy-MM-dd".equals(fa))
		{
			format = DateUtil.FORMAT_LONOGRAM;
		}else if("hh:mm AM/PM".equals(fa))
		{
			format = DateUtil.FORMAT_HOURSMINUTES;
		}else if("hh:mm".equals(fa))
		{
			format = DateUtil.FORMAT_HOURSMINUTES_2;
		}else if("yyyy-MM-dd hh:mm AM/PM".equals(fa))
		{
			format = DateUtil.FORMAT_FULL_12;
		}else if("yyyy/MM/dd".equals(fa))
		{
			format = DateUtil.FORMAT_LONOGRAM_2;
		}else if("yyyy\"年\"M\"月\"d\"日\"".equals(fa))
		{
			format = DateUtil.FORMAT_LONOGRAM_CN_2;
		}else if("MM-dd".equals(fa))
		{
			format = DateUtil.FORMAT_DATE;
		}else if("M-d".equals(fa))
		{
			format = DateUtil.FORMAT_DATE_2;
		}else if("M\"月\"d\"日\"".equals(fa))
		{
			format = DateUtil.FORMAT_DATE_CN;
		}else {
			format = DateUtil.FORMAT_WITHOUTSECONDS;
		}
		return format;
	}
	
	public static String[] getDatasetNames(String datasetName) {
		if(StringUtil.isNotEmpty(datasetName)) {
			String[] datasetNames = datasetName.split(",");
			List<String> datasetNameList = Arrays.asList(datasetNames);
			Set<String> set = new HashSet(datasetNameList);
			datasetNames=(String [])set.toArray(new String[0]);
			return datasetNames;
		}else {
			return new String[] {};
		}
		
	}
	
	/**  
	 * @MethodName: calculateTop
	 * @Description: 计算上方高度
	 * @author caiyang
	 * @param configRowLen
	 * @param r
	 * @return 
	 * @return double
	 * @date 2023-04-13 11:06:52 
	 */  
	public static double calculateTop(Map<String, Object> configRowLen,int r,Object rowhidden) {
		double result = 0;
		JSONObject rowhiddenJsonobject = null;
		if(rowhidden != null)
		{
			rowhiddenJsonobject = JSONObject.parseObject(JSON.toJSONString(rowhidden));
		}
		for (int i = 0; i < r; i++) {
			if(rowhidden != null && rowhiddenJsonobject.containsKey(i+""))
			{
				continue;
			}
			double temp = Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT;
			Object rowlen = configRowLen.get(String.valueOf(i));
			if(rowlen != null)
			{
				temp = Double.parseDouble(String.valueOf(rowlen));
			}
			result = result +temp;
		}
		result = result+r-1;
		return Math.ceil(result);
	}
	
	public static double calculateLeft(Map<String, Object> configColumnLen,int c,Object colhidden) {
		double result = 0;
		int hiddenCount = 0;
		JSONObject colhiddenJsonobject = null;
		if(colhidden != null)
		{
			colhiddenJsonobject = JSONObject.parseObject(JSON.toJSONString(colhidden));
		}
		for (int i = 0; i < c; i++) {
			if(colhiddenJsonobject != null && colhiddenJsonobject.containsKey(i+""))
			{
				hiddenCount = hiddenCount + 1;
				continue;
			}
			double temp = Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH;
			Object collen = configColumnLen.get(String.valueOf(i));
			if(collen != null)
			{
				temp = Double.parseDouble(String.valueOf(collen));
			}
			result = result +temp;
		}
		result = result  - hiddenCount + c;
		return Math.ceil(result);
	}
	
	public static double calculateHeight(Map<String, Object> configRowLen,int r,int rowSpan) {
		double result = 0;
		for (int i = r; i < (r+rowSpan); i++) {
			double temp = Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT;
			Object rowlen = configRowLen.get(String.valueOf(i));
			if(rowlen != null)
			{
				temp = Double.parseDouble(String.valueOf(rowlen));
			}
			result = result +temp;
		}
		return Math.ceil(result);
	}
	
	public static double calculateWidth(Map<String, Object> configColumnLen,int c,int colSpan) {
		double result = 0;
		for (int i = c; i < (c+colSpan); i++) {
			double temp = Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH;
			Object collen = configColumnLen.get(String.valueOf(i));
			if(collen != null)
			{
				temp = Double.parseDouble(String.valueOf(collen));
			}
			result = result +temp;
		}
		return Math.ceil(result);
	}
	
	public static void getChartsSeriesAttr(JSONArray jsonArray,Set<String> chartsAttrs,Map<String, List<String>> chartAttrsMap){
		if(!ListUtil.isEmpty(jsonArray))
		{
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				JSONObject chartOptions = jsonObject.getJSONObject("chartOptions");
				if(chartOptions != null)
				{
					JSONObject defaultOption = chartOptions.getJSONObject("defaultOption");
					String chartId = jsonObject.getString("chart_id");
					if(defaultOption != null)
					{
						JSONArray seriesData = defaultOption.getJSONArray("seriesData");
						if(!ListUtil.isEmpty(seriesData))
						{
							List<String> chartAttrs = new ArrayList<>();
							for (int j = 0; j < seriesData.size(); j++) {
								 JSONArray series = seriesData.getJSONArray(j);
								 String attr = series.getString(0);
								 chartAttrs.add(attr);
								 chartsAttrs.add(attr);
							}
							chartAttrsMap.put(chartId, chartAttrs);
						}
					}
				}
				
			}
		}
	}
	
	public static Map<String, String> getChartRangeCells(JSONArray jsonArray){
		Map<String, String> result = new HashMap<>();
		if(!ListUtil.isEmpty(jsonArray))
		{
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				JSONObject chartOptions = jsonObject.getJSONObject("chartOptions");
				String rangeTxt = chartOptions.getString("rangeTxt");
				String chartId = jsonObject.getString("chart_id");
				int startR = 0;
				int endR = 0;
				int startC = 0;
				int endC = 0;
				if(rangeTxt.contains(":"))
				{
					String[] ranges = rangeTxt.split(":");
					int c = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(ranges[0])) - 1;
					int c1 = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(ranges[1])) - 1;
					int r = SheetUtil.getRowNum(ranges[0]) - 1;
					int r1 = SheetUtil.getRowNum(ranges[1]) - 1;
					startR = r;
					endR = r1;
					startC = c;
					endC = c1;
				}else {
					int c = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(rangeTxt)) - 1;
					int r = SheetUtil.getRowNum(rangeTxt) - 1;
					startR = r;
					endR = r;
					startC = c;
					endC = c;
				}
				for (int j = startR; j <= endR; j++) {
					for (int j2 = startC; j2 <= endC; j2++) {
						String key = j+"_"+j2;
						if(result.containsKey(key))
						{
							result.put(key, result.get(key)+","+chartId);
						}else {
							result.put(key, chartId);
						}
					}
				}
			}
		}
		return result;
	}
	
	/**  
	 * @MethodName: calculateImgCoors
	 * @Description: 计算图片坐标
	 * @author caiyang
	 * @param imgInfo
	 * @param configRowLen
	 * @param rowhidden
	 * @param configColumnLen
	 * @param colhidden
	 * @return Map<String,Integer>
	 * @date 2023-12-14 10:16:13 
	 */ 
	public static JSONObject calculateImgCoors(JSONObject imgInfo,Map<String, Object> configRowLen,Object rowhidden,Map<String, Object> configColumnLen,Object colhidden){
		if(rowhidden == null)
		{
			rowhidden = new JSONObject();
		}
		if(colhidden == null)
		{
			colhidden = new JSONObject();
		}
		if(configRowLen == null) {
			configRowLen = new HashMap<>();
		}
		if(configColumnLen == null) {
			configColumnLen = new HashMap<>();
		}
		JSONObject result = new JSONObject();
		double top = imgInfo.getJSONObject("default").getIntValue("top");
		double left = imgInfo.getJSONObject("default").getIntValue("left");
		double width = imgInfo.getJSONObject("default").getIntValue("width");
		double height = imgInfo.getJSONObject("default").getIntValue("height");
		double topMax = top + height;
		double leftMax = left + width;
		int stc = 0;
		int str = 0;
		JSONObject rowhiddenJsonobject = null;
		if(rowhidden != null)
		{
			rowhiddenJsonobject = JSONObject.parseObject(JSON.toJSONString(rowhidden));
		}
		JSONObject colhiddenJsonobject = null;
		if(colhidden != null)
		{
			colhiddenJsonobject = JSONObject.parseObject(JSON.toJSONString(colhidden));
		}
		//计算开始横坐标
		while(true)
		{
			if(rowhiddenJsonobject.containsKey(str))
			{
				str = str + 1;
			}else if(configRowLen.containsKey(String.valueOf(str)))
			{
				top = top - Double.parseDouble(String.valueOf(configRowLen.get(String.valueOf(str))));
				str = str + 1;
			}else {
				top = top - Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT;
				str = str + 1;
			}
			if(top < 0)
			{
				str = str - 1;
				break;
			}
			if(top == 0)
			{
				break;
			}
		}
		//计算结束横坐标
		int edr = 0;
		while(true)
		{
			if(rowhiddenJsonobject.containsKey(edr))
			{
				edr = edr + 1;
			}else if(configRowLen.containsKey(String.valueOf(edr)))
			{
				topMax = topMax - Double.parseDouble(String.valueOf(configRowLen.get(String.valueOf(edr))));
				edr = edr + 1;
			}else {
				topMax = topMax - Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT;
				edr = edr + 1;
			}
			if(topMax < 0)
			{
				edr = edr - 1;
				break;
			}
			if(topMax == 0)
			{
				break;
			}
		}
		//计算开始纵坐标
		while(true)
		{
			if(colhiddenJsonobject.containsKey(stc))
			{
				stc = stc + 1;
			}else if(configColumnLen.containsKey(String.valueOf(stc)))
			{
				left = left - Double.parseDouble(String.valueOf(configColumnLen.get(String.valueOf(stc))));
				stc = stc + 1;
			}else {
				left = left - Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH;
				stc = stc + 1;
			}
			if(left < 0)
			{
				stc = stc - 1;
				break;
			}
			if(left == 0)
			{
				break;
			}
		}
		//计算结束纵坐标
		int edc = 0;
		while(true)
		{
			if(colhiddenJsonobject.containsKey(edc))
			{
				edc = edc + 1;
			}else if(configColumnLen.containsKey(String.valueOf(edc)))
			{
				leftMax = leftMax - Double.parseDouble(String.valueOf(configColumnLen.get(String.valueOf(edc))));
				edc = edc + 1;
			}else {
				leftMax = leftMax - Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH;
				edc = edc + 1;
			}
			if(leftMax < 0)
			{
				edc = edc - 1;
				break;
			}
			if(leftMax == 0)
			{
				break;
			}
		}
		result.put("str", str);
		result.put("edr", edr);
		result.put("stc", stc);
		result.put("edc", edc);
		return result;
	}
	
//	public static double calculateTop(Map<String, Object> configRowLen,Object rowhidden) {
//		
//	}
	
	/**  
	 * @MethodName: calculateCols
	 * @Description: 根据宽度计算列坐标
	 * @author caiyang
	 * @param width
	 * @param columnlen
	 * @param colhidden
	 * @return double
	 * @date 2024-07-12 02:12:50 
	 */ 
	public static JSONObject calculateCols(double width,JSONObject columnlen,JSONObject colhidden) {
		if(columnlen == null) {
			columnlen = new JSONObject();
		}
		if(colhidden == null) {
			colhidden = new JSONObject();
		}
		JSONObject result = new JSONObject();
		int c = 0;
		double temp = width;//用于记录上次计算完以后的值，该值返回去计算偏移量
		while(true) {
			if(colhidden.containsKey(String.valueOf(c))) {
				c = c + 1;
			}else if(columnlen.containsKey(String.valueOf(c))) {
				width = width - Double.parseDouble(String.valueOf(columnlen.get(String.valueOf(c))))-1;
				c = c + 1;
			}else {
				width = width - Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH;
				c = c + 1;
			}
			if(width < 0) {
				c = c - 1;
				BigDecimal colWidth = new BigDecimal(0);
				if(columnlen.containsKey(String.valueOf(c))) {
					colWidth = columnlen.getBigDecimal(String.valueOf(c));
				}else {
					colWidth = new BigDecimal(Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
				}
				result.put("percent", new BigDecimal(temp).divide(colWidth,2,RoundingMode.HALF_UP).floatValue());
				break;
			}
			if(width == 0) {
				result.put("percent", 1);
				break;
			}
			if(width < 0) {
				c = c - 1;
			}
			temp = width;
		}
		result.put("c", c);
		result.put("dx", temp);
		return result;
	}
	
	public static JSONObject calculateRows(double height,JSONObject rowlen,JSONObject rowhidden) {
		if(rowlen == null) {
			rowlen = new JSONObject();
		}
		if(rowhidden == null) {
			rowhidden = new JSONObject();
		}
		JSONObject result = new JSONObject();
		int r = 0;
		double temp = height;//用于记录上次计算完以后的值，该值返回去计算偏移量
		while(true) {
			if(rowhidden.containsKey(r)) {
				r = r + 1;
			}else if(rowlen.containsKey(String.valueOf(r))) {
				height = height - Double.parseDouble(String.valueOf(rowlen.get(String.valueOf(r)))) - 1;
				r = r + 1;
			}else {
				height = height - Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT;
				r = r + 1;
			}
			if(height < 0) {
				r = r - 1;
				BigDecimal rowHeight = new BigDecimal(0);
				if(rowlen.containsKey(String.valueOf(r))) {
					rowHeight = rowlen.getBigDecimal(String.valueOf(r));
				}else {
					rowHeight = new BigDecimal(Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
				}
				result.put("percent", new BigDecimal(temp).divide(rowHeight,2,RoundingMode.HALF_UP).floatValue());
				break;
			}
			if(height == 0) {
				result.put("percent", 1);
				break;
			}
			temp = height;
			
		}
		result.put("r", r);
		result.put("dy", temp);
		return result;
	}
	
	public static void main(String[] args) {
//		java.text.NumberFormat percentFormat =java.text.NumberFormat.getPercentInstance();
//		percentFormat.setMaximumFractionDigits(0);//最大小数位数
//		System.out.println(String.valueOf(percentFormat.format(10.335323)).replaceAll(",", ""));
//		
//		double a = 1231254;
////		System.out.println(wanyuanFormat(a));
//		DecimalFormat decimalFormat = new DecimalFormat(".00");
////		return "-¥" + Math.abs(bigDecimal.longValue());
//		System.out.println(wanyuanFormat2(a));
//		System.out.println(strAddComma("456.36"));
//		double data = 8.00;
//        int digit = 2;
//        String numberPercent = getPercent(data, digit);
        System.out.println(LuckysheetUtil.numberTranform(null,2));
	}
}
