package com.springreport.controller.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.annotation.Check;
import com.springreport.annotation.MethodLog;
import com.springreport.api.common.ICommonService;
import com.springreport.base.BaseController;
import com.springreport.base.Response;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.dto.common.ApiRequestDto;
import com.springreport.dto.common.PrintApiRequestDto;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.exception.BizException;
import com.springreport.util.JsonUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.Pako_GzipUtils;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;

@RestController
@RequestMapping("/springReport/api/common")
public class CommonController extends BaseController{

	
	@Autowired
	private ICommonService iCommonService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@RequestMapping(value = "/unnauthorized",method = RequestMethod.GET)
	public Response unnauthorized() {
		throw new BizException(StatusCode.AUTH_FAILURE, MessageUtil.getValue("error.auth"));
	}
	
	/**  
	 * @Title: upload
	 * @Description: 文件上传
	 * @param file
	 * @return
	 * @throws IOException
	 * @author caiyang
	 * @date 2021-07-28 07:52:22 
	 */ 
	@RequestMapping("/upload")
	public Response upload(@RequestParam("file") MultipartFile file) throws IOException {
		Object result = this.iCommonService.upload(file);
		return Response.success(result);
	}
	
	/**  
	 * @Title: shareUpload
	 * @Description: 文件上传(分享链接用)
	 * @param file
	 * @return
	 * @throws IOException
	 * @author caiyang
	 * @date 2021-07-28 07:52:22 
	 */ 
	@RequestMapping("/shareUpload")
	public Response shareUpload(@RequestParam("file") MultipartFile file) throws IOException {
		String shareCode = this.httpServletRequest.getHeader("shareCode");
		String shareUser = this.httpServletRequest.getHeader("shareUser");
		if(StringUtil.isNullOrEmpty(shareCode) || StringUtil.isNullOrEmpty(shareUser))
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
		}
		Object redisShareCode = redisUtil.get(RedisPrefixEnum.SHAREREPORT.getCode()+shareCode);;
		if(redisShareCode == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.time"));
		}
		Object result = this.iCommonService.upload(file);
		return Response.success(result);
	}
	
	/**  
	 * @Title: upload
	 * @Description: 视频文件上传
	 * @param file
	 * @return
	 * @throws IOException
	 * @author caiyang
	 * @date 2021-07-28 07:52:22 
	 */ 
	@RequestMapping("/uploadFile")
	public Response uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {
		Object result = this.iCommonService.uploadFile(file);
		return Response.success(result);
	}
	
	/**  
	 * @Title: apiTest
	 * @Description: 接口测试
	 * @param apiRequestDto
	 * @return
	 * @author caiyang
	 * @date 2021-07-13 06:43:12 
	 */ 
	@RequestMapping(value = "/apiTest",method = RequestMethod.POST)
	@MethodLog(module="common",remark="接口测试",operateType=Constants.OPERATE_TYPE_REMOTE)
	@Check({"url:required#请求url","requestType:required#请求方式"})
	@RequiresPermissions(value = {"screenTpl_screenDesign"})
	public Response apiTest(@RequestBody ApiRequestDto apiRequestDto) {
		Object result = this.iCommonService.apiTest(apiRequestDto);
		return Response.success(result);
	}
	
	/**  
	 * @MethodName: parseXlsxByUrl
	 * @Description: 通过url解析xlsx文件
	 * @author caiyang
	 * @return Response
	 * @throws Exception 
	 * @date 2024-09-18 10:54:00 
	 */ 
	@RequestMapping(value = "/parseXlsxByUrl",method = RequestMethod.POST)
	@MethodLog(module="common",remark="解析xlsx文件",operateType=Constants.OPERATE_TYPE_SEARCH)
	public String parseXlsxByUrl(@RequestBody JSONObject model) throws Exception {
		httpServletResponse.setHeader("Content-Encoding", "gzip");
		httpServletResponse.setContentType("text/html");
		String resultStr="";
		JSONArray result = this.iCommonService.parseXlsxByUrl(model);
		Response response = new Response();
		response.setCode("200");
		response.setResponseData(result);
		resultStr=JsonUtil.toJson(response);
		try {
	         byte dest[]= Pako_GzipUtils.compress2(resultStr);
	         OutputStream out=httpServletResponse.getOutputStream();
	         out.write(dest);
	         out.close();
	         out.flush();
	     } catch (Exception e) {
	        e.printStackTrace();
	     }
		return null;
	}
	
	@RequestMapping(value = "/printTestRespose",method = RequestMethod.POST)
	public Response printTestRespose() {
		Map<String, String> result = new HashMap<>();
		result.put("name", "SpringReport");
		result.put("dept", "研发部");
		result.put("title", "java开发工程师");
		result.put("dest", "上海");
		result.put("startTime", "2022-01-01");
		result.put("endTime", "2022-01-30");
		result.put("reason", "SpringReport项目部署出差");
		result.put("fee1", "10");
		result.put("fee2", "20");
		result.put("fee3", "30");
		result.put("fee4", "40");
		result.put("fee5", "50");
		result.put("fee6", "60");
		result.put("total", "160");
		result.put("remark", "赶快报销，没钱花了！");
		return Response.success(result);
	}
}
