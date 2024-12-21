package com.springreport.impl.reporttype;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springreport.api.reporttype.IReportTypeService;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.constants.StatusCode;
import com.springreport.entity.reporttype.ReportType;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;
import com.springreport.mapper.reporttype.ReportTypeMapper;
import com.springreport.util.MessageUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

 /**  
* @Description: ReportType服务实现
* @author 
* @date 2021-02-09 08:59:59
* @version V1.0  
 */
@Service
public class ReportTypeServiceImpl extends ServiceImpl<ReportTypeMapper, ReportType> implements IReportTypeService {
  
	@Value("${merchantmode}")
    private Integer merchantmode;
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(ReportType model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<ReportType> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(ReportType model) {
		BaseEntity result = new BaseEntity();
		//校验报表类型是否已经存在
		QueryWrapper<ReportType> queryWrapper = new QueryWrapper<ReportType>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("report_type_name", model.getReportTypeName());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		queryWrapper.eq("type", model.getType());
		ReportType isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该目录"}));
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
	public BaseEntity update(ReportType model) {
		BaseEntity result = new BaseEntity();
		//校验报表类型是否已经存在
		QueryWrapper<ReportType> queryWrapper = new QueryWrapper<ReportType>();
		queryWrapper.ne("id", model.getId());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("report_type_name", model.getReportTypeName());
		queryWrapper.eq("type", model.getType());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportType isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该目录"}));
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
		ReportType reportType = new ReportType();
		reportType.setId(id);
		reportType.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(reportType);
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
		List<ReportType> list = new ArrayList<ReportType>();
		for (int i = 0; i < ids.size(); i++) {
			ReportType reportType = new ReportType();
			reportType.setId(ids.get(i));
			reportType.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(reportType);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**
	*<p>Title: getReportType</p>
	*<p>Description: 获取报表类型</p>
	* @author caiyang
	* @return
	*/
	@Override
	public List<ReportType> getReportType(ReportType reportType) {
		QueryWrapper<ReportType> queryWrapper = new QueryWrapper<ReportType>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", reportType.getMerchantNo());
		}
		queryWrapper.eq("type", reportType.getType());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportType> result = this.list(queryWrapper);
		return result;
	}
}