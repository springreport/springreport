package com.springreport.dto.reporttpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.entity.reportsheetpdfprintsetting.ReportSheetPdfPrintSetting;

import lombok.Data;

@Data
public class ResLuckySheetDataDto {
	
	/**  
	 * @Fields sheetId : 当前sheet页id
	 * @author caiyang
	 * @date 2025-05-06 11:47:50 
	 */  
	private Long sheetId;
	
	/**  
	 * @Fields hyperlinks : 超链接
	 * @author caiyang
	 * @date 2022-02-01 10:48:03 
	 */ 
	private Map<String, Map<String, Object>> hyperlinks;
	
	/**  
	 * @Fields cellDatas : 单元格数据
	 * @author caiyang
	 * @date 2022-02-01 10:49:04 
	 */ 
	private List<Map<String, Object>> cellDatas;
	
	/**  
	 * @Fields config : sheet页面的配置信息，例如行宽行高
	 * @author caiyang
	 * @date 2022-02-10 07:24:32 
	 */ 
	private Map<String, Object> config;
	
	/**  
	 * @Fields paginationMap : 分页信息
	 * @author caiyang
	 * @date 2022-02-10 07:23:50 
	 */ 
	private Map<String, Map<String, Long>> pagination;
	
	/**  
	 * @Fields maxXAndY : 最大横坐标和纵坐标
	 * @author caiyang
	 * @date 2022-02-14 02:07:19 
	 */ 
	private Map<String, Integer> maxXAndY;
	
	
	/**  
	 * @Fields mergePagination : 合并参数分页内容
	 * @author caiyang
	 * @date 2022-03-31 10:44:16 
	 */ 
	private Map<String, Object> mergePagination;
	
	/**  
	 * @Fields frozen : 冻结信息
	 * @author caiyang
	 * @date 2022-05-05 15:18:43 
	 */ 
	private Map<String, Object> frozen;
	
	/**  
	 * @Fields images : 图片
	 * @author caiyang
	 * @date 2022-06-02 02:34:14 
	 */  
	private Object images;
	
	/** sheet_index - sheet页唯一索引 */
    private String sheetIndex;

    /** calc_chain - 计算链，有公式的单元格信息 */
    private List<JSONObject> calcChain;
    
    /**  
     * @Fields sheetName : sheet名称
     * @author caiyang
     * @date 2022-10-20 06:17:10 
     */  
    private String sheetName;
    
    /**  
     * @Fields sheetOrder : sheet顺序
     * @author caiyang
     * @date 2022-10-21 04:14:34 
     */  
    private Integer sheetOrder;
    
    /**  
     * @Fields datasourceConfig : 绑定数据配置
     * @author caiyang
     * @date 2022年11月17日08:29:39
     */  
//    private JSONArray datasourceConfig;
    
    /**  
     * @Fields extendCellOrigin : 扩展单元格的坐标与原始单元格坐标的对应关系
     * @author caiyang
     * @date 2022-11-18 04:44:01 
     */  
    private Map<String, JSONObject> extendCellOrigin;
    
    /**  
	 * @Fields extraCustomCellConfigs : 自定义额外单元格配置
	 * @author caiyang
	 * @date 2022-02-01 10:49:34 
	 */ 
	private Map<String, JSONObject> extraCustomCellConfigs;
	
	/**  
	 * @Fields columnStartCoords : 列对应的起始坐标
	 * @author caiyang
	 * @date 2022-11-22 07:34:52 
	 */  
	private Map<String, JSONObject> columnStartCoords;
	
	/**  
	 * @Fields cellDatasourceConfig : 单元格数据源配置对应关系
	 * @author caiyang
	 * @date 2022-11-22 02:11:37 
	 */  
	private List<Map<String, JSONObject>> cellDatasourceConfigs;
	
	/**  
	 * @Fields tableKeys : 表格主键
	 * @author caiyang
	 * @date 2022-11-22 02:13:26 
	 */  
	private Map<String, JSONObject> tableKeys;
	
	/**  
	 * @Fields imageDatas : 图片数据
	 * @author caiyang
	 * @date 2023-01-06 02:31:43 
	 */  
	private List<JSONObject> imageDatas;
	
	
	/**  
	 * @Fields base64Imgs : base64图片
	 * @author caiyang
	 * @date 2023-01-09 02:42:55 
	 */  
	private JSONObject base64Imgs;
	
	/**  
	 * @Fields imgCells : 有图片的单元格
	 * @author caiyang
	 * @date 2023-01-09 05:42:34 
	 */  
	private Map<String, JSONObject> imgCells;
	
