package com.springreport.impl.sysrolesheet;

import com.springreport.entity.reporttpl.ReportTpl;
import com.springreport.entity.reporttplsheet.ReportTplSheet;
import com.springreport.entity.reporttype.ReportType;
import com.springreport.entity.sysrolesheet.SysRoleSheet;
import com.springreport.mapper.sysrolesheet.SysRoleSheetMapper;
import com.springreport.api.reporttpl.IReportTplService;
import com.springreport.api.reporttplsheet.IReportTplSheetService;
import com.springreport.api.reporttype.IReportTypeService;
import com.springreport.api.sysrolesheet.ISysRoleSheetService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.base.BaseEntity;
import com.springreport.dto.report.ReportTreeDto;
import com.springreport.dto.report.TreeDto;
import com.springreport.dto.sysrole.MesAuthDto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.YesNoEnum;

 /**  
* @Description: SysRoleSheet服务实现
* @author 
* @date 2022-11-08 03:27:24
* @version V1.0  
 */
@Service
public class SysRoleSheetServiceImpl extends ServiceImpl<SysRoleSheetMapper, SysRoleSheet> implements ISysRoleSheetService {
  
	@Autowired
	private IReportTypeService iReportTypeService;
	
	@Autowired
	private IReportTplService iReportTplService;
	
	@Autowired
	private IReportTplSheetService iReportTplSheetService;
	
	@Value("${merchantmode}")
    private Integer merchantmode;
	
