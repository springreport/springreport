package com.springreport.mapper.luckysheetreportcell;
import com.springreport.entity.luckysheetreportcell.LuckysheetReportCell;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: LuckysheetReportCellMapper类
* @author 
* @date 2022-01-31 11:06:19
* @version V1.0  
 */
public interface LuckysheetReportCellMapper extends BaseMapper<LuckysheetReportCell>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<LuckysheetReportCell> searchDataLike(final LuckysheetReportCell model);
    
    /**  
     * @MethodName: getVariableCells
     * @Description: 获取sheet下的动态单元格数据
     * @author caiyang
     * @param model
     * @return 
     * @return List<LuckysheetReportCell>
     * @date 2023-02-28 08:20:25 
     */  
    List<LuckysheetReportCell> getVariableCells(final LuckysheetReportCell model);
}
