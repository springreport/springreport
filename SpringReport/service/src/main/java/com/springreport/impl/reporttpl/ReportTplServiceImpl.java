package com.springreport.impl.reporttpl;

import com.springreport.entity.luckysheet.Luckysheet;
import com.springreport.entity.luckysheetcell.LuckysheetCell;
import com.springreport.entity.luckysheethis.LuckysheetHis;
import com.springreport.entity.luckysheetonlinecell.LuckysheetOnlineCell;
import com.springreport.entity.luckysheetreportblockcell.LuckysheetReportBlockCell;
import com.springreport.entity.luckysheetreportcell.LuckysheetReportCell;
import com.springreport.entity.luckysheetreportformscell.LuckysheetReportFormsCell;
import com.springreport.entity.onlinetpl.OnlineTpl;
import com.springreport.entity.onlinetplsheet.OnlineTplSheet;
import com.springreport.entity.reportdatasource.ReportDatasource;
import com.springreport.entity.reportdatasourcedictdata.ReportDatasourceDictData;
import com.springreport.entity.reportformsdatasource.ReportFormsDatasource;
import com.springreport.entity.reportformsdatasourceattrs.ReportFormsDatasourceAttrs;
import com.springreport.entity.reportrangeauth.ReportRangeAuth;
import com.springreport.entity.reportrangeauthuser.ReportRangeAuthUser;
import com.springreport.entity.reportsheetpdfprintsetting.ReportSheetPdfPrintSetting;
import com.springreport.entity.reporttpl.ReportTpl;
import com.springreport.entity.reporttpldataset.ReportTplDataset;
import com.springreport.entity.reporttpldatasource.ReportTplDatasource;
import com.springreport.entity.reporttplsheet.ReportTplSheet;
import com.springreport.entity.reporttplsheetchart.ReportTplSheetChart;
import com.springreport.entity.reporttype.ReportType;
import com.springreport.entity.sysrolesheet.SysRoleSheet;
import com.springreport.entity.sysuser.SysUser;
import com.springreport.mapper.luckysheetreportcell.LuckysheetReportCellMapper;
import com.springreport.mapper.reporttpl.ReportTplMapper;
import com.springreport.report.calculate.LuckySheetAvgCalculate;
import com.springreport.report.calculate.LuckySheetCompareCalculate;
import com.springreport.report.calculate.LuckySheetCompareRateCalculate;
import com.springreport.report.calculate.LuckySheetCountCalculate;
import com.springreport.report.CellUtil;
import com.springreport.report.calculate.Calculate;
import com.springreport.report.calculate.GroupAddCalculate;
import com.springreport.report.calculate.GroupAvgCalculate;
import com.springreport.report.calculate.GroupCompareCalculate;
import com.springreport.report.calculate.GroupCompareRateCalculate;
import com.springreport.report.calculate.GroupCountCalculate;
import com.springreport.report.calculate.GroupMaxCalculate;
import com.springreport.report.calculate.GroupMinCalculate;
import com.springreport.report.calculate.LuckySheetAddCalculate;
import com.springreport.report.calculate.LuckySheetMaxCalculate;
import com.springreport.report.calculate.LuckySheetMinCalculate;
import com.springreport.report.dataprocess.LuckySheetBasicDynamicDataProcess;
import com.springreport.report.dataprocess.LuckySheetListDataProcess;
import com.springreport.api.coedit.ICoeditService;
import com.springreport.api.common.ICommonService;
import com.springreport.api.luckysheet.ILuckysheetService;
import com.springreport.api.luckysheethis.ILuckysheetHisService;
import com.springreport.api.luckysheetonlinecell.ILuckysheetOnlineCellService;
import com.springreport.api.luckysheetreportblockcell.ILuckysheetReportBlockCellService;
import com.springreport.api.luckysheetreportcell.ILuckysheetReportCellService;
import com.springreport.api.luckysheetreportformscell.ILuckysheetReportFormsCellService;
import com.springreport.api.onlinetpl.IOnlineTplService;
import com.springreport.api.onlinetplsheet.IOnlineTplSheetService;
import com.springreport.api.reportdatasource.IReportDatasourceService;
import com.springreport.api.reportdatasourcedictdata.IReportDatasourceDictDataService;
import com.springreport.api.reportformsdatasource.IReportFormsDatasourceService;
import com.springreport.api.reportformsdatasourceattrs.IReportFormsDatasourceAttrsService;
import com.springreport.api.reportrangeauth.IReportRangeAuthService;
import com.springreport.api.reportrangeauthuser.IReportRangeAuthUserService;
import com.springreport.api.reportsheetpdfprintsetting.IReportSheetPdfPrintSettingService;
import com.springreport.api.reporttpl.IReportTplFormsService;
import com.springreport.api.reporttpl.IReportTplService;
import com.springreport.api.reporttpldataset.IReportTplDatasetService;
import com.springreport.api.reporttpldatasource.IReportTplDatasourceService;
import com.springreport.api.reporttplsheet.IReportTplSheetService;
import com.springreport.api.reporttplsheetchart.IReportTplSheetChartService;
import com.springreport.api.reporttype.IReportTypeService;
import com.springreport.api.sysrolesheet.ISysRoleSheetService;
import com.springreport.api.sysuser.ISysUserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.collections4.ListUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.springreport.util.CheckUtil;
import com.springreport.util.CustomFunction;
import com.springreport.util.DateUtil;
import com.springreport.util.DocumentToLuckysheetUtil;
import com.springreport.util.FileUtil;
import com.springreport.util.HttpClientUtil;
import com.springreport.util.InfluxDBConnection;
import com.springreport.util.JWTUtil;
import com.springreport.util.JdbcUtils;
import com.springreport.util.ListUtil;
import com.springreport.util.LuckysheetUtil;
import com.springreport.util.Md5Util;
import com.springreport.util.MessageUtil;
import com.springreport.util.MongoClientUtil;
import com.springreport.util.ParamUtil;
import com.springreport.util.PropertyRatio;
import com.springreport.util.RedisUtil;
import com.springreport.util.ReportDataUtil;
import com.springreport.util.ReportExcelUtil;
import com.springreport.util.RowConvertColUtil;
import com.springreport.util.SendEmailUtil;
import com.springreport.util.SheetUtil;
import com.springreport.util.StringUtil;
import com.springreport.util.UUIDUtil;
import com.springreport.util.UrlUtils;
import com.springreport.util.VelocityUtil;

import cn.hutool.core.convert.Convert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Options;

import net.sf.jsqlparser.JSQLParserException;

import com.springreport.base.BaseEntity;
import com.springreport.base.DataSourceConfig;
import com.springreport.base.EmailAttachementDto;
import com.springreport.base.EsDataSourceConfig;
import com.springreport.base.InfluxDbDataSourceConfig;
import com.springreport.base.MesExportExcel;
import com.springreport.base.MesSheetConfig;
import com.springreport.base.PageEntity;
import com.springreport.base.TDengineConnection;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.dto.onlinetpl.OnlineTplTreeDto;
import com.springreport.dto.reporttpl.AlternateformatDto;
import com.springreport.dto.reporttpl.GroupSummaryData;
import com.springreport.base.LuckySheetBindData;
import com.springreport.dto.reporttpl.MesChangePwd;
import com.springreport.dto.reporttpl.MesGenerateReportDto;
import com.springreport.dto.reporttpl.MesLuckySheetTplDto;
import com.springreport.dto.reporttpl.MesLuckysheetsTplDto;
import com.springreport.dto.reporttpl.MobilePreviewDto;
import com.springreport.dto.reporttpl.ReportCellDictsDto;
import com.springreport.dto.reporttpl.ReportTplDto;
import com.springreport.dto.reporttpl.ReportTplTreeDto;
import com.springreport.dto.reporttpl.ResLuckySheetDataDto;
import com.springreport.dto.reporttpl.ResLuckySheetTplSettingsDto;
import com.springreport.dto.reporttpl.ResMobileReport;
import com.springreport.dto.reporttpl.ResPreviewData;
import com.springreport.dto.reporttpl.ResSheetsSettingsDto;
import com.springreport.dto.reporttpl.SaveLuckySheetTplDto;
import com.springreport.dto.reporttpl.ShareDto;
import com.springreport.dto.sysrolereport.MesRoleReportDto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.springreport.enums.AggregateTypeEnum;
import com.springreport.enums.BorderTypeEnum;
import com.springreport.enums.CellExtendEnum;
import com.springreport.enums.CellFormatEnum;
import com.springreport.enums.CellValueTypeEnum;
import com.springreport.enums.DataFromEnum;
import com.springreport.enums.DatasetTypeEnum;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.FunctionExpressEnum;
import com.springreport.enums.FunctionTypeEnum;
import com.springreport.enums.LuckySheetPropsEnum;
import com.springreport.enums.MqTypeEnums;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.enums.ShareTypeEnum;
import com.springreport.enums.SqlTypeEnum;
import com.springreport.enums.TplTypeEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.excel2pdf.Excel2Pdf;
import com.springreport.excel2pdf.ExcelObject;
import com.springreport.excel2pdf.PrintSettingsDto;
import com.springreport.excel2pdf.ResMobileInfos;
import com.springreport.excel2pdf.TableCell;
import com.springreport.exception.BizException;
//import com.springreport.function.CustomSpringReportFunction;
import com.springreport.base.CustomSpringReportFunction;
import com.springreport.impl.codeit.MqProcessService;

 /**  
* @Description: ReportTpl服务实现
* @author 
* @date 2021-02-13 11:16:33
* @version V1.0  
 */
@Service
public class ReportTplServiceImpl extends ServiceImpl<ReportTplMapper, ReportTpl> implements IReportTplService {
  
	@Autowired
	private IReportTplDatasourceService iReportTplDatasourceService;
	
	
	@Autowired
	private IReportTplDatasetService iReportTplDatasetService;
	
	@Autowired
	private IReportDatasourceService iReportDatasourceService;
	
	
	@Autowired
	private ILuckysheetReportCellService iLuckysheetReportCellService;
	
	@Autowired
	private ILuckysheetReportBlockCellService iLuckysheetReportBlockCellService;
	
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private IReportTplSheetService iReportTplSheetService;
	
	@Autowired
	@Lazy
	private ISysRoleSheetService iSysRoleSheetService;
	
	@Autowired
	private ILuckysheetReportFormsCellService iLuckysheetReportFormsCellService;
	
	@Autowired
	private IReportFormsDatasourceService iReportFormsDatasourceService;
	
	@Autowired
	private IReportFormsDatasourceAttrsService iReportFormsDatasourceAttrsService;
	
	@Autowired
	private IOnlineTplService iOnlineTplService;
	
	@Autowired
	private IOnlineTplSheetService iOnlineTplSheetService;
	
	@Autowired
	private ILuckysheetOnlineCellService iLuckysheetOnlineCellService;
	
	@Autowired
	private LuckysheetReportCellMapper luckysheetReportCellMapper;
	
	@Autowired
	private IReportDatasourceDictDataService iReportDatasourceDictDataService;
	
	@Autowired
	private IReportTplSheetChartService iReportTplSheetChartService;
	
	@Autowired
	private SendEmailUtil sendEmailUtil;
	
	@Autowired
	private ICommonService iCommonService;
	
	@Autowired
	private ICoeditService iCoeditService;
	
	@Autowired
	private MqProcessService mqProcessService;
	
	@Autowired
	private ILuckysheetService iLuckysheetService;
	
	@Autowired
	private ILuckysheetHisService iLuckysheetHisService;
	
	@Autowired
	private IReportSheetPdfPrintSettingService iReportSheetPdfPrintSettingService;
	
	@Autowired
	@Lazy
	private IReportTplFormsService iReportTplFormsService;
	
	@Autowired
	private IReportRangeAuthService iReportRangeAuthService;
	
	@Autowired
	private IReportRangeAuthUserService iReportRangeAuthUserService;
	
	@Autowired
	private ISysUserService iSysUserService;
	
	@Value("${merchantmode}")
    private Integer merchantmode;
	
	@Autowired
	private IReportTypeService iReportTypeService;
	
	@Value("${thirdParty.type}")
    private String thirdPartyType;
	
	/**
     * 本地保存路径
     */
	@Value("${file.path}")
    private String dirPath;
	
	@Value("${show.report.sql}")
	private boolean showReportSql;
	
	@Value("${authentic.enabale}")
    private boolean authenticEnabale;
	
	
	private static Map<Integer,LuckySheetBasicDynamicDataProcess> luckySheetDataProcess=new HashMap<Integer,LuckySheetBasicDynamicDataProcess>();
	
	private static Map<Integer, Calculate> calculates = new HashMap<Integer, Calculate>();
	
	private static Map<Integer, Calculate> luckySheetCalculates = new HashMap<Integer, Calculate>();
	
	private static Map<Integer, Calculate> luckySheetGroupCalculates = new HashMap<Integer, Calculate>();
	
	static{
		luckySheetDataProcess.put(TplTypeEnum.LIST.getCode(),new LuckySheetListDataProcess());
	}
	
	static{
		luckySheetCalculates.put(FunctionTypeEnum.SUM.getCode(), new LuckySheetAddCalculate());
		luckySheetCalculates.put(FunctionTypeEnum.AVG.getCode(), new LuckySheetAvgCalculate());
		luckySheetCalculates.put(FunctionTypeEnum.MAX.getCode(), new LuckySheetMaxCalculate());
		luckySheetCalculates.put(FunctionTypeEnum.MIN.getCode(), new LuckySheetMinCalculate());
		luckySheetCalculates.put(FunctionTypeEnum.COUNT.getCode(), new LuckySheetCountCalculate());
		luckySheetCalculates.put(FunctionTypeEnum.COMPARE.getCode(), new LuckySheetCompareCalculate());
		luckySheetCalculates.put(FunctionTypeEnum.COMPARERATE.getCode(), new LuckySheetCompareRateCalculate());
	}
	
	static{
		luckySheetGroupCalculates.put(FunctionTypeEnum.SUM.getCode(), new GroupAddCalculate());
		luckySheetGroupCalculates.put(FunctionTypeEnum.AVG.getCode(), new GroupAvgCalculate());
		luckySheetGroupCalculates.put(FunctionTypeEnum.MAX.getCode(), new GroupMaxCalculate());
		luckySheetGroupCalculates.put(FunctionTypeEnum.MIN.getCode(), new GroupMinCalculate());
		luckySheetGroupCalculates.put(FunctionTypeEnum.COUNT.getCode(), new GroupCountCalculate());
		luckySheetGroupCalculates.put(FunctionTypeEnum.COMPARE.getCode(), new GroupCompareCalculate());
		luckySheetGroupCalculates.put(FunctionTypeEnum.COMPARERATE.getCode(), new GroupCompareRateCalculate());
	}
	
	private static CustomSpringReportFunction customSpringReportFunction;
	
	static {
		customSpringReportFunction = new CustomSpringReportFunction();
	}
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(ReportTpl model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		model.setIsTemplate(model.getIsTemplate());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<ReportTplDto> list = this.baseMapper.getTableList(model);
		if(!ListUtil.isEmpty(list))
		{
			for (int i = 0; i < list.size(); i++) {
				if(StringUtil.isNotEmpty(list.get(i).getDatasourceId()))
				{
					String[] datasourceIds = list.get(i).getDatasourceId().split(",");
					List<String> ids = Arrays.asList(datasourceIds);
					QueryWrapper<ReportDatasource> queryWrapper = new QueryWrapper<>();
					queryWrapper.in("id", ids);
					List<ReportDatasource> datasources = this.iReportDatasourceService.list(queryWrapper);
					if(!ListUtil.isEmpty(datasources))
					{
						String dataSourceName = "";
						String dataSourceCode = "";
						for (int j = 0; j < datasources.size(); j++) {
							if(j == 0)
							{
								dataSourceName = dataSourceName + datasources.get(j).getName();
								dataSourceCode = dataSourceCode + datasources.get(j).getCode();
							}else {
								dataSourceName = dataSourceName + "," + datasources.get(j).getName();
								dataSourceCode = dataSourceCode + "," + datasources.get(j).getCode();
							}
						}
						list.get(i).setDataSourceName(dataSourceName);
						list.get(i).setDataSourceCode(dataSourceCode);
					}
				}
			}
		}
		result.setData(list);
		result.setTotal(page.getTotal());
		result.setCurrentPage(model.getCurrentPage());
		result.setPageSize(model.getPageSize());
		return result;
	}
	
	@Override
	public List<ReportTplTreeDto> getChildren(ReportTpl model) {
		List<ReportTplTreeDto> result = new ArrayList<>();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		List<ReportTplDto> list = this.baseMapper.getTableList(model);
		if(!ListUtil.isEmpty(list))
		{
			ReportTplTreeDto reportTplTreeDto = null;
			for (int i = 0; i < list.size(); i++) {
				reportTplTreeDto = new ReportTplTreeDto();
				if(StringUtil.isNotEmpty(list.get(i).getDatasourceId()))
				{
					String[] datasourceIds = list.get(i).getDatasourceId().split(",");
					List<String> ids = Arrays.asList(datasourceIds);
					QueryWrapper<ReportDatasource> queryWrapper = new QueryWrapper<>();
					queryWrapper.in("id", ids);
					List<ReportDatasource> datasources = this.iReportDatasourceService.list(queryWrapper);
					if(!ListUtil.isEmpty(datasources))
					{
						String dataSourceName = "";
						String dataSourceCode = "";
						for (int j = 0; j < datasources.size(); j++) {
							if(j == 0)
							{
								dataSourceName = dataSourceName + datasources.get(j).getName();
								dataSourceCode = dataSourceCode + datasources.get(j).getCode();
							}else {
								dataSourceName = dataSourceName + "," + datasources.get(j).getName();
								dataSourceCode = dataSourceCode + "," + datasources.get(j).getCode();
							}
						}
						list.get(i).setDataSourceName(dataSourceName);
						list.get(i).setDataSourceCode(dataSourceCode);
					}
				}
				BeanUtils.copyProperties(list.get(i), reportTplTreeDto);
				reportTplTreeDto.setIcon("iconfont icon-Excel");
				reportTplTreeDto.setType("2");
				reportTplTreeDto.setHasChildren(false);
				result.add(reportTplTreeDto);
			}
		}
		return result;
	}


	/**
	*<p>Title: getDetail</p>
	*<p>Description: 获取详情</p>
	* @author 
	* @param id
	* @return
	*/
	@Override
	public BaseEntity getDetail(Long id) {
		ReportTplDto result = new ReportTplDto();
		ReportTpl reportTpl = this.getById(id);
		BeanUtils.copyProperties(reportTpl, result);
		QueryWrapper<ReportTplDatasource> queryWrapper = new QueryWrapper<ReportTplDatasource>();
		queryWrapper.eq("tpl_id", reportTpl.getId());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplDatasource> list = this.iReportTplDatasourceService.list(queryWrapper);
		List<Long> dataSource = new ArrayList<Long>();
		if (!ListUtil.isEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				dataSource.add(list.get(i).getDatasourceId());
			}
		}
		result.setDataSource(dataSource);
//		QueryWrapper<SysRoleSheet> roleSheetQueryWrapper = new QueryWrapper<>();
//		roleSheetQueryWrapper.eq("report_id", id);
//		roleSheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//		List<SysRoleSheet> roleReports = iSysRoleSheetService.list(roleSheetQueryWrapper);
//		List<Long> roles = new ArrayList<Long>();
//		if(!ListUtil.isEmpty(roleReports))
//		{
//			for (int i = 0; i < roleReports.size(); i++) {
//				roles.add(roleReports.get(i).getRoleId());
//			}
//		}
//		result.setRoles(roles);
		return result;
	}

	/**
	*<p>Title: insert</p>
	*<p>Description: 新增数据</p>
	* @author 
	* @param model
	* @return
	*/
	@Transactional
	@Override
	public BaseEntity insert(ReportTplDto model) {
		BaseEntity result = new BaseEntity();
		//校验报表代码是否已经存在
		QueryWrapper<ReportTpl> queryWrapper = new QueryWrapper<ReportTpl>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("tpl_code", model.getTplCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		if(model.getIsTemplate() != null && model.getIsTemplate().intValue() == YesNoEnum.YES.getCode().intValue()) {
			queryWrapper.eq("is_template", YesNoEnum.YES.getCode());
		}else {
			queryWrapper.eq("is_template", YesNoEnum.NO.getCode());
		}
		ReportTpl isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该报表标识"}));
		}
		ReportTpl reportTpl = new ReportTpl();
		BeanUtils.copyProperties(model, reportTpl);
		if(StringUtil.isNotEmpty(reportTpl.getDesignPwd()))
		{
			reportTpl.setDesignPwd(Md5Util.generateMd5(reportTpl.getDesignPwd()));
		}
		reportTpl.setIsParamMerge(YesNoEnum.YES.getCode());
		//保存报表
		this.save(reportTpl);
		//保存报表关联的数据源
		List<ReportTplDatasource> datasources = new ArrayList<ReportTplDatasource>();
		ReportTplDatasource datasource = null;
		for (int i = 0; i < model.getDataSource().size(); i++) {
			datasource = new ReportTplDatasource();
			datasource.setTplId(reportTpl.getId());
			datasource.setDatasourceId(model.getDataSource().get(i));
			datasources.add(datasource);
		}
		this.iReportTplDatasourceService.saveBatch(datasources);
		//保存报表角色关联关系
//		if(model.getViewAuth().intValue() == 2)
//		{
//			if(!ListUtil.isEmpty(model.getRoles()))
//			{
//				List<SysRoleReport> roleReports = new ArrayList<>();
//				SysRoleReport roleReport = null;
//				for (int i = 0; i < model.getRoles().size(); i++) {
//					roleReport = new SysRoleReport();
//					roleReport.setReportId(reportTpl.getId());
//					roleReport.setRoleId(model.getRoles().get(i));
//					roleReports.add(roleReport);
//				}
//				this.iSysRoleReportService.saveBatch(roleReports);
//			}
//		}
		result.setStatusMsg(MessageUtil.getValue("info.insert"));
		return result;
	}

	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author 
	* @param model
	* @return
	*/
	@Transactional
	@Override
	public BaseEntity update(ReportTplDto model) {
		BaseEntity result = new BaseEntity();
		//校验报表代码是否已经存在
		QueryWrapper<ReportTpl> queryWrapper = new QueryWrapper<ReportTpl>();
		queryWrapper.ne("id", model.getId());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("tpl_code", model.getTplCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		if(model.getIsTemplate() != null && model.getIsTemplate().intValue() == YesNoEnum.YES.getCode().intValue()) {
			queryWrapper.eq("is_template", YesNoEnum.YES.getCode());
		}else {
			queryWrapper.eq("is_template", YesNoEnum.NO.getCode());
		}
		ReportTpl isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该报表标识"}));
		}
		ReportTpl reportTpl = new ReportTpl();
		BeanUtils.copyProperties(model, reportTpl);
		ReportTpl originalData = this.getById(model.getId());
		if(StringUtil.isNullOrEmpty(originalData.getDesignPwd()))
		{
			if(StringUtil.isNotEmpty(model.getDesignPwd()))
			{
				reportTpl.setDesignPwd(Md5Util.generateMd5(model.getDesignPwd()));
			}
		}
		//更新报表
		this.updateById(reportTpl);
		//更新报表关联的数据源
		UpdateWrapper<ReportTplDatasource> updateWrapper = new UpdateWrapper<ReportTplDatasource>();
		updateWrapper.eq("tpl_id", model.getId());
		updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportTplDatasource entity = new ReportTplDatasource();
		entity.setDelFlag(DelFlagEnum.DEL.getCode());
		//先删除之前的数据源
		this.iReportTplDatasourceService.update(entity, updateWrapper);
		//再新增数据源
		List<ReportTplDatasource> datasources = new ArrayList<ReportTplDatasource>();
		ReportTplDatasource datasource = null;
		for (int i = 0; i < model.getDataSource().size(); i++) {
			datasource = new ReportTplDatasource();
			datasource.setTplId(reportTpl.getId());
			datasource.setDatasourceId(model.getDataSource().get(i));
			datasources.add(datasource);
		}
		this.iReportTplDatasourceService.saveBatch(datasources);
		//删除报表角色关联关系
//		UpdateWrapper<SysRoleReport> roleReportUpdateWrapper = new UpdateWrapper<>();
//		roleReportUpdateWrapper.eq("report_id", model.getId());
//		roleReportUpdateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//		SysRoleReport updateRoleReport = new SysRoleReport();
//		updateRoleReport.setDelFlag(DelFlagEnum.DEL.getCode());
//		this.iSysRoleReportService.update(updateRoleReport, roleReportUpdateWrapper);
//		//保存报表角色关联关系
//		if(model.getViewAuth().intValue() == 2)
//		{
//			if(!ListUtil.isEmpty(model.getRoles()))
//			{
//				List<SysRoleReport> roleReports = new ArrayList<>();
//				SysRoleReport roleReport = null;
//				for (int i = 0; i < model.getRoles().size(); i++) {
//					roleReport = new SysRoleReport();
//					roleReport.setReportId(reportTpl.getId());
//					roleReport.setRoleId(model.getRoles().get(i));
//					roleReports.add(roleReport);
//				}
//				this.iSysRoleReportService.saveBatch(roleReports);
//			}
//		}
		result.setStatusMsg(MessageUtil.getValue("info.update"));
		return result;
	}

	/**
	*<p>Title: delete</p>
	*<p>Description: 单条删除数据</p>
	* @author 
	* @param model
	* @return
	*/
	@Transactional
	@Override
	public BaseEntity delete(Long id) {
		ReportTpl reportTpl = new ReportTpl();
		reportTpl.setId(id);
		reportTpl.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(reportTpl);
		BaseEntity result = new BaseEntity();
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}

	/**
	*<p>Title: deleteBatch</p>
	*<p>Description: 批量删除数据</p>
	* @author 
	* @param list
	* @return
	*/
	@Transactional
	@Override
	public BaseEntity deleteBatch(List<Long> ids) {
		List<ReportTpl> list = new ArrayList<ReportTpl>();
		for (int i = 0; i < ids.size(); i++) {
			ReportTpl reportTpl = new ReportTpl();
			reportTpl.setId(ids.get(i));
			reportTpl.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(reportTpl);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @Title: getTplDatasetsColumnNames
	 * @Description: 获取模板数据集所有的列名，列名格式是dataSetName.${columnName}，后期可以优化，放到redis中
	 * @param tplId
	 * @return
	 * @author caiyang
	 * @throws SQLException 
	 * @date 2021-05-25 07:01:49 
	 */ 
	@Override
	public List<List<String>> getTplDatasetsColumnNames(Long tplId,Map<String, String> datasetNameIdMap,UserInfoDto userInfoDto) throws SQLException{
		if(datasetNameIdMap == null) {
			datasetNameIdMap = new HashMap<>();
		} 
		List<List<String>> result = new ArrayList<List<String>>();
		QueryWrapper<ReportTplDataset> queryWrapper = new QueryWrapper<ReportTplDataset>();
		queryWrapper.eq("tpl_id", tplId);
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		queryWrapper.eq("is_common", YesNoEnum.NO.getCode());
		List<ReportTplDataset> datasets = this.iReportTplDatasetService.list(queryWrapper);
		if(ListUtil.isEmpty(datasets)) {
			datasets = new ArrayList<ReportTplDataset>();
		}
		queryWrapper = new QueryWrapper<ReportTplDataset>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", userInfoDto.getMerchantNo());
		}
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		queryWrapper.eq("is_common", YesNoEnum.YES.getCode());
		List<ReportTplDataset> commonDatasets = this.iReportTplDatasetService.list(queryWrapper);
		if(ListUtil.isNotEmpty(commonDatasets)) {
			datasets.addAll(commonDatasets);
		}
		if(datasets != null && datasets.size() > 0)
		{
			for (int i = 0; i < datasets.size(); i++) {
				Long id = datasets.get(i).getId();
				datasetNameIdMap.put(datasets.get(i).getDatasetName(), id.toString());
				List<String> columnsList = new ArrayList<>();
				List<Map<String, Object>> columns = (List<Map<String, Object>>) redisUtil.get(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(datasets.get(i).getId()));
				if(ListUtil.isEmpty(columns))
				{
					ReportDatasource reportDatasource = this.iReportDatasourceService.getById(datasets.get(i).getDatasourceId());
					if (reportDatasource == null) {
						throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"数据库信息"}));
					}
					if(DatasetTypeEnum.SQL.getCode().intValue() == datasets.get(i).getDatasetType().intValue())
					{
						//解析sql
						List<Map<String, Object>> dataSetColumns = null;
						if(reportDatasource.getType().intValue() == 6)
						{
							InfluxDbDataSourceConfig dataSourceConfig = new InfluxDbDataSourceConfig(datasets.get(i).getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl().substring(0,reportDatasource.getJdbcUrl().lastIndexOf("/")), reportDatasource.getJdbcUrl().substring(reportDatasource.getJdbcUrl().lastIndexOf("/")+1));
							InfluxDBConnection dbConnection = JdbcUtils.getInfluxdbDatasource(dataSourceConfig);
							if(dbConnection == null) {
								throw new BizException(StatusCode.FAILURE, "influxdb连接失败!");
							}
							dataSetColumns = JdbcUtils.parseInfluxdbColumns(dbConnection, datasets.get(i).getTplSql(), reportDatasource.getType(),datasets.get(i).getTplParam(),userInfoDto);
						}else {
							DataSource dataSource = null;
							if(reportDatasource.getType().intValue() == 9)
							{
								EsDataSourceConfig esDataSourceConfig = new EsDataSourceConfig(datasets.get(i).getDatasourceId(), reportDatasource.getUserName(), reportDatasource.getPassword(), reportDatasource.getJdbcUrl());
								dataSource = JdbcUtils.getEsDataSource(esDataSourceConfig);
							}else {
								//数据源配置
								DataSourceConfig dataSourceConfig = new DataSourceConfig(datasets.get(i).getDatasourceId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
								//获取数据源
								dataSource = JdbcUtils.getDataSource(dataSourceConfig);
							}
							if(SqlTypeEnum.SQL.getCode().intValue() == datasets.get(i).getSqlType().intValue())
							{
								if(reportDatasource.getType().intValue() == 9)
								{
									dataSetColumns = JdbcUtils.parseMetaDataColumns(dataSource, datasets.get(i).getTplSql(),reportDatasource.getType(),datasets.get(i).getTplParam(),reportDatasource.getUserName(),reportDatasource.getPassword(),userInfoDto);
								}else {
									dataSetColumns = JdbcUtils.parseMetaDataColumns(dataSource, datasets.get(i).getTplSql(),reportDatasource.getType(),datasets.get(i).getTplParam(),userInfoDto);
								}
							}else {
								dataSetColumns = JdbcUtils.parseProcedureColumns(dataSource, datasets.get(i).getTplSql(), reportDatasource.getType(), JSONArray.parseArray(datasets.get(i).getInParam()), JSONArray.parseArray(datasets.get(i).getOutParam()),userInfoDto);
							}
						}
						
						redisUtil.set(RedisPrefixEnum.DATASETCOLUMN.getCode()+String.valueOf(datasets.get(i).getId()), dataSetColumns);
						if(dataSetColumns != null && dataSetColumns.size() > 0)
						{
							String dataSetName = datasets.get(i).getDatasetName();
							for (int j = 0; j < dataSetColumns.size(); j++) {
								columnsList.add(id+"-"+dataSetName + ".${" + dataSetColumns.get(j).get("name")+"}");
							}
						}
					}else {
						JSONArray jsonArray = JSONArray.parseArray(reportDatasource.getApiColumns());
						if(!ListUtil.isEmpty(jsonArray))
						{
							String dataSetName = datasets.get(i).getDatasetName();
							for (int j = 0; j < jsonArray.size(); j++) {
								columnsList.add(id+"-"+dataSetName + ".${" + jsonArray.getJSONObject(j).get("propCode")+"}");
							}
						}
					}
				}else {
					String dataSetName = datasets.get(i).getDatasetName();
					for (int j = 0; j < columns.size(); j++) {
						columnsList.add(id+"-"+dataSetName + ".${" + columns.get(j).get("name")+"}");
					}
				}
				result.add(columnsList);
			}
		}
		return result;
	}


	
	
	/**  
	 * @Title: getDatasetParamInfo
	 * @Description: 获取数据集的参数信息
	 * @param usedDatasetName
	 * @param mesGenerateReportDto
	 * @return
	 * @author caiyang
	 * @date 2021-06-18 04:24:20 
	 */ 
	private Map<String, Object> getDatasetParamInfo(String usedDatasetName,MesGenerateReportDto mesGenerateReportDto){
		Map<String, Object> result = null;
		if(ListUtil.isEmpty(mesGenerateReportDto.getSearchData()))
		{
			return result;
		}else {
			for (int i = 0; i < mesGenerateReportDto.getSearchData().size(); i++) {
				if(usedDatasetName.equals(mesGenerateReportDto.getSearchData().get(i).get("datasetName")))
				{
					result = mesGenerateReportDto.getSearchData().get(i);
					break;
				}
			}
		}
		return result;
	}
	
	/**  
	 * @Title: getDatasetParamInfo
	 * @Description: 获取全部数据集的参数信息
	 * @param mesGenerateReportDto
	 * @return
	 * @author caiyang
	 * @date 2025年11月4日10:41:20
	 */ 
	private Map<String, Object> getDatasetParamInfoMap(MesGenerateReportDto mesGenerateReportDto){
		Map<String, Object> result = null;
		if(ListUtil.isEmpty(mesGenerateReportDto.getSearchData()))
		{
			return result;
		}else {
			result = new HashMap<String, Object>();
			for (int i = 0; i < mesGenerateReportDto.getSearchData().size(); i++) {
				result.putAll(mesGenerateReportDto.getSearchData().get(i));
			}
		}
		return result;
	}
	
	/**  
	 * @Title: getDatasetParamInfo
	 * @Description: 获取全部数据集的参数信息
	 * @param mesGenerateReportDto
	 * @return
	 * @author caiyang
	 * @date 2025年11月4日10:41:20
	 */ 
	private List<Map<String, Object>> getDatasetParamInfo(MesGenerateReportDto mesGenerateReportDto){
		List<Map<String, Object>> result = null;
		if(!ListUtil.isEmpty(mesGenerateReportDto.getSearchData()))
		{
			result = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < mesGenerateReportDto.getSearchData().size(); i++) {
				result.add(mesGenerateReportDto.getSearchData().get(i));
			}
		}
		return result;
	}
	
	private Map<String, Integer> getMaxRowAndCol(Map<String, Integer> maxCoordinate,int coordsx,int coordsy,int rowSpan,int colSpan)
	{
		Map<String, Integer> rowAndCol = new HashMap<String, Integer>();
		//默认自己最大
		int maxX = coordsx;
		int maxY = coordsy;
		for (int i = 0; i < rowSpan; i++) {
			for (int j = 0; j < colSpan; j++) {
				Integer x = maxCoordinate.get("y-"+(coordsy+j)) == null?0:maxCoordinate.get("y-"+(coordsy+j));
				Integer y = maxCoordinate.get("x-"+(coordsx+i)) == null?0:maxCoordinate.get("x-"+(coordsx+i));
				if(maxX >= x)
				{
					maxCoordinate.put("y-"+(coordsy+j), maxX);
				}else {
					maxX = x;
				}
				if(maxY >= y)
				{
					maxCoordinate.put("x-"+(coordsx+i), maxY);
				}else {
					maxY = y;
				}
			}
		}
		rowAndCol.put("maxX", maxX);
		rowAndCol.put("maxY", maxY);
		return rowAndCol;
	}
	
	/**  
	 * @MethodName: calculateOriginCellsExtend
	 * @Description: 计算在原始单元格的基础上扩展是否可以
	 * @author caiyang
	 * @param bindData
	 * @param usedCells 
	 * @return void
	 * @date 2023-03-08 08:19:10 
	 */  
	private Boolean calculateOriginCellsExtend(LuckySheetBindData bindData,Map<String, Map<String, Object>> usedCells) {
		Boolean result = true;
		if(bindData == null || usedCells == null)
		{
			result = false;
			return result;
		}
		int x = 0;
		int y = 0;
		List<String> cells = new ArrayList<String>(); 
		if(CellValueTypeEnum.FIXED.getCode().intValue() == bindData.getCellValueType())
		{
			cells.add(bindData.getCoordsx()+"_"+bindData.getCoordsy());
		}else if(CellValueTypeEnum.BLOCK.getCode().intValue() == bindData.getCellValueType())
		{
			//do nothing
		}else {
			if(AggregateTypeEnum.SUMMARY.getCode().equals(bindData.getAggregateType()))
			{
				for (int k = 0; k < bindData.getRowSpan(); k++) {
					for (int i = 0; i < bindData.getColSpan(); i++) {
						cells.add((bindData.getCoordsx()+k)+"_"+(bindData.getCoordsy()+i));
					}
				}
			}else {
				if(CellExtendEnum.NOEXTEND.getCode().intValue() == bindData.getCellExtend().intValue())
				{
					for (int k = 0; k < bindData.getRowSpan(); k++) {
						for (int i = 0; i < bindData.getColSpan(); i++) {
							cells.add((bindData.getCoordsx()+k)+"_"+(bindData.getCoordsy()+i));
						}
					}
				}else if(CellExtendEnum.VERTICAL.getCode().intValue() == bindData.getCellExtend().intValue())
				{
					List<List<Map<String, Object>>> datas = null;
					if(YesNoEnum.YES.getCode().intValue() == bindData.getIsConditions().intValue())
					{
						datas = bindData.getFilterDatas();
					}else {
						datas = bindData.getDatas();
					}
					for (int i = 0; i < datas.size(); i++) {
						int rowSpan = bindData.getRowSpan();
						int colSpan = bindData.getColSpan();
						if(!bindData.getIsGroupMerge())
						{
							rowSpan = rowSpan * datas.get(i).size();
						}
						for (int k = 0; k < rowSpan; k++) {
							for (int t = 0; t < colSpan; t++) {
								cells.add((bindData.getCoordsx()+k+x)+"_"+(bindData.getCoordsy()+t));
							}
						}
						x = x + rowSpan;
					}
				}else if(CellExtendEnum.HORIZONTAL.getCode().intValue() == bindData.getCellExtend().intValue())
				{
					List<List<Map<String, Object>>> datas = null;
					if(YesNoEnum.YES.getCode().intValue() == bindData.getIsConditions().intValue())
					{
						datas = bindData.getFilterDatas();
					}else {
						datas = bindData.getDatas();
					}
					for (int i = 0; i < datas.size(); i++) {
						int rowSpan = bindData.getRowSpan();
						int colSpan = bindData.getColSpan();
						if(!bindData.getIsGroupMerge())
						{
							colSpan = colSpan * datas.get(i).size();
						}
						for (int k = 0; k < rowSpan; k++) {
							for (int t = 0; t < colSpan; t++) {
								cells.add((bindData.getCoordsx()+k)+"_"+(bindData.getCoordsy()+t+y));
							}
						}
						y = y + colSpan;
					}
				}
			}
		}
		if(ListUtil.isEmpty(cells))
		{
			result = false;
		}else {
			for (int i = 0; i < cells.size(); i++) {
				if(usedCells.containsKey(cells.get(i)))
				{
					result = false;
					break;
				}
			}
		}
		return result;
	}
	
	private Integer getMaxRow(Map<String, Integer> maxCoordinate,int coordsx,int coordsy,int rowSpan) {
		int maxX = coordsx;
		for (int i = 0; i < rowSpan; i++) {
			Integer x = maxCoordinate.get("y-"+(coordsy)) == null?0:maxCoordinate.get("y-"+(coordsy));
			if(maxX >= x)
			{
				maxCoordinate.put("y-"+(coordsy), maxX);
			}else {
				maxX = x;
			}
		}
		return maxX;
	}
	
	private Integer getMaxCol(Map<String, Integer> maxCoordinate,int coordsx,int coordsy,int colSpan) {
		int maxY = coordsy;
		for (int i = 0; i < colSpan; i++) {
			Integer y = maxCoordinate.get("x-"+(coordsx)) == null?0:maxCoordinate.get("x-"+(coordsx));
			if(maxY >= y)
			{
				maxCoordinate.put("x-"+(coordsx), maxY);
			}else {
				maxY = y;
			}
		}
		return maxY;
	}
	
	
	/**  
	 * @MethodName: getViewParams
	 * @Description: 获取页面所有参数
	 * @author caiyang
	 * @param datasetParams
	 * @return
	 * @throws ParseException Map<String,Object>
	 * @date 2023-08-10 10:54:40 
	 */ 
	private Map<String, Object> getViewParams(List<Map<String, Object>> datasetParams,UserInfoDto userInfoDto) throws ParseException
	{
		Map<String, Object> result = new HashMap<String, Object>();
		if(!ListUtil.isEmpty(datasetParams))
		{
			for (int i = 0; i < datasetParams.size(); i++) {
				Map<String, Object> params = ParamUtil.getViewParams((JSONArray)datasetParams.get(i).get("params"),userInfoDto);
				result.putAll(params);
			}
		}
		return result;
	}
	
	/**  
	 * @Title: getReports
	 * @Description: 获取所有的报表
	 * @return 
	 * @see com.caiyang.api.reporttpl.IReportTplService#getReports() 
	 * @author caiyang
	 * @date 2021-08-30 07:28:22 
	 */
	@Override
	public List<ReportTpl> getReports() {
		QueryWrapper<ReportTpl> queryWrapper = new QueryWrapper<ReportTpl>();
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		queryWrapper.orderByAsc("id");
		List<ReportTpl> result = this.list(queryWrapper);
		return result;
	}


	/**  
	 * @Title: saveLuckySheetTpl
	 * @Description: 保存luckysheet报表模板
	 * @param mesLuckySheetTplDto
	 * @return 
	 * @see com.caiyang.api.reporttpl.IReportTplService#saveLuckySheetTpl(com.caiyang.dto.reporttpl.MesLuckySheetTplDto) 
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws SQLException 
	 * @date 2022-02-01 10:53:29 
	 */
	@Override
	@Transactional
	public BaseEntity saveLuckySheetTpl(MesLuckysheetsTplDto mesLuckySheetsTplDto,UserInfoDto userInfoDto) throws JsonProcessingException, SQLException {
		ObjectMapper objectMapper = new ObjectMapper();
		SaveLuckySheetTplDto result = new SaveLuckySheetTplDto();
		ReportTpl tpl = this.getById(mesLuckySheetsTplDto.getTplId());
		if(tpl == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"报表模板"}));
		}
		if(tpl.getIsExample().intValue() == YesNoEnum.YES.getCode().intValue() && userInfoDto.getIsAdmin().intValue() != YesNoEnum.YES.getCode().intValue()) {
			//示例模板只允许超级管理员去修改保存
			throw new BizException(StatusCode.FAILURE, "该模板是示例模板，不允许修改，请见谅！");
		}
		ReportTpl reportTpl = new ReportTpl();
		reportTpl.setId(mesLuckySheetsTplDto.getTplId());
		reportTpl.setIsParamMerge(mesLuckySheetsTplDto.getIsParamMerge());
		this.updateById(reportTpl);
		reportTpl = this.getById(reportTpl.getId());
		//删除sheets
		if(!ListUtil.isEmpty(mesLuckySheetsTplDto.getDelSheetsIndex()))
		{
			UpdateWrapper<ReportTplSheet> sheetUpdateWrapper = new UpdateWrapper<>();
			sheetUpdateWrapper.eq("tpl_id", mesLuckySheetsTplDto.getTplId());
			sheetUpdateWrapper.in("sheet_index", mesLuckySheetsTplDto.getDelSheetsIndex());
			sheetUpdateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			ReportTplSheet updateSheet = new ReportTplSheet();
			updateSheet.setDelFlag(DelFlagEnum.DEL.getCode());
			this.iReportTplSheetService.update(updateSheet, sheetUpdateWrapper);
			for (int i = 0; i < mesLuckySheetsTplDto.getDelSheetsIndex().size(); i++) {
				redisUtil.del(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+"designMode-"+mesLuckySheetsTplDto.getTplId()+"-"+mesLuckySheetsTplDto.getDelSheetsIndex().get(i));
			}
		}
		//获取模板数据集对应的列,用于判断单元格的类型，若columnNames中存在该值，则说明是动态数据，否则是固定值
		Map<String, String> datasetNameIdMap = new HashMap<String, String>();
		List<List<String>> columnNames = this.getTplDatasetsColumnNames(mesLuckySheetsTplDto.getTplId(),datasetNameIdMap,userInfoDto);
		Map<String, Long> printSettings = new HashMap<>();
		for (int i = 0; i < mesLuckySheetsTplDto.getConfigs().size(); i++) {
			MesLuckySheetTplDto mesLuckySheetTplDto = mesLuckySheetsTplDto.getConfigs().get(i);
			Map<String, String> chartCellMap = LuckysheetUtil.getChartRangeCells(mesLuckySheetTplDto.getChart());
			//根据index查询sheet是否已经存在
			QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
			sheetQueryWrapper.eq("tpl_id", mesLuckySheetsTplDto.getTplId());
			sheetQueryWrapper.eq("sheet_index", mesLuckySheetTplDto.getSheetIndex());
//			sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			ReportTplSheet isExistSheet = this.iReportTplSheetService.getOne(sheetQueryWrapper,false);
			ReportTplSheet reportTplSheet = new ReportTplSheet();
			if(isExistSheet != null && DelFlagEnum.DEL.getCode().intValue() == isExistSheet.getDelFlag()) {
				continue;
			}
			if(isExistSheet != null)
			{
				reportTplSheet.setId(isExistSheet.getId());
			}
			reportTplSheet.setTplId(mesLuckySheetsTplDto.getTplId());
			reportTplSheet.setSheetIndex(mesLuckySheetTplDto.getSheetIndex());
			reportTplSheet.setSheetName(mesLuckySheetTplDto.getSheetName());
			reportTplSheet.setSheetOrder(mesLuckySheetTplDto.getSheetOrder());
			if(mesLuckySheetTplDto.getConfig() != null)
			{
				reportTplSheet.setConfig(objectMapper.writeValueAsString(mesLuckySheetTplDto.getConfig()));
			}
			if(mesLuckySheetTplDto.getFrozen() != null)
			{
				reportTplSheet.setFrozen(objectMapper.writeValueAsString(mesLuckySheetTplDto.getFrozen()));
			}
			if(mesLuckySheetTplDto.getImages() != null)
			{
				reportTplSheet.setImages(objectMapper.writeValueAsString(mesLuckySheetTplDto.getImages()));
			}
			if(mesLuckySheetTplDto.getCalcChain() != null)
			{
				reportTplSheet.setCalcChain(objectMapper.writeValueAsString(mesLuckySheetTplDto.getCalcChain()));
			}
			if(mesLuckySheetTplDto.getLuckysheetAlternateformatSave() == null)
			{
				mesLuckySheetTplDto.setLuckysheetAlternateformatSave(new JSONArray());
			}
			if(mesLuckySheetTplDto.getChart() == null)
			{
				mesLuckySheetTplDto.setChart(new JSONArray());
			}
			if(mesLuckySheetTplDto.getDataVerification() == null)
			{
				mesLuckySheetTplDto.setDataVerification(new JSONObject());
			}
			if(mesLuckySheetTplDto.getLuckysheetConditionformatSave() == null)
			{
				mesLuckySheetTplDto.setLuckysheetConditionformatSave(new JSONArray());
			}
			reportTplSheet.setAlternateformatSave(objectMapper.writeValueAsString(mesLuckySheetTplDto.getLuckysheetAlternateformatSave()));
			reportTplSheet.setConditionformatSave(objectMapper.writeValueAsString(mesLuckySheetTplDto.getLuckysheetConditionformatSave()));
			reportTplSheet.setChart(objectMapper.writeValueAsString(mesLuckySheetTplDto.getChart()));
			reportTplSheet.setDataVerification(objectMapper.writeValueAsString(mesLuckySheetTplDto.getDataVerification()));
			reportTplSheet.setXxbtScreenshot(JSON.toJSONString(mesLuckySheetTplDto.getXxbtScreenShot()));
			if(ListUtil.isNotEmpty(mesLuckySheetTplDto.getPageDivider()))
			{
				reportTplSheet.setPageDivider(JSON.toJSONString(mesLuckySheetTplDto.getPageDivider()));
			}else {
				reportTplSheet.setPageDivider(JSON.toJSONString(new JSONArray()));
			}
			
			this.iReportTplSheetService.saveOrUpdate(reportTplSheet);
			//更新图表相关的配置信息，先删除，再新增
			QueryWrapper<ReportTplSheetChart> updateWrapper = new QueryWrapper<ReportTplSheetChart>();
			updateWrapper.eq("tpl_id", mesLuckySheetsTplDto.getTplId());
			updateWrapper.eq("sheet_id", reportTplSheet.getId());
//			updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//			ReportTplSheetChart update = new ReportTplSheetChart();
//			update.setDelFlag(DelFlagEnum.DEL.getCode());
//			iReportTplSheetChartService.update(update, updateWrapper);
			iReportTplSheetChartService.remove(updateWrapper);
			if(!ListUtil.isEmpty(mesLuckySheetTplDto.getChartXaxisData()))
			{//增加图表相关的配置信息
				List<ReportTplSheetChart> reportTplSheetCharts = new ArrayList<>();
				ReportTplSheetChart insert = null;
				for (int j = 0; j < mesLuckySheetTplDto.getChartXaxisData().size(); j++) {
					JSONObject jsonObject = mesLuckySheetTplDto.getChartXaxisData().getJSONObject(j);
					insert = new ReportTplSheetChart();
					insert.setTplId(mesLuckySheetsTplDto.getTplId());
					insert.setSheetId(reportTplSheet.getId());
					insert.setChartId(jsonObject.getString("chartId"));
					insert.setTitle(jsonObject.getString("title"));
					insert.setDataType(jsonObject.getInteger("dataType"));
					insert.setDatasetId(jsonObject.getLong("datasetId"));
					insert.setDatasetName(jsonObject.getString("datasetName"));
					insert.setAttr(jsonObject.getString("attr"));
					insert.setXaxisDatas(jsonObject.getString("xAxisDatas"));
					reportTplSheetCharts.add(insert);
				}
				this.iReportTplSheetChartService.saveBatch(reportTplSheetCharts);
			}
			//删除所有图表相关的单元格
			QueryWrapper<LuckysheetReportCell> chartCellUpdateWrapper = new QueryWrapper<>();
			chartCellUpdateWrapper.eq("tpl_id", mesLuckySheetsTplDto.getTplId());
			chartCellUpdateWrapper.eq("sheet_id", reportTplSheet.getId());
			chartCellUpdateWrapper.eq("is_chart_cell", YesNoEnum.YES.getCode());
//			chartCellUpdateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//			LuckysheetReportCell updateCell = new LuckysheetReportCell();
//			updateCell.setDelFlag(DelFlagEnum.DEL.getCode());
//			this.iLuckysheetReportCellService.update(updateCell, chartCellUpdateWrapper);
			this.iLuckysheetReportCellService.remove(chartCellUpdateWrapper);
			QueryWrapper<LuckysheetReportCell> queryWrapper = new QueryWrapper<LuckysheetReportCell>();
			queryWrapper.eq("tpl_id", mesLuckySheetsTplDto.getTplId());
			queryWrapper.eq("sheet_id", reportTplSheet.getId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<LuckysheetReportCell> tplCells = this.iLuckysheetReportCellService.list(queryWrapper);
			List<LuckysheetReportCell> addCells = new ArrayList<LuckysheetReportCell>();//新增的单元格
			List<LuckysheetReportCell> deleteCells = new ArrayList<LuckysheetReportCell>();//需要删除的单元格
			List<LuckysheetReportCell> updateCells = new ArrayList<LuckysheetReportCell>();//需要更新的单元格
			List<LuckysheetReportCell> sheetCells = new ArrayList<>();//sheet页中所有需要新增或者更新的单元格数据
			Map<String, JSONObject> extraCustomCellConfigs = mesLuckySheetTplDto.getExtraCustomCellConfigs()!=null?mesLuckySheetTplDto.getExtraCustomCellConfigs():new HashMap<String, JSONObject>();
			Map<String, JSONObject> hyperlinks = mesLuckySheetTplDto.getHyperlinks()!=null?mesLuckySheetTplDto.getHyperlinks():new HashMap<String, JSONObject>();
			Map<String, String> cellRelyRelations = new HashMap<>();
			if(ListUtil.isEmpty(tplCells))
			{//如果是空，则全部新增
				if(!ListUtil.isEmpty(mesLuckySheetTplDto.getCellDatas()))
				{
					for (Map<String, Object> object : mesLuckySheetTplDto.getCellDatas()) {
						LuckysheetReportCell luckysheetReportCell = this.getReportCell(object, columnNames, extraCustomCellConfigs, hyperlinks,mesLuckySheetTplDto.getLuckysheetAlternateformatSave(),chartCellMap
								,mesLuckySheetTplDto.getDataVerification());
						luckysheetReportCell.setTplId(mesLuckySheetsTplDto.getTplId());
						luckysheetReportCell.setSheetId(reportTplSheet.getId());
						addCells.add(luckysheetReportCell);
						this.getCellRelyRelations(luckysheetReportCell, cellRelyRelations);
					}
				}
			}else {
				//如果非空，则找出需要更新，删除和新增的单元格
				Map<String, LuckysheetReportCell> tplCellsMap = new HashMap<>();
				for (int j = 0; j < tplCells.size(); j++) {
					String key = tplCells.get(j).getCoordsx().toString() +LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+ tplCells.get(j).getCoordsy().toString();
					if(!tplCellsMap.containsKey(key))
					{
						tplCellsMap.put(key, tplCells.get(j));
					}else {
						tplCells.get(j).setDelFlag(DelFlagEnum.DEL.getCode());
						deleteCells.add(tplCells.get(j));
					}
				}
				Map<String, List<Map<String, Object>>> cellDatasMap = mesLuckySheetTplDto.getCellDatas().stream().collect(Collectors.groupingBy(this::customKey));
				//找出需要删除的单元格
				if(ListUtil.isEmpty(mesLuckySheetTplDto.getCellDatas()))
				{
					for (LuckysheetReportCell luckysheetReportCell : tplCells) {
						luckysheetReportCell.setDelFlag(DelFlagEnum.DEL.getCode());
						deleteCells.add(luckysheetReportCell);
					}
				}else {
					for (LuckysheetReportCell luckysheetReportCell : tplCells) {
						boolean isExist = cellDatasMap.containsKey(String.valueOf(luckysheetReportCell.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(luckysheetReportCell.getCoordsy())));
						if(!isExist)
						{
							luckysheetReportCell.setDelFlag(DelFlagEnum.DEL.getCode());
							deleteCells.add(luckysheetReportCell);
						}
					}
				}
				//找出需要新增和更新的单元格
				if(!ListUtil.isEmpty(mesLuckySheetTplDto.getCellDatas()))
				{
					for (Map<String, Object> object : mesLuckySheetTplDto.getCellDatas()) {
						String key = String.valueOf(object.get(LuckySheetPropsEnum.R.getCode())) + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() + String.valueOf(object.get(LuckySheetPropsEnum.C.getCode()));
						boolean isExist = tplCellsMap.containsKey(key);
						LuckysheetReportCell luckysheetReportCell = this.getReportCell(object, columnNames, extraCustomCellConfigs, hyperlinks,mesLuckySheetTplDto.getLuckysheetAlternateformatSave(),chartCellMap
								,mesLuckySheetTplDto.getDataVerification());
						luckysheetReportCell.setTplId(mesLuckySheetsTplDto.getTplId());
						luckysheetReportCell.setSheetId(reportTplSheet.getId());
						if(isExist)
						{
							luckysheetReportCell.setId(tplCellsMap.get(key).getId());
							updateCells.add(luckysheetReportCell);
							this.getCellRelyRelations(luckysheetReportCell, cellRelyRelations);
						}else {
							addCells.add(luckysheetReportCell);
							this.getCellRelyRelations(luckysheetReportCell, cellRelyRelations);
						}
					}
				}
			}
			List<LuckysheetReportCell> reportTplBlockCells = new ArrayList<LuckysheetReportCell>();
			List<LuckysheetReportBlockCell>  blockCells = new ArrayList<LuckysheetReportBlockCell>();
			if(!ListUtil.isEmpty(mesLuckySheetTplDto.getBlockCellDatas()))
			{//循环块处理
				for (int t = 0; t < mesLuckySheetTplDto.getBlockCellDatas().size(); t++) {
					LuckysheetReportCell luckysheetReportCell = this.getBlockReportCell(mesLuckySheetTplDto.getBlockCellDatas().get(t));
					luckysheetReportCell.setTplId(mesLuckySheetsTplDto.getTplId());
					luckysheetReportCell.setSheetId(reportTplSheet.getId());
					List<String> blockDatasets = new ArrayList<String>();//记录循环块的数据集，一个循环块只允许有一个数据集
					if(mesLuckySheetTplDto.getBlockCells() != null)
					{
						List<Map<String, Object>> blockCellMaps = mesLuckySheetTplDto.getBlockCells().get(luckysheetReportCell.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportCell.getCoordsy());
						if(!ListUtil.isEmpty(blockCellMaps))
						{
							for (int j = 0; j < blockCellMaps.size(); j++) {
								LuckysheetReportBlockCell luckysheetReportBlockCell = this.getBlockCell(blockCellMaps.get(j), mesLuckySheetsTplDto.getTplId(), luckysheetReportCell.getId(), columnNames, hyperlinks,mesLuckySheetTplDto.getDataVerification());
								if(StringUtil.isNotEmpty(luckysheetReportBlockCell.getDatasetName()))
								{
									if(!blockDatasets.contains(luckysheetReportBlockCell.getDatasetName()))
									{
										blockDatasets.add(luckysheetReportBlockCell.getDatasetName());
									}
								}
								
								blockCells.add(luckysheetReportBlockCell);
//								if(mesLuckySheetTplDto.getSubBlockCells() != null) {
//									String key = luckysheetReportBlockCell.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCell.getCoordsy();
//									if(mesLuckySheetTplDto.getSubBlockCells().containsKey(key)) {
//										List<Map<String, Object>> subBlockCellMaps = mesLuckySheetTplDto.getSubBlockCells().get(key);
//										for (int k = 0; k < subBlockCellMaps.size(); k++) {
//											LuckysheetReportBlockCell subLuckysheetReportBlockCell = this.getBlockCell(subBlockCellMaps.get(k), mesLuckySheetsTplDto.getTplId(), luckysheetReportCell.getId(), columnNames, hyperlinks,mesLuckySheetTplDto.getDataVerification(),YesNoEnum.YES.getCode().intValue());
//											if(StringUtil.isNotEmpty(subLuckysheetReportBlockCell.getDatasetName()))
//											{
//												if(!blockDatasets.contains(subLuckysheetReportBlockCell.getDatasetName()))
//												{
//													blockDatasets.add(subLuckysheetReportBlockCell.getDatasetName());
//												}
//											}
//											blockCells.add(subLuckysheetReportBlockCell);
//										}
//									}
//								}
							}
						}
					}
					
//					if(blockDatasets.size()>1)
//					{
//						throw new BizException(StatusCode.FAILURE, "循环块不支持多个数据集，请修改后重新保存！");
//					} 
					if(!ListUtil.isEmpty(blockDatasets))
					{
						luckysheetReportCell.setDatasetName(String.join(",", blockDatasets));
					}
					reportTplBlockCells.add(luckysheetReportCell);
				}
			}
			sheetCells.addAll(addCells);
			sheetCells.addAll(updateCells);
			Map<String, List<LuckysheetReportCell>> formsCellsMap = sheetCells.stream().collect(Collectors.groupingBy(this::customKey));
			//删掉绑定的填报数据源信息
			UpdateWrapper<ReportFormsDatasource> formsDatasourceUpdateWrapper = new UpdateWrapper<>();
			formsDatasourceUpdateWrapper.eq("tpl_id", mesLuckySheetsTplDto.getTplId());
			formsDatasourceUpdateWrapper.eq("sheet_id", reportTplSheet.getId());
			formsDatasourceUpdateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			ReportFormsDatasource updateFormsDatasource = new ReportFormsDatasource();
			updateFormsDatasource.setDelFlag(DelFlagEnum.DEL.getCode());
			this.iReportFormsDatasourceService.update(updateFormsDatasource, formsDatasourceUpdateWrapper);
			//新增填报绑定数据源和数据源属性
			JSONArray datasourceConfig = mesLuckySheetTplDto.getDatasourceConfig();
			List<ReportFormsDatasource> formsDatasources = new ArrayList<>();
			List<ReportFormsDatasourceAttrs> formsDatasourceAttrs = new ArrayList<>();
			if(!ListUtil.isEmpty(datasourceConfig))
			{
				for (int k = 0; k < datasourceConfig.size(); k++) {
					JSONObject config = datasourceConfig.getJSONObject(k);
					ReportFormsDatasource reportFormsDatasource = new ReportFormsDatasource();
					reportFormsDatasource.setId(IdWorker.getId());
					reportFormsDatasource.setName(config.getString("name"));
					reportFormsDatasource.setDatasourceId(config.getLong("datasourceId"));
					reportFormsDatasource.setTplId(mesLuckySheetsTplDto.getTplId());
					reportFormsDatasource.setSheetId(reportTplSheet.getId());
					reportFormsDatasource.setTableName(config.getString("table"));
					boolean isMain = config.getBooleanValue("isMain");
					if(isMain) {
						reportFormsDatasource.setIsMain(YesNoEnum.YES.getCode());
					}else {
						reportFormsDatasource.setIsMain(YesNoEnum.NO.getCode());
					}
					formsDatasources.add(reportFormsDatasource);
					JSONArray tableDatas = config.getJSONArray("tableDatas");
					Integer cellExtend = 1;//扩展方向
					String aggregateType = "list";//聚合方式
					if(!ListUtil.isEmpty(tableDatas))
					{
						for (int j = 0; j < tableDatas.size(); j++) {
							JSONObject tableData = tableDatas.getJSONObject(j);
							ReportFormsDatasourceAttrs reportFormsDatasourceAttrs = new ReportFormsDatasourceAttrs();
							reportFormsDatasourceAttrs.setReportFormsDatasourceId(reportFormsDatasource.getId());
							reportFormsDatasourceAttrs.setType(1);
							reportFormsDatasourceAttrs.setColumnName(tableData.getString("columnName"));
							String r = StringUtil.getNumeric(tableData.getString("cellCoords"));
							if(StringUtil.isNotEmpty(r))
							{
								reportFormsDatasourceAttrs.setCoordsx(Integer.parseInt(r)-1);
								String col = tableData.getString("cellCoords").replace(r, "");
								if(StringUtil.isNotEmpty(col))
								{
									int c = SheetUtil.excelColStrToNum(col)-1;
									reportFormsDatasourceAttrs.setCoordsy(c);
									reportFormsDatasourceAttrs.setCellCoords(tableData.getString("cellCoords"));
									formsDatasourceAttrs.add(reportFormsDatasourceAttrs);
									String key = String.valueOf(Integer.parseInt(r)-1)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() + c;
									List<LuckysheetReportCell> cells = formsCellsMap.get(key);
									if(!ListUtil.isEmpty(cells))
									{
										if(j == 0)
										{
											cellExtend = cells.get(0).getCellExtend()==null?1:cells.get(0).getCellExtend();
											if(StringUtil.isNotEmpty(cells.get(0).getAggregateType()))
											{
												aggregateType = cells.get(0).getAggregateType();
											}else {
												aggregateType = "list";
											}
										}else {
											String cellAggregate = null;
											if(StringUtil.isNotEmpty(cells.get(0).getAggregateType()))
											{
												cellAggregate = cells.get(0).getAggregateType();
											}else {
												cellAggregate = "list";
											}
											if(cells.get(0).getCellExtend() != null && cellExtend.intValue() != cells.get(0).getCellExtend().intValue())
											{
												throw new BizException(StatusCode.FAILURE,"填报属性【"+ config.getString("name") +"】绑定的单元格扩展方向属性必须一致。");
											}
											if(!aggregateType.equals(cellAggregate)) {
												throw new BizException(StatusCode.FAILURE,"填报属性【"+ config.getString("name") +"】绑定的单元格聚合方式属性必须一致。");
											}
										}
									}else {
										cellExtend = 1;
										aggregateType = "list";
										//不存在，将数据添加到添加单元格中
										LuckysheetReportCell addCell = new LuckysheetReportCell();
										addCell.setTplId(mesLuckySheetsTplDto.getTplId());
										addCell.setSheetId(reportTplSheet.getId());
										addCell.setCoordsx(Integer.valueOf(r)-1);
										addCell.setCoordsy(c);
										addCell.setCellExtend(1);
										addCell.setCellData("{\"r\":"+addCell.getCoordsx()+",\"c\":"+addCell.getCoordsy()+",\"v\":{\"ct\":{\"t\":\"g\",\"fa\":\"General\"},\"v\":\"\",\"m\":\"\"}}");
										addCell.setCellValueType(1);
										if(mesLuckySheetTplDto.getExtraCustomCellConfigs() != null && mesLuckySheetTplDto.getExtraCustomCellConfigs().get(key) != null)
										{
											JSONObject cellConfig = mesLuckySheetTplDto.getExtraCustomCellConfigs().get(key);
											BeanUtils.copyProperties(cellConfig, addCell);
										}else {
//											addCell.setCellAttrs(Constants.DEFALUT_CELL_ATTRS);
											addCell.setFormsAttrs("{}");
										}
										addCells.add(addCell);
										sheetCells.add(addCell);
									}
								}
							}
						}
					}
					JSONArray keys = config.getJSONArray("keys");
					if(!ListUtil.isEmpty(keys)) {
						for (int j = 0; j < keys.size(); j++) {
							JSONObject key = keys.getJSONObject(j);
							ReportFormsDatasourceAttrs reportFormsDatasourceAttrs = new ReportFormsDatasourceAttrs();
							reportFormsDatasourceAttrs.setReportFormsDatasourceId(reportFormsDatasource.getId());
							reportFormsDatasourceAttrs.setType(2);
							reportFormsDatasourceAttrs.setColumnName(key.getString("columnName"));
							reportFormsDatasourceAttrs.setIdType(key.getInteger("idType"));
							formsDatasourceAttrs.add(reportFormsDatasourceAttrs);
						}
					}
					JSONArray autoFillAttrs = config.getJSONArray("autoFillAttrs");
					if(!ListUtil.isEmpty(autoFillAttrs)) {
						for (int j = 0; j < autoFillAttrs.size(); j++) {
							JSONObject autoFillAttr = autoFillAttrs.getJSONObject(j);
							ReportFormsDatasourceAttrs reportFormsDatasourceAttrs = new ReportFormsDatasourceAttrs();
							reportFormsDatasourceAttrs.setReportFormsDatasourceId(reportFormsDatasource.getId());
							reportFormsDatasourceAttrs.setType(3);
							reportFormsDatasourceAttrs.setColumnName(autoFillAttr.getString("columnName"));
							reportFormsDatasourceAttrs.setFillType(autoFillAttr.getInteger("fillType"));
							reportFormsDatasourceAttrs.setFillStrategy(autoFillAttr.getInteger("fillStrategy"));
							reportFormsDatasourceAttrs.setFillValue(autoFillAttr.getString("fillValue"));
							formsDatasourceAttrs.add(reportFormsDatasourceAttrs);
						}
					}
					JSONArray deleteTypes = config.getJSONArray("deleteTypes");
					if(!ListUtil.isEmpty(deleteTypes)) {
						for (int j = 0; j < deleteTypes.size(); j++) {
							JSONObject deleteType = deleteTypes.getJSONObject(j);
							ReportFormsDatasourceAttrs reportFormsDatasourceAttrs = new ReportFormsDatasourceAttrs();
							reportFormsDatasourceAttrs.setReportFormsDatasourceId(reportFormsDatasource.getId());
							reportFormsDatasourceAttrs.setType(4);
							reportFormsDatasourceAttrs.setColumnName(deleteType.getString("columnName"));
							reportFormsDatasourceAttrs.setDeleteType(deleteType.getInteger("deleteType"));
							reportFormsDatasourceAttrs.setDeleteValue(deleteType.getString("deleteValue"));
							formsDatasourceAttrs.add(reportFormsDatasourceAttrs);
						}
					}
					JSONArray mainAttrs = config.getJSONArray("mainAttrs");
					if(!ListUtil.isEmpty(mainAttrs)) {
						for (int j = 0; j < mainAttrs.size(); j++) {
							JSONObject mainAttr = mainAttrs.getJSONObject(j);
							ReportFormsDatasourceAttrs reportFormsDatasourceAttrs = new ReportFormsDatasourceAttrs();
							reportFormsDatasourceAttrs.setReportFormsDatasourceId(reportFormsDatasource.getId());
							reportFormsDatasourceAttrs.setType(5);
							reportFormsDatasourceAttrs.setColumnName(mainAttr.getString("columnName"));
							reportFormsDatasourceAttrs.setMainColumn(mainAttr.getString("mainColumn"));
							reportFormsDatasourceAttrs.setMainName(mainAttr.getString("mainName"));
							reportFormsDatasourceAttrs.setMainDatasourceId(mainAttr.getLong("mainDatasourceId"));
							reportFormsDatasourceAttrs.setMainTable(mainAttr.getString("mainTable"));
							formsDatasourceAttrs.add(reportFormsDatasourceAttrs);
						}
					}
				}
			}
			//模板权限处理，只有模板创建者才能操作
			List<ReportRangeAuth> rangeAuths = new ArrayList<>();
			List<ReportRangeAuthUser> rangeAuthUsers = new ArrayList<>();
			if(this.authenticEnabale)
			{
				if(userInfoDto.getUserId() != null && reportTpl.getCreator().longValue() == userInfoDto.getUserId().longValue())
				{
					//先删除再新增
					QueryWrapper<ReportRangeAuth> rangeAuthQueryWrapper = new QueryWrapper<>();
					rangeAuthQueryWrapper.eq("tpl_id", reportTpl.getId());
	 				rangeAuthQueryWrapper.eq("sheet_id", reportTplSheet.getId());
	  				this.iReportRangeAuthService.remove(rangeAuthQueryWrapper);
					QueryWrapper<ReportRangeAuthUser> rangeAuthUserQueryWrapper = new QueryWrapper<>();
					rangeAuthUserQueryWrapper.eq("tpl_id", reportTpl.getId());
					rangeAuthUserQueryWrapper.eq("sheet_id", reportTplSheet.getId());
					this.iReportRangeAuthUserService.remove(rangeAuthUserQueryWrapper);
					if(mesLuckySheetTplDto.getSheetRangeAuth() != null && !StringUtil.isEmptyMap(mesLuckySheetTplDto.getSheetRangeAuth()))
					{
						ReportRangeAuth reportRangeAuth = null;
						ReportRangeAuthUser reportRangeAuthUser = null;
						for(String mapKey : mesLuckySheetTplDto.getSheetRangeAuth().keySet()){
							reportRangeAuth = new ReportRangeAuth();
							reportRangeAuth.setId(IdWorker.getId());
							reportRangeAuth.setTplId(reportTpl.getId());
							reportRangeAuth.setSheetId(reportTplSheet.getId());
							reportRangeAuth.setSheetIndex(reportTplSheet.getSheetIndex());
							reportRangeAuth.setRangeTxt(mapKey);
							JSONObject range = mesLuckySheetTplDto.getSheetRangeAuth().getJSONObject(mapKey).getJSONObject("range");
							reportRangeAuth.setRowsNo(JSON.toJSONString(range.getJSONArray("row")));
							reportRangeAuth.setColsNo(JSON.toJSONString(range.getJSONArray("column")));
							rangeAuths.add(reportRangeAuth);
							JSONArray userIds = mesLuckySheetTplDto.getSheetRangeAuth().getJSONObject(mapKey).getJSONArray("userIds");
							for (int j = 0; j < userIds.size(); j++) {
								reportRangeAuthUser = new ReportRangeAuthUser();
								reportRangeAuthUser.setTplId(reportTpl.getId());
								reportRangeAuthUser.setSheetId(reportTplSheet.getId());
								reportRangeAuthUser.setRangeAuthId(reportRangeAuth.getId());
								reportRangeAuthUser.setUserId(userIds.getLong(j));
								rangeAuthUsers.add(reportRangeAuthUser);
							}
						}
					}
				}
			}
			//图表单元格处理
			List<LuckysheetReportCell> chartCells = new ArrayList<>();
			if(!ListUtil.isEmpty(mesLuckySheetTplDto.getChartCells()))
			{
				for (int j = 0; j < mesLuckySheetTplDto.getChartCells().size(); j++) {
					LuckysheetReportCell chartCell = this.getChartCell(mesLuckySheetTplDto.getChartCells().getJSONObject(j));
					chartCell.setTplId(mesLuckySheetsTplDto.getTplId());
					chartCell.setSheetId(reportTplSheet.getId());
					chartCells.add(chartCell);
				}
			}
			if(!ListUtil.isEmpty(chartCells))
			{
				this.iLuckysheetReportCellService.saveBatch(chartCells);
			}
			if(!ListUtil.isEmpty(reportTplBlockCells))
			{
				this.iLuckysheetReportCellService.saveBatch(reportTplBlockCells);
			}
			if(!ListUtil.isEmpty(blockCells))
			{
				this.iLuckysheetReportBlockCellService.saveBatch(blockCells);
			}
			if(!ListUtil.isEmpty(addCells))
			{
				for (int j = 0; j < addCells.size(); j++) {
					String key = addCells.get(j).getSheetId() + "_" + addCells.get(j).getCoordsx() + "_" + addCells.get(j).getCoordsy();
					if(cellRelyRelations.get(key) == null)
					{
						addCells.get(j).setIsRelied(YesNoEnum.NO.getCode());
					}else {
						addCells.get(j).setIsRelied(YesNoEnum.YES.getCode());
						addCells.get(j).setRelyCells(cellRelyRelations.get(key));
					}
				}
				this.iLuckysheetReportCellService.saveBatch(addCells);
			}
			if(!ListUtil.isEmpty(updateCells))
			{
				for (int j = 0; j < updateCells.size(); j++) {
					String key = updateCells.get(j).getSheetId() + "_" + updateCells.get(j).getCoordsx() + "_" + updateCells.get(j).getCoordsy();
					if(cellRelyRelations.get(key) == null)
					{
						updateCells.get(j).setIsRelied(YesNoEnum.NO.getCode());
					}else {
						updateCells.get(j).setIsRelied(YesNoEnum.YES.getCode());
						updateCells.get(j).setRelyCells(cellRelyRelations.get(key));
					}
				}
				this.iLuckysheetReportCellService.updateBatchById(updateCells);
			}
			if(!ListUtil.isEmpty(deleteCells))
			{
				this.iLuckysheetReportCellService.updateBatchById(deleteCells);
			}
			if(!ListUtil.isEmpty(formsDatasources))
			{
				this.iReportFormsDatasourceService.saveBatch(formsDatasources);
			}
			if(!ListUtil.isEmpty(formsDatasourceAttrs))
			{
				this.iReportFormsDatasourceAttrsService.saveBatch(formsDatasourceAttrs);
			}
			if(mesLuckySheetsTplDto.getConfigs().get(i).getPrintSettings() != null)
			{
				mesLuckySheetsTplDto.getConfigs().get(i).getPrintSettings().setTplId(mesLuckySheetsTplDto.getTplId());
				mesLuckySheetsTplDto.getConfigs().get(i).getPrintSettings().setTplSheetId(reportTplSheet.getId());
				this.iReportSheetPdfPrintSettingService.saveOrUpdate(mesLuckySheetsTplDto.getConfigs().get(i).getPrintSettings());
				printSettings.put(mesLuckySheetTplDto.getSheetIndex(), mesLuckySheetsTplDto.getConfigs().get(i).getPrintSettings().getId());
			}
			if(ListUtil.isNotEmpty(rangeAuths)) {
				this.iReportRangeAuthService.saveBatch(rangeAuths);
			}
			if(ListUtil.isNotEmpty(rangeAuthUsers)){
				this.iReportRangeAuthUserService.saveBatch(rangeAuthUsers);
			}
		}
		result.setPrintSettings(printSettings);
		result.setStatusMsg(MessageUtil.getValue("info.save"));
		return result;
	}
	
	private void getCellRelyRelations(LuckysheetReportCell luckysheetReportCell,Map<String, String> cellRelyRelations)
	{
		if(luckysheetReportCell.getDataFrom() != null && luckysheetReportCell.getDataFrom().intValue() == 3 && StringUtil.isNotEmpty(luckysheetReportCell.getGroupSummaryDependencyC()) && 
				StringUtil.isNotEmpty(luckysheetReportCell.getGroupSummaryDependencyR()) && CheckUtil.isNumeric(luckysheetReportCell.getGroupSummaryDependencyR()))
		{
			int r = Integer.valueOf(luckysheetReportCell.getGroupSummaryDependencyR()) - 1;
			int c = SheetUtil.excelColStrToNum(luckysheetReportCell.getGroupSummaryDependencyC())-1;
			String key = luckysheetReportCell.getSheetId() + "_" + r + "_" + c;
			if(cellRelyRelations.get(key) == null)
			{
				cellRelyRelations.put(key, luckysheetReportCell.getCoordsx()+"_"+luckysheetReportCell.getCoordsy());
			}else {
				String cells = cellRelyRelations.get(key)+","+luckysheetReportCell.getCoordsx()+"_"+luckysheetReportCell.getCoordsy();
				cellRelyRelations.put(key, cells);
			}
		}
	}
	
	private  String customKey(Map<String,Object> map){
        return map.get(LuckySheetPropsEnum.R.getCode()).toString()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+map.get(LuckySheetPropsEnum.C.getCode()).toString();
    }
	
	private  String customKey(LuckysheetReportFormsCell luckysheetReportFormsCell){
        return luckysheetReportFormsCell.getCoordsx().toString()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportFormsCell.getCoordsy().toString();
    }
	
	private  String customKey(LuckysheetReportCell luckysheetReportCell){
        return luckysheetReportCell.getCoordsx().toString()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportCell.getCoordsy().toString();
    }
	
	/**  
	 * @Title: getReportCell
	 * @Description: 设置单元格属性
	 * @param object
	 * @param columnNames
	 * @param extraCustomCellConfigs
	 * @param hyperlinks
	 * @return
	 * @throws JsonProcessingException
	 * @author caiyang
	 * @date 2022-04-03 08:52:33 
	 */ 
	private LuckysheetReportCell getReportCell(Map<String, Object> object,List<List<String>> columnNames,Map<String, JSONObject> extraCustomCellConfigs,
			Map<String, JSONObject> hyperlinks,JSONArray luckysheetAlternateformatSave,Map<String, String> chartCellMap,JSONObject dataVerification) throws JsonProcessingException {
		LuckysheetReportCell luckysheetReportCell = new LuckysheetReportCell();
		luckysheetReportCell.setCoordsx((Integer) object.get(LuckySheetPropsEnum.R.getCode()));
		luckysheetReportCell.setCoordsy((Integer) object.get(LuckySheetPropsEnum.C.getCode()));
		ObjectMapper objectMapper = new ObjectMapper();
		luckysheetReportCell.setCellData(objectMapper.writeValueAsString(object));
		Map<String, Object> cellValueMap = (Map<String, Object>) object.get(LuckySheetPropsEnum.CELLCONFIG.getCode());
		Map<String, Object> cellType = (Map<String, Object>) cellValueMap.get(LuckySheetPropsEnum.CELLTYPE.getCode());
		Map<String, AlternateformatDto> alternateFormat = this.getAlternateformat(luckysheetAlternateformatSave);
		String key = luckysheetReportCell.getCoordsx()+"_"+luckysheetReportCell.getCoordsy();
		String cellValue = "";
		if(cellType != null)
		{
			String t = String.valueOf(cellType.get(LuckySheetPropsEnum.TYPE.getCode()));
			String f = String.valueOf(cellValueMap.get(LuckySheetPropsEnum.FUNCTION.getCode()) == null ? "" : cellValueMap.get(LuckySheetPropsEnum.FUNCTION.getCode()));
			if(StringUtil.isNotEmpty(f))
			{
				luckysheetReportCell.setCellValue(f);
				cellValue = f;
				luckysheetReportCell.setIsFunction(YesNoEnum.YES.getCode());;
			}else {
				luckysheetReportCell.setIsFunction(YesNoEnum.NO.getCode());
				if(LuckySheetPropsEnum.INLINESTR.getCode().equals(t))
				{
					List<Map<String, Object>> list = (List<Map<String, Object>>) cellType.get(LuckySheetPropsEnum.STRING.getCode());
					if(!ListUtil.isEmpty(list))
					{
						cellValue = String.valueOf(list.get(0).get(LuckySheetPropsEnum.CELLVALUE.getCode()));
					}
					luckysheetReportCell.setCellValue(cellValue);
				}else {
					if(cellValueMap.get(LuckySheetPropsEnum.CELLVALUE.getCode()) == null)
					{
						cellValue = "";
					}else {
						cellValue = String.valueOf(cellValueMap.get(LuckySheetPropsEnum.CELLVALUE.getCode()));
					}
					luckysheetReportCell.setCellValue(cellValue);
				}
			}
		}else {
			luckysheetReportCell.setIsFunction(YesNoEnum.NO.getCode());
			luckysheetReportCell.setCellValue(cellValue);
		}
		Map<String, Object> configMap = (Map<String, Object>) object.get(LuckySheetPropsEnum.CELLCONFIG.getCode());
		Map<String, Object> mergeConfig = (Map<String, Object>) configMap.get(LuckySheetPropsEnum.MERGECELLS.getCode());
		if(mergeConfig == null) {
			luckysheetReportCell.setIsMerge(YesNoEnum.NO.getCode());
			luckysheetReportCell.setRowSpan(1);
			luckysheetReportCell.setColSpan(1);
		}else {
			luckysheetReportCell.setIsMerge(YesNoEnum.YES.getCode());
			luckysheetReportCell.setRowSpan((Integer) mergeConfig.get(LuckySheetPropsEnum.ROWSPAN.getCode()));
			luckysheetReportCell.setColSpan((Integer) mergeConfig.get(LuckySheetPropsEnum.COLSPAN.getCode()));
		}
		Pattern paramPattern=Pattern.compile("\\$\\s*\\{(.*?)}");
		Matcher parammatcher=paramPattern.matcher(cellValue);
		boolean matchFlag = false;
		while(parammatcher.find()){
			matchFlag = true;
			break;
		}
		if(!ListUtil.isEmpty(columnNames) && matchFlag)
		{
			luckysheetReportCell.setCellValueType(CellValueTypeEnum.VARIABLE.getCode());
			if(YesNoEnum.NO.getCode().intValue() == luckysheetReportCell.getIsFunction().intValue())
			{
				luckysheetReportCell.setDatasetName(ListUtil.getFormulaDatasetName(cellValue, columnNames));
			}else {
				luckysheetReportCell.setDatasetName(ListUtil.getFormulaDatasetName(cellValue, columnNames));
			}
		}else {
			luckysheetReportCell.setCellValueType(CellValueTypeEnum.FIXED.getCode());
		}
		String mapKey = object.get(LuckySheetPropsEnum.R.getCode())+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+object.get(LuckySheetPropsEnum.C.getCode());
		JSONObject extraCustomCellConfig = extraCustomCellConfigs.get(mapKey);
		if(extraCustomCellConfig != null) {
			Integer cellExtend = extraCustomCellConfig.getInteger(LuckySheetPropsEnum.CELLEXTEND.getCode());
			if(cellExtend != null)
			{
				luckysheetReportCell.setCellExtend(cellExtend.intValue());
			}else {
				luckysheetReportCell.setCellExtend(1);
			}
			String aggregateType = extraCustomCellConfig.getString(LuckySheetPropsEnum.AGGREGATETYPE.getCode());
			if(StringUtil.isNotEmpty(aggregateType))
			{
				luckysheetReportCell.setAggregateType(aggregateType);
			}
			String cellFunction = extraCustomCellConfig.getString(LuckySheetPropsEnum.CELLFUNCTION.getCode());
			if(StringUtil.isNotEmpty(cellFunction))
			{
				luckysheetReportCell.setCellFunction(Integer.valueOf(cellFunction));
			}
			String digit = extraCustomCellConfig.getString(LuckySheetPropsEnum.DIGIT.getCode());
			if(StringUtil.isNotEmpty(digit))
			{
				luckysheetReportCell.setDigit(Integer.valueOf(digit));
			}
			String groupSummaryDependencyr = extraCustomCellConfig.getString(LuckySheetPropsEnum.GROUPSUMMARYDEPENDENCYR.getCode());
			if(StringUtil.isNotEmpty(groupSummaryDependencyr))
			{
				luckysheetReportCell.setGroupSummaryDependencyR(groupSummaryDependencyr);
			}
			String groupSummaryDependencyc = extraCustomCellConfig.getString(LuckySheetPropsEnum.GROUPSUMMARYDEPENDENCYC.getCode());
			if(StringUtil.isNotEmpty(groupSummaryDependencyc))
			{
				luckysheetReportCell.setGroupSummaryDependencyC(groupSummaryDependencyc);
			}
			Integer dataFrom =extraCustomCellConfig.getInteger(LuckySheetPropsEnum.DATAFROM.getCode());
			if(dataFrom != null)
			{
				luckysheetReportCell.setDataFrom(dataFrom.intValue());
			}
			boolean isGroupMerge = extraCustomCellConfig.getBooleanValue(LuckySheetPropsEnum.ISGROUPMERGE.getCode());
			luckysheetReportCell.setIsGroupMerge(isGroupMerge);
			//2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//			boolean warnging = extraCustomCellConfig.getBooleanValue(LuckySheetPropsEnum.WARNING.getCode());
//			luckysheetReportCell.setWarning(warnging);
//			String warningColor = extraCustomCellConfig.getString(LuckySheetPropsEnum.WARNINGCOLOR.getCode());
//			if(StringUtil.isNotEmpty(warningColor))
//			{
//				luckysheetReportCell.setWarningColor(warningColor);;
//			}else {
//				luckysheetReportCell.setWarningColor("#FF0000");
//			}
//			String threshold = extraCustomCellConfig.getString(LuckySheetPropsEnum.THRESHOLD.getCode());
//			if(StringUtil.isNotEmpty(threshold))
//			{
//				luckysheetReportCell.setThreshold(threshold);
//			}else {
//				luckysheetReportCell.setThreshold("80");
//			}
//			String warningRules = extraCustomCellConfig.getString(LuckySheetPropsEnum.WARNINGRULES.getCode());
//			if(StringUtil.isNotEmpty(warningRules))
//			{
//				luckysheetReportCell.setWarningRules(warningRules);
//			}else {
//				luckysheetReportCell.setWarningRules(">=");
//			}
//			String warningContent = extraCustomCellConfig.getString(LuckySheetPropsEnum.WARNINGCONTENT.getCode());
//			if(StringUtil.isNotEmpty(warningContent))
//			{
//				luckysheetReportCell.setWarningContent(warningContent);
//			}
			boolean isDict = extraCustomCellConfig.getBooleanValue(LuckySheetPropsEnum.ISDICT.getCode());
			luckysheetReportCell.setIsDict(isDict);
			if(isDict)
			{
				Long datasourceId = extraCustomCellConfig.getLong(LuckySheetPropsEnum.DATASOURCEID.getCode());
				if(datasourceId != null)
				{
					luckysheetReportCell.setDatasourceId(datasourceId);
				}
				String dictType = extraCustomCellConfig.getString(LuckySheetPropsEnum.DICTTYPE.getCode());
				if(StringUtil.isNotEmpty(dictType))
				{
					luckysheetReportCell.setDictType(dictType);
				}
			}
			String groupProperty = extraCustomCellConfig.getString(LuckySheetPropsEnum.GROUPPROPERTY.getCode());
			if(StringUtil.isNotEmpty(groupProperty))
			{
				luckysheetReportCell.setGroupProperty(groupProperty);
			}else {
				luckysheetReportCell.setGroupProperty("");
			}
			JSONArray cellConditions = extraCustomCellConfig.getJSONArray(LuckySheetPropsEnum.CELLCONDITIONS.getCode());
			if(ListUtil.isEmpty(cellConditions))
			{
				cellConditions = new JSONArray();
				luckysheetReportCell.setIsConditions(YesNoEnum.NO.getCode());
			}else {
				luckysheetReportCell.setIsConditions(YesNoEnum.YES.getCode());
			}
			luckysheetReportCell.setCellConditions(objectMapper.writeValueAsString(cellConditions));
			String filterType = extraCustomCellConfig.getString(LuckySheetPropsEnum.FILTERTYPE.getCode());
			luckysheetReportCell.setFilterType(filterType);
			String seriesName = extraCustomCellConfig.getString("seriesName");
			if(StringUtil.isNotEmpty(seriesName))
			{
				luckysheetReportCell.setSeriesName(seriesName);
			}else {
				luckysheetReportCell.setSeriesName("");
			}
			boolean isDrill = extraCustomCellConfig.getBooleanValue("isDrill");
			luckysheetReportCell.setIsDrill(isDrill);
			if(isDrill)
			{
				Long drillId = extraCustomCellConfig.getLong("drillId");
				luckysheetReportCell.setDrillId(drillId);
				String drillAttrs = extraCustomCellConfig.getString("drillAttrs");
				luckysheetReportCell.setDrillAttrs(drillAttrs);
			}
			boolean unitTransfer = extraCustomCellConfig.getBooleanValue("unitTransfer");
			luckysheetReportCell.setUnitTransfer(unitTransfer);
			Integer transferType = extraCustomCellConfig.getInteger("transferType");
			if(transferType != null)
			{
				luckysheetReportCell.setTransferType(transferType);
			}else {
				luckysheetReportCell.setTransferType(1);
			}
			String multiple = extraCustomCellConfig.getString("multiple");
			if(StringUtil.isNotEmpty(multiple))
			{
				luckysheetReportCell.setMultiple(multiple);
			}
			JSONArray cellHiddenConditions = extraCustomCellConfig.getJSONArray(LuckySheetPropsEnum.CELLHIDDENCONDITIONS.getCode());
			if(ListUtil.isEmpty(cellHiddenConditions))
			{
				cellHiddenConditions = new JSONArray();
				luckysheetReportCell.setIsHiddenConditions(YesNoEnum.NO.getCode());
			}else {
				luckysheetReportCell.setIsHiddenConditions(YesNoEnum.YES.getCode());
			}
			luckysheetReportCell.setHiddenConditions(objectMapper.writeValueAsString(cellHiddenConditions));
			String hiddenType = extraCustomCellConfig.getString(LuckySheetPropsEnum.CELLHIDDENTYPE.getCode());
			luckysheetReportCell.setHiddenType(hiddenType);
			boolean isSubtotal = extraCustomCellConfig.getBooleanValue(LuckySheetPropsEnum.ISSUBTOTAL.getCode());
			luckysheetReportCell.setIsSubtotal(isSubtotal);
			JSONArray subTotalCells = extraCustomCellConfig.getJSONArray(LuckySheetPropsEnum.SUBTOTALCELLS.getCode());
			if(ListUtil.isEmpty(subTotalCells))
			{
				subTotalCells = new JSONArray();
			}
			luckysheetReportCell.setSubtotalCells(objectMapper.writeValueAsString(subTotalCells));
			JSONArray subtotalCalc = extraCustomCellConfig.getJSONArray(LuckySheetPropsEnum.SUBTOTALCALC.getCode());
			if(ListUtil.isEmpty(subtotalCalc))
			{
				luckysheetReportCell.setIsSubtotalCalc(false);
				subtotalCalc = new JSONArray();
			}else {
				luckysheetReportCell.setIsSubtotalCalc(true);
			}
			luckysheetReportCell.setSubtotalCalc(objectMapper.writeValueAsString(subtotalCalc));
			JSONArray subtotalAttrs = extraCustomCellConfig.getJSONArray(LuckySheetPropsEnum.SUBTOTALATTRS.getCode());
			if(ListUtil.isEmpty(subtotalAttrs))
			{
				subtotalAttrs = new JSONArray();
			}
			luckysheetReportCell.setSubtotalAttrs(objectMapper.writeValueAsString(subtotalAttrs));
			Integer cellFillType = extraCustomCellConfig.getInteger("cellFillType");
			if(cellFillType != null) {
				luckysheetReportCell.setCellFillType(cellFillType);
			}else {
				luckysheetReportCell.setCellFillType(1);
			}
			JSONObject formsAttrs = extraCustomCellConfig.getJSONObject("formsAttrs");
			if(formsAttrs != null) {
				luckysheetReportCell.setFormsAttrs(JSON.toJSONString(formsAttrs));
			}else {
				luckysheetReportCell.setFormsAttrs("{}");
			}
			boolean isObject = extraCustomCellConfig.getBooleanValue("isObject");
			luckysheetReportCell.setIsObject(isObject);
			Integer dataType = extraCustomCellConfig.getInteger("dataType");
			if(dataType != null) {
				luckysheetReportCell.setDataType(dataType);
			}else {
				luckysheetReportCell.setDataType(1);
			}
			String dataAttr = extraCustomCellConfig.getString("dataAttr");
			luckysheetReportCell.setDataAttr(dataAttr);
			Integer subExtend = extraCustomCellConfig.getInteger("subExtend");
			if(subExtend != null) {
				luckysheetReportCell.setSubExtend(subExtend);
			}else {
				luckysheetReportCell.setSubExtend(1);
			}
			Integer priortyMoveDirection = extraCustomCellConfig.getInteger("priortyMoveDirection");
			if(priortyMoveDirection != null) {
				luckysheetReportCell.setPriortyMoveDirection(priortyMoveDirection);
			}else {
				luckysheetReportCell.setPriortyMoveDirection(1);
			}
			Integer sourceType = extraCustomCellConfig.getInteger("sourceType");
			if(sourceType != null) {
				luckysheetReportCell.setSourceType(sourceType);
			}else {
				luckysheetReportCell.setSourceType(1);
			}
			String dictContent = extraCustomCellConfig.getString("dictContent");
			luckysheetReportCell.setDictContent(dictContent);
			
			String compareAttr1 = extraCustomCellConfig.getString("compareAttr1");
			luckysheetReportCell.setCompareAttr1(compareAttr1);
			String compareAttr2 = extraCustomCellConfig.getString("compareAttr2");
			luckysheetReportCell.setCompareAttr2(compareAttr2);
			boolean isDump = extraCustomCellConfig.getBooleanValue("isDump");
			luckysheetReportCell.setIsDump(isDump);
			String dumpAttr = extraCustomCellConfig.getString("dumpAttr");
			luckysheetReportCell.setDumpAttr(dumpAttr);
			boolean keepEmptyCell = extraCustomCellConfig.getBooleanValue("keepEmptyCell");
			luckysheetReportCell.setKeepEmptyCell(keepEmptyCell);
		}
		JSONObject hyperlink = hyperlinks.get(mapKey);
		if(hyperlink != null)
		{
			luckysheetReportCell.setIsLink(YesNoEnum.YES.getCode());
			luckysheetReportCell.setLinkConfig(JSONObject.toJSONString(hyperlink));
		}else {
			luckysheetReportCell.setIsLink(YesNoEnum.NO.getCode());
		}
		AlternateformatDto alternateformatDto = alternateFormat.get(luckysheetReportCell.getCoordsx() + "_" + luckysheetReportCell.getCoordsy());
		if(alternateformatDto == null)
		{
			luckysheetReportCell.setAlternateFormat(YesNoEnum.NO.getCode());
		}else {
			luckysheetReportCell.setAlternateFormat(YesNoEnum.YES.getCode());
			luckysheetReportCell.setAlternateFormatFcOdd(alternateformatDto.getFcOdd());
			luckysheetReportCell.setAlternateFormatFcEven(alternateformatDto.getFcEven());
			luckysheetReportCell.setAlternateFormatBcOdd(alternateformatDto.getBcOdd());
			luckysheetReportCell.setAlternateFormatBcEven(alternateformatDto.getBcEven());
		}
		if(chartCellMap.containsKey(key))
		{
			luckysheetReportCell.setIsChartAttr(YesNoEnum.YES.getCode());
			luckysheetReportCell.setChartIds(chartCellMap.get(key));
		}else {
			luckysheetReportCell.setIsChartAttr(YesNoEnum.NO.getCode());
			luckysheetReportCell.setChartIds("");
		}
		if(dataVerification.containsKey(key))
		{
			luckysheetReportCell.setIsDataVerification(YesNoEnum.YES.getCode());
			luckysheetReportCell.setDataVerification(dataVerification.getString(key));
		}else {
			luckysheetReportCell.setIsDataVerification(YesNoEnum.NO.getCode());
			luckysheetReportCell.setDataVerification("{}");
		}
		return luckysheetReportCell;
	}
	
	/**  
	 * @MethodName: getChartCell
	 * @Description: 图表单元格
	 * @author caiyang
	 * @param jsonObject
	 * @return 
	 * @return LuckysheetReportCell
	 * @throws JsonProcessingException 
	 * @date 2023-04-25 08:33:53 
	 */  
	private LuckysheetReportCell getChartCell(JSONObject jsonObject) throws JsonProcessingException
	{
		LuckysheetReportCell luckysheetReportCell = new LuckysheetReportCell();
		luckysheetReportCell.setCoordsx(jsonObject.getInteger(LuckySheetPropsEnum.R.getCode()));
		luckysheetReportCell.setCoordsy(jsonObject.getInteger(LuckySheetPropsEnum.C.getCode()));
		ObjectMapper objectMapper = new ObjectMapper();
		luckysheetReportCell.setCellData(objectMapper.writeValueAsString(jsonObject));
		luckysheetReportCell.setIsFunction(YesNoEnum.NO.getCode());
		luckysheetReportCell.setCellValue("");
		JSONObject configMap = jsonObject.getJSONObject(LuckySheetPropsEnum.CELLCONFIG.getCode());
		JSONObject mergeConfig = configMap.getJSONObject(LuckySheetPropsEnum.MERGECELLS.getCode());
		if(mergeConfig == null) {
			luckysheetReportCell.setIsMerge(YesNoEnum.NO.getCode());
			luckysheetReportCell.setRowSpan(1);
			luckysheetReportCell.setColSpan(1);
		}else {
			luckysheetReportCell.setIsMerge(YesNoEnum.YES.getCode());
			luckysheetReportCell.setRowSpan(mergeConfig.getInteger(LuckySheetPropsEnum.ROWSPAN.getCode()));
			luckysheetReportCell.setColSpan(mergeConfig.getInteger(LuckySheetPropsEnum.COLSPAN.getCode()));
		}
		luckysheetReportCell.setCellValueType(CellValueTypeEnum.FIXED.getCode());
		luckysheetReportCell.setIsLink(YesNoEnum.NO.getCode());
		luckysheetReportCell.setAlternateFormat(YesNoEnum.NO.getCode());
		luckysheetReportCell.setIsChartAttr(YesNoEnum.NO.getCode());
		luckysheetReportCell.setIsChartCell(YesNoEnum.YES.getCode());
		luckysheetReportCell.setChartIds(jsonObject.getString("chartId"));
		return luckysheetReportCell;
	}
	
	/**  
	 * @MethodName: getAlternateformat
	 * @Description: 获取交替颜色配置
	 * @author caiyang
	 * @param luckysheetAlternateformatSave
	 * @return 
	 * @return Map<String,String>
	 * @date 2023-03-13 04:56:05 
	 */  
	private Map<String, AlternateformatDto> getAlternateformat(JSONArray luckysheetAlternateformatSave)
	{
		Map<String, AlternateformatDto> result = new HashMap<String, AlternateformatDto>();
		if(!ListUtil.isEmpty(luckysheetAlternateformatSave))
		{
			for (int i = 0; i < luckysheetAlternateformatSave.size(); i++) {
				JSONObject cellRange = luckysheetAlternateformatSave.getJSONObject(i).getJSONObject("cellrange");
				JSONObject format = luckysheetAlternateformatSave.getJSONObject(i).getJSONObject("format");
				JSONObject one = format.getJSONObject("one");
				JSONObject two = format.getJSONObject("two");
				String fcOdd = one.getString("fc");
				String fcEven = two.getString("fc");
				String bcOdd = one.getString("bc");
				String bcEven = two.getString("bc");
				JSONArray column = cellRange.getJSONArray("column");
				JSONArray row = cellRange.getJSONArray("row");
				int startR = row.getIntValue(0);
				int rowSpan = row.getIntValue(1) - row.getIntValue(0) + 1;
				int startC = column.getIntValue(0);
				int colSpan = column.getIntValue(1) - column.getIntValue(0) + 1;
				AlternateformatDto alternateformatDto = null;
				for (int j = 0; j < rowSpan; j++) {
					for (int k = 0; k < colSpan; k++) {
						alternateformatDto = new AlternateformatDto();
						alternateformatDto.setFcOdd(fcOdd);
						alternateformatDto.setFcEven(fcEven);
						alternateformatDto.setBcOdd(bcOdd);
						alternateformatDto.setBcEven(bcEven);
						result.put((startR+j)+"_"+(startC+k), alternateformatDto);
					}
				}
			}
		}
		return result;
	}
	
	/**  
	 * @Title: getBlockReportCell
	 * @Description: 设置快循环单元格属性
	 * @param object
	 * @return
	 * @author caiyang
	 * @date 2022-04-03 08:51:58 
	 */ 
	private LuckysheetReportCell getBlockReportCell(Map<String, Object> object) {
		LuckysheetReportCell luckysheetReportCell = new LuckysheetReportCell();
		luckysheetReportCell.setId(IdWorker.getId());
		luckysheetReportCell.setCoordsx((Integer) object.get(LuckySheetPropsEnum.R.getCode()));
		luckysheetReportCell.setCoordsy((Integer) object.get(LuckySheetPropsEnum.C.getCode()));
		luckysheetReportCell.setCellExtend(CellExtendEnum.VERTICAL.getCode());//快循环默认向下扩展
		luckysheetReportCell.setRowSpan(Integer.valueOf(String.valueOf(object.get("rs"))));
		luckysheetReportCell.setColSpan(Integer.valueOf(String.valueOf(object.get("cs"))));
		luckysheetReportCell.setCellValueType(CellValueTypeEnum.BLOCK.getCode());
		luckysheetReportCell.setAggregateType(String.valueOf(object.get("aggregateType")));
		luckysheetReportCell.setGroupProperty(String.valueOf(object.get("groupProperty")));
		luckysheetReportCell.setHloopCount(object.get("hloopCount")==null?1:Integer.parseInt(String.valueOf(object.get("hloopCount"))));
		luckysheetReportCell.setHloopEmptyCount(object.get("hloopEmptyCount")==null?1:Integer.parseInt(String.valueOf(object.get("hloopEmptyCount"))));
		luckysheetReportCell.setVloopEmptyCount(object.get("vloopEmptyCount")==null?1:Integer.parseInt(String.valueOf(object.get("vloopEmptyCount"))));
		luckysheetReportCell.setSubBlockRange(object.get("subBlockRange")==null?"":String.valueOf(object.get("subBlockRange")));
		return luckysheetReportCell;
	}
	
	/**  
	 * @Title: getBlockCell
	 * @Description: 设置循环块中单元格属性
	 * @param object
	 * @param tplId
	 * @param reportCellId
	 * @param columnNames
	 * @param hyperlinks
	 * @return
	 * @throws JsonProcessingException
	 * @author caiyang
	 * @date 2022-04-04 09:42:27 
	 */ 
	private LuckysheetReportBlockCell getBlockCell(Map<String, Object> object,Long tplId,Long reportCellId,
			List<List<String>> columnNames,Map<String, JSONObject> hyperlinks,JSONObject dataVerification) throws JsonProcessingException {
		LuckysheetReportBlockCell luckysheetReportBlockCell = new LuckysheetReportBlockCell();
		luckysheetReportBlockCell.setTplId(tplId);
		luckysheetReportBlockCell.setReportCellId(reportCellId);
		luckysheetReportBlockCell.setCoordsx((Integer) object.get(LuckySheetPropsEnum.R.getCode()));
		luckysheetReportBlockCell.setCoordsy((Integer) object.get(LuckySheetPropsEnum.C.getCode()));
		ObjectMapper objectMapper = new ObjectMapper();
		luckysheetReportBlockCell.setCellData(objectMapper.writeValueAsString(object));
	    Map<String, Object> cellValueMap = (Map<String, Object>) object.get(LuckySheetPropsEnum.CELLCONFIG.getCode());
	    Map<String, Object> cellType = (Map<String, Object>) cellValueMap.get(LuckySheetPropsEnum.CELLTYPE.getCode());
	    String key = luckysheetReportBlockCell.getCoordsx()+"_"+luckysheetReportBlockCell.getCoordsy();
	    String cellValue = "";
        if(cellType != null)
        {
            String t = String.valueOf(cellType.get(LuckySheetPropsEnum.TYPE.getCode()));
            String f = String.valueOf(cellValueMap.get(LuckySheetPropsEnum.FUNCTION.getCode()) == null ? "" : cellValueMap.get(LuckySheetPropsEnum.FUNCTION.getCode()));
            if(StringUtil.isNotEmpty(f))
            {
            	luckysheetReportBlockCell.setCellValue(f);
				cellValue = f;
				luckysheetReportBlockCell.setIsFunction(YesNoEnum.YES.getCode());
            }else {
            	luckysheetReportBlockCell.setIsFunction(YesNoEnum.NO.getCode());
            	if(LuckySheetPropsEnum.INLINESTR.getCode().equals(t))
                {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) cellType.get(LuckySheetPropsEnum.STRING.getCode());
                    cellValue = String.valueOf(list.get(0).get(LuckySheetPropsEnum.CELLVALUE.getCode()));
                    luckysheetReportBlockCell.setCellValue(cellValue);
                }else {
                    cellValue = String.valueOf(cellValueMap.get(LuckySheetPropsEnum.CELLVALUE.getCode()));
                    luckysheetReportBlockCell.setCellValue(cellValue);
                }
            }
            
        }else {
        	luckysheetReportBlockCell.setIsFunction(YesNoEnum.NO.getCode());
        	luckysheetReportBlockCell.setCellValue(cellValue);
        }
        Map<String, Object> configMap = (Map<String, Object>) object.get(LuckySheetPropsEnum.CELLCONFIG.getCode());
        Map<String, Object> mergeConfig = (Map<String, Object>) configMap.get(LuckySheetPropsEnum.MERGECELLS.getCode());
        if(mergeConfig == null) {
        	luckysheetReportBlockCell.setIsMerge(YesNoEnum.NO.getCode());
        	luckysheetReportBlockCell.setRowSpan(1);
        	luckysheetReportBlockCell.setColSpan(1);
        }else {
        	luckysheetReportBlockCell.setIsMerge(YesNoEnum.YES.getCode());
        	luckysheetReportBlockCell.setRowSpan((Integer) mergeConfig.get(LuckySheetPropsEnum.ROWSPAN.getCode()));
        	luckysheetReportBlockCell.setColSpan((Integer) mergeConfig.get(LuckySheetPropsEnum.COLSPAN.getCode()));
        }
        Pattern paramPattern=Pattern.compile("\\$\\s*\\{(.*?)}");
		Matcher parammatcher=paramPattern.matcher(cellValue);
		boolean matchFlag = false;
		while(parammatcher.find()){
			matchFlag = true;
			break;
		}
		if(!ListUtil.isEmpty(columnNames) && matchFlag) {
        	luckysheetReportBlockCell.setCellValueType(CellValueTypeEnum.VARIABLE.getCode());
        	if(YesNoEnum.NO.getCode().intValue() == luckysheetReportBlockCell.getIsFunction().intValue())
			{
        		luckysheetReportBlockCell.setDatasetName(ListUtil.getFormulaDatasetName(cellValue, columnNames).split(",")[0]);
			}else {
				luckysheetReportBlockCell.setDatasetName(ListUtil.getFormulaDatasetName(cellValue, columnNames).split(",")[0]);
			}
        }else {
        	luckysheetReportBlockCell.setCellValueType(CellValueTypeEnum.FIXED.getCode());
        }
        String mapKey = object.get(LuckySheetPropsEnum.R.getCode())+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+object.get(LuckySheetPropsEnum.C.getCode());
        JSONObject hyperlink = hyperlinks.get(mapKey);
        if(hyperlink != null)
        {
        	luckysheetReportBlockCell.setIsLink(YesNoEnum.YES.getCode());
        	luckysheetReportBlockCell.setLinkConfig(JSONObject.toJSONString(hyperlink));
        }else {
        	luckysheetReportBlockCell.setIsLink(YesNoEnum.NO.getCode());
        }
        if(dataVerification.containsKey(key))
		{
        	luckysheetReportBlockCell.setIsDataVerification(YesNoEnum.YES.getCode());
        	luckysheetReportBlockCell.setDataVerification(dataVerification.getString(key));
		}else {
			luckysheetReportBlockCell.setIsDataVerification(YesNoEnum.NO.getCode());
			luckysheetReportBlockCell.setDataVerification("{}");
		}
        if(object.get("isSubCell") != null) {
        	luckysheetReportBlockCell.setIsSubCell(1);
        }else {
        	luckysheetReportBlockCell.setIsSubCell(2);
        }
		return luckysheetReportBlockCell;
	}
	
	/**  
	 * @Title: getLuckySheetTplSettings
	 * @Description: 获取luckysheet模板内容
	 * @param reportTpl
	 * @return 
	 * @see com.caiyang.api.reporttpl.IReportTplService#getLuckySheetTplSettings(com.caiyang.entity.reporttpl.ReportTpl) 
	 * @author caiyang
	 * @throws IOException 
	 * @date 2022-02-01 04:38:42 
	 */
	@Override
	@Transactional
	public ResSheetsSettingsDto getLuckySheetTplSettings(ReportTplDto reportTplDto,UserInfoDto userInfoDto) throws Exception {
		ResSheetsSettingsDto settings = new ResSheetsSettingsDto();
		List<ResLuckySheetTplSettingsDto> list = new ArrayList<>();
		ReportTpl reportTpl = this.getById(reportTplDto.getId());
		if(reportTpl == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"报表模板"}));
		}
		Object redisCache = null;
		if(reportTplDto.isAllowUpdate()) {
			redisCache = redisUtil.get(RedisPrefixEnum.REPORTTPLTEMPCACHE.getCode()+"designMode-"+reportTpl.getId());
		}
		if(redisCache != null)
		{
			settings = JSON.parseObject(redisCache.toString(), ResSheetsSettingsDto.class);
		}else {
			settings.setIsParamMerge(reportTpl.getIsParamMerge());
			settings.setTplName(reportTpl.getTplName());
			if(reportTplDto.isAllowUpdate()) {
				redisUtil.set(RedisPrefixEnum.REPORTTPLTEMPCACHE.getCode()+"designMode-"+reportTpl.getId(),JSON.toJSONString(settings),Constants.REPORT_TPL_CACHE_TIME);
			}
		}
		//获取所有有权限的sheet
		List<ReportTplSheet> sheets = null;
//		if(userInfoDto.getIsAdmin().intValue() == YesNoEnum.YES.getCode() || reportTpl.getViewAuth().intValue() == 1)
//		{//超级管理员
//			QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
//			sheetQueryWrapper.eq("tpl_id", reportTpl.getId());
//			sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//			sheets = this.iReportTplSheetService.list(sheetQueryWrapper);
//		}else {
//			QueryWrapper<SysRoleSheet> roleSheetQueryWrapper = new QueryWrapper<>();
//			roleSheetQueryWrapper.eq("report_id", reportTpl.getId());
//			roleSheetQueryWrapper.eq("role_id", userInfoDto.getRoleId());
//			roleSheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//			List<SysRoleSheet> roleSheets = this.iSysRoleSheetService.list(roleSheetQueryWrapper);
//			if(ListUtil.isEmpty(roleSheets))
//			{
//				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.noauth.design.report"));
//			}
//			List<Long> sheetIds = new ArrayList<>();
//			for (int i = 0; i < roleSheets.size(); i++) {
//				sheetIds.add(roleSheets.get(i).getSheetId());
//			}
//			QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
//			sheetQueryWrapper.in("id", sheetIds);
//			sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//			sheets = this.iReportTplSheetService.list(sheetQueryWrapper);
//		}
		//获取所有sheet
		QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
		sheetQueryWrapper.eq("tpl_id", reportTpl.getId());
		sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		sheets = this.iReportTplSheetService.list(sheetQueryWrapper);
		boolean isCreator = false;
		if(this.authenticEnabale) {
			if(StringUtil.isNotEmpty(userInfoDto.getUserName()) && userInfoDto.getUserName().equals(this.thirdPartyType)) {
				isCreator = true;
			}else {
				isCreator = reportTpl.getCreator().longValue() == userInfoDto.getUserId().longValue();
			}
		}else {
			isCreator = true;
		}
		if(!ListUtil.isEmpty(sheets)) {
			this.processSheetCells(sheets, reportTpl, list,isCreator,userInfoDto.getUserId(),settings,reportTplDto.isAllowUpdate());
		}else {
			//上一个版本的数据，创建一个sheet并更新cell表中的sheetid字段，做到兼容
			ReportTplSheet reportTplSheet = new ReportTplSheet();
			reportTplSheet.setTplId(reportTpl.getId());
			reportTplSheet.setSheetName("sheet1");
			reportTplSheet.setConfig(reportTpl.getConfig());
			reportTplSheet.setFrozen(reportTpl.getFrozen());
			reportTplSheet.setImages(reportTpl.getImages());
			reportTplSheet.setSheetIndex("Sheet"+UUIDUtil.getUUID());
			reportTplSheet.setCalcChain(reportTpl.getCalcChain());
			reportTplSheet.setSheetOrder(0);
			this.iReportTplSheetService.save(reportTplSheet);
			UpdateWrapper<LuckysheetReportCell> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("tpl_id", reportTpl.getId());
			updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			LuckysheetReportCell luckysheetReportCell = new LuckysheetReportCell();
			luckysheetReportCell.setSheetId(reportTplSheet.getId());
			this.iLuckysheetReportCellService.update(luckysheetReportCell, updateWrapper);
			sheets = new ArrayList<>();
			sheets.add(reportTplSheet);
			this.processSheetCells(sheets, reportTpl, list,isCreator,userInfoDto.getUserId(),settings,reportTplDto.isAllowUpdate());
		}
		settings.setCreator(isCreator);
		settings.setSettings(list);
		if(reportTpl.getCreator() != null)
		{
			SysUser sysUser = this.iSysUserService.getById(reportTpl.getCreator());
			if(sysUser != null)
			{
				settings.setCreatorName(sysUser.getUserName());
			}
		}
		if(StringUtil.isNotEmpty(userInfoDto.getUserName()) && userInfoDto.getUserName().equals(this.thirdPartyType)) {
			//是否是ifram嵌入的第三方调用
			settings.setIsThirdParty(YesNoEnum.YES.getCode());
		}
		settings.setTplType(reportTpl.getTplType());
		return settings;
	}

	private void processSheetCells(List<ReportTplSheet> sheets,ReportTpl reportTpl,List<ResLuckySheetTplSettingsDto> list,
			boolean isCreator,Long userId,ResSheetsSettingsDto settings,boolean allowUpdate) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> databaseSheets = new ArrayList<>();//数据库中已经保存的sheet
		for (int t = 0; t < sheets.size(); t++) {
			if(StringUtil.isNullOrEmpty(sheets.get(t).getSheetIndex()))
			{
				sheets.get(t).setSheetIndex("Sheet"+UUIDUtil.getUUID());
			}
			String key = RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+"designMode-" +reportTpl.getId()+"-"+ sheets.get(t).getSheetIndex();
			databaseSheets.add(key);
			Object redisCache = null;
			if(allowUpdate) {
				redisCache = redisUtil.get(key);
			}
			ResLuckySheetTplSettingsDto result = new ResLuckySheetTplSettingsDto();
			if(redisCache == null)
			{
				QueryWrapper<LuckysheetReportCell> queryWrapper = new QueryWrapper<LuckysheetReportCell>();
				queryWrapper.eq("tpl_id", reportTpl.getId());
				queryWrapper.eq("sheet_id", sheets.get(t).getId());
				queryWrapper.eq("is_chart_cell", YesNoEnum.NO.getCode());
				queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<LuckysheetReportCell> luckysheetReportCells = this.iLuckysheetReportCellService.list(queryWrapper);
				Map<String, Map<String, Object>> mergeMap = new HashMap<String, Map<String,Object>>();//合并单元格
				if(!ListUtil.isEmpty(luckysheetReportCells))
				{
					List<Map<String, Object>> cellDatas = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> blockData = new ArrayList<Map<String,Object>>();
					Map<String, Object> cellFormats = new HashMap<>();
					Map<String, Map<String, Object>> hyperlinks = new HashMap<String, Map<String, Object>>();
					Map<String, JSONObject> extraCustomCellConfigs = new HashMap<String, JSONObject>();
					Map<String, Object> merge = null;
					for (LuckysheetReportCell luckysheetReportCell : luckysheetReportCells) {
						if(CellValueTypeEnum.BLOCK.getCode().intValue() == luckysheetReportCell.getCellValueType().intValue())
						{
							Map<String, Object> blockMap = new HashMap<String, Object>();
							blockMap.put("startCell",SheetUtil.excelColIndexToStr(luckysheetReportCell.getCoordsy()+1)+String.valueOf(luckysheetReportCell.getCoordsx()+1));
							blockMap.put("endCell",SheetUtil.excelColIndexToStr(luckysheetReportCell.getCoordsy()+luckysheetReportCell.getColSpan())+String.valueOf(luckysheetReportCell.getCoordsx()+luckysheetReportCell.getRowSpan()));
							blockMap.put("aggregateType",luckysheetReportCell.getAggregateType());
							blockMap.put("groupProperty",luckysheetReportCell.getGroupProperty());
							blockMap.put("hloopCount",luckysheetReportCell.getHloopCount()+"");
							blockMap.put("hloopEmptyCount",luckysheetReportCell.getHloopEmptyCount()+"");
							blockMap.put("vloopEmptyCount",luckysheetReportCell.getVloopEmptyCount()+"");
							blockMap.put("subBlockRange",luckysheetReportCell.getSubBlockRange());
							blockData.add(blockMap);
							QueryWrapper<LuckysheetReportBlockCell> blockCellQueryWrapper = new QueryWrapper<LuckysheetReportBlockCell>();
							blockCellQueryWrapper.eq("report_cell_id", luckysheetReportCell.getId());
							blockCellQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
							List<LuckysheetReportBlockCell> blockCells = this.iLuckysheetReportBlockCellService.list(blockCellQueryWrapper);
							if(!ListUtil.isEmpty(blockCells))
							{
								for (LuckysheetReportBlockCell luckysheetReportBlockCell : blockCells) {
									JSONObject cellData = objectMapper.readValue(luckysheetReportBlockCell.getCellData(), JSONObject.class);
									cellDatas.add(cellData);
									JSONObject cellFormat = this.getCellFormat(cellData);
									if(luckysheetReportBlockCell.getCellValueType().intValue() == 2) {
										cellFormats.put(cellData.getInteger("r")+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+cellData.getInteger("c"), cellFormat);
									}
									JSONObject extraCustomCellConfig = new JSONObject();
									extraCustomCellConfig.put(LuckySheetPropsEnum.CELLEXTEND.getCode(), CellExtendEnum.NOEXTEND.getCode());
									extraCustomCellConfig.put(LuckySheetPropsEnum.AGGREGATETYPE.getCode(), AggregateTypeEnum.LIST.getCode());
									extraCustomCellConfig.put(LuckySheetPropsEnum.CELLFUNCTION.getCode(), FunctionTypeEnum.SUM.getCode());
									extraCustomCellConfig.put(LuckySheetPropsEnum.DIGIT.getCode(), 2);
									extraCustomCellConfig.put(LuckySheetPropsEnum.GROUPSUMMARYDEPENDENCYR.getCode(), "");
									extraCustomCellConfig.put(LuckySheetPropsEnum.GROUPSUMMARYDEPENDENCYC.getCode(), "");
									extraCustomCellConfig.put(LuckySheetPropsEnum.ISGROUPMERGE.getCode(), YesNoEnum.YES.getCode());
									extraCustomCellConfig.put(LuckySheetPropsEnum.DATAFROM.getCode(), DataFromEnum.ORIGINAL.getCode());
									extraCustomCellConfigs.put(String.valueOf(luckysheetReportBlockCell.getCoordsx())+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()
										+String.valueOf(luckysheetReportBlockCell.getCoordsy()), extraCustomCellConfig);
									if(YesNoEnum.YES.getCode().intValue() == luckysheetReportBlockCell.getIsLink().intValue())
									{//超链接
										JSONObject hyperlink = JSONObject.parseObject(luckysheetReportBlockCell.getLinkConfig());
										hyperlinks.put(String.valueOf(luckysheetReportBlockCell.getCoordsx())+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()
										+String.valueOf(luckysheetReportBlockCell.getCoordsy()), hyperlink);
									}
									if(YesNoEnum.YES.getCode().intValue() == luckysheetReportBlockCell.getIsMerge().intValue())
									{
										merge = new HashMap<String, Object>();
										merge.put(LuckySheetPropsEnum.R.getCode(), luckysheetReportBlockCell.getCoordsx());
										merge.put(LuckySheetPropsEnum.C.getCode(), luckysheetReportBlockCell.getCoordsy());
										merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckysheetReportBlockCell.getRowSpan());
										merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckysheetReportBlockCell.getColSpan());
										mergeMap.put(String.valueOf(luckysheetReportBlockCell.getCoordsx())+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(luckysheetReportBlockCell.getCoordsy()), merge);
										for (int i = 0; i < luckysheetReportBlockCell.getRowSpan(); i++) {
											for (int j = 0; j < luckysheetReportBlockCell.getColSpan(); j++) {
												if(i != 0 || j != 0)
												{
													Map<String, Object> mc = new HashMap<String, Object>();
													mc.put(LuckySheetPropsEnum.R.getCode(), luckysheetReportBlockCell.getCoordsx());
													mc.put(LuckySheetPropsEnum.C.getCode(), luckysheetReportBlockCell.getCoordsy());
													Map<String, Object> cellValue = new HashMap<String, Object>();
													cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
													Map<String, Object> mergeCellData = new HashMap<String, Object>();
													mergeCellData.put(LuckySheetPropsEnum.R.getCode(), luckysheetReportBlockCell.getCoordsx()+i);
													mergeCellData.put(LuckySheetPropsEnum.C.getCode(), luckysheetReportBlockCell.getCoordsy()+j);
													mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
													cellDatas.add(mergeCellData);
												}
											}
										}
										
									}
								}
							}
		 				}else {
		 					JSONObject cellData = objectMapper.readValue(luckysheetReportCell.getCellData(), JSONObject.class);
							cellDatas.add(cellData);
							JSONObject cellFormat = this.getCellFormat(cellData);
							if(luckysheetReportCell.getCellValueType().intValue() == 2) {
								cellFormats.put(cellData.getInteger("r")+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+cellData.getInteger("c"), cellFormat);
							}
							JSONObject extraCustomCellConfig = new JSONObject();
							extraCustomCellConfig.put(LuckySheetPropsEnum.CELLEXTEND.getCode(), luckysheetReportCell.getCellExtend().intValue());
							extraCustomCellConfig.put(LuckySheetPropsEnum.AGGREGATETYPE.getCode(), luckysheetReportCell.getAggregateType());
							extraCustomCellConfig.put(LuckySheetPropsEnum.CELLFUNCTION.getCode(), luckysheetReportCell.getCellFunction());
							extraCustomCellConfig.put(LuckySheetPropsEnum.DIGIT.getCode(), luckysheetReportCell.getDigit());
							extraCustomCellConfig.put(LuckySheetPropsEnum.GROUPSUMMARYDEPENDENCYR.getCode(), luckysheetReportCell.getGroupSummaryDependencyR());
							extraCustomCellConfig.put(LuckySheetPropsEnum.GROUPSUMMARYDEPENDENCYC.getCode(), luckysheetReportCell.getGroupSummaryDependencyC());
							extraCustomCellConfig.put(LuckySheetPropsEnum.ISGROUPMERGE.getCode(), luckysheetReportCell.getIsGroupMerge());
							extraCustomCellConfig.put(LuckySheetPropsEnum.DATAFROM.getCode(), luckysheetReportCell.getDataFrom());
							//2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//							extraCustomCellConfig.put(LuckySheetPropsEnum.WARNING.getCode(), luckysheetReportCell.getWarning());
//							extraCustomCellConfig.put(LuckySheetPropsEnum.WARNINGRULES.getCode(), luckysheetReportCell.getWarningRules());
//							extraCustomCellConfig.put(LuckySheetPropsEnum.WARNINGCOLOR.getCode(), luckysheetReportCell.getWarningColor());
//							extraCustomCellConfig.put(LuckySheetPropsEnum.THRESHOLD.getCode(), luckysheetReportCell.getThreshold());
//							extraCustomCellConfig.put(LuckySheetPropsEnum.WARNINGCONTENT.getCode(), luckysheetReportCell.getWarningContent());
							extraCustomCellConfigs.put(String.valueOf(luckysheetReportCell.getCoordsx())+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()
								+String.valueOf(luckysheetReportCell.getCoordsy()), extraCustomCellConfig);
							extraCustomCellConfig.put(LuckySheetPropsEnum.ISDICT.getCode(), luckysheetReportCell.getIsDict());
							extraCustomCellConfig.put(LuckySheetPropsEnum.DATASOURCEID.getCode(), luckysheetReportCell.getDatasourceId());
							extraCustomCellConfig.put(LuckySheetPropsEnum.DICTTYPE.getCode(), luckysheetReportCell.getDictType());
							extraCustomCellConfig.put(LuckySheetPropsEnum.GROUPPROPERTY.getCode(), luckysheetReportCell.getGroupProperty());
							extraCustomCellConfig.put(LuckySheetPropsEnum.CELLCONDITIONS.getCode(), objectMapper.readValue(StringUtil.isNullOrEmpty(luckysheetReportCell.getCellConditions())?"[]":luckysheetReportCell.getCellConditions(), JSONArray.class));
							extraCustomCellConfig.put(LuckySheetPropsEnum.SERIESNAME.getCode(), luckysheetReportCell.getSeriesName());
							extraCustomCellConfig.put(LuckySheetPropsEnum.ISDRILL.getCode(), luckysheetReportCell.getIsDrill());
							extraCustomCellConfig.put(LuckySheetPropsEnum.UNITTRANSFER.getCode(), luckysheetReportCell.getUnitTransfer());
							extraCustomCellConfig.put(LuckySheetPropsEnum.TRANSFERTYPE.getCode(), luckysheetReportCell.getTransferType());
							extraCustomCellConfig.put(LuckySheetPropsEnum.MULTIPLE.getCode(), luckysheetReportCell.getMultiple());
							extraCustomCellConfig.put(LuckySheetPropsEnum.CELLHIDDENCONDITIONS.getCode(),  objectMapper.readValue(StringUtil.isNullOrEmpty(luckysheetReportCell.getHiddenConditions())?"[]":luckysheetReportCell.getHiddenConditions(), JSONArray.class));
							extraCustomCellConfig.put(LuckySheetPropsEnum.CELLHIDDENTYPE.getCode(), luckysheetReportCell.getHiddenType());
							extraCustomCellConfig.put(LuckySheetPropsEnum.FILTERTYPE.getCode(), luckysheetReportCell.getFilterType());
							extraCustomCellConfig.put(LuckySheetPropsEnum.CELLFILLTYPE.getCode(), luckysheetReportCell.getCellFillType());
							extraCustomCellConfig.put("formsAttrs", objectMapper.readValue(StringUtil.isNullOrEmpty(luckysheetReportCell.getFormsAttrs())?"{}":luckysheetReportCell.getFormsAttrs(), JSONObject.class));
							extraCustomCellConfig.put("isObject", luckysheetReportCell.getIsObject());
							extraCustomCellConfig.put("dataType", luckysheetReportCell.getDataType());
							extraCustomCellConfig.put("dataAttr", luckysheetReportCell.getDataAttr());
							extraCustomCellConfig.put("subExtend", luckysheetReportCell.getSubExtend());
							extraCustomCellConfig.put("priortyMoveDirection", luckysheetReportCell.getPriortyMoveDirection());
							extraCustomCellConfig.put("sourceType", luckysheetReportCell.getSourceType());
							extraCustomCellConfig.put("dictContent", luckysheetReportCell.getDictContent());
							extraCustomCellConfig.put("compareAttr1", luckysheetReportCell.getCompareAttr1());
							extraCustomCellConfig.put("compareAttr2", luckysheetReportCell.getCompareAttr2());
							extraCustomCellConfig.put("isDump", luckysheetReportCell.getIsDump());
							extraCustomCellConfig.put("dumpAttr", luckysheetReportCell.getDumpAttr());
							extraCustomCellConfig.put("keepEmptyCell", luckysheetReportCell.getKeepEmptyCell());
							if(luckysheetReportCell.getIsDrill())
							{
								extraCustomCellConfig.put(LuckySheetPropsEnum.DRILLID.getCode(), luckysheetReportCell.getDrillId());
								extraCustomCellConfig.put(LuckySheetPropsEnum.DRILLATTRS.getCode(), luckysheetReportCell.getDrillAttrs());
								ReportTpl drillTpl = this.getById(luckysheetReportCell.getDrillId());
								if(drillTpl != null)
								{
									extraCustomCellConfig.put("drillTplName", drillTpl.getTplName());
								}
							}
							if(YesNoEnum.YES.getCode().intValue() == luckysheetReportCell.getIsLink().intValue())
							{//超链接
								JSONObject hyperlink = JSONObject.parseObject(luckysheetReportCell.getLinkConfig());
								hyperlinks.put(String.valueOf(luckysheetReportCell.getCoordsx())+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()
								+String.valueOf(luckysheetReportCell.getCoordsy()), hyperlink);
							}
							if(YesNoEnum.YES.getCode().intValue() == luckysheetReportCell.getIsMerge().intValue())
							{
								merge = new HashMap<String, Object>();
								merge.put(LuckySheetPropsEnum.R.getCode(), luckysheetReportCell.getCoordsx());
								merge.put(LuckySheetPropsEnum.C.getCode(), luckysheetReportCell.getCoordsy());
								merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckysheetReportCell.getRowSpan());
								merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckysheetReportCell.getColSpan());
								mergeMap.put(String.valueOf(luckysheetReportCell.getCoordsx())+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(luckysheetReportCell.getCoordsy()), merge);
								for (int i = 0; i < luckysheetReportCell.getRowSpan(); i++) {
									for (int j = 0; j < luckysheetReportCell.getColSpan(); j++) {
										if(i != 0 || j != 0)
										{
											Map<String, Object> mc = new HashMap<String, Object>();
											mc.put(LuckySheetPropsEnum.R.getCode(), luckysheetReportCell.getCoordsx());
											mc.put(LuckySheetPropsEnum.C.getCode(), luckysheetReportCell.getCoordsy());
											Map<String, Object> cellValue = new HashMap<String, Object>();
											cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
											Map<String, Object> mergeCellData = new HashMap<String, Object>();
											mergeCellData.put(LuckySheetPropsEnum.R.getCode(), luckysheetReportCell.getCoordsx()+i);
											mergeCellData.put(LuckySheetPropsEnum.C.getCode(), luckysheetReportCell.getCoordsy()+j);
											mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
											cellDatas.add(mergeCellData);
										}
									}
								}
							}
							extraCustomCellConfig.put("isSubtotal", luckysheetReportCell.getIsSubtotal());
							if(StringUtil.isNullOrEmpty(luckysheetReportCell.getSubtotalCells()))
							{
								extraCustomCellConfig.put("subTotalCells", new JSONArray());
							}else {
								extraCustomCellConfig.put("subTotalCells", objectMapper.readValue(luckysheetReportCell.getSubtotalCells(), JSONArray.class));
							}
							if(luckysheetReportCell.getIsSubtotalCalc())
							{
								extraCustomCellConfig.put("subTotalCalc", objectMapper.readValue(luckysheetReportCell.getSubtotalCalc(), JSONArray.class));
							}else {
								extraCustomCellConfig.put("subTotalCalc", new JSONArray());
							}
							if(StringUtil.isNullOrEmpty(luckysheetReportCell.getSubtotalAttrs()))
							{
								extraCustomCellConfig.put("subTotalAttrs", new JSONArray());
							}else {
								extraCustomCellConfig.put("subTotalAttrs", objectMapper.readValue(luckysheetReportCell.getSubtotalAttrs(), JSONArray.class));
							}
						}
						
					}
					result.setBlockData(blockData);
					result.setCellDatas(cellDatas);
					result.setHyperlinks(hyperlinks);
					result.setExtraCustomCellConfigs(extraCustomCellConfigs);
					result.setCellFormats(cellFormats);
				}
				QueryWrapper<ReportTplSheetChart> chartQueryWrapper = new QueryWrapper<>();
				chartQueryWrapper.eq("tpl_id", reportTpl.getId());
				chartQueryWrapper.eq("sheet_id", sheets.get(t).getId());
				chartQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<ReportTplSheetChart> charts = this.iReportTplSheetChartService.list(chartQueryWrapper);
				if(!ListUtil.isEmpty(charts))
				{
					JSONArray chartXaxisData = new JSONArray();
					JSONObject jsonObject = null;
					for (int i = 0; i < charts.size(); i++) {
						jsonObject = new JSONObject();
						jsonObject.put("chartId", charts.get(i).getChartId());
						jsonObject.put("title", charts.get(i).getTitle());
						jsonObject.put("dataType", charts.get(i).getDataType());
						jsonObject.put("datasetId", charts.get(i).getDatasetId());
						jsonObject.put("datasetName", charts.get(i).getDatasetName());
						jsonObject.put("attr", charts.get(i).getAttr());
						jsonObject.put("xAxisDatas", charts.get(i).getXaxisDatas());
						chartXaxisData.add(jsonObject);
					}
					result.setChartXaxisData(chartXaxisData);
				}
				if(StringUtil.isNotEmpty(sheets.get(t).getConfig()))
				{
					result.setConfig(objectMapper.readValue(sheets.get(t).getConfig(), Map.class));
				}else {
					result.setConfig(new HashMap<String, Object>());
				}
				result.getConfig().put(LuckySheetPropsEnum.MERGECONFIG.getCode(), mergeMap);
				if(StringUtil.isNotEmpty(sheets.get(t).getFrozen()))
				{
					result.setFrozen(objectMapper.readValue(sheets.get(t).getFrozen(), Map.class));
				}else {
					result.setFrozen(new HashMap<String, Object>());
				}
				if(StringUtil.isNotEmpty(sheets.get(t).getImages()))
				{
					result.setImages(objectMapper.readValue(sheets.get(t).getImages(), Object.class));
				}else {
					result.setImages(null);
				}
				result.setSheetIndex(sheets.get(t).getSheetIndex());
				if(StringUtil.isNotEmpty(sheets.get(t).getCalcChain()))
				{
					result.setCalcChain(JSONArray.parseArray(sheets.get(t).getCalcChain()));
				}
				result.setSheetName(sheets.get(t).getSheetName());
				result.setSheetOrder(sheets.get(t).getSheetOrder());
				if(StringUtil.isNullOrEmpty(sheets.get(t).getAlternateformatSave()))
				{
					result.setLuckysheetAlternateformatSave(new JSONArray());
				}else {
					result.setLuckysheetAlternateformatSave(JSONArray.parseArray(sheets.get(t).getAlternateformatSave()));
				}
				if(StringUtil.isNullOrEmpty(sheets.get(t).getConditionformatSave()))
				{
					result.setLuckysheetConditionformatSave(new JSONArray());
				}else {
					result.setLuckysheetConditionformatSave(JSONArray.parseArray(sheets.get(t).getConditionformatSave()));
				}
				if(StringUtil.isNullOrEmpty(sheets.get(t).getChart()))
				{
					result.setChart(new JSONArray());
				}else {
					result.setChart(JSONArray.parseArray(sheets.get(t).getChart()));
				}
				if(StringUtil.isNullOrEmpty(sheets.get(t).getDataVerification()))
				{
					result.setDataVerification(new JSONObject());
				}else {
					result.setDataVerification(objectMapper.readValue(sheets.get(t).getDataVerification(), JSONObject.class));
				}
				if(StringUtil.isNullOrEmpty(sheets.get(t).getPageDivider()))
				{
					result.setPageDivider(new JSONArray());
				}else {
					result.setPageDivider(JSON.parseArray(sheets.get(t).getPageDivider()));
				}
				QueryWrapper<ReportFormsDatasource> datasourceQueryWrapper = new QueryWrapper<>();
				datasourceQueryWrapper.eq("tpl_id", reportTpl.getId());
				datasourceQueryWrapper.eq("sheet_id", sheets.get(t).getId());
				datasourceQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<ReportFormsDatasource> reportFormsDatasources = this.iReportFormsDatasourceService.list(datasourceQueryWrapper);
				JSONArray datasourceConfigs = new JSONArray();
				if(!ListUtil.isEmpty(reportFormsDatasources))
				{
					for (int i = 0; i < reportFormsDatasources.size(); i++) {
						JSONObject datasourceConfig = new JSONObject();
						QueryWrapper<ReportFormsDatasourceAttrs> attrsQueryWrapper = new QueryWrapper<>();
						attrsQueryWrapper.eq("report_forms_datasource_id", reportFormsDatasources.get(i).getId());
						attrsQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
						List<ReportFormsDatasourceAttrs> formsDatasourceAttrs = this.iReportFormsDatasourceAttrsService.list(attrsQueryWrapper);
						datasourceConfig.put("name", reportFormsDatasources.get(i).getName());
						datasourceConfig.put("datasourceId", String.valueOf(reportFormsDatasources.get(i).getDatasourceId()));
						datasourceConfig.put("table", reportFormsDatasources.get(i).getTableName());
						datasourceConfig.put("isMain", reportFormsDatasources.get(i).getIsMain()!=null&&reportFormsDatasources.get(i).getIsMain().intValue() == 1?true:false);
						JSONArray tableDatas = new JSONArray();
						JSONArray keys = new JSONArray();
						JSONArray autoFillAttrs = new JSONArray();
						JSONArray deleteTypes = new JSONArray();
						JSONArray mainAttrs = new JSONArray();
						if(!ListUtil.isEmpty(formsDatasourceAttrs))
						{
							for (int j = 0; j < formsDatasourceAttrs.size();j++) {
								JSONObject jsonObject = new JSONObject();
								if(formsDatasourceAttrs.get(j).getType().intValue() == 1) {
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("cellCoords", formsDatasourceAttrs.get(j).getCellCoords());
									tableDatas.add(jsonObject);
								}else if(formsDatasourceAttrs.get(j).getType().intValue() == 2){
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("idType", formsDatasourceAttrs.get(j).getIdType());
									keys.add(jsonObject);
								}else if(formsDatasourceAttrs.get(j).getType().intValue() == 3){
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("fillType", formsDatasourceAttrs.get(j).getFillType());
									jsonObject.put("fillStrategy", formsDatasourceAttrs.get(j).getFillStrategy());
									jsonObject.put("fillValue", formsDatasourceAttrs.get(j).getFillValue());
									autoFillAttrs.add(jsonObject);
								}else if(formsDatasourceAttrs.get(j).getType().intValue() == 4){
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("deleteType", formsDatasourceAttrs.get(j).getDeleteType());
									jsonObject.put("deleteValue", formsDatasourceAttrs.get(j).getDeleteValue());
									deleteTypes.add(jsonObject);
								}else if(formsDatasourceAttrs.get(j).getType().intValue() == 5){
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("mainColumn", formsDatasourceAttrs.get(j).getMainColumn());
									jsonObject.put("mainName", formsDatasourceAttrs.get(j).getMainName());
									jsonObject.put("mainDatasourceId", formsDatasourceAttrs.get(j).getMainDatasourceId());
									jsonObject.put("mainTable", formsDatasourceAttrs.get(j).getMainTable());
									mainAttrs.add(jsonObject);
								}
							}
						}
						datasourceConfig.put("tableDatas", tableDatas);
						datasourceConfig.put("keys", keys);
						datasourceConfig.put("autoFillAttrs", autoFillAttrs);
						datasourceConfig.put("deleteTypes", deleteTypes);
						datasourceConfig.put("mainAttrs", mainAttrs);
						datasourceConfigs.add(datasourceConfig);
					}
				}
				result.setDatasourceConfig(datasourceConfigs);
				//获取打印设置
				QueryWrapper<ReportSheetPdfPrintSetting> settingQueryWrapper = new QueryWrapper<>();
				settingQueryWrapper.eq("tpl_sheet_id", sheets.get(t).getId());
				settingQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				ReportSheetPdfPrintSetting setting = this.iReportSheetPdfPrintSettingService.getOne(settingQueryWrapper);
				result.setReportSheetPdfPrintSetting(setting);
				if(allowUpdate) {
					redisUtil.set(key, JSON.toJSONString(result),Constants.REPORT_TPL_CACHE_TIME);
				}
			}else {
				result = JSON.parseObject(redisCache.toString(), ResLuckySheetTplSettingsDto.class);
			}
			list.add(result);
		}
		//获取权限信息
		JSONObject sheetRangeAuth = new JSONObject();
		if(isCreator) {
			QueryWrapper<ReportRangeAuth> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("tpl_id", sheets.get(0).getTplId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<ReportRangeAuth> reportRangeAuths = this.iReportRangeAuthService.list(queryWrapper);
			if(ListUtil.isNotEmpty(reportRangeAuths))
			{
				List<Long> authIds = new ArrayList<>();
				for (int i = 0; i < reportRangeAuths.size(); i++) {
					authIds.add(reportRangeAuths.get(i).getId());
				}
				//获取权限对应的用户信息
				QueryWrapper<ReportRangeAuthUser> rangeAuthUserQueryWrapper = new QueryWrapper<>();
				rangeAuthUserQueryWrapper.eq("tpl_id", sheets.get(0).getTplId());
				rangeAuthUserQueryWrapper.in("range_auth_id", authIds);
				rangeAuthUserQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<ReportRangeAuthUser> reportRangeAuthUsers = this.iReportRangeAuthUserService.list(rangeAuthUserQueryWrapper);
				Map<Long, List<ReportRangeAuthUser>> groupDatas = null;
				if(ListUtil.isNotEmpty(reportRangeAuthUsers))
				{
					groupDatas = reportRangeAuthUsers.stream().collect(Collectors.groupingBy(ReportRangeAuthUser::getRangeAuthId));
				}
				for (int i = 0; i < reportRangeAuths.size(); i++) {
					JSONObject rangeAxis = new JSONObject();
					rangeAxis.put("rangeAxis", reportRangeAuths.get(i).getRangeTxt());
					JSONObject range = new JSONObject();
					range.put("row", JSON.parseArray(reportRangeAuths.get(i).getRowsNo()));
					range.put("column", JSON.parseArray(reportRangeAuths.get(i).getColsNo()));
					rangeAxis.put("range", range);
					rangeAxis.put("sheetIndex", reportRangeAuths.get(i).getSheetIndex());
					List<ReportRangeAuthUser> datas = groupDatas.get(reportRangeAuths.get(i).getId());
					if(ListUtil.isNotEmpty(datas))
					{
						JSONArray userIds = new JSONArray();
						for (int j = 0; j < datas.size(); j++) {
							userIds.add(datas.get(j).getUserId());
						}
						rangeAxis.put("userIds", userIds);
					}
					if(!sheetRangeAuth.containsKey(reportRangeAuths.get(i).getSheetIndex())) {
						sheetRangeAuth.put(reportRangeAuths.get(i).getSheetIndex(), new JSONObject());
					}
					sheetRangeAuth.getJSONObject(reportRangeAuths.get(i).getSheetIndex()).put(reportRangeAuths.get(i).getRangeTxt(), rangeAxis);
				}
			}
		}else {
			//获取用户对应的权限
			QueryWrapper<ReportRangeAuthUser> rangeAuthUserQueryWrapper = new QueryWrapper<>();
			rangeAuthUserQueryWrapper.eq("tpl_id", sheets.get(0).getTplId());
			rangeAuthUserQueryWrapper.eq("user_id", userId);
			rangeAuthUserQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<ReportRangeAuthUser> reportRangeAuthUsers = this.iReportRangeAuthUserService.list(rangeAuthUserQueryWrapper);
			if(ListUtil.isNotEmpty(reportRangeAuthUsers))
			{
				List<Long> authRangeIds = new ArrayList<>();
				for (int i = 0; i < reportRangeAuthUsers.size(); i++) {
					authRangeIds.add(reportRangeAuthUsers.get(i).getRangeAuthId());
				}
				//获取对应的权限操作范围
				QueryWrapper<ReportRangeAuth> queryWrapper = new QueryWrapper<>();
				queryWrapper.in("id", authRangeIds);
				List<ReportRangeAuth> reportRangeAuths = this.iReportRangeAuthService.list(queryWrapper);
				if(ListUtil.isNotEmpty(reportRangeAuths))
				{
					for (int i = 0; i < reportRangeAuths.size(); i++) {
						JSONObject rangeAxis = new JSONObject();
						rangeAxis.put("rangeAxis", reportRangeAuths.get(i).getRangeTxt());
						JSONObject range = new JSONObject();
						range.put("row", JSON.parseArray(reportRangeAuths.get(i).getRowsNo()));
						range.put("column", JSON.parseArray(reportRangeAuths.get(i).getColsNo()));
						rangeAxis.put("range", range);
						rangeAxis.put("sheetIndex", reportRangeAuths.get(i).getSheetIndex());
						if(!sheetRangeAuth.containsKey(reportRangeAuths.get(i).getSheetIndex())) {
							sheetRangeAuth.put(reportRangeAuths.get(i).getSheetIndex(), new JSONObject());
						}
						sheetRangeAuth.getJSONObject(reportRangeAuths.get(i).getSheetIndex()).put(reportRangeAuths.get(i).getRangeTxt(), rangeAxis);
					}
				}
			}
		}
		List<String> keys = redisUtil.getKeys(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+"designMode-" +reportTpl.getId());
		if(!ListUtil.isEmpty(keys) && allowUpdate)
		{
			for (int i = 0; i < keys.size(); i++) {
				if(!databaseSheets.contains(keys.get(i)))
				{
					Object redisCache = redisUtil.get(keys.get(i));
					ResLuckySheetTplSettingsDto result = JSON.parseObject(redisCache.toString(), ResLuckySheetTplSettingsDto.class);
					list.add(result);
				}
			}
		}
		settings.setSheetRangeAuth(sheetRangeAuth);
	}

	/**  
	 * @Title: previewLuckysheetReportData
	 * @Description: luckysheet预览报表
	 * @param mesGenerateReportDto
	 * @return 
	 * @see com.caiyang.api.reporttpl.IReportTplService#previewLuckysheetReportData(com.caiyang.dto.reporttpl.MesGenerateReportDto) 
	 * @author caiyang
	 * @throws JSQLParserException 
	 * @date 2022-02-02 08:37:18 
	 */
	@Override
	public ResPreviewData previewLuckysheetReportData(MesGenerateReportDto mesGenerateReportDto,UserInfoDto userInfoDto) throws Exception {
		ReportTpl reportTpl = this.getById(mesGenerateReportDto.getTplId());
		if (reportTpl == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"报表模板"}));
		}
		ResPreviewData result = null;
//		if(1 == reportTpl.getTplType().intValue())
//		{//展示报表
			result = this.generateLuckySheetReportData(mesGenerateReportDto,true,userInfoDto,reportTpl,false);
//		}else {
//			//填报报表
//			result = this.iReportTplFormsService.previewLuckysheetReportFormsData(mesGenerateReportDto,userInfoDto,reportTpl,true);
//		}
		result.setCoeditFlag(reportTpl.getCoeditFlag());
		result.setTplType(reportTpl.getTplType());
		result.setRefreshPage(reportTpl.getRefreshPage());
		result.setShowReportSql(showReportSql);
		result.setIsRefresh(reportTpl.getIsRefresh());
		result.setRefreshTime(reportTpl.getRefreshTime());
		return result;
	}
	
	/**  
	 * @Title: generateLuckySheetReportData
	 * @Description: 生成luckysheet报表数据
	 * @param mesGenerateReportDto
	 * @param isPagination 是否分页
	 * @return
	 * @throws JSQLParserException
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2022-02-09 05:33:57 
	 */ 
	private ResPreviewData generateLuckySheetReportData(MesGenerateReportDto mesGenerateReportDto,boolean isPagination
			,UserInfoDto userInfoDto,ReportTpl reportTpl,boolean isExport) throws Exception{
		ResPreviewData resPreviewData = new ResPreviewData();
		List<ResLuckySheetDataDto> sheetDatas = new ArrayList<>();
//		ReportTpl reportTpl = this.getById(mesGenerateReportDto.getTplId());
//		if (reportTpl == null) {
//			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"报表模板"}));
//		}
		if(reportTpl.getTplType() == 2 && reportTpl.getConcurrencyFlag().intValue() == YesNoEnum.YES.getCode())
		{//该报表提交数据时需要控制防止多人编辑，先后提交数据覆盖的问题
			Long version = null;
			if(redisUtil.hasKey(RedisPrefixEnum.REPORTDATAVERSION.getCode()+reportTpl.getId()))
			{
				version =  new Long(String.valueOf(redisUtil.get(RedisPrefixEnum.REPORTDATAVERSION.getCode()+reportTpl.getId())));
			}else {
				version = redisUtil.increment(RedisPrefixEnum.REPORTDATAVERSION.getCode()+reportTpl.getId());
			}
			resPreviewData.setVersion(version);;
		}
		//获取所有有权限的sheet
		List<ReportTplSheet> sheets = null;
		if(userInfoDto.getIsAdmin().intValue() == YesNoEnum.YES.getCode() || reportTpl.getViewAuth().intValue() == 1)
		{//超级管理员
			QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
			sheetQueryWrapper.eq("tpl_id", reportTpl.getId());
			sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			if(StringUtil.isNotEmpty(mesGenerateReportDto.getSheetIndex()))
			{
				sheetQueryWrapper.eq("sheet_index", mesGenerateReportDto.getSheetIndex());
			}
			sheets = this.iReportTplSheetService.list(sheetQueryWrapper);
		}else {
			QueryWrapper<SysRoleSheet> roleSheetQueryWrapper = new QueryWrapper<>();
			roleSheetQueryWrapper.eq("report_id", reportTpl.getId());
			roleSheetQueryWrapper.eq("role_id", userInfoDto.getRoleId());
			roleSheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<SysRoleSheet> roleSheets = this.iSysRoleSheetService.list(roleSheetQueryWrapper);
			if(ListUtil.isEmpty(roleSheets))
			{
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.noauth.view.report"));
			}
			List<Long> sheetIds = new ArrayList<>();
			for (int i = 0; i < roleSheets.size(); i++) {
				sheetIds.add(roleSheets.get(i).getSheetId());
			}
			QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
			sheetQueryWrapper.in("id", sheetIds);
			sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			if(StringUtil.isNotEmpty(mesGenerateReportDto.getSheetIndex()))
			{
				sheetQueryWrapper.eq("sheet_index", mesGenerateReportDto.getSheetIndex());
			}
			sheets = this.iReportTplSheetService.list(sheetQueryWrapper);
		}
		List<Map<String, String>> reportSqls = new ArrayList<>();
		Map<String, JSONObject> imgCells = new HashMap<>();
		Map<String, Object> mergePagination = new HashMap();
		List<Map<String, Object>> datasetsParams = this.getDatasetParamInfo(mesGenerateReportDto);
		Map<String, Object> viewParams = this.getViewParams(datasetsParams,userInfoDto);
		Map<String, LuckySheetBindData> blockBindDatas = new HashMap<String, LuckySheetBindData>();//循环块对应绑定的数据，可能会有多个数据集的情况，用来记录
		Map<String, Object> subtotalCellDatas = new HashMap<>();//小计单元格数据
		Map<String, Object> subtotalCellMap = new HashMap<>();//需要小计的原始单元格
		Map<String, JSONObject> subTotalDigits = new HashMap<>();//需要小计的原始单元格
		if(!ListUtil.isEmpty(sheets))
		{
			List<Long> sheetIds = new ArrayList<>();
			for (int i = 0; i < sheets.size(); i++) {
				sheetIds.add(sheets.get(i).getId());
			}
			//获取所有的变量单元格
			QueryWrapper<LuckysheetReportCell> queryWrapper = new QueryWrapper<LuckysheetReportCell>();
			queryWrapper.eq("tpl_id", reportTpl.getId());
			queryWrapper.in("sheet_id", sheetIds);
			queryWrapper.eq("cell_value_type", CellValueTypeEnum.VARIABLE.getCode());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			queryWrapper.orderByAsc("coordsx","coordsy");
			List<LuckysheetReportCell> list = this.iLuckysheetReportCellService.list(queryWrapper);
			List<String> usedDataSet = new ArrayList<String>();//存放所有数据集
			Map<String, List<Long>> datasetSheets = new HashMap<String, List<Long>>();//数据集所属于的sheet页
  			Set<String> hs = new LinkedHashSet<>();
			//将变量单元格的数据集放到数据集数组中
  			List<Long> datasetSheetIds = null;
 			if (!ListUtil.isEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					String[] datesetNames = list.get(i).getDatasetName().split(",");
					for (String datasetName : datesetNames) {
						hs.add(datasetName);
						Long sheetId = list.get(i).getSheetId();
						if(datasetSheets.containsKey(datasetName)) {
							datasetSheetIds = datasetSheets.get(datasetName);
						}else {
							datasetSheetIds = new ArrayList<>();
						}
						if(!datasetSheetIds.contains(sheetId)) {
							datasetSheetIds.add(sheetId);
							datasetSheets.put(datasetName, datasetSheetIds);
						}
					}
				}
				usedDataSet = new ArrayList<>(hs);
			}
			Map<String, String> datasetNameIdMap = new HashMap<String, String>();
			List<List<String>> columnNames = this.getTplDatasetsColumnNames(reportTpl.getId(),datasetNameIdMap,userInfoDto);
			//获取所有图表绑定的动态数据集属性
//			Set<String> chartsAttrs = new HashSet<>();
//			Map<String, List<String>> chartAttrsMap = new HashMap<String, List<String>>();
//			for (int i = 0; i < sheets.size(); i++) {
//				if(StringUtil.isNotEmpty(sheets.get(i).getChart()))
//				{
//					LuckysheetUtil.getChartsSeriesAttr(JSONArray.parseArray(sheets.get(i).getChart()), chartsAttrs, chartAttrsMap);
//				}
//			}
			Map<String, Map<String, List<List<Map<String, Object>>>>> processedCells = new HashMap<>();
			Map<String, List<Map<String, Object>>> datasetDatas = new HashMap<String, List<Map<String,Object>>>();//数据集对应的原始数据
			Map<String, Map<String, Object>> datasetParamsCache = new HashMap<>();//数据集对应的参数
			Map<String, Map<String, Object>> datasetPaginationCache = new HashMap<>();//数据集对应的分页信息
			Map<String, Map<String, String>> dictCache = new HashMap<>();//自定义和sql数据字典缓存
			List<ReportTplDataset> reportTplDatasets = null;
			//获取数据字典
			ReportCellDictsDto cellDictsDto = this.getReportDict(sheets,dictCache,reportTpl,viewParams);
			for (int t = 0; t < sheets.size(); t++) {
				ResLuckySheetDataDto resLuckySheetDataDto = new ResLuckySheetDataDto();
				resLuckySheetDataDto.setSheetId(sheets.get(t).getId());
				//获取打印设置
				QueryWrapper<ReportSheetPdfPrintSetting> pdfPrintQueryWrapper = new QueryWrapper<ReportSheetPdfPrintSetting>();
				pdfPrintQueryWrapper.eq("tpl_id", reportTpl.getId());
				pdfPrintQueryWrapper.eq("tpl_sheet_id", sheets.get(t).getId());
				pdfPrintQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				ReportSheetPdfPrintSetting printSettings = this.iReportSheetPdfPrintSettingService.getOne(pdfPrintQueryWrapper,false);
				if(printSettings != null && printSettings.getPageHeaderShow().intValue() == 1) {
					if(StringUtil.isNotEmpty(printSettings.getPageHeaderContent())) {
						if(printSettings.getPageHeaderContent().contains(".${")) {
							boolean flag = true;
							for (int i = 0; i < usedDataSet.size(); i++) {
								if(printSettings.getPageHeaderContent().contains(usedDataSet.get(i)+".${")) {
									flag = false;
									break;
								}
							}
							if(flag) {
								if(reportTplDatasets == null) {
									QueryWrapper<ReportTplDataset> datasetQueryWrapper = new QueryWrapper<>();
									pdfPrintQueryWrapper.eq("tpl_id", reportTpl.getId());
									pdfPrintQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
									reportTplDatasets = this.iReportTplDatasetService.list(datasetQueryWrapper);
								}
								if(ListUtil.isNotEmpty(reportTplDatasets)) {
									for (int i = 0; i < reportTplDatasets.size(); i++) {
										if(printSettings.getPageHeaderContent().contains(reportTplDatasets.get(i).getDatasetName()+".${")) {
											usedDataSet.add(reportTplDatasets.get(i).getDatasetName());
											break;
										}
									}
								}
							}
						}
					}
				}
				//获取所有的循环块
				QueryWrapper<LuckysheetReportCell> blockCellQueryWrapper = new QueryWrapper<LuckysheetReportCell>();
				blockCellQueryWrapper.eq("tpl_id", reportTpl.getId());
				blockCellQueryWrapper.eq("sheet_id", sheets.get(t).getId());
				blockCellQueryWrapper.eq("cell_value_type", CellValueTypeEnum.BLOCK.getCode());
				blockCellQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<LuckysheetReportCell> blocks = this.iLuckysheetReportCellService.list(blockCellQueryWrapper);
				if(!ListUtil.isEmpty(blocks))
				{
					QueryWrapper<LuckysheetReportBlockCell> reportBlockCellQueryWrapper = null;
					for (int i = 0; i < blocks.size(); i++) {
						//获取循环块中的变量单元格
						reportBlockCellQueryWrapper = new QueryWrapper<LuckysheetReportBlockCell>();
						reportBlockCellQueryWrapper.eq("report_cell_id", blocks.get(i).getId());
						reportBlockCellQueryWrapper.eq("cell_value_type", CellValueTypeEnum.VARIABLE.getCode());
						reportBlockCellQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
						List<LuckysheetReportBlockCell> blockCells = this.iLuckysheetReportBlockCellService.list(reportBlockCellQueryWrapper);
						if(!ListUtil.isEmpty(blockCells))
						{
							for (int j = 0; j < blockCells.size(); j++) {
								if (!usedDataSet.contains(blockCells.get(j).getDatasetName())) {
									usedDataSet.add(blockCells.get(j).getDatasetName());
								}
								String datasetName = blockCells.get(j).getDatasetName();
								if(datasetSheets.containsKey(datasetName)) {
									datasetSheetIds = datasetSheets.get(datasetName);
								}else {
									datasetSheetIds = new ArrayList<>();
								}
								if(!datasetSheetIds.contains(sheets.get(t).getId())) {
									datasetSheetIds.add(sheets.get(t).getId());
									datasetSheets.put(datasetName, datasetSheetIds);
								}
							}
						}
					}
				}
				ObjectMapper objectMapper = new ObjectMapper();
				Map<String, Object> config = new HashMap<String, Object>();
				Map<String, Object> configColhidden = new HashMap<String, Object>();
				Map<String, Map<String, Object>> paginationMap = new HashMap<>();//sheet页中的分页信息
				if(StringUtil.isNotEmpty(sheets.get(t).getConfig()))
				{
					config = objectMapper.readValue(sheets.get(t).getConfig(), Map.class);
					if(config.containsKey("colhidden"))
					{
						configColhidden = (Map<String, Object>) config.get("colhidden");
					}
				}
				Map<String, JSONArray> cellConditionFormat = new HashMap<>();//动态单元格对应的条件格式
				Map<String, String> fixedCellsMap = new HashMap<>();//所有的静态单元格
				Map<String, String> variableCellsMap = new HashMap<>();//所有的动态单元格
				//查询每个数据集对应的数据
				if(!ListUtil.isEmpty(usedDataSet))
				{
					List<LuckySheetBindData> dataSetsBindDatas = new ArrayList<LuckySheetBindData>();//所有数据集绑定的数据
					Map<String, LuckySheetBindData> cellBindData = new HashMap<>();//每个单元格绑定的数据，用于单元格过滤绑定单元格或者查询绑定单元格数据
					//获取所有固定的单元格,并封装成bindata与动态数据组成一个list
					QueryWrapper<LuckysheetReportCell> fixed = new QueryWrapper<LuckysheetReportCell>();
					fixed.eq("tpl_id", reportTpl.getId());
					fixed.eq("sheet_id", sheets.get(t).getId());
					fixed.eq("cell_value_type", CellValueTypeEnum.FIXED.getCode());
					if(mesGenerateReportDto.getIsMobile().intValue() == YesNoEnum.YES.getCode().intValue() && mesGenerateReportDto.getIsTask().intValue() != YesNoEnum.YES.getCode().intValue())
					{
						fixed.eq("is_chart_cell", YesNoEnum.NO.getCode());
					}
					fixed.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					fixed.orderByAsc("coordsx","coordsy");
					List<LuckysheetReportCell> fixedCells = this.iLuckysheetReportCellService.list(fixed);
					if(!ListUtil.isEmpty(fixedCells))
					{
						LuckySheetBindData bindData = null;
						for (int i = 0; i < fixedCells.size(); i++) {
							this.isHiddenCol(fixedCells.get(i), configColhidden, viewParams);
							bindData = new LuckySheetBindData();
							bindData.setTplType(reportTpl.getTplType());
							bindData.setReportCellId(fixedCells.get(i).getId());
							bindData.setCoordsx(fixedCells.get(i).getCoordsx());
							bindData.setCoordsy(fixedCells.get(i).getCoordsy());
							bindData.setCellExtend(fixedCells.get(i).getCellExtend());
							bindData.setCellValueType(CellValueTypeEnum.FIXED.getCode());
							bindData.setRowSpan(fixedCells.get(i).getRowSpan());
							bindData.setColSpan(fixedCells.get(i).getColSpan());
							bindData.setIsLink(fixedCells.get(i).getIsLink());
							bindData.setIsMerge(fixedCells.get(i).getIsMerge());
							bindData.setCellValue(fixedCells.get(i).getCellValue());
							bindData.setIsFunction(fixedCells.get(i).getIsFunction());
							//2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//							bindData.setWarning(fixedCells.get(i).getWarning());
//							bindData.setWarningRules(fixedCells.get(i).getWarningRules());
//							bindData.setWarningColor(fixedCells.get(i).getWarningColor());
//							bindData.setWarningContent(fixedCells.get(i).getWarningContent());
//							bindData.setThreshold(fixedCells.get(i).getThreshold());
							bindData.setDatasetName(fixedCells.get(i).getDatasetName());
							bindData.setIsGroupMerge(fixedCells.get(i).getIsGroupMerge());
							bindData.setIsChartCell(fixedCells.get(i).getIsChartCell());
							bindData.setIsDataVerification(fixedCells.get(i).getIsDataVerification());
							bindData.setDataVerification(fixedCells.get(i).getDataVerification());
							bindData.setUnitTransfer(fixedCells.get(i).getUnitTransfer());
							bindData.setTransferType(fixedCells.get(i).getTransferType());
							bindData.setMultiple(fixedCells.get(i).getMultiple());
							bindData.setIsSubtotal(fixedCells.get(i).getIsSubtotal());
							bindData.setSubtotalCells(fixedCells.get(i).getSubtotalCells());
							bindData.setIsSubtotalCalc(fixedCells.get(i).getIsSubtotalCalc());
							bindData.setSheetId(fixedCells.get(i).getSheetId());
							bindData.setCellFillType(fixedCells.get(i).getCellFillType());
							bindData.setPriortyMoveDirection(fixedCells.get(i).getPriortyMoveDirection());
							if(StringUtil.isNotEmpty(fixedCells.get(i).getFormsAttrs())) {
								JSONObject formsAttrs = JSON.parseObject(fixedCells.get(i).getFormsAttrs());
								boolean isOperationCol =  formsAttrs.getBooleanValue("isOperationCol");
								bindData.setOperationCol(isOperationCol);
							}
							if(YesNoEnum.YES.getCode().intValue() == fixedCells.get(i).getIsChartCell().intValue())
							{
								bindData.setChartId(fixedCells.get(i).getChartIds());
							}
							try {
								bindData.setCellData(objectMapper.readValue(fixedCells.get(i).getCellData(), Map.class));
							} catch (Exception e) {
								e.printStackTrace();
								throw new BizException(StatusCode.FAILURE, "单元格数据解析失败，请检查单元格数据格式是否正确！");
							}
							if(YesNoEnum.YES.getCode().intValue() == fixedCells.get(i).getIsLink().intValue())
							{
								try {
									bindData.setLinkConfig(objectMapper.readValue(fixedCells.get(i).getLinkConfig(), Map.class));
								} catch (Exception e) {
									e.printStackTrace();
									throw new BizException(StatusCode.FAILURE, "单元格超链接数据解析失败，请检查单元格超链接数据格式是否正确！");
								}
							}
							String key = sheets.get(t).getSheetIndex()+bindData.getCoordsx()+"-"+bindData.getCoordsy();
							cellBindData.put(key, bindData);
							dataSetsBindDatas.add(bindData);
							fixedCellsMap.put(fixedCells.get(i).getCoordsx() + "_" + fixedCells.get(i).getCoordsy(), fixedCells.get(i).getCoordsx() + "_" + fixedCells.get(i).getCoordsy());
						}
					}
					Map<String, Object> subParams = new HashMap<String, Object>();//传给子表的参数
					Map<String, String> apiCache = new HashMap<>();//api请求返回结果缓存，同一个api多个数据集的情况下，直接使用缓存数据，防止多次请求
					List<String> subTotalCellCoords = new ArrayList<String>();
					for (int i = 0; i < usedDataSet.size(); i++) {
						List<Map<String, Object>> datas = null;
						Map<String, Object> result = null;
						if(!datasetDatas.containsKey(usedDataSet.get(i)))
						{
							result = this.getDatasetDatas(reportTpl, mesGenerateReportDto, usedDataSet.get(i), isPagination, mergePagination,reportSqls,userInfoDto,apiCache,subParams);
 							datas = (List<Map<String, Object>>) result.get("datas");
							datasetDatas.put(usedDataSet.get(i), datas);
							datasetParamsCache.put(usedDataSet.get(i), (Map<String, Object>) result.get("params"));
							datasetPaginationCache.put(usedDataSet.get(i), result.get("pagination")==null?null:(Map<String, Object>) result.get("pagination"));
							if(datasetSheets.containsKey(usedDataSet.get(i)) && datasetSheets.get(usedDataSet.get(i)).contains(sheets.get(t).getId())) {
								paginationMap.put(usedDataSet.get(i), result.get("pagination")==null?null:(Map<String, Object>) result.get("pagination"));
							}
						}else {
							datas = datasetDatas.get(usedDataSet.get(i));
							if(datasetSheets.containsKey(usedDataSet.get(i)) && datasetSheets.get(usedDataSet.get(i)).contains(sheets.get(t).getId())) {
								paginationMap.put(usedDataSet.get(i), datasetPaginationCache.get(usedDataSet.get(i)));
							}
						}
						//获取数据集对应的变量
						LuckysheetReportCell params = new LuckysheetReportCell();
						params.setTplId(reportTpl.getId());
 						params.setSheetId(sheets.get(t).getId());
 						params.setDatasetName(usedDataSet.get(i));
 						List<LuckysheetReportCell> variableCells = this.luckysheetReportCellMapper.getVariableCells(params);
						if(!ListUtil.isEmpty(variableCells)) {
							Map<String, Object> datasetParams = null;
							if(result != null)
							{
								datasetParams = (Map<String, Object>) result.get("params");
							}else {
								datasetParams = datasetParamsCache.get(usedDataSet.get(i));
							}
							for (int j = 0; j < variableCells.size(); j++) {
								this.isHiddenCol(variableCells.get(j), configColhidden, datasetParams);
								if(CellValueTypeEnum.VARIABLE.getCode().intValue() == variableCells.get(j).getCellValueType())
								{
									Map<String, String> map = ListUtil.getNewCellValue(variableCells.get(j).getCellValue(), columnNames, datasetNameIdMap);
									variableCells.get(j).setCellValue(map.get("cellValue"));
									variableCells.get(j).setDatasetName(map.get("datasetNames"));
									variableCellsMap.put(variableCells.get(j).getCoordsx() + "_" + variableCells.get(j).getCoordsy(), variableCells.get(j).getCoordsx() + "_" + variableCells.get(j).getCoordsy());
								}else {
									if(StringUtil.isNotEmpty(variableCells.get(j).getDatasetName()))
									{
										String datasetName = "";
										String[] datasetNames = LuckysheetUtil.getDatasetNames(variableCells.get(j).getDatasetName());
										for (int k = 0; k < datasetNames.length; k++) {
											if(datasetNameIdMap.containsKey(datasetNames[k]))
											{
												if(StringUtil.isNullOrEmpty(datasetName))
												{
													datasetName = datasetNameIdMap.get(datasetNames[k])+"_"+datasetNames[k];
												}else {
													datasetName = datasetName + "," + datasetNameIdMap.get(datasetNames[k])+"_"+datasetNames[k];
												}
											}else {
												if(StringUtil.isNullOrEmpty(datasetName))
												{
													datasetName = datasetNames[k];
												}else {
													datasetName = datasetName + "," + datasetNames[k];
												}
											}
										}
										variableCells.get(j).setDatasetName(datasetName);
									}
								}
							}
							String datasetName = usedDataSet.get(i);
							if(datasetNameIdMap.containsKey(usedDataSet.get(i)))
							{
 								datasetName = datasetNameIdMap.get(usedDataSet.get(i))+"-"+usedDataSet.get(i);
							}
 							List<LuckySheetBindData> dataSetBindDatas = luckySheetDataProcess.get(1).process(variableCells, datas,datasetName,processedCells,blockBindDatas,subtotalCellDatas,subtotalCellMap,sheets.get(t).getSheetIndex(),cellBindData,subTotalDigits,reportTpl.getTplType(),subTotalCellCoords);
							if(!ListUtil.isEmpty(dataSetBindDatas))
							{
								dataSetsBindDatas.addAll(dataSetBindDatas);
							}
						}
					}
					//将所有的循环块封装成bindata与动态数据组成一个list
//					if(!ListUtil.isEmpty(blocks))
//					{
//						objectMapper = new ObjectMapper();
//						LuckySheetBindData bindData = null;
//						for (int i = 0; i < blocks.size(); i++) {
//							bindData = new LuckySheetBindData();
//							bindData.setReportCellId(blocks.get(i).getId());
//							bindData.setCoordsx(blocks.get(i).getCoordsx());
//							bindData.setCoordsy(blocks.get(i).getCoordsy());
//							bindData.setCellValueType(CellValueTypeEnum.BLOCK.getCode());
//							bindData.setRowSpan(blocks.get(i).getRowSpan());
//							bindData.setColSpan(blocks.get(i).getColSpan());
//							bindData.setIsDataVerification(blocks.get(i).getIsDataVerification());
//							bindData.setDataVerification(blocks.get(i).getDataVerification());
//							List<Map<String, Object>> originalData = datasetDatas.get(blocks.get(i).getDatasetName());
//							List<List<Map<String, Object>>> datas = new ArrayList<List<Map<String,Object>>>();
//							datas.add(originalData);
//							bindData.setDatas(datas);
//							dataSetsBindDatas.add(bindData);
//						}
//					}
					JSONArray conditionForamt = JSONArray.parseArray(sheets.get(t).getConditionformatSave());
					if(ListUtil.isNotEmpty(conditionForamt)) {
						for (int i = 0; i < conditionForamt.size(); i++) {
							JSONArray cellRange = JSON.parseArray(JSON.toJSONString(conditionForamt.getJSONObject(i).getJSONArray("cellrange")));
							for (int j = 0; j < cellRange.size(); j++) {
								JSONArray column = cellRange.getJSONObject(j).getJSONArray("column");
								JSONArray row = cellRange.getJSONObject(j).getJSONArray("row");
								for (int k = row.getIntValue(0); k <= row.getIntValue(1); k++) {
									for (int k2 = column.getIntValue(0); k2 <= column.getIntValue(1); k2++) {
										JSONObject formatObj = JSON.parseObject(JSON.toJSONString(conditionForamt.getJSONObject(i)));
										String key = k+"_"+k2;
										if(variableCellsMap.containsKey(key)) {
											if(cellConditionFormat.containsKey(key)) {
												cellConditionFormat.get(key).add(formatObj);
											}else {
												JSONArray conditions = new JSONArray();
												conditions.add(formatObj);
												cellConditionFormat.put(k+"_"+k2, conditions);
											}
										}
									}
								}
							}
						}
					}
					QueryWrapper<LuckysheetReportCell> allCellsQueryWrapper = new QueryWrapper<LuckysheetReportCell>();
					allCellsQueryWrapper.eq("tpl_id", reportTpl.getId());
					allCellsQueryWrapper.eq("sheet_id", sheets.get(t).getId());
					allCellsQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					allCellsQueryWrapper.orderByAsc("coordsx","coordsy");
					List<LuckysheetReportCell> allCells = this.iLuckysheetReportCellService.list(allCellsQueryWrapper);
					config.put("colhidden", configColhidden);
					resLuckySheetDataDto = this.buildLuckysheetDatas(allCells, dataSetsBindDatas,config,datasetNameIdMap,columnNames,subtotalCellDatas,subtotalCellMap,cellConditionFormat,subTotalDigits,sheets.get(t).getSheetIndex(),reportTpl.getId(),reportTpl.getTplType(),subTotalCellCoords,isExport,dictCache,userInfoDto,viewParams);
				}else {
					//没有数据集查询所有的静态单元格
					//获取所有固定的单元格,并封装成bindata与动态数据组成一个list
					List<LuckySheetBindData> dataSetsBindDatas = new ArrayList<LuckySheetBindData>();//所有数据集绑定的数据
					QueryWrapper<LuckysheetReportCell> fixed = new QueryWrapper<LuckysheetReportCell>();
					fixed.eq("tpl_id", reportTpl.getId());
					fixed.eq("sheet_id", sheets.get(t).getId());
					fixed.eq("cell_value_type", CellValueTypeEnum.FIXED.getCode());
					fixed.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					fixed.orderByAsc("coordsx","coordsy");
					List<LuckysheetReportCell> fixedCells = this.iLuckysheetReportCellService.list(fixed);
					if(!ListUtil.isEmpty(fixedCells))
					{
						LuckySheetBindData bindData = null;
						for (int i = 0; i < fixedCells.size(); i++) {
							this.isHiddenCol(fixedCells.get(i), configColhidden, viewParams);
							bindData = new LuckySheetBindData();
							bindData.setReportCellId(fixedCells.get(i).getId());
							bindData.setCoordsx(fixedCells.get(i).getCoordsx());
							bindData.setCoordsy(fixedCells.get(i).getCoordsy());
							bindData.setCellExtend(fixedCells.get(i).getCellExtend());
							bindData.setCellValueType(CellValueTypeEnum.FIXED.getCode());
							bindData.setRowSpan(fixedCells.get(i).getRowSpan());
							bindData.setColSpan(fixedCells.get(i).getColSpan());
							bindData.setIsLink(fixedCells.get(i).getIsLink());
							bindData.setIsMerge(fixedCells.get(i).getIsMerge());
							bindData.setCellValue(fixedCells.get(i).getCellValue());
							bindData.setIsFunction(fixedCells.get(i).getIsFunction());
							bindData.setDatasetName(fixedCells.get(i).getDatasetName());
							bindData.setIsGroupMerge(fixedCells.get(i).getIsGroupMerge());
							bindData.setIsChartCell(fixedCells.get(i).getIsChartCell());
							bindData.setIsDataVerification(fixedCells.get(i).getIsDataVerification());
							bindData.setDataVerification(fixedCells.get(i).getDataVerification());
							bindData.setUnitTransfer(fixedCells.get(i).getUnitTransfer());
							bindData.setTransferType(fixedCells.get(i).getTransferType());
							bindData.setMultiple(fixedCells.get(i).getMultiple());
							bindData.setIsSubtotal(fixedCells.get(i).getIsSubtotal());
							bindData.setSubtotalCells(fixedCells.get(i).getSubtotalCells());
							bindData.setIsSubtotalCalc(fixedCells.get(i).getIsSubtotalCalc());
							bindData.setSheetId(fixedCells.get(i).getSheetId());
							bindData.setCellFillType(fixedCells.get(i).getCellFillType());
							try {
								bindData.setCellData(objectMapper.readValue(fixedCells.get(i).getCellData(), Map.class));
							} catch (Exception e) {
								e.printStackTrace();
								throw new BizException(StatusCode.FAILURE, "单元格数据解析失败，请检查单元格数据格式是否正确！");
							}
							if(YesNoEnum.YES.getCode().intValue() == fixedCells.get(i).getIsLink().intValue())
							{
								try {
									bindData.setLinkConfig(objectMapper.readValue(fixedCells.get(i).getLinkConfig(), Map.class));
								} catch (Exception e) {
									e.printStackTrace();
									throw new BizException(StatusCode.FAILURE, "单元格超链接数据解析失败，请检查单元格超链接数据格式是否正确！");
								}
							}
							dataSetsBindDatas.add(bindData);
						}
						QueryWrapper<LuckysheetReportCell> allCellsQueryWrapper = new QueryWrapper<LuckysheetReportCell>();
						allCellsQueryWrapper.eq("tpl_id", reportTpl.getId());
						allCellsQueryWrapper.eq("sheet_id", sheets.get(t).getId());
						allCellsQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
						allCellsQueryWrapper.orderByAsc("coordsx","coordsy");
						List<LuckysheetReportCell> allCells = this.iLuckysheetReportCellService.list(allCellsQueryWrapper);
						config.put("colhidden", configColhidden);
						resLuckySheetDataDto = this.buildLuckysheetDatas(allCells, dataSetsBindDatas,config,datasetNameIdMap,columnNames,subtotalCellDatas,subtotalCellMap,cellConditionFormat,subTotalDigits,sheets.get(t).getSheetIndex(),reportTpl.getId(),reportTpl.getTplType(),null,isExport,dictCache,userInfoDto,viewParams);
					}
				}
//				resLuckySheetDataDto.setPagination(paginationMap);
//				resLuckySheetDataDto.setIsParamMerge(reportTpl.getIsParamMerge());
				mergePagination.put("currentPage", mesGenerateReportDto.getPagination().get("currentPage"));
//				resLuckySheetDataDto.setMergePagination(mergePagination);
				if(StringUtil.isNotEmpty(sheets.get(t).getFrozen()))
				{
					resLuckySheetDataDto.setFrozen(objectMapper.readValue(sheets.get(t).getFrozen(), Map.class));
				}else {
					resLuckySheetDataDto.setFrozen(new HashMap<String, Object>());
				}
				if(StringUtil.isNotEmpty(sheets.get(t).getImages()))
				{
					JSONObject images = objectMapper.readValue(sheets.get(t).getImages(), JSONObject.class);
					if(!ListUtil.isEmpty(resLuckySheetDataDto.getImageDatas()))
					{
						for (int j = 0; j < resLuckySheetDataDto.getImageDatas().size(); j++) {
							images.put(IdWorker.getIdStr(), resLuckySheetDataDto.getImageDatas().get(j).getJSONObject("imgInfo"));
						}
					}
					resLuckySheetDataDto.setImages(images);
				}else {
					if(!ListUtil.isEmpty(resLuckySheetDataDto.getImageDatas()))
					{
						JSONObject images = new JSONObject();
						for (int j = 0; j < resLuckySheetDataDto.getImageDatas().size(); j++) {
							images.put(IdWorker.getIdStr(), resLuckySheetDataDto.getImageDatas().get(j).getJSONObject("imgInfo"));
						}
						resLuckySheetDataDto.setImages(images);
					}else {
						resLuckySheetDataDto.setImages(null);
					}
				}
//				if(!ListUtil.isEmpty(resLuckySheetDataDto.getImageDatas()))
//				{
//					Map<String, Object> rowLen = (Map<String, Object>) resLuckySheetDataDto.getConfig().get(LuckySheetPropsEnum.ROWLEN.getCode());
//					Map<String, Object> colLen = (Map<String, Object>) resLuckySheetDataDto.getConfig().get(LuckySheetPropsEnum.COLUMNLEN.getCode());
////					this.processImageDatas(resLuckySheetDataDto.getImageDatas(),rowLen,colLen,imgCells,sheets.get(t).getSheetIndex());
//				}
				if(resLuckySheetDataDto.getCalcChain() != null && resLuckySheetDataDto.getCalcChain().size() > 0) {
					for (int j = 0; j < resLuckySheetDataDto.getCalcChain().size(); j++) {
						resLuckySheetDataDto.getCalcChain().get(j).put("index", sheets.get(t).getSheetIndex());
					}
				}
				resLuckySheetDataDto.setSheetIndex(sheets.get(t).getSheetIndex());
				resLuckySheetDataDto.setSheetName(sheets.get(t).getSheetName());
				resLuckySheetDataDto.setSheetOrder(sheets.get(t).getSheetOrder());
				QueryWrapper<ReportFormsDatasource> datasourceQueryWrapper = new QueryWrapper<>();
				datasourceQueryWrapper.eq("tpl_id", reportTpl.getId());
				datasourceQueryWrapper.eq("sheet_id", sheets.get(t).getId());
				datasourceQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<ReportFormsDatasource> reportFormsDatasources = this.iReportFormsDatasourceService.list(datasourceQueryWrapper);
				List<Map<String, JSONObject>> cellDatasourceConfigs = new ArrayList<>();
				Map<String, JSONObject> tableKeys = new HashMap<>();//主键字段
				Map<String, JSONObject> autoFillAttrs = new HashMap<>();//自动填充字段
				Map<String, JSONObject> deleteTypes = new HashMap<>();//删除规则
				Map<String, JSONArray> mainAttrs = new HashMap<>();//主子表关联规则
				Map<String,Object> mainDatasource = new HashMap<>();//主表数据源
				if(!ListUtil.isEmpty(reportFormsDatasources))
				{
					for (int i = 0; i < reportFormsDatasources.size(); i++) {
						if(reportFormsDatasources.get(i).getIsMain().intValue() == YesNoEnum.YES.getCode().intValue()) {
							mainDatasource.put(reportFormsDatasources.get(i).getDatasourceId()+"|"+reportFormsDatasources.get(i).getName()
									+"|"+reportFormsDatasources.get(i).getTableName(), 1);
						}
						Map<String, JSONObject> cellDatasourceConfig = new HashMap<>();
						List<String> keys = new ArrayList<>();
						QueryWrapper<ReportFormsDatasourceAttrs> attrsQueryWrapper = new QueryWrapper<>();
						attrsQueryWrapper.eq("report_forms_datasource_id", reportFormsDatasources.get(i).getId());
						attrsQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
						List<ReportFormsDatasourceAttrs> formsDatasourceAttrs = this.iReportFormsDatasourceAttrsService.list(attrsQueryWrapper);
						if(!ListUtil.isEmpty(formsDatasourceAttrs))
						{
							for (int j = 0; j < formsDatasourceAttrs.size();j++) {
								 if(formsDatasourceAttrs.get(j).getType().intValue() == 2) {
									 keys.add(formsDatasourceAttrs.get(j).getColumnName());
								 }
							}
							for (int j = 0; j < formsDatasourceAttrs.size();j++) {
								JSONObject jsonObject = new JSONObject();
								if(formsDatasourceAttrs.get(j).getType().intValue() == 1) {
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("r", formsDatasourceAttrs.get(j).getCoordsx());
									jsonObject.put("c", formsDatasourceAttrs.get(j).getCoordsy());
									jsonObject.put("datasourceId", String.valueOf(reportFormsDatasources.get(i).getDatasourceId()));
									jsonObject.put("table", reportFormsDatasources.get(i).getTableName());
									jsonObject.put("name", reportFormsDatasources.get(i).getName());
									if(keys.contains(formsDatasourceAttrs.get(j).getColumnName())) {
										jsonObject.put("isKey", YesNoEnum.YES.getCode());	
									}else {
										jsonObject.put("isKey", YesNoEnum.NO.getCode());	
									}
									String key = formsDatasourceAttrs.get(j).getCoordsx() + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+formsDatasourceAttrs.get(j).getCoordsy();
									cellDatasourceConfig.put(key, jsonObject);
								}else if(formsDatasourceAttrs.get(j).getType().intValue() == 2){
									//主键字段
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("idType", formsDatasourceAttrs.get(j).getIdType());
									jsonObject.put("datasourceId", String.valueOf(reportFormsDatasources.get(i).getDatasourceId()));
									jsonObject.put("table", reportFormsDatasources.get(i).getTableName());
									jsonObject.put("name", reportFormsDatasources.get(i).getName());
									String key = String.valueOf(reportFormsDatasources.get(i).getDatasourceId()) + "|"
										+  reportFormsDatasources.get(i).getTableName() + "|"
										+ formsDatasourceAttrs.get(j).getColumnName()+"|"+reportFormsDatasources.get(i).getName();
									tableKeys.put(key, jsonObject);
								}else if(formsDatasourceAttrs.get(j).getType().intValue() == 3){
									//自动填充字段 
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("fillType", formsDatasourceAttrs.get(j).getFillType());
									jsonObject.put("fillStrategy", formsDatasourceAttrs.get(j).getFillStrategy());
									jsonObject.put("fillValue", formsDatasourceAttrs.get(j).getFillValue());
									jsonObject.put("datasourceId", String.valueOf(reportFormsDatasources.get(i).getDatasourceId()));
									jsonObject.put("table", reportFormsDatasources.get(i).getTableName());
									jsonObject.put("name", reportFormsDatasources.get(i).getName());
									String key = String.valueOf(reportFormsDatasources.get(i).getDatasourceId()) + "|"
											+  reportFormsDatasources.get(i).getTableName() + "|"
											+ reportFormsDatasources.get(i).getName()+"|"+formsDatasourceAttrs.get(j).getColumnName();
									autoFillAttrs.put(key, jsonObject);
								}else if(formsDatasourceAttrs.get(j).getType().intValue() == 4){
									//删除规则
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("deleteType", formsDatasourceAttrs.get(j).getDeleteType());
									jsonObject.put("deleteValue", formsDatasourceAttrs.get(j).getDeleteValue());
									jsonObject.put("datasourceId", String.valueOf(reportFormsDatasources.get(i).getDatasourceId()));
									jsonObject.put("table", reportFormsDatasources.get(i).getTableName());
									jsonObject.put("name", reportFormsDatasources.get(i).getName());
									String key = String.valueOf(reportFormsDatasources.get(i).getDatasourceId()) + "|"
											+  reportFormsDatasources.get(i).getTableName() + "|"
											+ reportFormsDatasources.get(i).getName();
									deleteTypes.put(key, jsonObject);
								}else if(formsDatasourceAttrs.get(j).getType().intValue() == 5){
									//主子表
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("mainColumn", formsDatasourceAttrs.get(j).getMainColumn());
									jsonObject.put("mainName", formsDatasourceAttrs.get(j).getMainName());
									jsonObject.put("mainDatasourceId", String.valueOf(formsDatasourceAttrs.get(j).getMainDatasourceId()));
									jsonObject.put("mainTable", formsDatasourceAttrs.get(j).getMainTable());
									jsonObject.put("mainName", formsDatasourceAttrs.get(j).getMainName());
									String key = String.valueOf(reportFormsDatasources.get(i).getDatasourceId()) + "|"
											+  reportFormsDatasources.get(i).getName() + "|"
											+ reportFormsDatasources.get(i).getTableName();
									if(!mainAttrs.containsKey(key)) {
										mainAttrs.put(key, new JSONArray());
									}
									mainAttrs.get(key).add(jsonObject);
									key = String.valueOf(formsDatasourceAttrs.get(j).getMainDatasourceId()) + "|"
											+  formsDatasourceAttrs.get(j).getMainName() + "|"
											+ formsDatasourceAttrs.get(j).getMainTable();
									if(mainDatasource.containsKey(key)) {
										key = key + "|" + formsDatasourceAttrs.get(j).getMainColumn();
										mainDatasource.put(key, 1);
									}
								}
							}
							cellDatasourceConfigs.add(cellDatasourceConfig);
						}
					}
//					if(!ListUtil.isEmpty(resLuckySheetDataDto.getImageDatas()))
//					{
//						Map<String, Object> rowLen = (Map<String, Object>) resLuckySheetDataDto.getConfig().get(LuckySheetPropsEnum.ROWLEN.getCode());
//						Map<String, Object> colLen = (Map<String, Object>) resLuckySheetDataDto.getConfig().get(LuckySheetPropsEnum.COLUMNLEN.getCode());
////						this.processImageDatas(resLuckySheetDataDto.getImageDatas(),rowLen,colLen,imgCells,sheets.get(t).getSheetIndex());
//					}
					if(resLuckySheetDataDto.getCalcChain() != null && resLuckySheetDataDto.getCalcChain().size() > 0) {
						for (int j = 0; j < resLuckySheetDataDto.getCalcChain().size(); j++) {
							resLuckySheetDataDto.getCalcChain().get(j).put("index", sheets.get(t).getSheetIndex());
						}
					}
					if(StringUtil.isNotEmpty(sheets.get(t).getFrozen()))
					{
						resLuckySheetDataDto.setFrozen(objectMapper.readValue(sheets.get(t).getFrozen(), Map.class));
					}else {
						resLuckySheetDataDto.setFrozen(new HashMap<String, Object>());
					}
					resLuckySheetDataDto.setCellDatasourceConfigs(cellDatasourceConfigs);
					resLuckySheetDataDto.setTableKeys(tableKeys);
					resLuckySheetDataDto.setAutoFillAttrs(autoFillAttrs);
					resLuckySheetDataDto.setDeleteTypes(deleteTypes);
					resLuckySheetDataDto.setMainDatasource(mainDatasource);
					resLuckySheetDataDto.setMainAttrs(mainAttrs);
				}
				JSONArray chart = new JSONArray();
				if(StringUtil.isNotEmpty(sheets.get(t).getChart()))
				{
					JSONArray sheetCharts = JSONArray.parseArray(sheets.get(t).getChart());
					if(!ListUtil.isEmpty(sheetCharts))
					{
						for (int i = 0; i < sheetCharts.size(); i++) {
							JSONObject jsonObject = sheetCharts.getJSONObject(i);
							this.getChartData(jsonObject, datasetDatas, columnNames,sheets.get(t),mesGenerateReportDto,reportTpl.getIsParamMerge(),
									reportTpl,resLuckySheetDataDto.getConfig(),resLuckySheetDataDto.getChartCells(),null,null,mesGenerateReportDto.getIsMobile(),resLuckySheetDataDto.getCellBindData());
							chart.add(jsonObject);
						}
					}
				}
				resLuckySheetDataDto.setChart(chart);
				if(StringUtil.isNotEmpty(sheets.get(t).getXxbtScreenshot()))
				{
					resLuckySheetDataDto.setXxbtScreenshot(JSON.parseObject(sheets.get(t).getXxbtScreenshot()));
				}
				if(printSettings != null && printSettings.getPageHeaderShow().intValue() == 1) {
					if(StringUtil.isNotEmpty(printSettings.getPageHeaderContent())) {
						if(printSettings.getPageHeaderContent().contains(".${")) {
							for (String key : datasetDatas.keySet()) {
								if(printSettings.getPageHeaderContent().contains(key+".${")) {
									List<Map<String, Object>> datas = datasetDatas.get(key);
									if(ListUtil.isNotEmpty(datas)) {
										for (String property : datas.get(0).keySet()) {
											if(printSettings.getPageHeaderContent().contains("${"+property+"}")) {
												Object value = datas.get(0).get(property);
												if(value == null) {
													value = "";
												}
												printSettings.setPageHeaderContent(printSettings.getPageHeaderContent().replace(key+"."+"${"+property+"}", String.valueOf(value)));
											}
										}
									}
									break;
								}
							}
						}
					}
				}
				resLuckySheetDataDto.setPrintSettings(printSettings);
				resLuckySheetDataDto.setBase64Imgs(JSON.parseObject(sheets.get(t).getImages()));
				if(printSettings != null && printSettings.getHorizontalPage().intValue() == 1)
				{
					resLuckySheetDataDto.setPageDivider(StringUtil.isNotEmpty(sheets.get(t).getPageDivider())?JSON.parseArray(sheets.get(t).getPageDivider()):new JSONArray());
				}
				if(resLuckySheetDataDto.getConfig() == null) {
					resLuckySheetDataDto.setConfig(new HashMap<>());
				}
				if(!StringUtil.isEmptyMap(resLuckySheetDataDto.getBase64Imgs()))
				{
					JSONObject image = new JSONObject();
					Set<String> set = resLuckySheetDataDto.getBase64Imgs().keySet();
					for (String key : set) {
						JSONObject imgInfo = resLuckySheetDataDto.getBase64Imgs().getJSONObject(key);
						Map<String, Object> configColumnLen = (Map<String, Object>) resLuckySheetDataDto.getConfig().get(LuckySheetPropsEnum.COLUMNLEN.getCode());
						Map<String, Object> configRowLen = (Map<String, Object>) resLuckySheetDataDto.getConfig().get(LuckySheetPropsEnum.ROWLEN.getCode());
						JSONObject coords = LuckysheetUtil.calculateImgCoors(imgInfo,configRowLen,resLuckySheetDataDto.getRowhidden(),configColumnLen,resLuckySheetDataDto.getColhidden());
						int str = coords.getIntValue("str");
						int stc = coords.getIntValue("stc");
						int edr = coords.getIntValue("edr");
						int edc = coords.getIntValue("edc");
						if(StringUtil.isEmptyMap(resLuckySheetDataDto.getStartXAndY()))
						{
							Map<String, Integer> startXAndY = new HashMap<>();
							startXAndY.put("x", str);
							startXAndY.put("y", stc);
							resLuckySheetDataDto.setStartXAndY(startXAndY);
						}else {
							if(str < resLuckySheetDataDto.getStartXAndY().get("x"))
							{
								resLuckySheetDataDto.getStartXAndY().put("x", str);
							}
							if(stc < resLuckySheetDataDto.getStartXAndY().get("y"))
							{
								resLuckySheetDataDto.getStartXAndY().put("y", stc);
							}
						}
						if(edr > resLuckySheetDataDto.getMaxXAndY().get("maxX"))
						{
							resLuckySheetDataDto.getMaxXAndY().put("maxX", edr);
						}
						if(edc > resLuckySheetDataDto.getMaxXAndY().get("maxY"))
						{
							resLuckySheetDataDto.getMaxXAndY().put("maxY", edc);
						}
						JSONObject data = new JSONObject();
						data.put("rs", edr-str+1);
						data.put("cs", edc-stc+1);
						image.put(str+"_"+stc, data);
					}
					resLuckySheetDataDto.setImgInfo(image);
				}
				resLuckySheetDataDto.setPageDivider(StringUtil.isNotEmpty(sheets.get(t).getPageDivider())?JSON.parseArray(sheets.get(t).getPageDivider()):new JSONArray());
				if(!StringUtil.isEmptyMap(cellConditionFormat)) {
					JSONArray luckysheetConditionformatSave = new JSONArray();
					cellConditionFormat.forEach((key, value) -> {
						luckysheetConditionformatSave.addAll(value);
					});
					resLuckySheetDataDto.setLuckysheetConditionformatSave(luckysheetConditionformatSave);
				}
				sheetDatas.add(resLuckySheetDataDto);
				this.processSheetPagination(paginationMap, resLuckySheetDataDto, mesGenerateReportDto.getPagination().get("currentPage"));
				resPreviewData.setCellDictsLabelValue(cellDictsDto.getCellDictsLabelValue());
			}
		}
		resPreviewData.setImgCells(imgCells);
		resPreviewData.setSheetDatas(sheetDatas);
		resPreviewData.setIsParamMerge(reportTpl.getIsParamMerge());
		resPreviewData.setPagination(mergePagination);
		resPreviewData.setTplName(reportTpl.getTplName());
		resPreviewData.setShowToolbar(reportTpl.getShowToolbar());
		resPreviewData.setShowRowHeader(reportTpl.getShowRowHeader());
		resPreviewData.setShowColHeader(reportTpl.getShowColHeader());
		resPreviewData.setShowGridlines(reportTpl.getShowGridlines());
		resPreviewData.setReportSqls(reportSqls);
		return resPreviewData;
	}
	
	/**  
	 * @MethodName: processSheetPagination
	 * @Description: 处理sheet页的分页信息
	 * @author caiyang
	 * @param paginationMap
	 * @param resLuckySheetDataDto void
	 * @date 2025-05-06 09:37:58 
	 */ 
	private void processSheetPagination(Map<String, Map<String, Object>> paginationMap,ResLuckySheetDataDto resLuckySheetDataDto,Integer currentPage) {
		if(!StringUtil.isEmptyMap(paginationMap)) {
			 Long totalCount = null;
			 Long pageCount = null;
			 for (String key : paginationMap.keySet()) {
				 Map<String, Object> pagination = paginationMap.get(key);
				 if(pagination != null) {
					 Long datasetTotalCount = Long.parseLong(String.valueOf(pagination.get("totalCount")));
					 if(totalCount == null || datasetTotalCount.longValue() > totalCount.longValue()) {
						 totalCount = datasetTotalCount;
					 }
					 Long datasetPageCount = Long.parseLong(String.valueOf(pagination.get("pageCount")));
					 if(pageCount == null || datasetPageCount.longValue() < pageCount.longValue()) {
						 pageCount = datasetPageCount;
					 }
				 }
			 }
			 if(totalCount != null && pageCount != null) {
				 Map<String, Object> mergePagination = new HashMap();
				 mergePagination.put("totalCount", totalCount);
				 mergePagination.put("pageCount", pageCount);
				 mergePagination.put("currentPage", currentPage);
				 resLuckySheetDataDto.setMergePagination(mergePagination);
			 }
		}
	}
	
	private Map<String, Object> getDatasetDatas(ReportTpl reportTpl,MesGenerateReportDto mesGenerateReportDto,String datasetName,
			boolean isPagination,Map<String, Object> mergePagination,List<Map<String, String>> reportSqls,UserInfoDto userInfoDto,Map<String, String> apiCache,Map<String, Object> subParams) throws Exception {
		Map<String, String> sqlMap = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> datas = null;
		Map<String, Object> searchInfo = null;
		if(YesNoEnum.YES.getCode().intValue() == reportTpl.getIsParamMerge().intValue() || YesNoEnum.YES.getCode().intValue() == mesGenerateReportDto.getIsMobile().intValue())
		{
			searchInfo = mesGenerateReportDto.getSearchData().get(0);
		}else {
			searchInfo = this.getDatasetParamInfo(datasetName, mesGenerateReportDto);
		}
		//获取数据源
		Map<String, Object> dataSetAndDatasource = this.iReportTplDatasetService.getTplDatasetAndDatasource(reportTpl.getId(), datasetName,userInfoDto.getMerchantNo());
		int type = (int) dataSetAndDatasource.get("type");
		DataSource dataSource = null;
		InfluxDBConnection dbConnection = null;
		TDengineConnection tDengineConnection = null;
		Object data = dataSetAndDatasource.get("dataSource");
		String userName = dataSetAndDatasource.get("useName") != null?String.valueOf(dataSetAndDatasource.get("useName")):"";
		String password = dataSetAndDatasource.get("password") != null?String.valueOf(dataSetAndDatasource.get("password")):"";
		if(data instanceof DataSource)
		{
			dataSource = (DataSource) data;
		}else if(data instanceof InfluxDBConnection)
		{
			dbConnection = (InfluxDBConnection) data;
		}else if(data instanceof TDengineConnection)
		{
			tDengineConnection = (TDengineConnection) data;
		}
		ReportTplDataset reportTplDataset = (ReportTplDataset) dataSetAndDatasource.get("tplDataSet");
		Map<String, Object> params = null;
		Map<String, Object> pagination = null;
		if(searchInfo != null)
		{
			params = ParamUtil.getViewParams((JSONArray) searchInfo.get("params"),userInfoDto);
		}
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		params.putAll(subParams);
		if(DatasetTypeEnum.SQL.getCode().intValue() == reportTplDataset.getDatasetType().intValue() || DatasetTypeEnum.MONGO.getCode().intValue() == reportTplDataset.getDatasetType().intValue())
		{
			String sql = reportTplDataset.getTplSql();
			if(SqlTypeEnum.SQL.getCode().intValue() == reportTplDataset.getSqlType().intValue())
			{
				String countSql = "";
				if(type == 6)
				{//influxdb
					sql = JdbcUtils.processSqlParams(sql, params);
					if(isPagination && YesNoEnum.YES.getCode().intValue() == reportTplDataset.getIsPagination().intValue())
					{
						countSql = JdbcUtils.getInfluxdbCountSql(sql);
						ReportDatasource reportDatasource = this.iReportDatasourceService.getById(reportTplDataset.getDatasourceId());
						if(YesNoEnum.YES.getCode().intValue() == mesGenerateReportDto.getIsCustomerPage().intValue())
						{
							sql = JdbcUtils.getPaginationSql(sql, reportDatasource.getType(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))), mesGenerateReportDto.getStartPage(),mesGenerateReportDto.getEndPage());
						}else {
							sql = JdbcUtils.getPaginationSql(sql, reportDatasource.getType(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("currentPage"))));
						}
					}
					datas = ReportDataUtil.getInfluxdbData(dbConnection, sql);
					if(StringUtil.isNotEmpty(countSql))
					{
						int count = ReportDataUtil.getInfluxdbDataCountBySQL(dbConnection, countSql);
						Long totalCount = (Long) mergePagination.get("totalCount");
						Integer pageCount = (Integer) mergePagination.get("pageCount");
						Integer paramsPageCount = Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount")));
						if(totalCount == null)
						{
							mergePagination.put("totalCount", count);
//							mergePagination.put("totalPage", count/Integer.valueOf(String.valueOf(params.get("pageCount")))+1);
						}else {
							if(count > totalCount)
							{
								mergePagination.put("totalCount", count);
//								mergePagination.put("totalPage", count/Integer.valueOf(String.valueOf(params.get("pageCount")))+1);
							}
						}
						if(pageCount == null)
						{
							mergePagination.put("pageCount", pageCount);
						}else {
							if(paramsPageCount < pageCount)
							{
								mergePagination.put("pageCount", paramsPageCount);
							}
						}
						pagination = new HashMap<String, Object>();
						pagination.put("totalCount", count);
						pagination.put("pageCount", paramsPageCount);
					}
				}else if(type == 10)
				{//tdengine
					sql = JdbcUtils.processSqlParams(sql, params);
					if(isPagination && YesNoEnum.YES.getCode().intValue() == reportTplDataset.getIsPagination().intValue())
					{
						countSql = JdbcUtils.getCountSql(sql);
						ReportDatasource reportDatasource = this.iReportDatasourceService.getById(reportTplDataset.getDatasourceId());
						if(YesNoEnum.YES.getCode().intValue() == mesGenerateReportDto.getIsCustomerPage().intValue())
						{
							sql = JdbcUtils.getPaginationSql(sql, reportDatasource.getType(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))), mesGenerateReportDto.getStartPage(),mesGenerateReportDto.getEndPage());
						}else {
							sql = JdbcUtils.getPaginationSql(sql, reportDatasource.getType(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("currentPage"))));
						}
					}
					datas = ReportDataUtil.getDatasourceDataBySql(tDengineConnection.getConnection(), sql);
					if(StringUtil.isNotEmpty(countSql))
					{
						tDengineConnection.buildConnection();
						int count = ReportDataUtil.getDataCountBySQL(tDengineConnection.getConnection(), countSql);
						Long totalCount = (Long) mergePagination.get("totalCount");
						Integer pageCount = (Integer) mergePagination.get("pageCount");
						Integer paramsPageCount = Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount")));
						if(totalCount == null)
						{
							mergePagination.put("totalCount", count);
						}else {
							if(count > totalCount)
							{
								mergePagination.put("totalCount", count);
							}
						}
						if(pageCount == null)
						{
							mergePagination.put("pageCount", paramsPageCount);
						}else {
							if(paramsPageCount < pageCount)
							{
								mergePagination.put("pageCount", paramsPageCount);
							}
						}
						pagination = new HashMap<String, Object>();
						pagination.put("totalCount", count);
						pagination.put("pageCount", paramsPageCount);
					}
				}else if(type == 14){//mongodb
					sql = JdbcUtils.processSqlParams(sql, params);
					if(reportTplDataset.getMongoSearchType().intValue() == 1) {
						if(isPagination && YesNoEnum.YES.getCode().intValue() == reportTplDataset.getIsPagination().intValue()) {
							datas = MongoClientUtil.findGetPageData(String.valueOf(dataSetAndDatasource.get("jdbcUrl")), reportTplDataset.getMongoTable(), sql, reportTplDataset.getMongoOrder(),Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))),Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("currentPage"))));
							long count = MongoClientUtil.findGetDataCount(String.valueOf(dataSetAndDatasource.get("jdbcUrl")), reportTplDataset.getMongoTable(), sql, reportTplDataset.getMongoOrder());
							Long totalCount = (Long) mergePagination.get("totalCount");
							Integer pageCount = (Integer) mergePagination.get("pageCount");
							Integer paramsPageCount = Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount")));
							if(totalCount == null)
							{
								mergePagination.put("totalCount", count);
							}else {
								if(count > totalCount)
								{
									mergePagination.put("totalCount", count);
								}
							}
							if(pageCount == null)
							{
								mergePagination.put("pageCount", paramsPageCount);
							}else {
								if(paramsPageCount < pageCount)
								{
									mergePagination.put("pageCount", paramsPageCount);
								}
							}
							pagination = new HashMap<String, Object>();
							pagination.put("totalCount", count);
							pagination.put("pageCount", paramsPageCount);
						}else {
							datas = MongoClientUtil.findGetData(String.valueOf(dataSetAndDatasource.get("jdbcUrl")), reportTplDataset.getMongoTable(), sql, reportTplDataset.getMongoOrder());
						}
					}else if(reportTplDataset.getMongoSearchType().intValue() == 2) {
						datas = MongoClientUtil.aggregateGetData(String.valueOf(dataSetAndDatasource.get("jdbcUrl")), reportTplDataset.getMongoTable(), sql);
					}
				}else {
					sql = JdbcUtils.processSqlParams(sql,params);
					if(isPagination && YesNoEnum.YES.getCode().intValue() == reportTplDataset.getIsPagination().intValue())
					{
						countSql = JdbcUtils.getCountSql(sql);
						ReportDatasource reportDatasource = this.iReportDatasourceService.getById(reportTplDataset.getDatasourceId());
						if(YesNoEnum.YES.getCode().intValue() == mesGenerateReportDto.getIsCustomerPage().intValue())
						{
							sql = JdbcUtils.getPaginationSql(sql, reportDatasource.getType(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))), mesGenerateReportDto.getStartPage(),mesGenerateReportDto.getEndPage());
						}else {
							sql = JdbcUtils.getPaginationSql(sql, reportDatasource.getType(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("currentPage"))));
						}
					}
					//根据sql获取数据
					if(type == 9)
					{
						datas = ReportDataUtil.getDatasourceDataBySql(dataSource, sql,userName,password);
						if(isPagination && YesNoEnum.YES.getCode().intValue() == reportTplDataset.getIsPagination().intValue())
						{
							datas = ListUtil.getSubList(datas,Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("currentPage"))),Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))));
						}
					}else {
						datas = ReportDataUtil.getDatasourceDataBySql(dataSource, sql);
					}
					if(StringUtil.isNotEmpty(countSql))
					{
						long count = ReportDataUtil.getDataCountBySQL(dataSource, countSql);
						Long totalCount = (Long) mergePagination.get("totalCount");
						Integer pageCount = (Integer) mergePagination.get("pageCount");
						Integer paramsPageCount = Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount")));
						if(totalCount == null)
						{
							mergePagination.put("totalCount", count);
						}else {
							if(count > totalCount)
							{
								mergePagination.put("totalCount", count);
							}
						}
						if(pageCount == null)
						{
							mergePagination.put("pageCount", paramsPageCount);
						}else {
							if(paramsPageCount < pageCount)
							{
								mergePagination.put("pageCount", paramsPageCount);
							}
						}
						pagination = new HashMap<String, Object>();
						pagination.put("totalCount", count);
						pagination.put("pageCount", paramsPageCount);
					}
				}
			}else {
				datas = ReportDataUtil.getDatasourceDataByProcedure(dataSource, sql, params, JSONArray.parseArray(reportTplDataset.getInParam()), JSONArray.parseArray(reportTplDataset.getOutParam()));
			}
			sqlMap.put("name", reportTplDataset.getDatasetName());
			sqlMap.put("sql", sql);
			reportSqls.add(sqlMap);
		}else {//api
			ReportDatasource reportDatasource = this.iReportDatasourceService.getById(reportTplDataset.getDatasourceId());
			Map<String, String> headers = null;
			if(StringUtil.isNotEmpty(reportDatasource.getApiRequestHeader()))
			{
				JSONArray headersArray = JSONArray.parseArray(reportDatasource.getApiRequestHeader());
				if(!ListUtil.isEmpty(headersArray))
				{
					headers = new HashMap<String, String>();
					for (int j = 0; j < headersArray.size(); j++) {
						String headerName = headersArray.getJSONObject(j).getString("headerName");
						if(mesGenerateReportDto.getApiHeaders() != null && mesGenerateReportDto.getApiHeaders().containsKey(headerName)) {
							headers.put(headerName, String.valueOf(mesGenerateReportDto.getApiHeaders().get(headerName)));	
						}else {
							headers.put(headerName, String.valueOf(headersArray.getJSONObject(j).getString("headerValue")));	
						}	
					}
				}
			}
			String result = null;
			if(reportTplDataset.getIsPagination().intValue() == YesNoEnum.YES.getCode().intValue()) {
				//分页查询
				if(YesNoEnum.YES.getCode().intValue() == mesGenerateReportDto.getIsCustomerPage().intValue()) {
					params.put(reportTplDataset.getCurrentPageAttr(), mesGenerateReportDto.getStartPage());
					params.put(reportTplDataset.getPageCountAttr(), (mesGenerateReportDto.getEndPage()-mesGenerateReportDto.getStartPage()+1)*Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))));
				}else {
					params.put(reportTplDataset.getCurrentPageAttr(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("currentPage"))));
					params.put(reportTplDataset.getPageCountAttr(), Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount"))));
				}
				
			}
			String key = Md5Util.generateMd5(reportDatasource.getJdbcUrl()+JSONObject.toJSONString(params));
			if(apiCache.containsKey(key)) {
				result = apiCache.get(key);
			}else {
				if("post".equals(reportDatasource.getApiRequestType()))
				{
					result = HttpClientUtil.doPostJson(reportDatasource.getJdbcUrl(), JSONObject.toJSONString(params), headers);
				}else {
					result = HttpClientUtil.doGet(reportDatasource.getJdbcUrl(),headers,params);
				}
				apiCache.put(key, result);
			}
			Map<String, Object> apiResult = ReportDataUtil.getApiResult(result, reportDatasource.getApiResultType(), reportDatasource.getApiColumnsPrefix(),reportTplDataset.getIsPagination().intValue() == YesNoEnum.YES.getCode().intValue()?reportTplDataset.getTotalAttr():null);
			datas = (List<Map<String, Object>>) apiResult.get("datas");
			if(reportTplDataset.getIsPagination().intValue() == YesNoEnum.YES.getCode().intValue()) {
				Long totalCount = (Long) mergePagination.get("totalCount");
				Integer pageCount = (Integer) mergePagination.get("pageCount");
				Integer paramsPageCount = Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("pageCount")));
				long count = (long) apiResult.get("total");
				if(totalCount == null)
				{
					mergePagination.put("totalCount", count);
				}else {
					if(count > totalCount)
					{
						mergePagination.put("totalCount", count);
					}
				}
				if(pageCount == null)
				{
					mergePagination.put("pageCount", paramsPageCount);
				}else {
					if(paramsPageCount < pageCount)
					{
						mergePagination.put("pageCount", paramsPageCount);
					}
				}
				pagination = new HashMap<String, Object>();
				pagination.put("totalCount", count);
				pagination.put("pageCount", paramsPageCount);
			}
		}
		String subParamAttrs = reportTplDataset.getSubParamAttrs();
		if(StringUtil.isNotEmpty(subParamAttrs)) {
			JSONArray attrs = JSON.parseArray(subParamAttrs);
			if(ListUtil.isNotEmpty(attrs) && ListUtil.isNotEmpty(datas) ) {
				//主数据向下传递，因为有可能有一条数据，也有可能有多条数据，所以统一使用数据的方式传递数据，
				//子数据集接收参数是注意使用数组方式接收，<foreach>
				for (int i = 0; i < datas.size(); i++) {
					for (int j = 0; j < attrs.size(); j++) {
						if(datas.get(i).containsKey(attrs.getString(j))) {
							JSONArray paramsArray = null;
							if(subParams.containsKey(attrs.getString(j))) {
								paramsArray = (JSONArray) subParams.get(attrs.getString(j));
							}else {
								paramsArray = new JSONArray();
								subParams.put(attrs.getString(j), paramsArray);
							}
							paramsArray.add(datas.get(i).get(attrs.getString(j)));
						}
					}
				}
			}
		}
		resultMap.put("datas", datas);
		resultMap.put("params", params);
		resultMap.put("pagination", pagination);
		if(ListUtil.isNotEmpty(datas) && reportTplDataset.getIsConvert() != null && YesNoEnum.YES.getCode().intValue() == reportTplDataset.getIsConvert().intValue()) {
			if(StringUtil.isNotEmpty(reportTplDataset.getHeaderName()) && StringUtil.isNotEmpty(reportTplDataset.getFixedColumn())
					&& StringUtil.isNotEmpty(reportTplDataset.getFixedColumn())) {
				List<Map<String, Object>> convertDatas = new ArrayList<>();
				List<String> strs1=JSONObject.parseObject(reportTplDataset.getFixedColumn(),ArrayList.class);
				String[] stringArray = strs1.toArray(new String[0]);
				List<List<Object>> lists = RowConvertColUtil.doConvert(datas, reportTplDataset.getHeaderName(), stringArray, reportTplDataset.getValueField(), true, stringArray, "");
				if(ListUtil.isNotEmpty(lists) && lists.size() > 1) {
					List<Object> attrs = lists.get(0);
					Map<String, Object> convertData = null;
					for (int i = 1; i < lists.size(); i++) {
						convertData = new LinkedHashMap<String, Object>();
						for (int j = 0; j < lists.get(i).size(); j++) {
							convertData.put(String.valueOf(attrs.get(j)), lists.get(i).get(j));
						}
						convertDatas.add(convertData);
					}
				}
				resultMap.put("datas", convertDatas);
			}
		}
		return resultMap;
	}
	
	private void getChartData(JSONObject jsonObject,Map<String, List<Map<String, Object>>> datasetDatas,List<List<String>> columnNames,
			ReportTplSheet reportTplSheet,MesGenerateReportDto mesGenerateReportDto,int isParamMerge,ReportTpl reportTpl,
			Map<String, Object> config,JSONObject chartCells,Object rowhidden,Object colhidden,Integer isMobile,
			Map<String, LuckySheetBindData> cellBindData) {
		JSONObject chartOptions = jsonObject.getJSONObject("chartOptions");
		String chartAllType = chartOptions.getString("chartAllType");
		String chartId = chartOptions.getString("chart_id");
		String dataset = chartOptions.getString("dataset");
		boolean dataGroup = chartOptions.getBooleanValue("dataGroup");
		Map<String, Object> configColumnLen = (Map<String, Object>) config.get(LuckySheetPropsEnum.COLUMNLEN.getCode());
		Map<String, Object> configRowLen = (Map<String, Object>) config.get(LuckySheetPropsEnum.ROWLEN.getCode());
		if(StringUtil.isNotEmpty(dataset)) {
			List<Map<String, Object>> datas = datasetDatas.get(dataset);
			String categoryField = chartOptions.getString("categoryField");
			JSONArray valueField = chartOptions.getJSONArray("valueField");
			JSONArray groupField = chartOptions.getJSONArray("groupField");
			if(valueField == null) {
				valueField = new JSONArray();
			}
			JSONArray seriesField = chartOptions.getJSONArray("seriesField");
			if(seriesField == null) {
				seriesField = new JSONArray();
			}
			if(dataGroup) {
				List<String> attrs = new ArrayList<>();
				if(ListUtil.isNotEmpty(groupField)) {
					for (int i = 0; i < groupField.size(); i++) {
						attrs.add(groupField.getString(i));
					}
				}else {
					attrs.add(categoryField);
				}
				List<List<Map<String, Object>>> groupDatas = ListUtil.groupDatas(datas, attrs);
				if(ListUtil.isNotEmpty(groupDatas)) {
					List<Map<String, Object>> newDatas = new ArrayList<Map<String,Object>>();
					for (int i = 0; i < groupDatas.size(); i++) {
						if(groupDatas.get(i).size() > 1) {
							Map<String, Object> newMap = new HashMap<>();
							newMap.putAll(groupDatas.get(i).get(0));
							for (int j = 0; j < valueField.size(); j++) {
								BigDecimal newValue = new BigDecimal(0);
								for (int t = 0; t < groupDatas.get(i).size(); t++) {
									Object value = groupDatas.get(i).get(t).get(valueField.getString(j));
									if(CheckUtil.isNumeric(String.valueOf(value))) {
										newValue = newValue.add(new BigDecimal(String.valueOf(value)));
									}
								}
								newMap.put(valueField.getString(j), newValue);
							}
							newDatas.add(newMap);
						}else {
							Map<String, Object> newMap = new HashMap<>();
							newMap.putAll(groupDatas.get(i).get(0));
							newDatas.add(newMap);
						}
					}
					datas = newDatas;
				}
			}
			JSONObject spec = chartOptions.getJSONObject("defaultOption").getJSONObject("spec");
			if("echarts|column|default".equals(chartAllType) || "echarts|line|default".equals(chartAllType) || "echarts|line|smooth".equals(chartAllType)
					|| "echarts|area|default".equals(chartAllType)) {
				JSONArray xField = new JSONArray();
				xField.add("type");
				xField.add("seriesField");
				spec.put("xField", xField);
				spec.put("yField", "value");
				spec.put("seriesField", "seriesField");
			}else if("echarts|column|stack".equals(chartAllType) || "echarts|area|stack".equals(chartAllType)) {
				spec.put("xField", "type");
				spec.put("yField", "value");
				spec.put("seriesField", "seriesField");
			}else if("echarts|bar|default".equals(chartAllType)) {
				JSONArray yField = new JSONArray();
				yField.add("type");
				yField.add("seriesField");
				spec.put("xField", "value");
				spec.put("yField", yField);
				spec.put("seriesField", "seriesField");
			}else if("echarts|bar|stack".equals(chartAllType)) {
				spec.put("xField", "value");
				spec.put("yField", "type");
				spec.put("seriesField", "seriesField");
			}else if("echarts|pie|default".equals(chartAllType)) {
				spec.put("valueField", "value");
				spec.put("categoryField", "type");
			}else if("echarts|radar|default".equals(chartAllType)) {
				spec.put("valueField", "value");
				spec.put("categoryField", "type");
				spec.put("seriesField", "seriesField");
			}
			boolean flag = false;
			if(seriesField.size()>=valueField.size()) {
				flag = true;
			}
			JSONArray chartDatas = new JSONArray();
			if(ListUtil.isNotEmpty(datas)) {
				if(ListUtil.isNotEmpty(valueField)) {
					for (int i = 0; i < valueField.size(); i++) {
						for (int j = 0; j < datas.size(); j++) {
							JSONObject chartData = new JSONObject();
							chartData.put("value", datas.get(j).get(valueField.getString(i)));
							chartData.put("type", datas.get(j).get(categoryField));
							if(flag) {
								chartData.put("seriesField", datas.get(j).get(seriesField.getString(i))==null?seriesField.getString(i):datas.get(j).get(seriesField.getString(i)));
							}else {
								chartData.put("seriesField", "系列"+(i+1));
							}
							chartDatas.add(chartData);
						}
					}
				}
			}
			spec.getJSONObject("data").put("values", chartDatas);
			
			QueryWrapper<LuckysheetReportCell> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("tpl_id", reportTplSheet.getTplId());
			queryWrapper.eq("sheet_id", reportTplSheet.getId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			queryWrapper.eq("is_chart_attr", YesNoEnum.YES.getCode());
			queryWrapper.like("chart_ids", chartId);
			queryWrapper.orderByAsc("coordsx", "coordsy");
			List<LuckysheetReportCell> list = this.iLuckysheetReportCellService.list(queryWrapper);
			if(!ListUtil.isEmpty(list))
			{
				Integer startx = null;
				Integer starty = null;
				Integer endx = null;
				Integer endy = null;
				for (int i = 0; i < list.size(); i++) {
					int r = list.get(i).getCoordsx();
					int c = list.get(i).getCoordsy();
	 				if(cellBindData.containsKey(r+"_"+c) || cellBindData.containsKey(r+"_"+c+"_remove"))
					{
						LuckySheetBindData bindData = cellBindData.get(r+"_"+c)==null?cellBindData.get(r+"_"+c+"_remove"):cellBindData.get(r+"_"+c);
						if(startx == null || startx.intValue()>bindData.getStartx().intValue())
						{
							startx = bindData.getStartx();
						}
						if(starty == null || starty.intValue() > bindData.getStarty().intValue())
						{
	 						starty = bindData.getStarty();
						}
						if(endx == null || endx.intValue() < bindData.getEndx().intValue())
						{
							endx = bindData.getEndx();
						}
						if(endy == null || endy.intValue() < bindData.getEndy().intValue())
						{
							endy = bindData.getEndy();
						}
					}
				if(startx != null)
				{
					String start = SheetUtil.excelColIndexToStr(starty+1)+(startx+1);
					String end = SheetUtil.excelColIndexToStr(endy+1)+(endx+1);
					jsonObject.getJSONObject("chartOptions").put("rangeTxt", start+":"+end);
					JSONArray rangeArray = new JSONArray();
					JSONObject rangeObject = new JSONObject();
					JSONArray column = new JSONArray();
					column.add(starty);
					column.add(endy);
					JSONArray column1 = new JSONArray();
					column1.add(0);
					column1.add(endy-starty);
					rangeObject.put("column", column);
					JSONArray row = new JSONArray();
					row.add(startx);
					row.add(endx);
					JSONArray row1 = new JSONArray();
					row1.add(0);
					row1.add(endx-startx);
					rangeObject.put("row", row);
					rangeArray.add(rangeObject);
					jsonObject.getJSONObject("chartOptions").put("rangeArray", rangeArray);
					JSONObject range = new JSONObject();
					range.put("range", rangeObject);
					JSONObject content = new JSONObject();
					content.put("column", column1);
					content.put("row", row1);
					range.put("content", content);
					jsonObject.getJSONObject("chartOptions").put("rangeSplitArray", range);
				}
			}
			//计算位置和宽高
			JSONObject chartCell = chartCells.getJSONObject(chartId);
			if(chartCell != null)
			{
				int r = chartCell.getIntValue("r");
				int c = chartCell.getIntValue("c");
				int rs = chartCell.getIntValue("rs");
				int cs = chartCell.getIntValue("cs");
				double top = LuckysheetUtil.calculateTop(configRowLen, r,rowhidden);
				double left = LuckysheetUtil.calculateLeft(configColumnLen, c,colhidden);
				double height = LuckysheetUtil.calculateHeight(configRowLen, r, rs);
				double width = LuckysheetUtil.calculateWidth(configColumnLen, c, cs);
				jsonObject.put("top", top);
				jsonObject.put("left", left);
				jsonObject.put("height", height-42);
				jsonObject.put("width", width-22);
				jsonObject.put("offsetHeight", height);
				jsonObject.put("offsetWidth", width);
			}
		}
	 }
	}
	
	/**  
	 * @MethodName: processImageDatas
	 * @Description: 处理数据中的图片信息
	 * @author caiyang
	 * @param images
	 * @param rowlen
	 * @param columnlen 
	 * @return void
	 * @date 2023-01-06 02:39:18 
	 */  
	private void processImageDatas(List<JSONObject> images,Map<String, Object> rowlen,Map<String, Object> columnlen,Map<String, JSONObject> imgCells,String sheetIndex)
	{
		if(!ListUtil.isEmpty(images))
		{
			for (int i = 0; i < images.size(); i++) {
				JSONObject img = images.get(i);
				int r = img.getIntValue("r");//横坐标
				int c = img.getIntValue("c");//纵坐标
				int isMerge = img.getIntValue("isMerge");
				//计算图片的距离左侧和上方的距离
//				double left = 0;
//				double top = 0;
				double imgHeight = img.getDoubleValue("height");//图片单元格高度
				double imgWidth = img.getDoubleValue("width");//图片单元格高度
				int extend = img.getIntValue("extend");
				if(YesNoEnum.YES.getCode().intValue() == isMerge)
				{
					int rowSpan = img.getIntValue("rowSpan");
					int colSpan = img.getIntValue("colSpan");
					for (int j = 0; j < rowSpan; j++) {
						if(j != 0)
						{
							Object len = rowlen.get(String.valueOf(r+j));
							if(len == null)
							{
								imgHeight = imgHeight + Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT;
							}else {
								imgHeight = imgHeight + Double.parseDouble(String.valueOf(len));
							}
						}
					}
					for (int j = 0; j < colSpan; j++) {
						if(j != 0)
						{
							Object len = columnlen.get(String.valueOf(c+j));
							if(len == null)
							{
								imgWidth = imgWidth + Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH;
							}else {
								imgWidth = imgWidth + Double.parseDouble(String.valueOf(len));
							}
						}
					}
				}
				
//				for (int j = 0; j < r; j++) {
//					Object len =  rowlen.get(String.valueOf(j));
//					if(len == null)
//					{
//						top = top + Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT;
//					}else {
//						top = top + Double.parseDouble(String.valueOf(len));
//					}
//				}
//				if(1 == extend)
//				{
//					top = top + r;	
//				}
//				for (int j = 0; j < c; j++) {
//					Object len = columnlen.get(String.valueOf(j));
//					if(len == null)
//					{
//						left = left + Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH;
//					}else {
//						left = left + Double.parseDouble(String.valueOf(len));
//					}
//				}
//				if(2 == extend)
//				{
//					left = left + c;
//				}
				JSONObject imgInfo = img.getJSONObject("imgInfo");
//				JSONObject defaultInfo = imgInfo.getJSONObject("default");
//				defaultInfo.put("left", left);
//				defaultInfo.put("top", top);
//				defaultInfo.put("width", imgWidth);
//				defaultInfo.put("height", imgHeight);
				JSONObject pic = new JSONObject();
//				pic.put("left", left);
//				pic.put("top", top);
				pic.put("width", imgWidth);
				pic.put("height", imgHeight);
				String url = imgInfo.getString("src");
				String imgBase64 = StringUtil.imgToBase64(url);
				pic.put("imgBase64", imgBase64);
				imgCells.put(sheetIndex + "_" + r + "_" + c, pic);
			}
		}
	}
	
	/**  
	 * @Title: buildLuckysheetDatas
	 * @Description: 构建单元格以及单元格数据
	 * @param allCells 所有的单元格，按照x和y坐标排序
	 * @param dataSetsBindDatas 单元格绑定的数据
	 * @param config 单元格配置
	 * @param datasetDatas 数据集对应的原始数据
	 * @return
	 * @author caiyang
	 * @throws IOException 
	 * @date 2022-04-05 03:14:57 
	 */ 
	private ResLuckySheetDataDto buildLuckysheetDatas(List<LuckysheetReportCell> allCells,List<LuckySheetBindData> dataSetsBindDatas,
			Map<String, Object> config,Map<String, String> datasetNameIdMap,List<List<String>> columnNames,
			Map<String, Object> subtotalCellDatas,Map<String, Object> subtotalCellMap,Map<String, JSONArray> cellConditionFormat,Map<String, JSONObject> subTotalDigits
			,String sheetIndex,Long tplId,int tplType,List<String> subTotalCellCoords,boolean isExport,Map<String, Map<String, String>> dictCache,UserInfoDto userInfoDto,Map<String, Object> viewParams) throws IOException
	{
		ResLuckySheetDataDto result = new ResLuckySheetDataDto();
		Map<String, LuckySheetBindData> binddataMap = CellUtil.luckySheetBindDataCoordinateMap(dataSetsBindDatas);
		//将dataSetsBindDatas按照allCells的顺序重新排列
		List<LuckySheetBindData> sortedBindData = new ArrayList<LuckySheetBindData>();
		Map<String, LuckySheetBindData> coverCells = new HashMap<>();//记录填充方式是覆盖的单元格数据
		Map<String, LuckySheetBindData> cellBindData = new HashMap<>();//坐标对应的单元格数据
		Map<String, Map<String, Object>> mergeMap = new HashMap<String, Map<String,Object>>();//合并单元格
		Map<String, Object> borderInfo = new HashMap<String, Object>();//边框
		List<Object> borderInfos = new ArrayList<Object>();
		Object authority = config.get("authority");
		Object colhidden = config.get(LuckySheetPropsEnum.COLHIDDEN.getCode());
		if(colhidden == null) {
			colhidden = new LinkedHashMap<>();
		}
		Object rowhidden = config.get(LuckySheetPropsEnum.ROWHIDDEN.getCode());
		if(rowhidden == null) {
			rowhidden = new LinkedHashMap<>();
		}
		Map<String, Object> configRowLen = config.get(LuckySheetPropsEnum.ROWLEN.getCode()) != null?(HashMap<String, Object>) config.get(LuckySheetPropsEnum.ROWLEN.getCode()):new HashMap<String, Object>() ;
		Map<String, Object> configColumnLen = config.get(LuckySheetPropsEnum.COLUMNLEN.getCode()) != null?(HashMap<String, Object>) config.get(LuckySheetPropsEnum.COLUMNLEN.getCode()):new HashMap<String, Object>();
		List<Map<String, Object>> borderConfig = config.get(LuckySheetPropsEnum.BORDERINFO.getCode()) != null?(List<Map<String, Object>>)config.get(LuckySheetPropsEnum.BORDERINFO.getCode()):new ArrayList<Map<String,Object>>();
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, Object>> cellDatas = new ArrayList<Map<String,Object>>();//单元格数据
		Map<String, Map<String, Object>> hyperlinks = new HashMap<String, Map<String,Object>>();//单元格超链接
		List<JSONObject> calcChain = new ArrayList<>();//计算链
		Map<String, Object> rowlen = new HashMap<String, Object>();//行高
		Map<String, Object> columnlen = new HashMap<String, Object>();//列宽
		Map<String, Integer> maxCoordinate = new HashMap<String, Integer>();//坐标对应的最大值，横坐标 x-+坐标值:纵坐标的值，纵坐标y-+坐标值：横坐标的值，
		Map<String, Integer> maxXAndY = new HashMap<String, Integer>();
		Map<String, Map<String, Object>> usedCells = new HashMap<>();
		Map<String, Map<String, String>> dicts = new HashMap<>();
		Map<String, Object> nowFunction = new HashMap<>();
		Map<String, Object> functionCellFormat = new HashMap<>();//函数单元格的格式
		Map<String, Integer> startXAndY = new HashMap<>();//起始横坐标和纵坐标
	    JSONObject chartCells = new JSONObject();//图表对应的坐标信息
	    JSONObject dataVerification = new JSONObject();//数据验证
	    JSONObject drillCells = new JSONObject();//下钻报表单元格
	    Map<String, JSONObject> subtotalRows = new HashMap<String, JSONObject>();
	    JSONObject dynamicRange = new JSONObject();//动态单元格范围
	    Map<String, JSONObject> extraCustomCellConfigs = new HashMap<>();//额外单元格配置
	    Map<String, Boolean> allowEditConfigs = new HashMap<>();//是否允许单元格进行编辑配置信息
		Map<String, JSONObject> extendCellOrigin = new HashMap<>();//扩展单元格的坐标与原始单元格坐标的对应关系
		Map<String, JSONObject> columnStartCoords = new HashMap<>();//列对应的起始坐标
//		Map<String, Object> replacedData = new HashMap<>();//被缓存替换的数据 
//		Map<String, JSONObject> cacheDatas = new HashMap<>();//缓存数据
		for (int i = 0; i < allCells.size(); i++) {
			LuckySheetBindData bindData = binddataMap.get(allCells.get(i).getCoordsx() + "-" + allCells.get(i).getCoordsy());
			if(bindData != null)
			{
				bindData.setOriginalCoordsx(bindData.getCoordsx());
				bindData.setOriginalCoordsy(bindData.getCoordsy());
				sortedBindData.add(bindData);
				cellBindData.put(allCells.get(i).getCoordsx() + "_" + allCells.get(i).getCoordsy(), bindData);
				if(allCells.get(i).getCellFillType().intValue() == 2) {
					coverCells.put(allCells.get(i).getCoordsx() + "_" + allCells.get(i).getCoordsy(), bindData);
				}
			}
			processExtendCellOrigin(extendCellOrigin,allCells.get(i));
			String originCell = allCells.get(i).getCoordsx() + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() + allCells.get(i).getCoordsy();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("r", allCells.get(i).getCoordsx());
			jsonObject.put("c", allCells.get(i).getCoordsy());
			columnStartCoords.put(originCell, jsonObject);
			if(tplType == 2) {
				JSONObject extraCustomCellConfig = objectMapper.readValue(StringUtil.isNullOrEmpty(allCells.get(i).getFormsAttrs())?"{}":allCells.get(i).getFormsAttrs(), JSONObject.class);
				boolean allowEdit = extraCustomCellConfig.getBooleanValue("allowEdit");
				if(allowEdit)
				{
					allowEditConfigs.put(allCells.get(i).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+allCells.get(i).getCoordsy(), true);
				}else {
					allowEditConfigs.put(allCells.get(i).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+allCells.get(i).getCoordsy(), false);
				}
				extraCustomCellConfig.put("cellExtend", allCells.get(i).getCellExtend());
				extraCustomCellConfig.put("transferType", allCells.get(i).getTransferType());
				extraCustomCellConfig.put("multiple", allCells.get(i).getMultiple());
//				extraCustomCellConfig.put("digit", allCells.get(i).getFormsAttrs());
				extraCustomCellConfigs.put(allCells.get(i).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+allCells.get(i).getCoordsy(), extraCustomCellConfig);
			}
		}
		result.setExtraCustomCellConfigs(extraCustomCellConfigs);
		result.setAllowEditConfigs(allowEditConfigs);
		int t = 0;
		List<JSONObject> images = new ArrayList<>();
		maxXAndY.put("maxX", 0);//最大横坐标
		maxXAndY.put("maxY", 0);//最大纵坐标
		for (int i = 0; i < sortedBindData.size(); i++) {
//			if(cellBindData.get(sortedBindData.get(i).getCoordsx()+"_"+sortedBindData.get(i).getCoordsy()) != null)
			LuckySheetBindData curCellBindData = cellBindData.get(sortedBindData.get(i).getCoordsx()+"_"+sortedBindData.get(i).getCoordsy());
			if(curCellBindData!=null && curCellBindData.getIsRelyCalculated() != 1)//未计算过的才计算
			   //if(cellBindData.get(sortedBindData.get(i).getCoordsx()+"_"+sortedBindData.get(i).getCoordsy()) != null)
			{
				if(t == 0)
				{
					startXAndY.put("x", sortedBindData.get(i).getCoordsx());
					startXAndY.put("y", sortedBindData.get(i).getCoordsy());
					t = t + 1;
				}
				Object dataRowLen = configRowLen.get(String.valueOf(sortedBindData.get(i).getCoordsx()));
				Object dataColLen = configColumnLen.get(String.valueOf(sortedBindData.get(i).getCoordsy()));
				this.processBindData(sortedBindData.get(i), maxCoordinate,cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, columnlen, 
						mergeMap, objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, calcChain, configRowLen, configColumnLen, 
						images, cellBindData,usedCells,dicts,nowFunction,functionCellFormat,chartCells,dataVerification,drillCells,rowhidden,
						colhidden,datasetNameIdMap,columnNames,subtotalCellDatas,subtotalRows,subtotalCellMap,cellConditionFormat,dynamicRange,subTotalDigits,coverCells
						,columnStartCoords,extendCellOrigin,subTotalCellCoords,isExport,dictCache,userInfoDto,viewParams);
			}
//			if(CellValueTypeEnum.FIXED.getCode().intValue() == sortedBindData.get(i).getCellValueType())
//			{//固定值
//				this.processFixedValue(maxCoordinate, sortedBindData.get(i), mergeMap,configRowLen, configColumnLen, 
//					rowlen, columnlen, cellDatas, hyperlinks,dataRowLen,dataColLen,maxXAndY,borderInfo,borderConfig,borderInfos,calcChain
//					,images);
//			}else if(CellValueTypeEnum.BLOCK.getCode().intValue() == sortedBindData.get(i).getCellValueType())
//			{
//				this.processBlocks(maxCoordinate, sortedBindData.get(i), mergeMap,configRowLen, configColumnLen, 
//						rowlen, columnlen, cellDatas, hyperlinks,dataRowLen,dataColLen,maxXAndY,borderInfo,borderConfig,borderInfos,calcChain,objectMapper,
//						images);
//			}
//			else {//动态值
////				if(((Map<String, Object>)sortedBindData.get(i).getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).containsKey(LuckySheetPropsEnum.MERGECELLS.getCode())
////						&& sortedBindData.get(i).getCellExtend().intValue() != CellExtendEnum.NOEXTEND.getCode().intValue())
////				{
////					((Map<String, Object>)sortedBindData.get(i).getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).remove(LuckySheetPropsEnum.MERGECELLS.getCode());
////				}
//				if(AggregateTypeEnum.SUMMARY.getCode().equals(sortedBindData.get(i).getAggregateType()))
//				{
//					this.processSummaryValue(maxCoordinate, sortedBindData.get(i), cellDatas, rowlen, columnlen,dataRowLen,dataColLen,maxXAndY,
//							borderInfo,borderConfig,borderInfos,mergeMap,configRowLen,configColumnLen);
//				}else if(AggregateTypeEnum.GROUPSUMMARY.getCode().equals(sortedBindData.get(i).getAggregateType())) {
//					this.processGroupSummaryValue(maxCoordinate, sortedBindData.get(i), mergeMap, cellDatas, configRowLen, configColumnLen, rowlen, columnlen, objectMapper, dataRowLen, dataColLen,maxXAndY,borderInfo,borderConfig,borderInfos);
//				}else {
//					this.processListGroupValue(maxCoordinate, sortedBindData.get(i), cellDatas, hyperlinks, dataRowLen, dataColLen, 
//							rowlen, columnlen, mergeMap, objectMapper,maxXAndY,borderInfo,borderConfig,borderInfos,calcChain,
//							configRowLen,configColumnLen,images,cellBindData);
//				}
//			}
		}
		if(!StringUtil.isEmptyMap(borderInfo))
		{
			borderInfo.forEach((key, value) -> {
				if(value != null) {
					borderInfos.addAll((Collection<? extends Object>) value);
				}
			});
		}
		result.setFunctionCellFormat(functionCellFormat);
		result.setNowFunction(nowFunction);
		result.setCellDatas(cellDatas);
		result.setHyperlinks(hyperlinks);
		result.setStartXAndY(startXAndY);
		result.setColhidden(JSONObject.parseObject(JSON.toJSONString(colhidden)));
		result.setRowhidden(JSONObject.parseObject(JSON.toJSONString(rowhidden)));
		result.setDataVerification(dataVerification);
		Map<String, Object> newConfig = new HashMap<String, Object>();
		newConfig.put(LuckySheetPropsEnum.ROWLEN.getCode(), rowlen);
		newConfig.put(LuckySheetPropsEnum.COLUMNLEN.getCode(), columnlen);
		newConfig.put(LuckySheetPropsEnum.MERGECONFIG.getCode(), mergeMap);
		newConfig.put(LuckySheetPropsEnum.ROWHIDDEN.getCode(), rowhidden);
		newConfig.put(LuckySheetPropsEnum.COLHIDDEN.getCode(), colhidden);
		newConfig.put("authority", authority);
		for(String key:configColumnLen.keySet()) {
			if(!columnlen.containsKey(key)) {
				columnlen.put(key, configColumnLen.get(key));
			}
		}
		if(!ListUtil.isEmpty(borderInfos))
		{
			newConfig.put(LuckySheetPropsEnum.BORDERINFO.getCode(), borderInfos);
		}
		result.setConfig(newConfig);
		result.setMaxXAndY(maxXAndY);
		result.setExtendCellOrigin(extendCellOrigin);
		result.setColumnStartCoords(columnStartCoords);
		result.setCalcChain(calcChain);
		result.setImageDatas(images);
		result.setChartCells(chartCells);
		result.setDrillCells(drillCells);
		result.setCellBindData(cellBindData);
		JSONArray wrapDatas = new JSONArray();
		if(!StringUtil.isEmptyMap(dynamicRange)) {
			for(String key : dynamicRange.keySet()){
				wrapDatas.add(dynamicRange.get(key));
			}
		}
		result.setWrapDatas(wrapDatas);
		return result;
	}
	
	private void processBindData(LuckySheetBindData bindData,Map<String, Integer> maxCoordinate,
			List<Map<String, Object>> cellDatas,Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen
			,Map<String, Object> rowlen,Map<String, Object> columnlen,Map<String, Map<String, Object>> mergeMap,
			ObjectMapper objectMapper,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos
			,List<JSONObject> calcChain,Map<String, Object> configRowLen,Map<String, Object> configColumnLen, List<JSONObject> images,Map<String, LuckySheetBindData> cellBindData
			,Map<String, Map<String, Object>> usedCells,Map<String, Map<String, String>> dicts,Map<String, Object> nowFunction,Map<String, Object> functionCellFormat,JSONObject chartCells
			,JSONObject dataVerification,JSONObject drillCells,Object rowhidden,Object colhidden,Map<String, String> datasetNameIdMap,List<List<String>> columnNames,
			Map<String, Object> subtotalCellDatas,Map<String, JSONObject> subtotalRows,Map<String, Object> subtotalCellMap,Map<String, JSONArray> cellConditionFormat
			,JSONObject dynamicRange,Map<String, JSONObject> subTotalDigits,Map<String, LuckySheetBindData> coverCells
			,Map<String, JSONObject> columnStartCoords,Map<String, JSONObject> extendCellOrigin,List<String> subTotalCellCoords,boolean isExport,Map<String, Map<String, String>> dictCache,UserInfoDto userInfoDto,Map<String, Object> viewParams) throws JsonMappingException, JsonProcessingException {
		if(CellValueTypeEnum.FIXED.getCode().intValue() == bindData.getCellValueType())
		{//固定值
			this.processFixedValue(maxCoordinate, bindData, mergeMap,configRowLen, configColumnLen, 
				rowlen, columnlen, cellDatas, hyperlinks,dataRowLen,dataColLen,maxXAndY,borderInfo,borderConfig,borderInfos,calcChain
				,images,objectMapper,usedCells,nowFunction,chartCells,dataVerification,rowhidden,colhidden,cellConditionFormat,subtotalCellDatas,subtotalRows,subTotalDigits,coverCells
				,columnStartCoords,extendCellOrigin,dynamicRange,subTotalCellCoords,isExport,userInfoDto,viewParams);
		}else if(CellValueTypeEnum.BLOCK.getCode().intValue() == bindData.getCellValueType())
		{
			this.processBlocks(maxCoordinate, bindData, mergeMap,configRowLen, configColumnLen, 
					rowlen, columnlen, cellDatas, hyperlinks,dataRowLen,dataColLen,maxXAndY,borderInfo,borderConfig,borderInfos,calcChain,objectMapper,
					images,usedCells,dataVerification,rowhidden,colhidden,datasetNameIdMap,columnNames,dynamicRange);
		}
		else {//动态值
//			if(((Map<String, Object>)sortedBindData.get(i).getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).containsKey(LuckySheetPropsEnum.MERGECELLS.getCode())
//					&& sortedBindData.get(i).getCellExtend().intValue() != CellExtendEnum.NOEXTEND.getCode().intValue())
//			{
//				((Map<String, Object>)sortedBindData.get(i).getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).remove(LuckySheetPropsEnum.MERGECELLS.getCode());
//			}
			if(AggregateTypeEnum.SUMMARY.getCode().equals(bindData.getAggregateType()))
			{
				boolean flag = this.calculateOriginCellsExtend(bindData, usedCells);
				this.processSummaryValue(maxCoordinate, bindData, cellDatas, rowlen, columnlen,dataRowLen,dataColLen,maxXAndY,
						borderInfo,borderConfig,borderInfos,mergeMap,configRowLen,configColumnLen,objectMapper,usedCells,flag,nowFunction,dataVerification,cellConditionFormat
						,columnStartCoords,extendCellOrigin);
			}else if(AggregateTypeEnum.GROUPSUMMARY.getCode().equals(bindData.getAggregateType())) {
				this.processGroupSummaryValue(maxCoordinate, bindData, mergeMap, cellDatas, configRowLen, configColumnLen, rowlen, columnlen, 
						objectMapper, dataRowLen, dataColLen,maxXAndY,borderInfo,borderConfig,borderInfos,usedCells,hyperlinks,calcChain,
						images,cellBindData,dicts,nowFunction,dataVerification,drillCells,subtotalCellDatas,subtotalRows,cellConditionFormat,coverCells
						,columnStartCoords,extendCellOrigin,subTotalCellCoords);
			}else {
				this.processListGroupValue(maxCoordinate, bindData, cellDatas, hyperlinks, dataRowLen, dataColLen, 
						rowlen, columnlen, mergeMap, objectMapper,maxXAndY,borderInfo,borderConfig,borderInfos,calcChain,
						configRowLen,configColumnLen,images,cellBindData,usedCells,dicts,nowFunction,functionCellFormat,
						dataVerification,drillCells,rowhidden,colhidden,subtotalCellDatas,subtotalRows,subtotalCellMap,cellConditionFormat,dynamicRange,subTotalDigits,coverCells
						,columnStartCoords,extendCellOrigin,subTotalCellCoords,isExport,dictCache,userInfoDto,viewParams);
			}
		}
	
	}
	
	/**  
	 * @MethodName: processBlocks
	 * @Description: 循环块处理
	 * @author caiyang
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param mergeMap
	 * @param configRowLen
	 * @param configColumnLen
	 * @param rowlen
	 * @param columnlen
	 * @param cellDatas
	 * @param hyperlinks
	 * @param dataRowLen
	 * @param dataColLen
	 * @param maxXAndY
	 * @param borderInfo
	 * @param borderConfig
	 * @param borderInfos
	 * @param calcChain
	 * @param objectMapper
	 * @throws JsonMappingException
	 * @throws JsonProcessingException 
	 * @return void
	 * @date 2022-12-12 03:22:38 
	 */  
	private void processBlocks(Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,
			Map<String, Map<String, Object>> mergeMap,Map<String, Object> configRowLen,
			Map<String, Object> configColumnLen,Map<String, Object> rowlen,Map<String, Object> columnlen,
			List<Map<String, Object>> cellDatas,Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen,Map<String, Integer> maxXAndY,
			Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos,List<JSONObject> calcChain,ObjectMapper objectMapper
			, List<JSONObject> images,Map<String, Map<String, Object>> usedCells,JSONObject dataVerification,Object rowhidden,Object colhidden,Map<String, String> datasetNameIdMap,
			List<List<String>> columnNames,JSONObject dynamicRange) throws JsonMappingException, JsonProcessingException {
		//循环块
		//获取循环块对应的单元格
		QueryWrapper<LuckysheetReportBlockCell> queryWrapper = new QueryWrapper<LuckysheetReportBlockCell>();
		queryWrapper.eq("report_cell_id", luckySheetBindData.getReportCellId());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		queryWrapper.eq("is_sub_cell", YesNoEnum.NO.getCode());
		queryWrapper.orderByAsc("coordsx","coordsy");
		List<LuckysheetReportBlockCell> luckysheetReportBlockCells = this.iLuckysheetReportBlockCellService.list(queryWrapper);
		//获取子循环块
		queryWrapper = new QueryWrapper<LuckysheetReportBlockCell>();
		queryWrapper.eq("report_cell_id", luckySheetBindData.getReportCellId());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		queryWrapper.eq("is_sub_cell", YesNoEnum.YES.getCode());
		queryWrapper.orderByAsc("coordsx","coordsy");
		List<LuckysheetReportBlockCell> subLuckysheetReportBlockCells = this.iLuckysheetReportBlockCellService.list(queryWrapper);
		if(ListUtil.isNotEmpty(subLuckysheetReportBlockCells)) {
			luckysheetReportBlockCells.add(subLuckysheetReportBlockCells.get(0));
			luckysheetReportBlockCells.sort(Comparator.comparing(LuckysheetReportBlockCell::getCoordsx).thenComparing(LuckysheetReportBlockCell::getCoordsy));
		}
		if(!ListUtil.isEmpty(luckysheetReportBlockCells)) {
			Set<Integer> cols = new HashSet<>();
			for (int i = 0; i < luckysheetReportBlockCells.size(); i++) {
				for (int j = 0; j < luckysheetReportBlockCells.get(i).getColSpan(); j++) {
					cols.add(luckysheetReportBlockCells.get(i).getCoordsy()+j);
				}
			}
			if(!ListUtil.isEmpty(luckySheetBindData.getDatas())) {
				int loopCount = luckySheetBindData.getHloopCount()<1?0:luckySheetBindData.getHloopCount();
				int hloopEmptyCount = luckySheetBindData.getHloopEmptyCount();
				int vloopEmptyCount = luckySheetBindData.getVloopEmptyCount();
				Map<String, Integer> fixedY = new HashMap<String, Integer>();
				String groupProperty = luckySheetBindData.getGroupProperty();
				Map<String, List<Map<String, Object>>> datasCache = new HashMap<>();
				int maxRow = 0;
				int maxCol = 0;
				int startCol = 0;
				int endCol = 0;
				for (int m = 0; m < luckySheetBindData.getDatas().size(); m++) {
					int z = 1;
					if(loopCount > 1) {
						z = (m+1)%loopCount;
					}
					String groupPropertyValue = luckySheetBindData.getDatas().get(m).get(0).get(groupProperty) == null?"":String.valueOf(luckySheetBindData.getDatas().get(m).get(0).get(groupProperty));
					for (int t = 0; t < luckysheetReportBlockCells.size(); t++) {
						List<Map<String, Object>> border = this.getBorderType(borderConfig, luckysheetReportBlockCells.get(t).getCoordsx(), luckysheetReportBlockCells.get(t).getCoordsy());//获取该单元格的边框信息
						Integer maxX = maxXAndY.get("maxX");
						Integer maxY = maxXAndY.get("maxY");
						if(CellValueTypeEnum.FIXED.getCode().intValue() == luckysheetReportBlockCells.get(t).getCellValueType().intValue())
						{
							if(z == 1) {
								maxRow = 0;
								maxCol = 0;
							}
							Map<String, Integer> rowAndCol = null;
							if(m == 0)
							{
								maxRow = this.getMaxRow(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy(),1);
								if(usedCells.containsKey(maxRow+"_"+luckysheetReportBlockCells.get(t).getCoordsy())) {
									rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy(),1,1);
								}else {
									rowAndCol = new HashMap<String, Integer>();
									rowAndCol.put("maxX", maxRow);
									rowAndCol.put("maxY", luckysheetReportBlockCells.get(t).getCoordsy());
								}
								maxRow = rowAndCol.get("maxX");
								maxCol = rowAndCol.get("maxY");
								for (int i = 0; i < luckysheetReportBlockCells.get(t).getColSpan(); i++) {
									fixedY.put(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+(luckysheetReportBlockCells.get(t).getCoordsy()+i), rowAndCol.get("maxY"));
								}
//								fixedY.put(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy(), rowAndCol.get("maxY"));
							}else {
								if(loopCount > 1) {
									if(z == 1) {
										maxRow = this.getMaxRow(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy(),1);
										maxCol = fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy());
									}else {
										if(z == 0) {
											maxRow = this.getMaxRow(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy()+(loopCount-1)*cols.size()+((hloopEmptyCount*(loopCount-1))),1);
											maxCol = fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy()) + (loopCount-1)*cols.size() + hloopEmptyCount*(loopCount-1);
										}else {
											maxRow = this.getMaxRow(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy()+(z-1)*cols.size()+(hloopEmptyCount*(z-1)),1);
											maxCol = fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy()) + (z-1)*cols.size()+ hloopEmptyCount*(z-1);
										}
									}
								}else {
									maxRow = this.getMaxRow(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy(),1);
									maxCol = fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy());
								}
							}
							Map<String, Object> cellData = null;
						    try {
						       cellData = objectMapper.readValue(luckysheetReportBlockCells.get(t).getCellData(), Map.class);
						    } catch (Exception e) {
						       e.printStackTrace();
						    }
						    cellData.put(LuckySheetPropsEnum.R.getCode(), maxRow);
					        cellData.put(LuckySheetPropsEnum.C.getCode(), maxCol);
					        usedCells.put(maxRow + "_" + maxCol,cellData);
					        String property = "";
					        Object value = null;
							if(YesNoEnum.YES.getCode().intValue() == luckysheetReportBlockCells.get(t).getIsFunction().intValue())
					        {
					        	((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.FUNCTION.getCode(), value);
					        	JSONObject jsonObject = new JSONObject();
								jsonObject.put("r", maxRow);
								jsonObject.put("c", maxCol);
								calcChain.add(jsonObject);
					        }
					        if(YesNoEnum.YES.getCode().intValue() == luckysheetReportBlockCells.get(t).getIsDataVerification().intValue())
			        		{
			        			String key = maxRow+"_"+maxCol;
			        			if(!dataVerification.containsKey(key))
			        			{
			        				dataVerification.put(key, objectMapper.readValue(luckysheetReportBlockCells.get(t).getDataVerification(), JSONObject.class));
			        			}
			        		}
					        if(YesNoEnum.YES.getCode().intValue() == luckysheetReportBlockCells.get(t).getIsMerge().intValue())
					        {
					        	((Map<String, Object>)(((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.R.getCode(), maxRow);
					        	((Map<String, Object>)(((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.C.getCode(), fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy()));
					        	Map<String, Object> merge = new HashMap<String, Object>();
								merge.put(LuckySheetPropsEnum.R.getCode(), maxRow);
								merge.put(LuckySheetPropsEnum.C.getCode(), maxCol);
								merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckysheetReportBlockCells.get(t).getRowSpan());
								merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckysheetReportBlockCells.get(t).getColSpan());
								mergeMap.put(String.valueOf(maxRow)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(maxCol), merge);
								if((maxRow + luckysheetReportBlockCells.get(t).getRowSpan()-1)> maxX)
								{
									maxX = maxRow + luckysheetReportBlockCells.get(t).getRowSpan()-1;
								}
								if((maxCol + luckysheetReportBlockCells.get(t).getColSpan()-1)> maxY)
								{
									maxY = maxCol + luckysheetReportBlockCells.get(t).getColSpan()-1;
								}
					        }else {
					        	if(maxRow>maxX)
								{
									maxX = maxRow;
								}
								if(maxCol>maxY)
								{
									maxY = maxCol;
								}
								Object mcDataRowLen = configRowLen.get(String.valueOf(luckysheetReportBlockCells.get(t).getCoordsx()));
								Object mcDataColLen = configColumnLen.get(String.valueOf(luckysheetReportBlockCells.get(t).getCoordsy()));
								if(mcDataRowLen != null)
								{
									if(rowlen.get(String.valueOf(maxRow))==null)
									{
										rowlen.put(String.valueOf(maxRow), mcDataRowLen);
									}
								}
								if(mcDataColLen != null)
								{
									if(columnlen.get(String.valueOf(maxCol))== null)
									{
										columnlen.put(String.valueOf(maxCol), mcDataColLen);
									}
								}
					        }
					        this.processDynamicRange(luckySheetBindData, dynamicRange, maxRow, maxCol, cellData);
							cellDatas.add(cellData);
							if(YesNoEnum.YES.getCode().intValue() == luckysheetReportBlockCells.get(t).getIsMerge().intValue()) {
								for (int k = 0; k < luckysheetReportBlockCells.get(t).getRowSpan(); k++) {
									for (int k2 = 0; k2 < luckysheetReportBlockCells.get(t).getColSpan(); k2++) {
										if(k != 0 || k2 != 0)
										{
											Map<String, Object> mc = new HashMap<String, Object>();
											mc.put(LuckySheetPropsEnum.R.getCode(), maxRow);
											mc.put(LuckySheetPropsEnum.C.getCode(), maxCol);
											Map<String, Object> cellValue = new HashMap<String, Object>();
											cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
											Map<String, Object> mergeCellData = new HashMap<String, Object>();
											mergeCellData.put(LuckySheetPropsEnum.R.getCode(), maxRow+k);
											mergeCellData.put(LuckySheetPropsEnum.C.getCode(), maxCol+k2);
											mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
											cellDatas.add(mergeCellData);
											Object mcDataRowLen = configRowLen.get(String.valueOf(luckysheetReportBlockCells.get(t).getCoordsx()+k));
											Object mcDataColLen = configColumnLen.get(String.valueOf(luckysheetReportBlockCells.get(t).getCoordsy()+k2));
											if(mcDataRowLen != null)
											{
												if(rowlen.get(String.valueOf(maxRow+k))==null)
												{
													rowlen.put(String.valueOf(maxRow+k), mcDataRowLen);
												}
											}
											if(mcDataColLen != null)
											{
												if(columnlen.get(String.valueOf(maxCol+k2))== null)
												{
													columnlen.put(String.valueOf(maxCol+k2), mcDataColLen);
												}
											}
											usedCells.put((maxRow+k)+"_"+(maxCol+k2), cellData);
										}
									}
								}
							}
							if(YesNoEnum.YES.getCode().intValue() == luckysheetReportBlockCells.get(t).getIsLink().intValue())
							{
								Map<String, Object> linkConfig = objectMapper.readValue(luckysheetReportBlockCells.get(t).getLinkConfig(), Map.class);
								hyperlinks.put(String.valueOf(maxRow)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()
									+String.valueOf(maxCol), linkConfig);
							}
							String borderKey = maxRow+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+maxCol;
							this.borderProcess(border, maxRow, maxRow+luckysheetReportBlockCells.get(t).getRowSpan()-1, maxCol, maxCol+luckysheetReportBlockCells.get(t).getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//							List<Map<String, Object>> cellBorder = this.borderProcess(border, maxRow, maxRow+luckysheetReportBlockCells.get(t).getRowSpan()-1, fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy()), fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy())+luckysheetReportBlockCells.get(t).getColSpan()-1);
//							if(!ListUtil.isEmpty(cellBorder))
//							{
//								//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//								borderInfos.addAll(cellBorder);
//							}
							if(m == 0)
							{
								for (int i = 0; i < luckysheetReportBlockCells.get(t).getRowSpan(); i++) {
									maxCoordinate.put("x-"+(luckysheetReportBlockCells.get(t).getCoordsx()+i), maxCol+luckysheetReportBlockCells.get(t).getColSpan());
								}
							}
							for (int i = 0; i < luckysheetReportBlockCells.get(t).getColSpan(); i++) {
								if(z == 0) {
//									maxRow = this.getMaxRow(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy()+(index-1)*cols.size(),1);
//									maxCol = fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy()) + (index-1)*cols.size();
									String key = "y-"+(luckysheetReportBlockCells.get(t).getCoordsy()+i+ (loopCount-1)*cols.size()+(hloopEmptyCount*(loopCount-1)));
									if(!maxCoordinate.containsKey(key) || maxRow+luckysheetReportBlockCells.get(t).getRowSpan()>maxCoordinate.get(key)) {
										maxCoordinate.put(key, maxRow+luckysheetReportBlockCells.get(t).getRowSpan());
									}
								}else {
//									maxRow = this.getMaxRow(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy()+(z-1)*cols.size(),1);
//									maxCol = fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy()) + (z-1)*cols.size();
									String key = "y-"+(luckysheetReportBlockCells.get(t).getCoordsy()+i+ (z-1)*cols.size()+hloopEmptyCount*(z-1));
									if(!maxCoordinate.containsKey(key) || maxRow+luckysheetReportBlockCells.get(t).getRowSpan()>maxCoordinate.get(key)) {
										maxCoordinate.put(key, maxRow+luckysheetReportBlockCells.get(t).getRowSpan());
									}
								}
//								maxCoordinate.put("y-"+(luckysheetReportBlockCells.get(t).getCoordsy()+i), maxRow+luckysheetReportBlockCells.get(t).getRowSpan());
							}
							maxXAndY.put("maxX", maxX);
							maxXAndY.put("maxY", maxY);
						}else {
							if(m == 0)
							{
								Map<String, String> map = ListUtil.getNewCellValue(luckysheetReportBlockCells.get(t).getCellValue(), columnNames, datasetNameIdMap);
								luckysheetReportBlockCells.get(t).setCellValue(map.get("cellValue"));
								luckysheetReportBlockCells.get(t).setDatasetName(map.get("datasetNames"));
							}
							List<Map<String, Object>> datas = null;
							if(StringUtil.isNotEmpty(groupPropertyValue)) {
								if(datasCache.containsKey(luckysheetReportBlockCells.get(t).getDatasetName()+"_"+groupPropertyValue))
								{
									datas = datasCache.get(luckysheetReportBlockCells.get(t).getDatasetName()+"_"+groupPropertyValue);
								}else {
									List<List<Map<String, Object>>> datasetDatas = luckySheetBindData.getBlockDatas().get(luckysheetReportBlockCells.get(t).getDatasetName());
									if(ListUtil.isNotEmpty(datasetDatas)) {
										for (int i = 0; i < datasetDatas.size(); i++) {
											String groupPropertyValue2 = datasetDatas.get(i).get(0).get(groupProperty) == null?"":String.valueOf(datasetDatas.get(i).get(0).get(groupProperty));
											if(groupPropertyValue.equals(groupPropertyValue2)) {
												datas = datasetDatas.get(i);
												datasCache.put(luckysheetReportBlockCells.get(t).getDatasetName()+"_"+groupPropertyValue, datas);
												break;
											}
										}
									}
								}
							}else {
								datas = luckySheetBindData.getDatas().get(m);
							}
							
							if(ListUtil.isNotEmpty(datas))
							{
								if(luckysheetReportBlockCells.get(t).getIsSubCell().intValue() == 1) {
									for (int j = 0; j < datas.size(); j++) {
										for (int i = 0; i < subLuckysheetReportBlockCells.size(); i++) {
											if(m == 0&&j==0)
											{
				 								Map<String, Integer> rowAndCol = this.getMaxRowAndCol(maxCoordinate, subLuckysheetReportBlockCells.get(i).getCoordsx(),subLuckysheetReportBlockCells.get(i).getCoordsy(),1,1);
				 								maxRow = rowAndCol.get("maxX");
												maxCol = rowAndCol.get("maxY");
												for (int k = 0; k < subLuckysheetReportBlockCells.get(i).getColSpan(); k++) {
													fixedY.put(subLuckysheetReportBlockCells.get(i).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+(subLuckysheetReportBlockCells.get(i).getCoordsy()+k), rowAndCol.get("maxY"));
												}
//												fixedY.put(subLuckysheetReportBlockCells.get(i).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+subLuckysheetReportBlockCells.get(i).getCoordsy(), rowAndCol.get("maxY"));
											}else {
//												maxRow = this.getMaxRow(maxCoordinate, subLuckysheetReportBlockCells.get(i).getCoordsx(),subLuckysheetReportBlockCells.get(i).getCoordsy(),1);
//												maxCol = fixedY.get(subLuckysheetReportBlockCells.get(i).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+subLuckysheetReportBlockCells.get(i).getCoordsy());
												if(loopCount > 1) {
													if(z == 1) {
														maxRow = this.getMaxRow(maxCoordinate, subLuckysheetReportBlockCells.get(i).getCoordsx(),subLuckysheetReportBlockCells.get(i).getCoordsy(),1);
														maxCol = fixedY.get(subLuckysheetReportBlockCells.get(i).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+subLuckysheetReportBlockCells.get(i).getCoordsy());
													}else {
														if(z == 0) {
															maxRow = this.getMaxRow(maxCoordinate, subLuckysheetReportBlockCells.get(i).getCoordsx(),subLuckysheetReportBlockCells.get(i).getCoordsy()+(loopCount-1)*cols.size()+(hloopEmptyCount*(loopCount-1)),1);
															maxCol = fixedY.get(subLuckysheetReportBlockCells.get(i).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+subLuckysheetReportBlockCells.get(i).getCoordsy()) + (loopCount-1)*cols.size() + hloopEmptyCount*(loopCount-1);
														}else {
															maxRow = this.getMaxRow(maxCoordinate, subLuckysheetReportBlockCells.get(i).getCoordsx(),subLuckysheetReportBlockCells.get(i).getCoordsy()+(z-1)*cols.size()+(hloopEmptyCount*(z-1)),1);
															maxCol = fixedY.get(subLuckysheetReportBlockCells.get(i).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+subLuckysheetReportBlockCells.get(i).getCoordsy()) + (z-1)*cols.size()+ hloopEmptyCount*(z-1);
														}
													}
												}else {
													maxRow = this.getMaxRow(maxCoordinate, subLuckysheetReportBlockCells.get(i).getCoordsx(),subLuckysheetReportBlockCells.get(i).getCoordsy(),1);
													maxCol = fixedY.get(subLuckysheetReportBlockCells.get(i).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+subLuckysheetReportBlockCells.get(i).getCoordsy());
												}
											}
											Map<String, Object> cellData = null;
										    try {
										       cellData = objectMapper.readValue(subLuckysheetReportBlockCells.get(i).getCellData(), Map.class);
			 							    } catch (Exception e) {
										       e.printStackTrace();
										    }
										    cellData.put(LuckySheetPropsEnum.R.getCode(), maxRow);
			 						        cellData.put(LuckySheetPropsEnum.C.getCode(), maxCol);
									        usedCells.put(maxRow + "_" + maxCol,cellData);
									        String property = "";
			 						        Object value = null;
			 						        boolean isImg = false;
			 						       if(CellValueTypeEnum.VARIABLE.getCode().intValue() == subLuckysheetReportBlockCells.get(i).getCellValueType().intValue()) {
									        	property = subLuckysheetReportBlockCells.get(i).getCellValue().replaceAll(subLuckysheetReportBlockCells.get(i).getDatasetName()+".", "").replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
									        	Map<String, Object> mapData = ListUtil.getProperties(property, datas.get(j));
									        	Set<String> set = mapData.keySet();
									        	boolean isJustProperty = false;//是否只有数据库中的属性
									        	for (String o : set) {
									            	property = property.replace(o, mapData.get(o) == null?"":String.valueOf(mapData.get(o)));
									            	if(set.size() == 1)
									            	{
									            		if(property.equals("") || property.length() == String.valueOf(mapData.get(o)).length())
									            		{
									            			isJustProperty = true;
									            		}
									            	}
									            }
									            try {
									            	if(set.size() > 1)
								                	{
									            		if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
									            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
									            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
									            			value = AviatorEvaluator.execute(property);
														}else {
								    						value = property;
								    					}
								                		if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
								                		{
								                			value = 0;
								                		}
								                	}else {
								                		if(StringUtil.isImgUrl(property))
								                		{
								                			value = luckySheetBindData.getTplType() == 1?"":property;
								        					isImg = true;
								                		}else if(!isJustProperty)
								                		{
								                			if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
								                				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
								                				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
								                				value = AviatorEvaluator.execute(property);
								        					}else {
								        						value = property;
								        					}
								                			if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
								                    		{
								                    			value = 0;
								                    		}
								                		}
								                		else {
								                			value = property;
								                		}
								                	}
									    		} catch (Exception e) {
									    			
									    		}
									            if(value == null)
									            {
//									            	value = property;
									            }
									            String format = LuckysheetUtil.getCellFormat(JSONObject.parseObject(subLuckysheetReportBlockCells.get(i).getCellData(), Map.class));
									        	Object v = LuckysheetUtil.formatValue(format, value);
									        	((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
											    ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);
			 						       }
			 						      if(YesNoEnum.YES.getCode().intValue() == subLuckysheetReportBlockCells.get(i).getIsFunction().intValue())
									        {
									        	((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.FUNCTION.getCode(), value);
									        	JSONObject jsonObject = new JSONObject();
												jsonObject.put("r", maxRow);
												jsonObject.put("c", maxCol);
												calcChain.add(jsonObject);
									        }
									        if(YesNoEnum.YES.getCode().intValue() == subLuckysheetReportBlockCells.get(i).getIsDataVerification().intValue())
							        		{
							        			String key = maxRow+"_"+maxCol;
							        			if(!dataVerification.containsKey(key))
							        			{
							        				dataVerification.put(key, objectMapper.readValue(subLuckysheetReportBlockCells.get(i).getDataVerification(), JSONObject.class));
							        			}
							        		}
									        if(YesNoEnum.YES.getCode().intValue() == subLuckysheetReportBlockCells.get(i).getIsMerge().intValue())
									        {
									        	((Map<String, Object>)(((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.R.getCode(), maxRow);
									        	((Map<String, Object>)(((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.C.getCode(), maxCol);
									        	Map<String, Object> merge = new HashMap<String, Object>();
												merge.put(LuckySheetPropsEnum.R.getCode(), maxRow);
												merge.put(LuckySheetPropsEnum.C.getCode(), maxCol);
												merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), subLuckysheetReportBlockCells.get(i).getRowSpan());
												merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), subLuckysheetReportBlockCells.get(i).getColSpan());
												mergeMap.put(String.valueOf(maxRow)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(maxCol), merge);
												if((maxRow + subLuckysheetReportBlockCells.get(i).getRowSpan()-1)> maxX)
												{
													maxX = maxRow + subLuckysheetReportBlockCells.get(i).getRowSpan()-1;
												}
												if((maxCol + subLuckysheetReportBlockCells.get(i).getColSpan()-1)> maxY)
												{
													maxY = maxCol + subLuckysheetReportBlockCells.get(i).getColSpan()-1;
												}
									        }else {
									        	if(maxRow>maxX)
												{
													maxX = maxRow;
												}
												if(maxCol>maxY)
												{
													maxY = maxCol;
												}
									        }
									        if(YesNoEnum.YES.getCode().intValue() == subLuckysheetReportBlockCells.get(i).getIsMerge().intValue()) {
												for (int k = 0; k < subLuckysheetReportBlockCells.get(i).getRowSpan(); k++) {
													for (int k2 = 0; k2 < subLuckysheetReportBlockCells.get(i).getColSpan(); k2++) {
														if(k != 0 || k2 != 0)
														{
															Map<String, Object> mc = new HashMap<String, Object>();
															mc.put(LuckySheetPropsEnum.R.getCode(), maxRow);
															mc.put(LuckySheetPropsEnum.C.getCode(), maxCol);
															Map<String, Object> cellValue = new HashMap<String, Object>();
															cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
															Map<String, Object> mergeCellData = new HashMap<String, Object>();
															mergeCellData.put(LuckySheetPropsEnum.R.getCode(), maxRow+k);
															mergeCellData.put(LuckySheetPropsEnum.C.getCode(), maxCol+k2);
															mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
															cellDatas.add(mergeCellData);
															Object mcDataRowLen = configRowLen.get(String.valueOf(subLuckysheetReportBlockCells.get(i).getCoordsx()+k));
															Object mcDataColLen = configColumnLen.get(String.valueOf(subLuckysheetReportBlockCells.get(i).getCoordsy()+k2));
															if(mcDataRowLen != null)
															{
																if(rowlen.get(String.valueOf(maxRow+k))==null)
																{
																	rowlen.put(String.valueOf(maxRow+k), mcDataRowLen);
																}
															}
															if(mcDataColLen != null)
															{
																if(columnlen.get(String.valueOf(maxCol+k2))== null)
																{
																	columnlen.put(String.valueOf(maxCol+k2), mcDataColLen);
																}
															}
															usedCells.put((maxRow+k)+"_"+(maxCol+k2), cellData);
														}
													}
												}
											}else {
												Object mcDataRowLen = configRowLen.get(String.valueOf(subLuckysheetReportBlockCells.get(i).getCoordsx()));
												if(mcDataRowLen != null)
												{
													if(rowlen.get(String.valueOf(maxRow))==null)
													{
														rowlen.put(String.valueOf(maxRow), mcDataRowLen);
													}
												}
												Object mcDataColLen = configColumnLen.get(String.valueOf(subLuckysheetReportBlockCells.get(i).getCoordsy()));
												if(mcDataColLen != null)
												{
													if(columnlen.get(String.valueOf(maxCol))== null)
													{
														columnlen.put(String.valueOf(maxCol), mcDataColLen);
													}
												}
											}
											if(isImg)
											{
												double top = LuckysheetUtil.calculateTop(rowlen, maxRow,rowhidden);
												double left = LuckysheetUtil.calculateLeft(columnlen, maxCol,colhidden);
												Object width = LuckysheetUtil.calculateWidth(columnlen, maxCol, subLuckysheetReportBlockCells.get(i).getColSpan());
												Object height = LuckysheetUtil.calculateHeight(rowlen, maxRow, subLuckysheetReportBlockCells.get(i).getRowSpan());
												JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
												imgInfo.getJSONObject("default").put("top", top);
												imgInfo.getJSONObject("default").put("left", left);
												imgInfo.getJSONObject("default").put("width", width);
												imgInfo.getJSONObject("default").put("height", height);
												imgInfo.getJSONObject("crop").put("width", width);
												imgInfo.getJSONObject("crop").put("height", height);
												imgInfo.put("src", property);
												JSONObject img = new JSONObject();
												img.put("imgInfo", imgInfo);
												img.put("r", maxRow);
												img.put("c", maxCol);
												Object mcDataRowLen = configRowLen.get(String.valueOf(subLuckysheetReportBlockCells.get(i).getCoordsx()));
												if(mcDataRowLen != null)
												{
													img.put("height", mcDataRowLen);
												}else {
													img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
												}
												Object mcDataColLen = configColumnLen.get(String.valueOf(subLuckysheetReportBlockCells.get(i).getCoordsy()));
												if(mcDataColLen != null)
												{
													img.put("width", mcDataColLen);
												}else {
													img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
												}
												if(YesNoEnum.YES.getCode().intValue() == subLuckysheetReportBlockCells.get(i).getIsMerge()) {
													img.put("isMerge", YesNoEnum.YES.getCode().intValue());
													img.put("rowSpan", subLuckysheetReportBlockCells.get(i).getRowSpan());
													img.put("colSpan", subLuckysheetReportBlockCells.get(i).getColSpan());
												}else {
													img.put("isMerge", YesNoEnum.NO.getCode().intValue());
												}
												img.put("extend", 1);
												images.add(img);
											}
											cellDatas.add(cellData);
											
											if(YesNoEnum.YES.getCode().intValue() == subLuckysheetReportBlockCells.get(i).getIsLink().intValue())
											{
												if(CellValueTypeEnum.FIXED.getCode().intValue() == subLuckysheetReportBlockCells.get(i).getCellValueType().intValue())
												{
													Map<String, Object> linkConfig = objectMapper.readValue(subLuckysheetReportBlockCells.get(i).getLinkConfig(), Map.class);
													hyperlinks.put(String.valueOf(maxRow)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()
														+String.valueOf(maxCol), linkConfig);
												}else {
													 Map<String, Object> linkConfig = objectMapper.readValue(subLuckysheetReportBlockCells.get(i).getLinkConfig(), Map.class);
													 String linkAddress = String.valueOf(linkConfig.get(LuckySheetPropsEnum.LINKADDRESS.getCode()));
											         String newLinkAddress = UrlUtils.appendParam(linkAddress, property, String.valueOf(datas.get(j).get(property)));
											         Map<String, Object> hyperLink = new HashMap<String, Object>();
											         hyperLink.put(LuckySheetPropsEnum.LINKADDRESS.getCode(), newLinkAddress);
											         hyperLink.put(LuckySheetPropsEnum.LINKTYPE.getCode(), linkConfig.get(LuckySheetPropsEnum.LINKTYPE.getCode()));
											         hyperLink.put(LuckySheetPropsEnum.LINKTOOLTIP.getCode(), linkConfig.get(LuckySheetPropsEnum.LINKTOOLTIP.getCode()));
											         hyperlinks.put(String.valueOf(maxRow)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(maxCol), hyperLink);
												}
											}
											String borderKey = maxRow+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+maxCol;
											this.borderProcess(border, maxRow, maxRow+subLuckysheetReportBlockCells.get(i).getRowSpan()-1, maxCol, maxCol+subLuckysheetReportBlockCells.get(i).getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
											if(m == 0)
											{
												for (int k = 0; k < subLuckysheetReportBlockCells.get(i).getRowSpan(); k++) {
													maxCoordinate.put("x-"+(subLuckysheetReportBlockCells.get(i).getCoordsx()+k), maxCol+subLuckysheetReportBlockCells.get(i).getColSpan());
												}
											}
											for (int k = 0; k < subLuckysheetReportBlockCells.get(i).getColSpan(); k++) {
												if(z == 0) {
													String key = "y-"+(subLuckysheetReportBlockCells.get(i).getCoordsy()+k+ (loopCount-1)*cols.size()+(hloopEmptyCount*(loopCount-1)));
													if(!maxCoordinate.containsKey(key) || maxRow+subLuckysheetReportBlockCells.get(i).getRowSpan()>maxCoordinate.get(key)) {
														maxCoordinate.put(key, maxRow+subLuckysheetReportBlockCells.get(i).getRowSpan());
													}
												}else {
													String key = "y-"+(subLuckysheetReportBlockCells.get(i).getCoordsy()+k+ (z-1)*cols.size()+hloopEmptyCount*(z-1));
													if(!maxCoordinate.containsKey(key) || maxRow+subLuckysheetReportBlockCells.get(i).getRowSpan()>maxCoordinate.get(key)) {
														maxCoordinate.put(key, maxRow+subLuckysheetReportBlockCells.get(i).getRowSpan());
													}
												}
											}
											maxXAndY.put("maxX", maxX);
											maxXAndY.put("maxY", maxY);
										}
									}
								}else {
									if(z == 1) {
										maxRow = 0;
										maxCol = 0;
									}
									if(m == 0)
									{
										maxRow = this.getMaxRow(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy(),1);
										Map<String, Integer> rowAndCol = null;
										if(usedCells.containsKey(maxRow+"_"+luckysheetReportBlockCells.get(t).getCoordsy())) {
											rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy(),1,1);
			 								maxRow = rowAndCol.get("maxX");
											maxCol = rowAndCol.get("maxY");	
										}else {
											rowAndCol = new HashMap<String, Integer>();
											rowAndCol.put("maxX", maxRow);
											rowAndCol.put("maxY", luckysheetReportBlockCells.get(t).getCoordsy());
										}
										maxRow = rowAndCol.get("maxX");
										maxCol = rowAndCol.get("maxY");
										for (int i = 0; i < luckysheetReportBlockCells.get(t).getColSpan(); i++) {
											fixedY.put(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+(luckysheetReportBlockCells.get(t).getCoordsy()+i), rowAndCol.get("maxY"));
										}
//										fixedY.put(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy(), rowAndCol.get("maxY"));
									}else {
										if(loopCount > 1) {
											if(z == 1) {
												maxRow = this.getMaxRow(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy(),1);
												maxCol = fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy());
											}else {
												if(z == 0) {
													maxRow = this.getMaxRow(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy()+(loopCount-1)*cols.size()+(hloopEmptyCount*(loopCount-1)),1);
													maxCol = fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy()) + (loopCount-1)*cols.size() + hloopEmptyCount*(loopCount-1);
												}else {
													maxRow = this.getMaxRow(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy()+(z-1)*cols.size()+(hloopEmptyCount*(z-1)),1);
													maxCol = fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy()) + (z-1)*cols.size()+ hloopEmptyCount*(z-1);
												}
											}
										}else {
											maxRow = this.getMaxRow(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy(),1);
											if(fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy())==null) {
												maxCol = this.getMaxCol(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy(),1);
											}else {
												maxCol = fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy());
											}
											
										}
//										maxRow = this.getMaxRow(maxCoordinate, luckysheetReportBlockCells.get(t).getCoordsx(),luckysheetReportBlockCells.get(t).getCoordsy(),1);
//										maxCol = fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy());
									}
									for (int j = 0; j < datas.size(); j++) {
										if(j > 0)
										{
											maxRow = maxRow + luckysheetReportBlockCells.get(t).getRowSpan(); 
										}
										Map<String, Object> cellData = null;
									    try {
									       cellData = objectMapper.readValue(luckysheetReportBlockCells.get(t).getCellData(), Map.class);
		 							    } catch (Exception e) {
									       e.printStackTrace();
									    }
									    cellData.put(LuckySheetPropsEnum.R.getCode(), maxRow);
		 						        cellData.put(LuckySheetPropsEnum.C.getCode(), maxCol);
								        usedCells.put(maxRow + "_" + maxCol,cellData);
		 						        String property = "";
		 						        Object value = null;
		 						        boolean isImg = false;
		  						        if(CellValueTypeEnum.VARIABLE.getCode().intValue() == luckysheetReportBlockCells.get(t).getCellValueType().intValue())
								        {
								        	property = luckysheetReportBlockCells.get(t).getCellValue().replaceAll(luckysheetReportBlockCells.get(t).getDatasetName()+".", "").replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
								        	Map<String, Object> mapData = ListUtil.getProperties(property, datas.get(j));
								        	Set<String> set = mapData.keySet();
								        	boolean isJustProperty = false;//是否只有数据库中的属性
								        	for (String o : set) {
								            	property = property.replace(o, mapData.get(o) == null?"":String.valueOf(mapData.get(o)));
								            	if(set.size() == 1)
								            	{
								            		if(property.equals("") || property.length() == String.valueOf(mapData.get(o)).length())
								            		{
								            			isJustProperty = true;
								            		}
								            	}
								            }
								            try {
								            	if(set.size() > 1)
							                	{
								            		if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
								            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
								            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
								            			value = AviatorEvaluator.execute(property);
													}else {
							    						value = property;
							    					}
							                		if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
							                		{
							                			value = 0;
							                		}
							                	}else {
							                		if(StringUtil.isImgUrl(property))
							                		{
							                			value = luckySheetBindData.getTplType() == 1?"":property;
							        					isImg = true;
							                		}else if(!isJustProperty)
							                		{
							                			if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
							                				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
							                				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
							                				value = AviatorEvaluator.execute(property);
							        					}else {
							        						value = property;
							        					}
							                			if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
							                    		{
							                    			value = 0;
							                    		}
							                		}
							                		else {
							                			value = property;
							                		}
							                	}
								    		} catch (Exception e) {
								    			
								    		}
								            if(value == null)
								            {
//								            	value = property;
								            }
								            String format = LuckysheetUtil.getCellFormat(JSONObject.parseObject(luckysheetReportBlockCells.get(t).getCellData(), Map.class));
								        	Object v = LuckysheetUtil.formatValue(format, value);
								        	((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
										    ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);
								        }
								        if(YesNoEnum.YES.getCode().intValue() == luckysheetReportBlockCells.get(t).getIsFunction().intValue())
								        {
								        	((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.FUNCTION.getCode(), value);
								        	JSONObject jsonObject = new JSONObject();
											jsonObject.put("r", maxRow);
											jsonObject.put("c", maxCol);
											calcChain.add(jsonObject);
								        }
								        if(YesNoEnum.YES.getCode().intValue() == luckysheetReportBlockCells.get(t).getIsDataVerification().intValue())
						        		{
						        			String key = maxRow+"_"+maxCol;
						        			if(!dataVerification.containsKey(key))
						        			{
						        				dataVerification.put(key, objectMapper.readValue(luckysheetReportBlockCells.get(t).getDataVerification(), JSONObject.class));
						        			}
						        		}
								        if(YesNoEnum.YES.getCode().intValue() == luckysheetReportBlockCells.get(t).getIsMerge().intValue())
								        {
								        	((Map<String, Object>)(((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.R.getCode(), maxRow);
								        	((Map<String, Object>)(((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.C.getCode(), fixedY.get(luckysheetReportBlockCells.get(t).getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckysheetReportBlockCells.get(t).getCoordsy()));
								        	Map<String, Object> merge = new HashMap<String, Object>();
											merge.put(LuckySheetPropsEnum.R.getCode(), maxRow);
											merge.put(LuckySheetPropsEnum.C.getCode(), maxCol);
											merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckysheetReportBlockCells.get(t).getRowSpan());
											merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckysheetReportBlockCells.get(t).getColSpan());
											((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), merge);
											mergeMap.put(String.valueOf(maxRow)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(maxCol), merge);
											if((maxRow + luckysheetReportBlockCells.get(t).getRowSpan()-1)> maxX)
											{
												maxX = maxRow + luckysheetReportBlockCells.get(t).getRowSpan()-1;
											}
											if((maxCol + luckysheetReportBlockCells.get(t).getColSpan()-1)> maxY)
											{
												maxY = maxCol + luckysheetReportBlockCells.get(t).getColSpan()-1;
											}
								        }else {
								        	if(maxRow>maxX)
											{
												maxX = maxRow;
											}
											if(maxCol>maxY)
											{
												maxY = maxCol;
											}
											Object mcDataRowLen = configRowLen.get(String.valueOf(luckysheetReportBlockCells.get(t).getCoordsx()));
											if(mcDataRowLen != null)
											{
												if(rowlen.get(String.valueOf(maxRow))==null)
												{
													rowlen.put(String.valueOf(maxRow), mcDataRowLen);
												}
											}
											Object mcDataColLen = configColumnLen.get(String.valueOf(luckysheetReportBlockCells.get(t).getCoordsy()));
											if(mcDataColLen != null)
											{
												if(columnlen.get(String.valueOf(maxCol))== null)
												{
													columnlen.put(String.valueOf(maxCol), mcDataColLen);
												}
											}
								        }
										if(isImg)
										{
											double top = LuckysheetUtil.calculateTop(rowlen, maxRow,rowhidden);
											double left = LuckysheetUtil.calculateLeft(columnlen, maxCol,colhidden);
											Object width = LuckysheetUtil.calculateWidth(columnlen, maxCol, luckysheetReportBlockCells.get(t).getColSpan());
											Object height = LuckysheetUtil.calculateHeight(rowlen, maxRow, luckysheetReportBlockCells.get(t).getRowSpan());
											JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
											imgInfo.getJSONObject("default").put("top", top);
											imgInfo.getJSONObject("default").put("left", left);
											imgInfo.getJSONObject("default").put("width", width);
											imgInfo.getJSONObject("default").put("height", height);
											imgInfo.getJSONObject("crop").put("width", width);
											imgInfo.getJSONObject("crop").put("height", height);
											imgInfo.put("src", property);
											JSONObject img = new JSONObject();
											img.put("imgInfo", imgInfo);
											img.put("r", maxRow);
											img.put("c", maxCol);
											if(dataRowLen != null)
											{
												img.put("height", dataRowLen);
											}else {
												img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
											}
											if(dataColLen != null)
											{
												img.put("width", dataColLen);
											}else {
												img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
											}
											if(YesNoEnum.YES.getCode().intValue() == luckysheetReportBlockCells.get(t).getIsMerge()) {
												img.put("isMerge", YesNoEnum.YES.getCode().intValue());
												img.put("rowSpan", luckysheetReportBlockCells.get(t).getRowSpan());
												img.put("colSpan", luckysheetReportBlockCells.get(t).getColSpan());
											}else {
												img.put("isMerge", YesNoEnum.NO.getCode().intValue());
											}
											img.put("extend", 1);
											images.add(img);
										}
										this.processDynamicRange(luckySheetBindData, dynamicRange, maxRow, maxCol, cellData);
										cellDatas.add(cellData);
										if(YesNoEnum.YES.getCode().intValue() == luckysheetReportBlockCells.get(t).getIsMerge().intValue()) {
											for (int k = 0; k < luckysheetReportBlockCells.get(t).getRowSpan(); k++) {
												for (int k2 = 0; k2 < luckysheetReportBlockCells.get(t).getColSpan(); k2++) {
													if(k != 0 || k2 != 0)
													{
														Map<String, Object> mc = new HashMap<String, Object>();
														mc.put(LuckySheetPropsEnum.R.getCode(), maxRow);
														mc.put(LuckySheetPropsEnum.C.getCode(), maxCol);
														Map<String, Object> cellValue = new HashMap<String, Object>();
														cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
														Map<String, Object> mergeCellData = new HashMap<String, Object>();
														mergeCellData.put(LuckySheetPropsEnum.R.getCode(), maxRow+k);
														mergeCellData.put(LuckySheetPropsEnum.C.getCode(), maxCol+k2);
														mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
														cellDatas.add(mergeCellData);
														Object mcDataRowLen = configRowLen.get(String.valueOf(luckysheetReportBlockCells.get(t).getCoordsx()+k));
														Object mcDataColLen = configColumnLen.get(String.valueOf(luckysheetReportBlockCells.get(t).getCoordsy()+k2));
														if(mcDataRowLen != null)
														{
															if(rowlen.get(String.valueOf(maxRow+k))==null)
															{
																rowlen.put(String.valueOf(maxRow+k), mcDataRowLen);
															}
														}
														if(mcDataColLen != null)
														{
															if(columnlen.get(String.valueOf(maxCol+k2))== null)
															{
																columnlen.put(String.valueOf(maxCol+k2), mcDataColLen);
															}
														}
														usedCells.put((maxRow+k)+"_"+(maxCol+k2), cellData);
													}
												}
											}
										}
										if(YesNoEnum.YES.getCode().intValue() == luckysheetReportBlockCells.get(t).getIsLink().intValue())
										{
											if(CellValueTypeEnum.FIXED.getCode().intValue() == luckysheetReportBlockCells.get(t).getCellValueType().intValue())
											{
												Map<String, Object> linkConfig = objectMapper.readValue(luckysheetReportBlockCells.get(t).getLinkConfig(), Map.class);
												hyperlinks.put(String.valueOf(maxRow)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()
													+String.valueOf(maxCol), linkConfig);
											}else {
												 Map<String, Object> linkConfig = objectMapper.readValue(luckysheetReportBlockCells.get(t).getLinkConfig(), Map.class);
												 String linkAddress = String.valueOf(linkConfig.get(LuckySheetPropsEnum.LINKADDRESS.getCode()));
										         String newLinkAddress = UrlUtils.appendParam(linkAddress, property, String.valueOf(datas.get(j).get(property)));
										         Map<String, Object> hyperLink = new HashMap<String, Object>();
										         hyperLink.put(LuckySheetPropsEnum.LINKADDRESS.getCode(), newLinkAddress);
										         hyperLink.put(LuckySheetPropsEnum.LINKTYPE.getCode(), linkConfig.get(LuckySheetPropsEnum.LINKTYPE.getCode()));
										         hyperLink.put(LuckySheetPropsEnum.LINKTOOLTIP.getCode(), linkConfig.get(LuckySheetPropsEnum.LINKTOOLTIP.getCode()));
										         hyperlinks.put(String.valueOf(maxRow)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(maxCol), hyperLink);
											}
										}
										String borderKey = maxRow+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+maxCol;
										this.borderProcess(border, maxRow, maxRow+luckysheetReportBlockCells.get(t).getRowSpan()-1, maxCol, maxCol+luckysheetReportBlockCells.get(t).getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
										if(m == 0)
										{
											for (int i = 0; i < luckysheetReportBlockCells.get(t).getRowSpan(); i++) {
												maxCoordinate.put("x-"+(luckysheetReportBlockCells.get(t).getCoordsx()+i), maxCol+luckysheetReportBlockCells.get(t).getColSpan());
											}
										}
										for (int i = 0; i < luckysheetReportBlockCells.get(t).getColSpan(); i++) {
											if(z == 0) {
												String key = "y-"+(luckysheetReportBlockCells.get(t).getCoordsy()+i+ (loopCount-1)*cols.size()+(hloopEmptyCount*(loopCount-1)));
												if(!maxCoordinate.containsKey(key) || maxRow+luckysheetReportBlockCells.get(t).getRowSpan()>maxCoordinate.get(key)) {
													maxCoordinate.put(key, maxRow+luckysheetReportBlockCells.get(t).getRowSpan());
												}
											}else {
												String key = "y-"+(luckysheetReportBlockCells.get(t).getCoordsy()+i+ (z-1)*cols.size()+hloopEmptyCount*(z-1));
												if(!maxCoordinate.containsKey(key) || maxRow+luckysheetReportBlockCells.get(t).getRowSpan()>maxCoordinate.get(key)) {
													maxCoordinate.put(key, maxRow+luckysheetReportBlockCells.get(t).getRowSpan());
												}
											}
										}
										maxXAndY.put("maxX", maxX);
										maxXAndY.put("maxY", maxY);
									}
								}
							}
						}
						if(t == 0) {
							startCol = maxCol;
						}
						if(t == luckysheetReportBlockCells.size() - 1) {
							endCol = maxCol+luckysheetReportBlockCells.get(t).getColSpan()-1+hloopEmptyCount;
						}
					}
					//添加空行
					for (int k = startCol; k <= endCol; k++) {
						maxCoordinate.put("y-"+k, maxRow+vloopEmptyCount+1);
					}
				}
			}
		}
	}

	/**  
	 * @Title: processFixedValue
	 * @Description: 单元格固定值处理
	 * @param maxCoordinate 行坐标对应的列最大值，列坐标对应的行最大值
	 * @param luckySheetBindData 绑定的数据
	 * @param mergeMap 合并单元格数据
	 * @param maxX 最大横坐标
	 * @param maxY 最大纵坐标
	 * @param configRowLen 行宽配置
	 * @param configColumnLen 列高配置
	 * @param rowlen 行宽
	 * @param columnlen 列高
	 * @param cellDatas 单元格对应的数据集合
	 * @param hyperlinks 超链接集合
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2022-03-20 10:54:30 
	 */ 
	private void processFixedValue(Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,
			Map<String, Map<String, Object>> mergeMap,Map<String, Object> configRowLen,
			Map<String, Object> configColumnLen,Map<String, Object> rowlen,Map<String, Object> columnlen,
			List<Map<String, Object>> cellDatas,Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen,Map<String, Integer> maxXAndY,
			Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos,List<JSONObject> calcChain, List<JSONObject> images
			,ObjectMapper objectMapper,Map<String, Map<String, Object>> usedCells,Map<String, Object> nowFunction,JSONObject chartCells,JSONObject dataVerification,Object rowhidden,Object colhidden
			,Map<String, JSONArray> cellConditionFormat,Map<String, Object> subtotalCellDatas,Map<String, JSONObject> subtotalRows,Map<String, JSONObject> subTotalDigits,Map<String, LuckySheetBindData> coverCells
			,Map<String, JSONObject> columnStartCoords,Map<String, JSONObject> extendCellOrigin,JSONObject dynamicRange,List<String> subTotalCellCoords,boolean isExport,UserInfoDto userInfoDto,Map<String, Object> viewParams) throws JsonMappingException, JsonProcessingException {
//		List<Map<String, Object>> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		}else {
			border = (List<Map<String, Object>>) borderInfo.get(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy());
		}
		if(luckySheetBindData.getIsRelyCell().intValue() != 1 && "删除".equals(luckySheetBindData.getCellValue())) {
			return;
		}
		Map<String, Integer> rowAndCol = null;
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		if(luckySheetBindData.getCellFillType().intValue() == 2 && luckySheetBindData.getIsRelyCell().intValue() != 1) {
			if(!isEmptyCellV(luckySheetBindData)) {
				rowAndCol = new HashMap<String, Integer>();
				rowAndCol.put("maxX", luckySheetBindData.getCoordsx());
				rowAndCol.put("maxY", luckySheetBindData.getCoordsy());
				Map<String, Object> merge = new HashMap<String, Object>();
				merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
				merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
				merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
				merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
				if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge()) {
					mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
				}
				this.processDynamicRange(luckySheetBindData, dynamicRange, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), luckySheetBindData.getCellData());
				this.processCoverCell(objectMapper, usedCells, luckySheetBindData, cellDatas, calcChain, dataRowLen, rowlen, dataColLen, columnlen, border, maxXAndY, maxCoordinate, borderInfo);
				return;
			}else {
				String key = luckySheetBindData.getCoordsx()+"_"+luckySheetBindData.getCoordsy();
				if(usedCells.containsKey(key)) {
					Map<String, Object> cellData = usedCells.get(key);
					if(cellData.get("v") != null) {
						Map<String, Object> v = (Map<String, Object>) cellData.get("v");
						Object value = v.get("v");
						Map<String, Object> coverCellData = luckySheetBindData.getCellData();
						if(coverCellData.get("v") != null) {
							Map<String, Object> coverCellv = (Map<String, Object>) coverCellData.get("v");
							if(coverCellv.get("v") != null) {
								coverCellv.put("v", value);
								coverCellv.put("m", value);
								cellData.clear();
								cellData.putAll(coverCellData);
							}
						}
					}
					return;
				}
			}
		}
		int verticalDataLenth = 1;
		int horizontalDataLenth = 1;
		//固定值
		String initCalculateSubtotalKey = "";
		boolean isFirst = false;
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			initCalculateSubtotalKey = luckySheetBindData.getSheetId()+"-"+luckySheetBindData.getRelyIndex()+"-"+luckySheetBindData.getCoordsx()+"-"+luckySheetBindData.getCoordsy();
			if(luckySheetBindData.getLastCoordsx() == null)
			{
				isFirst = true;
//				rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
				JSONArray newCoords = getNewCoords(luckySheetBindData.getPriortyMoveDirection().intValue(), luckySheetBindData.getLastCoordsx()==null?luckySheetBindData.getCoordsx():luckySheetBindData.getLastCoordsx(), luckySheetBindData.getLastCoordsy()==null?luckySheetBindData.getCoordsy():luckySheetBindData.getLastCoordsy(), maxCoordinate, usedCells);
				if(!ListUtil.isEmpty(newCoords)) {
					luckySheetBindData.setLastCoordsx(newCoords.getIntValue(0));
					luckySheetBindData.setLastCoordsy(newCoords.getIntValue(1));
				}
				if(ListUtil.isEmpty(newCoords))
				{
					rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
				}else {
					rowAndCol = new HashMap<String, Integer>();
					rowAndCol.put("maxX", newCoords.getIntValue(0));
					rowAndCol.put("maxY", newCoords.getIntValue(1));
				}
			}else {
				rowAndCol = new HashMap<String, Integer>();
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					maxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", maxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()){
					maxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
					rowAndCol.put("maxY", maxY);
				}
			}
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue() && luckySheetBindData.getCoordsx().intValue() == luckySheetBindData.getRelyCoordsx().intValue() && !luckySheetBindData.getIsGroupMerge())
			{
				for (int i = 0; i < luckySheetBindData.getDatas().size(); i++) {
					verticalDataLenth = verticalDataLenth + luckySheetBindData.getDatas().get(i).size();
				}
				verticalDataLenth = verticalDataLenth - 1;
			}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue() && luckySheetBindData.getCoordsy().intValue() == luckySheetBindData.getRelyCoordsy().intValue() && !luckySheetBindData.getIsGroupMerge()) {
				for (int i = 0; i < luckySheetBindData.getDatas().size(); i++) {
					horizontalDataLenth = horizontalDataLenth + luckySheetBindData.getDatas().get(i).size();
				}
				horizontalDataLenth = horizontalDataLenth - 1;
			}
		}else {
			if(luckySheetBindData.getRecalculateCoords().intValue() == 1)
			{
				JSONArray newCoords = getNewCoords(luckySheetBindData.getPriortyMoveDirection().intValue(), luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), maxCoordinate, usedCells);
				if(ListUtil.isEmpty(newCoords))
				{
					rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
				}else {
					rowAndCol = new HashMap<String, Integer>();
					rowAndCol.put("maxX", newCoords.getIntValue(0));
					rowAndCol.put("maxY", newCoords.getIntValue(1));
				}
				
			}else {
				rowAndCol = new HashMap<String, Integer>();
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					maxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", maxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue())
				{
					maxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
					rowAndCol.put("maxY", maxY);
				}
			}
		}
		
		luckySheetBindData.getCellData().put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
		luckySheetBindData.getCellData().put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
		this.processDynamicRange(luckySheetBindData, dynamicRange, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), luckySheetBindData.getCellData());
		this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol,luckySheetBindData.getIsRelyCell());
		this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		boolean isImg = false;
		Map<String, Object> cellConfig = (Map<String, Object>) luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode());
		Object originalValue = cellConfig.get(LuckySheetPropsEnum.CELLVALUE.getCode());
		String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
		if(StringUtil.isNotEmpty(luckySheetBindData.getCellValue()) && FunctionExpressEnum.NOW.getCode().toLowerCase().equals(luckySheetBindData.getCellValue().toLowerCase()))
		{
			Map<String, Object> ct = (Map<String, Object>) cellConfig.get("ct");
			String v = DateUtil.getNow(LuckysheetUtil.getDateFormat(ct));
			cellConfig.put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
			cellConfig.put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);
			nowFunction.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"), cellConfig);
		}else {
			if(originalValue != null)
			{
				Object value = null;
				if(customSpringReportFunction.isSpringReportFunction(String.valueOf(originalValue))) {
		        	Map<String, Object> extraParams = new HashMap<>();
		        	extraParams.put("userInfo", userInfoDto);
		        	extraParams.put("viewParams", viewParams);
		        	luckySheetBindData.setProperty(String.valueOf(originalValue));
		        	value = customSpringReportFunction.calculate(luckySheetBindData, extraParams);
		        }else {
		        	if(StringUtil.isImgUrl(String.valueOf(originalValue)))
					{
						value = luckySheetBindData.getTplType() == 1?"":originalValue;
						isImg = true;
					}else {
						try {
							if(CheckUtil.validate(String.valueOf(originalValue))&&CheckUtil.containsOperator(String.valueOf(originalValue))&&!format.equals(CellFormatEnum.TEXT.getCode())) {
								AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
								AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
								value = AviatorEvaluator.execute(String.valueOf(originalValue));
							}
							if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
	                		{
	                			value = 0;
	                		}
						} catch (Exception e) {
						}
						if(value==null)
						{
							value = originalValue;
						}
						//2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//						if(luckySheetBindData.getWarning())
//						{
//							if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//							{
//								JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//								if(jsonObject != null)
//								{
//									cellConfig.put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//									if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//									{
//										cellConfig.put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//									}else {
//										cellConfig.put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//									}
//								}
//							}
//						}
						if(luckySheetBindData.getUnitTransfer()!=null && luckySheetBindData.getUnitTransfer())
						{
							value = this.processUnitTransfer(value, luckySheetBindData);
						}
					}
		        }
				value = LuckysheetUtil.formatValue(format, value);
				cellConfig.put(LuckySheetPropsEnum.CELLVALUE.getCode(), value);
			}
		}
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue())
		{
			String f = String.valueOf(cellConfig.get("f"));
			f = this.processCellFCustomF(f, luckySheetBindData);
			if(luckySheetBindData.getIsRelyCell().intValue() == 1 && "list".equals(luckySheetBindData.getLastAggregateType())) {
				if(isFirst) {
					String formula = SheetUtil.calculateFormula(String.valueOf(f),0, luckySheetBindData.getRelyCellExtend().intValue()==2?1:2);
					cellConfig.put("f", formula);
				}else {
					String formula = SheetUtil.calculateFormula(String.valueOf(f),luckySheetBindData.getRowSpan(), luckySheetBindData.getRelyCellExtend().intValue()==2?1:2);
					cellConfig.put("f", formula);
				}
			}else {
				cellConfig.put("f", f);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("r", rowAndCol.get("maxX"));
			jsonObject.put("c", rowAndCol.get("maxY"));
			calcChain.add(jsonObject);
		}
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsDataVerification().intValue())
		{
			String key = rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY");
			if(!dataVerification.containsKey(key))
			{
				dataVerification.put(key, objectMapper.readValue(luckySheetBindData.getDataVerification(), JSONObject.class));
			}
		}
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsRelyCell().intValue()) {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), luckySheetBindData.getRelyIndex() == 0);
        }else {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX"), rowAndCol.get("maxY"), true);
        }
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge() || luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(YesNoEnum.NO.getCode().intValue() == luckySheetBindData.getIsChartCell().intValue())
			{
				int rs = luckySheetBindData.getRowSpan()*verticalDataLenth;
				int cs = luckySheetBindData.getColSpan()*horizontalDataLenth;
				if(rs >1 || cs > 1) {
					if(((Map<String, Object>)((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode())) == null)
		    		{
		    			((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), new HashMap<>());
		    		}
					((Map<String, Object>)(((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
					((Map<String, Object>)(((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
					Map<String, Object> merge = new HashMap<String, Object>();
					merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
					merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
					merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan()*verticalDataLenth);
					merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan()*horizontalDataLenth);
					mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
				}
				if((rowAndCol.get("maxX") + verticalDataLenth*luckySheetBindData.getRowSpan()-1)> maxX)
				{
					maxX = rowAndCol.get("maxX") + verticalDataLenth*luckySheetBindData.getRowSpan()-1;
				}
				if((rowAndCol.get("maxY") + horizontalDataLenth*luckySheetBindData.getColSpan()-1)> maxY)
				{
					maxY = rowAndCol.get("maxY") + horizontalDataLenth*luckySheetBindData.getColSpan()-1;
				}
			}else {
				((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).remove(LuckySheetPropsEnum.MERGECELLS.getCode());
				if((rowAndCol.get("maxX") + luckySheetBindData.getRowSpan()-1)> maxX)
				{
					maxX = rowAndCol.get("maxX") + luckySheetBindData.getRowSpan()-1;
				}
				if((rowAndCol.get("maxY") + luckySheetBindData.getColSpan()-1)> maxY)
				{
					maxY = rowAndCol.get("maxY") + luckySheetBindData.getColSpan()-1;
				}
			}
		}else {
			if(rowAndCol.get("maxX")>maxX)
			{
				maxX = rowAndCol.get("maxX");
			}
			if(rowAndCol.get("maxY")>maxY)
			{
				maxY = rowAndCol.get("maxY");
			}
		}
		if(dataRowLen != null)
		{
			if(rowlen.get(String.valueOf(rowAndCol.get("maxX"))) == null)
			{
				rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
			}
		}
		if(dataColLen != null)
		{
			if(columnlen.get(String.valueOf(rowAndCol.get("maxY"))) == null)
			{
				columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
			}
		}
		try {
            Map<String, Object> cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
            cellDatas.add(cellData);
            usedCells.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"), cellData);
		} catch (Exception e) {
            e.printStackTrace();
        }
		this.isOperationCol(luckySheetBindData, rowAndCol.get("maxY"), isExport, (Map) colhidden);
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge() || luckySheetBindData.getIsRelyCell().intValue() == 1) {
			if(YesNoEnum.NO.getCode().intValue() == luckySheetBindData.getIsChartCell().intValue())
			{
				for (int t = 0; t < luckySheetBindData.getRowSpan()*verticalDataLenth; t++) {
					for (int j = 0; j < luckySheetBindData.getColSpan()*horizontalDataLenth; j++) {
						if(t != 0 || j != 0)
						{
							Map<String, Object> mc = new HashMap<String, Object>();
							mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
							mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
							Map<String, Object> cellValue = new HashMap<String, Object>();
							cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
							Map<String, Object> mergeCellData = new HashMap<String, Object>();
							mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+t);
							mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+j);
							mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
							cellDatas.add(mergeCellData);
							Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+j));
							if(mcDataColLen != null)
							{
								if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+j))== null)
								{
									columnlen.put(String.valueOf(rowAndCol.get("maxY")+j), mcDataColLen);
								}
							}
							usedCells.put((rowAndCol.get("maxX")+t)+"_"+(rowAndCol.get("maxY")+j), mergeCellData);
						}
					}
					Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+t));
					if(mcDataRowLen != null)
					{
						if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+t))==null)
						{
							rowlen.put(String.valueOf(rowAndCol.get("maxX")+t), mcDataRowLen);
						}
					}
				}
			}else {
				for (int t = 0; t < luckySheetBindData.getRowSpan()*verticalDataLenth; t++) {
					for (int j = 0; j < luckySheetBindData.getColSpan()*horizontalDataLenth; j++) {
						if(t != 0 || j != 0)
						{
							usedCells.put((rowAndCol.get("maxX")+t)+"_"+(rowAndCol.get("maxY")+j), null);
						}
						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+j));
						if(mcDataColLen != null)
						{
							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+j))== null)
							{
								columnlen.put(String.valueOf(rowAndCol.get("maxY")+j), mcDataColLen);
							}
						}
					}
					Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+t));
					if(mcDataRowLen != null)
					{
						if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+t))==null)
						{
							rowlen.put(String.valueOf(rowAndCol.get("maxX")+t), mcDataRowLen);
						}
					}
				}
				JSONObject chartCell = new JSONObject();
				chartCell.put("r", rowAndCol.get("maxX"));
				chartCell.put("c", rowAndCol.get("maxY"));
				chartCell.put("rs", luckySheetBindData.getRowSpan());
				chartCell.put("cs", luckySheetBindData.getColSpan());
				chartCells.put(luckySheetBindData.getChartId(), chartCell);
			}
		}
		if(isImg)
		{
			double top = LuckysheetUtil.calculateTop(rowlen, rowAndCol.get("maxX"),rowhidden);
			double left = LuckysheetUtil.calculateLeft(columnlen, rowAndCol.get("maxY"),colhidden);
			Object width = LuckysheetUtil.calculateWidth(columnlen, rowAndCol.get("maxY"), horizontalDataLenth*luckySheetBindData.getColSpan());
			Object height = LuckysheetUtil.calculateHeight(rowlen, rowAndCol.get("maxX"), verticalDataLenth*luckySheetBindData.getRowSpan());
			JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
			imgInfo.getJSONObject("default").put("top", top);
			imgInfo.getJSONObject("default").put("left", left);
			imgInfo.getJSONObject("default").put("width", width);
			imgInfo.getJSONObject("default").put("height", height);
			imgInfo.getJSONObject("crop").put("width", width);
			imgInfo.getJSONObject("crop").put("height", height);
			imgInfo.put("src", originalValue);
			JSONObject img = new JSONObject();
			img.put("imgInfo", imgInfo);
			img.put("r", rowAndCol.get("maxX"));
			img.put("c", rowAndCol.get("maxY"));
			if(dataRowLen != null)
			{
				img.put("height", dataRowLen);
			}else {
				img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
			}
			if(dataColLen != null)
			{
				img.put("width", dataColLen);
			}else {
				img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
			}
			if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge()) {
				img.put("isMerge", YesNoEnum.YES.getCode().intValue());
				img.put("rowSpan", luckySheetBindData.getRowSpan()*verticalDataLenth);
				img.put("colSpan", luckySheetBindData.getColSpan()*horizontalDataLenth);
			}else {
				img.put("isMerge", YesNoEnum.NO.getCode().intValue());
			}
			img.put("extend", 0);
			images.add(img);
		}
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsLink())
		{
			hyperlinks.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), luckySheetBindData.getLinkConfig());
		}
		//边框处理
		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
		this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//		List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1);
//		if(!ListUtil.isEmpty(cellBorder))
//		{
//			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//			borderInfos.addAll(cellBorder);
//		}
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
        		maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()*verticalDataLenth);
        	}
			for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
        		maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan());
			}
		}else {
//			maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan());
//	    	maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan());
			for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
	    		maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan());
	    	}
			for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
				String key = "x-"+(luckySheetBindData.getCoordsx()+i);
				int value = rowAndCol.get("maxY")+luckySheetBindData.getColSpan();
				if(maxCoordinate != null && maxCoordinate.containsKey(key) && maxCoordinate.get(key)<value) {
					maxCoordinate.put(key, value);
				}
	    	}
		}
		maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
		if(StringUtil.isNotEmpty(initCalculateSubtotalKey)) {
			boolean isSubtotal = false;
			if(ListUtil.isNotEmpty(subTotalCellCoords)) {
				for (int i = 0; i < subTotalCellCoords.size(); i++) {
					String calculateSubtotalKey = initCalculateSubtotalKey + "-" + subTotalCellCoords.get(i);
					if(subtotalCellDatas != null && (subtotalCellDatas.containsKey(calculateSubtotalKey) || subtotalCellDatas.containsKey("next-"+calculateSubtotalKey)))
					{
						if(subtotalCellDatas.containsKey("next-"+calculateSubtotalKey))
						{
							addCalculateSubtotalCell(luckySheetBindData.getRelyIndex(),luckySheetBindData, cellDatas, maxCoordinate, maxXAndY, border, 
									rowAndCol, objectMapper, borderInfos, borderInfo,calculateSubtotalKey,subtotalCellDatas,subtotalRows,"next-",mergeMap,usedCells,subTotalDigits);
							isSubtotal = true;
						}
						if(subtotalCellDatas.containsKey(calculateSubtotalKey))
						{
							addCalculateSubtotalCell(luckySheetBindData.getRelyIndex(),luckySheetBindData, cellDatas, maxCoordinate, maxXAndY, border, 
									rowAndCol, objectMapper, borderInfos, borderInfo,calculateSubtotalKey,subtotalCellDatas,subtotalRows,"",mergeMap,usedCells,subTotalDigits);
							isSubtotal = true;
						}
					}
				}
			}
			if(isSubtotal && YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue() && luckySheetBindData.getIsRelyCell().intValue() == 1 && "list".equals(luckySheetBindData.getLastAggregateType())) {
				String formula = SheetUtil.calculateFormula(String.valueOf(cellConfig.get("f")),1, 2);
				cellConfig.put("f", formula);
			}
		}
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
			{
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan());
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY"));
			}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()){
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX"));
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan());
			}
		}
	}
	
	/**  
	 * @Title: borderProcess
	 * @Description: 边框处理
	 * @param border
	 * @author caiyang
	 * @date 2022-03-24 10:58:05 
	 */ 
	private void borderProcess(List<Map<String, Object>> borders,int startCoordsx,int endCoordsx,int startCoordsy,int endCoordsy,
			Map<String, Object> borderInfo,LuckySheetBindData luckySheetBindData,String key) {
		List<Map<String, Object>> list = null;
		if(borderInfo.containsKey(key))
		{
			list = (List<Map<String, Object>>) borderInfo.get(key);
			if(ListUtil.isNotEmpty(list)){
				for (int i = 0; i < list.size(); i++) {
					List<Map<String, List<Integer>>> ranges = (List<Map<String, List<Integer>>>) list.get(i).get("range");
					Map<String, List<Integer>> range = ranges.get(0);
					if(endCoordsy > range.get("column").get(1))
					{
						range.get("column").set(1, endCoordsy);
					}
					if(endCoordsx > range.get("row").get(1))
					{
						range.get("row").set(1, endCoordsx);
					}
				}	
			}
		}else {
			if(!ListUtil.isEmpty(borders))
			{
				for (int t = 0; t < borders.size(); t++) {
					Map<String, Object> border = borders.get(t);
					if(border != null)
					{
						String rangType = (String) border.get(LuckySheetPropsEnum.RANGETYPE.getCode());
						if(LuckySheetPropsEnum.BORDERRANGE.getCode().equals(rangType))
						{
							Map<String, Object> cellBorder = null;
							String borderType = (String) border.get(LuckySheetPropsEnum.BORDERTYPE.getCode());
							if(BorderTypeEnum.BORDERALL.getCode().equals(borderType) || BorderTypeEnum.BORDERLEFT.getCode().equals(borderType)
							|| BorderTypeEnum.BORDERRIGHT.getCode().equals(borderType)|| BorderTypeEnum.BORDERTOP.getCode().equals(borderType)
//							|| BorderTypeEnum.BORDERBOTTOM.getCode().equals(borderType)|| BorderTypeEnum.BORDERNONE.getCode().equals(borderType))
							|| BorderTypeEnum.BORDERBOTTOM.getCode().equals(borderType))
							{
								cellBorder = new HashMap<String, Object>();
								cellBorder.put(LuckySheetPropsEnum.BORDERTYPE.getCode(), border.get(LuckySheetPropsEnum.BORDERTYPE.getCode()));
								cellBorder.put(LuckySheetPropsEnum.RANGETYPE.getCode(), border.get(LuckySheetPropsEnum.RANGETYPE.getCode()));
								cellBorder.put(LuckySheetPropsEnum.RANGECOLOR.getCode(), border.get(LuckySheetPropsEnum.RANGECOLOR.getCode()));
//								Map<String, Object> range = new HashMap<String, Object>();
								List<Map<String, Object>> rangeList = new ArrayList<Map<String,Object>>();
								Map<String, Object> rangeMap = new HashMap<String, Object>();
								List<Integer> column = new ArrayList<Integer>();
								column.add(startCoordsy);
								column.add(endCoordsy);
								List<Integer> row = new ArrayList<Integer>();
								row.add(startCoordsx);
								row.add(endCoordsx);
								rangeMap.put(LuckySheetPropsEnum.BORDERCOLUMNRANGE.getCode(), column);
								rangeMap.put(LuckySheetPropsEnum.BORDERROWRANGE.getCode(), row);
								rangeList.add(rangeMap);
								cellBorder.put(LuckySheetPropsEnum.BORDERRANGE.getCode(), rangeList);
								cellBorder.put(LuckySheetPropsEnum.RANGESTYLE.getCode(), border.get(LuckySheetPropsEnum.RANGESTYLE.getCode()));
							}
							if(list == null)
							{
								list = new ArrayList<Map<String,Object>>();
							}
							list.add(cellBorder);
						}
					}
				}
				borderInfo.put(key, list);
			}
		}
	}
	
	/**  
	 * @Title: processSummaryValue
	 * @Description: 处理汇总聚合
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param cellDatas
	 * @param configRowLen
	 * @param configColumnLen
	 * @param maxX
	 * @param maxY
	 * @param rowlen
	 * @param columnlen
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2022-03-20 05:29:45 
	 */ 
	private void processSummaryValue(Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,
			List<Map<String, Object>> cellDatas,Map<String, Object> rowlen,
			Map<String, Object> columnlen,Object dataRowLen,Object dataColLen,Map<String, Integer> maxXAndY,
			Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos,Map<String, Map<String, Object>> mergeMap
			,Map<String, Object> configRowLen,Map<String, Object> configColumnLen,ObjectMapper objectMapper,Map<String, Map<String, Object>> usedCells,boolean calculateFlag,
			Map<String, Object> nowFunction,JSONObject dataVerification,Map<String, JSONArray> cellConditionFormat
			,Map<String, JSONObject> columnStartCoords,Map<String, JSONObject> extendCellOrigin) throws JsonMappingException, JsonProcessingException {
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getOriginalCoordsx(), luckySheetBindData.getOriginalCoordsy());//获取该单元格的边框信息
		}else {
			border = (List<Map<String, Object>>) borderInfo.get(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy());
		}
//		List<Map<String, Object>> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		//计算结果
		Map<String, Integer> rowAndCol = null;
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsConditions().intValue())
		{
			luckySheetBindData.setDatas(luckySheetBindData.getFilterDatas());
		}
        String calculateResult = luckySheetCalculates.get(luckySheetBindData.getCellFunction().intValue()).calculate(luckySheetBindData);
        //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(calculateResult, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer()!=null && luckySheetBindData.getUnitTransfer())
 		{
 			Object resultData  = this.processUnitTransfer(calculateResult, luckySheetBindData);
 			if(resultData != null)
 			{
 				calculateResult = String.valueOf(resultData);
 			}
 		}
        Map<String, Object> cellData = luckySheetBindData.getCellData();
        String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
    	Object v = LuckysheetUtil.formatValue(format, calculateResult);
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);
        if(luckySheetBindData.getCellFillType().intValue() == 2) {
        	this.processSummaryCoverCell(objectMapper, usedCells, luckySheetBindData, cellDatas, null, dataRowLen, configRowLen, dataColLen, configColumnLen, border, maxXAndY, maxCoordinate, borderInfo, v);
        	return;
        }
        int verticalDataLenth = 1;
		int horizontalDataLenth = 1;
        if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(luckySheetBindData.getLastCoordsx() == null)
			{
				rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
			}else {
				rowAndCol = new HashMap<String, Integer>();
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					int cellMaxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", cellMaxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()) {
					int cellMaxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
					rowAndCol.put("maxY", cellMaxY);
				}
			}
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue() && luckySheetBindData.getCoordsx().intValue() == luckySheetBindData.getRelyCoordsx().intValue())
			{
				for (int i = 0; i < luckySheetBindData.getDatas().size(); i++) {
					verticalDataLenth = verticalDataLenth + luckySheetBindData.getDatas().get(i).size();
				}
				verticalDataLenth = verticalDataLenth - 1;
			}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue() && luckySheetBindData.getCoordsy().intValue() == luckySheetBindData.getRelyCoordsy().intValue()) {
				for (int i = 0; i < luckySheetBindData.getDatas().size(); i++) {
					horizontalDataLenth = horizontalDataLenth + luckySheetBindData.getDatas().get(i).size();
				}
				horizontalDataLenth = horizontalDataLenth - 1;
			}
		}else {
			if(luckySheetBindData.getRecalculateCoords().intValue() == 1)
			{
				if(calculateFlag)
				{
					rowAndCol = new HashMap<String, Integer>();
					rowAndCol.put("maxX", luckySheetBindData.getCoordsx());
					rowAndCol.put("maxY", luckySheetBindData.getCoordsy());
				}else {
					JSONArray newCoords = getNewCoords(luckySheetBindData.getPriortyMoveDirection().intValue(), luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), maxCoordinate, usedCells);
					if(ListUtil.isEmpty(newCoords))
					{
						rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
					}else {
						rowAndCol = new HashMap<String, Integer>();
						rowAndCol.put("maxX", newCoords.getIntValue(0));
						rowAndCol.put("maxY", newCoords.getIntValue(1));
					}
				}
			}else {
				rowAndCol = new HashMap<String, Integer>();
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					int cellMaxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", cellMaxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()){
					int cellMaxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
					rowAndCol.put("maxY", cellMaxY);
				}
			}
		}
        this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
		this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
        cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        usedCells.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"), cellData);
        if(luckySheetBindData.getIsMerge().intValue() == 1 || luckySheetBindData.getIsRelyCell().intValue() == 1) {
        	Map<String, Object> mergeConfig = new HashMap<String, Object>();
			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),luckySheetBindData.getRowSpan());
			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
			Map<String, Object> merge = new HashMap<String, Object>();
			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan()*verticalDataLenth);
			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan()*horizontalDataLenth);
			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
			for (int k = 1; k <= luckySheetBindData.getRowSpan()*verticalDataLenth; k++) {
				for (int i = 1; i <= luckySheetBindData.getColSpan()*horizontalDataLenth; i++) {
					if(k == 1 && i == 1)
					{
						//do nothing
					}else {
						Map<String, Object> mc = new HashMap<String, Object>();
						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
						Map<String, Object> cellValue = new HashMap<String, Object>();
						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
						Map<String, Object> mergeCellData = new HashMap<String, Object>();
						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
						cellDatas.add(mergeCellData);
						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i-1));
						if(mcDataColLen != null)
						{
							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i-1))== null)
							{
								columnlen.put(String.valueOf(rowAndCol.get("maxY")+i-1), mcDataColLen);
							}
						}
						usedCells.put((rowAndCol.get("maxX")+k-1)+"_"+(rowAndCol.get("maxY")+i-1), cellData);
					}
				}
				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k-1));
				if(mcDataRowLen != null)
				{
					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k-1))==null)
					{
						rowlen.put(String.valueOf(rowAndCol.get("maxX")+k-1), mcDataRowLen);
					}
				}
			}
        }
        try {
            Map<String, Object> cellData1 = objectMapper.readValue(objectMapper.writeValueAsString(cellData), Map.class);
            cellDatas.add(cellData1);
            if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsDataVerification().intValue())
    		{
    			String key = cellData1.get(LuckySheetPropsEnum.R.getCode())+"_"+cellData1.get(LuckySheetPropsEnum.C.getCode());
    			if(!dataVerification.containsKey(key))
    			{
    				dataVerification.put(key, objectMapper.readValue(luckySheetBindData.getDataVerification(), JSONObject.class));
    			}
    		}
            this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX"), rowAndCol.get("maxY"), true);
		} catch (Exception e) {
            e.printStackTrace();
        }
        if(dataRowLen != null)
        {
        	if(rowlen.get(String.valueOf(rowAndCol.get("maxX")))==null)
        	{
        		rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
        	}
        }
        if(dataColLen != null)
        {
        	if(columnlen.get(String.valueOf(rowAndCol.get("maxY")))== null)
        	{
        		columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
        	}
        }
        if(rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1>maxX)
        {
            maxX = rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1;
        }
        if(rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1>maxY)
        {
            maxY = rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1;
        }
        String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
        this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//        List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1);
//        if(!ListUtil.isEmpty(cellBorder))
//		{
//			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//			borderInfos.addAll(cellBorder);
//		}
        for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
        	maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan());
        }
        for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
        	maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan());
        }
        maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
			{
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan());
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY"));
			}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()){
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX"));
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan());
			}
		}
	}
	
	
	/**  
	 * @Title: processGroupSummaryValue
	 * @Description: 处理分组汇总数据
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param mergeMap
	 * @param cellDatas
	 * @param configRowLen
	 * @param configColumnLen
	 * @param maxX
	 * @param maxY
	 * @param rowlen
	 * @param columnlen
	 * @param objectMapper
	 * @param dataRowLen
	 * @param dataColLen
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2022-03-20 07:52:19 
	 */ 
	private void processGroupSummaryValue(Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,
			 Map<String, Map<String, Object>> mergeMap,List<Map<String, Object>> cellDatas,Map<String, Object> configRowLen,Map<String, Object> configColumnLen,
			Map<String, Object> rowlen,Map<String, Object> columnlen,ObjectMapper objectMapper,
			Object dataRowLen,Object dataColLen,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos
			,Map<String, Map<String, Object>> usedCells,Map<String, Map<String, Object>> hyperlinks,List<JSONObject> calcChain, List<JSONObject> images,Map<String, LuckySheetBindData> cellBindData
			,Map<String, Map<String, String>> dicts,Map<String, Object> nowFunction,JSONObject dataVerification,JSONObject drillCells
			,Map<String, Object> subtotalCellDatas,Map<String, JSONObject> subtotalRows,Map<String, JSONArray> cellConditionFormat,Map<String, LuckySheetBindData> coverCells
			,Map<String, JSONObject> columnStartCoords,Map<String, JSONObject> extendCellOrigin,List<String> subTotalCellCoords) throws JsonMappingException, JsonProcessingException {
		//分组聚合
        List<List<Map<String, Object>>> datas = null;
        if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode())
		{
			datas = luckySheetBindData.getFilterDatas();
		}else {
			datas = luckySheetBindData.getDatas();
		}
        if(!ListUtil.isEmpty(datas))
        {
        	GroupSummaryData groupSummaryData = new GroupSummaryData();
            groupSummaryData.setDigit(luckySheetBindData.getDigit());
            groupSummaryData.setProperty(luckySheetBindData.getProperty());
            groupSummaryData.setCompareAttr1(luckySheetBindData.getCompareAttr1());
            groupSummaryData.setCompareAttr2(luckySheetBindData.getCompareAttr2());
            boolean flag = false;
            if(luckySheetBindData.getIsRelyCell().intValue() != 1 && luckySheetBindData.getRecalculateCoords().intValue() == 1)
            {
            	flag = this.calculateOriginCellsExtend(luckySheetBindData, usedCells);
            }
            for (int j = 0; j < datas.size(); j++) {
                if(CellExtendEnum.NOEXTEND.getCode().intValue() == luckySheetBindData.getCellExtend().intValue())
                {//不扩展
                	Map<String, Integer> rowAndCol = null;
                	if(flag)
                	{
                		rowAndCol = new HashMap<String, Integer>();
                		rowAndCol.put("maxX", luckySheetBindData.getCoordsx());
                		rowAndCol.put("maxY", luckySheetBindData.getCoordsy());
                	}else {
                		rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
                	}
                    this.processNotExtendGroupSummaryValue(maxCoordinate, luckySheetBindData, rowAndCol, groupSummaryData, datas.get(j),
                      cellDatas, dataRowLen, dataColLen, rowlen, columnlen,objectMapper,maxXAndY,borderInfo,borderConfig,borderInfos,mergeMap,
                      configRowLen,configColumnLen,usedCells,nowFunction,dataVerification,drillCells,cellConditionFormat,coverCells
                      ,columnStartCoords,extendCellOrigin,subTotalCellCoords);
                    break;
                }else if(CellExtendEnum.VERTICAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()){
                    //向下扩展
                	Map<String, Integer> rowAndCol = null;
                	if(flag)
                	{
                		rowAndCol = new HashMap<String, Integer>();
                		rowAndCol.put("maxX", this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1));
                		rowAndCol.put("maxY", luckySheetBindData.getCoordsy());
                	}else {
                		rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
                	}
                	boolean result = this.processVerticalGroupSummaryValue(j, maxCoordinate, luckySheetBindData, rowAndCol, groupSummaryData, datas.get(j), cellDatas, dataRowLen, dataColLen, 
                    		mergeMap, rowlen, columnlen,objectMapper, datas.size(),maxXAndY,borderInfo,borderConfig,borderInfos,configRowLen,configColumnLen
                    		,usedCells,hyperlinks,calcChain,images,cellBindData,dicts,nowFunction,dataVerification,drillCells,subtotalCellDatas,subtotalRows,cellConditionFormat,coverCells
                    		,columnStartCoords,extendCellOrigin,subTotalCellCoords);
                	if(result) {
                		break;
                	}
                }else if(CellExtendEnum.HORIZONTAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()){
                	Map<String, Integer> rowAndCol = null;
                	if(flag)
                	{
                		rowAndCol = new HashMap<String, Integer>();
                		rowAndCol.put("maxX", luckySheetBindData.getCoordsx());
                		rowAndCol.put("maxY", this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1));
                	}else {
                		rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
                	}
                	boolean result =  this.processHorizontalGroupSummaryValue(j, maxCoordinate, luckySheetBindData, rowAndCol, groupSummaryData, datas.get(j), cellDatas, 
                			dataRowLen, dataColLen, mergeMap, rowlen, columnlen, objectMapper, datas.size(),maxXAndY,borderInfo,borderConfig,borderInfos
                			,configRowLen,configColumnLen,usedCells,hyperlinks,calcChain,images,cellBindData,dicts,nowFunction,dataVerification,drillCells,cellConditionFormat,coverCells
                			,columnStartCoords,extendCellOrigin,subTotalCellCoords);
                	if(result) {
                		break;
                	}
                }
            }
        }
	}
	
	/**  
	 * @Title: processNotExtendGroupSummaryValue
	 * @Description: 处理分组合计不扩展单元格数据
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param rowAndCol
	 * @param groupSummaryData
	 * @param data
	 * @param cellDatas
	 * @param dataRowLen
	 * @param dataColLen
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @param objectMapper
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2022-03-21 07:17:36 
	 */ 
	private void processNotExtendGroupSummaryValue(Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,Map<String, Integer> rowAndCol,GroupSummaryData groupSummaryData,
			List<Map<String, Object>> data,List<Map<String, Object>> cellDatas,Object dataRowLen,Object dataColLen,
			Map<String, Object> rowlen,Map<String, Object> columnlen,ObjectMapper objectMapper,Map<String, Integer> maxXAndY,
			Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos,Map<String, Map<String, Object>> mergeMap
			,Map<String, Object> configRowLen,Map<String, Object> configColumnLen,Map<String, Map<String, Object>> usedCells,Map<String, Object> nowFunction,JSONObject dataVerification
			,JSONObject drillCells,Map<String, JSONArray> cellConditionFormat,Map<String, LuckySheetBindData> coverCells
			,Map<String, JSONObject> columnStartCoords,Map<String, JSONObject> extendCellOrigin,List<String> subTotalCellCoords) throws JsonMappingException, JsonProcessingException {
//		List<Map<String, Object>> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getOriginalCoordsx(), luckySheetBindData.getOriginalCoordsy());//获取该单元格的边框信息
		}else {
			border = (List<Map<String, Object>>) borderInfo.get(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy());
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
		this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		int verticalDataLenth = 1;
		int horizontalDataLenth = 1;
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(luckySheetBindData.getLastCoordsx() == null)
			{
//				rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
			}else {
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					int cellMaxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", cellMaxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()) {
					int cellMaxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
					rowAndCol.put("maxY", cellMaxY);
				}
			}
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue() && luckySheetBindData.getCoordsx().intValue() == luckySheetBindData.getRelyCoordsx().intValue()
					&& !luckySheetBindData.getIsGroupMerge())
			{
				verticalDataLenth = verticalDataLenth + data.size()-1;
			}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue() && luckySheetBindData.getCoordsy().intValue() == luckySheetBindData.getRelyCoordsy().intValue()
					&& !luckySheetBindData.getIsGroupMerge()) {
				horizontalDataLenth = horizontalDataLenth + data.size()-1;
			}
		}else {
			if(luckySheetBindData.getRecalculateCoords().intValue() == 1)
			{
				JSONArray newCoords = getNewCoords(luckySheetBindData.getPriortyMoveDirection().intValue(), luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), maxCoordinate, usedCells);
				if(ListUtil.isEmpty(newCoords))
				{
					rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
				}else {
					rowAndCol = new HashMap<String, Integer>();
					rowAndCol.put("maxX", newCoords.getIntValue(0));
					rowAndCol.put("maxY", newCoords.getIntValue(1));
				}
//		        rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
			}else {
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					int cellMaxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", cellMaxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()){
					int cellMaxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
					rowAndCol.put("maxY", cellMaxY);
				}
			}
		}
		//不扩展
//        luckySheetBindData.setCoordsx(rowAndCol.get("maxX"));
//        luckySheetBindData.setCoordsy(rowAndCol.get("maxY"));
        Map<String, Object> cellData = null;
        try {
            cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(luckySheetBindData.getCellFillType().intValue() != 2) {
        	cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
            cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        }
        usedCells.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"), cellData);
        String key = cellData.get(LuckySheetPropsEnum.R.getCode())+"_"+cellData.get(LuckySheetPropsEnum.C.getCode());
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsDataVerification().intValue())
		{
			if(!dataVerification.containsKey(key))
			{
				dataVerification.put(key, objectMapper.readValue(luckySheetBindData.getDataVerification(), JSONObject.class));
			}
		}
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsRelyCell().intValue()) {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), luckySheetBindData.getRelyIndex() == 0);
        }else {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX"), rowAndCol.get("maxY"), true);
        }
        if(luckySheetBindData.getIsDrill() && drillCells != null)
    	{
    		JSONObject drillInfo = new JSONObject();
    		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getDrillId()+"");
    		JSONObject drillParams = getDrillParams(data.get(0),luckySheetBindData.getDrillAttrs());
    		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
    		drillCells.put(key, drillInfo);
    	}
        //计算
        groupSummaryData.setDatas(data);
        String value = luckySheetGroupCalculates.get(luckySheetBindData.getCellFunction().intValue()).calculate(groupSummaryData);
      //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer()!=null && luckySheetBindData.getUnitTransfer())
		{
			Object resultData  = this.processUnitTransfer(value, luckySheetBindData);
			if(resultData != null)
			{
				value = String.valueOf(resultData);
			}
		}
        String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
    	Object v = LuckysheetUtil.formatValue(format, value);
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);
        if(luckySheetBindData.getCellFillType().intValue() == 2) {
    		this.processSummaryCoverCell(objectMapper, usedCells, luckySheetBindData, cellDatas, null, dataRowLen, configRowLen, dataColLen, configColumnLen, border, maxXAndY, maxCoordinate, borderInfo, v);
         	return;
        }
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge() || luckySheetBindData.getIsRelyCell().intValue() == 1)
        {
        	if(((Map<String, Object>)((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode())) == null)
    		{
    			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), new HashMap<>());
    		}
//        	((Map<String, Object>)(((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
//			((Map<String, Object>)(((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			Map<String, Object> merge = new HashMap<String, Object>();
			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan()*verticalDataLenth);
			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan()*horizontalDataLenth);
			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), merge);
			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
			for (int t = 0; t < luckySheetBindData.getRowSpan()*verticalDataLenth; t++) {
				for (int m = 0; m < luckySheetBindData.getColSpan()*horizontalDataLenth; m++) {
					if(t != 0 || m != 0)
					{
						Map<String, Object> mc = new HashMap<String, Object>();
						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
						Map<String, Object> cellValue = new HashMap<String, Object>();
						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
						Map<String, Object> mergeCellData = new HashMap<String, Object>();
						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+t);
						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+m);
						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
						cellDatas.add(mergeCellData);
						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+m));
						if(mcDataColLen != null)
						{
							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+m))== null)
							{
								columnlen.put(String.valueOf(rowAndCol.get("maxY")+m), mcDataColLen);
							}
						}
						usedCells.put((rowAndCol.get("maxX")+t)+"_"+(rowAndCol.get("maxY")+m), cellData);
					}
					Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()));
					if(mcDataRowLen != null)
					{
						if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+t))==null)
						{
							rowlen.put(String.valueOf(rowAndCol.get("maxX")+t), mcDataRowLen);
						}
					}
				}
			}
			if((rowAndCol.get("maxX") + verticalDataLenth*luckySheetBindData.getRowSpan()-1)> maxX)
			{
				maxX = rowAndCol.get("maxX") + luckySheetBindData.getRowSpan()-1;
			}
			if((rowAndCol.get("maxY") + horizontalDataLenth*luckySheetBindData.getColSpan()-1)> maxY)
			{
				maxY = rowAndCol.get("maxY") + luckySheetBindData.getColSpan()-1;
			}
        }else {
        	if(rowAndCol.get("maxX")>maxX)
			{
				maxX = rowAndCol.get("maxX");
			}
			if(rowAndCol.get("maxY")>maxY)
			{
				maxY = rowAndCol.get("maxY");
			}
			if(dataRowLen != null)
			{
				if(rowlen.get(String.valueOf(rowAndCol.get("maxX")))==null)
				{
					rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
				}
			}
			if(dataColLen != null)
			{
				if(columnlen.get(String.valueOf(rowAndCol.get("maxY")))== null)
				{
					columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
				}
			}
        }
        cellDatas.add(cellData);
        for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
        	maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan());
        }
        for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
        	maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan());
        }
        maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
		this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//		List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1);
//		if(!ListUtil.isEmpty(cellBorder))
//		{
//			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//			borderInfos.addAll(cellBorder);
//		}
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue()) {
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan());
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY"));
			}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()) {
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX"));
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan());
			}
		}
	}
	
	/**  
	 * @Title: processVerticalGroupSummaryValue
	 * @Description: 处理分组合计向下扩展数据
	 * @param j
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param rowAndCol
	 * @param groupSummaryData
	 * @param data
	 * @param cellDatas
	 * @param dataRowLen
	 * @param dataColLen
	 * @param mergeMap
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @param objectMapper
	 * @param dataSize
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2022-03-21 07:39:12 
	 */ 
	private boolean processVerticalGroupSummaryValue(int j,Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,Map<String, Integer> rowAndCol,GroupSummaryData groupSummaryData,
			List<Map<String, Object>> data,List<Map<String, Object>> cellDatas,Object dataRowLen,Object dataColLen,Map<String, Map<String, Object>> mergeMap,
			Map<String, Object> rowlen,Map<String, Object> columnlen,ObjectMapper objectMapper, int dataSize,Map<String, Integer> maxXAndY,
			Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos,Map<String, Object> configRowLen,
			Map<String, Object> configColumnLen,Map<String, Map<String, Object>> usedCells,Map<String, Map<String, Object>> hyperlinks,List<JSONObject> calcChain, 
			List<JSONObject> images,Map<String, LuckySheetBindData> cellBindData,Map<String, Map<String, String>> dicts,Map<String, Object> nowFunction
			,JSONObject dataVerification,JSONObject drillCells,Map<String, Object> subtotalCellDatas,Map<String, JSONObject> subtotalRows,Map<String, JSONArray> cellConditionFormat
			,Map<String, LuckySheetBindData> coverCells,Map<String, JSONObject> columnStartCoords,Map<String, JSONObject> extendCellOrigin,List<String> subTotalCellCoords) throws JsonMappingException, JsonProcessingException {
//		List<Map<String, Object>> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		boolean result = false;
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getOriginalCoordsx(), luckySheetBindData.getOriginalCoordsy());//获取该单元格的边框信息
		}else {
			border = (List<Map<String, Object>>) borderInfo.get(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy());
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		int verticalDataLenth = 1;
		int horizontalDataLenth = 1;
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(luckySheetBindData.getLastCoordsx() == null)
			{
//				rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
			}else {
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					int cellMaxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", cellMaxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()) {
					int cellMaxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
					rowAndCol.put("maxY", cellMaxY);
				}
			}
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue() && luckySheetBindData.getCoordsx().intValue() == luckySheetBindData.getRelyCoordsx().intValue())
			{
				verticalDataLenth = verticalDataLenth + data.size()-1;
			}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue() && luckySheetBindData.getCoordsy().intValue() == luckySheetBindData.getRelyCoordsy().intValue()) {
				horizontalDataLenth = horizontalDataLenth + data.size()-1;
			}
		}else {
			if(luckySheetBindData.getRecalculateCoords().intValue() == 1)
			{
				JSONArray newCoords = getNewCoords(luckySheetBindData.getPriortyMoveDirection().intValue(), luckySheetBindData.getLastCoordsx()==null?luckySheetBindData.getCoordsx():luckySheetBindData.getLastCoordsx(), luckySheetBindData.getLastCoordsy()==null?luckySheetBindData.getCoordsy():luckySheetBindData.getLastCoordsy(), maxCoordinate, usedCells);
				if(j == 0 && !ListUtil.isEmpty(newCoords)) {
					luckySheetBindData.setLastCoordsx(newCoords.getIntValue(0));
					luckySheetBindData.setLastCoordsy(newCoords.getIntValue(1));
				}
				if(ListUtil.isEmpty(newCoords))
				{
					rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
				}else {
					rowAndCol = new HashMap<String, Integer>();
					rowAndCol.put("maxX", newCoords.getIntValue(0));
					rowAndCol.put("maxY", newCoords.getIntValue(1));
				}
				verticalDataLenth = data.size();
			}else {
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					int cellMaxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", cellMaxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()){
					int cellMaxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
					rowAndCol.put("maxY", cellMaxY);
				}
				verticalDataLenth = data.size();
			}
		}
		if(this.isExtendCoverCell(rowAndCol, coverCells, luckySheetBindData)) {
			return true;
		}
		this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
        if(j == 0)
		{
			this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		}
        Map<String, Object> cellData = null;
        try {
            cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        String key = cellData.get(LuckySheetPropsEnum.R.getCode())+"_"+cellData.get(LuckySheetPropsEnum.C.getCode());
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsDataVerification().intValue())
		{
			if(!dataVerification.containsKey(key))
			{
				dataVerification.put(key, objectMapper.readValue(luckySheetBindData.getDataVerification(), JSONObject.class));
			}
		}
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsRelyCell().intValue()) {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), luckySheetBindData.getRelyIndex() == 0);
        }else {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX"), rowAndCol.get("maxY"), j == 0);
        }
        if(luckySheetBindData.getIsDrill() && drillCells != null)
    	{
    		JSONObject drillInfo = new JSONObject();
    		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getDrillId()+"");
    		JSONObject drillParams = getDrillParams(data.get(0),luckySheetBindData.getDrillAttrs());
    		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
    		drillCells.put(key, drillInfo);
    	}
        if(luckySheetBindData.getAlternateFormat().intValue() == YesNoEnum.YES.getCode().intValue())
        {
        	if(j%2 == 0)
        	{
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcEven());
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcEven());
        	}else {
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcOdd());
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcOdd());
        	}
        }
        usedCells.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"), cellData);
        groupSummaryData.setDatas(data);
        String value = luckySheetGroupCalculates.get(luckySheetBindData.getCellFunction().intValue()).calculate(groupSummaryData);
      //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer()!=null && luckySheetBindData.getUnitTransfer())
		{
			Object resultData  = this.processUnitTransfer(value, luckySheetBindData);
			if(resultData != null)
			{
				value = String.valueOf(resultData);
			}
		}
        String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
    	Object v = LuckysheetUtil.formatValue(format, value);
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);
        if(dataRowLen != null)
        {
        	if(rowlen.get(String.valueOf(rowAndCol.get("maxX")))==null)
        	{
        		rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
        	}
        }
        if(dataColLen != null)
        {
        	if(columnlen.get(String.valueOf(rowAndCol.get("maxY")))== null)
        	{
        		columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
        	}
        }
        int groupMergeRows = 1;
        if(luckySheetBindData.getIsGroupMerge()) {
        	groupMergeRows = luckySheetBindData.getGroupMergeSize().get(j)-1;
        	cellDatas.add(cellData);
        	if(luckySheetBindData.getIsMerge().intValue() == 1 || groupMergeRows > 1)
        	{
        		Map<String, Object> mergeConfig = new HashMap<String, Object>();
    			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),luckySheetBindData.getRowSpan()+groupMergeRows);
    			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
    			Map<String, Object> merge = new HashMap<String, Object>();
    			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
    			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan()+groupMergeRows);
    			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
    			for (int k = 1; k <= luckySheetBindData.getRowSpan()+groupMergeRows; k++) {
    				for (int i = 1; i <= luckySheetBindData.getColSpan(); i++) {
    					if(k == 1 && i == 1)
    					{
    						//do nothing
    					}else {
    						Map<String, Object> mc = new HashMap<String, Object>();
    						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    						Map<String, Object> cellValue = new HashMap<String, Object>();
    						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
    						Map<String, Object> mergeCellData = new HashMap<String, Object>();
    						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
    						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
    						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
    						cellDatas.add(mergeCellData);
    						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
    						if(mcDataColLen != null)
    						{
    							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
    							{
    								columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
    							}
    						}
    						usedCells.put((rowAndCol.get("maxX")+k-1)+"_"+(rowAndCol.get("maxY")+i-1), cellData);
    					}
    				}
    				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
    				if(mcDataRowLen != null)
					{
    					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
    					{
    						rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
    					}
					}
    			}
    			
        	}
        	if(rowAndCol.get("maxX")+groupMergeRows+luckySheetBindData.getRowSpan()-1>maxX)
            {
                maxX = rowAndCol.get("maxX")+groupMergeRows+luckySheetBindData.getRowSpan()-1;
            }
        	if(rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1>maxY)
            {
        		maxY = rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1;
            }
        	String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
        	this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+groupMergeRows+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//        	List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
//        	if(!ListUtil.isEmpty(cellBorder))
//    		{
//    			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//    			borderInfos.addAll(cellBorder);
//    		}
        }else {
        	if(data.size()>1)
        	{
        		Map<String, Object> mergeConfig = new HashMap<String, Object>();
        		mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        		mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        		int rowSpan = verticalDataLenth;
        		int colSpan = luckySheetBindData.getColSpan();
        		if(luckySheetBindData.getIsMerge().intValue() == 1)
        		{
        			rowSpan = verticalDataLenth * luckySheetBindData.getRowSpan();
        		}
        		mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
        		mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
        		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
        		cellDatas.add(cellData);
        		Map<String, Object> merge = new HashMap<String, Object>();
        		merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        		merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        		merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
        		merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
        		mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
        		for (int k = 1; k <= rowSpan; k++) {
        			for (int i = 1; i <= colSpan; i++) {
        				if(k == 1 && i == 1)
    					{
    						//do nothing
    					}else{
        					Map<String, Object> mc = new HashMap<String, Object>();
            				mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
            				mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
            				Map<String, Object> cellValue = new HashMap<String, Object>();
            				Map<String, Object> mergeCellData = new HashMap<String, Object>();
            				mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
            				mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
            				mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
            				cellDatas.add(mergeCellData);
            				Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
    						if(mcDataColLen != null)
    						{
    							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
    							{
    								columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
    							}
    						}
    						usedCells.put((rowAndCol.get("maxX")+k-1)+"_"+(rowAndCol.get("maxY")+i-1), cellData);
        				}
    				}
        			Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
    				if(mcDataRowLen != null)
					{
    					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
    					{
    						rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
    					}
						
					}
        		}
        		if((rowAndCol.get("maxX")+rowSpan-1)>maxX)
        		{
        			 maxX = rowAndCol.get("maxX")+rowSpan-1;
        		}
        		if((rowAndCol.get("maxY")+colSpan-1)>maxY)
    			{
    				maxY = rowAndCol.get("maxY")+colSpan-1;
    			}
        		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
        		this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1,borderInfo,luckySheetBindData,borderKey);
//        		List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1);
//        		if(!ListUtil.isEmpty(cellBorder))
//         		{
//         			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//         			borderInfos.addAll(cellBorder);
//         		}
        	}else {
        		if(luckySheetBindData.getIsMerge().intValue() == 1)
        		{
        			Map<String, Object> mergeConfig = new HashMap<String, Object>();
        			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),luckySheetBindData.getRowSpan());
        			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
        			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
        			cellDatas.add(cellData);
        			Map<String, Object> merge = new HashMap<String, Object>();
        			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
        			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
        			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
        			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
        			for (int k = 1; k <= luckySheetBindData.getRowSpan(); k++) {
        				for (int i = 1; i <= luckySheetBindData.getColSpan(); i++) {
        					if(k == 1 && i == 1)
        					{
        						//do nothing
        					}else {
        						Map<String, Object> mc = new HashMap<String, Object>();
        						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        						Map<String, Object> cellValue = new HashMap<String, Object>();
        						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
        						Map<String, Object> mergeCellData = new HashMap<String, Object>();
        						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
        						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
        						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
        						cellDatas.add(mergeCellData);
        						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
        						if(mcDataColLen != null)
        						{
        							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
        							{
        								columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
        							}
        						}
        						usedCells.put((rowAndCol.get("maxX")+k-1)+"_"+(rowAndCol.get("maxY")+i-1), cellData);
        					}
        				}
        				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
        				if(mcDataRowLen != null)
    					{
        					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
        					{
        						rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
        					}
    					}
        			}
        			if((rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1)>maxX)
        			{
        				maxX = rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1;
        			}
        			if((rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1)>maxY)
        			{
        				maxY = rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1;
        			}
        			String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
        			this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//        			List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1);
//        			if(!ListUtil.isEmpty(cellBorder))
//            		{
//            			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//            			borderInfos.addAll(cellBorder);
//            		}
        		}else {
        			cellDatas.add(cellData);
                    if(rowAndCol.get("maxX")>maxX)
                    {
                        maxX = rowAndCol.get("maxX");
                    }
                    if(rowAndCol.get("maxY")>maxY)
                    {
                    	maxY = rowAndCol.get("maxY");
                    }
                    String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
                    this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"),borderInfo,luckySheetBindData,borderKey);
//                    List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"));
//                    if(!ListUtil.isEmpty(cellBorder))
//            		{
//            			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//            			borderInfos.addAll(cellBorder);
//            		}
        		}
        	}
        }
        if(luckySheetBindData.getIsChartAttr().intValue() == YesNoEnum.YES.getCode().intValue())
		{
			if(luckySheetBindData.getStartx() == null || luckySheetBindData.getStartx() > rowAndCol.get("maxX"))
			{
				luckySheetBindData.setStartx(rowAndCol.get("maxX"));
			}
			if(luckySheetBindData.getStarty() == null || luckySheetBindData.getStarty() > rowAndCol.get("maxY"))
			{
				luckySheetBindData.setStarty(rowAndCol.get("maxY"));
			}
			if(luckySheetBindData.getEndx() == null || luckySheetBindData.getEndx() < (rowAndCol.get("maxX")+(luckySheetBindData.getIsGroupMerge()?groupMergeRows:data.size()*luckySheetBindData.getRowSpan())-1))
			{
				luckySheetBindData.setEndx(rowAndCol.get("maxX")+(luckySheetBindData.getIsGroupMerge()?groupMergeRows:data.size()*luckySheetBindData.getRowSpan())-1);
			}
			if(luckySheetBindData.getEndy() == null || luckySheetBindData.getEndy() < (rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1))
			{
				luckySheetBindData.setEndy(rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
			}
		}
        if(luckySheetBindData.getIsRelied().intValue() == 1)
        {
        	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
        		maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+luckySheetBindData.getColSpan());
        	}
        }else {
        	if(j == dataSize-1)
            {
            	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
            		maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+luckySheetBindData.getColSpan());
            	}
            }
        }
        if(luckySheetBindData.getIsGroupMerge())
        {
        	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
        		maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()+groupMergeRows);
			}
        }else {
        	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
        		maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()*verticalDataLenth);
        	}
        }
        maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
		String rowKey = luckySheetBindData.getDatasetName()+"-"+luckySheetBindData.getSheetId()+"-"+maxCoordinate.get("y-"+(luckySheetBindData.getCoordsy()));
		if(subtotalRows!=null && subtotalRows.containsKey(rowKey))
		{
			addEmptyCell(j,luckySheetBindData, cellDatas, maxCoordinate, maxXAndY, border, rowAndCol, objectMapper, borderInfos, borderInfo,subtotalRows,rowKey,mergeMap,usedCells);
		}
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue()) {
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan());
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY"));
			}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()) {
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX"));
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan());
			}
		}
		if(luckySheetBindData.getIsRelied().intValue() == 1)
		{
			luckySheetBindData.setRecalculateCoords(YesNoEnum.NO.getCode().intValue());
			luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX")+data.size());
			luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY"));
			luckySheetBindData.setRelyCellExtend(CellExtendEnum.VERTICAL.getCode());
			this.processReliedCells(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, columnlen, 
					mergeMap, objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, calcChain, configRowLen, configColumnLen, 
					images, cellBindData,usedCells,dicts,nowFunction,null,dataVerification,drillCells,null,null,subtotalCellDatas,null,null,cellConditionFormat,null,null,coverCells,columnStartCoords,extendCellOrigin,subTotalCellCoords,false,null,null,null);
		}
		return result;
	}
	
	/**  
	 * @Title: processHorizontalGroupSummaryValue
	 * @Description: 处理分组数据向右扩展数据
	 * @param j
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param rowAndCol
	 * @param groupSummaryData
	 * @param data
	 * @param cellDatas
	 * @param dataRowLen
	 * @param dataColLen
	 * @param mergeMap
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @param objectMapper
	 * @param dataSize
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2022-03-21 07:39:29 
	 */ 
	private boolean processHorizontalGroupSummaryValue(int j,Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,Map<String, Integer> rowAndCol,GroupSummaryData groupSummaryData,
			List<Map<String, Object>> data,List<Map<String, Object>> cellDatas,Object dataRowLen,Object dataColLen,Map<String, Map<String, Object>> mergeMap,
			Map<String, Object> rowlen,Map<String, Object> columnlen,ObjectMapper objectMapper, int dataSize,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,
			List<Map<String, Object>> borderConfig,List<Object> borderInfos,Map<String, Object> configRowLen,Map<String, Object> configColumnLen,Map<String, Map<String, Object>> usedCells
			,Map<String, Map<String, Object>> hyperlinks,List<JSONObject> calcChain, List<JSONObject> images,Map<String, LuckySheetBindData> cellBindData
			,Map<String, Map<String, String>> dicts,Map<String, Object> nowFunction,JSONObject dataVerification,JSONObject drillCells,Map<String, JSONArray> cellConditionFormat
			,Map<String, LuckySheetBindData> coverCells,Map<String, JSONObject> columnStartCoords,Map<String, JSONObject> extendCellOrigin,List<String> subTotalCellCoords) throws JsonMappingException, JsonProcessingException
	{
		boolean result = false;
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getOriginalCoordsx(), luckySheetBindData.getOriginalCoordsy());//获取该单元格的边框信息
		}else {
			border = (List<Map<String, Object>>) borderInfo.get(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy());
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		int verticalDataLenth = 1;
		int horizontalDataLenth = 1;
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(luckySheetBindData.getLastCoordsx() == null)
			{
//				rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
			}else {
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					int cellMaxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", cellMaxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()) {
					int cellMaxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
					rowAndCol.put("maxY", cellMaxY);
				}
			}
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue() && luckySheetBindData.getCoordsx().intValue() == luckySheetBindData.getRelyCoordsx().intValue())
			{
				verticalDataLenth = verticalDataLenth + data.size() - 1;
			}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue() && luckySheetBindData.getCoordsy().intValue() == luckySheetBindData.getRelyCoordsy().intValue()) {
				horizontalDataLenth = horizontalDataLenth + data.size() - 1;
			}
		}else {
			if(luckySheetBindData.getRecalculateCoords().intValue() == 1)
			{
				JSONArray newCoords = getNewCoords(luckySheetBindData.getPriortyMoveDirection().intValue(), luckySheetBindData.getLastCoordsx()==null?luckySheetBindData.getCoordsx():luckySheetBindData.getLastCoordsx(), luckySheetBindData.getLastCoordsy()==null?luckySheetBindData.getCoordsy():luckySheetBindData.getLastCoordsy(), maxCoordinate, usedCells);
				if(j == 0 && !ListUtil.isEmpty(newCoords)) {
					luckySheetBindData.setLastCoordsx(newCoords.getIntValue(0));
					luckySheetBindData.setLastCoordsy(newCoords.getIntValue(1));
				}
				if(ListUtil.isEmpty(newCoords))
				{
					rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
				}else {
					rowAndCol = new HashMap<String, Integer>();
					rowAndCol.put("maxX", newCoords.getIntValue(0));
					rowAndCol.put("maxY", newCoords.getIntValue(1));
				}
//		        rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
				horizontalDataLenth = data.size();
			}else {
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					int cellMaxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", cellMaxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()){
					int cellMaxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
					rowAndCol.put("maxY", cellMaxY);
				}
				horizontalDataLenth = data.size();
			}
		}
		if(this.isExtendCoverCell(rowAndCol, coverCells, luckySheetBindData)) {
			return true;
		}
		this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
        if(j == 0)
		{
			this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		}
        Map<String, Object> cellData = null;
        try {
            cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        String key = cellData.get(LuckySheetPropsEnum.R.getCode())+"_"+cellData.get(LuckySheetPropsEnum.C.getCode());
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsDataVerification().intValue())
		{
			if(!dataVerification.containsKey(key))
			{
				dataVerification.put(key, objectMapper.readValue(luckySheetBindData.getDataVerification(), JSONObject.class));
			}
		}
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsRelyCell().intValue()) {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), luckySheetBindData.getRelyIndex() == 0);
        }else {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX"), rowAndCol.get("maxY"), j == 0);
        }
        if(luckySheetBindData.getIsDrill() && drillCells != null)
    	{
    		JSONObject drillInfo = new JSONObject();
    		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getDrillId()+"");
    		JSONObject drillParams = getDrillParams(data.get(0),luckySheetBindData.getDrillAttrs());
    		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
    		drillCells.put(key, drillInfo);
    	}
        if(luckySheetBindData.getAlternateFormat().intValue() == YesNoEnum.YES.getCode().intValue())
        {
        	if(j%2 == 0)
        	{
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcEven());
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcEven());
        	}else {
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcOdd());
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcOdd());
        	}
        }
        usedCells.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"), cellData);
        groupSummaryData.setDatas(data);
        String value = luckySheetGroupCalculates.get(luckySheetBindData.getCellFunction().intValue()).calculate(groupSummaryData);
      //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer()!=null && luckySheetBindData.getUnitTransfer())
		{
			Object resultData  = this.processUnitTransfer(value, luckySheetBindData);
			if(resultData != null)
			{
				value = String.valueOf(resultData);
			}
		}
        String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
    	Object v = LuckysheetUtil.formatValue(format, value);
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);
        if(luckySheetBindData.getIsGroupMerge())
        {
        	cellDatas.add(cellData);
        	if(luckySheetBindData.getIsMerge().intValue() == 1) {
        		Map<String, Object> mergeConfig = new HashMap<String, Object>();
    			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),luckySheetBindData.getRowSpan());
    			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
    			Map<String, Object> merge = new HashMap<String, Object>();
    			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
    			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
    			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
    			for (int k = 1; k <= luckySheetBindData.getRowSpan(); k++) {
    				for (int i = 1; i <= luckySheetBindData.getColSpan(); i++) {
    					if(k == 1 && i == 1)
    					{
    						//do nothing
    					}else {
    						Map<String, Object> mc = new HashMap<String, Object>();
    						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    						Map<String, Object> cellValue = new HashMap<String, Object>();
    						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
    						Map<String, Object> mergeCellData = new HashMap<String, Object>();
    						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
    						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
    						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
    						cellDatas.add(mergeCellData);
    						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
    						if(mcDataColLen != null)
    						{
    							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
    							{
    								columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
    							}
    						}
    						usedCells.put((rowAndCol.get("maxX")+k-1)+"_"+(rowAndCol.get("maxY")+i-1), cellData);
    					}
    				}
    				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
    				if(mcDataRowLen != null)
					{
    					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
    					{
    						rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
    					}
					}
    			}
        	}
            if(rowAndCol.get("maxY")+luckySheetBindData.getColSpan() - 1>maxY)
            {
            	maxY = rowAndCol.get("maxY")+luckySheetBindData.getColSpan() - 1;
            }
            if(rowAndCol.get("maxX")+luckySheetBindData.getRowSpan() - 1>maxX)
            {
            	maxX = rowAndCol.get("maxX")+luckySheetBindData.getRowSpan() - 1;
            }
            String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
            this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//            List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
//            if(!ListUtil.isEmpty(cellBorder))
//    		{
//    			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//    			borderInfos.addAll(cellBorder);
//    		}
        }else {
        	if(data.size()>1)
        	{
            	Map<String, Object> mergeConfig = new HashMap<String, Object>();
        		mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        		mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        		int rowSpan = luckySheetBindData.getRowSpan();
        		int colSpan = horizontalDataLenth;
        		if(luckySheetBindData.getIsMerge().intValue() == 1)
        		{
        			colSpan = horizontalDataLenth * luckySheetBindData.getColSpan();
        		}
        		mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
        		mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
        		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
        		cellDatas.add(cellData);
        		Map<String, Object> merge = new HashMap<String, Object>();
        		merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        		merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        		merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
        		merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
        		mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
        		for (int k = 1; k <= rowSpan; k++) {
        			for (int i = 1; i <= colSpan; i++) {
        				if(k == 1 && i == 1)
    					{
    						//do nothing
    					}else {
    						Map<String, Object> mc = new HashMap<String, Object>();
    	    				mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    	    				mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    	    				Map<String, Object> cellValue = new HashMap<String, Object>();
    	    				Map<String, Object> mergeCellData = new HashMap<String, Object>();
    	    				mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
    	    				mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
    	    				mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
    	    				cellDatas.add(mergeCellData);
    	    				Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i-1));
    						if(mcDataColLen != null)
    						{
    							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
    							{
    								columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
    							}
    						}
    						usedCells.put((rowAndCol.get("maxX")+k-1)+"_"+(rowAndCol.get("maxY")+i-1), cellData);
    					}
        			}
        			Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
    				if(mcDataRowLen != null)
					{
    					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
    					{
    						rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
    					}
					}
        		}
        		if((rowAndCol.get("maxY")+colSpan-1)>maxY)
        		{
        			maxY = rowAndCol.get("maxY")+colSpan-1;
        		}
        		if((rowAndCol.get("maxX")+rowSpan-1)>maxX)
        		{
        			maxX = rowAndCol.get("maxX")+rowSpan-1;
        		}
        		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
        		this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1,borderInfo,luckySheetBindData,borderKey);
//        		List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1);
//        		if(!ListUtil.isEmpty(cellBorder))
//         		{
//         			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//         			borderInfos.addAll(cellBorder);
//         		}
        	}else {
            	if(luckySheetBindData.getIsMerge().intValue() == 1) {
            		Map<String, Object> mergeConfig = new HashMap<String, Object>();
        			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),luckySheetBindData.getRowSpan());
        			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
        			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
        			cellDatas.add(cellData);
        			Map<String, Object> merge = new HashMap<String, Object>();
        			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
        			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
        			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
        			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
        			for (int k = 1; k <= luckySheetBindData.getRowSpan(); k++) {
        				for (int i = 1; i <= luckySheetBindData.getColSpan(); i++) {
        					if(k == 1 && i == 1)
        					{
        						//do nothing
        					}else {
        						Map<String, Object> mc = new HashMap<String, Object>();
        						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        						Map<String, Object> cellValue = new HashMap<String, Object>();
        						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
        						Map<String, Object> mergeCellData = new HashMap<String, Object>();
        						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
        						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
        						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
        						cellDatas.add(mergeCellData);
        						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
        						if(mcDataColLen != null)
        						{
        							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
        							{
        								columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
        							}
        						}
        						usedCells.put((rowAndCol.get("maxX")+k-1)+"_"+(rowAndCol.get("maxY")+i-1), cellData);
        					}
        				}
        				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
        				if(mcDataRowLen != null)
    					{
        					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
        					{
        						rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);	
        					}
    					}
        			}
        			if((rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1)>maxX)
        			{
        				maxX = rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1;
        			}
        			if((rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1)>maxY)
        			{
        				maxY = rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1;
        			}
        			String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
        			this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//        			if(!ListUtil.isEmpty(cellBorder))
//            		{
//            			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//            			borderInfos.addAll(cellBorder);
//            		}
            	}else {
            		cellDatas.add(cellData);
                    if(rowAndCol.get("maxY")>maxY)
                    {
                    	maxY = rowAndCol.get("maxY");
                    }
                    if(rowAndCol.get("maxX")>maxX)
                    {
                    	maxX = rowAndCol.get("maxX");
                    }
                    String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
                    this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"),borderInfo,luckySheetBindData,borderKey);
//                    List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"));
//                    if(!ListUtil.isEmpty(cellBorder))
//            		{
//            			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//            			borderInfos.addAll(cellBorder);
//            		}
            	}
        	}
        }
        if(luckySheetBindData.getIsGroupMerge())
        {
        	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
        		maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+luckySheetBindData.getColSpan());
        	}
        }else {
        	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
        		maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()*horizontalDataLenth);
        	}
        }
        if(luckySheetBindData.getIsRelied().intValue() == 1)
        {
        	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
        		maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan());
        	}
        }else {
        	if(j==dataSize-1)
            {
            	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
            		maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan());
            	}
            }
        }
        maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue()) {
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan());
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY"));
			}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()) {
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX"));
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan());
			}
		}
		if(luckySheetBindData.getIsRelied().intValue() == 1)
		{
			luckySheetBindData.setRecalculateCoords(YesNoEnum.NO.getCode().intValue());
			luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX"));
			luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY")+data.size());
			luckySheetBindData.setRelyCellExtend(CellExtendEnum.HORIZONTAL.getCode());
			this.processReliedCells(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, columnlen, 
					mergeMap, objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, calcChain, configRowLen, configColumnLen, 
					images, cellBindData,usedCells,dicts,nowFunction,null,dataVerification,drillCells,null,null,null,null,null,cellConditionFormat,null,null,coverCells,columnStartCoords,extendCellOrigin,subTotalCellCoords,false,null,null,null);
		}
		return result;
	}
	
	/**  
	 * @Title: processListGroupValue
	 * @Description: 处理列表和分组数据
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param cellDatas
	 * @param hyperlinks
	 * @param dataRowLen
	 * @param dataColLen
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @param mergeMap
	 * @param objectMapper
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2022-03-20 07:58:10 
	 */ 
	private void processListGroupValue(Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,
			List<Map<String, Object>> cellDatas,Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen
			,Map<String, Object> rowlen,Map<String, Object> columnlen,Map<String, Map<String, Object>> mergeMap,
			ObjectMapper objectMapper,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos
			,List<JSONObject> calcChain,Map<String, Object> configRowLen,Map<String, Object> configColumnLen, List<JSONObject> images,Map<String, LuckySheetBindData> cellBindData
			,Map<String, Map<String, Object>> usedCells,Map<String, Map<String, String>> dicts,Map<String, Object> nowFunction,Map<String, Object> functionCellFormat
			,JSONObject dataVerification,JSONObject drillCells,Object rowhidden,Object colhidden,Map<String, Object> subtotalCellDatas,Map<String, JSONObject> subtotalRows,Map<String, Object> subtotalCellMap
			,Map<String, JSONArray> cellConditionFormat,JSONObject dynamicRange,Map<String, JSONObject> subTotalDigits,Map<String, LuckySheetBindData> coverCells
			,Map<String, JSONObject> columnStartCoords,Map<String, JSONObject> extendCellOrigin,List<String> subTotalCellCoords,boolean isExport,Map<String, Map<String, String>> dictCache,UserInfoDto userInfoDto,Map<String, Object> viewParams) throws JsonMappingException, JsonProcessingException {
		Map<String, Integer> rowAndCol = null;
		boolean flag = this.calculateOriginCellsExtend(luckySheetBindData, usedCells);
		List<List<Map<String, Object>>> datas = null;
		if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode())
		{
			datas = luckySheetBindData.getFilterDatas();
		}else {
			datas = luckySheetBindData.getDatas();
		}
		if(ListUtil.isEmpty(datas))
		{
//			return;
		}
		Map<String, List<Map<String, Object>>> subCalculateDatas = new LinkedHashMap<>();
		if(ListUtil.isNotEmpty(datas)) {
			for (int j = 0; j < datas.size(); j++) {
	            if(CellExtendEnum.NOEXTEND.getCode().intValue() == luckySheetBindData.getCellExtend().intValue())
	            {//非扩展单元格处理
	            	this.processNotExtendListGroupValue(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, 
	            			rowlen, columnlen, maxXAndY,borderInfo,borderConfig,borderInfos,calcChain,mergeMap,configRowLen,configColumnLen,images
	            			,objectMapper,cellBindData,usedCells,flag,dicts,nowFunction,functionCellFormat,dataVerification,drillCells,rowhidden,colhidden,cellConditionFormat,dynamicRange,coverCells
	            			,columnStartCoords,extendCellOrigin,isExport,dictCache,userInfoDto,viewParams);
	                break;
	            }else if(CellExtendEnum.VERTICAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()){
	                //向下扩展单元格处理
	            	boolean result = this.processVerticalListGroupValue(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, 
	            			rowlen, columnlen,mergeMap, objectMapper,maxXAndY,borderInfo,borderConfig,borderInfos,calcChain,configRowLen,configColumnLen,
	            			images,cellBindData,usedCells,flag,dicts,nowFunction,functionCellFormat,dataVerification,drillCells,rowhidden,colhidden,subtotalCellDatas,subtotalRows,cellConditionFormat,dynamicRange,subTotalDigits,
	            			coverCells,columnStartCoords,extendCellOrigin,subTotalCellCoords,isExport,dictCache,userInfoDto,viewParams);
	            	if(result) {
	            		break;
	            	}
	            }else if(CellExtendEnum.HORIZONTAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()){
	            	//向右扩展单元格处理
	            	boolean result = this.processHorizontalListGroupValue(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, 
	            			rowlen, columnlen, mergeMap, objectMapper,maxXAndY,borderInfo,borderConfig,borderInfos,calcChain,configRowLen,configColumnLen,
	            			images,cellBindData,usedCells,flag,dicts,nowFunction,functionCellFormat,dataVerification,drillCells,rowhidden,colhidden,cellConditionFormat,dynamicRange,subTotalDigits,coverCells
	            			,columnStartCoords,extendCellOrigin,subTotalCellCoords,isExport,dictCache,userInfoDto,viewParams);
	            	if(result) {
	            		break;
	            	}
	            }else if(CellExtendEnum.CROSS.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()) {
	            	if(j == 0)
	                {
	                    rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
	                }
	            	//交叉扩展处理
	            	this.processCrossListGroupValue(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, 
	            		rowlen, columnlen, mergeMap, objectMapper,rowAndCol,maxXAndY,borderInfo,borderConfig,borderInfos,calcChain,configRowLen,configColumnLen,
	            		images,usedCells,cellBindData,dicts,dataVerification,drillCells,rowhidden,colhidden,subtotalCellDatas,subCalculateDatas,subtotalCellMap,
	            		subtotalRows,cellConditionFormat,dynamicRange,subTotalDigits,subTotalCellCoords,dictCache,userInfoDto,viewParams);
	            }
	        }
		}else {
			if(CellExtendEnum.CROSS.getCode().intValue() != luckySheetBindData.getCellExtend().intValue() && luckySheetBindData.getKeepEmptyCell()) {
				JSONObject cellData = JSON.parseObject(JSON.toJSONString(luckySheetBindData.getCellData()));
				if(cellData.get("v")!=null) {
					cellData.getJSONObject("v").put("v", "");
					cellData.getJSONObject("v").put("m", "");
				}
				luckySheetBindData.setCellData(cellData);
				this.processFixedValue(maxCoordinate, luckySheetBindData, mergeMap,configRowLen, configColumnLen, 
						rowlen, columnlen, cellDatas, hyperlinks,dataRowLen,dataColLen,maxXAndY,borderInfo,borderConfig,borderInfos,calcChain
						,images,objectMapper,usedCells,nowFunction,null,dataVerification,rowhidden,colhidden,cellConditionFormat,subtotalCellDatas,subtotalRows,subTotalDigits,coverCells
						,columnStartCoords,extendCellOrigin,dynamicRange,subTotalCellCoords,isExport,userInfoDto,viewParams);
			}
			
		}
        
	}
	
	private void processSubDatas(int j,Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,
			List<Map<String, Object>> cellDatas,Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen
			,Map<String, Object> rowlen,Map<String, Object> columnlen,Map<String, Map<String, Object>> mergeMap,
			ObjectMapper objectMapper,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos
			,List<JSONObject> calcChain,Map<String, Object> configRowLen,Map<String, Object> configColumnLen, List<JSONObject> images,Map<String, LuckySheetBindData> cellBindData
			,Map<String, Map<String, Object>> usedCells,Map<String, Map<String, String>> dicts,Map<String, Object> nowFunction,Map<String, Object> functionCellFormat
			,JSONObject dataVerification,JSONObject drillCells,Object rowhidden,Object colhidden,Map<String, Object> subtotalCellDatas,Map<String, JSONObject> subtotalRows,Map<String, Object> subtotalCellMap
			,Map<String, JSONArray> cellConditionFormat,JSONObject dynamicRange,Map<String, JSONObject> subTotalDigits,Map<String, LuckySheetBindData> coverCells
			,Map<String, JSONObject> columnStartCoords,Map<String, JSONObject> extendCellOrigin,List<String> subTotalCellCoords,Map<String, Map<String, String>> dictCache,UserInfoDto userInfoDto,Map<String, Object> viewParams) throws JsonMappingException, JsonProcessingException {
		LuckySheetBindData bindData = new LuckySheetBindData();
		BeanUtils.copyProperties(luckySheetBindData, bindData);
		bindData.setCellExtend(luckySheetBindData.getSubExtend());
		bindData.setIsObject(false);
		int dataType = bindData.getDataType();
		List<List<Map<String, Object>>> datas = new ArrayList<>();
		bindData.setProperty("cellValue");
		bindData.setCellText("${cellValue}");
		if(dataType == 1) {//数组
			if(ListUtil.isNotEmpty(luckySheetBindData.getDatas()) && ListUtil.isNotEmpty(luckySheetBindData.getDatas().get(0))) {
				for (int i = 0; i < luckySheetBindData.getDatas().get(j).size(); i++) {
					Object data = luckySheetBindData.getDatas().get(j).get(i).get(luckySheetBindData.getProperty());
					if(data != null) {
						JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(data));
						for (int t = 0; t < jsonArray.size(); t++) {
							List<Map<String, Object>> list = new ArrayList<>();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("cellValue", jsonArray.get(t));
							list.add(map);
							datas.add(list);
						}
					}
				}
			}
		}else if(dataType == 2) {//对象数组
			String[] attrs = luckySheetBindData.getDataAttr().split("\\.");
			Object data = luckySheetBindData.getDatas().get(j).get(0).get(luckySheetBindData.getProperty());
			if(data != null) {
				JSONArray subDatas = JSON.parseArray(JSON.toJSONString(luckySheetBindData.getDatas().get(j).get(0).get(luckySheetBindData.getProperty())));
				for (int i = 0; i < attrs.length; i++) {
					subDatas = this.analysisJson(attrs[i], subDatas);
				}
				if(ListUtil.isNotEmpty(subDatas)) {
					for (int i = 0; i < subDatas.size(); i++) {
						List<Map<String, Object>> list = new ArrayList<>();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("cellValue", subDatas.get(i));
						list.add(map);
						datas.add(list);
					}
				}
			}
		}else if(dataType == 3) {//对象
			String[] attrs = luckySheetBindData.getDataAttr().split("\\.");
			Object data = luckySheetBindData.getDatas().get(j).get(0).get(luckySheetBindData.getProperty());
			if(data != null) {
				JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(luckySheetBindData.getDatas().get(j).get(0).get(luckySheetBindData.getProperty())));
				JSONArray subDatas = new JSONArray();
				subDatas.add(jsonObject);
				for (int i = 0; i < attrs.length; i++) {
					subDatas = this.analysisJson(attrs[i], subDatas);
				}
				if(ListUtil.isNotEmpty(subDatas)) {
					for (int i = 0; i < subDatas.size(); i++) {
						List<Map<String, Object>> list = new ArrayList<>();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("cellValue", subDatas.get(i));
						list.add(map);
						datas.add(list);
					}
				}
			}
		}
		bindData.setDatas(datas);
		this.processListGroupValue(maxCoordinate, bindData, cellDatas, hyperlinks, dataRowLen, 
				dataColLen, rowlen, columnlen, mergeMap, objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, 
				calcChain, configRowLen, configColumnLen, images, cellBindData, usedCells, dicts, nowFunction, 
				functionCellFormat, dataVerification, drillCells, rowhidden, colhidden, subtotalCellDatas, subtotalRows, 
				subtotalCellMap, cellConditionFormat, dynamicRange, subTotalDigits, coverCells, columnStartCoords, 
				extendCellOrigin, subTotalCellCoords,false,dictCache,userInfoDto,viewParams);
	}
	
	private JSONArray analysisJson(String key,Object objJson) {
		JSONArray result = new JSONArray();
		Object data = null;
		if(objJson instanceof JSONArray){
    		JSONArray objArray = (JSONArray)objJson;
			for (int i = 0; i < objArray.size(); i++) {
				data = objArray.get(i);
				if(data instanceof JSONObject) {
					JSONObject jsonObject = (JSONObject) data;
					data = jsonObject.get(key); 
					if(data instanceof JSONObject) {
						result.add(data);
					}else if(data instanceof JSONArray) {
						result.addAll((JSONArray) data);
					}else {
						result.add(data);
					}
				}else if(data instanceof JSONArray) {
					JSONArray objArray2 = (JSONArray) data;
					for (int j = 0; j < objArray2.size(); j++) {
						data = objArray2.get(j);
						if(data instanceof JSONObject) {
							JSONObject jsonObject = (JSONObject) data;
							data = jsonObject.get(key); 
							if(data instanceof JSONObject) {
								result.add(data);
							}else if(data instanceof JSONArray) {
								result.addAll((JSONArray) data);
							}else {
								result.add(data);
							}
						}
					}
				}
			}
    	}else if(objJson instanceof JSONObject) {
    		JSONObject jsonObject = (JSONObject) objJson;
			data = jsonObject.get(key); 
			if(data instanceof JSONObject) {
				result.add(data);
			}else if(data instanceof JSONArray) {
				result.addAll((JSONArray) data);
			}else {
				result.add(data);
			}
    	}
		return result;
	}
	
	/**  
	 * @MethodName: processCellWarning
	 * @Description: 单元格预警处理
	 * @author caiyang
	 * @param value
	 * @param bindData 
	 * @return void
	 * @date 2022-11-04 08:02:39 
	 */  
	private JSONObject processCellWarning(Object value,LuckySheetBindData bindData) {
		if(value != null && CheckUtil.isNumber(String.valueOf(value)))
		{
			String rules = bindData.getWarningRules();
			BigDecimal data = new BigDecimal(String.valueOf(value));
			BigDecimal threshold = new BigDecimal(bindData.getThreshold());
			switch (rules) {
			case ">":
				if(data.compareTo(threshold)>0)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("left", null);
					jsonObject.put("top", null);
					jsonObject.put("width", null);
					jsonObject.put("height", null);
					jsonObject.put("value", bindData.getWarningContent());
					jsonObject.put("isshow", false);
					return jsonObject;
				}
				break;
			case ">=":
				if(data.compareTo(threshold)>=0)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("left", null);
					jsonObject.put("top", null);
					jsonObject.put("width", null);
					jsonObject.put("height", null);
					jsonObject.put("value", bindData.getWarningContent());
					jsonObject.put("isshow", false);
					return jsonObject;
				}
				break;
			case "=":
				if(data.compareTo(threshold)==0)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("left", null);
					jsonObject.put("top", null);
					jsonObject.put("width", null);
					jsonObject.put("height", null);
					jsonObject.put("value", bindData.getWarningContent());
					jsonObject.put("isshow", false);
					return jsonObject;
				}
				break;
			case "<":
				if(data.compareTo(threshold)<0)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("left", null);
					jsonObject.put("top", null);
					jsonObject.put("width", null);
					jsonObject.put("height", null);
					jsonObject.put("value", bindData.getWarningContent());
					jsonObject.put("isshow", false);
					return jsonObject;
				}
				break;
			case "<=":
				if(data.compareTo(threshold)<=0)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("left", null);
					jsonObject.put("top", null);
					jsonObject.put("width", null);
					jsonObject.put("height", null);
					jsonObject.put("value", bindData.getWarningContent());
					jsonObject.put("isshow", false);
					return jsonObject;
				}
				break;
			default:
				break;
			}
			
		}
		return null;
	}
	
	/**  
	 * @Title: processNotExtendListGroupValue
	 * @Description: 处理列表分组不扩展数据
	 * @param j
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param cellDatas
	 * @param hyperlinks
	 * @param dataRowLen
	 * @param dataColLen
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2022-03-21 07:45:50 
	 */ 
	private void processNotExtendListGroupValue(int j,Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,List<Map<String, Object>> cellDatas,
			Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen,Map<String, Object> rowlen,Map<String, Object> columnlen
			,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos,List<JSONObject> calcChain,
			Map<String, Map<String, Object>> mergeMap,Map<String, Object> configRowLen,Map<String, Object> configColumnLen, List<JSONObject> images,ObjectMapper objectMapper
			,Map<String, LuckySheetBindData> cellBindData,Map<String, Map<String, Object>> usedCells,boolean calculateFlag,Map<String, Map<String, String>> dictsMap
			,Map<String, Object> nowFunction,Map<String, Object> functionCellFormat,JSONObject dataVerification,JSONObject drillCells,Object rowhidden,Object colhidden
			,Map<String, JSONArray> cellConditionFormat,JSONObject dynamicRange,Map<String, LuckySheetBindData> coverCells
			,Map<String, JSONObject> columnStartCoords,Map<String, JSONObject> extendCellOrigin,boolean isExport,Map<String, Map<String, String>> dictCache,UserInfoDto userInfoDto,Map<String, Object> viewParams) throws JsonMappingException, JsonProcessingException {
		List<List<Map<String, Object>>> bindDatas = null;
		if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode())
		{
			bindDatas = luckySheetBindData.getFilterDatas();
		}else {
			bindDatas = luckySheetBindData.getDatas();
		}
		if(luckySheetBindData.getIsObject()) {
			this.processSubDatas(j,maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, columnlen, mergeMap, objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, calcChain, configRowLen, configColumnLen, images, cellBindData, usedCells, dictsMap, nowFunction, functionCellFormat, dataVerification, drillCells, rowhidden, colhidden, dataVerification, extendCellOrigin, drillCells, cellConditionFormat, dynamicRange, null, coverCells, columnStartCoords, extendCellOrigin, null,dictCache,userInfoDto,viewParams);
			return;
		}
//		List<Map<String, Object>> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getOriginalCoordsx(), luckySheetBindData.getOriginalCoordsy());//获取该单元格的边框信息
		}else {
			border = (List<Map<String, Object>>) borderInfo.get(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy());
		}
		Map<String, Integer> rowAndCol = null;
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		//不扩展
		int verticalDataLenth = 1;
		int horizontalDataLenth = 1;
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(luckySheetBindData.getLastCoordsx() == null)
			{
//				rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
				JSONArray newCoords = getNewCoords(luckySheetBindData.getPriortyMoveDirection().intValue(), luckySheetBindData.getLastCoordsx()==null?luckySheetBindData.getCoordsx():luckySheetBindData.getLastCoordsx(), luckySheetBindData.getLastCoordsy()==null?luckySheetBindData.getCoordsy():luckySheetBindData.getLastCoordsy(), maxCoordinate, usedCells);
				if(!ListUtil.isEmpty(newCoords)) {
					luckySheetBindData.setLastCoordsx(newCoords.getIntValue(0));
					luckySheetBindData.setLastCoordsy(newCoords.getIntValue(1));
				}
				if(ListUtil.isEmpty(newCoords))
				{
					rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
				}else {
					rowAndCol = new HashMap<String, Integer>();
					rowAndCol.put("maxX", newCoords.getIntValue(0));
					rowAndCol.put("maxY", newCoords.getIntValue(1));
				}
			}else {
				rowAndCol = new HashMap<String, Integer>();
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					maxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", maxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()){
					maxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
					rowAndCol.put("maxY", maxY);
				}
			}
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue() && luckySheetBindData.getCoordsx().intValue() == luckySheetBindData.getRelyCoordsx().intValue())
			{
				for (int i = 0; i < bindDatas.size(); i++) {
					verticalDataLenth = verticalDataLenth + bindDatas.get(i).size();
				}
				verticalDataLenth = verticalDataLenth - 1;
			}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue() && luckySheetBindData.getCoordsy().intValue() == luckySheetBindData.getRelyCoordsy().intValue())
			{
				for (int i = 0; i < bindDatas.size(); i++) {
					horizontalDataLenth = horizontalDataLenth + bindDatas.get(i).size();
				}
				horizontalDataLenth = horizontalDataLenth - 1;
			}
		}else {
			if(luckySheetBindData.getRecalculateCoords().intValue() == 1)
			{
				if(calculateFlag)
				{
					rowAndCol = new HashMap<String, Integer>();
					rowAndCol.put("maxX", luckySheetBindData.getCoordsx());
					rowAndCol.put("maxY", luckySheetBindData.getCoordsy());
				}else {
					JSONArray newCoords = getNewCoords(luckySheetBindData.getPriortyMoveDirection().intValue(), luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), maxCoordinate, usedCells);
					if(ListUtil.isEmpty(newCoords))
					{
						rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
					}else {
						rowAndCol = new HashMap<String, Integer>();
						rowAndCol.put("maxX", newCoords.getIntValue(0));
						rowAndCol.put("maxY", newCoords.getIntValue(1));
					}
				}
			}else {
				rowAndCol = new HashMap<String, Integer>();
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					maxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", maxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue())
				{
					maxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getCoordsx());
					rowAndCol.put("maxY", maxY);
				}
			}
		}
		if(luckySheetBindData.getCellFillType().intValue() != 2) {
			luckySheetBindData.getCellData().put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
		    luckySheetBindData.getCellData().put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
		}
		this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
		if(j == 0)
		{
			this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		}
		String[] datasetNames = null;
		if(luckySheetBindData.getDatasetNamesCache() == null) {
			datasetNames = LuckysheetUtil.getDatasetNames(luckySheetBindData.getDatasetName());
			luckySheetBindData.setDatasetNamesCache(datasetNames);
		}else {
			datasetNames = luckySheetBindData.getDatasetNamesCache();
		}
        boolean isJustProperty = false;
        String property = luckySheetBindData.getProperty();
        Object value = null;
        boolean isImg = false;
        
        if(luckySheetBindData.getIsDict())
        {
        	Map<String, String> dicts = null;
        	if(dictsMap.containsKey(luckySheetBindData.getDatasourceId()+"_"+luckySheetBindData.getDictType()))
        	{
        		dicts = dictsMap.get(luckySheetBindData.getDatasourceId()+"_"+luckySheetBindData.getDictType());
        	}else {
        		dicts = this.getDict(luckySheetBindData,dictCache);
        		dictsMap.put(luckySheetBindData.getDatasourceId()+"_"+luckySheetBindData.getDictType(), dicts);
        	}
        	if(dicts != null)
        	{
        		if(luckySheetBindData.getSourceType().intValue() == 2 || luckySheetBindData.getSourceType().intValue() == 3) {
        			value = dicts.get(bindDatas.get(j).get(0).get(property)+"");
        		}else {
        			value = dicts.get(RedisPrefixEnum.REPORTDICT.getCode()+luckySheetBindData.getDatasourceId()+"_"+luckySheetBindData.getDictType()+"_"+bindDatas.get(j).get(0).get(property));
        		}
        		if(value != null) {
        			bindDatas.get(j).get(0).put(property, value);
        		}
        	}
        }else if(customSpringReportFunction.isSpringReportFunction(property)) {
        	Map<String, Object> extraParams = new HashMap<>();
        	extraParams.put("userInfo", userInfoDto);
        	extraParams.put("viewParams", viewParams);
        	value = customSpringReportFunction.calculate(luckySheetBindData, extraParams);
        }
        else {
        	List<String> properties = null;
        	Map<String, Object> datas = new LinkedHashMap<String, Object>();
            if(datasetNames.length > 1)
            {
            	for (int i = 0; i < datasetNames.length; i++) {
            		Map<String, Object> datasetDatas = ListUtil.getProperties(luckySheetBindData.getProperty(), 
            				luckySheetBindData.getMultiDatas().get(datasetNames[i]).get(j).get(0), datasetNames[i]);
            		datas.putAll(datasetDatas);
    			}
            }else {
            	if(luckySheetBindData.getPropertiesCache() == null) {
            		datas = ListUtil.getProperties(luckySheetBindData.getProperty(), bindDatas.get(j).get(0));
                	properties = new ArrayList<String>();
                	for (String key : datas.keySet()) {
    					properties.add(key);
    				}
                	luckySheetBindData.setPropertiesCache(properties);
            	}else {
            		properties = luckySheetBindData.getPropertiesCache();
            		datas = ListUtil.getProperties(properties, bindDatas.get(j).get(0));
            	}
            }
            Set<String> set = datas.keySet();
            String cellText = luckySheetBindData.getCellText();
            for (String o : set) {
            	if(StringUtil.isNotEmpty(luckySheetBindData.getCellText())) {
            		property = cellText.replace("${"+o+"}", datas.get(o)==null?"":String.valueOf(datas.get(o)));
            		cellText = property;
            	}else {
            		property = property.replace(o, datas.get(o)==null?"":String.valueOf(datas.get(o)));
            	}
            	if(set.size() == 1)
            	{
            		if(property.equals("") || property.length() == String.valueOf(datas.get(o)).length())
            		{
            			isJustProperty = true;
            		}
            	}
            }
            try {
            	if(set.size() > 1)
            	{
            		if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
            			value = AviatorEvaluator.execute(property);
					}else {
						value = property;
					}
            		if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
            		{
            			value = 0;
            		}
            	}else {
            		if(StringUtil.isImgUrl(property))
            		{
            			value = luckySheetBindData.getTplType() == 1?"":property;
    					isImg = true;
            		}else if(!isJustProperty)
            		{
            			if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
            				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
            				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
            				value = AviatorEvaluator.execute(property);
    					}else {
    						value = property;
    					}
            			if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
                		{
                			value = 0;
                		}
            		}else {
            			value = property;
            		}
            	}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }
        if(value == null)
        {
//        	value = property;
        }
      //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer()!=null && luckySheetBindData.getUnitTransfer())
		{
			value  = this.processUnitTransfer(value, luckySheetBindData);
		}
        String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
    	Object v = LuckysheetUtil.formatValue(format, value);
    	((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
    	((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);
    	if(luckySheetBindData.getCellFillType().intValue() == 2) {
    		this.processSummaryCoverCell(objectMapper, usedCells, luckySheetBindData, cellDatas, calcChain, dataRowLen, configRowLen, dataColLen, configColumnLen, border, maxXAndY, maxCoordinate, borderInfo, v);
         	return;
        }
    	if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue() && "list".equals(luckySheetBindData.getAggregateType()) && luckySheetBindData.getIsRelyCell().intValue() == 1) {
    		int rowSpan = luckySheetBindData.getRowSpan();
    		value = SheetUtil.calculateFormula(property,rowSpan*luckySheetBindData.getRelyIndex(), 2);
    	}
    	if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue())
    	{
    		((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.FUNCTION.getCode(), value);
    		JSONObject jsonObject = new JSONObject();
			jsonObject.put("r", rowAndCol.get("maxX"));
			jsonObject.put("c", rowAndCol.get("maxY"));
			calcChain.add(jsonObject);
    	}
    	String key = rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY");
    	if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsDataVerification().intValue())
		{
			if(!dataVerification.containsKey(key))
			{
				dataVerification.put(key, objectMapper.readValue(luckySheetBindData.getDataVerification(), JSONObject.class));
			}
		}
    	if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsRelyCell().intValue()) {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), luckySheetBindData.getRelyIndex() == 0);
        }else {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX"), rowAndCol.get("maxY"), true);
        }
    	if(luckySheetBindData.getIsDrill() && drillCells != null)
    	{
    		JSONObject drillInfo = new JSONObject();
    		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getDrillId()+"");
    		JSONObject drillParams = getDrillParams(bindDatas.get(j).get(0),luckySheetBindData.getDrillAttrs());
    		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
    		drillCells.put(key, drillInfo);
    	}
    	if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge() || luckySheetBindData.getIsRelyCell().intValue() == 1) {
    		if(((Map<String, Object>)((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode())) == null)
    		{
    			((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), new HashMap<>());
    		}
    		((Map<String, Object>)(((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			((Map<String, Object>)(((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).get(LuckySheetPropsEnum.MERGECELLS.getCode()))).put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			Map<String, Object> merge = new HashMap<String, Object>();
			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan()*verticalDataLenth);
			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan()*horizontalDataLenth);
			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
			if((rowAndCol.get("maxX") + luckySheetBindData.getRowSpan()-1)> maxX)
			{
				maxX = rowAndCol.get("maxX") + luckySheetBindData.getRowSpan()-1;
			}
			if((rowAndCol.get("maxY") + luckySheetBindData.getColSpan()-1)> maxY)
			{
				maxY = rowAndCol.get("maxY") + luckySheetBindData.getColSpan()-1;
			}
			for (int t = 0; t < luckySheetBindData.getRowSpan()*verticalDataLenth; t++) {
				for (int m = 0; m < luckySheetBindData.getColSpan()*horizontalDataLenth; m++) {
					if(t != 0 || m != 0)
					{
						Map<String, Object> mc = new HashMap<String, Object>();
						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
						Map<String, Object> cellValue = new HashMap<String, Object>();
						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
						Map<String, Object> mergeCellData = new HashMap<String, Object>();
						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+t);
						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+m);
						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
						cellDatas.add(mergeCellData);
						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+m));
						if(mcDataColLen != null)
						{
							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+m))== null)
							{
								columnlen.put(String.valueOf(rowAndCol.get("maxY")+m), mcDataColLen);
							}
						}
						usedCells.put((rowAndCol.get("maxX")+t)+"_"+(rowAndCol.get("maxY")+m), mergeCellData);
					}
				}
				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+t));
				if(mcDataRowLen != null)
				{
					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+t))==null)
					{
						rowlen.put(String.valueOf(rowAndCol.get("maxX")+t), mcDataRowLen);
					}
				}
			}
		}else {
			if(rowAndCol.get("maxX")>maxX)
			{
				maxX = rowAndCol.get("maxX");
			}
			if(rowAndCol.get("maxY")>maxY)
			{
				maxY = rowAndCol.get("maxY");
			}
		}
		if(dataRowLen != null)
		{
			if(rowlen.get(String.valueOf(rowAndCol.get("maxX")))==null)
			{
				rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
			}
		}
		if(dataColLen != null)
		{
			if(columnlen.get(String.valueOf(rowAndCol.get("maxY")))== null)
			{
				columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
			}
		}
		if(isImg)
		{
			double top = LuckysheetUtil.calculateTop(rowlen, rowAndCol.get("maxX"),rowhidden);
			double left = LuckysheetUtil.calculateLeft(columnlen, rowAndCol.get("maxY"),colhidden);
			Object width = LuckysheetUtil.calculateWidth(columnlen, rowAndCol.get("maxY"), horizontalDataLenth*luckySheetBindData.getColSpan());
			Object height = LuckysheetUtil.calculateHeight(rowlen, rowAndCol.get("maxX"), verticalDataLenth*luckySheetBindData.getRowSpan());
			JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
			imgInfo.getJSONObject("default").put("top", top);
			imgInfo.getJSONObject("default").put("left", left);
			imgInfo.getJSONObject("default").put("width", width);
			imgInfo.getJSONObject("default").put("height", height);
			imgInfo.getJSONObject("crop").put("width", width);
			imgInfo.getJSONObject("crop").put("height", height);
			imgInfo.put("src", property);
			JSONObject img = new JSONObject();
			img.put("imgInfo", imgInfo);
			img.put("r", rowAndCol.get("maxX"));
			img.put("c", rowAndCol.get("maxY"));
			if(dataRowLen != null)
			{
				img.put("height", dataRowLen);
			}else {
				img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
			}
			if(dataColLen != null)
			{
				img.put("width", dataColLen);
			}else {
				img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
			}
			if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge()) {
				img.put("isMerge", YesNoEnum.YES.getCode().intValue());
				img.put("rowSpan", luckySheetBindData.getRowSpan());
				img.put("colSpan", luckySheetBindData.getColSpan());
			}else {
				img.put("isMerge", YesNoEnum.NO.getCode().intValue());
			}
			img.put("extend", 0);
			images.add(img);
		}
		try {
            Map<String, Object> cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
            this.processDynamicRange(luckySheetBindData, dynamicRange, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), cellData);
            cellDatas.add(cellData);
            this.isOperationCol(luckySheetBindData, rowAndCol.get("maxY"), isExport, (Map) colhidden);
            usedCells.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"), cellData);
            if(luckySheetBindData.getIsFunction().intValue() == YesNoEnum.YES.getCode().intValue())
    		{
            	JSONObject ct = LuckysheetUtil.getCellFormatObject(cellData);
            	if(ct != null)
            	{
            		functionCellFormat.put(cellData.get("r") + "_" + cellData.get("c"), ct);
            	}
    		}
            if(luckySheetBindData.getAlternateFormat().intValue() == YesNoEnum.YES.getCode().intValue())
            {
            	if(j%2 == 0)
            	{
            		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcEven());
            		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcEven());
            	}else {
            		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcOdd());
            		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcOdd());
            	}
            }
		} catch (Exception e) {
            e.printStackTrace();
        }
//        cellDatas.add(luckySheetBindData.getCellData());
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsLink())
        {
            String linkAddress = String.valueOf(luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKADDRESS.getCode()));
            String newLinkAddress = UrlUtils.appendParam(linkAddress, luckySheetBindData.getProperty(), String.valueOf(bindDatas.get(j).get(0).get(luckySheetBindData.getProperty())));
            luckySheetBindData.getLinkConfig().put(LuckySheetPropsEnum.LINKADDRESS.getCode(), newLinkAddress);
            hyperlinks.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), luckySheetBindData.getLinkConfig());
        }
        for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
        	 maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan());
        }
        for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
        	maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan());
        }
		maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
		this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//		if(border != null)
//		{
//			List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1);
//			if(!ListUtil.isEmpty(cellBorder))
//			{
//				//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//				borderInfos.addAll(cellBorder);
//			}
//		}
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue()) {
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan());
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY"));
			}else {
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX"));
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan());
			}
		}
		if(luckySheetBindData.getIsChartAttr().intValue() == YesNoEnum.YES.getCode().intValue())
		{
			if(luckySheetBindData.getStartx() == null || luckySheetBindData.getStartx() > rowAndCol.get("maxX"))
			{
				luckySheetBindData.setStartx(rowAndCol.get("maxX"));
			}
			if(luckySheetBindData.getStarty() == null || luckySheetBindData.getStarty() > rowAndCol.get("maxY"))
			{
				luckySheetBindData.setStarty(rowAndCol.get("maxY"));
			}
			if(luckySheetBindData.getEndx() == null || luckySheetBindData.getEndx() < (rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1))
			{
				luckySheetBindData.setEndx(rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan()-1);
			}
			if(luckySheetBindData.getEndy() == null || luckySheetBindData.getEndy() < (rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1))
			{
				luckySheetBindData.setEndy(rowAndCol.get("maxY")+horizontalDataLenth*luckySheetBindData.getColSpan()-1);
			}
		}
//		if(luckySheetBindData.getIsRelied().intValue() == 1)
//		{
//			luckySheetBindData.setRecalculateCoords(YesNoEnum.NO.getCode().intValue());
//			luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX")+verticalDataLenth*luckySheetBindData.getRowSpan());
//			luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY"));
//			this.processReliedCells(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, columnlen, mergeMap, objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, calcChain, configRowLen, configColumnLen, images, cellBindData);
//		}
	}
	
	/**  
	 * @Title: processVerticalListGroupValue
	 * @Description: 处理分组列表向下扩展数据
	 * @param j
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param cellDatas
	 * @param hyperlinks
	 * @param dataRowLen
	 * @param dataColLen
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @param mergeMap
	 * @param objectMapper
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2022-03-21 07:49:26 
	 */ 
	private boolean processVerticalListGroupValue(int j,Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,List<Map<String, Object>> cellDatas,
			Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen,Map<String, Object> rowlen,Map<String, Object> columnlen
			, Map<String, Map<String, Object>> mergeMap,ObjectMapper objectMapper,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos
			,List<JSONObject> calcChain,Map<String, Object> configRowLen,Map<String, Object> configColumnLen, List<JSONObject> images,Map<String, LuckySheetBindData> cellBindData,Map<String, Map<String, Object>> usedCells,
			boolean calculateFalg,Map<String, Map<String, String>> dictsMap,Map<String, Object> nowFunction,Map<String, Object> functionCellFormat,JSONObject dataVerification
			,JSONObject drillCells,Object rowhidden,Object colhidden,Map<String, Object> subtotalCellDatas,Map<String, JSONObject> subtotalRows,Map<String, JSONArray> cellConditionFormat
			,JSONObject dynamicRange,Map<String, JSONObject> subTotalDigits,Map<String, LuckySheetBindData> coverCells
			,Map<String, JSONObject> columnStartCoords,Map<String, JSONObject> extendCellOrigin,List<String> subTotalCellCoords,boolean isExport,Map<String, Map<String, String>> dictCache,UserInfoDto userInfoDto,Map<String, Object> viewParams) throws JsonMappingException, JsonProcessingException
	{
		boolean result = false;
		List<List<Map<String, Object>>> bindDatas = null;
		if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode())
		{
			bindDatas = luckySheetBindData.getFilterDatas();
		}else {
			bindDatas = luckySheetBindData.getDatas();
		}
		if(luckySheetBindData.getIsObject()) {
			this.processSubDatas(j,maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, columnlen, mergeMap, objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, calcChain, configRowLen, configColumnLen, images, cellBindData, usedCells, dictsMap, nowFunction, functionCellFormat, dataVerification, drillCells, rowhidden, colhidden, dataVerification, extendCellOrigin, drillCells, cellConditionFormat, dynamicRange, null, coverCells, columnStartCoords, extendCellOrigin, null,dictCache,userInfoDto,viewParams);
			return result;
		}
		String initCalculateSubtotalKey = luckySheetBindData.getSheetId()+"-"+j+"-"+luckySheetBindData.getCoordsx()+"-"+luckySheetBindData.getCoordsy();
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getOriginalCoordsx(), luckySheetBindData.getOriginalCoordsy());//获取该单元格的边框信息
		}else {
			border = (List<Map<String, Object>>) borderInfo.get(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy());
		}
//		List<Map<String, Object>> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		Map<String, Integer> rowAndCol = null;
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		int subtotal = 0;
		if(luckySheetBindData.getGroupSubtotalCount() != null && luckySheetBindData.getGroupSubtotalCount().get(j) != null)
		{
			subtotal = luckySheetBindData.getGroupSubtotalCount().get(j);
		}
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			JSONArray newCoords = getNewCoords(luckySheetBindData.getPriortyMoveDirection().intValue(), luckySheetBindData.getLastCoordsx()==null?luckySheetBindData.getCoordsx():luckySheetBindData.getLastCoordsx(), luckySheetBindData.getLastCoordsy()==null?luckySheetBindData.getCoordsy():luckySheetBindData.getLastCoordsy(), maxCoordinate, usedCells);
			if(!ListUtil.isEmpty(newCoords)) {
				luckySheetBindData.setLastCoordsx(newCoords.getIntValue(0));
				luckySheetBindData.setLastCoordsy(newCoords.getIntValue(1));
			}
			if(ListUtil.isEmpty(newCoords))
			{
				rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
			}else {
				rowAndCol = new HashMap<String, Integer>();
				rowAndCol.put("maxX", newCoords.getIntValue(0));
				rowAndCol.put("maxY", newCoords.getIntValue(1));
			}
//			if(luckySheetBindData.getLastCoordsx() == null)
//			{
////				rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
//				JSONArray newCoords = getNewCoords(luckySheetBindData.getPriortyMoveDirection().intValue(), luckySheetBindData.getLastCoordsx()==null?luckySheetBindData.getCoordsx():luckySheetBindData.getLastCoordsx(), luckySheetBindData.getLastCoordsy()==null?luckySheetBindData.getCoordsy():luckySheetBindData.getLastCoordsy(), maxCoordinate, usedCells);
//				if(j == 0 && !ListUtil.isEmpty(newCoords)) {
//					luckySheetBindData.setLastCoordsx(newCoords.getIntValue(0));
//					luckySheetBindData.setLastCoordsy(newCoords.getIntValue(1));
//				}
//				if(ListUtil.isEmpty(newCoords))
//				{
//					rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
//				}else {
//					rowAndCol = new HashMap<String, Integer>();
//					rowAndCol.put("maxX", newCoords.getIntValue(0));
//					rowAndCol.put("maxY", newCoords.getIntValue(1));
//				}
//			}else {
//				rowAndCol = new HashMap<String, Integer>();
//				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
//				{
//					maxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
//					rowAndCol.put("maxX", maxX);
//					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
//				}else {
//					maxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
//					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
//					rowAndCol.put("maxY", maxY);
//				}
//			}
		}else {
			if(luckySheetBindData.getRecalculateCoords().intValue() == 1)
			{
				if(calculateFalg)
				{
					rowAndCol = new HashMap<String, Integer>();
					int x = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", x);
					rowAndCol.put("maxY", luckySheetBindData.getCoordsy());
				}else {
					JSONArray newCoords = getNewCoords(luckySheetBindData.getPriortyMoveDirection().intValue(), luckySheetBindData.getLastCoordsx()==null?luckySheetBindData.getCoordsx():luckySheetBindData.getLastCoordsx(), luckySheetBindData.getLastCoordsy()==null?luckySheetBindData.getCoordsy():luckySheetBindData.getLastCoordsy(), maxCoordinate, usedCells);
					if(j == 0 && !ListUtil.isEmpty(newCoords)) {
						luckySheetBindData.setLastCoordsx(newCoords.getIntValue(0));
						luckySheetBindData.setLastCoordsy(newCoords.getIntValue(1));
						luckySheetBindData.setPriortyMoveDirection(1);
						luckySheetBindData.setCoordsx(newCoords.getIntValue(0));
						luckySheetBindData.setCoordsy(newCoords.getIntValue(1));
					}
					if(ListUtil.isEmpty(newCoords))
					{
						rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
					}else {
						rowAndCol = new HashMap<String, Integer>();
						rowAndCol.put("maxX", newCoords.getIntValue(0));
						rowAndCol.put("maxY", newCoords.getIntValue(1));
					}
					
//					int x = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
//					if(usedCells.containsKey(x+"_"+luckySheetBindData.getCoordsy()))
//					{
//						rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
//					}else {
//						rowAndCol = new HashMap<String, Integer>();
//						rowAndCol.put("maxX", x);
////						int y = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
//						rowAndCol.put("maxY", luckySheetBindData.getCoordsy());
//					}
				}
			}else {
				rowAndCol = new HashMap<String, Integer>();
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					maxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", maxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else {
					maxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
					rowAndCol.put("maxY", maxY);
				}
			}
		}
		if(this.isExtendCoverCell(rowAndCol, coverCells, luckySheetBindData)) {
			return true;
		}
		this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
        if(j == 0)
		{
			this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		}
        //纵向扩展(向下扩展)
        Map<String, Object> cellData = null;
        try {
            cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        if(luckySheetBindData.getIsFunction().intValue() == YesNoEnum.YES.getCode().intValue() && functionCellFormat != null)
		{
        	JSONObject ct = LuckysheetUtil.getCellFormatObject(cellData);
        	if(ct != null)
        	{
        		functionCellFormat.put(cellData.get("r") + "_" + cellData.get("c"), ct);
        	}
		}
        String key = cellData.get(LuckySheetPropsEnum.R.getCode())+"_"+cellData.get(LuckySheetPropsEnum.C.getCode());
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsDataVerification().intValue())
		{
			if(!dataVerification.containsKey(key))
			{
				dataVerification.put(key, objectMapper.readValue(luckySheetBindData.getDataVerification(), JSONObject.class));
			}
		}
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsRelyCell().intValue()) {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), luckySheetBindData.getRelyIndex() == 0);
        }else {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), j == 0);
        }
		String conditonFormatKey = luckySheetBindData.getCoordsx() + "_" + luckySheetBindData.getCoordsy();
        if(luckySheetBindData.getAlternateFormat().intValue() == YesNoEnum.YES.getCode().intValue())
        {
        	if(j%2 == 0)
        	{
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcEven());
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcEven());
        	}else {
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcOdd());
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcOdd());
        	}
        }
        usedCells.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"), cellData);
        String property = luckySheetBindData.getProperty();
        boolean isJustProperty = false;
        Object value = null;
        boolean isImg = false;
        if(luckySheetBindData.getIsDict())
        {
        	Map<String, String> dicts = null;
        	if(dictsMap.containsKey(luckySheetBindData.getDatasourceId()+"_"+luckySheetBindData.getDictType()))
        	{
        		dicts = dictsMap.get(luckySheetBindData.getDatasourceId()+"_"+luckySheetBindData.getDictType());
        	}else {
        		dicts = this.getDict(luckySheetBindData,dictCache);
        		dictsMap.put(luckySheetBindData.getDatasourceId()+"_"+luckySheetBindData.getDictType(), dicts);
        	}
        	if(dicts != null)
        	{
        		if(luckySheetBindData.getSourceType().intValue() == 2 || luckySheetBindData.getSourceType().intValue() == 3) {
        			value = dicts.get(bindDatas.get(j).get(0).get(property)+"");
        		}else {
        			value = dicts.get(RedisPrefixEnum.REPORTDICT.getCode()+luckySheetBindData.getDatasourceId()+"_"+luckySheetBindData.getDictType()+"_"+bindDatas.get(j).get(0).get(property));
        		}
        		if(value != null) {
        			bindDatas.get(j).get(0).put(property, value);
        		}
        	}
        }else if(customSpringReportFunction.isSpringReportFunction(property)) {
        	Map<String, Object> extraParams = new HashMap<>();
        	extraParams.put("index", j);
        	extraParams.put("userInfo", userInfoDto);
        	extraParams.put("viewParams", viewParams);
        	value = customSpringReportFunction.calculate(luckySheetBindData, extraParams);
        }else {
        	List<String> properties = null;
        	Map<String, Object> datas = null;
        	if(luckySheetBindData.getPropertiesCache() == null) {
        		datas = ListUtil.getProperties(luckySheetBindData.getProperty(), bindDatas.get(j).get(0));
        		properties = new ArrayList<String>();
            	for (String propertyKey : datas.keySet()) {
					properties.add(propertyKey);
				}
            	luckySheetBindData.setPropertiesCache(properties);
        	}else {
        		properties = luckySheetBindData.getPropertiesCache();
        		datas = ListUtil.getProperties(properties, bindDatas.get(j).get(0));
        	}
            Set<String> set = datas.keySet();
            String cellText = luckySheetBindData.getCellText();
            for (String o : set) {
            	if(StringUtil.isNotEmpty(luckySheetBindData.getCellText())) {
            		property = cellText.replace("${"+o+"}", datas.get(o)==null?"":String.valueOf(datas.get(o)));
            		cellText = property;
            	}else {
            		property = property.replace(o, datas.get(o)==null?"":String.valueOf(datas.get(o)));
            	}
            	if(set.size() == 1)
            	{
            		if(property.equals("") || property.length() == String.valueOf(datas.get(o)).length())
            		{
            			isJustProperty = true;
            		}
            	}
            }
            try {
            	if(set.size() > 1)
            	{
            		if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
            			value = AviatorEvaluator.execute(property);
					}else {
						value = property;
					}
            		if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
            		{
            			value = 0;
            		}
            	}else {
            		if(StringUtil.isImgUrl(property))
            		{
            			value = luckySheetBindData.getTplType() == 1?"":property;
    					isImg = true;
            		}else if(!isJustProperty)
            		{
            			if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
            				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
            				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
            				value = AviatorEvaluator.execute(property);
    					}else {
    						value = property;
    					}
            			if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
                		{
                			value = 0;
                		}
            		}else {
            			value = property;
            		}
            	}
    		} catch (Exception e) {
    			
    		}
        }
        if(value == null)
        {
//        	value = property == null?"":property;
        }
      //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer()!=null && luckySheetBindData.getUnitTransfer())
		{
			value  = this.processUnitTransfer(value, luckySheetBindData);
		}
        String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
    	Object v = LuckysheetUtil.formatValue(format, value);
    	if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue() && "list".equals(luckySheetBindData.getAggregateType())) {
    		this.processCellFCustomF(property, luckySheetBindData);
    		if(luckySheetBindData.getIsRelyCell().intValue() == YesNoEnum.YES.getCode().intValue()) {
    			if(luckySheetBindData.getLastCoordsx() == null) {
    				int rowSpan = luckySheetBindData.getRowSpan();
            		value = SheetUtil.calculateFormula(property,0, 2);
    			}else {
    				int rowSpan = luckySheetBindData.getRowSpan();
            		value = SheetUtil.calculateFormula(property,rowSpan*luckySheetBindData.getRelyIndex(), 2);
    			}
    		}else {
    			int rowSpan = luckySheetBindData.getRowSpan();
        		value = SheetUtil.calculateFormula(property,rowSpan*j, 2);
    		}
    		
    	}
    	((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
    	((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);
    	if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue())
    	{
    		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.FUNCTION.getCode(), value);
    		JSONObject jsonObject = new JSONObject();
			jsonObject.put("r", rowAndCol.get("maxX"));
			jsonObject.put("c", rowAndCol.get("maxY"));
			calcChain.add(jsonObject);
    	}
    	if(luckySheetBindData.getIsDrill()  && drillCells != null)
    	{
    		JSONObject drillInfo = new JSONObject();
    		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getDrillId()+"");
    		JSONObject drillParams = getDrillParams(bindDatas.get(j).get(0),luckySheetBindData.getDrillAttrs());
    		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
    		drillCells.put(key, drillInfo);
    	}
    	this.processDynamicRange(luckySheetBindData, dynamicRange, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), cellData);
    	if(dataRowLen != null)
        {
    		if(rowlen.get(String.valueOf(rowAndCol.get("maxX")))==null)
    		{
    			rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
    		}
        }
        if(dataColLen != null)
        {
        	if(columnlen.get(String.valueOf(rowAndCol.get("maxY")))== null)
        	{
        		columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
        	}
        }
        this.isOperationCol(luckySheetBindData, rowAndCol.get("maxY"), isExport, (Map) colhidden);
        int groupMergeRows = 1;
        if(luckySheetBindData.getIsGroupMerge())
        {
        	groupMergeRows = luckySheetBindData.getGroupMergeSize().get(j);
        	cellDatas.add(cellData);
        	if(luckySheetBindData.getIsMerge().intValue() == 1 || subtotal > 0 || groupMergeRows > 1)
            {
        		Map<String, Object> mergeConfig = new HashMap<String, Object>();
    			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),groupMergeRows+subtotal);
    			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
    			Map<String, Object> merge = new HashMap<String, Object>();
    			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
    			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), groupMergeRows+subtotal);
    			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
    			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
    			for (int k = 1; k <= groupMergeRows+subtotal; k++) {
    				for (int i = 1; i <= luckySheetBindData.getColSpan(); i++) {
    					if(k == 1 && i == 1)
    					{
    						//do nothing
    					}else {
    						Map<String, Object> mc = new HashMap<String, Object>();
    						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    						Map<String, Object> cellValue = new HashMap<String, Object>();
    						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
    						Map<String, Object> mergeCellData = new HashMap<String, Object>();
    						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
    						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
    						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
    						cellDatas.add(mergeCellData);
    						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
    						if(mcDataColLen != null)
    						{
    							if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
    							{
    								columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
    							}
    						}
    						usedCells.put((rowAndCol.get("maxX")+k-1)+"_"+(rowAndCol.get("maxY")+i-1), cellData);
    					}
    				}
    				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
    				if(mcDataRowLen != null)
					{
    					if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
    					{
    						rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
    					}
					}
    			}
            }
            if(rowAndCol.get("maxX")+groupMergeRows+subtotal-1>maxX)
            {
                maxX = rowAndCol.get("maxX")+groupMergeRows+luckySheetBindData.getRowSpan()+subtotal-1;
            }
            if(rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1>maxY)
            {
            	maxY = rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1;
            }
            String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
            this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+groupMergeRows+subtotal-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//            if(!ListUtil.isEmpty(cellBorder))
//    		{
//    			borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), border);
//    			borderInfos.addAll(cellBorder);
//    		}
        }else {
        	if(bindDatas.get(j).size()>1)
        	{
        		Map<String, Object> mergeConfig = new HashMap<String, Object>();
        		mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        		mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        		int rowSpan = bindDatas.get(j).size();
        		int colSpan = luckySheetBindData.getColSpan();
        		if(luckySheetBindData.getIsMerge().intValue() == 1)
        		{
        			rowSpan = bindDatas.get(j).size() * luckySheetBindData.getRowSpan()+subtotal;
        		}else {
        			rowSpan = rowSpan +subtotal;
        		}
        		mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
        		mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
        		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
        		cellDatas.add(cellData);
        		Map<String, Object> merge = new HashMap<String, Object>();
        		merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        		merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        		merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
        		merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
        		mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
        		for (int k = 1; k <= rowSpan; k++) {
        			int dataIndex = k/luckySheetBindData.getRowSpan();
        			for (int i = 1; i <= colSpan; i++) {
        				if(k == 1 && i == 1)
    					{
    						//do nothing
    					}else{
        					Map<String, Object> mc = new HashMap<String, Object>();
            				mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
            				mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
            				Map<String, Object> cellValue = new HashMap<String, Object>();
            				Map<String, Object> mergeCellData = new HashMap<String, Object>();
            				mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
            				mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
            				mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
            				cellDatas.add(mergeCellData);
            				Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i-1));
            				if(mcDataColLen != null)
    						{
            					if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i-1))== null)
            					{
            						columnlen.put(String.valueOf(rowAndCol.get("maxY")+i-1), mcDataColLen);
            					}
    						}
            				usedCells.put((rowAndCol.get("maxX")+k-1)+"_"+(rowAndCol.get("maxY")+i-1), cellData);
        				}
    				}
        			Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k-dataIndex*luckySheetBindData.getRowSpan()));
        			if(mcDataRowLen != null)
					{
        				if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
        				{
        					rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
        				}
					}
        		}
        		if((rowAndCol.get("maxX")+rowSpan-1)>maxX)
        		{
        			 maxX = rowAndCol.get("maxX")+rowSpan-1;
        		}
        		if((rowAndCol.get("maxY")+colSpan-1)>maxY)
        		{
        			maxY = rowAndCol.get("maxY")+colSpan-1;
        		}
        		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
        		this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1,borderInfo,luckySheetBindData,borderKey);
//        		List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1);
//        		if(!ListUtil.isEmpty(cellBorder))
//         		{
//         			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//         			borderInfos.addAll(cellBorder);
//         		}
        	}else {
        		if(luckySheetBindData.getIsMerge().intValue() == 1)
        		{
        			Map<String, Object> mergeConfig = new HashMap<String, Object>();
        			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),luckySheetBindData.getRowSpan());
        			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
        			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
        			cellDatas.add(cellData);
        			Map<String, Object> merge = new HashMap<String, Object>();
        			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
        			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
        			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
        			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
        			for (int k = 1; k <= luckySheetBindData.getRowSpan(); k++) {
        				for (int i = 1; i <= luckySheetBindData.getColSpan(); i++) {
        					if(k == 1 && i == 1)
        					{
        						//do nothing
        					}else {
        						Map<String, Object> mc = new HashMap<String, Object>();
        						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        						Map<String, Object> cellValue = new HashMap<String, Object>();
        						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
        						Map<String, Object> mergeCellData = new HashMap<String, Object>();
        						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
        						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
        						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
        						cellDatas.add(mergeCellData);
        						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
                				if(mcDataColLen != null)
        						{
                					if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
                					{
                						columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
                					}
        						}
                				usedCells.put((rowAndCol.get("maxX")+k-1)+"_"+(rowAndCol.get("maxY")+i-1), cellData);
        					}
        				}
        				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
            			if(mcDataRowLen != null)
    					{
            				if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
            				{
            					rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
            				}
    					}
        			}
        			if((rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1)>maxX)
        			{
        				maxX = rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1;
        			}
        			if((rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1)>maxY)
        			{
        				maxY = rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1;
        			}
        			String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
        			this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//        			List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
//        			if(!ListUtil.isEmpty(cellBorder))
//            		{
//            			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//            			borderInfos.addAll(cellBorder);
//            		}
        		}else {
                  cellDatas.add(cellData);
                  if(rowAndCol.get("maxX")>maxX)
                  {
                      maxX = rowAndCol.get("maxX");
                  }
                  if(rowAndCol.get("maxY")>maxY)
                  {
                	  maxY = rowAndCol.get("maxY");
                  }
                  String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
                  this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"),borderInfo,luckySheetBindData,borderKey);
//                  if(!ListUtil.isEmpty(border))
//                  {
//                	  List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"));
//              		  if(cellBorder != null)
//              		  {
//              			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//              			borderInfos.addAll(cellBorder);
//              		  }
//                  }
        		}
        	}
        }
        if(isImg)
		{
        	double top = LuckysheetUtil.calculateTop(rowlen, rowAndCol.get("maxX"),rowhidden);
			double left = LuckysheetUtil.calculateLeft(columnlen, rowAndCol.get("maxY"),colhidden);
			Object width = LuckysheetUtil.calculateWidth(columnlen, rowAndCol.get("maxY"), luckySheetBindData.getColSpan());
			Object height = LuckysheetUtil.calculateHeight(rowlen, rowAndCol.get("maxX"), (luckySheetBindData.getIsGroupMerge()?groupMergeRows:bindDatas.get(j).size())*luckySheetBindData.getRowSpan());
			JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
			imgInfo.getJSONObject("default").put("top", top);
			imgInfo.getJSONObject("default").put("left", left);
			imgInfo.getJSONObject("default").put("width", width);
			imgInfo.getJSONObject("default").put("height", height);
			imgInfo.getJSONObject("crop").put("width", width);
			imgInfo.getJSONObject("crop").put("height", height);
			imgInfo.put("src", property);
			JSONObject img = new JSONObject();
			img.put("imgInfo", imgInfo);
			img.put("r", rowAndCol.get("maxX"));
			img.put("c", rowAndCol.get("maxY"));
			if(dataRowLen != null)
			{
				img.put("height", dataRowLen);
			}else {
				img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
			}
			if(dataColLen != null)
			{
				img.put("width", dataColLen);
			}else {
				img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
			}
			if(luckySheetBindData.getIsGroupMerge())
			{
				if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge()) {
					img.put("isMerge", YesNoEnum.YES.getCode().intValue());
					img.put("rowSpan", groupMergeRows);
					img.put("colSpan", luckySheetBindData.getColSpan());
				}else {
					img.put("isMerge", YesNoEnum.NO.getCode().intValue());
				}
			}else {
				int colSpan = luckySheetBindData.getColSpan();
				int rowSpan = bindDatas.get(j).size() * luckySheetBindData.getRowSpan();
				if(colSpan > 1 || rowSpan > 1)
				{
					img.put("isMerge", YesNoEnum.YES.getCode().intValue());
					img.put("rowSpan", rowSpan);
					img.put("colSpan", colSpan);
				}else {
					img.put("isMerge", YesNoEnum.NO.getCode().intValue());
				}
			}
			img.put("extend", 1);
			images.add(img);
		}
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsLink())
        {
            String linkAddress = String.valueOf(luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKADDRESS.getCode()));
            String newLinkAddress = UrlUtils.appendParam(linkAddress, luckySheetBindData.getProperty(), String.valueOf(bindDatas.get(j).get(0).get(luckySheetBindData.getProperty())));
            Map<String, Object> hyperLink = new HashMap<String, Object>();
            hyperLink.put(LuckySheetPropsEnum.LINKADDRESS.getCode(), newLinkAddress);
            hyperLink.put(LuckySheetPropsEnum.LINKTYPE.getCode(), luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKTYPE.getCode()));
            hyperLink.put(LuckySheetPropsEnum.LINKTOOLTIP.getCode(), luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKTOOLTIP.getCode()));
            hyperlinks.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), hyperLink);
        }
        if(luckySheetBindData.getIsRelyCell().intValue() == 1)
        {
        	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
        		maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+luckySheetBindData.getColSpan());
			}
        }else {
        	if(luckySheetBindData.getIsRelied().intValue() == 1)
        	{
        		for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
             		maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+luckySheetBindData.getColSpan());
     			}
        	}else {
//        		if(j == bindDatas.size()-1)
//                {
//                	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
//                		maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+luckySheetBindData.getColSpan());
//        			}
//                }
        		for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
            		maxCoordinate.put("x-"+(rowAndCol.get("maxX")+i), rowAndCol.get("maxY")+luckySheetBindData.getColSpan());
    			}
        		
        	}
        }
        if(luckySheetBindData.getIsGroupMerge())
        {
        	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
        		maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+groupMergeRows+subtotal);
			}
        }else {
        	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
        		if(luckySheetBindData.getPriortyMoveDirection() == 2) {
        			maxCoordinate.put("y-"+(rowAndCol.get("maxY")+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()*bindDatas.get(j).size()+subtotal);
        		}else {
        			maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()*bindDatas.get(j).size()+subtotal);
        		}
        		
        	}
        }
        maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
		if(luckySheetBindData.getIsChartAttr().intValue() == YesNoEnum.YES.getCode().intValue())
		{
			if(luckySheetBindData.getStartx() == null || luckySheetBindData.getStartx() > rowAndCol.get("maxX"))
			{
				luckySheetBindData.setStartx(rowAndCol.get("maxX"));
			}
			if(luckySheetBindData.getStarty() == null || luckySheetBindData.getStarty() > rowAndCol.get("maxY"))
			{
				luckySheetBindData.setStarty(rowAndCol.get("maxY"));
			}
			if(luckySheetBindData.getEndx() == null || luckySheetBindData.getEndx() < (rowAndCol.get("maxX")+(luckySheetBindData.getIsGroupMerge()?groupMergeRows:bindDatas.get(j).size()*luckySheetBindData.getRowSpan())-1))
			{
				luckySheetBindData.setEndx(rowAndCol.get("maxX")+(luckySheetBindData.getIsGroupMerge()?groupMergeRows:bindDatas.get(j).size()*luckySheetBindData.getRowSpan())-1);
			}
			if(luckySheetBindData.getEndy() == null || luckySheetBindData.getEndy() < (rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1))
			{
				luckySheetBindData.setEndy(rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
			}
		}
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
			{
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX")+(luckySheetBindData.getIsGroupMerge()?groupMergeRows:bindDatas.get(j).size()*luckySheetBindData.getRowSpan()));
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY"));
			}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()){
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX"));
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY")+(luckySheetBindData.getIsGroupMerge()?groupMergeRows:bindDatas.get(j).size()*luckySheetBindData.getColSpan()));
			}
		}
		if(luckySheetBindData.getIsSubtotal())
		{
			addSubtotalCell(j,luckySheetBindData, cellDatas, maxCoordinate, maxXAndY, border, rowAndCol, objectMapper, borderInfos, borderInfo,subtotalRows,mergeMap,usedCells);
		}
		if(ListUtil.isNotEmpty(subTotalCellCoords)) {
			for (int i = 0; i < subTotalCellCoords.size(); i++) {
				String calculateSubtotalKey = initCalculateSubtotalKey + "-" + subTotalCellCoords.get(i);
				if(subtotalCellDatas.containsKey(calculateSubtotalKey) || subtotalCellDatas.containsKey("next-"+calculateSubtotalKey))
				{
					if(subtotalCellDatas.containsKey("next-"+calculateSubtotalKey))
					{
						addCalculateSubtotalCell(j,luckySheetBindData, cellDatas, maxCoordinate, maxXAndY, border, 
								rowAndCol, objectMapper, borderInfos, borderInfo,calculateSubtotalKey,subtotalCellDatas,subtotalRows,"next-",mergeMap,usedCells,subTotalDigits);
					}
					if(subtotalCellDatas.containsKey(calculateSubtotalKey))
					{
						addCalculateSubtotalCell(j,luckySheetBindData, cellDatas, maxCoordinate, maxXAndY, border, 
								rowAndCol, objectMapper, borderInfos, borderInfo,calculateSubtotalKey,subtotalCellDatas,subtotalRows,"",mergeMap,usedCells,subTotalDigits);
					}
				}
			}
			String rowKey = luckySheetBindData.getDatasetName()+"-"+luckySheetBindData.getSheetId()+"-"+maxCoordinate.get("y-"+(luckySheetBindData.getCoordsy()));
			if(subtotalRows!=null &&  subtotalRows.containsKey(rowKey))
			{
				addEmptyCell(j,luckySheetBindData, cellDatas, maxCoordinate, maxXAndY, border, rowAndCol, objectMapper, borderInfos, borderInfo,subtotalRows,rowKey,mergeMap,usedCells);
			}
		}
		
		
		if(luckySheetBindData.getIsRelied().intValue() == 1)
		{
			luckySheetBindData.setRecalculateCoords(YesNoEnum.NO.getCode().intValue());
			luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX")+(luckySheetBindData.getIsGroupMerge()?groupMergeRows:bindDatas.get(j).size()*luckySheetBindData.getRowSpan()));
			luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY"));
			luckySheetBindData.setRelyCellExtend(CellExtendEnum.VERTICAL.getCode());
			this.processReliedCells(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, columnlen, 
					mergeMap, objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, calcChain, configRowLen, configColumnLen, 
					images, cellBindData,usedCells,dictsMap,nowFunction,functionCellFormat,dataVerification,drillCells,rowhidden,colhidden,subtotalCellDatas,subtotalRows,null,cellConditionFormat,dynamicRange,subTotalDigits,coverCells,columnStartCoords,extendCellOrigin,subTotalCellCoords,isExport,dictCache,userInfoDto,viewParams);
		}
		return result;
	}
	
	/**  
	 * @MethodName: addSubtotalCell
	 * @Description: 添加小计单元格
	 * @author caiyang
	 * @param bindData
	 * @param cellDatas
	 * @param maxCoordinate void
	 * @date 2024-01-26 08:35:27 
	 */ 
	private void addSubtotalCell(int j,LuckySheetBindData bindData,List<Map<String, Object>> cellDatas,Map<String, Integer> maxCoordinate,Map<String, Integer> maxXAndY
			,List<Map<String, Object>> border,Map<String, Integer> rowAndCol,ObjectMapper objectMapper,List<Object> borderInfos,Map<String, Object> borderInfo,
			Map<String, JSONObject> subtotalRows, Map<String, Map<String, Object>> mergeMap,Map<String, Map<String, Object>> usedCells) {
		List<List<Map<String, Object>>> bindDatas = null;
		if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode())
		{
			bindDatas = bindData.getFilterDatas();
		}else {
			bindDatas = bindData.getDatas();
		}
		if(bindDatas.get(j).size() <= 1)
		{
			return;
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		maxX = this.getMaxRow(maxCoordinate, bindData.getCoordsx(), bindData.getCoordsy(), 1);
		rowAndCol.put("maxX", maxX);
		//纵向扩展(向下扩展)
        JSONObject cellData = null;
        try {
            cellData = objectMapper.readValue(objectMapper.writeValueAsString(bindData.getCellData()), JSONObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(cellData == null)
        {
        	cellData = new JSONObject();
        	cellData.put("v", new JSONObject());
        }
       
        cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        if(bindData.getSubtotalAttrs() != null)
        {
        	cellData.getJSONObject("v").put("v", VelocityUtil.simpleParse(bindData.getSubtotalAttrs().getString("name"), bindDatas.get(j).get(0)));
            cellData.getJSONObject("v").put("m", bindData.getSubtotalAttrs().getString("name"));
            cellData.getJSONObject("v").put("bg", bindData.getSubtotalAttrs().getString("bgColor"));
            cellData.getJSONObject("v").put("fc", bindData.getSubtotalAttrs().getString("fontColor"));
            cellData.getJSONObject("v").put("fs", bindData.getSubtotalAttrs().getString("fontSize"));
            cellData.getJSONObject("v").put("bl", bindData.getSubtotalAttrs().getBoolean("fontWeight")?1:0);
        }else {
        	cellData.getJSONObject("v").put("v", bindData.getSubTotalName());
            cellData.getJSONObject("v").put("m", bindData.getSubTotalName());
        }
        if(bindData.getColSpan()>1)
        {
        	Map<String, Object> mergeConfig = new HashMap<String, Object>();
			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),1);
			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), bindData.getColSpan());
			cellData.getJSONObject(LuckySheetPropsEnum.CELLCONFIG.getCode()).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), mergeConfig);
        }
        cellDatas.add(cellData);
        if(rowAndCol.get("maxX")>maxX)
        {
            maxX = rowAndCol.get("maxX");
        }
        String borderKey = bindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+bindData.getCoordsy();
        this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY")+bindData.getColSpan()-1,borderInfo,bindData,borderKey);
//        List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"));
//		if(!ListUtil.isEmpty(cellBorder))
//		{
//			//borderInfo.put(bindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+bindData.getCoordsy(), true);
//			borderInfos.addAll(cellBorder);
//		}
        for (int i = 0; i < bindData.getColSpan(); i++) {
        	maxCoordinate.put("y-"+(bindData.getCoordsy()+i), rowAndCol.get("maxX")+1);
		}
        maxCoordinate.put("x-"+(bindData.getCoordsx()), rowAndCol.get("maxY")+bindData.getColSpan());
		if(subtotalRows != null)
		{
			subtotalRows.put(bindData.getDatasetName()+"-"+bindData.getSheetId()+"-"+rowAndCol.get("maxX"), bindData.getSubtotalAttrs());//小计对应的行
		}
		String rowKey = bindData.getDatasetName()+"-"+bindData.getSheetId()+"-"+maxCoordinate.get("y-"+(bindData.getCoordsy()));
		if(subtotalRows!=null && subtotalRows.containsKey(rowKey))
		{
			addEmptyCell(j,bindData, cellDatas, maxCoordinate, maxXAndY, border, rowAndCol, objectMapper, borderInfos, borderInfo,subtotalRows,rowKey,mergeMap,usedCells);
		}
	}
	
	private void addEmptyCell(int j,LuckySheetBindData bindData,List<Map<String, Object>> cellDatas,Map<String, Integer> maxCoordinate,Map<String, Integer> maxXAndY
			,List<Map<String, Object>> border,Map<String, Integer> rowAndCol,ObjectMapper objectMapper,List<Object> borderInfos,Map<String, Object> borderInfo
			,Map<String, JSONObject> subtotalRows,String key,Map<String, Map<String, Object>> mergeMap,Map<String, Map<String, Object>> usedCells) {
		if(subtotalRows == null) {
			return;
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		maxX = this.getMaxRow(maxCoordinate, bindData.getCoordsx(), bindData.getCoordsy(), 1);
		rowAndCol.put("maxX", maxX);
		//纵向扩展(向下扩展)
        JSONObject cellData = null;
        try {
            cellData = objectMapper.readValue(objectMapper.writeValueAsString(bindData.getCellData()), JSONObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(cellData == null)
        {
        	cellData = new JSONObject();
        	cellData.put("v", new JSONObject());
        }
        cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        cellData.getJSONObject("v").put("v", "");
        cellData.getJSONObject("v").put("m", "");
        if(subtotalRows.get(key) != null)
        {
        	cellData.getJSONObject("v").put("bg", subtotalRows.get(key).getString("bgColor"));
            cellData.getJSONObject("v").put("fc", subtotalRows.get(key).getString("fontColor"));
            cellData.getJSONObject("v").put("fs", subtotalRows.get(key).getString("fontSize"));
            cellData.getJSONObject("v").put("bl", subtotalRows.get(key).getBoolean("fontWeight")?1:0);
        }
        if(bindData.getColSpan()>1)
        {
        	Map<String, Object> mergeConfig = new HashMap<String, Object>();
			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),1);
			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), bindData.getColSpan());
			cellData.getJSONObject(LuckySheetPropsEnum.CELLCONFIG.getCode()).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), mergeConfig);
        }
        cellDatas.add(cellData);
        usedCells.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"), cellData);
        if(rowAndCol.get("maxX")>maxX)
        {
            maxX = rowAndCol.get("maxX");
        }
        String borderKey = bindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+bindData.getCoordsy();
        this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY")+bindData.getColSpan()-1,borderInfo,bindData,borderKey);
		for (int i = 0; i < bindData.getColSpan(); i++) {
        	maxCoordinate.put("y-"+(bindData.getCoordsy()+i), rowAndCol.get("maxX")+1);
		}
        maxCoordinate.put("x-"+(bindData.getCoordsx()), rowAndCol.get("maxY")+bindData.getColSpan());
		maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
		
		String rowKey = bindData.getDatasetName()+"-"+bindData.getSheetId()+"-"+maxCoordinate.get("y-"+(bindData.getCoordsy()));
		if(subtotalRows!=null && subtotalRows.containsKey(rowKey))
		{
			addEmptyCell(j,bindData, cellDatas, maxCoordinate, maxXAndY, border, rowAndCol, objectMapper, borderInfos, borderInfo,subtotalRows,rowKey,mergeMap,usedCells);
		}
	}
	
	/**  
	 * @MethodName: addCalculateSubtotalCell
	 * @Description: 新增计算单元格
	 * @author caiyang
	 * @param j
	 * @param bindData
	 * @param cellDatas
	 * @param maxCoordinate
	 * @param maxXAndY
	 * @param border
	 * @param rowAndCol
	 * @param objectMapper
	 * @param borderInfos
	 * @param borderInfo void
	 * @date 2024-01-26 12:21:45 
	 */ 
	private void addCalculateSubtotalCell(int j,LuckySheetBindData bindData,List<Map<String, Object>> cellDatas,Map<String, Integer> maxCoordinate,Map<String, Integer> maxXAndY
			,List<Map<String, Object>> border,Map<String, Integer> rowAndCol,ObjectMapper objectMapper,List<Object> borderInfos,Map<String, Object> borderInfo,String key,
			Map<String, Object> subtotalCellDatas,Map<String, JSONObject> subtotalRows,String prefix,Map<String, Map<String, Object>> mergeMap,Map<String, Map<String, Object>> usedCells,Map<String, JSONObject> subTotalDigits) {
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		maxX = this.getMaxRow(maxCoordinate, bindData.getCoordsx(), bindData.getCoordsy(), 1);
		rowAndCol.put("maxX", maxX);
		//纵向扩展(向下扩展)
        JSONObject cellData = null;
        try {
            cellData = objectMapper.readValue(objectMapper.writeValueAsString(bindData.getCellData()), JSONObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(cellData == null)
        {
        	cellData = new JSONObject();
        	cellData.put("v", new JSONObject());
        }
        String digitKey = bindData.getSheetId()+"_"+cellData.getIntValue("r")+"_"+cellData.getIntValue("c");
        cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        GroupSummaryData groupSummaryData = new GroupSummaryData();
        groupSummaryData.setDigit(bindData.getDigit());
        groupSummaryData.setProperty(bindData.getProperty());
        groupSummaryData.setDatas((List<Map<String, Object>>) subtotalCellDatas.get(prefix+key));
        
        JSONObject cellType = (JSONObject) subtotalCellDatas.get(prefix+"cellType-"+key);
        if(cellType == null)
        {
        	cellType = new JSONObject();
        }
      	if(subTotalDigits.containsKey(digitKey)) {
      	    groupSummaryData.setDigit(subTotalDigits.get(digitKey).getInteger("digit"));
      	}
      	if(cellType.getInteger("type").intValue() == 6) {
      		if(subTotalDigits.containsKey(digitKey)) {
      			String compareAttr1 = subTotalDigits.get(digitKey).getString("compareAttr1");
      			String compareAttr2 = subTotalDigits.get(digitKey).getString("compareAttr2");
      			groupSummaryData.setCompareAttr1(compareAttr1);
      	        groupSummaryData.setCompareAttr2(compareAttr2);
      		}
      	}
        Object value = luckySheetGroupCalculates.get(cellType.getInteger("type")!=null?cellType.getInteger("type"):1).calculate(groupSummaryData);
        if(subTotalDigits.containsKey(digitKey)) {
        	int digit = subTotalDigits.get(digitKey).getInteger("digit");
        	boolean unitTransfer = subTotalDigits.get(digitKey).getBooleanValue("unitTransfer");
        	if(unitTransfer) {
        		int transferType = subTotalDigits.get(digitKey).getInteger("transferType");
            	String multiple = subTotalDigits.get(digitKey).getString("multiple");
        		LuckySheetBindData luckySheetBindData = new LuckySheetBindData();
        		luckySheetBindData.setDigit(digit);
        		luckySheetBindData.setUnitTransfer(unitTransfer);
        		luckySheetBindData.setTransferType(transferType);
        		luckySheetBindData.setMultiple(multiple);
        		value = this.processUnitTransfer(value, luckySheetBindData);
        	}
        }
        cellData.getJSONObject("v").put("v", value==null?"":String.valueOf(value));
        cellData.getJSONObject("v").put("m", value==null?"":String.valueOf(value));
        usedCells.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"), cellData);
        if(cellType.get("bgColor") != null)
        {
        	cellData.getJSONObject("v").put("bg", cellType.getString("bgColor"));
        }
        if(cellType.get("fontColor") != null)
        {
        	cellData.getJSONObject("v").put("fc", cellType.getString("fontColor"));
        }
        if(cellType.get("fontSize") != null)
        {
        	cellData.getJSONObject("v").put("fs", cellType.getString("fontSize"));
        }
        if(cellType.get("fontWeight") != null)
        {
        	cellData.getJSONObject("v").put("bl", cellType.getBoolean("fontWeight")?1:0);
        }
        if(bindData.getColSpan()>1)
        {
        	Map<String, Object> mergeConfig = new HashMap<String, Object>();
			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),1);
			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), bindData.getColSpan());
			cellData.getJSONObject(LuckySheetPropsEnum.CELLCONFIG.getCode()).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), mergeConfig);
        }
        cellDatas.add(cellData);
        if(rowAndCol.get("maxX")>maxX)
        {
            maxX = rowAndCol.get("maxX");
        }
        String borderKey = bindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+bindData.getCoordsy();
        this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY")+bindData.getColSpan()-1,borderInfo,bindData,borderKey);
        for (int i = 0; i < bindData.getColSpan(); i++) {
        	maxCoordinate.put("y-"+(bindData.getCoordsy()+i), rowAndCol.get("maxX")+1);
		}
        maxCoordinate.put("x-"+(bindData.getCoordsx()), rowAndCol.get("maxY")+bindData.getColSpan());
		maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
	}
	
	
	/**  
	 * @MethodName: processReliedCells
	 * @Description: 依赖单元格处理
	 * @author caiyang
	 * @param j
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param cellDatas
	 * @param hyperlinks
	 * @param dataRowLen
	 * @param dataColLen
	 * @param rowlen
	 * @param columnlen
	 * @param mergeMap
	 * @param objectMapper
	 * @param maxXAndY
	 * @param borderInfo
	 * @param borderConfig
	 * @param borderInfos
	 * @param calcChain
	 * @param configRowLen
	 * @param configColumnLen
	 * @param images
	 * @param cellBindData
	 * @throws JsonMappingException
	 * @throws JsonProcessingException 
	 * @return void
	 * @date 2023-03-02 02:03:15 
	 */  
	private void processReliedCells(int j,Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,List<Map<String, Object>> cellDatas,
			Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen,Map<String, Object> rowlen,Map<String, Object> columnlen
			, Map<String, Map<String, Object>> mergeMap,ObjectMapper objectMapper,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos
			,List<JSONObject> calcChain,Map<String, Object> configRowLen,Map<String, Object> configColumnLen, List<JSONObject> images,Map<String, LuckySheetBindData> cellBindData,Map<String, Map<String, Object>> usedCells
			,Map<String, Map<String, String>> dicts,Map<String, Object> nowFunction,Map<String, Object> functionCellFormat,JSONObject dataVerification,JSONObject drillCells,Object rowhidden,Object colhidden
			,Map<String, Object> subtotalCellDatas,Map<String, JSONObject> subtotalRows,Map<String, List<Map<String, Object>>> subCalculateDatas,Map<String, JSONArray> cellConditionFormat,JSONObject dynamicRange,Map<String, JSONObject> subTotalDigits
			,Map<String, LuckySheetBindData> coverCells,Map<String, JSONObject> columnStartCoords,Map<String, JSONObject> extendCellOrigin,List<String> subTotalCellCoords,boolean isExport,Map<String, Map<String, String>> dictCache,UserInfoDto userInfoDto,Map<String, Object> viewParams) 
					throws JsonMappingException, JsonProcessingException {
		String relyCells = luckySheetBindData.getRelyCells();
		String[] relyCellsArray = relyCells.split(",");
		for (int i = 0; i < relyCellsArray.length; i++) {	
			LuckySheetBindData relyBindData = cellBindData.get(relyCellsArray[i]);
			if(relyBindData == null) {
				relyBindData = cellBindData.get(relyCellsArray[i]+"_remove");
			}
			if(relyBindData != null && relyBindData.getCellExtend().intValue() != 4)
			{
				List<List<Map<String, Object>>> datas = new ArrayList<>();
				if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsConditions().intValue())
				{
					datas = this.processReliedDatas(luckySheetBindData.getFilterDatas().get(j), relyBindData);
				}else {
					datas = this.processReliedDatas(luckySheetBindData.getDatas().get(j), relyBindData);
				}
				relyBindData.setDataSize(luckySheetBindData.getDataSize());
				relyBindData.setDatas(datas);
				relyBindData.setFilterDatas(datas);
				relyBindData.setIsRelyCell(YesNoEnum.YES.getCode());
				relyBindData.setRelyCellExtend(luckySheetBindData.getRelyCellExtend());
				relyBindData.setRelyCoordsx(luckySheetBindData.getCoordsx());
				relyBindData.setRelyCoordsy(luckySheetBindData.getCoordsy());
				relyBindData.setLastCellExtend(luckySheetBindData.getLastCellExtend());
				relyBindData.setLastAggregateType(luckySheetBindData.getAggregateType());
				relyBindData.setRelyIndex(j);
				this.processBindData(relyBindData, maxCoordinate, cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, columnlen, mergeMap, 
						objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, calcChain, configRowLen, configColumnLen, images, 
						cellBindData,usedCells,dicts,nowFunction,functionCellFormat,null,dataVerification,null,rowhidden,colhidden,null,null,subtotalCellDatas,subtotalRows,null,cellConditionFormat,dynamicRange,subTotalDigits,coverCells,columnStartCoords,extendCellOrigin,subTotalCellCoords,isExport,dictCache,userInfoDto,viewParams);
				if(j == (YesNoEnum.YES.getCode().intValue()==luckySheetBindData.getIsConditions()?luckySheetBindData.getFilterDatas().size():luckySheetBindData.getDatas().size()) - 1)
				{
					cellBindData.put(relyCellsArray[i]+"_remove", cellBindData.get(relyCellsArray[i]));
//					cellBindData.remove(relyCellsArray[i]);
					cellBindData.get(relyCellsArray[i]).setIsRelyCalculated(1);
				}
			}else {
				if(relyBindData != null && ((relyBindData.getCellExtend().intValue() == 4 && luckySheetBindData.getCellExtend().intValue() == 4)))
				{
					List<List<Map<String, Object>>> datas = new ArrayList<>();
					datas.add(luckySheetBindData.getDatas().get(j));
					relyBindData.setDatas(datas);
					relyBindData.setIsRelyCell(YesNoEnum.YES.getCode());
					relyBindData.setRelyCellExtend(luckySheetBindData.getRelyCellExtend());
					relyBindData.setRelyCoordsx(luckySheetBindData.getCoordsx());
					relyBindData.setRelyCoordsy(luckySheetBindData.getCoordsy());
					relyBindData.setLastCellExtend(luckySheetBindData.getLastCellExtend());
					relyBindData.setRelyCrossIndex(luckySheetBindData.getRelyCrossIndex());
					relyBindData.setLastIsGroupMerge(luckySheetBindData.getLastIsGroupMerge());
					relyBindData.setLastCoordsx(luckySheetBindData.getLastCoordsx());
					relyBindData.setLastCoordsy(luckySheetBindData.getLastCoordsy());
					luckySheetBindData.setReliedCellSize(luckySheetBindData.getReliedCellSize()+1);
					this.processReliedCrossListGroupValue(j, maxCoordinate, relyBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, columnlen, mergeMap, objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, calcChain, configRowLen, configColumnLen, images, usedCells, cellBindData,dataVerification,drillCells,rowhidden,colhidden,subCalculateDatas,cellConditionFormat,dynamicRange);
					if(j == luckySheetBindData.getDatas().size() - 1 && luckySheetBindData.getRelyCrossIndex().intValue() == luckySheetBindData.getDatas().get(j).size() - 1)
					{
//						cellBindData.remove(relyCellsArray[i]);
						cellBindData.get(relyCellsArray[i]).setIsRelyCalculated(1);
					}
				}
			}
		}
	}
	
	/**  
	 * @MethodName: processReliedDatas
	 * @Description: 依赖单元格数据处理
	 * @author caiyang
	 * @param datas 依赖单元格数据
	 * @param aggregateType 依赖单元格分组方式
	 * @return 
	 * @return List<List<Map<String,Object>>>
	 * @date 2023-03-02 02:03:34 
	 */  
	private List<List<Map<String, Object>>> processReliedDatas(List<Map<String, Object>> datas,LuckySheetBindData bindData){
		List<List<Map<String, Object>>> result = new ArrayList<>();
		if("list".equals(bindData.getAggregateType()))
		{//列表
			List<Map<String, Object>> data = null;
			for (int i = 0; i < datas.size(); i++) {
				data = new ArrayList<Map<String,Object>>();
				data.add(datas.get(i));
				result.add(data);
			}
		}else if("group".equals(bindData.getAggregateType()))
		{
			Map<String, List<Map<String, Object>>> dataMap = new LinkedHashMap<String, List<Map<String, Object>>>();
			for (int i = 0; i < datas.size(); i++) {
				Map<String, Object> map = datas.get(i);
				String value = String.valueOf(map.get(bindData.getProperty()));
				List<Map<String, Object>> rowList=null;
				if (dataMap.containsKey(value)) {
					rowList = dataMap.get(value);
				}else {
					rowList = new ArrayList<Map<String,Object>>();
					dataMap.put(value, rowList);
				}
				rowList.add(map);
			}
			Iterator<Entry<String, List<Map<String, Object>>>> entries = dataMap.entrySet().iterator();
			while(entries.hasNext()){
				result.add(entries.next().getValue());
			}
		}else {
			result.add(datas);
		}
	
		
		return result;
	}
	
	/**  
	 * @Title: processHorizontalListGroupValue
	 * @Description: 处理分组列表向右扩展数据
	 * @param j
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param cellDatas
	 * @param hyperlinks
	 * @param dataRowLen
	 * @param dataColLen
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @param mergeMap
	 * @param objectMapper
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2022-03-21 07:50:28 
	 */ 
	private boolean processHorizontalListGroupValue(int j,Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,List<Map<String, Object>> cellDatas,
			Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen,Map<String, Object> rowlen,Map<String, Object> columnlen
			, Map<String, Map<String, Object>> mergeMap,ObjectMapper objectMapper,Map<String, Integer> maxXAndY,Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos
			,List<JSONObject> calcChain,Map<String, Object> configRowLen,Map<String, Object> configColumnLen, List<JSONObject> images,Map<String, LuckySheetBindData> cellBindData,Map<String, Map<String, Object>> usedCells
			,boolean calculateFlag,Map<String, Map<String, String>> dictsMap,Map<String, Object> nowFunction,Map<String, Object> functionCellFormat
			,JSONObject dataVerification,JSONObject drillCells,Object rowhidden,Object colhidden,Map<String, JSONArray> cellConditionFormat,JSONObject dynamicRange,Map<String, JSONObject> subTotalDigits,
			Map<String, LuckySheetBindData> coverCells,Map<String, JSONObject> columnStartCoords,Map<String, JSONObject> extendCellOrigin,List<String> subTotalCellCoords,boolean isExport,Map<String, Map<String, String>> dictCache,UserInfoDto userInfoDto,Map<String, Object> viewParams) throws JsonMappingException, JsonProcessingException
	{
		boolean result = false;
		List<List<Map<String, Object>>> bindDatas = null;
		if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode())
		{
			bindDatas = luckySheetBindData.getFilterDatas();
		}else {
			bindDatas = luckySheetBindData.getDatas();
		}
		if(luckySheetBindData.getIsObject()) {
			this.processSubDatas(j,maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, columnlen, mergeMap, objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, calcChain, configRowLen, configColumnLen, images, cellBindData, usedCells, dictsMap, nowFunction, functionCellFormat, dataVerification, drillCells, rowhidden, colhidden, dataVerification, extendCellOrigin, drillCells, cellConditionFormat, dynamicRange, null, coverCells, columnStartCoords, extendCellOrigin, null,dictCache,userInfoDto,viewParams);
			return result;
		}
//		List<Map<String, Object>> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy()))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getOriginalCoordsx(), luckySheetBindData.getOriginalCoordsy());//获取该单元格的边框信息
		}else {
			border = (List<Map<String, Object>>) borderInfo.get(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy());
		}
		Map<String, Integer> rowAndCol = null;
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			JSONArray newCoords = getNewCoords(luckySheetBindData.getPriortyMoveDirection().intValue(), luckySheetBindData.getLastCoordsx()==null?luckySheetBindData.getCoordsx():luckySheetBindData.getLastCoordsx(), luckySheetBindData.getLastCoordsy()==null?luckySheetBindData.getCoordsy():luckySheetBindData.getLastCoordsy(), maxCoordinate, usedCells);
			if(!ListUtil.isEmpty(newCoords)) {
				luckySheetBindData.setLastCoordsx(newCoords.getIntValue(0));
				luckySheetBindData.setLastCoordsy(newCoords.getIntValue(1));
			}
			if(ListUtil.isEmpty(newCoords))
			{
				rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
			}else {
				rowAndCol = new HashMap<String, Integer>();
				rowAndCol.put("maxX", newCoords.getIntValue(0));
				rowAndCol.put("maxY", newCoords.getIntValue(1));
			}
		}else {
			if(luckySheetBindData.getRecalculateCoords().intValue() == 1)
			{
				if(calculateFlag)
				{
					rowAndCol = new HashMap<String, Integer>();
					int y = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getCoordsx());
					rowAndCol.put("maxY", y);
				}else {
					JSONArray newCoords = getNewCoords(luckySheetBindData.getPriortyMoveDirection().intValue(), luckySheetBindData.getLastCoordsx()==null?luckySheetBindData.getCoordsx():luckySheetBindData.getLastCoordsx(), luckySheetBindData.getLastCoordsy()==null?luckySheetBindData.getCoordsy():luckySheetBindData.getLastCoordsy(), maxCoordinate, usedCells);
					if(j == 0 && !ListUtil.isEmpty(newCoords)) {
						luckySheetBindData.setLastCoordsx(newCoords.getIntValue(0));
						luckySheetBindData.setLastCoordsy(newCoords.getIntValue(1));
						luckySheetBindData.setCoordsx(newCoords.getIntValue(0));
						luckySheetBindData.setCoordsy(newCoords.getIntValue(1));
						luckySheetBindData.setPriortyMoveDirection(2);
					}
					if(ListUtil.isEmpty(newCoords))
					{
						rowAndCol = this.getMaxRowAndCol(maxCoordinate, luckySheetBindData.getCoordsx(),luckySheetBindData.getCoordsy(),1,1);
					}else {
						rowAndCol = new HashMap<String, Integer>();
						rowAndCol.put("maxX", newCoords.getIntValue(0));
						rowAndCol.put("maxY", newCoords.getIntValue(1));
					}
				}
			}else {
				rowAndCol = new HashMap<String, Integer>();
				if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
				{
					maxX = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", maxX);
					rowAndCol.put("maxY", luckySheetBindData.getLastCoordsy());
				}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()){
					maxY = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
					rowAndCol.put("maxX", luckySheetBindData.getLastCoordsx());
					rowAndCol.put("maxY", maxY);
				}
			}
		}
		if(this.isExtendCoverCell(rowAndCol, coverCells, luckySheetBindData)) {
			return true;
		}
		this.processExtendCellOrigin(extendCellOrigin, luckySheetBindData, rowAndCol);
        if(j == 0)
		{
			this.processColumnStartCoords(columnStartCoords, luckySheetBindData, rowAndCol);
		}
        //横向扩展(向右扩展)
        Map<String, Object> cellData = null;
        try {
            cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        cellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        if(luckySheetBindData.getIsFunction().intValue() == YesNoEnum.YES.getCode().intValue() && functionCellFormat != null)
		{
        	JSONObject ct = LuckysheetUtil.getCellFormatObject(cellData);
        	if(ct != null)
        	{
        		functionCellFormat.put(cellData.get("r") + "_" + cellData.get("c"), ct);
        	}
		}
        String key = cellData.get(LuckySheetPropsEnum.R.getCode())+"_"+cellData.get(LuckySheetPropsEnum.C.getCode());
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsDataVerification().intValue())
		{
			if(!dataVerification.containsKey(key))
			{
				dataVerification.put(key, objectMapper.readValue(luckySheetBindData.getDataVerification(), JSONObject.class));
			}
		}
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsRelyCell().intValue()) {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), luckySheetBindData.getRelyIndex() == 0);
        }else {
        	this.processConditionFormat(luckySheetBindData, cellConditionFormat, rowAndCol.get("maxX"), rowAndCol.get("maxY"), j == 0);
        }
        if(luckySheetBindData.getIsDrill() && drillCells != null)
    	{
    		JSONObject drillInfo = new JSONObject();
    		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getDrillId()+"");
    		JSONObject drillParams = getDrillParams(bindDatas.get(j).get(0),luckySheetBindData.getDrillAttrs());
    		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
    		drillCells.put(key, drillInfo);
    	}
        if(luckySheetBindData.getAlternateFormat().intValue() == YesNoEnum.YES.getCode().intValue())
        {
        	if(j%2 == 0)
        	{
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcEven());
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcEven());
        	}else {
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcOdd());
        		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcOdd());
        	}
        }
        usedCells.put(rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY"), cellData);
        String property = luckySheetBindData.getProperty();
        boolean isJustProperty = false;
        boolean isImg = false;
        Object value = null;
        if(luckySheetBindData.getIsDict())
        {
        	Map<String, String> dicts = null;
        	if(dictsMap.containsKey(luckySheetBindData.getDatasourceId()+"_"+luckySheetBindData.getDictType()))
        	{
        		dicts = dictsMap.get(luckySheetBindData.getDatasourceId()+"_"+luckySheetBindData.getDictType());
        	}else {
        		dicts = this.getDict(luckySheetBindData,dictCache);
        		dictsMap.put(luckySheetBindData.getDatasourceId()+"_"+luckySheetBindData.getDictType(), dicts);
        	}
        	if(dicts != null)
        	{
        		if(luckySheetBindData.getSourceType().intValue() == 2 || luckySheetBindData.getSourceType().intValue() == 3) {
        			value = dicts.get(bindDatas.get(j).get(0).get(property)+"");
        		}else {
        			value = dicts.get(luckySheetBindData.getDatasourceId()+"_"+luckySheetBindData.getDictType()+"_"+bindDatas.get(j).get(0).get(property));
        		}
        		if(value != null) {
        			bindDatas.get(j).get(0).put(property, value);
        		}
        	}
        }else if(customSpringReportFunction.isSpringReportFunction(property)) {
        	Map<String, Object> extraParams = new HashMap<>();
        	extraParams.put("index", j);
        	extraParams.put("userInfo", userInfoDto);
        	extraParams.put("viewParams", viewParams);
        	value = customSpringReportFunction.calculate(luckySheetBindData, extraParams);
        }else {
        	List<String> properties = null;
        	Map<String, Object> datas = null;
        	if(luckySheetBindData.getPropertiesCache() == null) {
        		datas = ListUtil.getProperties(luckySheetBindData.getProperty(), bindDatas.get(j).get(0));
        		properties = new ArrayList<String>();
            	for (String propertyKey : datas.keySet()) {
					properties.add(propertyKey);
				}
            	luckySheetBindData.setPropertiesCache(properties);
        	}else {
        		properties = luckySheetBindData.getPropertiesCache();
        		datas = ListUtil.getProperties(properties, bindDatas.get(j).get(0));
        	}
            Set<String> set = datas.keySet();
            String cellText = luckySheetBindData.getCellText();
            for (String o : set) {
            	if(StringUtil.isNotEmpty(luckySheetBindData.getCellText())) {
            		property = cellText.replace("${"+o+"}", datas.get(o)==null?"":String.valueOf(datas.get(o)));
            		cellText = property;
            	}else {
            		property = property.replace(o, datas.get(o)==null?"":String.valueOf(datas.get(o)));
            	}
            	if(set.size() == 1)
            	{
            		if(property.equals("") || property.length() == String.valueOf(datas.get(o)).length())
            		{
            			isJustProperty = true;
            		}
            	}
            }
            try {
            	if(set.size() > 1)
            	{
            		if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
            			value = AviatorEvaluator.execute(property);
					}else {
						value = property;
					}
            		if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
            		{
            			value = 0;
            		}
            	}else {
            		if(StringUtil.isImgUrl(property))
            		{
            			value = luckySheetBindData.getTplType() == 1?"":property;
    					isImg = true;
            		}else if(!isJustProperty)
            		{
            			if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
            				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
            				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
            				value = AviatorEvaluator.execute(property);
    					}else {
    						value = property;
    					}
            			if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
                		{
                			value = 0;
                		}
            		}else {
            			value = property;
            		}
            	}
    		} catch (Exception e) {
    			 
    		}
        }
        
        if(value == null)
        {
//        	value = property;
        }
      //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//        if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//    	{
//    		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//			if(jsonObject != null)
//			{
//				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//    			{
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//    			}else {
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//    			}
//			}
//    	}
        if(luckySheetBindData.getUnitTransfer()!=null && luckySheetBindData.getUnitTransfer())
		{
			value  = this.processUnitTransfer(value, luckySheetBindData);
		}
        String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
    	Object v = LuckysheetUtil.formatValue(format, value);
    	if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue() && "list".equals(luckySheetBindData.getAggregateType())) {
    		if(luckySheetBindData.getIsRelyCell().intValue() == YesNoEnum.YES.getCode().intValue()) {
    			if(luckySheetBindData.getLastCoordsx() == null) {
        			int colSpan = luckySheetBindData.getColSpan();
            		value = SheetUtil.calculateFormula(property,0, 1);
        		}else {
        			int colSpan = luckySheetBindData.getColSpan();
            		value = SheetUtil.calculateFormula(property,colSpan*luckySheetBindData.getRelyIndex(), 1);
        		}
    		}else {
    			int colSpan = luckySheetBindData.getColSpan();
        		value = SheetUtil.calculateFormula(property,colSpan*j, 1);
    		}
    	}
    	((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
    	((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);
    	if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue())
    	{
    		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.FUNCTION.getCode(), value);
    		JSONObject jsonObject = new JSONObject();
			jsonObject.put("r", rowAndCol.get("maxX"));
			jsonObject.put("c", rowAndCol.get("maxY"));
			calcChain.add(jsonObject);
    	}
    	this.processDynamicRange(luckySheetBindData, dynamicRange, rowAndCol.get("maxX").intValue(), rowAndCol.get("maxY").intValue(), cellData);
    	if(dataRowLen != null)
        {
    		if(rowlen.get(String.valueOf(rowAndCol.get("maxX")))==null)
    		{
    			rowlen.put(String.valueOf(rowAndCol.get("maxX")), dataRowLen);
    		}
        }
        if(dataColLen != null)
        {
        	if(columnlen.get(String.valueOf(rowAndCol.get("maxY")))== null)
        	{
        		columnlen.put(String.valueOf(rowAndCol.get("maxY")), dataColLen);
        	}
        }
        this.isOperationCol(luckySheetBindData, rowAndCol.get("maxY"), isExport, (Map) colhidden);
        int groupMergeRows = 1;
        if(luckySheetBindData.getIsGroupMerge())
        {
        	groupMergeRows = luckySheetBindData.getGroupMergeSize().get(j)*luckySheetBindData.getColSpan();
        	cellDatas.add(cellData);
        	if(luckySheetBindData.getIsMerge().intValue() == 1  || groupMergeRows > 1) {
        		Map<String, Object> mergeConfig = new HashMap<String, Object>();
    			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),luckySheetBindData.getRowSpan());
    			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), groupMergeRows);
    			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
    			Map<String, Object> merge = new HashMap<String, Object>();
    			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
    			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
    			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), groupMergeRows);
    			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
    			for (int k = 1; k <= luckySheetBindData.getRowSpan(); k++) {
    				for (int i = 1; i <= groupMergeRows; i++) {
    					if(k == 1 && i == 1)
    					{
    						//do nothing
    					}else {
    						Map<String, Object> mc = new HashMap<String, Object>();
    						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    						Map<String, Object> cellValue = new HashMap<String, Object>();
    						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
    						Map<String, Object> mergeCellData = new HashMap<String, Object>();
    						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
    						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
    						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
    						cellDatas.add(mergeCellData);
    						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i-1));
            				if(mcDataColLen != null)
    						{
            					if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i-1))== null)
            					{
            						columnlen.put(String.valueOf(rowAndCol.get("maxY")+i-1), mcDataColLen);
            					}
    						}
            				usedCells.put((rowAndCol.get("maxX")+k-1)+"_"+(rowAndCol.get("maxY")+i-1), cellData);
    					}
    				}
    				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
        			if(mcDataRowLen != null)
					{
        				if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k-1))==null)
        				{
        					rowlen.put(String.valueOf(rowAndCol.get("maxX")+k-1), mcDataRowLen);
        				}
					}
    			}
        	}
            if(rowAndCol.get("maxY") + groupMergeRows - 1 > maxY)
            {
            	maxY = rowAndCol.get("maxY") + luckySheetBindData.getColSpan() - 1;
            }
            String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
            this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+groupMergeRows-1,borderInfo,luckySheetBindData,borderKey);
//            List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
//            if(!ListUtil.isEmpty(cellBorder))
//    		{
//    			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//    			borderInfos.addAll(cellBorder);
//    		}
        }else {
        	if(bindDatas.get(j).size()>1)
            {
            	Map<String, Object> mergeConfig = new HashMap<String, Object>();
        		mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        		mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        		int rowSpan = luckySheetBindData.getRowSpan();
        		int colSpan = bindDatas.get(j).size();
        		if(luckySheetBindData.getIsMerge().intValue() == 1)
        		{
        			colSpan = bindDatas.get(j).size() * luckySheetBindData.getColSpan();
        		}
        		mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
        		mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
        		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
        		cellDatas.add(cellData);
        		Map<String, Object> merge = new HashMap<String, Object>();
        		merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        		merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        		merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), rowSpan);
        		merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
        		mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
        		for (int k = 1; k <= rowSpan; k++) {
        			for (int i = 1; i <= colSpan; i++) {
        				int dataIndex = i/luckySheetBindData.getColSpan();
        				if(k == 1 && i == 1)
    					{
    						//do nothing
    					}else {
    						Map<String, Object> mc = new HashMap<String, Object>();
    	    				mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
    	    				mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
    	    				Map<String, Object> cellValue = new HashMap<String, Object>();
    	    				Map<String, Object> mergeCellData = new HashMap<String, Object>();
    	    				mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
    	    				mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
    	    				mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
    	    				cellDatas.add(mergeCellData);
    	    				Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i-dataIndex*luckySheetBindData.getColSpan()));
            				if(mcDataColLen != null)
    						{
            					if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
            					{
            						columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
            					}
    						}
            				usedCells.put((rowAndCol.get("maxX")+k-1)+"_"+(rowAndCol.get("maxY")+i-1), cellData);
    					}
        				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
            			if(mcDataRowLen != null)
    					{
            				if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
            				{
            					rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
            				}
    					}
        			}
        		}
        		if((rowAndCol.get("maxY")+colSpan-1)>maxY)
        		{
        			maxY = rowAndCol.get("maxY")+colSpan-1;
        		}
        		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
        		this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1,borderInfo,luckySheetBindData,borderKey);
//        		List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+rowSpan-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+colSpan-1);
//        		if(!ListUtil.isEmpty(cellBorder))
//         		{
//         			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//         			borderInfos.addAll(cellBorder);
//         		}
            }else {
            	if(luckySheetBindData.getIsMerge().intValue() == 1) {
            		Map<String, Object> mergeConfig = new HashMap<String, Object>();
        			mergeConfig.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        			mergeConfig.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        			mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(),luckySheetBindData.getRowSpan());
        			mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
        			((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
        			cellDatas.add(cellData);
        			Map<String, Object> merge = new HashMap<String, Object>();
        			merge.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")); 
        			merge.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        			merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
        			merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
        			mergeMap.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), merge);
        			for (int k = 1; k <= luckySheetBindData.getRowSpan(); k++) {
        				for (int i = 1; i <= luckySheetBindData.getColSpan(); i++) {
        					if(k == 1 && i == 1)
        					{
        						//do nothing
        					}else {
        						Map<String, Object> mc = new HashMap<String, Object>();
        						mc.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX"));
        						mc.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY"));
        						Map<String, Object> cellValue = new HashMap<String, Object>();
        						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
        						Map<String, Object> mergeCellData = new HashMap<String, Object>();
        						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), rowAndCol.get("maxX")+k-1);
        						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), rowAndCol.get("maxY")+i-1);
        						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
        						cellDatas.add(mergeCellData);
        						Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
                				if(mcDataColLen != null)
        						{
                					if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i))== null)
                					{
                						columnlen.put(String.valueOf(rowAndCol.get("maxY")+i), mcDataColLen);
                					}
        						}
                				usedCells.put((rowAndCol.get("maxX")+k-1)+"_"+(rowAndCol.get("maxY")+i-1), cellData);
        					}
        				}
        				Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k));
            			if(mcDataRowLen != null)
    					{
            				if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k))==null)
            				{
            					rowlen.put(String.valueOf(rowAndCol.get("maxX")+k), mcDataRowLen);
            				}
    					}
        			}
        			if((rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1)>maxX)
        			{
        				maxX = rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1;
        			}
        			if((rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1)>maxY)
        			{
        				maxY = rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1;
        			}
        			String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
        			this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//        			List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1, rowAndCol.get("maxY"), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()-1);
//        			if(!ListUtil.isEmpty(cellBorder))
//            		{
//            			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//            			borderInfos.addAll(cellBorder);
//            		}
            	}else {
            		cellDatas.add(cellData);
                    if(rowAndCol.get("maxY")>maxY)
                    {
                    	maxY = rowAndCol.get("maxY");
                    }
                    if(rowAndCol.get("maxX")>maxX)
                    {
                    	maxX = rowAndCol.get("maxX");
                    }
                    String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
                    this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"),borderInfo,luckySheetBindData,borderKey);
//                    List<Map<String, Object>> cellBorder = this.borderProcess(border, rowAndCol.get("maxX"), rowAndCol.get("maxX"), rowAndCol.get("maxY"), rowAndCol.get("maxY"));
//                    if(!ListUtil.isEmpty(cellBorder))
//            		{
//            			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//            			borderInfos.addAll(cellBorder);
//            		}
            	}
            }
        }
        if(isImg)
		{
        	double top = LuckysheetUtil.calculateTop(rowlen, rowAndCol.get("maxX"),rowhidden);
			double left = LuckysheetUtil.calculateLeft(columnlen, rowAndCol.get("maxY"),colhidden);
			Object width = LuckysheetUtil.calculateWidth(columnlen, rowAndCol.get("maxY"), (luckySheetBindData.getIsGroupMerge()?groupMergeRows:bindDatas.get(j).size())*luckySheetBindData.getColSpan());
			Object height = LuckysheetUtil.calculateHeight(rowlen, rowAndCol.get("maxX"), luckySheetBindData.getRowSpan());
			JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
			imgInfo.getJSONObject("default").put("top", top);
			imgInfo.getJSONObject("default").put("left", left);
			imgInfo.getJSONObject("default").put("width", width);
			imgInfo.getJSONObject("default").put("height", height);
			imgInfo.getJSONObject("crop").put("width", width);
			imgInfo.getJSONObject("crop").put("height", height);
			imgInfo.put("src", property);
			JSONObject img = new JSONObject();
			img.put("imgInfo", imgInfo);
			img.put("r", rowAndCol.get("maxX"));
			img.put("c", rowAndCol.get("maxY"));
			if(dataRowLen != null)
			{
				img.put("height", dataRowLen);
			}else {
				img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
			}
			if(dataColLen != null)
			{
				img.put("width", dataColLen);
			}else {
				img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
			}
			if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge()) {
				img.put("isMerge", YesNoEnum.YES.getCode().intValue());
				img.put("rowSpan", luckySheetBindData.getRowSpan());
				img.put("colSpan", luckySheetBindData.getColSpan());
			}else {
				img.put("isMerge", YesNoEnum.NO.getCode().intValue());
			}
			if(luckySheetBindData.getIsGroupMerge())
			{
				if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge()) {
					img.put("isMerge", YesNoEnum.YES.getCode().intValue());
					img.put("rowSpan", luckySheetBindData.getRowSpan());
					img.put("colSpan", groupMergeRows);
				}else {
					img.put("isMerge", YesNoEnum.NO.getCode().intValue());
				}
			}else {
				int colSpan = bindDatas.get(j).size() * luckySheetBindData.getColSpan();
				int rowSpan = luckySheetBindData.getRowSpan();
				if(colSpan > 1 || rowSpan > 1)
				{
					img.put("isMerge", YesNoEnum.YES.getCode().intValue());
					img.put("rowSpan", rowSpan);
					img.put("colSpan", colSpan);
				}else {
					img.put("isMerge", YesNoEnum.NO.getCode().intValue());
				}
			}
			img.put("extend", 2);
			images.add(img);
		}
        if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsLink())
        {
            String linkAddress = String.valueOf(luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKADDRESS.getCode()));
            String newLinkAddress = UrlUtils.appendParam(linkAddress, luckySheetBindData.getProperty(), String.valueOf(bindDatas.get(j).get(0).get(luckySheetBindData.getProperty())));
            Map<String, Object> hyperLink = new HashMap<String, Object>();
            hyperLink.put(LuckySheetPropsEnum.LINKADDRESS.getCode(), newLinkAddress);
            hyperLink.put(LuckySheetPropsEnum.LINKTYPE.getCode(), luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKTYPE.getCode()));
            hyperLink.put(LuckySheetPropsEnum.LINKTOOLTIP.getCode(), luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKTOOLTIP.getCode()));
            hyperlinks.put(String.valueOf(rowAndCol.get("maxX"))+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(rowAndCol.get("maxY")), hyperLink);
        }
        if(luckySheetBindData.getIsRelyCell().intValue() == 1)
        {
        	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
        		maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan());
			}
        }else {
        	if(luckySheetBindData.getIsRelied().intValue() == 1)
        	{
        		for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
            		maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan());
    			}
        	}else {
        		if(j == bindDatas.size()-1)
                {
                	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
                		maxCoordinate.put("y-"+(luckySheetBindData.getCoordsy()+i), rowAndCol.get("maxX")+luckySheetBindData.getRowSpan());
        			}
                }
        	}
        }
        int cs = 1;
        if(cellData.containsKey("mc")) {
        	Map<String, Object> mc = (Map<String, Object>) cellData.get("mc");
        	cs = (int) mc.get("cs");
        }
        int c = (int) cellData.get(LuckySheetPropsEnum.C.getCode());
        for (int i = 0; i < cs; i++) {
        	String ckey = "y-"+ (c+i);
            if(maxCoordinate.containsKey(ckey)) {
            	int xvalue = maxCoordinate.get(ckey);
            	if(xvalue <rowAndCol.get("maxX")) {
            		maxCoordinate.put(ckey, rowAndCol.get("maxX")+1);
            	}
            }else {
            	maxCoordinate.put(ckey, rowAndCol.get("maxX")+1);
            }
		}
        if(luckySheetBindData.getIsGroupMerge())
        {
        	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
        		maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+groupMergeRows);
			}
        }else {
        	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
        		maxCoordinate.put("x-"+(luckySheetBindData.getCoordsx()+i), rowAndCol.get("maxY")+luckySheetBindData.getColSpan()*bindDatas.get(j).size());
        	}
        }
        maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
		if(luckySheetBindData.getIsChartAttr().intValue() == YesNoEnum.YES.getCode().intValue())
		{
			if(luckySheetBindData.getStartx() == null || luckySheetBindData.getStartx() > rowAndCol.get("maxX"))
			{
				luckySheetBindData.setStartx(rowAndCol.get("maxX"));
			}
 			if(luckySheetBindData.getStarty() == null || luckySheetBindData.getStarty() > rowAndCol.get("maxY"))
			{
				luckySheetBindData.setStarty(rowAndCol.get("maxY"));
			}
			if(luckySheetBindData.getEndx() == null || luckySheetBindData.getEndx() < (rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1))
			{
				luckySheetBindData.setEndx(rowAndCol.get("maxX")+luckySheetBindData.getRowSpan()-1);
			}
			if(luckySheetBindData.getEndy() == null || luckySheetBindData.getEndy() < (rowAndCol.get("maxY")+(luckySheetBindData.getIsGroupMerge()?groupMergeRows:bindDatas.get(j).size()*luckySheetBindData.getColSpan())-1))
			{
				luckySheetBindData.setEndy(rowAndCol.get("maxY")+(luckySheetBindData.getIsGroupMerge()?groupMergeRows:bindDatas.get(j).size()*luckySheetBindData.getColSpan())-1);
			}
		}
		if(luckySheetBindData.getIsRelyCell().intValue() == 1)
		{
			if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.VERTICAL.getCode().intValue())
			{
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX")+bindDatas.get(j).size()*luckySheetBindData.getRowSpan());
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY"));
			}else if(luckySheetBindData.getRelyCellExtend().intValue() == CellExtendEnum.HORIZONTAL.getCode().intValue()){
				luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX"));
				luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY")+(luckySheetBindData.getIsGroupMerge()?groupMergeRows:bindDatas.get(j).size()*luckySheetBindData.getColSpan()));
			}
		}
		if(luckySheetBindData.getIsRelied().intValue() == 1)
		{
			luckySheetBindData.setRecalculateCoords(YesNoEnum.NO.getCode().intValue());
			luckySheetBindData.setRelyCellExtend(CellExtendEnum.HORIZONTAL.getCode());
			luckySheetBindData.setLastCoordsx(rowAndCol.get("maxX"));
			luckySheetBindData.setLastCoordsy(rowAndCol.get("maxY")+(luckySheetBindData.getIsGroupMerge()?groupMergeRows:bindDatas.get(j).size()*luckySheetBindData.getColSpan()));
			
			this.processReliedCells(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, columnlen, 
					mergeMap, objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, calcChain, configRowLen, configColumnLen, images, 
					cellBindData,usedCells,dictsMap,nowFunction,functionCellFormat,dataVerification,drillCells,rowhidden,colhidden,null,null,null,cellConditionFormat,dynamicRange,subTotalDigits,coverCells,columnStartCoords,extendCellOrigin,subTotalCellCoords,isExport,dictCache,userInfoDto,viewParams);
		}
		return result;
	}
	
	/**  
	 * @Title: processCrossListGroupValue
	 * @Description: 处理交叉扩展数据
	 * @param j
	 * @param maxCoordinate
	 * @param luckySheetBindData
	 * @param cellDatas
	 * @param hyperlinks
	 * @param dataRowLen
	 * @param dataColLen
	 * @param rowlen
	 * @param columnlen
	 * @param maxX
	 * @param maxY
	 * @param mergeMap
	 * @param objectMapper
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @date 2022-03-21 07:52:51 
	 */ 
	private void processCrossListGroupValue(int j,Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,List<Map<String, Object>> cellDatas,
			Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen,Map<String, Object> rowlen,Map<String, Object> columnlen
			, Map<String, Map<String, Object>> mergeMap,ObjectMapper objectMapper,Map<String, Integer> rowAndCol,Map<String, Integer> maxXAndY,
			Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos,List<JSONObject> calcChain
			,Map<String, Object> configRowLen,Map<String, Object> configColumnLen, List<JSONObject> images,Map<String, Map<String, Object>> usedCells
			,Map<String, LuckySheetBindData> cellBindData,Map<String, Map<String, String>> dictsMap,JSONObject dataVerification,JSONObject drillCells,Object rowhidden,Object colhidden
			,Map<String, Object> subtotalCellDatas,Map<String, List<Map<String, Object>>> subCalculateDatas,Map<String, Object> subtotalCellMap,Map<String, JSONObject> subtotalRows
			,Map<String, JSONArray> cellConditionFormat,JSONObject dynamicRange,Map<String, JSONObject> subTotalDigits,List<String> subTotalCellCoords,Map<String, Map<String, String>> dictCache,UserInfoDto userInfoDto,Map<String, Object> viewParams) throws JsonMappingException, JsonProcessingException
	{
//		List<Map<String, Object>> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		int dataSize = luckySheetBindData.getDatas().get(j).size();
        for (int i = 0; i < luckySheetBindData.getDatas().get(j).size(); i++) {
			if(StringUtil.isEmptyMap(luckySheetBindData.getDatas().get(j).get(i))){
				dataSize = dataSize - 1;
			}
		}
        int size = 0;
        for (int i = 0; i <= j; i++) {
			for (int k = 0; k < luckySheetBindData.getDatas().get(i).size();k++) {
				if(!StringUtil.isEmptyMap(luckySheetBindData.getDatas().get(i).get(k))){
					size = size + 1;
				}
			}
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		String dataKey = luckySheetBindData.getSheetId()+"-"+(size-1)+"-"+luckySheetBindData.getCoordsx()+"-"+luckySheetBindData.getCoordsy();
        List<String> dataKeys = new ArrayList<>();
        if(ListUtil.isNotEmpty(subTotalCellCoords)) {
        	for (int i = 0; i < subTotalCellCoords.size(); i++) {
        		String key = dataKey + "-" + subTotalCellCoords.get(i);
        		dataKeys.add(key) ;
        	}
        }
        
        if(StringUtil.isNotEmpty(luckySheetBindData.getRelyCells()))
        {
        	String[] relyCells = luckySheetBindData.getRelyCells().replaceAll("_", "-").split(",");
        	for (int i = 0; i < relyCells.length; i++) {
        		dataKey = luckySheetBindData.getSheetId()+"-"+(size-1)+"-"+relyCells[i];
        		if(ListUtil.isNotEmpty(subTotalCellCoords)) {
        			for (int t = 0; t < subTotalCellCoords.size(); t++) {
        				String key = dataKey + "-" + subTotalCellCoords.get(t);
        				dataKeys.add(key);
        			}
        		}
        	}
        }
		if(CellExtendEnum.VERTICAL.getCode().intValue() == luckySheetBindData.getLastCellExtend().intValue())
        {//依赖数据向下扩展，则交叉数据的每组数据都向右扩展
            int x = rowAndCol.get("maxX");
//            if(!luckySheetBindData.getLastIsGroupMerge())
//            {//判断依赖的数据单元格是否合一，如果不合一则交叉扩展的数据也需要按照依赖的数据进行合并单元格
//                x = rowAndCol.get("maxX") + (j*luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getRowSpan());
//            }else {
//                x = rowAndCol.get("maxX") + j*luckySheetBindData.getRowSpan();
//            }
            x = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
            int y = rowAndCol.get("maxY");
            luckySheetBindData.setReliedCellSize(0);
        	List<String> properties = null;
        	Map<String, Object> datas = null;
            for (int n = 0; n < luckySheetBindData.getDatas().get(j).size(); n++) {
                y = rowAndCol.get("maxY") + n*luckySheetBindData.getColSpan()+luckySheetBindData.getReliedCellSize()*luckySheetBindData.getColSpan();
                List<Map<String, Object>> border = null;
        		if(!borderInfo.containsKey(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+y))
        		{
        			border = this.getBorderType(borderConfig, luckySheetBindData.getOriginalCoordsx(), luckySheetBindData.getOriginalCoordsy());//获取该单元格的边框信息
        		}else {
        			border = (List<Map<String, Object>>) borderInfo.get(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy());
        		}
                //该组数据向右扩展
                Map<String, Object> cellData = null;
                try {
                    cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cellData.put(LuckySheetPropsEnum.R.getCode(), x);
                cellData.put(LuckySheetPropsEnum.C.getCode(), y);
                if(luckySheetBindData.getAlternateFormat().intValue() == YesNoEnum.YES.getCode().intValue())
                {
                	if(j%2 == 0)
                	{
                		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcEven());
                		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcEven());
                	}else {
                		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcOdd());
                		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcOdd());
                	}
                }
                usedCells.put(x+"_"+y, cellData);
                if(luckySheetBindData.getPropertiesCache() == null) {
                	datas = ListUtil.getProperties(luckySheetBindData.getProperty(), luckySheetBindData.getDatas().get(j).get(n));
                	properties = new ArrayList<String>();
                	for (String key : datas.keySet()) {
    					properties.add(key);
    				}
                	luckySheetBindData.setPropertiesCache(properties);
                }else {
                	properties = luckySheetBindData.getPropertiesCache();
                	datas = ListUtil.getProperties(properties, luckySheetBindData.getDatas().get(j).get(n));
                }
                Set<String> set = datas.keySet();
                String property = luckySheetBindData.getProperty();
                boolean isJustProperty = false;
                if(ListUtil.isEmpty(set)) {
                	property = "";
                }else {
                	for (String o : set) {
                    	property = property.replace(o, datas.get(o)==null?"":String.valueOf(datas.get(o)));
                    	if(set.size() == 1)
    	            	{
    	            		if(property.equals("") || property.length() == String.valueOf(datas.get(o)).length())
    	            		{
    	            			isJustProperty = true;
    	            		}
    	            	}
                    }
                }
                
                Object value = null;
                boolean isImg = false;
                try {
                	if(set.size() > 1)
                	{
                		if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
                			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
                			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
                			value = AviatorEvaluator.execute(property);
    					}else {
    						value = property;
    					}
                		if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
                		{
                			value = 0;
                		}
                	}else {
                		if(StringUtil.isImgUrl(property))
                		{
                			value = luckySheetBindData.getTplType() == 1?"":property;
        					isImg = true;
                		}else if(!isJustProperty)
                		{
                			if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
                				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
                				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
                				value = AviatorEvaluator.execute(property);
        					}else {
        						value = property;
        					}
                			if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
                    		{
                    			value = 0;
                    		}
                		}else {
                			value = property;
                		}
                	}
        		} catch (Exception e) {
        			
        		}
                if(value == null)
                {
//                	value = property;
                }
              //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//                if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//            	{
//            		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//        			if(jsonObject != null)
//        			{
//        				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//        				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//            			{
//            				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//            			}else {
//            				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//            			}
//        			}
//            	}
            	if(luckySheetBindData.getUnitTransfer()!=null && luckySheetBindData.getUnitTransfer())
         		{
         			value  = this.processUnitTransfer(value, luckySheetBindData);
         		}
                String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
            	Object v = LuckysheetUtil.formatValue(format, value);
                ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
                ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);  
                if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue())
            	{
            		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.FUNCTION.getCode(), value);
            		JSONObject jsonObject = new JSONObject();
        			jsonObject.put("r", x);
        			jsonObject.put("c", y);
        			calcChain.add(jsonObject);
            	}
                String key = x+"_"+y;
                if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsDataVerification().intValue())
        		{
        			if(!dataVerification.containsKey(key))
        			{
        				dataVerification.put(key, objectMapper.readValue(luckySheetBindData.getDataVerification(), JSONObject.class));
        			}
        		}
                this.processCrossConditionFormat(luckySheetBindData, cellConditionFormat, x, y, n);
                this.processDynamicRange(luckySheetBindData, dynamicRange, x, y, cellData);
                if(luckySheetBindData.getIsDrill() && drillCells != null)
            	{
            		JSONObject drillInfo = new JSONObject();
            		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getDrillId()+"");
            		JSONObject drillParams = getDrillParams(luckySheetBindData.getDatas().get(j).get(n),luckySheetBindData.getDrillAttrs());
            		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
            		drillCells.put(key, drillInfo);
            	}
                if(dataRowLen != null)
                {
                	if(rowlen.get(String.valueOf(x))==null)
                	{
                		rowlen.put(String.valueOf(x), dataRowLen);
                	}
                }
                if(dataColLen != null)
                {
                	if(columnlen.get(String.valueOf(y))== null)
                	{
                		columnlen.put(String.valueOf(y), dataColLen);
                	}
                }
                if(!luckySheetBindData.getLastIsGroupMerge())
                {//判断依赖的数据单元格是否合一，如果不合一则交叉扩展的数据也需要按照依赖的数据进行合并单元格
                    Map<String, Object> mergeConfig = new HashMap<String, Object>();
                    mergeConfig.put(LuckySheetPropsEnum.R.getCode(), x);
                    mergeConfig.put(LuckySheetPropsEnum.C.getCode(), y);
                    mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), dataSize*luckySheetBindData.getRowSpan());
                    mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
                    ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
                    Map<String, Object> merge = new HashMap<String, Object>();
                    merge.put(LuckySheetPropsEnum.R.getCode(), x);
                    merge.put(LuckySheetPropsEnum.C.getCode(), y);
                    merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), dataSize*luckySheetBindData.getRowSpan());
                    merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
                    mergeMap.put(String.valueOf(x)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(y), merge);
                    for (int k = 0; k < dataSize*luckySheetBindData.getRowSpan(); k++) {
                    	int dataIndex = k/luckySheetBindData.getRowSpan();
                    	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
                    		if(k==0 && i==0)
                    		{
                    			//do nothing
                    		}else {
                    			((Map)rowhidden).put((x+k)+"", 0);
                    			Map<String, Object> mc = new HashMap<String, Object>();
                                mc.put(LuckySheetPropsEnum.R.getCode(), x);
                                mc.put(LuckySheetPropsEnum.C.getCode(), y);
                                Map<String, Object> cellValue = new HashMap<String, Object>();
                                cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
                                Map<String, Object> mergeCellData = new HashMap<String, Object>();
                                mergeCellData.put(LuckySheetPropsEnum.R.getCode(), x+k);
                                mergeCellData.put(LuckySheetPropsEnum.C.getCode(), y+i);
                                mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
                                cellDatas.add(mergeCellData);
                                Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
                                if(mcDataColLen != null)
        						{
                                	if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i+n*luckySheetBindData.getColSpan()))== null)
                                	{
                                		columnlen.put(String.valueOf(rowAndCol.get("maxY")+i+n*luckySheetBindData.getColSpan()), mcDataColLen);
                                	}
        						}
                                usedCells.put((x+k)+"_"+(y+i), cellData);
                    		}
						}
                    	Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k-dataIndex*luckySheetBindData.getRowSpan()));
                    	if(mcDataRowLen != null)
    					{
                    		if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k+j*dataSize*luckySheetBindData.getRowSpan()))==null)
                    		{
                    			rowlen.put(String.valueOf(rowAndCol.get("maxX")+k+j*dataSize*luckySheetBindData.getRowSpan()), mcDataRowLen);
                    		}
    					}
                    }
                }else {
                    Map<String, Object> mergeConfig = new HashMap<String, Object>();
                    mergeConfig.put(LuckySheetPropsEnum.R.getCode(), x);
                    mergeConfig.put(LuckySheetPropsEnum.C.getCode(), y);
                    mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
                    mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
                    ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
                    Map<String, Object> merge = new HashMap<String, Object>();
                    merge.put(LuckySheetPropsEnum.R.getCode(), x);
                    merge.put(LuckySheetPropsEnum.C.getCode(), y);
                    merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
                    merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
                    mergeMap.put(String.valueOf(x)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(y), merge);
                    for (int k = 0; k < luckySheetBindData.getRowSpan(); k++) {
                    	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
                    		if(k==0 && i==0)
                    		{
                    			//do nothing
                    		}else {
                    			Map<String, Object> mc = new HashMap<String, Object>();
                                mc.put(LuckySheetPropsEnum.R.getCode(), x);
                                mc.put(LuckySheetPropsEnum.C.getCode(), y);
                                Map<String, Object> cellValue = new HashMap<String, Object>();
                                cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
                                Map<String, Object> mergeCellData = new HashMap<String, Object>();
                                mergeCellData.put(LuckySheetPropsEnum.R.getCode(), x+k);
                                mergeCellData.put(LuckySheetPropsEnum.C.getCode(), y+i);
                                mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
                                cellDatas.add(mergeCellData);
                                usedCells.put((x+k)+"_"+(y+i), mergeCellData);
                    		}
                    		Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
                    		if(mcDataColLen != null)
    						{
                    			if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i+n*luckySheetBindData.getColSpan()))== null)
                    			{
                    				columnlen.put(String.valueOf(rowAndCol.get("maxY")+i+n*luckySheetBindData.getColSpan()), mcDataColLen);
                    			}
    						}
						}
                    }
                }
                if(isImg)
        		{
                	double top = LuckysheetUtil.calculateTop(rowlen, x,rowhidden);
        			double left = LuckysheetUtil.calculateLeft(columnlen, y,colhidden);
        			Object width = LuckysheetUtil.calculateWidth(columnlen, y, luckySheetBindData.getColSpan());
        			Object height = LuckysheetUtil.calculateHeight(rowlen, x, (luckySheetBindData.getIsGroupMerge()?1:dataSize)*luckySheetBindData.getRowSpan());
        			JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
        			imgInfo.getJSONObject("default").put("top", top);
        			imgInfo.getJSONObject("default").put("left", left);
        			imgInfo.getJSONObject("default").put("width", width);
        			imgInfo.getJSONObject("default").put("height", height);
        			imgInfo.getJSONObject("crop").put("width", width);
        			imgInfo.getJSONObject("crop").put("height", height);
        			imgInfo.put("src", property);
        			JSONObject img = new JSONObject();
        			img.put("imgInfo", imgInfo);
        			img.put("r", x);
        			img.put("c", y);
        			if(dataRowLen != null)
        			{
        				img.put("height", dataRowLen);
        			}else {
        				img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
        			}
        			if(dataColLen != null)
        			{
        				img.put("width", dataColLen);
        			}else {
        				img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
        			}
        			if(!luckySheetBindData.getLastIsGroupMerge())
        			{
        				img.put("isMerge", YesNoEnum.YES.getCode().intValue());
    					img.put("rowSpan", dataSize*luckySheetBindData.getRowSpan());
    					img.put("colSpan", luckySheetBindData.getColSpan());
        			}else {
        				int colSpan = luckySheetBindData.getColSpan();
        				int rowSpan = luckySheetBindData.getRowSpan();
        				if(colSpan > 1 || rowSpan > 1)
        				{
        					img.put("isMerge", YesNoEnum.YES.getCode().intValue());
        					img.put("rowSpan", rowSpan);
        					img.put("colSpan", colSpan);
        				}else {
        					img.put("isMerge", YesNoEnum.NO.getCode().intValue());
        				}
        			}
        			img.put("extend", 2);
        			images.add(img);
        		}
                if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsLink())
                {
                    String linkAddress = String.valueOf(luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKADDRESS.getCode()));
                    String newLinkAddress = UrlUtils.appendParam(linkAddress, luckySheetBindData.getProperty(), String.valueOf(luckySheetBindData.getDatas().get(j).get(n).get(luckySheetBindData.getProperty())));
                    Map<String, Object> hyperLink = new HashMap<String, Object>();
                    hyperLink.put(LuckySheetPropsEnum.LINKADDRESS.getCode(), newLinkAddress);
                    hyperLink.put(LuckySheetPropsEnum.LINKTYPE.getCode(), luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKTYPE.getCode()));
                    hyperLink.put(LuckySheetPropsEnum.LINKTOOLTIP.getCode(), luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKTOOLTIP.getCode()));
                    hyperlinks.put(String.valueOf(x)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(y), hyperLink);
                }
                cellDatas.add(cellData);
                if(!luckySheetBindData.getLastIsGroupMerge())
                {
                	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
                		maxCoordinate.put("y-" + (luckySheetBindData.getCoordsy()+i), x+dataSize*luckySheetBindData.getRowSpan());
                		maxCoordinate.put("y-" + (y), x+dataSize*luckySheetBindData.getRowSpan());
                	}
                	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
                		maxCoordinate.put("x-" + (luckySheetBindData.getCoordsx()+i), y+luckySheetBindData.getColSpan());
                	}
                	String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+y;
                	this.borderProcess(border, x, x+dataSize*luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//                	if(!ListUtil.isEmpty(border))
//                	{
//                		List<Map<String, Object>> cellBorder = this.borderProcess(border, x, x+luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderInfo,luckySheetBindData);
//                		if(!ListUtil.isEmpty(cellBorder))
//                		{
//                			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//                			borderInfos.addAll(cellBorder);
//                		}
//                	}
//                    
                    if(x+dataSize*luckySheetBindData.getRowSpan()>maxX)
                    {
                        maxX = x+dataSize*luckySheetBindData.getRowSpan();
                    }
                    if((y+luckySheetBindData.getColSpan()>maxY))
                    {
                        maxY = y+luckySheetBindData.getColSpan();
                    }
                }else {
                	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
                		maxCoordinate.put("y-" + (luckySheetBindData.getCoordsy()+i), x+luckySheetBindData.getRowSpan());
                		maxCoordinate.put("y-" + (y), x+luckySheetBindData.getRowSpan());
                	}
                	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
                		maxCoordinate.put("x-" + (luckySheetBindData.getCoordsx()+i), y+luckySheetBindData.getColSpan());	
                	}
                	String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+y;
                	this.borderProcess(border, x, x+luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//                	List<Map<String, Object>> cellBorder = this.borderProcess(border, x, x+luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData);
//            		if(!ListUtil.isEmpty(cellBorder))
//            		{
//            			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//            			borderInfos.addAll(cellBorder);
//            		}
            		if(x+luckySheetBindData.getRowSpan()>maxX)
                    {
                        maxX = x+luckySheetBindData.getRowSpan();
                    }
                    if(y+luckySheetBindData.getColSpan()>maxY)
                    {
                        maxY = y+luckySheetBindData.getColSpan();
                    }
                }
                List<Map<String, Object>> verticalGroupData = null;
                if(subCalculateDatas.containsKey(luckySheetBindData.getCoordsx()+"-"+luckySheetBindData.getCoordsy()+"-"+y+"-"+luckySheetBindData.getColSpan()+"-"+luckySheetBindData.getProperty()))
                {
                	verticalGroupData = subCalculateDatas.get(luckySheetBindData.getCoordsx()+"-"+luckySheetBindData.getCoordsy()+"-"+y+"-"+luckySheetBindData.getColSpan()+"-"+luckySheetBindData.getProperty());
                }else {
                	verticalGroupData = new ArrayList<>();
                	subCalculateDatas.put(luckySheetBindData.getCoordsx()+"-"+luckySheetBindData.getCoordsy()+"-"+y+"-"+luckySheetBindData.getColSpan()+"-"+luckySheetBindData.getProperty(), verticalGroupData);
                }
                verticalGroupData.add(luckySheetBindData.getDatas().get(j).get(n));
                if(luckySheetBindData.getIsRelied().intValue() == 1)
        		{
        			luckySheetBindData.setRecalculateCoords(YesNoEnum.NO.getCode().intValue());
        			luckySheetBindData.setLastCoordsx(x);
        			luckySheetBindData.setLastCoordsy(y);
        			luckySheetBindData.setRelyCellExtend(CellExtendEnum.CROSS.getCode());
        			luckySheetBindData.setLastCellExtend(CellExtendEnum.VERTICAL.getCode());
        			luckySheetBindData.setRelyCrossIndex(n);
        			this.processReliedCells(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, columnlen,
        					mergeMap, objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, calcChain, configRowLen, configColumnLen, 
        					images, cellBindData,usedCells,dictsMap,null,null,dataVerification,drillCells,rowhidden,colhidden,subtotalCellDatas,null,subCalculateDatas,cellConditionFormat,dynamicRange,subTotalDigits,null,null,null,subTotalCellCoords,false,dictCache,userInfoDto,viewParams);
        		}
            }
            for (int t = 0; t < dataKeys.size(); t++) {
            	if(subtotalCellDatas.containsKey(dataKeys.get(t)))
                {
                	Map<String, Integer> coords = new HashMap<>();
                	LuckySheetBindData bindData = new LuckySheetBindData();
                	for (Map.Entry<String, List<Map<String,Object>>> entry : subCalculateDatas.entrySet()) {
                		String[] splits = entry.getKey().split("-");
                		int r = Integer.parseInt(splits[0]);
    	   	       	    int c = Integer.parseInt(splits[1]);
    	   	       	    int y_ = Integer.parseInt(splits[2]);
    	   	       	    int colSpan = Integer.parseInt(splits[3]);
    	   	       	    String property = splits[4];
    	   	       	    bindData.setCoordsx(r);
    	   	       	    bindData.setCoordsy(c);
                        JSONObject cellData = new JSONObject();
                        if(coords.containsKey(r+"_"+c))
                        {
                        	maxX = coords.get(r+"_"+c);
                        }else {
                        	maxX = this.getMaxRow(maxCoordinate, r, c, 1);
                        	coords.put(r+"_"+c, maxX);
                        }
                        cellData.put("v", new JSONObject());
                        String[] splitKeys = dataKeys.get(t).split("-");
                        String keys = splitKeys[0] + "-" + splitKeys[1] +"-"+ r + "-" + c + "-" + splitKeys[4] + "-" + splitKeys[5];
                        JSONObject cellType = (JSONObject) subtotalCellDatas.get("cellType-"+keys);
                        if(cellType == null)
    		   	        {
    		   	        	cellType = new JSONObject();
    		   	        }
                        if(subtotalCellMap !=null && subtotalCellMap.containsKey(luckySheetBindData.getSheetId()+"-"+r+"-"+c))
                        {
                        	GroupSummaryData groupSummaryData = new GroupSummaryData();
        	   	       		groupSummaryData.setDigit(luckySheetBindData.getDigit());
        	   	       	    groupSummaryData.setProperty(property);
        	   	       	    groupSummaryData.setDatas(entry.getValue());
        	   	       	    String digitKey = luckySheetBindData.getSheetId()+"_"+r+"_"+c;
        	   	       	    if(subTotalDigits.containsKey(digitKey)) {
        	   	       	    	groupSummaryData.setDigit(subTotalDigits.get(digitKey).getInteger("digit"));
        	   	       	    }
	        	   	       	if(cellType.getInteger("type").intValue() == 6) {
	        	   	      		if(subTotalDigits.containsKey(digitKey)) {
	        	   	      			String compareAttr1 = subTotalDigits.get(digitKey).getString("compareAttr1");
	        	   	      			String compareAttr2 = subTotalDigits.get(digitKey).getString("compareAttr2");
	        	   	      			groupSummaryData.setCompareAttr1(compareAttr1);
	        	   	      	        groupSummaryData.setCompareAttr2(compareAttr2);
	        	   	      		}
	        	   	      	}
        	   	       	    Object value = luckySheetGroupCalculates.get(cellType.getInteger("type")!=null?cellType.getInteger("type"):1).calculate(groupSummaryData);
        	   	       	 if(subTotalDigits.containsKey(digitKey)) {
        	   	        	int digit = subTotalDigits.get(digitKey).getInteger("digit");
        	   	        	boolean unitTransfer = subTotalDigits.get(digitKey).getBooleanValue("unitTransfer");
        	   	        	if(unitTransfer) {
        	   	        		int transferType = subTotalDigits.get(digitKey).getInteger("transferType");
            	   	        	String multiple = subTotalDigits.get(digitKey).getString("multiple");
        	   	        		LuckySheetBindData binddata = new LuckySheetBindData();
        	   	        		binddata.setDigit(digit);
        	   	        		binddata.setUnitTransfer(unitTransfer);
        	   	        		binddata.setTransferType(transferType);
        	   	        		binddata.setMultiple(multiple);
        	   	        		value = this.processUnitTransfer(value, binddata);
        	   	        	}
        	   	        }
        	   	       	    cellData.put(LuckySheetPropsEnum.R.getCode(), maxX);
        	   	       	    cellData.put(LuckySheetPropsEnum.C.getCode(), y_);
        		   	       	cellData.getJSONObject("v").put("v", value);
        		   	        cellData.getJSONObject("v").put("m", value);
                        }else {
                        	cellData.put(LuckySheetPropsEnum.R.getCode(), maxX);
        	   	       	    cellData.put(LuckySheetPropsEnum.C.getCode(), y_);
        		   	       	cellData.getJSONObject("v").put("v", "");
        		   	        cellData.getJSONObject("v").put("m", "");
                        }
                        usedCells.put(maxX+"_"+y_, cellData);
    			   	     if(cellType.get("bgColor") != null)
    			         {
    			         	cellData.getJSONObject("v").put("bg", cellType.getString("bgColor"));
    			         }
    			         if(cellType.get("fontColor") != null)
    			         {
    			         	cellData.getJSONObject("v").put("fc", cellType.getString("fontColor"));
    			         }
    			         if(cellType.get("fontSize") != null)
    			         {
    			         	cellData.getJSONObject("v").put("fs", cellType.getString("fontSize"));
    			         }
    			         if(cellType.get("fontWeight") != null)
    			         {
    			         	cellData.getJSONObject("v").put("bl", cellType.getBoolean("fontWeight")?1:0);
    			         }
    		   	        if(colSpan > 1)
    		   	        {
    			   	        Map<String, Object> mergeConfig = new HashMap<String, Object>();
    		                mergeConfig.put(LuckySheetPropsEnum.R.getCode(), maxX);
    		                mergeConfig.put(LuckySheetPropsEnum.C.getCode(), y_);
    		                mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), 1);
    		                mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
    		                cellData.getJSONObject(LuckySheetPropsEnum.CELLCONFIG.getCode()).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
    		                Map<String, Object> merge = new HashMap<String, Object>();
    	                    merge.put(LuckySheetPropsEnum.R.getCode(), maxX);
    	                    merge.put(LuckySheetPropsEnum.C.getCode(), y_);
    	                    merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), 1);
    	                    merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), colSpan);
    	                    mergeMap.put(maxX+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+y_, merge);
    		   	        }
    		   	        cellDatas.add(cellData);
    			   	    List<Map<String, Object>> border2 = null;
    			 		if(!borderInfo.containsKey(r+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+c+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+y_))
    			 		{
    			 			border2 = this.getBorderType(borderConfig, r, c);//获取该单元格的边框信息
    			 		}
    			 		String borderKey = r+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+c+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+y_;
    			 		this.borderProcess(border2, maxX, maxX, y_, y_+colSpan-1,borderInfo,bindData,borderKey);
    			 		for (int i = 0; i < colSpan; i++) {
    			 			maxCoordinate.put("y-"+(c+i), maxX+1);
    					}
    	   	       	    maxCoordinate.put("x-"+r, maxY);
                    }
                	subCalculateDatas.clear();
                	break;
                }
			}
            
        }else if(CellExtendEnum.HORIZONTAL.getCode().intValue() == luckySheetBindData.getLastCellExtend().intValue()){
        	//该部分逻辑暂时忽略即可，没什么作用
        	List<Map<String, Object>> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
        	//依赖数据向右扩展，则交叉数据的每组数据都向下扩展 
            int x = rowAndCol.get("maxX");
            int y = rowAndCol.get("maxY");
            //判断依赖的数据单元格是否合一，如果不合一则交叉扩展的数据也需要按照依赖的数据进行合并单元格
            if(!luckySheetBindData.getLastIsGroupMerge())
            {
                y = rowAndCol.get("maxY") + (j*luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getColSpan());
            }else {
                y = rowAndCol.get("maxY") + j*luckySheetBindData.getColSpan();
            }
            luckySheetBindData.setReliedCellSize(0);
            for (int n = 0; n < luckySheetBindData.getDatas().get(j).size(); n++) {
                x = rowAndCol.get("maxX") + n*luckySheetBindData.getRowSpan()+luckySheetBindData.getReliedCellSize()*luckySheetBindData.getRowSpan();
                //分组数据向下扩展
                Map<String, Object> cellData = null;
                try {
                    cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cellData.put(LuckySheetPropsEnum.R.getCode(), x);
                cellData.put(LuckySheetPropsEnum.C.getCode(), y);
                if(luckySheetBindData.getAlternateFormat().intValue() == YesNoEnum.YES.getCode().intValue())
                {
                	if(j%2 == 0)
                	{
                		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcEven());
                		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcEven());
                	}else {
                		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcOdd());
                		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcOdd());
                	}
                }
                usedCells.put(x+"_"+y, cellData);
                Map<String, Object> datas = ListUtil.getProperties(luckySheetBindData.getProperty(), luckySheetBindData.getDatas().get(j).get(n));
                Set<String> set = datas.keySet();
                String property = luckySheetBindData.getProperty();
                boolean isJustProperty = false;
                for (String o : set) {
                	property = property.replace(o, datas.get(o)==null?"":String.valueOf(datas.get(o)));
                	if(set.size() == 1)
	            	{
	            		if(property.equals("") || property.length() == String.valueOf(datas.get(o)).length())
	            		{
	            			isJustProperty = true;
	            		}
	            	}
                }
                Object value = null;
                boolean isImg = false;
                try {
                	if(set.size() > 1)
                	{
                		if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
                			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
                			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
                			value = AviatorEvaluator.execute(property);
    					}else {
    						value = property;
    					}
                		if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
                		{
                			value = 0;
                		}
                	}else {
                		if(StringUtil.isImgUrl(property))
                		{
                			value = luckySheetBindData.getTplType() == 1?"":property;
        					isImg = true;
                		}else if(!isJustProperty)
                		{
                			if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
                				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
                				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
                				value = AviatorEvaluator.execute(property);
        					}else {
        						value = property;
        					}
                			if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
                    		{
                    			value = 0;
                    		}
                		}else {
                			value = property;
                		}
                	}
        		} catch (Exception e) {
        			
        		}
                if(value == null)
                {
//                	value = property;
                }
              //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//                if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//            	{
//            		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//        			if(jsonObject != null)
//        			{
//        				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//        				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//            			{
//            				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//            			}else {
//            				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//            			}
//        			}
//            	}
            	if(luckySheetBindData.getUnitTransfer()!=null && luckySheetBindData.getUnitTransfer())
         		{
         			value  = this.processUnitTransfer(value, luckySheetBindData);
         		}
                ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), value);
                ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), value);
                if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue())
            	{
            		((Map<String, Object>)luckySheetBindData.getCellData().get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.FUNCTION.getCode(), value);
            		JSONObject jsonObject = new JSONObject();
        			jsonObject.put("r", x);
        			jsonObject.put("c", y);
        			calcChain.add(jsonObject);
            	}
                String key = x+"_"+y;
                if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsDataVerification().intValue())
        		{
        			if(!dataVerification.containsKey(key))
        			{
        				dataVerification.put(key, objectMapper.readValue(luckySheetBindData.getDataVerification(), JSONObject.class));
        			}
        		}
                if(luckySheetBindData.getIsDrill() && drillCells != null)
            	{
            		JSONObject drillInfo = new JSONObject();
            		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getDrillId()+"");
            		JSONObject drillParams = getDrillParams(luckySheetBindData.getDatas().get(j).get(0),luckySheetBindData.getDrillAttrs());
            		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
            		drillCells.put(key, drillInfo);
            	}
                if(dataRowLen != null)
                {
                	if(rowlen.get(String.valueOf(x))==null)
                	{
                		rowlen.put(String.valueOf(x), dataRowLen);
                	}
                }
                if(dataColLen != null)
                {
                	if(columnlen.get(String.valueOf(y))== null)
                	{
                		columnlen.put(String.valueOf(y), dataColLen);
                	}
                }
                if(isImg)
        		{
                	double top = LuckysheetUtil.calculateTop(rowlen, x,rowhidden);
        			double left = LuckysheetUtil.calculateLeft(columnlen, y,colhidden);
        			Object width = LuckysheetUtil.calculateWidth(columnlen, y, (luckySheetBindData.getIsGroupMerge()?1:luckySheetBindData.getDatas().get(j).size())*luckySheetBindData.getColSpan());
        			Object height = LuckysheetUtil.calculateHeight(rowlen, x, luckySheetBindData.getRowSpan());
        			JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
        			imgInfo.getJSONObject("default").put("top", top);
        			imgInfo.getJSONObject("default").put("left", left);
        			imgInfo.getJSONObject("default").put("width", width);
        			imgInfo.getJSONObject("default").put("height", height);
        			imgInfo.getJSONObject("crop").put("width", width);
        			imgInfo.getJSONObject("crop").put("height", height);
        			imgInfo.put("src", property);
        			JSONObject img = new JSONObject();
        			img.put("imgInfo", imgInfo);
        			img.put("r", x);
        			img.put("c", y);
        			if(dataRowLen != null)
        			{
        				img.put("height", dataRowLen);
        			}else {
        				img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
        			}
        			if(dataColLen != null)
        			{
        				img.put("width", dataColLen);
        			}else {
        				img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
        			}
        			if(!luckySheetBindData.getLastIsGroupMerge())
        			{
        				img.put("isMerge", YesNoEnum.YES.getCode().intValue());
    					img.put("rowSpan", luckySheetBindData.getRowSpan());
    					img.put("colSpan", luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getColSpan());
        			}else {
        				int colSpan = luckySheetBindData.getColSpan();
        				int rowSpan = luckySheetBindData.getRowSpan();
        				if(colSpan > 1 || rowSpan > 1)
        				{
        					img.put("isMerge", YesNoEnum.YES.getCode().intValue());
        					img.put("rowSpan", rowSpan);
        					img.put("colSpan", colSpan);
        				}else {
        					img.put("isMerge", YesNoEnum.NO.getCode().intValue());
        				}
        			}
        			img.put("extend", 1);
        			images.add(img);
        		}
                if(!luckySheetBindData.getLastIsGroupMerge()) {
                    Map<String, Object> mergeConfig = new HashMap<String, Object>();
                    mergeConfig.put(LuckySheetPropsEnum.R.getCode(), x);
                    mergeConfig.put(LuckySheetPropsEnum.C.getCode(), y);
                    mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
                    mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getColSpan());
                    ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
                    Map<String, Object> merge = new HashMap<String, Object>();
                    merge.put(LuckySheetPropsEnum.R.getCode(), x);
                    merge.put(LuckySheetPropsEnum.C.getCode(), y);
                    merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
                    merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getColSpan());
                    mergeMap.put(String.valueOf(x)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(y), merge);
                    for (int k = 0; k < luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getColSpan(); k++) {
                    	int dataIndex = k/luckySheetBindData.getColSpan();
                    	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
                    		if(k==0 && i==0)
                    		{
                    			//do nothing
                    		}else {
                    			Map<String, Object> mc = new HashMap<String, Object>();
                                mc.put(LuckySheetPropsEnum.R.getCode(), x);
                                mc.put(LuckySheetPropsEnum.C.getCode(), y);
                                Map<String, Object> cellValue = new HashMap<String, Object>();
                                cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
                                Map<String, Object> mergeCellData = new HashMap<String, Object>();
                                mergeCellData.put(LuckySheetPropsEnum.R.getCode(), x+i);
                                mergeCellData.put(LuckySheetPropsEnum.C.getCode(), y+k);
                                mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
                                cellDatas.add(mergeCellData);
                                Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+i));
                                if(mcDataRowLen != null)
         						{
                                	if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+i+n*luckySheetBindData.getRowSpan()))==null)
                                	{
                                		rowlen.put(String.valueOf(rowAndCol.get("maxX")+i+n*luckySheetBindData.getRowSpan()), mcDataRowLen);
                                	}
         						}
                                usedCells.put((x+i)+"_"+(y+k), mergeCellData);
                    		}
                    	}
                    	Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+k-dataIndex*luckySheetBindData.getColSpan()));
                    	if(mcDataColLen != null)
    					{
                    		if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+k+j*luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getColSpan()))== null)
                    		{
                    			columnlen.put(String.valueOf(rowAndCol.get("maxY")+k+j*luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getColSpan()), mcDataColLen);
                    		}
    					}
                    }
                }else {
                	Map<String, Object> mergeConfig = new HashMap<String, Object>();
                    mergeConfig.put(LuckySheetPropsEnum.R.getCode(), x);
                    mergeConfig.put(LuckySheetPropsEnum.C.getCode(), y);
                    mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
                    mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
                    ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
                    Map<String, Object> merge = new HashMap<String, Object>();
                    merge.put(LuckySheetPropsEnum.R.getCode(), x);
                    merge.put(LuckySheetPropsEnum.C.getCode(), y);
                    merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
                    merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
                    mergeMap.put(String.valueOf(x)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(y), merge);
                    for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
                    	for (int k = 0; k < luckySheetBindData.getColSpan(); k++) {
                    		if(k == 0 && i == 0)
                    		{
                    			//do nothing
                    		}else {
                    			Map<String, Object> mc = new HashMap<String, Object>();
                                mc.put(LuckySheetPropsEnum.R.getCode(), x);
                                mc.put(LuckySheetPropsEnum.C.getCode(), y);
                                Map<String, Object> cellValue = new HashMap<String, Object>();
                                cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
                                Map<String, Object> mergeCellData = new HashMap<String, Object>();
                                mergeCellData.put(LuckySheetPropsEnum.R.getCode(), x+i);
                                mergeCellData.put(LuckySheetPropsEnum.C.getCode(), y+k);
                                mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
                                cellDatas.add(mergeCellData);
                                usedCells.put((x+i)+"_"+(y+k), mergeCellData);
                    		}
                    	}
                    	Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+i));
            			if(mcDataRowLen != null)
    					{
            				if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+i+n*luckySheetBindData.getRowSpan()))==null)
            				{
            					rowlen.put(String.valueOf(rowAndCol.get("maxX")+i+n*luckySheetBindData.getRowSpan()), mcDataRowLen);
            				}
    					}	
                    }
                }
                if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsLink())
                {
                    String linkAddress = String.valueOf(luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKADDRESS.getCode()));
                    String newLinkAddress = UrlUtils.appendParam(linkAddress, luckySheetBindData.getProperty(), String.valueOf(luckySheetBindData.getDatas().get(j).get(n).get(luckySheetBindData.getProperty())));
                    Map<String, Object> hyperLink = new HashMap<String, Object>();
                    hyperLink.put(LuckySheetPropsEnum.LINKADDRESS.getCode(), newLinkAddress);
                    hyperLink.put(LuckySheetPropsEnum.LINKTYPE.getCode(), luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKTYPE.getCode()));
                    hyperLink.put(LuckySheetPropsEnum.LINKTOOLTIP.getCode(), luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKTOOLTIP.getCode()));
                    hyperlinks.put(String.valueOf(x)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(y), hyperLink);
                }
                cellDatas.add(cellData);
                if(!luckySheetBindData.getLastIsGroupMerge())
                {
                	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
                		maxCoordinate.put("x-" + (luckySheetBindData.getCoordsx()+i), y+luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getColSpan());
                	}
                	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
                		maxCoordinate.put("y-" + (luckySheetBindData.getCoordsy()+i),x+luckySheetBindData.getRowSpan());
                	}
                	String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+n;
                	this.borderProcess(border, x, x+luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//                	List<Map<String, Object>> cellBorder = this.borderProcess(border, x, x+luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData);
//                	if(!ListUtil.isEmpty(cellBorder))
//            		{
//            			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//            			borderInfos.addAll(cellBorder);
//            		}
            		if(x+luckySheetBindData.getRowSpan()>maxX)
                    {
                        maxX = x+luckySheetBindData.getRowSpan();
                    }
                    if(y+luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getColSpan()>maxY)
                    {
                        maxY = y+luckySheetBindData.getDatas().get(j).size()*luckySheetBindData.getColSpan();
                    }
                }else {
                	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
                		maxCoordinate.put("x-" + (luckySheetBindData.getCoordsx()+i), y+luckySheetBindData.getColSpan());
                	}
                	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
                		maxCoordinate.put("y-" + (luckySheetBindData.getCoordsy()+i),x+luckySheetBindData.getRowSpan());
                	}
                	String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
                	this.borderProcess(border, x, x+luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//                	List<Map<String, Object>> cellBorder = this.borderProcess(border, x, x+luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData);
//                    if(!ListUtil.isEmpty(border))
//            		{
//            			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//            			borderInfos.addAll(cellBorder);
//            		}
            		if(x+luckySheetBindData.getRowSpan()>maxX)
                    {
                        maxX = x+luckySheetBindData.getRowSpan();
                    }
                    if(y+luckySheetBindData.getColSpan()>maxY)
                    {
                        maxY = y+luckySheetBindData.getColSpan();
                    }
                }
                if(luckySheetBindData.getIsRelied().intValue() == 1)
        		{
        			luckySheetBindData.setRecalculateCoords(YesNoEnum.NO.getCode().intValue());
        			luckySheetBindData.setLastCoordsx(x);
        			luckySheetBindData.setLastCoordsy(y);
        			luckySheetBindData.setRelyCellExtend(CellExtendEnum.CROSS.getCode());
        			luckySheetBindData.setLastCellExtend(CellExtendEnum.HORIZONTAL.getCode());
        			luckySheetBindData.setRelyCrossIndex(n);
        			this.processReliedCells(j, maxCoordinate, luckySheetBindData, cellDatas, hyperlinks, dataRowLen, dataColLen, rowlen, columnlen,
        					mergeMap, objectMapper, maxXAndY, borderInfo, borderConfig, borderInfos, calcChain, configRowLen, configColumnLen, images, 
        					cellBindData,usedCells,dictsMap,null,null,dataVerification,drillCells,rowhidden,colhidden,subtotalCellDatas,null,subCalculateDatas,cellConditionFormat,dynamicRange,subTotalDigits,null,null,null,subTotalCellCoords,false,dictCache,userInfoDto,viewParams);
        		}
            }
        }
        if(maxX > maxXAndY.get("maxX"))
        {
        	maxXAndY.put("maxX", maxX);
        }
        if(maxY > maxXAndY.get("maxY"))
        {
        	maxXAndY.put("maxY", maxY);
        }
	}
	
	private void processReliedCrossListGroupValue(int j,Map<String, Integer> maxCoordinate,LuckySheetBindData luckySheetBindData,List<Map<String, Object>> cellDatas,
			Map<String, Map<String, Object>> hyperlinks,Object dataRowLen,Object dataColLen,Map<String, Object> rowlen,Map<String, Object> columnlen
			, Map<String, Map<String, Object>> mergeMap,ObjectMapper objectMapper,Map<String, Integer> maxXAndY,
			Map<String, Object> borderInfo,List<Map<String, Object>> borderConfig,List<Object> borderInfos,List<JSONObject> calcChain
			,Map<String, Object> configRowLen,Map<String, Object> configColumnLen, List<JSONObject> images,Map<String, Map<String, Object>> usedCells
			,Map<String, LuckySheetBindData> cellBindData,JSONObject dataVerification,JSONObject drillCells,Object rowhidden,Object colhidden,Map<String, List<Map<String, Object>>> subCalculateDatas
			,Map<String, JSONArray> cellConditionFormat,JSONObject dynamicRange) throws JsonMappingException, JsonProcessingException
	{
		int n = luckySheetBindData.getRelyCrossIndex();
		int dataSize = luckySheetBindData.getDatas().get(0).size();
        for (int i = 0; i < luckySheetBindData.getDatas().get(0).size(); i++) {
			if(StringUtil.isEmptyMap(luckySheetBindData.getDatas().get(0).get(i))){
				dataSize = dataSize - 1;
			}
		}
//		List<Map<String, Object>> border = this.getBorderType(borderConfig, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy());//获取该单元格的边框信息
		List<Map<String, Object>> border = null;
		if(!borderInfo.containsKey(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+n))
		{
			border = this.getBorderType(borderConfig, luckySheetBindData.getOriginalCoordsx(), luckySheetBindData.getOriginalCoordsy());//获取该单元格的边框信息
		}else {
			border = (List<Map<String, Object>>) borderInfo.get(luckySheetBindData.getOriginalCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getOriginalCoordsy());
		}
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		Map<String, Integer> rowAndCol = new HashMap<String, Integer>();;
		if(CellExtendEnum.VERTICAL.getCode().intValue() == luckySheetBindData.getLastCellExtend().intValue())
		{
			int x = luckySheetBindData.getLastCoordsx();
			int y = this.getMaxCol(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
			this.processCrossConditionFormat(luckySheetBindData, cellConditionFormat, x, y, n);
			rowAndCol.put("maxX", x);
			rowAndCol.put("maxY", y);
			Map<String, Object> cellData = null;
            try {
                cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cellData.put(LuckySheetPropsEnum.R.getCode(), x);
            cellData.put(LuckySheetPropsEnum.C.getCode(), y);
            if(luckySheetBindData.getAlternateFormat().intValue() == YesNoEnum.YES.getCode().intValue())
            {
            	if(j%2 == 0)
            	{
            		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcEven());
            		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcEven());
            	}else {
            		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcOdd());
            		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcOdd());
            	}
            }
            usedCells.put(x+"_"+y, cellData);
            List<String> properties = null;
            Map<String, Object> datas = null;
        	if(luckySheetBindData.getPropertiesCache() == null) {
        		datas = ListUtil.getProperties(luckySheetBindData.getProperty(), luckySheetBindData.getDatas().get(0).get(n));
        		properties = new ArrayList<String>();
            	for (String key : datas.keySet()) {
					properties.add(key);
				}
            	luckySheetBindData.setPropertiesCache(properties);
        	}else {
        		properties = luckySheetBindData.getPropertiesCache();
        		datas = ListUtil.getProperties(properties, luckySheetBindData.getDatas().get(0).get(n));
        	}
            Set<String> set = datas.keySet();
            String property = luckySheetBindData.getProperty();
            boolean isJustProperty = false;
            if(ListUtil.isEmpty(set)) {
            	property = "";
            }else {
            	for (String o : set) {
                	property = property.replace(o, datas.get(o)==null?"":String.valueOf(datas.get(o)));
                	if(set.size() == 1)
                	{
                		if(property.equals("") || property.length() == String.valueOf(datas.get(o)).length())
                		{
                			isJustProperty = true;
                		}
                	}
                }
            }
            Object value = null;
            boolean isImg = false;
            try {
            	if(set.size() > 1)
            	{
            		if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
            			value = AviatorEvaluator.execute(property);
					}else {
						value = property;
					}
            		if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
            		{
            			value = 0;
            		}
            	}else {
            		if(StringUtil.isImgUrl(property))
            		{
            			value = luckySheetBindData.getTplType() == 1?"":property;
    					isImg = true;
            		}else if(!isJustProperty)
            		{
            			if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
            				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
            				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
            				value = AviatorEvaluator.execute(property);
    					}else {
    						value = property;
    					}
            			if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
                		{
                			value = 0;
                		}
            		}else {
            			value = property;
            		}
            	}
    		} catch (Exception e) {
    			
    		}
            if(value == null)
            {
//            	value = property;
            }
          //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//            if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//        	{
//        		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//    			if(jsonObject != null)
//    			{
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//    				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//        			{
//        				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//        			}else {
//        				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//        			}
//    			}
//        	}
        	if(luckySheetBindData.getUnitTransfer()!=null && luckySheetBindData.getUnitTransfer())
     		{
     			value  = this.processUnitTransfer(value, luckySheetBindData);
     		}
            String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
        	Object v = LuckysheetUtil.formatValue(format, value);
            ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
            ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);  
            if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue())
        	{
        		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.FUNCTION.getCode(), value);
        		JSONObject jsonObject = new JSONObject();
    			jsonObject.put("r", x);
    			jsonObject.put("c", y);
    			calcChain.add(jsonObject);
        	}
            String key = x+"_"+y;
            if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsDataVerification().intValue())
    		{
    			if(!dataVerification.containsKey(key))
    			{
    				dataVerification.put(key, objectMapper.readValue(luckySheetBindData.getDataVerification(), JSONObject.class));
    			}
    		}
            if(luckySheetBindData.getIsDrill() && drillCells != null)
        	{
        		JSONObject drillInfo = new JSONObject();
        		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getDrillId()+"");
        		JSONObject drillParams = getDrillParams(luckySheetBindData.getDatas().get(0).get(n),luckySheetBindData.getDrillAttrs());
        		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
        		drillCells.put(key, drillInfo);
        	}
            this.processDynamicRange(luckySheetBindData, dynamicRange, x, y, cellData);
            if(dataRowLen != null)
            {
            	if(rowlen.get(String.valueOf(x))==null)
            	{
            		rowlen.put(String.valueOf(x), dataRowLen);
            	}
            }
            if(dataColLen != null)
            {
            	if(columnlen.get(String.valueOf(y))== null)
            	{
            		columnlen.put(String.valueOf(y), dataColLen);
            	}
            }
            if(!luckySheetBindData.getLastIsGroupMerge())
            {//判断依赖的数据单元格是否合一，如果不合一则交叉扩展的数据也需要按照依赖的数据进行合并单元格
            	 Map<String, Object> mergeConfig = new HashMap<String, Object>();
                 mergeConfig.put(LuckySheetPropsEnum.R.getCode(), x);
                 mergeConfig.put(LuckySheetPropsEnum.C.getCode(), y);
                 mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), dataSize*luckySheetBindData.getRowSpan());
                 mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
                 ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
                 Map<String, Object> merge = new HashMap<String, Object>();
                 merge.put(LuckySheetPropsEnum.R.getCode(), x);
                 merge.put(LuckySheetPropsEnum.C.getCode(), y);
                 merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), dataSize*luckySheetBindData.getRowSpan());
                 merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
                 mergeMap.put(String.valueOf(x)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(y), merge);
                 for (int k = 0; k < dataSize*luckySheetBindData.getRowSpan(); k++) {
                 	int dataIndex = k/luckySheetBindData.getRowSpan();
                 	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
                 		if(k==0 && i==0)
                 		{
                 			//do nothing
                 		}else {
                 			Map<String, Object> mc = new HashMap<String, Object>();
                             mc.put(LuckySheetPropsEnum.R.getCode(), x);
                             mc.put(LuckySheetPropsEnum.C.getCode(), y);
                             Map<String, Object> cellValue = new HashMap<String, Object>();
                             cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
                             Map<String, Object> mergeCellData = new HashMap<String, Object>();
                             mergeCellData.put(LuckySheetPropsEnum.R.getCode(), x+k);
                             mergeCellData.put(LuckySheetPropsEnum.C.getCode(), y+i);
                             mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
                             cellDatas.add(mergeCellData);
                             Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
                             if(mcDataColLen != null)
     						 {
                            	 if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i+n*luckySheetBindData.getColSpan()))== null)
                            	 {
                            		 columnlen.put(String.valueOf(rowAndCol.get("maxY")+i+n*luckySheetBindData.getColSpan()), mcDataColLen);
                            	 }
     						 }
                             usedCells.put((x+k)+"_"+(y+i), mergeCellData);
                 		}
						}
                 	Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+k-dataIndex*luckySheetBindData.getRowSpan()));
                 	if(mcDataRowLen != null)
 					{
                 		if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+k+j*dataSize*luckySheetBindData.getRowSpan()))==null)
                 		{
                 			rowlen.put(String.valueOf(rowAndCol.get("maxX")+k+j*dataSize*luckySheetBindData.getRowSpan()), mcDataRowLen);
                 		}
 					}
                 }
            }else {
                Map<String, Object> mergeConfig = new HashMap<String, Object>();
                mergeConfig.put(LuckySheetPropsEnum.R.getCode(), x);
                mergeConfig.put(LuckySheetPropsEnum.C.getCode(), y);
                mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
                mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
                ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
                Map<String, Object> merge = new HashMap<String, Object>();
                merge.put(LuckySheetPropsEnum.R.getCode(), x);
                merge.put(LuckySheetPropsEnum.C.getCode(), y);
                merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
                merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
                mergeMap.put(String.valueOf(x)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(y), merge);
                for (int k = 0; k < luckySheetBindData.getRowSpan(); k++) {
                	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
                		if(k==0 && i==0)
                		{
                			//do nothing
                		}else {
                			Map<String, Object> mc = new HashMap<String, Object>();
                            mc.put(LuckySheetPropsEnum.R.getCode(), x);
                            mc.put(LuckySheetPropsEnum.C.getCode(), y);
                            Map<String, Object> cellValue = new HashMap<String, Object>();
                            cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
                            Map<String, Object> mergeCellData = new HashMap<String, Object>();
                            mergeCellData.put(LuckySheetPropsEnum.R.getCode(), x+k);
                            mergeCellData.put(LuckySheetPropsEnum.C.getCode(), y+i);
                            mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
                            cellDatas.add(mergeCellData);
                            usedCells.put((x+k)+"_"+(y+i), mergeCellData);
                		}
                		Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+i));
                		if(mcDataColLen != null)
						{
                			if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+i+n*luckySheetBindData.getColSpan()))== null)
                			{
                				columnlen.put(String.valueOf(rowAndCol.get("maxY")+i+n*luckySheetBindData.getColSpan()), mcDataColLen);
                			}
						}
					}
                }
            }
            if(isImg)
    		{
            	double top = LuckysheetUtil.calculateTop(rowlen, x,rowhidden);
    			double left = LuckysheetUtil.calculateLeft(columnlen, y,colhidden);
    			Object width = LuckysheetUtil.calculateWidth(columnlen, y, luckySheetBindData.getColSpan());
    			Object height = LuckysheetUtil.calculateHeight(rowlen, x, (luckySheetBindData.getLastIsGroupMerge()?1:dataSize)*luckySheetBindData.getRowSpan());
    			JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
    			imgInfo.getJSONObject("default").put("top", top);
    			imgInfo.getJSONObject("default").put("left", left);
    			imgInfo.getJSONObject("default").put("width", width);
    			imgInfo.getJSONObject("default").put("height", height);
    			imgInfo.getJSONObject("crop").put("width", width);
    			imgInfo.getJSONObject("crop").put("height", height);
    			imgInfo.put("src", property);
    			JSONObject img = new JSONObject();
    			img.put("imgInfo", imgInfo);
    			img.put("r", x);
    			img.put("c", y);
    			if(dataRowLen != null)
    			{
    				img.put("height", dataRowLen);
    			}else {
    				img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
    			}
    			if(dataColLen != null)
    			{
    				img.put("width", dataColLen);
    			}else {
    				img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
    			}
    			if(!luckySheetBindData.getLastIsGroupMerge())
    			{
    				img.put("isMerge", YesNoEnum.YES.getCode().intValue());
					img.put("rowSpan", dataSize*luckySheetBindData.getRowSpan());
					img.put("colSpan", luckySheetBindData.getColSpan());
    			}else {
    				int colSpan = luckySheetBindData.getColSpan();
    				int rowSpan = luckySheetBindData.getRowSpan();
    				if(colSpan > 1 || rowSpan > 1)
    				{
    					img.put("isMerge", YesNoEnum.YES.getCode().intValue());
    					img.put("rowSpan", rowSpan);
    					img.put("colSpan", colSpan);
    				}else {
    					img.put("isMerge", YesNoEnum.NO.getCode().intValue());
    				}
    			}
    			img.put("extend", 2);
    			images.add(img);
    		}
            if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsLink())
            {
                String linkAddress = String.valueOf(luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKADDRESS.getCode()));
                String newLinkAddress = UrlUtils.appendParam(linkAddress, luckySheetBindData.getProperty(), String.valueOf(luckySheetBindData.getDatas().get(0).get(n).get(luckySheetBindData.getProperty())));
                Map<String, Object> hyperLink = new HashMap<String, Object>();
                hyperLink.put(LuckySheetPropsEnum.LINKADDRESS.getCode(), newLinkAddress);
                hyperLink.put(LuckySheetPropsEnum.LINKTYPE.getCode(), luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKTYPE.getCode()));
                hyperLink.put(LuckySheetPropsEnum.LINKTOOLTIP.getCode(), luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKTOOLTIP.getCode()));
                hyperlinks.put(String.valueOf(x)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(y), hyperLink);
            }
            cellDatas.add(cellData);
            if(!luckySheetBindData.getLastIsGroupMerge())
            {
            	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
            		maxCoordinate.put("y-" + (luckySheetBindData.getCoordsy()+i), x+dataSize*luckySheetBindData.getRowSpan());
            	}
            	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
            		maxCoordinate.put("x-" + (luckySheetBindData.getCoordsx()+i), y+luckySheetBindData.getColSpan());
            	}
            	String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+n;
            	this.borderProcess(border, x, x+dataSize*luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//            	List<Map<String, Object>> cellBorder = this.borderProcess(border, x, x+luckySheetBindData.getDatas().get(0).size()*luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getColSpan()-1);
//            	if(!ListUtil.isEmpty(border))
//        		{
//        			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//        			borderInfos.addAll(cellBorder);
//        		}
                if(x+dataSize*luckySheetBindData.getRowSpan()>maxX)
                {
                    maxX = x+dataSize*luckySheetBindData.getRowSpan();
                }
                if((y+luckySheetBindData.getColSpan()-1>maxY))
                {
                    maxY = y+luckySheetBindData.getColSpan();
                }
            }else {
            	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
            		maxCoordinate.put("y-" + (luckySheetBindData.getCoordsy()+i), x+luckySheetBindData.getRowSpan());
            	}
            	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
            		maxCoordinate.put("x-" + (luckySheetBindData.getCoordsx()+i), y+luckySheetBindData.getColSpan());	
            	}
            	String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+n;
            	this.borderProcess(border, x, x+luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//            	List<Map<String, Object>> cellBorder = this.borderProcess(border, x, x+luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getColSpan()-1);
//            	if(!ListUtil.isEmpty(cellBorder))
//        		{
//        			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//        			borderInfos.addAll(cellBorder);
//        		}
        		if(x+luckySheetBindData.getRowSpan()>maxX)
                {
                    maxX = x+luckySheetBindData.getRowSpan();
                }
                if(y+luckySheetBindData.getColSpan()>maxY)
                {
                    maxY = y+luckySheetBindData.getColSpan();
                }
            }
            List<Map<String, Object>> verticalGroupData = null;
            if(subCalculateDatas.containsKey(luckySheetBindData.getCoordsx()+"-"+luckySheetBindData.getCoordsy()+"-"+y+"-"+luckySheetBindData.getColSpan()+"-"+luckySheetBindData.getProperty()))
            {
            	verticalGroupData = subCalculateDatas.get(luckySheetBindData.getCoordsx()+"-"+luckySheetBindData.getCoordsy()+"-"+y+"-"+luckySheetBindData.getColSpan()+"-"+luckySheetBindData.getProperty());
            }else {
            	verticalGroupData = new ArrayList<>();
            	subCalculateDatas.put(luckySheetBindData.getCoordsx()+"-"+luckySheetBindData.getCoordsy()+"-"+y+"-"+luckySheetBindData.getColSpan()+"-"+luckySheetBindData.getProperty(), verticalGroupData);
            }
            verticalGroupData.add(luckySheetBindData.getDatas().get(0).get(n));
		}else if(CellExtendEnum.HORIZONTAL.getCode().intValue() == luckySheetBindData.getLastCellExtend().intValue()){
			int x = this.getMaxRow(maxCoordinate, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsy(), 1);
			int y = luckySheetBindData.getLastCoordsy();
			rowAndCol.put("maxX", x);
			rowAndCol.put("maxY", y);
			Map<String, Object> cellData = null;
            try {
                cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cellData.put(LuckySheetPropsEnum.R.getCode(), x);
            cellData.put(LuckySheetPropsEnum.C.getCode(), y);
            if(luckySheetBindData.getAlternateFormat().intValue() == YesNoEnum.YES.getCode().intValue())
            {
            	if(j%2 == 0)
            	{
            		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcEven());
            		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcEven());
            	}else {
            		((Map)cellData.get("v")).put(LuckySheetPropsEnum.FONTCOLOR.getCode(), luckySheetBindData.getAlternateFormatFcOdd());
            		((Map)cellData.get("v")).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getAlternateFormatBcOdd());
            	}
            }
            usedCells.put(x+"_"+y, cellData);
            Map<String, Object> datas = ListUtil.getProperties(luckySheetBindData.getProperty(), luckySheetBindData.getDatas().get(0).get(n));
            Set<String> set = datas.keySet();
            String property = luckySheetBindData.getProperty();
            boolean isJustProperty = false;
            for (String o : set) {
            	property = property.replace(o, datas.get(o)==null?"":String.valueOf(datas.get(o)));
            	if(set.size() == 1)
            	{
            		if(property.equals("") || property.length() == String.valueOf(datas.get(o)).length())
            		{
            			isJustProperty = true;
            		}
            	}
            }
            Object value = null;
            boolean isImg = false;
            try {
            	if(set.size() > 1)
            	{
            		if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
            			AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
            			value = AviatorEvaluator.execute(property);
					}else {
						value = property;
					}
            		if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
            		{
            			value = 0;
            		}
            	}else {
            		if(StringUtil.isImgUrl(property))
            		{
            			value = luckySheetBindData.getTplType() == 1?"":property;
    					isImg = true;
            		}else if(!isJustProperty)
            		{
            			if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
            				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
            				AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
            				value = AviatorEvaluator.execute(property);
    					}else {
    						value = property;
    					}
            			if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
                		{
                			value = 0;
                		}
            		}else {
            			value = property;
            		}
            	}
    		} catch (Exception e) {
    			
    		}
            if(value == null)
            {
//            	value = property;
            }
          //2024年8月22日09:59:29注释掉，新增加了条件格式功能，不需要该功能了
//            if(luckySheetBindData.getWarning() && StringUtil.isNotEmpty(luckySheetBindData.getThreshold()) && CheckUtil.isNumber(luckySheetBindData.getThreshold()))
//        	{
//        		JSONObject jsonObject = this.processCellWarning(value, luckySheetBindData);
//    			if(jsonObject != null)
//    			{
//    				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.POSTIL.getCode(), jsonObject);
//    				if(StringUtil.isNotEmpty(luckySheetBindData.getWarningColor()))
//        			{
//        				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), luckySheetBindData.getWarningColor());
//        			}else {
//        				((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.BACKGROUND.getCode(), "#FF0000");
//        			}
//    			}
//        	}
        	if(luckySheetBindData.getUnitTransfer()!=null && luckySheetBindData.getUnitTransfer())
     		{
     			value  = this.processUnitTransfer(value, luckySheetBindData);
     		}
            String format = LuckysheetUtil.getCellFormat(luckySheetBindData.getCellData());
        	Object v = LuckysheetUtil.formatValue(format, value);
            ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
            ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);  
            if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue())
        	{
        		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.FUNCTION.getCode(), value);
        		JSONObject jsonObject = new JSONObject();
    			jsonObject.put("r", x);
    			jsonObject.put("c", y);
    			calcChain.add(jsonObject);
        	}
            String key = x+"_"+y;
            if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsDataVerification().intValue())
    		{
    			if(!dataVerification.containsKey(key))
    			{
    				dataVerification.put(key, objectMapper.readValue(luckySheetBindData.getDataVerification(), JSONObject.class));
    			}
    		}
            if(luckySheetBindData.getIsDrill() && drillCells != null)
        	{
        		JSONObject drillInfo = new JSONObject();
        		drillInfo.put(LuckySheetPropsEnum.DRILLID.getCode(), luckySheetBindData.getDrillId()+"");
        		JSONObject drillParams = getDrillParams(luckySheetBindData.getDatas().get(0).get(n),luckySheetBindData.getDrillAttrs());
        		drillInfo.put(LuckySheetPropsEnum.DRILLPARAMS.getCode(), drillParams);
        		drillCells.put(key, drillInfo);
        	}
            if(dataRowLen != null)
            {
            	if(rowlen.get(String.valueOf(x))==null)
            	{
            		rowlen.put(String.valueOf(x), dataRowLen);	
            	}
            }
            if(dataColLen != null)
            {
            	if(columnlen.get(String.valueOf(y))== null)
            	{
            		columnlen.put(String.valueOf(y), dataColLen);
            	}
            }
            if(!luckySheetBindData.getLastIsGroupMerge())
            {
            	//判断依赖的数据单元格是否合一，如果不合一则交叉扩展的数据也需要按照依赖的数据进行合并单元格
            	Map<String, Object> mergeConfig = new HashMap<String, Object>();
                mergeConfig.put(LuckySheetPropsEnum.R.getCode(), x);
                mergeConfig.put(LuckySheetPropsEnum.C.getCode(), y);
                mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
                mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getDatas().get(0).size()*luckySheetBindData.getColSpan());
                ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
                Map<String, Object> merge = new HashMap<String, Object>();
                merge.put(LuckySheetPropsEnum.R.getCode(), x);
                merge.put(LuckySheetPropsEnum.C.getCode(), y);
                merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getDatas().get(0).size()*luckySheetBindData.getRowSpan());
                merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
                mergeMap.put(String.valueOf(x)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(y), merge);
                for (int k = 0; k < luckySheetBindData.getDatas().get(0).size()*luckySheetBindData.getColSpan(); k++) {
                	int dataIndex = k/luckySheetBindData.getColSpan();
                	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
                		if(k==0 && i==0)
                		{
                			//do nothing
                		}else {
                			Map<String, Object> mc = new HashMap<String, Object>();
                            mc.put(LuckySheetPropsEnum.R.getCode(), x);
                            mc.put(LuckySheetPropsEnum.C.getCode(), y);
                            Map<String, Object> cellValue = new HashMap<String, Object>();
                            cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
                            Map<String, Object> mergeCellData = new HashMap<String, Object>();
                            mergeCellData.put(LuckySheetPropsEnum.R.getCode(), x+i);
                            mergeCellData.put(LuckySheetPropsEnum.C.getCode(), y+k);
                            mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
                            cellDatas.add(mergeCellData);
                            Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+i));
                            if(mcDataRowLen != null)
     						{
                            	if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+i+n*luckySheetBindData.getRowSpan()))==null)
                            	{
                            		rowlen.put(String.valueOf(rowAndCol.get("maxX")+i+n*luckySheetBindData.getRowSpan()), mcDataRowLen);
                            	}
     						}
                            usedCells.put((x+i)+"_"+(y+k), mergeCellData);
                		}
                	}
                	Object mcDataColLen = configColumnLen.get(String.valueOf(luckySheetBindData.getCoordsy()+k-dataIndex*luckySheetBindData.getColSpan()));
                	if(mcDataColLen != null)
					{
                		if(columnlen.get(String.valueOf(rowAndCol.get("maxY")+k+j*luckySheetBindData.getDatas().get(0).size()*luckySheetBindData.getColSpan()))== null)
                		{
                			columnlen.put(String.valueOf(rowAndCol.get("maxY")+k+j*luckySheetBindData.getDatas().get(0).size()*luckySheetBindData.getColSpan()), mcDataColLen);
                		}
					}
                }
            }else {
            	Map<String, Object> mergeConfig = new HashMap<String, Object>();
                mergeConfig.put(LuckySheetPropsEnum.R.getCode(), x);
                mergeConfig.put(LuckySheetPropsEnum.C.getCode(), y);
                mergeConfig.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
                mergeConfig.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
                ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.MERGECELLS.getCode(), mergeConfig);
                Map<String, Object> merge = new HashMap<String, Object>();
                merge.put(LuckySheetPropsEnum.R.getCode(), x);
                merge.put(LuckySheetPropsEnum.C.getCode(), y);
                merge.put(LuckySheetPropsEnum.ROWSPAN.getCode(), luckySheetBindData.getRowSpan());
                merge.put(LuckySheetPropsEnum.COLSPAN.getCode(), luckySheetBindData.getColSpan());
                mergeMap.put(String.valueOf(x)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(y), merge);
                for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
                	for (int k = 0; k < luckySheetBindData.getColSpan(); k++) {
                		if(k == 0 && i == 0)
                		{
                			//do nothing
                		}else {
                			Map<String, Object> mc = new HashMap<String, Object>();
                            mc.put(LuckySheetPropsEnum.R.getCode(), x);
                            mc.put(LuckySheetPropsEnum.C.getCode(), y);
                            Map<String, Object> cellValue = new HashMap<String, Object>();
                            cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
                            Map<String, Object> mergeCellData = new HashMap<String, Object>();
                            mergeCellData.put(LuckySheetPropsEnum.R.getCode(), x+i);
                            mergeCellData.put(LuckySheetPropsEnum.C.getCode(), y+k);
                            mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
                            cellDatas.add(mergeCellData);
                            usedCells.put((x+i)+"_"+(y+k), mergeCellData);
                		}
                	}
                	Object mcDataRowLen = configRowLen.get(String.valueOf(luckySheetBindData.getCoordsx()+i));
        			if(mcDataRowLen != null)
					{
        				if(rowlen.get(String.valueOf(rowAndCol.get("maxX")+i+n*luckySheetBindData.getRowSpan()))==null)
        				{
        					rowlen.put(String.valueOf(rowAndCol.get("maxX")+i+n*luckySheetBindData.getRowSpan()), mcDataRowLen);
        				}
					}	
                }
            }
            if(isImg)
    		{
            	double top = LuckysheetUtil.calculateTop(rowlen, x,rowhidden);
    			double left = LuckysheetUtil.calculateLeft(columnlen, y,colhidden);
    			Object width = LuckysheetUtil.calculateWidth(columnlen, y, (luckySheetBindData.getLastIsGroupMerge()?1:luckySheetBindData.getDatas().get(0).size())*luckySheetBindData.getColSpan());
    			Object height = LuckysheetUtil.calculateHeight(rowlen, x, luckySheetBindData.getRowSpan());
    			JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
    			imgInfo.getJSONObject("default").put("top", top);
    			imgInfo.getJSONObject("default").put("left", left);
    			imgInfo.getJSONObject("default").put("width", width);
    			imgInfo.getJSONObject("default").put("height", height);
    			imgInfo.getJSONObject("crop").put("width", width);
    			imgInfo.getJSONObject("crop").put("height", height);
    			imgInfo.put("src", property);
    			JSONObject img = new JSONObject();
    			img.put("imgInfo", imgInfo);
    			img.put("r", x);
    			img.put("c", y);
    			if(dataRowLen != null)
    			{
    				img.put("height", dataRowLen);
    			}else {
    				img.put("height", Constants.DEFAULT_LUCKYSHEET_CELL_HEIGHT);
    			}
    			if(dataColLen != null)
    			{
    				img.put("width", dataColLen);
    			}else {
    				img.put("width", Constants.DEFAULT_LUCKYSHEET_CELL_WIDTH);
    			}
    			if(!luckySheetBindData.getLastIsGroupMerge())
    			{
    				img.put("isMerge", YesNoEnum.YES.getCode().intValue());
					img.put("rowSpan", luckySheetBindData.getDatas().get(0).size()*luckySheetBindData.getRowSpan());
					img.put("colSpan", luckySheetBindData.getColSpan());
    			}else {
    				int colSpan = luckySheetBindData.getColSpan();
    				int rowSpan = luckySheetBindData.getRowSpan();
    				if(colSpan > 1 || rowSpan > 1)
    				{
    					img.put("isMerge", YesNoEnum.YES.getCode().intValue());
    					img.put("rowSpan", rowSpan);
    					img.put("colSpan", colSpan);
    				}else {
    					img.put("isMerge", YesNoEnum.NO.getCode().intValue());
    				}
    			}
    			img.put("extend", 2);
    			images.add(img);
    		}
            if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsLink())
            {
                String linkAddress = String.valueOf(luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKADDRESS.getCode()));
                String newLinkAddress = UrlUtils.appendParam(linkAddress, luckySheetBindData.getProperty(), String.valueOf(luckySheetBindData.getDatas().get(0).get(n).get(luckySheetBindData.getProperty())));
                Map<String, Object> hyperLink = new HashMap<String, Object>();
                hyperLink.put(LuckySheetPropsEnum.LINKADDRESS.getCode(), newLinkAddress);
                hyperLink.put(LuckySheetPropsEnum.LINKTYPE.getCode(), luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKTYPE.getCode()));
                hyperLink.put(LuckySheetPropsEnum.LINKTOOLTIP.getCode(), luckySheetBindData.getLinkConfig().get(LuckySheetPropsEnum.LINKTOOLTIP.getCode()));
                hyperlinks.put(String.valueOf(x)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+String.valueOf(y), hyperLink);
            }
            cellDatas.add(cellData);
            if(!luckySheetBindData.getLastIsGroupMerge())
            {
            	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
            		maxCoordinate.put("x-" + (luckySheetBindData.getCoordsx()+i), y+luckySheetBindData.getDatas().get(0).size()*luckySheetBindData.getColSpan());
            	}
            	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
            		maxCoordinate.put("y-" + (luckySheetBindData.getCoordsy()+i),x+luckySheetBindData.getRowSpan());
            	}
            	String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+n;
            	this.borderProcess(border, x, x+luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getDatas().get(0).size()*luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//            	List<Map<String, Object>> cellBorder = this.borderProcess(border, x, x+luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getDatas().get(0).size()*luckySheetBindData.getColSpan()-1);
//            	if(!ListUtil.isEmpty(cellBorder))
//        		{
//        			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//        			borderInfos.addAll(cellBorder);
//        		}
        		if(x+luckySheetBindData.getRowSpan()>maxX)
                {
                    maxX = x+luckySheetBindData.getRowSpan();
                }
                if(y+luckySheetBindData.getDatas().get(0).size()*luckySheetBindData.getColSpan()>maxY)
                {
                    maxY = y+luckySheetBindData.getDatas().get(0).size()*luckySheetBindData.getColSpan();
                }
            }else {
            	for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
            		maxCoordinate.put("x-" + (luckySheetBindData.getCoordsx()+i), y+luckySheetBindData.getColSpan());
            	}
            	for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
            		maxCoordinate.put("y-" + (luckySheetBindData.getCoordsy()+i),x+luckySheetBindData.getRowSpan());
            	}
            	String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+n;
            	this.borderProcess(border, x, x+luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
//            	List<Map<String, Object>> cellBorder = this.borderProcess(border, x, x+luckySheetBindData.getRowSpan()-1, y, y+luckySheetBindData.getColSpan()-1);
//                if(!ListUtil.isEmpty(cellBorder))
//        		{
//        			//borderInfo.put(luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy(), true);
//        			borderInfos.addAll(cellBorder);
//        		}
        		if(x+luckySheetBindData.getRowSpan()>maxX)
                {
                    maxX = x+luckySheetBindData.getRowSpan();
                }
                if(y+luckySheetBindData.getColSpan()>maxY)
                {
                    maxY = y+luckySheetBindData.getColSpan();
                }
            }
		}
		maxXAndY.put("maxX", maxX);
		maxXAndY.put("maxY", maxY);
	}
	
	/**  
	 * @Title: getBorderType
	 * @Description: 获取边框类型
	 * @param borderConfig 边框配置信息
	 * @param coordsx 横坐标
	 * @param coordsy 纵坐标
	 * @author caiyang
	 * @date 2022-03-24 08:07:14 
	 */ 
	private List<Map<String, Object>> getBorderType(List<Map<String, Object>> borderConfig,int coordsx,int coordsy) {
		List<Map<String, Object>> borders = new ArrayList<>();
		if(!ListUtil.isEmpty(borderConfig))
		{
			Map<String, Object> result = null;
			Map<String, Integer> borderTypes = new HashMap<>();
			for (int i = 0; i < borderConfig.size(); i++) {
				String rangeType = (String) borderConfig.get(i).get(LuckySheetPropsEnum.RANGETYPE.getCode());
				if(!"range".equals(rangeType))
				{
					Map<String, Object> map = (Map<String, Object>) borderConfig.get(i).get("value");
					int row_index = (int) map.get("row_index");
					int col_index = (int) map.get("col_index");
					if(coordsx == row_index && coordsy == col_index)
					{
						result = borderConfig.get(i);
					}
				}else {
					List<Map<String, Object>> range = (List<Map<String, Object>>) borderConfig.get(i).get(LuckySheetPropsEnum.BORDERRANGE.getCode());
					for (int j = 0; j < range.size(); j++) {
						List<Integer> column = (List<Integer>) range.get(j).get(LuckySheetPropsEnum.BORDERCOLUMNRANGE.getCode()); 
						List<Integer> row = (List<Integer>) range.get(j).get(LuckySheetPropsEnum.BORDERROWRANGE.getCode()); 
						if(coordsx >= row.get(0).intValue() && coordsx <= row.get(1).intValue() && coordsy >= column.get(0).intValue() && coordsy <= column.get(1).intValue())
						{
							result = borderConfig.get(i);
							break;
						}
					}
				}
				
				if(result != null)
				{
					if(BorderTypeEnum.BORDERNONE.getCode().equals(result.get(LuckySheetPropsEnum.BORDERTYPE.getCode())))
					{
						result = null;
						borders = null;
						borders = new ArrayList<>();
						borderTypes = null;
						borderTypes = new HashMap<>();
					}else {
						if(!borderTypes.containsKey(String.valueOf(result.get(LuckySheetPropsEnum.BORDERTYPE.getCode()))))
						{
							borders.add(result);
							borderTypes.put(String.valueOf(result.get(LuckySheetPropsEnum.BORDERTYPE.getCode())),borders.size()-1);
						}else {
							int index = borderTypes.get(String.valueOf(result.get(LuckySheetPropsEnum.BORDERTYPE.getCode())));
							borders.set(index, result);
						}
					}
				}else {
//					result = new HashMap<>();
//					result.put("borderType", "border-none");
//					result.put("rangeType", "range");
//					result.put("color",  "#000");
//					JSONArray range = new JSONArray();
//					JSONObject rangeObj = new JSONObject();
//					JSONArray row = new JSONArray();
//					row.add(coordsx);
//					row.add(coordsx);
//					JSONArray column = new JSONArray();
//					column.add(coordsy);
//					column.add(coordsy);
//					range.add(rangeObj);
//					rangeObj.put("row", row);
//					rangeObj.put("column", column);
//					result.put("range", range);
//					result.put("style", 1);
//					borders.add(result);
//					borderTypes.put(String.valueOf(result.get(LuckySheetPropsEnum.BORDERTYPE.getCode())),borders.size()-1);
				}
			}
		}
		return borders;
	}
	
	/**  
	 * @Title: luckySheetExportExcel
	 * @Description: luckysheet导出excel
	 * @param mesGenerateReportDto
	 * @param httpServletResponse
	 * @throws Exception 
	 * @see com.caiyang.api.reporttpl.IReportTplService#luckySheetExportExcel(com.caiyang.dto.reporttpl.MesGenerateReportDto, javax.servlet.http.HttpServletResponse) 
	 * @author caiyang
	 * @date 2022-02-15 07:33:40 
	 */
	@Override
	public void luckySheetExportExcel(MesGenerateReportDto mesGenerateReportDto,UserInfoDto userInfoDto,
			HttpServletResponse httpServletResponse) throws Exception {
		ReportTpl reportTpl = this.getById(mesGenerateReportDto.getTplId());
		if (reportTpl == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"报表模板"}));
		}
		ResPreviewData resPreviewData = null;
//		if(reportTpl.getTplType().intValue() == 1)
//		{
			resPreviewData = this.generateLuckySheetReportData(mesGenerateReportDto,mesGenerateReportDto.isPatination(),userInfoDto,reportTpl,true);
//		}else {
//			resPreviewData = this.iReportTplFormsService.previewLuckysheetReportFormsData(mesGenerateReportDto,userInfoDto,reportTpl,mesGenerateReportDto.isPatination());
//		}
		MesExportExcel mesExportExcel = new MesExportExcel();
		List<MesSheetConfig> sheetConfigs = new ArrayList<MesSheetConfig>();
		if(!ListUtil.isEmpty(resPreviewData.getSheetDatas()))
		{
			for (int i = 0; i < resPreviewData.getSheetDatas().size(); i++) {
				MesSheetConfig mesSheetConfig = new MesSheetConfig();
				Object borderInfos = null;
				Map<String, Object> rowlen = null;
				Map<String, Object> columnlen = null;
				JSONObject authority = null;
				if(resPreviewData.getSheetDatas().get(i).getConfig() != null)
				{
					borderInfos = resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.BORDERINFO.getCode());
					rowlen = (Map<String, Object>) resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.ROWLEN.getCode());//行高
					columnlen = (Map<String, Object>) resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.COLUMNLEN.getCode());//列宽
					authority = JSON.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getConfig().get("authority")));
				}
				List<Object> borderInfoList = null;
				if(borderInfos != null)
				{
					borderInfoList = (List<Object>) borderInfos;
				}
				mesSheetConfig.setCellDatas(resPreviewData.getSheetDatas().get(i).getCellDatas());
				mesSheetConfig.setSheetname(resPreviewData.getSheetDatas().get(i).getSheetName());
				mesSheetConfig.setBorderInfos(borderInfoList);
				mesSheetConfig.setRowlen(rowlen);
				mesSheetConfig.setColumnlen(columnlen);
				mesSheetConfig.setFrozen(JSONObject.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getFrozen())));
				mesSheetConfig.setImages(JSONObject.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getImages())));
				mesSheetConfig.setHyperlinks(resPreviewData.getSheetDatas().get(i).getHyperlinks());
				mesSheetConfig.setBase64Images(resPreviewData.getSheetDatas().get(i).getBase64Imgs());
				mesSheetConfig.setImageDatas(resPreviewData.getSheetDatas().get(i).getImageDatas());
				mesSheetConfig.setChart(resPreviewData.getSheetDatas().get(i).getChart());
				mesSheetConfig.setChartCells(resPreviewData.getSheetDatas().get(i).getChartCells());
				mesSheetConfig.setColhidden(resPreviewData.getSheetDatas().get(i).getColhidden());
				mesSheetConfig.setRowhidden(resPreviewData.getSheetDatas().get(i).getRowhidden());
				mesSheetConfig.setDataVerification(resPreviewData.getSheetDatas().get(i).getDataVerification());
				mesSheetConfig.setAuthority(authority);
				mesSheetConfig.setLuckysheetConditionformatSave(resPreviewData.getSheetDatas().get(i).getLuckysheetConditionformatSave());
				if(resPreviewData.getSheetDatas().get(i).getMaxXAndY() != null)
				{
					mesSheetConfig.setMaxXAndY(resPreviewData.getSheetDatas().get(i).getMaxXAndY());	
				}else {
					Map<String, Integer> maxXAndY = new HashMap<>();
					maxXAndY.put("maxX", 50);
					maxXAndY.put("maxY", 26);
					mesSheetConfig.setMaxXAndY(maxXAndY);	
				}
				sheetConfigs.add(mesSheetConfig);
			}
		}
		mesExportExcel.setPassword(mesGenerateReportDto.getPassword());
		mesExportExcel.setSheetConfigs(sheetConfigs);
		mesExportExcel.setChartsBase64(mesGenerateReportDto.getChartsBase64());
		if(mesGenerateReportDto.isLarge()) {
			ReportExcelUtil.largeExport(mesExportExcel,reportTpl.getTplName(),mesGenerateReportDto.getPassword(),httpServletResponse);
		}else {
			ReportExcelUtil.export(mesExportExcel,reportTpl.getTplName(),mesGenerateReportDto.getPassword(),httpServletResponse);
		}
	}


	/**  
	 * @MethodName: validateDesignPwd
	 * @Description: 校验设计密码
	 * @author caiyang
	 * @param reportTpl
	 * @see com.springreport.api.reporttpl.IReportTplService#validateDesignPwd(com.springreport.entity.reporttpl.ReportTpl)
	 * @date 2022-07-05 08:19:47 
	 */  
	@Override
	public ReportTpl validateDesignPwd(ReportTpl reportTpl) {
		ReportTpl originalData = this.getById(reportTpl.getId());
		if(originalData == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"模板"}));
		}
		if(!originalData.getDesignPwd().equals(Md5Util.generateMd5(reportTpl.getDesignPwd())))
		{
			throw new BizException(StatusCode.FAILURE, "密码输入错误！");
		}
		reportTpl.setDesignPwd(originalData.getDesignPwd());
		return reportTpl;
	}


	/**  
	 * @MethodName: changeDesignPwd
	 * @Description: 修改设计密码
	 * @author caiyang
	 * @param reportTpl
	 * @return
	 * @see com.springreport.api.reporttpl.IReportTplService#changeDesignPwd(com.springreport.entity.reporttpl.ReportTpl)
	 * @date 2022-07-05 05:51:12 
	 */  
	@Override
	public BaseEntity changeDesignPwd(MesChangePwd model) {
		ReportTpl reportTpl = this.getById(model.getId());
		if(reportTpl == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"模板"}));
		}
		if(reportTpl.getDesignPwd().equals(Md5Util.generateMd5(model.getOldPwd())))
		{
			ReportTpl update = new ReportTpl();
			update.setId(model.getId());
			if(StringUtil.isNullOrEmpty(model.getDesignPwd().trim()))
			{
				update.setDesignPwd("");
			}else {
				update.setDesignPwd(Md5Util.generateMd5(model.getDesignPwd()));	
			}
			this.updateById(update);
		}else {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.oldpwd"));
		}
		BaseEntity result = new BaseEntity();
		result.setStatusMsg("密码修改成功！");
		return result;
	}


	/**  
	 * @MethodName: getRoleReports
	 * @Description: 获取角色报表
	 * @author caiyang
	 * @param model
	 * @return
	 * @see com.springreport.api.reporttpl.IReportTplService#getRoleReports(com.springreport.entity.reporttpl.ReportTpl, com.springreport.base.UserInfoDto)
	 * @date 2022-07-06 08:29:07 
	 */  
	@Override
	public List<ReportTplTreeDto> getRoleReports(MesRoleReportDto model) {
		List<ReportTplTreeDto> result = new ArrayList<>();
		if(model.getIsAdmin().intValue() == YesNoEnum.YES.getCode().intValue())
		{//超级管理员，获取全部报表
			ReportTpl reportTpl = new ReportTpl();
			BeanUtils.copyProperties(model, reportTpl);
			result = this.getChildren(reportTpl);
		}else {
			//非超级管理员获取对应角色的报表
			List<ReportTplDto> list = this.baseMapper.getRoleReports(model);
			if(!ListUtil.isEmpty(list))
			{
				ReportTplTreeDto reportTplTreeDto = null;
				for (int i = 0; i < list.size(); i++) {
					reportTplTreeDto = new ReportTplTreeDto();
					BeanUtils.copyProperties(list.get(i), reportTplTreeDto);
					reportTplTreeDto.setIcon("iconfont icon-Excel");
					reportTplTreeDto.setType("2");
					reportTplTreeDto.setHasChildren(false);
					result.add(reportTplTreeDto);
				}
			}
		}
		return result;
	}


	/**  
	 * @MethodName: saveReportFormsTpl
	 * @Description: 保存填报模板
	 * @author caiyang
	 * @param mesLuckysheetsTplDto
	 * @throws JsonProcessingException 
	 * @throws SQLException 
	 * @see com.springreport.api.reporttpl.IReportTplService#saveReportFormsTpl(com.springreport.dto.reporttpl.MesLuckysheetsTplDto)
	 * @date 2022-11-16 01:40:09 
	 */  
	@Override
	@Transactional
	public BaseEntity saveReportFormsTpl(MesLuckysheetsTplDto mesLuckySheetsTplDto,UserInfoDto userInfoDto) throws JsonProcessingException, SQLException {
		SaveLuckySheetTplDto result = new SaveLuckySheetTplDto();
		ReportTpl reportTpl = new ReportTpl();
		reportTpl.setId(mesLuckySheetsTplDto.getTplId());
		reportTpl.setIsParamMerge(mesLuckySheetsTplDto.getIsParamMerge());
		this.updateById(reportTpl);
		reportTpl = this.getById(reportTpl.getId());
		ObjectMapper objectMapper = new ObjectMapper();
		//删除sheets
		if(!ListUtil.isEmpty(mesLuckySheetsTplDto.getDelSheetsIndex()))
		{
			UpdateWrapper<ReportTplSheet> sheetUpdateWrapper = new UpdateWrapper<>();
			sheetUpdateWrapper.eq("tpl_id", mesLuckySheetsTplDto.getTplId());
			sheetUpdateWrapper.in("sheet_index", mesLuckySheetsTplDto.getDelSheetsIndex());
			sheetUpdateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			ReportTplSheet updateSheet = new ReportTplSheet();
			updateSheet.setDelFlag(DelFlagEnum.DEL.getCode());
			this.iReportTplSheetService.update(updateSheet, sheetUpdateWrapper);
			for (int i = 0; i < mesLuckySheetsTplDto.getDelSheetsIndex().size(); i++) {
				redisUtil.del(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+"designMode-"+mesLuckySheetsTplDto.getTplId()+"-"+mesLuckySheetsTplDto.getDelSheetsIndex().get(i));
			}
		}
		//获取模板数据集对应的列,用于判断单元格的类型，若columnNames中存在该值，则说明是动态数据，否则是固定值
		Map<String, String> datasetNameIdMap = new HashMap<>();
		List<List<String>> columnNames = this.getTplDatasetsColumnNames(mesLuckySheetsTplDto.getTplId(),datasetNameIdMap,userInfoDto);
		Map<String, Long> printSettings = new HashMap<>();
		for (int i = 0; i < mesLuckySheetsTplDto.getConfigs().size(); i++) {
			MesLuckySheetTplDto mesLuckySheetTplDto = mesLuckySheetsTplDto.getConfigs().get(i);
			//根据index查询sheet是否已经存在
			QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
			sheetQueryWrapper.eq("tpl_id", mesLuckySheetsTplDto.getTplId());
			sheetQueryWrapper.eq("sheet_index", mesLuckySheetTplDto.getSheetIndex());
			sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			ReportTplSheet isExistSheet = this.iReportTplSheetService.getOne(sheetQueryWrapper,false);
			ReportTplSheet reportTplSheet = new ReportTplSheet();
			if(isExistSheet != null)
			{
				reportTplSheet.setId(isExistSheet.getId());
			}
			reportTplSheet.setTplId(mesLuckySheetsTplDto.getTplId());
			reportTplSheet.setSheetIndex(mesLuckySheetTplDto.getSheetIndex());
			reportTplSheet.setSheetName(mesLuckySheetTplDto.getSheetName());
			reportTplSheet.setSheetOrder(mesLuckySheetTplDto.getSheetOrder());
			if(mesLuckySheetTplDto.getConfig() != null)
			{
				reportTplSheet.setConfig(objectMapper.writeValueAsString(mesLuckySheetTplDto.getConfig()));
			}
			if(mesLuckySheetTplDto.getFrozen() != null)
			{
				reportTplSheet.setFrozen(objectMapper.writeValueAsString(mesLuckySheetTplDto.getFrozen()));
			}
			if(mesLuckySheetTplDto.getImages() != null)
			{
				reportTplSheet.setImages(objectMapper.writeValueAsString(mesLuckySheetTplDto.getImages()));
			}
			if(mesLuckySheetTplDto.getCalcChain() != null)
			{
				reportTplSheet.setCalcChain(objectMapper.writeValueAsString(mesLuckySheetTplDto.getCalcChain()));
			}
			if(ListUtil.isNotEmpty(mesLuckySheetTplDto.getPageDivider()))
			{
				reportTplSheet.setPageDivider(JSON.toJSONString(mesLuckySheetTplDto.getPageDivider()));
			}else {
				reportTplSheet.setPageDivider(JSON.toJSONString(new JSONArray()));
			}
			if(mesLuckySheetTplDto.getLuckysheetConditionformatSave() == null)
			{
				mesLuckySheetTplDto.setLuckysheetConditionformatSave(new JSONArray());
			}
			reportTplSheet.setConditionformatSave(objectMapper.writeValueAsString(mesLuckySheetTplDto.getLuckysheetConditionformatSave()));
			this.iReportTplSheetService.saveOrUpdate(reportTplSheet);
			QueryWrapper<LuckysheetReportFormsCell> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("tpl_id", mesLuckySheetsTplDto.getTplId());
			queryWrapper.eq("sheet_id", reportTplSheet.getId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<LuckysheetReportFormsCell> tplCells = this.iLuckysheetReportFormsCellService.list(queryWrapper);
			List<LuckysheetReportFormsCell> addCells = new ArrayList<>();//新增的单元格
			List<LuckysheetReportFormsCell> deleteCells = new ArrayList<>();//需要删除的单元格
			List<LuckysheetReportFormsCell> updateCells = new ArrayList<>();//需要更新的单元格
			Map<String, JSONObject> extraCustomCellConfigs = mesLuckySheetTplDto.getExtraCustomCellConfigs()!=null?mesLuckySheetTplDto.getExtraCustomCellConfigs():new HashMap<String, JSONObject>();
			List<LuckysheetReportFormsCell> sheetCells = new ArrayList<>();//sheet页中所有需要新增或者更新的单元格数据
			if(ListUtil.isEmpty(tplCells))
			{//如果是空，则全部新增
				if(!ListUtil.isEmpty(mesLuckySheetTplDto.getCellDatas()))
				{
					for (Map<String, Object> object : mesLuckySheetTplDto.getCellDatas()) {
						LuckysheetReportFormsCell luckysheetReportFormsCell = this.getReportFormsCell(object, columnNames, extraCustomCellConfigs);
						luckysheetReportFormsCell.setTplId(mesLuckySheetsTplDto.getTplId());
						luckysheetReportFormsCell.setSheetId(reportTplSheet.getId());
						addCells.add(luckysheetReportFormsCell);
					}
				}
			}else {
				//如果非空，则找出需要更新，删除和新增的单元格
				Map<String, LuckysheetReportFormsCell> tplCellsMap = new HashMap<>();
				for (int j = 0; j < tplCells.size(); j++) {
					String key = tplCells.get(j).getCoordsx().toString() + tplCells.get(j).getCoordsy().toString();
					if(!tplCellsMap.containsKey(key))
					{
						tplCellsMap.put(key, tplCells.get(j));
					}else {
						tplCells.get(j).setDelFlag(DelFlagEnum.DEL.getCode());
						deleteCells.add(tplCells.get(j));
					}
				}
				Map<String, List<Map<String, Object>>> cellDatasMap = mesLuckySheetTplDto.getCellDatas().stream().collect(Collectors.groupingBy(this::customKey));
				//找出需要删除的单元格
				if(ListUtil.isEmpty(mesLuckySheetTplDto.getCellDatas()))
				{
					for (LuckysheetReportFormsCell luckysheetReportFormsCell : tplCells) {
						luckysheetReportFormsCell.setDelFlag(DelFlagEnum.DEL.getCode());
						deleteCells.add(luckysheetReportFormsCell);
					}
				}else {
					for (LuckysheetReportFormsCell luckysheetReportFormsCell : tplCells) {
						boolean isExist = cellDatasMap.containsKey(String.valueOf(luckysheetReportFormsCell.getCoordsx() + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() +String.valueOf(luckysheetReportFormsCell.getCoordsy())));
						if(!isExist)
						{
							luckysheetReportFormsCell.setDelFlag(DelFlagEnum.DEL.getCode());
							deleteCells.add(luckysheetReportFormsCell);
						}
					}
				}
				//找出需要新增和更新的单元格
				if(!ListUtil.isEmpty(mesLuckySheetTplDto.getCellDatas()))
				{
					for (Map<String, Object> object : mesLuckySheetTplDto.getCellDatas()) {
						String key = String.valueOf(object.get(LuckySheetPropsEnum.R.getCode()))+ String.valueOf(object.get(LuckySheetPropsEnum.C.getCode()));
						boolean isExist = tplCellsMap.containsKey(key);
						LuckysheetReportFormsCell luckysheetReportFormsCell = this.getReportFormsCell(object, columnNames, extraCustomCellConfigs);
						luckysheetReportFormsCell.setTplId(mesLuckySheetsTplDto.getTplId());
						luckysheetReportFormsCell.setSheetId(reportTplSheet.getId());
						if(isExist)
						{
							luckysheetReportFormsCell.setId(tplCellsMap.get(key).getId());
							updateCells.add(luckysheetReportFormsCell);
						}else {
							addCells.add(luckysheetReportFormsCell);
						}
					}
				}
			}
			sheetCells.addAll(addCells);
			sheetCells.addAll(updateCells);
			Map<String, List<LuckysheetReportFormsCell>> formsCellsMap = sheetCells.stream().collect(Collectors.groupingBy(this::customKey));
			//删掉绑定的填报数据源信息
			UpdateWrapper<ReportFormsDatasource> formsDatasourceUpdateWrapper = new UpdateWrapper<>();
			formsDatasourceUpdateWrapper.eq("tpl_id", mesLuckySheetsTplDto.getTplId());
			formsDatasourceUpdateWrapper.eq("sheet_id", reportTplSheet.getId());
			formsDatasourceUpdateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			ReportFormsDatasource updateFormsDatasource = new ReportFormsDatasource();
			updateFormsDatasource.setDelFlag(DelFlagEnum.DEL.getCode());
			this.iReportFormsDatasourceService.update(updateFormsDatasource, formsDatasourceUpdateWrapper);
			//新增填报绑定数据源和数据源属性
			JSONArray datasourceConfig = mesLuckySheetTplDto.getDatasourceConfig();
			List<ReportFormsDatasource> formsDatasources = new ArrayList<>();
			List<ReportFormsDatasourceAttrs> formsDatasourceAttrs = new ArrayList<>();
			if(!ListUtil.isEmpty(datasourceConfig))
			{
				for (int k = 0; k < datasourceConfig.size(); k++) {
					JSONObject config = datasourceConfig.getJSONObject(k);
					ReportFormsDatasource reportFormsDatasource = new ReportFormsDatasource();
					reportFormsDatasource.setId(IdWorker.getId());
					reportFormsDatasource.setName(config.getString("name"));
					reportFormsDatasource.setDatasourceId(config.getLong("datasourceId"));
					reportFormsDatasource.setTplId(mesLuckySheetsTplDto.getTplId());
					reportFormsDatasource.setSheetId(reportTplSheet.getId());
					reportFormsDatasource.setTableName(config.getString("table"));
					formsDatasources.add(reportFormsDatasource);
					JSONArray tableDatas = config.getJSONArray("tableDatas");
					Integer cellExtend = null;//扩展方向
					String aggregateType = null;//聚合方式
					if(!ListUtil.isEmpty(tableDatas))
					{
						for (int j = 0; j < tableDatas.size(); j++) {
							JSONObject tableData = tableDatas.getJSONObject(j);
							ReportFormsDatasourceAttrs reportFormsDatasourceAttrs = new ReportFormsDatasourceAttrs();
							reportFormsDatasourceAttrs.setReportFormsDatasourceId(reportFormsDatasource.getId());
							reportFormsDatasourceAttrs.setType(1);
							reportFormsDatasourceAttrs.setColumnName(tableData.getString("columnName"));
							String r = StringUtil.getNumeric(tableData.getString("cellCoords"));
							if(StringUtil.isNotEmpty(r))
							{
								reportFormsDatasourceAttrs.setCoordsx(Integer.parseInt(r)-1);
								String col = tableData.getString("cellCoords").replace(r, "");
								if(StringUtil.isNotEmpty(col))
								{
									int c = SheetUtil.excelColStrToNum(col)-1;
									reportFormsDatasourceAttrs.setCoordsy(c);
									reportFormsDatasourceAttrs.setCellCoords(tableData.getString("cellCoords"));
									formsDatasourceAttrs.add(reportFormsDatasourceAttrs);
									String key = String.valueOf(Integer.parseInt(r)-1)+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() + c;
									List<LuckysheetReportFormsCell> cells = formsCellsMap.get(key);
									if(!ListUtil.isEmpty(cells))
									{
										if(j == 0)
										{
											cellExtend = cells.get(0).getCellExtend();
											if(StringUtil.isNotEmpty(cells.get(0).getCellAttrs()))
											{
												JSONObject cellAttrs = JSONObject.parseObject(cells.get(0).getCellAttrs());
												aggregateType = StringUtil.isNullOrEmpty(cellAttrs.getString("aggregateType"))?"list":cellAttrs.getString("aggregateType");
											}else {
												aggregateType = "list";
											}
										}else {
											String cellAggregate = null;
											if(StringUtil.isNotEmpty(cells.get(0).getCellAttrs()))
											{
												JSONObject cellAttrs = JSONObject.parseObject(cells.get(0).getCellAttrs());
												cellAggregate = StringUtil.isNullOrEmpty(cellAttrs.getString("aggregateType"))?"list":cellAttrs.getString("aggregateType");
											}else {
												cellAggregate = "list";
											}
											if(cellExtend.intValue() != cells.get(0).getCellExtend().intValue())
											{
												throw new BizException(StatusCode.FAILURE,"填报属性【"+ config.getString("name") +"】绑定的单元格扩展方向属性必须一致。");
											}
											if(!aggregateType.equals(cellAggregate)) {
												throw new BizException(StatusCode.FAILURE,"填报属性【"+ config.getString("name") +"】绑定的单元格聚合方式属性必须一致。");
											}
										}
									}else {
										cellExtend = 1;
										aggregateType = "list";
										//不存在，将数据添加到添加单元格中
										LuckysheetReportFormsCell addCell = new LuckysheetReportFormsCell();
										addCell.setTplId(mesLuckySheetsTplDto.getTplId());
										addCell.setSheetId(reportTplSheet.getId());
										addCell.setCoordsx(Integer.valueOf(r)-1);
										addCell.setCoordsy(c);
										addCell.setCellExtend(1);
										addCell.setCellData("{\"r\":"+addCell.getCoordsx()+",\"c\":"+addCell.getCoordsy()+",\"v\":{\"ct\":{\"t\":\"g\",\"fa\":\"General\"},\"v\":\"\",\"m\":\"\"}}");
										addCell.setCellValueType(1);
										if(mesLuckySheetTplDto.getExtraCustomCellConfigs() != null && mesLuckySheetTplDto.getExtraCustomCellConfigs().get(key) != null)
										{
											addCell.setCellAttrs(objectMapper.writeValueAsString(mesLuckySheetTplDto.getExtraCustomCellConfigs().get(key)));
										}else {
											addCell.setCellAttrs(Constants.DEFALUT_CELL_ATTRS);
										}
										addCells.add(addCell);
										sheetCells.add(addCell);
									}
								}
							}
						}
					}
					JSONArray keys = config.getJSONArray("keys");
					if(!ListUtil.isEmpty(keys)) {
						for (int j = 0; j < keys.size(); j++) {
							JSONObject key = keys.getJSONObject(j);
							ReportFormsDatasourceAttrs reportFormsDatasourceAttrs = new ReportFormsDatasourceAttrs();
							reportFormsDatasourceAttrs.setReportFormsDatasourceId(reportFormsDatasource.getId());
							reportFormsDatasourceAttrs.setType(2);
							reportFormsDatasourceAttrs.setColumnName(key.getString("columnName"));
							reportFormsDatasourceAttrs.setIdType(key.getInteger("idType"));
							formsDatasourceAttrs.add(reportFormsDatasourceAttrs);
						}
					}
				}
			}
			//模板权限处理，只有模板创建者才能操作
			List<ReportRangeAuth> rangeAuths = new ArrayList<>();
			List<ReportRangeAuthUser> rangeAuthUsers = new ArrayList<>();
			if(reportTpl.getCreator().longValue() == userInfoDto.getUserId().longValue())
			{
				//先删除再新增
				QueryWrapper<ReportRangeAuth> rangeAuthQueryWrapper = new QueryWrapper<>();
				rangeAuthQueryWrapper.eq("tpl_id", reportTpl.getId());
 				rangeAuthQueryWrapper.eq("sheet_id", reportTplSheet.getId());
  				this.iReportRangeAuthService.remove(rangeAuthQueryWrapper);
				QueryWrapper<ReportRangeAuthUser> rangeAuthUserQueryWrapper = new QueryWrapper<>();
				rangeAuthUserQueryWrapper.eq("tpl_id", reportTpl.getId());
				rangeAuthUserQueryWrapper.eq("sheet_id", reportTplSheet.getId());
				this.iReportRangeAuthUserService.remove(rangeAuthUserQueryWrapper);
				if(mesLuckySheetTplDto.getSheetRangeAuth() != null && !StringUtil.isEmptyMap(mesLuckySheetTplDto.getSheetRangeAuth()))
				{
					ReportRangeAuth reportRangeAuth = null;
					ReportRangeAuthUser reportRangeAuthUser = null;
					for(String mapKey : mesLuckySheetTplDto.getSheetRangeAuth().keySet()){
						reportRangeAuth = new ReportRangeAuth();
						reportRangeAuth.setId(IdWorker.getId());
						reportRangeAuth.setTplId(reportTpl.getId());
						reportRangeAuth.setSheetId(reportTplSheet.getId());
						reportRangeAuth.setSheetIndex(reportTplSheet.getSheetIndex());
						reportRangeAuth.setRangeTxt(mapKey);
						JSONObject range = mesLuckySheetTplDto.getSheetRangeAuth().getJSONObject(mapKey).getJSONObject("range");
						reportRangeAuth.setRowsNo(JSON.toJSONString(range.getJSONArray("row")));
						reportRangeAuth.setColsNo(JSON.toJSONString(range.getJSONArray("column")));
						rangeAuths.add(reportRangeAuth);
						JSONArray userIds = mesLuckySheetTplDto.getSheetRangeAuth().getJSONObject(mapKey).getJSONArray("userIds");
						for (int j = 0; j < userIds.size(); j++) {
							reportRangeAuthUser = new ReportRangeAuthUser();
							reportRangeAuthUser.setTplId(reportTpl.getId());
							reportRangeAuthUser.setSheetId(reportTplSheet.getId());
							reportRangeAuthUser.setRangeAuthId(reportRangeAuth.getId());
							reportRangeAuthUser.setUserId(userIds.getLong(j));
							rangeAuthUsers.add(reportRangeAuthUser);
						}
					}
				}
			}
			if(!ListUtil.isEmpty(addCells))
			{
				this.iLuckysheetReportFormsCellService.saveBatch(addCells);
			}
			if(!ListUtil.isEmpty(updateCells))
			{
				this.iLuckysheetReportFormsCellService.updateBatchById(updateCells);
			}
			if(!ListUtil.isEmpty(deleteCells))
			{
				this.iLuckysheetReportFormsCellService.updateBatchById(deleteCells);
			}
			if(!ListUtil.isEmpty(formsDatasources))
			{
				this.iReportFormsDatasourceService.saveBatch(formsDatasources);
			}
			if(!ListUtil.isEmpty(formsDatasourceAttrs))
			{
				this.iReportFormsDatasourceAttrsService.saveBatch(formsDatasourceAttrs);
			}
			if(mesLuckySheetsTplDto.getConfigs().get(i).getPrintSettings() != null)
			{
				mesLuckySheetsTplDto.getConfigs().get(i).getPrintSettings().setTplId(mesLuckySheetsTplDto.getTplId());
				mesLuckySheetsTplDto.getConfigs().get(i).getPrintSettings().setTplSheetId(reportTplSheet.getId());
				this.iReportSheetPdfPrintSettingService.saveOrUpdate(mesLuckySheetsTplDto.getConfigs().get(i).getPrintSettings());
				printSettings.put(mesLuckySheetTplDto.getSheetIndex(), mesLuckySheetsTplDto.getConfigs().get(i).getPrintSettings().getId());
			}
			if(ListUtil.isNotEmpty(rangeAuths)) {
				this.iReportRangeAuthService.saveBatch(rangeAuths);
			}
			if(ListUtil.isNotEmpty(rangeAuthUsers)){
				this.iReportRangeAuthUserService.saveBatch(rangeAuthUsers);
			}
		}
		result.setPrintSettings(printSettings);
		result.setStatusMsg(MessageUtil.getValue("info.save"));
		return result;
	}

	private LuckysheetReportFormsCell getReportFormsCell(Map<String, Object> object,List<List<String>> columnNames,Map<String, JSONObject> extraCustomCellConfigs) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		LuckysheetReportFormsCell reportFormsCell = new LuckysheetReportFormsCell();
		reportFormsCell.setCoordsx((Integer) object.get(LuckySheetPropsEnum.R.getCode()));
		reportFormsCell.setCoordsy((Integer) object.get(LuckySheetPropsEnum.C.getCode()));
		reportFormsCell.setCellData(objectMapper.writeValueAsString(object));
		Map<String, Object> cellValueMap = (Map<String, Object>) object.get(LuckySheetPropsEnum.CELLCONFIG.getCode());
		Map<String, Object> cellType = (Map<String, Object>) cellValueMap.get(LuckySheetPropsEnum.CELLTYPE.getCode());
		String cellValue = "";
		if(cellType != null) {
			String t = String.valueOf(cellType.get(LuckySheetPropsEnum.TYPE.getCode()));
			String f = String.valueOf(cellValueMap.get(LuckySheetPropsEnum.FUNCTION.getCode()) == null ? "" : cellValueMap.get(LuckySheetPropsEnum.FUNCTION.getCode()));
			if(StringUtil.isNotEmpty(f))
			{
				reportFormsCell.setCellValue(f);
				cellValue = f;
				reportFormsCell.setIsFunction(YesNoEnum.YES.getCode());;
			}else {
				reportFormsCell.setIsFunction(YesNoEnum.NO.getCode());
				if(LuckySheetPropsEnum.INLINESTR.getCode().equals(t))
				{
					List<Map<String, Object>> list = (List<Map<String, Object>>) cellType.get(LuckySheetPropsEnum.STRING.getCode());
					cellValue = String.valueOf(list.get(0).get(LuckySheetPropsEnum.CELLVALUE.getCode()));
					reportFormsCell.setCellValue(cellValue);
				}else {
					cellValue = String.valueOf(cellValueMap.get(LuckySheetPropsEnum.CELLVALUE.getCode()));
					reportFormsCell.setCellValue(cellValue);
				}
			}
		}else {
			reportFormsCell.setIsFunction(YesNoEnum.NO.getCode());
			reportFormsCell.setCellValue(cellValue);
		}
		Map<String, Object> configMap = (Map<String, Object>) object.get(LuckySheetPropsEnum.CELLCONFIG.getCode());
		Map<String, Object> mergeConfig = (Map<String, Object>) configMap.get(LuckySheetPropsEnum.MERGECELLS.getCode());
		if(mergeConfig == null) {
			reportFormsCell.setIsMerge(YesNoEnum.NO.getCode());
			reportFormsCell.setRowSpan(1);
			reportFormsCell.setColSpan(1);
		}else {
			reportFormsCell.setIsMerge(YesNoEnum.YES.getCode());
			reportFormsCell.setRowSpan((Integer) mergeConfig.get(LuckySheetPropsEnum.ROWSPAN.getCode()));
			reportFormsCell.setColSpan((Integer) mergeConfig.get(LuckySheetPropsEnum.COLSPAN.getCode()));
		}
		Pattern paramPattern=Pattern.compile("\\$\\s*\\{(.*?)}");
		Matcher parammatcher=paramPattern.matcher(cellValue);
		boolean matchFlag = false;
		while(parammatcher.find()){
			matchFlag = true;
			break;
		}
		if(!ListUtil.isEmpty(columnNames) && matchFlag)
		{
			reportFormsCell.setCellValueType(CellValueTypeEnum.VARIABLE.getCode());
			reportFormsCell.setDatasetName(ListUtil.getFormulaDatasetName(cellValue, columnNames));
		}else {
			reportFormsCell.setCellValueType(CellValueTypeEnum.FIXED.getCode());
		}
		String mapKey = object.get(LuckySheetPropsEnum.R.getCode())+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+object.get(LuckySheetPropsEnum.C.getCode());
		JSONObject extraCustomCellConfig = extraCustomCellConfigs.get(mapKey);
		if(extraCustomCellConfig != null) {
			reportFormsCell.setCellAttrs(JSONObject.toJSONString(extraCustomCellConfig));
			reportFormsCell.setCellExtend(extraCustomCellConfig.getInteger("cellExtend"));		
		}else {
			reportFormsCell.setCellAttrs(Constants.DEFALUT_CELL_ATTRS);
			reportFormsCell.setCellExtend(1);
		}
		return reportFormsCell;
	}


	/**  
	 * @MethodName: getReportFormsTpl
	 * @Description: 获取填报模板
	 * @author caiyang
	 * @param reportTpl
	 * @return
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 * @see com.springreport.api.reporttpl.IReportTplService#getReportFormsTpl(com.springreport.entity.reporttpl.ReportTpl)
	 * @date 2022-11-17 08:31:59 
	 */  
	@Override
	public ResSheetsSettingsDto getReportFormsTpl(ReportTpl reportTpl,UserInfoDto userInfoDto) throws JsonMappingException, JsonProcessingException {
   		ResSheetsSettingsDto settings = new ResSheetsSettingsDto();
		List<ResLuckySheetTplSettingsDto> list = new ArrayList<>();
		reportTpl = this.getById(reportTpl.getId());
		if(reportTpl == null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"报表模板"}));
		}
		Object settingRedisCache = redisUtil.get(RedisPrefixEnum.REPORTTPLTEMPCACHE.getCode()+"designMode-"+reportTpl.getId());
		if(settingRedisCache != null)
		{
			settings = JSON.parseObject(settingRedisCache.toString(), ResSheetsSettingsDto.class);
		}else {
			settings.setIsParamMerge(reportTpl.getIsParamMerge());
			settings.setTplName(reportTpl.getTplName());
			redisUtil.set(RedisPrefixEnum.REPORTTPLTEMPCACHE.getCode()+"designMode-"+reportTpl.getId(),JSON.toJSONString(settings),Constants.REPORT_TPL_CACHE_TIME);
		}
		//获取所有有权限的sheet
		List<ReportTplSheet> sheets = null;
//		if(userInfoDto.getIsAdmin().intValue() == YesNoEnum.YES.getCode() || reportTpl.getViewAuth().intValue() == 1)
//		{//超级管理员
//			QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
//			sheetQueryWrapper.eq("tpl_id", reportTpl.getId());
//			sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//			sheets = this.iReportTplSheetService.list(sheetQueryWrapper);
//		}else {
//			QueryWrapper<SysRoleSheet> roleSheetQueryWrapper = new QueryWrapper<>();
//			roleSheetQueryWrapper.eq("report_id", reportTpl.getId());
//			roleSheetQueryWrapper.eq("role_id", userInfoDto.getRoleId());
//			roleSheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//			List<SysRoleSheet> roleSheets = this.iSysRoleSheetService.list(roleSheetQueryWrapper);
//			if(ListUtil.isEmpty(roleSheets))
//			{
//				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.noauth.design.report"));
//			}
//			List<Long> sheetIds = new ArrayList<>();
//			for (int i = 0; i < roleSheets.size(); i++) {
//				sheetIds.add(roleSheets.get(i).getSheetId());
//			}
//			QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
//			sheetQueryWrapper.in("id", sheetIds);
//			sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//			sheets = this.iReportTplSheetService.list(sheetQueryWrapper);
//		}
		QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
		sheetQueryWrapper.eq("tpl_id", reportTpl.getId());
		sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		sheets = this.iReportTplSheetService.list(sheetQueryWrapper);
		if(ListUtil.isEmpty(sheets))
		{
			ReportTplSheet reportTplSheet = new ReportTplSheet();
			reportTplSheet.setTplId(reportTpl.getId());
			reportTplSheet.setSheetName("sheet1");
			reportTplSheet.setConfig(reportTpl.getConfig());
			reportTplSheet.setFrozen(reportTpl.getFrozen());
			reportTplSheet.setImages(reportTpl.getImages());
			reportTplSheet.setSheetIndex("Sheet"+UUIDUtil.getUUID());
			reportTplSheet.setCalcChain(reportTpl.getCalcChain());
			reportTplSheet.setSheetOrder(0);
			this.iReportTplSheetService.save(reportTplSheet);
			sheets = new ArrayList<ReportTplSheet>();
			sheets.add(reportTplSheet);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> databaseSheets = new ArrayList<>();//数据库中已经保存的sheet
		boolean isCreator = false;
		if(this.authenticEnabale) {
			if(StringUtil.isNotEmpty(userInfoDto.getUserName()) && userInfoDto.getUserName().equals(this.thirdPartyType)) {
				isCreator = true;
			}else {
				isCreator = reportTpl.getCreator().longValue() == userInfoDto.getUserId().longValue();
			}
		}else {
			isCreator = true;
		}
		for (int t = 0; t < sheets.size(); t++) {
			ResLuckySheetTplSettingsDto result = new ResLuckySheetTplSettingsDto();
			String key = RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+"designMode-" +reportTpl.getId()+"-"+ sheets.get(t).getSheetIndex();
			databaseSheets.add(key);
			Object redisCache = redisUtil.get(key);
			if(redisCache == null) {
				QueryWrapper<LuckysheetReportFormsCell> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("tpl_id", reportTpl.getId());
				queryWrapper.eq("sheet_id", sheets.get(t).getId());
				queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<LuckysheetReportFormsCell> luckysheetReportFormsCells = this.iLuckysheetReportFormsCellService.list(queryWrapper);
				if(!ListUtil.isEmpty(luckysheetReportFormsCells))
				{
					Map<String, Object> cellFormats = new HashMap<>();
					List<Map<String, Object>> cellDatas = new ArrayList<Map<String, Object>>();
					Map<String, JSONObject> extraCustomCellConfigs = new HashMap<String, JSONObject>();
					for (LuckysheetReportFormsCell LuckysheetReportFormsCell : luckysheetReportFormsCells) {
						JSONObject cellData = objectMapper.readValue(LuckysheetReportFormsCell.getCellData(), JSONObject.class);
						cellDatas.add(cellData);
						JSONObject cellFormat = this.getCellFormat(cellData);
						cellFormats.put(cellData.getInteger("r")+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+cellData.getInteger("c"), cellFormat);
						JSONObject extraCustomCellConfig = JSONObject.parseObject(LuckysheetReportFormsCell.getCellAttrs());
						Boolean allowEdit = extraCustomCellConfig.getBoolean("allowEdit");
						if(allowEdit == null)
						{
							extraCustomCellConfig.put("allowEdit", true);
						}
						if(extraCustomCellConfig.getBooleanValue("isDrill"))
						{
							ReportTpl drillTpl = this.getById(extraCustomCellConfig.getLong("drillId"));
							if(drillTpl != null)
							{
								extraCustomCellConfig.put("drillTplName", drillTpl.getTplName());
							}
						}
						extraCustomCellConfigs.put(String.valueOf(LuckysheetReportFormsCell.getCoordsx())+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()
						+String.valueOf(LuckysheetReportFormsCell.getCoordsy()), extraCustomCellConfig);
					}
					result.setCellDatas(cellDatas);
					result.setExtraCustomCellConfigs(extraCustomCellConfigs);
					result.setCellFormats(cellFormats);
				}
				QueryWrapper<ReportFormsDatasource> datasourceQueryWrapper = new QueryWrapper<>();
				datasourceQueryWrapper.eq("tpl_id", reportTpl.getId());
				datasourceQueryWrapper.eq("sheet_id", sheets.get(t).getId());
				datasourceQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<ReportFormsDatasource> reportFormsDatasources = this.iReportFormsDatasourceService.list(datasourceQueryWrapper);
				JSONArray datasourceConfigs = new JSONArray();
				if(!ListUtil.isEmpty(reportFormsDatasources))
				{
					for (int i = 0; i < reportFormsDatasources.size(); i++) {
						JSONObject datasourceConfig = new JSONObject();
						QueryWrapper<ReportFormsDatasourceAttrs> attrsQueryWrapper = new QueryWrapper<>();
						attrsQueryWrapper.eq("report_forms_datasource_id", reportFormsDatasources.get(i).getId());
						attrsQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
						List<ReportFormsDatasourceAttrs> formsDatasourceAttrs = this.iReportFormsDatasourceAttrsService.list(attrsQueryWrapper);
						datasourceConfig.put("name", reportFormsDatasources.get(i).getName());
						datasourceConfig.put("datasourceId", String.valueOf(reportFormsDatasources.get(i).getDatasourceId()));
						datasourceConfig.put("table", reportFormsDatasources.get(i).getTableName());
						JSONArray tableDatas = new JSONArray();
						JSONArray keys = new JSONArray();
						if(!ListUtil.isEmpty(formsDatasourceAttrs))
						{
							for (int j = 0; j < formsDatasourceAttrs.size();j++) {
								JSONObject jsonObject = new JSONObject();
								if(formsDatasourceAttrs.get(j).getType().intValue() == 1) {
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("cellCoords", formsDatasourceAttrs.get(j).getCellCoords());
									tableDatas.add(jsonObject);
								}else {
									jsonObject.put("columnName", formsDatasourceAttrs.get(j).getColumnName());
									jsonObject.put("idType", formsDatasourceAttrs.get(j).getIdType());
									keys.add(jsonObject);
								}
							}
						}
						datasourceConfig.put("tableDatas", tableDatas);
						datasourceConfig.put("keys", keys);
						datasourceConfigs.add(datasourceConfig);
					}
				}
				result.setDatasourceConfig(datasourceConfigs);
				if(StringUtil.isNotEmpty(sheets.get(t).getConfig()))
				{
					result.setConfig(objectMapper.readValue(sheets.get(t).getConfig(), Map.class));
				}else {
					result.setConfig(new HashMap<String, Object>());
				}
				if(StringUtil.isNotEmpty(sheets.get(t).getFrozen()))
				{
					result.setFrozen(objectMapper.readValue(sheets.get(t).getFrozen(), Map.class));
				}else {
					result.setFrozen(new HashMap<String, Object>());
				}
				if(StringUtil.isNullOrEmpty(sheets.get(t).getSheetIndex()))
				{
					result.setSheetIndex("Sheet"+UUIDUtil.getUUID());
				}else {
					result.setSheetIndex(sheets.get(t).getSheetIndex());
				}
				if(StringUtil.isNotEmpty(sheets.get(t).getImages()))
				{
					result.setImages(objectMapper.readValue(sheets.get(t).getImages(), Map.class));
				}else {
					result.setImages(new HashMap<String, Object>());
				}
				if(StringUtil.isNotEmpty(sheets.get(t).getCalcChain()))
				{
					result.setCalcChain(JSON.parseArray(sheets.get(t).getCalcChain()));
				}
				result.setSheetName(sheets.get(t).getSheetName());
				result.setSheetOrder(sheets.get(t).getSheetOrder());
				if(StringUtil.isNullOrEmpty(sheets.get(t).getPageDivider()))
				{
					result.setPageDivider(new JSONArray());
				}else {
					result.setPageDivider(JSON.parseArray(sheets.get(t).getPageDivider()));
				}
				if(StringUtil.isNullOrEmpty(sheets.get(t).getConditionformatSave()))
				{
					result.setLuckysheetConditionformatSave(new JSONArray());
				}else {
					result.setLuckysheetConditionformatSave(JSONArray.parseArray(sheets.get(t).getConditionformatSave()));
				}
				//获取打印设置
				QueryWrapper<ReportSheetPdfPrintSetting> settingQueryWrapper = new QueryWrapper<>();
				settingQueryWrapper.eq("tpl_sheet_id", sheets.get(t).getId());
				settingQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				ReportSheetPdfPrintSetting setting = this.iReportSheetPdfPrintSettingService.getOne(settingQueryWrapper);
				result.setReportSheetPdfPrintSetting(setting);
				redisUtil.set(key, JSON.toJSONString(result),Constants.REPORT_TPL_CACHE_TIME);
			}else {
				result = JSON.parseObject(redisCache.toString(), ResLuckySheetTplSettingsDto.class);
			}
			
			list.add(result);
		}
		//获取权限信息
		JSONObject sheetRangeAuth = new JSONObject();
		if(isCreator) {
			QueryWrapper<ReportRangeAuth> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("tpl_id", sheets.get(0).getTplId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<ReportRangeAuth> reportRangeAuths = this.iReportRangeAuthService.list(queryWrapper);
			if(ListUtil.isNotEmpty(reportRangeAuths))
			{
				List<Long> authIds = new ArrayList<>();
				for (int i = 0; i < reportRangeAuths.size(); i++) {
					authIds.add(reportRangeAuths.get(i).getId());
				}
				//获取权限对应的用户信息
				QueryWrapper<ReportRangeAuthUser> rangeAuthUserQueryWrapper = new QueryWrapper<>();
				rangeAuthUserQueryWrapper.eq("tpl_id", sheets.get(0).getTplId());
				rangeAuthUserQueryWrapper.in("range_auth_id", authIds);
				rangeAuthUserQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<ReportRangeAuthUser> reportRangeAuthUsers = this.iReportRangeAuthUserService.list(rangeAuthUserQueryWrapper);
				Map<Long, List<ReportRangeAuthUser>> groupDatas = null;
				if(ListUtil.isNotEmpty(reportRangeAuthUsers))
				{
					groupDatas = reportRangeAuthUsers.stream().collect(Collectors.groupingBy(ReportRangeAuthUser::getRangeAuthId));
				}
				for (int i = 0; i < reportRangeAuths.size(); i++) {
					JSONObject rangeAxis = new JSONObject();
					rangeAxis.put("rangeAxis", reportRangeAuths.get(i).getRangeTxt());
					JSONObject range = new JSONObject();
					range.put("row", JSON.parseArray(reportRangeAuths.get(i).getRowsNo()));
					range.put("column", JSON.parseArray(reportRangeAuths.get(i).getColsNo()));
					rangeAxis.put("range", range);
					rangeAxis.put("sheetIndex", reportRangeAuths.get(i).getSheetIndex());
					List<ReportRangeAuthUser> datas = groupDatas.get(reportRangeAuths.get(i).getId());
					if(ListUtil.isNotEmpty(datas))
					{
						JSONArray userIds = new JSONArray();
						for (int j = 0; j < datas.size(); j++) {
							userIds.add(datas.get(j).getUserId());
						}
						rangeAxis.put("userIds", userIds);
					}
					if(!sheetRangeAuth.containsKey(reportRangeAuths.get(i).getSheetIndex())) {
						sheetRangeAuth.put(reportRangeAuths.get(i).getSheetIndex(), new JSONObject());
					}
					sheetRangeAuth.getJSONObject(reportRangeAuths.get(i).getSheetIndex()).put(reportRangeAuths.get(i).getRangeTxt(), rangeAxis);
				}
			}
		}else {
			//获取用户对应的权限
			QueryWrapper<ReportRangeAuthUser> rangeAuthUserQueryWrapper = new QueryWrapper<>();
			rangeAuthUserQueryWrapper.eq("tpl_id", sheets.get(0).getTplId());
			rangeAuthUserQueryWrapper.eq("user_id", userInfoDto.getUserId());
			rangeAuthUserQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<ReportRangeAuthUser> reportRangeAuthUsers = this.iReportRangeAuthUserService.list(rangeAuthUserQueryWrapper);
			if(ListUtil.isNotEmpty(reportRangeAuthUsers))
			{
				List<Long> authRangeIds = new ArrayList<>();
				for (int i = 0; i < reportRangeAuthUsers.size(); i++) {
					authRangeIds.add(reportRangeAuthUsers.get(i).getRangeAuthId());
				}
				//获取对应的权限操作范围
				QueryWrapper<ReportRangeAuth> queryWrapper = new QueryWrapper<>();
				queryWrapper.in("id", authRangeIds);
				List<ReportRangeAuth> reportRangeAuths = this.iReportRangeAuthService.list(queryWrapper);
				if(ListUtil.isNotEmpty(reportRangeAuths))
				{
					for (int i = 0; i < reportRangeAuths.size(); i++) {
						JSONObject rangeAxis = new JSONObject();
						rangeAxis.put("rangeAxis", reportRangeAuths.get(i).getRangeTxt());
						JSONObject range = new JSONObject();
						range.put("row", JSON.parseArray(reportRangeAuths.get(i).getRowsNo()));
						range.put("column", JSON.parseArray(reportRangeAuths.get(i).getColsNo()));
						rangeAxis.put("range", range);
						rangeAxis.put("sheetIndex", reportRangeAuths.get(i).getSheetIndex());
						if(!sheetRangeAuth.containsKey(reportRangeAuths.get(i).getSheetIndex())) {
							sheetRangeAuth.put(reportRangeAuths.get(i).getSheetIndex(), new JSONObject());
						}
						sheetRangeAuth.getJSONObject(reportRangeAuths.get(i).getSheetIndex()).put(reportRangeAuths.get(i).getRangeTxt(), rangeAxis);
					}
				}
			}
		}
		List<String> keys = redisUtil.getKeys(RedisPrefixEnum.REPORTTPLSHEETTEMPCACHE.getCode()+"designMode-" +reportTpl.getId());
		if(!ListUtil.isEmpty(keys))
		{
			for (int i = 0; i < keys.size(); i++) {
				if(!databaseSheets.contains(keys.get(i)))
				{
					Object redisCache = redisUtil.get(keys.get(i));
					ResLuckySheetTplSettingsDto result = JSON.parseObject(redisCache.toString(), ResLuckySheetTplSettingsDto.class);
					list.add(result);
				}
			}
		}
		settings.setSettings(list);
		settings.setCreator(isCreator);
		settings.setSheetRangeAuth(sheetRangeAuth);
		if(reportTpl.getCreator() != null)
		{
			SysUser sysUser = this.iSysUserService.getById(reportTpl.getCreator());
			if(sysUser != null)
			{
				settings.setCreatorName(sysUser.getUserName());
			}
		}
		if(StringUtil.isNotEmpty(userInfoDto.getUserName()) && userInfoDto.getUserName().equals(this.thirdPartyType)) {
			//是否是ifram嵌入的第三方调用
			settings.setIsThirdParty(YesNoEnum.YES.getCode());
		}
		return settings;
	}


	/**  
	 * @MethodName: copyReport
	 * @Description: 复制报表
	 * @author caiyang
	 * @param reportTpl
	 * @return
	 * @see com.springreport.api.reporttpl.IReportTplService#copyReport(com.springreport.entity.reporttpl.ReportTpl)
	 * @date 2022-12-05 03:20:59 
	 */  
	@Override
	@Transactional
	public BaseEntity copyReport(ReportTplDto model) {
		BaseEntity result = new BaseEntity();
		Long tplId = model.getId();
		ReportTpl reportTpl = this.getById(tplId);
		String end = DateUtil.getLastSixDigits();
		String newCode = reportTpl.getTplCode()+"_copy_"+end;
		String newName = reportTpl.getTplName()+"_copy_"+end;
		reportTpl.setTplCode(newCode);
		reportTpl.setTplName(newName);
		reportTpl.setIsTemplate(YesNoEnum.NO.getCode());
		reportTpl.setTemplateField(null);
		reportTpl.setIsExample(YesNoEnum.NO.getCode());
		reportTpl.setId(null);
		//保存报表
		this.save(reportTpl);
		//保存报表关联的数据源
		QueryWrapper<ReportTplDatasource> tplDatasourceQueryWrapper = new QueryWrapper<>();
		tplDatasourceQueryWrapper.eq("tpl_id", tplId);
		tplDatasourceQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplDatasource> datasources = this.iReportTplDatasourceService.list(tplDatasourceQueryWrapper);
		if(ListUtil.isNotEmpty(datasources)) {
			for (int i = 0; i < datasources.size(); i++) {
				datasources.get(i).setId(null);
				datasources.get(i).setTplId(reportTpl.getId());
			}
			this.iReportTplDatasourceService.saveBatch(datasources);
		}
		
		//报表数据集查询和保存
		QueryWrapper<ReportTplDataset> tplDatasetQueryWrapper = new QueryWrapper<>();
		tplDatasetQueryWrapper.eq("tpl_id", tplId);
		tplDatasetQueryWrapper.eq("is_common", YesNoEnum.NO.getCode());
		tplDatasetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplDataset> datasets = this.iReportTplDatasetService.list(tplDatasetQueryWrapper);
		if(!ListUtil.isEmpty(datasets))
		{
			for (int i = 0; i < datasets.size(); i++) {
				datasets.get(i).setId(null);
				datasets.get(i).setTplId(reportTpl.getId());
			}
			this.iReportTplDatasetService.saveBatch(datasets);
		}
		//查询sheet信息和sheet下的单元格信息
		QueryWrapper<ReportTplSheet> tplSheetQueryWrapper = new QueryWrapper<>();
		tplSheetQueryWrapper.eq("tpl_id", tplId);
		tplSheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplSheet> reportTplSheets = this.iReportTplSheetService.list(tplSheetQueryWrapper);
		List<LuckysheetReportCell> insertLuckysheetReportCells = new ArrayList<>();
		List<LuckysheetReportBlockCell> insertLuckysheetReportBlockCells = new ArrayList<>();
		List<LuckysheetReportFormsCell> insertLuckysheetReportFormsCells = new ArrayList<>();
		List<ReportFormsDatasource> insertFormsDatasources = new ArrayList<>();
		List<ReportFormsDatasourceAttrs> insertFormsDatasourceAttrs = new ArrayList<>();
		List<ReportTplSheetChart> reportTplSheetCharts = new ArrayList<>();
		List<ReportSheetPdfPrintSetting> reportSheetPdfPrintSettings = new ArrayList<>();
		if(!ListUtil.isEmpty(reportTplSheets))
		{
			for (int i = 0; i < reportTplSheets.size(); i++) {
				Long sheetId = reportTplSheets.get(i).getId();
//				if(reportTpl.getTplType().intValue() == 1)
//				{//展示报表,获取luckysheet_report_cell中的数据和luckysheet_report_block_cell中的数据
					QueryWrapper<LuckysheetReportCell> reportCellQueryWrapper = new QueryWrapper<>();
					reportCellQueryWrapper.eq("sheet_id", reportTplSheets.get(i).getId());
					reportCellQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					List<LuckysheetReportCell> luckysheetReportCells = this.iLuckysheetReportCellService.list(reportCellQueryWrapper);
					String sheetIndex = reportTplSheets.get(i).getSheetIndex();
					reportTplSheets.get(i).setId(IdWorker.getId());
					reportTplSheets.get(i).setTplId(reportTpl.getId());
					reportTplSheets.get(i).setSheetIndex("Sheet"+UUIDUtil.getUUID());
					String calcChain = reportTplSheets.get(i).getCalcChain();
					if(StringUtil.isNotEmpty(calcChain))
					{
						calcChain = calcChain.replaceAll(sheetIndex, reportTplSheets.get(i).getSheetIndex());
					}
					reportTplSheets.get(i).setCalcChain(calcChain);
					String alternateformatSave = reportTplSheets.get(i).getAlternateformatSave();
					if(StringUtil.isNotEmpty(alternateformatSave))
					{
						alternateformatSave = alternateformatSave.replaceAll(sheetIndex, reportTplSheets.get(i).getSheetIndex());
					}
					reportTplSheets.get(i).setAlternateformatSave(alternateformatSave);
					if(!ListUtil.isEmpty(luckysheetReportCells))
					{
						for (int j = 0; j < luckysheetReportCells.size(); j++) {
							luckysheetReportCells.get(j).setTplId(reportTpl.getId());
							luckysheetReportCells.get(j).setSheetId(reportTplSheets.get(i).getId());
							if(luckysheetReportCells.get(j).getCellValueType().intValue() == 3)
							{//循环块
								QueryWrapper<LuckysheetReportBlockCell> blockCellQueryWrapper = new QueryWrapper<>();
								blockCellQueryWrapper.eq("report_cell_id", luckysheetReportCells.get(j).getId());
								blockCellQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
								List<LuckysheetReportBlockCell> blockCells = this.iLuckysheetReportBlockCellService.list(blockCellQueryWrapper);
								luckysheetReportCells.get(j).setId(IdWorker.getId());
								if(!ListUtil.isEmpty(blockCells)) {
									for (int k = 0; k < blockCells.size(); k++) {
										blockCells.get(k).setId(null);
										blockCells.get(k).setReportCellId(luckysheetReportCells.get(j).getId());
									}
									insertLuckysheetReportBlockCells.addAll(blockCells);
								}
							}else {
								luckysheetReportCells.get(j).setId(IdWorker.getId());
							}
						}
						insertLuckysheetReportCells.addAll(luckysheetReportCells);
					}
					//关联数据源和数据源属性获取
					QueryWrapper<ReportFormsDatasource> formsDatasourceQueryWrapper = new QueryWrapper<>();
					formsDatasourceQueryWrapper.eq("sheet_id", sheetId);
					formsDatasourceQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					List<ReportFormsDatasource> formsDatasources = this.iReportFormsDatasourceService.list(formsDatasourceQueryWrapper);
					if(!ListUtil.isEmpty(formsDatasources)) {
						for (int j = 0; j < formsDatasources.size(); j++) {
							QueryWrapper<ReportFormsDatasourceAttrs> attrsQueryWrapper = new QueryWrapper<>();
							attrsQueryWrapper.eq("report_forms_datasource_id", formsDatasources.get(j).getId());
							attrsQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
							List<ReportFormsDatasourceAttrs> formsDatasourceAttrs = this.iReportFormsDatasourceAttrsService.list(attrsQueryWrapper);
							formsDatasources.get(j).setId(IdWorker.getId());
							formsDatasources.get(j).setTplId(reportTpl.getId());
							formsDatasources.get(j).setSheetId(reportTplSheets.get(i).getId());
							if(!ListUtil.isEmpty(formsDatasourceAttrs)) {
								for (int k = 0; k < formsDatasourceAttrs.size(); k++) {
									formsDatasourceAttrs.get(k).setId(null);
									formsDatasourceAttrs.get(k).setReportFormsDatasourceId(formsDatasources.get(j).getId());
								}
								insertFormsDatasourceAttrs.addAll(formsDatasourceAttrs);
							}
						}
						insertFormsDatasources.addAll(formsDatasources);
					}
//				}else {//填报报表
//					//填报单元格信息获取
//					QueryWrapper<LuckysheetReportFormsCell> formsCellQueryWrapper = new QueryWrapper<>();
//					formsCellQueryWrapper.eq("sheet_id", sheetId);
//					formsCellQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
//					List<LuckysheetReportFormsCell> formsCells = this.iLuckysheetReportFormsCellService.list(formsCellQueryWrapper);
//					String sheetIndex = reportTplSheets.get(i).getSheetIndex();
//					reportTplSheets.get(i).setId(IdWorker.getId());
//					reportTplSheets.get(i).setTplId(reportTpl.getId());
//					reportTplSheets.get(i).setSheetIndex("Sheet"+UUIDUtil.getUUID());
//					String calcChain = reportTplSheets.get(i).getCalcChain();
//					if(StringUtil.isNotEmpty(calcChain))
//					{
//						calcChain = calcChain.replaceAll(sheetIndex, reportTplSheets.get(i).getSheetIndex());
//					}
//					reportTplSheets.get(i).setCalcChain(calcChain);
//					String alternateformatSave = reportTplSheets.get(i).getAlternateformatSave();
//					if(StringUtil.isNotEmpty(alternateformatSave))
//					{
//						alternateformatSave = alternateformatSave.replaceAll(sheetIndex, reportTplSheets.get(i).getSheetIndex());
//					}
//					reportTplSheets.get(i).setAlternateformatSave(alternateformatSave);
//					if(!ListUtil.isEmpty(formsCells))
//					{
//						for (int j = 0; j < formsCells.size(); j++) {
//							formsCells.get(j).setId(null);
//							formsCells.get(j).setTplId(reportTpl.getId());
//							formsCells.get(j).setSheetId(reportTplSheets.get(i).getId());
//						}
//						insertLuckysheetReportFormsCells.addAll(formsCells);
//					}
//					
//				}
				//获取chart配置信息
				QueryWrapper<ReportTplSheetChart> sheetChartQueryWrapper = new QueryWrapper<>();
				sheetChartQueryWrapper.eq("tpl_id", tplId);
				sheetChartQueryWrapper.eq("sheet_id", sheetId);
				sheetChartQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				reportTplSheetCharts = this.iReportTplSheetChartService.list(sheetChartQueryWrapper);
				if(ListUtil.isNotEmpty(reportTplSheetCharts)) {
					for (int j = 0; j < reportTplSheetCharts.size(); j++) {
						reportTplSheetCharts.get(j).setId(null);
						reportTplSheetCharts.get(j).setTplId(reportTpl.getId());
						reportTplSheetCharts.get(j).setSheetId(reportTplSheets.get(i).getId());
					}
				}
				//获取打印配置信息
				QueryWrapper<ReportSheetPdfPrintSetting> printSettingQueryWrapper = new QueryWrapper<>();
				printSettingQueryWrapper.eq("tpl_id", tplId);
				printSettingQueryWrapper.eq("tpl_sheet_id", sheetId);
				printSettingQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				reportSheetPdfPrintSettings = this.iReportSheetPdfPrintSettingService.list(printSettingQueryWrapper);
				if(ListUtil.isNotEmpty(reportSheetPdfPrintSettings)) {
					for (int j = 0; j < reportSheetPdfPrintSettings.size(); j++) {
						reportSheetPdfPrintSettings.get(j).setId(null);
						reportSheetPdfPrintSettings.get(j).setTplId(reportTpl.getId());
						reportSheetPdfPrintSettings.get(j).setTplSheetId(reportTplSheets.get(i).getId());
					}
				}
			}
			this.iReportTplSheetService.saveBatch(reportTplSheets);
			if(!ListUtil.isEmpty(insertLuckysheetReportCells))
			{
				this.iLuckysheetReportCellService.saveBatch(insertLuckysheetReportCells);
			}
			if(!ListUtil.isEmpty(insertLuckysheetReportBlockCells))
			{
				this.iLuckysheetReportBlockCellService.saveBatch(insertLuckysheetReportBlockCells);
			}
			if(!ListUtil.isEmpty(insertLuckysheetReportFormsCells))
			{
				this.iLuckysheetReportFormsCellService.saveBatch(insertLuckysheetReportFormsCells);
			}
			if(!ListUtil.isEmpty(insertFormsDatasources))
			{
				this.iReportFormsDatasourceService.saveBatch(insertFormsDatasources);
			}
			if(!ListUtil.isEmpty(insertFormsDatasourceAttrs))
			{
				this.iReportFormsDatasourceAttrsService.saveBatch(insertFormsDatasourceAttrs);
			}
			if(ListUtil.isNotEmpty(reportSheetPdfPrintSettings)) {
				this.iReportSheetPdfPrintSettingService.saveBatch(reportSheetPdfPrintSettings);
			}
			if(ListUtil.isNotEmpty(reportTplSheetCharts)) {
				this.iReportTplSheetChartService.saveBatch(reportTplSheetCharts);
			}
		}
		result.setStatusMsg(MessageUtil.getValue("info.copy",new String[] {newName}));
		return result;
	}
	
	/**  
	 * @MethodName: getCellFormat
	 * @Description: 获取单元格格式
	 * @author caiyang
	 * @param cellData
	 * @return 
	 * @return JSONObject
	 * @date 2023-01-25 10:42:39 
	 */  
	private JSONObject getCellFormat(JSONObject cellData)
	{
		JSONObject result = null;
		if(cellData != null)
		{
			JSONObject v = cellData.getJSONObject("v");	
			if(v!=null)
			{
				result = v.getJSONObject("ct");
			}
		}
		return result;
	}
	
	/**  
	 * @MethodName: getDict
	 * @Description: 获取数据字典
	 * @author caiyang
	 * @param datasourceId
	 * @param dictType
	 * @return 
	 * @return Map<String,String>
	 * @date 2023-03-13 11:06:38 
	 */  
	private Map<String, String> getDict(LuckySheetBindData bindData,Map<String, Map<String, String>> dictCache)
	{
		Map<String, String> result = null;
		if(bindData.getSourceType() == 2 || bindData.getSourceType() == 3) {
			String key = bindData.getSheetIndex()+"_"+bindData.getCoordsx()+"_"+bindData.getCoordsy();
			if(dictCache.containsKey(key)) {
				result = dictCache.get(key);
			}
		}else {
			if(bindData.getDatasourceId() != null && StringUtil.isNotEmpty(bindData.getDictType()))
			{
				List<ReportDatasourceDictData> dictDatas = (List<ReportDatasourceDictData>) redisUtil.get(RedisPrefixEnum.REPORTDICT.getCode()+bindData.getDatasourceId()+"_"+bindData.getDictType());
				if(ListUtil.isEmpty(dictDatas))
				{
					QueryWrapper<ReportDatasourceDictData> dictDataQueryWrapper = new QueryWrapper<>();
					dictDataQueryWrapper.eq("datasource_id", bindData.getDatasourceId());
					dictDataQueryWrapper.eq("dict_type", bindData.getDictType());
					dictDataQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					dictDatas = this.iReportDatasourceDictDataService.list(dictDataQueryWrapper);
					if(!ListUtil.isEmpty(dictDatas))
					{
						redisUtil.set(RedisPrefixEnum.REPORTDICT.getCode()+bindData.getDatasourceId()+"_"+bindData.getDictType(), dictDatas);
					}
				}
				if(!ListUtil.isEmpty(dictDatas))
				{
					result = new HashMap<>();
					for (int i = 0; i < dictDatas.size(); i++) {
						result.put(RedisPrefixEnum.REPORTDICT.getCode()+bindData.getDatasourceId()+"_"+bindData.getDictType()+"_"+dictDatas.get(i).getDictValue(), dictDatas.get(i).getDictLabel());
					}
				}
			}
		}
		return result;
	}


	/**  
	 * @MethodName: transf2OnlineReport
	 * @Description: 转成在线报表文档
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @return
	 * @throws Exception 
	 * @see com.springreport.api.reporttpl.IReportTplService#transf2OnlineReport(com.springreport.dto.reporttpl.MesGenerateReportDto)
	 * @date 2023-02-06 08:32:32 
	 */  
	@Override
	public BaseEntity transf2OnlineReport(MesGenerateReportDto mesGenerateReportDto,UserInfoDto userInfoDto) throws Exception {
		ReportTpl reportTpl = this.getById(mesGenerateReportDto.getTplId());
		if (reportTpl == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"报表模板"}));
		}
		ResPreviewData resPreviewData = this.generateLuckySheetReportData(mesGenerateReportDto,true,userInfoDto,reportTpl,false);
		OnlineTpl onlineTpl = new OnlineTpl();
		onlineTpl.setId(IdWorker.getId());
		onlineTpl.setTplName(mesGenerateReportDto.getTplName());
		onlineTpl.setReportId(mesGenerateReportDto.getTplId());
		onlineTpl.setSource(2);
		List<OnlineTplSheet> onlineTplSheets = new ArrayList<OnlineTplSheet>();
		List<LuckysheetOnlineCell> luckysheetOnlineCells = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		if(resPreviewData != null && !ListUtil.isEmpty(resPreviewData.getSheetDatas()))
		{
			for (int i = 0; i < resPreviewData.getSheetDatas().size(); i++) {
				OnlineTplSheet onlineTplSheet = new OnlineTplSheet();
				onlineTplSheet.setId(IdWorker.getId());
				onlineTplSheet.setTplId(onlineTpl.getId());
				onlineTplSheet.setSheetName(resPreviewData.getSheetDatas().get(i).getSheetName());
				onlineTplSheet.setSheetIndex(resPreviewData.getSheetDatas().get(i).getSheetIndex());
				onlineTplSheet.setSheetOrder(resPreviewData.getSheetDatas().get(i).getSheetOrder());
				onlineTplSheet.setRowlen(objectMapper.writeValueAsString(resPreviewData.getSheetDatas().get(i).getConfig().get("rowlen")));
				onlineTplSheet.setColumnlen(objectMapper.writeValueAsString(resPreviewData.getSheetDatas().get(i).getConfig().get("columnlen")));
				onlineTplSheet.setMerge(objectMapper.writeValueAsString(resPreviewData.getSheetDatas().get(i).getConfig().get("merge")));
				onlineTplSheet.setBorderInfo(objectMapper.writeValueAsString(resPreviewData.getSheetDatas().get(i).getConfig().get("borderInfo")));
				onlineTplSheet.setFrozen(objectMapper.writeValueAsString(resPreviewData.getSheetDatas().get(i).getFrozen()));
				onlineTplSheet.setImages(objectMapper.writeValueAsString(resPreviewData.getSheetDatas().get(i).getImages()));
				onlineTplSheet.setCalcChain(objectMapper.writeValueAsString(resPreviewData.getSheetDatas().get(i).getCalcChain()));
				onlineTplSheet.setHyperLink(objectMapper.writeValueAsString(resPreviewData.getSheetDatas().get(i).getHyperlinks()));
				onlineTplSheets.add(onlineTplSheet);
				if(!ListUtil.isEmpty(resPreviewData.getSheetDatas().get(i).getCellDatas()))
				{
					for (int j = 0; j < resPreviewData.getSheetDatas().get(i).getCellDatas().size(); j++) {
						LuckysheetOnlineCell luckysheetOnlineCell = new LuckysheetOnlineCell();
						luckysheetOnlineCell.setTplId(onlineTpl.getId());
						luckysheetOnlineCell.setSheetId(onlineTplSheet.getId());
						int r = (int) resPreviewData.getSheetDatas().get(i).getCellDatas().get(j).get("r");
						int c = (int) resPreviewData.getSheetDatas().get(i).getCellDatas().get(j).get("c");
						luckysheetOnlineCell.setCoordsx(r);
						luckysheetOnlineCell.setCoordsy(c);
						JSONObject cellData =  objectMapper.readValue(objectMapper.writeValueAsString(resPreviewData.getSheetDatas().get(i).getCellDatas().get(j)), JSONObject.class);
						JSONObject cellValue = cellData.getJSONObject(LuckySheetPropsEnum.CELLVALUE.getCode());
						String v = cellValue.getString("v");
						if(StringUtil.isNotEmpty(v))
						{
							luckysheetOnlineCell.setCellData(v);
						}
						JSONObject ct = cellValue.getJSONObject("ct");
						if(ct != null)
						{
							luckysheetOnlineCell.setCt(objectMapper.writeValueAsString(ct));
						}
						String ff = cellValue.getString("ff");
						if(StringUtil.isNotEmpty(ff))
						{
							luckysheetOnlineCell.setFf(ff);
						}
						String bg = cellValue.getString("bg");
						if(StringUtil.isNotEmpty(bg))
						{
							luckysheetOnlineCell.setBg(bg);
						}
						String fc = cellValue.getString("fc");
						if(StringUtil.isNotEmpty(fc))
						{
							luckysheetOnlineCell.setFc(fc);
						}
						Integer bl = cellValue.getInteger("bl");
						if(bl != null)
						{
							luckysheetOnlineCell.setBl(bl);
						}
						Integer it = cellValue.getInteger("it");
						if(it != null)
						{
							luckysheetOnlineCell.setIt(it);
						}
						Integer fs = cellValue.getInteger("fs");
						if(fs != null)
						{
							luckysheetOnlineCell.setIt(fs);
						}
						Integer cl = cellValue.getInteger("cl");
						if(cl != null)
						{
							luckysheetOnlineCell.setCl(cl);
						}
						Integer vt = cellValue.getInteger("vt");
						if(vt != null)
						{
							luckysheetOnlineCell.setVt(vt);
						}
						Integer ht =cellValue.getInteger("ht");
						if(ht != null)
						{
							luckysheetOnlineCell.setHt(ht);
						}
//						luckysheetOnlineCell.setCellData(objectMapper.writeValueAsString(resPreviewData.getSheetDatas().get(i).getCellDatas().get(j)));
						luckysheetOnlineCells.add(luckysheetOnlineCell);
					}
				}
			}
		}else {
			OnlineTplSheet onlineTplSheet = new OnlineTplSheet();
			onlineTplSheet.setId(IdWorker.getId());
			onlineTplSheet.setTplId(onlineTpl.getId());
			onlineTplSheet.setSheetName("sheet1");
			onlineTplSheet.setSheetIndex("Sheet"+UUIDUtil.getUUID());
			onlineTplSheet.setSheetOrder(0);
			onlineTplSheets.add(onlineTplSheet);
		}
		this.iOnlineTplService.save(onlineTpl);
		if(!ListUtil.isEmpty(onlineTplSheets))
		{
			this.iOnlineTplSheetService.saveBatch(onlineTplSheets);
		}
		if(!ListUtil.isEmpty(luckysheetOnlineCells))
		{
			this.iLuckysheetOnlineCellService.saveBatch(luckysheetOnlineCells);
		}
		return onlineTpl;
	}


	/**  
	 * @MethodName: getSheetPdf
	 * @Description: 生成sheet页的pdf并返回访问路径
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param userInfoDto
	 * @return
	 * @throws Exception 
	 * @see com.springreport.api.reporttpl.IReportTplService#getSheetPdf(com.springreport.dto.reporttpl.MesGenerateReportDto, com.springreport.base.UserInfoDto)
	 * @date 2023-04-03 01:48:43 
	 */  
	@Override
	public Map<String, Object> getSheetPdf(MesGenerateReportDto mesGenerateReportDto, UserInfoDto userInfoDto,
			HttpServletResponse httpServletResponse) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		ReportTpl reportTpl = this.getById(mesGenerateReportDto.getTplId());
		if (reportTpl == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"报表模板"}));
		}
		ResPreviewData resPreviewData = null;
//		if(reportTpl.getTplType().intValue() == 1)
//		{
			resPreviewData = this.generateLuckySheetReportData(mesGenerateReportDto,mesGenerateReportDto.isPatination(),userInfoDto,reportTpl,true);
//		}else {
//			resPreviewData = this.iReportTplFormsService.previewLuckysheetReportFormsData(mesGenerateReportDto, userInfoDto, reportTpl,mesGenerateReportDto.isPatination());
//		}
		MesExportExcel mesExportExcel = new MesExportExcel();
		List<MesSheetConfig> sheetConfigs = new ArrayList<MesSheetConfig>();
		if(!ListUtil.isEmpty(resPreviewData.getSheetDatas()))
		{
			for (int i = 0; i < resPreviewData.getSheetDatas().size(); i++) {
				MesSheetConfig mesSheetConfig = new MesSheetConfig();
				Object borderInfos = null;
				Map<String, Object> rowlen = null;
				Map<String, Object> columnlen = null;
				if(resPreviewData.getSheetDatas().get(i).getConfig() != null)
				{
					borderInfos = resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.BORDERINFO.getCode());
					rowlen = (Map<String, Object>) resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.ROWLEN.getCode());//行高
					columnlen = (Map<String, Object>) resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.COLUMNLEN.getCode());//列宽
				}
				List<Object> borderInfoList = null;
				if(borderInfos != null)
				{
					borderInfoList = (List<Object>) borderInfos;
				}
				mesSheetConfig.setCellDatas(resPreviewData.getSheetDatas().get(i).getCellDatas());
				mesSheetConfig.setSheetname(resPreviewData.getSheetDatas().get(i).getSheetName());
				mesSheetConfig.setBorderInfos(borderInfoList);
				mesSheetConfig.setRowlen(rowlen);
				mesSheetConfig.setColumnlen(columnlen);
				mesSheetConfig.setFrozen(JSONObject.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getFrozen())));
				mesSheetConfig.setImages(JSONObject.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getImages())));
				mesSheetConfig.setHyperlinks(resPreviewData.getSheetDatas().get(i).getHyperlinks());
				mesSheetConfig.setBase64Images(resPreviewData.getSheetDatas().get(i).getBase64Imgs());
				mesSheetConfig.setImageDatas(resPreviewData.getSheetDatas().get(i).getImageDatas());
				mesSheetConfig.setChart(resPreviewData.getSheetDatas().get(i).getChart());
				mesSheetConfig.setChartCells(resPreviewData.getSheetDatas().get(i).getChartCells());
				mesSheetConfig.setLuckysheetConditionformatSave(resPreviewData.getSheetDatas().get(i).getLuckysheetConditionformatSave());
				if(resPreviewData.getSheetDatas().get(i).getMaxXAndY() != null)
				{
					mesSheetConfig.setMaxXAndY(resPreviewData.getSheetDatas().get(i).getMaxXAndY());	
				}else {
					Map<String, Integer> maxXAndY = new HashMap<>();
					maxXAndY.put("maxX", 50);
					maxXAndY.put("maxY", 26);
					mesSheetConfig.setMaxXAndY(maxXAndY);	
				}
				sheetConfigs.add(mesSheetConfig);
			}
			mesExportExcel.setPassword(mesGenerateReportDto.getPassword());
			mesExportExcel.setSheetConfigs(sheetConfigs);
			mesExportExcel.setChartsBase64(mesGenerateReportDto.getChartsBase64());
			mesExportExcel.setImageInfos(new HashMap<>());
			mesExportExcel.setBackImages(new HashMap<>());
			ByteArrayInputStream is =ReportExcelUtil.getExcelStream(mesExportExcel,reportTpl.getTplName(),"",1);
			String filename = IdWorker.getIdStr()+".pdf";
			String date = DateUtil.getNow(DateUtil.FORMAT_LONOGRAM);
			File dest = new File(dirPath + date + "/" + filename);
			FileUtil.createFile(dest);
			String fileUri = MessageUtil.getValue("file.url.prefix")+date+"/"+filename+"?t="+System.currentTimeMillis();
			result.put("fileUrl", fileUri);
			List<ExcelObject> objects = new ArrayList<ExcelObject>();
			int x = resPreviewData.getSheetDatas().get(0).getStartXAndY().get("x")==null?0:resPreviewData.getSheetDatas().get(0).getStartXAndY().get("x");
			int y = resPreviewData.getSheetDatas().get(0).getStartXAndY().get("y")==null?0:resPreviewData.getSheetDatas().get(0).getStartXAndY().get("y");
			int endx = resPreviewData.getSheetDatas().get(0).getMaxXAndY().get("maxX")==null?0:resPreviewData.getSheetDatas().get(0).getMaxXAndY().get("maxX");
			int endy = resPreviewData.getSheetDatas().get(0).getMaxXAndY().get("maxY")==null?0:resPreviewData.getSheetDatas().get(0).getMaxXAndY().get("maxY");
			JSONObject colhidden = resPreviewData.getSheetDatas().get(0).getColhidden() == null?new JSONObject():resPreviewData.getSheetDatas().get(0).getColhidden();
			JSONObject rowhidden = resPreviewData.getSheetDatas().get(0).getRowhidden() == null?new JSONObject():resPreviewData.getSheetDatas().get(0).getRowhidden();
			PrintSettingsDto printSettingsDto = new PrintSettingsDto();
			if(resPreviewData.getSheetDatas().get(0).getPrintSettings() != null)
			{
				BeanUtils.copyProperties(resPreviewData.getSheetDatas().get(0).getPrintSettings(), printSettingsDto);
			}
			printSettingsDto.setAuthor(userInfoDto.getUserName());
			printSettingsDto.setTitle(reportTpl.getTplName());
			printSettingsDto.setSubject(resPreviewData.getSheetDatas().get(0).getSheetName());
			printSettingsDto.setKeyWords("SpringReport_2.1.0");
			printSettingsDto.setHorizontalPage(
					resPreviewData.getSheetDatas().get(0).getPrintSettings() == null?2:resPreviewData.getSheetDatas().get(0).getPrintSettings().getHorizontalPage());
			printSettingsDto.setPageDivider(resPreviewData.getSheetDatas().get(0).getPageDivider());
			if(mesGenerateReportDto.isPatination()) {
				if(YesNoEnum.YES.getCode().intValue() == mesGenerateReportDto.getIsCustomerPage().intValue()) {
					printSettingsDto.setStartPage(mesGenerateReportDto.getStartPage());
				}else {
					printSettingsDto.setStartPage(Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("currentPage"))));
				}
			}
			objects.add(new ExcelObject("excel",is,x,y,endx,endy,mesGenerateReportDto.getPdfType(),colhidden,rowhidden,resPreviewData.getSheetDatas().get(0).getXxbtScreenshot(),printSettingsDto,mesExportExcel.getImageInfos(),mesExportExcel.getBackImages(),mesExportExcel.getSheetConfigs().get(0).getWrapText()));
			FileOutputStream fos = new FileOutputStream(dirPath + date + "/" + filename);
			Excel2Pdf pdf = new Excel2Pdf(objects, fos);
			pdf.convert();
		}
		return result;
	}
	
	private List<String> processChartData(LuckySheetBindData bindData) {
		List<String> seriesData = new ArrayList<>();
		if(CellValueTypeEnum.FIXED.getCode().intValue() == bindData.getCellValueType())
		{//固定值
			seriesData.add(bindData.getCellValue());
		}else if(CellValueTypeEnum.VARIABLE.getCode().intValue() == bindData.getCellValueType())
		{//动态值
			if(AggregateTypeEnum.SUMMARY.getCode().equals(bindData.getAggregateType())) {
				//汇总进行汇总计算
				if(YesNoEnum.YES.getCode().intValue() == bindData.getIsConditions().intValue())
				{
					bindData.setDatas(bindData.getFilterDatas());
				}
				String calculateResult = luckySheetCalculates.get(bindData.getCellFunction().intValue()).calculate(bindData);
				seriesData.add(calculateResult);
			}else if(AggregateTypeEnum.GROUPSUMMARY.getCode().equals(bindData.getAggregateType())) {
				//分组汇总
				GroupSummaryData groupSummaryData = new GroupSummaryData();
	            groupSummaryData.setDigit(bindData.getDigit());
	            groupSummaryData.setProperty(bindData.getProperty());
	            List<List<Map<String, Object>>> bindDatas = null;
	    		if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode())
	    		{
	    			bindDatas = bindData.getFilterDatas();
	    		}else {
	    			bindDatas = bindData.getDatas();
	    		}
	    		if(ListUtil.isNotEmpty(bindDatas))
	    		{
	    			if(CellExtendEnum.NOEXTEND.getCode().intValue() == bindData.getCellExtend().intValue()) {
						//不扩展
	    				groupSummaryData.setDatas(bindDatas.get(0));
						String value = luckySheetGroupCalculates.get(bindData.getCellFunction().intValue()).calculate(groupSummaryData);
						seriesData.add(value);
					}else {
						for (int i = 0; i < bindDatas.size(); i++) {
							groupSummaryData.setDatas(bindDatas.get(i));
							String value = luckySheetGroupCalculates.get(bindData.getCellFunction().intValue()).calculate(groupSummaryData);
							seriesData.add(value);
						}
					}
	    		}
			}
			else if(AggregateTypeEnum.LIST.getCode().equals(bindData.getAggregateType()) || AggregateTypeEnum.GROUP.getCode().equals(bindData.getAggregateType())){
				List<List<Map<String, Object>>> bindDatas = null;
	    		if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode())
	    		{
	    			bindDatas = bindData.getFilterDatas();
	    		}else {
	    			bindDatas = bindData.getDatas();
	    		}
	    		if(ListUtil.isNotEmpty(bindDatas))
	    		{
	    			String property = bindData.getProperty();
	    			if(CellExtendEnum.NOEXTEND.getCode().intValue() == bindData.getCellExtend().intValue())
					{//不扩展
						Map<String, Object> mapDatas = new HashMap<String, Object>();
						mapDatas = ListUtil.getProperties(property, bindDatas.get(0).get(0));	
						Set<String> set = mapDatas.keySet();
						for (String o : set) {
							property = property.replace(o, mapDatas.get(o)==null?"":String.valueOf(mapDatas.get(o)));
						}
						Object value = null;
						try {
							if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
								AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
								AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
								value = AviatorEvaluator.execute(property);
							}else {
	    						value = property;
	    					}
							if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
	                		{
	                			value = 0;
	                		}
						} catch (Exception e) {
							
						}
						seriesData.add(String.valueOf(value));
					}else {
						//扩展
						for (int i = 0; i < bindDatas.size(); i++) {
							for (int j = 0; j < bindDatas.get(i).size(); j++) {
								String tempProperty = property;
								Map<String, Object> mapDatas = new HashMap<String, Object>();
								mapDatas = ListUtil.getProperties(tempProperty, bindDatas.get(i).get(j));	
								Set<String> set = mapDatas.keySet();
								for (String o : set) {
									tempProperty = tempProperty.replace(o, mapDatas.get(o)==null?"":String.valueOf(mapDatas.get(o)));
								}
								Object value = null;
								try {
									if(CheckUtil.validate(String.valueOf(property))&&CheckUtil.containsOperator(String.valueOf(property))) {
										AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, true);
										AviatorEvaluator.getInstance().setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, true);
										value = AviatorEvaluator.execute(property);
									}else {
			    						value = tempProperty;
			    					}
									if(value instanceof Double && (Double.isNaN((double) value) || Double.isInfinite((double) value)))
			                		{
			                			value = 0;
			                		}
								} catch (Exception e) {
									
								}
								seriesData.add(String.valueOf(value));
							}
						}
					}
	    		}
			}
		}
		return seriesData;
	}


	@Override
	public JSONArray uploadExcel(MultipartFile file,String gridKey,UserInfoDto userInfoDto) throws Exception {
		int sheetSize = 0;
		List<String> keys = redisUtil.getKeys(RedisPrefixEnum.DOCOMENTDATA.getCode()+gridKey);
		List<String> cacheSheetNames = new ArrayList<>();
		if(!ListUtil.isEmpty(keys))
		{
			sheetSize = keys.size();
			List<Object> cacheDatas = redisUtil.multiGet(keys);
			if(!ListUtil.isEmpty(cacheDatas)) {
				for (int i = 0; i < cacheDatas.size(); i++) {
					Luckysheet luckysheet = JSON.parseObject(String.valueOf(cacheDatas.get(i)), Luckysheet.class);
					if(luckysheet != null) {
						cacheSheetNames.add(luckysheet.getSheetName());
					}
				}
			}
		}
		JSONArray result = null;
		if(file.getOriginalFilename().endsWith(".xlsx")) {
			List<String> sheetNames = DocumentToLuckysheetUtil.getSheetsName(file);
			if(!ListUtil.isEmpty(sheetNames))
			{
				for (int i = 0; i < sheetNames.size(); i++) {
					for (int j = 0; j < cacheSheetNames.size(); j++) {
						if(sheetNames.get(i).equals(cacheSheetNames.get(j)))
						{
							throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist",new String[] {"sheet名称【"+sheetNames.get(i)+"】"}));
						}
					}
				}
			}
			result =  DocumentToLuckysheetUtil.xlsx2Luckysheet(file);
		}else if(file.getOriginalFilename().endsWith(".csv")){
			String sheetName = file.getOriginalFilename().replace(".csv", "");
			for (int j = 0; j < cacheSheetNames.size(); j++) {
				if(sheetName.equals(cacheSheetNames.get(j)))
				{
					throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist",new String[] {"sheet名称【"+sheetName+"】"}));
				}
			}
			result =  DocumentToLuckysheetUtil.csv2Luckysheet(file);
		}
		
		if(!ListUtil.isEmpty(result))
		{
			List<LuckysheetHis> list = new ArrayList<>();
			for (int i = 0; i < result.size(); i++) {
				LuckysheetHis luckysheetHis = new LuckysheetHis();
				JSONObject sheetData = result.getJSONObject(i);
				//先处理图片数据，将图片上传到服务器
				JSONArray images = sheetData.getJSONArray("images");
				JSONObject imageData = new JSONObject();
				if(!ListUtil.isEmpty(images))
				{
					for (int j = 0; j < images.size(); j++) {
						JSONObject image = images.getJSONObject(j);
						byte[] imgData = image.getBytes("imgBytes");
						String imgType = image.getString("imgType");
						String fileName = IdWorker.getIdStr()+"."+imgType;
						Map<String, String> uploadResult = this.iCommonService.upload(imgData, fileName);
						image.put("src", uploadResult.get("fileUri"));
						image.remove("imgBytes");
						image.remove("imgType");
						imageData.put("img_"+IdWorker.get32UUID(), image);
					}
				}
				String blockId = this.iCoeditService.getIdByListId(gridKey);
				Luckysheet luckysheet = new Luckysheet();
				luckysheet.setId(IdWorker.getId());
				luckysheet.setBlockId(blockId);
				luckysheet.setRowSize(sheetData.getIntValue("row"));
				luckysheet.setColumnSize(sheetData.getIntValue("column"));
				luckysheet.setSheetIndex(sheetData.getString("index"));
				luckysheet.setListId(gridKey);
				luckysheet.setStatus(sheetData.getIntValue("status"));
				luckysheet.setSheetOrder(sheetData.getIntValue("order")+sheetSize);
				luckysheet.setSheetName(sheetData.getString("name"));
				luckysheet.setHide(sheetData.getIntValue("hide"));
				luckysheet.setMergeInfo(JSON.toJSONString(sheetData.getJSONObject("config").get("merge")));
				luckysheet.setRowlen(JSON.toJSONString(sheetData.getJSONObject("config").get("rowlen")));
				luckysheet.setColumnlen(JSON.toJSONString(sheetData.getJSONObject("config").get("columnlen")));
				luckysheet.setRowhidden(JSON.toJSONString(sheetData.getJSONObject("config").get("rowhidden")));
				luckysheet.setColhidden(JSON.toJSONString(sheetData.getJSONObject("config").get("colhidden")));
				luckysheet.setBorderInfo(JSON.toJSONString(sheetData.getJSONObject("config").get("borderInfo")));
				luckysheet.setCalcchain(JSON.toJSONString(sheetData.get("calcChain")));
				luckysheet.setImage(JSON.toJSONString(imageData));
				luckysheet.setDataverification(JSON.toJSONString(sheetData.get("dataVerification")));
				luckysheet.setFrozen(JSON.toJSONString(sheetData.get("frozen")));
				luckysheet.setFilterSelect(JSON.toJSONString(sheetData.get("filter_select")));
				luckysheet.setLuckysheetConditionformatSave(JSON.toJSONString(sheetData.get("luckysheet_conditionformat_save")));
				String sheetKey = RedisPrefixEnum.DOCOMENTDATA.getCode() + gridKey+"_"+luckysheet.getSheetIndex()+"_"+blockId;
				redisUtil.set(sheetKey, JSON.toJSONString(luckysheet));
				this.iLuckysheetService.save(luckysheet);
				JSONArray cellDatas = sheetData.getJSONArray("celldata");
				if(!ListUtil.isEmpty(cellDatas))
				{
					List<Map<String, JSONObject>> datas = new ArrayList<Map<String,JSONObject>>();
					Map<String, JSONObject> map = null;
					JSONObject cellData = null;
					List<LuckysheetCell> luckysheetCells = new ArrayList<LuckysheetCell>();
					LuckysheetCell newCellData = null;
					for (int j = 0; j < cellDatas.size(); j++) {
						if(j == 0 || j%Constants.MQ_LIST_LIMIT_SIZE == 0)
        				{
        					Map<String, JSONObject> tempMap = new HashMap<String, JSONObject>();
        					map = tempMap;
        					datas.add(map);
        				}
						cellData = cellDatas.getJSONObject(j);
						int r = cellData.getIntValue("r");
        				int c = cellData.getIntValue("c");
        				long id = cellData.getLongValue("id");
        				cellData.remove("r");
        				cellData.remove("c");
        				newCellData = new LuckysheetCell();
        				newCellData.setId(id);
        				newCellData.setBlockId(blockId);
        				newCellData.setRowNo(r);
        				newCellData.setColumnNo(c);
        				newCellData.setSheetIndex(luckysheet.getSheetIndex());
        				newCellData.setListId(gridKey);
        				newCellData.setCellData(JSON.toJSONString(cellData));
        				luckysheetCells.add(newCellData);
        				String cellDataKey = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +gridKey + "_" + luckysheet.getSheetIndex() + "_" + r + "_" + c;
        				cellData.put("r", r);
        				cellData.put("c", c);
        				map.put(cellDataKey, cellData);
					}
					for (int j = 0; j < datas.size(); j++) {
						redisUtil.multiSet(datas.get(j));
					}
	    			List<List<LuckysheetCell>> subs = ListUtils.partition(luckysheetCells , Constants.MQ_LIST_LIMIT_SIZE);
	    			for (int j = 0; j < subs.size(); j++) {
	    				this.mqProcessService.batchProcessCellData(MqTypeEnums.BATCHINSERTCELLDATA.getCode(), subs.get(j),luckysheet.getSheetIndex(), gridKey,blockId);
					}
				}
				luckysheetHis.setSheetIndex(sheetData.getString("index"));
				luckysheetHis.setListId(gridKey);
				luckysheetHis.setChangeDesc("导入操作：从excel导入数据，sheet名称："+sheetData.getString("name")+"，sheet标识："+luckysheetHis.getSheetIndex());
				luckysheetHis.setRemark("导入操作：从excel导入数据，sheet名称："+sheetData.getString("name")+"，sheet标识："+luckysheetHis.getSheetIndex());
				luckysheetHis.setType(1);
				luckysheetHis.setOperateKey("shi");
				luckysheetHis.setOperator(userInfoDto.getUserName());
				luckysheetHis.setCreator(userInfoDto.getUserId());
				luckysheetHis.setCreateTime(new Date());
				luckysheetHis.setUpdater(userInfoDto.getUserId());
				luckysheetHis.setUpdateTime(new Date());
				list.add(luckysheetHis);
			}
			for (int i = 0; i < result.size(); i++) {
				result.getJSONObject(i).remove("celldata");
			}
			if(!ListUtil.isEmpty(list))
			{
				this.iLuckysheetHisService.saveBatch(list);
			}
		}
		return result;
	}
	
	/**  
	 * @MethodName: getDrillParams
	 * @Description: 获取下钻参数
	 * @author caiyang
	 * @param cellData
	 * @param drillAttrs
	 * @return 
	 * @return JSONObject
	 * @date 2023-05-09 08:38:28 
	 */  
	private JSONObject getDrillParams(Map<String, Object> cellData,String drillAttrs)
	{
		JSONObject result = null;
		if(StringUtil.isNotEmpty(drillAttrs) && cellData != null && !cellData.isEmpty())
		{
			result = new JSONObject();
			String[] attrs = drillAttrs.split(",");
			for (int i = 0; i < attrs.length; i++) {
				Object value = cellData.get(attrs[i]);
				if(value != null)
				{
					result.put(attrs[i], String.valueOf(value));
				}
			}
		}
		return result;
	}


	/**  
	 * @MethodName: getShareUrl
	 * @Description: 获取分享url
	 * @author caiyang
	 * @param shareDto
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.reporttpl.IReportTplService#getShareUrl(com.springreport.dto.reporttpl.ShareDto, com.springreport.base.UserInfoDto)
	 * @date 2023-06-25 02:07:05 
	 */
	@Override
	public ShareDto getShareUrl(ShareDto shareDto, UserInfoDto userInfoDto) {
		ShareDto result = new ShareDto();
		String shareCode = IdWorker.getIdStr();
		String shareUrl = "";
		String shareType = "电脑浏览器";
		if(YesNoEnum.YES.getCode().intValue() == shareDto.getIsShareForever().intValue()) {
			SysUser sysUser = iSysUserService.getById(userInfoDto.getUserId());
			if(sysUser == null) {
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist",new String[] {"用户信息"}));
			}
			String token = JWTUtil.sign(userInfoDto, sysUser.getPassword(),3153600000L);
			if(ShareTypeEnum.PC.getCode().intValue() == shareDto.getShareType().intValue())
			{//pc
				shareUrl = MessageUtil.getValue("showreport.share.url.pc");
			}else if(ShareTypeEnum.H5.getCode().intValue() == shareDto.getShareType().intValue()){
				//h5
				shareUrl = MessageUtil.getValue("showreport.share.url.h5");
				shareType = "手机浏览器";
			}else {
				throw new BizException(StatusCode.FAILURE, "分享类型传入错误");
			}
			shareUrl = shareUrl + "?tplId="+shareDto.getTplId()+"&token="+token;
			String shareMsg = MessageUtil.getValue("info.share.screen", new String[] {shareUrl,userInfoDto.getUserName(),DateUtil.getNow(),YesNoEnum.YES.getCode().intValue() == shareDto.getIsShareForever().intValue()?"永久有效":String.valueOf(shareDto.getShareTime())+"分钟"});
			result.setShareMsg(shareMsg);
		}else {
			if(ShareTypeEnum.PC.getCode().intValue() == shareDto.getShareType().intValue())
			{//pc
				shareUrl = MessageUtil.getValue("showreport.share.url.pc");
			}else if(ShareTypeEnum.H5.getCode().intValue() == shareDto.getShareType().intValue()){
				//h5
				shareUrl = MessageUtil.getValue("showreport.share.url.h5");
				shareType = "手机浏览器";
			}else {
				throw new BizException(StatusCode.FAILURE, "分享类型传入错误");
			}
			if(StringUtil.isNotEmpty(shareDto.getThirdPartyType())) {
				userInfoDto.setUserName(shareDto.getThirdPartyType());
				shareUrl = shareUrl + "?tplId="+shareDto.getTplId()+"&shareCode="+shareCode+"&shareUser="+shareDto.getThirdPartyType();
			}else {
				shareUrl = shareUrl + "?tplId="+shareDto.getTplId()+"&shareCode="+shareCode+"&shareUser="+userInfoDto.getUserId();
			}
			if(shareDto.getTplType().intValue() == 1)
			{
				String shareMsg = MessageUtil.getValue("info.share.showreport", new String[] {shareUrl,userInfoDto.getUserName(),DateUtil.getNow(),String.valueOf(shareDto.getShareTime()),shareType});
				result.setShareMsg(shareMsg);
			}else {
				String shareMsg = MessageUtil.getValue("info.share.submitreport", new String[] {shareUrl,userInfoDto.getUserName(),DateUtil.getNow(),String.valueOf(shareDto.getShareTime()),shareType,shareDto.getAllowReport().intValue() == 1?"是":"否"});
				result.setShareMsg(shareMsg);
			}
			redisUtil.set(RedisPrefixEnum.SHAREREPORT.getCode()+shareCode, shareDto.getAllowReport(), shareDto.getShareTime()*60);
		}
		
		return result;
	}


	@Override
	public MobilePreviewDto getMobileInfo(MesGenerateReportDto mesGenerateReportDto, UserInfoDto userInfoDto) throws Exception {
		MobilePreviewDto result = new MobilePreviewDto();
		ReportTpl reportTpl = this.getById(mesGenerateReportDto.getTplId());
		if (reportTpl == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"报表模板"}));
		}
		mesGenerateReportDto.setSheetIndex(null);
		mesGenerateReportDto.setIsMobile(YesNoEnum.YES.getCode());
		ResPreviewData resPreviewData = this.generateLuckySheetReportData(mesGenerateReportDto,true,userInfoDto,reportTpl,true);
		MesExportExcel mesExportExcel = new MesExportExcel();
		if(!ListUtil.isEmpty(resPreviewData.getSheetDatas()))
		{
			List<ResMobileReport> reports = new ArrayList<ResMobileReport>();
			for (int i = 0; i < resPreviewData.getSheetDatas().size(); i++) {
				List<MesSheetConfig> sheetConfigs = new ArrayList<MesSheetConfig>();
				MesSheetConfig mesSheetConfig = new MesSheetConfig();
				Object borderInfos = null;
				Map<String, Object> rowlen = null;
				Map<String, Object> columnlen = null;
				if(resPreviewData.getSheetDatas().get(i).getConfig() != null)
				{
					borderInfos = resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.BORDERINFO.getCode());
					rowlen = (Map<String, Object>) resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.ROWLEN.getCode());//行高
					columnlen = (Map<String, Object>) resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.COLUMNLEN.getCode());//列宽
				}
				List<Object> borderInfoList = null;
				if(borderInfos != null)
				{
					borderInfoList = (List<Object>) borderInfos;
				}
				mesSheetConfig.setCellDatas(resPreviewData.getSheetDatas().get(i).getCellDatas());
				mesSheetConfig.setSheetname(resPreviewData.getSheetDatas().get(i).getSheetName());
				mesSheetConfig.setBorderInfos(borderInfoList);
				mesSheetConfig.setRowlen(rowlen);
				mesSheetConfig.setColumnlen(columnlen);
				mesSheetConfig.setFrozen(JSONObject.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getFrozen())));
				mesSheetConfig.setImages(JSONObject.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getImages())));
				mesSheetConfig.setHyperlinks(resPreviewData.getSheetDatas().get(i).getHyperlinks());
				mesSheetConfig.setBase64Images(resPreviewData.getSheetDatas().get(i).getBase64Imgs());
				mesSheetConfig.setImageDatas(resPreviewData.getSheetDatas().get(i).getImageDatas());
				mesSheetConfig.setChart(resPreviewData.getSheetDatas().get(i).getChart());
				mesSheetConfig.setChartCells(resPreviewData.getSheetDatas().get(i).getChartCells());
				if(resPreviewData.getSheetDatas().get(i).getMaxXAndY() != null)
				{
					mesSheetConfig.setMaxXAndY(resPreviewData.getSheetDatas().get(i).getMaxXAndY());	
				}else {
					Map<String, Integer> maxXAndY = new HashMap<>();
					maxXAndY.put("maxX", 50);
					maxXAndY.put("maxY", 26);
					mesSheetConfig.setMaxXAndY(maxXAndY);	
				}
				sheetConfigs.add(mesSheetConfig);
				mesExportExcel.setSheetConfigs(sheetConfigs);
				mesExportExcel.setImageInfos(new HashMap<>());
				mesExportExcel.setBackImages(new HashMap<>());
				ByteArrayInputStream is =ReportExcelUtil.getExcelStream(mesExportExcel,resPreviewData.getTplName(),"",1);
				List<ExcelObject> objects = new ArrayList<ExcelObject>();
				int x = resPreviewData.getSheetDatas().get(i).getStartXAndY().get("x")==null?0:resPreviewData.getSheetDatas().get(i).getStartXAndY().get("x");
				int y = resPreviewData.getSheetDatas().get(i).getStartXAndY().get("y")==null?0:resPreviewData.getSheetDatas().get(i).getStartXAndY().get("y");
				int endx = resPreviewData.getSheetDatas().get(i).getMaxXAndY().get("maxX")==null?0:resPreviewData.getSheetDatas().get(i).getMaxXAndY().get("maxX");
				int endy = resPreviewData.getSheetDatas().get(i).getMaxXAndY().get("maxY")==null?0:resPreviewData.getSheetDatas().get(i).getMaxXAndY().get("maxY");
				JSONObject colhidden = resPreviewData.getSheetDatas().get(i).getColhidden() == null?new JSONObject():resPreviewData.getSheetDatas().get(i).getColhidden();
				JSONObject rowhidden = resPreviewData.getSheetDatas().get(i).getRowhidden() == null?new JSONObject():resPreviewData.getSheetDatas().get(i).getRowhidden();
				PrintSettingsDto printSettingsDto = new PrintSettingsDto();
				if(resPreviewData.getSheetDatas().get(i).getPrintSettings() != null)
				{
					BeanUtils.copyProperties(resPreviewData.getSheetDatas().get(i).getPrintSettings(), printSettingsDto);
				}
				printSettingsDto.setAuthor(userInfoDto.getUserName());
				printSettingsDto.setTitle(reportTpl.getTplName());
				printSettingsDto.setSubject(resPreviewData.getSheetDatas().get(i).getSheetName());
				printSettingsDto.setKeyWords("SpringReport_2.1.0");
				printSettingsDto.setHorizontalPage(resPreviewData.getSheetDatas().get(i).getPrintSettings()!=null?resPreviewData.getSheetDatas().get(i).getPrintSettings().getHorizontalPage():2);
				printSettingsDto.setPageDivider(resPreviewData.getSheetDatas().get(i).getPageDivider());
				objects.add(new ExcelObject("excel",is,x,y,endx,endy,mesGenerateReportDto.getPdfType(),colhidden,rowhidden,resPreviewData.getSheetDatas().get(i).getXxbtScreenshot(),printSettingsDto,mesExportExcel.getImageInfos(),mesExportExcel.getBackImages(),mesExportExcel.getSheetConfigs().get(0).getWrapText()));
				Excel2Pdf pdf = new  Excel2Pdf(objects, null);
				ResMobileInfos resMobileInfos = pdf.getMobileInfos();
				ResMobileReport resMobileReport = new ResMobileReport();
				resMobileReport.setSheetIndex(resPreviewData.getSheetDatas().get(i).getSheetIndex());
				resMobileReport.setSheetName(resPreviewData.getSheetDatas().get(i).getSheetName());
				resMobileReport.setMergePagination(resPreviewData.getSheetDatas().get(i).getMergePagination());
//				List<ImageInfo> imageInfos =  JFreeChartUtil.getChartInfos(resPreviewData.getSheetDatas().get(i).getChart());
//				resMobileReport.setImageInfos(imageInfos);
				List<JSONObject> chartsOptions = null;
				if(!ListUtil.isEmpty(resPreviewData.getSheetDatas().get(i).getChart()))
				{
					chartsOptions = new ArrayList<>();
					for (int j = 0; j < resPreviewData.getSheetDatas().get(i).getChart().size(); j++) {
						JSONObject options = resPreviewData.getSheetDatas().get(i).getChart().getJSONObject(j).getJSONObject("chartOptions").getJSONObject("defaultOption");
						chartsOptions.add(options);
					}
				}
				Map<String, String> imgs = null;
				if(!ListUtil.isEmpty(resPreviewData.getSheetDatas().get(i).getImageDatas()))
				{
					imgs = new HashMap<>();
					for (int j = 0; j < resPreviewData.getSheetDatas().get(i).getImageDatas().size(); j++) {
						JSONObject img = resPreviewData.getSheetDatas().get(i).getImageDatas().get(j);
						String r = img.getString("r");
						String c = img.getString("c");
						String key = r+"_"+c;
						JSONObject imgInfo = img.getJSONObject("imgInfo");
						String src = imgInfo.getString("src");
						imgs.put(key, src);
					}
				}
				resMobileReport.setChartsOptions(chartsOptions);
				String tableHtml = this.buildTables(x,y,endx, endy, resMobileInfos.getTableCells(),resMobileInfos.getMergeCells(),colhidden,rowhidden,resMobileInfos.getEmptyRows(),imgs,resPreviewData.getSheetDatas().get(i).getXxbtScreenshot());
				resMobileReport.setImages(imgs);
				resMobileReport.setTable(tableHtml);
				resMobileReport.setDrillCells(resPreviewData.getSheetDatas().get(i).getDrillCells());
				reports.add(resMobileReport);
			}
			result.setReports(reports);
		}
		result.setReportName(resPreviewData.getTplName());
		result.setPagination(resPreviewData.getPagination());
		return result;
	}
	private String buildTables(int startx,int starty,int endx,int endy,List<List<TableCell>> tableCells,Map<String, Cell> mergeCells,JSONObject colhidden,JSONObject rowhidden,Set<Integer> emptyRows,Map<String, String> imgs,JSONObject xxbtScreenshot) {
		String result = "";
		if(!ListUtil.isEmpty(tableCells))
		{
			Map<Integer, Map<String, String>> emptyCells = this.buildAllEmptyCells(startx,starty,endx,endy,tableCells,mergeCells,colhidden,rowhidden,emptyRows);
			for (int i = 0; i < tableCells.size(); i++) {
				List<TableCell> rowCells = tableCells.get(i);
				for (int j = 0; j < rowCells.size(); j++) {
					int x = rowCells.get(j).getX();
					int y = rowCells.get(j).getY();
					Map<String, String> rowTDs = emptyCells.get(rowCells.get(j).getX());
					if(rowTDs != null)
					{
						if(colhidden.get(String.valueOf(rowCells.get(j).getY())) == null)
						{
							String td = this.buildCellHtml(rowCells.get(j),imgs,xxbtScreenshot);
							rowTDs.put(x+"-"+y, td);	
						}
					}
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append("<table id='table' class='reportTable'");
			Iterator iterator = emptyCells.entrySet().iterator();
			while(iterator.hasNext())
			{
				Map.Entry entry = (Map.Entry) iterator.next(); 
				Map<String, String> rowMap = (Map<String, String>) entry.getValue();
				Iterator rowIterator = rowMap.entrySet().iterator();
				sb.append("<tr>");
				while(rowIterator.hasNext())
				{
					Map.Entry td = (Map.Entry) rowIterator.next(); 
					sb.append(td.getValue().toString());
				}
				sb.append("</tr>");
			}
			sb.append("</table>");
			result = sb.toString();
		}
		return result;
	}
	
	private Map<Integer, Map<String, String>> buildAllEmptyCells(int startx,int starty,int endx,int endy,List<List<TableCell>> tableCells,Map<String, Cell> mergeCells,JSONObject colhidden,JSONObject rowhidden,Set<Integer> emptyRows){
		Map<Integer, Map<String, String>> result = new LinkedHashMap<Integer, Map<String,String>>();
		for (int i = startx; i <= endx; i++) {
			if(rowhidden.get(String.valueOf(i)) != null || emptyRows.contains(i))
			{
				continue;
			}
			Map<String, String> emptyTd = new LinkedHashMap<String, String>();
			for (int j = starty; j <= endy; j++) {
				if(!mergeCells.containsKey(i+"_"+j) && colhidden.get(String.valueOf(j)) == null)
				{
					StringBuffer td = new StringBuffer();
					td.append("<td>").append("</td>");
					emptyTd.put(i+"-"+j, td.toString());
				}
			}
			result.put(i, emptyTd);
		}
		return result;
	}
	
	private String buildCellHtml(TableCell tableCell,Map<String, String> imgs,JSONObject xxbtScreenshot) {
		StringBuffer td = new StringBuffer();
		StringBuffer style = new StringBuffer();
		String id = tableCell.getX() + "_" + tableCell.getY();
		td.append("<td id='").append(id).append("'").append(" class='").append(id).append("'");
		if (YesNoEnum.YES.getCode().intValue() == tableCell.getIsMerge().intValue()) {
			td.append(" rowspan="+tableCell.getRowSpan()+" colspan="+tableCell.getColSpan()+"");
		}
		if(StringUtil.isNotEmpty(tableCell.getBackgroundColor()))
		{
			td.append(" bgcolor='"+tableCell.getBackgroundColor()+"'");
		}
		if (StringUtil.isNotEmpty(tableCell.getFontSize())) {
			style.append("font-size:"+tableCell.getFontSize()+"pt");
		}
		if(StringUtil.isNotEmpty(tableCell.getFontColor()))
		{
			if(StringUtil.isNotEmpty(style.toString()))
			{
				style.append(";color:"+tableCell.getFontColor());
			}else {
				style.append("color:"+tableCell.getFontColor());
			}
		}
		if(tableCell.isBold())
		{
			if(StringUtil.isNotEmpty(style.toString()))
			{
				style.append(";font-weight:bold");
			}else {
				style.append("font-weight:bold");
			}
		}
		if(tableCell.isItalic())
		{
			if(StringUtil.isNotEmpty(style.toString()))
			{
				style.append(";font-style:italic");
			}else {
				style.append("font-style:italic");
			}
		}
		if(tableCell.getUnderLine() == 1)
		{
			if(StringUtil.isNotEmpty(style.toString()))
			{
				style.append(";text-decoration:underline");
			}else {
				style.append("text-decoration:none");
			}
		}
//		水平对齐方式 0左对齐 1居中对齐 2右对齐
		if(tableCell.getHorizontalAlignment().intValue() == 0)
		{
			td.append(" align='left'");
		}else if(tableCell.getHorizontalAlignment().intValue() == 1)
		{
			td.append(" align='center'");
		}else if(tableCell.getHorizontalAlignment().intValue() == 2)
		{
			td.append(" align='right'");
		}else
		{
			td.append(" align='left'");
		}
//		垂直对齐方式 6下方对齐 5中间对齐 4 顶端对齐
		if(tableCell.getVerticalAlignment().intValue() == 5)
		{
			td.append(" valign='center'");
		}else if(tableCell.getVerticalAlignment().intValue() == 4)
		{
			td.append(" valign='top'");
		}else if(tableCell.getVerticalAlignment().intValue() == 6)
		{
			td.append(" valign='bottom'");
		}else {
			td.append(" valign='center'");
		}
		td.append(">");
		if(imgs!=null && imgs.containsKey(id))
		{
			if(style.toString().contains("text-decoration"))
			{
				td.append("图片");
			}else {
				td.append("<ins class='").append(id).append("'>").append("图片").append("</ins>");
			}
			
		}else {
			if(xxbtScreenshot != null && xxbtScreenshot.containsKey(id+"_screenshot")) {
				td.append("<img class='xxbt_img' src='").append(xxbtScreenshot.getString(id+"_screenshot")).append("'>").append("</img>");
			}else {
				td.append(tableCell.getCellValue());
			}
		}
		td.append("</td>");
		return td.toString();
	}
	
	private Object processUnitTransfer(Object value,LuckySheetBindData bindData) {
		Object result = null;
		try {
			if(value != null && CheckUtil.isNumber(String.valueOf(value)))
			{
				BigDecimal data = new BigDecimal(String.valueOf(value));
				int transferType = bindData.getTransferType().intValue();
				switch (transferType) {
				case 1://乘法
					data = data.multiply(new BigDecimal(bindData.getMultiple()));
					result = data.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
					break;
				case 2://除法
					data = data.divide(new BigDecimal(bindData.getMultiple()));
					result = data.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
					break;
				case 3://乘法并转换成中文大写
					data = data.multiply(new BigDecimal(bindData.getMultiple()));
//					result = data.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
					result = Convert.digitToChinese(data.setScale(bindData.getDigit(), RoundingMode.HALF_UP));
					break;
				case 4://除法并转换成中文大写
					data = data.divide(new BigDecimal(bindData.getMultiple()));
//					result = data.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
					result = Convert.digitToChinese(data.setScale(bindData.getDigit(), RoundingMode.HALF_UP));
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result == null)
		{
			result = value;
		}
		return result;
	}


	/**  
	 * @MethodName: reportTask
	 * @Description: 报表导出定时任务
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param userInfoDto
	 * @throws Exception
	 * @see com.springreport.api.reporttpl.IReportTplService#reportTask(com.springreport.dto.reporttpl.MesGenerateReportDto, com.springreport.base.UserInfoDto)
	 * @date 2023-07-28 08:57:21 
	 */
	@Override
	public void reportTask(MesGenerateReportDto mesGenerateReportDto, UserInfoDto userInfoDto) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		ReportTpl reportTpl = this.getById(mesGenerateReportDto.getTplId());
		if (reportTpl == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"报表模板"}));
		}
		mesGenerateReportDto.setIsMobile(YesNoEnum.YES.getCode());
		mesGenerateReportDto.setIsTask(YesNoEnum.YES.getCode());
		ResPreviewData resPreviewData = this.generateLuckySheetReportData(mesGenerateReportDto,false,userInfoDto,reportTpl,true);
		MesExportExcel mesExportExcel = new MesExportExcel();
		List<MesSheetConfig> sheetConfigs = new ArrayList<MesSheetConfig>();
		if(!ListUtil.isEmpty(resPreviewData.getSheetDatas()))
		{
			List<EmailAttachementDto> attachments = new ArrayList<>();
			for (int i = 0; i < resPreviewData.getSheetDatas().size(); i++) {
				MesSheetConfig mesSheetConfig = new MesSheetConfig();
				Object borderInfos = null;
				Map<String, Object> rowlen = null;
				Map<String, Object> columnlen = null;
				if(resPreviewData.getSheetDatas().get(i).getConfig() != null)
				{
					borderInfos = resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.BORDERINFO.getCode());
					rowlen = (Map<String, Object>) resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.ROWLEN.getCode());//行高
					columnlen = (Map<String, Object>) resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.COLUMNLEN.getCode());//列宽
				}
				List<Object> borderInfoList = null;
				if(borderInfos != null)
				{
					borderInfoList = (List<Object>) borderInfos;
				}
				mesSheetConfig.setCellDatas(resPreviewData.getSheetDatas().get(i).getCellDatas());
				mesSheetConfig.setSheetname(resPreviewData.getSheetDatas().get(i).getSheetName());
				mesSheetConfig.setBorderInfos(borderInfoList);
				mesSheetConfig.setRowlen(rowlen);
				mesSheetConfig.setColumnlen(columnlen);
				mesSheetConfig.setFrozen(JSONObject.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getFrozen())));
				mesSheetConfig.setImages(JSONObject.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getImages())));
				mesSheetConfig.setHyperlinks(resPreviewData.getSheetDatas().get(i).getHyperlinks());
				mesSheetConfig.setBase64Images(resPreviewData.getSheetDatas().get(i).getBase64Imgs());
				mesSheetConfig.setImageDatas(resPreviewData.getSheetDatas().get(i).getImageDatas());
				mesSheetConfig.setChart(resPreviewData.getSheetDatas().get(i).getChart());
				mesSheetConfig.setChartCells(resPreviewData.getSheetDatas().get(i).getChartCells());
				if(resPreviewData.getSheetDatas().get(i).getMaxXAndY() != null)
				{
					mesSheetConfig.setMaxXAndY(resPreviewData.getSheetDatas().get(i).getMaxXAndY());	
				}else {
					Map<String, Integer> maxXAndY = new HashMap<>();
					maxXAndY.put("maxX", 50);
					maxXAndY.put("maxY", 26);
					mesSheetConfig.setMaxXAndY(maxXAndY);	
				}
				sheetConfigs.add(mesSheetConfig);
			}
			mesExportExcel.setPassword(mesGenerateReportDto.getPassword());
			mesExportExcel.setSheetConfigs(sheetConfigs);
			mesExportExcel.setChartsBase64(mesGenerateReportDto.getChartsBase64());
			mesExportExcel.setImageInfos(new HashMap<>());
			mesExportExcel.setBackImages(new HashMap<>());
			ByteArrayInputStream is =ReportExcelUtil.getExcelStream(mesExportExcel,reportTpl.getTplName(),"",1);
			ByteArrayOutputStream baos = FileUtil.cloneInputStream(is);
			ByteArrayInputStream is2 =ReportExcelUtil.getExcelStream(mesExportExcel,reportTpl.getTplName(),"",2);
			ByteArrayOutputStream baos2 = FileUtil.cloneInputStream(is2);
			if(mesGenerateReportDto.getExportType() == 1 || mesGenerateReportDto.getExportType() == 3)
			{
				EmailAttachementDto excel = new EmailAttachementDto();
				excel.setFileName(resPreviewData.getTplName()+".xlsx");
				excel.setIs(new ByteArrayInputStream(baos2.toByteArray()));
				attachments.add(excel);
			}
			if(mesGenerateReportDto.getExportType() == 2 || mesGenerateReportDto.getExportType() == 3)
			{
				for (int i = 0; i < resPreviewData.getSheetDatas().size(); i++) {
					int x = resPreviewData.getSheetDatas().get(i).getStartXAndY().get("x")==null?0:resPreviewData.getSheetDatas().get(i).getStartXAndY().get("x");
					int y = resPreviewData.getSheetDatas().get(i).getStartXAndY().get("y")==null?0:resPreviewData.getSheetDatas().get(i).getStartXAndY().get("y");
					int endx = resPreviewData.getSheetDatas().get(i).getMaxXAndY().get("maxX")==null?0:resPreviewData.getSheetDatas().get(i).getMaxXAndY().get("maxX");
					int endy = resPreviewData.getSheetDatas().get(i).getMaxXAndY().get("maxY")==null?0:resPreviewData.getSheetDatas().get(i).getMaxXAndY().get("maxY");
					JSONObject colhidden = resPreviewData.getSheetDatas().get(i).getColhidden() == null?new JSONObject():resPreviewData.getSheetDatas().get(i).getColhidden();
					JSONObject rowhidden = resPreviewData.getSheetDatas().get(i).getRowhidden() == null?new JSONObject():resPreviewData.getSheetDatas().get(i).getRowhidden();
					List<ExcelObject> objects = new ArrayList<ExcelObject>();
					PrintSettingsDto printSettingsDto = new PrintSettingsDto();
					if(resPreviewData.getSheetDatas().get(i).getPrintSettings() != null)
					{
						BeanUtils.copyProperties(resPreviewData.getSheetDatas().get(i).getPrintSettings(), printSettingsDto);
					}
					printSettingsDto.setTitle(reportTpl.getTplName());
					printSettingsDto.setSubject(resPreviewData.getSheetDatas().get(i).getSheetName());
					printSettingsDto.setKeyWords("SpringReport_2.1.0");
					printSettingsDto.setHorizontalPage(resPreviewData.getSheetDatas().get(i).getPrintSettings() == null?2:resPreviewData.getSheetDatas().get(i).getPrintSettings().getHorizontalPage());
					printSettingsDto.setPageDivider(resPreviewData.getSheetDatas().get(i).getPageDivider());
					objects.add(new ExcelObject("excel",new ByteArrayInputStream(baos.toByteArray()),x,y,endx,endy,mesGenerateReportDto.getPdfType(),colhidden,rowhidden,i,resPreviewData.getSheetDatas().get(i).getXxbtScreenshot(),printSettingsDto,mesExportExcel.getImageInfos(),mesExportExcel.getBackImages(),mesExportExcel.getSheetConfigs().get(i).getWrapText()));
					ByteArrayOutputStream pdfbaos = new ByteArrayOutputStream();
					Excel2Pdf pdf = new Excel2Pdf(objects, pdfbaos);
					pdf.convert();
					byte[] buffer = pdfbaos.toByteArray();
					InputStream pdfIs = new ByteArrayInputStream(buffer);
					EmailAttachementDto pdfattach = new EmailAttachementDto();
					pdfattach.setFileName(resPreviewData.getTplName()+"_"+resPreviewData.getSheetDatas().get(i).getSheetName()+".pdf");
					pdfattach.setIs(pdfIs);
					attachments.add(pdfattach);
				}
			}
			if(!ListUtil.isEmpty(attachments))
			{
				String subject = "定时任务【"+mesGenerateReportDto.getJobName()+"】";
				String content = "附件中为定时任务【"+mesGenerateReportDto.getJobName()+"】的执行结果。";
				sendEmailUtil.sendMessage(mesGenerateReportDto.getSendEmail(), subject, content, attachments);
			}
		}
	}
	
	private void isHiddenCol(LuckysheetReportCell luckysheetReportCell,Map<String, Object> colhidden,Map<String, Object> params) {
		int y = luckysheetReportCell.getCoordsy();
		if(!colhidden.containsKey(String.valueOf(y)))
		{
			if(YesNoEnum.YES.getCode().intValue() == luckysheetReportCell.getIsHiddenConditions().intValue())
			{
				String hiddenType = luckysheetReportCell.getHiddenType();
				JSONArray conditions = JSONArray.parseArray(luckysheetReportCell.getHiddenConditions());
				if(!ListUtil.isEmpty(conditions))
				{
					boolean flag = ListUtil.filterDatas(conditions, params, hiddenType);
					if(flag)
					{
						colhidden.put(String.valueOf(y), 0);
					}
				}
			}
		}
	}


	/**  
	 * @MethodName: getSheetPdfStream
	 * @Description: 获取pdf文件流
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param userInfoDto
	 * @param httpServletResponse
	 * @throws Exception
	 * @see com.springreport.api.reporttpl.IReportTplService#getSheetPdfStream(com.springreport.dto.reporttpl.MesGenerateReportDto, com.springreport.base.UserInfoDto, javax.servlet.http.HttpServletResponse)
	 * @date 2023-12-03 08:17:06 
	 */
	@Override
	public void getSheetPdfStream(MesGenerateReportDto mesGenerateReportDto, UserInfoDto userInfoDto,
			HttpServletResponse httpServletResponse) throws Exception {
		ReportTpl reportTpl = this.getById(mesGenerateReportDto.getTplId());
		if (reportTpl == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"报表模板"}));
		}
		ResPreviewData resPreviewData = null;
//		if(reportTpl.getTplType().intValue() == 1)
//		{
			resPreviewData = this.generateLuckySheetReportData(mesGenerateReportDto,mesGenerateReportDto.isPatination(),userInfoDto,reportTpl,true);
//		}else {
//			resPreviewData = this.iReportTplFormsService.previewLuckysheetReportFormsData(mesGenerateReportDto, userInfoDto, reportTpl,mesGenerateReportDto.isPatination());
//		}
		MesExportExcel mesExportExcel = new MesExportExcel();
		List<MesSheetConfig> sheetConfigs = new ArrayList<MesSheetConfig>();
		if(!ListUtil.isEmpty(resPreviewData.getSheetDatas()))
		{
			for (int i = 0; i < resPreviewData.getSheetDatas().size(); i++) {
				MesSheetConfig mesSheetConfig = new MesSheetConfig();
				Object borderInfos = null;
				Map<String, Object> rowlen = null;
				Map<String, Object> columnlen = null;
				if(resPreviewData.getSheetDatas().get(i).getConfig() != null)
				{
					borderInfos = resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.BORDERINFO.getCode());
					rowlen = (Map<String, Object>) resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.ROWLEN.getCode());//行高
					columnlen = (Map<String, Object>) resPreviewData.getSheetDatas().get(i).getConfig().get(LuckySheetPropsEnum.COLUMNLEN.getCode());//列宽
				}
				List<Object> borderInfoList = null;
				if(borderInfos != null)
				{
					borderInfoList = (List<Object>) borderInfos;
				}
				mesSheetConfig.setCellDatas(resPreviewData.getSheetDatas().get(i).getCellDatas());
				mesSheetConfig.setSheetname(resPreviewData.getSheetDatas().get(i).getSheetName());
				mesSheetConfig.setBorderInfos(borderInfoList);
				mesSheetConfig.setRowlen(rowlen);
				mesSheetConfig.setColumnlen(columnlen);
				mesSheetConfig.setFrozen(JSONObject.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getFrozen())));
				mesSheetConfig.setImages(JSONObject.parseObject(JSON.toJSONString(resPreviewData.getSheetDatas().get(i).getImages())));
				mesSheetConfig.setHyperlinks(resPreviewData.getSheetDatas().get(i).getHyperlinks());
				mesSheetConfig.setBase64Images(resPreviewData.getSheetDatas().get(i).getBase64Imgs());
				mesSheetConfig.setImageDatas(resPreviewData.getSheetDatas().get(i).getImageDatas());
				mesSheetConfig.setChart(resPreviewData.getSheetDatas().get(i).getChart());
				mesSheetConfig.setChartCells(resPreviewData.getSheetDatas().get(i).getChartCells());
				if(resPreviewData.getSheetDatas().get(i).getMaxXAndY() != null)
				{
					mesSheetConfig.setMaxXAndY(resPreviewData.getSheetDatas().get(i).getMaxXAndY());	
				}else {
					Map<String, Integer> maxXAndY = new HashMap<>();
					maxXAndY.put("maxX", 50);
					maxXAndY.put("maxY", 26);
					mesSheetConfig.setMaxXAndY(maxXAndY);	
				}
				sheetConfigs.add(mesSheetConfig);
			}
			mesExportExcel.setPassword(mesGenerateReportDto.getPassword());
			mesExportExcel.setSheetConfigs(sheetConfigs);
			mesExportExcel.setChartsBase64(mesGenerateReportDto.getChartsBase64());
			mesExportExcel.setImageInfos(new HashMap<>());
			mesExportExcel.setBackImages(new HashMap<>());
			ByteArrayInputStream is =ReportExcelUtil.getExcelStream(mesExportExcel,reportTpl.getTplName(),"",1);
			List<ExcelObject> objects = new ArrayList<ExcelObject>();
			int x = resPreviewData.getSheetDatas().get(0).getStartXAndY().get("x")==null?0:resPreviewData.getSheetDatas().get(0).getStartXAndY().get("x");
			int y = resPreviewData.getSheetDatas().get(0).getStartXAndY().get("y")==null?0:resPreviewData.getSheetDatas().get(0).getStartXAndY().get("y");
			int endx = resPreviewData.getSheetDatas().get(0).getMaxXAndY().get("maxX")==null?0:resPreviewData.getSheetDatas().get(0).getMaxXAndY().get("maxX");
			int endy = resPreviewData.getSheetDatas().get(0).getMaxXAndY().get("maxY")==null?0:resPreviewData.getSheetDatas().get(0).getMaxXAndY().get("maxY");
			JSONObject colhidden = resPreviewData.getSheetDatas().get(0).getColhidden() == null?new JSONObject():resPreviewData.getSheetDatas().get(0).getColhidden();
			JSONObject rowhidden = resPreviewData.getSheetDatas().get(0).getRowhidden() == null?new JSONObject():resPreviewData.getSheetDatas().get(0).getRowhidden();
			PrintSettingsDto printSettingsDto = new PrintSettingsDto();
			if(resPreviewData.getSheetDatas().get(0).getPrintSettings() != null)
			{
				BeanUtils.copyProperties(resPreviewData.getSheetDatas().get(0).getPrintSettings(), printSettingsDto);
			}
			printSettingsDto.setAuthor(userInfoDto.getUserName());
			printSettingsDto.setTitle(reportTpl.getTplName());
			printSettingsDto.setSubject(resPreviewData.getSheetDatas().get(0).getSheetName());
			printSettingsDto.setKeyWords("SpringReport_2.1.0");
			printSettingsDto.setHorizontalPage(resPreviewData.getSheetDatas().get(0).getPrintSettings() == null?2:resPreviewData.getSheetDatas().get(0).getPrintSettings().getHorizontalPage());
			printSettingsDto.setPageDivider(resPreviewData.getSheetDatas().get(0).getPageDivider());
			if(mesGenerateReportDto.isPatination()) {
				if(YesNoEnum.YES.getCode().intValue() == mesGenerateReportDto.getIsCustomerPage().intValue()) {
					printSettingsDto.setStartPage(mesGenerateReportDto.getStartPage());
				}else {
					printSettingsDto.setStartPage(Integer.valueOf(String.valueOf(mesGenerateReportDto.getPagination().get("currentPage"))));
				}
			}
			objects.add(new ExcelObject("excel",is,x,y,endx,endy,mesGenerateReportDto.getPdfType(),colhidden,rowhidden,resPreviewData.getSheetDatas().get(0).getXxbtScreenshot(),printSettingsDto,mesExportExcel.getImageInfos(),mesExportExcel.getBackImages(),mesExportExcel.getSheetConfigs().get(0).getWrapText()));
			httpServletResponse.setContentType("application/pdf");
	    	//设置文件名编码格式
	        String filename = URLEncoder.encode(resPreviewData.getSheetDatas().get(0).getSheetName(), "UTF-8");
	        httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" +filename + ".pdf");
	        httpServletResponse.addHeader("filename", filename + ".pdf");
			ServletOutputStream outputStream = httpServletResponse.getOutputStream();
			try {
				Excel2Pdf pdf = new Excel2Pdf(objects, outputStream);
				pdf.convert();
				outputStream.flush();
	            outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}finally {
				try {
					outputStream.close();
				} catch (Exception e2) {
					e2.printStackTrace();
					// TODO: handle exception
				}
			}
			
		}
	}


	/**  
	 * @MethodName: uploadReportTpl
	 * @Description: 上传报表模板(excel)
	 * @author caiyang
	 * @param file
	 * @param userInfoDto
	 * @return
	 * @throws Exception
	 * @see com.springreport.api.reporttpl.IReportTplService#uploadReportTpl(org.springframework.web.multipart.MultipartFile, com.springreport.base.UserInfoDto)
	 * @date 2023-12-13 10:23:53 
	 */
	@Override
	public JSONArray uploadReportTpl(MultipartFile file,long tplId,int isFormsReport, UserInfoDto userInfoDto) throws Exception {
		List<String> names = this.iReportTplSheetService.getAllSheetNames(tplId);
		JSONArray result = null;
		if(file.getOriginalFilename().endsWith(".xlsx")) {
			if(isFormsReport != 1) {
				List<String> sheetNames = DocumentToLuckysheetUtil.getSheetsName(file);
				if(!ListUtil.isEmpty(sheetNames))
				{
					for (int i = 0; i < sheetNames.size(); i++) {
						for (int j = 0; j < names.size(); j++) {
							if(sheetNames.get(i).equals(names.get(j)))
							{
								throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist",new String[] {"sheet名称【"+sheetNames.get(i)+"】"}));
							}
						}
					}
				}
			}
			result =  DocumentToLuckysheetUtil.xlsx2Luckysheet(file);
		}else if(file.getOriginalFilename().endsWith(".csv")){
			String sheetName = file.getOriginalFilename().replace(".csv", "");
			for (int j = 0; j < names.size(); j++) {
				if(sheetName.equals(names.get(j)))
				{
					throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist",new String[] {"sheet名称【"+sheetName+"】"}));
				}
			}
			result =  DocumentToLuckysheetUtil.csv2Luckysheet(file);
		}
		if(!ListUtil.isEmpty(result)) {
			for (int i = 0; i < result.size(); i++) {
				JSONObject sheetData = result.getJSONObject(i);
				//先处理图片数据，将图片上传到服务器
				JSONArray images = sheetData.getJSONArray("images");
				JSONObject imageData = new JSONObject();
				if(!ListUtil.isEmpty(images))
				{
					for (int j = 0; j < images.size(); j++) {
						JSONObject image = images.getJSONObject(j);
						byte[] imgData = image.getBytes("imgBytes");
						String imgType = image.getString("imgType");
						String fileName = IdWorker.getIdStr()+"."+imgType;
						Map<String, String> uploadResult = this.iCommonService.upload(imgData, fileName);
						image.put("src", uploadResult.get("fileUri"));
						image.remove("imgBytes");
						image.remove("imgType");
						imageData.put("img_"+IdWorker.get32UUID(), image);
					}
				}
				sheetData.put("images", imageData);
				sheetData.put("order", sheetData.getIntValue("order")+names.size());
				sheetData.put("load", "1");
			}
		}
		return result;
	}


	/**  
	 * @MethodName: getAllTpls
	 * @Description: 获取所有的模板
	 * @author caiyang
	 * @param model
	 * @return
	 * @see com.springreport.api.reporttpl.IReportTplService#getAllTpls(com.springreport.entity.reporttpl.ReportTpl)
	 * @date 2024-01-13 12:29:55 
	 */
	@Override
	public List<ReportTpl> getAllTpls(ReportTpl model) {
		QueryWrapper<ReportTpl> queryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTpl> result = this.list(queryWrapper);
		return result;
	}


	/**  
	 * @MethodName: getTplAuth
	 * @Description: 获取模板权限
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.reporttpl.IReportTplService#getTplAuth(com.springreport.entity.reporttpl.ReportTpl, com.springreport.base.UserInfoDto)
	 * @date 2024-03-07 06:31:35 
	 */
	@Override
	public JSONObject getTplAuth(ReportTpl model, UserInfoDto userInfoDto) {
		JSONObject sheetRangeAuth = new JSONObject();
		//获取用户对应的权限
		QueryWrapper<ReportRangeAuthUser> rangeAuthUserQueryWrapper = new QueryWrapper<>();
		rangeAuthUserQueryWrapper.eq("tpl_id", model.getId());
		rangeAuthUserQueryWrapper.eq("user_id", userInfoDto.getUserId());
		rangeAuthUserQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportRangeAuthUser> reportRangeAuthUsers = this.iReportRangeAuthUserService.list(rangeAuthUserQueryWrapper);
		if(ListUtil.isNotEmpty(reportRangeAuthUsers))
		{
			List<Long> authRangeIds = new ArrayList<>();
			for (int i = 0; i < reportRangeAuthUsers.size(); i++) {
				authRangeIds.add(reportRangeAuthUsers.get(i).getRangeAuthId());
			}
			//获取对应的权限操作范围
			QueryWrapper<ReportRangeAuth> queryWrapper = new QueryWrapper<>();
			queryWrapper.in("id", authRangeIds);
			List<ReportRangeAuth> reportRangeAuths = this.iReportRangeAuthService.list(queryWrapper);
			if(ListUtil.isNotEmpty(reportRangeAuths))
			{
				for (int i = 0; i < reportRangeAuths.size(); i++) {
					JSONObject rangeAxis = new JSONObject();
					rangeAxis.put("rangeAxis", reportRangeAuths.get(i).getRangeTxt());
					JSONObject range = new JSONObject();
					range.put("row", JSON.parseArray(reportRangeAuths.get(i).getRowsNo()));
					range.put("column", JSON.parseArray(reportRangeAuths.get(i).getColsNo()));
					rangeAxis.put("range", range);
					rangeAxis.put("sheetIndex", reportRangeAuths.get(i).getSheetIndex());
					if(!sheetRangeAuth.containsKey(reportRangeAuths.get(i).getSheetIndex())) {
						sheetRangeAuth.put(reportRangeAuths.get(i).getSheetIndex(), new JSONObject());
					}
					sheetRangeAuth.getJSONObject(reportRangeAuths.get(i).getSheetIndex()).put(reportRangeAuths.get(i).getRangeTxt(), rangeAxis);
				}
			}
		}
		return sheetRangeAuth;
	}
	
	/**  
	 * @MethodName: processConditionFormat
	 * @Description: 条件格式处理
	 * @author caiyang
	 * @param luckySheetBindData
	 * @param cellConditionFormat 单元格对应的条件格式
	 * @param x 计算后的横坐标
	 * @param y 计算后的纵坐标
	 * @param extend 扩展方向 1不扩展 2向下扩展 2向右扩展 
	 * @param isInit 是否是第一个计算的单元格
	 * void
	 * @date 2024-08-18 08:56:52 
	 */ 
	private void processConditionFormat(LuckySheetBindData luckySheetBindData,Map<String, JSONArray> cellConditionFormat,int x,int y,boolean isInit) {
		String conditonFormatKey = luckySheetBindData.getCoordsx() + "_" + luckySheetBindData.getCoordsy();
		if(!StringUtil.isEmptyMap(cellConditionFormat) && cellConditionFormat.containsKey(conditonFormatKey)) {
			for (int i = 0; i < cellConditionFormat.get(conditonFormatKey).size(); i++) {
				if(isInit) {
					JSONArray cellrange = new JSONArray();
					JSONObject range = new JSONObject();
					JSONArray row = new JSONArray();
					row.add(x);
					row.add(x);
					JSONArray column = new JSONArray();
					column.add(y);
					column.add(y);
					range.put("row", row);
					range.put("column", column);
					cellrange.add(range);
					cellConditionFormat.get(conditonFormatKey).getJSONObject(i).put("cellrange", cellrange);
				}else {
					JSONObject range = cellConditionFormat.get(conditonFormatKey).getJSONObject(i).getJSONArray("cellrange").getJSONObject(0);
					range.getJSONArray("row").set(1, x);
					range.getJSONArray("column").set(1, y);
				}
			}
		}
	}
	
	private void processDynamicRange(LuckySheetBindData luckySheetBindData,JSONObject dynamicRange,int x,int y,Map<String, Object> cellData) {
		if(dynamicRange == null) {
			return;
		}
		if(cellData.get("v") == null) {
			return;
		}
		Map<String, Object> v = (Map<String, Object>) cellData.get("v");
		if(v.get("tb") == null || !"2".equals(String.valueOf(v.get("tb")))) {
			return;
		}
		String key = luckySheetBindData.getCoordsx() + "_" + luckySheetBindData.getCoordsy();
		JSONObject range = null;
		if(dynamicRange.containsKey(key)) {
			range = dynamicRange.getJSONObject(key);
			range.put("edr", x);
			range.put("edc", y);
			String rangeValue = range.getString("value");
			String value = "";
			if(v.get("v") != null) {
				value = String.valueOf(v.get("v"));
			}
			if(value.length() > rangeValue.length()) {
				range.put("r", x);//内容长度最长的单元格x坐标
				range.put("c", y);//内容长度最长的单元格x坐标
				range.put("value", value);//内容长度最长的单元格x坐标
			}
		}else {
			range = new JSONObject();
			range.put("str", x);//起始行
			range.put("edr", x);//结束行
			range.put("stc", y);//起始列
			range.put("edc", y);//结束列
			range.put("r", x);//内容长度最长的单元格x坐标
			range.put("c", y);//内容长度最长的单元格x坐标
			String value = "";
			if(v.get("v") != null) {
				value = String.valueOf(v.get("v"));
			}
			range.put("value", value);//内容长度最长的单元格x坐标
			dynamicRange.put(key, range);
		}
	}
	
	/**  
	 * @MethodName: processCrossConditionFormat
	 * @Description: TODO
	 * @author caiyang
	 * @param luckySheetBindData
	 * @param cellConditionFormat
	 * @param x
	 * @param y
	 * @param j 对应的第几列数据
	 * @param isInit void
	 * @date 2024-08-18 05:37:06 
	 */ 
	private void processCrossConditionFormat(LuckySheetBindData luckySheetBindData,Map<String, JSONArray> cellConditionFormat,int x,int y,int j) {
		String conditonFormatKey = luckySheetBindData.getCoordsx() + "_" + luckySheetBindData.getCoordsy();
		String conditonFormatKey2 = conditonFormatKey + "_"+j;
		boolean isInit = false;
		if(!StringUtil.isEmptyMap(cellConditionFormat) && cellConditionFormat.containsKey(conditonFormatKey)) {
			if(!cellConditionFormat.containsKey(conditonFormatKey2)) {
				isInit = true;
				cellConditionFormat.put(conditonFormatKey2, JSONArray.parseArray(JSON.toJSONString(cellConditionFormat.get(conditonFormatKey))));
			}
			for (int i = 0; i < cellConditionFormat.get(conditonFormatKey2).size(); i++) {
				if(isInit) {
					JSONArray cellrange = new JSONArray();
					JSONObject range = new JSONObject();
					JSONArray row = new JSONArray();
					row.add(x);
					row.add(x);
					JSONArray column = new JSONArray();
					column.add(y);
					column.add(y);
					range.put("row", row);
					range.put("column", column);
					cellrange.add(range);
					cellConditionFormat.get(conditonFormatKey2).getJSONObject(i).put("cellrange", cellrange);
				}else {
					JSONObject range = cellConditionFormat.get(conditonFormatKey2).getJSONObject(i).getJSONArray("cellrange").getJSONObject(0);
					range.getJSONArray("row").set(1, x);
					range.getJSONArray("column").set(1, y);
				}
			}
		}
	}
	
	private void processCoverCell(ObjectMapper objectMapper,Map<String, Map<String, Object>> usedCells,LuckySheetBindData luckySheetBindData
			,List<Map<String, Object>> cellDatas,List<JSONObject> calcChain,Object dataRowLen,Map<String, Object> rowlen,Object dataColLen
			,Map<String, Object> columnlen,List<Map<String, Object>> border,Map<String, Integer> maxXAndY,Map<String, Integer> maxCoordinate,
			Map<String, Object> borderInfo) throws JsonMappingException, JsonProcessingException {
		//覆盖
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		Map<String, Object> cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
		if(usedCells.containsKey(luckySheetBindData.getCoordsx()+"_"+luckySheetBindData.getCoordsy())) {
			if(usedCells.get(luckySheetBindData.getCoordsx()+"_"+luckySheetBindData.getCoordsy()) != null) {
				usedCells.get(luckySheetBindData.getCoordsx()+"_"+luckySheetBindData.getCoordsy()).clear();
				usedCells.get(luckySheetBindData.getCoordsx()+"_"+luckySheetBindData.getCoordsy()).putAll(cellData);
			}else {
				cellDatas.add(cellData);
				usedCells.put(luckySheetBindData.getCoordsx()+"_"+luckySheetBindData.getCoordsy(), cellData);
			}
		}else {
			cellDatas.add(cellData);
			usedCells.put(luckySheetBindData.getCoordsx()+"_"+luckySheetBindData.getCoordsy(), cellData);
		}
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue())
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("r", luckySheetBindData.getCoordsx());
			jsonObject.put("c", luckySheetBindData.getCoordsy());
			calcChain.add(jsonObject);
		}
		if(dataRowLen != null)
		{
			if(rowlen.get(String.valueOf(luckySheetBindData.getCoordsx())) == null)
			{
				rowlen.put(String.valueOf(luckySheetBindData.getCoordsx()), dataRowLen);
			}
		}
		if(dataColLen != null)
		{
			if(columnlen.get(String.valueOf(luckySheetBindData.getCoordsy())) == null)
			{
				columnlen.put(String.valueOf(luckySheetBindData.getCoordsy()), dataColLen);
			}
		}
		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
		this.borderProcess(border, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsx()+luckySheetBindData.getRowSpan()-1, luckySheetBindData.getCoordsy(), luckySheetBindData.getCoordsy()+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
		if((luckySheetBindData.getCoordsx() + luckySheetBindData.getRowSpan()-1)> maxX)
		{
			maxX = luckySheetBindData.getCoordsx() + luckySheetBindData.getRowSpan()-1;
		}
		if((luckySheetBindData.getCoordsx() + luckySheetBindData.getColSpan()-1)> maxY)
		{
			maxY = luckySheetBindData.getCoordsx() + luckySheetBindData.getColSpan()-1;
		}
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge()) {
			for (int t = 0; t < luckySheetBindData.getRowSpan(); t++) {
				for (int j = 0; j < luckySheetBindData.getColSpan(); j++) {
					if(t != 0 || j != 0) {
						Map<String, Object> mc = new HashMap<String, Object>();
						mc.put(LuckySheetPropsEnum.R.getCode(), luckySheetBindData.getCoordsx());
						mc.put(LuckySheetPropsEnum.C.getCode(), luckySheetBindData.getCoordsy());
						Map<String, Object> cellValue = new HashMap<String, Object>();
						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
						Map<String, Object> mergeCellData = new HashMap<String, Object>();
						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), luckySheetBindData.getCoordsx()+t);
						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), luckySheetBindData.getCoordsy()+j);
						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
						String key = (luckySheetBindData.getCoordsx()+t) + "_" + (luckySheetBindData.getCoordsy()+j);
						if(usedCells.containsKey(key)) {
							if(usedCells.get(key) != null) {
								usedCells.get(key).clear();
								usedCells.get(key).putAll(mergeCellData);
							}else {
								cellDatas.add(mergeCellData);
								usedCells.put(key, mergeCellData);
							}
						}else {
							cellDatas.add(mergeCellData);
							usedCells.put(key, mergeCellData);
						}
					}
				}
			}
		}
		for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
			String key = "y-"+(luckySheetBindData.getCoordsy()+i);
			if(maxCoordinate.containsKey(key)){
				if(maxCoordinate.get(key).intValue() < luckySheetBindData.getCoordsx()+luckySheetBindData.getRowSpan()) {
					maxCoordinate.put(key, luckySheetBindData.getCoordsx()+luckySheetBindData.getRowSpan());
				}
			}else {
				maxCoordinate.put(key, luckySheetBindData.getCoordsx()+luckySheetBindData.getRowSpan());
			}
    	}
		for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
			String key = "x-"+(luckySheetBindData.getCoordsx()+i);
			if(maxCoordinate.containsKey(key)){
				if(maxCoordinate.get(key).intValue() < luckySheetBindData.getCoordsy()+luckySheetBindData.getColSpan()) {
					maxCoordinate.put(key, luckySheetBindData.getCoordsy()+luckySheetBindData.getColSpan());
				}
			}else {
				maxCoordinate.put(key, luckySheetBindData.getCoordsy()+luckySheetBindData.getColSpan());
			}
    	}
	}
	
	private void processSummaryCoverCell(ObjectMapper objectMapper,Map<String, Map<String, Object>> usedCells,LuckySheetBindData luckySheetBindData
			,List<Map<String, Object>> cellDatas,List<JSONObject> calcChain,Object dataRowLen,Map<String, Object> rowlen,Object dataColLen
			,Map<String, Object> columnlen,List<Map<String, Object>> border,Map<String, Integer> maxXAndY,Map<String, Integer> maxCoordinate,
			Map<String, Object> borderInfo,Object v) throws JsonMappingException, JsonProcessingException {
		//覆盖
		Integer maxX = maxXAndY.get("maxX");
		Integer maxY = maxXAndY.get("maxY");
		Map<String, Object> cellData = objectMapper.readValue(objectMapper.writeValueAsString(luckySheetBindData.getCellData()), Map.class);
		((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUE.getCode(), v);
        ((Map<String, Object>)cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode())).put(LuckySheetPropsEnum.CELLVALUEM.getCode(), v);
		if(usedCells.containsKey(luckySheetBindData.getCoordsx()+"_"+luckySheetBindData.getCoordsy())) {
			if(usedCells.get(luckySheetBindData.getCoordsx()+"_"+luckySheetBindData.getCoordsy()) != null) {
				usedCells.get(luckySheetBindData.getCoordsx()+"_"+luckySheetBindData.getCoordsy()).clear();
				usedCells.get(luckySheetBindData.getCoordsx()+"_"+luckySheetBindData.getCoordsy()).putAll(cellData);
			}else {
				cellDatas.add(cellData);
				usedCells.put(luckySheetBindData.getCoordsx()+"_"+luckySheetBindData.getCoordsy(), cellData);
			}
		}else {
			cellDatas.add(cellData);
			usedCells.put(luckySheetBindData.getCoordsx()+"_"+luckySheetBindData.getCoordsy(), cellData);
		}
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsFunction().intValue() && calcChain != null)
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("r", luckySheetBindData.getCoordsx());
			jsonObject.put("c", luckySheetBindData.getCoordsy());
			calcChain.add(jsonObject);
		}
		if(dataRowLen != null)
		{
			if(rowlen.get(String.valueOf(luckySheetBindData.getCoordsx())) == null)
			{
				rowlen.put(String.valueOf(luckySheetBindData.getCoordsx()), dataRowLen);
			}
		}
		if(dataColLen != null)
		{
			if(columnlen.get(String.valueOf(luckySheetBindData.getCoordsy())) == null)
			{
				columnlen.put(String.valueOf(luckySheetBindData.getCoordsy()), dataColLen);
			}
		}
		String borderKey = luckySheetBindData.getCoordsx()+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+luckySheetBindData.getCoordsy();
		this.borderProcess(border, luckySheetBindData.getCoordsx(), luckySheetBindData.getCoordsx()+luckySheetBindData.getRowSpan()-1, luckySheetBindData.getCoordsy(), luckySheetBindData.getCoordsy()+luckySheetBindData.getColSpan()-1,borderInfo,luckySheetBindData,borderKey);
		if((luckySheetBindData.getCoordsx() + luckySheetBindData.getRowSpan()-1)> maxX)
		{
			maxX = luckySheetBindData.getCoordsx() + luckySheetBindData.getRowSpan()-1;
		}
		if((luckySheetBindData.getCoordsx() + luckySheetBindData.getColSpan()-1)> maxY)
		{
			maxY = luckySheetBindData.getCoordsx() + luckySheetBindData.getColSpan()-1;
		}
		if(YesNoEnum.YES.getCode().intValue() == luckySheetBindData.getIsMerge()) {
			for (int t = 0; t < luckySheetBindData.getRowSpan(); t++) {
				for (int j = 0; j < luckySheetBindData.getColSpan(); j++) {
					if(t != 0 || j != 0) {
						Map<String, Object> mc = new HashMap<String, Object>();
						mc.put(LuckySheetPropsEnum.R.getCode(), luckySheetBindData.getCoordsx());
						mc.put(LuckySheetPropsEnum.C.getCode(), luckySheetBindData.getCoordsy());
						Map<String, Object> cellValue = new HashMap<String, Object>();
						cellValue.put(LuckySheetPropsEnum.MERGECELLS.getCode(), mc);
						Map<String, Object> mergeCellData = new HashMap<String, Object>();
						mergeCellData.put(LuckySheetPropsEnum.R.getCode(), luckySheetBindData.getCoordsx()+t);
						mergeCellData.put(LuckySheetPropsEnum.C.getCode(), luckySheetBindData.getCoordsy()+j);
						mergeCellData.put(LuckySheetPropsEnum.CELLCONFIG.getCode(), cellValue);
						String key = (luckySheetBindData.getCoordsx()+t) + "_" + (luckySheetBindData.getCoordsy()+j);
						if(usedCells.containsKey(key)) {
							if(usedCells.get(key) != null) {
								usedCells.get(key).clear();
								usedCells.get(key).putAll(mergeCellData);
							}else {
								cellDatas.add(mergeCellData);
								usedCells.put(key, mergeCellData);
							}
						}else {
							cellDatas.add(mergeCellData);
							usedCells.put(key, mergeCellData);
						}
					}
				}
			}
		}
		for (int i = 0; i < luckySheetBindData.getColSpan(); i++) {
			String key = "y-"+(luckySheetBindData.getCoordsy()+i);
			if(maxCoordinate.containsKey(key)){
				if(maxCoordinate.get(key).intValue() < luckySheetBindData.getCoordsx()+luckySheetBindData.getRowSpan()) {
					maxCoordinate.put(key, luckySheetBindData.getCoordsx()+luckySheetBindData.getRowSpan());
				}
			}else {
				maxCoordinate.put(key, luckySheetBindData.getCoordsx()+luckySheetBindData.getRowSpan());
			}
    	}
		for (int i = 0; i < luckySheetBindData.getRowSpan(); i++) {
			String key = "x-"+(luckySheetBindData.getCoordsx()+i);
			if(maxCoordinate.containsKey(key)){
				if(maxCoordinate.get(key).intValue() < luckySheetBindData.getCoordsy()+luckySheetBindData.getColSpan()) {
					maxCoordinate.put(key, luckySheetBindData.getCoordsy()+luckySheetBindData.getColSpan());
				}
			}else {
				maxCoordinate.put(key, luckySheetBindData.getCoordsy()+luckySheetBindData.getColSpan());
			}
    	}
	}
	
	/**  
	 * @MethodName: isEmptyCellV
	 * @Description: 判断单元格的内容是否为空
	 * @author caiyang
	 * @return boolean
	 * @date 2025-01-02 08:04:57 
	 */ 
	private boolean isEmptyCellV(LuckySheetBindData luckySheetBindData) {
		boolean result = true;
		Map<String, Object> cellData = luckySheetBindData.getCellData();
		if(cellData != null) {
			Map<String, Object> v = (Map<String, Object>) cellData.get("v");
			if(v.get("ct") != null) {
				Map<String, Object> ct = (Map<String, Object>) v.get("ct");
				if(ct.get("t") != null) {
					String t = (String) ct.get("t");
					if("inlineStr".equals(t)) {
						result = false;
					}else {
						if(v.get("v") != null && StringUtil.isNotEmpty(String.valueOf(v.get("v")))) {
							result = false;
						}
					}
				}else {
					if(v.get("v") != null) {
						result = false;
					}
				}
			}
		}
		return result;
	}
	
	/**  
	 * @MethodName: isExtendCoverCell
	 * @Description: 判断当前计算的单元格是否是扩展覆盖单元格
	 * @author caiyang
	 * @return boolean
	 * @date 2025-01-04 09:39:59 
	 */ 
	private boolean isExtendCoverCell(Map<String, Integer> rowAndCol,Map<String, LuckySheetBindData> coverCells,LuckySheetBindData luckySheetBindData) {
		String coverCelllKey = rowAndCol.get("maxX")+"_"+rowAndCol.get("maxY");
		if(coverCells.containsKey(coverCelllKey)) {
			if(!Objects.equals(coverCells.get(coverCelllKey), luckySheetBindData)) {
				LuckySheetBindData coverCellBindData = coverCells.get(coverCelllKey);
				if(CellValueTypeEnum.VARIABLE.getCode().intValue() == coverCellBindData.getCellValueType()) {
					if(AggregateTypeEnum.GROUPSUMMARY.getCode().equals(coverCellBindData.getAggregateType())
							|| AggregateTypeEnum.GROUP.getCode().equals(coverCellBindData.getAggregateType())
							|| AggregateTypeEnum.LIST.getCode().equals(coverCellBindData.getAggregateType())) {
						if(CellExtendEnum.VERTICAL.getCode().intValue() == coverCellBindData.getCellExtend().intValue()
								|| CellExtendEnum.HORIZONTAL.getCode().intValue() == coverCellBindData.getCellExtend().intValue()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	private void processExtendCellOrigin(Map<String, JSONObject> extendCellOrigin,LuckysheetReportCell luckysheetReportCell)
	{
		String cellFlag = luckysheetReportCell.getCoordsx() + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() + luckysheetReportCell.getCoordsy();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("r", luckysheetReportCell.getCoordsx());
		jsonObject.put("c", luckysheetReportCell.getCoordsy());
		Map<String, Object> cellData = JSON.parseObject(luckysheetReportCell.getCellData(), Map.class);
		String m = "";
		if(cellData != null && cellData.get("v") != null)
		{
			Map<String, Object> v = (Map<String, Object>) cellData.get("v");
			if(v.get("m") != null) {
				m = String.valueOf(v.get("m"));	
			}
			Object bg = v.get("bg");
			if(bg != null)
			{
				jsonObject.put("bg", bg);
			}
			Object ps = v.get("ps");
			if(ps != null)
			{
				jsonObject.put("ps", ps);
			}
		}
		if(StringUtil.isNotEmpty(m)) {
			extendCellOrigin.put(cellFlag, jsonObject);
		}else {
			if(luckysheetReportCell.getCellFillType().intValue() == 1) {
				extendCellOrigin.put(cellFlag, jsonObject);
			}
		}
//		extendCellOrigin.put(cellFlag, jsonObject);
	}
	
	/**  
	 * @MethodName: processExtendCellOrigin
	 * @Description: 扩展单元格和原始单元格的关系
	 * @author caiyang
	 * @param extendCellOrigin
	 * @param luckySheetBindData
	 * @param rowAndCol 
	 * @return void
	 * @date 2022-12-09 08:31:14 
	 */  
	private void processExtendCellOrigin(Map<String, JSONObject> extendCellOrigin,LuckySheetBindData luckySheetBindData,Map<String, Integer> rowAndCol)
	{
		String cellFlag = rowAndCol.get("maxX") + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() + rowAndCol.get("maxY");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("r", luckySheetBindData.getOriginalCoordsx());
		jsonObject.put("c", luckySheetBindData.getOriginalCoordsy());
		Map<String, Object> cellData = luckySheetBindData.getCellData();
		if(cellData.get("v") != null)
		{
			Map<String, Object> v = (Map<String, Object>) cellData.get("v");
			Object bg = v.get("bg");
			if(bg != null)
			{
				jsonObject.put("bg", bg);
			}
			Object ps = v.get("ps");
			if(ps != null)
			{
				jsonObject.put("ps", ps);
			}
		}
		
		extendCellOrigin.put(cellFlag, jsonObject);
	}
	
	private void processExtendCellOrigin(Map<String, JSONObject> extendCellOrigin,LuckySheetBindData luckySheetBindData,Map<String, Integer> rowAndCol,Integer isRely)
	{
		String cellFlag = rowAndCol.get("maxX") + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() + rowAndCol.get("maxY");
		JSONObject jsonObject = new JSONObject();
		String m = "";
		if(luckySheetBindData.getCellData() != null && luckySheetBindData.getCellData().get("v") != null) {
			Map<String, Object> vObj = (Map<String, Object>) luckySheetBindData.getCellData().get("v");
			if(vObj.get("m") != null) {
				m = String.valueOf(vObj.get("m"));	
			}
		}
		if(isRely.intValue() == 1 && "删除".equals(m)) {
			jsonObject.put("r", luckySheetBindData.getRelyCoordsx());
			jsonObject.put("c", luckySheetBindData.getRelyCoordsy());
		}else {
			jsonObject.put("r", luckySheetBindData.getCoordsx());
			jsonObject.put("c", luckySheetBindData.getCoordsy());
		}
		Map<String, Object> cellData = luckySheetBindData.getCellData();
		if(cellData.get("v") != null)
		{
			Map<String, Object> v = (Map<String, Object>) cellData.get("v");
			Object bg = v.get("bg");
			if(bg != null)
			{
				jsonObject.put("bg", bg);
			}
			Object ps = v.get("ps");
			if(ps != null)
			{
				jsonObject.put("ps", ps);
			}
		}
		if(StringUtil.isNotEmpty(m)) {
			extendCellOrigin.put(cellFlag, jsonObject);
		}else {
			if(luckySheetBindData.getCellFillType().intValue() == 1) {
				extendCellOrigin.put(cellFlag, jsonObject);
			}
		}
		
	}
	
	/**  
	 * @MethodName: processColumnStartCoords
	 * @Description: 原始动态单元格对应的起始单元格
	 * @author caiyang
	 * @param columnStartCoords
	 * @param luckySheetBindData
	 * @param rowAndCol 
	 * @return void
	 * @date 2022-12-09 08:31:28 
	 */  
	private void processColumnStartCoords(Map<String, JSONObject> columnStartCoords,LuckySheetBindData luckySheetBindData,Map<String, Integer> rowAndCol) {
		String originCell = luckySheetBindData.getOriginalCoordsx() + LuckySheetPropsEnum.COORDINATECONNECTOR.getCode() + luckySheetBindData.getOriginalCoordsy();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("r", rowAndCol.get("maxX"));
		jsonObject.put("c", rowAndCol.get("maxY"));
		columnStartCoords.put(originCell, jsonObject);
	}
	
	/**  
	 * @MethodName: getReportDict
	 * @Description: 获取报表的数据字典
	 * @author caiyang
	 * @param sheets 
	 * @return void
	 * @throws JSQLParserException 
	 * @throws ParseException 
	 * @date 2022-11-28 09:43:56 
	 */  
	private ReportCellDictsDto getReportDict(List<ReportTplSheet> sheets,Map<String, Map<String, String>> dictCache,ReportTpl reportTpl,Map<String, Object> viewParams) throws JSQLParserException, ParseException {
		ReportCellDictsDto result = new ReportCellDictsDto();
		List<Long> sheetIds = new ArrayList<>();
		Map<Long, String> sheetIndexMap = new HashMap<>();
		for (int i = 0; i < sheets.size(); i++) {
			sheetIds.add(sheets.get(i).getId());
			sheetIndexMap.put(sheets.get(i).getId(), sheets.get(i).getSheetIndex());
		}
		QueryWrapper<LuckysheetReportCell> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("sheet_id", sheetIds);
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<LuckysheetReportCell> list = this.iLuckysheetReportCellService.list(queryWrapper);
		if(!ListUtil.isEmpty(list))
		{
			Map<String, List<Map<String, Object>>> sqlDataTempCache = new HashMap<>();
			Map<String, Map<String, String>> cellDictsValueLabel = new HashMap<String, Map<String,String>>();
			Map<String, Map<String, String>> cellDictsLabelValue = new HashMap<String, Map<String,String>>();
			for (int i = 0; i < list.size(); i++) {
				JSONObject cellAttrs = JSONObject.parseObject(list.get(i).getFormsAttrs());
				if(cellAttrs == null) {
					continue;
				}
				int valueType = cellAttrs.getIntValue("valueType");
				if(valueType == 4 || list.get(i).getIsDict())
				{
					int sourceType = reportTpl.getTplType().intValue() == 1?list.get(i).getSourceType():cellAttrs.getInteger("sourceType")==null?list.get(i).getSourceType():cellAttrs.getInteger("sourceType");
					if(sourceType == 2) {
						//sql语句
						Long datasourceId = reportTpl.getTplType().intValue() == 1?list.get(i).getDatasourceId():cellAttrs.getLong("datasourceId")==null?list.get(i).getDatasourceId():cellAttrs.getLong("datasourceId");
						String content = reportTpl.getTplType().intValue() == 1?list.get(i).getDictContent():cellAttrs.getString("content")==null?list.get(i).getDictContent():cellAttrs.getString("content");
						String key = datasourceId + "_" + content;
						List<Map<String, Object>> selectDatas = null;
						if(sqlDataTempCache.containsKey(key)) {
							selectDatas = sqlDataTempCache.get(key);
						}else {
							ReportDatasource reportDatasource  = iReportDatasourceService.getById(datasourceId);
							//数据源配置
							DataSourceConfig dataSourceConfig = new DataSourceConfig(reportDatasource.getId(), reportDatasource.getDriverClass(), reportDatasource.getJdbcUrl(), reportDatasource.getUserName(), reportDatasource.getPassword(), null);
							//获取数据源
							DataSource dataSource = JdbcUtils.getDataSource(dataSourceConfig);
							String sql = JdbcUtils.processSqlParams(content, viewParams);
							selectDatas = ReportDataUtil.getSelectData(dataSource, sql,true);
							sqlDataTempCache.put(key, selectDatas);
						}
						if(ListUtil.isNotEmpty(selectDatas)) {
							Map<String, String> valueLabelMap = new HashMap<>();
							Map<String, String> labelValueMap = new HashMap<>();
							for (int j = 0; j < selectDatas.size(); j++) {
								Object value = selectDatas.get(j).get("value");
								Object name = selectDatas.get(j).get("name");
								valueLabelMap.put(String.valueOf(value), String.valueOf(name));
								labelValueMap.put(String.valueOf(name), String.valueOf(value));
							}
							cellDictsValueLabel.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), valueLabelMap);
							dictCache.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), valueLabelMap);
							cellDictsLabelValue.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), labelValueMap);
						}
					}else if(sourceType == 3) {
						//自定义
						String content = reportTpl.getTplType().intValue() == 1?list.get(i).getDictContent():cellAttrs.getString("content");
						if(StringUtil.isNotEmpty(content)) {
							JSONArray selectDatas = null;
							try {
								selectDatas = JSON.parseArray(content);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if(ListUtil.isNotEmpty(selectDatas)) {
								Map<String, String> valueLabelMap = new LinkedHashMap<>();
								Map<String, String> labelValueMap = new LinkedHashMap<>();
								for (int j = 0; j < selectDatas.size(); j++) {
									Object value = selectDatas.getJSONObject(j).getString("value");
									Object name = selectDatas.getJSONObject(j).getString("name");
									valueLabelMap.put(String.valueOf(value), String.valueOf(name));
									labelValueMap.put(String.valueOf(name), String.valueOf(value));
								}
								cellDictsValueLabel.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), valueLabelMap);
								dictCache.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), valueLabelMap);
								cellDictsLabelValue.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), labelValueMap);
							}
						}
					}else {
						//数据字典
						Long datasourceId = cellAttrs.getLong("datasourceId");
						String dictType = cellAttrs.getString("dictType");
						List<ReportDatasourceDictData> dictDatas = null;
						if(datasourceId != null && StringUtil.isNotEmpty(dictType))
						{
							dictDatas = (List<ReportDatasourceDictData>) redisUtil.get(datasourceId+"_"+dictType);
							if(ListUtil.isEmpty(dictDatas))
							{
								QueryWrapper<ReportDatasourceDictData> dictDataQueryWrapper = new QueryWrapper<>();
								dictDataQueryWrapper.eq("datasource_id", datasourceId);
								dictDataQueryWrapper.eq("dict_type", dictType);
								dictDataQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
								dictDatas = this.iReportDatasourceDictDataService.list(dictDataQueryWrapper);
								if(!ListUtil.isEmpty(dictDatas))
								{
									redisUtil.set(datasourceId+"_"+dictType, dictDatas);
									Map<String, String> valueLabelMap = new HashMap<>();
									Map<String, String> labelValueMap = new HashMap<>();
									for (int j = 0; j < dictDatas.size(); j++) {
										valueLabelMap.put(dictDatas.get(j).getDictValue(), dictDatas.get(j).getDictLabel());
										labelValueMap.put(dictDatas.get(j).getDictLabel(), dictDatas.get(j).getDictValue());
									}
									cellDictsValueLabel.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), valueLabelMap);
									cellDictsLabelValue.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), labelValueMap);
								}
							}else {
								Map<String, String> valueLabelMap = new HashMap<>();
								Map<String, String> labelValueMap = new HashMap<>();
								for (int j = 0; j < dictDatas.size(); j++) {
									valueLabelMap.put(dictDatas.get(j).getDictValue(), dictDatas.get(j).getDictLabel());
									labelValueMap.put(dictDatas.get(j).getDictLabel(), dictDatas.get(j).getDictValue());
									cellDictsValueLabel.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), valueLabelMap);
									cellDictsLabelValue.put(sheetIndexMap.get(list.get(i).getSheetId())+"_"+list.get(i).getCoordsx()+"_"+list.get(i).getCoordsy(), labelValueMap);
								}
							}
						}
					}
				}
			}
			result.setCellDictsLabelValue(cellDictsLabelValue);
			result.setCellDictsValueLabel(cellDictsValueLabel);
		}
		return result;
	}
	
	/**  
	 * @MethodName: processCellFCustom
	 * @Description: 处理单元格函数中的自定义函数
	 * @author caiyang
	 * @param f
	 * @param luckySheetBindData
	 * @return String
	 * @date 2025-03-20 01:51:18 
	 */ 
	private String processCellFCustomF(String f,LuckySheetBindData luckySheetBindData) {
		try {
			Pattern pattern = Pattern.compile(
				    "\\b(dataLength|other)\\b\\s*\\(\\s*([^)]*?)\\s*\\)",  // 分组1: 对象前缀，分组2: 参数
				    Pattern.CASE_INSENSITIVE  // 可选：忽略大小写
				);
			Matcher matcher = pattern.matcher(f);
			while (matcher.find()) {
			    String methodName = matcher.group(1);
			    String params = matcher.group(2);
			    List<String> list = Convert.toList(String.class, params);
			    list.add(0, String.valueOf(luckySheetBindData.getDataSize()));
			    Object result = CustomFunction.execute(methodName, list);
			    String replaceData = methodName + "("+params+")";
			    f = f.replace(replaceData, String.valueOf(result==null?"":result));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
	
	private void isOperationCol(LuckySheetBindData bindData,int c,boolean isExport,Map colhidden) {
		if(isExport && bindData.getTplType() == 2 && bindData.isOperationCol()) {
		   colhidden.put(c, 0);
		}
	}
	
	private JSONArray getNewCoords(int prorityDirection,int coordsx,int coordsy,Map<String, Integer> maxCoordinate,Map<String, Map<String, Object>> usedCells) {
		JSONArray result = new JSONArray();
		int x = coordsx;
		int y = coordsy;
		String key = "";
		boolean flag = true;
		if(prorityDirection == 1) {
			x = this.getMaxRow(maxCoordinate, coordsx, coordsy, 1);
			key = x+"_"+coordsy;
			if(usedCells.containsKey(key)) {
				x = coordsx;
				y = this.getMaxCol(maxCoordinate, coordsx, coordsy, 1);
				key = coordsx + "_" + y;
				if(usedCells.containsKey(key)) {
					flag = false;
				}
			}
		}else {
			y = this.getMaxCol(maxCoordinate, coordsx, coordsy, 1);
			key = coordsx + "_" + y;
			if(usedCells.containsKey(key)) {
				y = coordsy;
				x = this.getMaxRow(maxCoordinate, coordsx, coordsy, 1);
				key = x+"_"+coordsy;
				if(usedCells.containsKey(key)) {
					flag = false;
				}
			}
		}
		if(flag) {
			result.add(x);
			result.add(y);
		}
		return result;
	}
}