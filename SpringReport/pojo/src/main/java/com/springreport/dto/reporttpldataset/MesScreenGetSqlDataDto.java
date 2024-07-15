package com.springreport.dto.reporttpldataset;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**  
 * @ClassName: MesScreenGetSqlDataDto
 * @Description: 大屏根据sql获取数据用参数实体类
 * @author caiyang
 * @date 2021-07-15 06:39:59 
*/  
@Data
public class MesScreenGetSqlDataDto {

	/**  
	 * @Fields dataSetId : 数据集id
	 * @author caiyang
	 * @date 2021-07-15 06:55:15 
	 */ 
	private Long dataSetId;
	
	/**  
	 * @Fields props : sql属性
	 * @author caiyang
	 * @date 2021-07-15 06:44:42 
	 */ 
	private List<String> props;
	
	/**  
	 * @Fields params : 参数
	 * @author caiyang
	 * @date 2021-11-24 08:41:25 
	 */ 
	private Map<String, Object> params;
	
	/**  
	 * @Fields screenComponentId : 大屏组件id
	 * @author caiyang
	 * @date 2022-01-05 07:25:34 
	 */ 
	private Long screenComponentId;
	
	/**  
	 * @Fields urlParams : url中传过来的参数
	 * @author caiyang
	 * @date 2022-01-05 07:48:30 
	 */ 
	private Map<String, Object> urlParams;
	
	/**  
	 * @Fields isCategory : 是否目录
	 * @author caiyang
	 * @date 2022-01-06 03:10:55 
	 */ 
	private boolean categoryType = false;
	
	/**  
	 * @Fields drillProp : 下钻属性
	 * @author caiyang
	 * @date 2022-08-15 06:33:26 
	 */  
	private List<String> drillProp;
	
	/**  
	 * @Fields isBindComponent : 是否是绑定组件
	 * @author caiyang
	 * @date 2022-08-10 08:49:23 
	 */  
	private Integer isBindComponent;
}
