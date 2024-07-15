package com.springreport.mapper.luckysheetreportblockcell;
import com.springreport.entity.luckysheetreportblockcell.LuckysheetReportBlockCell;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: LuckysheetReportBlockCellMapper类
* @author 
* @date 2022-04-02 05:05:14
* @version V1.0  
 */
public interface LuckysheetReportBlockCellMapper extends BaseMapper<LuckysheetReportBlockCell>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<LuckysheetReportBlockCell> searchDataLike(final LuckysheetReportBlockCell model);
}
