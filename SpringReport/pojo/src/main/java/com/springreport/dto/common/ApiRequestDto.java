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
public class ApiRequestDto {

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
	private List<Map<String, String>> resultTypeProp;
	
	 /**  
	 * @Fields screenComponentId : 大屏组件id
	 * @author caiyang
	 * @date 2022-01-05 07:25:34 
	 */ 
	private Long screenComponentId;
	
	/**  
	 * @Fields urlParams : url中传过来的参数
	 * @author caiyang
	 * @date 2022-01-05 07:48:30 
	 */ 
	private Map<String, Object> urlParams;
	
	/**  
	 * @Fields resultTypePropPerfix : 返回值前缀
	 * @author caiyang
	 * @date 2022-07-14 09:15:33 
	 */  
	private String resultTypePropPerfix;
	
	/**  
	 * @Fields isBindComponent : 是否是绑定组件
	 * @author caiyang
	 * @date 2022-08-10 08:49:23 
	 */  
	private Integer isBindComponent;
	
	/**  
	 * @Fields drillProp : 下钻属性
	 * @author caiyang
	 * @date 2022-08-15 06:33:26 
	 */  
	private List<Map<String, String>> drillProp;
	
}
