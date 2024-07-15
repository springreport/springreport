package com.springreport.mapper.luckysheetreportformscell;
import com.springreport.entity.luckysheetreportformscell.LuckysheetReportFormsCell;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: LuckysheetReportFormsCellMapper类
* @author 
* @date 2022-11-16 01:47:45
* @version V1.0  
 */
public interface LuckysheetReportFormsCellMapper extends BaseMapper<LuckysheetReportFormsCell>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<LuckysheetReportFormsCell> searchDataLike(final LuckysheetReportFormsCell model);
    
    /**  
     * @MethodName: getVariableCells
     * @Description: 获取sheet下的动态单元格数据
     * @author caiyang
     * @param model
     * @return 
     * @return List<LuckysheetReportCell>
     * @date 2023-02-28 08:20:25 
     */  
    List<LuckysheetReportFormsCell> getVariableCells(final LuckysheetReportFormsCell model);
}
