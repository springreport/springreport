package com.springreport.impl.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springreport.api.doctpl.IDocTplService;
import com.springreport.api.index.IIndexService;
import com.springreport.api.onlinetpl.IOnlineTplService;
import com.springreport.api.reportdatasource.IReportDatasourceService;
import com.springreport.api.reporttpl.IReportTplService;
import com.springreport.api.screentpl.IScreenTplService;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.index.IndexDto;
import com.springreport.entity.doctpl.DocTpl;
import com.springreport.entity.onlinetpl.OnlineTpl;
import com.springreport.entity.reportdatasource.ReportDatasource;
import com.springreport.entity.reporttpl.ReportTpl;
import com.springreport.entity.screentpl.ScreenTpl;
import com.springreport.entity.sysmerchant.SysMerchant;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.YesNoEnum;

@Service
public class IIndexServiceImpl implements IIndexService{
	
	@Autowired
	private IReportTplService iReportTplService;
	
	@Autowired
	private IDocTplService iDocTplService;
	
	@Autowired
	private IOnlineTplService iOnlineTplService;
	
	@Autowired
	private IScreenTplService iScreenTplService;
	
	@Autowired
	private IReportDatasourceService iReportDatasourceService;
	
	@Value("${merchantmode}")
    private Integer merchantmode;

	/**  
	 * @MethodName: getIndexData
	 * @Description: 获取首页数据
	 * @author caiyang
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.index.IIndexService#getIndexData(com.springreport.base.UserInfoDto)
	 * @date 2024-12-08 06:54:04 
	 */
	@Override
	public IndexDto getIndexData(SysMerchant sysMerchant,UserInfoDto userInfoDto) {
		IndexDto result = new IndexDto();
		QueryWrapper<ReportTpl> reportTplQueryWrapper = new QueryWrapper<>();
		if(YesNoEnum.YES.getCode().intValue() == merchantmode.intValue()) {
			reportTplQueryWrapper.eq("merchant_no", sysMerchant.getMerchantNo());
		}
		reportTplQueryWrapper.eq("is_template", YesNoEnum.NO.getCode());
		reportTplQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		long excelCount = this.iReportTplService.count(reportTplQueryWrapper);
		result.setExcelCount(excelCount);
		
		QueryWrapper<DocTpl> docTplQueryWrapper = new QueryWrapper<>();
		if(YesNoEnum.YES.getCode().intValue() == merchantmode.intValue()) {
			docTplQueryWrapper.eq("merchant_no", sysMerchant.getMerchantNo());
		}
		docTplQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		docTplQueryWrapper.eq("is_template", YesNoEnum.NO.getCode());
		long wordCount = this.iDocTplService.count(docTplQueryWrapper);
		result.setWordCount(wordCount);
		
		QueryWrapper<OnlineTpl> onlineTplQueryWrapper = new QueryWrapper<>();
		if(YesNoEnum.YES.getCode().intValue() == merchantmode.intValue()) {
			onlineTplQueryWrapper.eq("merchant_no", sysMerchant.getMerchantNo());
		}
		onlineTplQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		long coeditCount = this.iOnlineTplService.count(onlineTplQueryWrapper);
		result.setCoeditCount(coeditCount);
		
		QueryWrapper<ScreenTpl> screenTplQueryWrapper = new QueryWrapper<>();
		if(YesNoEnum.YES.getCode().intValue() == merchantmode.intValue()) {
			screenTplQueryWrapper.eq("merchant_no", sysMerchant.getMerchantNo());
		}
		screenTplQueryWrapper.eq("is_template", YesNoEnum.NO.getCode());
		screenTplQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		long screenCount = this.iScreenTplService.count(screenTplQueryWrapper);
		result.setScreenCount(screenCount);
		
		QueryWrapper<ReportDatasource> reportDatasourceQueryWrapper = new QueryWrapper<>();
		if(YesNoEnum.YES.getCode().intValue() == merchantmode.intValue()) {
			reportDatasourceQueryWrapper.eq("merchant_no", sysMerchant.getMerchantNo());
		}
		reportDatasourceQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		long datasourceCount = this.iReportDatasourceService.count(reportDatasourceQueryWrapper);
		result.setDatasourceCount(datasourceCount);
		return result;
	}

}
