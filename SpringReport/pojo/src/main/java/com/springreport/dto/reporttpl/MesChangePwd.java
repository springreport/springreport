package com.springreport.dto.reporttpl;

import com.springreport.entity.reporttpl.ReportTpl;

import lombok.Data;

/**  
 * @ClassName: MesChangePwd
 * @Description: 修改密码用参数实体类
 * @author caiyang
 * @date 2022-07-05 05:55:47 
*/ 
@Data
public class MesChangePwd extends ReportTpl{

	/**  
	 * @Fields oldPwd : 旧密码
	 * @author caiyang
	 * @date 2022-07-05 05:56:14 
	 */  
	private String oldPwd;
}
