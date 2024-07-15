package com.springreport.api.reporttype;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.entity.reporttype.ReportType;

 /**  
* @Description: ReportType服务接口
* @author 
* @date 2021-02-09 08:59:59
* @version V1.0  
 */
public interface IReportTypeService extends IService<ReportType> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(ReportType model);

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
	BaseEntity insert(ReportType model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(ReportType model);
	
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
	*<p>Title: getReportType</p>
	*<p>Description: 获取报表类型</p>
	* @author caiyang
	* @return
	*/
	List<ReportType> getReportType(ReportType reportType);
	
}
