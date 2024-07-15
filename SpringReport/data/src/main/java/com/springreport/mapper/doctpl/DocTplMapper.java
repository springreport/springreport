package com.springreport.mapper.doctpl;
import com.springreport.dto.doctpl.DocTplDto;
import com.springreport.entity.doctpl.DocTpl;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: DocTplMapper类
* @author 
* @date 2024-05-02 08:55:33
* @version V1.0  
 */
public interface DocTplMapper extends BaseMapper<DocTpl>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<DocTpl> searchDataLike(final DocTpl model);
    
    /**  
     * @MethodName: getTableList
     * @Description: 获取表格数据
     * @author caiyang
     * @param model
     * @return List<DocTplDto>
     * @date 2024-05-03 08:50:35 
     */ 
    List<DocTplDto> getTableList(final DocTpl model);
}
