package com.springreport.dto.doctpl;

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
}
