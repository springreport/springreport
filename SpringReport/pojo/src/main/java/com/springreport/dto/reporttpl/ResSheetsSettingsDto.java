package com.springreport.dto.reporttpl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.springreport.enums.YesNoEnum;

import lombok.Data;

@Data
public class ResSheetsSettingsDto {

	/**  
	 * @Fields isParamMerge : 是否合并参数
	 * @author caiyang
	 * @date 2022-03-31 01:49:07 
	 */ 
	private Integer isParamMerge;
	
	/**  
	 * @Fields settings : sheet配置
	 * @author caiyang
	 * @date 2022-10-20 08:41:22 
	 */  
	private List<ResLuckySheetTplSettingsDto> settings;
	
	/**  
     * @Fields sheetIndexIdMap : sheet的index 和 sheet的id map，可以通过sheet的index获取sheet的id
     * @author caiyang
     * @date 2023-02-13 07:41:12 
     */  
    private Map<String, Long> sheetIndexIdMap;
    
    /**  
     * @Fields tplName : 模板名称
     * @author caiyang
     * @date 2023-03-10 08:33:48 
     */  
    private String tplName;
    
    /**  
     * @Fields isCreator : 是否是创建人
     * @author caiyang
     * @date 2024-03-04 09:05:00 
     */  
    private boolean isCreator;
    
    /**  
     * @Fields rangeAuth : 权限
     * @author caiyang
     * @date 2024-03-04 09:15:48 
     */  
    private JSONObject sheetRangeAuth;
    
    /**  
     * @Fields creatorName : 创建者名称
     * @author caiyang
     * @date 2024-03-04 03:03:24 
     */  
    private String creatorName;
    
}
