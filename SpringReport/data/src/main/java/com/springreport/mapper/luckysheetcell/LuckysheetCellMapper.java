package com.springreport.mapper.luckysheetcell;
import com.springreport.entity.luckysheetcell.LuckysheetCell;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: LuckysheetCellMapper类
* @author 
* @date 2023-10-07 03:19:47
* @version V1.0  
 */
public interface LuckysheetCellMapper extends BaseMapper<LuckysheetCell>{

	/**  
	 * @MethodName: delRowUpdate
	 * @Description: 删除行更新坐标
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-10-24 03:43:09 
	 */ 
	void delRowUpdate(JSONObject jsonObject);
	
	/**  
	 * @MethodName: delColUpdate
	 * @Description: 删除列更新坐标
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-10-24 03:43:22 
	 */ 
	void delColUpdate(JSONObject jsonObject);
	
	/**  
	 * @MethodName: delRowDelCells
	 * @Description: 删除行删除对应的单元格数据
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-10-26 12:22:33 
	 */ 
	void delRowDelCells(JSONObject jsonObject);
	
	/**  
	 * @MethodName: delColDelCells
	 * @Description: 删除列删除对应的单元格数据
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-10-26 12:23:02 
	 */ 
	void delColDelCells(JSONObject jsonObject);
	
	/**  
	 * @MethodName: addRowUpdate
	 * @Description: 添加行更新单元格数据
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-10-28 10:03:07 
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
	
	/**  
	 * @MethodName: updateCellFormulaSheetName
	 * @Description: 修改sheet名称时更新单元格函数中的sheet名称
	 * @author caiyang
	 * @param jsonObject void
	 * @date 2023-11-09 11:01:13 
	 */ 
	void updateCellFormulaSheetName(JSONObject jsonObject);
}
