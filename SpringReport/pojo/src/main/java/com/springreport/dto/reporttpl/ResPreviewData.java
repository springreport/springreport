package com.springreport.dto.reporttpl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.springreport.enums.YesNoEnum;

import lombok.Data;

/**  
 * @ClassName: ResPreviewData
 * @Description: 预览返回结果用实体类
 * @author caiyang
 * @date 2022-10-20 05:30:25 
*/  
@Data
public class ResPreviewData {

	/**  
	 * @Fields isParamMerge : 是否是合并参数
	 * @author caiyang
	 * @date 2022-03-31 10:42:57 
	 */ 
	private Integer isParamMerge = YesNoEnum.NO.getCode();
	
	/**  
	 * @Fields sheetDatas : 返回数据
	 * @author caiyang
	 * @date 2022-10-20 05:31:32 
	 */  
	private List<ResLuckySheetDataDto> sheetDatas;
	
	/**  
	 * @Fields cellDictsLabelValue : 单元格坐标对应的字典 外层map的key是sheetindex_r_c，内层map的key是label，根据label获取value，返回给前端上报数据时用
	 * @author caiyang
	 * @date 2022-11-28 02:57:00 
	 */  
	private Map<String, Map<String, String>> cellDictsLabelValue;
	
	/**  
	 * @Fields imgCells : 有图片的单元格
	 * @author caiyang
	 * @date 2023-01-10 11:15:54 
	 */  
	private Map<String, JSONObject> imgCells;
	
	/**  
	 * @Fields pagination : 分页信息
	 * @author caiyang
	 * @date 2023-01-16 09:59:01 
	 */  
	private Map<String, Object> pagination;
	
	/**  
	 * @Fields version : 当前更新版本号，防止重复提交覆盖数据
	 * @author caiyang
	 * @date 2023-02-01 02:28:23 
	 */  
	private Long version;
	
	/**  
	 * @Fields tplName : 报表名称
	 * @author caiyang
	 * @date 2023-02-21 10:53:18 
	 */  
	private String tplName;
	
	/**  
	 * @Fields showToolbar : 预览是否展示工具栏
	 * @author caiyang
	 * @date 2023-02-21 02:45:59 
	 */  
	private Integer showToolbar;
	
	/**  
	 * @Fields showRowHeader : 预览是否显示行标题1是 2否
	 * @author caiyang
	 * @date 2023-03-29 07:46:44 
	 */  
	private Integer showRowHeader;
	
	/**  
	 * @Fields showColHeader : 预览是否显示列标题 1是 2否
	 * @author caiyang
	 * @date 2023-03-29 07:47:12 
	 */  
	private Integer showColHeader;
	
	/**  
	 * @Fields showGridlines : 预览是否显示网格线 1是 2否
	 * @author caiyang
	 * @date 2023-03-29 07:47:42 
	 */  
	private Integer showGridlines;
	
	/**  
	 * @Fields tplType : 报表类型 1查询报表 2填报报表
	 * @author caiyang
	 * @date 2023-12-10 10:29:15 
	 */  
	private Integer tplType;
	
	/**  
     * @Fields refreshPage : 填报报表提交后是否刷新页面 1是 2否
     * @author caiyang
     * @date 2023-12-13 07:55:31 
     */  
    private Integer refreshPage = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields coeditFlag : 是否开启协同 1是 2否
     * @author caiyang
     * @date 2024-01-16 09:03:41 
     */  
    private Integer coeditFlag = YesNoEnum.YES.getCode();
    
    /**  
     * @Fields sqlMaps : 本次查询每个数据集对应的sql语句
     * @author caiyang
     * @date 2024-01-23 06:54:18 
     */  
    private List<Map<String, String>> reportSqls;
    
    /**  
     * @Fields showReportSql : 是否显示sql语句
     * @author caiyang
     * @date 2024-01-24 11:42:12 
     */  
    private boolean showReportSql = false;
    
    /** is_refresh - 是否自动刷新 1是 2否 */
    private Integer isRefresh;

    /** refresh_time - 自动刷新时长 单位：秒 */
    private Long refreshTime;
}
