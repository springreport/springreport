package com.springreport.mapper.sysapi;
import com.springreport.dto.sysmenu.MenuTreeDto;
import com.springreport.dto.sysmenu.MesGetAuthTreeDto;
import com.springreport.entity.sysapi.SysApi;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SysApiMapper类
* @author 
* @date 2021-06-15 07:11:40
* @version V1.0  
 */
public interface SysApiMapper extends BaseMapper<SysApi>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SysApi> searchDataLike(final SysApi model);
    
    /**  
     * @Title: getMenuApis
     * @Description: 获取菜单下需授权的接口
     * @param mesGetAuthTreeDto
     * @return
     * @author caiyang
     * @date 2021-06-16 06:58:42 
     */ 
    List<MenuTreeDto> getMenuApis(MesGetAuthTreeDto mesGetAuthTreeDto);
}
