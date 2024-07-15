package com.springreport.api.luckysheethis;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.luckysheet.Luckysheet;
import com.springreport.entity.luckysheethis.LuckysheetHis;
import com.springreport.dto.coedit.WSUserModel;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

 /**  
* @Description: LuckysheetHis服务接口
* @author 
* @date 2023-08-23 09:46:05
* @version V1.0  
 */
public interface ILuckysheetHisService extends IService<LuckysheetHis> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(LuckysheetHis model);

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
	BaseEntity insert(LuckysheetHis model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(LuckysheetHis model);
	
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
	
//	/**  
//	 * @MethodName: saveCellHisOne
//	 * @Description: 保存单元格修改历史记录(单条)
//	 * @author caiyang
//	 * @param beforeData
//	 * @param newData
//	 * @param type void
//	 * @date 2023-08-23 09:55:28 
//	 */ 
//	void saveCellHisOne(JSONObject beforeData,JSONObject newData,WSUserModel wsUserModel,String index,String listId,String remark);
//
//	/**  
//	 * @MethodName: saveBatchUpdate(批量操作单元格)
//	 * @Description: 批量更新单元格记录历史记录
//	 * @author caiyang
//	 * @param originalExistsBlock
//	 * @param _existsBlock
//	 * @param wsUserModel
//	 * @param index
//	 * @param listId void
//	 * @date 2023-08-24 10:01:49 
//	 */ 
//	void saveBatchUpdate(HashMap<String, JSONObject> originalExistsBlock,HashMap<String, JSONObject> _existsBlock,WSUserModel wsUserModel,String index,String listId,String remark);
//
//	/**  
//	 * @MethodName: saveBatchInsert
//	 * @Description: 批量插入单元格更新历史记录(批量操作单元格)
//	 * @author caiyang
//	 * @param _notexistsBlock
//	 * @param wsUserModel
//	 * @param index
//	 * @param listId void
//	 * @date 2023-08-24 10:42:31 
//	 */ 
//	void saveBatchInsert(HashMap<String, JSONObject> _notexistsBlock,WSUserModel wsUserModel,String index,String listId,String remark);
//
//	/**  
//	 * @MethodName: batchDelCells
//	 * @Description: 增加行列引起的批量删除单元格
//	 * @author caiyang
//	 * @param lists void
//	 * @date 2023-08-24 01:31:42 
//	 */ 
//	void batchDelCells(List<Luckysheet> lists, WSUserModel wsUserModel, String index,String listId,String remark);
//	
//	/**  
//	 * @MethodName: saveBatchInsert
//	 * @Description: 增加行列引起的批量新增单元格
//	 * @author caiyang
//	 * @param lists
//	 * @param wsUserModel
//	 * @param index
//	 * @param listId
//	 * @param remark void
//	 * @date 2023-08-24 05:01:04 
//	 */ 
//	void saveBatchInsert(List<Luckysheet> lists, WSUserModel wsUserModel, String index,String listId,String remark);
//
//	/**  
//	 * @MethodName: saveSheetSettings
//	 * @Description: 保存sheet配置信息修改历史
//	 * @author caiyang
//	 * @param beforeData
//	 * @param newData
//	 * @param key
//	 * @param wsUserModel
//	 * @param index
//	 * @param listId
//	 * @param remark void
//	 * @date 2023-08-24 08:20:04 
//	 */ 
//	void saveSheetSettings(JSONObject beforeData,Object newDataObject,String key, WSUserModel wsUserModel, String index,String listId,String operate);
//
//	/**  
//	 * @MethodName: saveSheetDelConfig
//	 * @Description: 删除config中的属性
//	 * @author caiyang
//	 * @param beforeData
//	 * @param newData
//	 * @param key
//	 * @param wsUserModel
//	 * @param index
//	 * @param listId
//	 * @param operate void
//	 * @date 2023-08-27 07:59:56 
//	 */ 
//	void saveSheetDelConfig(JSONObject beforeData,JSONObject newData,String key, WSUserModel wsUserModel, String index,String listId,String operate);

}
