package com.springreport.mapper.sysuser;
import com.springreport.dto.coedit.UserTreeDto;
import com.springreport.dto.sysuser.SysUserDto;
import com.springreport.entity.sysuser.SysUser;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SysUserMapper类
* @author 
* @date 2021-06-15 07:12:03
* @version V1.0  
 */
public interface SysUserMapper extends BaseMapper<SysUser>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SysUser> searchDataLike(final SysUser model);
    
    /**  
     * @Title: getTableList
     * @Description: 获取表格数据
     * @param model
     * @return
     * @author caiyang
     * @date 2021-06-15 02:45:08 
     */ 
    List<SysUserDto> getTableList(final SysUserDto model);
    
    /**  
     * @MethodName: getusers
     * @Description: 获取用户信息
     * @author caiyang
     * @param model
     * @return List<UserTreeDto>
     * @date 2024-11-13 05:51:35 
     */ 
    List<UserTreeDto> getdeptusers(final SysUser model);
}
