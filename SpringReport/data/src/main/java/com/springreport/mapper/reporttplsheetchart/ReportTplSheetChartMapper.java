package com.springreport.mapper.reporttplsheetchart;
import com.springreport.entity.reporttplsheetchart.ReportTplSheetChart;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ReportTplSheetChartMapper类
* @author 
* @date 2023-04-19 01:40:52
* @version V1.0  
 */
public interface ReportTplSheetChartMapper extends BaseMapper<ReportTplSheetChart>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ReportTplSheetChart> searchDataLike(final ReportTplSheetChart model);
}
