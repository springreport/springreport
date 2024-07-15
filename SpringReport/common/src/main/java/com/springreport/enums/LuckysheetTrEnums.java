package com.springreport.enums;

import lombok.Getter;

/**  
 * @ClassName: LuckysheetTbEnums
 * @Description: 换行方式
 * @author caiyang
 * @date 2023-08-24 08:31:10 
*/ 
@Getter
public enum LuckysheetTrEnums {
	none("0", "无倾斜"),
	up("1", "向上倾斜"),
	down("2", "向下倾斜"),
	vertical("3", "竖排文字"),
	up90("4", "向上90°"),
	down90("5", "向下90°");
	
	
	private final String code;

    private final String message;

    LuckysheetTrEnums(String code, String message) {
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
    	LuckysheetTrEnums[] enums = values();
        for (LuckysheetTrEnums item : enums) {
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
    	LuckysheetTrEnums[] enums = values();
        for (LuckysheetTrEnums item : enums) {
            if (item.message().equals(message)) {
                return item.code();
            }
        }
        return null;
    }
}
