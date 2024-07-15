package com.springreport.dto.reporttpl;

import lombok.Data;

/**  
 * @ClassName: AlternateformatDto
 * @Description: 交替颜色配置实体类
 * @author caiyang
 * @date 2023-03-13 04:57:17 
*/  
@Data
public class AlternateformatDto {

	/**  
	 * @Fields fcOdd : 奇数行字体颜色
	 * @author caiyang
	 * @date 2023-03-13 04:57:47 
	 */  
	private String fcOdd;
	
	/**  
	 * @Fields fcEven : 偶数行字体颜色
	 * @author caiyang
	 * @date 2023-03-13 04:58:07 
	 */  
	private String fcEven;
	
	/**  
	 * @Fields bcOdd : 奇数行背景颜色
	 * @author caiyang
	 * @date 2023-03-13 04:58:21 
	 */  
	private String bcOdd;
	
	/**  
	 * @Fields bcEven : 偶数行背景色
	 * @author caiyang
	 * @date 2023-03-13 04:58:39 
	 */  
	private String bcEven;
}
