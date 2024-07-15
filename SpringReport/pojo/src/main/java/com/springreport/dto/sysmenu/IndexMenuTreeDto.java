package com.springreport.dto.sysmenu;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.springreport.base.BaseEntity;

import lombok.Data;

/**  
 * @ClassName: IndexMenuTreeDto
 * @Description: 首页菜单树用实体类
 * @author caiyang
 * @date 2020-06-11 05:09:23 
*/  
@Data
public class IndexMenuTreeDto extends BaseEntity{

	/**
	* @Feilds:id
	* @author caiyang
	*/  
	private Long id;
	
	/**
	* @Fields icon 菜单图标
	* @author caiyang
	*/  
	private String icon;
	
	/**
	* @Fields path 菜单路径
	* @author caiyang
	*/  
	private String path;
	
	/**
	* @Fields title 菜单标题
	* @author caiyang
	*/  
	private String title;
	
	private boolean page = true;;
	
	/**
	* @Fields subs 子菜单
	* @author caiyang
	*/  
	private List<IndexMenuTreeDto> subs;
	
	/**  
	 * @Fields clazz : class属性
	 * @author caiyang
	 * @date 2020-07-07 07:48:01 
	 */ 
	private String clazz;
	
}
