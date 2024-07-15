package com.springreport.qrtz;


public interface MessageHandler {

	  void handlerMessage(String jobData,Long taskId) throws Exception;
}

