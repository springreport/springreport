package com.springreport.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UrlUtils {
	/** 
     * 向url链接追加参数 
     * @param url 
     * @param params Map<String, String> 
     * @return 
     */  
    public static String appendParams(String url, Map<String, String> params){  
        if(StringUtil.isNullOrEmpty(url)){  
            return "";  
        }else if(StringUtil.isEmptyMap(params)){  
            return url.trim();  
        }else{  
            StringBuffer sb = new StringBuffer("");  
            Set<String> keys = params.keySet();  
            for (String key : keys) {  
                sb.append(key).append("=").append(params.get(key)).append("&");  
            }  
            sb.deleteCharAt(sb.length() - 1);  
              
            url = url.trim();  
            int length = url.length();  
            int index = url.indexOf("?");  
            if(index > -1){//url说明有问号  
                if((length - 1) == index){//url最后一个符号为？，如：http://wwww.baidu.com?  
                    url += sb.toString();  
                }else{//情况为：http://wwww.baidu.com?aa=11  
                    url += "&" + sb.toString();  
                }  
            }else{//url后面没有问号，如：http://wwww.baidu.com  
                url += "?" + sb.toString();  
            }  
            return url;  
        }  
    }  
      
    /** 
     * 向url链接追加参数(单个) 
     * @param url 
     * @param name String 
     * @param value String 
     * @return 
     */  
    public static String appendParam(String url, String name, String value){  
        if(StringUtil.isNullOrEmpty(url)){  
            return "";  
        }else if(StringUtil.isNullOrEmpty(name)){  
            return url.trim();  
        }else{  
            Map<String, String> params = new HashMap<String, String>();  
            params.put(name, value);  
            return appendParams(url, params);  
        }  
    }  
    
    /**
     * 解析完整URL中的请求参数
     * @param url 完整url 例: http://a.com?name=test&age=18
     * @return 参数Map
     */
    public static Map<String, String> getUrlParamMap(String url) {
        if (url == null || url.isEmpty() || !url.contains("?")) {
            return new HashMap<>();
        }
        // 截取?后面所有参数
        String queryStr = url.substring(url.indexOf("?") + 1);
        return parseQueryString(queryStr);
    }

    /**
     * 解码
     * @param str
     * @return
     */
    public static String urlDecode(String str){
        try {
            String strReturn= URLDecoder.decode(str, "UTF-8");
            return strReturn;
        } catch (UnsupportedEncodingException e) {
           System.out.println("urlDecode error:"+str+" info:"+e.toString());
        }
        return null;
    }

    /**
     * 编码
     * @param str
     * @return
     */
    public static String urlEncode(String str){
        try {
            String strReturn= URLEncoder.encode(str, "UTF-8");
            return strReturn;
        } catch (UnsupportedEncodingException e) {
            System.out.println("urlEncode error:"+str+" info:"+e.toString());
        }
        return null;
    }
    
    /**
     * 单独解析参数字符串
     * @param queryStr 参数串 例: name=test&age=18
     * @return 参数Map
     */
    public static Map<String, String> parseQueryString(String queryStr) {
        Map<String, String> paramMap = new HashMap<>(16);
        if (queryStr == null || queryStr.isEmpty()) {
            return paramMap;
        }
        String[] paramArr = queryStr.split("&");
        for (String param : paramArr) {
            if (!param.contains("=")) {
                continue;
            }
            String[] kv = param.split("=", 2);
            String key = decode(kv[0]);
            String value = kv.length > 1 ? decode(kv[1]) : "";
            paramMap.put(key, value);
        }
        return paramMap;
    }
    
    /**
     * URL解码 UTF-8
     */
    private static String decode(String str) {
        if (str == null) {
            return "";
        }
        try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return "";
    }


    /**
     * 根据key获取单个参数值
     * @param url 完整url
     * @param key 参数名
     * @return 参数值
     */
    public static String getParamValue(String url, String key) {
        Map<String, String> map = getUrlParamMap(url);
        return map.getOrDefault(key, "");
    }

    //字符串转字节
    public static byte[] stringTobyte(String str){
        return stringTobyte(str,"ISO-8859-1");
    }
    public static byte[] stringTobyte(String str,String charsetName){
        try {
            return str.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            System.out.println("stringTobyte error:"+str+" info:"+e.toString());
        }
        return null;
    }
    

}
