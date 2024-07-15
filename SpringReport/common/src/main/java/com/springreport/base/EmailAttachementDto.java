package com.springreport.base;

import java.io.InputStream;

import lombok.Data;

/**  
 * @ClassName: EmailAttachementDto
 * @Description: 邮件附件实体类
 * @author caiyang
 * @date 2023-07-29 10:47:42 
*/ 
@Data
public class EmailAttachementDto {

	private String fileName;
	
	/**  
	 * @Fields is : 文件流
	 * @author caiyang
	 * @date 2023-07-29 10:48:02 
	 */  
	private InputStream is;
}
