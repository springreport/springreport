package com.springreport.api.reporttpl;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.reporttpl.ReportTpl;
import com.springreport.entity.reporttype.ReportType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.reporttpl.MesChangePwd;
import com.springreport.dto.reporttpl.MesGenerateReportDto;
import com.springreport.dto.reporttpl.MesLuckySheetTplDto;
import com.springreport.dto.reporttpl.MesLuckysheetsTplDto;
import com.springreport.dto.reporttpl.MobilePreviewDto;
import com.springreport.dto.reporttpl.ReportTplDto;
import com.springreport.dto.reporttpl.ReportTplTreeDto;
import com.springreport.dto.reporttpl.ResLuckySheetDataDto;
import com.springreport.dto.reporttpl.ResLuckySheetTplSettingsDto;
import com.springreport.dto.reporttpl.ResPreviewData;
import com.springreport.dto.reporttpl.ResReportTplSettingsDto;
import com.springreport.dto.reporttpl.ResSheetsSettingsDto;
import com.springreport.dto.reporttpl.ShareDto;
import com.springreport.dto.sysrolereport.MesRoleReportDto;

 /**  
* @Description: ReportTpl服务接口
* @author 
* @date 2021-02-13 11:16:33
* @version V1.0  
 */
public interface IReportTplService extends IService<ReportTpl> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(ReportTpl model);
	
	List<ReportTplTreeDto> getChildren(ReportTpl model);

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
	BaseEntity insert(ReportTplDto model);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(ReportTplDto model);
	
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
	 * @Title: getReports
	 * @Description: 获取所有的报表
	 * @return
	 * @author caiyang
	 * @date 2021-08-30 07:28:03 
	 */ 
	List<ReportTpl> getReports();
	
	/**  
	 * @Title: saveLuckySheetTpl
	 * @Description: 保存luckysheet报表模板
	 * @param mesLuckySheetTplDto
	 * @return
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws SQLException 
	 * @date 2022-02-01 10:52:52 
	 */ 
	BaseEntity saveLuckySheetTpl(MesLuckysheetsTplDto mesLuckySheetsTplDto,UserInfoDto userInfoDto) throws JsonProcessingException, SQLException;
	
	/**  
	 * @Title: getLuckySheetTplSettings
	 * @Description: 获取luckysheet模板内容
	 * @param reportTpl
	 * @return
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @throws IOException 
	 * @date 2022-02-01 04:38:13 
	 */ 
	ResSheetsSettingsDto getLuckySheetTplSettings(ReportTplDto reportTpl,UserInfoDto userInfoDto) throws Exception;
	
	/**  
	 * @Title: previewLuckysheetReportData
	 * @Description: luckysheet预览报表
	 * @param mesGenerateReportDto
	 * @return
	 * @author caiyang
	 * @throws Exception 
	 * @date 2022-02-02 08:36:59 
	 */ 
	ResPreviewData previewLuckysheetReportData(MesGenerateReportDto mesGenerateReportDto,UserInfoDto userInfoDto) throws Exception;
	
	/**  
	 * @Title: luckySheetExportExcel
	 * @Description: luckysheet导出excel
	 * @param mesGenerateReportDto
	 * @param httpServletResponse
	 * @throws Exception
	 * @author caiyang
	 * @date 2022-02-15 07:33:21 
	 */ 
	void luckySheetExportExcel(MesGenerateReportDto mesGenerateReportDto,UserInfoDto userInfoDto,HttpServletResponse httpServletResponse) throws Exception;
	
	/**  
	 * @MethodName: validateDesignPwd
	 * @Description: 校验设计密码
	 * @author caiyang
	 * @param reportTpl 
	 * @return void
	 * @date 2022-07-05 08:19:27 
	 */  
	ReportTpl validateDesignPwd(ReportTpl reportTpl);
	
	/**  
	 * @MethodName: changeDesignPwd
	 * @Description: 修改设计密码
	 * @author caiyang
	 * @param reportTpl
	 * @return 
	 * @return BaseEntity
	 * @date 2022-07-05 05:50:26 
	 */  
	BaseEntity changeDesignPwd(MesChangePwd model);
	
	/**  
	 * @MethodName: getRoleReports
	 * @Description: 获取角色报表
	 * @author caiyang
	 * @param model
	 * @return 
	 * @return PageEntity
	 * @date 2022-07-06 08:28:50 
	 */  
	List<ReportTplTreeDto> getRoleReports(MesRoleReportDto model);
	
	/**  
	 * @MethodName: saveReportFormsTpl
	 * @Description: 保存填报模板
	 * @author caiyang
	 * @param mesLuckysheetsTplDto 
	 * @return void
	 * @throws JsonProcessingException 
	 * @throws SQLException 
	 * @date 2022-11-16 01:39:41 
	 */  
	BaseEntity saveReportFormsTpl(MesLuckysheetsTplDto mesLuckysheetsTplDto,UserInfoDto userInfoDto) throws JsonProcessingException, SQLException;
	
	/**  
	 * @MethodName: getReportFormsTpl
	 * @Description: 获取填报模板
	 * @author caiyang
	 * @param reportTpl
	 * @return 
	 * @return ResSheetsSettingsDto
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2022-11-17 08:31:39 
	 */  
	ResSheetsSettingsDto getReportFormsTpl(ReportTpl reportTpl,UserInfoDto userInfoDto) throws JsonMappingException, JsonProcessingException;
	
	/**  
	 * @MethodName: copyReport
	 * @Description: 复制报表
	 * @author caiyang
	 * @param reportTpl
	 * @return 
	 * @return BaseEntity
	 * @date 2022-12-05 03:15:28 
	 */  
	BaseEntity copyReport(ReportTplDto model);
	
	/**  
	 * @MethodName: transf2OnlineReport
	 * @Description: 转成在线报表文档
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @return 
	 * @return BaseEntity
	 * @throws Exception 
	 * @date 2023-02-06 08:31:38 
	 */  
	BaseEntity transf2OnlineReport(MesGenerateReportDto mesGenerateReportDto,UserInfoDto userInfoDto) throws Exception;
	
	/**  
	 * @MethodName: getSheetPdf
	 * @Description: 生成sheet页的pdf并返回访问路径
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param userInfoDto
	 * @return 
	 * @return Map<String,Object>
	 * @throws Exception 
	 * @date 2023-04-03 01:47:59 
	 */  
	Map<String, Object> getSheetPdf(MesGenerateReportDto mesGenerateReportDto,UserInfoDto userInfoDto,HttpServletResponse httpServletResponse) throws Exception;
	

	JSONArray uploadExcel(MultipartFile file,String gridKey,UserInfoDto userInfoDto) throws Exception;
	
	/**  
	 * @MethodName: getShareUrl
	 * @Description: 获取分享url
	 * @author caiyang
	 * @param shareDto
	 * @param userInfoDto
	 * @return ShareDto
	 * @date 2023-06-25 02:03:51 
	 */ 
	ShareDto getShareUrl(ShareDto shareDto,UserInfoDto userInfoDto);
	
	/**  
	 * @MethodName: getMobileInfo
	 * @Description: 获取手机报表信息
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param userInfoDto
	 * @return MobilePreviewDto
	 * @throws Exception 
	 * @date 2023-06-29 09:44:20 
	 */ 
	MobilePreviewDto getMobileInfo(MesGenerateReportDto mesGenerateReportDto,UserInfoDto userInfoDto) throws Exception;
	
	/**  
	 * @MethodName: reportTask
	 * @Description: 报表导出定时任务
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param userInfoDto
	 * @throws Exception void
	 * @date 2023-07-28 08:55:44 
	 */ 
	void reportTask(MesGenerateReportDto mesGenerateReportDto,UserInfoDto userInfoDto) throws Exception;
	
	/**  
	 * @MethodName: getSheetPdfStream
	 * @Description: 获取pdf文件流
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param userInfoDto
	 * @param httpServletResponse
	 * @throws Exception void
	 * @date 2023-12-03 08:16:24 
	 */ 
	void getSheetPdfStream(MesGenerateReportDto mesGenerateReportDto,UserInfoDto userInfoDto,HttpServletResponse httpServletResponse) throws Exception;
	
	/**  
	 * @MethodName: uploadReportTpl
	 * @Description: 上传报表模板(excel)
	 * @author caiyang
	 * @param file
	 * @param userInfoDto
	 * @return
	 * @throws Exception JSONArray
	 * @date 2023-12-13 10:23:27 
	 */ 
	JSONArray uploadReportTpl(MultipartFile file,long tplId,int isFormsReport,UserInfoDto userInfoDto) throws Exception;
	
	/**  
	 * @Title: getTplDatasetsColumnNames
	 * @Description: 获取模板数据集所有的列名，列名格式是dataSetName.${columnName}，后期可以优化，放到redis中
	 * @param tplId
	 * @return
	 * @author caiyang
	 * @throws SQLException 
	 * @date 2021-05-25 07:01:49 
	 */ 
	List<List<String>> getTplDatasetsColumnNames(Long tplId,Map<String, String> datasetNameIdMap,UserInfoDto userInfoDto) throws SQLException;

	/**  
	 * @MethodName: getAllTpls
	 * @Description: 获取所有的模板
	 * @author caiyang
	 * @param model
	 * @return List<ReportTpl>
	 * @date 2024-01-13 12:27:30 
	 */ 
	List<ReportTpl> getAllTpls(ReportTpl model);
	
	/**  
	 * @MethodName: getTplAuth
	 * @Description: 获取模板权限
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return JSONObject
	 * @date 2024-03-07 06:30:23 
	 */ 
	JSONObject getTplAuth(ReportTpl model,UserInfoDto userInfoDto);
}
