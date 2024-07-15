package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**  
 * @ClassName: TextWrapModeEnum
 * @Description: 文本换行方式
 * @author caiyang
 * @date 2022年3月29日07:57:17
*/  
public enum TextWrapModeEnum implements BaseCharEnum{

	OVERFLOW {
		public String getCode() {
			return "1";
		}

		public String getName() {
			return "溢出";
		}
	},
	WRAP {
		public String getCode() {
			return "2";
		}

		public String getName() {
			return "自动换行";
		}
	},
	CUT {
		public String getCode() {
			return "0";
		}

		public String getName() {
			return "截断";
		}
	}
}
