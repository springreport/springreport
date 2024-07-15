package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**  
 * @ClassName: SystemDatasourceTypeEnum
 * @Description: 系统数据库类型
 * @author caiyang
 * @date 2021年11月23日17:24:03
*/  
public enum SystemDatasourceTypeEnum implements BaseCharEnum{

	MYSQL {
		public String getCode() {
			return "mysql";
		}

		public String getName() {
			return "MYSQL";
		}
	},
	POSTGRESQL {
		public String getCode() {
			return "postgresql";
		}

		public String getName() {
			return "postgresql";
		}
	},
	KINGBASE {
		public String getCode() {
			return "kingbase";
		}

		public String getName() {
			return "人大金仓";
		}
	},
	ORACLE {
		public String getCode() {
			return "oracle";
		}

		public String getName() {
			return "oracle";
		}
	},
	DM {
		public String getCode() {
			return "dm";
		}

		public String getName() {
			return "达梦";
		}
	},
	SQLSERVER {
		public String getCode() {
			return "sqlserver";
		}

		public String getName() {
			return "sqlserver";
		}
	},
}
