package com.springreport.base;



import java.util.Map;

import lombok.Data;

/**  
 * @ClassName: CellValueDto
 * @Description: 单元格数据
 * @author caiyang
 * @date 2021-05-31 05:27:10 
*/  
@Data
public class CellValueDto {

	/**
	* @Feilds:startRow 开始行坐标
	* @author caiyang
	*/  
	private int startRow;
	
	/**
	* @Feilds:startColumn 开始列坐标
	* @author caiyang
	*/  
	private int startColumn;
	
	/**
	* @Feilds:endRow 结束行坐标
	* @author caiyang
	*/  
	private int endRow;
	
	/**
	* @Feilds:endColumn 结束列坐标
	* @author caiyang
	*/  
	private int endColumn;
	
	/**
	* @Feilds:aggregateType 聚合类型 list:列表 group分组
	* @author caiyang
	*/  
	private String aggregateType;
	
	/** cell_extend - 单元格扩展方向 1不扩展 2纵向扩展 2横向扩展 */
    private Integer cellExtend;
    
    /**  
     * @Fields property : 属性值
     * @author caiyang
     * @date 2021-05-28 07:07:02 
     */ 
    private String property;
    
    /**  
     * @Fields cellValueType : 单元格类型 1固定值 2变量
     * @author caiyang
     * @date 2021-05-31 08:01:43 
     */ 
    private Integer cellValueType;
    
    /**  
     * @Fields data : 数据
     * @author caiyang
     * @date 2021-05-31 05:31:03 
     */ 
    private Object data;
    
    /**  
     * @Fields rowSpan : 合并行数
     * @author caiyang
     * @date 2021-05-31 05:42:07 
     */ 
    private int rowSpan;
    
    /**  
     * @Fields colSpan : 合并列数
     * @author caiyang
     * @date 2021-05-31 05:42:32 
     */ 
    private int colSpan;
    
    /**  
     * @Fields isMerge : 是否是合并单元格 1是 2否
     * @author caiyang
     * @date 2021-05-31 06:35:58 
     */ 
    private Integer isMerge = 2;
    
    /** font_color - 字体颜色 */
    private String fontColor;

    /** back_color - 背景颜色 */
    private String backColor;

    /** font_size - 字体大小 */
    private String fontSize;

    /** is_bold - 是否字体加粗 1是 2否 */
    private Integer isBold = 2;
    
    /**  
     * @Fields cellClassname : 单元格class
     * @author caiyang
     * @date 2021-06-10 03:12:46 
     */ 
    private String cellClassname;
    
    /**  
     * @Fields isLink : 是否超链接
     * @author caiyang
     * @date 2021-09-01 08:01:44 
     */ 
    private Integer isLink;
    
    /**  
     * @Fields cellLink : 超链接
     * @author caiyang
     * @date 2021-09-01 08:03:06 
     */ 
    private Map<String,Object> cellLink;
}
