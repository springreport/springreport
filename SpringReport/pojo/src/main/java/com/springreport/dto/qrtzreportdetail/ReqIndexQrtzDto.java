package com.springreport.dto.qrtzreportdetail;

import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

import lombok.Data;

/**  
 * @ClassName: ReqIndexQrtzDto
 * @Description: 首页定时任务用参数实体类
 * @author caiyang
 * @date 2024-12-10 05:48:19 
*/ 
@Data
public class ReqIndexQrtzDto extends PageEntity{

	/**  
	 * @Fields type : 类型 1全部任务 2我创建的
	 * @author caiyang
	 * @date 2024-12-10 05:49:00 
	 */  
	private int type = 1;
	
	/**  
	 * @Fields creator : 创建用户id
	 * @author caiyang
	 * @date 2024-12-10 06:18:50 
	 */  
	private Long creator;
	
	/**  
	 * @Fields delFlag : 删除标记
	 * @author caiyang
	 * @date 2024-12-10 06:29:54 
	 */  
	private Integer delFlag;
	
	 /**  
	 * @Fields merchantNo : 商户号
	 * @author caiyang
	 * @date 2024-12-10 06:41:25 
	 */  
	private String merchantNo;
}
