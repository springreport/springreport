package com.springreport.util;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.constants.Constants;
import com.springreport.enums.ParamTypeEnum;

/**  
 * @ClassName: CellUtil
 * @Description: 单元格工具类
 * @author caiyang
 * @date 2021-06-09 06:13:14 
*/  
public class ParamUtil {
	/**
	*<p>Title: getViewParams</p>
	*<p>Description: 获取页面的参数</p>
	* @author caiyang
	* @param jsonArray
	* @return
	 * @throws ParseException 
	*/
	public static Map<String, Object> getViewParams(JSONArray jsonArray) throws ParseException
	{
		Map<String, Object> result = new HashMap<String, Object>();
		if(jsonArray != null)
		{
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject param = jsonArray.getJSONObject(i);
				boolean isDefault = param.getBooleanValue("isDefault");
				String key = param.getString("paramCode");
				if(isDefault)
				{
					String paramType = param.getString("paramType");
					if(ParamTypeEnum.VARCHAR.getCode().equals(paramType) || ParamTypeEnum.NUMBER.getCode().equals(paramType)
						|| ParamTypeEnum.SELECT.getCode().equals(paramType))
					{
						Object value = param.get("paramDefault");
						result.put(key, value);
					}else if(ParamTypeEnum.MUTISELECT.getCode().equals(paramType)) {
						JSONArray value = param.getJSONArray("paramDefault");
						result.put(key, value);
					}else if(ParamTypeEnum.DATE.getCode().equals(paramType)) {
						String dateFormat = param.getString("dateFormat");
						String value = param.getString("dateDefaultValue");
						if(Constants.CURRENT_DATE.equals(StringUtil.trim(value).toLowerCase()))
						{
							String currentDate = DateUtil.getNow(StringUtil.isNotEmpty(dateFormat)?dateFormat:DateUtil.FORMAT_LONOGRAM);
							result.put(key, currentDate);
						}else {
							if(CheckUtil.isNumber(value))
							{
								int days = Double.valueOf(value).intValue();
								if(DateUtil.FORMAT_YEARMONTH.equals(dateFormat))
								{
									String date = DateUtil.addMonth(days, DateUtil.getNow(DateUtil.FORMAT_LONOGRAM),DateUtil.FORMAT_YEARMONTH);
									result.put(key, date);
								}else {
									String date = DateUtil.addDays(days, DateUtil.getNow(),StringUtil.isNotEmpty(dateFormat)?dateFormat:DateUtil.FORMAT_LONOGRAM);
									result.put(key, date);
								}
							}else {
								result.put(key, "");
							}
						}
					}
				}else {
					Object value = param.get(key);
					result.put(key, value);
				}
				
			}
		}
		return result;
	}
}
