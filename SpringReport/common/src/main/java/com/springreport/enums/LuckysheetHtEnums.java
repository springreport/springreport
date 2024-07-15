package com.springreport.enums;

import lombok.Getter;

/**  
 * @ClassName: LuckysheetHtEnums
 * @Description: 水平对齐方式
 * @author caiyang
 * @date 2023-08-24 08:31:10 
*/ 
@Getter
public enum LuckysheetHtEnums {
	center("0", "中间对齐"),
	left("1", "左对齐"),
	right("2", "右对齐");
	
	
	private final String code;

    private final String message;

    LuckysheetHtEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code() {
        return this.code;
    }

    private String message() {
        return this.message;
    }
    
    /**
     * 根据key获取value
     *
     * @param code
     * @return
     */
    public static String getValue(String code) {
    	LuckysheetHtEnums[] enums = values();
        for (LuckysheetHtEnums item : enums) {
            if (item.code().equals(code)) {
                return item.message();
            }
        }
        return null;
    }

    /**
     * 根据value获取key
     *
     * @param message
     * @return
     */
    public static String getCode(String message) {
    	LuckysheetHtEnums[] enums = values();
        for (LuckysheetHtEnums item : enums) {
            if (item.message().equals(message)) {
                return item.code();
            }
        }
        return null;
    }
}
