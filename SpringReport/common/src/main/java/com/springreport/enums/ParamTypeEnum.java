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
* <p>Description: 参数类型用枚举</p>
* @author caiyang
* @date 2020年3月9日
*/
public enum ParamTypeEnum implements BaseCharEnum{

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
			return "日期类型";
		}
	},
	SELECT{
		public String getCode() {
			return "select";
		}

		public String getName() {
			return "下拉单选";
		}
	},
	MUTISELECT{
		public String getCode() {
			return "mutiselect";
		}

		public String getName() {
			return "下拉多选";
		}
	},
	TREESELECT{
		public String getCode() {
			return "treeSelect";
		}

		public String getName() {
			return "下拉单选树";
		}
	},
	MULTITREESELECT{
		public String getCode() {
			return "multiTreeSelect";
		}

		public String getName() {
			return "下拉多选树";
		}
	},
}
