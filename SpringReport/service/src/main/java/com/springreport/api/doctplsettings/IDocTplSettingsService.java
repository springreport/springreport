package com.springreport.api.doctplsettings;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.doctplsettings.DocTplSettings;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

 /**  
* @Description: DocTplSettings服务接口
* @author 
* @date 2024-05-02 08:55:39
* @version V1.0  
 */
public interface IDocTplSettingsService extends IService<DocTplSettings> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(DocTplSettings model);

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
	BaseEntity insert(DocTplSettings model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(DocTplSettings model);
	
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
