package com.springreport.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import cn.hutool.core.util.NumberUtil;

public class CheckUtil {

	/**
	 * 是否不为空
	 * 
	 * @param value    字段值
	 * @param msgParam 错误消息参数，其中包括一些判断条件，例如长度
	 * @return 是否不为空
	 */
	public static Boolean isNotNull(Object value) {
		Boolean isNotNull = Boolean.TRUE;
		Boolean isStringNull = (value instanceof String) && StringUtil.isNullOrEmpty((String) value);
		Boolean isCollectionNull = (value instanceof Collection) && CollectionUtils.isEmpty((Collection) value);
		if (value == null) {
			isNotNull = Boolean.FALSE;
		} else if (isStringNull || isCollectionNull) {
			isNotNull = Boolean.FALSE;
		}
		return isNotNull;
	}

	/**
	 * 数字校验
	 * 
	 * @param number 待校验数字
	 * @param intMax 最大整数位
	 * @param decMax 最大小数位(没有小数位写0)
	 * @return 校验结果 满足true 不满足false
	 */
	public static boolean isNumber(String number, int intMax, int decMax) {
		boolean flag = false;
		if (StringUtils.isEmpty(number)) {
			flag = true;
		} else if (decMax == 0) {
			flag = number.matches("^[0-9]+$") && number.length() <= intMax;
		} else {
			String[] num = number.split("\\.");
			if (num.length < 2) {
				flag = number.matches("^[0-9]+\\.{0,1}[0-9]*") && num[0].length() <= intMax;
			} else {
				flag = number.matches("^[0-9]+\\.{0,1}[0-9]*") && num[0].length() <= intMax
						&& num[1].length() <= decMax;
			}
		}
		return flag;
	}

