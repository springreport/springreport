package com.springreport.mapper.doctplcodes;
import com.springreport.entity.doctplcodes.DocTplCodes;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: DocTplCodesMapper类
* @author 
* @date 2024-10-23 09:51:07
* @version V1.0  
 */
public interface DocTplCodesMapper extends BaseMapper<DocTplCodes>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<DocTplCodes> searchDataLike(final DocTplCodes model);
}
