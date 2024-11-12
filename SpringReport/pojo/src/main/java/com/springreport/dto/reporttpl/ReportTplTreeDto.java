package com.springreport.dto.reporttpl;

import com.springreport.entity.reporttpl.ReportTpl;

import lombok.Data;

@Data
public class ReportTplTreeDto extends ReportTplDto{

	/**  
	 * @Fields hasChildren : 是否有子节点
	 * @author caiyang
	 * @date 2024-11-05 02:58:48 
	 */  
	private boolean hasChildren = true;
	
	/**  
	 * @Fields icon : 图标
	 * @author caiyang
	 * @date 2024-11-05 03:15:37 
	 */  
	private String icon;
	
	/**  
	 * @Fields type : 类型 1文件夹 2文件
	 * @author caiyang
	 * @date 2024-11-05 07:45:09 
	 */  
	private String type = "1";
}
