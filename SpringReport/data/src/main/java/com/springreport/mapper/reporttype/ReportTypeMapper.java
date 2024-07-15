package com.springreport.mapper.reporttype;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springreport.entity.reporttype.ReportType;

 /**  
* @Description: ReportTypeMapper类
* @author 
* @date 2021-02-09 08:59:59
* @version V1.0  
 */
public interface ReportTypeMapper extends BaseMapper<ReportType> {

	/**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ReportType> searchDataLike(final ReportType model);
    
}
