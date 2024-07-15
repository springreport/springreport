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
* @ClassName: SqlOperatorEnum 
* @Description: sql操作符
* @author joseph
* @date 2021年6月7日18:02:39
*  
*/
public enum SqlOperatorEnum implements BaseCharEnum{

	EQUAL {
		public String getCode() {
			return "=";
		}

		public String getName() {
			return "等于号";
		}
	},
	IN {
		public String getCode() {
			return "IN";
		}

		public String getName() {
			return "存在于";
		}
	},
	NULL {
		public String getCode() {
			return "IS NULL";
		}

		public String getName() {
			return "为空";
		}
	},
	NOTNULL {
		public String getCode() {
			return "IS NOT NULL";
		}

		public String getName() {
			return "不为空";
		}
	},
	THAN {
		public String getCode() {
			return ">";
		}

		public String getName() {
			return "大于";
		}
	},
	MORETHAN{
		public String getCode() {
			return ">=";
		}

		public String getName() {
			return "大于等于";
		}
	},
	LESS{
		public String getCode() {
			return "<";
		}

		public String getName() {
			return "小于";
		}
	},
	LESSTHAN{
		public String getCode() {
			return "<=";
		}

		public String getName() {
			return "小于等于";
		}
	},
	NOTEQUALE{
		public String getCode() {
			return "!=";
		}

		public String getName() {
			return "不等于";
		}
	},
	NOTEQUALE2{
		public String getCode() {
			return "<>";
		}

		public String getName() {
			return "不等于";
		}
	},
}
