package com.springreport.dto.reporttpl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.entity.reportsheetpdfprintsetting.ReportSheetPdfPrintSetting;

import lombok.Data;

/**  
 * @ClassName: ResLuckySheetTplSettingsDto
 * @Description: luckysheet模板返回结果用实体类
 * @author caiyang
 * @date 2022-02-01 04:00:45 
*/  
/**  
 * @ClassName: ResLuckySheetTplSettingsDto
 * @Description: TODO
 * @author caiyang
 * @date 2023-01-25 08:37:33 
*/  
@Data
public class ResLuckySheetTplSettingsDto {

	
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
	 * @Fields extraCustomCellConfigs : 自定义额外单元格配置
	 * @author caiyang
	 * @date 2022-02-01 10:49:34 
	 */ 
	private Map<String, JSONObject> extraCustomCellConfigs;
	
	/**  
	 * @Fields config :  列宽行高等额外配置
	 * @author caiyang
	 * @date 2022-02-11 07:52:21 
	 */ 
	private Map<String, Object> config;
	
	
	/**  
	 * @Fields blockData : 循环块
	 * @author caiyang
	 * @date 2022-04-04 06:11:36 
	 */ 
	private List<Map<String, Object>> blockData;
	
	/**  
	 * @Fields frozen : 冻结信息
	 * @author caiyang
	 * @date 2022-05-05 09:42:59 
	 */ 
	private Map<String, Object> frozen;
	
	/**  
	 * @Fields images : 图片
	 * @author caiyang
	 * @date 2022-06-02 02:02:40 
	 */  
	private Object images;
	
	/** sheet_index - sheet页唯一索引 */
    private String sheetIndex;

    /** calc_chain - 计算链，有公式的单元格信息 */
    private JSONArray calcChain = new JSONArray();
    
    /**  
     * @Fields sheetName : sheet名称
     * @author caiyang
     * @date 2022-10-20 08:22:27 
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
    private JSONArray datasourceConfig;
    
    /**  
     * @Fields cellFormats : 单元格格式
     * @author caiyang
     * @date 2023-01-25 08:37:39 
     */  
    private Map<String, Object> cellFormats;
    
    /**  
     * @Fields sheetId : sheet id
     * @author caiyang
     * @date 2023-02-13 07:40:48 
     */  
    private Long sheetId;
    
    /**  
     * @Fields luckysheetAlternateformatSave : 交替颜色配置
     * @author caiyang
     * @date 2023-03-13 04:36:21 
     */  
    private JSONArray luckysheetAlternateformatSave;
    
    /**  
     * @Fields chart : 图表
     * @author caiyang
     * @date 2023-04-17 11:01:24 
     */  
    private JSONArray chart;
    
    /**  
     * @Fields chartXaxisData : 图表x轴配置
     * @author caiyang
     * @date 2023-04-19 02:45:50 
     */  
    private JSONArray chartXaxisData;
    
    /**  
     * @Fields dataVerification : 数据校验项
     * @author caiyang
     * @date 2023-05-04 08:08:03 
     */  
    private JSONObject dataVerification;
    
    /**  
     * @Fields printSettings : 打印设置
     * @author caiyang
     * @date 2023-12-06 06:11:32 
     */  
    private ReportSheetPdfPrintSetting reportSheetPdfPrintSetting;
    
    /**  
     * @Fields pageDivider : 分页线配置
     * @author caiyang
     * @date 2024-02-25 05:38:21 
     */  
    private JSONArray pageDivider;
    
    /**  
     * @Fields luckysheetConditionformatSave : 条件格式配置
     * @author caiyang
     * @date 2024年8月16日18:54:55
     */  
    private JSONArray luckysheetConditionformatSave;
    
}
