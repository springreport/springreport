package com.springreport.impl.reporttpldatasource;

import com.springreport.entity.reporttpldatasource.ReportTplDatasource;
import com.springreport.mapper.reporttpldatasource.ReportTplDatasourceMapper;
import com.springreport.api.reporttpldatasource.IReportTplDatasourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import com.springreport.util.MessageUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.dto.reporttpldatasource.ReportTplDatasourceDto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;

 /**  
* @Description: ReportTplDatasource服务实现
* @author 
* @date 2021-02-13 11:16:43
* @version V1.0  
 */
@Service
public class ReportTplDatasourceServiceImpl extends ServiceImpl<ReportTplDatasourceMapper, ReportTplDatasource> implements IReportTplDatasourceService {
  
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(ReportTplDatasource model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<ReportTplDatasource> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(ReportTplDatasource model) {
		BaseEntity result = new BaseEntity();
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
	public BaseEntity update(ReportTplDatasource model) {
		BaseEntity result = new BaseEntity();
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
		ReportTplDatasource reportTplDatasource = new ReportTplDatasource();
		reportTplDatasource.setId(id);
		reportTplDatasource.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(reportTplDatasource);
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
		List<ReportTplDatasource> list = new ArrayList<ReportTplDatasource>();
		for (int i = 0; i < ids.size(); i++) {
			ReportTplDatasource reportTplDatasource = new ReportTplDatasource();
			reportTplDatasource.setId(ids.get(i));
			reportTplDatasource.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(reportTplDatasource);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**
	*<p>Title: getReportTplDatasoure</p>
	*<p>Description: 获取报表模板数据源</p>
	* @author caiyang
	* @param reportTplDatasource
	* @return
	*/
	@Override
	public List<ReportTplDatasourceDto> getReportTplDatasoure(ReportTplDatasource reportTplDatasource) {
		List<ReportTplDatasourceDto> result = this.baseMapper.getReportTplDatasources(reportTplDatasource);
		return result;
	}

}