package com.springreport.api.luckysheetcell;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.luckysheetcell.LuckysheetCell;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

 /**  
* @Description: LuckysheetCell服务接口
* @author 
* @date 2023-10-07 03:19:47
* @version V1.0  
 */
public interface ILuckysheetCellService extends IService<LuckysheetCell> {

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
	BaseEntity insert(LuckysheetCell model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(LuckysheetCell model);
	
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
	 * @MethodName: delRowUpdate
	 * @Description: 删除行更新坐标
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-10-24 03:41:31 
	 */ 
	void delRowUpdate(JSONObject jsonObject);
	
	/**  
	 * @MethodName: delColUpdate
	 * @Description: 删除列更新坐标
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-10-24 03:41:54 
	 */ 
	void delColUpdate(JSONObject jsonObject);
	
	/**  
	 * @MethodName: delRowDelCells
	 * @Description: 删除行删除对应的单元格数据
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-10-26 12:21:01 
	 */ 
	void delRowDelCells(JSONObject jsonObject);
	
	/**  
	 * @MethodName: delColDelCells
	 * @Description: 删除列删除对应的单元格数据
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-10-26 12:21:29 
	 */ 
	void delColDelCells(JSONObject jsonObject);
	
	/**  
	 * @MethodName: addRowUpdate
	 * @Description: 添加行更新单元格数据
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-10-28 10:01:15 
	 */ 
	void addRowUpdate(JSONObject jsonObject);
	
	/**  
	 * @MethodName: addColUpdate
	 * @Description: 添加列更新单元格数据
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-10-28 10:01:59 
	 */ 
	void addColUpdate(JSONObject jsonObject);
}
