package com.springreport.dto.qrtzreportdetail;

import com.springreport.entity.qrtzreportdetail.QrtzReportDetail;

import lombok.Data;

@Data
public class QrtzReportDetailDto extends QrtzReportDetail{

	/**  
	 * @Fields jobStatus : 任务状态
	 * @author caiyang
	 * @date 2023-07-29 09:04:21 
	 */  
	private String jobStatus;
	
	/**  
	 * @Fields previousFireTime : 上次执行时间
	 * @author caiyang
	 * @date 2023-07-29 09:04:50 
	 */  
	private String previousFireTime;
	
	/**  
	 * @Fields nextFireTime : 下次执行时间
	 * @author caiyang
	 * @date 2023-07-29 09:05:24 
	 */  
	private String nextFireTime;
}
