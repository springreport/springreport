package com.springreport.mapper.screencontent;
import com.springreport.entity.screencontent.ScreenContent;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ScreenContentMapper类
* @author 
* @date 2021-08-02 07:01:12
* @version V1.0  
 */
public interface ScreenContentMapper extends BaseMapper<ScreenContent>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ScreenContent> searchDataLike(final ScreenContent model);
    
    /**
     * 获取模板所有的组件id
     * @param model
     * @return
     */
    List<Long> getTplContentIds(final ScreenContent model);
}
