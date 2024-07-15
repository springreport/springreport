package com.springreport.dto.reportdatasource;

import com.springreport.base.BaseEntity;

import lombok.Data;

/**  
 * @ClassName: ApiTestResultDto
 * @Description: api接口测试返回结果用实体类
 * @author caiyang
 * @date 2023-01-11 08:39:50 
*/  
@Data
public class ApiTestResultDto extends BaseEntity{

	/**  
	 * @Fields apiResult : 接口返回结果
	 * @author caiyang
	 * @date 2023-01-11 08:41:18 
	 */  
	private String apiResult;
}
