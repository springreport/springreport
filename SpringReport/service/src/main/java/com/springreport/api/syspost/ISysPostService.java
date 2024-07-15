package com.springreport.api.syspost;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.syspost.SysPost;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;

 /**  
* @Description: SysPost服务接口
* @author 
* @date 2023-12-22 05:19:10
* @version V1.0  
 */
public interface ISysPostService extends IService<SysPost> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(SysPost model);

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
	BaseEntity insert(SysPost model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(SysPost model);
	
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
	 * @MethodName: getSysPosts
	 * @Description: 获取所有岗位
	 * @author caiyang
	 * @return List<SysPost>
	 * @date 2023-12-26 03:52:02 
	 */ 
	List<SysPost> getSysPosts(SysPost sysPost);
}
