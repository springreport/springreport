package com.springreport.dto.sysmerchant;

import com.springreport.entity.sysmerchant.SysMerchant;

import lombok.Data;

@Data
public class SysMerchantDto extends SysMerchant{

	/**  
	 * @Fields userName : 用户名
	 * @author caiyang
	 * @date 2023-12-24 10:31:26 
	 */  
	private String userName;
	
	/**  
	 * @Fields password : 密码
	 * @author caiyang
	 * @date 2023-12-24 10:31:42 
	 */  
	private String password;
	
	/**  
	 * @Fields templateName : 权限模板名称
	 * @author caiyang
	 * @date 2023-12-24 10:31:56 
	 */  
	private String templateName;
}
