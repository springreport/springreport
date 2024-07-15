package com.springreport.qrtz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class MessageJob implements Job {

  private ApplicationContext applicationContext;

  private String jobData;

  private String jobHandlerClass;

  @SneakyThrows
  @Override
  public void execute(JobExecutionContext context) {
	String jobName =  context.getJobDetail().getKey().getName();
    log.info("quartz job data: " + jobData + ", jobHandlerClass: " + jobHandlerClass);
    MessageHandler messageHandler = (MessageHandler) applicationContext.getBean(jobHandlerClass);
    Long taskId = Long.parseLong(jobName.split("_")[0]);
    messageHandler.handlerMessage(jobData,taskId);
  }
}

