package com.springreport.api.coedit;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.base.BaseEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.coedit.MesDownloadDto;
import com.springreport.dto.coedit.RangeValueDto;
import com.springreport.entity.luckysheet.Luckysheet;
import com.springreport.entity.onlinetpl.OnlineTpl;
import com.springreport.dto.coedit.WSUserModel;

/**  
 * @ClassName: ICoeditService
 * @Description:协同编辑用服务
 * @author caiyang
 * @date 2023-08-12 06:23:48 
*/ 
public interface ICoeditService extends IService<Luckysheet>{

	/**  
	 * @MethodName: getDefaultByGridKey
	 * @Description: 获取表格数据 按gridKey获取,默认载入status为1
	 * @author caiyang
	 * @param listId
	 * @return List<JSONObject>
	 * @date 2023-08-12 06:25:35 
	 */ 
	public List<JSONObject> getDefaultByGridKey(String listId,String isLoadALL,UserInfoDto userInfoDto);
	
	
	/**
     * 获取指定的xls激活的sheet页的 返回index（控制块）
     *
     * @param listId
     * @return
     */
	public String getFirstBlockIndexByGridKey(String listId,String blockId);
	
	/**  
	 * @MethodName: Operation_mv
	 * @Description: TODO (暂时还不知道这个方法具体是干嘛的)
	 * @author caiyang
	 * @param gridKey
	 * @param bson void
	 * @date 2023-08-13 08:20:25 
	 */ 
	public void Operation_mv(String gridKey, JSONObject bson, WSUserModel wsUserModel);
	
	/**  
	 * @MethodName: updateRvDbContent
	 * @Description: TODO (暂时还不知道这个方法具体是干嘛的)
	 * @author caiyang
	 * @param gridKey
	 * @param bson
	 * @param key
	 * @return String
	 * @date 2023-08-14 09:24:49 
	 */ 
	public String updateRvDbContent(String gridKey, JSONObject bson, String key,WSUserModel wsUserModel);
	
	/**  
	 * @MethodName: getIndexRvForThread
	 * @Description: 批量单元格操作，将需要操作的数据记录在redis中当再次收到rv_end指令时，在进行具体的实现操作
	 * @author caiyang
	 * @param gridKey
	 * @param bson
	 * @return String
	 * @date 2023-08-14 02:21:06 
	 */ 
	public String getIndexRvForThread(String gridKey, JSONObject bson,String key,WSUserModel wsUserModel);
	
	/**  
	 * @MethodName: handleUpdate
	 * @Description: 执行更新操作,集合拆分
	 * @author caiyang
	 * @param gridKey
	 * @param bson
	 * @return String
	 * @date 2023-08-14 04:34:29 
	 */ 
	public String handleUpdate(String gridKey, Object bson,WSUserModel wsUserModel);
	
	/**  
	 * @MethodName: getByGridKeys
	 * @Description: 获取sheet数据  参数为gridKey（表格主键） 和 index（sheet主键合集
	 * @author caiyang
	 * @param listId
	 * @param indexs
	 * @return LinkedHashMap
	 * @date 2023-08-18 06:06:54 
	 */ 
	public LinkedHashMap getByGridKeys(String listId, String index);
	
	/**  
	 * @MethodName: getIdByListId
	 * @Description: 根据list获取主键id
	 * @author caiyang
	 * @return String
	 * @date 2023-08-19 09:10:29 
	 */
	String getIdByListId(String listId);
	
	/**  
	 * @MethodName: downLoadExcel
	 * @Description: 导出excel
	 * @author caiyang
	 * @param model void
	 * @throws Exception 
	 * @date 2023-08-31 02:29:12 
	 */ 
	void downLoadExcel(MesDownloadDto model,HttpServletResponse httpServletResponse,UserInfoDto userInfoDto) throws Exception;
	
	/**  
	 * @MethodName: beforeEnterShareMode
	 * @Description: 进入共享模式前调用url，锁定共享模式
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return BaseEntity
	 * @date 2024-01-01 04:31:45 
	 */ 
	BaseEntity beforeEnterShareMode(MesDownloadDto model,UserInfoDto userInfoDto);
	
	/**  
	 * @MethodName: getRangeValues
	 * @Description: 获取范围内的单元格数据
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return JSONArray
	 * @date 2024-06-12 04:26:31 
	 */ 
	List<Object> getRangeValues(RangeValueDto model,UserInfoDto userInfoDto);
	
}
