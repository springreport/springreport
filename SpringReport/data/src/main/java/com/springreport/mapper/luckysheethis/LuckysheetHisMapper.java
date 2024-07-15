package com.springreport.mapper.luckysheethis;
import com.springreport.entity.luckysheethis.LuckysheetHis;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: LuckysheetHisMapper类
* @author 
* @date 2023-08-23 09:46:05
* @version V1.0  
 */
public interface LuckysheetHisMapper extends BaseMapper<LuckysheetHis>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<LuckysheetHis> searchDataLike(final LuckysheetHis model);
    
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
}
