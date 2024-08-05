package com.springreport.dto.screentpl;

import java.util.List;

import com.springreport.entity.screencontent.ScreenContent;
import com.springreport.entity.screentpl.ScreenTpl;

import lombok.Data;

@Data
public class ScreenTplDto extends ScreenTpl{

	/**  
	 * @Fields components : 页面组件
	 * @author caiyang
	 * @date 2021-08-02 11:29:09 
	 */ 
	private List<ScreenContent> components;
}
