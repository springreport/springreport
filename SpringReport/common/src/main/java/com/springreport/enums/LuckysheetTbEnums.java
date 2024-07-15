package com.springreport.enums;

import lombok.Getter;

/**  
 * @ClassName: LuckysheetTbEnums
 * @Description: 换行方式
 * @author caiyang
 * @date 2023-08-24 08:31:10 
*/ 
@Getter
public enum LuckysheetTbEnums {
	cut("0", "截断"),
	overflow("1", "溢出"),
	wrap("2", "自动换行");
	
	
	private final String code;

    private final String message;

    LuckysheetTbEnums(String code, String message) {
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
    	LuckysheetTbEnums[] enums = values();
        for (LuckysheetTbEnums item : enums) {
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
    	LuckysheetTbEnums[] enums = values();
        for (LuckysheetTbEnums item : enums) {
            if (item.message().equals(message)) {
                return item.code();
            }
        }
        return null;
    }
}
