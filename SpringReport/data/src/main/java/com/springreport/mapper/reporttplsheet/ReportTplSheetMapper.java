package com.springreport.mapper.reporttplsheet;
import com.springreport.entity.reporttplsheet.ReportTplSheet;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ReportTplSheetMapper类
* @author 
* @date 2022-10-18 06:23:34
* @version V1.0  
 */
public interface ReportTplSheetMapper extends BaseMapper<ReportTplSheet>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ReportTplSheet> searchDataLike(final ReportTplSheet model);
}
