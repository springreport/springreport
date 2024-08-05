package com.springreport.dto.screentpl;

import java.util.List;
import java.util.Map;

import com.springreport.entity.screentpl.ScreenTpl;

import lombok.Data;

@Data
public class SaveScreenTplDto extends ScreenTpl{

	/**  
	 * @Fields components : 组件
	 * @author caiyang
	 * @date 2021-08-03 07:03:22 
	 */ 
	private List<Map<String, Object>> components;
}
