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
* @ClassName: SelectTypeEnum 
* @Description: 下拉框数据来源
* @author joseph
* @date 2019年9月29日 下午4:21:02 
*  
*/
public enum SelectTypeEnum implements BaseCharEnum{

	CUSTOM {
		public String getCode() {
			return "1";
		}

		public String getName() {
			return "自定义";
		}
	},
	SQL {
		public String getCode() {
			return "2";
		}

		public String getName() {
			return "sql语句";
		}
	},
	INTERFACE{
		public String getCode() {
			return "3";
		}

		public String getName() {
			return "接口获取";
		}
	},
}
