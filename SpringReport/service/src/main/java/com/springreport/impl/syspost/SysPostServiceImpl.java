package com.springreport.impl.syspost;

import com.springreport.entity.syspost.SysPost;
import com.springreport.mapper.syspost.SysPostMapper;
import com.springreport.api.syspost.ISysPostService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.MessageUtil;
import com.springreport.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.constants.StatusCode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.SqlOrderEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: SysPost服务实现
* @author 
* @date 2023-12-22 05:19:10
* @version V1.0  
 */
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements ISysPostService {
  
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
	public PageEntity tablePagingQuery(SysPost model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("post_sort", SqlOrderEnum.ASC.getCode());
		String orderSql = StringUtil.formatOrderSql(map);
		model.setOrderSql(orderSql);
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<SysPost> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(SysPost model) {
		BaseEntity result = new BaseEntity();
		QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("post_code", model.getPostCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		SysPost isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该岗位编码"}));
		}
		model.setMerchantNo(model.getMerchantNo());
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
	public BaseEntity update(SysPost model) {
		BaseEntity result = new BaseEntity();
		QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("id", model.getId());
		queryWrapper.eq("post_code", model.getPostCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		SysPost isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该岗位编码"}));
		}
		model.setMerchantNo(model.getMerchantNo());
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
		SysPost sysPost = new SysPost();
		sysPost.setId(id);
		sysPost.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(sysPost);
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
		List<SysPost> list = new ArrayList<SysPost>();
		for (int i = 0; i < ids.size(); i++) {
			SysPost sysPost = new SysPost();
			sysPost.setId(ids.get(i));
			sysPost.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(sysPost);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	@Override
	public List<SysPost> getSysPosts(SysPost sysPost) {
		QueryWrapper<SysPost> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", sysPost.getMerchantNo());
		}
		List<SysPost> result = this.list(queryWrapper);
		return result;
	}
}