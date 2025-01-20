package com.springreport.controller.coedit;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.springreport.annotation.LoginUser;
import com.springreport.api.coedit.ICoeditService;
import com.springreport.base.BaseController;
import com.springreport.base.BaseEntity;
import com.springreport.base.Response;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.coedit.MesDownloadDto;
import com.springreport.dto.coedit.RangeValueDto;
import com.springreport.util.JsonUtil;
import com.springreport.util.Pako_GzipUtils;

/**  
 * @ClassName: CoeditController
 * @Description: 协同编辑用controller
 * @author caiyang
 * @date 2023-08-12 06:10:20 
*/ 
@RestController
@RequestMapping("/springReport/api/coedit")
public class CoeditController extends BaseController{
	
	@Autowired
	private ICoeditService iCoeditService;

	@PostMapping("/load")
	public String load(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(defaultValue = "") String gridKey,@RequestParam(defaultValue = "1") String isLoadALL,
			@LoginUser UserInfoDto userInfoDto)
	{
		 response.setHeader("Content-Encoding", "gzip");
	     response.setContentType("text/html");
	     String resultStr="";
	     if(gridKey.trim().length()!=0){
	    	 List<JSONObject> dbObject = this.iCoeditService.getDefaultByGridKey(gridKey,isLoadALL,userInfoDto);
	    	 if(dbObject!=null){
//                 delErrorKey(dbObject);
                 resultStr=JsonUtil.toJson(dbObject);
             }
	     }
	     try {
	         byte dest[]= Pako_GzipUtils.compress2(resultStr);
	         OutputStream out=response.getOutputStream();
	         out.write(dest);
	         out.close();
	         out.flush();
	     } catch (Exception e) {
	        e.printStackTrace();
	     }
	     return null;
	}
	
	 /**
     * 加载指定表格
     * @param map
     * @param request
     * @param response
     * @param gridKey
     * @param index
     * @return
     */
    @PostMapping("/loadsheet")
    public String loadsheet(Map map, HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(defaultValue = "") String gridKey,
                            @RequestParam(defaultValue = "") String index,
                            @LoginUser UserInfoDto userInfoDto) {
        ////告诉浏览器，当前发送的是gzip格式的内容
        response.setHeader("Content-Encoding", "gzip");
        response.setContentType("text/html");
        String resultStr="";
        if(gridKey.trim().length()!=0){
            try {
                String _id=gridKey;
//                String _checkStr=check(request,_id,null,OperationTypeEnum.Read);
//                if(_checkStr.length()>0){
//                    return null;
//                }
                LinkedHashMap dbObject=null;
                dbObject=this.iCoeditService.getByGridKeys(_id, index);
                if(dbObject!=null){
                    resultStr=JsonUtil.toJson(dbObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        byte dest[]= Pako_GzipUtils.compress2(resultStr);
        OutputStream out=null;
        try {
            out = response.getOutputStream();
            out.write(dest);
            out.close();
            out.flush();
        } catch (IOException e) {
        	e.printStackTrace();
        }catch (Exception ex){
        	ex.printStackTrace();
        }

        return null;
    }
    
    /**  
     * @MethodName: downLoadExcel
     * @Description: 导出excel
     * @author caiyang
     * @param model void
     * @throws Exception 
     * @date 2023-08-31 02:26:20 
     */ 
    @RequestMapping(value = "/downLoadExcel",method = RequestMethod.POST)
    public void downLoadExcel(@RequestBody MesDownloadDto model,@LoginUser UserInfoDto userInfoDto) throws Exception {
    	this.iCoeditService.downLoadExcel(model, httpServletResponse,userInfoDto);
    }
    
    /**  
     * @MethodName: beforeEnterShareMode
     * @Description: 进入共享模式前调用url，锁定共享模式
     * @author caiyang
     * @param model
     * @param userInfoDto
     * @return BaseEntity
     * @date 2024-01-01 04:28:10 
     */ 
    @RequestMapping(value = "/beforeEnterShareMode",method = RequestMethod.POST)
    public Response beforeEnterShareMode(@RequestBody MesDownloadDto model,@LoginUser UserInfoDto userInfoDto) {
    	BaseEntity result = this.iCoeditService.beforeEnterShareMode(model, userInfoDto);
    	return Response.success(result);
    }
    
    /**  
     * @MethodName: getRangeValues
     * @Description: 获取范围内的单元格数据
     * @author caiyang
     * @param model
     * @param userInfoDto
     * @return Response
     * @date 2024-06-12 04:24:14 
     */ 
    @RequestMapping(value = "/getRangeValues",method = RequestMethod.POST)
    public Response getRangeValues(@RequestBody RangeValueDto model,@LoginUser UserInfoDto userInfoDto) {
    	List<Object> result = this.iCoeditService.getRangeValues(model, userInfoDto);
    	return Response.success(result);
    }
	
	 /**
     * 数据返回时，去掉数组变字符串，引发错误的key
     * 删除会发生错误的对象
     */
    private void delErrorKey(List<JSONObject> dbObject){
        if(dbObject!=null){
            for(JSONObject obj :dbObject){
                delErrorKeyByCheck(obj,"calcChain");
                delErrorKeyByCheck(obj,"luckysheet_alternateformat_save");
                delErrorKeyByCheck(obj,"luckysheet_conditionformat_save");
            }
        }
    }
    private void delErrorKeyByCheck(JSONObject obj,String key){
        if(obj.containsKey(key) && obj.get(key) instanceof String){
            obj.remove(key);
        }
    }
}
