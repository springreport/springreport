package com.springreport.dto.doctpl;

import java.util.List;

import com.springreport.entity.doctplcharts.DocTplCharts;
import com.springreport.entity.doctplcodes.DocTplCodes;
import com.springreport.entity.doctplsettings.DocTplSettings;

import lombok.Data;

@Data
public class DocTplSettingsDto extends DocTplSettings{

	/**  
	 * @Fields tplName : 模板名称
	 * @author caiyang
	 * @date 2024-05-10 07:38:13 
	 */  
	private String tplName;
	
	/**  
	 * @Fields chartUrlPrefix : 图表图片的前缀
	 * @author caiyang
	 * @date 2024-08-08 09:05:25 
	 */  
	private String chartUrlPrefix;
	
	/**  
	 * @Fields docTplCharts : 模板中的图表
	 * @author caiyang
	 * @date 2024-08-09 09:31:44 
	 */  
	private List<DocTplCharts> docTplCharts;
	
	/**  
	 * @Fields docTplCodes : 模板中的二维码和条形码
	 * @author caiyang
	 * @date 2024-10-23 10:05:38 
	 */  
	private List<DocTplCodes> docTplCodes;
}
