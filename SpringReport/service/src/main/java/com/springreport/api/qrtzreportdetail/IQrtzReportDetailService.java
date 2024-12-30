package com.springreport.api.qrtzreportdetail;
import java.text.ParseException;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.qrtzreportdetail.QrtzReportDetail;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.qrtzreportdetail.ReqIndexQrtzDto;

 /**  
* @Description: QrtzReportDetail服务接口
* @author 
* @date 2023-07-28 09:43:20
* @version V1.0  
 */
public interface IQrtzReportDetailService extends IService<QrtzReportDetail> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	 * @throws ParseException 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(QrtzReportDetail model) throws ParseException;

	/**
	*<p>Title: getDetail</p>
	*<p>Description: 获取详情</p>
	* @author caiyang
	* @param id
	* @return
	 * @throws ParseException 
	*/
	BaseEntity getDetail(Long id) throws ParseException;
	
	/**
	*<p>Title: insert</p>
	*<p>Description: 新增数据</p>
	* @author caiyang
	* @param model
	* @return
	 * @throws Exception 
	*/
	BaseEntity insert(QrtzReportDetail model) throws Exception;
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	 * @throws Exception 
	*/
	BaseEntity update(QrtzReportDetail model) throws Exception;
	
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
	 * @MethodName: runTask
	 * @Description: 执行一次定时任务
	 * @author caiyang
	 * @param id
	 * @return BaseEntity
	 * @throws Exception 
	 * @date 2023-07-30 09:58:35 
	 */ 
	BaseEntity runTask(Long id) throws Exception;
	
	/**  
	 * @MethodName: pauseTask
	 * @Description: 暂停任务
	 * @author caiyang
	 * @param id
	 * @return BaseEntity
	 * @date 2023-07-30 11:40:40 
	 */ 
	BaseEntity pauseTask(Long id);
	
	/**  
	 * @MethodName: resumeTask
	 * @Description: 恢复任务
	 * @author caiyang
	 * @param id
	 * @return BaseEntity
	 * @date 2023-07-30 11:42:46 
	 */ 
	BaseEntity resumeTask(Long id);
	
	/**  
	 * @MethodName: getIndexTaskList
	 * @Description: 获取首页任务列表
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return PageEntity
	 * @date 2024-12-10 06:02:50 
	 */ 
	PageEntity getIndexTaskList(ReqIndexQrtzDto model,UserInfoDto userInfoDto);
}
