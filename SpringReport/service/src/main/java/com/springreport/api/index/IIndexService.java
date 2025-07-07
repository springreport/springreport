package com.springreport.api.index;

import com.springreport.base.UserInfoDto;
import com.springreport.dto.index.IndexDto;
import com.springreport.entity.sysmerchant.SysMerchant;

public interface IIndexService {

	/**  
	 * @MethodName: getIndexData
	 * @Description: 获取首页数据
	 * @author caiyang
	 * @param userInfoDto
	 * @return IndexDto
	 * @date 2024-12-08 06:53:47 
	 */ 
	IndexDto getIndexData(SysMerchant sysMerchant,UserInfoDto userInfoDto);
}
