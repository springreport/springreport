package com.springreport.mapper.sysmerchantauthtemplateids;
import com.springreport.entity.sysmerchantauthtemplateids.SysMerchantAuthTemplateIds;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SysMerchantAuthTemplateIdsMapper类
* @author 
* @date 2023-12-22 05:19:05
* @version V1.0  
 */
public interface SysMerchantAuthTemplateIdsMapper extends BaseMapper<SysMerchantAuthTemplateIds>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SysMerchantAuthTemplateIds> searchDataLike(final SysMerchantAuthTemplateIds model);
}
