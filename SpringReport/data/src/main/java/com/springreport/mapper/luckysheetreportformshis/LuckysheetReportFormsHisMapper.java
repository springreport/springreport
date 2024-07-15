package com.springreport.mapper.luckysheetreportformshis;
import com.springreport.dto.LuckysheetReportFormsHis.LuckysheetReportFormsHisDto;
import com.springreport.entity.luckysheetreportformshis.LuckysheetReportFormsHis;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: LuckysheetReportFormsHisMapper类
* @author 
* @date 2023-01-29 04:05:08
* @version V1.0  
 */
public interface LuckysheetReportFormsHisMapper extends BaseMapper<LuckysheetReportFormsHis>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<LuckysheetReportFormsHis> searchDataLike(final LuckysheetReportFormsHis model);
    
    /**  
     * @MethodName: getTableList
     * @Description: 获取上报历史记录
     * @author caiyang
     * @param model
     * @return 
     * @return List<LuckysheetReportFormsHisDto>
     * @date 2023-01-31 08:19:18 
     */  
    List<LuckysheetReportFormsHisDto> getTableList(final LuckysheetReportFormsHis model);
}
