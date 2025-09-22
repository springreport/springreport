package com.springreport.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.springreport.enums.ConditionTypeEnum;
import com.springreport.enums.OperatorEnum;

public class ListUtil {

    
    /**  
     * @Title: isEmpty
     * @Description: 判断集合是否为空
     * @param collection
     * @return
     * @author caiyang
     * @date 2021-05-25 07:18:21 
     */ 
    public static boolean isEmpty(Collection<?> collection)
    {
    	if(collection == null || collection.size() == 0)
    	{
    		return true;
    	}
    	return false;
    }
    
    public static boolean isNotEmpty(Collection<?> collection)
    {
    	if(collection == null || collection.size() == 0)
    	{
    		return false;
    	}
    	return true;
    }
    
    /**  
	 * @Title: getDiffData
	 * @Description: 获取集合差值
	 * @param list1
	 * @param list2
	 * @return
	 * @author caiyang
	 * @date 2020-06-11 02:55:49 
	 */ 
	public static List<String> getDiffData(List<String> list1,List<String> list2)
	{
		List<String> newlist1 = new ArrayList<String>();
		newlist1.addAll(list1);
		List<String> newlist2 = new ArrayList<String>();
		newlist2.addAll(list2);
		newlist1.removeAll(newlist2);
		return newlist1;
	}
	
