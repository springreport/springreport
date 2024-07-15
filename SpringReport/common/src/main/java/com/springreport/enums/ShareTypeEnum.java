package com.springreport.enums;

import com.springreport.base.BaseIntEnum;

/**  
 * @ClassName: ShareTypeEnum
 * @Description: 分享类型枚举类
 * @author caiyang
 * @date 2023-06-25 02:26:07 
*/ 
public enum ShareTypeEnum implements BaseIntEnum{

	PC {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "PC";
		}
	},
	H5 {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "H5";
		}
	}
}
