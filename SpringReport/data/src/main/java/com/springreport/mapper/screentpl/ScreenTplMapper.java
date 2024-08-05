package com.springreport.mapper.screentpl;
import com.springreport.entity.screentpl.ScreenTpl;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ScreenTplMapper类
* @author 
* @date 2021-08-02 07:01:17
* @version V1.0  
 */
public interface ScreenTplMapper extends BaseMapper<ScreenTpl>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ScreenTpl> searchDataLike(final ScreenTpl model);
}
