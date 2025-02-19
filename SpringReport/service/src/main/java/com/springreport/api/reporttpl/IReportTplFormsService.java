package com.springreport.api.reporttpl;

import com.alibaba.fastjson.JSONObject;
import com.springreport.base.BaseEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.reporttpl.MesGenerateReportDto;
import com.springreport.dto.reporttpl.ReportDataDto;
import com.springreport.dto.reporttpl.ResPreviewData;
import com.springreport.entity.reporttpl.ReportTpl;

public interface IReportTplFormsService {

	/**  
	 * @MethodName: previewLuckysheetReportFormsData
	 * @Description: 填报报表预览数据
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param userInfoDto
	 * @return 
	 * @return ResPreviewData
	 * @throws Exception 
	 * @date 2022-11-17 04:11:17 
	 */  
	ResPreviewData previewLuckysheetReportFormsData(MesGenerateReportDto mesGenerateReportDto,UserInfoDto userInfoDto,ReportTpl reportTpl,boolean isPagination) throws Exception;
	
	/**  
	 * @MethodName: reportData
	 * @Description: 上报数据
	 * @author caiyang
	 * @param model
	 * @return 
	 * @return BaseEntity
	 * @date 2022-11-23 07:22:57 
	 */  
	BaseEntity reportData(ReportDataDto model,UserInfoDto userInfoDto);
	
	/**  
	 * @MethodName: deleteReportData
	 * @Description: 填报报表删除数据
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return BaseEntity
	 * @date 2025-02-17 12:22:43 
	 */ 
	BaseEntity deleteReportData(JSONObject model,UserInfoDto userInfoDto);
	
}
