package com.springreport.mapper.reporttpldatasetgroup;
import com.springreport.entity.reporttpldatasetgroup.ReportTplDatasetGroup;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ReportTplDatasetGroupMapper类
* @author 
* @date 2024-12-13 08:27:12
* @version V1.0  
 */
public interface ReportTplDatasetGroupMapper extends BaseMapper<ReportTplDatasetGroup>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ReportTplDatasetGroup> searchDataLike(final ReportTplDatasetGroup model);
}
