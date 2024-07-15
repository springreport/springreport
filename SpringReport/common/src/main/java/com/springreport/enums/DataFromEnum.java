
package com.springreport.enums;

import com.springreport.base.BaseIntEnum;

/** 
* @ClassName: DataFromEnum 
* @Description: 数据来源枚举类
* @author caiyang
* @date 2022年3月16日08:15:11
*  
*/
public enum DataFromEnum implements BaseIntEnum{

	DEFAULT {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "默认";
		}
	},
	ORIGINAL {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "原始数据";
		}
	},
	CELL {
		public Integer getCode() {
			return 3;
		}

		public String getName() {
			return "单元格";
		}
	}
}
