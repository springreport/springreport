package com.springreport.api.index;

import com.springreport.base.UserInfoDto;
import com.springreport.dto.index.IndexDto;

public interface IIndexService {

	/**  
	 * @MethodName: getIndexData
	 * @Description: 获取首页数据
	 * @author caiyang
	 * @param userInfoDto
	 * @return IndexDto
	 * @date 2024-12-08 06:53:47 
	 */ 
	IndexDto getIndexData(UserInfoDto userInfoDto);
}
