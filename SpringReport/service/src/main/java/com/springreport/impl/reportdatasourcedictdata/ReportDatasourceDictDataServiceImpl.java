package com.springreport.impl.reportdatasourcedictdata;

import com.springreport.entity.reportdatasourcedictdata.ReportDatasourceDictData;
import com.springreport.mapper.reportdatasourcedictdata.ReportDatasourceDictDataMapper;
import com.springreport.api.reportdatasourcedictdata.IReportDatasourceDictDataService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
* @Description: ReportDatasourceDictData服务实现
* @author 
* @date 2022-11-21 01:46:25
* @version V1.0  
 */
@Service
public class ReportDatasourceDictDataServiceImpl extends ServiceImpl<ReportDatasourceDictDataMapper, ReportDatasourceDictData> implements IReportDatasourceDictDataService {
  
	@Autowired
	private RedisUtil redisUtil;
	
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
	public PageEntity tablePagingQuery(ReportDatasourceDictData model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<ReportDatasourceDictData> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(ReportDatasourceDictData model) {
		BaseEntity result = new BaseEntity();
		//校验是否已经存在
		QueryWrapper<ReportDatasourceDictData> queryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("datasource_id", model.getDatasourceId());
		queryWrapper.eq("dict_type", model.getDictType());
		queryWrapper.eq("dict_value", model.getDictValue());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportDatasourceDictData isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该字典键值"}));
		}
		this.save(model);
		result.setStatusMsg(MessageUtil.getValue("info.insert"));
		//更新缓存
		QueryWrapper<ReportDatasourceDictData> dictDataQueryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			dictDataQueryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		dictDataQueryWrapper.eq("datasource_id", model.getDatasourceId());
		dictDataQueryWrapper.eq("dict_type", model.getDictType());
		dictDataQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportDatasourceDictData> list = this.list(dictDataQueryWrapper);
		redisUtil.set(RedisPrefixEnum.REPORTDICT.getCode() + model.getDatasourceId() + "_" + model.getDictType(), list);
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
	public BaseEntity update(ReportDatasourceDictData model) {
		BaseEntity result = new BaseEntity();
		//校验是否已经存在
		QueryWrapper<ReportDatasourceDictData> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("id", model.getId());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("datasource_id", model.getDatasourceId());
		queryWrapper.eq("dict_type", model.getDictType());
		queryWrapper.eq("dict_value", model.getDictValue());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportDatasourceDictData isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该字典键值"}));
		}
		this.updateById(model);
		result.setStatusMsg(MessageUtil.getValue("info.update"));
		//更新缓存
		QueryWrapper<ReportDatasourceDictData> dictDataQueryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			dictDataQueryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		dictDataQueryWrapper.eq("datasource_id", model.getDatasourceId());
		dictDataQueryWrapper.eq("dict_type", model.getDictType());
		dictDataQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportDatasourceDictData> list = this.list(dictDataQueryWrapper);
		if(!ListUtil.isEmpty(list))
		{
			redisUtil.set(RedisPrefixEnum.REPORTDICT.getCode() + model.getDatasourceId() + "_" + model.getDictType(), list);
		}
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
		ReportDatasourceDictData reportDatasourceDictData = new ReportDatasourceDictData();
		reportDatasourceDictData.setId(id);
		reportDatasourceDictData.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(reportDatasourceDictData);
		BaseEntity result = new BaseEntity();
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		//更新缓存
		ReportDatasourceDictData model = this.getById(id);
		QueryWrapper<ReportDatasourceDictData> dictDataQueryWrapper = new QueryWrapper<>();
		dictDataQueryWrapper.eq("datasource_id", model.getDatasourceId());
		dictDataQueryWrapper.eq("dict_type", model.getDictType());
		dictDataQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportDatasourceDictData> list = this.list(dictDataQueryWrapper);
		if(!ListUtil.isEmpty(list))
		{
			redisUtil.set(RedisPrefixEnum.REPORTDICT.getCode() + model.getDatasourceId() + "_" + model.getDictType(), list);
		}
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
		List<ReportDatasourceDictData> list = new ArrayList<ReportDatasourceDictData>();
		for (int i = 0; i < ids.size(); i++) {
			ReportDatasourceDictData reportDatasourceDictData = new ReportDatasourceDictData();
			reportDatasourceDictData.setId(ids.get(i));
			reportDatasourceDictData.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(reportDatasourceDictData);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		//更新缓存
		ReportDatasourceDictData model = this.getById(ids.get(0));
		QueryWrapper<ReportDatasourceDictData> dictDataQueryWrapper = new QueryWrapper<>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			dictDataQueryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		dictDataQueryWrapper.eq("datasource_id", model.getDatasourceId());
		dictDataQueryWrapper.eq("dict_type", model.getDictType());
		dictDataQueryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportDatasourceDictData> datas = this.list(dictDataQueryWrapper);
		if(!ListUtil.isEmpty(datas))
		{
			redisUtil.set(RedisPrefixEnum.REPORTDICT.getCode() + model.getDatasourceId() + "_" + model.getDictType(), datas);
		}
		
		return result;
	}


	/**  
	 * @MethodName: getDictDatas
	 * @Description: 获取数据源数据字典值
	 * @author caiyang
	 * @param model
	 * @return
	 * @see com.springreport.api.reportdatasourcedictdata.IReportDatasourceDictDataService#getDictDatas(com.springreport.entity.reportdatasourcedictdata.ReportDatasourceDictData)
	 * @date 2022-11-21 04:20:29 
	 */  
	@Override
	public List<ReportDatasourceDictData> getDictDatas(ReportDatasourceDictData model) {
		List<ReportDatasourceDictData> result = (List<ReportDatasourceDictData>) redisUtil.get(RedisPrefixEnum.REPORTDICT.getCode() + model.getDatasourceId() + "_" + model.getDictType());
		if(ListUtil.isEmpty(result))
		{
			QueryWrapper<ReportDatasourceDictData> queryWrapper = new QueryWrapper<>();
			if(this.merchantmode == YesNoEnum.YES.getCode()) {
				queryWrapper.eq("merchant_no", model.getMerchantNo());
			}
			queryWrapper.eq("datasource_id", model.getDatasourceId());
			queryWrapper.eq("dict_type", model.getDictType());
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			result = this.list(queryWrapper);
			if(!ListUtil.isEmpty(result))
			{
				redisUtil.set(RedisPrefixEnum.REPORTDICT.getCode() + model.getDatasourceId() + "_" + model.getDictType(), result);
			}
		}
		return result;
	}
}