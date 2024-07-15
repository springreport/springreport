package com.springreport.api.onlinetplsheet;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.onlinetplsheet.OnlineTplSheet;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

 /**  
* @Description: OnlineTplSheet服务接口
* @author 
* @date 2023-02-06 08:03:30
* @version V1.0  
 */
public interface IOnlineTplSheetService extends IService<OnlineTplSheet> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(OnlineTplSheet model);

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
	BaseEntity insert(OnlineTplSheet model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(OnlineTplSheet model);
	
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
	 * @MethodName: updateAsync
	 * @Description: 异步更新
	 * @author caiyang
	 * @param model
	 * @param key
	 * @param version 
	 * @return void
	 * @date 2023-02-14 04:24:08 
	 */  
	void updateAsync(OnlineTplSheet model,String key, Long version,JSONArray cells,Long sheetId);
	
	/**  
	 * @MethodName: insertAsync
	 * @Description: 异步新增
	 * @author caiyang
	 * @param model 
	 * @return void
	 * @date 2023-02-16 08:15:46 
	 */  
	void insertAsync(OnlineTplSheet model);
	
	/**  
	 * @MethodName: deleteAsync
	 * @Description: 异步删除
	 * @author caiyang
	 * @param model 
	 * @return void
	 * @date 2023-02-16 10:25:56 
	 */  
	void deleteAsync(OnlineTplSheet model);
}
