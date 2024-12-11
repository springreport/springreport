package com.springreport.impl.qrtzreportdetail;

import com.springreport.entity.qrtzreportdetail.QrtzReportDetail;
import com.springreport.mapper.qrtzreportdetail.QrtzReportDetailMapper;
import com.springreport.qrtz.QuartzTaskService;
import com.springreport.qrtz.ScheduleTask;
import com.springreport.api.qrtzreportdetail.IQrtzReportDetailService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.quartz.CronExpression;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springreport.util.CheckUtil;
import com.springreport.util.DateUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.dto.qrtzreportdetail.IndexQrtzDto;
import com.springreport.dto.qrtzreportdetail.QrtzReportDetailDto;
import com.springreport.dto.qrtzreportdetail.ReqIndexQrtzDto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.ParamTypeEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: QrtzReportDetail服务实现
* @author 
* @date 2023-07-28 09:43:20
* @version V1.0  
 */
@Service
public class QrtzReportDetailServiceImpl extends ServiceImpl<QrtzReportDetailMapper, QrtzReportDetail> implements IQrtzReportDetailService {
  
	@Autowired
	private QuartzTaskService quartzTaskService;
	
	@Value("${merchantmode}")
    private Integer merchantmode;
	
	private final static String jobHandlerClass = "reportExportTaskHandler";
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	 * @throws ParseException 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(QrtzReportDetail model) throws ParseException {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<QrtzReportDetailDto> details = new ArrayList<>();
		List<QrtzReportDetail> list = this.baseMapper.searchDataLike(model);
		if(!ListUtil.isEmpty(list))
		{
			Map<String, JSONObject> map = quartzTaskService.getJobStatus("report"+"_"+list.get(0).getTplId());
			for (int i = 0; i < list.size(); i++) {
				String jobData = list.get(i).getJobData();
				if(StringUtil.isNotEmpty(jobData))
				{
					JSONArray jsonArray = JSON.parseArray(jobData);
					if(!ListUtil.isEmpty(jsonArray))
					{
						JSONObject param = new JSONObject();
						for (int j = 0; j < jsonArray.size(); j++) {
							String key = jsonArray.getJSONObject(j).getString("paramCode");
							Object value = jsonArray.getJSONObject(j).get(key);
							String paramType = jsonArray.getJSONObject(j).getString("paramType");
							boolean isDefault = jsonArray.getJSONObject(j).getBooleanValue("isDefault");
							if(ParamTypeEnum.VARCHAR.getCode().equals(paramType) || ParamTypeEnum.NUMBER.getCode().equals(paramType)
									|| ParamTypeEnum.SELECT.getCode().equals(paramType) || ParamTypeEnum.MUTISELECT.getCode().equals(paramType))
							{
								param.put(key, value);
							}else if(ParamTypeEnum.DATE.getCode().equals(paramType)) {
								if(isDefault)
								{
									String dateFormat = jsonArray.getJSONObject(j).getString("dateFormat");
									String dateDefaultValue = jsonArray.getJSONObject(j).getString("dateDefaultValue");
									if("YYYY-MM-DD".equals(dateFormat))
									{
										dateFormat = DateUtil.FORMAT_LONOGRAM;
									}else if("YYYY-MM".equals(dateFormat))
									{
										dateFormat = DateUtil.FORMAT_YEARMONTH;
									}else if("YYYY-MM-DD HH:mm".equals(dateFormat))
									{
										dateFormat = DateUtil.FORMAT_WITHOUTSECONDS;
									}
									if(StringUtil.isNotEmpty(dateDefaultValue))
									{
										if(Constants.CURRENT_DATE.equals(StringUtil.trim(dateDefaultValue).toLowerCase()))
										{
											String currentDate = DateUtil.getNow(StringUtil.isNotEmpty(dateFormat)?dateFormat:DateUtil.FORMAT_LONOGRAM);
											param.put(key, currentDate);
										}else {
											if(CheckUtil.isNumber(dateDefaultValue))
											{
												int days = Double.valueOf(dateDefaultValue).intValue();
												if(DateUtil.FORMAT_YEARMONTH.equals(dateFormat))
												{
													String date = DateUtil.addMonth(days, DateUtil.getNow(DateUtil.FORMAT_LONOGRAM),DateUtil.FORMAT_YEARMONTH);
													param.put(key, date);
												}else {
													String date = DateUtil.addDays(days, DateUtil.getNow(),StringUtil.isNotEmpty(dateFormat)?dateFormat:DateUtil.FORMAT_LONOGRAM);
													param.put(key, date);
												}
											}else {
												param.put(key, "");
											}
										}
									}
								}else {
									param.put(key, value);
								}
							}
							
						}
						list.get(i).setJobData(JSON.toJSONString(param));
					}
				}
				QrtzReportDetailDto detailDto = new QrtzReportDetailDto();
				BeanUtils.copyProperties(list.get(i), detailDto);
				if(detailDto.getJobTimeType() == 1)
				{
					detailDto.setJobCron(detailDto.getJobTime());
				}
				JSONObject jsonObject = map.get(detailDto.getId()+"");
				if(jsonObject != null)
				{
					String jobStatus = jsonObject.getString("jobStatus");
					detailDto.setJobStatus(jobStatus);
					Date previousFireTime = jsonObject.getDate("previousFireTime");
					if(previousFireTime != null)
					{
						detailDto.setPreviousFireTime(DateUtil.date2String(previousFireTime, DateUtil.FORMAT_FULL));
					}
					Date nextFireTime = jsonObject.getDate("nextFireTime");
					if(nextFireTime != null)
					{
						detailDto.setNextFireTime(DateUtil.date2String(nextFireTime, DateUtil.FORMAT_FULL));
					}
				}
				details.add(detailDto);
			}
		}
		result.setData(details);
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
	 * @throws ParseException 
	*/
	@Override
	public BaseEntity getDetail(Long id) throws ParseException {
		QrtzReportDetail detail = this.getById(id);
		if(StringUtil.isNotEmpty(detail.getJobData()))
		{
			JSONArray jsonArray = JSON.parseArray(detail.getJobData());
			if(!ListUtil.isEmpty(jsonArray))
			{
				for (int j = 0; j < jsonArray.size(); j++) {
					String key = jsonArray.getJSONObject(j).getString("paramCode");
					String paramType = jsonArray.getJSONObject(j).getString("paramType");
					boolean isDefault = jsonArray.getJSONObject(j).getBooleanValue("isDefault");
					if(isDefault && ParamTypeEnum.DATE.getCode().equals(paramType)) {
						String dateFormat = jsonArray.getJSONObject(j).getString("dateFormat");
						String dateDefaultValue = jsonArray.getJSONObject(j).getString("dateDefaultValue");
						if("YYYY-MM-DD".equals(dateFormat))
						{
							dateFormat = DateUtil.FORMAT_LONOGRAM;
						}else if("YYYY-MM".equals(dateFormat))
						{
							dateFormat = DateUtil.FORMAT_YEARMONTH;
						}else if("YYYY-MM-DD HH:mm".equals(dateFormat))
						{
							dateFormat = DateUtil.FORMAT_WITHOUTSECONDS;
						}
						if(StringUtil.isNotEmpty(dateDefaultValue))
						{
							if(Constants.CURRENT_DATE.equals(StringUtil.trim(dateDefaultValue).toLowerCase()))
							{
								String currentDate = DateUtil.getNow(StringUtil.isNotEmpty(dateFormat)?dateFormat:DateUtil.FORMAT_LONOGRAM);
								jsonArray.getJSONObject(j).put(key, currentDate);
							}else {
								if(CheckUtil.isNumber(dateDefaultValue))
								{
									int days = Double.valueOf(dateDefaultValue).intValue();
									if(DateUtil.FORMAT_YEARMONTH.equals(dateFormat))
									{
										String date = DateUtil.addMonth(days, DateUtil.getNow(DateUtil.FORMAT_LONOGRAM),DateUtil.FORMAT_YEARMONTH);
										jsonArray.getJSONObject(j).put(key, date);
									}else {
										String date = DateUtil.addDays(days, DateUtil.getNow(),StringUtil.isNotEmpty(dateFormat)?dateFormat:DateUtil.FORMAT_LONOGRAM);
										jsonArray.getJSONObject(j).put(key, date);
									}
								}else {
									jsonArray.getJSONObject(j).put(key, "");
								}
							}
						}
					}
					
				}
			}
			detail.setJobData(JSON.toJSONString(jsonArray));
		}
		return detail;
	}

	/**
	*<p>Title: insert</p>
	*<p>Description: 新增数据</p>
	* @author 
	* @param model
	* @return
	 * @throws Exception 
	*/
	@Transactional
	@Override
	public BaseEntity insert(QrtzReportDetail model) throws Exception {
		BaseEntity result = new BaseEntity();
		if(model.getJobTimeType() == 2 && !CronExpression.isValidExpression(model.getJobCron()))
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.cron.expression"));
		}else {
			if(model.getJobTimeType() == 1 && DateUtil.daysCompare(model.getJobTime(), DateUtil.getNow(DateUtil.FORMAT_FULL), DateUtil.FORMAT_FULL) <= 0)
			{
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.job.time"));
			}
		}
		//校验任务名称是否已经存在
		QueryWrapper<QrtzReportDetail> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		queryWrapper.eq("job_name", model.getJobName());
		queryWrapper.eq("tpl_id", model.getTplId());
		QrtzReportDetail exist = this.getOne(queryWrapper, false);
		if(exist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"任务名称"}));
		}
		this.save(model);
		result.setStatusMsg(MessageUtil.getValue("info.insert"));
		//添加定时任务
		String jobName = model.getId()+"_"+model.getJobName();
	    String jogGroupName = "report"+"_"+model.getTplId();

	    ScheduleTask scheduleTask = ScheduleTask.builder()
	        .jobName(jobName)
	        .groupName(jogGroupName)
	        .jobData(model.getJobData())
	        .jobHandlerClass(jobHandlerClass)
	        .jobTime(model.getJobTimeType() == 1?DateUtil.string2Date(model.getJobTime(), DateUtil.FORMAT_FULL):null)
	        .jobCron(model.getJobTimeType() == 1?null:model.getJobCron())
	        .build();
	    quartzTaskService.createJob(scheduleTask);
		return result;
	}

	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author 
	* @param model
	* @return
	 * @throws Exception 
	*/
	@Transactional
	@Override
	public BaseEntity update(QrtzReportDetail model) throws Exception {
		if(model.getJobTimeType() == 2 && !CronExpression.isValidExpression(model.getJobCron()))
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.cron.expression"));
		}else if(model.getJobTimeType() == 1){
			if(DateUtil.daysCompare(model.getJobTime(), DateUtil.getNow(DateUtil.FORMAT_FULL), DateUtil.FORMAT_FULL) <= 0)
			{
				throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.job.time"));
			}
		}
		//校验任务名称是否已经存在
		QueryWrapper<QrtzReportDetail> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("id", model.getId());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		queryWrapper.eq("job_name", model.getJobName());
		queryWrapper.eq("tpl_id", model.getTplId());
		QrtzReportDetail exist = this.getOne(queryWrapper, false);
		if(exist != null)
		{
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"任务名称"}));
		}
		BaseEntity result = new BaseEntity();
		this.updateById(model);
		result.setStatusMsg(MessageUtil.getValue("info.update"));
		//添加定时任务
		String jobName = model.getId()+"_"+model.getJobName();
		String jogGroupName = "report"+"_"+model.getTplId();

		ScheduleTask scheduleTask = ScheduleTask.builder()
			.jobName(jobName)
			.groupName(jogGroupName)
			.jobData(model.getJobData())
			.jobHandlerClass(jobHandlerClass)
			.jobTime(model.getJobTimeType() == 1?DateUtil.string2Date(model.getJobTime(), DateUtil.FORMAT_FULL):null)
	        .jobCron(model.getJobTimeType() == 1?null:model.getJobCron())
			.build();
		quartzTaskService.updateJob(scheduleTask);
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
		QrtzReportDetail qrtzReportDetail = new QrtzReportDetail();
		qrtzReportDetail.setId(id);
		qrtzReportDetail.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(qrtzReportDetail);
		BaseEntity result = new BaseEntity();
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		QrtzReportDetail model = this.getById(id);
		//删除定时任务
		String jobName = model.getId()+"_"+model.getJobName();
		String jogGroupName = "report"+"_"+model.getTplId();

		ScheduleTask scheduleTask = ScheduleTask.builder()
			.jobName(jobName)
			.groupName(jogGroupName)
					.build();
		quartzTaskService.removeTask(scheduleTask);
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
		List<QrtzReportDetail> list = new ArrayList<QrtzReportDetail>();
		for (int i = 0; i < ids.size(); i++) {
			QrtzReportDetail qrtzReportDetail = new QrtzReportDetail();
			qrtzReportDetail.setId(ids.get(i));
			qrtzReportDetail.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(qrtzReportDetail);
		}
		BaseEntity result = new BaseEntity();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		return result;
	}


	/**  
	 * @MethodName: runTask
	 * @Description: 执行一次定时任务
	 * @author caiyang
	 * @param id
	 * @return
	 * @throws Exception 
	 * @see com.springreport.api.qrtzreportdetail.IQrtzReportDetailService#runTask(java.lang.Long)
	 * @date 2023-07-30 09:59:08 
	 */
	@Override
	public BaseEntity runTask(Long id) throws Exception {
		BaseEntity result = new BaseEntity();
		QrtzReportDetail model = this.getById(id);
		//立即执行定时任务
		String jobName = model.getId()+"_"+model.getJobName();
		String jogGroupName = "report"+"_"+model.getTplId();

		ScheduleTask scheduleTask = ScheduleTask.builder()
				.jobName(jobName)
				.groupName(jogGroupName)
				.jobData(model.getJobData())
				.jobHandlerClass(jobHandlerClass)
				.jobTime(model.getJobTimeType() == 1?DateUtil.string2Date(model.getJobTime(), DateUtil.FORMAT_FULL):null)
		        .jobCron(model.getJobTimeType() == 1?null:model.getJobCron())
				.build();
		quartzTaskService.runJobNow(scheduleTask);
		result.setStatusMsg(MessageUtil.getValue("info.operate.success"));
		return result;
	}


	@Override
	public BaseEntity pauseTask(Long id) {
		BaseEntity result = new BaseEntity();
		QrtzReportDetail model = this.getById(id);
		//立即执行定时任务
		String jobName = model.getId()+"_"+model.getJobName();
		String jogGroupName = "report"+"_"+model.getTplId();

		ScheduleTask scheduleTask = ScheduleTask.builder()
				.jobName(jobName)
				.groupName(jogGroupName)
				.jobData(model.getJobData())
				.build();
		quartzTaskService.pauseJob(scheduleTask);
		result.setStatusMsg(MessageUtil.getValue("info.operate.success"));
		return result;
	}


	@Override
	public BaseEntity resumeTask(Long id) {
		BaseEntity result = new BaseEntity();
		QrtzReportDetail model = this.getById(id);
		//立即执行定时任务
		String jobName = model.getId()+"_"+model.getJobName();
		String jogGroupName = "report"+"_"+model.getTplId();

		ScheduleTask scheduleTask = ScheduleTask.builder()
				.jobName(jobName)
				.groupName(jogGroupName)
				.jobData(model.getJobData())
				.build();
		quartzTaskService.resumeJob(scheduleTask);
		result.setStatusMsg(MessageUtil.getValue("info.operate.success"));
		return result;
	}


	/**  
	 * @MethodName: getIndexTaskList
	 * @Description: 获取首页任务列表
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return PageEntity
	 * @date 2024-12-10 06:02:50 
	 */ 
	@Override
	public PageEntity getIndexTaskList(ReqIndexQrtzDto model, UserInfoDto userInfoDto) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		if(this.merchantmode.intValue() == YesNoEnum.YES.getCode().intValue()) {
			model.setMerchantNo(userInfoDto.getMerchantNo());
		}
		if(model.getType() == 2) {
			model.setCreator(userInfoDto.getUserId());
		}
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<IndexQrtzDto> list = this.baseMapper.getIndexTaskList(model);
		result.setData(list);
		result.setTotal(page.getTotal());
		result.setCurrentPage(model.getCurrentPage());
		result.setPageSize(model.getPageSize());
		return result;
	}
}