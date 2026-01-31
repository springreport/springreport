package com.springreport.dto.reporttpl;

import java.util.List;
import java.util.Map;

import com.springreport.base.LuckySheetBindData;

import lombok.Data;

/**  
 * @ClassName: GroupSummaryData
 * @Description: 分组汇总计算用实体类
 * @author caiyang
 * @date 2022-02-19 10:18:25 
*/  
@Data
public class GroupSummaryData {

	/**  
	 * @Fields datas : 数据
	 * @author caiyang
	 * @date 2022-02-19 10:19:00 
	 */ 
	private List<Map<String, Object>> datas;
	
	/**  
	 * @Fields digit : 小数位数
	 * @author caiyang
	 * @date 2022-02-19 10:19:15 
	 */ 
	private Integer digit;
	
	/**  
	 * @Fields property : 属性
	 * @author caiyang
	 * @date 2022-02-19 10:21:02 
	 */ 
	private String property;
	
	 /** compare_attr1 - 同比/环比本期属性 */
    private String compareAttr1;

    /** compare_attr2 - 同比/环比同期属性 */
    private String compareAttr2;
    
    /**
     * 数据集对应的当前计算index
     */
    private Integer index;
    
    /**  
     * @Fields multiDatas : 多数据集数据
     * @author caiyang
     * @date 2026年1月28日11:40:55
     */  
    private Map<String, List<List<Map<String, Object>>>> multiDatas;
    
    /**  
     * @Fields datasetName : 数据集名称
     * @author caiyang
     * @date 2026年1月28日12:04:26
     */  
    private String datasetName;
    
    /**  
     * @Fields luckySheetBindData
     * @author caiyang
     * @date 2026年1月28日18:42:24
     */  
    private LuckySheetBindData luckySheetBindData;
}
