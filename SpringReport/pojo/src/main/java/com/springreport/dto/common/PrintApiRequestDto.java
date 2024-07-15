package com.springreport.dto.common;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**  
 * @ClassName: ApiRequestDto
 * @Description: api接口请求用实体类
 * @author caiyang
 * @date 2021-07-13 06:39:11 
*/  
@Data
public class PrintApiRequestDto {

	/**  
	 * @Fields url : url
	 * @author caiyang
	 * @date 2021-07-13 06:39:29 
	 */ 
	private String url;
	
	/**  
	 * @Fields requestType : 请求方式 post get 默认post
	 * @author caiyang
	 * @date 2021-07-13 06:39:48 
	 */ 
	private String requestType = "post";
	
	/**  
	 * @Fields params : 参数
	 * @author caiyang
	 * @date 2021-07-13 06:40:35 
	 */ 
	private List<Map<String, Object>> params;
	
	/**  
	 * @Fields resultType : 返回值类型 String Array Object  默认String
	 * @author caiyang
	 * @date 2021-07-13 06:40:59 
	 */ 
	private String resultType = "String";
	
	/**  
	 * @Fields resultTypeProp : 返回值属性，当resultType = Object时需要用到该参数
	 * @author caiyang
	 * @date 2021-07-13 06:41:42 
	 */ 
	private String resultTypeProp;
	
	/**  
	 * @Fields mapParams : 参数
	 * @author caiyang
	 * @date 2021-11-24 08:53:24 
	 */ 
	private Map<String,Object> mapParams;
}
