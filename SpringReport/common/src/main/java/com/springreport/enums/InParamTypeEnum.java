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
import com.springreport.base.BaseIntEnum;

/**  
 * @ClassName: InParamTypeEnum
 * @Description: 输入参数类型
 * @author caiyang
 * @date 2021-10-29 10:53:43 
*/  
public enum InParamTypeEnum implements BaseCharEnum{

	INT {
		public String getCode() {
			return "int";
		}

		public String getName() {
			return "int";
		}
	},
	STRING {
		public String getCode() {
			return "String";
		}

		public String getName() {
			return "String";
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
	DOUBLE {
		public String getCode() {
			return "Double";
		}

		public String getName() {
			return "Double";
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
	},
}
