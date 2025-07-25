package com.springreport.base;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.enums.YesNoEnum;

import lombok.Data;

/**  
 * @ClassName: MesSheetConfig
 * @Description: 导出excel 单个sheet配置
 * @author caiyang
 * @date 2022-10-21 05:10:18 
*/  
@Data
public class MesSheetConfig {

	/**  
	 * @Fields cellDatas : 单元格数据
	 * @author caiyang
	 * @date 2022-10-21 05:11:06 
	 */  
	private List<Map<String, Object>> cellDatas;
	
	/**  
	 * @Fields maxXAndY : 最大行最大列，创建sheet行列用
	 * @author caiyang
	 * @date 2022-10-21 05:11:34 
	 */  
	private Map<String, Integer> maxXAndY;
	
	/**  
	 * @Fields hyperlinks : 超链接配置
	 * @author caiyang
	 * @date 2022-10-21 05:12:05 
	 */  
	private Map<String, Map<String, Object>> hyperlinks;
	
	/**  
	 * @Fields borderInfos : 边框信息
	 * @author caiyang
	 * @date 2022-10-21 05:13:33 
	 */  
	private List<Object> borderInfos;
	
	/**  
	 * @Fields rowlen : 行高信息
	 * @author caiyang
	 * @date 2022-10-21 05:13:50 
	 */  
	private Map<String, Object> rowlen;
	
	/**  
	 * @Fields columnlen : 列宽信息
	 * @author caiyang
	 * @date 2022-10-21 05:15:24 
	 */  
	private Map<String, Object> columnlen;
	
	/**  
	 * @Fields sheetname : sheet名称
	 * @author caiyang
	 * @date 2022-10-21 05:17:34 
	 */  
	private String sheetname;
	
	/**  
	 * @Fields frozen : 行列冻结信息
	 * @author caiyang
	 * @date 2022-10-21 05:18:03 
	 */  
	private JSONObject frozen;
	
	/**  
	 * @Fields images : 图片信息
	 * @author caiyang
	 * @date 2022-10-21 05:18:22 
	 */  
	private JSONObject images;
	
	/**  
	 * @Fields base64Images : base64格式图片
	 * @author caiyang
	 * @date 2023-01-09 02:44:59 
	 */  
	private JSONObject base64Images;
	
	/**  
	 * @Fields imageDatas : 图片数据
	 * @author caiyang
	 * @date 2023-01-06 02:31:43 
	 */  
	private List<JSONObject> imageDatas;
	
	/**  
	 * @Fields chart : 图表
	 * @author caiyang
	 * @date 2023-04-18 11:07:01 
	 */  
	private JSONArray chart;
	
	/**  
	 * @Fields chartCells : 图表对应的单元格坐标信息
	 * @author caiyang
	 * @date 2023-04-26 04:33:58 
	 */  
	private JSONObject chartCells;
	
	/**  
	 * @Fields colhidden : 隐藏列
	 * @author caiyang
	 * @date 2023-04-27 05:16:08 
	 */  
	private JSONObject colhidden;
	
	/**  
	 * @Fields rowhidden : 隐藏行
	 * @author caiyang
	 * @date 2023-04-27 05:16:32 
	 */  
	private JSONObject rowhidden;
	
	/**  
	 * @Fields dataVerification : 数据校验
	 * @author caiyang
	 * @date 2023-05-04 10:20:24 
	 */  
	private JSONObject dataVerification;
	
	/**  
	 * @Fields authority : 保护信息
	 * @author caiyang
	 * @date 2023-05-06 02:41:41 
	 */  
	private JSONObject authority;
	
	/**  
	 * @Fields isCoedit : 是否是协同编辑 默认否
	 * @author caiyang
	 * @date 2023-08-31 05:11:04 
	 */  
	private Integer isCoedit = YesNoEnum.NO.getCode();
	
	/**  
	 * @Fields merge : 合并单元格信息
	 * @author caiyang
	 * @date 2023-08-31 05:11:35 
	 */  
	private JSONObject merge;
	
	/**  
	 * @Fields filter : 过滤信息
	 * @author caiyang
	 * @date 2023-09-04 10:57:41 
	 */  
	private JSONObject filter;
	
	/**  
     * @Fields luckysheetConditionformatSave : 条件格式配置
     * @author caiyang
     * @date 2024年8月16日18:54:55
     */  
    private JSONArray luckysheetConditionformatSave;
    
    /**  
     * @Fields wrapText : 自动换行的行标记
     * @author caiyang
     * @date 2024-10-13 03:50:46 
     */  
    private Map<String, Object> wrapText;
    
    /**  
     * @Fields sheetIndex : sheet唯一标识
     * @author caiyang
     * @date 2025-01-20 09:21:12 
     */  
    private String sheetIndex;
    
    /**  
	 * @Fields noViewAuthCells : 没有查看权限的单元格
	 * @author caiyang
	 * @date 2025-01-20 08:29:21 
	 */  
	private List<String> noViewAuthCells;
}
