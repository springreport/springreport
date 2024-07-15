/** 
 * 模块：报表系统-ReportRangeAuthUser
 */
package com.springreport.controller.reportrangeauthuser;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.springreport.base.BaseController;
import com.springreport.base.BaseEntity;
import com.springreport.base.Response;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.dto.reportRangeAuthUser.RangeAuthUserDto;
import com.springreport.entity.reportrangeauthuser.ReportRangeAuthUser;
import com.springreport.annotation.Check;
import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.reportrangeauthuser.IReportRangeAuthUserService;

 /**  
* @Description: ReportRangeAuthUsercontroller类
* @author 
* @date 2024-03-03 05:52:17
* @version V1.0  
 */
@RestController
@RequestMapping("/springReport/api/reportRangeAuthUser")
public class ReportRangeAuthUserController extends BaseController {

	/**
	 * iReportRangeAuthUserService服务注入
	 */
	@Autowired
	private IReportRangeAuthUserService iReportRangeAuthUserService;

	/** 
	* @Description: 获取授权用户
	* @param ReportRangeAuthUser
	* @param @return  
	* @return Response
	* @throws 
	*/ 
	@RequestMapping(value = "/getRangeUsers",method = RequestMethod.POST)
	@MethodLog(module="ReportRangeAuthUser",remark="获取用户和对应的授权",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getRangeUsers(@LoginUser UserInfoDto userInfoDto)
	{
		List<RangeAuthUserDto> result = iReportRangeAuthUserService.getRangeUsers(userInfoDto);
		return Response.success(result);
	}

}
