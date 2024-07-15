package com.springreport.impl.luckysheetcell;

import com.springreport.entity.luckysheetcell.LuckysheetCell;
import com.springreport.mapper.luckysheetcell.LuckysheetCellMapper;
import com.springreport.api.luckysheetcell.ILuckysheetCellService;
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
* @Description: LuckysheetCell服务实现
* @author 
* @date 2023-10-07 03:19:47
* @version V1.0  
 */
@Service
public class LuckysheetCellServiceImpl extends ServiceImpl<LuckysheetCellMapper, LuckysheetCell> implements ILuckysheetCellService {
  

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
	public BaseEntity insert(LuckysheetCell model) {
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
	public BaseEntity update(LuckysheetCell model) {
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
		LuckysheetCell luckysheetCell = new LuckysheetCell();
		luckysheetCell.setId(id);
		luckysheetCell.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(luckysheetCell);
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
		List<LuckysheetCell> list = new ArrayList<LuckysheetCell>();
		for (int i = 0; i < ids.size(); i++) {
			LuckysheetCell luckysheetCell = new LuckysheetCell();
			luckysheetCell.setId(ids.get(i));
			luckysheetCell.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(luckysheetCell);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}

	/**  
	 * @MethodName: delRowUpdate
	 * @Description: 删除行更新坐标
	 * @author caiyang
	 * @param jsonObject
	 * @see com.springreport.api.luckysheetcell.ILuckysheetCellService#delRowUpdate(com.alibaba.fastjson.JSONObject)
	 * @date 2023-10-24 03:42:14 
	 */
	@Override
	public void delRowUpdate(JSONObject jsonObject) {
		this.baseMapper.delRowUpdate(jsonObject);
	}

	/**  
	 * @MethodName: delColUpdate
	 * @Description: 删除列更新坐标
	 * @author caiyang
	 * @param jsonObject
	 * @see com.springreport.api.luckysheetcell.ILuckysheetCellService#delColUpdate(com.alibaba.fastjson.JSONObject)
	 * @date 2023-10-24 03:42:23 
	 */
	@Override
	public void delColUpdate(JSONObject jsonObject) {
		this.baseMapper.delColUpdate(jsonObject);
	}

	/**  
	 * @MethodName: delRowDelCells
	 * @Description: 删除行删除对应的单元格数据
	 * @author caiyang
	 * @param jsonObject
	 * @see com.springreport.api.luckysheetcell.ILuckysheetCellService#delRowDelCells(com.alibaba.fastjson.JSONObject)
	 * @date 2023-10-26 12:21:53 
	 */
	@Override
	public void delRowDelCells(JSONObject jsonObject) {
		this.baseMapper.delRowDelCells(jsonObject);
	}

	/**  
	 * @MethodName: delColDelCells
	 * @Description: 删除列删除对应的单元格数据
	 * @author caiyang
	 * @param jsonObject
	 * @see com.springreport.api.luckysheetcell.ILuckysheetCellService#delColDelCells(com.alibaba.fastjson.JSONObject)
	 * @date 2023-10-26 12:21:55 
	 */
	@Override
	public void delColDelCells(JSONObject jsonObject) {
		this.baseMapper.delColDelCells(jsonObject);
	}

	/**  
	 * @MethodName: addRowUpdate
	 * @Description: 添加行更新单元格数据
	 * @author caiyang
	 * @param jsonObject
	 * @see com.springreport.api.luckysheetcell.ILuckysheetCellService#addRowUpdate(com.alibaba.fastjson.JSONObject)
	 * @date 2023-10-28 10:02:24 
	 */
	@Override
	public void addRowUpdate(JSONObject jsonObject) {
		this.baseMapper.addRowUpdate(jsonObject);
	}

	/**  
	 * @MethodName: addColUpdate
	 * @Description: 添加列更新单元格数据
	 * @author caiyang
	 * @param jsonObject
	 * @see com.springreport.api.luckysheetcell.ILuckysheetCellService#addColUpdate(com.alibaba.fastjson.JSONObject)
	 * @date 2023-10-28 10:02:27 
	 */
	@Override
	public void addColUpdate(JSONObject jsonObject) {
		this.baseMapper.addColUpdate(jsonObject);
	}
}