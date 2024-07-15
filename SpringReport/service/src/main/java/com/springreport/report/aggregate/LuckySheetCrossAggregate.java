package com.springreport.report.aggregate;



import java.util.Map;

import com.springreport.dto.reporttpl.LuckySheetBindData;
import com.springreport.entity.luckysheetreportcell.LuckysheetReportCell;

/**  
 * @ClassName: LuckySheetCrossAggregate
 * @Description: 交叉数据处理
 * @author caiyang
 * @date 2021-05-27 04:55:07 
*/  
public class LuckySheetCrossAggregate extends Aggregate<LuckysheetReportCell,LuckySheetBindData,Map<String, LuckySheetBindData>>{

	@Override
	public LuckySheetBindData aggregate(LuckysheetReportCell reportCell,LuckySheetBindData bindData,Map<String, LuckySheetBindData> cellBinddata) {
//		String property = reportCell.getCellValue().split("\\.")[1].replace("${", "").replace("}", "");//单元格属性值
		String property = reportCell.getCellValue().replaceAll(reportCell.getDatasetName()+".", "").replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
		bindData.setProperty(property);
		return bindData;
	}

}
