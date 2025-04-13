package com.springreport.dto.reporttpl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.springreport.enums.CellExtendEnum;
import com.springreport.enums.YesNoEnum;

import lombok.Data;

@Data
public class LuckySheetBindData {
	
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
	
	/**
	* @Feilds:aggregateType 聚合类型 list:列表 group分组
	* @author caiyang
	*/  
	private String aggregateType;
	
	/** cell_extend - 单元格扩展方向 1不扩展 3纵向扩展 2横向扩展 */
    private Integer cellExtend;
    
    /**  
     * @Fields lastAggregateType : 上次操作的聚合方式 聚合类型 list:列表 group分组 summary汇总
     * @author caiyang
     * @date 2021-05-28 06:55:03 
     */ 
    private String lastAggregateType;
    
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
     * @Fields isLink : 是否是超链接 1是 2否
     * @author caiyang
     * @date 2021-08-31 07:08:35 
     */ 
    private Integer isLink = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields cellData : 单元格配置
     * @author caiyang
     * @date 2022-02-02 10:54:23 
     */ 
    private Map<String, Object> cellData;
    
    /**  
     * @Fields linkConfig : 超链接配置
     * @author caiyang
     * @date 2022-02-02 10:55:36 
     */ 
    private Map<String, Object> linkConfig;
    
    /**  
     * @Fields isMerge : 是否合并单元格
     * @author caiyang
     * @date 2022-02-02 10:57:06 
     */ 
    private Integer isMerge = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields isFunction : 是否是函数
     * @author caiyang
     * @date 2022-02-05 10:25:04 
     */ 
    private Integer isFunction = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields digit : 小数位数
     * @author caiyang
     * @date 2022-02-15 02:36:49 
     */ 
    private Integer digit;
    
    /** cell_function - 函数类型 0无函数 1求和 2求平均值 3最大值 4最小值 */
    private Integer cellFunction;
    
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
     * @Fields dataFrom : 数据来源 1默认 2原始数据 3单元格
     * @author caiyang
     * @date 2022-03-16 07:41:44 
     */ 
    private Integer dataFrom; 
    
    /**  
     * @Fields downProperty : 向下扩展属性
     * @author caiyang
     * @date 2022-03-16 07:42:27 
     */ 
    private String downProperty;
    
    /**  
     * @Fields rightProperty : 向右扩展属性
     * @author caiyang
     * @date 2022-03-16 07:42:55 
     */ 
    private String rightProperty;

    /**  
     * @Fields isGroupMerge : 合并单元格是否合一
     * @author caiyang
     * @date 2022-03-16 07:43:02 
     */ 
    private Boolean isGroupMerge = false;
    
    /**  
     * @Fields lastIsGroupMerge : 上组合并单元格是否合一
     * @author caiyang
     * @date 2022-03-18 07:15:12 
     */ 
    private Boolean lastIsGroupMerge = false;
    
    /**  
     * @Fields lastCellExtend : 上组数据扩展方向
     * @author caiyang
     * @date 2022-03-18 07:40:33 
     */ 
    private Integer lastCellExtend = CellExtendEnum.NOEXTEND.getCode();
    
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
     * @Fields multiDatas : 多数据集数据
     * @author caiyang
     * @date 2023-02-28 08:01:46 
     */  
    private Map<String, List<List<Map<String, Object>>>> multiDatas;
    
    /**  
     * @Fields datasetName : 数据集名称
     * @author caiyang
     * @date 2023-02-28 11:17:55 
     */  
    private String datasetName;
    
    /**  
     * @Fields isRelied : 数据是否被其他单元格依赖 1是 2否 
     * @author caiyang
     * @date 2023-03-01 05:01:38 
     */  
    private Integer isRelied;
    
    /**  
     * @Fields relyCells : 依赖单元格
     * @author caiyang
     * @date 2023-03-01 05:04:25 
     */  
    private String relyCells;
    
