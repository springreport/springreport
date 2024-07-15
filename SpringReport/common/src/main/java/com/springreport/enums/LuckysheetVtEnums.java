package com.springreport.enums;

import lombok.Getter;

/**  
 * @ClassName: LuckysheetVtEnums
 * @Description: 垂直对齐方式
 * @author caiyang
 * @date 2023-08-24 08:31:10 
*/ 
@Getter
public enum LuckysheetVtEnums {
	middle("0", "居中对齐"),
	top("1", "顶部对齐"),
	bottom("2", "底部对齐");
	
	
	private final String code;

    private final String message;

    LuckysheetVtEnums(String code, String message) {
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
    	LuckysheetVtEnums[] enums = values();
        for (LuckysheetVtEnums item : enums) {
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
    	LuckysheetVtEnums[] enums = values();
        for (LuckysheetVtEnums item : enums) {
            if (item.message().equals(message)) {
                return item.code();
            }
        }
        return null;
    }
}
