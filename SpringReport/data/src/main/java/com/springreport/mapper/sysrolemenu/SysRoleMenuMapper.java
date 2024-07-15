package com.springreport.mapper.sysrolemenu;
import com.springreport.entity.sysrolemenu.SysRoleMenu;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SysRoleMenuMapper类
* @author 
* @date 2021-06-15 07:11:59
* @version V1.0  
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SysRoleMenu> searchDataLike(final SysRoleMenu model);
}
