package com.springreport.mapper.sysuserrole;
import com.springreport.entity.sysuserrole.SysUserRole;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SysUserRoleMapper类
* @author 
* @date 2021-06-15 07:12:09
* @version V1.0  
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SysUserRole> searchDataLike(final SysUserRole model);
}
