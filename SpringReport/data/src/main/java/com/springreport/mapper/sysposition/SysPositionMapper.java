package com.springreport.mapper.sysposition;
import com.springreport.entity.sysposition.SysPosition;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SysPositionMapper类
* @author 
* @date 2022-06-24 10:54:59
* @version V1.0  
 */
public interface SysPositionMapper extends BaseMapper<SysPosition>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SysPosition> searchDataLike(final SysPosition model);
}
