package com.springreport.enums;

import com.springreport.base.BaseIntEnum;

/**  
 * @ClassName: TplTypeEnum
 * @Description: 报表类型用枚举类
 * @author caiyang
 * @date 2021-05-28 03:48:38 
*/  
public enum TplTypeEnum implements BaseIntEnum{
	
	LIST {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "列表式报表";
		}
	},
	FORMS {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "填报式报表";
		}
	}
}
