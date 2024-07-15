package com.springreport.mapper.onlinetplsheet;
import com.springreport.entity.onlinetplsheet.OnlineTplSheet;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: OnlineTplSheetMapper类
* @author 
* @date 2023-02-06 08:03:30
* @version V1.0  
 */
public interface OnlineTplSheetMapper extends BaseMapper<OnlineTplSheet>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<OnlineTplSheet> searchDataLike(final OnlineTplSheet model);
}
