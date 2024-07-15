package com.springreport.impl.sysroleapi;

import com.springreport.entity.sysapi.SysApi;
import com.springreport.entity.sysroleapi.SysRoleApi;
import com.springreport.mapper.sysroleapi.SysRoleApiMapper;
import com.springreport.api.sysroleapi.ISysRoleApiService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.springreport.util.MessageUtil;

import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;

 /**  
* @Description: SysRoleApi服务实现
* @author 
* @date 2021-06-15 07:11:53
* @version V1.0  
 */
@Service
public class SysRoleApiServiceImpl extends ServiceImpl<SysRoleApiMapper, SysRoleApi> implements ISysRoleApiService {
  
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(SysRoleApi model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<SysRoleApi> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(SysRoleApi model) {
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
	public BaseEntity update(SysRoleApi model) {
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
		SysRoleApi sysRoleApi = new SysRoleApi();
		sysRoleApi.setId(id);
		sysRoleApi.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(sysRoleApi);
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
		List<SysRoleApi> list = new ArrayList<SysRoleApi>();
		for (int i = 0; i < ids.size(); i++) {
			SysRoleApi sysRoleApi = new SysRoleApi();
			sysRoleApi.setId(ids.get(i));
			sysRoleApi.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(sysRoleApi);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @Title: getApisByRole
	 * @Description: 根据角色获取接口权限
	 * @param roleId
	 * @return 
	 * @see com.caiyang.api.sysroleapi.ISysRoleApiService#getApisByRole(java.lang.Long) 
	 * @author caiyang
	 * @date 2021-06-16 05:18:31 
	 */
	@Override
	public List<SysApi> getApisByRole(JSONObject jsonObject) {
		List<SysApi> result = this.baseMapper.getApisByRole(jsonObject);
		return result;
	}
}