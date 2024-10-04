package com.springreport.dto.doctpl;

import java.util.List;

import lombok.Data;

@Data
public class DocTableRowDto {

	/**  
	 * @Fields height : 行高
	 * @author caiyang
	 * @date 2024-09-26 09:45:30 
	 */  
	private Integer height;
	
	/**  
	 * @Fields minHeight : 行最小高度
	 * @author caiyang
	 * @date 2024-09-26 09:47:28 
	 */  
	private int minHeight = 42;
	
	/**  
	 * @Fields tdList : 行数据
	 * @author caiyang
	 * @date 2024-09-26 09:46:02 
	 */  
	private List<DocTableCellDto> tdList;
}
