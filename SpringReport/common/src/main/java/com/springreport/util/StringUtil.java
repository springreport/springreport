package com.springreport.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.springreport.constants.Constants;
import com.springreport.enums.LuckysheetFormatEnum;
import com.springreport.enums.SqlOrderEnum;
import com.springreport.enums.SystemDatasourceTypeEnum;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import dm.jdbc.driver.DmdbClob;

/**  
 * @ClassName: StringUtil
 * @Description: 工具类
 * @author caiyang
 * @date 2020-05-29 11:25:12 
*/  
public class StringUtil {
	
	/**
	 * 判断字符串是否为空，null,空字符串，空格字符串都是返回true
	 * 
	 * @param str
	 * @return 是空，返回true，否则false
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否不为空，null,空字符串，空格字符串都是返回false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return false;
		}
		return true;
	}
	
	/**  
	 * @Title: trim
	 * @Description: 去除字符串首尾空格
	 * @param str
	 * @return
	 * @author caiyang
	 * @date 2020-08-25 06:44:04 
	 */ 
	public static String trim(String str) {
		if(str == null)
		{
			return str;
		}
		if(str.trim().length() == 0)
		{
			return "";
		}
		return str.trim();
	}
	
    protected static final String COMMA = ",";
	
    protected static final String orderTemplete = " %s %s ";
	/**  
	 * @Title: formatOrderSql
	 * @Description: 排序格式化
	 * @param sorts
	 * @return
	 * @author caiyang
	 * @date 2020-05-29 11:23:02 
	 */ 
	public static String formatOrderSql(Map<String, String> sorts)
	{
		if (CollectionUtils.isEmpty(sorts)) {
            return StringUtils.EMPTY;
        }
		List<String> sortSql = new ArrayList<>(1);
		Iterator<Map.Entry<String, String>> it = sorts.entrySet().iterator();
		while (it.hasNext()) {
			 Map.Entry<String, String> entry = it.next();
			 String value = entry.getValue().toUpperCase().trim();
	         String column = entry.getKey();
	         if (StringUtils.isEmpty(column)) {
	             continue;
	         }
	         if (StringUtils.equals(SqlOrderEnum.ASC.getCode(), value)) {
	             sortSql.add(String.format(orderTemplete, column, "asc"));
	         }
	         if (StringUtils.equals(SqlOrderEnum.DESC.getCode(), value)) {
	             sortSql.add(String.format(orderTemplete, column, "desc"));
	         }
		}
		return StringUtils.join(sortSql, COMMA);
	}
	
	/**  
	 * @Title: getFileNameFromUrl
	 * @Description: 根据url获取文件名
	 * @param url
	 * @return
	 * @author caiyang
	 * @date 2020-07-02 06:55:05 
	 */ 
	public static String getFileNameFromUrl(String url)
	{
		return url.substring(url.lastIndexOf("/")+1);
	}
	
	 /**
	    *<p>Title: hexToRgb</p>
	    *<p>Description: hex转rgb</p>
	    * @author caiyang
	    * @param hex
	    * @return
	    */
	    public static int[] hexToRgb(String hex) {
	    	int[] color=new int[3];
			color[0]=Integer.parseInt(hex.substring(1, 3), 16);
			color[1]=Integer.parseInt(hex.substring(3, 5), 16);
			color[2]=Integer.parseInt(hex.substring(5, 7), 16);
			return color;
	    }
	    
	    public static int[] rgbStringToRgb(String rgb) {
	    	int[] color=new int[3];
	    	rgb = rgb.replace("rgb", "");
	    	rgb = rgb.replace("(", "").replace(")", "").replaceAll(" ", "");
	    	String[] colors = rgb.split(",");
	    	color[0]=Integer.parseInt(colors[0]);
			color[1]=Integer.parseInt(colors[1]);
			color[2]=Integer.parseInt(colors[2]);
	    	return color;
	    }
	    
	    public static String  rgb2Hex(int r,int g,int b){
	        return String.format("%02x%02x%02x", r,g,b);
	    }
	public static boolean isEmptyMap(Map map) {
		if(map == null)
		{
			return true;
		}
		return map.isEmpty();
	}
	
