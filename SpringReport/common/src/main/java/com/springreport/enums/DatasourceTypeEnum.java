package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**  
 * @ClassName: DatasourceTypeEnum
 * @Description: 数据来源类型
 * @author caiyang
 * @date 2021年11月23日17:24:03
*/  
public enum DatasourceTypeEnum implements BaseCharEnum{

	CUSTOM {
		public String getCode() {
			return "1";
		}

		public String getName() {
			return "自定义";
		}
	},
	API {
		public String getCode() {
			return "2";
		}

		public String getName() {
			return "接口";
		}
	},
	SQL {
		public String getCode() {
			return "3";
		}

		public String getName() {
			return "sql数据集";
		}
	},
}
