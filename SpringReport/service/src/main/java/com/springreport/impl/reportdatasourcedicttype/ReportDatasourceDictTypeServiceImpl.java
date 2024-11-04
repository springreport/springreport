package com.springreport.impl.reportdatasourcedicttype;

import com.springreport.entity.reportdatasourcedictdata.ReportDatasourceDictData;
import com.springreport.entity.reportdatasourcedicttype.ReportDatasourceDictType;
import com.springreport.mapper.reportdatasourcedicttype.ReportDatasourceDictTypeMapper;
import com.springreport.api.reportdatasourcedictdata.IReportDatasourceDictDataService;
import com.springreport.api.reportdatasourcedicttype.IReportDatasourceDictTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.constants.StatusCode;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: ReportDatasourceDictType服务实现
* @author 
* @date 2022-11-21 01:46:20
* @version V1.0  
 */
@Service
public class ReportDatasourceDictTypeServiceImpl extends ServiceImpl<ReportDatasourceDictTypeMapper, ReportDatasourceDictType> implements IReportDatasourceDictTypeService {
  
	@Value("${merchantmode}")
    private Integer merchantmode;
	
	@Autowired
	private IReportDatasourceDictDataService iReportDatasourceDictDataService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(ReportDatasourceDictType model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<ReportDatasourceDictType> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(ReportDatasourceDictType model) {
		BaseEntity result = new BaseEntity();
		//校验类型是否已经存在
		QueryWrapper<ReportDatasourceDictType> queryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("datasource_id", model.getDatasourceId());
		queryWrapper.eq("dict_type", model.getDictType());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportDatasourceDictType isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该字典类型"}));
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
	public BaseEntity update(ReportDatasourceDictType model) {
		BaseEntity result = new BaseEntity();
		//校验类型是否已经存在
		QueryWrapper<ReportDatasourceDictType> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("id", model.getId());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("datasource_id", model.getDatasourceId());
		queryWrapper.eq("dict_type", model.getDictType());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportDatasourceDictType isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该字典类型"}));
		}
		ReportDatasourceDictType reportDatasourceDictType = this.getById(model.getId());
		this.updateById(model);
		//更新对应的字典值
		UpdateWrapper<ReportDatasourceDictData> updateWrapper = new UpdateWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			updateWrapper.eq("merchant_no", model.getMerchantNo());
		}
		updateWrapper.eq("datasource_id", reportDatasourceDictType.getDatasourceId());
		updateWrapper.eq("dict_type", reportDatasourceDictType.getDictType());
		updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportDatasourceDictData update = new ReportDatasourceDictData();
		update.setDictType(model.getDictType());
		this.iReportDatasourceDictDataService.update(update, updateWrapper);
		redisUtil.del(RedisPrefixEnum.REPORTDICT.getCode() + reportDatasourceDictType.getDatasourceId() + "_" + reportDatasourceDictType.getDictType());
		//更新缓存
		QueryWrapper<ReportDatasourceDictData> dictDataQueryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			dictDataQueryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		dictDataQueryWrapper.eq("datasource_id", model.getDatasourceId());
		dictDataQueryWrapper.eq("dict_type", model.getDictType());
		dictDataQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportDatasourceDictData> list = this.iReportDatasourceDictDataService.list(dictDataQueryWrapper);
		redisUtil.set(RedisPrefixEnum.REPORTDICT.getCode() + model.getDatasourceId() + "_" + model.getDictType(), list);
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
		ReportDatasourceDictType reportDatasourceDictType = new ReportDatasourceDictType();
		reportDatasourceDictType.setId(id);
		reportDatasourceDictType.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(reportDatasourceDictType);
		reportDatasourceDictType = this.getById(id);
		redisUtil.del(RedisPrefixEnum.REPORTDICT.getCode() + reportDatasourceDictType.getDatasourceId() + "_" + reportDatasourceDictType.getDictType());
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
		List<ReportDatasourceDictType> list = new ArrayList<ReportDatasourceDictType>();
		List<String> keys = new ArrayList<>();
		for (int i = 0; i < ids.size(); i++) {
			ReportDatasourceDictType reportDatasourceDictType = new ReportDatasourceDictType();
			reportDatasourceDictType.setId(ids.get(i));
			reportDatasourceDictType.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(reportDatasourceDictType);
		}
		QueryWrapper<ReportDatasourceDictType> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("id", ids);
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportDatasourceDictType> datas = this.list(queryWrapper);
		if(ListUtil.isNotEmpty(datas)) {
			for (int i = 0; i < datas.size(); i++) {
				String key = RedisPrefixEnum.REPORTDICT.getCode() + datas.get(i).getDatasourceId() + "_" + datas.get(i).getDictType();
				keys.add(key);
			}
			redisUtil.del(keys);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @MethodName: getDatasourceDictTypes
	 * @Description: 获取数据源字典类型
	 * @author caiyang
	 * @param model
	 * @return
	 * @see com.springreport.api.reportdatasourcedicttype.IReportDatasourceDictTypeService#getDatasourceDictTypes(com.springreport.entity.reportdatasourcedicttype.ReportDatasourceDictType)
	 * @date 2022-11-21 03:55:48 
	 */  
	@Override
	public List<ReportDatasourceDictType> getDatasourceDictTypes(ReportDatasourceDictType model) {
		QueryWrapper<ReportDatasourceDictType> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("datasource_id", model.getDatasourceId());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportDatasourceDictType> result = this.list(queryWrapper);
		return result;
	}
}