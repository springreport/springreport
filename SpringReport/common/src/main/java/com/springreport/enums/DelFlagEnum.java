package com.springreport.enums;

import com.springreport.base.BaseIntEnum;

/** 
* @ClassName: DelFlagEnum 
* @Description: 删除标记枚举类
* @author caiyang
* @date 2020年5月29日10:11:30
*  
*/
public enum DelFlagEnum implements BaseIntEnum{

	UNDEL {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "未删除";
		}
	},
	DEL {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "已删除";
		}
	}
}
