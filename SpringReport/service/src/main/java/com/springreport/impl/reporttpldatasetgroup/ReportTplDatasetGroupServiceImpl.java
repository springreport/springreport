package com.springreport.impl.reporttpldatasetgroup;

import com.springreport.entity.doctpl.DocTpl;
import com.springreport.entity.reporttpldataset.ReportTplDataset;
import com.springreport.entity.reporttpldatasetgroup.ReportTplDatasetGroup;
import com.springreport.mapper.reporttpldatasetgroup.ReportTplDatasetGroupMapper;
import com.springreport.api.reporttpldataset.IReportTplDatasetService;
import com.springreport.api.reporttpldatasetgroup.IReportTplDatasetGroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springreport.util.MessageUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.constants.StatusCode;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: ReportTplDatasetGroup服务实现
* @author 
* @date 2024-12-13 08:27:12
* @version V1.0  
 */
@Service
public class ReportTplDatasetGroupServiceImpl extends ServiceImpl<ReportTplDatasetGroupMapper, ReportTplDatasetGroup> implements IReportTplDatasetGroupService {
  
	@Autowired
	private IReportTplDatasetService iReportTplDatasetService;
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public List<ReportTplDatasetGroup> tablePagingQuery(ReportTplDatasetGroup model) {
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		QueryWrapper<ReportTplDatasetGroup> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("tpl_id", model.getTplId());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplDatasetGroup> list = this.baseMapper.selectList(queryWrapper);
		return list;
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
		return this.getById(id);
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
	public BaseEntity insert(ReportTplDatasetGroup model) {
		BaseEntity result = new BaseEntity();
		//校验分组名称否已经存在
		QueryWrapper<ReportTplDatasetGroup> queryWrapper = new QueryWrapper<ReportTplDatasetGroup>();
		queryWrapper.eq("tpl_id", model.getTplId());
		queryWrapper.eq("group_name", model.getGroupName());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportTplDatasetGroup isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"分组名称"}));
		}
		this.save(model);
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
	public BaseEntity update(ReportTplDatasetGroup model) {
		BaseEntity result = new BaseEntity();
		//校验分组名称否已经存在
		QueryWrapper<ReportTplDatasetGroup> queryWrapper = new QueryWrapper<ReportTplDatasetGroup>();
		queryWrapper.ne("id", model.getId());
		queryWrapper.eq("tpl_id", model.getTplId());
		queryWrapper.eq("group_name", model.getGroupName());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportTplDatasetGroup isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"分组名称"}));
		}
		this.updateById(model);
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
		QueryWrapper<ReportTplDataset> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("group_id", id);
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		long count = this.iReportTplDatasetService.count(queryWrapper);
		if(count > 0) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.delete.notemptygroup"));
		}
		ReportTplDatasetGroup reportTplDatasetGroup = new ReportTplDatasetGroup();
		reportTplDatasetGroup.setId(id);
		reportTplDatasetGroup.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(reportTplDatasetGroup);
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
		List<ReportTplDatasetGroup> list = new ArrayList<ReportTplDatasetGroup>();
		for (int i = 0; i < ids.size(); i++) {
			ReportTplDatasetGroup reportTplDatasetGroup = new ReportTplDatasetGroup();
			reportTplDatasetGroup.setId(ids.get(i));
			reportTplDatasetGroup.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(reportTplDatasetGroup);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}
}