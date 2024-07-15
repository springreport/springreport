package com.springreport.dto.sysrolereport;

import com.springreport.base.PageEntity;

import lombok.Data;

/**  
 * @ClassName: MesRoleReportDto
 * @Description: 获取角色报表用参数实体类
 * @author caiyang
 * @date 2021-06-22 06:19:08 
*/  
@Data
public class MesRoleReportDto extends PageEntity{

	/**  
	 * @Fields isAdmin : 是否超级管理员
	 * @author caiyang
	 * @date 2021-06-22 06:19:11 
	 */ 
	private Integer isAdmin;
	
	/**  
	 * @Fields roleId : 角色id
	 * @author caiyang
	 * @date 2021-06-22 06:19:35 
	 */ 
	private Long roleId;
	
	/**  
	 * @Fields reportType : 报表类型id
	 * @author caiyang
	 * @date 2021-06-22 06:20:49 
	 */ 
	private Long reportType;
	
	/**  
	 * @Fields tplName : 报表名称
	 * @author caiyang
	 * @date 2022-07-06 08:37:57 
	 */  
	private String tplName;
	
	 /** merchant_no - 商户号 */
    private String merchantNo;
}
