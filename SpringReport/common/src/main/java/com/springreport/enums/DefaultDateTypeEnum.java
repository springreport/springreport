/*   
 *  青岛灵数软件. All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * 青岛灵数软件. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with 青岛灵数软件.   
 *   
 */ 

package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/** 
* @ClassName: AccessRuleEnum 
* @Description: 菜单访问规则枚举类
* @author joseph
* @date 2019年10月24日13:54:30
*  
*/
public enum DefaultDateTypeEnum implements BaseCharEnum{

	WF {
		public String getCode() {
			return "wf";
		}

		public String getName() {
			return "本周第一天";
		}
	},
	WL {
		public String getCode() {
			return "wl";
		}

		public String getName() {
			return "本周最后一天";
		}
	},
	MF {
		public String getCode() {
			return "mf";
		}

		public String getName() {
			return "本月第一天";
		}
	},
	ML {
		public String getCode() {
			return "ml";
		}

		public String getName() {
			return "本月最后一天";
		}
	},
	SF {
		public String getCode() {
			return "sf";
		}

		public String getName() {
			return "本季度第一天";
		}
	},
	SL {
		public String getCode() {
			return "sl";
		}

		public String getName() {
			return "本季度最后一天";
		}
	},
	YF {
		public String getCode() {
			return "yf";
		}

		public String getName() {
			return "本年第一天";
		}
	},
	YL {
		public String getCode() {
			return "yl";
		}

		public String getName() {
			return "本年最后一天";
		}
	}
}