    /**  
     * @Fields recalculateCoords : 是否重新计算坐标 1是 2否 默认1
     * @author caiyang
     * @date 2023-03-02 09:45:10 
     */  
    private Integer recalculateCoords = YesNoEnum.YES.getCode();
    
    /**  
     * @Fields lastCoordsx : 上次计算的横坐标
     * @author caiyang
     * @date 2023-03-02 09:49:08 
     */  
    private Integer lastCoordsx;
    
    /**  
     * @Fields lastCoordsy : 上次计算的纵坐标
     * @author caiyang
     * @date 2023-03-02 09:49:30 
     */  
    private Integer lastCoordsy;
    
    /**  
     * @Fields isRelyCell : 是否是依赖单元格处理 1是 2否 默认2
     * @author caiyang
     * @date 2023-03-02 10:29:39 
     */  
    private Integer isRelyCell = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields relyCellExtend : 依赖单元格的扩展方向 单元格扩展方向 1不扩展 3纵向扩展 2横向扩展 4交叉扩展
     * @author caiyang
     * @date 2023-03-03 04:40:22 
     */  
    private Integer relyCellExtend = CellExtendEnum.NOEXTEND.getCode();
    
    /**  
     * @Fields relyCoordsx : 依赖单元格的横坐标
     * @author caiyang
     * @date 2023-03-06 05:46:27 
     */  
    private Integer relyCoordsx;
    
    /**  
     * @Fields relyCoordsy : 依赖单元格纵坐标
     * @author caiyang
     * @date 2023-03-06 05:46:52 
     */  
    private Integer relyCoordsy;
    
    /**  
     * @Fields isDict : 是否是数据字典
     * @author caiyang
     * @date 2023-03-13 10:19:45 
     */  
    private Boolean isDict = false;
    
    /** datasource_id - 数据字典数据源id */
    private Long datasourceId;

    /** dict_type - 数据字典类型 */
    private String dictType;
    
    /** alternate_format - 是否交替颜色1是 2否 */
    private Integer alternateFormat = YesNoEnum.NO.getCode();

    /** alternate_format_bc_odd - 奇数行背景颜色 */
    private String alternateFormatBcOdd;

    /** alternate_format_bc_even - 偶数行背景颜色 */
    private String alternateFormatBcEven;

    /** alternate_format_fc_odd - 奇数行字体颜色 */
    private String alternateFormatFcOdd;

    /** alternate_format_fc_even - 偶数行字体颜色 */
    private String alternateFormatFcEven;
    
    /**  
     * @Fields relyCrossIndex : 交叉索引
     * @author caiyang
     * @date 2023-03-23 11:08:36 
     */  
    private Integer relyCrossIndex;
    
    /**  
     * @Fields reliedCellSize : 依赖单元格数据
     * @author caiyang
     * @date 2023-03-23 03:16:00 
     */  
    private Integer reliedCellSize = 0;
    
    /**  
     * @Fields groupProperty : 分组属性
     * @author caiyang
     * @date 2023-03-24 03:52:50 
     */  
    private String groupProperty;
    
    /** is_conditions - 是否有单元格过滤条件 1是 2否 */
    private Integer isConditions;

    /** cell_conditions - 单元格过滤条件 */
    private String cellConditions;
    
    /**  
     * @Fields isChartCell : 是否图表单元格 1是 2否
     * @author caiyang
     * @date 2023-04-25 10:21:33 
     */  
    private Integer isChartCell = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields chartId : 图表id
     * @author caiyang
     * @date 2023-04-25 04:33:25 
     */  
    private String chartId;
    
    /** is_data_verification - 是否是数据校验项 1是 2否 */
    private Integer isDataVerification = YesNoEnum.NO.getCode();

    /** data_verification - 数据校验项目 */
    private String dataVerification;
    
    private Boolean isDrill;

    /** drill_id - 下钻报表id */
    private Long drillId;

    /** drill_attrs - 下钻属性 */
    private String drillAttrs;
    
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
    
