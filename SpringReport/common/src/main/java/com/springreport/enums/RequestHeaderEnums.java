package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**  
 * @ClassName: RequestHeaderEnums
 * @Description: 请求头用枚举类
 * @author caiyang
 * @date 2020-06-15 04:49:54 
*/  
public enum RequestHeaderEnums implements BaseCharEnum{

	Authorization {
		public String getCode() {
			return "Authorization";
		}

		public String getName() {
			return "授权";
		}
	},
	ReqSource {
		public String getCode() {
			return "reqSource";
		}

		public String getName() {
			return "请求类型";
		}
	},
	Version {
		public String getCode() {
			return "version";
		}

		public String getName() {
			return "版本号";
		}
	},
	DeviceId {
		public String getCode() {
			return "deviceId";
		}

		public String getName() {
			return "设备标识";
		}
	},
	DeviceType{
		public String getCode() {
			return "deviceType";
		}

		public String getName() {
			return "设备类型";
		}
	},
	
}