	/**  
	 * @Title: getReportTree
	 * @Description: 获取报表树
	 * @param model
	 * @return 
	 * @see com.caiyang.api.sysrolereport.ISysRoleReportService#getReportTree(com.caiyang.entity.sysrolereport.SysRoleReport) 
	 * @author caiyang
	 * @date 2022年7月6日07:58:06
	 */
	@Override
	public TreeDto getReportTree(SysRoleSheet model) {
		TreeDto result = new TreeDto();
		List<String> authed = new ArrayList<String>();//已经授权的报表
		QueryWrapper<SysRoleSheet> roleSheetQueryWrapper = new QueryWrapper<>();
		roleSheetQueryWrapper.eq("role_id", model.getRoleId());
		roleSheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			roleSheetQueryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		List<SysRoleSheet> authedSheets = this.list(roleSheetQueryWrapper);
		if(!ListUtil.isEmpty(authedSheets))
		{
			for (int i = 0; i < authedSheets.size(); i++) {
				authed.add(String.valueOf(authedSheets.get(i).getSheetId()));
			}
		}
		//获取所有的报表类型
		QueryWrapper<ReportType> reportTypeQueryWrapper = new QueryWrapper<ReportType>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			reportTypeQueryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		reportTypeQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportType> reportTypes = this.iReportTypeService.list(reportTypeQueryWrapper);
		List<ReportTreeDto> treeData = new ArrayList<ReportTreeDto>();
		if(!ListUtil.isEmpty(reportTypes))
		{
			ReportTreeDto reportTreeDto = null;
			List<Long> reportTypeIds = new ArrayList<>();
			for (int i = 0; i < reportTypes.size(); i++) {
				reportTreeDto = new ReportTreeDto();
				reportTreeDto.setReportId("type-"+reportTypes.get(i).getId());
				reportTreeDto.setReportName(reportTypes.get(i).getReportTypeName());
				reportTreeDto.setChildren(new ArrayList<ReportTreeDto>());
				reportTypeIds.add(reportTypes.get(i).getId());
				treeData.add(reportTreeDto);
			}
			QueryWrapper<ReportTpl> reportTplQueryWrapper = new QueryWrapper<ReportTpl>();
			reportTplQueryWrapper.in("report_type", reportTypeIds);
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				reportTplQueryWrapper.eq("merchant_no", model.getMerchantNo());
			}
			reportTplQueryWrapper.eq("view_auth", 2);
			reportTplQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<ReportTpl> reportTpls = iReportTplService.list(reportTplQueryWrapper);
			Map<Long, List<ReportTpl>> reportTplMap = null;
			Map<Long, List<ReportTplSheet>> reportSheetMap = null;
			List<Long> reportIds = new ArrayList<Long>();
			if(!ListUtil.isEmpty(reportTpls))
			{
				reportTplMap = reportTpls.stream().collect(Collectors.groupingBy(ReportTpl::getReportType));
				for (int i = 0; i < reportTpls.size(); i++) {
					reportIds.add(reportTpls.get(i).getId());
				}
				QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
				sheetQueryWrapper.in("tpl_id", reportIds);
				sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<ReportTplSheet> sheets = this.iReportTplSheetService.list(sheetQueryWrapper);
				
				if(!ListUtil.isEmpty(sheets))
				{
					reportSheetMap = sheets.stream().collect(Collectors.groupingBy(ReportTplSheet::getTplId));
				}
			}
			if(reportTplMap != null)
			{
				for (int i = 0; i < treeData.size(); i++) {
					List<ReportTpl> tpls = reportTplMap.get(Long.parseLong(treeData.get(i).getReportId().split("-")[1]));
					if(!ListUtil.isEmpty(tpls))
					{
						for (int j = 0; j < tpls.size(); j++) {
							ReportTreeDto report = new ReportTreeDto();
							report.setReportId("report-" + String.valueOf(tpls.get(j).getId()));
							report.setReportName(tpls.get(j).getTplName());
							if(ListUtil.isEmpty(treeData.get(i).getChildren()))
							{
								treeData.get(i).setChildren(new ArrayList<>());
							}
							treeData.get(i).getChildren().add(report);
							if(reportSheetMap != null)
							{
								List<ReportTplSheet> sheets = reportSheetMap.get(tpls.get(j).getId());
								if(!ListUtil.isEmpty(sheets))
								{
									for (int k = 0; k < sheets.size(); k++) {
										ReportTreeDto sheet = new ReportTreeDto();
										sheet.setReportId(String.valueOf(sheets.get(k).getId()));
										sheet.setReportName(sheets.get(k).getSheetName());
										if(ListUtil.isEmpty(report.getChildren()))
										{
											report.setChildren(new ArrayList<ReportTreeDto>());
										}
										report.getChildren().add(sheet);
									}
								}
							}
						}
					}
				}
			}
			
		}
		result.setTreeData(treeData);
		result.setAuthed(authed);
		return result;
	}
	
	/**  
	 * @Title: authed
	 * @Description: 报表角色授权
	 * @param mesAuthDto
	 * @return 
	 * @see com.caiyang.api.sysrolereport.ISysRoleReportService#authed(com.caiyang.dto.sysrole.MesAuthDto) 
	 * @author caiyang
	 * @date 2022年7月6日07:58:43
	 */
	@Override
	@Transactional
	public BaseEntity authed(MesAuthDto mesAuthDto) {
		BaseEntity result = new BaseEntity();
		if(ListUtil.isEmpty(mesAuthDto.getAuthed()))
		{
			//删除所有的权限
			UpdateWrapper<SysRoleSheet> updateWrapper = new UpdateWrapper<SysRoleSheet>();
			updateWrapper.eq("role_id", mesAuthDto.getRoleId());
			updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			SysRoleSheet sysRoleSheet = new SysRoleSheet();
			sysRoleSheet.setDelFlag(DelFlagEnum.DEL.getCode());
			this.update(sysRoleSheet, updateWrapper);
		}else {
			//移除所有的报表类型数据
			Iterator<String> iterator = mesAuthDto.getAuthed().iterator();
			while (iterator.hasNext()) {
				String s = iterator.next();
				if(s.contains("type") || s.contains("report"))
				{
					iterator.remove();
				}
			}
			List<String> userAuthed = new ArrayList<String>();//用户已有的权限
			//获取当前有权限的报表
			QueryWrapper<SysRoleSheet> queryWrapper = new QueryWrapper<SysRoleSheet>();
			queryWrapper.eq("role_id", mesAuthDto.getRoleId());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			List<SysRoleSheet> sysRoleSheets = this.list(queryWrapper);
			if (!ListUtil.isEmpty(sysRoleSheets)) {
				for (int i = 0; i < sysRoleSheets.size(); i++) {
					userAuthed.add(String.valueOf(sysRoleSheets.get(i).getSheetId()));
				}
			}
			List<String> addData = ListUtil.getDiffData(mesAuthDto.getAuthed(), userAuthed);//需要添加的权限
			List<String> delData = ListUtil.getDiffData(userAuthed, mesAuthDto.getAuthed());//需要删除的权限
			List<SysRoleSheet> addRports = new ArrayList<SysRoleSheet>();
			if(!ListUtil.isEmpty(delData))
			{
				for (int i = 0; i < delData.size(); i++) {
					UpdateWrapper<SysRoleSheet> updateWrapper = new UpdateWrapper<SysRoleSheet>();
					updateWrapper.eq("role_id", mesAuthDto.getRoleId());
					updateWrapper.eq("sheet_id", delData.get(i));
					updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					SysRoleSheet sysRoleSheet = new SysRoleSheet();
					sysRoleSheet.setDelFlag(DelFlagEnum.DEL.getCode());
					this.update(sysRoleSheet, updateWrapper);
				}
			}
			if(!ListUtil.isEmpty(addData))
			{
				QueryWrapper<ReportTplSheet> sheetQueryWrapper = new QueryWrapper<>();
				sheetQueryWrapper.in("id", addData);
				sheetQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				List<ReportTplSheet> sheets = this.iReportTplSheetService.list(sheetQueryWrapper);
				Map<Long, List<ReportTplSheet>> sheetsMap = sheets.stream().collect(Collectors.groupingBy(ReportTplSheet::getId));
//				iReportTplSheetService.get
				for (int i = 0; i < addData.size(); i++) {
					SysRoleSheet sysRoleSheet = new SysRoleSheet();
					sysRoleSheet.setRoleId(mesAuthDto.getRoleId());
					sysRoleSheet.setSheetId(Long.valueOf(addData.get(i)));
					List<ReportTplSheet> list = sheetsMap.get(Long.valueOf(addData.get(i)));
					if(!ListUtil.isEmpty(list))
					{
						sysRoleSheet.setReportId(list.get(0).getTplId());
					}
					if(this.merchantmode == YesNoEnum.YES.getCode()) {
						sysRoleSheet.setMerchantNo(mesAuthDto.getMerchantNo());
					}
					addRports.add(sysRoleSheet);
				}
			}
			if(!ListUtil.isEmpty(addRports))
			{
				this.saveBatch(addRports);
			}
		}
		result.setStatusMsg(MessageUtil.getValue("info.report.auth"));
		return result;
	}
}