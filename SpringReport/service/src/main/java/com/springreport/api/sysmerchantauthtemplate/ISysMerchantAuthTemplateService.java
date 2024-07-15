package com.springreport.api.sysmerchantauthtemplate;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.sysmerchantauthtemplate.SysMerchantAuthTemplate;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.dto.sysmenu.AuthTreeDto;
import com.springreport.dto.sysmerchantauthtemplate.SysMerchantAuthTemplateDto;

 /**  
* @Description: SysMerchantAuthTemplate服务接口
* @author 
* @date 2023-12-22 05:18:59
* @version V1.0  
 */
public interface ISysMerchantAuthTemplateService extends IService<SysMerchantAuthTemplate> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(SysMerchantAuthTemplate model);

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
	BaseEntity insert(SysMerchantAuthTemplateDto model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(SysMerchantAuthTemplateDto model);
	
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
	 * @MethodName: getMerchantTemplateAuthTree
	 * @Description: 获取租户权限模板权限树
	 * @author caiyang
	 * @param merchantAuthTemplate
	 * @return AuthTreeDto
	 * @date 2023-12-23 05:20:09 
	 */ 
	AuthTreeDto getMerchantTemplateAuthTree(SysMerchantAuthTemplate merchantAuthTemplate);
	
	/**  
	 * @MethodName: getAuthTemplates
	 * @Description: 获取所有的权限模板
	 * @author caiyang
	 * @return List<SysMerchantAuthTemplate>
	 * @date 2023-12-24 10:06:13 
	 */ 
	List<SysMerchantAuthTemplate> getAuthTemplates();
	
}
