package com.springreport.mapper.sysuserpost;
import com.springreport.entity.sysuserpost.SysUserPost;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SysUserPostMapper类
* @author 
* @date 2023-12-22 05:19:42
* @version V1.0  
 */
public interface SysUserPostMapper extends BaseMapper<SysUserPost>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SysUserPost> searchDataLike(final SysUserPost model);
}
