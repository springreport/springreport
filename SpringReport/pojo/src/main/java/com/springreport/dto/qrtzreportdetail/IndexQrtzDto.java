package com.springreport.dto.qrtzreportdetail;

import com.springreport.base.PageEntity;

import lombok.Data;

@Data
public class IndexQrtzDto extends PageEntity{

	/**  
	 * @Fields tplName : 报表名称
	 * @author caiyang
	 * @date 2024-12-10 05:55:25 
	 */  
	private String tplName;
	
	/**  
	 * @Fields id : 任务id
	 * @author caiyang
	 * @date 2024-12-10 05:56:19 
	 */  
	private Long id;
	
	/**  
	 * @Fields tplId : 报表id
	 * @author caiyang
	 * @date 2024-12-10 05:56:36 
	 */  
	private Long tplId;
	
	/**  
	 * @Fields jobName : 任务名称
	 * @author caiyang
	 * @date 2024-12-10 05:56:49 
	 */  
	private String jobName;
	
	/**  
	 * @Fields email : 发送邮件
	 * @author caiyang
	 * @date 2024-12-10 05:57:05 
	 */  
	private String email;
	
	/**  
	 * @Fields exportType : 导出类型 1excel 2pdf 3excel+pdf
	 * @author caiyang
	 * @date 2024-12-10 05:57:24 
	 */  
	private int exportType;
	
	/**  
	 * @Fields nextFireName : 下次执行时间
	 * @author caiyang
	 * @date 2024-12-10 06:01:37 
	 */  
	private Long nextFireTime;
	
	/**  
	 * @Fields triggerState : 状态
	 * @author caiyang
	 * @date 2024-12-10 06:02:06 
	 */  
	private String triggerState;
}
