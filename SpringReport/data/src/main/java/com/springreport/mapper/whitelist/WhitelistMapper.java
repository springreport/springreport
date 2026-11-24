package com.springreport.mapper.whitelist;
import com.springreport.entity.whitelist.Whitelist;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: WhitelistMapper类
* @author 
* @date 2026-11-24 10:50:41
* @version V1.0  
 */
public interface WhitelistMapper extends BaseMapper<Whitelist>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<Whitelist> searchDataLike(final Whitelist model);
}
