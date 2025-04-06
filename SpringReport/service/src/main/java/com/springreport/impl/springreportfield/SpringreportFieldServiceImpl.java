package com.springreport.impl.springreportfield;

import com.springreport.entity.doctpl.DocTpl;
import com.springreport.entity.reporttpl.ReportTpl;
import com.springreport.entity.screentpl.ScreenTpl;
import com.springreport.entity.springreportfield.SpringreportField;
import com.springreport.mapper.springreportfield.SpringreportFieldMapper;
import com.springreport.api.doctpl.IDocTplService;
import com.springreport.api.reporttpl.IReportTplService;
import com.springreport.api.screentpl.IScreenTplService;
import com.springreport.api.springreportfield.ISpringreportFieldService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.MessageUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.StatusCode;
import com.springreport.dto.template.MesGetTemplateDto;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: SpringreportField服务实现
* @author 
* @date 2025-03-18 10:36:28
* @version V1.0  
 */
@Service
public class SpringreportFieldServiceImpl extends ServiceImpl<SpringreportFieldMapper, SpringreportField> implements ISpringreportFieldService {
  
	@Value("${merchantmode}")
    private Integer merchantmode;
	
	@Autowired
	private IReportTplService iReportTplService;
	
	@Autowired
	private IDocTplService iDocTplService;
	
	@Autowired
	private IScreenTplService iScreenTplService;
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(SpringreportField model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<SpringreportField> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(SpringreportField model) {
		BaseEntity result = new BaseEntity();
		//校验分类是否已经存在
		QueryWrapper<SpringreportField> queryWrapper = new QueryWrapper<SpringreportField>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("field_name", model.getFieldName());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		queryWrapper.eq("type", model.getType());
		SpringreportField isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该分类"}));
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
	public BaseEntity update(SpringreportField model) {
		BaseEntity result = new BaseEntity();
		//校验分类是否已经存在
		QueryWrapper<SpringreportField> queryWrapper = new QueryWrapper<SpringreportField>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.ne("id", model.getId());
		queryWrapper.eq("field_name", model.getFieldName());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		queryWrapper.eq("type", model.getType());
		SpringreportField isExist = this.getOne(queryWrapper,false);
		if(isExist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该分类"}));
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
		SpringreportField springreportField = new SpringreportField();
		springreportField.setId(id);
		springreportField.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(springreportField);
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
		List<SpringreportField> list = new ArrayList<SpringreportField>();
		for (int i = 0; i < ids.size(); i++) {
			SpringreportField springreportField = new SpringreportField();
			springreportField.setId(ids.get(i));
			springreportField.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(springreportField);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @MethodName: getFields
	 * @Description: 获取模板分类
	 * @author caiyang
	 * @param model
	 * @return
	 * @see com.springreport.api.springreportfield.ISpringreportFieldService#getFields(com.springreport.entity.springreportfield.SpringreportField)
	 * @date 2025-03-18 11:28:43 
	 */
	@Override
	public List<SpringreportField> getFields(SpringreportField model) {
		QueryWrapper<SpringreportField> queryWrapper = new QueryWrapper<SpringreportField>();
		if(this.merchantmode == YesNoEnum.YES.getCode()) {
			queryWrapper.eq("merchant_no", model.getMerchantNo());
		}
		queryWrapper.eq("type", model.getType());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<SpringreportField> fields = this.list(queryWrapper);
		return fields;
	}


	@Override
	public PageEntity getTemplates(MesGetTemplateDto model,UserInfoDto userInfoDto) {
		PageEntity result = null;
		if("excel".equals(model.getTemType())) {
			ReportTpl reportTpl = new ReportTpl();
			reportTpl.setIsTemplate(model.getIsTemplate());
			reportTpl.setTemplateField(model.getTemplateField());
			reportTpl.setTplName(model.getTplName());
			reportTpl.setPageSize(model.getPageSize());
			reportTpl.setCurrentPage(model.getCurrentPage());
			result = this.iReportTplService.tablePagingQuery(reportTpl);
		}else if("word".equals(model.getTemType())) {
			DocTpl docTpl = new DocTpl();
			docTpl.setIsTemplate(model.getIsTemplate());
			docTpl.setTemplateField(model.getTemplateField());
			docTpl.setTplName(model.getTplName());
			docTpl.setPageSize(model.getPageSize());
			docTpl.setCurrentPage(model.getCurrentPage());
			result = this.iDocTplService.tablePagingQuery(docTpl);
		}else if("screen".equals(model.getTemType())) {
			ScreenTpl screenTpl = new ScreenTpl();
			screenTpl.setIsTemplate(model.getIsTemplate());
			screenTpl.setTemplateField(model.getTemplateField());
			screenTpl.setTplName(model.getTplName());
			screenTpl.setPageSize(model.getPageSize());
			screenTpl.setCurrentPage(model.getCurrentPage());
			result = this.iScreenTplService.tablePagingQuery(screenTpl);
		}
		return result;
	}
}