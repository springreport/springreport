package com.springreport.api.reporttplsheet;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.reporttplsheet.ReportTplSheet;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

 /**  
* @Description: ReportTplSheet服务接口
* @author 
* @date 2022-10-18 06:23:34
* @version V1.0  
 */
public interface IReportTplSheetService extends IService<ReportTplSheet> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(ReportTplSheet model);

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
	BaseEntity insert(ReportTplSheet model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(ReportTplSheet model);
	
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
	 * @MethodName: getAllSheetNames
	 * @Description: 获取所有的sheet名称
	 * @author caiyang
	 * @param id
	 * @return List<String>
	 * @date 2023-12-13 10:37:33 
	 */ 
	List<String> getAllSheetNames(Long tplId);
}
