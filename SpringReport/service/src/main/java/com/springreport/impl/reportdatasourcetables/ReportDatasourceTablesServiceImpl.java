package com.springreport.impl.reportdatasourcetables;

import com.springreport.entity.reportdatasourcetables.ReportDatasourceTables;
import com.springreport.mapper.reportdatasourcetables.ReportDatasourceTablesMapper;
import com.springreport.api.reportdatasource.IReportDatasourceService;
import com.springreport.api.reportdatasourcetables.IReportDatasourceTablesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.constants.StatusCode;
import com.springreport.dto.reporttpldatasource.MesExecSqlDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.exception.BizException;

 /**  
* @Description: ReportDatasourceTables服务实现
* @author 
* @date 2026-11-24 10:51:13
* @version V1.0  
 */
@Service
public class ReportDatasourceTablesServiceImpl extends ServiceImpl<ReportDatasourceTablesMapper, ReportDatasourceTables> implements IReportDatasourceTablesService {
  
	@Autowired
	private RedisUtil redisUtil;
	
	@Value("${enable.tablecolumn.white.check}")
    private boolean enableTablecolumnWhiteCheck;
	
	@Autowired
	@Lazy
	private IReportDatasourceService iReportDatasourceService;
	
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public PageEntity tablePagingQuery(ReportDatasourceTables model) {
		PageEntity result = new PageEntity();
		model.setDelFlag(DelFlagEnum.UNDEL.getCode());
		com.github.pagehelper.Page<?> page = PageHelper.startPage(model.getCurrentPage(), model.getPageSize()); //分页条件
		List<ReportDatasourceTables> list = this.baseMapper.searchDataLike(model);
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
	public BaseEntity insert(ReportDatasourceTables model) {
		BaseEntity result = new BaseEntity();
		QueryWrapper<ReportDatasourceTables> queryWrapper = new QueryWrapper<ReportDatasourceTables>();
		queryWrapper.eq("table_name", model.getTableName());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportDatasourceTables isExist = this.getOne(queryWrapper,false);
		if(isExist != null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该表"}));
		}
		this.save(model);
		try {
			this.updateRedisTableCache(model.getDatasourceId());
			this.updateRedisTableColumnCache(model.getDatasourceId(), model.getTableName());
		} catch (Exception e) {
		}
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
	public BaseEntity update(ReportDatasourceTables model) {
		BaseEntity result = new BaseEntity();
		QueryWrapper<ReportDatasourceTables> queryWrapper = new QueryWrapper<ReportDatasourceTables>();
		queryWrapper.ne("id", model.getId());
		queryWrapper.eq("table_name", model.getTableName());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		ReportDatasourceTables isExist = this.getOne(queryWrapper,false);
		if(isExist != null) {
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.exist", new String[] {"该表"}));
		}
		this.updateById(model);
		try {
			this.updateRedisTableCache(model.getDatasourceId());
			this.updateRedisTableColumnCache(model.getDatasourceId(), model.getTableName());
		} catch (Exception e) {
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
		ReportDatasourceTables table = this.getById(id);
		ReportDatasourceTables reportDatasourceTables = new ReportDatasourceTables();
		reportDatasourceTables.setId(id);
		reportDatasourceTables.setDelFlag(DelFlagEnum.DEL.getCode());
		this.updateById(reportDatasourceTables);
		BaseEntity result = new BaseEntity();
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		try {
			if(table != null) {
				this.updateRedisTableCache(table.getDatasourceId());
				List<String> keys = new ArrayList<String>();
				String key = RedisPrefixEnum.DATASOURCETABLECOLUMNSKEY.getCode()+String.valueOf(table.getDatasourceId())+":"+table.getTableName();
				keys.add(key);
				this.deleteRedisTableColumnCache(keys);
			}
		} catch (Exception e) {
		}
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
		List<ReportDatasourceTables> list = new ArrayList<ReportDatasourceTables>();
		List<ReportDatasourceTables> list2 = this.listByIds(ids);
		for (int i = 0; i < ids.size(); i++) {
			ReportDatasourceTables reportDatasourceTables = new ReportDatasourceTables();
			reportDatasourceTables.setId(ids.get(i));
			reportDatasourceTables.setDelFlag(DelFlagEnum.DEL.getCode());
			list.add(reportDatasourceTables);
		}
		BaseEntity result = new BaseEntity();
		List<String> keys = new ArrayList<String>();
		if (list != null && list.size() > 0) {
			this.updateBatchById(list);
			try {
				this.updateRedisTableCache(list.get(0).getDatasourceId());
			} catch (Exception e) {
			}
		}
		if(ListUtil.isNotEmpty(list2)) {
			for (int i = 0; i < list2.size(); i++) {
				String key = RedisPrefixEnum.DATASOURCETABLECOLUMNSKEY.getCode()+String.valueOf(list2.get(i).getDatasourceId())+":"+list2.get(i).getTableName();
				keys.add(key);
			}
			try {
				this.deleteRedisTableColumnCache(keys);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		result.setStatusMsg(MessageUtil.getValue("info.delete"));
		
		return result;
	}
	
	/**
	 * 更新数据源对应的表数据缓存
	 * @param datasourceId
	 */
	private void updateRedisTableCache(Long datasourceId) {
		QueryWrapper<ReportDatasourceTables> queryWrapper = new QueryWrapper<ReportDatasourceTables>();
		queryWrapper.eq("datasource_id", datasourceId);
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		List<ReportDatasourceTables> reportDatasourceTables = this.baseMapper.selectList(queryWrapper);
		if(ListUtil.isNotEmpty(reportDatasourceTables)) {
			redisUtil.set(RedisPrefixEnum.DATASOURCETABLESKEY.getCode()+String.valueOf(datasourceId), reportDatasourceTables);
		}else {
			redisUtil.del(RedisPrefixEnum.DATASOURCETABLESKEY.getCode()+String.valueOf(datasourceId));
		}
	}
	
	/**
	 * 更新表对应的字段缓存
	 * @param datasourceId
	 * @param tableName
	 * @throws Exception
	 */
	private void updateRedisTableColumnCache(Long datasourceId,String tableName) throws Exception {
		MesExecSqlDto mesExecSqlDto = new MesExecSqlDto();
		mesExecSqlDto.setDatasourceId(datasourceId);
		mesExecSqlDto.setSqlType(1);
		mesExecSqlDto.setTplSql("select * from " + tableName);
		List<Map<String, Object>> tableColumns = null;
		tableColumns = (List<Map<String, Object>>) redisUtil.get(RedisPrefixEnum.DATASOURCETABLECOLUMNSKEY.getCode()+String.valueOf(datasourceId)+":"+tableName);
		if(ListUtil.isEmpty(tableColumns)) {
			tableColumns = iReportDatasourceService.execSql(mesExecSqlDto, null);
			if(ListUtil.isNotEmpty(tableColumns)) {
				redisUtil.set(RedisPrefixEnum.DATASOURCETABLECOLUMNSKEY.getCode()+String.valueOf(datasourceId)+":"+tableName, tableColumns);
			}else {
				redisUtil.del(RedisPrefixEnum.DATASOURCETABLECOLUMNSKEY.getCode()+String.valueOf(datasourceId)+":"+tableName);
			}
		}
	}
	
	/**
	 * 删除表对应的字段缓存
	 * @param datasourceId
	 * @param tableName
	 * @throws Exception
	 */
	private void deleteRedisTableColumnCache(List<String> keys) throws Exception {
		if(ListUtil.isNotEmpty(keys)) {
			redisUtil.del(keys);
		}
	}


	/**
	*<p>Title: datasouceWhiteCheck</p>
	*<p>Description: 对数据源的表和列名进行白名单校验，防止注入</p>
	* @author caiyang
	* @param list
	* @return
	 * @throws Exception 
	 * @throws SQLException 
	*/
	@Override
	public void datasouceWhiteCheck(Long datasourceId, String tableName, String columnName) throws Exception {
		if(enableTablecolumnWhiteCheck) {
			List<ReportDatasourceTables> reportDatasourceTables = null;
			reportDatasourceTables = (List<ReportDatasourceTables>) redisUtil.get(RedisPrefixEnum.DATASOURCETABLESKEY.getCode()+String.valueOf(datasourceId));
			if(!ListUtil.isNotEmpty(reportDatasourceTables)) {
				QueryWrapper<ReportDatasourceTables> queryWrapper = new QueryWrapper<ReportDatasourceTables>();
				queryWrapper.eq("datasource_id", datasourceId);
				queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
				reportDatasourceTables = this.baseMapper.selectList(queryWrapper);
				if(ListUtil.isNotEmpty(reportDatasourceTables)) {
					redisUtil.set(RedisPrefixEnum.DATASOURCETABLESKEY.getCode()+String.valueOf(datasourceId), reportDatasourceTables);
				}else {
					redisUtil.del(RedisPrefixEnum.DATASOURCETABLESKEY.getCode()+String.valueOf(datasourceId));
				}
			}
			List<String> tableNames = new ArrayList<String>();
			if(ListUtil.isNotEmpty(reportDatasourceTables)) {
				for (int i = 0; i < reportDatasourceTables.size(); i++) {
					tableNames.add(reportDatasourceTables.get(i).getTableName());
				}
			}
			if(ListUtil.isEmpty(tableNames) || !tableNames.stream()
			        .anyMatch(tableName::equalsIgnoreCase)) {
				throw new BizException(StatusCode.FAILURE, "该表不在白名单列表中，请检查表名是否正确");
			}
			MesExecSqlDto mesExecSqlDto = new MesExecSqlDto();
			mesExecSqlDto.setDatasourceId(datasourceId);
			mesExecSqlDto.setSqlType(1);
			mesExecSqlDto.setTplSql("select * from " + tableName);
			List<Map<String, Object>> tableColumns = null;
			tableColumns = (List<Map<String, Object>>) redisUtil.get(RedisPrefixEnum.DATASOURCETABLECOLUMNSKEY.getCode()+String.valueOf(datasourceId)+":"+tableName);
			if(ListUtil.isEmpty(tableColumns)) {
				tableColumns = iReportDatasourceService.execSql(mesExecSqlDto, null);
				if(ListUtil.isNotEmpty(tableColumns)) {
					redisUtil.set(RedisPrefixEnum.DATASOURCETABLECOLUMNSKEY.getCode()+String.valueOf(datasourceId)+":"+tableName, tableColumns);
				}else {
					redisUtil.del(RedisPrefixEnum.DATASOURCETABLECOLUMNSKEY.getCode()+String.valueOf(datasourceId)+":"+tableName);
				}
			}
			List<String> columns = new ArrayList<String>();
			if(ListUtil.isNotEmpty(tableColumns)) {
				for (int i = 0; i < tableColumns.size(); i++) {
					columns.add(String.valueOf(tableColumns.get(i).get("columnName")));
				}
			}
			if(ListUtil.isEmpty(columns) || !columns.stream()
			        .anyMatch(columnName::equalsIgnoreCase)) {
				throw new BizException(StatusCode.FAILURE, "表中没有列："+columnName);
			}
		}
	}
}