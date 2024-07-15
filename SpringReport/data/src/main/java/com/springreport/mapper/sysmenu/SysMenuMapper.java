package com.springreport.mapper.sysmenu;
import com.springreport.dto.sysmenu.IndexMenuTreeDto;
import com.springreport.dto.sysmenu.MenuTreeDto;
import com.springreport.dto.sysmenu.MesGetAuthTreeDto;
import com.springreport.dto.sysmenu.MesGetIndexMenuDto;
import com.springreport.entity.sysmenu.SysMenu;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SysMenuMapper类
* @author 
* @date 2021-06-15 07:11:44
* @version V1.0  
 */
public interface SysMenuMapper extends BaseMapper<SysMenu>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SysMenu> searchDataLike(final SysMenu model);
    
    /**  
     * @Title: getMenuTree
     * @Description: 获取菜单树
     * @param mesGetAuthTreeDto
     * @return
     * @author caiyang
     * @date 2021-06-15 06:57:44 
     */ 
    List<MenuTreeDto> getMenuTree(MesGetAuthTreeDto mesGetAuthTreeDto);
    
    /**  
     * @Title: getIndexMenu
     * @Description: 获取首页菜单
     * @param mesGetAuthTreeDto
     * @return
     * @author caiyang
     * @date 2021-06-18 02:16:05 
     */ 
    List<IndexMenuTreeDto> getIndexMenu(MesGetIndexMenuDto mesGetIndexMenuDto);
}
