package com.springreport.util;

import java.io.InputStream;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.springreport.base.EmailAttachementDto;

/**  
 * @ClassName: SendEmailUtil
 * @Description: 发送邮件工具类
 * @author caiyang
 * @date 2023-07-28 09:26:12 
*/ 
@Component
public class SendEmailUtil {

	  @Value("${spring.mail.from}")
	  private String from; // 发送发邮箱地址

	  @Autowired
	  private JavaMailSender mailSender;
	  
	  public void sendMessage(String to,String subject,String content,List<EmailAttachementDto> attachements) {
		  MimeMessage mimeMessage = mailSender.createMimeMessage();
		  //解决附件文件名称过长乱码问题
		  System.setProperty("mail.mime.splitlongparameters","false");
		  try {
			  MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
	          helper.setFrom(from); // 设置发送发
	          helper.setTo(to.split(";")); // 设置接收方
	          helper.setSubject(subject); // 设置邮件主题
	          helper.setText(content,true); // 设置邮件内容
	          if(!ListUtil.isEmpty(attachements))
	          {
	        	  for (int i = 0; i < attachements.size(); i++) {
	        		  helper.addAttachment(attachements.get(i).getFileName(), new ByteArrayResource(IOUtils.toByteArray(attachements.get(i).getIs())));
				}
	          }
	          
			} catch (Exception e) {
				e.printStackTrace();
			}
		  mailSender.send(mimeMessage);
	  }
}
