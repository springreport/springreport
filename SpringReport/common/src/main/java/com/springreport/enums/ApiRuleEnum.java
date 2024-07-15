package com.springreport.enums;

import com.springreport.base.BaseIntEnum;

/**  
 * @ClassName: ApiRuleEnum
 * @Description: 功能单访问权限规则枚举类
 * @author caiyang
 * @date 2020-06-08 09:00:22 
*/  
public enum ApiRuleEnum implements BaseIntEnum{

	PUB {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "公开访问";
		}
	},
	LOGIN {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "登陆后访问";
		}
	},
	AUTH {
		public Integer getCode() {
			return 3;
		}

		public String getName() {
			return "登陆后并授权访问";
		}
	}
}
