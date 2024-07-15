package com.springreport.enums;

import com.springreport.base.BaseIntEnum;

/**  
 * @ClassName: MenuRuleEnum
 * @Description: 菜单访问权限规则枚举类
 * @author caiyang
 * @date 2020-06-08 09:00:22 
*/  
public enum MenuRuleEnum implements BaseIntEnum{

	PUB {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "公开访问";
		}
	},
	AUTH {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "登陆后并授权访问";
		}
	}
}
