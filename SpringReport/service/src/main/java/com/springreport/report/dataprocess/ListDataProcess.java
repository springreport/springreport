//package com.springreport.report.dataprocess;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.springreport.dto.reportcell.ReportCellDto;
//import com.springreport.dto.reporttpl.BindData;
//import com.springreport.entity.reportcell.ReportCell;
//import com.springreport.enums.AggregateTypeEnum;
//import com.springreport.enums.CellValueTypeEnum;
//import com.springreport.enums.YesNoEnum;
//import com.springreport.report.aggregate.Aggregate;
//import com.springreport.report.aggregate.GroupAggregate;
//import com.springreport.report.aggregate.ListAggregate;
//import com.springreport.util.ListUtil;
//
///**  
// * @ClassName: ListDataProcess
// * @Description: 列表式报表的动态数据处理
// * @author caiyang
// * @date 2021-05-27 04:33:34 
//*/  
//public class ListDataProcess extends BasicDynamicDataProcess{
//
//	private static Map<String,Aggregate<ReportCell,BindData>> aggregates=new HashMap<String,Aggregate<ReportCell,BindData>>();
//	
//	static{
//		aggregates.put(AggregateTypeEnum.GROUP.getCode(),new GroupAggregate());
//		aggregates.put(AggregateTypeEnum.LIST.getCode(),new ListAggregate());
//		aggregates.put(AggregateTypeEnum.SUMMARY.getCode(),new ListAggregate());
//	}
//	@Override
//	public List<BindData> process(List<ReportCellDto> variableCells, List<Map<String, Object>> data) {
//		List<BindData> bindDatas = new ArrayList<BindData>();
//		if(!ListUtil.isEmpty(data))
//		{
//			BindData bindData = null;
//			String lastAggregateType = "";
//			List<List<Map<String, Object>>> lastData = null;
//			for (int i = 0; i < variableCells.size(); i++) {
//				bindData = new BindData();
//				bindData.setCoordsx(variableCells.get(i).getCoordsx());
//				bindData.setCoordsy(variableCells.get(i).getCoordsy());
//				bindData.setCellExtend(variableCells.get(i).getCellExtend());
//				bindData.setAggregateType(variableCells.get(i).getAggregateType());
//				bindData.setCellValueType(CellValueTypeEnum.VARIABLE.getCode());
//				bindData.setFontColor(variableCells.get(i).getFontColor());
//				bindData.setBackColor(variableCells.get(i).getBackColor());
//				bindData.setIsBold(variableCells.get(i).getIsBold());
//				bindData.setFontSize(variableCells.get(i).getFontSize());
//				bindData.setCellClassname(variableCells.get(i).getCellClassname());
//				bindData.setCellFunction(variableCells.get(i).getCellFunction());
//				bindData.setDigit(variableCells.get(i).getDigit());
//				bindData.setIsLink(variableCells.get(i).getIsLink());
//				if(YesNoEnum.YES.getCode().intValue() == variableCells.get(i).getIsLink().intValue())
//				{
//					bindData.setCellLink(variableCells.get(i).getCellLink());
//				}
//				if(i == 0)
//				{
//					bindData.setLastAggregateType(variableCells.get(i).getAggregateType());
//					List<List<Map<String, Object>>> datas = new ArrayList<List<Map<String,Object>>>();
//					datas.add(data);
//					bindData.setDatas(datas);
//					bindData.setIsFirst(YesNoEnum.YES.getCode());
//				}else {
//					bindData.setAggregateType(variableCells.get(i).getAggregateType());
//					bindData.setLastAggregateType(lastAggregateType);
//					bindData.setDatas(lastData);
//					bindData.setIsFirst(YesNoEnum.NO.getCode());
//				}
//				bindData = aggregates.get(variableCells.get(i).getAggregateType()).aggregate(variableCells.get(i),bindData);
//				bindDatas.add(bindData);
//				lastAggregateType = bindData.getAggregateType();
//				lastData = bindData.getDatas();
//			}
//		}
//		
//		return bindDatas;
//	}
//
//}
