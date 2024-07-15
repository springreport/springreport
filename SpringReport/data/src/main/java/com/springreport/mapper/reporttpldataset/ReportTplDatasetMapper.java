package com.springreport.mapper.reporttpldataset;
import com.springreport.entity.reporttpldataset.ReportTplDataset;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ReportTplDatasetMapper类
* @author 
* @date 2021-02-13 11:16:39
* @version V1.0  
 */
public interface ReportTplDatasetMapper extends BaseMapper<ReportTplDataset>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ReportTplDataset> searchDataLike(final ReportTplDataset model);
}
