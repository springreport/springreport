package com.springreport.util;

import java.security.MessageDigest;

public class Md5Util {

    /**  
     * @Title: generateMd5
     * @Description: 生成32位md5
     * @param str
     * @return
     * @author caiyang
     * @date 2020-06-01 09:18:04 
     */ 
    public static String generateMd5(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            char[] charArray = str.toCharArray();
            byte[] byteArray = new byte[charArray.length];

            for (int i = 0; i < charArray.length; i++) {
                byteArray[i] = (byte) charArray[i];
            }
            byte[] md5Bytes = md5.digest(byteArray);

            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }

            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
}
