package com.springreport.dto.reporttpl;

import com.springreport.base.BaseEntity;

import lombok.Data;

/**  
 * @ClassName: ResReportDataDto
 * @Description: 上报数据返回结果用实体类
 * @author caiyang
 * @date 2023-12-15 11:41:10 
*/ 
@Data
public class ResReportDataDto extends BaseEntity{

	/**  
	 * @Fields version : 版本号
	 * @author caiyang
	 * @date 2023-12-15 11:41:26 
	 */  
	private Long version;
}