	/**  
	 * @Fields allowEditConfigs : 是否允许单元格进行编辑配置信息
	 * @author caiyang
	 * @date 2023-01-12 09:03:37 
	 */  
	Map<String, Boolean> allowEditConfigs;
	
	/**  
	 * @Fields nowFunction : now函数
	 * @author caiyang
	 * @date 2023-03-31 09:33:15 
	 */  
	private Map<String, Object> nowFunction = new HashMap<>();
	
	/**  
	 * @Fields startXAndY : 起始横坐标和纵坐标
	 * @author caiyang
	 * @date 2023-04-04 02:29:37 
	 */  
	private Map<String, Integer> startXAndY = new HashMap<>();
	
	/**  
	 * @Fields functionCellFormat : 函数格式
	 * @author caiyang
	 * @date 2023-04-09 01:02:41 
	 */  
	private Map<String, Object> functionCellFormat = new HashMap<>();
	
	/**  
	 * @Fields chart : 图表
	 * @author caiyang
	 * @date 2023-04-18 11:07:01 
	 */  
	private JSONArray chart;
	
	/**  
	 * @Fields chartCells : 图表坐标
	 * @author caiyang
	 * @date 2023-04-26 07:43:44 
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
	 * @Fields drillCells : 下钻单元格
	 * @author caiyang
	 * @date 2023-05-09 10:58:34 
	 */  
	private JSONObject drillCells;
	
	/**  
	 * @Fields xxbtScreenshot : 斜线表头截图
	 * @author caiyang
	 * @date 2023-11-29 12:09:53 
	 */  
	private JSONObject xxbtScreenshot;
	
	/**  
	 * @Fields printSettings : pdf/打印设置
	 * @author caiyang
	 * @date 2023-12-08 09:32:15 
	 */  
	private ReportSheetPdfPrintSetting printSettings;
	
	/**  
	 * @Fields imgInfo : 插入图片信息
	 * @author caiyang
	 * @date 2023-12-14 12:02:39 
	 */  
	private JSONObject imgInfo;
	
	/**  
	 * @Fields replacedData : 被缓存替换的数据
	 * @author caiyang
	 * @date 2024-01-16 12:59:45 
	 */  
	private Map<String, Object> replacedData; 
	
	/**  
	 * @author caiyang
	 * @date 2022-02-01 10:49:04 
	 */ 
	private List<Object> newCellDatas;
	
	/**  
	 * @Fields cellBindData : 坐标对应的单元格数据
	 * @author caiyang
	 * @date 2024-02-12 10:49:08 
	 */  
	private Map<String, LuckySheetBindData> cellBindData;
	
	/**  
	 * @Fields pageDivider : 分页线配置
	 * @author caiyang
	 * @date 2024-02-26 04:45:47 
	 */  
	private JSONArray pageDivider;
	
	/**  
     * @Fields luckysheetConditionformatSave : 条件格式配置
     * @author caiyang
     * @date 2024年8月16日18:54:55
     */  
    private JSONArray luckysheetConditionformatSave;
    
    /**  
     * @Fields wrapDatas : 自动换行的数据
     * @author caiyang
     * @date 2024-09-06 09:09:37 
     */  
    private JSONArray wrapDatas;
    
    /**  
     * @Fields autoFillAttrs : 自动填充属性
     * @author caiyang
     * @date 2025-02-05 08:50:22 
     */  
    private Map<String, JSONObject> autoFillAttrs;
    
    /**  
     * @Fields deleteTypes : 删除规则
     * @author caiyang
     * @date 2025-02-17 02:58:15 
     */  
    private Map<String, JSONObject> deleteTypes;
    
    /**  
     * @Fields mainDatasource : 主表数据源
     * @author caiyang
     * @date 2025-02-27 05:00:39 
     */  
    private Map<String,Object> mainDatasource;
    
    /**  
     * @Fields mainAttrs : 主子表配置
     * @author caiyang
     * @date 2025-02-27 05:37:23 
     */  
    private Map<String, JSONArray> mainAttrs;
    
    /**  
     * @Fields paginationMap : sheet页分页信息
     * @author caiyang
     * @date 2025-05-06 08:54:14 
     */  
    private Map<String, Map<String, Object>> paginationMap;

	public Map<String, Integer> getMaxXAndY() {
		if(maxXAndY == null)
		{
			maxXAndY = new HashMap<>();
			maxXAndY.put("maxX", 0);
			maxXAndY.put("maxY", 0);
		}
		return maxXAndY;
	}
}
