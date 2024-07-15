package com.springreport.impl.reporttplsheet;

import com.springreport.entity.reporttplsheet.ReportTplSheet;
import com.springreport.mapper.reporttplsheet.ReportTplSheetMapper;
import com.springreport.api.reporttplsheet.IReportTplSheetService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;

 /**  
* @Description: ReportTplSheet服务实现
* @author 
* @date 2022-10-18 06:23:34
* @version V1.0  
 */
@Service
public class ReportTplSheetServiceImpl extends ServiceImpl<ReportTplSheetMapper, ReportTplSheet> implements IReportTplSheetService {
  
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(ReportTplSheet model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<ReportTplSheet> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(ReportTplSheet model) {
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
	public BaseEntity update(ReportTplSheet model) {
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
		ReportTplSheet reportTplSheet = new ReportTplSheet();
		reportTplSheet.setId(id);
		reportTplSheet.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(reportTplSheet);
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
		List<ReportTplSheet> list = new ArrayList<ReportTplSheet>();
		for (int i = 0; i < ids.size(); i++) {
			ReportTplSheet reportTplSheet = new ReportTplSheet();
			reportTplSheet.setId(ids.get(i));
			reportTplSheet.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(reportTplSheet);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @MethodName: getAllSheetNames
	 * @Description: 获取所有的sheet名称
	 * @author caiyang
	 * @param id
	 * @return
	 * @see com.springreport.api.reporttplsheet.IReportTplSheetService#getAllSheetNames(java.lang.Long)
	 * @date 2023-12-13 10:37:59 
	 */
	@Override
	public List<String> getAllSheetNames(Long tplId) {
		List<String> result = new ArrayList<>();
		QueryWrapper<ReportTplSheet> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("tpl_id", tplId);
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportTplSheet> reportTplSheets = this.list(queryWrapper);
		if(!ListUtil.isEmpty(reportTplSheets))
		{
			for (int i = 0; i < reportTplSheets.size(); i++) {
				result.add(reportTplSheets.get(i).getSheetName());
			}
		}
		return result;
	}
}