package com.springreport.util;


import lombok.extern.slf4j.Slf4j;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.springreport.constants.StatusCode;
import com.springreport.exception.BizException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @Author zhangcheng
 * @Version  1.0
 * @Description
    封装了一些采用HttpClient发送HTTP请求的方法
 * @Return
 * @Exception
 * @Date 2020-6-4 14:34
 */
@Slf4j
public class HttpClientUtil {

	private static int TIME_OUT = 10000;

    /**
     * Http Get方法
     *
     * @param url
     * @param param
     * @return
     */
    public static String doGet(String url, Map<String, Object> param) {
    	log.info("调用第三方接口开始，接口地址："+url+",调用参数："+JSONObject.toJSONString(param));
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String result = "";
        CloseableHttpResponse response = null;
        String builderUrl="";
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, String.valueOf(param.get(key)));
                }
            }
            URI uri = builder.build();
            builderUrl = uri.toString();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
            	result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
        	log.error("调用第三方接口出现异常，接口地址："+url+",调用参数："+JSONObject.toJSONString(param)+"，参数拼接后地址："+builderUrl+"，异常信息："+e);
        	throw new BizException(StatusCode.FAILURE, "调用第三方接口出现异常，接口地址："+url+",调用参数："+JSONObject.toJSONString(param)+"，参数拼接后地址："+builderUrl+"，异常信息："+e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                log.error("系统错误:",e);
            }
        }
        log.info("调用第三方接口技术，接口地址：" + url + ",调用参数：" + JSONObject.toJSONString(param)+"，返回结果：" + result);
        return result;
    }

    /**
     * Http Get方法
     *
     * @param url
     * @param param
     * @return
     */
    public static String doGet(String url,Map<String, String> headMap,Map<String, Object> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, String.valueOf(param.get(key)));
                }
            }

            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            if (headMap != null && !headMap.isEmpty()) {
                for (String key : headMap.keySet()) {
                    log.info("头部信息key:" + key + "===值: " + headMap.get(key));
                    httpGet.addHeader(key, headMap.get(key));
                }
            }

            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
