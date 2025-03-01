package com.springreport.dto.reporttpl;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class ReportDataDto {

	/**  
	 * @Fields reportDatas : 上报的数据
	 * @author caiyang
	 * @date 2022-11-22 07:04:24 
	 */  
	private Map<String, Map<String, Object>> reportDatas;
	
	/**  
	 * @Fields datasKey : 主键信息
	 * @author caiyang
	 * @date 2022-11-22 07:05:59 
	 */  
	private Map<String, Map<String, Object>> datasKey;
	
	/**  
	 * @Fields basicDatas : 原始数据
	 * @author caiyang
	 * @date 2023-01-29 03:58:35 
	 */  
	private Map<String, Map<String, Object>> basicDatas;
	
	/**  
	 * @Fields tplId : 模板id
	 * @author caiyang
	 * @date 2023-01-29 05:46:41 
	 */  
	private Long tplId;
	
	/**  
	 * @Fields version : 当前更新版本号，防止重复提交覆盖数据
	 * @author caiyang
	 * @date 2023-02-01 02:29:00 
	 */  
	private Long version;
	
	/**  
	 * @Fields reCalculate : 需要重新计算的列数据
	 * @author caiyang
	 * @date 2023-07-26 08:14:10 
	 */  
	private JSONObject reCalculate;
	
	/**  
	 * @Fields autoFillAttrs : 自动填充属性
	 * @author caiyang
	 * @date 2025-02-05 09:15:01 
	 */  
	private JSONObject autoFillAttrs;
	
	/**  
	 * @Fields mainAttrs : 主子表配置
	 * @author caiyang
	 * @date 2025-02-27 08:51:09 
	 */  
	private JSONObject mainAttrs;
	
	/**  
	 * @Fields mainDatasources : 主数据的数据源
	 * @author caiyang
	 * @date 2025-02-27 08:50:58 
	 */  
	private JSONObject mainDatasources;
	
	/**  
	 * @Fields mainRowDatas : 主数据源数据
	 * @author caiyang
	 * @date 2025-02-28 01:51:37 
	 */  
	private JSONObject mainRowDatas;
}
