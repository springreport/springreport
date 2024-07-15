package com.springreport.api.reportrangeauthuser;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.reportrangeauth.ReportRangeAuth;
import com.springreport.entity.reportrangeauthuser.ReportRangeAuthUser;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.reportRangeAuthUser.RangeAuthUserDto;

 /**  
* @Description: ReportRangeAuthUser服务接口
* @author 
* @date 2024-03-03 05:52:17
* @version V1.0  
 */
public interface IReportRangeAuthUserService extends IService<ReportRangeAuthUser> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(ReportRangeAuthUser model);

	/**
	*<p>Title: getDetail</p>
	*<p>Description: 获取详情</p>
	* @author caiyang
	* @param id
	* @return
	*/
	BaseEntity getDetail(Long id);
	
	/**
	*<p>Title: insert</p>
	*<p>Description: 新增数据</p>
	* @author caiyang
	* @param model
	* @return
	*/
	BaseEntity insert(ReportRangeAuthUser model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(ReportRangeAuthUser model);
	
	/**
	*<p>Title: delete</p>
	*<p>Description: 单条删除数据</p>
	* @author caiyang
	* @param model
	* @return
	*/
	BaseEntity delete(Long id);
	
	/**
	*<p>Title: deleteBatch</p>
	*<p>Description: 批量删除数据</p>
	* @author caiyang
	* @param list
	* @return
	*/
	BaseEntity deleteBatch(List<Long> ids);
	
	/**  
	 * @MethodName: getRangeUsers
	 * @Description: 获取用户和对应的授权
	 * @author caiyang
	 * @param userInfoDto
	 * @return List<RangeAuthUserDto>
	 * @date 2024-06-05 11:30:29 
	 */ 
	List<RangeAuthUserDto> getRangeUsers(UserInfoDto userInfoDto);
	
	/**  
	 * @MethodName: getUserNoAuthRange
	 * @Description: 获取用户没有查看权限的授权
	 * @author caiyang
	 * @param userInfoDto
	 * @return JSONObject
	 * @date 2024-06-10 08:03:34 
	 */ 
	Map<String, List<ReportRangeAuth>> getUserNoAuthRange(String listId,UserInfoDto userInfoDto);
}
