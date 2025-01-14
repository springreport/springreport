package com.springreport.dto.onlinetpl;

import com.alibaba.fastjson.JSONObject;
import com.springreport.enums.YesNoEnum;

import lombok.Data;

@Data
public class ResOnlineTplInfo {

	/**  
     * @Fields isCreator : 是否是创建人
     * @author caiyang
     * @date 2024-03-04 09:05:00 
     */  
    private boolean isCreator = false;
    
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
    
    /**  
     * @Fields tplName : 文档名称
     * @author caiyang
     * @date 2024-08-28 03:37:23 
     */  
    private String tplName;
    
    /**  
     * @Fields isThirdParty : 是否是第三方调用 1是 2否
     * @author caiyang
     * @date 2025-01-09 11:17:35 
     */  
    private Integer isThirdParty = YesNoEnum.NO.getCode();
}
