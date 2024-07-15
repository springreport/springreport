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
* @ClassName: ValueTypeEnum 
* @Description: 单元格数据类型枚举类
* @author joseph
* @date 2022年11月19日13:43:35
*  
*/
public enum ValueTypeEnum implements BaseCharEnum{

	TEXT {
		public String getCode() {
			return "1";
		}

		public String getName() {
			return "文本";
		}
	},
	NUMERIC {
		public String getCode() {
			return "2";
		}

		public String getName() {
			return "数值";
		}
	},
	SELECT {
		public String getCode() {
			return "4";
		}

		public String getName() {
			return "下拉单选";
		}
	}
}
