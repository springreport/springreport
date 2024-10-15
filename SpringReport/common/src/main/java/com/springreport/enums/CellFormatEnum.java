package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

public enum CellFormatEnum implements BaseCharEnum{

	GENERAL {
		public String getCode() {
			return "General";
		}

		public String getName() {
			return "自动";
		}
	},
	TEXT {
		public String getCode() {
			return "@";
		}

		public String getName() {
			return "纯文本";
		}
	},
	INTEGER{
		public String getCode() {
			return "0";
		}

		public String getName() {
			return "整数";
		}
	},
	INTEGER_2{
		public String getCode() {
			return "#,##0";
		}

		public String getName() {
			return "逗号分割整数";
		}
	},
	FLOAT1{
		public String getCode() {
			return "0.0";
		}

		public String getName() {
			return "一位小数";
		}
	},
	FLOAT2_1{
		public String getCode() {
			return "0.00";
		}
		public String getName() {
			return "两位小数";
		}
	},
	FLOAT2_2{
		public String getCode() {
			return "##0.00";
		}
		public String getName() {
			return "两位小数";
		}
	},
	FLOAT2_3{
		public String getCode() {
			return "#,##0.00";
		}
		public String getName() {
			return "逗号分割两位小数";
		}
	},
	FLOAT2_4{
		public String getCode() {
			return "#,##0.00_);[Red](#,##0.00)";
		}
		public String getName() {
			return "逗号分割两位小数";
		}
	},
	PERCENT1{
		public String getCode() {
			return "0%";
		}
		public String getName() {
			return "整数百分比";
		}
	},
	PERCENT2_1{
		public String getCode() {
			return "#0.00%";
		}
		public String getName() {
			return "两位小数百分比";
		}
	},
	PERCENT2_2{
		public String getCode() {
			return "0.00%";
		}
		public String getName() {
			return "两位小数百分比";
		}
	},
	SCIENTIC_1{
		public String getCode() {
			return "0.00E+00";
		}
		public String getName() {
			return "科学计数法";
		}
	},
	SCIENTIC_2{
		public String getCode() {
			return "##0.0E+0";
		}
		public String getName() {
			return "科学计数法";
		}
	},
	ACCOUNT{
		public String getCode() {
			return "¥(0.00)";
		}
		public String getName() {
			return "会计";
		}
	},
	WANYUAN{
		public String getCode() {
			return "w";
		}
		public String getName() {
			return "万元";
		}
	},
	WANYUAN_2{
		public String getCode() {
			return "w0.00";
		}
		public String getName() {
			return "万元2位小数";
		}
	},
	CURRENCY_1{
		public String getCode() {
			return "¥0.00";
		}
		public String getName() {
			return "人民币";
		}
	},
	CURRENCY_2{
		public String getCode() {
			return "\"¥\" 0.00";
		}
		public String getName() {
			return "人民币";
		}
	},
	DATE_1{
		public String getCode() {
			return "yyyy-MM-dd";
		}
		public String getName() {
			return "日期";
		}
	},
	DATE_2{
		public String getCode() {
			return "yyyy-MM-dd hh:mm AM/PM";
		}
		public String getName() {
			return "日期";
		}
	},
	DATE_3{
		public String getCode() {
			return "yyyy-MM-dd hh:mm";
		}
		public String getName() {
			return "日期";
		}
	},
	DATE_4{
		public String getCode() {
			return "hh:mm AM/PM";
		}
		public String getName() {
			return "日期";
		}
	},
	DATE_5{
		public String getCode() {
			return "hh:mm";
		}
		public String getName() {
			return "日期";
		}
	},
	DATE_6{
		public String getCode() {
			return "yyyy/MM/dd";
		}
		public String getName() {
			return "日期";
		}
	},
	DATE_7{
		public String getCode() {
			return "yyyy\"年\"M\"月\"d\"日\"";
		}
		public String getName() {
			return "日期";
		}
	},
	DATE_8{
		public String getCode() {
			return "MM-dd";
		}
		public String getName() {
			return "日期";
		}
	},
	DATE_9{
		public String getCode() {
			return "M-d";
		}
		public String getName() {
			return "日期";
		}
	},
	DATE_10{
		public String getCode() {
			return "M\"月\"d\"日\"";
		}
		public String getName() {
			return "日期";
		}
	},
	DATE_11{
		public String getCode() {
			return "h:mm:ss";
		}
		public String getName() {
			return "日期";
		}
	},
	DATE_12{
		public String getCode() {
			return "h:mm";
		}
		public String getName() {
			return "日期";
		}
	}
	
}
