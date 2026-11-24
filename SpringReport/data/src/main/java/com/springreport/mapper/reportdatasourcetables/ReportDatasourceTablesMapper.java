package com.springreport.mapper.reportdatasourcetables;
import com.springreport.entity.reportdatasourcetables.ReportDatasourceTables;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ReportDatasourceTablesMapper类
* @author 
* @date 2026-11-24 10:51:13
* @version V1.0  
 */
public interface ReportDatasourceTablesMapper extends BaseMapper<ReportDatasourceTables>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ReportDatasourceTables> searchDataLike(final ReportDatasourceTables model);
}
