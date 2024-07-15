package com.springreport.mapper.luckysheet;
import com.springreport.entity.luckysheet.Luckysheet;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: LuckysheetMapper类
* @author 
* @date 2023-08-12 05:57:21
* @version V1.0  
 */
public interface LuckysheetMapper extends BaseMapper<Luckysheet>{

	void updateJsonData(JSONObject jsonObject);
	
	/**  
	 * @MethodName: updateJsonDataRowAndColumn
	 * @Description: 更新row和column配置信息
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-08-14 01:29:08 
	 */ 
	void updateJsonDataRowAndColumn(JSONObject jsonObject);
	
	/**  
	 * @MethodName: rmCellDataValue
	 * @Description: 清除指定层级下某条数据
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-08-14 07:52:38 
	 */ 
	boolean rmCellDataValue(JSONObject jsonObject);
	
	/**  
	 * @MethodName: updateJsonbForElementInsert
	 * @Description: jsonb数据中元素添加元素
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-08-15 08:54:38 
	 */ 
	void updateJsonbForElementInsert(JSONObject jsonObject);
	
	/**  
	 * @MethodName: config新增属性
	 * @Description: 通用更新
	 * @author caiyang
	 * @param jsonObject
	 * @return boolean
	 * @date 2023-08-15 11:19:32 
	 */ 
	boolean createConfigAttr(JSONObject jsonObject);
	
	/**  
	 * @MethodName: updateConfigAttr
	 * @Description: config属性更新
	 * @author caiyang
	 * @param jsonObject
	 * @return boolean
	 * @date 2023-08-15 01:44:56 
	 */ 
	boolean updateConfigAttr(JSONObject jsonObject);
	
	/**  
	 * @MethodName: dynamicUpdateJsonbData
	 * @Description: 更新
	 * @author caiyang
	 * @param jsonObject
	 * @return boolean
	 * @date 2023-08-15 03:38:20 
	 */ 
	boolean dynamicUpdateJsonbData(JSONObject jsonObject);
	
	/**  
	 * @MethodName: getMaxOrder
	 * @Description: 获取sheet最大的order
	 * @author caiyang
	 * @param luckysheet
	 * @return int
	 * @date 2023-08-30 09:13:51 
	 */ 
	int getMaxOrder(Luckysheet luckysheet);
	
	/**  
	 * @MethodName: getOneButJsonData
	 * @Description: 获取一条数据除了json_data字段，如果json_data字段太大查询太慢
	 * @author caiyang
	 * @param jsonObject
	 * @return Luckysheet
	 * @date 2023-09-10 09:07:03 
	 */ 
	Luckysheet getOneButJsonData(JSONObject jsonObject);
	
	/**  
	 * @MethodName: updateArrayInsert
	 * @Description: 数组后面新增数据
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-10-05 07:23:16 
	 */ 
	void updateArrayInsert(JSONObject jsonObject);
	
}
