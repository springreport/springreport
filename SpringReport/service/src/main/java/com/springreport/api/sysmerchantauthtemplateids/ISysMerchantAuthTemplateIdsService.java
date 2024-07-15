package com.springreport.api.sysmerchantauthtemplateids;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.sysmerchantauthtemplateids.SysMerchantAuthTemplateIds;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

 /**  
* @Description: SysMerchantAuthTemplateIds服务接口
* @author 
* @date 2023-12-22 05:19:05
* @version V1.0  
 */
public interface ISysMerchantAuthTemplateIdsService extends IService<SysMerchantAuthTemplateIds> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(SysMerchantAuthTemplateIds model);

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
	BaseEntity insert(SysMerchantAuthTemplateIds model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(SysMerchantAuthTemplateIds model);
	
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
}
