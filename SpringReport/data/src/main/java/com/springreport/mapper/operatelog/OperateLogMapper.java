package com.springreport.mapper.operatelog;
import com.springreport.entity.operatelog.OperateLog;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: OperateLogMapper类
* @author 
* @date 2021-08-12 05:18:41
* @version V1.0  
 */
public interface OperateLogMapper extends BaseMapper<OperateLog>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<OperateLog> searchDataLike(final OperateLog model);
}
