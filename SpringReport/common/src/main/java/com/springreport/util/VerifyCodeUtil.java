package com.springreport.util;

/**  
 * @ClassName: VerifyCodeUtil
 * @Description: 生成随机验证码工具类
 * @author caiyang
 * @date 2020-06-29 09:59:39 
*/  
public class VerifyCodeUtil {

	/**  
	 * @Title: generateCode
	 * @Description: 生成六位随机码，纯数字
	 * @return
	 * @author caiyang
	 * @date 2020-06-29 10:04:16 
	 */ 
	public static String generateCode() {
		String verifyCode = String.valueOf((int)(Math.random()*900000 + 100000));
		return verifyCode;
	}
	
//	public static void main(String[] args) {
//		VerifyCodeUtil.generateCode();
//	}
}
