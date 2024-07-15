package com.springreport.impl.onlinetplsheet;

import com.springreport.entity.luckysheetonlinecell.LuckysheetOnlineCell;
import com.springreport.entity.onlinetplsheet.OnlineTplSheet;
import com.springreport.mapper.luckysheetonlinecell.LuckysheetOnlineCellMapper;
import com.springreport.mapper.onlinetplsheet.OnlineTplSheetMapper;
import com.springreport.api.onlinetplsheet.IOnlineTplSheetService;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.constants.Constants;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;

 /**  
* @Description: OnlineTplSheet服务实现
* @author 
* @date 2023-02-06 08:03:30
* @version V1.0  
 */
@Service
public class OnlineTplSheetServiceImpl extends ServiceImpl<OnlineTplSheetMapper, OnlineTplSheet> implements IOnlineTplSheetService {
  
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private LuckysheetOnlineCellMapper luckysheetOnlineCellMapper;
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(OnlineTplSheet model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<OnlineTplSheet> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(OnlineTplSheet model) {
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
	public BaseEntity update(OnlineTplSheet model) {
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
		OnlineTplSheet onlineTplSheet = new OnlineTplSheet();
		onlineTplSheet.setId(id);
		onlineTplSheet.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(onlineTplSheet);
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
		List<OnlineTplSheet> list = new ArrayList<OnlineTplSheet>();
		for (int i = 0; i < ids.size(); i++) {
			OnlineTplSheet onlineTplSheet = new OnlineTplSheet();
			onlineTplSheet.setId(ids.get(i));
			onlineTplSheet.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(onlineTplSheet);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @MethodName: updateAsync
	 * @Description: 异步更新
	 * @author caiyang
	 * @param model
	 * @param key
	 * @param version
	 * @see com.springreport.api.onlinetplsheet.IOnlineTplSheetService#updateAsync(com.springreport.entity.onlinetplsheet.OnlineTplSheet, java.lang.String, java.lang.Long)
	 * @date 2023-02-14 04:24:50 
	 */  
	@Override
	@Async
	public void updateAsync(OnlineTplSheet model, String key, Long version,JSONArray cells,Long sheetId) {
		List<String> keys = new ArrayList<String>();
		keys.add(key);
		Long luaResult = redisUtil.executeLua(Constants.EQUALLUA, keys, version);
		if(luaResult.intValue() == 1)
		{
			this.updateById(model);
			UpdateWrapper<LuckysheetOnlineCell> updateWrapper = null;
			LuckysheetOnlineCell update = null;
			if(!ListUtil.isEmpty(cells))
			{
				for (int i = 0; i < cells.size(); i++) {
					updateWrapper = new UpdateWrapper<>();
					updateWrapper.eq("sheet_id", sheetId);
					updateWrapper.eq("coordsx", cells.getJSONArray(i).get(0));
					updateWrapper.eq("coordsy", cells.getJSONArray(i).get(1));
					updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
					update = new LuckysheetOnlineCell();
					update.setDelFlag(DelFlagEnum.DEL.getCode());
					this.luckysheetOnlineCellMapper.update(update, updateWrapper);
				}
			}
		}
	}


	/**  
	 * @MethodName: insertAsync
	 * @Description: 异步新增
	 * @author caiyang
	 * @param model
	 * @see com.springreport.api.onlinetplsheet.IOnlineTplSheetService#insertAsync(com.springreport.entity.onlinetplsheet.OnlineTplSheet)
	 * @date 2023-02-16 10:26:30 
	 */  
	@Override
	@Async
	public void insertAsync(OnlineTplSheet model) {
		this.save(model);
		
	}


	/**  
	 * @MethodName: deleteAsync
	 * @Description: 异步删除
	 * @author caiyang
	 * @param model
	 * @see com.springreport.api.onlinetplsheet.IOnlineTplSheetService#deleteAsync(com.springreport.entity.onlinetplsheet.OnlineTplSheet)
	 * @date 2023-02-16 10:26:37 
	 */  
	@Override
	@Async
	public void deleteAsync(OnlineTplSheet model) {
		this.delete(model.getId());
		UpdateWrapper<LuckysheetOnlineCell> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("sheet_id", model.getId());
		updateWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		LuckysheetOnlineCell update = new LuckysheetOnlineCell();
		update.setDelFlag(DelFlagEnum.DEL.getCode());
		this.luckysheetOnlineCellMapper.update(update, updateWrapper);
	}
}