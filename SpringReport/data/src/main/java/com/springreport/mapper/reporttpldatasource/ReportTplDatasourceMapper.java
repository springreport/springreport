package com.springreport.mapper.reporttpldatasource;
import com.springreport.dto.reporttpldatasource.ReportTplDatasourceDto;
import com.springreport.entity.reporttpldatasource.ReportTplDatasource;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ReportTplDatasourceMapper类
* @author 
* @date 2021-02-13 11:16:43
* @version V1.0  
 */
public interface ReportTplDatasourceMapper extends BaseMapper<ReportTplDatasource>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ReportTplDatasource> searchDataLike(final ReportTplDatasource model);
    
    /**
    *<p>Title: getReportTplDatasources</p>
    *<p>Description: 获取报表模板关联的数据源</p>
    * @author caiyang
    * @param model
    * @return
    */
    List<ReportTplDatasourceDto> getReportTplDatasources(final ReportTplDatasource model);
}
