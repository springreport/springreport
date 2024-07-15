package com.springreport.api.sysapi;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.sysapi.SysApi;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.dto.sysmenu.MenuTreeDto;
import com.springreport.dto.sysmenu.MesGetAuthTreeDto;

 /**  
* @Description: SysApi服务接口
* @author 
* @date 2021-06-15 07:11:40
* @version V1.0  
 */
public interface ISysApiService extends IService<SysApi> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(SysApi model);

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
	BaseEntity insert(SysApi model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(SysApi model);
	
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
	 * @Title: getMenuApis
	 * @Description: 获取菜单下需授权的接口
	 * @param mesGetAuthTreeDto
	 * @return
	 * @author caiyang
	 * @date 2021-06-16 06:57:26 
	 */ 
	List<MenuTreeDto> getMenuApis(MesGetAuthTreeDto mesGetAuthTreeDto);
}
