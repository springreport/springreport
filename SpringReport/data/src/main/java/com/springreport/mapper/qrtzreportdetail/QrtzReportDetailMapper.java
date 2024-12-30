package com.springreport.mapper.qrtzreportdetail;
import com.springreport.dto.qrtzreportdetail.IndexQrtzDto;
import com.springreport.dto.qrtzreportdetail.ReqIndexQrtzDto;
import com.springreport.entity.qrtzreportdetail.QrtzReportDetail;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: QrtzReportDetailMapper类
* @author 
* @date 2023-07-28 09:43:20
* @version V1.0  
 */
public interface QrtzReportDetailMapper extends BaseMapper<QrtzReportDetail>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<QrtzReportDetail> searchDataLike(final QrtzReportDetail model);
    
    /**  
     * @MethodName: getIndexTaskList
     * @Description: 获取首页任务列表
     * @author caiyang
     * @param model
     * @return List<IndexQrtzDto>
     * @date 2024-12-10 06:08:56 
     */ 
    List<IndexQrtzDto> getIndexTaskList(ReqIndexQrtzDto model);
}
