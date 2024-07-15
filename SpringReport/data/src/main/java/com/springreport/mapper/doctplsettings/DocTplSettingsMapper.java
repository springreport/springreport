package com.springreport.mapper.doctplsettings;
import com.springreport.entity.doctplsettings.DocTplSettings;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: DocTplSettingsMapper类
* @author 
* @date 2024-05-02 08:55:39
* @version V1.0  
 */
public interface DocTplSettingsMapper extends BaseMapper<DocTplSettings>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<DocTplSettings> searchDataLike(final DocTplSettings model);
}
