package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**  
 * @ClassName: RequestTypeEnum
 * @Description: 请求类型用枚举类
 * @author caiyang
 * @date 2021年7月13日06:55:10
*/  
public enum RequestTypeEnum implements BaseCharEnum{

	POST {
		public String getCode() {
			return "post";
		}

		public String getName() {
			return "post请求";
		}
	},
	GET {
		public String getCode() {
			return "get";
		}

		public String getName() {
			return "get请求";
		}
	},
	
}
