package com.springreport.api.doctpl;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.doctpl.DocTpl;
import com.springreport.entity.doctplsettings.DocTplSettings;
import com.springreport.entity.reporttype.ReportType;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.doctpl.DocDto;
import com.springreport.dto.doctpl.DocTableDto;
import com.springreport.dto.doctpl.DocTplDto;
import com.springreport.dto.doctpl.DocTplSettingsDto;
import com.springreport.dto.doctpl.DocTplTreeDto;
import com.springreport.dto.reporttpl.MesGenerateReportDto;
import com.springreport.dto.reporttpldataset.ReportDatasetDto;

 /**  
* @Description: DocTpl服务接口
* @author 
* @date 2024-05-02 08:55:33
* @version V1.0  
 */
public interface IDocTplService extends IService<DocTpl> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(DocTpl model);
	
	List<DocTplTreeDto> getChildren(DocTpl model);

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
	BaseEntity insert(DocTplDto model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(DocTplDto model);
	
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
	 * @MethodName: getDocTplSettings
	 * @Description: 获取doc文档模板数据
	 * @author caiyang
	 * @param model
	 * @return DocTplSettings
	 * @date 2024-05-03 09:53:33 
	 */ 
	DocTplSettingsDto getDocTplSettings(DocTplSettings model);
	
	/**  
	 * @MethodName: saveDocTplSettings
	 * @Description: 保存模板数据
	 * @author caiyang
	 * @param model void
	 * @date 2024-05-03 04:09:51 
	 */ 
	BaseEntity saveDocTplSettings(DocTplSettingsDto model);
	
	/**  
	 * @MethodName: downLoadDocTpl
	 * @Description: 导出word模板
	 * @author caiyang
	 * @param model void
	 * @throws Exception 
	 * @date 2024-05-09 05:09:54 
	 */ 
	void downLoadDocTpl(DocTplSettings model,HttpServletResponse httpServletResponse) throws Exception;
	
	/**  
	 * @MethodName: previewDoc
	 * @Description: doc预览
	 * @author caiyang
	 * @param model void
	 * @throws Exception 
	 * @throws SQLException 
	 * @date 2024-05-07 09:25:55 
	 */ 
	Map<String, Object> previewDoc(MesGenerateReportDto model,UserInfoDto userInfoDto) throws SQLException, Exception;
	
	/**  
	 * @MethodName: uploadDocx
	 * @Description: 上传docx文件并解析
	 * @author caiyang
	 * @param file
	 * @return DocTableDto
	 * @throws Exception 
	 * @date 2024-09-28 07:19:54 
	 */ 
	DocDto uploadDocx(MultipartFile file) throws Exception;
	
	/**  
	 * @MethodName: getDatasetDatas
	 * @Description: 获取数据集数据
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param reportTplDataset
	 * @param reportSqls
	 * @param paramsType
	 * @param userInfoDto
	 * @param apiCache
	 * @param subParams
	 * @return
	 * @throws Exception Object
	 * @date 2025-03-04 11:38:43 
	 */ 
	Object getDatasetDatas(MesGenerateReportDto mesGenerateReportDto,ReportDatasetDto reportTplDataset,List<Map<String, String>> reportSqls,
			Map<String, List<String>> paramsType,UserInfoDto userInfoDto,Map<String, String> apiCache,Map<String, Object> subParams) throws Exception;
	
	/**  
	 * @MethodName: copyReport
	 * @Description: 复制报表
	 * @author caiyang
	 * @param docTpl void
	 * @date 2025-04-01 04:47:49 
	 */ 
	BaseEntity copyReport(DocTpl docTpl);
}
