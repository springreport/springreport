package com.springreport.controller.screendesign;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springreport.annotation.LoginUser;
import com.springreport.annotation.MethodLog;
import com.springreport.api.screendesign.IScreenDesignService;
import com.springreport.base.BaseController;
import com.springreport.base.Response;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.dto.screendesign.MesGetDynamicData;

/**  
 * @ClassName: ScreenDesignController
 * @Description: 大屏设计页面用controller
 * @author caiyang
 * @date 2024-07-11 10:39:07 
*/ 
@RestController
@RequestMapping("/springReport/api/screenDesign")
public class ScreenDesignController extends BaseController{

	@Autowired
	private IScreenDesignService iScreenDesignService;
	
	/**  
	 * @MethodName: getDynamicDatas
	 * @Description: 获取动态数据
	 * @author caiyang
	 * @param mesGetDynamicData
	 * @return Response
	 * @date 2024-07-11 10:51:01 
	 */ 
	@RequestMapping(value = "/getDynamicDatas",method = RequestMethod.POST)
	@MethodLog(module="ScreenDesign",remark="获取动态数据",operateType=Constants.OPERATE_TYPE_SEARCH)
	public Response getDynamicDatas(@RequestBody MesGetDynamicData mesGetDynamicData,@LoginUser UserInfoDto userInfoDto) throws Exception
	{
		List<Map<String, Object>> result = this.iScreenDesignService.getDynamicDatas(mesGetDynamicData,userInfoDto);
		return Response.success(result);
	}
}
