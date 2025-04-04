package com.springreport.api.springreportfield;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.reporttpl.ReportTpl;
import com.springreport.entity.springreportfield.SpringreportField;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.template.MesGetTemplateDto;

 /**  
* @Description: SpringreportField服务接口
* @author 
* @date 2025-03-18 10:36:28
* @version V1.0  
 */
public interface ISpringreportFieldService extends IService<SpringreportField> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(SpringreportField model);

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
	BaseEntity insert(SpringreportField model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(SpringreportField model);
	
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
	 * @MethodName: getFields
	 * @Description: 获取模板分类
	 * @author caiyang
	 * @param model
	 * @return List<SpringreportField>
	 * @date 2025-03-18 11:28:17 
	 */ 
	List<SpringreportField> getFields(SpringreportField model);
	
	/**  
	 * @MethodName: getTemplates
	 * @Description: 获取模板
	 * @author caiyang
	 * @param model
	 * @return PageEntity
	 * @date 2025-03-31 09:21:21 
	 */ 
	PageEntity getTemplates(MesGetTemplateDto model,UserInfoDto userInfoDto);
}
