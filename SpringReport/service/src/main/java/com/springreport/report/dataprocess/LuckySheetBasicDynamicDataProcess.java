package com.springreport.report.dataprocess;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.springreport.dto.reporttpl.LuckySheetBindData;
import com.springreport.dto.reporttpl.LuckySheetFormsBindData;
import com.springreport.entity.luckysheetreportcell.LuckysheetReportCell;
import com.springreport.entity.luckysheetreportformscell.LuckysheetReportFormsCell;

/**  
 * @ClassName: ReportDataProcess
 * @Description: 报表数据处理用抽象类
 * @author caiyang
 * @date 2021-05-27 04:14:37 
*/  
public abstract class LuckySheetBasicDynamicDataProcess {

	public abstract List<LuckySheetBindData> process(List<LuckysheetReportCell> variableCells,List<Map<String, Object>> data,String datasetName,
			Map<String, Map<String, List<List<Map<String, Object>>>>> processedCells,Map<String, LuckySheetBindData> blockBindDatas,
			Map<String, Object> subtotalCellDatas,Map<String, Object> subtotalCellMap,String sheetIndex,Map<String, LuckySheetBindData> cellBindData,Map<String, JSONObject> subTotalDigits,int tplType,List<String> subTotalCellCoords);
	
	public abstract List<LuckySheetFormsBindData> processForms(List<LuckysheetReportFormsCell> variableCells,List<Map<String, Object>> data,String datasetName,Map<String, Map<String, List<List<Map<String, Object>>>>> processedCells,String sheetIndex);
}
