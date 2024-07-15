package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**  
 * @ClassName: SqlOrderEnum
 * @Description: sql排序用枚举类
 * @author caiyang
 * @date 2020年5月14日 
*/  
public enum SqlOrderEnum implements BaseCharEnum{

	ASC {
		public String getCode() {
			return "ASC";
		}

		public String getName() {
			return "正序";
		}
	},
	DESC {
		public String getCode() {
			return "DESC";
		}

		public String getName() {
			return "倒序";
		}
	}
}
