package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**  
 * @ClassName: MsgLevelEnum
 * @Description: 消息级别枚举类
 * @author caiyang
 * @date 2020-05-29 10:11:56 
*/  
public enum MsgLevelEnum implements BaseCharEnum{

	SUCCESS {
		public String getCode() {
			return "success";
		}

		public String getName() {
			return "成功信息";
		}
	},
	INFO {
		public String getCode() {
			return "info";
		}

		public String getName() {
			return "通知信息";
		}
	},
	WARN {
		public String getCode() {
			return "warning";
		}

		public String getName() {
			return "警告信息";
		}
	},
	ERROR {
		public String getCode() {
			return "error";
		}

		public String getName() {
			return "错误信息";
		}
	};
}
