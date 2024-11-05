/*   
 * Copyright (c) 2016-2020 canaanQd. All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * canaanQd. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with canaanQd.   
 *   
 */ 

package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**
* <p>Title: ParamTypeEnum</p>
* <p>Description: 存储过程入参参数类型用枚举</p>
* @author caiyang
* @date 2020年3月9日
*/
public enum ProcedureParamTypeEnum implements BaseCharEnum{

	STRING {
		public String getCode() {
			return "String";
		}

		public String getName() {
			return "字符串";
		}
	},
	INT {
		public String getCode() {
			return "int";
		}

		public String getName() {
			return "int";
		}
	},
	FLOAT {
		public String getCode() {
			return "Float";
		}

		public String getName() {
			return "Float";
		}
	},
	DOUBLE {
		public String getCode() {
			return "Double";
		}

		public String getName() {
			return "Double";
		}
	},
	LONG {
		public String getCode() {
			return "Long";
		}

		public String getName() {
			return "Long";
		}
	},
	BIGDECIMAL {
		public String getCode() {
			return "BigDecimal";
		}

		public String getName() {
			return "BigDecimal";
		}
	},
	DATE {
		public String getCode() {
			return "Date";
		}

		public String getName() {
			return "Date";
		}
	},
	DATETIME {
		public String getCode() {
			return "DateTime";
		}

		public String getName() {
			return "DateTime";
		}
	}
}
