package com.springreport.mapper.sysmerchant;
import com.springreport.entity.sysmerchant.SysMerchant;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SysMerchantMapper类
* @author 
* @date 2023-12-22 05:18:53
* @version V1.0  
 */
public interface SysMerchantMapper extends BaseMapper<SysMerchant>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SysMerchant> searchDataLike(final SysMerchant model);
}
