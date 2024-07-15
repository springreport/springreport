package com.springreport.mapper.reportdatasource;
import com.springreport.entity.reportdatasource.ReportDatasource;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ReportDatasourceMapper类
* @author 
* @date 2021-02-09 01:18:28
* @version V1.0  
 */
public interface ReportDatasourceMapper extends BaseMapper<ReportDatasource>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ReportDatasource> searchDataLike(final ReportDatasource model);
}
