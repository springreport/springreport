package com.springreport.api.sysmerchant;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.sysmerchant.SysMerchant;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.dto.sysmerchant.SysMerchantDto;

 /**  
* @Description: SysMerchant服务接口
* @author 
* @date 2023-12-22 05:18:53
* @version V1.0  
 */
public interface ISysMerchantService extends IService<SysMerchant> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(SysMerchant model);

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
	BaseEntity insert(SysMerchantDto model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(SysMerchantDto model);
	
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
	 * @MethodName: getMerchantList
	 * @Description: 获取所有的商户列表
	 * @author caiyang
	 * @return List<SysMerchant>
	 * @date 2023-12-23 11:32:41 
	 */ 
	List<SysMerchant> getMerchantList();
	
	/**  
	 * @MethodName: getMerchantAuthApi
	 * @Description: 获取商户有权限的按钮权限
	 * @author caiyang
	 * @param merchant
	 * @return List<Long>
	 * @date 2023-12-27 08:46:28 
	 */ 
	List<Long> getMerchantAuthApi(SysMerchant merchant);
}
