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
* @ClassName: AggregateEnum 
* @Description: 聚合类型举类
* @author joseph
* @date 2020年3月29日20:51:59
*  
*/
public enum AggregateTypeEnum implements BaseCharEnum{

	GROUP {
		public String getCode() {
			return "group";
		}

		public String getName() {
			return "分组聚合";
		}
	},
	LIST {
		public String getCode() {
			return "list";
		}

		public String getName() {
			return "列表";
		}
	},
	SUMMARY {
		public String getCode() {
			return "summary";
		}

		public String getName() {
			return "汇总";
		}
	},
	GROUPSUMMARY {
		public String getCode() {
			return "groupSummary";
		}

		public String getName() {
			return "分组汇总";
		}
	},
	CROSS {
		public String getCode() {
			return "cross";
		}

		public String getName() {
			return "交叉扩展";
		}
	},
	BLOCK {
		public String getCode() {
			return "block";
		}

		public String getName() {
			return "循环块";
		}
	}
}
