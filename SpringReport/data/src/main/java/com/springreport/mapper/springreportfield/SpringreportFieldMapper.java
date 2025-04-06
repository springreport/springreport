package com.springreport.mapper.springreportfield;
import com.springreport.entity.springreportfield.SpringreportField;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SpringreportFieldMapper类
* @author 
* @date 2025-03-18 10:36:28
* @version V1.0  
 */
public interface SpringreportFieldMapper extends BaseMapper<SpringreportField>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SpringreportField> searchDataLike(final SpringreportField model);
}
