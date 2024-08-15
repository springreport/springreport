package com.springreport.impl.doctpl;

import com.springreport.entity.doctpl.DocTpl;
import com.springreport.entity.doctplcharts.DocTplCharts;
import com.springreport.entity.doctplsettings.DocTplSettings;
import com.springreport.entity.reportdatasource.ReportDatasource;
import com.springreport.entity.reporttpldataset.ReportTplDataset;
import com.springreport.entity.reporttpldatasource.ReportTplDatasource;
import com.springreport.mapper.doctpl.DocTplMapper;
import com.springreport.api.doctpl.IDocTplService;
import com.springreport.api.doctplcharts.IDocTplChartsService;
import com.springreport.api.doctplsettings.IDocTplSettingsService;
import com.springreport.api.reportdatasource.IReportDatasourceService;
import com.springreport.api.reporttpldataset.IReportTplDatasetService;
import com.springreport.api.reporttpldatasource.IReportTplDatasourceService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.plugin.table.LoopColumnTableRenderPolicy;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.docx4j.Docx4J;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.AsposeUtil;
import com.springreport.util.DateUtil;
import com.springreport.util.FileUtil;
import com.springreport.util.HttpClientUtil;
import com.springreport.util.InfluxDBConnection;
import com.springreport.util.JdbcUtils;
import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.ParamUtil;
import com.springreport.util.ReportDataUtil;
import com.springreport.util.StringUtil;
import com.springreport.util.WordUtil;

import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.DocChartSettingDto;
import com.springreport.base.PageEntity;
import com.springreport.base.TDengineConnection;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.StatusCode;
import com.springreport.dto.doctpl.DocTplDto;
import com.springreport.dto.doctpl.DocTplSettingsDto;
import com.springreport.dto.reporttpl.MesGenerateReportDto;
import com.springreport.dto.reporttpldataset.ReportDatasetDto;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.transaction.annotation.Transactional;

import com.springreport.enums.DatasetTypeEnum;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.SqlTypeEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: DocTpl服务实现
* @author 
* @date 2024-05-02 08:55:33
* @version V1.0  
 */
@Service
public class DocTplServiceImpl extends ServiceImpl<DocTplMapper, DocTpl> implements IDocTplService {
  
	
	@Autowired
	private IReportDatasourceService iReportDatasourceService;
	
	@Autowired
	private IReportTplDatasourceService iReportTplDatasourceService;
	
	@Autowired
	private IDocTplSettingsService iDocTplSettingsService;
	
	@Autowired
	private IReportTplDatasetService iReportTplDatasetService;
	
	@Autowired
	private IDocTplChartsService iDocTplChartsService;
	
	@Value("${file.path}")
    private String dirPath;
	
	@Value("${merchantmode}")
    private Integer merchantmode;
	
	@Value("${show.report.sql}")
	private boolean showReportSql;
	
	private static Mapper fontMapper = null;
	
