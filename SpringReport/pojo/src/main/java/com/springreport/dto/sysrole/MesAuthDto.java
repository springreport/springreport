package com.springreport.dto.sysrole;

import java.util.List;


import lombok.Data;

/**  
 * @ClassName: MesAuthDto
 * @Description: 授权用参数实体类
 * @author caiyang
 * @date 2021-06-16 06:57:52 
*/  
@Data
public class MesAuthDto {

	/**  
	 * @Fields roleId : 角色id
	 * @author caiyang
	 * @date 2021-06-16 06:58:24 
	 */ 
	private Long roleId;
	
	 /** merchant_no - 商户号 */
    private String merchantNo;
	
	/**  
	 * @Fields authed : 授权的菜单和接口
	 * @author caiyang
	 * @date 2021-06-16 06:58:44 
	 */ 
	private List<String> authed;
}
