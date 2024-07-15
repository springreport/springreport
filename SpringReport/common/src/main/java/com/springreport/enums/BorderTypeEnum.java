package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**  
 * @ClassName: BorderTypeEnum
 * @Description: 边框类型用枚举类
 * @author caiyang
 * @date 2022年3月24日10:25:59
*/  
public enum BorderTypeEnum implements BaseCharEnum{
	
	BORDERNONE {
		public String getCode() {
			return "border-none";
		}

		public String getName() {
			return "BORDERNONE";//"无边框";
		}
	},
	BORDERALL {
		public String getCode() {
			return "border-all";
		}

		public String getName() {
			return "BORDERALL";//"全边框";
		}
	},
	BORDERLEFT {
		public String getCode() {
			return "border-left";
		}

		public String getName() {
			return "BORDERALL";//"左边框";
		}
	},
	BORDERRIGHT {
		public String getCode() {
			return "border-right";
		}

		public String getName() {
			return "BORDERRIGHT";//"右边框";
		}
	},
	BORDERTOP {
		public String getCode() {
			return "border-top";
		}

		public String getName() {
			return "BORDERTOP";//"上边框";
		}
	},
	BORDERBOTTOM {
		public String getCode() {
			return "border-bottom";
		}

		public String getName() {
			return "BORDERBOTTOM";//"下边框";
		}
	},
}
