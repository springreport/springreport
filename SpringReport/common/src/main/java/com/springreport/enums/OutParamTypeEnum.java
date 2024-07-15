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
 * @Description: 输出参数类型
 * @author caiyang
 * @date 2021-10-29 10:53:43 
*/  
public enum OutParamTypeEnum implements BaseCharEnum{

	VARCHAR {
		public String getCode() {
			return "VARCHAR";
		}

		public String getName() {
			return "VARCHAR";
		}
	},
	INTEGER {
		public String getCode() {
			return "INTEGER";
		}

		public String getName() {
			return "INTEGER";
		}
	},
	BIGINT {
		public String getCode() {
			return "BIGINT";
		}

		public String getName() {
			return "BIGINT";
		}
	},
	FLOAT {
		public String getCode() {
			return "FLOAT";
		}

		public String getName() {
			return "FLOAT";
		}
	},
	DOUBLE {
		public String getCode() {
			return "DOUBLE";
		}

		public String getName() {
			return "DOUBLE";
		}
	},
	DECIMAL {
		public String getCode() {
			return "DECIMAL";
		}

		public String getName() {
			return "DECIMAL";
		}
	},
}
