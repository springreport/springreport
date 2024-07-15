package com.springreport.qrtz;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.springreport.constants.StatusCode;
import com.springreport.exception.BizException;
import com.springreport.util.MessageUtil;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class QuartzTaskService {

  public static final String JOB_DATA_KEY = "jobData";
  public static final String JOB_HANDLER_CLASS_KEY = "jobHandlerClass";
  private final Scheduler scheduler;

  /**  
 * @MethodName: createJob
 * @Description: 创建定时任务
 * @author caiyang
 * @param task
 * @throws SchedulerException void
 * @date 2023-07-29 04:20:44 
 */ 
public void createJob(ScheduleTask task) throws SchedulerException {
    JobDetail jobDetail = JobBuilder.newJob().ofType(MessageJob.class)
        .withIdentity(task.getJobName(), task.getGroupName())
        .usingJobData(JOB_DATA_KEY, task.getJobData())
        .usingJobData(JOB_HANDLER_CLASS_KEY, task.getJobHandlerClass())
        .build();
    Trigger trigger;
    if (StrUtil.isNotBlank(task.getJobCron())) {
      trigger = TriggerBuilder.newTrigger().forJob(jobDetail)
          .withIdentity(task.getJobName() + "_trigger", task.getGroupName())
          .withSchedule(CronScheduleBuilder.cronSchedule(task.getJobCron())).build();
    } else {
      trigger = TriggerBuilder.newTrigger().forJob(jobDetail)
          .withIdentity(task.getJobName() + "_trigger", task.getGroupName())
          .startAt(task.getJobTime())
          .build();
    }
    scheduler.scheduleJob(jobDetail, trigger);
  }


 
  /**  
 * @MethodName: updateJob
 * @Description: 更新定时任务
 * @author caiyang
 * @param jobName
 * @param jobGroupName
 * @param jobTime void
 * @date 2023-07-29 04:23:09 
 */ 
@SneakyThrows
  public void updateJob(ScheduleTask task) {
	JobKey jobKey = new JobKey(task.getJobName(), task.getGroupName());
    //检查任务key是否存在
    if (scheduler.checkExists(jobKey)) {
    	removeTask(task);
    }
    createJob(task);
  }

  /**  
 * @MethodName: removeTask
 * @Description: 删除定时任务
 * @author caiyang
 * @param task void
 * @date 2023-07-29 04:28:57 
 */ 
@SneakyThrows
  public void removeTask(ScheduleTask task) {
	  scheduler.deleteJob(JobKey.jobKey(task.getJobName(), task.getGroupName()));
  }

  // 暂停一个job
  @SneakyThrows
  public void pauseJob(ScheduleTask task) {
	  if(checkExists(task))
	  {
		  scheduler.pauseJob(JobKey.jobKey(task.getJobName(), task.getGroupName()));
	  }
  }

  // 恢复一个job
  @SneakyThrows
  public void resumeJob(ScheduleTask task) {
	  if(checkExists(task))
	  {
		  scheduler.resumeJob(JobKey.jobKey(task.getJobName(), task.getGroupName()));
	  }
  }

  // 立即执行一个job
  public void runJobNow(ScheduleTask task) throws Exception {
	  if(!this.checkExists(task))
	  {
		  try {
			  this.createJob(task);
		} catch (Exception e) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.qrtz.create"));
		}
		  
	  }
	  scheduler.triggerJob(JobKey.jobKey(task.getJobName(), task.getGroupName()));
  }
  
  @SneakyThrows
  public Map<String, JSONObject> getJobStatus(String groupName){
	  Map<String, JSONObject> result = new HashMap<>();
	  GroupMatcher<JobKey> matcher =  GroupMatcher.groupEquals(groupName);
	  Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
	  for (JobKey jobKey : jobKeys) {
		  JSONObject jsonObject = new JSONObject();
		  String taskId = jobKey.getName().split("_")[0];
		  List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
		  Trigger.TriggerState triggerState = scheduler.getTriggerState(triggers.get(0).getKey());
		  jsonObject.put("jobStatus", triggerState.name());
		  if (triggers.get(0) instanceof CronTrigger)
		  {
			  CronTrigger cronTrigger = (CronTrigger) triggers.get(0);
			  jsonObject.put("previousFireTime",  cronTrigger.getPreviousFireTime());
			  jsonObject.put("nextFireTime", cronTrigger.getNextFireTime());
		  }
		  result.put(taskId, jsonObject);
	  }
	  return result;
  }
  
  /**  
 * @MethodName: checkExists
 * @Description: 校验任务是否存在
 * @author caiyang
 * @param task
 * @return boolean
 * @date 2023-07-30 10:34:01 
 */ 
@SneakyThrows
  public boolean checkExists(ScheduleTask task) {
	return scheduler.checkExists(JobKey.jobKey(task.getJobName(), task.getGroupName()));
  }

}

