package com.springreport.dto.reporttpldataset;

import com.springreport.entity.reporttpldataset.ReportTplDataset;
import com.springreport.enums.YesNoEnum;

import lombok.Data;

@Data
public class ReportTplDatasetDto extends ReportTplDataset{

	/**  
	 * @Fields isMobile : 是否是手机端
	 * @author caiyang
	 * @date 2023-07-17 02:56:22 
	 */  
	private Integer isMobile = YesNoEnum.NO.getCode();
	
	private boolean initSelectData = false;
	
	/**  
	 * @Fields reportType : 类型 1excel 2doc 3 onlyoffice
	 * @author caiyang
	 * @date 2024-05-09 11:16:15 
	 */  
	private Integer reportType = 1;
}
