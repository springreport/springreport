package com.springreport.base;

import com.springreport.enums.YesNoEnum;

import lombok.Data;

/**  
 * @ClassName: SqlWhereDto
 * @Description: sql where条件用实体类
 * @author caiyang
 * @date 2021-06-04 07:32:53 
*/  
@Data
public class SqlWhereDto {

	/**  
	 * @Fields column : 列名
	 * @author caiyang
	 * @date 2021-06-04 07:33:34 
	 */ 
	private String column;
	
	/**  
	 * @Fields operator : 操作符
	 * @author caiyang
	 * @date 2021-06-04 07:34:14 
	 */ 
	private String operator;
	
	/**  
	 * @Fields paramValue : 参数值
	 * @author caiyang
	 * @date 2021-06-04 07:34:58 
	 */ 
	private Object paramValue;
	
	/**  
	 * @Fields expression : 表达式
	 * @author caiyang
	 * @date 2021-06-07 06:49:09 
	 */ 
	private String expression;
	
	/**  
	 * @Fields replaceExpression : 新的表达式
	 * @author caiyang
	 * @date 2021-06-07 06:57:13 
	 */ 
	private String replaceExpression;
	
	/**  
	 * @Fields tableName : 表名
	 * @author caiyang
	 * @date 2022-03-28 11:21:32 
	 */ 
	private String tableName;
	
	/**  
	 * @Fields isReplace : 旧表达式是否需要替换 1是 2否
	 * @author caiyang
	 * @date 2021-06-07 06:57:31 
	 */ 
	private Integer isReplace = YesNoEnum.NO.getCode();
	
}
