package com.springreport.dto.sysmerchantauthtemplate;

import java.util.List;

import com.springreport.entity.sysmerchantauthtemplate.SysMerchantAuthTemplate;

import lombok.Data;

@Data
public class SysMerchantAuthTemplateDto extends SysMerchantAuthTemplate{

	/**  
	 * @Fields authed : 授权的菜单和接口
	 * @author caiyang
	 * @date 2021-06-16 06:58:44 
	 */ 
	private List<String> authed;
}
