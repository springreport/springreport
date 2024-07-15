package com.springreport.dto.reporttpldatasource;

import lombok.Data;

/**
* <p>Title: MesExecSqlDto</p>
* <p>Description: 执行sql用参数实体类</p>
* @author caiyang
* @date 2021年3月21日
*/
@Data
public class MesExecSqlDto {

	/**
	* @Feilds:tplSql sql语句
	* @author caiyang
	*/  
	private String tplSql;
	
	/**
	* @Feilds:tplId 模板id
	* @author caiyang
	*/  
	private Long tplId;
	
	/**
	* @Feilds:datasourceId 数据源id
	* @author caiyang
	*/  
	private Long datasourceId;
	
	/**  
	 * @Fields sqlType : sql类型 1标准sql 2存储过程
	 * @author caiyang
	 * @date 2021-10-29 08:03:06 
	 */ 
	private Integer sqlType;
	
	/**  
	 * @Fields inParam : 输入参数
	 * @author caiyang
	 * @date 2021-10-29 08:04:53 
	 */ 
	private String inParam;
	
	/**  
	 * @Fields outParam : 输出参数
	 * @author caiyang
	 * @date 2021-10-29 08:05:14 
	 */ 
	private String outParam;
	
	/**  
	 * @Fields params : sql参数
	 * @author caiyang
	 * @date 2022-12-15 10:25:14 
	 */  
	private String sqlParams;
	
}
