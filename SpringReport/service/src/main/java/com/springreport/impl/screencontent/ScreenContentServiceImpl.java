package com.springreport.impl.screencontent;

import com.springreport.entity.screencontent.ScreenContent;
import com.springreport.mapper.screencontent.ScreenContentMapper;
import com.springreport.api.screencontent.IScreenContentService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.ScreenContentDto;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import com.springreport.enums.ComponentTypeEnum;
import com.springreport.enums.DelFlagEnum;

 /**  
* @Description: ScreenContent服务实现
* @author 
* @date 2021-08-02 07:01:12
* @version V1.0  
 */
@Service
public class ScreenContentServiceImpl extends ServiceImpl<ScreenContentMapper, ScreenContent> implements IScreenContentService {
  
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(ScreenContent model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<ScreenContent> list = this.baseMapper.searchDataLike(model);
		List<ScreenContentDto> screenContentDtos = new ArrayList<ScreenContentDto>();
		if(!ListUtil.isEmpty(list))
		{
			ScreenContentDto screenContentDto = null;
			for (int i = 0; i < list.size(); i++) {
				screenContentDto = new ScreenContentDto();
				JSONObject screenContent = JSONObject.parseObject(list.get(i).getContent());
				String type = screenContent.getString("type");
				if(type.equals(ComponentTypeEnum.TEXT.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.TEXT.getName());
				}else if(type.equals(ComponentTypeEnum.PICTURE.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.PICTURE.getName());
				}else if(type.equals(ComponentTypeEnum.HISTOGRAM.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.HISTOGRAM.getName());
				}else if(type.equals(ComponentTypeEnum.LINECHART.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.LINECHART.getName());
				}else if(type.equals(ComponentTypeEnum.PIECHART.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.PIECHART.getName());
				}else if(type.equals(ComponentTypeEnum.TABLE.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.TABLE.getName());
				}else if(type.equals(ComponentTypeEnum.DATE.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.DATE.getName());
				}else if(type.equals(ComponentTypeEnum.MAP.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.MAP.getName());
				}else if(type.equals(ComponentTypeEnum.MAP3D.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.MAP3D.getName());
				}else if(type.equals(ComponentTypeEnum.PIE3DCHART.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.PIE3DCHART.getName());
				}else if(type.equals(ComponentTypeEnum.GAUGE.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.GAUGE.getName());
				}else if(type.equals(ComponentTypeEnum.TAB.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.TAB.getName());
				}else if(type.equals(ComponentTypeEnum.VIDEO.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.VIDEO.getName());
				}else if(type.equals(ComponentTypeEnum.HISTOGRAMLINECHART.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.HISTOGRAMLINECHART.getName());
				}else if(type.equals(ComponentTypeEnum.MAPSCATTER.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.MAPSCATTER.getName());
				}else if(type.equals(ComponentTypeEnum.MAPMIGRATE.getCode()))
				{
					screenContentDto.setComponentsType(ComponentTypeEnum.MAPMIGRATE.getName());
				}
				
				screenContentDto.setX(screenContent.getString("x"));
				screenContentDto.setY(screenContent.getString("y"));
				screenContentDto.setIsRefresh(screenContent.getBoolean("isRefresh"));
				screenContentDto.setRefreshTime(screenContent.getString("refreshTime"));
				screenContentDto.setContent(list.get(i).getContent());
				screenContentDto.setId(list.get(i).getId());
				screenContentDtos.add(screenContentDto);
			}
		}
		result.setData(screenContentDtos);
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
	public BaseEntity insert(ScreenContent model) {
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
	public BaseEntity update(ScreenContent model) {
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
		ScreenContent screenContent = new ScreenContent();
		screenContent.setId(id);
		screenContent.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(screenContent);
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
		List<ScreenContent> list = new ArrayList<ScreenContent>();
		for (int i = 0; i < ids.size(); i++) {
			ScreenContent screenContent = new ScreenContent();
			screenContent.setId(ids.get(i));
			screenContent.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(screenContent);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}
}