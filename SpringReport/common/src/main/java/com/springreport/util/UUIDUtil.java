package com.springreport.util;

import java.util.UUID;

/**  
 * @ClassName: UUIDUtil
 * @Description: uuid工具类
 * @author caiyang
 * @date 2020-06-23 10:05:58 
*/  
public class UUIDUtil {

	/**  
	 * @Title: getUUID
	 * @Description: 生成uuid
	 * @return
	 * @author caiyang
	 * @date 2020-06-23 10:06:06 
	 */ 
	public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
   }
	
	public static void main(String[] args) {
		System.out.println(UUIDUtil.getUUID());
	}

}
