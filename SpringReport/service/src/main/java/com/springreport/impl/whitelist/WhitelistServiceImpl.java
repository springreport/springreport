package com.springreport.impl.whitelist;

import com.springreport.entity.whitelist.Whitelist;
import com.springreport.mapper.whitelist.WhitelistMapper;
import com.springreport.api.whitelist.IWhitelistService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.springreport.util.MessageUtil;
import com.springreport.util.NetworkValidator;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.constants.StatusCode;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: Whitelist服务实现
* @author 
* @date 2026-11-24 10:50:41
* @version V1.0  
 */
@Service
public class WhitelistServiceImpl extends ServiceImpl<WhitelistMapper, Whitelist> implements IWhitelistService {
  
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(Whitelist model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<Whitelist> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(Whitelist model) {
		if(model.getValueType() == 1) {
			if(!NetworkValidator.isIp(model.getValue())) {
				throw new BizException(StatusCode.FAILURE, "非法的IP格式！");
			}
		}else {
			if(!NetworkValidator.isDomain(model.getValue())) {
				throw new BizException(StatusCode.FAILURE, "非法的域名格式！");
			}
		}
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
	public BaseEntity update(Whitelist model) {
		if(model.getValueType() == 1) {
			if(!NetworkValidator.isIp(model.getValue())) {
				throw new BizException(StatusCode.FAILURE, "非法的IP格式！");
			}
		}else {
			if(!NetworkValidator.isDomain(model.getValue())) {
				throw new BizException(StatusCode.FAILURE, "非法的域名格式！");
			}
		}
		
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
		Whitelist whitelist = new Whitelist();
		whitelist.setId(id);
		whitelist.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(whitelist);
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
		List<Whitelist> list = new ArrayList<Whitelist>();
		for (int i = 0; i < ids.size(); i++) {
			Whitelist whitelist = new Whitelist();
			whitelist.setId(ids.get(i));
			whitelist.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(whitelist);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**
	*<p>Title: getWhiteList</p>
	*<p>Description: 获取ip和域名白名单 ipWhitelist domainWhitelist</p>
	* @author caiyang
	* @return
	*/
	@Override
	public JSONObject getWhiteList() {
		JSONObject result = new JSONObject();
		QueryWrapper<Whitelist> queryWrapper = new QueryWrapper<Whitelist>();
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<String> ipWhitelist = new ArrayList<String>();
		List<String> domainWhitelist = new ArrayList<String>();
		List<Whitelist> whitelists = this.list(queryWrapper);
		for (int i = 0; i < whitelists.size(); i++) {
			if(whitelists.get(i).getValueType() == 1) {
				ipWhitelist.add(whitelists.get(i).getValue());
			}else {
				domainWhitelist.add(whitelists.get(i).getValue());
			}
		}
		result.put("ipWhitelist", ipWhitelist);
		result.put("domainWhitelist", domainWhitelist);
		return result;
	}
}