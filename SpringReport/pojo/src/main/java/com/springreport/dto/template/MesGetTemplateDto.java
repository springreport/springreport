package com.springreport.dto.template;

import com.springreport.base.PageEntity;

import lombok.Data;

/**  
 * @ClassName: MesGetTemplateDto
 * @Description: 获取模板请求参数实体类
 * @author caiyang
 * @date 2025-04-01 01:33:46 
*/ 
@Data
public class MesGetTemplateDto extends PageEntity{

	/**  
	 * @Fields isTemplate : 是否模板 1是 2否
	 * @author caiyang
	 * @date 2025-04-01 01:34:02 
	 */  
	private int isTemplate = 1;
	
	/**  
	 * @Fields merchantNo : 商户号
	 * @author caiyang
	 * @date 2025-04-01 01:34:12 
	 */  
	private String merchantNo; 
	
	/**  
	 * @Fields temType : 模板类型
	 * @author caiyang
	 * @date 2025-04-01 01:50:52 
	 */  
	private String temType;
	
	/**  
	 * @Fields tplName : 模板名称
	 * @author caiyang
	 * @date 2025-04-01 01:51:11 
	 */  
	private String tplName;
	
	/**  
	 * @Fields templateField : 分类
	 * @author caiyang
	 * @date 2025-04-01 01:51:35 
	 */  
	private Long templateField;
}
