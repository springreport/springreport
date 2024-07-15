package com.springreport.enums;

import com.springreport.base.BaseIntEnum;

/**  
 * @ClassName: SqlTypeEnum
 * @Description: sql类型枚举类
 * @author caiyang
 * @date 2021-10-25 08:28:32 
*/  
public enum SqlTypeEnum implements BaseIntEnum{

	SQL {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "sql语句";
		}
	},
	FUNCTION {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "函数或者存储过程";
		}
	}
}
