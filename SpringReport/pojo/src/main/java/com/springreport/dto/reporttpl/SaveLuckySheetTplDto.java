package com.springreport.dto.reporttpl;

import java.util.Map;

import com.springreport.base.BaseEntity;

import lombok.Data;

@Data
public class SaveLuckySheetTplDto extends BaseEntity{
	
	/**  
	 * @Fields printSettings : 打印设置id
	 * @author caiyang
	 * @date 2023-12-08 06:12:04 
	 */  
	private Map<String, Long> printSettings;
}
