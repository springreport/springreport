package com.springreport.dto.reporttpl;

import java.util.List;

import lombok.Data;

@Data
public class MesLuckysheetsTplDto {

	/**  
	 * @Fields id : 模板id
	 * @author caiyang
	 * @date 2021-05-24 06:14:32 
	 */ 
	private Long tplId;
	
	/**  
	 * @Fields isParamMerge : 是否合并参数
	 * @author caiyang
	 * @date 2022-03-31 01:49:07 
	 */ 
	private Integer isParamMerge;
	
	/**  
	 * @Fields configs : sheet配置信息
	 * @author caiyang
	 * @date 2022-10-18 06:04:38 
	 */  
	private List<MesLuckySheetTplDto> configs;
	
	/**  
	 * @Fields delSheetsIndex : 删除的sheet页
	 * @author caiyang
	 * @date 2022-10-21 08:41:52 
	 */  
	private List<String> delSheetsIndex;
}
