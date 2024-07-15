package com.springreport.dto.sysmenu;

import java.util.List;

import com.springreport.base.BaseEntity;

import lombok.Data;

/**
* <p>Title: AuthTreeDto</p>
* <p>Description: 权限树用实体类</p>
* @author caiyang
* @date 2020年6月10日17:03:11
*/
@Data
public class AuthTreeDto extends BaseEntity{

	/**
	* @Feilds:treeData 权限树数据
	* @author caiyang
	*/  
	private List<MenuTreeDto> treeData;
	
	/**
	* @Feilds:authed 已经有的权限
	* @author caiyang
	*/  
	private List<String> authed;
}
