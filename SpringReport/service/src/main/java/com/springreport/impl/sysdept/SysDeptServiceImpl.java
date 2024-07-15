package com.springreport.impl.sysdept;

import com.springreport.entity.sysdept.SysDept;
import com.springreport.mapper.sysdept.SysDeptMapper;
import com.springreport.api.sysdept.ISysDeptService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.constants.StatusCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.SqlOrderEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: SysDept服务实现
* @author 
* @date 2023-12-22 05:18:48
* @version V1.0  
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {
  
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
	public List<SysDept> tablePagingQuery(SysDept model) {
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("dept_sort", SqlOrderEnum.ASC.getCode());
		map.put("id", SqlOrderEnum.ASC.getCode());
		String orderSql = StringUtil.formatOrderSql(map);
		model.setOrderSql(orderSql);
		List<SysDept> list = this.baseMapper.searchDataLike(model);
		List<SysDept> resultList = new ArrayList<>();
		if(!ListUtil.isEmpty(list))
		{
			Map<Long, SysDept> entityMap = new HashMap<>();
			Map<Long, List<SysDept>> childrenMap = new HashMap<>();
			for (SysDept entity : list){
				entity.setChildren(new ArrayList<>());
				entityMap.put(entity.getId(),entity);
				childrenMap.put(entity.getId(), (List<SysDept>) entity.getChildren());
			}
			// 组装成数结构列表
			for (SysDept entity : list){
				SysDept parentEntity = entityMap.get(entity.getParentId());
				// 如果查询出的列表里面该节点的没有父节点说明是顶级节点
				if(parentEntity == null)
				{
					// 将顶级节点加入结果集中
			         resultList.add(entity);
			         continue;
				}
				// 把自己加到父节点对象里面去
				childrenMap.get(parentEntity.getId()).add(entity);
			}
		}
		return resultList;
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
	public BaseEntity insert(SysDept model) {
		BaseEntity result = new BaseEntity();
		QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("dept_name", model.getDeptName().trim());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		SysDept isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该岗位编码"}));
		}
		if(model.getParentId() == null)
		{
			model.setParentIdList("0");
		}else {
			SysDept parentDept = this.getById(model.getParentId());
			if(parentDept == null)
			{
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"上级部门"}));
			}
			model.setParentIdList(parentDept.getParentIdList()+","+model.getParentId());
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
	public BaseEntity update(SysDept model) {
		BaseEntity result = new BaseEntity();
		QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("id", model.getId());
		queryWrapper.eq("dept_name", model.getDeptName().trim());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		SysDept oldDept = this.getById(model.getId());
		String oldParentIdList = oldDept.getParentIdList();
		SysDept isExist = this.getOne(queryWrapper, false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该岗位编码"}));
		}
		if(model.getParentId() == null)
		{
			model.setParentIdList("0");
		}else {
			if(model.getParentId() == null || model.getParentId() == 0)
			{
				model.setParentIdList("0");
			}else {
				SysDept parentDept = this.getById(model.getParentId());
				if(parentDept == null)
				{
					throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.notexist", new String[] {"上级部门"}));
				}
				model.setParentIdList(parentDept.getParentIdList()+","+model.getParentId());
			}
		}
		this.updateById(model);
		List<SysDept> subDepts = new ArrayList<>();
		this.getAllSubDeptIds(model.getId(), subDepts);
		if(!ListUtil.isEmpty(subDepts))
		{
			List<SysDept> depts = new ArrayList<>();
			SysDept dept = null;
			for (int i = 0; i < subDepts.size(); i++) {
				dept = new SysDept();
				dept.setId(subDepts.get(i).getId());
				dept.setParentIdList(subDepts.get(i).getParentIdList().replaceFirst(oldParentIdList, model.getParentIdList()));
				depts.add(dept);
			}
			this.updateBatchById(depts);
		}
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
		SysDept sysDept = new SysDept();
		sysDept.setId(id);
		sysDept.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(sysDept);
		//删除子部门
		List<SysDept> subDepts = new ArrayList<>();
		this.getAllSubDeptIds(id, subDepts);
		if(!ListUtil.isEmpty(subDepts))
		{
			List<SysDept> delDepts = new ArrayList<>();
			SysDept dept = null;
			for (int i = 0; i < subDepts.size(); i++) {
				dept = new SysDept();
				dept.setId(subDepts.get(i).getId());
				dept.setDelFlag(DelFlagEnum.DEL.getCode());
				delDepts.add(dept);
			}
			this.updateBatchById(delDepts);
		}
		BaseEntity result = new BaseEntity();
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}
	
	/**  
	 * @MethodName: getAllSubDeptIds
	 * @Description: 获取所有的子部门
	 * @author caiyang
	 * @param parentId
	 * @param sysDepts
	 * @see com.springreport.api.sysdept.ISysDeptService#getAllSubDeptIds(java.lang.Long, java.util.List)
	 * @date 2023-12-26 02:34:43 
	 */
	@Override
	public void getAllSubDeptIds(Long parentId,List<SysDept> sysDepts) {
		QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<SysDept> depts = this.list(queryWrapper);
		if(!ListUtil.isEmpty(depts))
		{
			sysDepts.addAll(depts);
			for (int i = 0; i < depts.size(); i++) {
				this.getAllSubDeptIds(depts.get(i).getId(), sysDepts);
			}
		}
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
		List<SysDept> list = new ArrayList<SysDept>();
		for (int i = 0; i < ids.size(); i++) {
			SysDept sysDept = new SysDept();
			sysDept.setId(ids.get(i));
			sysDept.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(sysDept);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}
}