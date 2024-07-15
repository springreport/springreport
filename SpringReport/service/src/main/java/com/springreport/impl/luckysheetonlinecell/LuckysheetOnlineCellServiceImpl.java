package com.springreport.impl.luckysheetonlinecell;

import com.springreport.entity.luckysheetonlinecell.LuckysheetOnlineCell;
import com.springreport.mapper.luckysheetonlinecell.LuckysheetOnlineCellMapper;
import com.springreport.api.luckysheetonlinecell.ILuckysheetOnlineCellService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
* @Description: LuckysheetOnlineCell服务实现
* @author 
* @date 2023-02-06 08:03:17
* @version V1.0  
 */
@Service
public class LuckysheetOnlineCellServiceImpl extends ServiceImpl<LuckysheetOnlineCellMapper, LuckysheetOnlineCell> implements ILuckysheetOnlineCellService {
  
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
	public PageEntity tablePagingQuery(LuckysheetOnlineCell model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<LuckysheetOnlineCell> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(LuckysheetOnlineCell model) {
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
	public BaseEntity update(LuckysheetOnlineCell model) {
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
		LuckysheetOnlineCell luckysheetOnlineCell = new LuckysheetOnlineCell();
		luckysheetOnlineCell.setId(id);
		luckysheetOnlineCell.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(luckysheetOnlineCell);
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
		List<LuckysheetOnlineCell> list = new ArrayList<LuckysheetOnlineCell>();
		for (int i = 0; i < ids.size(); i++) {
			LuckysheetOnlineCell luckysheetOnlineCell = new LuckysheetOnlineCell();
			luckysheetOnlineCell.setId(ids.get(i));
			luckysheetOnlineCell.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(luckysheetOnlineCell);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @MethodName: insertAsync
	 * @Description: 新增数据(异步)
	 * @author caiyang
	 * @param model
	 * @param key
	 * @param messageTime
	 * @see com.springreport.api.luckysheetonlinecell.ILuckysheetOnlineCellService#insertAsync(com.springreport.entity.luckysheetonlinecell.LuckysheetOnlineCell, java.lang.String, java.lang.Long)
	 * @date 2023-02-13 11:12:18 
	 */  
	@Override
	@Async
	public void insertAsync(LuckysheetOnlineCell model, String key, Long version) {
		List<String> keys = new ArrayList<String>();
		keys.add(key);
		Long luaResult = redisUtil.executeLua(Constants.EQUALLUA, keys, version);
		if(luaResult.intValue() == 1)
		{
			this.save(model);
		}
	}


	/**  
	 * @MethodName: updateAsync
	 * @Description: 更新数据(异步)
	 * @author caiyang
	 * @param model
	 * @param key
	 * @param version
	 * @see com.springreport.api.luckysheetonlinecell.ILuckysheetOnlineCellService#updateAsync(com.springreport.entity.luckysheetonlinecell.LuckysheetOnlineCell, java.lang.String, java.lang.Long)
	 * @date 2023-02-13 11:12:27 
	 */  
	@Override
	@Async
	public void updateAsync(LuckysheetOnlineCell model, String key, Long version) {
		List<String> keys = new ArrayList<String>();
		keys.add(key);
		Long luaResult = redisUtil.executeLua(Constants.EQUALLUA, keys, version);
		if(luaResult.intValue() == 1)
		{
			this.updateById(model);
		}
	}


	/**  
	 * @MethodName: updateCoordinateAsync
	 * @Description: 异步更新坐标信息(增加行列)
	 * @author caiyang
	 * @param params
	 * @param sheetId
	 * @see com.springreport.api.luckysheetonlinecell.ILuckysheetOnlineCellService#updateCoordinateAsync(com.alibaba.fastjson.JSONObject, java.lang.Long)
	 * @date 2023-02-16 03:12:06 
	 */  
	@Override
	public void updateAddCoordinateAsync(JSONObject params, Long sheetId) {
		String type = params.getString("type");//row column
		int index = params.getIntValue("index");
		int value = params.getIntValue("value");
		String direction = params.getString("direction");//lefttop rightbottom
		if("row".equals(type))
		{
			this.baseMapper.updateAddr(sheetId, index, value, direction);
		}else {
			this.baseMapper.updateAddc(sheetId, index, value, direction);
		}
	}


	/**  
	 * @MethodName: updateMinusCoordinateAsync
	 * @Description: 异步更新坐标信息(删除行列)
	 * @author caiyang
	 * @param params
	 * @param sheetId
	 * @see com.springreport.api.luckysheetonlinecell.ILuckysheetOnlineCellService#updateMinusCoordinateAsync(com.alibaba.fastjson.JSONObject, java.lang.Long)
	 * @date 2023-02-16 04:38:12 
	 */  
	@Override
	@Transactional
	public void updateMinusCoordinateAsync(JSONObject params, Long sheetId) {
		String type = params.getString("type");//row column
		int st = params.getIntValue("st");
		int ed = params.getIntValue("ed");
		if("row".equals(type))
		{
			this.baseMapper.deleteCellsByDelRows(sheetId, ed, ed-st+1);
			this.baseMapper.updateMinusr(sheetId, ed, ed-st+1);
		}else {
			this.baseMapper.deleteCellsByDelCols(sheetId, ed, ed-st+1);
			this.baseMapper.updateMinusc(sheetId, ed, ed-st+1);
		}
	}
}