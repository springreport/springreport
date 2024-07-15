package com.springreport.enums;

import lombok.Getter;

/**  
 * @ClassName: LuckysheetFormatEnum
 * @Description: 单元格格式
 * @author caiyang
 * @date 2023-08-24 08:31:23 
*/ 
@Getter
public enum LuckysheetFormatEnum {
	General("General", "自动"),
	Text("@", "纯文本"),
	Integer("0", "整数"),
	Integer1("0.0", "数字一位小数"),
	Integer2("0.00", "数字两位小数"),
	Percent("0%", "百分比整数"),
	Percent1("0.00%", "百分比"),
	Scientific("0.00E+00", "科学计数"),
	Scientific1("##0.0E+0", "科学计数"),
	fraction("# ?/?", "分数"),
	fraction1("# ??/??", "分数"),
	w("w", "万元"),
	w1("w0.00", "万元2位小数"),
	accounting("¥(0.00)", "会计"),
	
	date("hh:mm AM/PM", "时间"),
	date1("hh:mm", "时间24H"),
	date2("yyyy-MM-dd hh:mm AM/PM", "日期时间"),
	date3("yyyy-MM-dd hh:mm", "日期时间24H"),
	date4("yyyy-MM-dd", "日期"),
	date5("yyyy/MM/dd", "日期"),
	date6("yyyy\"年\"M\"月\"d\"日\"", "日期"),
	date7("MM-dd", "日期"),
	date8("M-d", "日期"),
	date9("M\"月\"d\"日\"", "日期"),
	date10("h:mm:ss", "日期"),
	date11("h:mm", "日期"),
	date12("AM/PM hh:mm", "日期"),
	date13("AM/PM h:mm", "日期"),
	date14("AM/PM h:mm:ss", "日期"),
	date15("MM-dd AM/PM hh:mm", "日期"),

	money("\"¥\" 0.00", "货币:人民币"),
	money1("\"$\" 0.00", "货币:美元/港元"),
	money2("\"$\" 0.00", "货币:欧元"),
	money3("\"￡\" 0.00", "货币:英镑"),
	money5("\"￥\" 0.00", "货币:日元"),
	money4("¥ #.00", "货币格式");
	
	
	private final String code;

    private final String message;

    LuckysheetFormatEnum(String code, String message) {
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
    	LuckysheetFormatEnum[] enums = values();
        for (LuckysheetFormatEnum item : enums) {
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
    	LuckysheetFormatEnum[] enums = values();
        for (LuckysheetFormatEnum item : enums) {
            if (item.message().equals(message)) {
                return item.code();
            }
        }
        return null;
    }
}
