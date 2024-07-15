package com.springreport.mapper.reportsheetpdfprintsetting;
import com.springreport.entity.reportsheetpdfprintsetting.ReportSheetPdfPrintSetting;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: ReportSheetPdfPrintSettingMapper类
* @author 
* @date 2023-12-06 03:01:21
* @version V1.0  
 */
public interface ReportSheetPdfPrintSettingMapper extends BaseMapper<ReportSheetPdfPrintSetting>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<ReportSheetPdfPrintSetting> searchDataLike(final ReportSheetPdfPrintSetting model);
}