	static{
		fontMapper = new IdentityPlusMapper();
        fontMapper.put("隶书", PhysicalFonts.get("LiSu"));
        fontMapper.put("宋体", PhysicalFonts.get("SimSun"));
        fontMapper.put("微软雅黑", PhysicalFonts.get("Microsoft Yahei"));
        fontMapper.put("黑体", PhysicalFonts.get("SimHei"));
        fontMapper.put("楷体", PhysicalFonts.get("KaiTi"));
        fontMapper.put("新宋体", PhysicalFonts.get("NSimSun"));
        fontMapper.put("华文行楷", PhysicalFonts.get("STXingkai"));
        fontMapper.put("华文仿宋", PhysicalFonts.get("STFangsong"));
        fontMapper.put("仿宋", PhysicalFonts.get("FangSong"));
        fontMapper.put("幼圆", PhysicalFonts.get("YouYuan"));
        fontMapper.put("华文宋体", PhysicalFonts.get("STSong"));
        fontMapper.put("华文中宋", PhysicalFonts.get("STZhongsong"));
        fontMapper.put("等线", PhysicalFonts.get("SimSun"));
        fontMapper.put("等线 Light", PhysicalFonts.get("SimSun"));
        fontMapper.put("华文琥珀", PhysicalFonts.get("STHupo"));
        fontMapper.put("华文隶书", PhysicalFonts.get("STLiti"));
        fontMapper.put("华文新魏", PhysicalFonts.get("STXinwei"));
        fontMapper.put("华文彩云", PhysicalFonts.get("STCaiyun"));
        fontMapper.put("方正姚体", PhysicalFonts.get("FZYaoti"));
        fontMapper.put("方正舒体", PhysicalFonts.get("FZShuTi"));
        fontMapper.put("华文细黑", PhysicalFonts.get("STXihei"));
        fontMapper.put("宋体扩展", PhysicalFonts.get("simsun-extB"));
        fontMapper.put("仿宋_GB2312", PhysicalFonts.get("FangSong_GB2312"));
        fontMapper.put("新細明體", PhysicalFonts.get("SimSun"));
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
	public PageEntity tablePagingQuery(DocTpl model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<DocTplDto> list = this.baseMapper.getTableList(model);
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


	/**
	*<p>Title: getDetail</p>
	*<p>Description: 获取详情</p>
	* @author 
	* @param id
	* @return
	*/
	@Override
	public BaseEntity getDetail(Long id) {
		DocTplDto result = new DocTplDto();
		DocTpl docTpl = this.getById(id);
		BeanUtils.copyProperties(docTpl, result);
		QueryWrapper<ReportTplDatasource> queryWrapper = new QueryWrapper<ReportTplDatasource>();
		queryWrapper.eq("tpl_id", docTpl.getId());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplDatasource> list = this.iReportTplDatasourceService.list(queryWrapper);
		List<Long> dataSource = new ArrayList<Long>();
		if (!ListUtil.isEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				dataSource.add(list.get(i).getDatasourceId());
			}
		}
		result.setDataSource(dataSource);
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
	public BaseEntity insert(DocTplDto model) {
		BaseEntity result = new BaseEntity();
		//校验报表代码是否已经存在
		QueryWrapper<DocTpl> queryWrapper = new QueryWrapper<DocTpl>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("tpl_code", model.getTplCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		DocTpl isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该报表标识"}));
		}
		DocTpl docTpl = new DocTpl();
		BeanUtils.copyProperties(model, docTpl);
		//保持doc模板
		this.save(docTpl);
		//保存报表关联的数据源
		List<ReportTplDatasource> datasources = new ArrayList<ReportTplDatasource>();
		ReportTplDatasource datasource = null;
		for (int i = 0; i < model.getDataSource().size(); i++) {
			datasource = new ReportTplDatasource();
			datasource.setTplId(docTpl.getId());
			datasource.setDatasourceId(model.getDataSource().get(i));
			datasources.add(datasource);
		}
		this.iReportTplDatasourceService.saveBatch(datasources);
		//模板数据表新增一条空数据
		DocTplSettings docTplSettings = new DocTplSettings();
		docTplSettings.setTplId(docTpl.getId());
		docTplSettings.setHeader("[]");
		docTplSettings.setFooter("[]");
		docTplSettings.setMain("[]");
		docTplSettings.setMargins("[]");
		docTplSettings.setHeight(1123);
		docTplSettings.setWidth(794);
		this.iDocTplSettingsService.save(docTplSettings);
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
	public BaseEntity update(DocTplDto model) {
		BaseEntity result = new BaseEntity();
		//校验报表代码是否已经存在
		QueryWrapper<DocTpl> queryWrapper = new QueryWrapper<DocTpl>();
		queryWrapper.ne("id", model.getId());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("tpl_code", model.getTplCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		DocTpl isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该报表标识"}));
		}
		DocTpl reportTpl = new DocTpl();
		BeanUtils.copyProperties(model, reportTpl);
		this.updateById(model);
		//更新报表关联的数据源
		result.setStatusMsg(MessageUtil.getValue("info.update"));
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
		DocTpl docTpl = new DocTpl();
		docTpl.setId(id);
		docTpl.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(docTpl);
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
		List<DocTpl> list = new ArrayList<DocTpl>();
		for (int i = 0; i < ids.size(); i++) {
			DocTpl docTpl = new DocTpl();
			docTpl.setId(ids.get(i));
			docTpl.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(docTpl);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}

	/**  
	 * @MethodName: getDocTplSettings
	 * @Description: 获取doc文档模板数据
	 * @author caiyang
	 * @param model
	 * @return DocTplSettings
	 * @date 2024-05-03 09:53:33 
	 */ 
	@Override
	public DocTplSettingsDto getDocTplSettings(DocTplSettings model) {
		DocTplSettingsDto result = new DocTplSettingsDto();
		QueryWrapper<DocTplSettings> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("tpl_id", model.getTplId());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		DocTplSettings docTplSettings = this.iDocTplSettingsService.getOne(queryWrapper, false);
		if(docTplSettings == null) {
			docTplSettings = new DocTplSettings();
		}
		BeanUtils.copyProperties(docTplSettings, result);
		DocTpl docTpl = this.getById(model.getTplId());
		if (docTpl != null) {
			result.setTplName(docTpl.getTplName());
		}
		result.setChartUrlPrefix(MessageUtil.getValue("chart.url.prefix"));
		//获取图表信息
		QueryWrapper<DocTplCharts> chartsWrapper = new QueryWrapper<>();
		chartsWrapper.eq("tpl_id", model.getTplId());
		chartsWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<DocTplCharts> docTplCharts = this.iDocTplChartsService.list(chartsWrapper);
		if(docTplCharts == null) {
			docTplCharts = new ArrayList<>();
		}
		result.setDocTplCharts(docTplCharts);;
		return result;
	}


	/**  
	 * @MethodName: saveDocTplSettings
	 * @Description: 保存模板数据
	 * @author caiyang
	 * @param model
	 * @return
	 * @see com.springreport.api.doctpl.IDocTplService#saveDocTplSettings(com.springreport.entity.doctplsettings.DocTplSettings)
	 * @date 2024-05-03 04:10:18 
	 */
	@Override
	public BaseEntity saveDocTplSettings(DocTplSettingsDto model) {
		BaseEntity result = new BaseEntity();
		DocTplSettings docTplSettings = new DocTplSettings();
		BeanUtils.copyProperties(model, docTplSettings);
		UpdateWrapper<DocTplSettings> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("tpl_id", model.getTplId());
		updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		this.iDocTplSettingsService.update(docTplSettings, updateWrapper);
		result.setStatusMsg(MessageUtil.getValue("info.save"));
		//先删除模板的所有图表信息，再新增
		QueryWrapper<DocTplCharts> chartsWrapper = new QueryWrapper<>();
		chartsWrapper.eq("tpl_id", model.getTplId());
		chartsWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		this.iDocTplChartsService.remove(chartsWrapper);
		if(ListUtil.isNotEmpty(model.getDocTplCharts())) {
			this.iDocTplChartsService.saveBatch(model.getDocTplCharts());
		}
		return result;
	}
	
	/**  
	 * @MethodName: downLoadDocTpl
	 * @Description: 导出word模板
	 * @author caiyang
	 * @param model
	 * @throws Exception 
	 * @see com.springreport.api.doctpl.IDocTplService#downLoadDocTpl(com.springreport.entity.doctplsettings.DocTplSettings)
	 * @date 2024-05-09 05:10:31 
	 */
	@Override
	public void downLoadDocTpl(DocTplSettings model,HttpServletResponse httpServletResponse) throws Exception {
		DocTpl docTpl = this.getById(model.getTplId());
		if (docTpl == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"报表模板"}));
		}
		QueryWrapper<DocTplSettings> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("tpl_id", model.getTplId());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		model = this.iDocTplSettingsService.getOne(queryWrapper, false);
		if(model == null) {
			model = new DocTplSettings();
			model.setHeader("[]");
			model.setFooter("[]");
			model.setMain("[]");
			model.setMain("[]");
			model.setWidth(794);
			model.setHeight(1123);
		}
		httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		//设置文件名编码格式
        String filename = URLEncoder.encode(docTpl.getTplName(), "UTF-8");
        httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" +filename + ".docx");
        httpServletResponse.addHeader("filename", filename + ".docx");
		ByteArrayOutputStream baos = this.getDocTplStream(model,null,true);
		byte[] bytes = baos.toByteArray();
		httpServletResponse.setHeader("Content-Length", String.valueOf(bytes.length));
        BufferedOutputStream bos = null;
        bos = new BufferedOutputStream(httpServletResponse.getOutputStream());
        bos.write(bytes);
        bos.close();
        baos.close();
	}


	/**  
	 * @MethodName: previewDoc
	 * @Description: doc预览
	 * @author caiyang
	 * @param model
	 * @throws Exception 
	 * @throws SQLException 
	 * @see com.springreport.api.doctpl.IDocTplService#previewDoc(com.springreport.dto.reporttpl.MesGenerateReportDto)
	 * @date 2024-05-07 09:29:59 
	 */
	@Override
	public Map<String, Object> previewDoc(MesGenerateReportDto model,UserInfoDto userInfoDto) throws SQLException, Exception {
		DocTpl docTpl = this.getById(model.getTplId());
		if (docTpl == null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"报表模板"}));
		}
		//获取模板关联的所有数据集
		ReportTplDataset dataset = new ReportTplDataset();
		dataset.setTplId(model.getTplId());
		List<ReportDatasetDto> datasets = this.iReportTplDatasetService.getTplDatasets(dataset);
		Map<String, Object> data = new HashMap<>();
		Map<String, List<String>> paramsType = new HashMap<>();//记录参数类型，vertical代表竖向列表参数，horizontal代表横向列表参数
		List<Map<String, String>> reportSqls = new ArrayList<>();
		if(ListUtil.isNotEmpty(datasets)) {
			for (int i = 0; i < datasets.size(); i++) {
				Object datasetData = this.getDatasetDatas(model, datasets.get(i), reportSqls,paramsType,userInfoDto);
				data.put(datasets.get(i).getDatasetName(), datasetData);
			}
		}
		Map<String, Object> result = this.generateDocPdf(model.getTplId(), data,paramsType,model.getFileId());
		result.put("tplName", docTpl.getTplName());
		result.put("reportSqls", reportSqls);
		result.put("showReportSql", showReportSql);
		return result;
	}
	
	/**  
	 * @MethodName: generateDocPdf
	 * @Description: 生成word文档和pdf并返回word文档和pdf文档的访问url
	 * @author caiyang
	 * @param tplId
	 * @param data
	 * @return Map<String,String>
	 * @date 2024-05-07 04:49:42 
	 */ 
	private Map<String, Object> generateDocPdf(Long tplId,Map<String, Object> data,Map<String, List<String>> paramsType,String fileId){
		Map<String, Object> result = new HashMap<String, Object>();
		QueryWrapper<DocTplSettings> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("tpl_id", tplId);
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		DocTplSettings model = this.iDocTplSettingsService.getOne(queryWrapper, false);
		if(model == null) {
			model = new DocTplSettings();
			model.setHeader("[]");
			model.setFooter("[]");
			model.setMain("[]");
			model.setMain("[]");
			model.setWidth(794);
			model.setHeight(1123);
		}
		ByteArrayOutputStream baos = null;
		ByteArrayInputStream inputStream = null;
		XWPFTemplate template = null;
		FileOutputStream docxFileOutputStream = null;
		FileOutputStream pdfFileOutputStream = null;
		try {
			baos = this.getDocTplStream(model,data,false);
			ZipSecureFile.setMinInflateRatio(-1.0d);
			inputStream = new ByteArrayInputStream(baos.toByteArray());
			String date = DateUtil.getNow(DateUtil.FORMAT_LONOGRAM);
			if(StringUtil.isNullOrEmpty(fileId)) {
				fileId = IdWorker.getIdStr();
			}
			String pdfName = fileId + ".pdf";
			String docxName = fileId + ".docx";
			File docxFile = new File(dirPath + date + "/" + docxName);
			FileUtil.createFile(docxFile);
			File pdfFile = new File(dirPath + date + "/" + pdfName);
			FileUtil.createFile(pdfFile);
			ConfigureBuilder configureBuilder = Configure.builder();
			if(!StringUtil.isEmptyMap(paramsType)) {
				for(String key : paramsType.keySet()){
					switch (key) {
					case "vertical":
						LoopRowTableRenderPolicy verticalPolicy = new LoopRowTableRenderPolicy();
						List<String> vertical = paramsType.get("vertical");
						for (int i = 0; i < vertical.size(); i++) {
							configureBuilder.bind(vertical.get(i), verticalPolicy);
						}
						break;
					case "horizontal":
						LoopColumnTableRenderPolicy horizontalPolicy = new LoopColumnTableRenderPolicy();
						List<String> horizontal = paramsType.get("horizontal");
						for (int i = 0; i < horizontal.size(); i++) {
							configureBuilder.bind(horizontal.get(i), horizontalPolicy);
						}
						break;
					default:
						break;
					}
				}
			}
			template = XWPFTemplate.compile(inputStream,configureBuilder.build()).render(data);
			docxFileOutputStream = new FileOutputStream(dirPath + date + "/" + docxName);
			template.write(docxFileOutputStream);
			docxFileOutputStream.flush();
			docxFileOutputStream.close();
			
//			pdfFileOutputStream = new FileOutputStream(dirPath + date + "/" + pdfName);
//			// word转pdf
//			InputStream in = new FileInputStream(dirPath + date + "/" + docxName);
//			WordprocessingMLPackage pkg = Docx4J.load(in);
//			
//            pkg.setFontMapper(fontMapper);
//            Docx4J.toPDF(pkg, pdfFileOutputStream);
			AsposeUtil.wordToPdf(dirPath + date + "/" + docxName, dirPath + date + "/" + pdfName);
			String docxUrl = MessageUtil.getValue("file.url.prefix")+date+"/"+docxName+"?t="+System.currentTimeMillis();
			String pdfUrl = MessageUtil.getValue("file.url.prefix")+date+"/"+pdfName+"?t="+System.currentTimeMillis();
			result.put("docxUrl", docxUrl);
			result.put("pdfUrl", pdfUrl);
			result.put("fileId", fileId);//页面加载时分配一个文件名，防止每次查询都生成一个新的文件
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(baos != null) {
					baos.close();
				}
				if(inputStream != null) {
					inputStream.close();
				}
				if(template != null) {
					template.close();
				}
				if(docxFileOutputStream != null) {
					docxFileOutputStream.close();
				}
				if(pdfFileOutputStream != null) {
					pdfFileOutputStream.close();
				}
			} catch (Exception e2) {
				log.error("流关闭失败，异常原因：" + e2.getMessage());
			}
		}
		return result;
	}
	
	private ByteArrayOutputStream getDocTplStream(DocTplSettings model,Map<String, Object> dynamicData,boolean isTemplate) {
		ByteArrayOutputStream baos = null;
		XWPFDocument doc = new XWPFDocument();
		QueryWrapper<DocTplCharts> chartsWrapper = new QueryWrapper<>();
		chartsWrapper.eq("tpl_id", model.getTplId());
		chartsWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<DocTplCharts> docTplCharts = this.iDocTplChartsService.list(chartsWrapper);
		Map<String, List<DocTplCharts>> docTplChartsMap = null;
		if(ListUtil.isNotEmpty(docTplCharts)) {
			docTplChartsMap = docTplCharts.stream().collect(Collectors.groupingBy(DocTplCharts::getChartUrl));
		}
		try {
			//添加自定义标题
//			for (int i = 1; i <= 6; i++) {
//				WordUtil.addCustomHeadingStyle(doc, "标题" + i, i);
//			}
			//设置纸张大小
			WordUtil.setPaperSize(doc, model.getHeight(), model.getWidth(),model.getPaperDirection());
			//设置纸张大小
			WordUtil.setPaperSize(doc, model.getHeight(), model.getWidth(),model.getPaperDirection());
			if(StringUtil.isNotEmpty(model.getWatermark())){
				JSONObject watermark = JSON.parseObject(model.getWatermark());
				String data = watermark.getString("data");
				if(StringUtil.isNotEmpty(data)) {
					int size = watermark.getIntValue("size");
					WordUtil.addWatermark(doc, data, "#aeb5c0", size);
				}
			}
			//页眉
			JSONArray header = JSON.parseArray(model.getHeader());
			if(ListUtil.isNotEmpty(header)) {
				XWPFHeader docHeader = doc.createHeader(HeaderFooterType.DEFAULT);
				XWPFParagraph paragraph = docHeader.createParagraph();
				for (int i = 0; i < header.size(); i++) {
					String type = header.getJSONObject(i).getString("type")==null?"":header.getJSONObject(i).getString("type");
					switch (type) {
					case "separator":
						WordUtil.addSeparator(paragraph, header.getJSONObject(i));
						break;
					default:
						WordUtil.addParagraph(paragraph, header.getJSONObject(i), null);
						break;
					}
				}
			}
			//页脚
			JSONArray footer = JSON.parseArray(model.getFooter());
			if(ListUtil.isNotEmpty(footer)) {
				XWPFFooter docFooter = doc.createFooter(HeaderFooterType.DEFAULT);
				XWPFParagraph paragraph = docFooter.createParagraph();
				for (int i = 0; i < footer.size(); i++) {
					String type = footer.getJSONObject(i).getString("type")==null?"":footer.getJSONObject(i).getString("type");
					switch (type) {
					case "separator":
						WordUtil.addSeparator(paragraph, footer.getJSONObject(i));
						break;
					default:
						WordUtil.addParagraph(paragraph, footer.getJSONObject(i), null);
						break;
					}
				}
			}
			//文档主体内容
			JSONArray main = JSON.parseArray(model.getMain());
			if(ListUtil.isNotEmpty(main)) {
				XWPFParagraph paragraph = doc.createParagraph();
				String lastType = "";
				for (int i = 0; i < main.size(); i++) {
					JSONObject content = main.getJSONObject(i);
					String type = content.getString("type") == null?"":content.getString("type");
					switch (type) {
					case "":
						if(content.getString("value").startsWith("\n") || 
								(!type.equals(lastType) && !"tab".equals(lastType) 
										&& !"superscript".equals(lastType) 
										&& !"subscript".equals(lastType)
										&& !"separator".equals(lastType))) {
							content.put("value", content.getString("value").replaceFirst("\n", ""));
							paragraph = doc.createParagraph();
						}
						if("separator".equals(lastType)) {
							String value = content.getString("value");
							if(StringUtil.isNotEmpty(value) && value.startsWith("\n")) {
								content.put("value", value.replaceFirst("\n", ""));
							}
						}
						WordUtil.addParagraph(paragraph,content, null);
						break;
					case "title":
						paragraph = doc.createParagraph();
						WordUtil.addTitleParagraph(paragraph, content);
						break;
					case "tab":
						WordUtil.addTab(paragraph,null);
						break;
					case "table":
						WordUtil.addTable(doc,content);
						break;
					case "superscript":
						WordUtil.addSubSupScript(paragraph, content, "sup");
						break;
					case "subscript":
						WordUtil.addSubSupScript(paragraph, content, "sub");
						break;
					case "separator":
//						paragraph = doc.createParagraph();
						WordUtil.addSeparator(paragraph, content);
						break;
					case "list":
						WordUtil.addList(doc, content);
						break;
					case "image":
						String chartUrlPrefix = MessageUtil.getValue("chart.url.prefix");
						String url = content.getString("value");
						if(url.contains(chartUrlPrefix)) {
							//图表
							if(!StringUtil.isEmptyMap(docTplChartsMap)) {
								if(docTplChartsMap.containsKey(url)) {
									DocTplCharts tplCharts = docTplChartsMap.get(url).get(0);
									DocChartSettingDto docChartSettingDto = new DocChartSettingDto();
									BeanUtils.copyProperties(tplCharts, docChartSettingDto);
									WordUtil.addChart(doc,paragraph, content,dynamicData,isTemplate,docChartSettingDto);
								}else {
									WordUtil.addImage(paragraph, content);
								}
							}else {
								WordUtil.addImage(paragraph, content);
							}
						}else {
							//图片
							WordUtil.addImage(paragraph, content);
						}
						break;
					default:
						if(content.getString("value").startsWith("\n") || 
								(!type.equals(lastType) && !"tab".equals(lastType) 
										&& !"superscript".equals(lastType) 
										&& !"subscript".equals(lastType)
										&& !"separator".equals(lastType))) {
							content.put("value", content.getString("value").replaceFirst("\n", ""));
							paragraph = doc.createParagraph();
						}
						if("separator".equals(lastType)) {
							String value = content.getString("value");
							if(StringUtil.isNotEmpty(value) && value.startsWith("\n")) {
								content.put("value", value.replaceFirst("\n", ""));
							}
						}
						WordUtil.addParagraph(paragraph,content, null);
						break;
					}
					lastType = type;
				}
			}
			
			baos = new ByteArrayOutputStream();
			doc.write(baos);
			baos.flush();
			doc.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if(doc != null) {
					doc.close();
				}
				if(baos != null) {
					baos.close();
				}
			} catch (Exception e2) {
				
			}
			
		}
		
		return baos;
	}
	
	/**  
	 * @MethodName: getDatasetDatas
	 * @Description: 获取数据集数据
	 * @author caiyang
	 * @param mesGenerateReportDto
	 * @param reportTplDataset
	 * @param reportSqls
	 * @return
	 * @throws Exception Object
	 * @date 2024-05-07 04:42:27 
	 */ 
	private Object getDatasetDatas(MesGenerateReportDto mesGenerateReportDto,ReportDatasetDto reportTplDataset,List<Map<String, String>> reportSqls,Map<String, List<String>> paramsType,UserInfoDto userInfoDto) throws Exception {
		Map<String, String> sqlMap = new HashMap<>();
		List<Map<String, Object>> datas = null;
		Map<String, Object> searchInfo = null;
		if(mesGenerateReportDto.getSearchData() != null) {
			searchInfo = mesGenerateReportDto.getSearchData().get(0);
		}
		DataSource dataSource = null;
		InfluxDBConnection dbConnection = null;
		TDengineConnection tDengineConnection = null;
		ReportDatasource reportDatasource = this.iReportDatasourceService.getById(reportTplDataset.getDatasourceId());
		Map<String, Object> params = null;
		if(searchInfo != null)
		{
			params = ParamUtil.getViewParams((JSONArray) searchInfo.get("params"),userInfoDto);
		}
		if(DatasetTypeEnum.SQL.getCode().intValue() == reportTplDataset.getDatasetType().intValue()) {
			Object data = this.iReportTplDatasetService.getDatasetDatasource(reportDatasource);
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
			String sql = reportTplDataset.getTplSql();
			sql = JdbcUtils.processSqlParams(sql, params);
			if(SqlTypeEnum.SQL.getCode().intValue() == reportTplDataset.getSqlType().intValue()) {
				if(reportDatasource.getType().intValue() == 6) {//influxdb
					datas = ReportDataUtil.getInfluxdbData(dbConnection, sql);
				}else if(reportDatasource.getType().intValue() == 10) {
					//tdengine
					datas = ReportDataUtil.getDatasourceDataBySql(tDengineConnection.getConnection(), sql);
				}else if(reportDatasource.getType().intValue() == 9) {
					datas = ReportDataUtil.getDatasourceDataBySql(dataSource, sql,reportDatasource.getUserName(),reportDatasource.getPassword());
				}else {
					datas = ReportDataUtil.getDatasourceDataBySql(dataSource, sql);
				}
			}else {
				datas = ReportDataUtil.getDatasourceDataByProcedure(dataSource, sql, params, JSONArray.parseArray(reportTplDataset.getInParam()), JSONArray.parseArray(reportTplDataset.getOutParam()));
			}
			sqlMap.put("name", reportTplDataset.getDatasetName());
			sqlMap.put("sql", sql);
			reportSqls.add(sqlMap);
		}else {//api
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
			if("post".equals(reportDatasource.getApiRequestType()))
			{
				result = HttpClientUtil.doPostJson(reportDatasource.getJdbcUrl(), JSONObject.toJSONString(params), headers);
			}else {
				result = HttpClientUtil.doGet(reportDatasource.getJdbcUrl(),headers,params);
			}
			datas = ReportDataUtil.getApiResult(result, reportDatasource.getApiResultType(), reportDatasource.getApiColumnsPrefix());
		}
		String datasetName = reportTplDataset.getDatasetName();
		//处理数据
		if(DatasetTypeEnum.SQL.getCode().intValue() == reportTplDataset.getDatasetType().intValue()) {
			//sql查询，如果数据集名称是以_v或者_V结尾的，则说明是列表数据，并且是竖向扩展，
			//如果数据集名称是以_h或者_H结尾的，则说明是列表数据，并且是横向扩展
			//如果数据集名称是以_l结尾的，则说明是列表数据，将列表数据直接返回
			//其余则认为是对象数据
			if(datasetName.toLowerCase().endsWith("_v")) {
				List<String> vertical = paramsType.get("vertical");
				if(vertical == null) {
					vertical = new ArrayList<>();
					paramsType.put("vertical", vertical);
				}
				vertical.add(datasetName);
				return datas;
			}else if(datasetName.toLowerCase().endsWith("_h")){
				List<String> horizontal = paramsType.get("horizontal");
				if(horizontal == null) {
					horizontal = new ArrayList<>();
					paramsType.put("horizontal", horizontal);
				}
				horizontal.add(datasetName);
				return datas;
			}else if(datasetName.toLowerCase().endsWith("_l")){
				if(ListUtil.isNotEmpty(datas)) {
					return datas;	
				}else {
					return null;
				}
			}else {
				if(ListUtil.isNotEmpty(datas)) {
					return datas.get(0);	
				}else {
					return null;
				}
			}
		}else {
			//api查询，根据reportdatasource返回值类型(api_result_type)字段判断，ObjectArray是列表数据，Object是对象数据
			String apiResultType = reportDatasource.getApiRequestType();
			if("ObjectArray".equals(apiResultType)) {
				//列表数据，如果数据集名称是以_v或者_V结尾的，则说明是列表数据，并且是竖向扩展，
				//如果数据集名称是以_h或者_H结尾的，则说明是列表数据，并且是横向扩展
				//如果都没有则默认按照竖向扩展的规则
				if(datasetName.toLowerCase().endsWith("_v")) {
					List<String> vertical = paramsType.get("vertical");
					if(vertical == null) {
						vertical = new ArrayList<>();
						paramsType.put("vertical", vertical);
					}
					vertical.add(datasetName);
					return datas;
				}else if(datasetName.toLowerCase().endsWith("_h")){
					List<String> horizontal = paramsType.get("horizontal");
					if(horizontal == null) {
						horizontal = new ArrayList<>();
						paramsType.put("horizontal", horizontal);
					}
					horizontal.add(datasetName);
					return datas;
				}else {
					List<String> vertical = paramsType.get("vertical");
					if(vertical == null) {
						vertical = new ArrayList<>();
						paramsType.put("vertical", vertical);
					}
					vertical.add(datasetName);
					return datas;
				}
			}else {
				if(ListUtil.isNotEmpty(datas)) {
					Map<String, Object> data = datas.get(0);
					for(String mapKey : data.keySet()){
						Object value = data.get(mapKey);
						if(value instanceof List) {
							List<String> vertical = paramsType.get("vertical");
							if(vertical == null) {
								vertical = new ArrayList<>();
								paramsType.put("vertical", vertical);
							}
							vertical.add(mapKey);
						}
					}
					if(datas.size() >1) {
						return datas;
					}else {
						return datas.get(0);	
					}
				}else {
					return null;
				}
			}
		}
	}
}