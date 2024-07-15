package com.springreport.mapper.reporttpl;
import com.springreport.dto.reporttpl.ReportTplDto;
import com.springreport.dto.sysrolereport.MesRoleReportDto;
import com.springreport.entity.reporttpl.ReportTpl;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ReportTplMapper类
* @author 
* @date 2021-02-13 11:16:33
* @version V1.0  
 */
public interface ReportTplMapper extends BaseMapper<ReportTpl>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ReportTpl> searchDataLike(final ReportTpl model);
    
    /**
     * 	查询表格数据
     * @param model
     * @return
     */
    List<ReportTplDto> getTableList(final ReportTpl model);
    
    /**  
     * @MethodName: getRoleReports
     * @Description: 获取角色报表
     * @author caiyang
     * @param model
     * @return 
     * @return List<ReportTpl>
     * @date 2022-07-06 08:34:46 
     */  
    List<ReportTplDto> getRoleReports(MesRoleReportDto model);
}
