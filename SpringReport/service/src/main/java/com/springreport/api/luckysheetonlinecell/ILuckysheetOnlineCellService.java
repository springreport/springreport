package com.springreport.api.luckysheetonlinecell;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.luckysheetonlinecell.LuckysheetOnlineCell;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

 /**  
* @Description: LuckysheetOnlineCell服务接口
* @author 
* @date 2023-02-06 08:03:17
* @version V1.0  
 */
public interface ILuckysheetOnlineCellService extends IService<LuckysheetOnlineCell> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(LuckysheetOnlineCell model);

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
	BaseEntity insert(LuckysheetOnlineCell model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(LuckysheetOnlineCell model);
	
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
	 * @MethodName: insertAsync
	 * @Description: 新增数据(异步)
	 * @author caiyang
	 * @param model
	 * @param key
	 * @param version
	 * @return 
	 * @return BaseEntity
	 * @date 2023-02-13 11:11:22 
	 */  
	void insertAsync(LuckysheetOnlineCell model,String key,Long version);
	
	/**  
	 * @MethodName: updateAsync
	 * @Description: 更新数据，异步
	 * @author caiyang
	 * @param model
	 * @param key
	 * @param version 
	 * @return void
	 * @date 2023-02-13 11:11:55 
	 */  
	void updateAsync(LuckysheetOnlineCell model,String key,Long version);
	
	/**  
	 * @MethodName: updateCoordinateAsync
	 * @Description: 异步更新坐标信息(增加行列)
	 * @author caiyang
	 * @param params
	 * @param sheetId 
	 * @return void
	 * @date 2023-02-16 03:10:31 
	 */  
	void updateAddCoordinateAsync(JSONObject params,Long sheetId);
	
	/**  
	 * @MethodName: updateMinusCoordinateAsync
	 * @Description: 异步更新坐标信息(删除行列)
	 * @author caiyang
	 * @param params
	 * @param sheetId 
	 * @return void
	 * @date 2023-02-16 04:04:34 
	 */  
	void updateMinusCoordinateAsync(JSONObject params,Long sheetId);
	
}
