package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

public enum FunctionExpressEnum implements BaseCharEnum{
	NOW {
		public String getCode() {
			return "=NOW()";
		}

		public String getName() {
			return "当前时间函数";
		}
	},
}
