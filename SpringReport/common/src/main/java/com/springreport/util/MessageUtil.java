package com.springreport.util;

import java.text.MessageFormat;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {
	 
	private static Environment env;
	
	
	public MessageUtil(Environment environment)
	{
		MessageUtil.env = environment;
	}

	private static  String getMessage(String msgId){
		return env.getProperty(msgId);
	}
	
	/**
	 * 设定message
	 * @param message MessagesDTO
	 * @param messageParam String[]
	 * @return String
	 * @throws SpaceParameterException 异常信息
	 */
	private static String setMessageByParam(String message, String[] messageParam) throws Exception {

		MessageFormat messageFormat = new MessageFormat(message);

		String messageValue = messageFormat.format(messageParam);

		return messageValue;

	}
	
	/**   
	 * @Description: 根据消息id获取配置文件中的消息内容
	 * @author caiyang
	 * @param: @param key
	 * @param: @param messageParam 消息内容中替换的参数
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: String      
	 * @throws   
	 */
	public static String getValue(String key, String[] messageParam)
	{
		try {
			return setMessageByParam(getMessage(key), messageParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**   
	 * @Description: 获取资源文件中的属性
	 * @author caiyang
	 * @param: @param key
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: String      
	 * @throws   
	 */
	public static String getValue(String key){
		
		String value = getMessage(key);
		return value;
	}
}
