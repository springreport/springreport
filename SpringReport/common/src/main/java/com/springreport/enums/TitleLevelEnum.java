package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**  
 * @ClassName: TitleLevelEnum
 * @Description: 标题级别
 * @author caiyang
 * @date 2024年5月4日07:38:30
*/  
public enum TitleLevelEnum implements BaseCharEnum{

	FIRST {
		public String getCode() {
			return "first";
		}

		public String getName() {
			return "标题1";
		}
	},
	SECOND {
		public String getCode() {
			return "second";
		}

		public String getName() {
			return "标题2";
		}
	},
	THIRD {
		public String getCode() {
			return "third";
		}

		public String getName() {
			return "标题3";
		}
	},
	FOURTH {
		public String getCode() {
			return "fourth";
		}

		public String getName() {
			return "标题4";
		}
	},
	FIFTH {
		public String getCode() {
			return "fifth";
		}

		public String getName() {
			return "标题5";
		}
	},
	SIXTH {
		public String getCode() {
			return "sixth";
		}

		public String getName() {
			return "标题6";
		}
	}
}
