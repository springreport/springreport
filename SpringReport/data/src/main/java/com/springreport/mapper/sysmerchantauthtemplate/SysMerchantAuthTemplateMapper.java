package com.springreport.mapper.sysmerchantauthtemplate;
import com.springreport.entity.sysmerchantauthtemplate.SysMerchantAuthTemplate;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: SysMerchantAuthTemplateMapper类
* @author 
* @date 2023-12-22 05:18:59
* @version V1.0  
 */
public interface SysMerchantAuthTemplateMapper extends BaseMapper<SysMerchantAuthTemplate>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<SysMerchantAuthTemplate> searchDataLike(final SysMerchantAuthTemplate model);
}
