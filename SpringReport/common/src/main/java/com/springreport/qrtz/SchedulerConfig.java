package com.springreport.qrtz;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;


@Configuration
public class SchedulerConfig {
	
  @Value("${org.quartz.jobStore.useProperties}")
  private String useProperties; 
  
  @Value("${org.quartz.scheduler.instanceName}")
  private String instanceName; 
  
  @Value("${org.quartz.scheduler.instanceId}")
  private String instanceId; 
  
  @Value("${org.quartz.scheduler.makeSchedulerThreadDaemon}")
  private String makeSchedulerThreadDaemon; 
  
  @Value("${org.quartz.jobStore.acquireTriggersWithinLock}")
  private String acquireTriggersWithinLock; 
  
  @Value("${org.quartz.threadPool.class}")
  private String threadPoolClass; 
  
  @Value("${org.quartz.threadPool.threadCount}")
  private String threadCount; 
  
  @Value("${org.quartz.threadPool.threadPriority}")
  private String threadPriority; 
  
  @Value("${org.quartz.jobStore.class}")
  private String jobStoreClass; 

  @Value("${org.quartz.jobStore.tablePrefix}")
  private String tablePrefix; 

  @Value("${org.quartz.jobStore.misfireThreshold}")
  private String misfireThreshold; 
  
  @Value("${org.quartz.jobStore.dataSource}")
  private String dataSource; 
  
  @Value("${org.quartz.jobStore.isClustered}")
  private String isClustered; 
  
  @Value("${org.quartz.jobStore.clusterCheckinInterval}")
  private String clusterCheckinInterval; 
  
  @Value("${org.quartz.jobStore.maxMisfiresToHandleAtATime}")
  private String maxMisfiresToHandleAtATime; 
  
  @Value("${org.quartz.dataSource.myDS.provider}")
  private String provider;
  
  @Value("${org.quartz.jobStore.driverDelegateClass}")
  private String driverDelegateClass;
  
  @Value("${org.quartz.dataSource.myDS.driver}")
  private String driver;
  
  @Value("${org.quartz.dataSource.myDS.URL}")
  private String url;
  
  @Value("${org.quartz.dataSource.myDS.user}")
  private String user;
  
  @Value("${org.quartz.dataSource.myDS.password}")
  private String password;
  
  @Value("${org.quartz.dataSource.myDS.maxConnections}")
  private String maxConnections;
	 

  @Bean
  public SchedulerFactoryBean scheduler(DataSource dataSource) {
    SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
    schedulerFactory.setQuartzProperties(quartzProperties());
    schedulerFactory.setDataSource(dataSource);
    schedulerFactory.setJobFactory(new SpringBeanJobFactory());
    schedulerFactory.setApplicationContextSchedulerContextKey("applicationContext");
    return schedulerFactory;
  }
  
//配置Quartz的属性，例如jobStore、schedulerName等
  private Properties quartzProperties() {
      Properties properties = new Properties();
      properties.setProperty("org.quartz.jobStore.useProperties", useProperties);
      properties.setProperty("org.quartz.scheduler.instanceName", instanceName);
      properties.setProperty("org.quartz.scheduler.instanceId", instanceId);
      properties.setProperty("org.quartz.scheduler.makeSchedulerThreadDaemon",makeSchedulerThreadDaemon);
      properties.setProperty("org.quartz.jobStore.acquireTriggersWithinLock", acquireTriggersWithinLock);
      properties.setProperty("org.quartz.threadPool.class", threadPoolClass);
      properties.setProperty("org.quartz.threadPool.threadCount", threadCount);
      properties.setProperty("org.quartz.threadPool.threadPriority", threadPriority);
      properties.setProperty("org.quartz.jobStore.class", jobStoreClass);
      properties.setProperty("org.quartz.jobStore.tablePrefix", tablePrefix);
      properties.setProperty("org.quartz.jobStore.misfireThreshold", misfireThreshold);
      properties.setProperty("org.quartz.jobStore.dataSource", dataSource);
      properties.setProperty("org.quartz.jobStore.isClustered", isClustered);
      properties.setProperty("org.quartz.jobStore.clusterCheckinInterval", clusterCheckinInterval);
      properties.setProperty("org.quartz.jobStore.maxMisfiresToHandleAtATime", maxMisfiresToHandleAtATime);
      properties.setProperty("org.quartz.dataSource.myDS.provider", provider);
      properties.setProperty("org.quartz.jobStore.driverDelegateClass", driverDelegateClass);
      properties.setProperty("org.quartz.dataSource.myDS.driver", driver);
      properties.setProperty("org.quartz.dataSource.myDS.URL", url);
      properties.setProperty("org.quartz.dataSource.myDS.user", user);
      properties.setProperty("org.quartz.dataSource.myDS.password", password);
      properties.setProperty("org.quartz.dataSource.myDS.maxConnections", maxConnections);
      return properties;
  }

}

