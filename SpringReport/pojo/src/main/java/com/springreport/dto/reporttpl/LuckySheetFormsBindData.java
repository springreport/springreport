package com.springreport.dto.reporttpl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.springreport.enums.CellExtendEnum;
import com.springreport.enums.YesNoEnum;

import lombok.Data;

@Data
public class LuckySheetFormsBindData {
	
	/**  
	 * @Fields reportCellId : 单元格id
	 * @author caiyang
	 * @date 2022-04-05 03:23:40 
	 */ 
	private Long reportCellId;

	/**
	* @Feilds:coordsx 横坐标
	* @author caiyang
	*/  
	private Integer coordsx;
	
	/**
	* @Feilds:coordsy 纵坐标
	* @author caiyang
	*/  
	private Integer coordsy;
	
	/**
	* @Feilds:aggregateType 聚合类型 list:列表 group分组
	* @author caiyang
	*/  
	private String aggregateType;
	
	/**  
	 * @Fields datas : 数据
	 * @author caiyang
	 * @date 2021-05-27 05:46:53 
	 */ 
	private List<List<Map<String, Object>>> datas;
	
	/**  
	 * @Fields filterDatas : 过滤后的数据
	 * @author caiyang
	 * @date 2023-04-11 10:55:30 
	 */  
	private List<List<Map<String, Object>>> filterDatas;
	
	
	/** cell_extend - 单元格扩展方向 1不扩展 2纵向扩展 2横向扩展 */
    private Integer cellExtend;
    
    /**  
     * @Fields property : 属性值
     * @author caiyang
     * @date 2021-05-28 07:07:02 
     */ 
    private String property;
    
    /**  
     * @Fields isFirst : 是否是第一次聚合计算
     * @author caiyang
     * @date 2021-05-28 06:29:21 
     */ 
    private Integer isFirst;
    
    /**  
     * @Fields cellValueType : 单元格类型 1固定值 2变量
     * @author caiyang
     * @date 2021-05-31 08:01:43 
     */ 
    private Integer cellValueType;
    
    /**  
     * @Fields cellData : 单元格配置
     * @author caiyang
     * @date 2022-02-02 10:54:23 
     */ 
    private Map<String, Object> cellData;
    
    
    /**  
     * @Fields cellValue : 单元格值
     * @author caiyang
     * @date 2022-08-05 04:11:21 
     */  
    private String cellValue;
    
    /**  
     * @Fields warning : 是否预警
     * @author caiyang
     * @date 2022-11-04 07:06:47 
     */  
    private Boolean warning = false;
    
    /**  
     * @Fields warningRules : 预警规则
     * @author caiyang
     * @date 2023-07-24 06:20:00 
     */  
    private String warningRules = ">=";
    
    /**  
     * @Fields warningColor : 预警颜色
     * @author caiyang
     * @date 2022-11-04 07:07:44 
     */  
    private String warningColor;
    
    /**  
     * @Fields threshold : 预警阈值
     * @author caiyang
     * @date 2022-11-04 07:07:50 
     */  
    private String threshold;
    
    /**  
     * @Fields warningContent : 预警内容
     * @author caiyang
     * @date 2022-11-04 07:07:59 
     */  
    private String warningContent;
    
    /**  
     * @Fields cellAttrs : 单元格属性
     * @author caiyang
     * @date 2022-11-19 11:15:09 
     */  
    private JSONObject cellAttrs;
    
    /**  
     * @Fields isMerge : 是否合并单元格
     * @author caiyang
     * @date 2022-02-02 10:57:06 
     */ 
    private Integer isMerge = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields rowSpan : 合并行数
     * @author caiyang
     * @date 2021-05-31 06:57:38 
     */ 
    private Integer rowSpan;
    
    /**  
     * @Fields colSpan : 合并列数
     * @author caiyang
     * @date 2021-05-31 06:58:00 
     */ 
    private Integer colSpan;
    
    /**  
     * @Fields digit : 小数位数
     * @author caiyang
     * @date 2022-02-15 02:36:49 
     */ 
    private Integer digit;
    
    /** cell_function - 函数类型 0无函数 1求和 2求平均值 3最大值 4最小值 */
    private Integer cellFunction;
    
    /**  
     * @Fields dataFrom : 数据来源 1默认 2原始数据 3单元格
     * @author caiyang
     * @date 2022-03-16 07:41:44 
     */ 
    private Integer dataFrom; 
    
    /**  
     * @Fields groupSummaryDependencyR : 分组聚合计算依赖横坐标
     * @author caiyang
     * @date 2022-02-19 04:17:50 
     */ 
    private String groupSummaryDependencyR;
    
    /**  
     * @Fields groupSummaryDependencyC : 分组聚合计算依赖纵坐标
     * @author caiyang
     * @date 2022-02-19 04:18:19 
     */ 
    private String groupSummaryDependencyC;
    
    /**  
     * @Fields isFunction : 是否是函数
     * @author caiyang
     * @date 2022-02-05 10:25:04 
     */ 
    private Integer isFunction = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields lastAggregateType : 上次操作的聚合方式 聚合类型 list:列表 group分组 summary汇总
     * @author caiyang
     * @date 2021-05-28 06:55:03 
     */ 
    private String lastAggregateType;
    
    /**  
     * @Fields lastCellExtend : 上组数据扩展方向
     * @author caiyang
     * @date 2022-03-18 07:40:33 
     */ 
    private Integer lastCellExtend = CellExtendEnum.NOEXTEND.getCode();
    
    /**  
     * @Fields datasetName : 数据集名称
     * @author caiyang
     * @date 2023-02-28 11:17:55 
     */  
    private String datasetName;
    
    /**  
     * @Fields multiDatas : 多数据集数据
     * @author caiyang
     * @date 2023-02-28 08:01:46 
     */  
    private Map<String, List<List<Map<String, Object>>>> multiDatas;
    
    /** is_conditions - 是否有单元格过滤条件 1是 2否 */
    private Integer isConditions;

    /** cell_conditions - 单元格过滤条件 */
    private String cellConditions;
    
    /**  
     * @Fields groupProperty : 分组属性
     * @author caiyang
     * @date 2023-03-24 03:52:50 
     */  
    private String groupProperty;
    
    /** unit_transfer - 是否数值单位转换 */
    private Boolean unitTransfer;

    /** transfer_type - 转换方式 1乘法 2除法 */
    private Integer transferType;

    /** multiple - 倍数 */
    private String multiple;
    
    /**  
     * @Fields cellConditionType : 过滤条件类型
     * @author caiyang
     * @date 2023-08-09 09:55:54 
     */  
    private String cellConditionType = "and";
    
    /** lastIsConditions - 上一个单元格数据是否有单元格过滤条件 1是 2否 */
    private Integer lastIsConditions = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields sheetIndex : sheetIndex
     * @author caiyang
     * @date 2024-02-08 09:48:06 
     */  
    private String sheetIndex;
}
