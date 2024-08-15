package com.springreport.api.screendesign;

import java.util.List;
import java.util.Map;

import com.springreport.base.UserInfoDto;
import com.springreport.dto.screendesign.MesGetDynamicData;

/**  
 * @ClassName: IScreenDesignService
 * @Description: 大屏设计用service接口
 * @author caiyang
 * @date 2024-07-10 07:05:36 
*/ 
public interface IScreenDesignService {

	/**  
	 * @MethodName: getDynamicDatas
	 * @Description: 获取动态数据集数据
	 * @author caiyang
	 * @param mesGetDynamicData
	 * @return List<Map<String,Object>>
	 * @throws Exception 
	 * @date 2024-07-10 07:30:42 
	 */ 
	List<Map<String, Object>> getDynamicDatas(MesGetDynamicData mesGetDynamicData,UserInfoDto userInfoDto) throws Exception;
}