//            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
//            }
        } catch (Exception e) {
            log.error("系统错误:",e);
            JSONObject resultObj = new JSONObject();
            resultObj.put("errCode", StatusCode.FAILURE);
            resultObj.put("errMsg", e.getMessage());
            resultString = JSON.toJSONString(resultObj);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                log.error("系统错误:",e);
                JSONObject resultObj = new JSONObject();
                resultObj.put("errCode", StatusCode.FAILURE);
                resultObj.put("errMsg", e.getMessage());
                resultString = JSON.toJSONString(resultObj);
            }
        }
        return resultString;
    }

    public static String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * httpclient post方法
     *
     * @param url
     * @param param
     * @return
     */
    public static String doPost(String url,Map<String, String> headers,Map<String, Object> param) {
        if(param == null) {
        	param = new HashMap();
        }
    	// 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
        	log.info("调用第三方接口开始，接口地址："+url+",调用参数："+JSONObject.toJSONString(param)+",调用header："+JSONObject.toJSONString(headers));
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            if(headers != null) {
                for (String key : headers.keySet()) {
                    httpPost.setHeader(key, headers.get(key));
                }
            }
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, String.valueOf(param.get(key))));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
        	log.error("调用第三方接口出现异常，接口地址："+url+",调用参数："+JSONObject.toJSONString(param)+"，异常信息："+e);
        	throw new BizException(StatusCode.FAILURE, "调用第三方接口出现异常，接口地址："+url+",调用参数："+JSONObject.toJSONString(param)+"，异常信息："+e.getCause());
        } finally {
            try {
                if (response!=null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("系统错误:",e);
            }
        }
        log.info("调用第三方接口技术，接口地址：" + url + ",调用参数：" + JSONObject.toJSONString(param)+"，返回结果：" + resultString);
        return resultString;
    }

    public static String doPost(String url) {
        return doPost(url,null,null);
    }

    /**
     * 请求的参数类型为json
     *
     * @param url
     * @param json
     * @return {username:"",pass:""}
     */
    public static String doPostJson(String url, String json) {

        log.info("=====请求地址:"+url);
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(3000).setConnectionRequestTimeout(3000)
                    .setSocketTimeout(3000).build();
            httpPost.setConfig(requestConfig);
            // 创建请求内容
            log.info("=====请求参数:"+json);
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            log.info("=====响应参数:"+response);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            log.error("系统错误:",e);
        } finally {
            try {
                if (response!=null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("系统错误:",e);
            }
        }
        return resultString;
    }
    
    public static String doPostJson(String url, String json,Map<String, String> headers) {

        log.info("=====请求地址:"+url);
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            if(headers != null) {
                for (String key : headers.keySet()) {
                    httpPost.setHeader(key, headers.get(key));
                }
            }
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(3000).setConnectionRequestTimeout(3000)
                    .setSocketTimeout(3000).build();
            httpPost.setConfig(requestConfig);
            // 创建请求内容
            log.info("=====请求参数:"+json);
            if(StringUtil.isNullOrEmpty(json)) {
            	json = "{}";
            }
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            log.info("=====响应参数:"+response);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            log.error("系统错误:",e);
            JSONObject resultObj = new JSONObject();
            resultObj.put("errCode", StatusCode.FAILURE);
            resultObj.put("errMsg", e.getMessage());
            resultString = JSON.toJSONString(resultObj);
        } finally {
            try {
                if (response!=null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("系统错误:",e);
                JSONObject resultObj = new JSONObject();
                resultObj.put("errCode", StatusCode.FAILURE);
                resultObj.put("errMsg", e.getMessage());
                resultString = JSON.toJSONString(resultObj);
            }
        }
        return resultString;
    }

    /**
     * 发送HTTP_POST请求,json格式数据
     *
     * @param url
     * @param body
     * @return
     * @throws Exception
     */
    public static String sendPostByJson(String url, String body) throws Exception {
        CloseableHttpClient httpclient = HttpClients.custom().build();
        HttpPost post = null;
        String resData = null;
        CloseableHttpResponse result = null;
        try {
            post = new HttpPost(url);
            HttpEntity entity2 = new StringEntity(body, Consts.UTF_8);
            post.setConfig(RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build());
            post.setHeader("Content-Type", "application/json");
            post.setEntity(entity2);
            result = httpclient.execute(post);
            if (HttpStatus.SC_OK == result.getStatusLine().getStatusCode()) {
                resData = EntityUtils.toString(result.getEntity());
            }
        } finally {
            if (result != null) {
                result.close();
            }
            if (post != null) {
                post.releaseConnection();
            }
            httpclient.close();
        }
        return resData;
    }

    /**
     * HttpPost发送header,Content(json格式)
     *
     * @param url
     * @param json
     * @param headers
     * @return
     */
    public static String post(String url, Map<String, String> headers, Map<String, String> jsonMap) {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        log.info("请求地址:" + url);
        // post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        log.info("请求头信息:" + headers);
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (Map.Entry<String, String> entrdy : headers.entrySet()) {
                post.addHeader(entrdy.getKey(), entrdy.getValue());
                //System.out.println("headers:" + entrdy.getKey() + ",值" + entrdy.getValue());
            }

        }
        String charset = null;
        try {
            StringEntity s = new StringEntity(jsonMap.toString(), "utf-8");
            log.info("请求json参数:" + jsonMap);
            // s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            // s.setContentType("application/json");
            // s.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(s);

            log.info("请求实体数据:" + post);
            // HttpResponse res = client.execute(post);
            HttpResponse httpResponse = client.execute(post);
            InputStream inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
            StringBuilder strber = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
                strber.append(line + "\n");
            inStream.close();
            log.info("MobilpriseActivity:" + strber);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                charset = EntityUtils.getContentCharSet(entity);
            }
        } catch (Exception e) {
            log.info("报错咯:" + e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("响应参数:" + charset);
        return charset;
    }
    
    /**  
     * @Title: postForm
     * @Description: postForm请求
     * @param url
     * @param parameters
     * @return
     * @author caiyang
     * @date 2020-06-28 02:42:27 
     */ 
    public static String postForm(String url, String parameters) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(TIME_OUT)
				.setConnectionRequestTimeout(TIME_OUT)
				.setSocketTimeout(TIME_OUT).build();
		httppost.setConfig(requestConfig);
		httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
		try {
			httppost.setEntity(new StringEntity(parameters, Charset.forName("UTF-8")));

			CloseableHttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity,"UTF-8");
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(StatusCode.FAILURE, e.getMessage());
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    /**  
     * @Title: connectionTest
     * @Description: get请求测试连接
     * @param url
     * @return
     * @author caiyang
     * @date 2022-04-24 01:57:40 
     */ 
    public static boolean getConnectionTest(String url) {
    	if(StringUtil.isNullOrEmpty(url))
    	{
    		 throw new BizException(StatusCode.FAILURE, "接口连接测试失败，失败原因：url不能为空。");
    	}
    	try {
            URL netUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) netUrl.openConnection();
            connection.setConnectTimeout(5000); //连接主机超时时间ms
            connection.setReadTimeout(5000); //从主机读取数据超时时间ms
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                return true;
            }else {
            	throw new BizException(StatusCode.FAILURE, "接口连接测试失败，返回状态码："+connection.getResponseCode()+"，返回信息："+connection.getResponseMessage());
            }
        } catch (Exception e) {
            throw new BizException(StatusCode.FAILURE, "接口连接测试失败，失败原因："+e.getMessage());
        }
    }
    
    /**  
     * @MethodName: connectionTest
     * @Description: api连接测试
     * @author caiyang
     * @param url
     * @param requestType
     * @return 
     * @return String
     * @date 2023-01-11 08:29:04 
     */  
    public static String connectionTest(String url,String requestType,Map<String, Object> params,Map<String, String> headers)
    {
    	String result = null;
    	if(params == null) {
    		params = new HashMap<>();
    	}
    	if(StringUtil.isNullOrEmpty(requestType) || "post".equals(requestType))
    	{//post请求
    		result = doPostJson(url,JSONObject.toJSONString(params),headers);
    	}else {
    		//get请求
//    		getConnectionTest(url);
    		result = doGet(url,headers,params);
    	}
    	return result;
    }
    
    public static void main(String[] args) {
        try {
//            Map<String,String> headmap =  new HashMap<String,String>();
//            headmap.put("Access-Token", "sund2f3bf3e7ecea902bcdb7027e9139a02");
//            Map<String,String> paramap =  new HashMap<String,String>();
//            paramap.put("customerDeptId", "38");
//            paramap.put("postCode", "qqqq");
//            System.out.println(doPost("http://10.39.137.100/api/needs/getInfoByCondition",headmap,paramap));
            //System.out.println(sendPostByJson("http://10.39.137.100/api/needs/getInfoByCondition","{\"customerDeptId\":38,\"postCode\":\"qqqq\"}"));
        } catch (Exception e) {
            log.error("系统错误:",e);
        }

    }

}