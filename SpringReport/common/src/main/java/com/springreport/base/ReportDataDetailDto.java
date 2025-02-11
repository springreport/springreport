package com.springreport.base;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

/**  
 * @ClassName: ReportDataDetailDto
 * @Description: 上报数据组装实体类
 * @author caiyang
 * @date 2022-11-23 07:28:35 
*/  
@Data
public class ReportDataDetailDto {

	/**  
	 * @Fields keys : 主键
	 * @author caiyang
	 * @date 2022-11-23 07:55:59 
	 */  
	private List<ReportDataColumnDto> keys;
	
	/**  
	 * @Fields columns : 普通列
	 * @author caiyang
	 * @date 2022-11-23 07:56:16 
	 */  
	private List<ReportDataColumnDto> columns;
	
	/**  
	 * @Fields autoFillAttrs : 自动填充列
	 * @author caiyang
	 * @date 2025-02-05 09:24:36 
	 */  
	private JSONObject autoFillAttrs;
}