	public static File getFile(String url) throws Exception {
        //对本地文件命名
        String fileName = url.substring(url.lastIndexOf("."),url.length());
        File file = null;

        URL urlfile;
        InputStream inStream = null;
        OutputStream os = null;
        try {
            file = File.createTempFile("net_url", fileName);
            //下载
            urlfile = new URL(url);
            inStream = urlfile.openStream();
            os = new FileOutputStream(file);

            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != inStream) {
                    inStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }
	
	/**
	* 过滤非数字
	* @param str
	* @return
	 */
	public static String getNumeric(String str) {
		str = str.trim();
		String str2 = "";
		if(str != null && !"".equals(str)){
			for(int i = 0; i < str.length(); i++){
				if(str.charAt(i) >= 48 && str.charAt(i) <= 57){
					str2 += str.charAt(i);
				}
			}
		}
		return str2;
	}
	
	/**  
	 * @MethodName: isUrl
	 * @Description: 判断字符串是否是超链接
	 * @author caiyang
	 * @param url
	 * @return 
	 * @return Boolean
	 * @date 2023-01-06 10:10:45 
	 */  
	public static Boolean isUrl(String url)
	{
		HttpURLConnection connection = null;
	    try {
	    	connection = (HttpURLConnection) new URL(url).openConnection();
	    	connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(500);  // 0.5秒连接超时
            connection.setReadTimeout(500);     // 0.5秒读取超时
            connection.setInstanceFollowRedirects(false); // 跟随重定向
            int responseCode = connection.getResponseCode();
            return (responseCode >= 200 && responseCode < 400);
	    } catch (Exception e) {
	        return false;
	    }finally {
            if (connection != null) {
                connection.disconnect(); // 及时释放连接
            }
        }
	}
	
	 // 完整的URL正则表达式
    private static final String URL_REGEX = 
        "^((http|https|ftp)://)?([\\w-]+\\.)+[\\w-]+(:\\d+)?(/[\\w-./?%&=]*)?$";
    
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);
    
