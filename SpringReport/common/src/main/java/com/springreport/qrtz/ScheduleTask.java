package com.springreport.qrtz;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleTask {

	 // 任务名
	  private String jobName;

	  // 任务组
	  private String groupName;

	  // 任务数据
	  private String jobData;

	  // 任务执行处理类，小写字母开头
	  private String jobHandlerClass;

	  // 任务执行时间
	  private Date jobTime;

	  // 任务执行时间，cron时间表达式 （如：0/5 * * * * ? ）
	  private String jobCron;

	  // 任务执行次数，（<0:表示不限次数）
	  private int jobTimes;
}
