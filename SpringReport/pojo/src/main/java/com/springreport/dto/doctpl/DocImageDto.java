package com.springreport.dto.doctpl;

import lombok.Data;

/**  
 * @ClassName: DocImageDto
 * @Description: word文档图片用实体类
 * @author caiyang
 * @date 2024-10-02 09:17:59 
*/ 
@Data
public class DocImageDto {

	/**  
	 * @Fields type : 类型
	 * @author caiyang
	 * @date 2024-09-29 08:53:27 
	 */  
	private String type = "image";
	/**  
	 * @Fields value : 内容
	 * @author caiyang
	 * @date 2024-09-24 10:20:12 
	 */  
	private String value = "";
	
	/**  
	 * @Fields width : 图片宽度
	 * @author caiyang
	 * @date 2024-10-02 09:22:33 
	 */  
	private double width;
	
	/**  
	 * @Fields height : 图片高度
	 * @author caiyang
	 * @date 2024-10-02 09:22:40 
	 */  
	private double height;
	
	/**  
	 * @Fields rowFlex : 水平对齐方式，默认left
	 * @author caiyang
	 * @date 2024-09-27 09:12:18 
	 */  
	private String rowFlex = "left";
}