    /**  
     * @Fields blockDatas : 循环块数据
     * @author caiyang
     * @date 2023-12-19 04:23:45 
     */  
    private Map<String, List<List<Map<String, Object>>>> blockDatas;
    
    /** is_subtotal - 是否需要小计 */
    private Boolean isSubtotal;

    /** subtotal_cells - 小计单元格 */
    private String subtotalCells;
    
    /**  
     * @Fields subTotalName : 小计名称
     * @author caiyang
     * @date 2024-01-26 08:21:05 
     */  
    private String subTotalName = "小计";

    /** is_subtotal_calc - 是否有小计公式链 */
    private Boolean isSubtotalCalc;

    /** subtotal_calc - 小计公式链 */
    private String subtotalCalc;
    
    /**  
     * @Fields groupSubtotalCount : 分组小计数量
     * @author caiyang
     * @date 2024-01-25 12:20:18 
     */  
    private Map<Integer, Integer> groupSubtotalCount;
    
    /**  
     * @Fields sheetId : sheet主键
     * @author caiyang
     * @date 2024-01-26 12:12:02 
     */  
    private Long sheetId;
    
    /**  
     * @Fields subtotalAttrs : 小计属性
     * @author caiyang
     * @date 2024-01-27 11:29:00 
     */  
    private JSONObject subtotalAttrs;
    
    /**  
     * @Fields sheetIndex : sheetIndex
     * @author caiyang
     * @date 2024-02-08 09:48:06 
     */  
    private String sheetIndex;
    
    /** is_chart_attr - 是否是图表中的单元格属性 1是 2否 */
    private Integer isChartAttr;
    
    private Integer startx;
    
    private Integer starty;
    
    private Integer endx;
    
    private Integer endy;
    
    /** lastIsConditions - 上一个单元格数据是否有单元格过滤条件 1是 2否 */
    private Integer lastIsConditions = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields originalData : 原始数据
     * @author caiyang
     * @date 2024-08-13 04:03:22 
     */  
    private List<Map<String, Object>> originalData;
    
    /**  
     * @Fields relyIndex : 依赖数据的index
     * @author caiyang
     * @date 2024-08-18 08:24:02 
     */  
    private Integer relyIndex;
    
    /**  
     * @Fields cellContent : 单元格文本信息(去掉数据集信息)
     * @author caiyang
     * @date 2024-08-23 03:33:34 
     */  
    private String cellText;
    
    /**  
     * @Fields cellFillType : 数据填充方式 1插入 2覆盖
     * @author caiyang
     * @date 2024-12-31 11:26:00 
     */  
    private Integer cellFillType = 1;
    
    /**  
     * @Fields tplType : 是否是填报数据 1是 2否
     * @author caiyang
     * @date 2025-01-31 03:00:27 
     */  
    private int tplType = 1;
    
    /** hloop_count - 横向循环次数 */
    private Integer hloopCount;

    /** hloop_empty_count - 横向循环间隔空行数 */
    private Integer hloopEmptyCount;

    /** vloop_empty_count - 纵向循环间隔空行数 */
    private Integer vloopEmptyCount;
    
    /**  
     * @Fields dataSize : 数据条数
     * @author caiyang
     * @date 2025-03-20 12:58:29 
     */  
    private int dataSize = 0;
    
    /**  
     * @Fields groupMergeSize : 分组合一单元格每一组对应的数据条数
     * @author caiyang
     * @date 2025-03-22 08:18:14 
     */  
    private Map<Integer, Integer> groupMergeSize;
    
    /** is_object - 是否是复杂对象 */
    private Boolean isObject;
    
    /** data_type - 数据类型 1数组 2对象数组 3对象 */
    private Integer dataType;
    
    /** data_attr - 属性 */
    private String dataAttr;
    
    /** sub_extend - 子数据扩展方向 1不扩展 2纵向扩展 3横向扩展 */
    private Integer subExtend;
}
