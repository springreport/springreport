package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**  
 * @ClassName: RequestTypeEnum
 * @Description: 请求类型用枚举类
 * @author caiyang
 * @date 2021年7月13日06:55:10
*/  
public enum ResultTypeEnum implements BaseCharEnum{

	STRING {
		public String getCode() {
			return "String";
		}

		public String getName() {
			return "字符串类型";
		}
	},
	ARRAY {
		public String getCode() {
			return "Array";
		}

		public String getName() {
			return "字符串数组类型";
		}
	},
	OBJECTARRAY {
		public String getCode() {
			return "ObjectArray";
		}

		public String getName() {
			return "对象数组类型";
		}
	},
	OBJECT {
		public String getCode() {
			return "Object";
		}

		public String getName() {
			return "对象类型";
		}
	},
	
}
