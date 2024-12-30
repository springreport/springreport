package com.springreport.dto.index;

import com.springreport.base.BaseEntity;

import lombok.Data;

/**  
 * @ClassName: IndexDto
 * @Description: 首页用实体类
 * @author caiyang
 * @date 2024-12-08 05:59:24 
*/ 
@Data
public class IndexDto extends BaseEntity{

	/**  
	 * @Fields excelCount : excel报表数量
	 * @author caiyang
	 * @date 2024-12-08 05:59:48 
	 */  
	private long excelCount;
	
	/**  
	 * @Fields wordCount : word报表数量
	 * @author caiyang
	 * @date 2024-12-08 06:00:11 
	 */  
	private long wordCount;
	
	/**  
	 * @Fields coeditCount : 协同文档数量
	 * @author caiyang
	 * @date 2024-12-08 06:41:52 
	 */  
	private long coeditCount;
	
	/**  
	 * @Fields screenCount : 大屏数量
	 * @author caiyang
	 * @date 2024-12-08 06:42:12 
	 */  
	private long screenCount;
	
	/**  
	 * @Fields datasourceCount : 数据源数量
	 * @author caiyang
	 * @date 2024-12-08 06:42:39 
	 */  
	private long datasourceCount;
}
