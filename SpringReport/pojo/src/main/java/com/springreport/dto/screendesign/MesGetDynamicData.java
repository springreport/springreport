package com.springreport.dto.screendesign;

import java.util.List;
import java.util.Map;

import com.springreport.enums.YesNoEnum;

import lombok.Data;

/**  
 * @ClassName: MesGetDynamicData
 * @Description: 获取动态数据用参数
 * @author caiyang
 * @date 2024-07-10 07:16:56 
*/ 
@Data
public class MesGetDynamicData {

	/**  
	 * @Fields dataSetId : 数据集id
	 * @author caiyang
	 * @date 2024-07-10 07:17:26 
	 */  
	private Long dataSetId;
	
	/**  
	 * @Fields dataColumns : 数据列
	 * @author caiyang
	 * @date 2024-07-10 07:18:21 
	 */  
	private List<String> dataColumns;
	
	/**  
	 * @Fields params : 参数
	 * @author caiyang
	 * @date 2024-07-10 07:29:08 
	 */  
	private Map<String, Object> params;
	
	/**  
	 * @Fields initPage : 是否是页面初始化时获取数据
	 * @author caiyang
	 * @date 2024-07-10 07:29:45 
	 */  
	private Integer initPage = YesNoEnum.NO.getCode();
	
	/**  
	 * @Fields requestKey : 请求key，每次请求都不一样
	 * @author caiyang
	 * @date 2024-07-11 09:42:55 
	 */  
	private String requestKey;
}