	/**  
	 * @MethodName: isStringContainsListElement
	 * @Description: 判断字符串中是否包含数组中的某个元素
	 * @author caiyang
	 * @param str
	 * @param list
	 * @return 
	 * @return boolean
	 * @date 2022-09-17 09:56:32 
	 */  
	public static boolean isStringContainsListElement(String str,List<List<String>> list) {
		boolean result = false;
		if(!ListUtil.isEmpty(list))
		{
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list.get(i).size(); j++) {
					if(str.contains(String.valueOf(list.get(i).get(j)).split("\\.")[0]) && str.contains(String.valueOf(list.get(i).get(j)).split("\\.")[1]))
					{
						result = true;
						break;
					}
					if(result)
					{
						break;
					}
				}
				
			}
		}
		return result;
	}
	
	/**  
	 * @MethodName: getFormulaDatasetName
	 * @Description: 获取公式中的数据集名称，公式中的必须是同一个数据集
	 * @author caiyang
	 * @param str
	 * @param list
	 * @return 
	 * @return String
	 * @date 2022-09-21 05:00:20 
	 */  
	public static String getFormulaDatasetName(String str,List<List<String>> list)
	{
		String result = null;
		if(!ListUtil.isEmpty(list))
		{
			for (int i = 0; i < list.size(); i++) {
				List<String> columns = list.get(i);
				for (int j = 0; j < columns.size(); j++) {
					if(str.contains(columns.get(j).split("-")[1]))
					{
						if(StringUtil.isNullOrEmpty(result))
						{
							result = String.valueOf(columns.get(j).split("-")[1]).split("\\.")[0];
						}else {
							result = result + "," + String.valueOf(columns.get(j).split("-")[1]).split("\\.")[0];
						}
					}
				}
			}
			if(StringUtil.isNullOrEmpty(result)) {
				result = str.split("\\.")[0].replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
			}
		}
		return result;
	}
	
	public static Map<String, String> getNewCellValue(String str,List<List<String>> list,Map<String, String> datasetNameIdMap)
	{
		Map<String, String> result = new HashMap<>();
		String datasetNames = "";
		String cellValue = str;
 		if(!ListUtil.isEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				List<String> columns = list.get(i);
				for (int j = 0; j < columns.size(); j++) {
					if(str.contains(columns.get(j).split("-")[1])) {
						String datasetName = String.valueOf(columns.get(j).split("-")[1]).split("\\.")[0];
						String newDatasetName = datasetName;
						if(datasetNameIdMap.containsKey(datasetName))
						{
							newDatasetName = datasetNameIdMap.get(datasetName)+"-"+newDatasetName;
						}
						if(StringUtil.isNullOrEmpty(datasetNames))
						{
							datasetNames = newDatasetName;
						}else {
							datasetNames = datasetNames + "," + newDatasetName;
						}
						cellValue = cellValue.replaceAll(datasetName+"\\.", newDatasetName+"\\.");
						break;
					}
				}
			}
		}
 		result.put("cellValue", cellValue);
 		if(StringUtil.isNullOrEmpty(datasetNames)) {
 			datasetNames = cellValue.split("\\.")[0].replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
 		}
 		result.put("datasetNames", datasetNames);
		return result;
	}
	
	public static float getSimilarityRatio(String str, String target) {

		int d[][]; // 矩阵
		int n = str.length();
		int m = target.length();
		int i; // 遍历str的
		int j; // 遍历target的
		char ch1; // str的
		char ch2; // target的
		int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
		if (n == 0 || m == 0) {
			return 0;
		}
		d = new int[n + 1][m + 1];
		for (i = 0; i <= n; i++) { // 初始化第一列
			d[i][0] = i;
		}

		for (j = 0; j <= m; j++) { // 初始化第一行
			d[0][j] = j;
		}

		for (i = 1; i <= n; i++) { // 遍历str
			ch1 = str.charAt(i - 1);
			// 去匹配target
			for (j = 1; j <= m; j++) {
				ch2 = target.charAt(j - 1);
				if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
					temp = 0;
				} else {
					temp = 1;
				}
				// 左边+1,上边+1, 左上角+temp取最小
				d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), d[i - 1][j - 1] + temp);
			}
		}

		return (1 - (float) d[n][m] / Math.max(str.length(), target.length())) * 100F;
	}
	
	public static Map<String, Object> getProperties(String str,Map<String, Object> datas)
	{
		Map<String, Object> result = new LinkedHashMap<>();
		Set<String> set = datas.keySet();
		boolean isMtch = false;
		for (String o : set) {
			if(str.equals(o))
			{
				isMtch = true;
				Object data = datas.get(o);
				if(data instanceof Timestamp)
				{
					Timestamp timestamp = (Timestamp) data;
		        	long stamp = timestamp.getTime();
		        	String date = DateUtil.Stamp2String(stamp);
		        	result.put(o, date);
				}else if(data instanceof Date)
				{
					Date date = (Date) data;
		        	long stamp = date.getTime();
		        	String dateString = DateUtil.Stamp2String(stamp,DateUtil.FORMAT_LONOGRAM);
		        	result.put(o, dateString);
				}else if(data instanceof LocalDateTime)
				{
					LocalDateTime localDateTime = (java.time.LocalDateTime) data;
		        	java.util.Date date = java.util.Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
		        	String dateString =  DateUtil.date2String(date, DateUtil.FORMAT_FULL);
		        	result.put(o, dateString);
				}else {
					result.put(o, data);
				}
				break;
			}
		}
		if(!isMtch)
		{
			List<PropertyRatio> propertyRatios = new ArrayList<>();
			for (String o : set) {
				if(str.contains(o))
				{
					PropertyRatio propertyRatio = new PropertyRatio();
					float ratio = getSimilarityRatio(str,o);
					propertyRatio.setProperty(o);
					propertyRatio.setRatio(ratio);
					propertyRatios.add(propertyRatio);
				}
			}
			if(!ListUtil.isEmpty(propertyRatios))
			{
				propertyRatios.sort(Comparator.comparing(PropertyRatio::getRatio).reversed());
				for (int i = 0; i < propertyRatios.size(); i++) {
					Object data = datas.get(propertyRatios.get(i).getProperty());
					if (data instanceof Timestamp) {
						Timestamp timestamp = (Timestamp) data;
						long stamp = timestamp.getTime();
						String date = DateUtil.Stamp2String(stamp);
						result.put(propertyRatios.get(i).getProperty(), date);
					} else if (data instanceof Date) {
						Date date = (Date) data;
						long stamp = date.getTime();
						String dateString = DateUtil.Stamp2String(stamp, DateUtil.FORMAT_LONOGRAM);
						result.put(propertyRatios.get(i).getProperty(), dateString);
					} else if (data instanceof LocalDateTime) {
						LocalDateTime localDateTime = (java.time.LocalDateTime) data;
						java.util.Date date = java.util.Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
						String dateString = DateUtil.date2String(date, DateUtil.FORMAT_FULL);
						result.put(propertyRatios.get(i).getProperty(), dateString);
					} else {
						result.put(propertyRatios.get(i).getProperty(), data);
					}
				}
			}else {
//				result.put(str, "");
			}
			
		}
		return result;
	}
	
	public static Map<String, Object> getProperties(String str,Map<String, Object> datas,String datesetName)
	{
		Map<String, Object> result = new LinkedHashMap<>();
		Set<String> set = datas.keySet();

		List<PropertyRatio> propertyRatios = new ArrayList<>();
		for (String o : set) {
			if(str.contains(datesetName+"."+o))
			{
				PropertyRatio propertyRatio = new PropertyRatio();
				float ratio = getSimilarityRatio(str,o);
				propertyRatio.setProperty(o);
 				propertyRatio.setRatio(ratio);
				propertyRatios.add(propertyRatio);
			}
		}
		if(!ListUtil.isEmpty(propertyRatios))
		{
			propertyRatios.sort(Comparator.comparing(PropertyRatio::getRatio).reversed());
			for (int i = 0; i < propertyRatios.size(); i++) {
				Object data = datas.get(propertyRatios.get(i).getProperty());
				if (data instanceof Timestamp) {
					Timestamp timestamp = (Timestamp) data;
					long stamp = timestamp.getTime();
					String date = DateUtil.Stamp2String(stamp);
					result.put(datesetName+"."+propertyRatios.get(i).getProperty(), date);
				} else if (data instanceof Date) {
					Date date = (Date) data;
					long stamp = date.getTime();
					String dateString = DateUtil.Stamp2String(stamp, DateUtil.FORMAT_LONOGRAM);
					result.put(datesetName+"."+propertyRatios.get(i).getProperty(), dateString);
				} else if (data instanceof LocalDateTime) {
					LocalDateTime localDateTime = (java.time.LocalDateTime) data;
					java.util.Date date = java.util.Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
					String dateString = DateUtil.date2String(date, DateUtil.FORMAT_FULL);
					result.put(datesetName+"."+propertyRatios.get(i).getProperty(), dateString);
				} else {
					result.put(datesetName+"."+propertyRatios.get(i).getProperty(), data);
				}
			}
		}
		return result;
	}
	
	public static Map<String, Object> getProperties(List<String> attrs,Map<String, Object> datas)
	{
		Map<String, Object> result = new LinkedHashMap<>();
		if(ListUtil.isNotEmpty(attrs)) {
			for (int i = 0; i < attrs.size(); i++) {
				result.put(attrs.get(i), datas.get(attrs.get(i)));
			}
		}
		return result;
	}
	
	public static List<String> getPropertyList(String str,Map<String, Object> datas,String datesetName)
	{
		List<String> result = new ArrayList<>();
		Set<String> set = datas.keySet();

		List<PropertyRatio> propertyRatios = new ArrayList<>();
		for (String o : set) {
			if(str.contains(datesetName+"."+o))
			{
				PropertyRatio propertyRatio = new PropertyRatio();
				float ratio = getSimilarityRatio(str,o);
				propertyRatio.setProperty(o);
				propertyRatio.setRatio(ratio);
				propertyRatios.add(propertyRatio);
			}
		}
		if(!ListUtil.isEmpty(propertyRatios))
		{
			propertyRatios.sort(Comparator.comparing(PropertyRatio::getRatio).reversed());
			for (int i = 0; i < propertyRatios.size(); i++) {
				result.add(propertyRatios.get(i).getProperty());
			}
		}
		return result;
	}
	
	public static List<String> seperateProperty(String property)
	{
		property = property.replaceFirst("=", "").replaceAll("\\(", "").replaceAll("\\)", "");
		List<String> result = new ArrayList<String>();
		char[] charArray = property.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < charArray.length; j++) 
		{
			if(ListUtil.isGeldigeOperator(charArray[j]) || j == charArray.length-1)
			{
				if(j == charArray.length-1)
				{
					sb.append(charArray[j]);
				}
				result.add(sb.toString());
				sb = new StringBuffer();
			}else {
				sb.append(charArray[j]);
			}
		}
		return result;
	}
	
	public static  boolean isGeldigeOperator(char o) {
		if(String.valueOf(o).equals("%") || String.valueOf(o).equals("/") || String.valueOf(o).equals("*") || String.valueOf(o).equals("+")
				|| String.valueOf(o).equals("-"))
		{
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean filterDatas(JSONArray filters,Map<String, Object> data,String filterType) {
		boolean result = true;
		if("and".equals(filterType))
		{
			result = true;
		}else {
			result = false;
		}
		try {
			if (!ListUtil.isEmpty(filters)) {
				for (int i = 0; i < filters.size(); i++) {
					Object value = data.get(filters.getJSONObject(i).getString("property"));
//					if(value != null)
//					{
						String operator = filters.getJSONObject(i).getString("operator");
						String condition = filters.getJSONObject(i).getString("value");
						String type = filters.getJSONObject(i).getString("type");
						String dateFormat = StringUtil.isNotEmpty(filters.getJSONObject(i).getString("dateFormat"))?filters.getJSONObject(i).getString("dateFormat"):DateUtil.FORMAT_LONOGRAM;
						if(OperatorEnum.EQ.getCode().equals(operator))
						{//等于
							if(StringUtil.isNotEmpty(condition) && !String.valueOf(value).equals(condition))
							{
								result = false;
							}else {
								result = true;
							}
						}
						else if(OperatorEnum.NE.getCode().equals(operator))
						{//不等于
							if(StringUtil.isNotEmpty(condition) && String.valueOf(value).equals(condition))
							{
								result = false;
							}else {
								result = true;
							}
						}
						else if(OperatorEnum.GT.getCode().equals(operator))
						{//大于
							if(ConditionTypeEnum.DATE.getCode().equals(type))
							{//日期类型
								if(CheckUtil.isDate(condition,dateFormat) && CheckUtil.isDate(String.valueOf(value),dateFormat))
								{
									int compareResult = DateUtil.daysCompare(String.valueOf(value), condition,dateFormat);
									if(compareResult <= 0)
									{
										result = false;
									}else {
										result = true;
									}
								}else {
									result = false;
								}
							}else {
								//数字类型
								if(value == null) {
									value = 0;
								}
								if(CheckUtil.isNumber(condition) && CheckUtil.isNumber(String.valueOf(value)))
								{
									BigDecimal bg1 = new BigDecimal(String.valueOf(value));
									BigDecimal bg2 = new BigDecimal(condition);
									if(bg1.compareTo(bg2)<=0)
									{
										result = false;
									}else {
										result = true;
									}
								}else {
									result = false;
								}
							}
						}
						else if(OperatorEnum.GE.getCode().equals(operator))
						{//大于等于
							if(ConditionTypeEnum.DATE.getCode().equals(type))
							{//日期类型
								if(CheckUtil.isDate(condition,dateFormat) && CheckUtil.isDate(String.valueOf(value),dateFormat))
								{
									int compareResult = DateUtil.daysCompare(String.valueOf(value), condition,dateFormat);
									if(compareResult < 0)
									{
										result = false;
									}else {
										result = true;
									}
								}else {
									result = false;
								}
							}else{//数字类型
								if(value == null) {
									value = 0;
								}
								if(CheckUtil.isNumber(condition) && CheckUtil.isNumber(String.valueOf(value)))
								{
									BigDecimal bg1 = new BigDecimal(String.valueOf(value));
									BigDecimal bg2 = new BigDecimal(condition);
									if(bg1.compareTo(bg2)<0)
									{
										result = false;
									}else {
										result = true;
									}
								}else {
									result = false;
								}
							}
						}
						else if(OperatorEnum.LT.getCode().equals(operator))
						{//小于
							if(ConditionTypeEnum.DATE.getCode().equals(type))
							{//日期类型
								if(CheckUtil.isDate(condition,dateFormat) && CheckUtil.isDate(String.valueOf(value),dateFormat))
								{
									int compareResult = DateUtil.daysCompare(String.valueOf(value), condition,dateFormat);
									if(compareResult >= 0)
									{
										result = false;
									}else {
										result = true;
									}
								}else {
									result = false;
								}
							}else{//数字类型
								if(value == null) {
									value = 0;
								}
								if(CheckUtil.isNumber(condition) && CheckUtil.isNumber(String.valueOf(value)))
								{
									BigDecimal bg1 = new BigDecimal(String.valueOf(value));
									BigDecimal bg2 = new BigDecimal(condition);
									if(bg1.compareTo(bg2)>=0)
									{
										result = false;
									}else {
										result = true;
									}
								}else {
									result = false;
								}
							}
						}
						else if(OperatorEnum.LE.getCode().equals(operator))
						{//小于等于
							if(ConditionTypeEnum.DATE.getCode().equals(type))
							{//日期类型
								if(CheckUtil.isDate(condition,dateFormat) && CheckUtil.isDate(String.valueOf(value),dateFormat))
								{
									int compareResult = DateUtil.daysCompare(String.valueOf(value), condition,dateFormat);
									if(compareResult > 0)
									{
										result = false;
									}else {
										result = true;
									}
								}else {
									result = false;
								}
							}else {//数字类型
								if(value == null) {
									value = 0;
								}
								if(CheckUtil.isNumber(condition) && CheckUtil.isNumber(String.valueOf(value)))
								{
									BigDecimal bg1 = new BigDecimal(String.valueOf(value));
									BigDecimal bg2 = new BigDecimal(condition);
									if(bg1.compareTo(bg2)>0)
									{
										result = false;
									}else {
										result = true;
									}
								}else {
									result = false;
								}
							}
						}
						else if(OperatorEnum.IN.getCode().equals(operator))
						{
							result = false;
							String [] conditions = condition.split(",");
							for (int j = 0; j < conditions.length; j++) {
								if(String.valueOf(value).equals(conditions[j]))
								{
									result = true;
									break;
								}
							}
						}
						else if(OperatorEnum.NOTIN.getCode().equals(operator))
						{
							String [] conditions = condition.split(",");
							for (int j = 0; j < conditions.length; j++) {
								if(String.valueOf(value).equals(conditions[j]))
								{
									result = false;
									break;
								}
							}
						}
//					}
					if("and".equals(filterType))
					{
						if(!result)
						{
							break;
						}
					}else {
						if(result)
						{
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	/**  
	 * @MethodName: getSerialNumList
	 * @Description: 获取列表中多组连续数据
	 * @author caiyang
	 * @param list
	 * @return 
	 * @return List<List<Integer>>
	 * @date 2023-05-05 04:00:05 
	 */  
	public static List<List<Integer>> getSerialNumList(List<Integer> list) {
	    List<List<Integer>> resultList = new ArrayList<>();
	    List<Integer> arrList = new ArrayList<>();
	    resultList.add(arrList);
	    if (list.size() == 1) {
	        resultList.get(resultList.size() - 1).add(list.get(0));
	        return resultList;
	    }
	    for (int i = 0; i < list.size(); i++) {
	        Integer nextNum = list.get(i + 1);
	        Integer nowNum = list.get(i);
	        if (nextNum - nowNum != 1) {
	            resultList.get(resultList.size() - 1).add(nowNum);
	            arrList = new ArrayList<>();
	            resultList.add(arrList);
	        } else {
	            arrList.add(nowNum);
	        }
	        if (i + 1 == list.size() - 1 ) {
	            arrList.add(nextNum);
	            break;
	        }
	    }
	    return resultList;
	}
	
	/**  
	 * @MethodName: getSubList
	 * @Description: list手动分页
	 * @author caiyang
	 * @param rows
	 * @param pageNumber
	 * @param pageSize
	 * @return 
	 * @return List<Map<String,Object>>
	 * @date 2023-05-13 09:36:26 
	 */  
	public static List<Map<String, Object>> getSubList(List<Map<String, Object>> rows,Integer pageNum,Integer pageSize)
	{
		List<Map<String, Object>> result = null;
		int startIndex = 0;
        int endIndex = rows.size();
        if (pageNum != null && pageSize != null) {
            startIndex = getStartIndex(pageNum, pageSize);
            endIndex = getEndIndex(pageNum, pageSize);
        }
        if (rows.size() >= endIndex) {
        	result = rows.subList(startIndex, endIndex);
        } else {
        	result = rows.subList(startIndex, rows.size());
        }
        return result;
	}
	

/**
	 * 获取起始位置
	 * @return
	 */
	public static int getStartIndex(int pageNum,int pageSize){
		return 0+(pageNum-1)*pageSize;
	}
	/**
	 * 获取结束位置
	 * @return
	 */
	public static int getEndIndex(int pageNum,int pageSize){
		return pageNum*pageSize;
	}
	
	public static List<List<Map<String, Object>>> groupDatas(List<Map<String, Object>> datas,List<String> attrs){
		List<List<Map<String, Object>>> result = new ArrayList<>();
		List<List<Map<String, Object>>> listDatas = new ArrayList<>();
		if(ListUtil.isNotEmpty(datas) && ListUtil.isNotEmpty(attrs)) {
			listDatas.add(datas);
			for (int j = 0; j < attrs.size(); j++) {
				Map<String, List<Map<String, Object>>> dataMap = null;
				for (int t = 0; t < listDatas.size(); t++) {
					dataMap = new LinkedHashMap<>();
					datas = listDatas.get(t);
					for (int i = 0; i < datas.size(); i++) {
						List<Map<String, Object>> rowList = null;
						String key = String.valueOf(datas.get(i).get(attrs.get(j)));
						if (dataMap.containsKey(key)) {
							rowList = dataMap.get(key);
						}else {
							rowList = new ArrayList<>();
							dataMap.put(key, rowList);
						}
						rowList.add(datas.get(i));
					}
				}
				Iterator<Entry<String, List<Map<String, Object>>>> entries = dataMap.entrySet().iterator();
				while(entries.hasNext()){
					result.add(entries.next().getValue());
				}
				listDatas = result;
			}
			
		}
		return listDatas;
	}
	
	public static void main(String[] args) {
		 Integer[] arr = {1,2, 3, 4, 6, 8, 10, 11, 12, 15, 16};
		    List<Integer> list = new ArrayList<>(Arrays.asList(arr));
		    List<List<Integer>> serialNumList = getSerialNumList(list);
		    serialNumList.forEach(System.out::println);
	}
}
