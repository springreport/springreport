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
* <p>Title: CellExtendEnum</p>
* <p>Description: 单元格扩展类型用枚举类</p>
* @author caiyang
* @date 2020年3月8日
*/
public enum CellExtendEnum implements BaseIntEnum{

	NOEXTEND {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "不扩展";
		}
	},
	HORIZONTAL  {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "横向扩展";
		}
	},
	VERTICAL  {
		public Integer getCode() {
			return 3;
		}

		public String getName() {
			return "纵向扩展";
		}
	},
	CROSS  {
		public Integer getCode() {
			return 4;
		}

		public String getName() {
			return "交叉扩展";
		}
	}
}
