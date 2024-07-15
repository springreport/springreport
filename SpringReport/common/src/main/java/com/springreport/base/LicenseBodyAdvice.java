//package com.springreport.base;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.lang.reflect.Type;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpInputMessage;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.springreport.constants.Constants;
//import com.springreport.constants.StatusCode;
//import com.springreport.exception.BizException;
//import com.springreport.util.CusAccessObjectUtil;
//
///**
// * @author caiyang
// * 	拦截请求校验license
// */
//@ControllerAdvice(annotations = RequestMapping.class)
//public class LicenseBodyAdvice implements RequestBodyAdvice{
//	
//	@Autowired
//	protected HttpServletRequest httpServletRequest;
//
//	@Override
//	public boolean supports(MethodParameter methodParameter, Type targetType,
//			Class<? extends HttpMessageConverter<?>> converterType) {
//		return true;
//	}
//
//	@Override
//	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
//			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
//		String ip = CusAccessObjectUtil.getIpAddress(httpServletRequest);
//		String ip2 = this.getIP();
//		String ip3 = this.getIP2();
//		if(Constants.BLACK_LIST.contains(ip) || Constants.BLACK_LIST.contains(ip2) || Constants.BLACK_LIST.contains(ip3))
//		{
//			throw new BizException(StatusCode.FAILURE, "本人拥有著作权，未经授权不得使用！");
//		}
//		return inputMessage;
//	}
//
//	@Override
//	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
//			Class<? extends HttpMessageConverter<?>> converterType) {
//		return body;
//	}
//
//	@Override
//	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
//			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
//		return body;
//	}
//	
//	  private String getIP()  {
//	        String ip = "http://pv.sohu.com/cityjson?ie=utf-8";
//	        String inputLine = "";
//	        String read = "";
//	        String toIp="";
//	        try {
//	            URL url = new URL(ip);
//	            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//	            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//	            while ((read = in.readLine()) != null) {
//	                inputLine += read;
//	            }
//	            String ObjJson=inputLine.substring(inputLine.indexOf("=")+1,inputLine.length()-1);
//	            JSONObject jsonObj= JSON.parseObject(ObjJson);
//	            toIp=jsonObj.getString("cip");
//	        } catch (Exception e) {
//	            toIp="";
//	        }
//	        return toIp;
//	    }
//	  private String getIP2() throws IOException {
//	        String ip = null;
//	        String chinaZ = "http://ip.chinaz.com";
//	        String response = sendGet(chinaZ);
//	        //过滤出响应中外网IP
//	        ip = match(response, "\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
//	        if (ip == null) {
//	            String newUrl = match(response, "window.location.href=\"(http://ip.chinaz.com.+)\"");
//	            response = sendGet(newUrl);
//	            ip = match(response, "\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
//	        } 
//	        return ip;
//	    }
//	  
//	  /**
//	     * @Param str
//	     * @Param regex
//	     * @Description 正则过滤
//	     * @Date: 2021/5/11
//	     **/
//	    public static String match(String str, String regex) {
//	        Pattern p = Pattern.compile(regex);
//	        Matcher m = p.matcher(str);
//	        if (m.find()) {
//	            return m.group(1);
//	        }
//	        return null;
//	    }
//	    /**
//	     * @Param url
//	     * @Description 发送get请求
//	     * @Date: 2021/5/11
//	     **/
//	    public static String sendGet(String url) throws IOException {
//	        URL urlObject = new URL(url);
//	        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
//	        connection.connect();
//	        try (InputStream inputStream = connection.getInputStream();
//	             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//	            StringBuilder response = new StringBuilder();
//	            String str;
//	            while ((str = reader.readLine()) != null) {
//	                response.append(str);
//	            }
//	            return response.toString();
//	        } catch (Exception ex) {
//	            ex.printStackTrace();
//	        }
//	        return null;
//	    }
//
//}
