package com.springreport.dto.reporttpldataset;

import com.springreport.base.BaseEntity;

import lombok.Data;

/**
* <p>Title: SqlParameterDto</p>
* <p>Description: sql参数用实体类</p>
* @author caiyang
* @date 2020年3月7日
*/
@Data
public class SqlParameterDto extends BaseEntity{

	/**
	* @Feilds:paramName 参数名称
	* @author caiyang
	*/  
	private String paramName;
	
	/**
	* @Feilds:paramCode 参数代码
	* @author caiyang
	*/  
	private String paramCode;
	
	/**
	* @Feilds:paramType 参数类型
	* @author caiyang
	*/  
	private String paramType;
	
	/**
	* @Feilds:paramDefault 默认值
	* @author caiyang
	*/  
	private String paramDefault;
	
	/**
	* @Feilds:paramRequired 是否必须
	* @author caiyang
	*/  
	private String paramRequired;
}
