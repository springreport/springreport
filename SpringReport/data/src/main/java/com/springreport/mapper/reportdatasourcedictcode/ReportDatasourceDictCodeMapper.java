package com.springreport.mapper.reportdatasourcedictcode;
import com.springreport.entity.reportdatasourcedictcode.ReportDatasourceDictCode;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ReportDatasourceDictCodeMapper类
* @author 
* @date 2021-08-23 07:52:04
* @version V1.0  
 */
public interface ReportDatasourceDictCodeMapper extends BaseMapper<ReportDatasourceDictCode>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ReportDatasourceDictCode> searchDataLike(final ReportDatasourceDictCode model);
}
