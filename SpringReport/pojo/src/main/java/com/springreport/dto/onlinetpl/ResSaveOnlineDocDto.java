package com.springreport.dto.onlinetpl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

/**  
 * @ClassName: ResSaveCellDto
 * @Description: 在线文档保存数据用参数实体类
 * @author caiyang
 * @date 2023-02-07 02:44:56 
*/  
@Data
public class ResSaveOnlineDocDto {
	
	/**  
	 * @Fields pageId : 页面标识
	 * @author caiyang
	 * @date 2023-02-16 03:49:46 
	 */  
	private String pageId;
	
	/**  
	 * @Fields tplId : 文档id
	 * @author caiyang
	 * @date 2023-02-07 02:49:56 
	 */  
	private Long tplId;
	
	/**  
	 * @Fields sheetIndex : sheet页面索引
	 * @author caiyang
	 * @date 2023-02-07 02:49:35 
	 */  
	private String sheetIndex;
	
	/**  
	 * @Fields sheetName : sheet名称
	 * @author caiyang
	 * @date 2023-02-16 11:02:26 
	 */  
	private String sheetName;
	
	/**  
	 * @Fields sheetId : sheetid
	 * @author caiyang
	 * @date 2023-02-13 07:49:08 
	 */  
	private Long sheetId;
	
	/**  
	 * @Fields value : 单元格数据
	 * @author caiyang
	 * @date 2023-02-07 02:45:48 
	 */  
	private List<JSONObject> cellDatas;
	
	/**  
	 * @Fields operateType : 操作类型
	 * @author caiyang
	 * @date 2023-02-07 04:31:09 
	 */  
	private String type;
	
	/**  
	 * @Fields taskId : 任务id
	 * @author caiyang
	 * @date 2023-02-08 05:03:21 
	 */  
	private String taskId;
	
	/**  
	 * @Fields formatType : 格式类型
	 * @author caiyang
	 * @date 2023-02-09 09:49:09 
	 */  
	private String formatType;
	
	/**  
	 * @Fields merge : 单元格合并信息
	 * @author caiyang
	 * @date 2023-02-10 08:27:33 
	 */  
	private Map<String, Object> merge;
	
	/**  
	 * @Fields version : 消息版本号
	 * @author caiyang
	 * @date 2023-02-13 07:24:28 
	 */  
	private Long version;
	
	/**  
	 * @Fields sheetOrder : sheet order
	 * @author caiyang
	 * @date 2023-02-13 07:56:50 
	 */  
	private Long sheetOrder;
	
	/**  
	 * @Fields cells : 合并单元格信息
	 * @author caiyang
	 * @date 2023-02-14 02:57:38 
	 */  
	private List<List<Integer>> cells;
	
	/**  
	 * @Fields borderInfo : 单元格信息
	 * @author caiyang
	 * @date 2023-02-15 03:05:23 
	 */  
	private List<Map<String, Object>> borderInfo;
	
	/**  
	 * @Fields rowlen : 行高
	 * @author caiyang
	 * @date 2023-02-15 04:29:31 
	 */  
	private Map<String, Object> rowlen;
	
	/**  
	 * @Fields columnlen : 列宽
	 * @author caiyang
	 * @date 2023-02-15 04:29:50 
	 */  
	private Map<String, Object> columnlen;
	
	/**  
	 * @Fields sheetConfig : 新sheet页配置
	 * @author caiyang
	 * @date 2023-02-16 07:57:39 
	 */  
	private JSONObject sheetConfig;
	
	/**  
	 * @Fields insertParams : 插入行或者列的参数
	 * @author caiyang
	 * @date 2023-02-16 02:30:53 
	 */  
	private JSONObject insertParams;
	
	/**  
	 * @Fields delParams : 删除行或者列的参数
	 * @author caiyang
	 * @date 2023-02-16 02:31:16 
	 */  
	private JSONObject delParams;
}
