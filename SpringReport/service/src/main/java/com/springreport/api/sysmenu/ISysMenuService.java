package com.springreport.api.sysmenu;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.sysmenu.SysMenu;
import com.springreport.entity.sysrole.SysRole;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.sysmenu.AuthTreeDto;
import com.springreport.dto.sysmenu.IndexMenuTreeDto;

 /**  
* @Description: SysMenu服务接口
* @author 
* @date 2021-06-15 07:11:44
* @version V1.0  
 */
public interface ISysMenuService extends IService<SysMenu> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	List<SysMenu> tablePagingQuery(SysMenu model);

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
	BaseEntity insert(SysMenu model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(SysMenu model);
	
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
	 * @Title: getParentMenus
	 * @Description: 获取上级菜单
	 * @param sysMenu
	 * @return
	 * @author caiyang
	 * @date 2021-06-15 04:34:39 
	 */ 
	List<SysMenu> getParentMenus(SysMenu sysMenu);
	
	/**  
	 * @Title: getAuthTree
	 * @Description: 获取权限树
	 * @param sysRole
	 * @param userInfoDto
	 * @return
	 * @author caiyang
	 * @date 2021-06-15 06:28:29 
	 */ 
	AuthTreeDto getAuthTree(SysRole sysRole,UserInfoDto userInfoDto);
	
	/**  
	 * @Title: getIndexMenu
	 * @Description: 获取首页菜单
	 * @param userInfoDto
	 * @return
	 * @author caiyang
	 * @date 2021-06-18 01:34:31 
	 */ 
	List<IndexMenuTreeDto> getIndexMenu(UserInfoDto userInfoDto);
}
