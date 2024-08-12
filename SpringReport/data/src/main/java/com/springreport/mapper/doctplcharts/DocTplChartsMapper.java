package com.springreport.mapper.doctplcharts;
import com.springreport.entity.doctplcharts.DocTplCharts;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: DocTplChartsMapper类
* @author 
* @date 2024-08-09 09:27:03
* @version V1.0  
 */
public interface DocTplChartsMapper extends BaseMapper<DocTplCharts>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<DocTplCharts> searchDataLike(final DocTplCharts model);
}
