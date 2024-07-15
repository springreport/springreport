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
* @ClassName: CellAttrEnum 
* @Description: 单元格属性美剧
* @author joseph
* @date 2022年11月19日13:52:21
*  
*/
public enum CellAttrEnum implements BaseCharEnum{

	VALUETYPE {
		public String getCode() {
			return "valueType";
		}

		public String getName() {
			return "值类型";
		}
	},
	REGEX {
		public String getCode() {
			return "regex";
		}

		public String getName() {
			return "正则表达式";
		}
	},
	REQUIRE {
		public String getCode() {
			return "require";
		}

		public String getName() {
			return "是否必填";
		}
	},
	DATEFORMAT {
		public String getCode() {
			return "dateFormat";
		}

		public String getName() {
			return "日期格式";
		}
	},
	SELECTTYPE {
		public String getCode() {
			return "selectType";
		}

		public String getName() {
			return "下拉选择类型";
		}
	},
	LENGTHVALID {
		public String getCode() {
			return "lengthValid";
		}

		public String getName() {
			return "是否长度校验";
		}
	},
	MINLENGTH {
		public String getCode() {
			return "minLength";
		}

		public String getName() {
			return "最小长度";
		}
	},
	MAXLENGTH {
		public String getCode() {
			return "maxLength";
		}

		public String getName() {
			return "最大长度";
		}
	},
	NUMBERRANGEVALID {
		public String getCode() {
			return "numberRangeValid";
		}

		public String getName() {
			return "数值大小校验";
		}
	},
	MINVALUE {
		public String getCode() {
			return "minValue";
		}

		public String getName() {
			return "最小值";
		}
	},
	MAXVALUE {
		public String getCode() {
			return "maxValue";
		}

		public String getName() {
			return "最大值";
		}
	},
	DIGIT {
		public String getCode() {
			return "digit";
		}

		public String getName() {
			return "小数位数";
		}
	}
}
