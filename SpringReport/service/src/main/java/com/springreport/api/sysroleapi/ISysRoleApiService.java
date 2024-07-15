package com.springreport.api.sysroleapi;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.sysapi.SysApi;
import com.springreport.entity.sysroleapi.SysRoleApi;

import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

 /**  
* @Description: SysRoleApi服务接口
* @author 
* @date 2021-06-15 07:11:53
* @version V1.0  
 */
public interface ISysRoleApiService extends IService<SysRoleApi> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(SysRoleApi model);

	/**
	*<p>Title: getDetail</p>
	*<p>Description: 获取详情</p>
	* @author caiyang
	* @param id
	* @return
	*/
	BaseEntity getDetail(Long id);
	
	/**
	*<p>Title: insert</p>
	*<p>Description: 新增数据</p>
	* @author caiyang
	* @param model
	* @return
	*/
	BaseEntity insert(SysRoleApi model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(SysRoleApi model);
	
	/**
	*<p>Title: delete</p>
	*<p>Description: 单条删除数据</p>
	* @author caiyang
	* @param model
	* @return
	*/
	BaseEntity delete(Long id);
	
	/**
	*<p>Title: deleteBatch</p>
	*<p>Description: 批量删除数据</p>
	* @author caiyang
	* @param list
	* @return
	*/
	BaseEntity deleteBatch(List<Long> ids);
	
	/**  
	 * @Title: getApisByRole
	 * @Description: 根据角色获取接口权限
	 * @param roleId
	 * @return
	 * @author caiyang
	 * @date 2021-06-16 05:18:07 
	 */ 
	List<SysApi> getApisByRole(JSONObject jsonObject);
}
