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

import com.springreport.base.BaseIntEnum;

/** 
* @ClassName: CellValueTypeEnum 
* @Description: 单元格类型
* @author joseph
* @date 2020年3月20日14:26:20
*  
*/
public enum CellValueTypeEnum implements BaseIntEnum{

	FIXED {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "固定值";
		}
	},
	VARIABLE {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "变量";
		}
	},
	BLOCK {
		public Integer getCode() {
			return 3;
		}

		public String getName() {
			return "循环块";
		}
	}
}