	/**
	 * 日期校验
	 * 
	 * @param str    待校验日期
	 * @param format 日期格式
	 * @return 校验结果 满足true 不满足false
	 */
	public static boolean isDate(String str, String format) {
		boolean flag = false;
		if (StringUtils.isEmpty(str)) {
			flag = true;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			try {
				sdf.setLenient(false);
				sdf.parse(str);
				flag = true;
			} catch (Exception e) {
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * 电话校验
	 * 
	 * @param tel 待校验电话
	 * @return 校验结果 满足true 不满足false
	 */
	public static boolean isTel(String tel) {
		boolean flag = false;
		if (StringUtils.isEmpty(tel)) {
			flag = true;
		} else {
			flag = tel.matches("^[0-9-+\\(\\) ]*$");
		}

		return flag;
	}

	/**
	 * 邮箱校验
	 * 
	 * @param mail 待校验邮箱
	 * @return 校验结果 满足true 不满足false
	 */
	public static boolean isMail(String mail) {
		boolean flag = false;
		if (StringUtils.isEmpty(mail)) {
			flag = true;
		} else {
			flag = mail.matches("^([_a-zA-Z0-9-]+)(\\.[_a-zA-Z0-9-]+)*@([_a-zA-Z0-9-]+\\.)+([a-zA-Z]{2,3})$");
		}

		return flag;
	}

	/**
	 * @Description: 判断是否是手机号 @param @param mobiles @param @return @return
	 * boolean @throws
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^(1[0-9])\\d{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	/**  
	 * @Title: isMobileOrPhone
	 * @Description: 判断是否是座机或者手机号
	 * @param mobilephone
	 * @return
	 * @author caiyang
	 * @date 2020-06-18 03:20:59 
	 */ 
	public static boolean isMobileOrPhone(String mobilephone) {
		Pattern p = Pattern.compile("^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})$|(1[0-9])\\d{9}$");
		Matcher m = p.matcher(mobilephone);
		return m.matches();
	}
	
	/**
	* @Title: validStrLength
	* @Description: 判断字符串长度
	* @param @param str
	* @param @param length
	* @param @return 参数
	* @return boolean
	* @throws
	*/
	public static boolean validStrLength(String str,Integer length)
	{
		if (StringUtil.isNullOrEmpty(str)) {
			return Boolean.TRUE;
		}
		if (str.length() > length) {
			return Boolean.FALSE;
		}else {
			return Boolean.TRUE;
		}
	}
	
	/**  
	 * @Title: validFixedLength
	 * @Description: 校验固定长度
	 * @param str
	 * @param length
	 * @return
	 * @author caiyang
	 * @date 2020-07-14 09:05:54 
	 */ 
	public static boolean validFixedLength(String str,Integer length)
	{
		if (StringUtil.isNullOrEmpty(str)) {
			return Boolean.TRUE;
		}
		if (str.length() == length) {
			return Boolean.TRUE;
		}else {
			return Boolean.FALSE;
		}
	}
	
	/**
	*<p>Title: validMax</p>
	*<p>Description: 比较大小</p>
	* @author caiyang
	* @param value
	* @param max
	* @return
	*/
	public static boolean validMax(BigDecimal value,BigDecimal max)
	{
		if (value.compareTo(max)>0) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	* @Title: isNumeric
	* @Description: 判断字符串是否为数字
	* @param @param str
	* @param @return 参数
	* @return boolean
	* @throws
	*/
	public static boolean isNumeric(String str)
	{
		 // 该正则表达式可以匹配所有的数字 包括负数
		Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
		String bigStr;
		try {
		bigStr = new BigDecimal(str).toString();
		} catch (Exception e) {
		return false;//异常 说明包含非数字。
		}

		Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	 /**
     * 判断一个字符串是否是数字。
     * 
     * @param string
     * @return
     */
    public static boolean isNumber(String string) {
//        if (string == null)
//            return false;
//        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return NumberUtil.isNumber(string);
    }
	
	public static boolean isEmail(String str)
	{
		if (StringUtil.isNullOrEmpty(str)) {
			return true;
		}
		String regex = "\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}";
		if (str.matches(regex)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**  
	 * @Title: checkPassword
	 * @Description: 校验密码复杂度
	 * @param str
	 * @return
	 * @author caiyang
	 * @date 2020-07-14 09:26:47 
	 */ 
	public static boolean checkPassword(String str)
	{
		if (StringUtil.isNullOrEmpty(str)) {
			return false;
		}
		boolean isRepeat = CheckUtil.isRepeat(str);
		if (isRepeat) {
			return false;
		}
		//检验是否连续
		if (!CheckUtil.isContinue(str)) {
			return false;
		}
		return true;
	}
	
	/**  
	 * @Title: isRepeat
	 * @Description: 判断是否有连续三个相同的字符
	 * @param str
	 * @return
	 * @author caiyang
	 * @date 2020-07-14 11:22:17 
	 */ 
	public static boolean isRepeat(String str) {
		char chars[] = str.toCharArray();
		for (int i = 1; i < chars.length - 1; i++) {
			int first = String.valueOf(chars[i-1]).hashCode();
			int second =  String.valueOf(chars[i]).hashCode();
			int third =  String.valueOf(chars[i+1]).hashCode();
			if (first == second && second == third) {
				return true;
			}
		}
		return false;
	}
	
	/**  
	 * @Title: isContinue
	 * @Description: 判断字符串是是否有三个以上的连续
	 * @param str
	 * @return
	 * @author caiyang
	 * @date 2020-07-14 09:34:23 
	 */ 
	public static boolean isContinue(String str) {
		char chars[] = str.toCharArray();
		//正向检查
		for (int i = 1; i < chars.length - 1; i++) {
			int first = String.valueOf(chars[i-1]).hashCode();
			int second =  String.valueOf(chars[i]).hashCode();
			int third =  String.valueOf(chars[i+1]).hashCode();
			if (third - second == 1 && second - first == 1) {
				return false;
			}
		}
		//反向检查
		for (int i = 1; i < chars.length - 1; i++) {
			int first = String.valueOf(chars[i-1]).hashCode();
			int second =  String.valueOf(chars[i]).hashCode();
			int third =  String.valueOf(chars[i+1]).hashCode();
			if (first - second == 1 && second - third == 1) {
				return false;
			}
		}
		return true;
	}
	
	  public static boolean isIDCardNo(String id) {
	        if (id == null)
	            return false;
	        id = id.toUpperCase();
	        if (id.length() != 15 && id.length() != 18) {
	            return false;
	        }
	        int y = 0, m = 0, d = 0;
	        if (id.length() == 15) {
	            y = Integer.parseInt("19" + id.substring(6, 8), 10);
	            m = Integer.parseInt(id.substring(8, 10), 10);
	            d = Integer.parseInt(id.substring(10, 12), 10);
	        } else if (id.length() == 18) {
	            if (id.indexOf("X") >= 0 && id.indexOf("X") != 17) {
	                return false;
	            }
	            char verifyBit = 0;
	            int sum = (id.charAt(0) - '0') * 7 + (id.charAt(1) - '0') * 9 + (id.charAt(2) - '0') * 10
	                    + (id.charAt(3) - '0') * 5 + (id.charAt(4) - '0') * 8 + (id.charAt(5) - '0') * 4
	                    + (id.charAt(6) - '0') * 2 + (id.charAt(7) - '0') * 1 + (id.charAt(8) - '0') * 6
	                    + (id.charAt(9) - '0') * 3 + (id.charAt(10) - '0') * 7 + (id.charAt(11) - '0') * 9
	                    + (id.charAt(12) - '0') * 10 + (id.charAt(13) - '0') * 5 + (id.charAt(14) - '0') * 8
	                    + (id.charAt(15) - '0') * 4 + (id.charAt(16) - '0') * 2;
	            sum = sum % 11;
	            switch (sum) {
	                case 0:
	                    verifyBit = '1';
	                    break;
	                case 1:
	                    verifyBit = '0';
	                    break;
	                case 2:
	                    verifyBit = 'X';
	                    break;
	                case 3:
	                    verifyBit = '9';
	                    break;
	                case 4:
	                    verifyBit = '8';
	                    break;
	                case 5:
	                    verifyBit = '7';
	                    break;
	                case 6:
	                    verifyBit = '6';
	                    break;
	                case 7:
	                    verifyBit = '5';
	                    break;
	                case 8:
	                    verifyBit = '4';
	                    break;
	                case 9:
	                    verifyBit = '3';
	                    break;
	                case 10:
	                    verifyBit = '2';
	                    break;

	            }

	            if (id.charAt(17) != verifyBit) {
	                return false;
	            }
	            y = Integer.parseInt(id.substring(6, 10), 10);
	            m = Integer.parseInt(id.substring(10, 12), 10);
	            d = Integer.parseInt(id.substring(12, 14), 10);
	        }

	        int currentY = Calendar.getInstance().get(Calendar.YEAR);

	        /*
	         * if(isGecko){ currentY += 1900; }
	         */
	        if (y > currentY || y < 1870) {
	            return false;
	        }
	        if (m < 1 || m > 12) {
	            return false;
	        }
	        if (d < 1 || d > 31) {
	            return false;
	        }
	        return true;
	    }
	  
	  public static boolean validate(String formula) {
	        String regex = "^((\\d+|\\d+[.]?\\d+)|\\(\\s*(\\d+|\\d+[.]?\\d+)\\s*[\\+\\-\\*/]\\s*(\\d+|\\d+[.]?\\d+)+\\s*\\))(\\s*[\\+\\-\\*/]\\s*((\\d+|\\d+[.]?\\d+)|\\(\\s*(\\d+|\\d+[.]?\\d+)\\s*[\\+\\-\\*/]\\s*(\\d+|\\d+[.]?\\d+)+\\s*\\)))*$";
	        Pattern pattern = Pattern.compile(regex);
	        return pattern.matcher(formula).matches();
	    }
	  private static  String[] operators = {"\\+", "-", "\\*", "/", "%"};
	  public static boolean containsOperator(String str) {
		    for (String operator : operators) {
		        Pattern pattern = Pattern.compile(operator);
		        Matcher matcher = pattern.matcher(str);
		        if (matcher.find()) {
		            return true;
		        }
		    }
		    return false;
		}
	  
	
	 public static void main(String[] args) {
		 System.err.println(CheckUtil.validate("370686199003284617")&&CheckUtil.containsOperator("370686199003284617"));;
	 }
}
