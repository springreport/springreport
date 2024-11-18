package com.springreport.dto.doctpl;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DocTableCellDto {

	/**  
	 * @Fields colspan : 合并列数
	 * @author caiyang
	 * @date 2024-09-26 09:37:27 
	 */  
	private int colspan;
	
	/**  
	 * @Fields rowspan : 合并行数
	 * @author caiyang
	 * @date 2024-09-26 09:38:22 
	 */  
	private int rowspan;
	
	/**  
	 * @Fields value : 单元格数据
	 * @author caiyang
	 * @date 2024-09-26 09:42:14 
	 */  
	private List<Object> value = new ArrayList<>();
	
	/**  
	 * @Fields verticalAlign : 垂直对齐方式，默认top
	 * @author caiyang
	 * @date 2024-09-27 09:16:56 
	 */  
	private String verticalAlign = "top";
	
	/**  
	 * @Fields backgroundColor : 背景颜色
	 * @author caiyang
	 * @date 2024-11-18 09:56:28 
	 */  
	private String backgroundColor;
}
