package com.springreport.api.sysrole;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.sysrole.SysRole;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.dto.sysrole.MesAuthDto;

 /**  
* @Description: SysRole服务接口
* @author 
* @date 2021-06-15 07:11:49
* @version V1.0  
 */
public interface ISysRoleService extends IService<SysRole> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(SysRole model);

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
	BaseEntity insert(SysRole model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(SysRole model);
	
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
	 * @Title: getRoles
	 * @Description: 获取角色
	 * @return
	 * @author caiyang
	 * @date 2021-06-15 01:17:10 
	 */ 
	List<SysRole> getRoles(SysRole sysRole);
	
	/**  
	 * @Title: authed
	 * @Description: 角色菜单功能授权
	 * @param mesAuthDto
	 * @return
	 * @author caiyang
	 * @date 2021-06-16 07:02:14 
	 */ 
	BaseEntity authed(MesAuthDto mesAuthDto);
}
