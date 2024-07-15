package com.springreport.mapper.reportrangeauthuser;
import com.springreport.entity.reportrangeauthuser.ReportRangeAuthUser;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ReportRangeAuthUserMapper类
* @author 
* @date 2024-03-03 05:52:17
* @version V1.0  
 */
public interface ReportRangeAuthUserMapper extends BaseMapper<ReportRangeAuthUser>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ReportRangeAuthUser> searchDataLike(final ReportRangeAuthUser model);
}
