package com.springreport.impl.qrtz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.util.StringUtil;
import com.springreport.api.qrtzreportdetail.IQrtzReportDetailService;
import com.springreport.api.reporttpl.IReportTplService;
import com.springreport.api.sysuser.ISysUserService;
import com.springreport.api.sysuserdept.ISysUserDeptService;
import com.springreport.api.sysuserrole.ISysUserRoleService;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.reporttpl.MesGenerateReportDto;
import com.springreport.entity.qrtzreportdetail.QrtzReportDetail;
import com.springreport.entity.sysuser.SysUser;
import com.springreport.entity.sysuserdept.SysUserDept;
import com.springreport.entity.sysuserrole.SysUserRole;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.qrtz.MessageHandler;

@Service
public class ReportExportTaskHandler implements MessageHandler{
	
	@Autowired
	private IReportTplService iReportTplService;
	
	@Autowired
	private IQrtzReportDetailService iQrtzReportDetailService;
	
	@Autowired
	private ISysUserService iSysUserService;
	
	@Autowired
	private ISysUserRoleService iSysUserRoleService;
	
	@Autowired
	private ISysUserDeptService iSysUserDeptService;

	@Override
	public void handlerMessage(String jobData,Long taskId) throws Exception {
		List<Map<String, Object>> searchData = new ArrayList<>();
		Map<String, Object> params = new HashMap<>();
		if(StringUtil.isNotEmpty(jobData))
		{
 			params.put("params", JSON.parseArray(jobData));
		}else {
			params.put("params", new JSONArray());
		}
		searchData.add(params);
		Map<String, Integer> pagination = new HashMap<>();
		pagination.put("currentPage", 1);
		pagination.put("pageCount", 100);
		QrtzReportDetail qrtzReportDetail = iQrtzReportDetailService.getById(taskId);
		MesGenerateReportDto mesGenerateReportDto = new MesGenerateReportDto();
		mesGenerateReportDto.setTplId(qrtzReportDetail.getTplId());
		mesGenerateReportDto.setTaskId(taskId);
		mesGenerateReportDto.setExportType(qrtzReportDetail.getExportType());
		mesGenerateReportDto.setSearchData(searchData);
		mesGenerateReportDto.setSendEmail(qrtzReportDetail.getEmail());
		mesGenerateReportDto.setJobName(qrtzReportDetail.getJobName());
		mesGenerateReportDto.setPagination(pagination);
		UserInfoDto userInfoDto = new UserInfoDto();
		userInfoDto.setIsAdmin(YesNoEnum.YES.getCode());
		if(qrtzReportDetail != null && qrtzReportDetail.getCreator() != null) {
			SysUser sysUser = this.iSysUserService.getById(qrtzReportDetail.getCreator());
			if(sysUser != null) {
				userInfoDto.setUserId(sysUser.getId());
				userInfoDto.setUserName(sysUser.getUserName());
				userInfoDto.setMerchantNo(sysUser.getMerchantNo());
				userInfoDto.setIsAdmin(sysUser.getIsAdmin());
				QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
				userRoleQueryWrapper.eq("user_id", sysUser.getId());
				userRoleQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				SysUserRole sysUserRole = this.iSysUserRoleService.getOne(userRoleQueryWrapper,false);
				if(sysUserRole != null) {
					userInfoDto.setRoleId(sysUserRole.getRoleId());
				}
				QueryWrapper<SysUserDept> userDeptQueryWrapper = new QueryWrapper<>();
				userDeptQueryWrapper.eq("user_id", sysUser.getId());
				userDeptQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				SysUserDept sysUserDept = this.iSysUserDeptService.getOne(userDeptQueryWrapper, false);
				if(sysUserDept != null) {
					userInfoDto.setDeptId(sysUserDept.getDeptId());
				}
				
			}
		}
		iReportTplService.reportTask(mesGenerateReportDto, userInfoDto);
	}

}
