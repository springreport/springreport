package com.springreport.api.luckysheet;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.luckysheet.Luckysheet;

 /**  
* @Description: Luckysheet服务接口
* @author 
* @date 2023-08-12 05:57:21
* @version V1.0  
 */
public interface ILuckysheetService extends IService<Luckysheet> {

	/**  
	 * @MethodName: saveBatch
	 * @Description: 批量保存
	 * @author caiyang
	 * @param models void
	 * @date 2023-08-30 09:58:59 
	 */ 
	void saveBatchDatas(List<Luckysheet> models);
	
	/**  
	 * @MethodName: getOneButJsonData
	 * @Description: 获取一条数据除了json_data字段，如果json_data字段太大查询太慢
	 * @author caiyang
	 * @param jsonObject
	 * @return Luckysheet
	 * @date 2023-09-10 08:58:40 
	 */ 
	Luckysheet getOneButJsonData(JSONObject jsonObject);
}
