package com.springreport.dto.reporttpl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.springreport.excel2pdf.ImageInfo;

import lombok.Data;

@Data
public class ResMobileReport {
	
	/**  
	 * @Fields sheetIndex : sheet标识
	 * @author caiyang
	 * @date 2023-06-29 09:31:22 
	 */  
	private String sheetIndex;
	
	/**  
	 * @Fields sheetName : sheet名称
	 * @author caiyang
	 * @date 2023-06-29 09:31:53 
	 */  
	private String sheetName;
	
	/**  
	 * @Fields table : 表格内容，html格式的字符串
	 * @author caiyang
	 * @date 2023-06-29 09:32:29 
	 */  
	private String table;
	
	/**  
	 * @Fields imageInfos : 图片信息
	 * @author caiyang
	 * @date 2023-06-29 09:33:25 
	 */  
//	private List<ImageInfo> imageInfos;
	private List<ImageInfo> imageInfos;
	
	/**  
	 * @Fields chartsOptions : 图表信息
	 * @author caiyang
	 * @date 2023-07-10 09:19:20 
	 */  
	private List<JSONObject> chartsOptions;
	
	/**  
	 * @Fields images : 单元格网络图片信息
	 * @author caiyang
	 * @date 2023-07-23 09:11:04 
	 */  
	private Map<String, String> images;
	
	/**  
	 * @Fields drillCells : 下钻单元格
	 * @author caiyang
	 * @date 2023-05-09 10:58:34 
	 */  
	private JSONObject drillCells;
	
	/**  
	 * @Fields mergePagination : 合并参数分页内容
	 * @author caiyang
	 * @date 2022-03-31 10:44:16 
	 */ 
	private Map<String, Object> mergePagination;
	
}
