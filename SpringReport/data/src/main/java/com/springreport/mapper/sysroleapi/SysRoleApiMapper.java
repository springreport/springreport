package com.springreport.mapper.sysroleapi;
import com.springreport.entity.sysapi.SysApi;
import com.springreport.entity.sysroleapi.SysRoleApi;


import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SysRoleApiMapper类
* @author 
* @date 2021-06-15 07:11:53
* @version V1.0  
 */
public interface SysRoleApiMapper extends BaseMapper<SysRoleApi>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SysRoleApi> searchDataLike(final SysRoleApi model);
    
    /**  
     * @Title: getApisByRole
     * @Description: 根据角色获取接口权限
     * @param roleId
     * @return
     * @author caiyang
     * @date 2021-06-16 05:19:19 
     */ 
    List<SysApi> getApisByRole(JSONObject jsonObject);
}
