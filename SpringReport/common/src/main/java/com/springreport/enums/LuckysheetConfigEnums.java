package com.springreport.enums;

import lombok.Getter;

/**  
 * @ClassName: LuckysheetConfigEnums
 * @Description: sheet配置信息枚举
 * @author caiyang
 * @date 2023-08-24 08:31:10 
*/ 
@Getter
public enum LuckysheetConfigEnums {
	merge("merge", "单元格合并"),
	rowlen("rowlen", "行高"),
	columnlen("columnlen", "列宽"),
	rowhidden("rowhidden", "隐藏行"),
	colhidden("colhidden", "隐藏列"),
	borderInfo("borderInfo", "边框"),
	authority("authority", "工作表保护");
	
	
	private final String code;

    private final String message;

    LuckysheetConfigEnums(String code, String message) {
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
    	LuckysheetConfigEnums[] enums = values();
        for (LuckysheetConfigEnums item : enums) {
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
    	LuckysheetConfigEnums[] enums = values();
        for (LuckysheetConfigEnums item : enums) {
            if (item.message().equals(message)) {
                return item.code();
            }
        }
        return null;
    }
}
