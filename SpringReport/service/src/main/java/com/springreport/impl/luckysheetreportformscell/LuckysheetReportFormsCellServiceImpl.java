package com.springreport.impl.luckysheetreportformscell;

import com.springreport.entity.luckysheetreportformscell.LuckysheetReportFormsCell;
import com.springreport.mapper.luckysheetreportformscell.LuckysheetReportFormsCellMapper;
import com.springreport.api.luckysheetreportformscell.ILuckysheetReportFormsCellService;
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
* @Description: LuckysheetReportFormsCell服务实现
* @author 
* @date 2022-11-16 01:47:45
* @version V1.0  
 */
@Service
public class LuckysheetReportFormsCellServiceImpl extends ServiceImpl<LuckysheetReportFormsCellMapper, LuckysheetReportFormsCell> implements ILuckysheetReportFormsCellService {
  
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(LuckysheetReportFormsCell model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<LuckysheetReportFormsCell> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(LuckysheetReportFormsCell model) {
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
	public BaseEntity update(LuckysheetReportFormsCell model) {
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
		LuckysheetReportFormsCell luckysheetReportFormsCell = new LuckysheetReportFormsCell();
		luckysheetReportFormsCell.setId(id);
		luckysheetReportFormsCell.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(luckysheetReportFormsCell);
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
		List<LuckysheetReportFormsCell> list = new ArrayList<LuckysheetReportFormsCell>();
		for (int i = 0; i < ids.size(); i++) {
			LuckysheetReportFormsCell luckysheetReportFormsCell = new LuckysheetReportFormsCell();
			luckysheetReportFormsCell.setId(ids.get(i));
			luckysheetReportFormsCell.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(luckysheetReportFormsCell);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}
}