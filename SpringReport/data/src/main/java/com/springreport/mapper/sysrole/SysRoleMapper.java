package com.springreport.mapper.sysrole;
import com.springreport.entity.sysrole.SysRole;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SysRoleMapper类
* @author 
* @date 2021-06-15 07:11:49
* @version V1.0  
 */
public interface SysRoleMapper extends BaseMapper<SysRole>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SysRole> searchDataLike(final SysRole model);
}
