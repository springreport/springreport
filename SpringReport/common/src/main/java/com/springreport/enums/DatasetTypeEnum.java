package com.springreport.enums;

import com.springreport.base.BaseIntEnum;

/**  
 * @ClassName: DatasetTypeEnum
 * @Description: 数据集类型
 * @author caiyang
 * @date 2021-10-25 08:28:32 
*/  
public enum DatasetTypeEnum implements BaseIntEnum{

	SQL {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "sql语句";
		}
	},
	MONGO {
		public Integer getCode() {
			return 3;
		}

		public String getName() {
			return "MONGODB";
		}
	},
	API {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "接口";
		}
	}
}
