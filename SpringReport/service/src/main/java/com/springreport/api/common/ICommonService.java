package com.springreport.api.common;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.dto.common.ApiRequestDto;
import com.springreport.dto.common.PrintApiRequestDto;

/**  
 * @ClassName: ICommonService
 * @Description: 共通服务
 * @author caiyang
 * @date 2021-07-13 06:45:57 
*/  
public interface ICommonService {

	/**  
	 * @Title: upload
	 * @Description: 上传文件
	 * @param file
	 * @return
	 * @author caiyang
	 * @throws IOException 
	 * @date 2021-07-28 07:21:26 
	 */ 
	Object upload(MultipartFile file) throws IOException;
	
	/**  
	 * @MethodName: upload
	 * @Description: 字节流上传图片
	 * @author caiyang
	 * @param bytes
	 * @param fileName
	 * @return Object
	 * @date 2023-11-11 10:25:01 
	 */ 
	Map<String, String> upload(byte[] bytes,String fileName);
	
	/**  
	 * @Title: upload
	 * @Description: 上传文件
	 * @param file
	 * @return
	 * @author caiyang
	 * @throws IOException 
	 * @date 2021-07-28 07:21:26 
	 */ 
	Object uploadFile(MultipartFile file) throws IOException;
	
	/**  
	 * @Title: apiTest
	 * @Description: 接口测试
	 * @param apiRequestDto
	 * @return
	 * @author caiyang
	 * @date 2021-07-13 06:47:16 
	 */ 
	Object apiTest(ApiRequestDto apiRequestDto);
	
	/**  
	 * @MethodName: parseXlsxByUrl
	 * @Description: 通过url解析xlsx文件
	 * @author caiyang
	 * @param model
	 * @return JSONArray
	 * @throws Exception 
	 * @date 2024-09-18 10:55:11 
	 */ 
	JSONArray parseXlsxByUrl(JSONObject model) throws Exception;
	
}