    public static boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }
	
	/**  
	 * @MethodName: isImgUrl
	 * @Description: TODO
	 * @author caiyang
	 * @param url 
	 * @return void
	 * @date 2023-01-06 10:48:14 
	 */ 
	public static boolean isImgUrl(String url) {
		boolean result = false;
//		boolean isUrl = isUrl(url);
		boolean isUrl = isValidUrl(url);
		if(isUrl)
		{
//			String contentType = getContentType(url);
			String contentType = getUrlExtensionSafe(url);
			if(StringUtil.isNotEmpty(contentType))
			{
				if(Constants.IMG_EXTENTIONS.contains(contentType))
				{
					result = true;
				}
			}
		}
		return result;
	}
	
	/**  
	 * @MethodName: getImagWandH
	 * @Description: 获取图片的高度和宽度
	 * @author caiyang
	 * @param fileUrl
	 * @return 
	 * @return JSONObject
	 * @date 2023-01-06 01:43:37 
	 */  
	public static JSONObject getImagWandH(String fileUrl){
		JSONObject result = new JSONObject();
		int width = 400;
		int height = 400;
		try {
			 URL url = new URL(fileUrl);
	         URLConnection connection = url.openConnection();
	         connection.setDoOutput(false);
	         BufferedImage image = ImageIO.read(connection.getInputStream());  
	         width = image .getWidth();      // 源图宽度
	         height = image .getHeight();    // 源图高度
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("width", width);
		result.put("height", height);
		return result;
	}
	/**  
	 * @MethodName: getContentType
	 * @Description: 获取文件类型
	 * @author caiyang
	 * @param fileUrl
	 * @return 
	 * @return String
	 * @date 2023-01-06 10:48:17 
	 */  
	public static String getContentType(String fileUrl) {
		String fileType = null;
		try {
			fileType = FileTypeUtil.getType(new URL(fileUrl).openStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
        if(null == fileType){
            return null;
        }
        return fileType;
	}
	private static final String BASE64_PREFIX="data:image/jpeg;base64,";
	public static String imgToBase64(String imgUrl){
		InputStream is = null;
		ByteArrayOutputStream outStream = null;
		try {
			if (!ObjectUtils.isEmpty(imgUrl)) {
				HttpResponse res = HttpRequest.get(imgUrl).execute();
				// 获取输入流
				is = res.bodyStream();
				outStream = new ByteArrayOutputStream();
				//创建一个Buffer字符串
				byte[] buffer = new byte[1024];
				//每次读取的字符串长度，如果为-1，代表全部读取完毕
				int len = 0;
				//使用输入流从buffer里把数据读取出来
				while ((len = is.read(buffer)) != -1) {
					//用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
					outStream.write(buffer, 0, len);
				}
				// 对字节数组Base64编码
				return BASE64_PREFIX + encode(outStream.toByteArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (outStream != null) {
					outStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String encode(byte[] image) {
		Base64Encoder decoder = new Base64Encoder();
		return replaceEnter(decoder.encode(image));
	}
	
	public static String replaceEnter(String str) {
		String reg = "[\n-\r]";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}
	
	/**  
	 * @MethodName: compare2MapDiff
	 * @Description: 比较两个map中的属性，获取发生变化的属性
	 * @author caiyang
	 * @param reportData 修改后的数据
	 * @param basicData 修改前的数据
	 * @return 
	 * @return Map<String,Object>
	 * @date 2023-01-29 06:22:26 
	 */  
	public static Map<String, String> compare2MapDiff(Map<String, Object> reportData,Map<String, Object> basicData){
		Map<String, String> result = null;
		if(reportData != null)
		{
			if(basicData == null)
			{
				basicData = new HashMap<>();
			}
			result = new HashMap<>();
			Map<String, Object> changeBefore = new HashMap<>();
			Map<String, Object> changeAfter = new HashMap<>();
			Set<String> set = reportData.keySet();
			for (String o : set) {
				Object value = reportData.get(o);
				Object basicValue = basicData.get(o);
				if(!String.valueOf(value).equals(String.valueOf(basicValue)))
				{
					changeBefore.put(o, basicValue);
					changeAfter.put(o, value);
				}
			}
			result.put("changeBefore", JSON.toJSONString(changeBefore, SerializerFeature.WriteMapNullValue));
			result.put("changeAfter", JSON.toJSONString(changeAfter, SerializerFeature.WriteMapNullValue));
		}
		return result;
	}
	
	public static Map<String, JSONObject> compare2MapDiff2(Map<String, Object> reportData,Map<String, Object> basicData){
		Map<String, JSONObject> result = null;
		if(reportData == null)
		{
			reportData = new HashMap<>();
		}
		if(basicData == null)
		{
			basicData = new HashMap<>();
		}
		if(!StringUtil.isEmptyMap(reportData))
		{
			result = new HashMap<>();
			JSONObject changeBefore = new JSONObject();
			JSONObject changeAfter = new JSONObject();
			Set<String> set = reportData.keySet();
			for (String o : set) {
				Object value = reportData.get(o);
				Object basicValue = basicData.get(o);
				if(!String.valueOf(value).equals(String.valueOf(basicValue)))
				{
					changeBefore.put(o, basicValue);
					changeAfter.put(o, value);
				}
			}
			result.put("changeBefore", changeBefore);
			result.put("changeAfter", changeAfter);
		}
		return result;
	}
	
	/**
     * 从MP复制过来的脚本解析方法
     */
    public static SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType) {
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }
    
    /**  
     * @MethodName: convertBase64ToByte
     * @Description: base64字符串转byte数组
     * @author caiyang
     * @param base64String
     * @return byte[]
     * @date 2023-07-03 10:31:56 
     */ 
    public static byte[] convertBase64ToByte(String base64String) {
    	byte[] bytes = Base64.getDecoder().decode(base64String.replaceFirst("data:image/png;base64,", ""));
        return bytes;
    }
    
    private final static Pattern p = Pattern.compile("\\s*|\t|\r|\n");
    
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    public static String getStringShow(String str,int limit){
        if(StringUtils.isEmpty(str)){
            return "";
        }else{
            return str.length()<=limit?str:str.substring(0,limit);
        }
    }
    public static String getStringShow(String str){
        return getStringShow(str,50);
    }
    
    public static String getCode() {
    	String code = String.valueOf((int)((Math.random() * 9 + 1) * Math.pow(10,5)));
        return code;
    }
    
    /**  
     * @MethodName: getSequenceNo
     * @Description: 获取序列号
     * @author caiyang
     * @param type
     * @param no
     * @param length
     * @return String
     * @date 2023-12-24 10:47:23 
     */ 
    public static String getSequenceNo(String type,long no,int length) {
    	String sequenceNo = String.format(type + "%0"+length+"d", no);
    	return sequenceNo;
    }
    
    public static boolean allSameCharacter(String str,String character) {
        if (str == null || str.length() == 0) {
            return false;
        }

        String replaced = str.replaceAll(character, "");
        return replaced.isEmpty(); // 如果替换后为空，则说明字符相同
    }
    
    /**  
     * @MethodName: countChineseCharaceters
     * @Description: 统计字符串中中文字符的数量
     * @author caiyang
     * @param Str
     * @return boolean
     * @date 2025-07-24 09:28:26 
     */ 
    public static int countChineseCharaceters(String str) {
    	int count = 0;
    	if(StringUtil.isNullOrEmpty(str)) {
    		return 0;
    	}
    	char[] c = str.toCharArray();
        for(int i = 0; i < c.length; i ++)
        {
            String len = Integer.toBinaryString(c[i]);
            if(len.length() > 8)
                count ++;
        }
        return count;
    }
    
    public static String getUrlExtensionSafe(String url) {
        if (url == null || url.isEmpty()) return "";
        String clean = url.split("[?#]")[0];
        String[] parts = clean.split("/");
        String last = parts.length > 0 ? parts[parts.length - 1] : "";
        int dot = last.lastIndexOf('.');
        return (dot > 0 && dot < last.length() - 1) ? 
               last.substring(dot + 1).toLowerCase() : "";
    }

	public static void main(String[] args) throws Exception {
//		 double number = 1;
//		 
//	        // 创建DecimalFormat对象，指定百分比格式
//	        DecimalFormat decimalFormat = new DecimalFormat("0.00%");
//	 
//	        // 格式化数字并输出结果
//	        String formattedNumber = decimalFormat.format(number);
//	        String value = "\n\n\ncdfadfad\n\n\n\n ";
//	        String [] split = value.split("\n");
	        System.out.println(getUrlExtensionSafe("https://www.springreport.vip//images/chart/bor2der1.png?t=1764759254279"));
	}
}