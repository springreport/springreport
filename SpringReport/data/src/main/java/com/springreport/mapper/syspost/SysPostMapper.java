package com.springreport.mapper.syspost;
import com.springreport.entity.syspost.SysPost;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SysPostMapper类
* @author 
* @date 2023-12-22 05:19:10
* @version V1.0  
 */
public interface SysPostMapper extends BaseMapper<SysPost>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SysPost> searchDataLike(final SysPost model);
}
