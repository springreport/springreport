package com.springreport.dto.reporttpl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.entity.reportsheetpdfprintsetting.ReportSheetPdfPrintSetting;

import lombok.Data;

/**  
 * @ClassName: MesLuckySheetTplDto
 * @Description: luckysheet模板实体类
 * @author caiyang
 * @date 2022-02-01 10:46:48 
*/  
@Data
public class MesLuckySheetTplDto {
	
	/**  
	 * @Fields id : 模板id
	 * @author caiyang
	 * @date 2021-05-24 06:14:32 
	 */ 
//	private Long tplId;

	/**  
	 * @Fields title : 模板标题
	 * @author caiyang
	 * @date 2022-02-01 10:47:05 
	 */ 
	private String title;
	
	/**  
	 * @Fields hyperlinks : 超链接
	 * @author caiyang
	 * @date 2022-02-01 10:48:03 
	 */ 
	private Map<String, JSONObject> hyperlinks;
	
	/**  
	 * @Fields cellDatas : 单元格数据
	 * @author caiyang
	 * @date 2022-02-01 10:49:04 
	 */ 
	private List<Map<String, Object>> cellDatas;
	
	/**  
	 * @Fields blockCellDatas : 循环块数据
	 * @author caiyang
	 * @date 2022-04-02 03:50:34 
	 */ 
	private List<Map<String, Object>> blockCellDatas;
	
	/**  
	 * @Fields extraCustomCellConfigs : 自定义额外单元格配置
	 * @author caiyang
	 * @date 2022-02-01 10:49:34 
	 */ 
	private Map<String, JSONObject> extraCustomCellConfigs;
	
	/**  
	 * @Fields config : 列宽行高等额外配置
	 * @author caiyang
	 * @date 2022-02-11 07:12:46 
	 */ 
	private Map<String, Object> config;
	
	/**  
	 * @Fields isParamMerge : 是否合并参数
	 * @author caiyang
	 * @date 2022-03-31 01:49:07 
	 */ 
//	private Integer isParamMerge;
	
	/**  
	 * @Fields blockCells : 数据块单元格
	 * @author caiyang
	 * @date 2022-04-02 04:01:25 
	 */ 
	private Map<String, List<Map<String, Object>>> blockCells;
	
	/**  
	 * @Fields subBlockCells : 子数据块单元格
	 * @author caiyang
	 * @date 2025年6月23日10:31:31
	 */ 
	private Map<String, List<Map<String, Object>>> subBlockCells;
	
	/**  
	 * @Fields frozen : 冻结信息
	 * @author caiyang
	 * @date 2022-05-05 09:34:33 
	 */ 
	private Map<String, Object> frozen;
	
	/**  
	 * @Fields images : 图片
	 * @author caiyang
	 * @date 2022-06-02 01:57:12 
	 */  
	private Object images; 
	
	/** sheet_index - sheet页唯一索引 */
    private String sheetIndex;

    /** calc_chain - 计算链，有公式的单元格信息 */
    private JSONArray calcChain;
    
    /**  
     * @Fields sheetOrder : TODO
     * @author caiyang
     * @date 2022-10-21 03:59:20 
     */  
    private Integer sheetOrder;
    
    /**  
     * @Fields sheetName : sheet名称
     * @author caiyang
     * @date 2022-10-21 04:04:53 
     */  
    private String sheetName;
    
    /**  
     * @Fields datasourceConfig : 绑定数据配置
     * @author caiyang
     * @date 2022-11-16 01:35:43 
     */  
    private JSONArray datasourceConfig;
    
    /**  
     * @Fields luckysheetAlternateformatSave : 交替颜色配置
     * @author caiyang
     * @date 2023-03-13 04:36:21 
     */  
    private JSONArray luckysheetAlternateformatSave;
    
    /**  
     * @Fields chart : 图表配置
     * @author caiyang
     * @date 2023-04-17 10:41:25 
     */  
    private JSONArray chart;
    
    /**  
     * @Fields chartXaxisData : 图表x轴配置
     * @author caiyang
     * @date 2023-04-19 01:44:12 
     */  
    private JSONArray chartXaxisData;
    
    /**  
     * @Fields chartCells : 图表对应的单元格
     * @author caiyang
     * @date 2023-04-25 07:41:05 
     */  
    private JSONArray chartCells;
    
    /**  
     * @Fields dataVerification : 数据校验项
     * @author caiyang
     * @date 2023-05-04 07:53:09 
     */  
    private JSONObject dataVerification;
    
    /**  
     * @Fields xxbtScreenShot : 斜线表头截图
     * @author caiyang
     * @date 2023-11-29 12:38:58 
     */  
    private JSONObject xxbtScreenShot;
    
    /**  
     * @Fields printSettings : pdf/打印设置
     * @author caiyang
     * @date 2023-12-07 07:30:26 
     */  
    private ReportSheetPdfPrintSetting printSettings;
    
    /**  
     * @Fields pageDivider : 分页线配置信息
     * @author caiyang
     * @date 2024-02-25 05:21:03 
     */  
    private List<Integer> pageDivider;
    
    /**  
     * @Fields sheetAuth : sheet页选区权限
     * @author caiyang
     * @date 2024-03-03 06:04:14 
     */  
    private JSONObject sheetRangeAuth;
    
    /**  
     * @Fields luckysheetConditionformatSave : 条件格式配置
     * @author caiyang
     * @date 2024年8月16日18:54:55
     */  
    private JSONArray luckysheetConditionformatSave;
}
