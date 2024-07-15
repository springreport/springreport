package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**  
 * @ClassName: ConditionTypeEnum
 * @Description: 条件值类型枚举类
 * @author caiyang
 * @date 2023-04-11 11:21:22 
*/  
public enum ConditionTypeEnum implements BaseCharEnum{

	VARCHAR {
		public String getCode() {
			return "varchar";
		}

		public String getName() {
			return "字符串";
		}
	},
	NUMBER {
		public String getCode() {
			return "number";
		}

		public String getName() {
			return "数字";
		}
	},
	DATE {
		public String getCode() {
			return "date";
		}

		public String getName() {
			return "日期";
		}
	},
	CELL {
		public String getCode() {
			return "cell";
		}

		public String getName() {
			return "单元格";
		}
	},
}
