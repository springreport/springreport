package com.springreport.dto.reporttpldataset;

import java.util.Map;

import com.springreport.base.BaseEntity;
import com.springreport.enums.YesNoEnum;

import lombok.Data;

/**
* <p>Title: ReportParamDto</p>
* <p>Description: 报表参数用实体类</p>
* @author caiyang
* @date 2020年3月9日
*/
@Data
public class ReportParamDto extends BaseEntity{

	/**
	* @Feilds:paramName 参数名称
	* @author caiyang
	*/  
	private String paramName;
	
	/**
	* @Feilds:paramCode 参数编码
	* @author caiyang
	*/  
	private String paramCode;
	
	/**
	* @Feilds:paramType 参数类型
	* @author caiyang
	*/  
	private String paramType;
	
	/**
	* @Feilds:paramDefault 默认值
	* @author caiyang
	*/  
	private String paramDefault;
	
	/**
	* @Feilds:paramRequired 是否必填
	* @author caiyang
	*/  
	private Integer paramRequired;
	
	/**
	* @Feilds:isPagination 是否需要分页
	* @author caiyang
	*/  
	private Integer isPagination;
	
	/**
	* @Feilds:rules 校验规则
	* @author caiyang
	*/  
	private Map<String, Object> rules;
	
	/**
	* @Feilds:selectType 下拉框选择内容来源 1自定义 2sql
	* @author caiyang
	*/  
	private String selectType;
	
	/**
	* @Feilds:selectContent 下拉选择内容
	* @author caiyang
	*/  
	private String selectContent;
	
	/**
	* @Feilds:selectData 下拉框数据
	* @author caiyang
	*/  
	private Object selectData;
	
	/**  
	 * @Fields disabled : 是否禁用 默认false 不禁用
	 * @author caiyang
	 * @date 2021-11-09 02:47:43 
	 */ 
	private boolean disabled = false;
	
	/**  
	 * @Fields isRelyOnParams : 是否依赖其他参数
	 * @author caiyang
	 * @date 2022-08-08 07:40:35 
	 */  
	private Integer isRelyOnParams;
	
	/**  
	 * @Fields relyOnParams : 依赖参数代码
	 * @author caiyang
	 * @date 2022-08-08 07:41:03 
	 */  
	private String relyOnParams;
	
	/**  
	 * @Fields datasourceId : 数据源id
	 * @author caiyang
	 * @date 2023-02-10 04:15:23 
	 */  
	private Long datasourceId;
	
	/**  
	 * @Fields dateFormt : 日期格式
	 * @author caiyang
	 * @date 2023-04-10 08:18:12 
	 */  
	private String dateFormat;
	
	/**  
	 * @Fields paramHidden : 参数是否隐藏 1是 2否
	 * @author caiyang
	 * @date 2023-07-07 04:25:03 
	 */  
	private Integer paramHidden = YesNoEnum.NO.getCode();
	
	/**  
	 * @Fields dateDefaultValue : 日期格式默认值
	 * @author caiyang
	 * @date 2023-07-31 06:22:20 
	 */  
	private String dateDefaultValue;
	
	/**  
	 * @Fields checkStrictly : 是否父子联动
	 * @author caiyang
	 * @date 2024-01-23 11:58:34 
	 */  
	private Integer checkStrictly;
	
	/**  
	 * @Fields init : 是否已经初始化数据
	 * @author caiyang
	 * @date 2024-01-23 04:04:27 
	 */  
	private boolean init = false;
	
	/**  
	 * @Fields paramPrefix : api参数前缀
	 * @author caiyang
	 * @date 2024-09-19 02:55:48 
	 */  
	private String paramPrefix;
	
	/**  
	 * @Fields componentType : 组件类型
	 * @author caiyang
	 * @date 2024-11-03 05:28:49 
	 */  
	private String componentType;
}
