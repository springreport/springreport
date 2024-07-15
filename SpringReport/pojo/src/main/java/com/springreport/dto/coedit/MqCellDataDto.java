package com.springreport.dto.coedit;

import java.util.List;

import com.springreport.entity.luckysheetcell.LuckysheetCell;

import lombok.Data;

@Data
public class MqCellDataDto {

	/**  
	 * @Fields keyName : 关键字
	 * @author caiyang
	 * @date 2023-09-30 01:44:37 
	 */  
	private String keyName;
	
	/**  
	 * @Fields v : 值
	 * @author caiyang
	 * @date 2023-09-30 01:44:46 
	 */  
	private LuckysheetCell luckysheetCell;
	
	/**  
	 * @Fields datas : 批量数据
	 * @author caiyang
	 * @date 2023-10-11 10:13:06 
	 */  
	private Object datas;
	
	private String index;
	
	private String listId;
	
	private String blockId;
}
