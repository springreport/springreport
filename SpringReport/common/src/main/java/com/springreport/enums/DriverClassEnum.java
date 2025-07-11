/*   
 * Copyright (c) 2016-2020 canaanQd. All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * canaanQd. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with canaanQd.   
 *   
 */ 

package com.springreport.enums;

import com.springreport.base.BaseIntEnum;

/** 
* @ClassName: DriverClassEnum
* @Description: 驱动类型用枚举类
* @author joseph
* @date 2021年2月13日10:21:27
*  
*/
public enum DriverClassEnum implements BaseIntEnum{

	MYSQL {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "com.mysql.cj.jdbc.Driver";
		}
	},
	SQLSERVER {
		public Integer getCode() {
			return 3;
		}

		public String getName() {
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}
	},
	ORACLE {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "oracle.jdbc.driver.OracleDriver";
		}
	},
	POSTGRESQL {
		public Integer getCode() {
			return 5;
		}

		public String getName() {
			return "org.postgresql.Driver";
		}
	},
	INFLUXDB {
		public Integer getCode() {
			return 6;
		}

		public String getName() {
			return "influxdb";
		}
	},
	DAMENG {
		public Integer getCode() {
			return 7;
		}

		public String getName() {
			return "dm.jdbc.driver.DmDriver";
		}
	},
	CLICKHOUSE {
		public Integer getCode() {
			return 8;
		}

		public String getName() {
			return "ru.yandex.clickhouse.ClickHouseDriver";
		}
	},
	TDENGINE {
		public Integer getCode() {
			return 10;
		}

		public String getName() {
			return "com.taosdata.jdbc.TSDBDriver";
		}
	},
	KINGBASE {
		public Integer getCode() {
			return 11;
		}

		public String getName() {
			return "com.kingbase8.Driver";
		}
	},
	HIGODB {
		public Integer getCode() {
			return 12;
		}

		public String getName() {
			return "com.highgo.jdbc.Driver";
		}
	},
	DORIS {
		public Integer getCode() {
			return 13;
		}

		public String getName() {
			return "com.mysql.cj.jdbc.Driver";
		}
	},
	MONGODB {
		public Integer getCode() {
			return 14;
		}

		public String getName() {
			return "com.mongodb.jdbc.MongoDriver";
		}
	},
}
