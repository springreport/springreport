package com.springreport.mapper.reportdatasourcedicttype;
import com.springreport.entity.reportdatasourcedicttype.ReportDatasourceDictType;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ReportDatasourceDictTypeMapper类
* @author 
* @date 2022-11-21 01:46:20
* @version V1.0  
 */
public interface ReportDatasourceDictTypeMapper extends BaseMapper<ReportDatasourceDictType>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ReportDatasourceDictType> searchDataLike(final ReportDatasourceDictType model);
}
