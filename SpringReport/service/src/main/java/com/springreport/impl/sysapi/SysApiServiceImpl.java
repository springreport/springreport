package com.springreport.impl.sysapi;

import com.springreport.entity.sysapi.SysApi;
import com.springreport.mapper.sysapi.SysApiMapper;
import com.springreport.api.sysapi.ISysApiService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.springreport.util.MessageUtil;
import com.springreport.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.dto.sysmenu.MenuTreeDto;
import com.springreport.dto.sysmenu.MesGetAuthTreeDto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.SqlOrderEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: SysApi服务实现
* @author 
* @date 2021-06-15 07:11:40
* @version V1.0  
 */
@Service
public class SysApiServiceImpl extends ServiceImpl<SysApiMapper, SysApi> implements ISysApiService {
  
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(SysApi model) {
		PageEntity result = new PageEntity();
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("sort", SqlOrderEnum.ASC.getCode());
		String orderSql = StringUtil.formatOrderSql(map);
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		model.setOrderSql(orderSql);
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<SysApi> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(SysApi model) {
		BaseEntity result = new BaseEntity();
		//校验code是否已经存在
		QueryWrapper<SysApi> queryWrapper = new QueryWrapper<SysApi>();
		queryWrapper.eq("api_code", model.getApiCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysApi exist = this.getOne(queryWrapper,false);
		if(exist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"接口标识"}));
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
	public BaseEntity update(SysApi model) {
		BaseEntity result = new BaseEntity();
		//校验code是否已经存在
		QueryWrapper<SysApi> queryWrapper = new QueryWrapper<SysApi>();
		queryWrapper.ne("id", model.getId());
		queryWrapper.eq("api_code", model.getApiCode());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		SysApi exist = this.getOne(queryWrapper,false);
		if(exist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"接口标识"}));
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
		SysApi sysApi = new SysApi();
		sysApi.setId(id);
		sysApi.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(sysApi);
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
		List<SysApi> list = new ArrayList<SysApi>();
		for (int i = 0; i < ids.size(); i++) {
			SysApi sysApi = new SysApi();
			sysApi.setId(ids.get(i));
			sysApi.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(sysApi);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @Title: getMenuApis
	 * @Description:  获取菜单下需授权的接口
	 * @param mesGetAuthTreeDto
	 * @return 
	 * @see com.caiyang.api.sysapi.ISysApiService#getMenuApis(com.caiyang.dto.sysmenu.MesGetAuthTreeDto) 
	 * @author caiyang
	 * @date 2021-06-16 06:57:53 
	 */
	@Override
	public List<MenuTreeDto> getMenuApis(MesGetAuthTreeDto mesGetAuthTreeDto) {
		List<MenuTreeDto> result = this.baseMapper.getMenuApis(mesGetAuthTreeDto);
		return result;
	}
}